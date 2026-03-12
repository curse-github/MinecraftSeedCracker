/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.placement.CaveSurface;
/*     */ 
/*     */ public class SurfaceRules {
/*     */   protected static final class Context { private static final int HOW_FAR_BELOW_PRELIMINARY_SURFACE_LEVEL_TO_BUILD_SURFACE = 8;
/*     */     private static final int SURFACE_CELL_BITS = 4;
/*     */     private static final int SURFACE_CELL_SIZE = 16;
/*     */     private static final int SURFACE_CELL_MASK = 15;
/*     */     private final SurfaceSystem system;
/*     */     private final SurfaceRules.Condition temperature;
/*     */     private final SurfaceRules.Condition steep;
/*     */     private final SurfaceRules.Condition hole;
/*     */     private final SurfaceRules.Condition abovePreliminarySurface;
/*     */     private final RandomState randomState;
/*     */     private final ChunkAccess chunk;
/*     */     private final NoiseChunk noiseChunk;
/*     */     private final Function<BlockPos, Holder<Biome>> biomeGetter;
/*     */     private final WorldGenerationContext context;
/*     */     private long lastPreliminarySurfaceCellOrigin;
/*     */     private final int[] preliminarySurfaceCache;
/*     */     private long lastUpdateXZ;
/*     */     private int blockX;
/*     */     private int blockZ;
/*     */     private int surfaceDepth;
/*     */     private long lastSurfaceDepth2Update;
/*     */     private double surfaceSecondary;
/*     */     private long lastMinSurfaceLevelUpdate;
/*     */     private int minSurfaceLevel;
/*     */     private long lastUpdateY;
/*     */     private final BlockPos.MutableBlockPos pos;
/*     */     private Supplier<Holder<Biome>> biome;
/*     */     private int blockY;
/*     */     private int waterHeight;
/*     */     private int stoneDepthBelow;
/*     */     private int stoneDepthAbove;
/*     */     
/*     */     protected Context(SurfaceSystem system, RandomState randomState, ChunkAccess chunk, NoiseChunk noiseChunk, Function<BlockPos, Holder<Biome>> biomeGetter, Registry<Biome> biomes, WorldGenerationContext context) {
/*  49 */       this.temperature = new TemperatureHelperCondition(this);
/*  50 */       this.steep = new SteepMaterialCondition(this);
/*  51 */       this.hole = new HoleCondition(this);
/*  52 */       this.abovePreliminarySurface = new AbovePreliminarySurfaceCondition();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  60 */       this.lastPreliminarySurfaceCellOrigin = Float.MAX_VALUE;
/*  61 */       this.preliminarySurfaceCache = new int[4];
/*     */ 
/*     */       
/*  64 */       this.lastUpdateXZ = -9223372036854775807L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  70 */       this.lastSurfaceDepth2Update = this.lastUpdateXZ - 1L;
/*     */ 
/*     */       
/*  73 */       this.lastMinSurfaceLevelUpdate = this.lastUpdateXZ - 1L;
/*     */ 
/*     */ 
/*     */       
/*  77 */       this.lastUpdateY = -9223372036854775807L;
/*  78 */       this.pos = new BlockPos.MutableBlockPos();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  86 */       this.system = system;
/*  87 */       this.randomState = randomState;
/*  88 */       this.chunk = chunk;
/*  89 */       this.noiseChunk = noiseChunk;
/*  90 */       this.biomeGetter = biomeGetter;
/*  91 */       this.context = context;
/*     */     }
/*     */     
/*     */     protected void updateXZ(int blockX, int blockZ) {
/*  95 */       this.lastUpdateXZ++;
/*  96 */       this.lastUpdateY++;
/*  97 */       this.blockX = blockX;
/*  98 */       this.blockZ = blockZ;
/*  99 */       this.surfaceDepth = this.system.getSurfaceDepth(blockX, blockZ);
/*     */     }
/*     */     
/*     */     protected void updateY(int stoneDepthAbove, int stoneDepthBelow, int waterHeight, int blockX, int blockY, int blockZ) {
/* 103 */       this.lastUpdateY++;
/* 104 */       this.biome = Suppliers.memoize(() -> (Holder)this.biomeGetter.apply(this.pos.set(blockX, blockY, blockZ)));
/* 105 */       this.blockY = blockY;
/* 106 */       this.waterHeight = waterHeight;
/* 107 */       this.stoneDepthBelow = stoneDepthBelow;
/* 108 */       this.stoneDepthAbove = stoneDepthAbove;
/*     */     }
/*     */     
/*     */     protected double getSurfaceSecondary() {
/* 112 */       if (this.lastSurfaceDepth2Update != this.lastUpdateXZ) {
/* 113 */         this.lastSurfaceDepth2Update = this.lastUpdateXZ;
/* 114 */         this.surfaceSecondary = this.system.getSurfaceSecondary(this.blockX, this.blockZ);
/*     */       } 
/* 116 */       return this.surfaceSecondary;
/*     */     }
/*     */ 
/*     */     
/* 120 */     public int getSeaLevel() { return this.system.getSeaLevel(); }
/*     */ 
/*     */ 
/*     */     
/* 124 */     private static int blockCoordToSurfaceCell(int blockCoord) { return blockCoord >> 4; }
/*     */ 
/*     */ 
/*     */     
/* 128 */     private static int surfaceCellToBlockCoord(int cellCoord) { return cellCoord << 4; }
/*     */ 
/*     */ 
/*     */     
/*     */     protected int getMinSurfaceLevel() {
/* 133 */       if (this.lastMinSurfaceLevelUpdate != this.lastUpdateXZ) {
/* 134 */         this.lastMinSurfaceLevelUpdate = this.lastUpdateXZ;
/* 135 */         int cornerCellX = blockCoordToSurfaceCell(this.blockX);
/* 136 */         int cornerCellZ = blockCoordToSurfaceCell(this.blockZ);
/*     */         
/* 138 */         long preliminarySurfaceCellOrigin = ChunkPos.asLong(cornerCellX, cornerCellZ);
/* 139 */         if (this.lastPreliminarySurfaceCellOrigin != preliminarySurfaceCellOrigin) {
/* 140 */           this.lastPreliminarySurfaceCellOrigin = preliminarySurfaceCellOrigin;
/*     */           
/* 142 */           this.preliminarySurfaceCache[0] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ));
/* 143 */           this.preliminarySurfaceCache[1] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ));
/* 144 */           this.preliminarySurfaceCache[2] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX), surfaceCellToBlockCoord(cornerCellZ + 1));
/* 145 */           this.preliminarySurfaceCache[3] = this.noiseChunk.preliminarySurfaceLevel(surfaceCellToBlockCoord(cornerCellX + 1), surfaceCellToBlockCoord(cornerCellZ + 1));
/*     */         } 
/* 147 */         int preliminarySurfaceLevel = Mth.floor(Mth.lerp2(((this.blockX & 0xF) / 16.0F), ((this.blockZ & 0xF) / 16.0F), this.preliminarySurfaceCache[0], this.preliminarySurfaceCache[1], this.preliminarySurfaceCache[2], this.preliminarySurfaceCache[3]));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 155 */         this.minSurfaceLevel = preliminarySurfaceLevel + this.surfaceDepth - 8;
/*     */       } 
/* 157 */       return this.minSurfaceLevel;
/*     */     }
/*     */     
/*     */     private static final class HoleCondition
/*     */       extends SurfaceRules.LazyXZCondition {
/* 162 */       private HoleCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       protected boolean compute() { return (this.context.surfaceDepth <= 0); }
/*     */     }
/*     */ 
/*     */     
/*     */     private final class AbovePreliminarySurfaceCondition
/*     */       implements SurfaceRules.Condition
/*     */     {
/* 174 */       public boolean test() { return (SurfaceRules.Context.this.blockY >= SurfaceRules.Context.this.getMinSurfaceLevel()); }
/*     */     }
/*     */     
/*     */     private static class TemperatureHelperCondition
/*     */       extends SurfaceRules.LazyYCondition
/*     */     {
/* 180 */       private TemperatureHelperCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 185 */       protected boolean compute() { return ((Biome)((Holder)this.context.biome.get()).value()).coldEnoughToSnow(this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel()); }
/*     */     }
/*     */     
/*     */     private static class SteepMaterialCondition
/*     */       extends SurfaceRules.LazyXZCondition
/*     */     {
/* 191 */       private SteepMaterialCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */       
/*     */       protected boolean compute()
/*     */       {
/* 196 */         int chunkBlockX = this.context.blockX & 0xF;
/* 197 */         int chunkBlockZ = this.context.blockZ & 0xF;
/*     */         
/* 199 */         int zNorth = Math.max(chunkBlockZ - 1, 0);
/* 200 */         int zSouth = Math.min(chunkBlockZ + 1, 15);
/*     */         
/* 202 */         ChunkAccess chunk = this.context.chunk;
/* 203 */         int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth);
/* 204 */         int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth);
/*     */         
/* 206 */         if (heightSouth >= heightNorth + 4) {
/* 207 */           return true;
/*     */         }
/*     */         
/* 210 */         int xWest = Math.max(chunkBlockX - 1, 0);
/* 211 */         int xEast = Math.min(chunkBlockX + 1, 15);
/* 212 */         int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ);
/* 213 */         int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ);
/*     */         
/* 215 */         return (heightWest >= heightEast + 4); } } } private static final class HoleCondition extends LazyXZCondition { private HoleCondition(SurfaceRules.Context context) { super(context); } protected boolean compute() { return (this.context.surfaceDepth <= 0); } } private final class AbovePreliminarySurfaceCondition implements Condition { private AbovePreliminarySurfaceCondition() {} public boolean test() { return (this.this$0.blockY >= this.this$0.getMinSurfaceLevel()); } } private static class TemperatureHelperCondition extends LazyYCondition { private TemperatureHelperCondition(SurfaceRules.Context context) { super(context); } protected boolean compute() { return ((Biome)((Holder)this.context.biome.get()).value()).coldEnoughToSnow(this.context.pos.set(this.context.blockX, this.context.blockY, this.context.blockZ), this.context.getSeaLevel()); } } private static class SteepMaterialCondition extends LazyXZCondition { protected boolean compute() { int chunkBlockX = this.context.blockX & 0xF; int chunkBlockZ = this.context.blockZ & 0xF; int zNorth = Math.max(chunkBlockZ - 1, 0); int zSouth = Math.min(chunkBlockZ + 1, 15); ChunkAccess chunk = this.context.chunk; int heightNorth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zNorth); int heightSouth = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, chunkBlockX, zSouth); if (heightSouth >= heightNorth + 4) return true;  int xWest = Math.max(chunkBlockX - 1, 0); int xEast = Math.min(chunkBlockX + 1, 15); int heightWest = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xWest, chunkBlockZ); int heightEast = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, xEast, chunkBlockZ); return (heightWest >= heightEast + 4); }
/*     */ 
/*     */ 
/*     */     
/*     */     private SteepMaterialCondition(SurfaceRules.Context context) { super(context); } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class LazyCondition
/*     */     implements Condition
/*     */   {
/*     */     protected final SurfaceRules.Context context;
/*     */     
/*     */     private long lastUpdate;
/*     */     Boolean result;
/*     */     
/*     */     protected LazyCondition(SurfaceRules.Context context) {
/* 232 */       this.context = context;
/* 233 */       this.lastUpdate = getContextLastUpdate() - 1L;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test() {
/* 238 */       long lastContextUpdate = getContextLastUpdate();
/*     */       
/* 240 */       if (lastContextUpdate == this.lastUpdate) {
/* 241 */         if (this.result == null) {
/* 242 */           throw new IllegalStateException("Update triggered but the result is null");
/*     */         }
/* 244 */         return this.result.booleanValue();
/*     */       } 
/* 246 */       this.lastUpdate = lastContextUpdate;
/*     */       
/* 248 */       this.result = Boolean.valueOf(compute());
/*     */       
/* 250 */       return this.result.booleanValue();
/*     */     }
/*     */     
/*     */     protected abstract long getContextLastUpdate();
/*     */     
/*     */     protected abstract boolean compute();
/*     */   }
/*     */   
/*     */   private static abstract class LazyXZCondition
/*     */     extends LazyCondition {
/* 260 */     protected LazyXZCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     protected long getContextLastUpdate() { return this.context.lastUpdateXZ; }
/*     */   }
/*     */   
/*     */   private static abstract class LazyYCondition
/*     */     extends LazyCondition
/*     */   {
/* 271 */     protected LazyYCondition(SurfaceRules.Context context) { super(context); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     protected long getContextLastUpdate() { return this.context.lastUpdateY; } }
/*     */   
/*     */   private static final class NotCondition extends Record implements Condition { private final SurfaceRules.Condition target;
/*     */     
/* 280 */     private NotCondition(SurfaceRules.Condition target) { this.target = target; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #280	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #280	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #280	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotCondition;
/* 280 */       //   0	8	1	o	Ljava/lang/Object; } public SurfaceRules.Condition target() { return this.target; }
/*     */ 
/*     */     
/* 283 */     public boolean test() { return !this.target.test(); } }
/*     */ 
/*     */ 
/*     */   
/* 287 */   public static final ConditionSource ON_FLOOR = stoneDepthCheck(0, false, CaveSurface.FLOOR);
/* 288 */   public static final ConditionSource UNDER_FLOOR = stoneDepthCheck(0, true, CaveSurface.FLOOR);
/* 289 */   public static final ConditionSource DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 6, CaveSurface.FLOOR);
/* 290 */   public static final ConditionSource VERY_DEEP_UNDER_FLOOR = stoneDepthCheck(0, true, 30, CaveSurface.FLOOR);
/*     */   
/* 292 */   public static final ConditionSource ON_CEILING = stoneDepthCheck(0, false, CaveSurface.CEILING);
/* 293 */   public static final ConditionSource UNDER_CEILING = stoneDepthCheck(0, true, CaveSurface.CEILING);
/*     */ 
/*     */   
/* 296 */   public static ConditionSource stoneDepthCheck(int offset, boolean addSurfaceDepth1, CaveSurface surfaceType) { return new StoneDepthCheck(offset, addSurfaceDepth1, 0, surfaceType); }
/*     */ 
/*     */ 
/*     */   
/* 300 */   public static ConditionSource stoneDepthCheck(int offset, boolean addSurfaceDepth1, int secondaryDepthRange, CaveSurface surfaceType) { return new StoneDepthCheck(offset, addSurfaceDepth1, secondaryDepthRange, surfaceType); }
/*     */ 
/*     */ 
/*     */   
/* 304 */   public static ConditionSource not(ConditionSource target) { return new NotConditionSource(target); }
/*     */ 
/*     */ 
/*     */   
/* 308 */   public static ConditionSource yBlockCheck(VerticalAnchor anchor, int surfaceDepthMultiplier) { return new YConditionSource(anchor, surfaceDepthMultiplier, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 315 */   public static ConditionSource yStartCheck(VerticalAnchor anchor, int surfaceDepthMultiplier) { return new YConditionSource(anchor, surfaceDepthMultiplier, true); }
/*     */ 
/*     */ 
/*     */   
/* 319 */   public static ConditionSource waterBlockCheck(int offset, int surfaceDepthMultiplier) { return new WaterConditionSource(offset, surfaceDepthMultiplier, false); }
/*     */ 
/*     */ 
/*     */   
/* 323 */   public static ConditionSource waterStartCheck(int offset, int surfaceDepthMultiplier) { return new WaterConditionSource(offset, surfaceDepthMultiplier, true); }
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/* 328 */   public static ConditionSource isBiome(ResourceKey... target) { return isBiome(List.of(target)); }
/*     */ 
/*     */ 
/*     */   
/* 332 */   private static BiomeConditionSource isBiome(List<ResourceKey<Biome>> target) { return new BiomeConditionSource(target); }
/*     */ 
/*     */ 
/*     */   
/* 336 */   public static ConditionSource noiseCondition(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange) { return noiseCondition(noise, minRange, Double.MAX_VALUE); }
/*     */ 
/*     */ 
/*     */   
/* 340 */   public static ConditionSource noiseCondition(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange, double maxRange) { return new NoiseThresholdConditionSource(noise, minRange, maxRange); }
/*     */ 
/*     */ 
/*     */   
/* 344 */   public static ConditionSource verticalGradient(String randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) { return new VerticalGradientConditionSource(Identifier.parse(randomName), trueAtAndBelow, falseAtAndAbove); }
/*     */ 
/*     */ 
/*     */   
/* 348 */   public static ConditionSource steep() { return Steep.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/* 352 */   public static ConditionSource hole() { return Hole.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/* 356 */   public static ConditionSource abovePreliminarySurface() { return AbovePreliminarySurface.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/* 360 */   public static ConditionSource temperature() { return Temperature.INSTANCE; }
/*     */   
/*     */   private static final class StateRule
/*     */     extends Record
/*     */     implements SurfaceRule {
/*     */     private final BlockState state;
/*     */     
/* 367 */     private StateRule(BlockState state) { this.state = state; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #367	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StateRule;
/* 367 */       //   0	8	1	o	Ljava/lang/Object; } public BlockState state() { return this.state; }
/*     */ 
/*     */     
/* 370 */     public BlockState tryApply(int blockX, int blockY, int blockZ) { return this.state; } }
/*     */   private static final class TestRule extends Record implements SurfaceRule { private final SurfaceRules.Condition condition;
/*     */     private final SurfaceRules.SurfaceRule followup;
/*     */     
/* 374 */     private TestRule(SurfaceRules.Condition condition, SurfaceRules.SurfaceRule followup) { this.condition = condition; this.followup = followup; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #374	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRule;
/* 374 */       //   0	8	1	o	Ljava/lang/Object; } public SurfaceRules.Condition condition() { return this.condition; } public SurfaceRules.SurfaceRule followup() { return this.followup; }
/*     */     
/*     */     public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 377 */       if (!this.condition.test()) {
/* 378 */         return null;
/*     */       }
/* 380 */       return this.followup.tryApply(blockX, blockY, blockZ);
/*     */     } }
/*     */   private static final class SequenceRule extends Record implements SurfaceRule { private final List<SurfaceRules.SurfaceRule> rules;
/*     */     
/* 384 */     private SequenceRule(List<SurfaceRules.SurfaceRule> rules) { this.rules = rules; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #384	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #384	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #384	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRule;
/* 384 */       //   0	8	1	o	Ljava/lang/Object; } public List<SurfaceRules.SurfaceRule> rules() { return this.rules; }
/*     */     
/*     */     public BlockState tryApply(int blockX, int blockY, int blockZ) {
/* 387 */       for (SurfaceRules.SurfaceRule rule : this.rules) {
/* 388 */         BlockState state = rule.tryApply(blockX, blockY, blockZ);
/* 389 */         if (state != null) {
/* 390 */           return state;
/*     */         }
/*     */       } 
/* 393 */       return null;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/* 398 */   public static RuleSource ifTrue(ConditionSource condition, RuleSource next) { return new TestRuleSource(condition, next); }
/*     */ 
/*     */   
/*     */   public static RuleSource sequence(RuleSource... rules) {
/* 402 */     if (rules.length == 0) {
/* 403 */       throw new IllegalArgumentException("Need at least 1 rule for a sequence");
/*     */     }
/* 405 */     return new SequenceRuleSource(Arrays.asList(rules));
/*     */   }
/*     */ 
/*     */   
/* 409 */   public static RuleSource state(BlockState state) { return new BlockRuleSource(state); }
/*     */ 
/*     */ 
/*     */   
/* 413 */   public static RuleSource bandlands() { return Bandlands.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/* 417 */   private static <A> MapCodec<? extends A> register(Registry<MapCodec<? extends A>> registry, String name, KeyDispatchDataCodec<? extends A> codec) { return (MapCodec)Registry.register(registry, name, codec.codec()); }
/*     */   
/*     */   public static interface ConditionSource
/*     */     extends Function<Context, Condition> {
/* 421 */     public static final Codec<ConditionSource> CODEC = BuiltInRegistries.MATERIAL_CONDITION.byNameCodec().dispatch(source -> source.codec().codec(), Function.identity());
/*     */     KeyDispatchDataCodec<? extends ConditionSource> codec();
/*     */     static MapCodec<? extends ConditionSource> bootstrap(Registry<MapCodec<? extends ConditionSource>> registry) {
/* 424 */       SurfaceRules.register(registry, "biome", SurfaceRules.BiomeConditionSource.CODEC);
/* 425 */       SurfaceRules.register(registry, "noise_threshold", SurfaceRules.NoiseThresholdConditionSource.CODEC);
/* 426 */       SurfaceRules.register(registry, "vertical_gradient", SurfaceRules.VerticalGradientConditionSource.CODEC);
/* 427 */       SurfaceRules.register(registry, "y_above", SurfaceRules.YConditionSource.CODEC);
/* 428 */       SurfaceRules.register(registry, "water", SurfaceRules.WaterConditionSource.CODEC);
/* 429 */       SurfaceRules.register(registry, "temperature", SurfaceRules.Temperature.CODEC);
/* 430 */       SurfaceRules.register(registry, "steep", SurfaceRules.Steep.CODEC);
/* 431 */       SurfaceRules.register(registry, "not", SurfaceRules.NotConditionSource.CODEC);
/* 432 */       SurfaceRules.register(registry, "hole", SurfaceRules.Hole.CODEC);
/* 433 */       SurfaceRules.register(registry, "above_preliminary_surface", SurfaceRules.AbovePreliminarySurface.CODEC);
/* 434 */       return SurfaceRules.register(registry, "stone_depth", SurfaceRules.StoneDepthCheck.CODEC);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface RuleSource
/*     */     extends Function<Context, SurfaceRule>
/*     */   {
/* 441 */     public static final Codec<RuleSource> CODEC = BuiltInRegistries.MATERIAL_RULE.byNameCodec().dispatch(source -> source.codec().codec(), Function.identity());
/*     */     KeyDispatchDataCodec<? extends RuleSource> codec();
/*     */     static MapCodec<? extends RuleSource> bootstrap(Registry<MapCodec<? extends RuleSource>> registry) {
/* 444 */       SurfaceRules.register(registry, "bandlands", SurfaceRules.Bandlands.CODEC);
/* 445 */       SurfaceRules.register(registry, "block", SurfaceRules.BlockRuleSource.CODEC);
/* 446 */       SurfaceRules.register(registry, "sequence", SurfaceRules.SequenceRuleSource.CODEC);
/* 447 */       return SurfaceRules.register(registry, "condition", SurfaceRules.TestRuleSource.CODEC);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NotConditionSource extends Record implements ConditionSource { private final SurfaceRules.ConditionSource target;
/*     */     
/* 453 */     private NotConditionSource(SurfaceRules.ConditionSource target) { this.target = target; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #453	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #453	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #453	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NotConditionSource;
/* 453 */       //   0	8	1	o	Ljava/lang/Object; } public SurfaceRules.ConditionSource target() { return this.target; }
/* 454 */     private static final KeyDispatchDataCodec<NotConditionSource> CODEC = KeyDispatchDataCodec.of(SurfaceRules.ConditionSource.CODEC.xmap(NotConditionSource::new, NotConditionSource::target).fieldOf("invert"));
/*     */ 
/*     */ 
/*     */     
/* 458 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 463 */     public SurfaceRules.Condition apply(SurfaceRules.Context context) { return new SurfaceRules.NotCondition((SurfaceRules.Condition)this.target.apply(context)); } }
/*     */   private static final class StoneDepthCheck extends Record implements ConditionSource { private final int offset; private final boolean addSurfaceDepth; private final int secondaryDepthRange;
/*     */     private final CaveSurface surfaceType;
/*     */     
/* 467 */     private StoneDepthCheck(int offset, boolean addSurfaceDepth, int secondaryDepthRange, CaveSurface surfaceType) { this.offset = offset; this.addSurfaceDepth = addSurfaceDepth; this.secondaryDepthRange = secondaryDepthRange; this.surfaceType = surfaceType; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #467	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #467	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #467	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$StoneDepthCheck;
/* 467 */       //   0	8	1	o	Ljava/lang/Object; } public int offset() { return this.offset; } public boolean addSurfaceDepth() { return this.addSurfaceDepth; } public int secondaryDepthRange() { return this.secondaryDepthRange; } public CaveSurface surfaceType() { return this.surfaceType; }
/* 468 */     private static final KeyDispatchDataCodec<StoneDepthCheck> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 469 */             .fieldOf("offset").forGetter(StoneDepthCheck::offset), Codec.BOOL
/* 470 */             .fieldOf("add_surface_depth").forGetter(StoneDepthCheck::addSurfaceDepth), Codec.INT
/*     */             
/* 472 */             .fieldOf("secondary_depth_range").forGetter(StoneDepthCheck::secondaryDepthRange), CaveSurface.CODEC
/* 473 */             .fieldOf("surface_type").forGetter(StoneDepthCheck::surfaceType))
/* 474 */           .apply(i, StoneDepthCheck::new)));
/*     */ 
/*     */ 
/*     */     
/* 478 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 483 */       final boolean ceiling = (this.surfaceType == CaveSurface.CEILING);
/*     */       class StoneDepthCondition
/*     */         extends SurfaceRules.LazyYCondition
/*     */       {
/* 487 */         private StoneDepthCondition() { super(param2Context); }
/*     */ 
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 492 */           int stoneDepth = ceiling ? this.context.stoneDepthBelow : this.context.stoneDepthAbove;
/* 493 */           int surfaceDepth = SurfaceRules.StoneDepthCheck.this.addSurfaceDepth ? this.context.surfaceDepth : 0;
/* 494 */           int secondarySurfaceDepth = (SurfaceRules.StoneDepthCheck.this.secondaryDepthRange == 0) ? 0 : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0D, 1.0D, 0.0D, SurfaceRules.StoneDepthCheck.this.secondaryDepthRange);
/*     */           
/* 496 */           return (stoneDepth <= 1 + SurfaceRules.StoneDepthCheck.this.offset + surfaceDepth + secondarySurfaceDepth);
/*     */         }
/*     */       };
/*     */       
/* 500 */       return new StoneDepthCondition();
/*     */     } } class StoneDepthCondition extends LazyYCondition { private StoneDepthCondition() { super(param1Context); } protected boolean compute() { int stoneDepth = ceiling ? this.context.stoneDepthBelow : this.context.stoneDepthAbove;
/*     */       int surfaceDepth = SurfaceRules.StoneDepthCheck.this.addSurfaceDepth ? this.context.surfaceDepth : 0;
/*     */       int secondarySurfaceDepth = (SurfaceRules.StoneDepthCheck.this.secondaryDepthRange == 0) ? 0 : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0D, 1.0D, 0.0D, SurfaceRules.StoneDepthCheck.this.secondaryDepthRange);
/*     */       return (stoneDepth <= 1 + SurfaceRules.StoneDepthCheck.this.offset + surfaceDepth + secondarySurfaceDepth); } }
/* 505 */   private enum AbovePreliminarySurface implements ConditionSource { INSTANCE; private static final KeyDispatchDataCodec<AbovePreliminarySurface> CODEC; static  {
/* 506 */       CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */     }
/*     */ 
/*     */     
/* 510 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 515 */     public SurfaceRules.Condition apply(SurfaceRules.Context context) { return context.abovePreliminarySurface; } }
/*     */ 
/*     */   
/*     */   private enum Hole
/*     */     implements ConditionSource {
/* 520 */     INSTANCE; private static final KeyDispatchDataCodec<Hole> CODEC; static  {
/* 521 */       CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */     }
/*     */ 
/*     */     
/* 525 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 530 */     public SurfaceRules.Condition apply(SurfaceRules.Context context) { return context.hole; } }
/*     */   private static final class YConditionSource extends Record implements ConditionSource { private final VerticalAnchor anchor; private final int surfaceDepthMultiplier;
/*     */     private final boolean addStoneDepth;
/*     */     
/* 534 */     private YConditionSource(VerticalAnchor anchor, int surfaceDepthMultiplier, boolean addStoneDepth) { this.anchor = anchor; this.surfaceDepthMultiplier = surfaceDepthMultiplier; this.addStoneDepth = addStoneDepth; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #534	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #534	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #534	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$YConditionSource;
/* 534 */       //   0	8	1	o	Ljava/lang/Object; } public VerticalAnchor anchor() { return this.anchor; } public int surfaceDepthMultiplier() { return this.surfaceDepthMultiplier; } public boolean addStoneDepth() { return this.addStoneDepth; }
/* 535 */     private static final KeyDispatchDataCodec<YConditionSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group(VerticalAnchor.CODEC
/* 536 */             .fieldOf("anchor").forGetter(YConditionSource::anchor), 
/* 537 */             Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(YConditionSource::surfaceDepthMultiplier), Codec.BOOL
/* 538 */             .fieldOf("add_stone_depth").forGetter(YConditionSource::addStoneDepth))
/* 539 */           .apply(i, YConditionSource::new)));
/*     */ 
/*     */ 
/*     */     
/* 543 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */       class YCondition
/*     */         extends SurfaceRules.LazyYCondition
/*     */       {
/* 550 */         private YCondition() { super(param2Context); }
/*     */ 
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 555 */           return (this.context.blockY + (SurfaceRules.YConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= SurfaceRules.YConditionSource.this.anchor.resolveY(this.context.context) + this.context.surfaceDepth * SurfaceRules.YConditionSource.this.surfaceDepthMultiplier);
/*     */         }
/*     */       };
/*     */       
/* 559 */       return new YCondition();
/*     */     } } class YCondition extends LazyYCondition { private YCondition() { super(param1Context); }
/*     */     protected boolean compute() { return (this.context.blockY + (SurfaceRules.YConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= SurfaceRules.YConditionSource.this.anchor.resolveY(this.context.context) + this.context.surfaceDepth * SurfaceRules.YConditionSource.this.surfaceDepthMultiplier); } }
/*     */   private static final class WaterConditionSource extends Record implements ConditionSource { private final int offset; private final int surfaceDepthMultiplier; private final boolean addStoneDepth;
/* 563 */     private WaterConditionSource(int offset, int surfaceDepthMultiplier, boolean addStoneDepth) { this.offset = offset; this.surfaceDepthMultiplier = surfaceDepthMultiplier; this.addStoneDepth = addStoneDepth; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #563	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #563	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #563	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$WaterConditionSource;
/* 563 */       //   0	8	1	o	Ljava/lang/Object; } public int offset() { return this.offset; } public int surfaceDepthMultiplier() { return this.surfaceDepthMultiplier; } public boolean addStoneDepth() { return this.addStoneDepth; }
/* 564 */     private static final KeyDispatchDataCodec<WaterConditionSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 565 */             .fieldOf("offset").forGetter(WaterConditionSource::offset), 
/* 566 */             Codec.intRange(-20, 20).fieldOf("surface_depth_multiplier").forGetter(WaterConditionSource::surfaceDepthMultiplier), Codec.BOOL
/* 567 */             .fieldOf("add_stone_depth").forGetter(WaterConditionSource::addStoneDepth))
/* 568 */           .apply(i, WaterConditionSource::new)));
/*     */ 
/*     */ 
/*     */     
/* 572 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */       class WaterCondition
/*     */         extends SurfaceRules.LazyYCondition
/*     */       {
/* 579 */         private WaterCondition() { super(param2Context); }
/*     */ 
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 584 */           return (this.context.waterHeight == Integer.MIN_VALUE || this.context.blockY + (SurfaceRules.WaterConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= this.context.waterHeight + SurfaceRules.WaterConditionSource.this.offset + this.context.surfaceDepth * SurfaceRules.WaterConditionSource.this.surfaceDepthMultiplier);
/*     */         }
/*     */       };
/*     */       
/* 588 */       return new WaterCondition();
/*     */     } }
/*     */   class WaterCondition extends LazyYCondition { private WaterCondition() { super(param1Context); }
/*     */     protected boolean compute() { return (this.context.waterHeight == Integer.MIN_VALUE || this.context.blockY + (SurfaceRules.WaterConditionSource.this.addStoneDepth ? this.context.stoneDepthAbove : 0) >= this.context.waterHeight + SurfaceRules.WaterConditionSource.this.offset + this.context.surfaceDepth * SurfaceRules.WaterConditionSource.this.surfaceDepthMultiplier); } }
/*     */   
/* 593 */   private static final class BiomeConditionSource implements ConditionSource { private static final KeyDispatchDataCodec<BiomeConditionSource> CODEC = KeyDispatchDataCodec.of(ResourceKey.codec(Registries.BIOME).listOf().fieldOf("biome_is").xmap(SurfaceRules::isBiome, e -> e.biomes));
/*     */     
/*     */     private final List<ResourceKey<Biome>> biomes;
/*     */     private final Predicate<ResourceKey<Biome>> biomeNameTest;
/*     */     
/*     */     private BiomeConditionSource(List<ResourceKey<Biome>> biomes) {
/* 599 */       this.biomes = biomes;
/* 600 */       Objects.requireNonNull(Set.copyOf(biomes)); this.biomeNameTest = Set.copyOf(biomes)::contains;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 605 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/*     */       class BiomeCondition
/*     */         extends SurfaceRules.LazyYCondition
/*     */       {
/* 612 */         private BiomeCondition() { super(param2Context); }
/*     */ 
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 617 */           return ((Holder)this.context.biome.get()).is(SurfaceRules.BiomeConditionSource.this.biomeNameTest);
/*     */         }
/*     */       };
/*     */       
/* 621 */       return new BiomeCondition();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 626 */       if (this == o) {
/* 627 */         return true;
/*     */       }
/* 629 */       if (o instanceof BiomeConditionSource) { BiomeConditionSource that = (BiomeConditionSource)o;
/* 630 */         return this.biomes.equals(that.biomes); }
/*     */       
/* 632 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 637 */     public int hashCode() { return this.biomes.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 642 */     public String toString() { return "BiomeConditionSource[biomes=" + String.valueOf(this.biomes) + "]"; } }
/*     */   class BiomeCondition extends LazyYCondition { private BiomeCondition() { super(param1Context); }
/*     */     protected boolean compute() { return ((Holder)this.context.biome.get()).is(SurfaceRules.BiomeConditionSource.this.biomeNameTest); } }
/*     */   private static final class NoiseThresholdConditionSource extends Record implements ConditionSource { private final ResourceKey<NormalNoise.NoiseParameters> noise; private final double minThreshold; private final double maxThreshold;
/* 646 */     private NoiseThresholdConditionSource(ResourceKey<NormalNoise.NoiseParameters> noise, double minThreshold, double maxThreshold) { this.noise = noise; this.minThreshold = minThreshold; this.maxThreshold = maxThreshold; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #646	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #646	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #646	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$NoiseThresholdConditionSource;
/* 646 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<NormalNoise.NoiseParameters> noise() { return this.noise; } public double minThreshold() { return this.minThreshold; } public double maxThreshold() { return this.maxThreshold; }
/* 647 */     private static final KeyDispatchDataCodec<NoiseThresholdConditionSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group(
/*     */             
/* 649 */             ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(NoiseThresholdConditionSource::noise), Codec.DOUBLE
/* 650 */             .fieldOf("min_threshold").forGetter(NoiseThresholdConditionSource::minThreshold), Codec.DOUBLE
/* 651 */             .fieldOf("max_threshold").forGetter(NoiseThresholdConditionSource::maxThreshold))
/* 652 */           .apply(i, NoiseThresholdConditionSource::new)));
/*     */ 
/*     */ 
/*     */     
/* 656 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 661 */       final NormalNoise noise = ruleContext.randomState.getOrCreateNoise(this.noise);
/*     */       class NoiseThresholdCondition
/*     */         extends SurfaceRules.LazyXZCondition {
/* 664 */         private NoiseThresholdCondition() { super(param2Context); }
/*     */ 
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 669 */           double value = noise.getValue(this.context.blockX, 0.0D, this.context.blockZ);
/* 670 */           return (value >= SurfaceRules.NoiseThresholdConditionSource.this.minThreshold && value <= SurfaceRules.NoiseThresholdConditionSource.this.maxThreshold);
/*     */         }
/*     */       };
/*     */       
/* 674 */       return new NoiseThresholdCondition();
/*     */     } } class NoiseThresholdCondition extends LazyXZCondition { private NoiseThresholdCondition() { super(param1Context); } protected boolean compute() {
/*     */       double value = noise.getValue(this.context.blockX, 0.0D, this.context.blockZ);
/*     */       return (value >= SurfaceRules.NoiseThresholdConditionSource.this.minThreshold && value <= SurfaceRules.NoiseThresholdConditionSource.this.maxThreshold);
/* 678 */     } } private static final class VerticalGradientConditionSource extends Record implements ConditionSource { private final Identifier randomName; private final VerticalAnchor trueAtAndBelow; private final VerticalAnchor falseAtAndAbove; private VerticalGradientConditionSource(Identifier randomName, VerticalAnchor trueAtAndBelow, VerticalAnchor falseAtAndAbove) { this.randomName = randomName; this.trueAtAndBelow = trueAtAndBelow; this.falseAtAndAbove = falseAtAndAbove; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #678	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #678	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #678	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$VerticalGradientConditionSource;
/* 678 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier randomName() { return this.randomName; } public VerticalAnchor trueAtAndBelow() { return this.trueAtAndBelow; } public VerticalAnchor falseAtAndAbove() { return this.falseAtAndAbove; }
/* 679 */     private static final KeyDispatchDataCodec<VerticalGradientConditionSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 680 */             .fieldOf("random_name").forGetter(VerticalGradientConditionSource::randomName), VerticalAnchor.CODEC
/* 681 */             .fieldOf("true_at_and_below").forGetter(VerticalGradientConditionSource::trueAtAndBelow), VerticalAnchor.CODEC
/* 682 */             .fieldOf("false_at_and_above").forGetter(VerticalGradientConditionSource::falseAtAndAbove))
/* 683 */           .apply(i, VerticalGradientConditionSource::new)));
/*     */ 
/*     */ 
/*     */     
/* 687 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */     
/*     */     public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
/* 692 */       final int trueAtAndBelow = trueAtAndBelow().resolveY(ruleContext.context);
/* 693 */       final int falseAtAndAbove = falseAtAndAbove().resolveY(ruleContext.context);
/* 694 */       final PositionalRandomFactory randomFactory = ruleContext.randomState.getOrCreateRandomFactory(randomName());
/*     */       class VerticalGradientCondition
/*     */         extends SurfaceRules.LazyYCondition
/*     */       {
/* 698 */         private VerticalGradientCondition(SurfaceRules.VerticalGradientConditionSource this$0) { super(param2Context); }
/*     */ 
/*     */ 
/*     */         
/*     */         protected boolean compute() {
/* 703 */           int blockY = this.context.blockY;
/* 704 */           if (blockY <= trueAtAndBelow) {
/* 705 */             return true;
/*     */           }
/* 707 */           if (blockY >= falseAtAndAbove) {
/* 708 */             return false;
/*     */           }
/* 710 */           double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0D, 0.0D);
/* 711 */           RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ);
/* 712 */           return (random.nextFloat() < probability);
/*     */         }
/*     */       };
/* 715 */       return new VerticalGradientCondition(this);
/*     */     } } class VerticalGradientCondition extends LazyYCondition { private VerticalGradientCondition(SurfaceRules.VerticalGradientConditionSource this$0) { super(param1Context); } protected boolean compute() { int blockY = this.context.blockY; if (blockY <= trueAtAndBelow)
/*     */         return true;  if (blockY >= falseAtAndAbove)
/*     */         return false;  double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0D, 0.0D); RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ);
/*     */       return (random.nextFloat() < probability); } }
/* 720 */   private enum Temperature implements ConditionSource { INSTANCE; private static final KeyDispatchDataCodec<Temperature> CODEC; static  {
/* 721 */       CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */     }
/*     */ 
/*     */     
/* 725 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 730 */     public SurfaceRules.Condition apply(SurfaceRules.Context context) { return context.temperature; } }
/*     */ 
/*     */   
/*     */   private enum Steep
/*     */     implements ConditionSource {
/* 735 */     INSTANCE; private static final KeyDispatchDataCodec<Steep> CODEC; static  {
/* 736 */       CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */     }
/*     */ 
/*     */     
/* 740 */     public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 745 */     public SurfaceRules.Condition apply(SurfaceRules.Context context) { return context.steep; } }
/*     */   private static final class BlockRuleSource extends Record implements RuleSource { private final BlockState resultState;
/*     */     private final SurfaceRules.StateRule rule;
/*     */     
/* 749 */     private BlockRuleSource(BlockState resultState, SurfaceRules.StateRule rule) { this.resultState = resultState; this.rule = rule; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #749	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #749	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #749	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$BlockRuleSource;
/* 749 */       //   0	8	1	o	Ljava/lang/Object; } public BlockState resultState() { return this.resultState; } public SurfaceRules.StateRule rule() { return this.rule; }
/* 750 */     private static final KeyDispatchDataCodec<BlockRuleSource> CODEC = KeyDispatchDataCodec.of(BlockState.CODEC.xmap(BlockRuleSource::new, BlockRuleSource::resultState).fieldOf("result_state"));
/*     */ 
/*     */     
/* 753 */     private BlockRuleSource(BlockState state) { this(state, new SurfaceRules.StateRule(state)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 758 */     public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 763 */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) { return this.rule; } }
/*     */   private static final class TestRuleSource extends Record implements RuleSource { private final SurfaceRules.ConditionSource ifTrue;
/*     */     private final SurfaceRules.RuleSource thenRun;
/*     */     
/* 767 */     private TestRuleSource(SurfaceRules.ConditionSource ifTrue, SurfaceRules.RuleSource thenRun) { this.ifTrue = ifTrue; this.thenRun = thenRun; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #767	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #767	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #767	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$TestRuleSource;
/* 767 */       //   0	8	1	o	Ljava/lang/Object; } public SurfaceRules.ConditionSource ifTrue() { return this.ifTrue; } public SurfaceRules.RuleSource thenRun() { return this.thenRun; }
/* 768 */     private static final KeyDispatchDataCodec<TestRuleSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group(SurfaceRules.ConditionSource.CODEC
/* 769 */             .fieldOf("if_true").forGetter(TestRuleSource::ifTrue), SurfaceRules.RuleSource.CODEC
/* 770 */             .fieldOf("then_run").forGetter(TestRuleSource::thenRun))
/* 771 */           .apply(i, TestRuleSource::new)));
/*     */ 
/*     */ 
/*     */     
/* 775 */     public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 780 */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) { return new SurfaceRules.TestRule((SurfaceRules.Condition)this.ifTrue.apply(context), (SurfaceRules.SurfaceRule)this.thenRun.apply(context)); } }
/*     */   
/*     */   private static final class SequenceRuleSource extends Record implements RuleSource { private final List<SurfaceRules.RuleSource> sequence;
/*     */     
/* 784 */     private SequenceRuleSource(List<SurfaceRules.RuleSource> sequence) { this.sequence = sequence; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #784	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #784	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #784	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/SurfaceRules$SequenceRuleSource;
/* 784 */       //   0	8	1	o	Ljava/lang/Object; } public List<SurfaceRules.RuleSource> sequence() { return this.sequence; }
/* 785 */     private static final KeyDispatchDataCodec<SequenceRuleSource> CODEC = KeyDispatchDataCodec.of(SurfaceRules.RuleSource.CODEC.listOf().xmap(SequenceRuleSource::new, SequenceRuleSource::sequence).fieldOf("sequence"));
/*     */ 
/*     */ 
/*     */     
/* 789 */     public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */     
/*     */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
/* 794 */       if (this.sequence.size() == 1) {
/* 795 */         return (SurfaceRules.SurfaceRule)((SurfaceRules.RuleSource)this.sequence.get(0)).apply(context);
/*     */       }
/*     */       
/* 798 */       ImmutableList.Builder<SurfaceRules.SurfaceRule> builder = ImmutableList.builder();
/* 799 */       for (SurfaceRules.RuleSource rule : this.sequence) {
/* 800 */         builder.add((SurfaceRules.SurfaceRule)rule.apply(context));
/*     */       }
/* 802 */       return new SurfaceRules.SequenceRule(builder.build());
/*     */     } }
/*     */ 
/*     */   
/*     */   private enum Bandlands implements RuleSource {
/* 807 */     INSTANCE; private static final KeyDispatchDataCodec<Bandlands> CODEC; static  {
/* 808 */       CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*     */     }
/*     */ 
/*     */     
/* 812 */     public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 817 */     public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) { Objects.requireNonNull(context.system); return context.system::getBand; }
/*     */   }
/*     */   
/*     */   protected static interface SurfaceRule {
/*     */     BlockState tryApply(int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */   
/*     */   private static interface Condition {
/*     */     boolean test();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\SurfaceRules.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */