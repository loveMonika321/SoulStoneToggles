package appeng.api.client;

import appeng.api.stacks.AEKey;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public interface AEKeyRenderHandler<T extends AEKey> {
    default void drawInGui(Minecraft mc, GuiGraphics gfx, int x, int y, T what) { }
    default void drawOnBlockFace(PoseStack ps, MultiBufferSource mbs, T what, float scale, int light, Level level) { }
    default Component getDisplayName(T stack) { return Component.literal("x"); }
    default List<Component> getTooltip(T stack) { return java.util.Collections.emptyList(); }
}
