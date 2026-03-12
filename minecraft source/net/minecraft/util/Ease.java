/*     */ package net.minecraft.util;
/*     */ 
/*     */ public class Ease
/*     */ {
/*     */   public static float inBack(float x) {
/*   6 */     float c1 = 1.70158F;
/*   7 */     float c3 = 2.70158F;
/*   8 */     return Mth.square(x) * (2.70158F * x - 1.70158F);
/*     */   }
/*     */ 
/*     */   
/*  12 */   public static float inBounce(float x) { return 1.0F - outBounce(1.0F - x); }
/*     */ 
/*     */ 
/*     */   
/*  16 */   public static float inCubic(float x) { return Mth.cube(x); }
/*     */ 
/*     */   
/*     */   public static float inElastic(float x) {
/*  20 */     if (x == 0.0F) {
/*  21 */       return 0.0F;
/*     */     }
/*  23 */     if (x == 1.0F) {
/*  24 */       return 1.0F;
/*     */     }
/*  26 */     float c4 = 2.0943952F;
/*  27 */     return (float)(-Math.pow(2.0D, 10.0D * x - 10.0D) * Math.sin((x * 10.0D - 10.75D) * 2.094395160675049D));
/*     */   }
/*     */ 
/*     */   
/*  31 */   public static float inExpo(float x) { return (x == 0.0F) ? 0.0F : (float)Math.pow(2.0D, 10.0D * x - 10.0D); }
/*     */ 
/*     */ 
/*     */   
/*  35 */   public static float inQuart(float x) { return Mth.square(Mth.square(x)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static float inQuint(float x) { return Mth.square(Mth.square(x)) * x; }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static float inSine(float x) { return 1.0F - Mth.cos((x * 1.5707964F)); }
/*     */ 
/*     */   
/*     */   public static float inOutBounce(float x) {
/*  47 */     if (x < 0.5F) {
/*  48 */       return (1.0F - outBounce(1.0F - 2.0F * x)) / 2.0F;
/*     */     }
/*  50 */     return (1.0F + outBounce(2.0F * x - 1.0F)) / 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutCirc(float x) {
/*  55 */     if (x < 0.5F) {
/*  56 */       return (float)((1.0D - Math.sqrt(1.0D - Math.pow(2.0D * x, 2.0D))) / 2.0D);
/*     */     }
/*  58 */     return (float)((Math.sqrt(1.0D - Math.pow(-2.0D * x + 2.0D, 2.0D)) + 1.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutCubic(float x) {
/*  63 */     if (x < 0.5F) {
/*  64 */       return 4.0F * Mth.cube(x);
/*     */     }
/*  66 */     return (float)(1.0D - Math.pow(-2.0D * x + 2.0D, 3.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutQuad(float x) {
/*  71 */     if (x < 0.5F) {
/*  72 */       return 2.0F * Mth.square(x);
/*     */     }
/*  74 */     return (float)(1.0D - Math.pow(-2.0D * x + 2.0D, 2.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutQuart(float x) {
/*  79 */     if (x < 0.5F) {
/*  80 */       return 8.0F * Mth.square(Mth.square(x));
/*     */     }
/*  82 */     return (float)(1.0D - Math.pow(-2.0D * x + 2.0D, 4.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float inOutQuint(float x) {
/*  87 */     if (x < 0.5D) {
/*  88 */       return 16.0F * x * x * x * x * x;
/*     */     }
/*  90 */     return (float)(1.0D - Math.pow(-2.0D * x + 2.0D, 5.0D) / 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public static float outBounce(float x) {
/*  95 */     float n1 = 7.5625F;
/*  96 */     float d1 = 2.75F;
/*  97 */     if (x < 0.36363637F)
/*  98 */       return 7.5625F * Mth.square(x); 
/*  99 */     if (x < 0.72727275F)
/* 100 */       return 7.5625F * Mth.square(x - 0.54545456F) + 0.75F; 
/* 101 */     if (x < 0.9090909090909091D) {
/* 102 */       return 7.5625F * Mth.square(x - 0.8181818F) + 0.9375F;
/*     */     }
/* 104 */     return 7.5625F * Mth.square(x - 0.95454544F) + 0.984375F;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float outElastic(float x) {
/* 109 */     float c4 = 2.0943952F;
/* 110 */     if (x == 0.0F) {
/* 111 */       return 0.0F;
/*     */     }
/* 113 */     if (x == 1.0F) {
/* 114 */       return 1.0F;
/*     */     }
/* 116 */     return (float)(Math.pow(2.0D, -10.0D * x) * Math.sin((x * 10.0D - 0.75D) * 2.094395160675049D) + 1.0D);
/*     */   }
/*     */   
/*     */   public static float outExpo(float x) {
/* 120 */     if (x == 1.0F) {
/* 121 */       return 1.0F;
/*     */     }
/* 123 */     return 1.0F - (float)Math.pow(2.0D, -10.0D * x);
/*     */   }
/*     */ 
/*     */   
/* 127 */   public static float outQuad(float x) { return 1.0F - Mth.square(1.0F - x); }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static float outQuint(float x) { return 1.0F - (float)Math.pow(1.0D - x, 5.0D); }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public static float outSine(float x) { return Mth.sin((x * 1.5707964F)); }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public static float inOutSine(float x) { return -(Mth.cos((3.1415927F * x)) - 1.0F) / 2.0F; }
/*     */ 
/*     */   
/*     */   public static float outBack(float x) {
/* 143 */     float c1 = 1.70158F;
/* 144 */     float c3 = 2.70158F;
/* 145 */     return 1.0F + 2.70158F * Mth.cube(x - 1.0F) + 1.70158F * Mth.square(x - 1.0F);
/*     */   }
/*     */ 
/*     */   
/* 149 */   public static float outQuart(float x) { return 1.0F - Mth.square(Mth.square(1.0F - x)); }
/*     */ 
/*     */ 
/*     */   
/* 153 */   public static float outCubic(float x) { return 1.0F - Mth.cube(1.0F - x); }
/*     */ 
/*     */   
/*     */   public static float inOutExpo(float x) {
/* 157 */     if (x < 0.5F) {
/* 158 */       return (x == 0.0F) ? 0.0F : (float)(Math.pow(2.0D, 20.0D * x - 10.0D) / 2.0D);
/*     */     }
/* 160 */     return (x == 1.0F) ? 1.0F : (float)((2.0D - Math.pow(2.0D, -20.0D * x + 10.0D)) / 2.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 165 */   public static float inQuad(float x) { return x * x; }
/*     */ 
/*     */ 
/*     */   
/* 169 */   public static float outCirc(float x) { return (float)Math.sqrt((1.0F - Mth.square(x - 1.0F))); }
/*     */ 
/*     */   
/*     */   public static float inOutElastic(float x) {
/* 173 */     float c5 = 1.3962635F;
/* 174 */     if (x == 0.0F) {
/* 175 */       return 0.0F;
/*     */     }
/* 177 */     if (x == 1.0F) {
/* 178 */       return 1.0F;
/*     */     }
/* 180 */     double sin = Math.sin((20.0D * x - 11.125D) * 1.3962634801864624D);
/* 181 */     if (x < 0.5F) {
/* 182 */       return (float)(-(Math.pow(2.0D, 20.0D * x - 10.0D) * sin) / 2.0D);
/*     */     }
/* 184 */     return (float)(Math.pow(2.0D, -20.0D * x + 10.0D) * sin / 2.0D + 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public static float inCirc(float x) { return (float)-Math.sqrt((1.0F - x * x)) + 1.0F; }
/*     */ 
/*     */   
/*     */   public static float inOutBack(float x) {
/* 193 */     float c1 = 1.70158F;
/* 194 */     float c2 = 2.5949094F;
/* 195 */     if (x < 0.5F) {
/* 196 */       return 4.0F * x * x * (7.189819F * x - 2.5949094F) / 2.0F;
/*     */     }
/* 198 */     float dt = 2.0F * x - 2.0F;
/* 199 */     return (dt * dt * (3.5949094F * dt + 2.5949094F) + 2.0F) / 2.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Ease.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */