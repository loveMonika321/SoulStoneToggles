package com.sst.mixin;

import com.inolia_zaicek.mine_fargo.accessory.IAccessoryAbility;
import com.inolia_zaicek.mine_fargo.accessory.InoHurtEvent;
import com.sst.core.SSTFeatureGate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 拦截 MineFargo 的 AccessoryEventHandler —— IAccessoryAbility 能力派发中心。
 *
 * 聚合魂石（如灾变之魂、暮色之魂）实现 IAccessoryAbility 接口，在
 * inoLivingHurt / onTick / onHurt 等方法中直接施放所有子效果（不经 hasXXX 检查）。
 * 通过 @Redirect 在派发能力方法前检查该魂石的整体开关，
 * 关闭时跳过整个能力方法调用。
 *
 * 五个 @Redirect 分别对应 AccessoryEventHandler 的五个事件处理方法：
 *   onPlayerTick → ability.onTick
 *   inoAttacker  → ability.inoLivingHurt
 *   isAttacked   → ability.onHurt
 *   inoAttacked  → ability.inoLivingAttacked
 *   isAttacker   → ability.onAttack
 *
 * 兼容性说明：所有 @Redirect 均设 require = 0。
 * MineFargo 不同版本可能重构 AccessoryEventHandler 的方法签名或 IAccessoryAbility
 * 调用点；当某版本中目标不存在时，该 @Redirect 会被静默跳过（聚合整体开关失效，
 * 但单体魂石子功能开关经由 MyGoUtilMixin.hasXXX 仍有效），避免 mod 加载崩溃。
 */
@Mixin(targets = "com.inolia_zaicek.mine_fargo.accessory.AccessoryEventHandler", remap = false)
public class AccessoryEventHandlerMixin {

    @Redirect(
            method = "*",
            at = @At(value = "INVOKE",
                    target = "Lcom/inolia_zaicek/mine_fargo/accessory/IAccessoryAbility;onTick(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraftforge/event/entity/living/LivingEvent$LivingTickEvent;)V",
                    remap = false),
            remap = false
    )
    private static void sst$gateOnTick(IAccessoryAbility ability, LivingEntity living,
                                        LivingEvent.LivingTickEvent event) {
        if (ability instanceof Item item && !SSTFeatureGate.isAbilityEnabled(living, item)) return;
        ability.onTick(living, event);
    }

    @Redirect(
            require = 0,
            method = "inoAttacker",
            at = @At(value = "INVOKE",
                    target = "Lcom/inolia_zaicek/mine_fargo/accessory/IAccessoryAbility;inoLivingHurt(Lnet/minecraft/world/entity/LivingEntity;Lcom/inolia_zaicek/mine_fargo/accessory/InoHurtEvent;)V",
                    remap = false),
            remap = false
    )
    private static void sst$gateInoLivingHurt(IAccessoryAbility ability, LivingEntity living,
                                               InoHurtEvent event) {
        if (ability instanceof Item item && !SSTFeatureGate.isAbilityEnabled(living, item)) return;
        ability.inoLivingHurt(living, event);
    }

    @Redirect(
            require = 0,
            method = "isAttacked",
            at = @At(value = "INVOKE",
                    target = "Lcom/inolia_zaicek/mine_fargo/accessory/IAccessoryAbility;onHurt(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraftforge/event/entity/living/LivingHurtEvent;)V",
                    remap = false),
            remap = false
    )
    private static void sst$gateOnHurt(IAccessoryAbility ability, LivingEntity living,
                                        LivingHurtEvent event) {
        if (ability instanceof Item item && !SSTFeatureGate.isAbilityEnabled(living, item)) return;
        ability.onHurt(living, event);
    }

    @Redirect(
            require = 0,
            method = "inoAttacked",
            at = @At(value = "INVOKE",
                    target = "Lcom/inolia_zaicek/mine_fargo/accessory/IAccessoryAbility;inoLivingAttacked(Lnet/minecraft/world/entity/LivingEntity;Lcom/inolia_zaicek/mine_fargo/accessory/InoHurtEvent;)V",
                    remap = false),
            remap = false
    )
    private static void sst$gateInoLivingAttacked(IAccessoryAbility ability, LivingEntity living,
                                                    InoHurtEvent event) {
        if (ability instanceof Item item && !SSTFeatureGate.isAbilityEnabled(living, item)) return;
        ability.inoLivingAttacked(living, event);
    }

    @Redirect(
            require = 0,
            method = "isAttacker",
            at = @At(value = "INVOKE",
                    target = "Lcom/inolia_zaicek/mine_fargo/accessory/IAccessoryAbility;onAttack(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraftforge/event/entity/living/LivingHurtEvent;)V",
                    remap = false),
            remap = false
    )
    private static void sst$gateOnAttack(IAccessoryAbility ability, LivingEntity living,
                                          LivingHurtEvent event) {
        if (ability instanceof Item item && !SSTFeatureGate.isAbilityEnabled(living, item)) return;
        ability.onAttack(living, event);
    }
}
