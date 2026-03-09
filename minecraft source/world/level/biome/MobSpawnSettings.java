/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MobSpawnSettings {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final float DEFAULT_CREATURE_SPAWN_PROBABILITY = 0.1F;
/*  27 */   public static final WeightedList<SpawnerData> EMPTY_MOB_LIST = WeightedList.of();
/*  28 */   public static final MobSpawnSettings EMPTY = (new Builder()).build();
/*     */   
/*  30 */   public static final MapCodec<MobSpawnSettings> CODEC = RecordCodecBuilder.mapCodec(i -> {
/*     */ 
/*     */ 
/*     */         
/*  34 */         Objects.requireNonNull(LOGGER); return i.group(Codec.floatRange(0.0F, 0.9999999F).optionalFieldOf("creature_spawn_probability", Float.valueOf(0.1F)).forGetter(()), Codec.simpleMap(MobCategory.CODEC, WeightedList.codec(SpawnerData.CODEC).promotePartial(Util.prefix("Spawn data: ", LOGGER::error)), 
/*  35 */               StringRepresentable.keys(MobCategory.values()))
/*  36 */             .fieldOf("spawners").forGetter(()), 
/*  37 */             Codec.simpleMap(BuiltInRegistries.ENTITY_TYPE
/*  38 */               .byNameCodec(), MobSpawnCost.CODEC, BuiltInRegistries.ENTITY_TYPE)
/*     */ 
/*     */             
/*  41 */             .fieldOf("spawn_costs").forGetter(()))
/*  42 */           .apply(i, MobSpawnSettings::new);
/*     */       });
/*     */   private final float creatureGenerationProbability;
/*     */   private final Map<MobCategory, WeightedList<SpawnerData>> spawners;
/*     */   private final Map<EntityType<?>, MobSpawnCost> mobSpawnCosts;
/*     */   
/*     */   private MobSpawnSettings(float creatureGenerationProbability, Map<MobCategory, WeightedList<SpawnerData>> spawners, Map<EntityType<?>, MobSpawnCost> mobSpawnCosts) {
/*  49 */     this.creatureGenerationProbability = creatureGenerationProbability;
/*  50 */     this.spawners = ImmutableMap.copyOf(spawners);
/*  51 */     this.mobSpawnCosts = ImmutableMap.copyOf(mobSpawnCosts);
/*     */   }
/*     */ 
/*     */   
/*  55 */   public WeightedList<SpawnerData> getMobs(MobCategory category) { return (WeightedList)this.spawners.getOrDefault(category, EMPTY_MOB_LIST); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public MobSpawnCost getMobSpawnCost(EntityType<?> type) { return (MobSpawnCost)this.mobSpawnCosts.get(type); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public float getCreatureProbability() { return this.creatureGenerationProbability; }
/*     */   public static final class SpawnerData extends Record { private final EntityType<?> type; private final int minCount;
/*     */     
/*  66 */     public EntityType<?> type() { return this.type; } private final int maxCount; public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #66	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;
/*  66 */       //   0	8	1	o	Ljava/lang/Object; } public int minCount() { return this.minCount; } public int maxCount() { return this.maxCount; }
/*  67 */     public static final MapCodec<SpawnerData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.ENTITY_TYPE
/*  68 */           .byNameCodec().fieldOf("type").forGetter(()), ExtraCodecs.POSITIVE_INT
/*  69 */           .fieldOf("minCount").forGetter(()), ExtraCodecs.POSITIVE_INT
/*  70 */           .fieldOf("maxCount").forGetter(()))
/*  71 */         .apply(i, SpawnerData::new)).validate(spawnerData -> {
/*  72 */           if (spawnerData.minCount > spawnerData.maxCount) {
/*  73 */             return DataResult.error(());
/*     */           }
/*  75 */           return DataResult.success(spawnerData);
/*     */         });
/*     */     
/*     */     public SpawnerData(EntityType<?> type, int minCount, int maxCount) {
/*  79 */       type = (type.getCategory() == MobCategory.MISC) ? EntityType.PIG : type;
/*     */       this.type = type;
/*     */       this.minCount = minCount;
/*     */       this.maxCount = maxCount;
/*     */     } public String toString() {
/*  84 */       return String.valueOf(EntityType.getKey(this.type)) + "*(" + String.valueOf(EntityType.getKey(this.type)) + "-" + this.minCount + ")";
/*     */     } }
/*     */   public static final class MobSpawnCost extends Record { private final double energyBudget; private final double charge;
/*     */     
/*  88 */     public MobSpawnCost(double energyBudget, double charge) { this.energyBudget = energyBudget; this.charge = charge; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #88	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$MobSpawnCost;
/*  88 */       //   0	8	1	o	Ljava/lang/Object; } public double energyBudget() { return this.energyBudget; } public double charge() { return this.charge; }
/*  89 */     public static final Codec<MobSpawnCost> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.DOUBLE
/*  90 */           .fieldOf("energy_budget").forGetter(()), Codec.DOUBLE
/*  91 */           .fieldOf("charge").forGetter(()))
/*  92 */         .apply(i, MobSpawnCost::new)); }
/*     */ 
/*     */   
/*     */   public static class Builder {
/*  96 */     private final Map<MobCategory, WeightedList.Builder<MobSpawnSettings.SpawnerData>> spawners = Util.makeEnumMap(MobCategory.class, c -> WeightedList.builder());
/*  97 */     private final Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts = Maps.newLinkedHashMap();
/*  98 */     private float creatureGenerationProbability = 0.1F;
/*     */     
/*     */     public Builder addSpawn(MobCategory category, int weight, MobSpawnSettings.SpawnerData spawnerData) {
/* 101 */       ((WeightedList.Builder)this.spawners.get(category)).add(spawnerData, weight);
/* 102 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addMobCharge(EntityType<?> type, double charge, double energyBudget) {
/* 128 */       this.mobSpawnCosts.put(type, new MobSpawnSettings.MobSpawnCost(energyBudget, charge));
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public Builder creatureGenerationProbability(float creatureGenerationProbability) {
/* 133 */       this.creatureGenerationProbability = creatureGenerationProbability;
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public MobSpawnSettings build() {
/* 138 */       return new MobSpawnSettings(this.creatureGenerationProbability, (Map)this.spawners
/*     */           
/* 140 */           .entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, e -> ((WeightedList.Builder)e.getValue()).build())), 
/* 141 */           ImmutableMap.copyOf(this.mobSpawnCosts));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\MobSpawnSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */