/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ public class FlatLevelSource
/*     */   extends ChunkGenerator {
/*  32 */   public static final MapCodec<FlatLevelSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(FlatLevelGeneratorSettings.CODEC
/*  33 */         .fieldOf("settings").forGetter(FlatLevelSource::settings))
/*  34 */       .apply(i, i.stable(FlatLevelSource::new)));
/*     */   
/*     */   private final FlatLevelGeneratorSettings settings;
/*     */   
/*     */   public FlatLevelSource(FlatLevelGeneratorSettings generatorSettings) {
/*  39 */     super(new FixedBiomeSource(generatorSettings.getBiome()), Util.memoize(generatorSettings::adjustGenerationSettings));
/*  40 */     this.settings = generatorSettings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSets, RandomState randomState, long levelSeed) {
/*  51 */     Stream<Holder<StructureSet>> structures = (Stream)this.settings.structureOverrides().map(HolderSet::stream).orElseGet(() -> structureSets.listElements().map(()));
/*  52 */     return ChunkGeneratorStructureState.createForFlat(randomState, levelSeed, this.biomeSource, structures);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected MapCodec<? extends ChunkGenerator> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public FlatLevelGeneratorSettings settings() { return this.settings; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState randomState, ChunkAccess protoChunk) {}
/*     */ 
/*     */ 
/*     */   
/*  70 */   public int getSpawnHeight(LevelHeightAccessor heightAccessor) { return heightAccessor.getMinY() + Math.min(heightAccessor.getHeight(), this.settings.getLayers().size()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess centerChunk) {
/*  77 */     List<BlockState> layers = this.settings.getLayers();
/*     */     
/*  79 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*  80 */     Heightmap oceanFloor = centerChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
/*  81 */     Heightmap worldSurface = centerChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
/*     */     
/*  83 */     for (int layerIndex = 0; layerIndex < Math.min(centerChunk.getHeight(), layers.size()); layerIndex++) {
/*  84 */       BlockState blockState = (BlockState)layers.get(layerIndex);
/*  85 */       if (blockState != null) {
/*     */ 
/*     */         
/*  88 */         int y = centerChunk.getMinY() + layerIndex;
/*     */         
/*  90 */         for (int x = 0; x < 16; x++) {
/*  91 */           for (int z = 0; z < 16; z++) {
/*  92 */             centerChunk.setBlockState(blockPos.set(x, y, z), blockState);
/*  93 */             oceanFloor.update(x, y, z, blockState);
/*  94 */             worldSurface.update(x, y, z, blockState);
/*     */           } 
/*     */         } 
/*     */       } 
/*  98 */     }  return CompletableFuture.completedFuture(centerChunk);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) {
/* 103 */     List<BlockState> layers = this.settings.getLayers();
/* 104 */     for (int layerIndex = Math.min(layers.size() - 1, heightAccessor.getMaxY()); layerIndex >= 0; layerIndex--) {
/* 105 */       BlockState state = (BlockState)layers.get(layerIndex);
/* 106 */       if (state != null)
/*     */       {
/*     */         
/* 109 */         if (type.isOpaque().test(state))
/* 110 */           return heightAccessor.getMinY() + layerIndex + 1; 
/*     */       }
/*     */     } 
/* 113 */     return heightAccessor.getMinY();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) { return new NoiseColumn(heightAccessor.getMinY(), (BlockState[])this.settings.getLayers().stream().limit(heightAccessor.getHeight()).map(state -> (state == null) ? Blocks.AIR.defaultBlockState() : state).toArray(x$0 -> new BlockState[x$0])); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {}
/*     */ 
/*     */ 
/*     */   
/* 135 */   public int getMinY() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public int getGenDepth() { return 384; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public int getSeaLevel() { return -63; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\FlatLevelSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */