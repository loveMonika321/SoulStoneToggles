package com.sst.client;

import com.sst.SoulStoneToggles;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

/**
 * 打开 GUI 的按键（默认 K）。1.20.1 通过 RegisterKeyMappingsEvent 注册，
 * 见 ClientEvents。
 */
public final class SSTKeyBindings {
    public static final KeyMapping OPEN_GUI = new KeyMapping(
            "key." + SoulStoneToggles.MODID + ".open_gui",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories." + SoulStoneToggles.MODID
    );

    private SSTKeyBindings() {}
}
