package com.sst.network;

import com.sst.client.ClientState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * S2C：服务端把功能状态同步给客户端，供 GUI 显示。
 * 内容：featureId -> enabled。
 */
public class SyncPacket {
    private final Map<String, Boolean> states;

    public SyncPacket(Map<String, Boolean> states) {
        this.states = states;
    }

    public SyncPacket(FriendlyByteBuf buf) {
        int n = buf.readVarInt();
        Map<String, Boolean> m = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String id = buf.readUtf(64);
            boolean on = buf.readBoolean();
            m.put(id, on);
        }
        this.states = m;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(states.size());
        states.forEach((id, on) -> {
            buf.writeUtf(id, 64);
            buf.writeBoolean(on);
        });
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientState.applySync(states));
        ctx.get().setPacketHandled(true);
    }
}
