package appeng.api.stacks;

import net.minecraft.resources.ResourceLocation;
import java.util.Collections;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class AEKeyTypes {
    private static final Map<ResourceLocation, AEKeyType> map = new HashMap<>();
    public static Collection<AEKeyType> getTypes() { return Collections.emptyList(); }
    public static AEKeyType get(ResourceLocation id) { return null; }
}
