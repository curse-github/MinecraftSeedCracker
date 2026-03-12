/*    */ package net.minecraft.world.level.dimension;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ 
/*    */ public final class LevelStem extends Record {
/*    */   private final Holder<DimensionType> type;
/*    */   private final ChunkGenerator generator;
/*    */   
/* 11 */   public LevelStem(Holder<DimensionType> type, ChunkGenerator generator) { this.type = type; this.generator = generator; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/dimension/LevelStem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/LevelStem; } public Holder<DimensionType> type() { return this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/dimension/LevelStem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/dimension/LevelStem; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/dimension/LevelStem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/dimension/LevelStem;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public ChunkGenerator generator() { return this.generator; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final Codec<LevelStem> CODEC = RecordCodecBuilder.create(i -> i.group(DimensionType.CODEC
/* 16 */         .fieldOf("type").forGetter(LevelStem::type), ChunkGenerator.CODEC
/* 17 */         .fieldOf("generator").forGetter(LevelStem::generator))
/* 18 */       .apply(i, i.stable(LevelStem::new)));
/*    */   
/* 20 */   public static final ResourceKey<LevelStem> OVERWORLD = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("overworld"));
/* 21 */   public static final ResourceKey<LevelStem> NETHER = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("the_nether"));
/* 22 */   public static final ResourceKey<LevelStem> END = ResourceKey.create(Registries.LEVEL_STEM, Identifier.withDefaultNamespace("the_end"));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\dimension\LevelStem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */