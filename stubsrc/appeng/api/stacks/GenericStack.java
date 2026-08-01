package appeng.api.stacks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

public record GenericStack(AEKey what, long amount) {
    public GenericStack {
        Objects.requireNonNull(what, "what");
    }
    @Nullable
    public static GenericStack readBuffer(FriendlyByteBuf buffer) { return null; }
    public static void writeBuffer(@Nullable GenericStack s, FriendlyByteBuf b) {}
    @Nullable
    public static GenericStack readTag(CompoundTag tag) { return null; }
    public static CompoundTag writeTag(@Nullable GenericStack s) { return new CompoundTag(); }
    @Nullable
    public static GenericStack fromItemStack(net.minecraft.world.item.ItemStack s) { return null; }
    @Nullable
    public static GenericStack unwrapItemStack(net.minecraft.world.item.ItemStack s) { return null; }
    public net.minecraft.world.item.ItemStack toItemStack() { return net.minecraft.world.item.ItemStack.EMPTY; }
}
