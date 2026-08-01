package com.inolia_zaicek.mine_fargo.Util;

import java.util.Set;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

/** 编译期桩：仅为满足 Mixin 注解处理器对目标类的解析，运行时由真实 MineFargo 提供。 */
public class MyGoUtil {

    // ===== hasXXX 三参重载（事件处理器用） =====
    public static boolean hasOre(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasNature(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasSupernatural(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasEntity(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasCataclysm(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasTwilight(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasTwilightLich(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasTwilightForest(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasGoetyItem(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasGoetyEntity(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasIAFDragon(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasIAFEntity(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasSonsOfSins(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasL2Hostility(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasL2Complements(Set<Item> curios, LivingEntity living, Item item) { return false; }
    public static boolean hasEnigmaticLegacy(Set<Item> curios, LivingEntity living, Item item) { return false; }

    // ===== isCurioEquipped 两参重载（农夫乐事等直接判定） =====
    public static boolean isCurioEquipped(LivingEntity entity, Item itemStackSupplier) { return false; }
}
