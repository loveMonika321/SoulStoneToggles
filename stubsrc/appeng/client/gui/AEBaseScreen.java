package appeng.client.gui;

/** 最小桩：让 StackWithBounds.fromSlot 能编译通过。 */
public abstract class AEBaseScreen<T extends net.minecraft.world.inventory.AbstractContainerMenu>
        extends net.minecraft.client.gui.screens.inventory.AbstractContainerScreen<T> {
    public AEBaseScreen(T menu, net.minecraft.world.entity.player.Inventory pi, net.minecraft.network.chat.Component c) {
        super(menu, pi, c);
    }
    public int getGuiLeft() { return this.leftPos; }
    public int getGuiTop() { return this.topPos; }
    public void drawTooltipWithHeader(net.minecraft.client.gui.GuiGraphics g, int x, int y,
                                       java.util.List<net.minecraft.network.chat.Component> lines) { }
    public Style getStyle() { return null; }
    public static class Style {
        public PaletteColor getColor(Object k) { return null; }
    }
    public static class PaletteColor {
        public int toARGB() { return 0xFFFFFFFF; }
        public static final Object DEFAULT_TEXT_COLOR = null;
    }
}
