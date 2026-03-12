/*    */ package net.minecraft.world.level;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ import net.minecraft.world.entity.EquipmentTable;
/*    */ 
/*    */ public final class SpawnData extends Record {
/*    */   private final CompoundTag entityToSpawn;
/*    */   private final Optional<CustomSpawnRules> customSpawnRules;
/*    */   private final Optional<EquipmentTable> equipment;
/*    */   
/* 18 */   public CompoundTag entityToSpawn() { return this.entityToSpawn; } public static final String ENTITY_TAG = "entity"; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/SpawnData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/SpawnData; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/SpawnData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/SpawnData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/SpawnData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/SpawnData;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<CustomSpawnRules> customSpawnRules() { return this.customSpawnRules; } public Optional<EquipmentTable> equipment() { return this.equipment; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final Codec<SpawnData> CODEC = RecordCodecBuilder.create(i -> i.group(CompoundTag.CODEC
/* 26 */         .fieldOf("entity").forGetter(()), CustomSpawnRules.CODEC
/* 27 */         .optionalFieldOf("custom_spawn_rules").forGetter(()), EquipmentTable.CODEC
/* 28 */         .optionalFieldOf("equipment").forGetter(()))
/* 29 */       .apply(i, SpawnData::new));
/*    */   
/* 31 */   public static final Codec<WeightedList<SpawnData>> LIST_CODEC = WeightedList.codec(CODEC);
/*    */ 
/*    */   
/* 34 */   public SpawnData() { this(new CompoundTag(), Optional.empty(), Optional.empty()); }
/*    */ 
/*    */   
/*    */   public SpawnData(CompoundTag entityToSpawn, Optional<CustomSpawnRules> customSpawnRules, Optional<EquipmentTable> equipment) {
/* 38 */     Optional<Identifier> id = entityToSpawn.read("id", Identifier.CODEC);
/*    */     
/* 40 */     if (id.isPresent()) {
/* 41 */       entityToSpawn.store("id", Identifier.CODEC, (Identifier)id.get());
/*    */     } else {
/* 43 */       entityToSpawn.remove("id");
/*    */     } 
/*    */     this.entityToSpawn = entityToSpawn;
/*    */     this.customSpawnRules = customSpawnRules;
/*    */     this.equipment = equipment;
/* 48 */   } public CompoundTag getEntityToSpawn() { return this.entityToSpawn; }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public Optional<CustomSpawnRules> getCustomSpawnRules() { return this.customSpawnRules; }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public Optional<EquipmentTable> getEquipment() { return this.equipment; }
/*    */   public static final class CustomSpawnRules extends Record { private final InclusiveRange<Integer> blockLightLimit; private final InclusiveRange<Integer> skyLightLimit;
/*    */     
/* 59 */     public CustomSpawnRules(InclusiveRange<Integer> blockLightLimit, InclusiveRange<Integer> skyLightLimit) { this.blockLightLimit = blockLightLimit; this.skyLightLimit = skyLightLimit; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #59	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/SpawnData$CustomSpawnRules;
/* 59 */       //   0	8	1	o	Ljava/lang/Object; } public InclusiveRange<Integer> blockLightLimit() { return this.blockLightLimit; } public InclusiveRange<Integer> skyLightLimit() { return this.skyLightLimit; }
/*    */ 
/*    */ 
/*    */     
/* 63 */     private static final InclusiveRange<Integer> LIGHT_RANGE = new InclusiveRange(Integer.valueOf(0), Integer.valueOf(15));
/*    */     
/*    */     private static DataResult<InclusiveRange<Integer>> checkLightBoundaries(InclusiveRange<Integer> range) {
/* 66 */       if (!LIGHT_RANGE.contains(range)) {
/* 67 */         return DataResult.error(() -> "Light values must be withing range " + String.valueOf(LIGHT_RANGE));
/*    */       }
/* 69 */       return DataResult.success(range);
/*    */     }
/*    */ 
/*    */     
/* 73 */     private static MapCodec<InclusiveRange<Integer>> lightLimit(String name) { return InclusiveRange.INT.lenientOptionalFieldOf(name, LIGHT_RANGE).validate(CustomSpawnRules::checkLightBoundaries); }
/*    */ 
/*    */     
/* 76 */     public static final Codec<CustomSpawnRules> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 77 */           lightLimit("block_light_limit").forGetter(()), 
/* 78 */           lightLimit("sky_light_limit").forGetter(()))
/* 79 */         .apply(i, CustomSpawnRules::new));
/*    */ 
/*    */     
/*    */     public boolean isValidPosition(BlockPos blockSpawnPos, ServerLevel level) {
/* 83 */       return (this.blockLightLimit.isValueInRange(Integer.valueOf(level.getBrightness(LightLayer.BLOCK, blockSpawnPos))) && this.skyLightLimit
/* 84 */         .isValueInRange(Integer.valueOf(level.getBrightness(LightLayer.SKY, blockSpawnPos))));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\SpawnData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */