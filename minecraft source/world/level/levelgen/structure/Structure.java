/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public abstract class Structure {
/*  48 */   public static final Codec<Structure> DIRECT_CODEC = BuiltInRegistries.STRUCTURE_TYPE.byNameCodec().dispatch(Structure::type, StructureType::codec);
/*  49 */   public static final Codec<Holder<Structure>> CODEC = RegistryFileCodec.create(Registries.STRUCTURE, DIRECT_CODEC); protected final StructureSettings settings;
/*     */   public static final class StructureSettings extends Record { private final HolderSet<Biome> biomes; private final Map<MobCategory, StructureSpawnOverride> spawnOverrides; private final GenerationStep.Decoration step; private final TerrainAdjustment terrainAdaptation;
/*  51 */     public StructureSettings(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawnOverrides, GenerationStep.Decoration step, TerrainAdjustment terrainAdaptation) { this.biomes = biomes; this.spawnOverrides = spawnOverrides; this.step = step; this.terrainAdaptation = terrainAdaptation; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;
/*  51 */       //   0	8	1	o	Ljava/lang/Object; } public HolderSet<Biome> biomes() { return this.biomes; } public Map<MobCategory, StructureSpawnOverride> spawnOverrides() { return this.spawnOverrides; } public GenerationStep.Decoration step() { return this.step; } public TerrainAdjustment terrainAdaptation() { return this.terrainAdaptation; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     private static final StructureSettings DEFAULT = new StructureSettings(HolderSet.direct(new Holder[0]), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
/*  58 */     public static final MapCodec<StructureSettings> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  59 */           RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(StructureSettings::biomes), 
/*  60 */           Codec.simpleMap(MobCategory.CODEC, StructureSpawnOverride.CODEC, StringRepresentable.keys(MobCategory.values())).fieldOf("spawn_overrides").forGetter(StructureSettings::spawnOverrides), GenerationStep.Decoration.CODEC
/*  61 */           .fieldOf("step").forGetter(StructureSettings::step), TerrainAdjustment.CODEC
/*  62 */           .optionalFieldOf("terrain_adaptation", DEFAULT.terrainAdaptation).forGetter(StructureSettings::terrainAdaptation))
/*  63 */         .apply(i, StructureSettings::new));
/*     */ 
/*     */ 
/*     */     
/*  67 */     public StructureSettings(HolderSet<Biome> biomes) { this(biomes, DEFAULT.spawnOverrides, DEFAULT.step, DEFAULT.terrainAdaptation); }
/*     */     public static class Builder { private final HolderSet<Biome> biomes; private Map<MobCategory, StructureSpawnOverride> spawnOverrides; private GenerationStep.Decoration step;
/*     */       private TerrainAdjustment terrainAdaption;
/*     */       
/*     */       public Builder(HolderSet<Biome> biomes) {
/*  72 */         this.spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides;
/*  73 */         this.step = Structure.StructureSettings.DEFAULT.step;
/*  74 */         this.terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation;
/*     */ 
/*     */         
/*  77 */         this.biomes = biomes;
/*     */       }
/*     */       
/*     */       public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
/*  81 */         this.spawnOverrides = spawnOverrides;
/*  82 */         return this;
/*     */       }
/*     */       
/*     */       public Builder generationStep(GenerationStep.Decoration step) {
/*  86 */         this.step = step;
/*  87 */         return this;
/*     */       }
/*     */       
/*     */       public Builder terrainAdapation(TerrainAdjustment terrainAdaption) {
/*  91 */         this.terrainAdaption = terrainAdaption;
/*  92 */         return this;
/*     */       }
/*     */ 
/*     */       
/*  96 */       public Structure.StructureSettings build() { return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption); } } } public static class Builder { private final HolderSet<Biome> biomes; private Map<MobCategory, StructureSpawnOverride> spawnOverrides; private GenerationStep.Decoration step; private TerrainAdjustment terrainAdaption; public Builder(HolderSet<Biome> biomes) { this.spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides; this.step = Structure.StructureSettings.DEFAULT.step; this.terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation; this.biomes = biomes; } public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> spawnOverrides) { this.spawnOverrides = spawnOverrides; return this; } public Structure.StructureSettings build() { return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption); } public Builder generationStep(GenerationStep.Decoration step) {
/*     */       this.step = step;
/*     */       return this;
/*     */     } public Builder terrainAdapation(TerrainAdjustment terrainAdaption) {
/*     */       this.terrainAdaption = terrainAdaption;
/*     */       return this;
/* 102 */     } } public static <S extends Structure> RecordCodecBuilder<S, StructureSettings> settingsCodec(RecordCodecBuilder.Instance<S> i) { return StructureSettings.CODEC.forGetter(e -> e.settings); }
/*     */ 
/*     */ 
/*     */   
/* 106 */   public static <S extends Structure> MapCodec<S> simpleCodec(Function<StructureSettings, S> constructor) { return RecordCodecBuilder.mapCodec(i -> i.group(settingsCodec(i)).apply(i, constructor)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected Structure(StructureSettings settings) { this.settings = settings; }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public HolderSet<Biome> biomes() { return this.settings.biomes; }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public Map<MobCategory, StructureSpawnOverride> spawnOverrides() { return this.settings.spawnOverrides; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public GenerationStep.Decoration step() { return this.settings.step; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public TerrainAdjustment terrainAdaptation() { return this.settings.terrainAdaptation; }
/*     */ 
/*     */   
/*     */   public BoundingBox adjustBoundingBox(BoundingBox boundingBox) {
/* 132 */     if (terrainAdaptation() != TerrainAdjustment.NONE) {
/* 133 */       return boundingBox.inflatedBy(12);
/*     */     }
/* 135 */     return boundingBox;
/*     */   }
/*     */   public static final class GenerationContext extends Record { private final RegistryAccess registryAccess; private final ChunkGenerator chunkGenerator; private final BiomeSource biomeSource; private final RandomState randomState; private final StructureTemplateManager structureTemplateManager; private final WorldgenRandom random; private final long seed; private final ChunkPos chunkPos; private final LevelHeightAccessor heightAccessor; private final Predicate<Holder<Biome>> validBiome;
/* 138 */     public GenerationContext(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, WorldgenRandom random, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) { this.registryAccess = registryAccess; this.chunkGenerator = chunkGenerator; this.biomeSource = biomeSource; this.randomState = randomState; this.structureTemplateManager = structureTemplateManager; this.random = random; this.seed = seed; this.chunkPos = chunkPos; this.heightAccessor = heightAccessor; this.validBiome = validBiome; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;
/* 138 */       //   0	8	1	o	Ljava/lang/Object; } public RegistryAccess registryAccess() { return this.registryAccess; } public ChunkGenerator chunkGenerator() { return this.chunkGenerator; } public BiomeSource biomeSource() { return this.biomeSource; } public RandomState randomState() { return this.randomState; } public StructureTemplateManager structureTemplateManager() { return this.structureTemplateManager; } public WorldgenRandom random() { return this.random; } public long seed() { return this.seed; } public ChunkPos chunkPos() { return this.chunkPos; } public LevelHeightAccessor heightAccessor() { return this.heightAccessor; } public Predicate<Holder<Biome>> validBiome() { return this.validBiome; }
/*     */     
/* 140 */     public GenerationContext(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) { this(registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, makeRandom(seed, chunkPos), seed, chunkPos, heightAccessor, validBiome); }
/*     */ 
/*     */     
/*     */     private static WorldgenRandom makeRandom(long seed, ChunkPos chunkPos) {
/* 144 */       WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
/* 145 */       random.setLargeFeatureSeed(seed, chunkPos.x, chunkPos.z);
/* 146 */       return random;
/*     */     } }
/*     */   public static final class GenerationStub extends Record { private final BlockPos position; private final Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator;
/* 149 */     public GenerationStub(BlockPos position, Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator) { this.position = position; this.generator = generator; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #149	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #149	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #149	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationStub;
/* 149 */       //   0	8	1	o	Ljava/lang/Object; } public BlockPos position() { return this.position; } public Either<Consumer<StructurePiecesBuilder>, StructurePiecesBuilder> generator() { return this.generator; }
/*     */     
/* 151 */     public GenerationStub(BlockPos position, Consumer<StructurePiecesBuilder> generator) { this(position, Either.left(generator)); }
/*     */     
/*     */     public StructurePiecesBuilder getPiecesBuilder() {
/* 154 */       return (StructurePiecesBuilder)this.generator.map(pieceGenerator -> {
/* 155 */             StructurePiecesBuilder newBuilder = new StructurePiecesBuilder();
/* 156 */             pieceGenerator.accept(newBuilder);
/* 157 */             return newBuilder;
/* 158 */           }previousBuilder -> previousBuilder);
/*     */     } }
/*     */ 
/*     */   
/*     */   public StructureStart generate(Holder<Structure> selected, ResourceKey<Level> dimension, RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkPos sourceChunkPos, int references, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) {
/* 163 */     ProfiledDuration profiled = JvmProfiler.INSTANCE.onStructureGenerate(sourceChunkPos, dimension, selected);
/*     */     
/* 165 */     GenerationContext context = new GenerationContext(registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, seed, sourceChunkPos, heightAccessor, validBiome);
/* 166 */     Optional<GenerationStub> generation = findValidGenerationPoint(context);
/* 167 */     if (generation.isPresent()) {
/* 168 */       StructurePiecesBuilder builder = ((GenerationStub)generation.get()).getPiecesBuilder();
/*     */       
/* 170 */       StructureStart testStart = new StructureStart(this, sourceChunkPos, references, builder.build());
/* 171 */       if (testStart.isValid()) {
/* 172 */         if (profiled != null) {
/* 173 */           profiled.finish(true);
/*     */         }
/* 175 */         return testStart;
/*     */       } 
/*     */     } 
/*     */     
/* 179 */     if (profiled != null) {
/* 180 */       profiled.finish(false);
/*     */     }
/* 182 */     return StructureStart.INVALID_START;
/*     */   }
/*     */   
/*     */   protected static Optional<GenerationStub> onTopOfChunkCenter(GenerationContext context, Heightmap.Types heightmap, Consumer<StructurePiecesBuilder> generator) {
/* 186 */     ChunkPos chunkPos = context.chunkPos();
/* 187 */     int blockX = chunkPos.getMiddleBlockX();
/* 188 */     int blockZ = chunkPos.getMiddleBlockZ();
/* 189 */     int blockY = context.chunkGenerator().getFirstOccupiedHeight(blockX, blockZ, heightmap, context.heightAccessor(), context.randomState());
/* 190 */     return Optional.of(new GenerationStub(new BlockPos(blockX, blockY, blockZ), generator));
/*     */   }
/*     */   
/*     */   private static boolean isValidBiome(GenerationStub stub, GenerationContext context) {
/* 194 */     BlockPos startPos = stub.position();
/* 195 */     return context.validBiome.test(context.chunkGenerator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(startPos.getX()), QuartPos.fromBlock(startPos.getY()), QuartPos.fromBlock(startPos.getZ()), context.randomState.sampler()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, PiecesContainer pieces) {}
/*     */   
/*     */   private static int[] getCornerHeights(GenerationContext context, int minX, int sizeX, int minZ, int sizeZ) {
/* 202 */     ChunkGenerator chunkGenerator = context.chunkGenerator();
/* 203 */     LevelHeightAccessor heightAccessor = context.heightAccessor();
/* 204 */     RandomState randomState = context.randomState();
/* 205 */     return new int[] { chunkGenerator
/* 206 */         .getFirstOccupiedHeight(minX, minZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState), chunkGenerator
/* 207 */         .getFirstOccupiedHeight(minX, minZ + sizeZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState), chunkGenerator
/* 208 */         .getFirstOccupiedHeight(minX + sizeX, minZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState), chunkGenerator
/* 209 */         .getFirstOccupiedHeight(minX + sizeX, minZ + sizeZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState) };
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getMeanFirstOccupiedHeight(GenerationContext context, int minX, int sizeX, int minZ, int sizeZ) {
/* 214 */     int[] cornerHeights = getCornerHeights(context, minX, sizeX, minZ, sizeZ);
/* 215 */     return (cornerHeights[0] + cornerHeights[1] + cornerHeights[2] + cornerHeights[3]) / 4;
/*     */   }
/*     */   
/*     */   protected static int getLowestY(GenerationContext context, int sizeX, int sizeZ) {
/* 219 */     ChunkPos chunkPos = context.chunkPos();
/* 220 */     int minX = chunkPos.getMinBlockX();
/* 221 */     int minZ = chunkPos.getMinBlockZ();
/* 222 */     return getLowestY(context, minX, minZ, sizeX, sizeZ);
/*     */   }
/*     */   
/*     */   protected static int getLowestY(GenerationContext context, int minX, int minZ, int sizeX, int sizeZ) {
/* 226 */     int[] cornerHeights = getCornerHeights(context, minX, sizeX, minZ, sizeZ);
/* 227 */     return Math.min(Math.min(cornerHeights[0], cornerHeights[1]), Math.min(cornerHeights[2], cornerHeights[3]));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected BlockPos getLowestYIn5by5BoxOffset7Blocks(GenerationContext context, Rotation rotation) {
/* 232 */     int offsetX = 5;
/* 233 */     int offsetZ = 5;
/* 234 */     if (rotation == Rotation.CLOCKWISE_90) {
/* 235 */       offsetX = -5;
/* 236 */     } else if (rotation == Rotation.CLOCKWISE_180) {
/* 237 */       offsetX = -5;
/* 238 */       offsetZ = -5;
/* 239 */     } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
/* 240 */       offsetZ = -5;
/*     */     } 
/*     */     
/* 243 */     ChunkPos chunkPos = context.chunkPos();
/* 244 */     int blockX = chunkPos.getBlockX(7);
/* 245 */     int blockZ = chunkPos.getBlockZ(7);
/* 246 */     return new BlockPos(blockX, getLowestY(context, blockX, blockZ, offsetX, offsetZ), blockZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 252 */   public Optional<GenerationStub> findValidGenerationPoint(GenerationContext context) { return findGenerationPoint(context).filter(generation -> isValidBiome(generation, context)); }
/*     */   
/*     */   protected abstract Optional<GenerationStub> findGenerationPoint(GenerationContext paramGenerationContext);
/*     */   
/*     */   public abstract StructureType<?> type();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\Structure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */