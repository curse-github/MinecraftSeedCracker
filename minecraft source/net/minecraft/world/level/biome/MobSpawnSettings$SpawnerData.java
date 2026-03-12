/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SpawnerData
/*    */   extends Record
/*    */ {
/*    */   private final EntityType<?> type;
/*    */   private final int minCount;
/*    */   private final int maxCount;
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/biome/MobSpawnSettings$SpawnerData;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 66 */   public EntityType<?> type() { return this.type; } public int minCount() { return this.minCount; } public int maxCount() { return this.maxCount; }
/* 67 */   public static final MapCodec<SpawnerData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.ENTITY_TYPE
/* 68 */         .byNameCodec().fieldOf("type").forGetter(()), ExtraCodecs.POSITIVE_INT
/* 69 */         .fieldOf("minCount").forGetter(()), ExtraCodecs.POSITIVE_INT
/* 70 */         .fieldOf("maxCount").forGetter(()))
/* 71 */       .apply(i, SpawnerData::new)).validate(spawnerData -> {
/* 72 */         if (spawnerData.minCount > spawnerData.maxCount) {
/* 73 */           return DataResult.error(());
/*    */         }
/* 75 */         return DataResult.success(spawnerData);
/*    */       });
/*    */   
/*    */   public SpawnerData(EntityType<?> type, int minCount, int maxCount) {
/* 79 */     type = (type.getCategory() == MobCategory.MISC) ? EntityType.PIG : type;
/*    */     this.type = type;
/*    */     this.minCount = minCount;
/*    */     this.maxCount = maxCount;
/*    */   } public String toString() {
/* 84 */     return String.valueOf(EntityType.getKey(this.type)) + "*(" + String.valueOf(EntityType.getKey(this.type)) + "-" + this.minCount + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\MobSpawnSettings$SpawnerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */