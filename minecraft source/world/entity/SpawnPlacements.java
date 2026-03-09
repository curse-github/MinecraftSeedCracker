/*     */ package net.minecraft.world.entity;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.ambient.Bat;
/*     */ import net.minecraft.world.entity.animal.AgeableWaterCreature;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.armadillo.Armadillo;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.camel.Camel;
/*     */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*     */ import net.minecraft.world.entity.animal.equine.SkeletonHorse;
/*     */ import net.minecraft.world.entity.animal.feline.Ocelot;
/*     */ import net.minecraft.world.entity.animal.fish.TropicalFish;
/*     */ import net.minecraft.world.entity.animal.fish.WaterAnimal;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.animal.frog.Frog;
/*     */ import net.minecraft.world.entity.animal.goat.Goat;
/*     */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*     */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*     */ import net.minecraft.world.entity.animal.polarbear.PolarBear;
/*     */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*     */ import net.minecraft.world.entity.animal.squid.GlowSquid;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.entity.monster.MagmaCube;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.PatrollingMonster;
/*     */ import net.minecraft.world.entity.monster.Silverfish;
/*     */ import net.minecraft.world.entity.monster.Slime;
/*     */ import net.minecraft.world.entity.monster.Strider;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.skeleton.Stray;
/*     */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ public class SpawnPlacements {
/*     */   private static final class Data extends Record {
/*     */     private final Heightmap.Types heightMap;
/*     */     private final SpawnPlacementType placement;
/*     */     private final SpawnPlacements.SpawnPredicate<?> predicate;
/*     */     
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SpawnPlacements$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #55	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/SpawnPlacements$Data; }
/*     */     
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SpawnPlacements$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #55	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/SpawnPlacements$Data; }
/*     */     
/*  55 */     private Data(Heightmap.Types heightMap, SpawnPlacementType placement, SpawnPlacements.SpawnPredicate<?> predicate) { this.heightMap = heightMap; this.placement = placement; this.predicate = predicate; } public Heightmap.Types heightMap() { return this.heightMap; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SpawnPlacements$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #55	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/SpawnPlacements$Data;
/*  55 */       //   0	8	1	o	Ljava/lang/Object; } public SpawnPlacementType placement() { return this.placement; } public SpawnPlacements.SpawnPredicate<?> predicate() { return this.predicate; }
/*     */   }
/*  57 */   private static final Map<EntityType<?>, Data> DATA_BY_TYPE = Maps.newHashMap();
/*     */   
/*     */   private static <T extends Mob> void register(EntityType<T> type, SpawnPlacementType placementType, Heightmap.Types heightmap, SpawnPredicate<T> spawnPredicate) {
/*  60 */     Data previous = (Data)DATA_BY_TYPE.put(type, new Data(heightmap, placementType, spawnPredicate));
/*  61 */     if (previous != null) {
/*  62 */       throw new IllegalStateException("Duplicate registration for type " + String.valueOf(BuiltInRegistries.ENTITY_TYPE.getKey(type)));
/*     */     }
/*     */   }
/*     */   
/*     */   static  {
/*  67 */     register(EntityType.AXOLOTL, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Axolotl::checkAxolotlSpawnRules);
/*  68 */     register(EntityType.COD, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
/*  69 */     register(EntityType.DOLPHIN, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableWaterCreature::checkSurfaceAgeableWaterCreatureSpawnRules);
/*  70 */     register(EntityType.DROWNED, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Drowned::checkDrownedSpawnRules);
/*  71 */     register(EntityType.GUARDIAN, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Guardian::checkGuardianSpawnRules);
/*  72 */     register(EntityType.PUFFERFISH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
/*  73 */     register(EntityType.SALMON, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
/*  74 */     register(EntityType.SQUID, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AgeableWaterCreature::checkSurfaceAgeableWaterCreatureSpawnRules);
/*  75 */     register(EntityType.TROPICAL_FISH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TropicalFish::checkTropicalFishSpawnRules);
/*     */     
/*  77 */     register(EntityType.ARMADILLO, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Armadillo::checkArmadilloSpawnRules);
/*  78 */     register(EntityType.BAT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Bat::checkBatSpawnRules);
/*  79 */     register(EntityType.BLAZE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
/*  80 */     register(EntityType.BOGGED, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  81 */     register(EntityType.BREEZE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
/*  82 */     register(EntityType.CAMEL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Camel::checkCamelSpawnRules);
/*  83 */     register(EntityType.CAMEL_HUSK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkSurfaceMonstersSpawnRules);
/*  84 */     register(EntityType.CAVE_SPIDER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  85 */     register(EntityType.CHICKEN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  86 */     register(EntityType.COW, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  87 */     register(EntityType.CREEPER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  88 */     register(EntityType.DONKEY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  89 */     register(EntityType.ENDERMAN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  90 */     register(EntityType.ENDERMITE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Endermite::checkEndermiteSpawnRules);
/*  91 */     register(EntityType.ENDER_DRAGON, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/*  92 */     register(EntityType.FROG, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Frog::checkFrogSpawnRules);
/*  93 */     register(EntityType.GHAST, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Ghast::checkGhastSpawnRules);
/*  94 */     register(EntityType.HAPPY_GHAST, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  95 */     register(EntityType.GIANT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*  96 */     register(EntityType.GLOW_SQUID, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GlowSquid::checkGlowSquidSpawnRules);
/*  97 */     register(EntityType.GOAT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Goat::checkGoatSpawnRules);
/*  98 */     register(EntityType.HORSE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/*  99 */     register(EntityType.HUSK, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkSurfaceMonstersSpawnRules);
/* 100 */     register(EntityType.IRON_GOLEM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 101 */     register(EntityType.LLAMA, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 102 */     register(EntityType.MAGMA_CUBE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MagmaCube::checkMagmaCubeSpawnRules);
/* 103 */     register(EntityType.MOOSHROOM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MushroomCow::checkMushroomSpawnRules);
/* 104 */     register(EntityType.MULE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 105 */     register(EntityType.NAUTILUS, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractNautilus::checkNautilusSpawnRules);
/* 106 */     register(EntityType.OCELOT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Ocelot::checkOcelotSpawnRules);
/* 107 */     register(EntityType.PARROT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Parrot::checkParrotSpawnRules);
/* 108 */     register(EntityType.PIG, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 109 */     register(EntityType.HOGLIN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Hoglin::checkHoglinSpawnRules);
/* 110 */     register(EntityType.PIGLIN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Piglin::checkPiglinSpawnRules);
/* 111 */     register(EntityType.PILLAGER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PatrollingMonster::checkPatrollingMonsterSpawnRules);
/* 112 */     register(EntityType.POLAR_BEAR, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PolarBear::checkPolarBearSpawnRules);
/* 113 */     register(EntityType.RABBIT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Rabbit::checkRabbitSpawnRules);
/* 114 */     register(EntityType.SHEEP, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 115 */     register(EntityType.SILVERFISH, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Silverfish::checkSilverfishSpawnRules);
/* 116 */     register(EntityType.SKELETON, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 117 */     register(EntityType.SKELETON_HORSE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SkeletonHorse::checkSkeletonHorseSpawnRules);
/* 118 */     register(EntityType.SLIME, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Slime::checkSlimeSpawnRules);
/* 119 */     register(EntityType.SNOW_GOLEM, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 120 */     register(EntityType.SPIDER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 121 */     register(EntityType.STRAY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Stray::checkStraySpawnRules);
/* 122 */     register(EntityType.PARCHED, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkSurfaceMonstersSpawnRules);
/* 123 */     register(EntityType.STRIDER, SpawnPlacementTypes.IN_LAVA, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Strider::checkStriderSpawnRules);
/* 124 */     register(EntityType.TURTLE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Turtle::checkTurtleSpawnRules);
/* 125 */     register(EntityType.VILLAGER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 126 */     register(EntityType.WITCH, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 127 */     register(EntityType.WITHER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 128 */     register(EntityType.WITHER_SKELETON, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 129 */     register(EntityType.WOLF, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Wolf::checkWolfSpawnRules);
/* 130 */     register(EntityType.ZOGLIN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules);
/* 131 */     register(EntityType.CREAKING, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 132 */     register(EntityType.ZOMBIE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 133 */     register(EntityType.ZOMBIE_HORSE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 134 */     register(EntityType.ZOMBIFIED_PIGLIN, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ZombifiedPiglin::checkZombifiedPiglinSpawnRules);
/* 135 */     register(EntityType.ZOMBIE_VILLAGER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*     */ 
/*     */     
/* 138 */     register(EntityType.CAT, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 139 */     register(EntityType.ELDER_GUARDIAN, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Guardian::checkGuardianSpawnRules);
/* 140 */     register(EntityType.EVOKER, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 141 */     register(EntityType.FOX, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Fox::checkFoxSpawnRules);
/* 142 */     register(EntityType.ILLUSIONER, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 143 */     register(EntityType.PANDA, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 144 */     register(EntityType.PHANTOM, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 145 */     register(EntityType.RAVAGER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 146 */     register(EntityType.SHULKER, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 147 */     register(EntityType.TRADER_LLAMA, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
/* 148 */     register(EntityType.VEX, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 149 */     register(EntityType.VINDICATOR, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/* 150 */     register(EntityType.WANDERING_TRADER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
/* 151 */     register(EntityType.WARDEN, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
/*     */   }
/*     */   
/*     */   public static SpawnPlacementType getPlacementType(EntityType<?> type) {
/* 155 */     Data data = (Data)DATA_BY_TYPE.get(type);
/* 156 */     return (data == null) ? SpawnPlacementTypes.NO_RESTRICTIONS : data.placement;
/*     */   }
/*     */ 
/*     */   
/* 160 */   public static boolean isSpawnPositionOk(EntityType<?> type, LevelReader level, BlockPos blockPos) { return getPlacementType(type).isSpawnPositionOk(level, blockPos, type); }
/*     */ 
/*     */   
/*     */   public static Heightmap.Types getHeightmapType(EntityType<?> type) {
/* 164 */     Data data = (Data)DATA_BY_TYPE.get(type);
/* 165 */     return (data == null) ? Heightmap.Types.MOTION_BLOCKING_NO_LEAVES : data.heightMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends Entity> boolean checkSpawnRules(EntityType<T> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 170 */     Data data = (Data)DATA_BY_TYPE.get(type);
/* 171 */     return (data == null || data.predicate.test(type, level, spawnReason, pos, random));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SpawnPredicate<T extends Entity> {
/*     */     boolean test(EntityType<T> param1EntityType, ServerLevelAccessor param1ServerLevelAccessor, EntitySpawnReason param1EntitySpawnReason, BlockPos param1BlockPos, RandomSource param1RandomSource);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\SpawnPlacements.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */