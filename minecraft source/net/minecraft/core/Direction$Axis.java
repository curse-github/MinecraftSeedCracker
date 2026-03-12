/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static final abstract enum Axis
/*     */   implements Predicate<Direction>, StringRepresentable
/*     */ {
/*     */   X, Y, Z;
/*     */   public static final Axis[] VALUES;
/*     */   public static final StringRepresentable.EnumCodec<Axis> CODEC;
/*     */   private final String name;
/*     */   
/*     */   static  {
/*     */     // Byte code:
/*     */     //   0: new net/minecraft/core/Direction$Axis$1
/*     */     //   3: dup
/*     */     //   4: ldc 'X'
/*     */     //   6: iconst_0
/*     */     //   7: ldc 'x'
/*     */     //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   12: putstatic net/minecraft/core/Direction$Axis.X : Lnet/minecraft/core/Direction$Axis;
/*     */     //   15: new net/minecraft/core/Direction$Axis$2
/*     */     //   18: dup
/*     */     //   19: ldc 'Y'
/*     */     //   21: iconst_1
/*     */     //   22: ldc 'y'
/*     */     //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   27: putstatic net/minecraft/core/Direction$Axis.Y : Lnet/minecraft/core/Direction$Axis;
/*     */     //   30: new net/minecraft/core/Direction$Axis$3
/*     */     //   33: dup
/*     */     //   34: ldc 'Z'
/*     */     //   36: iconst_2
/*     */     //   37: ldc 'z'
/*     */     //   39: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */     //   42: putstatic net/minecraft/core/Direction$Axis.Z : Lnet/minecraft/core/Direction$Axis;
/*     */     //   45: invokestatic $values : ()[Lnet/minecraft/core/Direction$Axis;
/*     */     //   48: putstatic net/minecraft/core/Direction$Axis.$VALUES : [Lnet/minecraft/core/Direction$Axis;
/*     */     //   51: invokestatic values : ()[Lnet/minecraft/core/Direction$Axis;
/*     */     //   54: putstatic net/minecraft/core/Direction$Axis.VALUES : [Lnet/minecraft/core/Direction$Axis;
/*     */     //   57: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */     //   62: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */     //   65: putstatic net/minecraft/core/Direction$Axis.CODEC : Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */     //   68: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #397	-> 0
/*     */     //   #423	-> 15
/*     */     //   #449	-> 30
/*     */     //   #396	-> 45
/*     */     //   #477	-> 51
/*     */     //   #479	-> 57
/*     */   }
/*     */   
/* 484 */   Axis(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */   
/* 488 */   public static Axis byName(String name) { return (Axis)CODEC.byName(name); }
/*     */ 
/*     */ 
/*     */   
/* 492 */   public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 496 */   public boolean isVertical() { return (this == Y); }
/*     */ 
/*     */ 
/*     */   
/* 500 */   public boolean isHorizontal() { return (this == X || this == Z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 508 */   public Direction[] getDirections() { return new Direction[] { getPositive(), getNegative() }; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 513 */   public String toString() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 517 */   public static Axis getRandom(RandomSource random) { return (Axis)Util.getRandom(VALUES, random); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 522 */   public boolean test(Direction input) { return (input != null && input.getAxis() == this); }
/*     */ 
/*     */   
/*     */   public Direction.Plane getPlane() {
/* 526 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 2: case 1: break; }  return 
/*     */       
/* 528 */       Direction.Plane.VERTICAL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 534 */   public String getSerializedName() { return this.name; }
/*     */   
/*     */   public abstract Direction getPositive();
/*     */   
/*     */   public abstract Direction getNegative();
/*     */   
/*     */   public abstract int choose(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public abstract double choose(double paramDouble1, double paramDouble2, double paramDouble3);
/*     */   
/*     */   public abstract boolean choose(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\Direction$Axis.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */