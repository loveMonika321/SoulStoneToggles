package com.sst.mixin.ae2;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 修复 AE2 AbstractTableRenderer.render 在服务器数据清空时的 NullPointerException。
 *
 * 崩溃场景：当服务器关闭/切服后，客户端最后一次 render 调用时：
 *   1. Minecraft.getInstance().level 可能已为 null
 *   2. entries 中存在 entry，使 getEntryStack(entry) 返回 null
 *   3. 后续 AEKeyRendering.drawInGui(..., entryStack) 读取 entryStack.getType() 触发 NPE
 *   4. 或 new GenericStack(entryStack, 0) 内部 Objects.requireNonNull(what) 触发 NPE
 *
 * 修复策略：
 *   A. @Inject HEAD：若 level==null（典型切服后残留渲染），直接取消本次 render；
 *   B. 对每个潜在 NPE 的调用点做 @Redirect 空值守卫（即使 level 不为空也存在 entryStack==null 的情况）：
 *      1. getEntryStack：调用方不拦截，仅在下游使用处判断；
 *      2. drawInGui：what==null 时直接 return，不再转发给原方法；
 *      3. GenericStack.<init>：what==null 时返回 null（调用方 StackWithBounds.record 接受 null）。
 *
 * 所有 @Inject/@Redirect require=0，AE2 未安装时不报错。
 */
@Mixin(targets = "appeng.client.gui.me.crafting.AbstractTableRenderer", remap = false)
public abstract class AE2AbstractTableRendererMixin {

    // ========== 策略 A：render 入口全局守卫 ==========

    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;IILjava/util/List;I)V",
            at = @At("HEAD"),
            cancellable = true,
            require = 0,
            remap = false
    )
    private void sst$onRenderHead(CallbackInfo ci) {
        // 服务器数据清空时（切服、退服）level 会先被置为 null
        if (Minecraft.getInstance().level == null) {
            ci.cancel();
        }
    }

    // ========== 策略 B-1：drawInGui 空值守卫 ==========

    @Redirect(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;IILjava/util/List;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/api/client/AEKeyRendering;drawInGui(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;IILappeng/api/stacks/AEKey;)V",
                    remap = false),
            remap = false,
            require = 0
    )
    private static void sst$safeDrawInGui(
            Minecraft mc, GuiGraphics gfx, int x, int y, AEKey what) {
        if (what == null) {
            return;
        }
        appeng.api.client.AEKeyRendering.drawInGui(mc, gfx, x, y, what);
    }

    // ========== 策略 B-2：GenericStack 构造空值守卫 ==========

    @Redirect(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;IILjava/util/List;I)V",
            at = @At(
                    value = "NEW",
                    target = "Lappeng/api/stacks/GenericStack;<init>(Lappeng/api/stacks/AEKey;J)V",
                    remap = false),
            remap = false,
            require = 0
    )
    private static GenericStack sst$safeGenericStack(AEKey what, long amount) {
        if (what == null) {
            return null;
        }
        return new GenericStack(what, amount);
    }
}
