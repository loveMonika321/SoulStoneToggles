package com.inolia_zaicek.mine_fargo.accessory;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * 编译期桩：MineFargo 自定义伤害事件。
 * 运行时由真实 MineFargo 提供，本桩仅包含 Mixin @Redirect 所需的类型签名。
 */
public class InoHurtEvent extends Event {

    public final LivingEntity attacker;
    public final LivingEntity target;
    public final LivingHurtEvent hurtEvent;

    public InoHurtEvent(LivingEntity attacker, LivingEntity target, LivingHurtEvent hurtEvent) {
        this.attacker = attacker;
        this.target = target;
        this.hurtEvent = hurtEvent;
    }
}
