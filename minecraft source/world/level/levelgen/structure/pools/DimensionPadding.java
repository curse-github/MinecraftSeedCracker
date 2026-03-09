/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class DimensionPadding extends Record {
/*    */   private final int bottom;
/*    */   private final int top;
/*    */   
/* 10 */   public DimensionPadding(int bottom, int top) { this.bottom = bottom; this.top = top; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding; } public int bottom() { return this.bottom; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int top() { return this.top; }
/* 11 */   private static final Codec<DimensionPadding> RECORD_CODEC = RecordCodecBuilder.create(i -> i.group(ExtraCodecs.NON_NEGATIVE_INT
/* 12 */         .lenientOptionalFieldOf("bottom", Integer.valueOf(0)).forGetter(()), ExtraCodecs.NON_NEGATIVE_INT
/* 13 */         .lenientOptionalFieldOf("top", Integer.valueOf(0)).forGetter(()))
/* 14 */       .apply(i, DimensionPadding::new));
/*    */   
/* 16 */   public static final Codec<DimensionPadding> CODEC = Codec.either(ExtraCodecs.NON_NEGATIVE_INT, RECORD_CODEC).xmap(e -> 
/* 17 */       (DimensionPadding)e.map(DimensionPadding::new, Function.identity()), padding -> 
/* 18 */       padding.hasEqualTopAndBottom() ? Either.left(Integer.valueOf(padding.bottom)) : Either.right(padding));
/*    */   
/* 20 */   public static final DimensionPadding ZERO = new DimensionPadding(0);
/*    */ 
/*    */   
/* 23 */   public DimensionPadding(int value) { this(value, value); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean hasEqualTopAndBottom() { return (this.top == this.bottom); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\DimensionPadding.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */