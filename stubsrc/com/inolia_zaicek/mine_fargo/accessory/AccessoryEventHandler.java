package com.inolia_zaicek.mine_fargo.accessory;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * 编译期桩：MineFargo 饰品事件处理器。
 *
 * 仅保留五个被 @Redirect 拦截的事件方法及其内部对 IAccessoryAbility 的 INVOKE 调用，
 * 以满足 Mixin 注解处理器对 @Redirect 目标的解析。运行时由真实 MineFargo 提供。
 */
public class AccessoryEventHandler {

    public static void onPlayerTick(LivingEvent.LivingTickEvent event) {
        IAccessoryAbility ability = null;
        LivingEntity living = event.getEntity();
        if (ability != null) {
            ability.onTick(living, event);
        }
    }

    public static void inoAttacker(InoHurtEvent event) {
        IAccessoryAbility ability = null;
        LivingEntity attacker = event.attacker;
        if (ability != null) {
            ability.inoLivingHurt(attacker, event);
        }
    }

    public static void isAttacked(LivingHurtEvent event) {
        IAccessoryAbility ability = null;
        LivingEntity living = event.getEntity();
        if (ability != null) {
            ability.onHurt(living, event);
        }
    }

    public static void inoAttacked(InoHurtEvent event) {
        IAccessoryAbility ability = null;
        LivingEntity attacked = event.target;
        if (ability != null) {
            ability.inoLivingAttacked(attacked, event);
        }
    }

    public static void isAttacker(LivingHurtEvent event) {
        IAccessoryAbility ability = null;
        LivingEntity attacker = event.getEntity();
        if (ability != null) {
            ability.onAttack(attacker, event);
        }
    }
}
