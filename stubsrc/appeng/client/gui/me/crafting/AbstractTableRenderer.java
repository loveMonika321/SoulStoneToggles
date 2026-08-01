package appeng.client.gui.me.crafting;

import appeng.api.stacks.AEKey;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.StackWithBounds;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;

/** 编译桩：Mixin 目标 AbstractTableRenderer。 */
public abstract class AbstractTableRenderer<T> {
    protected static final int COLS = 1;
    protected static final int CELL_WIDTH = 32;
    protected static final int CELL_BORDER = 2;
    protected static final int CELL_HEIGHT = 32;
    protected static final float TEXT_SCALE = 1.0f;
    protected static final int LINE_SPACING = 2;
    protected static final float INV_TEXT_SCALE = 1.0f;

    protected final AEBaseScreen<?> screen;
    protected final int x;
    protected final int y;
    protected final int rows;
    protected final net.minecraft.client.gui.Font fontRenderer;
    protected final float lineHeight;

    public AbstractTableRenderer(AEBaseScreen<?> screen, int x, int y, int rows) {
        this.screen = screen; this.x = x; this.y = y; this.rows = rows;
        this.fontRenderer = net.minecraft.client.Minecraft.getInstance().font;
        this.lineHeight = 10;
    }

    public final void render(GuiGraphics guiGraphics, int mouseX, int mouseY, List<T> entries, int scrollOffset) { }

    protected abstract List<net.minecraft.network.chat.Component> getEntryDescription(T entry);
    protected abstract AEKey getEntryStack(T entry);
    protected abstract List<net.minecraft.network.chat.Component> getEntryTooltip(T entry);
    protected int getEntryBackgroundColor(T entry) { return 0; }
    protected int getEntryOverlayColor(T entry) { return 0; }

    public StackWithBounds getHoveredStack() { return null; }
    public int getScrollableRows(int size) { return 0; }
    protected static int getScrollableRows(int size, int rows) { return 0; }
}
