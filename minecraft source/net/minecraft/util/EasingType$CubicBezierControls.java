/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CubicBezierControls
/*     */   extends Record
/*     */ {
/*     */   private final float x1;
/*     */   private final float y1;
/*     */   private final float x2;
/*     */   private final float y2;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezierControls;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/EasingType$CubicBezierControls;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/EasingType$CubicBezierControls;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 137 */   public CubicBezierControls(float x1, float y1, float x2, float y2) { this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2; } public float x1() { return this.x1; } public float y1() { return this.y1; } public float x2() { return this.x2; } public float y2() { return this.y2; }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public static final Codec<CubicBezierControls> CODEC = Codec.FLOAT.listOf(4, 4).xmap(floats -> 
/* 142 */       new CubicBezierControls(((Float)floats.get(0)).floatValue(), ((Float)floats.get(1)).floatValue(), ((Float)floats.get(2)).floatValue(), ((Float)floats.get(3)).floatValue()), controls -> 
/* 143 */       List.of(Float.valueOf(controls.x1), Float.valueOf(controls.y1), Float.valueOf(controls.x2), Float.valueOf(controls.y2)))
/* 144 */     .validate(CubicBezierControls::validate);
/*     */   
/*     */   private DataResult<CubicBezierControls> validate() {
/* 147 */     if (this.x1 < 0.0F || this.x1 > 1.0F)
/* 148 */       return DataResult.error(() -> "x1 must be in range [0; 1]"); 
/* 149 */     if (this.x2 < 0.0F || this.x2 > 1.0F) {
/* 150 */       return DataResult.error(() -> "x2 must be in range [0; 1]");
/*     */     }
/* 152 */     return DataResult.success(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\EasingType$CubicBezierControls.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */