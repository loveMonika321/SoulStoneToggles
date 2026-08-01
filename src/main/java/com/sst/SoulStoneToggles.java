package com.sst;

import com.sst.network.NetworkHandler;
import com.sst.events.ServerEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SoulStoneToggles.MODID)
public class SoulStoneToggles {
    public static final String MODID = "soulstonetoggles";
    public static final String NBT_PREFIX = MODID + ":";

    public SoulStoneToggles() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(ServerEvents.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkHandler::register);
    }
}
