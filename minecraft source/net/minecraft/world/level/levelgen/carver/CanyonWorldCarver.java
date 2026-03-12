/*     */ package net.minecraft.world.level.levelgen.carver;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.Aquifer;
/*     */ 
/*     */ 
/*     */ public class CanyonWorldCarver
/*     */   extends WorldCarver<CanyonCarverConfiguration>
/*     */ {
/*  19 */   public CanyonWorldCarver(Codec<CanyonCarverConfiguration> configurationFactory) { super(configurationFactory); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   public boolean isStartChunk(CanyonCarverConfiguration configuration, RandomSource random) { return (random.nextFloat() <= configuration.probability); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean carve(CarvingContext context, CanyonCarverConfiguration configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, RandomSource random, Aquifer aquifer, ChunkPos sourceChunkPos, CarvingMask mask) {
/*  29 */     int maxDistance = (getRange() * 2 - 1) * 16;
/*     */     
/*  31 */     double x = sourceChunkPos.getBlockX(random.nextInt(16));
/*  32 */     int y = configuration.y.sample(random, context);
/*  33 */     double z = sourceChunkPos.getBlockZ(random.nextInt(16));
/*     */     
/*  35 */     float horizontalRotation = random.nextFloat() * 6.2831855F;
/*  36 */     float verticalRotation = configuration.verticalRotation.sample(random);
/*  37 */     double yScale = configuration.yScale.sample(random);
/*  38 */     float thickness = configuration.shape.thickness.sample(random);
/*  39 */     int distance = (int)(maxDistance * configuration.shape.distanceFactor.sample(random));
/*  40 */     int initialStep = 0;
/*  41 */     doCarve(context, configuration, chunk, biomeGetter, random.nextLong(), aquifer, x, y, z, thickness, horizontalRotation, verticalRotation, 0, distance, yScale, mask);
/*     */     
/*  43 */     return true;
/*     */   }
/*     */   
/*     */   private void doCarve(CarvingContext context, CanyonCarverConfiguration configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, long tunnelSeed, Aquifer aquifer, double x, double y, double z, float thickness, float horizontalRotation, float verticalRotation, int step, int distance, double yScale, CarvingMask mask) {
/*  47 */     RandomSource random = RandomSource.create(tunnelSeed);
/*     */     
/*  49 */     float[] widthFactorPerHeight = initWidthFactors(context, configuration, random);
/*     */     
/*  51 */     float yRota = 0.0F;
/*  52 */     float xRota = 0.0F;
/*     */     
/*  54 */     for (int currentStep = step; currentStep < distance; currentStep++) {
/*  55 */       double horizontalRadius = 1.5D + (Mth.sin((currentStep * 3.1415927F / distance)) * thickness);
/*  56 */       double verticalRadius = horizontalRadius * yScale;
/*     */       
/*  58 */       horizontalRadius *= configuration.shape.horizontalRadiusFactor.sample(random);
/*  59 */       verticalRadius = updateVerticalRadius(configuration, random, verticalRadius, distance, currentStep);
/*     */       
/*  61 */       float xc = Mth.cos(verticalRotation);
/*  62 */       float xs = Mth.sin(verticalRotation);
/*  63 */       x += (Mth.cos(horizontalRotation) * xc);
/*  64 */       y += xs;
/*  65 */       z += (Mth.sin(horizontalRotation) * xc);
/*     */       
/*  67 */       verticalRotation *= 0.7F;
/*     */       
/*  69 */       verticalRotation += xRota * 0.05F;
/*  70 */       horizontalRotation += yRota * 0.05F;
/*     */       
/*  72 */       xRota *= 0.8F;
/*  73 */       yRota *= 0.5F;
/*  74 */       xRota += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
/*  75 */       yRota += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
/*     */       
/*  77 */       if (random.nextInt(4) != 0) {
/*     */ 
/*     */ 
/*     */         
/*  81 */         if (!canReach(chunk.getPos(), x, z, currentStep, distance, thickness)) {
/*     */           return;
/*     */         }
/*     */         
/*  85 */         carveEllipsoid(context, configuration, chunk, biomeGetter, aquifer, x, y, z, horizontalRadius, verticalRadius, mask, (context1, xd, yd, zd, y1) -> shouldSkip(context1, widthFactorPerHeight, xd, yd, zd, y1));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private float[] initWidthFactors(CarvingContext context, CanyonCarverConfiguration configuration, RandomSource random) {
/*  90 */     int depth = context.getGenDepth();
/*  91 */     float[] widthFactorPerHeight = new float[depth];
/*  92 */     float widthFactor = 1.0F;
/*  93 */     for (int yIndex = 0; yIndex < depth; yIndex++) {
/*  94 */       if (yIndex == 0 || random.nextInt(configuration.shape.widthSmoothness) == 0) {
/*  95 */         widthFactor = 1.0F + random.nextFloat() * random.nextFloat();
/*     */       }
/*  97 */       widthFactorPerHeight[yIndex] = widthFactor * widthFactor;
/*     */     } 
/*  99 */     return widthFactorPerHeight;
/*     */   }
/*     */   
/*     */   private double updateVerticalRadius(CanyonCarverConfiguration configuration, RandomSource random, double verticalRadius, float distance, float currentStep) {
/* 103 */     float verticalMultiplier = 1.0F - Mth.abs(0.5F - currentStep / distance) * 2.0F;
/* 104 */     float factor = configuration.shape.verticalRadiusDefaultFactor + configuration.shape.verticalRadiusCenterFactor * verticalMultiplier;
/* 105 */     return factor * verticalRadius * Mth.randomBetween(random, 0.75F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldSkip(CarvingContext context, float[] widthFactorPerHeight, double xd, double yd, double zd, int y) {
/* 110 */     int yIndex = y - context.getMinGenY();
/* 111 */     return ((xd * xd + zd * zd) * widthFactorPerHeight[yIndex - 1] + yd * yd / 6.0D >= 1.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CanyonWorldCarver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */