/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.FixedBiomeSource;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ 
/*     */ public class DebugLevelSource extends ChunkGenerator {
/*  36 */   public static final MapCodec<DebugLevelSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  37 */         RegistryOps.retrieveElement(Biomes.PLAINS))
/*  38 */       .apply(i, i.stable(DebugLevelSource::new)));
/*     */   
/*     */   private static final int BLOCK_MARGIN = 2;
/*  41 */   private static final List<BlockState> ALL_BLOCKS = (List)StreamSupport.stream(BuiltInRegistries.BLOCK.spliterator(), false).flatMap(b -> b.getStateDefinition().getPossibleStates().stream()).collect(Collectors.toList());
/*  42 */   private static final int GRID_WIDTH = Mth.ceil(Mth.sqrt(ALL_BLOCKS.size()));
/*  43 */   private static final int GRID_HEIGHT = Mth.ceil(ALL_BLOCKS.size() / GRID_WIDTH);
/*     */   
/*  45 */   protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*  46 */   protected static final BlockState BARRIER = Blocks.BARRIER.defaultBlockState();
/*     */   
/*     */   public static final int HEIGHT = 70;
/*     */   
/*     */   public static final int BARRIER_HEIGHT = 60;
/*     */   
/*  52 */   public DebugLevelSource(Holder.Reference<Biome> plains) { super(new FixedBiomeSource(plains)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected MapCodec<? extends ChunkGenerator> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState randomState, ChunkAccess protoChunk) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
/*  66 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*     */     
/*  68 */     ChunkPos centerPos = chunk.getPos();
/*  69 */     int chunkX = centerPos.x;
/*  70 */     int chunkZ = centerPos.z;
/*     */     
/*  72 */     for (int x = 0; x < 16; x++) {
/*  73 */       for (int z = 0; z < 16; z++) {
/*  74 */         int worldX = SectionPos.sectionToBlockCoord(chunkX, x);
/*  75 */         int worldZ = SectionPos.sectionToBlockCoord(chunkZ, z);
/*  76 */         level.setBlock(blockPos.set(worldX, 60, worldZ), BARRIER, 2);
/*  77 */         BlockState state = getBlockStateFor(worldX, worldZ);
/*  78 */         level.setBlock(blockPos.set(worldX, 70, worldZ), state, 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess centerChunk) { return CompletableFuture.completedFuture(centerChunk); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) { return new NoiseColumn(0, new BlockState[0]); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {}
/*     */ 
/*     */   
/*     */   public static BlockState getBlockStateFor(int worldX, int worldZ) {
/* 103 */     BlockState state = AIR;
/*     */     
/* 105 */     if (worldX > 0 && worldZ > 0 && worldX % 2 != 0 && worldZ % 2 != 0) {
/* 106 */       worldX /= 2;
/* 107 */       worldZ /= 2;
/*     */       
/* 109 */       if (worldX <= GRID_WIDTH && worldZ <= GRID_HEIGHT) {
/* 110 */         int index = Mth.abs(worldX * GRID_WIDTH + worldZ);
/* 111 */         if (index < ALL_BLOCKS.size()) {
/* 112 */           state = (BlockState)ALL_BLOCKS.get(index);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     return state;
/*     */   }
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
/* 130 */   public int getMinY() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   public int getGenDepth() { return 384; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   public int getSeaLevel() { return 63; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\DebugLevelSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */