package com.sst.client.gui;

import com.sst.client.ClientState;
import com.sst.core.FeatureDef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 魂石功能开关 —— 一级分类页面。
 *
 * 显示当前装备中「至少有 1 项可开关功能」的所有分类（矿石/自然/实体/超然/伊始 + 各联动mod），
 * 点击分类按钮跳转 ToggleScreen 二级页面查看具体功能开关。
 */
public class CategoryScreen extends Screen {

    private static final int PANEL_W = 360;
    private static final int ROW_H = 32;
    private static final int VISIBLE_ROWS = 9;

    private int scrollOffset = 0;
    private int maxScroll = 0;
    private final List<FeatureDef.Category> rows = new ArrayList<>();

    public CategoryScreen() {
        super(Component.literal("魂石功能开关 · 分类"));
    }

    @Override
    protected void init() {
        refresh();
    }

    public void refresh() {
        rows.clear();
        rows.addAll(ClientState.availableCategories());
        maxScroll = Math.max(0, rows.size() - VISIBLE_ROWS);
        if (scrollOffset > maxScroll) scrollOffset = maxScroll;
        if (scrollOffset < 0) scrollOffset = 0;
        rebuildButtons();
    }

    private void rebuildButtons() {
        clearWidgets();
        int panelX = (this.width - PANEL_W) / 2;
        int topY = 44;
        int visibleCount = Math.min(VISIBLE_ROWS, rows.size() - scrollOffset);
        for (int i = 0; i < visibleCount; i++) {
            int idx = i + scrollOffset;
            FeatureDef.Category cat = rows.get(idx);
            int y = topY + i * ROW_H;
            int btnW = PANEL_W - 24;
            int btnX = panelX + 12;
            int btnY = y + (ROW_H - 20) / 2;
            String title = cat.title.getString();
            String sub = cat.subtitle.getString();
            int count = ClientState.countInCategory(cat);
            String label = String.format("  %s  §7%s  §8[%d项]", title, sub.isEmpty() ? "" : sub, count);
            addRenderableWidget(Button.builder(Component.literal("进入 →"),
                            b -> Minecraft.getInstance().setScreen(new ToggleScreen(cat)))
                    .bounds(btnX + btnW - 72, btnY, 64, 20)
                    .build());
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

        graphics.drawString(font, this.title, panelX, 18, 0xFFFFFFFF);
        graphics.drawString(font, Component.literal("§7按 K 打开/ESC 关闭 · 共 " + rows.size() + " 个分类"),
                panelX, 30, 0xFFCCCCCC);

        for (int i = 0; i < visibleCount; i++) {
            int idx = i + scrollOffset;
            FeatureDef.Category cat = rows.get(idx);
            int y = topY + i * ROW_H;
            int count = ClientState.countInCategory(cat);

            int rowBg = (i % 2 == 0) ? 0x30FFFFFF : 0x18FFFFFF;
            graphics.fill(panelX + 12, y, panelX + PANEL_W - 12, y + ROW_H - 2, rowBg);

            String title = cat.title.getString();
            String sub = cat.subtitle.getString();
            graphics.drawString(font, "§e" + title, panelX + 20, y + 4, 0xFFFFFFFF);
            graphics.drawString(font, Component.literal("§7" + sub + "  §8[ " + count + " 项 ]"),
                    panelX + 20, y + 18, 0xFFCCCCCC);
        }

        if (rows.isEmpty()) {
            graphics.drawString(font, Component.literal("§e当前没有装备可开关的魂石"),
                    panelX + 40, topY + 24, 0xFFFFFFFF);
        }

        if (rows.size() > VISIBLE_ROWS) {
            int barX = panelX + PANEL_W + 2;
            int barH = panelH;
            int knobH = Math.max(14, barH * VISIBLE_ROWS / rows.size());
            int knobY = topY + (barH - knobH) * scrollOffset / Math.max(1, maxScroll);
            graphics.fill(barX, topY, barX + 3, topY + barH, 0x40FFFFFF);
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

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
