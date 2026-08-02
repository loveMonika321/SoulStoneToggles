package com.sst.core;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.sst.core.FeatureDef.Category.*;

/**
 * 全部可开关功能及其"提供者魂石"映射表。
 *
 * 设计原则：
 *  - 不直接引用 MineFargo 的类，全部用注册名（字符串）匹配，避免编译期依赖。
 *  - 超然之魂 / 实体之魂 / 矿石之魂 / 自然之魂 / 伊始之魂 等聚合魂石作为某项功能的 provider 出现。
 *  - 每项功能都有 category，用于 GUI 二级页面分组。
 */
public final class FeatureRegistry {
    public static final String MF = "mine_fargo";

    // === 聚合魂石 ===
    public static final String SOUL_OF_ORES         = MF + ":soul_of_ores";
    public static final String SOUL_OF_NATURE       = MF + ":soul_of_nature";
    public static final String SOUL_OF_SUPERNATURAL = MF + ":soul_of_supernatural";
    public static final String SOUL_OF_ENTITY       = MF + ":soul_of_entity";
    public static final String SOUL_OF_INOLIA       = MF + ":soul_of_inolia";

    // === 矿石之魂 (9 种) ===
    public static final String COAL        = MF + ":coal_soul_stone";
    public static final String COPPER      = MF + ":copper_soul_stone";
    public static final String LAPIS       = MF + ":lapis_lazuli_soul_stone";
    public static final String IRON        = MF + ":iron_soul_stone";
    public static final String REDSTONE    = MF + ":redstone_soul_stone";
    public static final String GOLD        = MF + ":gold_soul_stone";
    public static final String EMERALD     = MF + ":emerald_soul_stone";
    public static final String DIAMOND     = MF + ":diamond_soul_stone";
    public static final String NETHERITE   = MF + ":netherite_soul_stone";

    // === 自然之魂 (8 种) ===
    public static final String SNOW       = MF + ":snow_soul_stone";
    public static final String LAVA       = MF + ":lava_soul_stone";
    public static final String MUSHROOM   = MF + ":mushroom_soul_stone";
    public static final String NETHER     = MF + ":nether_soul_stone";
    public static final String ENDER      = MF + ":ender_soul_stone";
    public static final String OCEAN      = MF + ":ocean_soul_stone";
    public static final String LUSH       = MF + ":lush_soul_stone";
    public static final String FOREST     = MF + ":forest_soul_stone";

    // === 超然之魂的子魂石 ===
    public static final String MAGNET   = MF + ":magnet_soul_stone";
    public static final String HAZARD   = MF + ":hazard_soul_stone";
    public static final String UNDYING  = MF + ":undying_soul_stone";
    public static final String EGA      = MF + ":enchanted_golden_apple_soul_stone";
    public static final String THE_SEA  = MF + ":the_sea_soul_stone";
    public static final String MENDING  = MF + ":mending_soul_stone";

    // === 实体之魂的子魂石 (6 种：烈焰人+死亡+节肢+动物+水生+羽翼) ===
    public static final String BLAZE        = MF + ":blaze_soul_stone";
    public static final String DEATH        = MF + ":death_soul_stone";
    public static final String ARTHROPOD    = MF + ":arthropod_soul_stone";
    public static final String ANIMAL       = MF + ":animal_soul_stone";
    public static final String AQUATIC      = MF + ":aquatic_soul_stone";
    public static final String WING         = MF + ":wing_soul_stone";

    // === MineFargo NBT 键（仅 MF_OPEN / MF_PIN 类型用） ===
    public static final String MF_MAGNET_OPEN  = MF + ":magnet_soul_stone_open";
    public static final String MF_HAZARD_OPEN  = MF + ":boolean_kill_range_skill_open";
    public static final String MF_FIRE_OPEN    = MF + ":fire_open";
    public static final String MF_TRACK_OPEN   = MF + ":projectile_tracking_capability_open";
    public static final String MF_EGA_CD       = MF + ":enchanted_golden_apple_soul_stone";

    private static final List<FeatureDef> FEATURES = new ArrayList<>();

    static {
        // ================================================================
        // ====== MineFargo 本体魂石 ======================================
        // ================================================================

        // ===== 矿物之魂聚合 + 9 子魂石（译名取自 mine_fargo zh_cn.json） =====
        Set<String> oresAgg = Set.of(SOUL_OF_ORES, SOUL_OF_INOLIA);
        addSoul("soul_of_ores", "矿物之魂", "综合矿物系联动效果，可整体开关", CORE_ORES, null);
        addSoul("coal_soul_stone",              "煤炭魂石",       "提升移动速度",                      CORE_ORES, "soul_of_ores", union(oresAgg, COAL));
        addSoul("copper_soul_stone",            "铜魂石",         "提升挖掘速度",                      CORE_ORES, "soul_of_ores", union(oresAgg, COPPER));
        addSoul("lapis_lazuli_soul_stone",      "青金石魂石",     "击杀实体掉落的经验提升",            CORE_ORES, "soul_of_ores", union(oresAgg, LAPIS));
        addSoul("iron_soul_stone",              "铁魂石",         "提升护甲值",                        CORE_ORES, "soul_of_ores", union(oresAgg, IRON));
        addSoul("redstone_soul_stone",          "红石魂石",       "提升攻击速度",                      CORE_ORES, "soul_of_ores", union(oresAgg, REDSTONE));
        addSoul("gold_soul_stone",              "金魂石",         "附魔时提升附魔等级",                CORE_ORES, "soul_of_ores", union(oresAgg, GOLD));
        addSoul("emerald_soul_stone",           "绿宝石魂石",     "提升村民声望降低交易价格",          CORE_ORES, "soul_of_ores", union(oresAgg, EMERALD));
        addSoul("diamond_soul_stone",           "钻石魂石",       "提升抢夺与时运等级",                CORE_ORES, "soul_of_ores", union(oresAgg, DIAMOND));
        addSoul("netherite_soul_stone",         "下界合金魂石",   "提升击退抗性与护甲韧性",            CORE_ORES, "soul_of_ores", union(oresAgg, NETHERITE));

        // ===== 自然之魂聚合 + 8 子魂石 =====
        Set<String> natureAgg = Set.of(SOUL_OF_NATURE, SOUL_OF_INOLIA);
        addSoul("soul_of_nature", "自然之魂", "综合自然系联动效果，可整体开关", CORE_NATURE, null);
        addSoul("snow_soul_stone",       "冰雪魂石",   "攻击附加缓慢I，免疫冻结伤害",       CORE_NATURE, "soul_of_nature", union(natureAgg, SNOW));
        addSoul("lava_soul_stone",       "熔岩魂石",   "可在熔岩上行走，获得火焰伤害减免",  CORE_NATURE, "soul_of_nature", union(natureAgg, LAVA));
        addSoul("mushroom_soul_stone",   "蘑菇魂石",   "食用碗装食物获得力量I，概率不消耗", CORE_NATURE, "soul_of_nature", union(natureAgg, MUSHROOM));
        addSoul("nether_soul_stone",     "下界魂石",   "位于下界维度时获得伤害增幅",        CORE_NATURE, "soul_of_nature", union(natureAgg, NETHER));
        addSoul("ender_soul_stone",      "末地魂石",   "对末影人增伤，受其伤害减免",        CORE_NATURE, "soul_of_nature", union(natureAgg, ENDER));
        addSoul("ocean_soul_stone",      "海洋魂石",   "可在水面行走，提升游泳速度",        CORE_NATURE, "soul_of_nature", union(natureAgg, OCEAN));
        addSoul("lush_soul_stone",       "翠绿魂石",   "每3秒回复饱和度与饥饿度",           CORE_NATURE, "soul_of_nature", union(natureAgg, LUSH));
        addSoul("forest_soul_stone",     "森林魂石",   "潜行时催熟周围农作物",              CORE_NATURE, "soul_of_nature", union(natureAgg, FOREST));

        // ===== 超然之魂子功能（锚定魂石已移除——属技能类，不应设开关） =====
        Set<String> supAgg = Set.of(SOUL_OF_SUPERNATURAL, SOUL_OF_INOLIA);
        add("magnet",   "磁铁魂石",       "每秒吸引周围物品和经验球",
                FeatureDef.Type.MF_OPEN, CORE_SUPERNATURAL, MF_MAGNET_OPEN, union(supAgg, MAGNET));
        add("hazard",   "危险魂石",       "周期性攻击最近的非随从实体",
                FeatureDef.Type.MF_OPEN, CORE_SUPERNATURAL, MF_HAZARD_OPEN, union(supAgg, HAZARD));
        add("undying",  "替死魂石",       "致命伤害时保留生命值并回血",
                FeatureDef.Type.SST, CORE_SUPERNATURAL, null, union(supAgg, UNDYING));
        add("ega",      "金果魂石",       "血量过低时自动触发金果效果",
                FeatureDef.Type.MF_PIN, CORE_SUPERNATURAL, MF_EGA_CD, union(supAgg, EGA));
        add("thesea",   "潮水魂石",       "于水中不再下沉，自动熄灭身上火焰",
                FeatureDef.Type.SST, CORE_SUPERNATURAL, null, union(supAgg, THE_SEA));
        add("mending",  "修复魂石",       "定期修补主副手物品与盔甲",
                FeatureDef.Type.SST, CORE_SUPERNATURAL, null, union(supAgg, MENDING));

        // ===== 生灵之魂聚合 + 6 子魂石 =====
        Set<String> entityAgg = Set.of(SOUL_OF_ENTITY, SOUL_OF_INOLIA);
        // 击中着火是狱灵魂石(烈焰人)的子功能，用 MF_OPEN
        add("fire", "魂石点燃目标", "攻击命中时点燃目标（狱灵魂石）",
                FeatureDef.Type.MF_OPEN, CORE_ENTITY, MF_FIRE_OPEN, Set.of(SOUL_OF_ENTITY, SOUL_OF_INOLIA, BLAZE));
        add("tracking", "弹射物追踪", "穿戴者释放的弹射物自动追踪目标",
                FeatureDef.Type.MF_OPEN, CORE_ENTITY, MF_TRACK_OPEN, Set.of(SOUL_OF_INOLIA));
        addSoul("blaze_soul_stone",       "狱灵魂石",   "攻击点燃目标，对燃烧目标增伤",      CORE_ENTITY, "soul_of_entity", union(entityAgg, BLAZE));
        addSoul("death_soul_stone",       "亡灵魂石",   "对亡灵生物造成伤害时获得伤害增幅",  CORE_ENTITY, "soul_of_entity", union(entityAgg, DEATH));
        addSoul("arthropod_soul_stone",   "节肢魂石",   "对节肢生物造成伤害时获得伤害增幅",  CORE_ENTITY, "soul_of_entity", union(entityAgg, ARTHROPOD));
        addSoul("animal_soul_stone",      "动物魂石",   "击杀生物时额外掉落战利品",          CORE_ENTITY, "soul_of_entity", union(entityAgg, ANIMAL));
        addSoul("aquatic_soul_stone",     "水生魂石",   "持续水下呼吸，水中/雨中增伤",       CORE_ENTITY, "soul_of_entity", union(entityAgg, AQUATIC));
        addSoul("wing_soul_stone",        "飞翼魂石",   "提升飞行速度，免疫摔落伤害",        CORE_ENTITY, "soul_of_entity", union(entityAgg, WING));

        // ===== 伊始之魂（万能聚合，整体开关） =====
        addSoul("soul_of_inolia", "伊始之魂", "视为穿戴所有魂石，可整体开关", CORE_INOLIA, null);

        // ================================================================
        // ====== 联动 mod 魂石 ===========================================
        // ================================================================

        // ===== 灾变之魂 (Cataclysm) =====
        addSoul("soul_of_cataclysm", "灾变之魂", "综合灾变联动效果，可整体开关", CATACLYSM, null);
        addSoul("ignis_soul_stone",                "焰魔魂石",             "攻击附加炽热烙印可升级，熔岩行走",       CATACLYSM, "soul_of_cataclysm");
        addSoul("the_leviathan_soul_stone",        "利维坦魂石",           "攻击附加深渊诅咒，水中增伤·离水减伤",    CATACLYSM, "soul_of_cataclysm");
        addSoul("maledictus_soul_stone",           "咒翼灵骸魂石",         "概率免疫伤害，致命伤时回血",              CATACLYSM, "soul_of_cataclysm");
        addSoul("ender_guardian_soul_stone",       "末影守卫魂石",         "攻击概率赋予晕眩效果",                   CATACLYSM, "soul_of_cataclysm");
        addSoul("ancient_remnant_soul_stone",      "远古遗魂魂石",         "提升护甲韧性，攻击附加骨裂",             CATACLYSM, "soul_of_cataclysm");
        addSoul("the_harbinger_soul_stone",        "先驱者魂石",           "攻击减少目标无敌时间",                   CATACLYSM, "soul_of_cataclysm");
        addSoul("netherite_monstrosity_soul_stone","下界合金巨兽魂石",     "攻击概率获得骇人之恶",                   CATACLYSM, "soul_of_cataclysm");
        addSoul("scylla_soul_stone",               "斯库拉魂石",           "攻击附加潮湿，对潮湿目标增伤",           CATACLYSM, "soul_of_cataclysm");

        // ===== 暮色之魂 (Twilight Forest - Boss) =====
        addSoul("soul_of_twilight", "暮色之魂", "综合暮色Boss联动效果，可整体开关", TWILIGHT, null);
        addSoul("naga_soul_stone",             "娜迦魂石",         "提升移动速度，回复数额提升",             TWILIGHT, "soul_of_twilight");
        addSoul("zombie_scepter_soul_stone",   "尸巫魂石",         "攻击时召唤忠诚僵尸",                     TWILIGHT, "soul_of_twilight");
        addSoul("lifedrain_soul_stone",        "血巫魂石",         "造成伤害时回复生命/饥饿/饱和",           TWILIGHT, "soul_of_twilight");
        addSoul("fortification_soul_stone",    "光巫魂石",         "造成伤害时生成巫妖护盾",                 TWILIGHT, "soul_of_twilight");
        addSoul("twilight_lich_soul_stone",    "巫妖魂石",         "巫妖联动效果",                           TWILIGHT, "soul_of_twilight");
        addSoul("minoshroom_soul_stone",       "米诺菇魂石",       "攻击破盾并无视护甲",                     TWILIGHT, "soul_of_twilight");
        addSoul("twilight_hydra_soul_stone",   "九头蛇魂石",       "受非范围伤害减免，失血越多回血/增伤越高",TWILIGHT, "soul_of_twilight");
        addSoul("knight_phantom_soul_stone",   "幻影骑士魂石",     "目标护甲越高伤害增幅越大",               TWILIGHT, "soul_of_twilight");
        addSoul("ur_ghast_soul_stone",         "暮初恶魂魂石",     "受击后获得伤害减免",                     TWILIGHT, "soul_of_twilight");
        addSoul("alpha_yeti_soul_stone",       "雪怪首领魂石",     "提升生命值，攻击对周围造成冰霜伤害",     TWILIGHT, "soul_of_twilight");
        addSoul("snow_queen_soul_stone",       "冰雪女王魂石",     "攻击对目标周围造成冰霜伤害",             TWILIGHT, "soul_of_twilight");

        // ===== 暮光之魂 (Twilight Forest - Materials) =====
        addSoul("soul_of_twilightforest", "暮光之魂", "综合暮色材料联动效果，可整体开关", TWILIGHT, null);
        addSoul("ironwood_soul_stone",         "铁木魂石",         "提升护甲值与护甲韧性",                   TWILIGHT, "soul_of_twilightforest");
        addSoul("steeleaf_soul_stone",         "钢叶魂石",         "定期为主副手物品与盔甲回复耐久",         TWILIGHT, "soul_of_twilightforest");
        addSoul("fiery_iron_soul_stone",       "炽铁魂石",         "攻击点燃目标并回复生命",                 TWILIGHT, "soul_of_twilightforest");
        addSoul("fluffy_cloud_soul_stone",     "浮云魂石",         "受到未站立地面目标的伤害时减免",         TWILIGHT, "soul_of_twilightforest");
        addSoul("twilight_giant_soul_stone",   "巨人魂石",         "提升实体范围与方块范围",                 TWILIGHT, "soul_of_twilightforest");
        addSoul("quest_ram_soul_stone",        "迷题羊魂石",       "减少受到的原版/铁魔法/魔艺魔法伤害",     TWILIGHT, "soul_of_twilightforest");

        // ===== 恶意之魂 (L2Hostility) =====
        addSoul("soul_of_l2hostility", "恶意之魂", "综合恶意联动效果，可整体开关", L2_HOSTILITY, null);
        addSoul("body_hostility_soul_stone",      "锻体魂石",   "生命护甲韧性加成，自适应减伤",           L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("corrosion_hostility_soul_stone", "腐蚀魂石",   "攻击附加多种负面效果",                  L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("resistance_hostility_soul_stone","四相魂石",   "火/物理/魔法/弹射物减伤",                L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("aqua_hostility_soul_stone",      "禁域魂石",   "范围内作战增伤减伤，随从增益",          L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("zone_hostility_soul_stone",      "疆域魂石",   "受伤反伤，禁止附近实体传送",            L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("destroy_hostility_soul_stone",   "毁坏魂石",   "清除目标正面效果，对同名实体真伤",      L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("ultra_hostility_soul_stone",     "神躯魂石",   "死亡回血，周期性范围魔法伤害",          L2_HOSTILITY, "soul_of_l2hostility");

        // ===== 恶锭之魂 (L2Complements) =====
        addSoul("soul_of_l2_complements", "恶锭之魂", "综合扩充联动效果，可整体开关", L2_COMPLEMENTS, null);
        addSoul("totemic_complements_soul_stone",    "生命魂石",   "对亡灵增伤，净化饥饿/凋零/中毒",         L2_COMPLEMENTS, "soul_of_l2_complements");
        addSoul("poseidite_complements_soul_stone",  "海神魂石",   "水中/雨中增伤，对水生生物额外增伤",      L2_COMPLEMENTS, "soul_of_l2_complements");
        addSoul("shulkerate_complements_soul_stone", "潜影魂石",   "提升攻击/触及距离，潜行减伤",            L2_COMPLEMENTS, "soul_of_l2_complements");
        addSoul("sculkium_complements_soul_stone",   "幽匿魂石",   "暗处增伤减伤，清除多种负面效果",         L2_COMPLEMENTS, "soul_of_l2_complements");
        addSoul("eternium_complements_soul_stone",   "永恒魂石",   "每秒回复工具与护甲耐久",                 L2_COMPLEMENTS, "soul_of_l2_complements");

        // ===== 神秘之魂 (EnigmaticLegacy) =====
        addSoul("soul_of_enigmatic_legacy", "神秘之魂", "综合遗物联动效果，可整体开关", ENIGMATIC, null);
        addSoul("curses_soul_stone",    "溢咒魂石",   "千咒卷轴记入诅咒数量加倍",              ENIGMATIC, "soul_of_enigmatic_legacy");
        addSoul("abyss_soul_stone",     "深渊魂石",   "攻击吸血，注视目标减速，击杀增伤",      ENIGMATIC, "soul_of_enigmatic_legacy");
        addSoul("etherium_soul_stone",  "以太魂石",   "限制生命上限，获得独立减伤与以太护盾",  ENIGMATIC, "soul_of_enigmatic_legacy");

        // ===== 诡物之魂 (Goety - Item) =====
        addSoul("soul_of_goety_item", "诡物之魂", "综合巫法物品联动效果，可整体开关", GOETY_ITEM, null);
        addSoul("ectoplasm_soul_stone",      "灵质魂石",     "击杀生物时额外获得灵魂能量",           GOETY_ITEM, "soul_of_goety_item");
        addSoul("goety_focus_soul_stone",    "聚晶魂石",     "聚晶附魔时提升附魔等级",               GOETY_ITEM, "soul_of_goety_item");
        addSoul("order_about_soul_stone",    "驱役魂石",     "随从增伤，攻击时转移随从仇恨",         GOETY_ITEM, "soul_of_goety_item");
        addSoul("escort_soul_stone",         "护卫魂石",     "随从获得伤害减免",                     GOETY_ITEM, "soul_of_goety_item");
        addSoul("brew_soul_stone",           "药酿魂石",     "饮用药酿/药水时概率不消耗",            GOETY_ITEM, "soul_of_goety_item");
        addSoul("legion_soul_stone",         "军团魂石",     "随从提升穿戴者生命/攻击/移速",         GOETY_ITEM, "soul_of_goety_item");
        addSoul("goety_dark_soul_stone",     "诡暗魂石",     "清除黑暗/失明，火/爆/魔减伤",           GOETY_ITEM, "soul_of_goety_item");

        // ===== 诡厄之魂 (Goety - Entity) =====
        addSoul("soul_of_goety_entity", "诡厄之魂", "综合巫法实体联动效果，可整体开关", GOETY_ENTITY, null);
        addSoul("apostle_soul_stone",               "使徒魂石",         "单次伤害封顶，致命伤回血，造成伤害回血",GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("nether_apostle_soul_stone",        "诡使魂石",         "免疫火焰，定期回血，攻击附加侵蚀",      GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("vizier_soul_stone",                "灾厄宰相魂石",     "聚晶附魔等级提升，致命时献祭怒鬼回血",  GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("redstone_monstrosity_soul_stone",  "红石巨兽魂石",     "提升生命上限，攻击附加目标当前生命伤害",GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("ender_keeper_soul_stone",          "末影守望者魂石",   "攻击附加虚空之蚀，对范围外实体减伤",    GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("minister_soul_stone",              "灾厄教父魂石",     "随从获得抗性/再生，非随从实体虚弱",     GOETY_ENTITY, "soul_of_goety_entity");

        // ===== 巨龙之魂 (Ice and Fire - Dragon) =====
        addSoul("soul_of_iaf_dragon", "巨龙之魂", "综合龙类联动效果，可整体开关", IAF_DRAGON, null);
        addSoul("dragon_bone_soul_stone",              "龙骨魂石",       "对冰火巨龙增伤减伤，无视护甲",          IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("fire_dragon_blood_soul_stone",        "炎血魂石",       "攻击点燃目标，对冰龙增伤",              IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("ice_dragon_blood_soul_stone",         "霜血魂石",       "攻击附加缓慢/挖掘疲劳/冰冻，对火龙增伤",IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("lightning_dragon_blood_soul_stone",   "霆血魂石",       "攻击附加雷电伤害，对火/冰龙增伤",       IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("fire_dragon_steel_soul_stone",        "龙炎魂石",       "提升护甲韧性，攻击附加火焰伤害",        IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("ice_dragon_steel_soul_stone",         "龙霜魂石",       "提升护甲韧性，攻击附加冻结伤害",        IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("lightning_dragon_steel_soul_stone",   "龙霆魂石",       "提升护甲韧性，攻击附加雷电伤害",        IAF_DRAGON, "soul_of_iaf_dragon");

        // ===== 冰火之魂 (Ice and Fire - Entity) =====
        addSoul("soul_of_iaf_entity", "冰火之魂", "综合冰火实体联动效果，可整体开关", IAF_ENTITY, null);
        addSoul("cyclops_soul_stone",       "独眼巨人魂石",     "持续为周围实体附加虚弱",                IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("gorgon_soul_stone",        "蛇发女妖魂石",     "对注视的实体持续附加缓慢",              IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("iaf_hydra_soul_stone",     "九头蛇魂石",       "每秒回血，失血越多回血越高",            IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("iaf_siren_soul_stone",     "塞壬魂石",         "攻击魅惑目标",                         IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("sea_serpent_soul_stone",   "海蟒魂石",         "雨中或水中持续获得力量",                IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("troll_soul_stone",         "食人妖魂石",       "提供弹射物伤害减免",                   IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("hippocampus_soul_stone",   "海马魂石",         "提升游泳速度，攻击施加缓慢/反胃",       IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("death_worm_soul_stone",    "死亡蠕虫魂石",     "提升攻击距离",                         IAF_ENTITY, "soul_of_iaf_entity");

        // ===== 七罪之魂 (Sons of Sins) =====
        addSoul("soul_of_sons_of_sins", "七罪之魂", "综合七罪联动效果，可整体开关", SONS_OF_SINS, null);
        addSoul("envy_sin_soul_stone",      "嫉妒魂石",   "提升攻速/伤害，疾跑时移速提升",         SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("gluttony_sin_soul_stone",  "暴食魂石",   "击杀实体获得伤害增幅",                  SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("greed_sin_soul_stone",     "贪婪魂石",   "窒息伤害减免，击杀额外掉落战利品",      SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("lust_sin_soul_stone",      "色欲魂石",   "弹射物减伤，攻击无视护甲",              SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("pride_sin_soul_stone",     "傲慢魂石",   "对生命比例低于自身的目标增伤",          SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("sloth_sin_soul_stone",     "怠惰魂石",   "提升最大生命值，攻击施加缓慢",          SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("wrath_sin_soul_stone",     "暴怒魂石",   "降低最大生命值，获得伤害增幅",          SONS_OF_SINS, "soul_of_sons_of_sins");

        // ===== 乐事魂石 (Farmer's Delight) =====
        addSoul("farmers_delight_soul_stone", "乐事魂石", "进食时概率获得滋养与舒适效果", FARMERS_DELIGHT, null);
    }

    private FeatureRegistry() {}

    public static List<FeatureDef> all() {
        return FEATURES;
    }

    public static FeatureDef byId(String id) {
        for (FeatureDef f : FEATURES) {
            if (f.id.equals(id)) return f;
        }
        return null;
    }

    /** 返回当前装备命中的 provider 集合对应的所有可用功能。 */
    public static List<FeatureDef> availableFor(Set<String> equippedIds) {
        // 扩展：若玩家装备了聚合容器（如 mine_fargo:soul_of_twilight），
        // 则把属于该聚合的所有子项也视为"装备中"，这样它们的 provider 也能匹配。
        Set<String> expanded = new HashSet<>(equippedIds);
        for (FeatureDef f : FEATURES) {
            if (isAggregateContainer(f.id)) {
                // 聚合容器的第一个 provider 就是它自己的注册名
                String selfKey = f.providers.iterator().next();
                if (equippedIds.contains(selfKey)) {
                    // 把所有 belongsTo 该聚合的子项的注册名也加进 expanded
                    for (FeatureDef sub : FEATURES) {
                        if (!isAggregateContainer(sub.id) && belongsToAggregate(sub.id, f.id)) {
                            String subKey = MF + ":" + sub.id;
                            expanded.add(subKey);
                        }
                    }
                }
            }
        }

        List<FeatureDef> out = new ArrayList<>();
        for (FeatureDef f : FEATURES) {
            for (String p : f.providers) {
                if (expanded.contains(p)) { out.add(f); break; }
            }
        }
        return out;
    }

    /** 返回「至少有 1 项可用功能」的分类列表，按枚举顺序。 */
    public static List<FeatureDef.Category> availableCategories(Set<String> equippedIds) {
        boolean[] has = new boolean[FeatureDef.Category.values().length];
        for (FeatureDef f : availableFor(equippedIds)) {
            has[f.category.ordinal()] = true;
        }
        List<FeatureDef.Category> out = new ArrayList<>();
        for (FeatureDef.Category c : FeatureDef.Category.values()) {
            if (has[c.ordinal()]) out.add(c);
        }
        return out;
    }

    /** 返回某分类下的可用功能。 */
    public static List<FeatureDef> availableInCategory(Set<String> equippedIds, FeatureDef.Category cat) {
        List<FeatureDef> out = new ArrayList<>();
        for (FeatureDef f : availableFor(equippedIds)) {
            if (f.category == cat) out.add(f);
        }
        return out;
    }

    /** 返回某分类下的可用功能总数（用于一级分类页显示 "n 项"）。 */
    public static int countInCategory(Set<String> equippedIds, FeatureDef.Category cat) {
        int n = 0;
        for (FeatureDef f : availableFor(equippedIds)) {
            if (f.category == cat) n++;
        }
        return n;
    }

    /**
     * 把"某分类下的可用功能"按二级聚合分组。
     *   - 聚合项：mineFargoKey==null && id==aggregateId（由 addSoul(id, ..., aggregateId==null) 添加，作为聚合容器）
     *   - 子项：其 aggregateId 等于某聚合的 id，或没有 aggregateId（这些归入 "直接功能（无聚合）" 组）
     *
     * 返回分组的有序列表，每组包含：aggregate 本身（可为 null 表示"无聚合直接功能"）、子项列表。
     */
    public static List<FeatureGroup> availableGroupsInCategory(Set<String> equippedIds,
                                                                FeatureDef.Category cat) {
        // 1. 收齐本分类下所有可用功能
        List<FeatureDef> all = new ArrayList<>();
        for (FeatureDef f : availableFor(equippedIds)) {
            if (f.category == cat) all.add(f);
        }

        // 2. 先找聚合容器（mineFargoKey==null 且无 aggregateId，自己就是聚合）
        Map<String, List<FeatureDef>> byAggregate = new LinkedHashMap<>();
        List<FeatureDef> topLevel = new ArrayList<>(); // 不属于任何聚合的顶层功能（如 magnet/hazard/ega/fire）
        List<FeatureDef> aggregates = new ArrayList<>();

        for (FeatureDef f : all) {
            if (f.mineFargoKey == null && isAggregateContainer(f.id)) {
                aggregates.add(f);
                byAggregate.put(f.id, new ArrayList<>());
            }
        }
        // 3. 归类子项
        for (FeatureDef f : all) {
            if (aggregates.contains(f)) continue;
            String aggId = lookupAggregateId(f.id);
            if (aggId != null && byAggregate.containsKey(aggId)) {
                byAggregate.get(aggId).add(f);
            } else {
                topLevel.add(f);
            }
        }

        // 4. 组装返回：先聚合（按注册顺序），再 topLevel 一组
        List<FeatureGroup> out = new ArrayList<>();
        for (FeatureDef agg : aggregates) {
            out.add(new FeatureGroup(agg, byAggregate.get(agg.id)));
        }
        if (!topLevel.isEmpty()) {
            out.add(new FeatureGroup(null, topLevel));
        }
        return out;
    }

    /** 功能是否为聚合容器（用 addSoul(id, ..., aggregateId==null) 添加）。 */
    private static boolean isAggregateContainer(String id) {
        for (FeatureDef f : FEATURES) {
            if (f.id.equals(id) && f.type == FeatureDef.Type.SST && f.mineFargoKey == null
                    && lookupAggregateId(id) == null) {
                return true;
            }
        }
        return false;
    }

    /** 查找某功能所属的 aggregateId（通过 ITEM_TO_AGGREGATE 反向查，不存在返回 null）。 */
    private static String lookupAggregateId(String featureId) {
        // featureId → registryName (MF + ":featureId")
        String registryName = MF + ":" + featureId;
        // 聚合映射是从 registryName 聚合 ID 来的；用 SSTFeatureGate 的映射反向
        String agg = SSTFeatureGate.lookupAggregateForRegistry(registryName);
        if (agg != null) return agg;
        return null;
    }

    /** 某子功能是否属于指定 aggregateId（通过 ITEM_TO_AGGREGATE 反查）。 */
    private static boolean belongsToAggregate(String featureId, String aggregateId) {
        return aggregateId.equals(lookupAggregateId(featureId));
    }

    /** 分组结构：某聚合容器 + 其下所有子功能；aggregate==null 表示"直接功能（无聚合）"。 */
    public static class FeatureGroup {
        /** 聚合容器本身（可能为 null，表示顶层无聚合功能）。 */
        public final FeatureDef aggregate;
        /** 聚合包含的子功能（aggregate==null 时就是顶层功能自己；aggregate!=null 时为子项）。 */
        public final List<FeatureDef> features;
        /** 分组标题。 */
        public final Component title;

        public FeatureGroup(FeatureDef aggregate, List<FeatureDef> features) {
            this.aggregate = aggregate;
            this.features = Collections.unmodifiableList(new ArrayList<>(features));
            this.title = aggregate == null
                    ? Component.literal("§7直接功能（无聚合）")
                    : Component.literal("§e" + aggregate.name.getString() + " §7[聚合，可整体开关]");
        }
    }

    // ===== 内部注册方法 =====

    private static void add(String id, String name, String desc, FeatureDef.Type type,
                            FeatureDef.Category cat, String mfKey, Set<String> providers) {
        FEATURES.add(new FeatureDef(id,
                Component.literal(name),
                Component.literal(desc),
                type, cat, mfKey, providers));
    }

    /**
     * 注册一个联动魂石功能（SST 类型）。
     * @param id           功能 id（同时作为注册名 path）
     * @param name         显示名
     * @param desc         描述
     * @param cat          所属分类
     * @param aggregateId  所属聚合魂石的功能 id（null=无聚合/自身即聚合）
     */
    private static void addSoul(String id, String name, String desc, FeatureDef.Category cat,
                                 String aggregateId) {
        String registryName = MF + ":" + id;
        Set<String> providers = new HashSet<>();
        providers.add(registryName);
        providers.add(SOUL_OF_INOLIA);
        FEATURES.add(new FeatureDef(id,
                Component.literal(name),
                Component.literal(desc),
                FeatureDef.Type.SST, cat, null, providers));
        // 注册魂石注册名 → 功能 id 映射
        SSTFeatureGate.registerItem(registryName, id);
        // 记录单体→聚合映射，供 SSTFeatureGate 在 hasXXX 拦截时检查聚合开关
        if (aggregateId != null) {
            SSTFeatureGate.registerAggregate(registryName, aggregateId);
        }
    }

    /** 4 参数版 addSoul：用于单体魂石，带额外 providers（矿石/自然/实体的子魂石用）。 */
    private static void addSoul(String id, String name, String desc, FeatureDef.Category cat,
                                 String aggregateId, Set<String> extraProviders) {
        String registryName = MF + ":" + id;
        Set<String> providers = new HashSet<>(extraProviders);
        providers.add(registryName);
        // providers 里可能已经包含 SOUL_OF_INOLIA（由外部传入的 oresAgg/natureAgg 等），这里不重复加
        FEATURES.add(new FeatureDef(id,
                Component.literal(name),
                Component.literal(desc),
                FeatureDef.Type.SST, cat, null, providers));
        SSTFeatureGate.registerItem(registryName, id);
        if (aggregateId != null) {
            SSTFeatureGate.registerAggregate(registryName, aggregateId);
        }
    }

    private static Set<String> union(Set<String> base, String... more) {
        Set<String> s = new HashSet<>(base);
        Collections.addAll(s, more);
        return s;
    }

    private static Set<String> union(String... more) {
        Set<String> s = new HashSet<>();
        Collections.addAll(s, more);
        return s;
    }
}
