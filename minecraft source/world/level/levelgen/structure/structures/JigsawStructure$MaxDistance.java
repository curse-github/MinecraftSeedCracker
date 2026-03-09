/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaxDistance
/*     */   extends Record
/*     */ {
/*     */   private final int horizontal;
/*     */   private final int vertical;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #123	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 123 */   public MaxDistance(int horizontal, int vertical) { this.horizontal = horizontal; this.vertical = vertical; } public int horizontal() { return this.horizontal; } public int vertical() { return this.vertical; }
/*     */ 
/*     */ 
/*     */   
/* 127 */   private static final Codec<Integer> HORIZONTAL_VALUE_CODEC = Codec.intRange(1, 128);
/*     */   
/* 129 */   private static final Codec<MaxDistance> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(HORIZONTAL_VALUE_CODEC
/* 130 */         .fieldOf("horizontal").forGetter(MaxDistance::horizontal), 
/* 131 */         ExtraCodecs.intRange(1, DimensionType.Y_SIZE).optionalFieldOf("vertical", Integer.valueOf(DimensionType.Y_SIZE)).forGetter(MaxDistance::vertical))
/* 132 */       .apply(i, MaxDistance::new));
/*     */   
/* 134 */   public static final Codec<MaxDistance> CODEC = Codec.either(FULL_CODEC, HORIZONTAL_VALUE_CODEC).xmap(either -> 
/* 135 */       (MaxDistance)either.map(Function.identity(), MaxDistance::new), distance -> 
/* 136 */       (distance.horizontal == distance.vertical) ? Either.right(Integer.valueOf(distance.horizontal)) : Either.left(distance));
/*     */ 
/*     */ 
/*     */   
/* 140 */   public MaxDistance(int value) { this(value, value); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\JigsawStructure$MaxDistance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */