/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ 
/*    */ public final class StructureSpawnOverride extends Record {
/*    */   private final BoundingBoxType boundingBox;
/*    */   private final WeightedList<MobSpawnSettings.SpawnerData> spawns;
/*    */   
/*  9 */   public StructureSpawnOverride(BoundingBoxType boundingBox, WeightedList<MobSpawnSettings.SpawnerData> spawns) { this.boundingBox = boundingBox; this.spawns = spawns; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride; } public BoundingBoxType boundingBox() { return this.boundingBox; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public WeightedList<MobSpawnSettings.SpawnerData> spawns() { return this.spawns; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static final Codec<StructureSpawnOverride> CODEC = RecordCodecBuilder.create(i -> i.group(BoundingBoxType.CODEC
/* 14 */         .fieldOf("bounding_box").forGetter(StructureSpawnOverride::boundingBox), 
/* 15 */         WeightedList.codec(MobSpawnSettings.SpawnerData.CODEC).fieldOf("spawns").forGetter(StructureSpawnOverride::spawns))
/* 16 */       .apply(i, StructureSpawnOverride::new));
/*    */   
/*    */   public enum BoundingBoxType implements StringRepresentable {
/* 19 */     PIECE("piece"),
/* 20 */     STRUCTURE("full"); public static final Codec<BoundingBoxType> CODEC; private final String id;
/*    */     static  {
/* 22 */       CODEC = StringRepresentable.fromEnum(BoundingBoxType::values);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 27 */     BoundingBoxType(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     public String getSerializedName() { return this.id; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructureSpawnOverride.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */