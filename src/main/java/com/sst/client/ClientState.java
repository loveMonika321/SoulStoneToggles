package com.sst.client;

import com.sst.core.CuriosScanner;
import com.sst.core.FeatureDef;
import com.sst.core.FeatureRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.*;

/**
 * 客户端缓存：服务端同步来的功能状态 + 本地装备计算出的可用功能列表。
 * GUI 打开时基于此渲染；收到 SyncPacket 后刷新（若 GUI 打开则重绘）。
 */
public final class ClientState {
    private static final Map<String, Boolean> STATES = new HashMap<>();
    private static List<FeatureDef> available = List.of();
    private static Set<String> equippedIdsCache = Set.of();
    private static List<FeatureDef.Category> cachedCategories = List.of();

    private ClientState() {}

    public static List<FeatureDef> available() {
        return available;
    }

    public static boolean isEnabled(String featureId) {
        return STATES.getOrDefault(featureId, Boolean.TRUE);
    }

    public static List<FeatureDef.Category> availableCategories() {
        return cachedCategories;
    }

    public static List<FeatureDef> inCategory(FeatureDef.Category cat) {
        return FeatureRegistry.availableInCategory(equippedIdsCache, cat);
    }

    public static List<FeatureRegistry.FeatureGroup> groupsInCategory(FeatureDef.Category cat) {
        return FeatureRegistry.availableGroupsInCategory(equippedIdsCache, cat);
    }

    public static int countInCategory(FeatureDef.Category cat) {
        int n = 0;
        for (FeatureDef f : available) {
            if (f.category == cat) n++;
        }
        return n;
    }

    /** 重新扫描本地饰品栏，刷新可用功能列表。 */
    public static void refreshAvailable() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            available = List.of();
            equippedIdsCache = Set.of();
            cachedCategories = List.of();
            return;
        }
        equippedIdsCache = CuriosScanner.equippedIds(player);
        available = FeatureRegistry.availableFor(equippedIdsCache);
        cachedCategories = FeatureRegistry.availableCategories(equippedIdsCache);
    }

    /** 收到服务端 SyncPacket 时调用。 */
    public static void applySync(Map<String, Boolean> states) {
        STATES.clear();
        STATES.putAll(states);
        refreshAvailable();
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof com.sst.client.gui.CategoryScreen cs) {
            cs.refresh();
        } else if (mc.screen instanceof com.sst.client.gui.AggregateScreen ags) {
            ags.refresh();
        } else if (mc.screen instanceof com.sst.client.gui.FeatureScreen fs) {
            fs.refresh();
        }
    }
}
