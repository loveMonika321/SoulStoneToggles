package com.sst.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务端读写各功能开关的统一入口。按 FeatureDef.type 分发：
 *
 *  - MF_OPEN : 读 MineFargo 的 *_open（<=50 开, >50 关）；写 25/75。
 *  - MF_PIN  : 读时根据是否被钉高位判断；写时钉高位(关)/归零(开)。
 *  - SST     : 读 soulstonetoggles:&lt;id&gt;（==1 关, 缺省/0 开）；写 0/1。默认全开。
 *
 * 注意：MF_PIN(金果) 的“关”靠 ServerEvents 每 tick 把冷却钉到高位实现。
 */
public final class ToggleState {

    /** 金果被钉住时使用的“高位”冷却值（约 63 年才会自然衰减到 0）。 */
    public static final int EGA_PIN_VALUE = 2_000_000_000;
    /** 判定金果是否已被钉住的阈值。 */
    public static final int EGA_PIN_THRESHOLD = 1_000_000;

    private ToggleState() {}

    public static boolean isEnabled(LivingEntity living, FeatureDef f) {
        CompoundTag pd = living.getPersistentData();
        switch (f.type) {
            case MF_OPEN:
                // MineFargo 约定：<=50 开, >50 关
                return pd.getInt(f.mineFargoKey) <= 50;
            case MF_PIN:
                // 被钉高位 = 关；正常冷却/0 = 开
                return pd.getInt(f.mineFargoKey) < EGA_PIN_THRESHOLD;
            case SST:
                return SSTFeatureGate.isEnabled(living, f.id);
            default:
                return true;
        }
    }

    public static void setEnabled(LivingEntity living, FeatureDef f, boolean enabled) {
        CompoundTag pd = living.getPersistentData();
        switch (f.type) {
            case MF_OPEN:
                pd.putInt(f.mineFargoKey, enabled ? 25 : 75);
                break;
            case MF_PIN:
                // 关 = 钉高位（ServerEvents 会持续维持）；开 = 归零，交还 MineFargo 自然管理
                pd.putInt(f.mineFargoKey, enabled ? 0 : EGA_PIN_VALUE);
                break;
            case SST:
                SSTFeatureGate.setEnabled(living, f.id, enabled);
                break;
            default:
                break;
        }
    }

    /** 把所有功能当前状态打包成 id->enabled，用于同步给客户端显示。 */
    public static Map<String, Boolean> snapshot(LivingEntity living, Iterable<FeatureDef> features) {
        Map<String, Boolean> out = new HashMap<>();
        for (FeatureDef f : features) {
            out.put(f.id, isEnabled(living, f));
        }
        return out;
    }
}
