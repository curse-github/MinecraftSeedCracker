/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ 
/*     */ public final class Context
/*     */ {
/*     */   private static final int HOW_FAR_BELOW_PRELIMINARY_SURFACE_LEVEL_TO_BUILD_SURFACE = 8;
/*     */   private static final int SURFACE_CELL_BITS = 4;
/*     */   private static final int SURFACE_CELL_SIZE = 16;
/*     */   private static final int SURFACE_CELL_MASK = 15;
/*     */   private final SurfaceSystem system;
/*     */   private final SurfaceRules.Condition temperature;
/*     */   private final SurfaceRules.Condition steep;
/*     */   private final SurfaceRules.Condition hole;
/*     */   private final SurfaceRules.Condition abovePreliminarySurface;
/*     */   private final RandomState randomState;
/*     */   private final ChunkAccess chunk;
/*     */   private final NoiseChunk noiseChunk;
/*     */   private final Function<BlockPos, Holder<Biome>> biomeGetter;
/*     */   private final WorldGenerationContext context;
/*     */   private long lastPreliminarySurfaceCellOrigin;
/*     */   private final int[] preliminarySurfaceCache;
/*     */   private long lastUpdateXZ;
/*     */   private int blockX;
/*     */   private int blockZ;
/*     */   private int surfaceDepth;
/*     */   private long lastSurfaceDepth2Update;
/*     */   private double surfaceSecondary;
/*     */   private long lastMinSurfaceLevelUpdate;
/*     */   private int minSurfaceLevel;
/*     */   private long lastUpdateY;
/*     */   private final BlockPos.MutableBlockPos pos;
/*     */   private Supplier<Holder<Biome>> biome;
/*     */   private int blockY;
/*     */   private int waterHeight;
/*     */   private int stoneDepthBelow;
/*     */   private int stoneDepthAbove;
/*     */   
/*     */   protected Context(SurfaceSystem system, RandomState randomState, ChunkAccess chunk, NoiseChunk noiseChunk, Function<BlockPos, Holder<Biome>> biomeGetter, Registry<Biome> biomes, WorldGenerationContext context) {
/*  49 */     this.temperature = new TemperatureHelperCondition(this);
/*  50 */     this.steep = new SteepMaterialCondition(this);
/*  51 */     this.hole = new HoleCondition(this);
/*  52 */     this.abovePreliminarySurface = new AbovePreliminarySurfaceCondition();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     this.lastPreliminarySurfaceCellOrigin = Float.MAX_VALUE;
/*  61 */     this.preliminarySurfaceCache = new int[4];
/*     */ 
/*     */     
/*  64 */     this.lastUpdateXZ = -9223372036854775807L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  70 */     this.lastSurfaceDepth2Update = this.lastUpdateXZ - 1L;
/*     */ 
/*     */     
/*  73 */     this.lastMinSurfaceLevelUpdate = this.lastUpdateXZ - 1L;
/*     */ 
/*     */ 
/*     */     
/*  77 */     this.lastUpdateY = -9223372036854775807L;
/*  78 */     this.pos = new BlockPos.MutableBlockPos();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     this.system = system;
/*  87 */     this.randomState = randomState;
/*  88 */     this.chunk = chunk;
/*  89 */     this.noiseChunk = noiseChunk;
/*  90 */     this.biomeGetter = biomeGetter;
/*  91 */     this.context = context;
/*     */   }
/*     */   
/*     */   protected void updateXZ(int blockX, int blockZ) {
/*  95 */     this.lastUpdateXZ++;
/*  96 */     this.lastUpdateY++;
/*  97 */     this.blockX = blockX;
/*  98 */     this.blockZ = blockZ;
/*  99 */     this.surfaceDepth = this.system.getSurfaceDepth(blockX, blockZ);
/*     */   }
/*     */   
/*     */   protected void updateY(int stoneDepthAbove, int stoneDepthBelow, int waterHeight, int blockX, int blockY, int blockZ) {
/* 103 */     this.lastUpdateY++;
/* 104 */     this.biome = Suppliers.memoize(() -> (Holder)this.biomeGetter.apply(this.pos.set(blockX, blockY, blockZ)));
/* 105 */     this.blockY = blockY;
/* 106 */     this.waterHeight = waterHeight;
/* 107 */     this.stoneDepthBelow = stoneDepthBelow;
/* 108 */     this.stoneDepthAbove = stoneDepthAbove;
/*     */   }
/*     */   
/*     */   protected double getSurfaceSecondary() {
/* 112 */     if (this.lastSurfaceDepth2Update != this.lastUpdateXZ) {
/* 113 */       this.lastSurfaceDepth2Update = this.lastUpdateXZ;
/* 114 */       this.surfaceSecondary = this.system.getSurfaceSecondary(this.blockX, this.blockZ);
/*     */     } 
/* 116 */     return this.surfaceSecondary;
/*     */   }
/*     */ 
/*     */   
/* 120 */   public int getSeaLevel() { return this.system.getSeaLevel(); }
/*     */ 
/*     */ 
/*     */   
/* 124 */   private static int blockCoordToSurfaceCell(int blockCoord) { return blockCoord >> 4; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   private static int surfaceCellToBlockCoord(int cellCoord) { return cellCoord << 4; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMinSurfaceLevel() {
/* 133 */     if (this.lastMinSurfaceLevelUpdate != this.lastUpdateXZ) {
/* 134 */       this.lastMinSurfaceLevelUpdate = this.lastUpdateXZ;
/* 135 */       int cornerCellX = blockCoordToSurfaceCell(this.blockX);
/* 136 */       int cornerCellZ = blockCoordToSurfaceCell(this.blockZ);
/*     */       
/* 138 */       long preliminarySurfaceCellOrigin = ChunkPos.asLong(cornerCellX, cornerCellZ);
/* 139 */       if (this.lastPreliminarySurfaceCellOrigin != preliminarySurfaceCellOrigin) {
/* 140 */         this.lastPreliminarySurfaceCellOrigin = preliminarySurfaceCellOrigin;
/*     */         
/* 142 */         this.preliminarySurfaceCache[0] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ));
/* 143 */         this.preliminarySurfaceCache[1] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ));
/* 144 */         this.preliminarySurfaceCache[2] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ + 1));
/* 145 */         this.preliminarySurfaceCache[3] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ + 1));
/*     */       } 
/* 147 */       int preliminarySurfaceLevel = Mth.floor(Mth.lerp2(((this.blockX & 0xF) / 16.0F), ((this.blockZ & 0xF) / 16.0F), this.preliminarySurfaceCache[0], this.preliminarySurfaceCache[1], this.preliminarySurfaceCache[2], this.preliminarySurfaceCache[3]));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 155 */       this.minSurfaceLevel = preliminarySurfaceLevel + this.surfaceDepth - 8;
/*     */     } 
/* 157 */     return this.minSurfaceLevel;
/*     */   }
/*     */   
/*     */   private static final class HoleCondition
/*     */     extends SurfaceRules.LazyXZCondition {
/* 162 */     private HoleCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 167 */     protected boolean compute() { return (this.context.surfaceDepth <= 0); }
/*     */   }
/*     */ 
/*     */   
/*     */   private final class AbovePreliminarySurfaceCondition
/*     */     implements SurfaceRules.Condition
/*     */   {
/* 174 */     public boolean test() { return (SurfaceRules.Context.this.blockY >= SurfaceRules.Context.this.getMinSurfaceLevel()); }
/*     */   }
/*     */   
/*     */   private static class TemperatureHelperCondition
/*     */     extends SurfaceRules.LazyYCondition
/*     */   {
/* 180 */     private TemperatureHelperCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     protected boolean compute() { return ((Biome)((Holder)this.context.biome.get()).value()).coldEnoughToSnow(this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel()); }
/*     */   }
/*     */   
/*     */   private static class SteepMaterialCondition
/*     */     extends SurfaceRules.LazyXZCondition
/*     */   {
/* 191 */     private SteepMaterialCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean compute() {
/* 196 */       int chunkBlockX = this.context.blockX & 0xF;
/* 197 */       int chunkBlockZ = this.context.blockZ & 0xF;
/*     */       
/* 199 */       int zNorth = Math.max(chunkBlockZ - 1, 0);
/* 200 */       int zSouth = Math.min(chunkBlockZ + 1, 15);
/*     */       
/* 202 */       ChunkAccess chunk = this.context.chunk;
/* 203 */       int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth);
/* 204 */       int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth);
/*     */       
/* 206 */       if (heightSouth >= heightNorth + 4) {
/* 207 */         return true;
/*     */       }
/*     */       
/* 210 */       int xWest = Math.max(chunkBlockX - 1, 0);
/* 211 */       int xEast = Math.min(chunkBlockX + 1, 15);
/* 212 */       int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ);
/* 213 */       int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ);
/*     */       
/* 215 */       return (heightWest >= heightEast + 4);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\SurfaceRules$Context.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */