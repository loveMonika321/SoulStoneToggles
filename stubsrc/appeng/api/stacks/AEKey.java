package appeng.api.stacks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/** 编译用桩：仅为 Mixin 引用的类型提供最小定义，运行时由真实 AE2 提供。 */
public abstract class AEKey {
    public abstract AEKeyType getType();
    public abstract AEKey dropSecondary();
    public abstract CompoundTag toTag();
    public abstract Object getPrimaryKey();
    public abstract ResourceLocation getId();
    public abstract void writeToPacket(FriendlyByteBuf data);
    public abstract Component computeDisplayName();
    public abstract void addDrops(long amount, List<ItemStack> drops, Level level, BlockPos pos);

    public Component getDisplayName() { return Component.literal("null"); }
    @Nullable public static AEKey fromTagGeneric(CompoundTag tag) { return null; }
    @Nullable public static AEKey readKey(FriendlyByteBuf buf) { return null; }
    public static void writeKey(FriendlyByteBuf buf, AEKey key) {}
    public CompoundTag toTagGeneric() { return new CompoundTag(); }
    public void writeToNBT(CompoundTag tag) {}
    public String toTagKey() { return "sst"; }
}
