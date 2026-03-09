/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public interface EasingType {
/*  12 */   public static final ExtraCodecs.LateBoundIdMapper<String, EasingType> SIMPLE_REGISTRY = new ExtraCodecs.LateBoundIdMapper();
/*     */   
/*  14 */   public static final Codec<EasingType> CODEC = Codec.either(SIMPLE_REGISTRY.codec(Codec.STRING), CubicBezier.CODEC).xmap(Either::unwrap, easing -> {
/*     */         
/*  16 */         CubicBezier bezier = (CubicBezier)easing; return (easing instanceof CubicBezier) ? Either.right(bezier) : Either.left(easing);
/*     */       });
/*     */   
/*  19 */   public static final EasingType CONSTANT = registerSimple("constant", x -> 0.0F);
/*  20 */   public static final EasingType LINEAR = registerSimple("linear", x -> x);
/*     */   
/*  22 */   public static final EasingType IN_BACK = registerSimple("in_back", Ease::inBack);
/*  23 */   public static final EasingType IN_BOUNCE = registerSimple("in_bounce", Ease::inBounce);
/*  24 */   public static final EasingType IN_CIRC = registerSimple("in_circ", Ease::inCirc);
/*  25 */   public static final EasingType IN_CUBIC = registerSimple("in_cubic", Ease::inCubic);
/*  26 */   public static final EasingType IN_ELASTIC = registerSimple("in_elastic", Ease::inElastic);
/*  27 */   public static final EasingType IN_EXPO = registerSimple("in_expo", Ease::inExpo);
/*  28 */   public static final EasingType IN_QUAD = registerSimple("in_quad", Ease::inQuad);
/*  29 */   public static final EasingType IN_QUART = registerSimple("in_quart", Ease::inQuart);
/*  30 */   public static final EasingType IN_QUINT = registerSimple("in_quint", Ease::inQuint);
/*  31 */   public static final EasingType IN_SINE = registerSimple("in_sine", Ease::inSine);
/*     */   
/*  33 */   public static final EasingType IN_OUT_BACK = registerSimple("in_out_back", Ease::inOutBack);
/*  34 */   public static final EasingType IN_OUT_BOUNCE = registerSimple("in_out_bounce", Ease::inOutBounce);
/*  35 */   public static final EasingType IN_OUT_CIRC = registerSimple("in_out_circ", Ease::inOutCirc);
/*  36 */   public static final EasingType IN_OUT_CUBIC = registerSimple("in_out_cubic", Ease::inOutCubic);
/*  37 */   public static final EasingType IN_OUT_ELASTIC = registerSimple("in_out_elastic", Ease::inOutElastic);
/*  38 */   public static final EasingType IN_OUT_EXPO = registerSimple("in_out_expo", Ease::inOutExpo);
/*  39 */   public static final EasingType IN_OUT_QUAD = registerSimple("in_out_quad", Ease::inOutQuad);
/*  40 */   public static final EasingType IN_OUT_QUART = registerSimple("in_out_quart", Ease::inOutQuart);
/*  41 */   public static final EasingType IN_OUT_QUINT = registerSimple("in_out_quint", Ease::inOutQuint);
/*  42 */   public static final EasingType IN_OUT_SINE = registerSimple("in_out_sine", Ease::inOutSine);
/*     */   
/*  44 */   public static final EasingType OUT_BACK = registerSimple("out_back", Ease::outBack);
/*  45 */   public static final EasingType OUT_BOUNCE = registerSimple("out_bounce", Ease::outBounce);
/*  46 */   public static final EasingType OUT_CIRC = registerSimple("out_circ", Ease::outCirc);
/*  47 */   public static final EasingType OUT_CUBIC = registerSimple("out_cubic", Ease::outCubic);
/*  48 */   public static final EasingType OUT_ELASTIC = registerSimple("out_elastic", Ease::outElastic);
/*  49 */   public static final EasingType OUT_EXPO = registerSimple("out_expo", Ease::outExpo);
/*  50 */   public static final EasingType OUT_QUAD = registerSimple("out_quad", Ease::outQuad);
/*  51 */   public static final EasingType OUT_QUART = registerSimple("out_quart", Ease::outQuart);
/*  52 */   public static final EasingType OUT_QUINT = registerSimple("out_quint", Ease::outQuint);
/*  53 */   public static final EasingType OUT_SINE = registerSimple("out_sine", Ease::outSine);
/*     */   
/*     */   static EasingType registerSimple(String id, EasingType easing) {
/*  56 */     SIMPLE_REGISTRY.put(id, easing);
/*  57 */     return easing;
/*     */   }
/*     */ 
/*     */   
/*  61 */   static EasingType cubicBezier(float x1, float y1, float x2, float y2) { return new CubicBezier(new CubicBezierControls(x1, y1, x2, y2)); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   static EasingType symmetricCubicBezier(float x1, float y1) { return cubicBezier(x1, y1, 1.0F - x1, 1.0F - y1); }
/*     */   
/*     */   float apply(float paramFloat);
/*     */   
/*     */   public static final class CubicBezier
/*     */     implements EasingType {
/*  71 */     public static final Codec<CubicBezier> CODEC = RecordCodecBuilder.create(i -> i.group(EasingType.CubicBezierControls.CODEC
/*  72 */           .fieldOf("cubic_bezier").forGetter(()))
/*  73 */         .apply(i, CubicBezier::new));
/*     */     
/*     */     private static final int NEWTON_RAPHSON_ITERATIONS = 4;
/*     */     
/*     */     private final EasingType.CubicBezierControls controls;
/*     */     private final CubicCurve xCurve;
/*     */     private final CubicCurve yCurve;
/*     */     
/*     */     public CubicBezier(EasingType.CubicBezierControls controls) {
/*  82 */       this.controls = controls;
/*  83 */       this.xCurve = curveFromControls(controls.x1, controls.x2);
/*  84 */       this.yCurve = curveFromControls(controls.y1, controls.y2);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     private static CubicCurve curveFromControls(float v1, float v2) { return new CubicCurve(3.0F * v1 - 3.0F * v2 + 1.0F, -6.0F * v1 + 3.0F * v2, 3.0F * v1); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float apply(float x) {
/*  99 */       float t = x;
/* 100 */       for (int i = 0; i < 4; i++) {
/* 101 */         float gradient = this.xCurve.sampleGradient(t);
/* 102 */         if (gradient < 1.0E-5F) {
/*     */           break;
/*     */         }
/* 105 */         float error = this.xCurve.sample(t) - x;
/* 106 */         t -= error / gradient;
/*     */       } 
/* 108 */       return this.yCurve.sample(t);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 113 */     public boolean equals(Object obj) { if (obj instanceof CubicBezier) { CubicBezier bezier = (CubicBezier)obj; if (this.controls.equals(bezier.controls)); }  return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     public int hashCode() { return this.controls.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     public String toString() { return "CubicBezier(" + this.controls.x1 + ", " + this.controls.y1 + ", " + this.controls.x2 + ", " + this.controls.y2 + ")"; }
/*     */     private static final class CubicCurve extends Record { private final float a; private final float b; private final float c;
/*     */       
/* 126 */       private CubicCurve(float a, float b, float c) { this.a = a; this.b = b; this.c = c; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #126	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #126	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #126	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;
/* 126 */         //   0	8	1	o	Ljava/lang/Object; } public float a() { return this.a; } public float b() { return this.b; } public float c() { return this.c; }
/*     */       
/* 128 */       public float sample(float t) { return ((this.a * t + this.b) * t + this.c) * t; }
/*     */ 
/*     */ 
/*     */       
/* 132 */       public float sampleGradient(float t) { return (3.0F * this.a * t + 2.0F * this.b) * t + this.c; } } } private static final class CubicCurve extends Record { private final float a; private final float b; private final float c; private CubicCurve(float a, float b, float c) { this.a = a; this.b = b; this.c = c; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 132 */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezier$CubicCurve; } public float sampleGradient(float t) { return (3.0F * this.a * t + 2.0F * this.b) * t + this.c; } public final int hashCode() { // Byte code:
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
/*     */       //   0	8	1	o	Ljava/lang/Object; } public float a() { return this.a; }
/*     */     public float b() { return this.b; }
/*     */     public float c() { return this.c; }
/*     */     public float sample(float t) { return ((this.a * t + this.b) * t + this.c) * t; } }
/*     */   public static final class CubicBezierControls extends Record { private final float x1; private final float y1; private final float x2; private final float y2;
/* 137 */     public CubicBezierControls(float x1, float y1, float x2, float y2) { this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/EasingType$CubicBezierControls;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/EasingType$CubicBezierControls;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/EasingType$CubicBezierControls;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #137	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/EasingType$CubicBezierControls;
/* 137 */       //   0	8	1	o	Ljava/lang/Object; } public float x1() { return this.x1; } public float y1() { return this.y1; } public float x2() { return this.x2; } public float y2() { return this.y2; }
/*     */ 
/*     */ 
/*     */     
/* 141 */     public static final Codec<CubicBezierControls> CODEC = Codec.FLOAT.listOf(4, 4).xmap(floats -> 
/* 142 */         new CubicBezierControls(((Float)floats.get(0)).floatValue(), ((Float)floats.get(1)).floatValue(), ((Float)floats.get(2)).floatValue(), ((Float)floats.get(3)).floatValue()), controls -> 
/* 143 */         List.of(Float.valueOf(controls.x1), Float.valueOf(controls.y1), Float.valueOf(controls.x2), Float.valueOf(controls.y2)))
/* 144 */       .validate(CubicBezierControls::validate);
/*     */     
/*     */     private DataResult<CubicBezierControls> validate() {
/* 147 */       if (this.x1 < 0.0F || this.x1 > 1.0F)
/* 148 */         return DataResult.error(() -> "x1 must be in range [0; 1]"); 
/* 149 */       if (this.x2 < 0.0F || this.x2 > 1.0F) {
/* 150 */         return DataResult.error(() -> "x2 must be in range [0; 1]");
/*     */       }
/* 152 */       return DataResult.success(this);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\EasingType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */