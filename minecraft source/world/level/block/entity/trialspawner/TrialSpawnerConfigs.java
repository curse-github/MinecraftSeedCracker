/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentTable;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrialSpawnerConfigs
/*     */ {
/*  23 */   private static final Keys TRIAL_CHAMBER_BREEZE = Keys.of("trial_chamber/breeze");
/*  24 */   private static final Keys TRIAL_CHAMBER_MELEE_HUSK = Keys.of("trial_chamber/melee/husk");
/*  25 */   private static final Keys TRIAL_CHAMBER_MELEE_SPIDER = Keys.of("trial_chamber/melee/spider");
/*  26 */   private static final Keys TRIAL_CHAMBER_MELEE_ZOMBIE = Keys.of("trial_chamber/melee/zombie");
/*  27 */   private static final Keys TRIAL_CHAMBER_RANGED_POISON_SKELETON = Keys.of("trial_chamber/ranged/poison_skeleton");
/*  28 */   private static final Keys TRIAL_CHAMBER_RANGED_SKELETON = Keys.of("trial_chamber/ranged/skeleton");
/*  29 */   private static final Keys TRIAL_CHAMBER_RANGED_STRAY = Keys.of("trial_chamber/ranged/stray");
/*  30 */   private static final Keys TRIAL_CHAMBER_SLOW_RANGED_POISON_SKELETON = Keys.of("trial_chamber/slow_ranged/poison_skeleton");
/*  31 */   private static final Keys TRIAL_CHAMBER_SLOW_RANGED_SKELETON = Keys.of("trial_chamber/slow_ranged/skeleton");
/*  32 */   private static final Keys TRIAL_CHAMBER_SLOW_RANGED_STRAY = Keys.of("trial_chamber/slow_ranged/stray");
/*  33 */   private static final Keys TRIAL_CHAMBER_SMALL_MELEE_BABY_ZOMBIE = Keys.of("trial_chamber/small_melee/baby_zombie");
/*  34 */   private static final Keys TRIAL_CHAMBER_SMALL_MELEE_CAVE_SPIDER = Keys.of("trial_chamber/small_melee/cave_spider");
/*  35 */   private static final Keys TRIAL_CHAMBER_SMALL_MELEE_SILVERFISH = Keys.of("trial_chamber/small_melee/silverfish");
/*  36 */   private static final Keys TRIAL_CHAMBER_SMALL_MELEE_SLIME = Keys.of("trial_chamber/small_melee/slime");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<TrialSpawnerConfig> context) {
/*  39 */     register(context, TRIAL_CHAMBER_BREEZE, 
/*  40 */         TrialSpawnerConfig.builder()
/*  41 */         .simultaneousMobs(1.0F)
/*  42 */         .simultaneousMobsAddedPerPlayer(0.5F)
/*  43 */         .ticksBetweenSpawn(20)
/*  44 */         .totalMobs(2.0F)
/*  45 */         .totalMobsAddedPerPlayer(1.0F)
/*  46 */         .spawnPotentialsDefinition(
/*  47 */           WeightedList.of(spawnData(EntityType.BREEZE)))
/*     */         
/*  49 */         .build(), 
/*     */         
/*  51 */         TrialSpawnerConfig.builder()
/*  52 */         .simultaneousMobsAddedPerPlayer(0.5F)
/*  53 */         .ticksBetweenSpawn(20)
/*  54 */         .totalMobs(4.0F)
/*  55 */         .totalMobsAddedPerPlayer(1.0F)
/*  56 */         .spawnPotentialsDefinition(
/*  57 */           WeightedList.of(spawnData(EntityType.BREEZE)))
/*     */         
/*  59 */         .lootTablesToEject(
/*  60 */           WeightedList.builder()
/*  61 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/*  62 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/*  63 */           .build())
/*     */         
/*  65 */         .build());
/*     */ 
/*     */     
/*  68 */     register(context, TRIAL_CHAMBER_MELEE_HUSK, 
/*  69 */         trialChamberBase()
/*  70 */         .spawnPotentialsDefinition(
/*  71 */           WeightedList.of(spawnData(EntityType.HUSK)))
/*     */         
/*  73 */         .build(), 
/*     */         
/*  75 */         trialChamberBase()
/*  76 */         .spawnPotentialsDefinition(
/*  77 */           WeightedList.of(spawnDataWithEquipment(EntityType.HUSK, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE)))
/*     */         
/*  79 */         .lootTablesToEject(
/*  80 */           WeightedList.builder()
/*  81 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/*  82 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/*  83 */           .build())
/*     */         
/*  85 */         .build());
/*     */ 
/*     */     
/*  88 */     register(context, TRIAL_CHAMBER_MELEE_SPIDER, 
/*  89 */         trialChamberBase()
/*  90 */         .spawnPotentialsDefinition(
/*  91 */           WeightedList.of(spawnData(EntityType.SPIDER)))
/*     */         
/*  93 */         .build(), 
/*     */         
/*  95 */         trialChamberMeleeOminous()
/*  96 */         .spawnPotentialsDefinition(
/*  97 */           WeightedList.of(spawnData(EntityType.SPIDER)))
/*     */         
/*  99 */         .lootTablesToEject(
/* 100 */           WeightedList.builder()
/* 101 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 102 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 103 */           .build())
/*     */         
/* 105 */         .build());
/*     */ 
/*     */     
/* 108 */     register(context, TRIAL_CHAMBER_MELEE_ZOMBIE, 
/* 109 */         trialChamberBase()
/* 110 */         .spawnPotentialsDefinition(
/* 111 */           WeightedList.of(spawnData(EntityType.ZOMBIE)))
/*     */         
/* 113 */         .build(), 
/*     */         
/* 115 */         trialChamberBase()
/* 116 */         .lootTablesToEject(
/* 117 */           WeightedList.builder()
/* 118 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 119 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 120 */           .build())
/*     */         
/* 122 */         .spawnPotentialsDefinition(
/* 123 */           WeightedList.of(spawnDataWithEquipment(EntityType.ZOMBIE, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE)))
/*     */         
/* 125 */         .build());
/*     */ 
/*     */     
/* 128 */     register(context, TRIAL_CHAMBER_RANGED_POISON_SKELETON, 
/* 129 */         trialChamberBase()
/* 130 */         .spawnPotentialsDefinition(
/* 131 */           WeightedList.of(spawnData(EntityType.BOGGED)))
/*     */         
/* 133 */         .build(), 
/*     */         
/* 135 */         trialChamberBase()
/* 136 */         .lootTablesToEject(
/* 137 */           WeightedList.builder()
/* 138 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 139 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 140 */           .build())
/*     */         
/* 142 */         .spawnPotentialsDefinition(
/* 143 */           WeightedList.of(spawnDataWithEquipment(EntityType.BOGGED, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 145 */         .build());
/*     */ 
/*     */     
/* 148 */     register(context, TRIAL_CHAMBER_RANGED_SKELETON, 
/* 149 */         trialChamberBase()
/* 150 */         .spawnPotentialsDefinition(
/* 151 */           WeightedList.of(spawnData(EntityType.SKELETON)))
/*     */         
/* 153 */         .build(), 
/*     */         
/* 155 */         trialChamberBase()
/* 156 */         .lootTablesToEject(
/* 157 */           WeightedList.builder()
/* 158 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 159 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 160 */           .build())
/*     */         
/* 162 */         .spawnPotentialsDefinition(
/* 163 */           WeightedList.of(spawnDataWithEquipment(EntityType.SKELETON, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 165 */         .build());
/*     */ 
/*     */     
/* 168 */     register(context, TRIAL_CHAMBER_RANGED_STRAY, 
/* 169 */         trialChamberBase()
/* 170 */         .spawnPotentialsDefinition(
/* 171 */           WeightedList.of(spawnData(EntityType.STRAY)))
/*     */         
/* 173 */         .build(), 
/*     */         
/* 175 */         trialChamberBase()
/* 176 */         .lootTablesToEject(
/* 177 */           WeightedList.builder()
/* 178 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 179 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 180 */           .build())
/*     */         
/* 182 */         .spawnPotentialsDefinition(
/* 183 */           WeightedList.of(spawnDataWithEquipment(EntityType.STRAY, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 185 */         .build());
/*     */ 
/*     */     
/* 188 */     register(context, TRIAL_CHAMBER_SLOW_RANGED_POISON_SKELETON, 
/* 189 */         trialChamberSlowRanged()
/* 190 */         .spawnPotentialsDefinition(
/* 191 */           WeightedList.of(spawnData(EntityType.BOGGED)))
/*     */         
/* 193 */         .build(), 
/*     */         
/* 195 */         trialChamberSlowRanged()
/* 196 */         .lootTablesToEject(
/* 197 */           WeightedList.builder()
/* 198 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 199 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 200 */           .build())
/*     */         
/* 202 */         .spawnPotentialsDefinition(
/* 203 */           WeightedList.of(spawnDataWithEquipment(EntityType.BOGGED, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 205 */         .build());
/*     */ 
/*     */     
/* 208 */     register(context, TRIAL_CHAMBER_SLOW_RANGED_SKELETON, 
/* 209 */         trialChamberSlowRanged()
/* 210 */         .spawnPotentialsDefinition(
/* 211 */           WeightedList.of(spawnData(EntityType.SKELETON)))
/*     */         
/* 213 */         .build(), 
/*     */         
/* 215 */         trialChamberSlowRanged()
/* 216 */         .lootTablesToEject(
/* 217 */           WeightedList.builder()
/* 218 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 219 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 220 */           .build())
/*     */         
/* 222 */         .spawnPotentialsDefinition(
/* 223 */           WeightedList.of(spawnDataWithEquipment(EntityType.SKELETON, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 225 */         .build());
/*     */ 
/*     */     
/* 228 */     register(context, TRIAL_CHAMBER_SLOW_RANGED_STRAY, 
/* 229 */         trialChamberSlowRanged()
/* 230 */         .spawnPotentialsDefinition(
/* 231 */           WeightedList.of(spawnData(EntityType.STRAY)))
/*     */         
/* 233 */         .build(), 
/*     */         
/* 235 */         trialChamberSlowRanged()
/* 236 */         .lootTablesToEject(
/* 237 */           WeightedList.builder()
/* 238 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 239 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 240 */           .build())
/*     */         
/* 242 */         .spawnPotentialsDefinition(
/* 243 */           WeightedList.of(spawnDataWithEquipment(EntityType.STRAY, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 245 */         .build());
/*     */ 
/*     */     
/* 248 */     register(context, TRIAL_CHAMBER_SMALL_MELEE_BABY_ZOMBIE, 
/* 249 */         TrialSpawnerConfig.builder()
/* 250 */         .simultaneousMobsAddedPerPlayer(0.5F)
/* 251 */         .ticksBetweenSpawn(20)
/* 252 */         .spawnPotentialsDefinition(
/* 253 */           WeightedList.of(customSpawnDataWithEquipment(EntityType.ZOMBIE, tag -> tag.putBoolean("IsBaby", true), null)))
/*     */         
/* 255 */         .build(), 
/*     */         
/* 257 */         TrialSpawnerConfig.builder()
/* 258 */         .simultaneousMobsAddedPerPlayer(0.5F)
/* 259 */         .ticksBetweenSpawn(20)
/* 260 */         .lootTablesToEject(
/* 261 */           WeightedList.builder()
/* 262 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 263 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 264 */           .build())
/*     */         
/* 266 */         .spawnPotentialsDefinition(
/* 267 */           WeightedList.of(customSpawnDataWithEquipment(EntityType.ZOMBIE, tag -> tag.putBoolean("IsBaby", true), BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE)))
/*     */         
/* 269 */         .build());
/*     */     
/* 271 */     register(context, TRIAL_CHAMBER_SMALL_MELEE_CAVE_SPIDER, 
/* 272 */         trialChamberBase()
/* 273 */         .spawnPotentialsDefinition(
/* 274 */           WeightedList.of(spawnData(EntityType.CAVE_SPIDER)))
/*     */         
/* 276 */         .build(), 
/*     */         
/* 278 */         trialChamberMeleeOminous()
/* 279 */         .lootTablesToEject(
/* 280 */           WeightedList.builder()
/* 281 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 282 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 283 */           .build())
/*     */         
/* 285 */         .spawnPotentialsDefinition(
/* 286 */           WeightedList.of(spawnData(EntityType.CAVE_SPIDER)))
/*     */         
/* 288 */         .build());
/*     */ 
/*     */     
/* 291 */     register(context, TRIAL_CHAMBER_SMALL_MELEE_SILVERFISH, 
/* 292 */         trialChamberBase()
/* 293 */         .spawnPotentialsDefinition(
/* 294 */           WeightedList.of(spawnData(EntityType.SILVERFISH)))
/*     */         
/* 296 */         .build(), 
/*     */         
/* 298 */         trialChamberMeleeOminous()
/* 299 */         .lootTablesToEject(
/* 300 */           WeightedList.builder()
/* 301 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 302 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 303 */           .build())
/*     */         
/* 305 */         .spawnPotentialsDefinition(
/* 306 */           WeightedList.of(spawnData(EntityType.SILVERFISH)))
/*     */         
/* 308 */         .build());
/*     */ 
/*     */     
/* 311 */     register(context, TRIAL_CHAMBER_SMALL_MELEE_SLIME, 
/* 312 */         trialChamberBase()
/* 313 */         .spawnPotentialsDefinition(
/* 314 */           WeightedList.builder()
/* 315 */           .add(customSpawnData(EntityType.SLIME, tag -> tag.putByte("Size", (byte)1)), 3)
/* 316 */           .add(customSpawnData(EntityType.SLIME, tag -> tag.putByte("Size", (byte)2)), 1)
/* 317 */           .build())
/*     */         
/* 319 */         .build(), 
/*     */         
/* 321 */         trialChamberMeleeOminous()
/* 322 */         .lootTablesToEject(
/* 323 */           WeightedList.builder()
/* 324 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 325 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 326 */           .build())
/*     */         
/* 328 */         .spawnPotentialsDefinition(
/* 329 */           WeightedList.builder()
/* 330 */           .add(customSpawnData(EntityType.SLIME, tag -> tag.putByte("Size", (byte)1)), 3)
/* 331 */           .add(customSpawnData(EntityType.SLIME, tag -> tag.putByte("Size", (byte)2)), 1)
/* 332 */           .build())
/*     */         
/* 334 */         .build());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 339 */   private static <T extends net.minecraft.world.entity.Entity> SpawnData spawnData(EntityType<T> type) { return customSpawnDataWithEquipment(type, tag -> {  }null); }
/*     */ 
/*     */ 
/*     */   
/* 343 */   private static <T extends net.minecraft.world.entity.Entity> SpawnData customSpawnData(EntityType<T> type, Consumer<CompoundTag> tagModifier) { return customSpawnDataWithEquipment(type, tagModifier, null); }
/*     */ 
/*     */ 
/*     */   
/* 347 */   private static <T extends net.minecraft.world.entity.Entity> SpawnData spawnDataWithEquipment(EntityType<T> type, ResourceKey<LootTable> equipmentLootTable) { return customSpawnDataWithEquipment(type, tag -> {  }equipmentLootTable); }
/*     */ 
/*     */   
/*     */   private static <T extends net.minecraft.world.entity.Entity> SpawnData customSpawnDataWithEquipment(EntityType<T> type, Consumer<CompoundTag> tagModifier, ResourceKey<LootTable> equipmentLootTable) {
/* 351 */     CompoundTag tag = new CompoundTag();
/* 352 */     tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
/* 353 */     tagModifier.accept(tag);
/* 354 */     Optional<EquipmentTable> table = Optional.ofNullable(equipmentLootTable).map(lootTable -> new EquipmentTable(lootTable, 0.0F));
/* 355 */     return new SpawnData(tag, Optional.empty(), table);
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<TrialSpawnerConfig> context, Keys keys, TrialSpawnerConfig normalConfig, TrialSpawnerConfig ominousConfig) {
/* 359 */     context.register(keys.normal, normalConfig);
/* 360 */     context.register(keys.ominous, ominousConfig);
/*     */   }
/*     */ 
/*     */   
/* 364 */   private static ResourceKey<TrialSpawnerConfig> registryKey(String id) { return ResourceKey.create(Registries.TRIAL_SPAWNER_CONFIG, Identifier.withDefaultNamespace(id)); }
/*     */   private static final class Keys extends Record { private final ResourceKey<TrialSpawnerConfig> normal; private final ResourceKey<TrialSpawnerConfig> ominous;
/*     */     
/* 367 */     private Keys(ResourceKey<TrialSpawnerConfig> normal, ResourceKey<TrialSpawnerConfig> ominous) { this.normal = normal; this.ominous = ominous; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 367 */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys; } public ResourceKey<TrialSpawnerConfig> normal() { return this.normal; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;
/* 367 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<TrialSpawnerConfig> ominous() { return this.ominous; }
/*     */     public static Keys of(String id) {
/* 369 */       return new Keys(TrialSpawnerConfigs.registryKey(id + "/normal"), TrialSpawnerConfigs.registryKey(id + "/ominous"));
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TrialSpawnerConfig.Builder trialChamberMeleeOminous() {
/* 376 */     return TrialSpawnerConfig.builder()
/* 377 */       .simultaneousMobs(4.0F)
/* 378 */       .simultaneousMobsAddedPerPlayer(0.5F)
/* 379 */       .ticksBetweenSpawn(20)
/* 380 */       .totalMobs(12.0F);
/*     */   }
/*     */   
/*     */   private static TrialSpawnerConfig.Builder trialChamberSlowRanged() {
/* 384 */     return TrialSpawnerConfig.builder()
/* 385 */       .simultaneousMobs(4.0F)
/* 386 */       .simultaneousMobsAddedPerPlayer(2.0F)
/* 387 */       .ticksBetweenSpawn(160);
/*     */   }
/*     */   
/*     */   private static TrialSpawnerConfig.Builder trialChamberBase() {
/* 391 */     return TrialSpawnerConfig.builder()
/* 392 */       .simultaneousMobs(3.0F)
/* 393 */       .simultaneousMobsAddedPerPlayer(0.5F)
/* 394 */       .ticksBetweenSpawn(20);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerConfigs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */