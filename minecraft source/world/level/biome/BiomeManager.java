/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.hash.Hashing;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.util.LinearCongruentialGenerator;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class BiomeManager
/*     */ {
/*  12 */   public static final int CHUNK_CENTER_QUART = QuartPos.fromBlock(8);
/*     */   
/*     */   private static final int ZOOM_BITS = 2;
/*     */   
/*     */   private static final int ZOOM = 4;
/*     */   private static final int ZOOM_MASK = 3;
/*     */   private final NoiseBiomeSource noiseBiomeSource;
/*     */   private final long biomeZoomSeed;
/*     */   
/*     */   public BiomeManager(NoiseBiomeSource noiseBiomeSource, long seed) {
/*  22 */     this.noiseBiomeSource = noiseBiomeSource;
/*  23 */     this.biomeZoomSeed = seed;
/*     */   }
/*     */ 
/*     */   
/*  27 */   public static long obfuscateSeed(long seed) { return Hashing.sha256().hashLong(seed).asLong(); }
/*     */ 
/*     */ 
/*     */   
/*  31 */   public BiomeManager withDifferentSource(NoiseBiomeSource biomeSource) { return new BiomeManager(biomeSource, this.biomeZoomSeed); }
/*     */ 
/*     */   
/*     */   public Holder<Biome> getBiome(BlockPos pos) {
/*  35 */     int absX = pos.getX() - 2;
/*  36 */     int absY = pos.getY() - 2;
/*  37 */     int absZ = pos.getZ() - 2;
/*     */     
/*  39 */     int parentX = absX >> 2;
/*  40 */     int parentY = absY >> 2;
/*  41 */     int parentZ = absZ >> 2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     double fractX = (absX & 0x3) / 4.0D;
/*  47 */     double fractY = (absY & 0x3) / 4.0D;
/*  48 */     double fractZ = (absZ & 0x3) / 4.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     int minI = 0;
/*  54 */     double minFiddledDistance = Double.POSITIVE_INFINITY;
/*  55 */     for (int i = 0; i < 8; i++) {
/*  56 */       boolean xEven = ((i & 0x4) == 0);
/*  57 */       boolean yEven = ((i & 0x2) == 0);
/*  58 */       boolean zEven = ((i & true) == 0);
/*     */       
/*  60 */       int cornerX = xEven ? parentX : (parentX + 1);
/*  61 */       int cornerY = yEven ? parentY : (parentY + 1);
/*  62 */       int cornerZ = zEven ? parentZ : (parentZ + 1);
/*     */       
/*  64 */       double distanceX = xEven ? fractX : (fractX - 1.0D);
/*  65 */       double distanceY = yEven ? fractY : (fractY - 1.0D);
/*  66 */       double distanceZ = zEven ? fractZ : (fractZ - 1.0D);
/*     */       
/*  68 */       double next = getFiddledDistance(this.biomeZoomSeed, cornerX, cornerY, cornerZ, distanceX, distanceY, distanceZ);
/*  69 */       if (minFiddledDistance > next) {
/*  70 */         minI = i;
/*  71 */         minFiddledDistance = next;
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     int biomeX = ((minI & 0x4) == 0) ? parentX : (parentX + 1);
/*  76 */     int biomeY = ((minI & 0x2) == 0) ? parentY : (parentY + 1);
/*  77 */     int biomeZ = ((minI & true) == 0) ? parentZ : (parentZ + 1);
/*     */     
/*  79 */     return this.noiseBiomeSource.getNoiseBiome(biomeX, biomeY, biomeZ);
/*     */   }
/*     */   
/*     */   public Holder<Biome> getNoiseBiomeAtPosition(double x, double y, double z) {
/*  83 */     int quartX = QuartPos.fromBlock(Mth.floor(x));
/*  84 */     int quartY = QuartPos.fromBlock(Mth.floor(y));
/*  85 */     int quartZ = QuartPos.fromBlock(Mth.floor(z));
/*  86 */     return getNoiseBiomeAtQuart(quartX, quartY, quartZ);
/*     */   }
/*     */   
/*     */   public Holder<Biome> getNoiseBiomeAtPosition(BlockPos blockPos) {
/*  90 */     int quartX = QuartPos.fromBlock(blockPos.getX());
/*  91 */     int quartY = QuartPos.fromBlock(blockPos.getY());
/*  92 */     int quartZ = QuartPos.fromBlock(blockPos.getZ());
/*  93 */     return getNoiseBiomeAtQuart(quartX, quartY, quartZ);
/*     */   }
/*     */ 
/*     */   
/*  97 */   public Holder<Biome> getNoiseBiomeAtQuart(int quartX, int quartY, int quartZ) { return this.noiseBiomeSource.getNoiseBiome(quartX, quartY, quartZ); }
/*     */ 
/*     */   
/*     */   private static double getFiddledDistance(long seed, int xRandom, int yRandom, int zRandom, double distanceX, double distanceY, double distanceZ) {
/* 101 */     long rval = seed;
/*     */     
/* 103 */     rval = LinearCongruentialGenerator.next(rval, xRandom);
/* 104 */     rval = LinearCongruentialGenerator.next(rval, yRandom);
/* 105 */     rval = LinearCongruentialGenerator.next(rval, zRandom);
/* 106 */     rval = LinearCongruentialGenerator.next(rval, xRandom);
/* 107 */     rval = LinearCongruentialGenerator.next(rval, yRandom);
/* 108 */     rval = LinearCongruentialGenerator.next(rval, zRandom);
/*     */     
/* 110 */     double fiddleX = getFiddle(rval);
/*     */     
/* 112 */     rval = LinearCongruentialGenerator.next(rval, seed);
/*     */     
/* 114 */     double fiddleY = getFiddle(rval);
/*     */     
/* 116 */     rval = LinearCongruentialGenerator.next(rval, seed);
/*     */     
/* 118 */     double fiddleZ = getFiddle(rval);
/*     */     
/* 120 */     return Mth.square(distanceZ + fiddleZ) + Mth.square(distanceY + fiddleY) + Mth.square(distanceX + fiddleX);
/*     */   }
/*     */   
/*     */   private static double getFiddle(long rval) {
/* 124 */     double uniform = Math.floorMod(rval >> 24, 1024) / 1024.0D;
/* 125 */     return (uniform - 0.5D) * 0.9D;
/*     */   }
/*     */   
/*     */   public static interface NoiseBiomeSource {
/*     */     Holder<Biome> getNoiseBiome(int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\BiomeManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */