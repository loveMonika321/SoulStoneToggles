package com.sst.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashSet;
import java.util.Set;

/**
 * 扫描 Curios 饰品栏，返回装备中的物品注册名集合。
 * 客户端（用于决定 GUI 显示哪些功能）与服务端（用于校验开关请求）共用。
 */
public final class CuriosScanner {

    private CuriosScanner() {}

    public static Set<String> equippedIds(LivingEntity living) {
        Set<String> out = new HashSet<>();
        if (living == null) return out;
        CuriosApi.getCuriosInventory(living).ifPresent(handler -> {
            var stacks = handler.getEquippedCurios();
            for (int i = 0; i < stacks.getSlots(); i++) {
                ItemStack stack = stacks.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                Item item = stack.getItem();
                ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
                if (rl != null) out.add(rl.toString());
            }
        });
        return out;
    }
}
