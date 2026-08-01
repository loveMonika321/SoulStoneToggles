package com.sst.network;

import com.sst.core.CuriosScanner;
import com.sst.core.FeatureRegistry;
import com.sst.core.ToggleState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * C2S：客户端打开 GUI 时请求当前所有可用功能的状态。
 * 服务端按玩家装备计算可用功能并回发 SyncPacket。
 */
public class RequestStatePacket {

    public RequestStatePacket() {}

    public RequestStatePacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            List<com.sst.core.FeatureDef> avail =
                    FeatureRegistry.availableFor(CuriosScanner.equippedIds(player));
            Map<String, Boolean> snap = ToggleState.snapshot(player, avail);
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new SyncPacket(snap));
        });
        ctx.get().setPacketHandled(true);
    }
}
