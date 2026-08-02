package com.sst.client.gui;

import com.sst.client.ClientState;
import com.sst.core.FeatureDef;
import com.sst.core.FeatureRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级页面：分类下的聚合分组列表。
 * 每个分组一行：聚合容器 + 子项计数，或"直接功能（无聚合）"组。
 * 点击分组 → FeatureScreen（三级）。
 */
public class AggregateScreen extends Screen {

    private static final int PANEL_W = 380;
    private static final int ROW_H = 40;
    private static final int VISIBLE_ROWS = 8;

    private final FeatureDef.Category category;
    private int scrollOffset = 0;
    private int maxScroll = 0;
    private final List<FeatureRegistry.FeatureGroup> rows = new ArrayList<>();

    public AggregateScreen(FeatureDef.Category category) {
        super(Component.literal(category.title.getString() + " · 魂石分组"));
        this.category = category;
    }

    @Override
    protected void init() {
        ClientState.refreshAvailable();
        refresh();
    }

    public void refresh() {
        rows.clear();
        rows.addAll(ClientState.groupsInCategory(category));
        maxScroll = Math.max(0, rows.size() - VISIBLE_ROWS);
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        if (scrollOffset < 0) scrollOffset = 0;
        rebuildButtons();
    }

    private void rebuildButtons() {
        clearWidgets();
        int panelX = (this.width - PANEL_W) / 2;

        // 面包屑返回
        addRenderableWidget(Button.builder(Component.literal("← 分类列表"),
                        b -> Minecraft.getInstance().setScreen(new CategoryScreen()))
                .bounds(panelX + 12, 16, 96, 18)
                .build());

        int topY = 44;
        int visibleCount = Math.min(VISIBLE_ROWS, rows.size() - scrollOffset);
        for (int i = 0; i < visibleCount; i++) {
            int idx = i + scrollOffset;
            FeatureRegistry.FeatureGroup group = rows.get(idx);
            int y = topY + i * ROW_H;
            int btnW = 84;
            int btnX = panelX + PANEL_W - btnW - 12;
            int btnY = y + (ROW_H - 20) / 2;
            addRenderableWidget(Button.builder(Component.literal("§e§l 进入 →"),
                            b -> Minecraft.getInstance().setScreen(new FeatureScreen(category, group)))
                    .bounds(btnX, btnY, btnW, 20)
                    .build());

            // 聚合级开/关 按钮（仅聚合容器存在时显示；toggle 对应 aggregate.id）
            if (group.aggregate != null) {
                boolean on = ClientState.isEnabled(group.aggregate.id);
                String label = on ? "§a§l 开 " : "§c§l 关 ";
                addRenderableWidget(Button.builder(Component.literal(label),
                                b -> FeatureScreen.sendToggle(group.aggregate, !on))
                        .bounds(btnX - 66, btnY, 60, 20)
                        .build());
            }
        }
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
        graphics.drawString(font, Component.literal("§7按 K 打开/ESC 关闭 · 共 " + rows.size() + " 个分组"),
                panelX, 30, 0xFFCCCCCC);

        for (int i = 0; i < visibleCount; i++) {
            int idx = i + scrollOffset;
            FeatureRegistry.FeatureGroup group = rows.get(idx);
            int y = topY + i * ROW_H;

            int rowBg = (i % 2 == 0) ? 0x30FFFFFF : 0x18FFFFFF;
            graphics.fill(panelX + 12, y, panelX + PANEL_W - 12, y + ROW_H - 2, rowBg);

            // 聚合名 or "直接功能"
            if (group.aggregate != null) {
                boolean aggOn = ClientState.isEnabled(group.aggregate.id);
                graphics.drawString(font, "§e§l" + group.aggregate.name.getString()
                                + (aggOn ? " §a[聚合·开]" : " §c[聚合·关]"),
                        panelX + 20, y + 4, 0xFFFFFFFF);
                graphics.drawString(font, Component.literal("§7" + group.aggregate.desc.getString()
                                + "  §8[ " + group.features.size() + " 个子项 ]"),
                        panelX + 20, y + 18, 0xFFCCCCCC);
            } else {
                graphics.drawString(font, "§e§l直接功能", panelX + 20, y + 4, 0xFFFFFFFF);
                graphics.drawString(font, Component.literal("§7不属于任何聚合的功能  §8[ " + group.features.size() + " 项 ]"),
                        panelX + 20, y + 18, 0xFFCCCCCC);
            }
        }

        if (rows.isEmpty()) {
            graphics.drawString(font, Component.literal("§e当前分类没有可用的分组"),
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
