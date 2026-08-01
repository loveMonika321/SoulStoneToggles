package com.sst.mixin;

import com.sst.core.SSTFeatureGate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

/**
 * 拦截 MineFargo 的 MyGoUtil 工具类。
 *
 * 拦截 1 — 所有 hasXXX(Set, LivingEntity, Item) 三参重载：
 *   MineFargo 的事件处理器（HurtEvent / TickEvent / DeathAndCloneEvent / HealEvent /
 *   FluidCollisionEvent / DropsEvent / TeleportEvent / ShieldEvent / 各 mod 子事件等）
 *   统一用 hasXXX 判定"是否拥有某项联动魂石子功能"。
 *   当子功能或其所属聚合被关闭时，这里直接返回 false。
 *
 * 拦截 2 — isCurioEquipped(LivingEntity, Item)：
 *   农夫乐事魂石在 UseItemEvent 中直接调用此方法判定效果是否触发。
 *   仅对农夫乐事等少数直接使用此方法的魂石生效，不影响 canEquip 中的常规调用。
 *
 * 注意：被拦截的方法是 MineFargo 自己的方法（不在混淆映射表中），
 * 故 @Inject 使用 remap = false。
 */
@Mixin(targets = "com.inolia_zaicek.mine_fargo.Util.MyGoUtil", remap = false)
public class MyGoUtilMixin {

    // ===== hasXXX 三参重载统一拦截 =====
    // 所有 hasXXX(Set, LivingEntity, Item) 方法签名一致，共用一个 handler。

    @Inject(
            method = {
                    "hasOre(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasNature(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasSupernatural(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasEntity(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasCataclysm(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasTwilight(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasTwilightLich(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasTwilightForest(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasGoetyItem(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasGoetyEntity(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasIAFDragon(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasIAFEntity(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasSonsOfSins(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasL2Hostility(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasL2Complements(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
                    "hasEnigmaticLegacy(Ljava/util/Set;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z"
            },
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void sst$gateHasFeature(Set<Item> curios, LivingEntity living, Item item,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (!SSTFeatureGate.isFeatureEnabled(living, item)) {
            cir.setReturnValue(false);
        }
    }

    // ===== isCurioEquipped 拦截（仅农夫乐事等直接判定魂石） =====

    @Inject(
            method = "isCurioEquipped(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void sst$gateIsCurioEquipped(LivingEntity entity, Item item,
                                                 CallbackInfoReturnable<Boolean> cir) {
        // 仅拦截直接使用 isCurioEquipped 的魂石（农夫乐事），
        // 不影响 canEquip 中经由 hasXXX 两参重载的常规调用。
        if (!SSTFeatureGate.usesDirectCurioCheck(item)) return;
        if (!SSTFeatureGate.isFeatureEnabled(entity, item)) {
            cir.setReturnValue(false);
        }
    }
}
