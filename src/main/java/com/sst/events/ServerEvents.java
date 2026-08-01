package com.sst.events;

import com.sst.core.FeatureDef;
import com.sst.core.FeatureRegistry;
import com.sst.core.ToggleState;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 服务端周期事件。
 *
 * 金果（EGA）没有独立开关：MineFargo 用 enchanted_golden_apple_soul_stone 这个“冷却”NBT，
 * 当其 > 0 时只衰减不触发。因此“关闭金果”= 把该冷却钉到高位。本类每 tick 维持钉住状态。
 */
public class ServerEvents {

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        // 仅当玩家装备了金果 provider 且开关为关时，维持钉高位
        FeatureDef ega = FeatureRegistry.byId("ega");
        if (ega == null) return;
        boolean hasProvider = false;
        for (String p : ega.providers) {
            if (com.sst.core.CuriosScanner.equippedIds(player).contains(p)) {
                hasProvider = true;
                break;
            }
        }
        if (!hasProvider) return;

        if (!ToggleState.isEnabled(player, ega)) {
            int cur = player.getPersistentData().getInt(ega.mineFargoKey);
            if (cur < ToggleState.EGA_PIN_THRESHOLD) {
                player.getPersistentData().putInt(ega.mineFargoKey, ToggleState.EGA_PIN_VALUE);
            }
        }
    }
}
