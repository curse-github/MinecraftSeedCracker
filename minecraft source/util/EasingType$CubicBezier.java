/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CubicBezier
/*     */   implements EasingType
/*     */ {
/*  71 */   public static final Codec<CubicBezier> CODEC = RecordCodecBuilder.create(i -> i.group(EasingType.CubicBezierControls.CODEC
/*  72 */         .fieldOf("cubic_bezier").forGetter(()))
/*  73 */       .apply(i, CubicBezier::new));
/*     */   
/*     */   private static final int NEWTON_RAPHSON_ITERATIONS = 4;
/*     */   
/*     */   private final EasingType.CubicBezierControls controls;
/*     */   private final CubicCurve xCurve;
/*     */   private final CubicCurve yCurve;
/*     */   
/*     */   public CubicBezier(EasingType.CubicBezierControls controls) {
/*  82 */     this.controls = controls;
/*  83 */     this.xCurve = curveFromControls(controls.x1, controls.x2);
/*  84 */     this.yCurve = curveFromControls(controls.y1, controls.y2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   private static CubicCurve curveFromControls(float v1, float v2) { return new CubicCurve(3.0F * v1 - 3.0F * v2 + 1.0F, -6.0F * v1 + 3.0F * v2, 3.0F * v1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float apply(float x) {
/*  99 */     float t = x;
/* 100 */     for (int i = 0; i < 4; i++) {
/* 101 */       float gradient = this.xCurve.sampleGradient(t);
/* 102 */       if (gradient < 1.0E-5F) {
/*     */         break;
/*     */       }
/* 105 */       float error = this.xCurve.sample(t) - x;
/* 106 */       t -= error / gradient;
/*     */     } 
/* 108 */     return this.yCurve.sample(t);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public boolean equals(Object obj) { if (obj instanceof CubicBezier) { CubicBezier bezier = (CubicBezier)obj; if (this.controls.equals(bezier.controls)); }  return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public int hashCode() { return this.controls.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public String toString() { return "CubicBezier(" + this.controls.x1 + ", " + this.controls.y1 + ", " + this.controls.x2 + ", " + this.controls.y2 + ")"; }
/*     */   private static final class CubicCurve extends Record { private final float a; private final float b; private final float c;
/*     */     
/* 126 */     private CubicCurve(float a, float b, float c) { this.a = a; this.b = b; this.c = c; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 126 */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve; } public float a() { return this.a; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/* 126 */       //   0	8	1	o	Ljava/lang/Object; } public float b() { return this.b; } public float c() { return this.c; }
/*     */     
/* 128 */     public float sample(float t) { return ((this.a * t + this.b) * t + this.c) * t; }
/*     */ 
/*     */ 
/*     */     
/* 132 */     public float sampleGradient(float t) { return (3.0F * this.a * t + 2.0F * this.b) * t + this.c; } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\EasingType$CubicBezier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */