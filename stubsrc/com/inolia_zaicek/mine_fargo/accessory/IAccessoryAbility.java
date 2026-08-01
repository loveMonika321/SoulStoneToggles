package com.inolia_zaicek.mine_fargo.accessory;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * 编译期桩：MineFargo 饰品能力接口。
 * 运行时由真实 MineFargo 提供，本桩仅为满足 Mixin 注解处理器对
 * AccessoryEventHandlerMixin @Redirect 目标方法的解析。
 */
public interface IAccessoryAbility {

    String getId();

    default void onHurt(LivingEntity livingEntity, LivingHurtEvent event) {}

    default void onAttack(LivingEntity livingEntity, LivingHurtEvent event) {}

    default void onTick(LivingEntity livingEntity, LivingEvent.LivingTickEvent event) {}

    default void inoLivingHurt(LivingEntity livingEntity, InoHurtEvent event) {}

    default void inoLivingAttacked(LivingEntity livingEntity, InoHurtEvent event) {}
}
