/*     */ package net.minecraft.world.level.levelgen.carver;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.Aquifer;
/*     */ 
/*     */ public class CaveWorldCarver
/*     */   extends WorldCarver<CaveCarverConfiguration>
/*     */ {
/*  19 */   public CaveWorldCarver(Codec<CaveCarverConfiguration> configurationFactory) { super(configurationFactory); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   public boolean isStartChunk(CaveCarverConfiguration configuration, RandomSource random) { return (random.nextFloat() <= configuration.probability); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean carve(CarvingContext context, CaveCarverConfiguration configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, RandomSource random, Aquifer aquifer, ChunkPos sourceChunkPos, CarvingMask mask) {
/*  29 */     int maxDistance = SectionPos.sectionToBlockCoord(getRange() * 2 - 1);
/*  30 */     int caveCount = random.nextInt(random.nextInt(random.nextInt(getCaveBound()) + 1) + 1);
/*     */     
/*  32 */     for (int cave = 0; cave < caveCount; cave++) {
/*     */       
/*  34 */       double x = sourceChunkPos.getBlockX(random.nextInt(16));
/*  35 */       double y = configuration.y.sample(random, context);
/*  36 */       double z = sourceChunkPos.getBlockZ(random.nextInt(16));
/*     */       
/*  38 */       double horizontalRadiusMultiplier = configuration.horizontalRadiusMultiplier.sample(random);
/*  39 */       double verticalRadiusMultiplier = configuration.verticalRadiusMultiplier.sample(random);
/*  40 */       double floorLevel = configuration.floorLevel.sample(random);
/*  41 */       WorldCarver.CarveSkipChecker skipChecker = (c, xd, yd, zd, worldY) -> shouldSkip(xd, yd, zd, floorLevel);
/*     */       
/*  43 */       int tunnels = 1;
/*  44 */       if (random.nextInt(4) == 0) {
/*     */         
/*  46 */         double yScale = configuration.yScale.sample(random);
/*  47 */         float thickness = 1.0F + random.nextFloat() * 6.0F;
/*  48 */         createRoom(context, configuration, chunk, biomeGetter, aquifer, x, y, z, thickness, yScale, mask, skipChecker);
/*  49 */         tunnels += random.nextInt(4);
/*     */       } 
/*     */       
/*  52 */       for (int i = 0; i < tunnels; i++) {
/*     */         
/*  54 */         float horizontalRotation = random.nextFloat() * 6.2831855F;
/*  55 */         float verticalRotation = (random.nextFloat() - 0.5F) / 4.0F;
/*  56 */         float thickness = getThickness(random);
/*  57 */         int distance = maxDistance - random.nextInt(maxDistance / 4);
/*  58 */         int initialStep = 0;
/*  59 */         createTunnel(context, configuration, chunk, biomeGetter, random.nextLong(), aquifer, x, y, z, horizontalRadiusMultiplier, verticalRadiusMultiplier, thickness, horizontalRotation, verticalRotation, 0, distance, getYScale(), mask, skipChecker);
/*     */       } 
/*     */     } 
/*     */     
/*  63 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  67 */   protected int getCaveBound() { return 15; }
/*     */ 
/*     */   
/*     */   protected float getThickness(RandomSource random) {
/*  71 */     float thickness = random.nextFloat() * 2.0F + random.nextFloat();
/*  72 */     if (random.nextInt(10) == 0) {
/*  73 */       thickness *= (random.nextFloat() * random.nextFloat() * 3.0F + 1.0F);
/*     */     }
/*  75 */     return thickness;
/*     */   }
/*     */ 
/*     */   
/*  79 */   protected double getYScale() { return 1.0D; }
/*     */ 
/*     */   
/*     */   protected void createRoom(CarvingContext context, CaveCarverConfiguration configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, Aquifer aquifer, double x, double y, double z, float thickness, double yScale, CarvingMask mask, WorldCarver.CarveSkipChecker skipChecker) {
/*  83 */     double horizontalRadius = 1.5D + (Mth.sin(1.5707963705062866D) * thickness);
/*  84 */     double verticalRadius = horizontalRadius * yScale;
/*     */ 
/*     */ 
/*     */     
/*  88 */     carveEllipsoid(context, configuration, chunk, biomeGetter, aquifer, x + 1.0D, y, z, horizontalRadius, verticalRadius, mask, skipChecker);
/*     */   }
/*     */   
/*     */   protected void createTunnel(CarvingContext context, CaveCarverConfiguration configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, long tunnelSeed, Aquifer aquifer, double x, double y, double z, double horizontalRadiusMultiplier, double verticalRadiusMultiplier, float thickness, float horizontalRotation, float verticalRotation, int step, int dist, double yScale, CarvingMask mask, WorldCarver.CarveSkipChecker skipChecker) {
/*  92 */     RandomSource random = RandomSource.create(tunnelSeed);
/*     */     
/*  94 */     int splitPoint = random.nextInt(dist / 2) + dist / 4;
/*  95 */     boolean steep = (random.nextInt(6) == 0);
/*     */     
/*  97 */     float yRota = 0.0F;
/*  98 */     float xRota = 0.0F;
/*     */     
/* 100 */     for (int currentStep = step; currentStep < dist; currentStep++) {
/* 101 */       double horizontalRadius = 1.5D + (Mth.sin((3.1415927F * currentStep / dist)) * thickness);
/* 102 */       double verticalRadius = horizontalRadius * yScale;
/*     */       
/* 104 */       float cosX = Mth.cos(verticalRotation);
/* 105 */       x += (Mth.cos(horizontalRotation) * cosX);
/* 106 */       y += Mth.sin(verticalRotation);
/* 107 */       z += (Mth.sin(horizontalRotation) * cosX);
/*     */       
/* 109 */       verticalRotation *= (steep ? 0.92F : 0.7F);
/* 110 */       verticalRotation += xRota * 0.1F;
/* 111 */       horizontalRotation += yRota * 0.1F;
/*     */       
/* 113 */       xRota *= 0.9F;
/* 114 */       yRota *= 0.75F;
/* 115 */       xRota += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
/* 116 */       yRota += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;
/*     */ 
/*     */       
/* 119 */       if (currentStep == splitPoint && thickness > 1.0F) {
/* 120 */         createTunnel(context, configuration, chunk, biomeGetter, random.nextLong(), aquifer, x, y, z, horizontalRadiusMultiplier, verticalRadiusMultiplier, random.nextFloat() * 0.5F + 0.5F, horizontalRotation - 1.5707964F, verticalRotation / 3.0F, currentStep, dist, 1.0D, mask, skipChecker);
/* 121 */         createTunnel(context, configuration, chunk, biomeGetter, random.nextLong(), aquifer, x, y, z, horizontalRadiusMultiplier, verticalRadiusMultiplier, random.nextFloat() * 0.5F + 0.5F, horizontalRotation + 1.5707964F, verticalRotation / 3.0F, currentStep, dist, 1.0D, mask, skipChecker);
/*     */         
/*     */         return;
/*     */       } 
/* 125 */       if (random.nextInt(4) != 0) {
/*     */ 
/*     */ 
/*     */         
/* 129 */         if (!canReach(chunk.getPos(), x, z, currentStep, dist, thickness)) {
/*     */           return;
/*     */         }
/*     */         
/* 133 */         carveEllipsoid(context, configuration, chunk, biomeGetter, aquifer, x, y, z, horizontalRadius * horizontalRadiusMultiplier, verticalRadius * verticalRadiusMultiplier, mask, skipChecker);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean shouldSkip(double xd, double yd, double zd, double floorLevel) {
/* 139 */     if (yd <= floorLevel) {
/* 140 */       return true;
/*     */     }
/*     */     
/* 143 */     return (xd * xd + yd * yd + zd * zd >= 1.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\carver\CaveWorldCarver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */