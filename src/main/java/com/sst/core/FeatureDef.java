package com.sst.core;

import net.minecraft.network.chat.Component;

import java.util.Set;

/**
 * 单个可开关功能的定义。
 *
 * type 说明：
 *  - MF_OPEN  : 复用 MineFargo 的 *_open NBT 开关（<=50 开, >50 关）。直接写 25/75。
 *  - MF_PIN   : MineFargo 没有独立开关，靠把某个冷却 NBT 钉到高位来抑制（如金果）。
 *  - SST      : 由本 mod 自己的 NBT 记录，并通过 Mixin 拦截 MineFargo 逻辑生效。
 *
 * category 用于 GUI 二级页面分组：
 *  - 一级页面列出所有「至少有 1 项可用功能」的分类（按分类枚举顺序）
 *  - 二级页面列出该分类下的所有可用功能开关
 */
public class FeatureDef {
    public enum Type { MF_OPEN, MF_PIN, SST }

    public enum Category {
        CORE_ORES          ("矿石之魂",    "MineFargo 本体 · 矿石系"),
        CORE_NATURE        ("自然之魂",    "MineFargo 本体 · 自然系"),
        CORE_ENTITY        ("实体之魂",    "MineFargo 本体 · 实体系"),
        CORE_SUPERNATURAL  ("超然之魂",    "MineFargo 本体 · 超然系"),
        CORE_INOLIA        ("伊始之魂",    "MineFargo 本体 · 伊始聚合"),

        CATACLYSM          ("灾变",         "联动 · 灾变 Cataclysm"),
        TWILIGHT           ("暮色森林",     "联动 · 暮色森林 Twilight Forest"),
        L2_HOSTILITY       ("莱特兰恶意",   "联动 · 莱特兰恶意 L2Hostility"),
        L2_COMPLEMENTS     ("莱特兰扩充",   "联动 · 莱特兰扩充 L2Complements"),
        ENIGMATIC          ("神秘遗物",     "联动 · 神秘遗物 Enigmatic Legacy"),
        FARMERS_DELIGHT    ("农夫乐事",     "联动 · 农夫乐事 Farmer's Delight"),
        GOETY_ITEM         ("诡厄巫法·物",  "联动 · 诡厄巫法 Goety · 物品系"),
        GOETY_ENTITY       ("诡厄巫法·实体","联动 · 诡厄巫法 Goety · 实体系"),
        IAF_DRAGON         ("冰火传说·龙",  "联动 · 冰火传说 Ice and Fire · 龙系"),
        IAF_ENTITY         ("冰火传说·实体","联动 · 冰火传说 Ice and Fire · 实体系"),
        SONS_OF_SINS       ("七罪之子",     "联动 · 七罪之子 Sons of Sins");

        public final Component title;
        public final Component subtitle;
        Category(String title, String subtitle) {
            this.title = Component.literal(title);
            this.subtitle = Component.literal(subtitle);
        }
    }

    public final String id;
    public final Component name;
    public final Component desc;
    public final Type type;
    public final Category category;
    /** MF_OPEN/MF_PIN：MineFargo 的 NBT 键；SST：null（用 soulstonetoggles:&lt;id&gt;） */
    public final String mineFargoKey;
    /** 提供该功能的魂石注册名集合（任一装备即显示该功能） */
    public final Set<String> providers;

    public FeatureDef(String id, Component name, Component desc, Type type, Category category,
                      String mineFargoKey, Set<String> providers) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.type = type;
        this.category = category;
        this.mineFargoKey = mineFargoKey;
        this.providers = providers;
    }
}
