package com.sst.network;

import com.sst.SoulStoneToggles;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(SoulStoneToggles.MODID, "main"))
            .networkProtocolVersion(() -> PROTOCOL)
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .simpleChannel();

    private static int id = 0;

    public static void register() {
        CHANNEL.messageBuilder(TogglePacket.class, id++)
                .encoder(TogglePacket::encode)
                .decoder(TogglePacket::new)
                .consumerMainThread(TogglePacket::handle)
                .add();

        CHANNEL.messageBuilder(RequestStatePacket.class, id++)
                .encoder(RequestStatePacket::encode)
                .decoder(RequestStatePacket::new)
                .consumerMainThread(RequestStatePacket::handle)
                .add();

        CHANNEL.messageBuilder(SyncPacket.class, id++)
                .encoder(SyncPacket::encode)
                .decoder(SyncPacket::new)
                .consumerMainThread(SyncPacket::handle)
                .add();
    }
}
