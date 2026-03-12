/*     */ package net.minecraft.core.particles;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public class ParticleTypes
/*     */ {
/*  15 */   public static final SimpleParticleType ANGRY_VILLAGER = register("angry_villager", false);
/*  16 */   public static final ParticleType<BlockParticleOption> BLOCK = register("block", false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
/*  17 */   public static final ParticleType<BlockParticleOption> BLOCK_MARKER = register("block_marker", true, BlockParticleOption::codec, BlockParticleOption::streamCodec);
/*  18 */   public static final SimpleParticleType BUBBLE = register("bubble", false);
/*  19 */   public static final SimpleParticleType CLOUD = register("cloud", false);
/*  20 */   public static final SimpleParticleType COPPER_FIRE_FLAME = register("copper_fire_flame", false);
/*  21 */   public static final SimpleParticleType CRIT = register("crit", false);
/*  22 */   public static final SimpleParticleType DAMAGE_INDICATOR = register("damage_indicator", true);
/*  23 */   public static final ParticleType<PowerParticleOption> DRAGON_BREATH = register("dragon_breath", false, PowerParticleOption::codec, PowerParticleOption::streamCodec);
/*  24 */   public static final SimpleParticleType DRIPPING_LAVA = register("dripping_lava", false);
/*  25 */   public static final SimpleParticleType FALLING_LAVA = register("falling_lava", false);
/*  26 */   public static final SimpleParticleType LANDING_LAVA = register("landing_lava", false);
/*  27 */   public static final SimpleParticleType DRIPPING_WATER = register("dripping_water", false);
/*  28 */   public static final SimpleParticleType FALLING_WATER = register("falling_water", false);
/*  29 */   public static final ParticleType<DustParticleOptions> DUST = register("dust", false, t -> DustParticleOptions.CODEC, t -> DustParticleOptions.STREAM_CODEC);
/*  30 */   public static final ParticleType<DustColorTransitionOptions> DUST_COLOR_TRANSITION = register("dust_color_transition", false, t -> DustColorTransitionOptions.CODEC, t -> DustColorTransitionOptions.STREAM_CODEC);
/*  31 */   public static final ParticleType<SpellParticleOption> EFFECT = register("effect", false, SpellParticleOption::codec, SpellParticleOption::streamCodec);
/*  32 */   public static final SimpleParticleType ELDER_GUARDIAN = register("elder_guardian", true);
/*  33 */   public static final SimpleParticleType ENCHANTED_HIT = register("enchanted_hit", false);
/*  34 */   public static final SimpleParticleType ENCHANT = register("enchant", false);
/*  35 */   public static final SimpleParticleType END_ROD = register("end_rod", false);
/*  36 */   public static final ParticleType<ColorParticleOption> ENTITY_EFFECT = register("entity_effect", false, ColorParticleOption::codec, ColorParticleOption::streamCodec);
/*  37 */   public static final SimpleParticleType EXPLOSION_EMITTER = register("explosion_emitter", true);
/*  38 */   public static final SimpleParticleType EXPLOSION = register("explosion", true);
/*  39 */   public static final SimpleParticleType GUST = register("gust", true);
/*  40 */   public static final SimpleParticleType SMALL_GUST = register("small_gust", false);
/*  41 */   public static final SimpleParticleType GUST_EMITTER_LARGE = register("gust_emitter_large", true);
/*  42 */   public static final SimpleParticleType GUST_EMITTER_SMALL = register("gust_emitter_small", true);
/*  43 */   public static final SimpleParticleType SONIC_BOOM = register("sonic_boom", true);
/*  44 */   public static final ParticleType<BlockParticleOption> FALLING_DUST = register("falling_dust", false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
/*  45 */   public static final SimpleParticleType FIREWORK = register("firework", false);
/*  46 */   public static final SimpleParticleType FISHING = register("fishing", false);
/*  47 */   public static final SimpleParticleType FLAME = register("flame", false);
/*  48 */   public static final SimpleParticleType INFESTED = register("infested", false);
/*  49 */   public static final SimpleParticleType CHERRY_LEAVES = register("cherry_leaves", false);
/*  50 */   public static final SimpleParticleType PALE_OAK_LEAVES = register("pale_oak_leaves", false);
/*  51 */   public static final ParticleType<ColorParticleOption> TINTED_LEAVES = register("tinted_leaves", false, ColorParticleOption::codec, ColorParticleOption::streamCodec);
/*  52 */   public static final SimpleParticleType SCULK_SOUL = register("sculk_soul", false);
/*  53 */   public static final ParticleType<SculkChargeParticleOptions> SCULK_CHARGE = register("sculk_charge", true, t -> SculkChargeParticleOptions.CODEC, t -> SculkChargeParticleOptions.STREAM_CODEC);
/*  54 */   public static final SimpleParticleType SCULK_CHARGE_POP = register("sculk_charge_pop", true);
/*  55 */   public static final SimpleParticleType SOUL_FIRE_FLAME = register("soul_fire_flame", false);
/*  56 */   public static final SimpleParticleType SOUL = register("soul", false);
/*  57 */   public static final ParticleType<ColorParticleOption> FLASH = register("flash", false, ColorParticleOption::codec, ColorParticleOption::streamCodec);
/*  58 */   public static final SimpleParticleType HAPPY_VILLAGER = register("happy_villager", false);
/*  59 */   public static final SimpleParticleType COMPOSTER = register("composter", false);
/*  60 */   public static final SimpleParticleType HEART = register("heart", false);
/*  61 */   public static final ParticleType<SpellParticleOption> INSTANT_EFFECT = register("instant_effect", false, SpellParticleOption::codec, SpellParticleOption::streamCodec);
/*  62 */   public static final ParticleType<ItemParticleOption> ITEM = register("item", false, ItemParticleOption::codec, ItemParticleOption::streamCodec);
/*  63 */   public static final ParticleType<VibrationParticleOption> VIBRATION = register("vibration", true, t -> VibrationParticleOption.CODEC, t -> VibrationParticleOption.STREAM_CODEC);
/*  64 */   public static final ParticleType<TrailParticleOption> TRAIL = register("trail", false, t -> TrailParticleOption.CODEC, t -> TrailParticleOption.STREAM_CODEC);
/*     */   
/*  66 */   public static final SimpleParticleType ITEM_SLIME = register("item_slime", false);
/*  67 */   public static final SimpleParticleType ITEM_COBWEB = register("item_cobweb", false);
/*  68 */   public static final SimpleParticleType ITEM_SNOWBALL = register("item_snowball", false);
/*  69 */   public static final SimpleParticleType LARGE_SMOKE = register("large_smoke", false);
/*  70 */   public static final SimpleParticleType LAVA = register("lava", false);
/*  71 */   public static final SimpleParticleType MYCELIUM = register("mycelium", false);
/*  72 */   public static final SimpleParticleType NOTE = register("note", false);
/*  73 */   public static final SimpleParticleType POOF = register("poof", true);
/*  74 */   public static final SimpleParticleType PORTAL = register("portal", false);
/*  75 */   public static final SimpleParticleType RAIN = register("rain", false);
/*  76 */   public static final SimpleParticleType SMOKE = register("smoke", false);
/*  77 */   public static final SimpleParticleType WHITE_SMOKE = register("white_smoke", false);
/*  78 */   public static final SimpleParticleType SNEEZE = register("sneeze", false);
/*  79 */   public static final SimpleParticleType SPIT = register("spit", true);
/*  80 */   public static final SimpleParticleType SQUID_INK = register("squid_ink", true);
/*  81 */   public static final SimpleParticleType SWEEP_ATTACK = register("sweep_attack", true);
/*  82 */   public static final SimpleParticleType TOTEM_OF_UNDYING = register("totem_of_undying", false);
/*     */   
/*  84 */   public static final SimpleParticleType UNDERWATER = register("underwater", false);
/*  85 */   public static final SimpleParticleType SPLASH = register("splash", false);
/*  86 */   public static final SimpleParticleType WITCH = register("witch", false);
/*  87 */   public static final SimpleParticleType BUBBLE_POP = register("bubble_pop", false);
/*  88 */   public static final SimpleParticleType CURRENT_DOWN = register("current_down", false);
/*  89 */   public static final SimpleParticleType BUBBLE_COLUMN_UP = register("bubble_column_up", false);
/*  90 */   public static final SimpleParticleType NAUTILUS = register("nautilus", false);
/*  91 */   public static final SimpleParticleType DOLPHIN = register("dolphin", false);
/*     */   
/*  93 */   public static final SimpleParticleType CAMPFIRE_COSY_SMOKE = register("campfire_cosy_smoke", true);
/*  94 */   public static final SimpleParticleType CAMPFIRE_SIGNAL_SMOKE = register("campfire_signal_smoke", true);
/*     */   
/*  96 */   public static final SimpleParticleType DRIPPING_HONEY = register("dripping_honey", false);
/*  97 */   public static final SimpleParticleType FALLING_HONEY = register("falling_honey", false);
/*  98 */   public static final SimpleParticleType LANDING_HONEY = register("landing_honey", false);
/*  99 */   public static final SimpleParticleType FALLING_NECTAR = register("falling_nectar", false);
/* 100 */   public static final SimpleParticleType FALLING_SPORE_BLOSSOM = register("falling_spore_blossom", false);
/*     */   
/* 102 */   public static final SimpleParticleType ASH = register("ash", false);
/* 103 */   public static final SimpleParticleType CRIMSON_SPORE = register("crimson_spore", false);
/* 104 */   public static final SimpleParticleType WARPED_SPORE = register("warped_spore", false);
/* 105 */   public static final SimpleParticleType SPORE_BLOSSOM_AIR = register("spore_blossom_air", false);
/* 106 */   public static final SimpleParticleType DRIPPING_OBSIDIAN_TEAR = register("dripping_obsidian_tear", false);
/* 107 */   public static final SimpleParticleType FALLING_OBSIDIAN_TEAR = register("falling_obsidian_tear", false);
/* 108 */   public static final SimpleParticleType LANDING_OBSIDIAN_TEAR = register("landing_obsidian_tear", false);
/*     */   
/* 110 */   public static final SimpleParticleType REVERSE_PORTAL = register("reverse_portal", false);
/*     */   
/* 112 */   public static final SimpleParticleType WHITE_ASH = register("white_ash", false);
/* 113 */   public static final SimpleParticleType SMALL_FLAME = register("small_flame", false);
/* 114 */   public static final SimpleParticleType SNOWFLAKE = register("snowflake", false);
/*     */   
/* 116 */   public static final SimpleParticleType DRIPPING_DRIPSTONE_LAVA = register("dripping_dripstone_lava", false);
/* 117 */   public static final SimpleParticleType FALLING_DRIPSTONE_LAVA = register("falling_dripstone_lava", false);
/*     */   
/* 119 */   public static final SimpleParticleType DRIPPING_DRIPSTONE_WATER = register("dripping_dripstone_water", false);
/* 120 */   public static final SimpleParticleType FALLING_DRIPSTONE_WATER = register("falling_dripstone_water", false);
/* 121 */   public static final SimpleParticleType GLOW_SQUID_INK = register("glow_squid_ink", true);
/* 122 */   public static final SimpleParticleType GLOW = register("glow", true);
/* 123 */   public static final SimpleParticleType WAX_ON = register("wax_on", true);
/* 124 */   public static final SimpleParticleType WAX_OFF = register("wax_off", true);
/* 125 */   public static final SimpleParticleType ELECTRIC_SPARK = register("electric_spark", true);
/* 126 */   public static final SimpleParticleType SCRAPE = register("scrape", true);
/* 127 */   public static final ParticleType<ShriekParticleOption> SHRIEK = register("shriek", false, t -> ShriekParticleOption.CODEC, t -> ShriekParticleOption.STREAM_CODEC);
/* 128 */   public static final SimpleParticleType EGG_CRACK = register("egg_crack", false);
/* 129 */   public static final SimpleParticleType DUST_PLUME = register("dust_plume", false);
/* 130 */   public static final SimpleParticleType TRIAL_SPAWNER_DETECTED_PLAYER = register("trial_spawner_detection", true);
/* 131 */   public static final SimpleParticleType TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS = register("trial_spawner_detection_ominous", true);
/* 132 */   public static final SimpleParticleType VAULT_CONNECTION = register("vault_connection", true);
/* 133 */   public static final ParticleType<BlockParticleOption> DUST_PILLAR = register("dust_pillar", false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
/* 134 */   public static final SimpleParticleType OMINOUS_SPAWNING = register("ominous_spawning", true);
/* 135 */   public static final SimpleParticleType RAID_OMEN = register("raid_omen", false);
/* 136 */   public static final SimpleParticleType TRIAL_OMEN = register("trial_omen", false);
/* 137 */   public static final ParticleType<BlockParticleOption> BLOCK_CRUMBLE = register("block_crumble", false, BlockParticleOption::codec, BlockParticleOption::streamCodec);
/* 138 */   public static final SimpleParticleType FIREFLY = register("firefly", false);
/*     */ 
/*     */   
/* 141 */   private static SimpleParticleType register(String name, boolean overrideLimiter) { return (SimpleParticleType)Registry.register(BuiltInRegistries.PARTICLE_TYPE, name, new SimpleParticleType(overrideLimiter)); }
/*     */ 
/*     */   
/*     */   private static <T extends ParticleOptions> ParticleType<T> register(String name, boolean overrideLimiter, final Function<ParticleType<T>, MapCodec<T>> codec, final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodec) {
/* 145 */     return (ParticleType)Registry.register(BuiltInRegistries.PARTICLE_TYPE, name, new ParticleType<T>(overrideLimiter)
/*     */         {
/*     */           public MapCodec<T> codec() {
/* 148 */             return (MapCodec)codec.apply(this);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 153 */           public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() { return (StreamCodec)streamCodec.apply(this); }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 158 */   public static final Codec<ParticleOptions> CODEC = BuiltInRegistries.PARTICLE_TYPE.byNameCodec().dispatch("type", ParticleOptions::getType, ParticleType::codec);
/*     */   
/* 160 */   public static final StreamCodec<RegistryFriendlyByteBuf, ParticleOptions> STREAM_CODEC = ByteBufCodecs.registry(Registries.PARTICLE_TYPE).dispatch(ParticleOptions::getType, ParticleType::streamCodec);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\ParticleTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */