/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
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
/*     */ public final class Packed
/*     */   extends Record
/*     */ {
/*     */   private final Set<UUID> detectedPlayers;
/*     */   private final Set<UUID> currentMobs;
/*     */   private final long cooldownEndsAt;
/*     */   private final long nextMobSpawnsAt;
/*     */   private final int totalMobsSpawned;
/*     */   private final Optional<SpawnData> nextSpawnData;
/*     */   private final Optional<ResourceKey<LootTable>> ejectingLootTable;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #331	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #331	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #331	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 331 */   public Packed(Set<UUID> detectedPlayers, Set<UUID> currentMobs, long cooldownEndsAt, long nextMobSpawnsAt, int totalMobsSpawned, Optional<SpawnData> nextSpawnData, Optional<ResourceKey<LootTable>> ejectingLootTable) { this.detectedPlayers = detectedPlayers; this.currentMobs = currentMobs; this.cooldownEndsAt = cooldownEndsAt; this.nextMobSpawnsAt = nextMobSpawnsAt; this.totalMobsSpawned = totalMobsSpawned; this.nextSpawnData = nextSpawnData; this.ejectingLootTable = ejectingLootTable; } public Set<UUID> detectedPlayers() { return this.detectedPlayers; } public Set<UUID> currentMobs() { return this.currentMobs; } public long cooldownEndsAt() { return this.cooldownEndsAt; } public long nextMobSpawnsAt() { return this.nextMobSpawnsAt; } public int totalMobsSpawned() { return this.totalMobsSpawned; } public Optional<SpawnData> nextSpawnData() { return this.nextSpawnData; } public Optional<ResourceKey<LootTable>> ejectingLootTable() { return this.ejectingLootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 340 */   public static final MapCodec<Packed> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(UUIDUtil.CODEC_SET
/* 341 */         .lenientOptionalFieldOf("registered_players", Set.of()).forGetter(Packed::detectedPlayers), UUIDUtil.CODEC_SET
/* 342 */         .lenientOptionalFieldOf("current_mobs", Set.of()).forGetter(Packed::currentMobs), Codec.LONG
/* 343 */         .lenientOptionalFieldOf("cooldown_ends_at", Long.valueOf(0L)).forGetter(Packed::cooldownEndsAt), Codec.LONG
/* 344 */         .lenientOptionalFieldOf("next_mob_spawns_at", Long.valueOf(0L)).forGetter(Packed::nextMobSpawnsAt), 
/* 345 */         Codec.intRange(0, 2147483647).lenientOptionalFieldOf("total_mobs_spawned", Integer.valueOf(0)).forGetter(Packed::totalMobsSpawned), SpawnData.CODEC
/* 346 */         .lenientOptionalFieldOf("spawn_data").forGetter(Packed::nextSpawnData), LootTable.KEY_CODEC
/* 347 */         .lenientOptionalFieldOf("ejecting_loot_table").forGetter(Packed::ejectingLootTable))
/* 348 */       .apply(i, Packed::new));
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerStateData$Packed.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */