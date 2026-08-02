package com.sst.client.gui;

import com.sst.client.ClientState;
import com.sst.core.FeatureDef;
import com.sst.core.FeatureRegistry;
import com.sst.network.NetworkHandler;
import com.sst.network.TogglePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

/**
 * 三级页面：某聚合分组下的具体功能开关。
 * 面包屑：分类 → 分组 → 本页。
 */
public class FeatureScreen extends Screen {

    private static final int PANEL_W = 380;
    private static final int ROW_H = 28;
    private static final int VISIBLE_ROWS = 9;

    private final FeatureDef.Category category;
    private final FeatureRegistry.FeatureGroup group;
    private int scrollOffset = 0;
    private int maxScroll = 0;
    private final List<FeatureDef> rows = new ArrayList<>();

    public FeatureScreen(FeatureDef.Category category, FeatureRegistry.FeatureGroup group) {
        super(Component.literal(category.title.getString() + " → "
                + (group.aggregate == null ? "直接功能" : group.aggregate.name.getString())
                + " · 魂石功能开关"));
        this.category = category;
        this.group = group;
    }

    @Override
    protected void init() {
        ClientState.refreshAvailable();
        refresh();
    }

    public void refresh() {
        rows.clear();
        rows.addAll(group.features);
        maxScroll = Math.max(0, rows.size() - VISIBLE_ROWS);
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        if (scrollOffset < 0) scrollOffset = 0;
        rebuildButtons();
    }

    private void rebuildButtons() {
        clearWidgets();
        int panelX = (this.width - PANEL_W) / 2;

        // 面包屑返回
        addRenderableWidget(Button.builder(Component.literal("← 分组列表"),
                        b -> Minecraft.getInstance().setScreen(new AggregateScreen(category)))
                .bounds(panelX + 12, 16, 96, 18)
                .build());

        // 聚合级 开/关 快捷按钮（仅聚合容器存在时显示）
        if (group.aggregate != null) {
            boolean aggOn = ClientState.isEnabled(group.aggregate.id);
            addRenderableWidget(Button.builder(Component.literal(aggOn ? "§c§l聚合：关" : "§a§l聚合：开"),
                            b -> sendToggle(group.aggregate, !aggOn))
                    .bounds(panelX + PANEL_W - 96, 16, 84, 18)
                    .build());
        }

        int topY = 44;
        int visibleCount = Math.min(VISIBLE_ROWS, rows.size() - scrollOffset);
        for (int i = 0; i < visibleCount; i++) {
            int idx = i + scrollOffset;
            FeatureDef f = rows.get(idx);
            int y = topY + i * ROW_H;
            boolean on = ClientState.isEnabled(f.id);
            String label = on ? "§a§l 开 " : "§c§l 关 ";
            int btnW = 60;
            int btnX = panelX + PANEL_W - btnW - 12;
            int btnY = y + (ROW_H - 18) / 2;
            addRenderableWidget(Button.builder(Component.literal(label),
                            b -> sendToggle(f, !on))
                    .bounds(btnX, btnY, btnW, 18)
                    .build());
        }
    }

    public static void sendToggle(FeatureDef f, boolean enabled) {
        NetworkHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(),
                new TogglePacket(f.id, enabled));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int panelX = (this.width - PANEL_W) / 2;
        int topY = 44;
        int visibleCount = Math.min(VISIBLE_ROWS, rows.size() - scrollOffset);
        int panelH = Math.max(VISIBLE_ROWS, Math.min(VISIBLE_ROWS, rows.size())) * ROW_H + 16;

        graphics.fill(panelX - 6, 24 - 6, panelX + PANEL_W + 6, topY + panelH, 0x90000000);
        graphics.fill(panelX - 6, 24 - 6, panelX + PANEL_W + 6, 26, 0xFF2B2B2B);

        graphics.drawString(font, this.title, panelX + 110, 18, 0xFFFFFFFF);
        graphics.drawString(font, Component.literal("§7按 K 打开/ESC 关闭 · 共 " + rows.size() + " 项"),
                panelX, 30, 0xFFCCCCCC);

        for (int i = 0; i < visibleCount; i++) {
            int idx = i + scrollOffset;
            FeatureDef f = rows.get(idx);
            int y = topY + i * ROW_H;
            boolean on = ClientState.isEnabled(f.id);

            int rowBg = (i % 2 == 0) ? 0x30FFFFFF : 0x18FFFFFF;
            graphics.fill(panelX, y, panelX + PANEL_W, y + ROW_H - 2, rowBg);

            graphics.drawString(font, f.name, panelX + 8, y + 5, on ? 0xFFFFFFFF : 0xFFAAAAAA);
            graphics.drawString(font, f.desc, panelX + 8, y + 16, 0xFF888888);

            int dotX = panelX + PANEL_W - 84;
            int dotY = y + (ROW_H - 6) / 2;
            graphics.fill(dotX, dotY, dotX + 6, dotY + 6, on ? 0xFF39D353 : 0xFFE5534F);
        }

        if (rows.isEmpty()) {
            graphics.drawString(font, Component.literal("§e当前分组没有可用功能"),
                    panelX + 40, topY + 24, 0xFFFFFFFF);
        }

        if (rows.size() > VISIBLE_ROWS) {
            int barX = panelX + PANEL_W + 2;
            int knobH = Math.max(14, panelH * VISIBLE_ROWS / rows.size());
            int knobY = topY + (panelH - knobH) * scrollOffset / Math.max(1, maxScroll);
            graphics.fill(barX, topY, barX + 3, topY + panelH, 0x40FFFFFF);
            graphics.fill(barX, knobY, barX + 3, knobY + knobH, 0xFFCCCCCC);
        }

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta > 0) scrollOffset = Math.max(0, scrollOffset - 1);
        else scrollOffset = Math.min(maxScroll, scrollOffset + 1);
        rebuildButtons();
        return true;
    }

    /** 打开 GUI 时暂停游戏。 */
    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
