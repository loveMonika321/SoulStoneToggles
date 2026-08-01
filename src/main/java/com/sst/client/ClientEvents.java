package com.sst.client;

import com.sst.SoulStoneToggles;
import com.sst.client.gui.CategoryScreen;
import com.sst.network.NetworkHandler;
import com.sst.network.RequestStatePacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SoulStoneToggles.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        // Forge 1.20.1：通过 RegisterKeyMappingsEvent 注册按键
        event.register(SSTKeyBindings.OPEN_GUI);
    }

    /** 输入事件走 FORGE 总线，单独注册。 */
    @Mod.EventBusSubscriber(modid = SoulStoneToggles.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeBus {
        @SubscribeEvent
        public static void onKey(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            if (SSTKeyBindings.OPEN_GUI.consumeClick()) {
                if (mc.screen != null) return;
                // 打开 GUI 前先请求最新状态
                ClientState.refreshAvailable();
                NetworkHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new RequestStatePacket());
                mc.setScreen(new CategoryScreen());
            }
        }
    }
}
