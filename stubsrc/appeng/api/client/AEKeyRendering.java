package appeng.api.client;

import appeng.api.stacks.AEKey;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class AEKeyRendering {
    public static void drawInGui(Minecraft mc, GuiGraphics gfx, int x, int y, AEKey what) { }
    public static void drawOnBlockFace(com.mojang.blaze3d.vertex.PoseStack ps,
                                        net.minecraft.client.renderer.MultiBufferSource mbs, AEKey what,
                                        float scale, int light, net.minecraft.world.level.Level level) { }
    public static Component getDisplayName(AEKey s) { return Component.literal("x"); }
    public static List<Component> getTooltip(AEKey s) { return new ArrayList<>(); }
    private static <T extends AEKey> AEKeyRenderHandler<T> getOrThrow(Object type) { return null; }
}
