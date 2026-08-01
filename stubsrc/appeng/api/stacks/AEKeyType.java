package appeng.api.stacks;

public interface AEKeyType {
    java.util.Collection<? extends AEKeyType> VALUES = java.util.Collections.emptyList();
    String getName();
    AEKey loadKeyFromTag(net.minecraft.nbt.CompoundTag tag);
}
