/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
/*     */ 
/*     */ public class IcebergFeature
/*     */   extends Feature<BlockStateConfiguration> {
/*  16 */   public IcebergFeature(Codec<BlockStateConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
/*  21 */     BlockPos origin = context.origin();
/*  22 */     WorldGenLevel level = context.level();
/*  23 */     origin = new BlockPos(origin.getX(), context.chunkGenerator().getSeaLevel(), origin.getZ());
/*  24 */     RandomSource random = context.random();
/*  25 */     boolean snowOnTop = (random.nextDouble() > 0.7D);
/*  26 */     BlockState mainBlockState = ((BlockStateConfiguration)context.config()).state;
/*     */ 
/*     */     
/*  29 */     double shapeAngle = random.nextDouble() * 2.0D * Math.PI;
/*  30 */     int shapeEllipseA = 11 - random.nextInt(5);
/*  31 */     int shapeEllipseC = 3 + random.nextInt(3);
/*  32 */     boolean isEllipse = (random.nextDouble() > 0.7D);
/*     */     
/*  34 */     int maxWidthRoundIceberg = 11;
/*  35 */     int overWaterHeight = isEllipse ? (random.nextInt(6) + 6) : (random.nextInt(15) + 3);
/*  36 */     if (!isEllipse && random.nextDouble() > 0.9D) {
/*  37 */       overWaterHeight += random.nextInt(19) + 7;
/*     */     }
/*     */     
/*  40 */     int underWaterHeight = Math.min(overWaterHeight + random.nextInt(11), 18);
/*  41 */     int width = Math.min(overWaterHeight + random.nextInt(7) - random.nextInt(5), 11);
/*  42 */     int a = isEllipse ? shapeEllipseA : 11;
/*     */ 
/*     */     
/*  45 */     for (int xo = -a; xo < a; xo++) {
/*  46 */       for (int zo = -a; zo < a; zo++) {
/*  47 */         for (int yOff = 0; yOff < overWaterHeight; yOff++) {
/*  48 */           int radius = isEllipse ? heightDependentRadiusEllipse(yOff, overWaterHeight, width) : heightDependentRadiusRound(random, yOff, overWaterHeight, width);
/*  49 */           if (isEllipse || xo < radius)
/*     */           {
/*     */             
/*  52 */             generateIcebergBlock(level, random, origin, overWaterHeight, xo, yOff, zo, radius, a, isEllipse, shapeEllipseC, shapeAngle, snowOnTop, mainBlockState);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  58 */     smooth(level, origin, width, overWaterHeight, isEllipse, shapeEllipseA);
/*     */ 
/*     */     
/*  61 */     for (int xo = -a; xo < a; xo++) {
/*  62 */       for (int zo = -a; zo < a; zo++) {
/*  63 */         for (int yOff = -1; yOff > -underWaterHeight; yOff--) {
/*  64 */           int newA = isEllipse ? Mth.ceil(a * (1.0F - (float)Math.pow(yOff, 2.0D) / underWaterHeight * 8.0F)) : a;
/*  65 */           int radius = heightDependentRadiusSteep(random, -yOff, underWaterHeight, width);
/*  66 */           if (xo < radius)
/*     */           {
/*     */             
/*  69 */             generateIcebergBlock(level, random, origin, underWaterHeight, xo, yOff, zo, radius, newA, isEllipse, shapeEllipseC, shapeAngle, snowOnTop, mainBlockState);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     boolean doCutOut = isEllipse ? ((random.nextDouble() > 0.1D)) : ((random.nextDouble() > 0.7D));
/*  76 */     if (doCutOut) {
/*  77 */       generateCutOut(random, level, width, overWaterHeight, origin, isEllipse, shapeEllipseA, shapeAngle, shapeEllipseC);
/*     */     }
/*     */     
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   private void generateCutOut(RandomSource random, LevelAccessor level, int width, int height, BlockPos globalOrigin, boolean isEllipse, int shapeEllipseA, double shapeAngle, int shapeEllipseC) {
/*  84 */     int randomSignX = random.nextBoolean() ? -1 : 1;
/*  85 */     int randomSignZ = random.nextBoolean() ? -1 : 1;
/*     */     
/*  87 */     int xOff = random.nextInt(Math.max(width / 2 - 2, 1));
/*  88 */     if (random.nextBoolean()) {
/*  89 */       xOff = width / 2 + 1 - random.nextInt(Math.max(width - width / 2 - 1, 1));
/*     */     }
/*  91 */     int zOff = random.nextInt(Math.max(width / 2 - 2, 1));
/*  92 */     if (random.nextBoolean()) {
/*  93 */       zOff = width / 2 + 1 - random.nextInt(Math.max(width - width / 2 - 1, 1));
/*     */     }
/*     */     
/*  96 */     if (isEllipse) {
/*  97 */       xOff = zOff = random.nextInt(Math.max(shapeEllipseA - 5, 1));
/*     */     }
/*     */     
/* 100 */     BlockPos localOrigin = new BlockPos(randomSignX * xOff, 0, randomSignZ * zOff);
/* 101 */     double angle = isEllipse ? (shapeAngle + 1.5707963267948966D) : (random.nextDouble() * 2.0D * Math.PI);
/*     */     
/* 103 */     for (int yOff = 0; yOff < height - 3; yOff++) {
/* 104 */       int radius = heightDependentRadiusRound(random, yOff, height, width);
/* 105 */       carve(radius, yOff, globalOrigin, level, false, angle, localOrigin, shapeEllipseA, shapeEllipseC);
/*     */     } 
/*     */     
/* 108 */     for (int yOff = -1; yOff > -height + random.nextInt(5); yOff--) {
/* 109 */       int radius = heightDependentRadiusSteep(random, -yOff, height, width);
/* 110 */       carve(radius, yOff, globalOrigin, level, true, angle, localOrigin, shapeEllipseA, shapeEllipseC);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void carve(int radius, int yOff, BlockPos globalOrigin, LevelAccessor level, boolean underWater, double angle, BlockPos localOrigin, int shapeEllipseA, int shapeEllipseC) {
/* 115 */     int a = radius + 1 + shapeEllipseA / 3;
/* 116 */     int c = Math.min(radius - 3, 3) + shapeEllipseC / 2 - 1;
/*     */     
/* 118 */     for (int xo = -a; xo < a; xo++) {
/* 119 */       for (int zo = -a; zo < a; zo++) {
/* 120 */         double signedDist = signedDistanceEllipse(xo, zo, localOrigin, a, c, angle);
/* 121 */         if (signedDist < 0.0D) {
/* 122 */           BlockPos pos = globalOrigin.offset(xo, yOff, zo);
/* 123 */           BlockState state = level.getBlockState(pos);
/* 124 */           if (isIcebergState(state) || state.is(Blocks.SNOW_BLOCK)) {
/* 125 */             if (underWater) {
/* 126 */               setBlock(level, pos, Blocks.WATER.defaultBlockState());
/*     */             } else {
/* 128 */               setBlock(level, pos, Blocks.AIR.defaultBlockState());
/* 129 */               removeFloatingSnowLayer(level, pos);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeFloatingSnowLayer(LevelAccessor level, BlockPos pos) {
/* 138 */     if (level.getBlockState(pos.above()).is(Blocks.SNOW)) {
/* 139 */       setBlock(level, pos.above(), Blocks.AIR.defaultBlockState());
/*     */     }
/*     */   }
/*     */   
/*     */   private void generateIcebergBlock(LevelAccessor level, RandomSource random, BlockPos origin, int height, int xo, int yOff, int zo, int radius, int a, boolean isEllipse, int shapeEllipseC, double shapeAngle, boolean snowOnTop, BlockState mainBlockState) {
/* 144 */     double signedDist = isEllipse ? signedDistanceEllipse(xo, zo, BlockPos.ZERO, a, getEllipseC(yOff, height, shapeEllipseC), shapeAngle) : signedDistanceCircle(xo, zo, BlockPos.ZERO, radius, random);
/* 145 */     if (signedDist < 0.0D) {
/* 146 */       BlockPos pos = origin.offset(xo, yOff, zo);
/* 147 */       double compareVal = isEllipse ? -0.5D : (-6 - random.nextInt(3));
/* 148 */       if (signedDist > compareVal && random.nextDouble() > 0.9D) {
/*     */         return;
/*     */       }
/* 151 */       setIcebergBlock(pos, level, random, height - yOff, height, isEllipse, snowOnTop, mainBlockState);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setIcebergBlock(BlockPos pos, LevelAccessor level, RandomSource random, int hDiff, int height, boolean isEllipse, boolean snowOnTop, BlockState mainBlockState) {
/* 156 */     BlockState state = level.getBlockState(pos);
/* 157 */     if (state.isAir() || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.ICE) || state.is(Blocks.WATER)) {
/* 158 */       boolean randomness = (!isEllipse || random.nextDouble() > 0.05D);
/* 159 */       int divisor = isEllipse ? 3 : 2;
/* 160 */       if (snowOnTop && !state.is(Blocks.WATER) && hDiff <= random.nextInt(Math.max(1, height / divisor)) + height * 0.6D && randomness) {
/* 161 */         setBlock(level, pos, Blocks.SNOW_BLOCK.defaultBlockState());
/*     */       } else {
/* 163 */         setBlock(level, pos, mainBlockState);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getEllipseC(int yOff, int height, int shapeEllipseC) {
/* 169 */     int c = shapeEllipseC;
/* 170 */     if (yOff > 0 && height - yOff <= 3) {
/* 171 */       c -= 4 - height - yOff;
/*     */     }
/*     */     
/* 174 */     return c;
/*     */   }
/*     */   
/*     */   private double signedDistanceCircle(int xo, int zo, BlockPos origin, int radius, RandomSource random) {
/* 178 */     float off = 10.0F * Mth.clamp(random.nextFloat(), 0.2F, 0.8F) / radius;
/* 179 */     return off + Math.pow((xo - origin.getX()), 2.0D) + Math.pow((zo - origin.getZ()), 2.0D) - Math.pow(radius, 2.0D);
/*     */   }
/*     */ 
/*     */   
/* 183 */   private double signedDistanceEllipse(int xo, int zo, BlockPos origin, int a, int c, double angle) { return Math.pow(((xo - origin.getX()) * Math.cos(angle) - (zo - origin.getZ()) * Math.sin(angle)) / a, 2.0D) + Math.pow(((xo - origin.getX()) * Math.sin(angle) + (zo - origin.getZ()) * Math.cos(angle)) / c, 2.0D) - 1.0D; }
/*     */ 
/*     */   
/*     */   private int heightDependentRadiusRound(RandomSource random, int yOff, int height, int width) {
/* 187 */     float k = 3.5F - random.nextFloat();
/* 188 */     float scale = (1.0F - (float)Math.pow(yOff, 2.0D) / height * k) * width;
/*     */     
/* 190 */     if (height > 15 + random.nextInt(5)) {
/* 191 */       int tempYOff = (yOff < 3 + random.nextInt(6)) ? (yOff / 2) : yOff;
/* 192 */       scale = (1.0F - tempYOff / height * k * 0.4F) * width;
/*     */     } 
/*     */     
/* 195 */     return Mth.ceil(scale / 2.0F);
/*     */   }
/*     */   
/*     */   private int heightDependentRadiusEllipse(int yOff, int height, int width) {
/* 199 */     float k = 1.0F;
/* 200 */     float scale = (1.0F - (float)Math.pow(yOff, 2.0D) / height * 1.0F) * width;
/* 201 */     return Mth.ceil(scale / 2.0F);
/*     */   }
/*     */   
/*     */   private int heightDependentRadiusSteep(RandomSource random, int yOff, int height, int width) {
/* 205 */     float k = 1.0F + random.nextFloat() / 2.0F;
/* 206 */     float scale = (1.0F - yOff / height * k) * width;
/* 207 */     return Mth.ceil(scale / 2.0F);
/*     */   }
/*     */ 
/*     */   
/* 211 */   private static boolean isIcebergState(BlockState state) { return (state.is(Blocks.PACKED_ICE) || state.is(Blocks.SNOW_BLOCK) || state.is(Blocks.BLUE_ICE)); }
/*     */ 
/*     */ 
/*     */   
/* 215 */   private boolean belowIsAir(BlockGetter level, BlockPos pos) { return level.getBlockState(pos.below()).isAir(); }
/*     */ 
/*     */   
/*     */   private void smooth(LevelAccessor level, BlockPos origin, int width, int height, boolean isEllipse, int shapeEllipseA) {
/* 219 */     int a = isEllipse ? shapeEllipseA : (width / 2);
/*     */     
/* 221 */     for (int x = -a; x <= a; x++) {
/* 222 */       for (int z = -a; z <= a; z++) {
/* 223 */         for (int yOff = 0; yOff <= height; yOff++) {
/* 224 */           BlockPos pos = origin.offset(x, yOff, z);
/* 225 */           BlockState state = level.getBlockState(pos);
/*     */ 
/*     */           
/* 228 */           if (isIcebergState(state) || state.is(Blocks.SNOW))
/* 229 */             if (belowIsAir(level, pos)) {
/* 230 */               setBlock(level, pos, Blocks.AIR.defaultBlockState());
/* 231 */               setBlock(level, pos.above(), Blocks.AIR.defaultBlockState());
/*     */ 
/*     */             
/*     */             }
/* 235 */             else if (isIcebergState(state)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 241 */               BlockState[] sides = { level.getBlockState(pos.west()), level.getBlockState(pos.east()), level.getBlockState(pos.north()), level.getBlockState(pos.south()) };
/*     */               
/* 243 */               int counter = 0;
/* 244 */               for (BlockState side : sides) {
/* 245 */                 if (!isIcebergState(side)) {
/* 246 */                   counter++;
/*     */                 }
/*     */               } 
/* 249 */               if (counter >= 3)
/* 250 */                 setBlock(level, pos, Blocks.AIR.defaultBlockState()); 
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\IcebergFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */