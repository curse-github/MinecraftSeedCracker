/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ 
/*     */ public final class ImprovedNoise
/*     */ {
/*     */   private static final float SHIFT_UP_EPSILON = 1.0E-7F;
/*     */   private final byte[] p;
/*     */   public final double xo;
/*     */   public final double yo;
/*     */   public final double zo;
/*     */   
/*     */   public ImprovedNoise(RandomSource random) {
/*  17 */     this.xo = random.nextDouble() * 256.0D;
/*  18 */     this.yo = random.nextDouble() * 256.0D;
/*  19 */     this.zo = random.nextDouble() * 256.0D;
/*     */     
/*  21 */     this.p = new byte[256];
/*     */     
/*  23 */     for (int i = 0; i < 256; i++) {
/*  24 */       this.p[i] = (byte)i;
/*     */     }
/*     */     
/*  27 */     for (int i = 0; i < 256; i++) {
/*  28 */       int offset = random.nextInt(256 - i);
/*  29 */       byte tmp = this.p[i];
/*  30 */       this.p[i] = this.p[i + offset];
/*  31 */       this.p[i + offset] = tmp;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  37 */   public double noise(double _x, double _y, double _z) { return noise(_x, _y, _z, 0.0D, 0.0D); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public double noise(double _x, double _y, double _z, double yScale, double yFudge) {
/*  45 */     double yrFudge, x = _x + this.xo;
/*  46 */     double y = _y + this.yo;
/*  47 */     double z = _z + this.zo;
/*     */     
/*  49 */     int xf = Mth.floor(x);
/*  50 */     int yf = Mth.floor(y);
/*  51 */     int zf = Mth.floor(z);
/*     */ 
/*     */     
/*  54 */     double xr = x - xf;
/*  55 */     double yr = y - yf;
/*  56 */     double zr = z - zf;
/*     */ 
/*     */ 
/*     */     
/*  60 */     if (yScale != 0.0D) {
/*     */       double fudgeLimit;
/*     */       
/*  63 */       if (yFudge >= 0.0D && yFudge < yr) {
/*  64 */         fudgeLimit = yFudge;
/*     */       } else {
/*  66 */         fudgeLimit = yr;
/*     */       } 
/*     */       
/*  69 */       yrFudge = Mth.floor(fudgeLimit / yScale + 1.0000000116860974E-7D) * yScale;
/*     */     } else {
/*  71 */       yrFudge = 0.0D;
/*     */     } 
/*     */ 
/*     */     
/*  75 */     return sampleAndLerp(xf, yf, zf, xr, yr - yrFudge, zr, yr);
/*     */   }
/*     */   
/*     */   public double noiseWithDerivative(double _x, double _y, double _z, double[] derivativeOut) {
/*  79 */     double x = _x + this.xo;
/*  80 */     double y = _y + this.yo;
/*  81 */     double z = _z + this.zo;
/*     */     
/*  83 */     int xf = Mth.floor(x);
/*  84 */     int yf = Mth.floor(y);
/*  85 */     int zf = Mth.floor(z);
/*     */ 
/*     */     
/*  88 */     double xr = x - xf;
/*  89 */     double yr = y - yf;
/*  90 */     double zr = z - zf;
/*     */     
/*  92 */     return sampleWithDerivative(xf, yf, zf, xr, yr, zr, derivativeOut);
/*     */   }
/*     */ 
/*     */   
/*  96 */   private static double gradDot(int hash, double x, double y, double z) { return SimplexNoise.dot(SimplexNoise.GRADIENT[hash & 0xF], x, y, z); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   private int p(int x) { return this.p[x & 0xFF] & 0xFF; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double sampleAndLerp(int x, int y, int z, double xr, double yr, double zr, double yrOriginal) {
/* 106 */     int x0 = p(x);
/* 107 */     int x1 = p(x + 1);
/*     */     
/* 109 */     int xy00 = p(x0 + y);
/* 110 */     int xy01 = p(x0 + y + 1);
/* 111 */     int xy10 = p(x1 + y);
/* 112 */     int xy11 = p(x1 + y + 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     double d000 = gradDot(p(xy00 + z), xr, yr, zr);
/* 121 */     double d100 = gradDot(p(xy10 + z), xr - 1.0D, yr, zr);
/* 122 */     double d010 = gradDot(p(xy01 + z), xr, yr - 1.0D, zr);
/* 123 */     double d110 = gradDot(p(xy11 + z), xr - 1.0D, yr - 1.0D, zr);
/* 124 */     double d001 = gradDot(p(xy00 + z + 1), xr, yr, zr - 1.0D);
/* 125 */     double d101 = gradDot(p(xy10 + z + 1), xr - 1.0D, yr, zr - 1.0D);
/* 126 */     double d011 = gradDot(p(xy01 + z + 1), xr, yr - 1.0D, zr - 1.0D);
/* 127 */     double d111 = gradDot(p(xy11 + z + 1), xr - 1.0D, yr - 1.0D, zr - 1.0D);
/*     */ 
/*     */     
/* 130 */     double xAlpha = Mth.smoothstep(xr);
/* 131 */     double yAlpha = Mth.smoothstep(yrOriginal);
/* 132 */     double zAlpha = Mth.smoothstep(zr);
/*     */ 
/*     */     
/* 135 */     return Mth.lerp3(xAlpha, yAlpha, zAlpha, d000, d100, d010, d110, d001, d101, d011, d111);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private double sampleWithDerivative(int x, int y, int z, double xr, double yr, double zr, double[] derivativeOut) {
/* 141 */     int x0 = p(x);
/* 142 */     int x1 = p(x + 1);
/*     */     
/* 144 */     int xy00 = p(x0 + y);
/* 145 */     int xy01 = p(x0 + y + 1);
/* 146 */     int xy10 = p(x1 + y);
/* 147 */     int xy11 = p(x1 + y + 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     int p000 = p(xy00 + z);
/* 153 */     int p100 = p(xy10 + z);
/* 154 */     int p010 = p(xy01 + z);
/* 155 */     int p110 = p(xy11 + z);
/* 156 */     int p001 = p(xy00 + z + 1);
/* 157 */     int p101 = p(xy10 + z + 1);
/* 158 */     int p011 = p(xy01 + z + 1);
/* 159 */     int p111 = p(xy11 + z + 1);
/*     */     
/* 161 */     int[] g000 = SimplexNoise.GRADIENT[p000 & 0xF];
/* 162 */     int[] g100 = SimplexNoise.GRADIENT[p100 & 0xF];
/* 163 */     int[] g010 = SimplexNoise.GRADIENT[p010 & 0xF];
/* 164 */     int[] g110 = SimplexNoise.GRADIENT[p110 & 0xF];
/* 165 */     int[] g001 = SimplexNoise.GRADIENT[p001 & 0xF];
/* 166 */     int[] g101 = SimplexNoise.GRADIENT[p101 & 0xF];
/* 167 */     int[] g011 = SimplexNoise.GRADIENT[p011 & 0xF];
/* 168 */     int[] g111 = SimplexNoise.GRADIENT[p111 & 0xF];
/*     */     
/* 170 */     double d000 = SimplexNoise.dot(g000, xr, yr, zr);
/* 171 */     double d100 = SimplexNoise.dot(g100, xr - 1.0D, yr, zr);
/* 172 */     double d010 = SimplexNoise.dot(g010, xr, yr - 1.0D, zr);
/* 173 */     double d110 = SimplexNoise.dot(g110, xr - 1.0D, yr - 1.0D, zr);
/* 174 */     double d001 = SimplexNoise.dot(g001, xr, yr, zr - 1.0D);
/* 175 */     double d101 = SimplexNoise.dot(g101, xr - 1.0D, yr, zr - 1.0D);
/* 176 */     double d011 = SimplexNoise.dot(g011, xr, yr - 1.0D, zr - 1.0D);
/* 177 */     double d111 = SimplexNoise.dot(g111, xr - 1.0D, yr - 1.0D, zr - 1.0D);
/*     */     
/* 179 */     double xAlpha = Mth.smoothstep(xr);
/* 180 */     double yAlpha = Mth.smoothstep(yr);
/* 181 */     double zAlpha = Mth.smoothstep(zr);
/*     */     
/* 183 */     double d1x = Mth.lerp3(xAlpha, yAlpha, zAlpha, g000[0], g100[0], g010[0], g110[0], g001[0], g101[0], g011[0], g111[0]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     double d1y = Mth.lerp3(xAlpha, yAlpha, zAlpha, g000[1], g100[1], g010[1], g110[1], g001[1], g101[1], g011[1], g111[1]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     double d1z = Mth.lerp3(xAlpha, yAlpha, zAlpha, g000[2], g100[2], g010[2], g110[2], g001[2], g101[2], g011[2], g111[2]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     double d2x = Mth.lerp2(yAlpha, zAlpha, d100 - d000, d110 - d010, d101 - d001, d111 - d011);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     double d2y = Mth.lerp2(zAlpha, xAlpha, d010 - d000, d011 - d001, d110 - d100, d111 - d101);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     double d2z = Mth.lerp2(xAlpha, yAlpha, d001 - d000, d101 - d100, d011 - d010, d111 - d110);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     double xSD = Mth.smoothstepDerivative(xr);
/* 232 */     double ySD = Mth.smoothstepDerivative(yr);
/* 233 */     double zSD = Mth.smoothstepDerivative(zr);
/*     */     
/* 235 */     double dX = d1x + xSD * d2x;
/* 236 */     double dY = d1y + ySD * d2y;
/* 237 */     double dZ = d1z + zSD * d2z;
/*     */     
/* 239 */     derivativeOut[0] = derivativeOut[0] + dX;
/* 240 */     derivativeOut[1] = derivativeOut[1] + dY;
/* 241 */     derivativeOut[2] = derivativeOut[2] + dZ;
/*     */ 
/*     */     
/* 244 */     return Mth.lerp3(xAlpha, yAlpha, zAlpha, d000, d100, d010, d110, d001, d101, d011, d111);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 249 */   public void parityConfigString(StringBuilder sb) { NoiseUtils.parityNoiseOctaveConfigString(sb, this.xo, this.yo, this.zo, this.p); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\synth\ImprovedNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */