/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.BlockColumn;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.carver.CarvingContext;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SurfaceSystem
/*     */ {
/*  31 */   private static final BlockState WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();
/*  32 */   private static final BlockState ORANGE_TERRACOTTA = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
/*  33 */   private static final BlockState TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();
/*  34 */   private static final BlockState YELLOW_TERRACOTTA = Blocks.YELLOW_TERRACOTTA.defaultBlockState();
/*  35 */   private static final BlockState BROWN_TERRACOTTA = Blocks.BROWN_TERRACOTTA.defaultBlockState();
/*  36 */   private static final BlockState RED_TERRACOTTA = Blocks.RED_TERRACOTTA.defaultBlockState();
/*  37 */   private static final BlockState LIGHT_GRAY_TERRACOTTA = Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState();
/*     */   
/*  39 */   private static final BlockState PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();
/*  40 */   private static final BlockState SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();
/*     */   
/*     */   private final BlockState defaultBlock;
/*     */   
/*     */   private final int seaLevel;
/*     */   
/*     */   private final BlockState[] clayBands;
/*     */   
/*     */   private final NormalNoise clayBandsOffsetNoise;
/*     */   
/*     */   private final NormalNoise badlandsPillarNoise;
/*     */   private final NormalNoise badlandsPillarRoofNoise;
/*     */   private final NormalNoise badlandsSurfaceNoise;
/*     */   private final NormalNoise icebergPillarNoise;
/*     */   private final NormalNoise icebergPillarRoofNoise;
/*     */   private final NormalNoise icebergSurfaceNoise;
/*     */   private final PositionalRandomFactory noiseRandom;
/*     */   private final NormalNoise surfaceNoise;
/*     */   private final NormalNoise surfaceSecondaryNoise;
/*     */   
/*     */   public SurfaceSystem(RandomState randomState, BlockState defaultBlock, int seaLevel, PositionalRandomFactory noiseRandom) {
/*  61 */     this.defaultBlock = defaultBlock;
/*  62 */     this.seaLevel = seaLevel;
/*     */     
/*  64 */     this.noiseRandom = noiseRandom;
/*     */     
/*  66 */     this.clayBandsOffsetNoise = randomState.getOrCreateNoise(Noises.CLAY_BANDS_OFFSET);
/*  67 */     this.clayBands = generateBands(noiseRandom.fromHashOf(Identifier.withDefaultNamespace("clay_bands")));
/*     */     
/*  69 */     this.surfaceNoise = randomState.getOrCreateNoise(Noises.SURFACE);
/*  70 */     this.surfaceSecondaryNoise = randomState.getOrCreateNoise(Noises.SURFACE_SECONDARY);
/*     */     
/*  72 */     this.badlandsPillarNoise = randomState.getOrCreateNoise(Noises.BADLANDS_PILLAR);
/*  73 */     this.badlandsPillarRoofNoise = randomState.getOrCreateNoise(Noises.BADLANDS_PILLAR_ROOF);
/*  74 */     this.badlandsSurfaceNoise = randomState.getOrCreateNoise(Noises.BADLANDS_SURFACE);
/*     */     
/*  76 */     this.icebergPillarNoise = randomState.getOrCreateNoise(Noises.ICEBERG_PILLAR);
/*  77 */     this.icebergPillarRoofNoise = randomState.getOrCreateNoise(Noises.ICEBERG_PILLAR_ROOF);
/*  78 */     this.icebergSurfaceNoise = randomState.getOrCreateNoise(Noises.ICEBERG_SURFACE);
/*     */   }
/*     */   
/*     */   public void buildSurface(RandomState randomState, BiomeManager biomeManager, Registry<Biome> biomes, boolean useLegacyRandom, WorldGenerationContext generationContext, final ChunkAccess protoChunk, NoiseChunk noiseChunk, SurfaceRules.RuleSource ruleSource) {
/*  82 */     final BlockPos.MutableBlockPos columnPos = new BlockPos.MutableBlockPos();
/*     */     
/*  84 */     final ChunkPos chunkPos = protoChunk.getPos();
/*  85 */     int minBlockX = chunkPos.getMinBlockX();
/*  86 */     int minBlockZ = chunkPos.getMinBlockZ();
/*     */     
/*  88 */     BlockColumn column = new BlockColumn(this)
/*     */       {
/*     */         public BlockState getBlock(int blockY) {
/*  91 */           return protoChunk.getBlockState(columnPos.setY(blockY));
/*     */         }
/*     */ 
/*     */         
/*     */         public void setBlock(int blockY, BlockState state) {
/*  96 */           LevelHeightAccessor heightAccessor = protoChunk.getHeightAccessorForGeneration();
/*  97 */           if (heightAccessor.isInsideBuildHeight(blockY)) {
/*  98 */             protoChunk.setBlockState(columnPos.setY(blockY), state);
/*  99 */             if (!state.getFluidState().isEmpty())
/* 100 */               protoChunk.markPosForPostprocessing(columnPos); 
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public String toString() {
/* 107 */           return "ChunkBlockColumn " + String.valueOf(chunkPos);
/*     */         }
/*     */       };
/*     */     
/* 111 */     Objects.requireNonNull(biomeManager); SurfaceRules.Context context = new SurfaceRules.Context(this, randomState, protoChunk, noiseChunk, biomeManager::getBiome, biomes, generationContext);
/* 112 */     SurfaceRules.SurfaceRule rule = (SurfaceRules.SurfaceRule)ruleSource.apply(context);
/*     */     
/* 114 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*     */     
/* 116 */     for (int x = 0; x < 16; x++) {
/* 117 */       for (int z = 0; z < 16; z++) {
/* 118 */         int blockX = minBlockX + x;
/* 119 */         int blockZ = minBlockZ + z;
/* 120 */         int startingHeight = protoChunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) + 1;
/*     */         
/* 122 */         columnPos.setX(blockX).setZ(blockZ);
/*     */         
/* 124 */         Holder<Biome> surfaceBiome = biomeManager.getBiome(blockPos.set(blockX, useLegacyRandom ? 0 : startingHeight, blockZ));
/*     */ 
/*     */         
/* 127 */         if (surfaceBiome.is(Biomes.ERODED_BADLANDS)) {
/* 128 */           erodedBadlandsExtension(column, blockX, blockZ, startingHeight, protoChunk);
/*     */         }
/* 130 */         int height = protoChunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) + 1;
/*     */         
/* 132 */         context.updateXZ(blockX, blockZ);
/*     */         
/* 134 */         int stoneAboveDepth = 0;
/* 135 */         int waterHeight = Integer.MIN_VALUE;
/* 136 */         int nextCeilingStoneY = Integer.MAX_VALUE;
/*     */         
/* 138 */         int endY = protoChunk.getMinY();
/* 139 */         for (int y = height; y >= endY; y--) {
/* 140 */           BlockState old = column.getBlock(y);
/*     */           
/* 142 */           if (old.isAir()) {
/* 143 */             stoneAboveDepth = 0;
/*     */             
/* 145 */             waterHeight = Integer.MIN_VALUE;
/*     */ 
/*     */           
/*     */           }
/* 149 */           else if (!old.getFluidState().isEmpty()) {
/*     */ 
/*     */ 
/*     */             
/* 153 */             if (waterHeight == Integer.MIN_VALUE) {
/* 154 */               waterHeight = y + 1;
/*     */             }
/*     */           }
/*     */           else {
/*     */             
/* 159 */             if (nextCeilingStoneY >= y) {
/*     */               
/* 161 */               nextCeilingStoneY = DimensionType.WAY_BELOW_MIN_Y;
/* 162 */               for (int lookaheadY = y - 1; lookaheadY >= endY - 1; lookaheadY--) {
/* 163 */                 BlockState nextState = column.getBlock(lookaheadY);
/* 164 */                 if (!isStone(nextState)) {
/* 165 */                   nextCeilingStoneY = lookaheadY + 1;
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 171 */             stoneAboveDepth++;
/* 172 */             int stoneBelowDepth = y - nextCeilingStoneY + 1;
/*     */             
/* 174 */             context.updateY(stoneAboveDepth, stoneBelowDepth, waterHeight, blockX, y, blockZ);
/*     */             
/* 176 */             if (old == this.defaultBlock) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 182 */               BlockState state = rule.tryApply(blockX, y, blockZ);
/*     */               
/* 184 */               if (state != null) {
/* 185 */                 column.setBlock(y, state);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/* 190 */         if (surfaceBiome.is(Biomes.FROZEN_OCEAN) || surfaceBiome.is(Biomes.DEEP_FROZEN_OCEAN)) {
/* 191 */           frozenOceanExtension(context.getMinSurfaceLevel(), (Biome)surfaceBiome.value(), column, blockPos, blockX, blockZ, startingHeight);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getSurfaceDepth(int blockX, int blockZ) {
/* 201 */     double noiseValue = this.surfaceNoise.getValue(blockX, 0.0D, blockZ);
/*     */     
/* 203 */     return (int)(noiseValue * 2.75D + 3.0D + this.noiseRandom.at(blockX, 0, blockZ).nextDouble() * 0.25D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   protected double getSurfaceSecondary(int blockX, int blockZ) { return this.surfaceSecondaryNoise.getValue(blockX, 0.0D, blockZ); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   private boolean isStone(BlockState state) { return (!state.isAir() && state.getFluidState().isEmpty()); }
/*     */ 
/*     */ 
/*     */   
/* 219 */   public int getSeaLevel() { return this.seaLevel; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Optional<BlockState> topMaterial(SurfaceRules.RuleSource ruleSource, CarvingContext carvingContext, Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunk, NoiseChunk noiseChunk, BlockPos pos, boolean underFluid) {
/* 227 */     SurfaceRules.Context context = new SurfaceRules.Context(this, carvingContext.randomState(), chunk, noiseChunk, biomeGetter, carvingContext.registryAccess().lookupOrThrow(Registries.BIOME), carvingContext);
/* 228 */     SurfaceRules.SurfaceRule rule = (SurfaceRules.SurfaceRule)ruleSource.apply(context);
/*     */     
/* 230 */     int blockX = pos.getX();
/* 231 */     int blockY = pos.getY();
/* 232 */     int blockZ = pos.getZ();
/*     */     
/* 234 */     context.updateXZ(blockX, blockZ);
/* 235 */     context.updateY(1, 1, underFluid ? (blockY + 1) : Integer.MIN_VALUE, blockX, blockY, blockZ);
/* 236 */     BlockState state = rule.tryApply(blockX, blockY, blockZ);
/*     */     
/* 238 */     return Optional.ofNullable(state);
/*     */   }
/*     */   
/*     */   private void erodedBadlandsExtension(BlockColumn column, int blockX, int blockZ, int height, LevelHeightAccessor protoChunk) {
/* 242 */     double pillarNoiseScale = 0.2D;
/* 243 */     double pillarBuffer = Math.min(Math.abs(this.badlandsSurfaceNoise.getValue(blockX, 0.0D, blockZ) * 8.25D), this.badlandsPillarNoise.getValue(blockX * 0.2D, 0.0D, blockZ * 0.2D) * 15.0D);
/* 244 */     if (pillarBuffer <= 0.0D) {
/*     */       return;
/*     */     }
/*     */     
/* 248 */     double floorNoiseSampleResolution = 0.75D;
/* 249 */     double floorAmplitude = 1.5D;
/* 250 */     double pillarFloor = Math.abs(this.badlandsPillarRoofNoise.getValue(blockX * 0.75D, 0.0D, blockZ * 0.75D) * 1.5D);
/* 251 */     double extensionTop = 64.0D + Math.min(pillarBuffer * pillarBuffer * 2.5D, Math.ceil(pillarFloor * 50.0D) + 24.0D);
/*     */     
/* 253 */     int startY = Mth.floor(extensionTop);
/*     */ 
/*     */     
/* 256 */     if (height > startY) {
/*     */       return;
/*     */     }
/*     */     
/* 260 */     for (int y = startY; y >= protoChunk.getMinY(); y--) {
/* 261 */       BlockState oldState = column.getBlock(y);
/* 262 */       if (oldState.is(this.defaultBlock.getBlock())) {
/*     */         break;
/*     */       }
/* 265 */       if (oldState.is(Blocks.WATER)) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 270 */     for (int y = startY; y >= protoChunk.getMinY() && 
/* 271 */       column.getBlock(y).isAir(); y--)
/*     */     {
/*     */       
/* 274 */       column.setBlock(y, this.defaultBlock);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void frozenOceanExtension(int minSurfaceLevel, Biome surfaceBiome, BlockColumn column, BlockPos.MutableBlockPos blockPos, int blockX, int blockZ, int height) {
/* 280 */     double extensionBottom, pillarScale = 1.28D;
/* 281 */     double iceberg = Math.min(Math.abs(this.icebergSurfaceNoise.getValue(blockX, 0.0D, blockZ) * 8.25D), this.icebergPillarNoise.getValue(blockX * 1.28D, 0.0D, blockZ * 1.28D) * 15.0D);
/*     */ 
/*     */     
/* 284 */     if (iceberg <= 1.8D) {
/*     */       return;
/*     */     }
/*     */     
/* 288 */     double roofScale = 1.17D;
/* 289 */     double roofAmplitude = 1.5D;
/* 290 */     double icebergRoof = Math.abs(this.icebergPillarRoofNoise.getValue(blockX * 1.17D, 0.0D, blockZ * 1.17D) * 1.5D);
/* 291 */     double top = Math.min(iceberg * iceberg * 1.2D, Math.ceil(icebergRoof * 40.0D) + 14.0D);
/*     */     
/* 293 */     if (surfaceBiome.shouldMeltFrozenOceanIcebergSlightly(blockPos.set(blockX, this.seaLevel, blockZ), this.seaLevel)) {
/* 294 */       top -= 2.0D;
/*     */     }
/*     */     
/* 297 */     if (top > 2.0D) {
/* 298 */       extensionBottom = this.seaLevel - top - 7.0D;
/* 299 */       top += this.seaLevel;
/*     */     } else {
/* 301 */       top = 0.0D;
/* 302 */       extensionBottom = 0.0D;
/*     */     } 
/* 304 */     double extensionTop = top;
/*     */     
/* 306 */     RandomSource random = this.noiseRandom.at(blockX, 0, blockZ);
/*     */     
/* 308 */     int maxSnowDepth = 2 + random.nextInt(4);
/* 309 */     int minSnowHeight = this.seaLevel + 18 + random.nextInt(10);
/*     */     
/* 311 */     int snowDepth = 0;
/*     */     
/* 313 */     for (int y = Math.max(height, (int)extensionTop + 1); y >= minSurfaceLevel; y--) {
/*     */       
/* 315 */       if ((column.getBlock(y).isAir() && y < (int)extensionTop && random.nextDouble() > 0.01D) || (column.getBlock(y).is(Blocks.WATER) && y > (int)extensionBottom && y < this.seaLevel && extensionBottom != 0.0D && random.nextDouble() > 0.15D)) {
/* 316 */         if (snowDepth <= maxSnowDepth && y > minSnowHeight) {
/* 317 */           column.setBlock(y, SNOW_BLOCK);
/* 318 */           snowDepth++;
/*     */         } else {
/* 320 */           column.setBlock(y, PACKED_ICE);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BlockState[] generateBands(RandomSource random) {
/* 327 */     BlockState[] clayBands = new BlockState[192];
/* 328 */     Arrays.fill(clayBands, TERRACOTTA);
/*     */ 
/*     */     
/* 331 */     for (int i = 0; i < clayBands.length; i++) {
/* 332 */       i += random.nextInt(5) + 1;
/* 333 */       if (i < clayBands.length) {
/* 334 */         clayBands[i] = ORANGE_TERRACOTTA;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 339 */     makeBands(random, clayBands, 1, YELLOW_TERRACOTTA);
/*     */     
/* 341 */     makeBands(random, clayBands, 2, BROWN_TERRACOTTA);
/*     */     
/* 343 */     makeBands(random, clayBands, 1, RED_TERRACOTTA);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 348 */     int whiteBandCount = random.nextIntBetweenInclusive(9, 15); int i, start;
/* 349 */     for (i = 0, start = 0; i < whiteBandCount && start < clayBands.length; i++, start += random.nextInt(16) + 4) {
/* 350 */       clayBands[start] = WHITE_TERRACOTTA;
/* 351 */       if (start - 1 > 0 && random.nextBoolean()) {
/* 352 */         clayBands[start - 1] = LIGHT_GRAY_TERRACOTTA;
/*     */       }
/* 354 */       if (start + 1 < clayBands.length && random.nextBoolean()) {
/* 355 */         clayBands[start + 1] = LIGHT_GRAY_TERRACOTTA;
/*     */       }
/*     */     } 
/*     */     
/* 359 */     return clayBands;
/*     */   }
/*     */   
/*     */   private static void makeBands(RandomSource random, BlockState[] clayBands, int baseWidth, BlockState state) {
/* 363 */     int bandCount = random.nextIntBetweenInclusive(6, 15);
/* 364 */     for (int i = 0; i < bandCount; i++) {
/* 365 */       int width = baseWidth + random.nextInt(3);
/* 366 */       int start = random.nextInt(clayBands.length);
/*     */       
/* 368 */       for (int p = 0; start + p < clayBands.length && p < width; p++) {
/* 369 */         clayBands[start + p] = state;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState getBand(int worldX, int y, int worldZ) {
/* 375 */     int offset = (int)Math.round(this.clayBandsOffsetNoise.getValue(worldX, 0.0D, worldZ) * 4.0D);
/* 376 */     return this.clayBands[(y + offset + this.clayBands.length) % this.clayBands.length];
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\SurfaceSystem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */