package com.sst.network;

import com.sst.core.CuriosScanner;
import com.sst.core.FeatureDef;
import com.sst.core.FeatureRegistry;
import com.sst.core.ToggleState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * C2S：客户端请求把某项功能切到指定状态。
 * 服务端会校验玩家确实装备了该功能的 provider，再写入 NBT，并回发 SyncPacket。
 */
public class TogglePacket {
    private final String featureId;
    private final boolean enabled;

    public TogglePacket(String featureId, boolean enabled) {
        this.featureId = featureId;
        this.enabled = enabled;
    }

    public TogglePacket(FriendlyByteBuf buf) {
        this.featureId = buf.readUtf(64);
        this.enabled = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(featureId, 64);
        buf.writeBoolean(enabled);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            FeatureDef f = FeatureRegistry.byId(featureId);
            if (f == null) return;

            // 校验：玩家必须装备了该功能的某个 provider
            Set<String> equipped = CuriosScanner.equippedIds(player);
            boolean allowed = false;
            for (String p : f.providers) {
                if (equipped.contains(p)) { allowed = true; break; }
            }
            if (!allowed) return;

            ToggleState.setEnabled(player, f, enabled);

            // 回发最新状态用于刷新界面
            List<FeatureDef> avail = FeatureRegistry.availableFor(equipped);
            Map<String, Boolean> snap = ToggleState.snapshot(player, avail);
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new SyncPacket(snap));
        });
        ctx.get().setPacketHandled(true);
    }
}
