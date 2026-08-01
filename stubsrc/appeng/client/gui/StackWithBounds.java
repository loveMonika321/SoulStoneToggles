package appeng.client.gui;

import appeng.api.stacks.GenericStack;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.Nullable;

/** 桩：record 两个字段，没有 requireNonNull（已确认源码）。 */
public record StackWithBounds(GenericStack stack, Rect2i bounds) {
    @Nullable
    public static StackWithBounds fromSlot(appeng.client.gui.AEBaseScreen<?> screen,
                                            net.minecraft.world.inventory.Slot slot) { return null; }
}
