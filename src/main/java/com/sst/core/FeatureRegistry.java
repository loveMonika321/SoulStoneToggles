package com.sst.core;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

        // ===== 矿石之魂聚合 + 9 子魂石 =====
        Set<String> oresAgg = Set.of(SOUL_OF_ORES, SOUL_OF_INOLIA);
        addSoul("soul_of_ores", "矿石之魂", "综合矿石系联动效果，可整体开关", CORE_ORES, null);
        addSoul("coal_soul_stone",              "煤矿魂石",       "火把照明半径·熔炉速度",            CORE_ORES, "soul_of_ores", union(oresAgg, COAL));
        addSoul("copper_soul_stone",            "铜矿魂石",       "雷击减伤·避雷针效果",              CORE_ORES, "soul_of_ores", union(oresAgg, COPPER));
        addSoul("lapis_lazuli_soul_stone",      "青金石魂石",     "附魔加成·经验加成",                CORE_ORES, "soul_of_ores", union(oresAgg, LAPIS));
        addSoul("iron_soul_stone",              "铁矿魂石",       "护甲/韧性加成·修理铁砧打折",       CORE_ORES, "soul_of_ores", union(oresAgg, IRON));
        addSoul("redstone_soul_stone",          "红石魂石",       "充能信标·范围效果增强",            CORE_ORES, "soul_of_ores", union(oresAgg, REDSTONE));
        addSoul("gold_soul_stone",              "金矿魂石",       "猪灵不攻击·金锭掉落加成",          CORE_ORES, "soul_of_ores", union(oresAgg, GOLD));
        addSoul("emerald_soul_stone",           "绿宝石魂石",     "村民交易打折·流浪商人礼遇",        CORE_ORES, "soul_of_ores", union(oresAgg, EMERALD));
        addSoul("diamond_soul_stone",           "钻石魂石",       "挖掘速度·挖掘幸运加成",            CORE_ORES, "soul_of_ores", union(oresAgg, DIAMOND));
        addSoul("netherite_soul_stone",         "下界合金魂石",   "击退抗性·熔岩游泳·伤害减免",      CORE_ORES, "soul_of_ores", union(oresAgg, NETHERITE));

        // ===== 自然之魂聚合 + 8 子魂石 =====
        Set<String> natureAgg = Set.of(SOUL_OF_NATURE, SOUL_OF_INOLIA);
        addSoul("soul_of_nature", "自然之魂", "综合自然系联动效果，可整体开关", CORE_NATURE, null);
        addSoul("snow_soul_stone",       "雪原魂石",   "细雪不陷·冰霜行者·冰冻免疫",       CORE_NATURE, "soul_of_nature", union(natureAgg, SNOW));
        addSoul("lava_soul_stone",       "熔岩魂石",   "熔岩游泳·燃烧免疫·火焰附加",       CORE_NATURE, "soul_of_nature", union(natureAgg, LAVA));
        addSoul("mushroom_soul_stone",   "蘑菇魂石",   "哞菇转换·炖菜效果·孢子效果",        CORE_NATURE, "soul_of_nature", union(natureAgg, MUSHROOM));
        addSoul("nether_soul_stone",     "下界魂石",   "下界环境增益·恶魂不主动攻击",       CORE_NATURE, "soul_of_nature", union(natureAgg, NETHER));
        addSoul("ender_soul_stone",      "末影魂石",   "随机传送·末影人不怒·末地石加速",    CORE_NATURE, "soul_of_nature", union(natureAgg, ENDER));
        addSoul("ocean_soul_stone",      "海洋魂石",   "水中呼吸·挖掘速度·海豚恩惠",        CORE_NATURE, "soul_of_nature", union(natureAgg, OCEAN));
        addSoul("lush_soul_stone",       "繁茂魂石",   "苔藓加速·孢子花·发光浆果",          CORE_NATURE, "soul_of_nature", union(natureAgg, LUSH));
        addSoul("forest_soul_stone",     "森林魂石",   "花粉传播·蜜蜂友好·自然再生",        CORE_NATURE, "soul_of_nature", union(natureAgg, FOREST));

        // ===== 超然之魂子功能（锚定魂石已移除——属技能类，不应设开关） =====
        Set<String> supAgg = Set.of(SOUL_OF_SUPERNATURAL, SOUL_OF_INOLIA);
        add("magnet",   "磁铁魂石",       "吸附附近掉落物与经验球",
                FeatureDef.Type.MF_OPEN, CORE_SUPERNATURAL, MF_MAGNET_OPEN, union(supAgg, MAGNET));
        add("hazard",   "危险魂石",       "周期性自动攻击最近敌人",
                FeatureDef.Type.MF_OPEN, CORE_SUPERNATURAL, MF_HAZARD_OPEN, union(supAgg, HAZARD));
        add("undying",  "不死魂石",       "致命伤害时免死并回血",
                FeatureDef.Type.SST, CORE_SUPERNATURAL, null, union(supAgg, UNDYING));
        add("ega",      "金果魂石",       "低血量时自动触发金果效果",
                FeatureDef.Type.MF_PIN, CORE_SUPERNATURAL, MF_EGA_CD, union(supAgg, EGA));
        add("thesea",   "潮水魂石",       "水中悬浮并熄灭自身火焰",
                FeatureDef.Type.SST, CORE_SUPERNATURAL, null, union(supAgg, THE_SEA));
        add("mending",  "经验修补魂石",   "定期修补主副手物品",
                FeatureDef.Type.SST, CORE_SUPERNATURAL, null, union(supAgg, MENDING));

        // ===== 实体之魂聚合 + 6 子魂石 =====
        Set<String> entityAgg = Set.of(SOUL_OF_ENTITY, SOUL_OF_INOLIA);
        // 击中着火是狱火魂石(烈焰人)的子功能，用 MF_OPEN
        add("fire", "击中实体着火", "攻击命中时点燃目标（狱火/烈焰人魂石）",
                FeatureDef.Type.MF_OPEN, CORE_ENTITY, MF_FIRE_OPEN, Set.of(SOUL_OF_ENTITY, SOUL_OF_INOLIA, BLAZE));
        add("tracking", "弹射物追踪", "投射物自动追踪目标",
                FeatureDef.Type.MF_OPEN, CORE_ENTITY, MF_TRACK_OPEN, Set.of(SOUL_OF_INOLIA));
        addSoul("death_soul_stone",       "死亡魂石",   "亡灵亲和·亡灵治疗·亡灵不攻击",       CORE_ENTITY, "soul_of_entity", union(entityAgg, DEATH));
        addSoul("arthropod_soul_stone",   "节肢魂石",   "蜘蛛攀爬·节肢减伤·毒伤免疫",         CORE_ENTITY, "soul_of_entity", union(entityAgg, ARTHROPOD));
        addSoul("animal_soul_stone",      "动物魂石",   "动物不逃·额外掉落·繁殖加速",         CORE_ENTITY, "soul_of_entity", union(entityAgg, ANIMAL));
        addSoul("aquatic_soul_stone",     "水生魂石",   "海豚恩惠·水行·鱼群额外掉落",         CORE_ENTITY, "soul_of_entity", union(entityAgg, AQUATIC));
        addSoul("wing_soul_stone",        "羽翼魂石",   "滑翔·缓降·鞘翅飞行加速",             CORE_ENTITY, "soul_of_entity", union(entityAgg, WING));

        // ===== 伊始之魂（万能聚合，整体开关） =====
        addSoul("soul_of_inolia", "伊始之魂", "万能聚合魂石，可整体开关", CORE_INOLIA, null);

        // ================================================================
        // ====== 联动 mod 魂石 ===========================================
        // ================================================================

        // ===== 灾变 (Cataclysm) =====
        addSoul("soul_of_cataclysm", "灾变之魂", "综合灾变联动效果，可整体开关", CATACLYSM, null);
        addSoul("ignis_soul_stone",               "炽焰魂石",             "击中施加燃烧烙印，可升级",               CATACLYSM, "soul_of_cataclysm");
        addSoul("the_leviathan_soul_stone",       "利维坦之魂石",         "水中增伤·深渊诅咒·离水减伤",            CATACLYSM, "soul_of_cataclysm");
        addSoul("maledictus_soul_stone",          "诅咒者之魂石",         "概率完全免除本次伤害",                   CATACLYSM, "soul_of_cataclysm");
        addSoul("ender_guardian_soul_stone",      "末影守卫者之魂石",     "击中概率眩晕目标",                       CATACLYSM, "soul_of_cataclysm");
        addSoul("ancient_remnant_soul_stone",     "远古残骸之魂石",       "击中施加骨折·韧性加成",                 CATACLYSM, "soul_of_cataclysm");
        addSoul("the_harbinger_soul_stone",       "先驱者之魂石",         "降低目标无敌帧",                         CATACLYSM, "soul_of_cataclysm");
        addSoul("netherite_monstrosity_soul_stone","下界合金恶兽之魂石",   "击中施加骇人效果",                       CATACLYSM, "soul_of_cataclysm");
        addSoul("scylla_soul_stone",              "斯库拉之魂石",         "击中施加潮湿·额外水伤",                  CATACLYSM, "soul_of_cataclysm");

        // ===== 暮色森林 (Twilight Forest) =====
        addSoul("soul_of_twilight", "暮色之魂", "综合暮色Boss联动效果，可整体开关", TWILIGHT, null);
        addSoul("naga_soul_stone",             "娜加之魂石",         "移速加成",                                   TWILIGHT, "soul_of_twilight");
        addSoul("zombie_scepter_soul_stone",   "僵尸权杖之魂石",     "攻击时召唤僵尸",                             TWILIGHT, "soul_of_twilight");
        addSoul("lifedrain_soul_stone",        "生命汲取之魂石",     "攻击时吸血",                                 TWILIGHT, "soul_of_twilight");
        addSoul("fortification_soul_stone",    "堡垒之魂石",         "防御加成",                                   TWILIGHT, "soul_of_twilight");
        addSoul("twilight_lich_soul_stone",    "暮色巫妖之魂石",     "巫妖联动效果",                               TWILIGHT, "soul_of_twilight");
        addSoul("minoshroom_soul_stone",       "米诺菇之魂石",       "穿甲·破坏盾牌",                              TWILIGHT, "soul_of_twilight");
        addSoul("twilight_hydra_soul_stone",   "暮色九头蛇之魂石",   "生命越低伤害与回血越高",                     TWILIGHT, "soul_of_twilight");
        addSoul("knight_phantom_soul_stone",   "骑士幽魂之魂石",     "护甲阈值额外伤害",                           TWILIGHT, "soul_of_twilight");
        addSoul("ur_ghast_soul_stone",         "恶魂之魂石",         "大额伤害减免",                               TWILIGHT, "soul_of_twilight");
        addSoul("alpha_yeti_soul_stone",       "雪怪首领之魂石",     "攻击概率冰冻",                               TWILIGHT, "soul_of_twilight");
        addSoul("snow_queen_soul_stone",       "雪后之魂石",         "攻击概率寒冰",                               TWILIGHT, "soul_of_twilight");
        addSoul("soul_of_twilightforest", "暮色森林之魂", "综合暮色材料联动效果，可整体开关", TWILIGHT, null);
        addSoul("ironwood_soul_stone",         "铁木之魂石",         "护甲与韧性加成",                             TWILIGHT, "soul_of_twilightforest");
        addSoul("steeleaf_soul_stone",         "钢叶之魂石",         "经验修补",                                   TWILIGHT, "soul_of_twilightforest");
        addSoul("fiery_iron_soul_stone",       "炽铁之魂石",         "攻击时点燃目标",                             TWILIGHT, "soul_of_twilightforest");
        addSoul("fluffy_cloud_soul_stone",     "绒云之魂石",         "离地时减伤",                                 TWILIGHT, "soul_of_twilightforest");
        addSoul("twilight_giant_soul_stone",   "暮色巨人之魂石",     "攻击与触及距离加成",                         TWILIGHT, "soul_of_twilightforest");
        addSoul("quest_ram_soul_stone",        "任务羊之魂石",       "魔法伤害减免",                               TWILIGHT, "soul_of_twilightforest");

        // ===== 莱特兰恶意 (L2Hostility) =====
        addSoul("soul_of_l2hostility", "莱特兰恶意之魂", "综合恶意联动效果，可整体开关", L2_HOSTILITY, null);
        addSoul("body_hostility_soul_stone",      "肉体恶意魂石",   "生命护甲加成·自适应减伤",                   L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("corrosion_hostility_soul_stone", "腐蚀恶意魂石",   "攻击施加多种负面效果",                     L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("resistance_hostility_soul_stone","抗性恶意魂石",   "火/魔/物/投射减伤",                        L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("aqua_hostility_soul_stone",      "水域恶意魂石",   "水域附近增伤与减伤",                       L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("zone_hostility_soul_stone",      "领域恶意魂石",   "阻止附近实体传送",                         L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("destroy_hostility_soul_stone",   "毁灭恶意魂石",   "根据目标debuff数增伤",                     L2_HOSTILITY, "soul_of_l2hostility");
        addSoul("ultra_hostility_soul_stone",     "极致恶意魂石",   "周期性斩杀·附魔权重增伤",                  L2_HOSTILITY, "soul_of_l2hostility");

        // ===== 莱特兰扩充 (L2Complements) =====
        addSoul("soul_of_l2_complements", "莱特兰扩充之魂", "综合扩充联动效果，可整体开关", L2_COMPLEMENTS, null);
        addSoul("totemic_complements_soul_stone",    "图腾扩充魂石",   "治疗·净化负面效果",                 L2_COMPLEMENTS, "soul_of_l2_complements");
        addSoul("poseidite_complements_soul_stone",  "海神石扩充魂石", "水中攻击力·对节肢增伤",             L2_COMPLEMENTS, "soul_of_l2_complements");
        addSoul("shulkerate_complements_soul_stone", "希尔扩充魂石",   "固定减伤·触及距离",                 L2_COMPLEMENTS, "soul_of_l2_complements");
        addSoul("sculkium_complements_soul_stone",   "幽匿扩充魂石",   "暗处增伤与减伤",                     L2_COMPLEMENTS, "soul_of_l2_complements");
        addSoul("eternium_complements_soul_stone",   "永恒扩充魂石",   "经验修补",                           L2_COMPLEMENTS, "soul_of_l2_complements");

        // ===== 神秘遗物 (EnigmaticLegacy) =====
        addSoul("soul_of_enigmatic_legacy", "神秘遗物之魂", "综合遗物联动效果，可整体开关", ENIGMATIC, null);
        addSoul("curses_soul_stone",    "诅咒之魂石",   "诅咒相关效果",                 ENIGMATIC, "soul_of_enigmatic_legacy");
        addSoul("abyss_soul_stone",     "深渊之魂石",   "击杀计数增伤·减速敌人",        ENIGMATIC, "soul_of_enigmatic_legacy");
        addSoul("etherium_soul_stone",  "以太之魂石",   "固定减伤·生命上限",            ENIGMATIC, "soul_of_enigmatic_legacy");

        // ===== 诡厄巫法·物品 (Goety Item) =====
        addSoul("soul_of_goety_item", "诡厄巫法·物之魂", "综合巫法物品联动效果，可整体开关", GOETY_ITEM, null);
        addSoul("ectoplasm_soul_stone",      "灵质之魂石",     "击杀时额外掉落",                 GOETY_ITEM, "soul_of_goety_item");
        addSoul("goety_focus_soul_stone",    "巫法焦点之魂石", "法术强度加成",                   GOETY_ITEM, "soul_of_goety_item");
        addSoul("order_about_soul_stone",    "差遣之魂石",     "攻击时令随从攻击目标",           GOETY_ITEM, "soul_of_goety_item");
        addSoul("escort_soul_stone",         "护卫之魂石",     "随从受伤减免",                   GOETY_ITEM, "soul_of_goety_item");
        addSoul("brew_soul_stone",           "酿造之魂石",     "饮用时触发增益",                 GOETY_ITEM, "soul_of_goety_item");
        addSoul("legion_soul_stone",         "军团之魂石",     "随从增强",                       GOETY_ITEM, "soul_of_goety_item");
        addSoul("goety_dark_soul_stone",     "暗黑之魂石",     "火/爆/魔减伤",                   GOETY_ITEM, "soul_of_goety_item");

        // ===== 诡厄巫法·实体 (Goety Entity) =====
        addSoul("soul_of_goety_entity", "诡厄巫法·实体之魂", "综合巫法实体联动效果，可整体开关", GOETY_ENTITY, null);
        addSoul("apostle_soul_stone",               "使徒之魂石",         "魔法减伤·生命阈值回血",          GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("nether_apostle_soul_stone",        "下界使徒之魂石",     "净化负面·燃烧诅咒",              GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("vizier_soul_stone",                "维齐尔之魂石",       "死亡相关效果",                   GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("redstone_monstrosity_soul_stone",  "红石巨兽之魂石",     "生命上限·百分比固定伤害",        GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("ender_keeper_soul_stone",          "末影守卫之魂石",     "虚空之触·近距离减伤",            GOETY_ENTITY, "soul_of_goety_entity");
        addSoul("minister_soul_stone",              "牧师之魂石",         "友方增益·敌方削弱",             GOETY_ENTITY, "soul_of_goety_entity");

        // ===== 冰火传说·龙 (Ice and Fire - Dragon) =====
        addSoul("soul_of_iaf_dragon", "冰火龙之魂", "综合龙类联动效果，可整体开关", IAF_DRAGON, null);
        addSoul("dragon_bone_soul_stone",              "龙骨之魂石",       "对龙增伤·对龙减伤",              IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("fire_dragon_blood_soul_stone",        "火龙血之魂石",     "燃烧增伤·冰龙克制",              IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("ice_dragon_blood_soul_stone",         "冰龙血之魂石",     "冰冻效果·火龙克制",              IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("lightning_dragon_blood_soul_stone",   "雷龙血之魂石",     "雷/冰/火龙克制",                 IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("fire_dragon_steel_soul_stone",        "火龙钢之魂石",     "对龙减伤·护甲",                  IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("ice_dragon_steel_soul_stone",         "冰龙钢之魂石",     "对龙减伤·护甲",                  IAF_DRAGON, "soul_of_iaf_dragon");
        addSoul("lightning_dragon_steel_soul_stone",   "雷龙钢之魂石",     "对龙减伤·护甲",                  IAF_DRAGON, "soul_of_iaf_dragon");

        // ===== 冰火传说·实体 (Ice and Fire - Entity) =====
        addSoul("soul_of_iaf_entity", "冰火实体之魂", "综合冰火实体联动效果，可整体开关", IAF_ENTITY, null);
        addSoul("cyclops_soul_stone",       "独眼巨人之魂石",     "附近敌人虚弱",                  IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("gorgon_soul_stone",        "戈尔贡之魂石",       "减速最近敌人",                  IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("iaf_hydra_soul_stone",     "冰火九头蛇之魂石",   "生命越低回血越多",              IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("iaf_siren_soul_stone",     "冰海妖之魂石",       "海妖相关效果",                  IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("sea_serpent_soul_stone",   "海蛇之魂石",         "水中伤害加成",                  IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("troll_soul_stone",         "巨魔之魂石",         "投射物减伤",                    IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("hippocampus_soul_stone",   "马头鱼之魂石",       "游泳速度",                      IAF_ENTITY, "soul_of_iaf_entity");
        addSoul("death_worm_soul_stone",    "死亡蠕虫之魂石",     "触及距离加成",                  IAF_ENTITY, "soul_of_iaf_entity");

        // ===== 七罪之子 (Sons of Sins) =====
        addSoul("soul_of_sons_of_sins", "七罪之子之魂", "综合七罪联动效果，可整体开关", SONS_OF_SINS, null);
        addSoul("envy_sin_soul_stone",      "嫉妒之罪魂石",   "攻速移速加成·额外伤害",         SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("gluttony_sin_soul_stone",  "暴食之罪魂石",   "击杀计数增伤·额外掉落",         SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("greed_sin_soul_stone",     "贪婪之罪魂石",   "免疫窒息·投射减伤",             SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("lust_sin_soul_stone",      "色欲之罪魂石",   "穿甲·投射减伤",                 SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("pride_sin_soul_stone",     "傲慢之罪魂石",   "生命比例增伤",                   SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("sloth_sin_soul_stone",     "怠惰之罪魂石",   "减速目标·生命加成",             SONS_OF_SINS, "soul_of_sons_of_sins");
        addSoul("wrath_sin_soul_stone",     "愤怒之罪魂石",   "额外伤害",                       SONS_OF_SINS, "soul_of_sons_of_sins");

        // ===== 农夫乐事 (Farmer's Delight) =====
        addSoul("farmers_delight_soul_stone", "农夫乐事之魂石", "食用时概率触发饱和与急迫", FARMERS_DELIGHT, null);
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
        List<FeatureDef> out = new ArrayList<>();
        for (FeatureDef f : FEATURES) {
            for (String p : f.providers) {
                if (equippedIds.contains(p)) { out.add(f); break; }
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
