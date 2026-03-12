/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.dimension.DimensionType;
/*    */ 
/*    */ public interface VerticalAnchor
/*    */ {
/* 10 */   public static final Codec<VerticalAnchor> CODEC = Codec.xor(Absolute.CODEC, 
/*    */       
/* 12 */       Codec.xor(AboveBottom.CODEC, BelowTop.CODEC))
/*    */ 
/*    */ 
/*    */     
/* 16 */     .xmap(VerticalAnchor::merge, VerticalAnchor::split);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static final VerticalAnchor BOTTOM = aboveBottom(0);
/* 22 */   public static final VerticalAnchor TOP = belowTop(0);
/*    */ 
/*    */   
/* 25 */   static VerticalAnchor absolute(int value) { return new Absolute(value); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   static VerticalAnchor aboveBottom(int offset) { return new AboveBottom(offset); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   static VerticalAnchor belowTop(int offset) { return new BelowTop(offset); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   static VerticalAnchor bottom() { return BOTTOM; }
/*    */ 
/*    */ 
/*    */   
/* 41 */   static VerticalAnchor top() { return TOP; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   private static VerticalAnchor merge(Either<Absolute, Either<AboveBottom, BelowTop>> either) { return (VerticalAnchor)either.map(Function.identity(), Either::unwrap); }
/*    */ 
/*    */   
/*    */   private static Either<Absolute, Either<AboveBottom, BelowTop>> split(VerticalAnchor anchor) {
/* 49 */     if (anchor instanceof Absolute) {
/* 50 */       return Either.left((Absolute)anchor);
/*    */     }
/* 52 */     return Either.right((anchor instanceof AboveBottom) ? Either.left((AboveBottom)anchor) : Either.right((BelowTop)anchor));
/*    */   }
/*    */   int resolveY(WorldGenerationContext paramWorldGenerationContext);
/*    */   public static final class Absolute extends Record implements VerticalAnchor { private final int y;
/*    */     
/* 57 */     public Absolute(int y) { this.y = y; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 57 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute; } public int y() { return this.y; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$Absolute;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/* 58 */     public static final Codec<Absolute> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("absolute").xmap(Absolute::new, Absolute::y).codec();
/*    */ 
/*    */ 
/*    */     
/* 62 */     public int resolveY(WorldGenerationContext heightAccessor) { return this.y; }
/*    */ 
/*    */ 
/*    */     
/*    */     public String toString() {
/* 67 */       return "" + this.y + " absolute";
/*    */     } }
/*    */   public static final class AboveBottom extends Record implements VerticalAnchor { private final int offset;
/*    */     
/* 71 */     public AboveBottom(int offset) { this.offset = offset; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$AboveBottom;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$AboveBottom; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$AboveBottom;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$AboveBottom;
/* 71 */       //   0	8	1	o	Ljava/lang/Object; } public int offset() { return this.offset; }
/* 72 */     public static final Codec<AboveBottom> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("above_bottom").xmap(AboveBottom::new, AboveBottom::offset).codec();
/*    */ 
/*    */ 
/*    */     
/* 76 */     public int resolveY(WorldGenerationContext heightAccessor) { return heightAccessor.getMinGenY() + this.offset; }
/*    */ 
/*    */ 
/*    */     
/*    */     public String toString() {
/* 81 */       return "" + this.offset + " above bottom";
/*    */     } }
/*    */   public static final class BelowTop extends Record implements VerticalAnchor { private final int offset;
/*    */     
/* 85 */     public BelowTop(int offset) { this.offset = offset; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #85	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/VerticalAnchor$BelowTop;
/* 85 */       //   0	8	1	o	Ljava/lang/Object; } public int offset() { return this.offset; }
/* 86 */     public static final Codec<BelowTop> CODEC = Codec.intRange(DimensionType.MIN_Y, DimensionType.MAX_Y).fieldOf("below_top").xmap(BelowTop::new, BelowTop::offset).codec();
/*    */ 
/*    */ 
/*    */     
/* 90 */     public int resolveY(WorldGenerationContext heightAccessor) { return heightAccessor.getGenDepth() - 1 + heightAccessor.getMinGenY() - this.offset; }
/*    */ 
/*    */ 
/*    */     
/*    */     public String toString() {
/* 95 */       return "" + this.offset + " below top";
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\VerticalAnchor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */