/*     */ package net.minecraft.util;
/*     */ 
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector4f;
/*     */ 
/*     */ 
/*     */ public class ARGB
/*     */ {
/*     */   private static final int LINEAR_CHANNEL_DEPTH = 1024;
/*  11 */   private static final short[] SRGB_TO_LINEAR = (short[])Util.make(new short[256], lookup -> {
/*  12 */         for (int i = 0; i < lookup.length; i++) {
/*  13 */           float channel = i / 255.0F;
/*  14 */           lookup[i] = (short)Math.round(computeSrgbToLinear(channel) * 1023.0F);
/*     */         } 
/*     */       });
/*  17 */   private static final byte[] LINEAR_TO_SRGB = (byte[])Util.make(new byte[1024], lookup -> {
/*  18 */         for (int i = 0; i < lookup.length; i++) {
/*  19 */           float channel = i / 1023.0F;
/*  20 */           lookup[i] = (byte)Math.round(computeLinearToSrgb(channel) * 255.0F);
/*     */         } 
/*     */       });
/*     */ 
/*     */   
/*     */   private static float computeSrgbToLinear(float x) {
/*  26 */     if (x >= 0.04045F) {
/*  27 */       return (float)Math.pow((x + 0.055D) / 1.055D, 2.4D);
/*     */     }
/*  29 */     return x / 12.92F;
/*     */   }
/*     */ 
/*     */   
/*     */   private static float computeLinearToSrgb(float x) {
/*  34 */     if (x >= 0.0031308F) {
/*  35 */       return (float)(1.055D * Math.pow(x, 0.4166666666666667D) - 0.055D);
/*     */     }
/*  37 */     return 12.92F * x;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static float srgbToLinearChannel(int srgb) { return SRGB_TO_LINEAR[srgb] / 1023.0F; }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static int linearToSrgbChannel(float linear) { return LINEAR_TO_SRGB[Mth.floor(linear * 1023.0F)] & 0xFF; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int meanLinear(int srgb1, int srgb2, int srgb3, int srgb4) {
/*  53 */     return color((
/*  54 */         alpha(srgb1) + alpha(srgb2) + alpha(srgb3) + alpha(srgb4)) / 4, 
/*  55 */         linearChannelMean(red(srgb1), red(srgb2), red(srgb3), red(srgb4)), 
/*  56 */         linearChannelMean(green(srgb1), green(srgb2), green(srgb3), green(srgb4)), 
/*  57 */         linearChannelMean(blue(srgb1), blue(srgb2), blue(srgb3), blue(srgb4)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int linearChannelMean(int c1, int c2, int c3, int c4) {
/*  62 */     int linear = (SRGB_TO_LINEAR[c1] + SRGB_TO_LINEAR[c2] + SRGB_TO_LINEAR[c3] + SRGB_TO_LINEAR[c4]) / 4;
/*  63 */     return LINEAR_TO_SRGB[linear] & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*  67 */   public static int alpha(int color) { return color >>> 24; }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static int red(int color) { return color >> 16 & 0xFF; }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static int green(int color) { return color >> 8 & 0xFF; }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public static int blue(int color) { return color & 0xFF; }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public static int color(int alpha, int red, int green, int blue) { return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF; }
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static int color(int red, int green, int blue) { return color(255, red, green, blue); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static int color(Vec3 vec) { return color(as8BitChannel((float)vec.x()), as8BitChannel((float)vec.y()), as8BitChannel((float)vec.z())); }
/*     */ 
/*     */   
/*     */   public static int multiply(int lhs, int rhs) {
/*  95 */     if (lhs == -1)
/*  96 */       return rhs; 
/*  97 */     if (rhs == -1) {
/*  98 */       return lhs;
/*     */     }
/* 100 */     return color(
/* 101 */         alpha(lhs) * alpha(rhs) / 255, 
/* 102 */         red(lhs) * red(rhs) / 255, 
/* 103 */         green(lhs) * green(rhs) / 255, 
/* 104 */         blue(lhs) * blue(rhs) / 255);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int addRgb(int lhs, int rhs) {
/* 109 */     return color(
/* 110 */         alpha(lhs), 
/* 111 */         Math.min(red(lhs) + red(rhs), 255), 
/* 112 */         Math.min(green(lhs) + green(rhs), 255), 
/* 113 */         Math.min(blue(lhs) + blue(rhs), 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int subtractRgb(int lhs, int rhs) {
/* 118 */     return color(
/* 119 */         alpha(lhs), 
/* 120 */         Math.max(red(lhs) - red(rhs), 0), 
/* 121 */         Math.max(green(lhs) - green(rhs), 0), 
/* 122 */         Math.max(blue(lhs) - blue(rhs), 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int multiplyAlpha(int color, float alphaMultiplier) {
/* 127 */     if (color == 0 || alphaMultiplier <= 0.0F)
/* 128 */       return 0; 
/* 129 */     if (alphaMultiplier >= 1.0F) {
/* 130 */       return color;
/*     */     }
/* 132 */     return color(alphaFloat(color) * alphaMultiplier, color);
/*     */   }
/*     */ 
/*     */   
/* 136 */   public static int scaleRGB(int color, float scale) { return scaleRGB(color, scale, scale, scale); }
/*     */ 
/*     */   
/*     */   public static int scaleRGB(int color, float scaleR, float scaleG, float scaleB) {
/* 140 */     return color(
/* 141 */         alpha(color), 
/* 142 */         Math.clamp((int)(red(color) * scaleR), 0, 255), 
/* 143 */         Math.clamp((int)(green(color) * scaleG), 0, 255), 
/* 144 */         Math.clamp((int)(blue(color) * scaleB), 0, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int scaleRGB(int color, int scale) {
/* 149 */     return color(
/* 150 */         alpha(color), 
/* 151 */         Math.clamp(red(color) * scale / 255L, 0, 255), 
/* 152 */         Math.clamp(green(color) * scale / 255L, 0, 255), 
/* 153 */         Math.clamp(blue(color) * scale / 255L, 0, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int greyscale(int color) {
/* 158 */     int greyscale = (int)(red(color) * 0.3F + green(color) * 0.59F + blue(color) * 0.11F);
/* 159 */     return color(alpha(color), greyscale, greyscale, greyscale);
/*     */   }
/*     */   
/*     */   public static int alphaBlend(int destination, int source) {
/* 163 */     int destinationAlpha = alpha(destination);
/* 164 */     int sourceAlpha = alpha(source);
/* 165 */     if (sourceAlpha == 255)
/* 166 */       return source; 
/* 167 */     if (sourceAlpha == 0) {
/* 168 */       return destination;
/*     */     }
/* 170 */     int alpha = sourceAlpha + destinationAlpha * (255 - sourceAlpha) / 255;
/* 171 */     return color(alpha, 
/* 172 */         alphaBlendChannel(alpha, sourceAlpha, red(destination), red(source)), 
/* 173 */         alphaBlendChannel(alpha, sourceAlpha, green(destination), green(source)), 
/* 174 */         alphaBlendChannel(alpha, sourceAlpha, blue(destination), blue(source)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 180 */   private static int alphaBlendChannel(int resultAlpha, int sourceAlpha, int destination, int source) { return (source * sourceAlpha + destination * (resultAlpha - sourceAlpha)) / resultAlpha; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int srgbLerp(float alpha, int p0, int p1) {
/* 189 */     int a = Mth.lerpInt(alpha, alpha(p0), alpha(p1));
/* 190 */     int red = Mth.lerpInt(alpha, red(p0), red(p1));
/* 191 */     int green = Mth.lerpInt(alpha, green(p0), green(p1));
/* 192 */     int blue = Mth.lerpInt(alpha, blue(p0), blue(p1));
/* 193 */     return color(a, red, green, blue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int linearLerp(float alpha, int p0, int p1) {
/* 201 */     return color(
/* 202 */         Mth.lerpInt(alpha, alpha(p0), alpha(p1)), LINEAR_TO_SRGB[
/* 203 */           Mth.lerpInt(alpha, SRGB_TO_LINEAR[red(p0)], SRGB_TO_LINEAR[red(p1)])] & 0xFF, LINEAR_TO_SRGB[
/* 204 */           Mth.lerpInt(alpha, SRGB_TO_LINEAR[green(p0)], SRGB_TO_LINEAR[green(p1)])] & 0xFF, LINEAR_TO_SRGB[
/* 205 */           Mth.lerpInt(alpha, SRGB_TO_LINEAR[blue(p0)], SRGB_TO_LINEAR[blue(p1)])] & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 210 */   public static int opaque(int color) { return color | 0xFF000000; }
/*     */ 
/*     */ 
/*     */   
/* 214 */   public static int transparent(int color) { return color & 0xFFFFFF; }
/*     */ 
/*     */ 
/*     */   
/* 218 */   public static int color(int alpha, int rgb) { return alpha << 24 | rgb & 0xFFFFFF; }
/*     */ 
/*     */ 
/*     */   
/* 222 */   public static int color(float alpha, int rgb) { return as8BitChannel(alpha) << 24 | rgb & 0xFFFFFF; }
/*     */ 
/*     */ 
/*     */   
/* 226 */   public static int white(float alpha) { return as8BitChannel(alpha) << 24 | 0xFFFFFF; }
/*     */ 
/*     */ 
/*     */   
/* 230 */   public static int white(int alpha) { return alpha << 24 | 0xFFFFFF; }
/*     */ 
/*     */ 
/*     */   
/* 234 */   public static int black(float alpha) { return as8BitChannel(alpha) << 24; }
/*     */ 
/*     */ 
/*     */   
/* 238 */   public static int black(int alpha) { return alpha << 24; }
/*     */ 
/*     */   
/*     */   public static int colorFromFloat(float alpha, float red, float green, float blue) {
/* 242 */     return color(
/* 243 */         as8BitChannel(alpha), 
/* 244 */         as8BitChannel(red), 
/* 245 */         as8BitChannel(green), 
/* 246 */         as8BitChannel(blue));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 251 */   public static Vector3f vector3fFromRGB24(int color) { return new Vector3f(redFloat(color), greenFloat(color), blueFloat(color)); }
/*     */ 
/*     */ 
/*     */   
/* 255 */   public static Vector4f vector4fFromARGB32(int color) { return new Vector4f(redFloat(color), greenFloat(color), blueFloat(color), alphaFloat(color)); }
/*     */ 
/*     */   
/*     */   public static int average(int lhs, int rhs) {
/* 259 */     return color((
/* 260 */         alpha(lhs) + alpha(rhs)) / 2, (
/* 261 */         red(lhs) + red(rhs)) / 2, (
/* 262 */         green(lhs) + green(rhs)) / 2, (
/* 263 */         blue(lhs) + blue(rhs)) / 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 268 */   public static int as8BitChannel(float value) { return Mth.floor(value * 255.0F); }
/*     */ 
/*     */ 
/*     */   
/* 272 */   public static float alphaFloat(int color) { return from8BitChannel(alpha(color)); }
/*     */ 
/*     */ 
/*     */   
/* 276 */   public static float redFloat(int color) { return from8BitChannel(red(color)); }
/*     */ 
/*     */ 
/*     */   
/* 280 */   public static float greenFloat(int color) { return from8BitChannel(green(color)); }
/*     */ 
/*     */ 
/*     */   
/* 284 */   public static float blueFloat(int color) { return from8BitChannel(blue(color)); }
/*     */ 
/*     */ 
/*     */   
/* 288 */   private static float from8BitChannel(int value) { return value / 255.0F; }
/*     */ 
/*     */ 
/*     */   
/* 292 */   public static int toABGR(int color) { return color & 0xFF00FF00 | (color & 0xFF0000) >> 16 | (color & 0xFF) << 16; }
/*     */ 
/*     */ 
/*     */   
/* 296 */   public static int fromABGR(int color) { return toABGR(color); }
/*     */   
/*     */   public static int setBrightness(int color, float brightness) {
/*     */     float hue, saturation;
/* 300 */     int red = red(color);
/* 301 */     int green = green(color);
/* 302 */     int blue = blue(color);
/* 303 */     int alpha = alpha(color);
/*     */ 
/*     */     
/* 306 */     int rgbMax = Math.max(Math.max(red, green), blue);
/* 307 */     int rgbMin = Math.min(Math.min(red, green), blue);
/* 308 */     float rgbConstantRange = (rgbMax - rgbMin);
/*     */ 
/*     */     
/* 311 */     if (rgbMax != 0) {
/* 312 */       saturation = rgbConstantRange / rgbMax;
/*     */     } else {
/* 314 */       saturation = 0.0F;
/*     */     } 
/*     */ 
/*     */     
/* 318 */     if (saturation == 0.0F) {
/* 319 */       hue = 0.0F;
/*     */     } else {
/* 321 */       float constantRed = (rgbMax - red) / rgbConstantRange;
/* 322 */       float constantGreen = (rgbMax - green) / rgbConstantRange;
/* 323 */       float constantBlue = (rgbMax - blue) / rgbConstantRange;
/*     */       
/* 325 */       if (red == rgbMax) {
/* 326 */         hue = constantBlue - constantGreen;
/* 327 */       } else if (green == rgbMax) {
/* 328 */         hue = 2.0F + constantRed - constantBlue;
/*     */       } else {
/* 330 */         hue = 4.0F + constantGreen - constantRed;
/*     */       } 
/*     */       
/* 333 */       hue /= 6.0F;
/*     */       
/* 335 */       if (hue < 0.0F) {
/* 336 */         hue++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 341 */     if (saturation == 0.0F) {
/* 342 */       red = green = blue = Math.round(brightness * 255.0F);
/* 343 */       return color(alpha, red, green, blue);
/*     */     } 
/*     */     
/* 346 */     float colorWheelSegment = (hue - (float)Math.floor(hue)) * 6.0F;
/* 347 */     float colorWheelOffset = colorWheelSegment - (float)Math.floor(colorWheelSegment);
/* 348 */     float primaryColor = brightness * (1.0F - saturation);
/* 349 */     float secondaryColor = brightness * (1.0F - saturation * colorWheelOffset);
/* 350 */     float tertiaryColor = brightness * (1.0F - saturation * (1.0F - colorWheelOffset));
/*     */     
/* 352 */     switch ((int)colorWheelSegment) {
/*     */       case 0:
/* 354 */         red = Math.round(brightness * 255.0F);
/* 355 */         green = Math.round(tertiaryColor * 255.0F);
/* 356 */         blue = Math.round(primaryColor * 255.0F);
/*     */         break;
/*     */       case 1:
/* 359 */         red = Math.round(secondaryColor * 255.0F);
/* 360 */         green = Math.round(brightness * 255.0F);
/* 361 */         blue = Math.round(primaryColor * 255.0F);
/*     */         break;
/*     */       case 2:
/* 364 */         red = Math.round(primaryColor * 255.0F);
/* 365 */         green = Math.round(brightness * 255.0F);
/* 366 */         blue = Math.round(tertiaryColor * 255.0F);
/*     */         break;
/*     */       case 3:
/* 369 */         red = Math.round(primaryColor * 255.0F);
/* 370 */         green = Math.round(secondaryColor * 255.0F);
/* 371 */         blue = Math.round(brightness * 255.0F);
/*     */         break;
/*     */       case 4:
/* 374 */         red = Math.round(tertiaryColor * 255.0F);
/* 375 */         green = Math.round(primaryColor * 255.0F);
/* 376 */         blue = Math.round(brightness * 255.0F);
/*     */         break;
/*     */       case 5:
/* 379 */         red = Math.round(brightness * 255.0F);
/* 380 */         green = Math.round(primaryColor * 255.0F);
/* 381 */         blue = Math.round(secondaryColor * 255.0F);
/*     */         break;
/*     */     } 
/*     */     
/* 385 */     return color(alpha, red, green, blue);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ARGB.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */