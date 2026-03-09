/*     */ package net.minecraft.world.level.levelgen.synth;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public class SimplexNoise {
/*     */   protected static final int[][] GRADIENT = { 
/*   7 */       { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 }, { 0, -1, 1 }, { -1, 1, 0 }, { 0, -1, -1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  26 */   private static final double SQRT_3 = Math.sqrt(3.0D);
/*  27 */   private static final double F2 = 0.5D * (SQRT_3 - 1.0D);
/*  28 */   private static final double G2 = (3.0D - SQRT_3) / 6.0D; private final int[] p; public final double xo;
/*     */   public SimplexNoise(RandomSource random) {
/*  30 */     this.p = new int[512];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     this.xo = random.nextDouble() * 256.0D;
/*  38 */     this.yo = random.nextDouble() * 256.0D;
/*  39 */     this.zo = random.nextDouble() * 256.0D;
/*  40 */     for (int i = 0; i < 256; i++) {
/*  41 */       this.p[i] = i;
/*     */     }
/*     */     
/*  44 */     for (int i = 0; i < 256; i++) {
/*  45 */       int offset = random.nextInt(256 - i);
/*  46 */       int tmp = this.p[i];
/*  47 */       this.p[i] = this.p[offset + i];
/*  48 */       this.p[offset + i] = tmp;
/*     */     } 
/*     */   }
/*     */   public final double yo; public final double zo;
/*     */   
/*  53 */   private int p(int x) { return this.p[x & 0xFF]; }
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected static double dot(int[] g, double x, double y, double z) { return g[0] * x + g[1] * y + g[2] * z; }
/*     */ 
/*     */ 
/*     */   
/*     */   private double getCornerNoise3D(int index, double x, double y, double z, double base) {
/*  62 */     double n0, t0 = base - x * x - y * y - z * z;
/*  63 */     if (t0 < 0.0D) {
/*  64 */       n0 = 0.0D;
/*     */     } else {
/*  66 */       t0 *= t0;
/*  67 */       n0 = t0 * t0 * dot(GRADIENT[index], x, y, z);
/*     */     } 
/*  69 */     return n0;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getValue(double xin, double yin) {
/*     */     int j1, i1;
/*  75 */     double s = (xin + yin) * F2;
/*  76 */     int i = Mth.floor(xin + s);
/*  77 */     int j = Mth.floor(yin + s);
/*     */ 
/*     */     
/*  80 */     double t = (i + j) * G2;
/*  81 */     double X0 = i - t;
/*  82 */     double Y0 = j - t;
/*     */ 
/*     */     
/*  85 */     double x0 = xin - X0;
/*  86 */     double y0 = yin - Y0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (x0 > y0) {
/*     */       
/*  96 */       i1 = 1;
/*  97 */       j1 = 0;
/*     */     } else {
/*     */       
/* 100 */       i1 = 0;
/* 101 */       j1 = 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     double x1 = x0 - i1 + G2;
/* 109 */     double y1 = y0 - j1 + G2;
/*     */ 
/*     */     
/* 112 */     double x2 = x0 - 1.0D + 2.0D * G2;
/* 113 */     double y2 = y0 - 1.0D + 2.0D * G2;
/*     */ 
/*     */     
/* 116 */     int ii = i & 0xFF;
/* 117 */     int jj = j & 0xFF;
/*     */     
/* 119 */     int gi0 = p(ii + p(jj)) % 12;
/* 120 */     int gi1 = p(ii + i1 + p(jj + j1)) % 12;
/* 121 */     int gi2 = p(ii + 1 + p(jj + 1)) % 12;
/*     */ 
/*     */ 
/*     */     
/* 125 */     double n0 = getCornerNoise3D(gi0, x0, y0, 0.0D, 0.5D);
/* 126 */     double n1 = getCornerNoise3D(gi1, x1, y1, 0.0D, 0.5D);
/* 127 */     double n2 = getCornerNoise3D(gi2, x2, y2, 0.0D, 0.5D);
/*     */ 
/*     */ 
/*     */     
/* 131 */     return 70.0D * (n0 + n1 + n2);
/*     */   }
/*     */   
/*     */   public double getValue(double xin, double yin, double zin) {
/*     */     int k2, j2, i2, k1, j1, i1;
/* 136 */     double F3 = 0.3333333333333333D;
/* 137 */     double s = (xin + yin + zin) * 0.3333333333333333D;
/*     */     
/* 139 */     int i = Mth.floor(xin + s);
/* 140 */     int j = Mth.floor(yin + s);
/* 141 */     int k = Mth.floor(zin + s);
/* 142 */     double G3 = 0.16666666666666666D;
/* 143 */     double t = (i + j + k) * 0.16666666666666666D;
/*     */     
/* 145 */     double X0 = i - t;
/* 146 */     double Y0 = j - t;
/* 147 */     double Z0 = k - t;
/*     */     
/* 149 */     double x0 = xin - X0;
/* 150 */     double y0 = yin - Y0;
/* 151 */     double z0 = zin - Z0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     if (x0 >= y0) {
/* 161 */       if (y0 >= z0) {
/*     */         
/* 163 */         i1 = 1;
/* 164 */         j1 = 0;
/* 165 */         k1 = 0;
/* 166 */         i2 = 1;
/* 167 */         j2 = 1;
/* 168 */         k2 = 0;
/* 169 */       } else if (x0 >= z0) {
/*     */         
/* 171 */         i1 = 1;
/* 172 */         j1 = 0;
/* 173 */         k1 = 0;
/* 174 */         i2 = 1;
/* 175 */         j2 = 0;
/* 176 */         k2 = 1;
/*     */       } else {
/*     */         
/* 179 */         i1 = 0;
/* 180 */         j1 = 0;
/* 181 */         k1 = 1;
/* 182 */         i2 = 1;
/* 183 */         j2 = 0;
/* 184 */         k2 = 1;
/*     */       }
/*     */     
/*     */     }
/* 188 */     else if (y0 < z0) {
/*     */       
/* 190 */       i1 = 0;
/* 191 */       j1 = 0;
/* 192 */       k1 = 1;
/* 193 */       i2 = 0;
/* 194 */       j2 = 1;
/* 195 */       k2 = 1;
/* 196 */     } else if (x0 < z0) {
/*     */       
/* 198 */       i1 = 0;
/* 199 */       j1 = 1;
/* 200 */       k1 = 0;
/* 201 */       i2 = 0;
/* 202 */       j2 = 1;
/* 203 */       k2 = 1;
/*     */     } else {
/*     */       
/* 206 */       i1 = 0;
/* 207 */       j1 = 1;
/* 208 */       k1 = 0;
/* 209 */       i2 = 1;
/* 210 */       j2 = 1;
/* 211 */       k2 = 0;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     double x1 = x0 - i1 + 0.16666666666666666D;
/* 221 */     double y1 = y0 - j1 + 0.16666666666666666D;
/* 222 */     double z1 = z0 - k1 + 0.16666666666666666D;
/*     */ 
/*     */     
/* 225 */     double x2 = x0 - i2 + 0.3333333333333333D;
/* 226 */     double y2 = y0 - j2 + 0.3333333333333333D;
/* 227 */     double z2 = z0 - k2 + 0.3333333333333333D;
/*     */ 
/*     */     
/* 230 */     double x3 = x0 - 1.0D + 0.5D;
/* 231 */     double y3 = y0 - 1.0D + 0.5D;
/* 232 */     double z3 = z0 - 1.0D + 0.5D;
/*     */ 
/*     */     
/* 235 */     int ii = i & 0xFF;
/* 236 */     int jj = j & 0xFF;
/* 237 */     int kk = k & 0xFF;
/*     */     
/* 239 */     int gi0 = p(ii + p(jj + p(kk))) % 12;
/* 240 */     int gi1 = p(ii + i1 + p(jj + j1 + p(kk + k1))) % 12;
/* 241 */     int gi2 = p(ii + i2 + p(jj + j2 + p(kk + k2))) % 12;
/* 242 */     int gi3 = p(ii + 1 + p(jj + 1 + p(kk + 1))) % 12;
/*     */ 
/*     */     
/* 245 */     double n0 = getCornerNoise3D(gi0, x0, y0, z0, 0.6D);
/* 246 */     double n1 = getCornerNoise3D(gi1, x1, y1, z1, 0.6D);
/* 247 */     double n2 = getCornerNoise3D(gi2, x2, y2, z2, 0.6D);
/* 248 */     double n3 = getCornerNoise3D(gi3, x3, y3, z3, 0.6D);
/*     */ 
/*     */ 
/*     */     
/* 252 */     return 32.0D * (n0 + n1 + n2 + n3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\synth\SimplexNoise.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */