package com.sst.core;

import com.sst.SoulStoneToggles;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 被 Mixin 调用的静态门禁工具。
 *
 * 拦截点 1 — MyGoUtil.hasXXX(Set, LivingEntity, Item)：
 *   MineFargo 的事件处理器（HurtEvent / TickEvent / DeathAndCloneEvent 等）用各
 *   hasXXX 方法判定"是否拥有某项联动魂石子功能"。当子功能被关闭时直接返回 false。
 *
 * 拦截点 2 — MyGoUtil.isCurioEquipped(LivingEntity, Item)：
 *   农夫乐事魂石在 UseItemEvent 中直接用此方法判定，需单独拦截。
 *
 * 拦截点 3 — AccessoryEventHandler 的 IAccessoryAbility 派发：
 *   聚合魂石（如灾变之魂）的 inoLivingHurt / onTick 等方法直接施放所有子效果，
 *   需 @Redirect 在调用前检查该魂石的整体开关。
 *
 * NBT 约定：soulstonetoggles:&lt;featureId&gt;
 *   缺省或非 0 = 开启（不拦截）
 *   == 0       = 关闭（拦截，返回 false / 跳过能力调用）
 */
public final class SSTFeatureGate {
    /** MineFargo 注册名 → 本 mod 功能 id */
    private static final Map<String, String> ITEM_TO_FEATURE = new HashMap<>();
    /** 单体魂石注册名 → 所属聚合魂石功能 id（用于 hasXXX 拦截时检查聚合整体开关） */
    private static final Map<String, String> ITEM_TO_AGGREGATE = new HashMap<>();
    /** 直接使用 isCurioEquipped 的魂石注册名集合（目前仅农夫乐事） */
    private static final Set<String> DIRECT_CURIO_CHECK_ITEMS = Set.of(
            FeatureRegistry.MF + ":farmers_delight_soul_stone"
    );

    static {
        // ===== 现有超然之魂 / 实体之魂的 SST 类型功能 =====
        ITEM_TO_FEATURE.put(FeatureRegistry.UNDYING, "undying");
        ITEM_TO_FEATURE.put(FeatureRegistry.THE_SEA, "thesea");
        ITEM_TO_FEATURE.put(FeatureRegistry.MENDING, "mending");

        // ===== 联动魂石（单体 + 聚合）由 FeatureRegistry.addSoul 自动注册 =====
        // FeatureRegistry 的 static 块会调用 registerItem 来填充 ITEM_TO_FEATURE。
    }

    private SSTFeatureGate() {}

    // ===== 注册接口（由 FeatureRegistry 调用） =====

    /** 注册一个魂石注册名 → 功能 id 映射。 */
    static void registerItem(String registryName, String featureId) {
        ITEM_TO_FEATURE.put(registryName, featureId);
    }

    /** 注册单体魂石 → 聚合魂石功能 id 映射。 */
    static void registerAggregate(String itemRegistryName, String aggregateFeatureId) {
        ITEM_TO_AGGREGATE.put(itemRegistryName, aggregateFeatureId);
    }

    // ===== 查询接口（由 Mixin 调用） =====

    /**
     * 返回该 Item 对应的功能 id。
     * 同时把功能 id 注册到 ITEM_TO_FEATURE（延迟注册，避免 FeatureRegistry 循环依赖）。
     */
    public static String mapItem(Item item) {
        if (item == null) return null;
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        if (rl == null) return null;
        String key = rl.toString();
        String featureId = ITEM_TO_FEATURE.get(key);
        if (featureId == null) {
            // 延迟注册：联动魂石的功能 id == 注册名 path
            String path = rl.getPath();
            // 检查是否是已注册的 feature（避免把无关物品也加进来）
            if (FeatureRegistry.byId(path) != null) {
                featureId = path;
                ITEM_TO_FEATURE.put(key, featureId);
            }
        }
        return featureId;
    }

    /** 返回该 Item 所属聚合魂石的功能 id（无聚合则 null）。 */
    public static String mapAggregateFeature(Item item) {
        if (item == null) return null;
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        if (rl == null) return null;
        return ITEM_TO_AGGREGATE.get(rl.toString());
    }

    /** 该 Item 是否使用 isCurioEquipped 直接判定（仅农夫乐事等少数魂石）。 */
    public static boolean usesDirectCurioCheck(Item item) {
        if (item == null) return false;
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
        if (rl == null) return false;
        return DIRECT_CURIO_CHECK_ITEMS.contains(rl.toString());
    }

    /** 该功能是否处于"开启"状态（缺省视为开启）。 */
    public static boolean isEnabled(LivingEntity living, String featureId) {
        if (living == null || featureId == null) return true;
        return living.getPersistentData().getInt(key(featureId)) != 0;
    }

    public static void setEnabled(LivingEntity living, String featureId, boolean enabled) {
        if (living == null || featureId == null) return;
        living.getPersistentData().putInt(key(featureId), enabled ? 1 : 0);
    }

    /**
     * hasXXX 拦截统一检查：单体功能关闭 OR 所属聚合整体关闭 → 返回 false。
     * @return true=允许通过（不拦截），false=应拦截（功能关闭）
     */
    public static boolean isFeatureEnabled(LivingEntity living, Item item) {
        String featureId = mapItem(item);
        if (featureId != null && !isEnabled(living, featureId)) {
            return false;
        }
        // 检查所属聚合的整体开关
        String aggId = mapAggregateFeature(item);
        if (aggId != null && !isEnabled(living, aggId)) {
            return false;
        }
        return true;
    }

    /**
     * AccessoryEventHandler @Redirect 用：检查装备物品的能力是否启用。
     * 只需检查物品自身的功能开关（聚合的 @Redirect 检查聚合自身，单体的检查单体自身）。
     * @return true=允许调用能力方法，false=跳过
     */
    public static boolean isAbilityEnabled(LivingEntity living, Item item) {
        String featureId = mapItem(item);
        if (featureId == null) return true;
        return isEnabled(living, featureId);
    }

    public static String key(String featureId) {
        return SoulStoneToggles.NBT_PREFIX + featureId;
    }
}
