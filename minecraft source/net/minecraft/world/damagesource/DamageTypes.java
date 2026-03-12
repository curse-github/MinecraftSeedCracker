/*     */ package net.minecraft.world.damagesource;
/*     */ 
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public interface DamageTypes
/*     */ {
/*  10 */   public static final ResourceKey<DamageType> IN_FIRE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("in_fire"));
/*  11 */   public static final ResourceKey<DamageType> CAMPFIRE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("campfire"));
/*  12 */   public static final ResourceKey<DamageType> LIGHTNING_BOLT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("lightning_bolt"));
/*  13 */   public static final ResourceKey<DamageType> ON_FIRE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("on_fire"));
/*  14 */   public static final ResourceKey<DamageType> LAVA = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("lava"));
/*  15 */   public static final ResourceKey<DamageType> HOT_FLOOR = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("hot_floor"));
/*  16 */   public static final ResourceKey<DamageType> IN_WALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("in_wall"));
/*  17 */   public static final ResourceKey<DamageType> CRAMMING = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("cramming"));
/*  18 */   public static final ResourceKey<DamageType> DROWN = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("drown"));
/*  19 */   public static final ResourceKey<DamageType> STARVE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("starve"));
/*  20 */   public static final ResourceKey<DamageType> CACTUS = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("cactus"));
/*  21 */   public static final ResourceKey<DamageType> FALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("fall"));
/*  22 */   public static final ResourceKey<DamageType> ENDER_PEARL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("ender_pearl"));
/*  23 */   public static final ResourceKey<DamageType> FLY_INTO_WALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("fly_into_wall"));
/*  24 */   public static final ResourceKey<DamageType> FELL_OUT_OF_WORLD = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("out_of_world"));
/*  25 */   public static final ResourceKey<DamageType> GENERIC = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("generic"));
/*  26 */   public static final ResourceKey<DamageType> MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("magic"));
/*  27 */   public static final ResourceKey<DamageType> WITHER = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("wither"));
/*  28 */   public static final ResourceKey<DamageType> DRAGON_BREATH = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("dragon_breath"));
/*  29 */   public static final ResourceKey<DamageType> DRY_OUT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("dry_out"));
/*  30 */   public static final ResourceKey<DamageType> SWEET_BERRY_BUSH = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("sweet_berry_bush"));
/*  31 */   public static final ResourceKey<DamageType> FREEZE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("freeze"));
/*  32 */   public static final ResourceKey<DamageType> STALAGMITE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("stalagmite"));
/*  33 */   public static final ResourceKey<DamageType> FALLING_BLOCK = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("falling_block"));
/*  34 */   public static final ResourceKey<DamageType> FALLING_ANVIL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("falling_anvil"));
/*  35 */   public static final ResourceKey<DamageType> FALLING_STALACTITE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("falling_stalactite"));
/*  36 */   public static final ResourceKey<DamageType> STING = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("sting"));
/*  37 */   public static final ResourceKey<DamageType> MOB_ATTACK = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("mob_attack"));
/*  38 */   public static final ResourceKey<DamageType> MOB_ATTACK_NO_AGGRO = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("mob_attack_no_aggro"));
/*  39 */   public static final ResourceKey<DamageType> PLAYER_ATTACK = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("player_attack"));
/*  40 */   public static final ResourceKey<DamageType> SPEAR = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("spear"));
/*  41 */   public static final ResourceKey<DamageType> ARROW = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("arrow"));
/*  42 */   public static final ResourceKey<DamageType> TRIDENT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("trident"));
/*  43 */   public static final ResourceKey<DamageType> MOB_PROJECTILE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("mob_projectile"));
/*  44 */   public static final ResourceKey<DamageType> SPIT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("spit"));
/*  45 */   public static final ResourceKey<DamageType> WIND_CHARGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("wind_charge"));
/*  46 */   public static final ResourceKey<DamageType> FIREWORKS = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("fireworks"));
/*  47 */   public static final ResourceKey<DamageType> FIREBALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("fireball"));
/*  48 */   public static final ResourceKey<DamageType> UNATTRIBUTED_FIREBALL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("unattributed_fireball"));
/*  49 */   public static final ResourceKey<DamageType> WITHER_SKULL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("wither_skull"));
/*  50 */   public static final ResourceKey<DamageType> THROWN = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("thrown"));
/*  51 */   public static final ResourceKey<DamageType> INDIRECT_MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("indirect_magic"));
/*  52 */   public static final ResourceKey<DamageType> THORNS = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("thorns"));
/*  53 */   public static final ResourceKey<DamageType> EXPLOSION = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("explosion"));
/*  54 */   public static final ResourceKey<DamageType> PLAYER_EXPLOSION = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("player_explosion"));
/*  55 */   public static final ResourceKey<DamageType> SONIC_BOOM = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("sonic_boom"));
/*  56 */   public static final ResourceKey<DamageType> BAD_RESPAWN_POINT = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("bad_respawn_point"));
/*  57 */   public static final ResourceKey<DamageType> OUTSIDE_BORDER = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("outside_border"));
/*  58 */   public static final ResourceKey<DamageType> GENERIC_KILL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("generic_kill"));
/*  59 */   public static final ResourceKey<DamageType> MACE_SMASH = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("mace_smash"));
/*     */   
/*     */   static void bootstrap(BootstrapContext<DamageType> context) {
/*  62 */     context.register(IN_FIRE, new DamageType("inFire", 0.1F, DamageEffects.BURNING));
/*  63 */     context.register(CAMPFIRE, new DamageType("inFire", 0.1F, DamageEffects.BURNING));
/*  64 */     context.register(LIGHTNING_BOLT, new DamageType("lightningBolt", 0.1F));
/*  65 */     context.register(ON_FIRE, new DamageType("onFire", 0.0F, DamageEffects.BURNING));
/*  66 */     context.register(LAVA, new DamageType("lava", 0.1F, DamageEffects.BURNING));
/*  67 */     context.register(HOT_FLOOR, new DamageType("hotFloor", 0.1F, DamageEffects.BURNING));
/*  68 */     context.register(IN_WALL, new DamageType("inWall", 0.0F));
/*  69 */     context.register(CRAMMING, new DamageType("cramming", 0.0F));
/*  70 */     context.register(DROWN, new DamageType("drown", 0.0F, DamageEffects.DROWNING));
/*  71 */     context.register(STARVE, new DamageType("starve", 0.0F));
/*  72 */     context.register(CACTUS, new DamageType("cactus", 0.1F));
/*  73 */     context.register(FALL, new DamageType("fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS));
/*  74 */     context.register(ENDER_PEARL, new DamageType("fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS));
/*  75 */     context.register(FLY_INTO_WALL, new DamageType("flyIntoWall", 0.0F));
/*  76 */     context.register(FELL_OUT_OF_WORLD, new DamageType("outOfWorld", 0.0F));
/*  77 */     context.register(GENERIC, new DamageType("generic", 0.0F));
/*  78 */     context.register(MAGIC, new DamageType("magic", 0.0F));
/*  79 */     context.register(WITHER, new DamageType("wither", 0.0F));
/*  80 */     context.register(DRAGON_BREATH, new DamageType("dragonBreath", 0.0F));
/*  81 */     context.register(DRY_OUT, new DamageType("dryout", 0.1F));
/*  82 */     context.register(SWEET_BERRY_BUSH, new DamageType("sweetBerryBush", 0.1F, DamageEffects.POKING));
/*  83 */     context.register(FREEZE, new DamageType("freeze", 0.0F, DamageEffects.FREEZING));
/*  84 */     context.register(STALAGMITE, new DamageType("stalagmite", 0.0F));
/*  85 */     context.register(FALLING_BLOCK, new DamageType("fallingBlock", 0.1F));
/*  86 */     context.register(FALLING_ANVIL, new DamageType("anvil", 0.1F));
/*  87 */     context.register(FALLING_STALACTITE, new DamageType("fallingStalactite", 0.1F));
/*  88 */     context.register(STING, new DamageType("sting", 0.1F));
/*  89 */     context.register(MOB_ATTACK, new DamageType("mob", 0.1F));
/*  90 */     context.register(MOB_ATTACK_NO_AGGRO, new DamageType("mob", 0.1F));
/*  91 */     context.register(PLAYER_ATTACK, new DamageType("player", 0.1F));
/*  92 */     context.register(SPEAR, new DamageType("spear", 0.1F));
/*  93 */     context.register(ARROW, new DamageType("arrow", 0.1F));
/*  94 */     context.register(TRIDENT, new DamageType("trident", 0.1F));
/*  95 */     context.register(MOB_PROJECTILE, new DamageType("mob", 0.1F));
/*  96 */     context.register(SPIT, new DamageType("mob", 0.1F));
/*  97 */     context.register(FIREWORKS, new DamageType("fireworks", 0.1F));
/*  98 */     context.register(UNATTRIBUTED_FIREBALL, new DamageType("onFire", 0.1F, DamageEffects.BURNING));
/*  99 */     context.register(FIREBALL, new DamageType("fireball", 0.1F, DamageEffects.BURNING));
/* 100 */     context.register(WITHER_SKULL, new DamageType("witherSkull", 0.1F));
/* 101 */     context.register(THROWN, new DamageType("thrown", 0.1F));
/* 102 */     context.register(INDIRECT_MAGIC, new DamageType("indirectMagic", 0.0F));
/* 103 */     context.register(THORNS, new DamageType("thorns", 0.1F, DamageEffects.THORNS));
/* 104 */     context.register(EXPLOSION, new DamageType("explosion", DamageScaling.ALWAYS, 0.1F));
/* 105 */     context.register(PLAYER_EXPLOSION, new DamageType("explosion.player", DamageScaling.ALWAYS, 0.1F));
/* 106 */     context.register(SONIC_BOOM, new DamageType("sonic_boom", DamageScaling.ALWAYS, 0.0F));
/* 107 */     context.register(BAD_RESPAWN_POINT, new DamageType("badRespawnPoint", DamageScaling.ALWAYS, 0.1F, DamageEffects.HURT, DeathMessageType.INTENTIONAL_GAME_DESIGN));
/* 108 */     context.register(OUTSIDE_BORDER, new DamageType("outsideBorder", 0.0F));
/* 109 */     context.register(GENERIC_KILL, new DamageType("genericKill", 0.0F));
/* 110 */     context.register(WIND_CHARGE, new DamageType("mob", 0.1F));
/* 111 */     context.register(MACE_SMASH, new DamageType("mace_smash", 0.1F));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\damagesource\DamageTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */