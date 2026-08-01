package com.sst.events;

import com.sst.SoulStoneToggles;
import com.sst.core.CuriosScanner;
import com.sst.core.FeatureDef;
import com.sst.core.FeatureRegistry;
import com.sst.core.ToggleState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

/**
 * 服务端周期事件。
 *
 * 负责三件事：
 *   1) 金果（EGA）没有独立开关：MineFargo 用 enchanted_golden_apple_soul_stone 这个"冷却"
 *      NBT，当其 > 0 时只衰减不触发。因此"关闭金果"= 把该冷却钉到高位。每 tick 维持钉住状态。
 *   2) 首次穿戴魂石：对应功能开关的 NBT 必须初始化为"开"。否则旧版本遗留的
 *      soulstonetoggles:<id>=1（关）会在玩家升级 mod 后仍然保留，出现"默认关"。
 *      按功能粒度写 sst_worn_<featureId> 标志，保证只初始化一次。
 *   3) 死亡（PlayerEvent.Clone）时把 SoulStoneToggles 相关 NBT 从 oldPlayer 迁移
 *      到 newPlayer，避免复活后配置被重置，要玩家重新开关。
 */
@Mod.EventBusSubscriber(modid = SoulStoneToggles.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ServerEvents {

    /** "某功能已经首次穿戴处理过"的持久化标志前缀（存在即已初始化）。 */
    private static final String WORN_FLAG_PREFIX = "sst_worn_";
    /** SST 功能 NBT 前缀（= SoulStoneToggles.NBT_PREFIX，但这里静态拼好方便判断）。 */
    private static final String SST_NBT_PREFIX = SoulStoneToggles.MODID + ":";

    private ServerEvents() {}

    // ============================================================
    // Tick：首次穿戴初始化 + EGA 钉高位
    // ============================================================

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        Set<String> equippedIds = CuriosScanner.equippedIds(player);
        CompoundTag pd = player.getPersistentData();

        // ---- 遍历所有"当前装备能提供"的功能：首次穿戴就初始化 NBT ----
        for (FeatureDef f : FeatureRegistry.availableFor(equippedIds)) {
            String wornKey = WORN_FLAG_PREFIX + f.id;
            if (!pd.getBoolean(wornKey)) {
                pd.putBoolean(wornKey, true);
                // 仅当"还没有用户自定义值"时才写默认开。
                //   * SST：pd 中没有 soulstonetoggles:<id>（或存在 0=开）→ 不写；存在 1=关 → 清零恢复开
                //   * MF_OPEN：pd 中没有 mineFargoKey → 写 25（开）；已有则不碰（保留用户/矿法设置）
                //   * MF_PIN：pd 中没有 mineFargoKey → 写 0（开，交还 MineFargo 自然管理）
                switch (f.type) {
                    case SST: {
                        String k = SoulStoneToggles.NBT_PREFIX + f.id;
                        if (pd.getInt(k) == 1) {
                            // 遗留旧值（上一版默认关）清掉，让 SSTFeatureGate.isEnabled 返回 true
                            pd.remove(k);
                        }
                        break;
                    }
                    case MF_OPEN: {
                        if (!pd.contains(f.mineFargoKey)) {
                            pd.putInt(f.mineFargoKey, 25); // MineFargo <=50 视为开
                        }
                        break;
                    }
                    case MF_PIN: {
                        if (!pd.contains(f.mineFargoKey)) {
                            pd.putInt(f.mineFargoKey, 0); // 0 / 正常冷却范围 = 开
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }

        // ---- 金果（MF_PIN 钉高位维持关闭） ----
        FeatureDef ega = FeatureRegistry.byId("ega");
        if (ega != null) {
            boolean hasProvider = false;
            for (String p : ega.providers) {
                if (equippedIds.contains(p)) { hasProvider = true; break; }
            }
            if (hasProvider && !ToggleState.isEnabled(player, ega)) {
                int cur = pd.getInt(ega.mineFargoKey);
                if (cur < ToggleState.EGA_PIN_THRESHOLD) {
                    pd.putInt(ega.mineFargoKey, ToggleState.EGA_PIN_VALUE);
                }
            }
        }
    }

    // ============================================================
    // 死亡复活：从旧玩家 NBT 迁移到新玩家 NBT
    // ============================================================

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // 仅死亡克隆（非末地返回传送克隆）需要迁移（不过两者都迁也不损失）。
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        if (oldPlayer == newPlayer) return;

        CompoundTag oldPd = oldPlayer.getPersistentData();
        CompoundTag newPd = newPlayer.getPersistentData();

        for (String key : oldPd.getAllKeys()) {
            if (key.startsWith(SST_NBT_PREFIX)        // soulstonetoggles:<featureId>
                    || key.startsWith(WORN_FLAG_PREFIX) // sst_worn_<featureId>
                    // MineFargo 侧被我们代理管理的 OPEN / PIN key：保留玩家设置
                    || key.equals(FeatureRegistry.MF_MAGNET_OPEN)
                    || key.equals(FeatureRegistry.MF_HAZARD_OPEN)
                    || key.equals(FeatureRegistry.MF_FIRE_OPEN)
                    || key.equals(FeatureRegistry.MF_TRACK_OPEN)
                    || key.equals(FeatureRegistry.MF_EGA_CD)) {
                // CompoundTag#put 支持任意 Tag 类型（Int/Byte/String 等都安全）
                newPd.put(key, oldPd.get(key));
            }
        }
    }
}
