/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*     */ import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureType;
/*     */ import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ 
/*     */ public final class JigsawStructure extends Structure {
/*  35 */   public static final DimensionPadding DEFAULT_DIMENSION_PADDING = DimensionPadding.ZERO;
/*  36 */   public static final LiquidSettings DEFAULT_LIQUID_SETTINGS = LiquidSettings.APPLY_WATERLOGGING;
/*     */   public static final int MAX_TOTAL_STRUCTURE_RANGE = 128;
/*     */   public static final int MIN_DEPTH = 0;
/*     */   public static final int MAX_DEPTH = 20;
/*  40 */   public static final MapCodec<JigsawStructure> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  41 */         settingsCodec(i), StructureTemplatePool.CODEC
/*  42 */         .fieldOf("start_pool").forGetter(()), Identifier.CODEC
/*  43 */         .optionalFieldOf("start_jigsaw_name").forGetter(()), 
/*  44 */         Codec.intRange(0, 20).fieldOf("size").forGetter(()), HeightProvider.CODEC
/*  45 */         .fieldOf("start_height").forGetter(()), Codec.BOOL
/*  46 */         .fieldOf("use_expansion_hack").forGetter(()), Heightmap.Types.CODEC
/*  47 */         .optionalFieldOf("project_start_to_heightmap").forGetter(()), MaxDistance.CODEC
/*  48 */         .fieldOf("max_distance_from_center").forGetter(()), 
/*  49 */         Codec.list(PoolAliasBinding.CODEC).optionalFieldOf("pool_aliases", List.of()).forGetter(()), DimensionPadding.CODEC
/*  50 */         .optionalFieldOf("dimension_padding", DEFAULT_DIMENSION_PADDING).forGetter(()), LiquidSettings.CODEC
/*  51 */         .optionalFieldOf("liquid_settings", DEFAULT_LIQUID_SETTINGS).forGetter(()))
/*  52 */       .apply(i, JigsawStructure::new)).validate(JigsawStructure::verifyRange); private final Holder<StructureTemplatePool> startPool; private final Optional<Identifier> startJigsawName; private final int maxDepth; private final HeightProvider startHeight; private final boolean useExpansionHack;
/*     */   
/*     */   private static DataResult<JigsawStructure> verifyRange(JigsawStructure structure) {
/*  55 */     switch (structure.terrainAdaptation()) { default: throw new MatchException(null, null);
/*     */       case NONE: 
/*  57 */       case BURY: case BEARD_THIN: case BEARD_BOX: case ENCAPSULATE: break; }  int edgeNeeded = 12;
/*     */     
/*  59 */     if (structure.maxDistanceFromCenter.horizontal() + edgeNeeded > 128) {
/*  60 */       return DataResult.error(() -> "Horizontal structure size including terrain adaptation must not exceed 128");
/*     */     }
/*     */     
/*  63 */     return DataResult.success(structure);
/*     */   }
/*     */ 
/*     */   
/*     */   private final Optional<Heightmap.Types> projectStartToHeightmap;
/*     */   
/*     */   private final MaxDistance maxDistanceFromCenter;
/*     */   
/*     */   private final List<PoolAliasBinding> poolAliases;
/*     */   
/*     */   private final DimensionPadding dimensionPadding;
/*     */   
/*     */   private final LiquidSettings liquidSettings;
/*     */   
/*     */   public JigsawStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<Identifier> startJigsawName, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, Optional<Heightmap.Types> projectStartToHeightmap, MaxDistance maxDistanceFromCenter, List<PoolAliasBinding> poolAliases, DimensionPadding dimensionPadding, LiquidSettings liquidSettings) {
/*  78 */     super(settings);
/*  79 */     this.startPool = startPool;
/*  80 */     this.startJigsawName = startJigsawName;
/*  81 */     this.maxDepth = maxDepth;
/*  82 */     this.startHeight = startHeight;
/*  83 */     this.useExpansionHack = useExpansionHack;
/*  84 */     this.projectStartToHeightmap = projectStartToHeightmap;
/*  85 */     this.maxDistanceFromCenter = maxDistanceFromCenter;
/*  86 */     this.poolAliases = poolAliases;
/*  87 */     this.dimensionPadding = dimensionPadding;
/*  88 */     this.liquidSettings = liquidSettings;
/*     */   }
/*     */ 
/*     */   
/*  92 */   public JigsawStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth, HeightProvider startHeight, boolean useExpansionHack, Heightmap.Types projectStartToHeightmap) { this(settings, startPool, Optional.empty(), maxDepth, startHeight, useExpansionHack, Optional.of(projectStartToHeightmap), new MaxDistance(80), List.of(), DEFAULT_DIMENSION_PADDING, DEFAULT_LIQUID_SETTINGS); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public JigsawStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, int maxDepth, HeightProvider startHeight, boolean useExpansionHack) { this(settings, startPool, Optional.empty(), maxDepth, startHeight, useExpansionHack, Optional.empty(), new MaxDistance(80), List.of(), DEFAULT_DIMENSION_PADDING, DEFAULT_LIQUID_SETTINGS); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
/* 101 */     ChunkPos chunkPos = context.chunkPos();
/* 102 */     int height = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
/* 103 */     BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), height, chunkPos.getMinBlockZ());
/*     */     
/* 105 */     return JigsawPlacement.addPieces(context, this.startPool, this.startJigsawName, this.maxDepth, startPos, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter, PoolAliasLookup.create(this.poolAliases, startPos, context.seed()), this.dimensionPadding, this.liquidSettings);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public StructureType<?> type() { return StructureType.JIGSAW; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 115 */   public Holder<StructureTemplatePool> getStartPool() { return this.startPool; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 120 */   public List<PoolAliasBinding> getPoolAliases() { return this.poolAliases; }
/*     */   public static final class MaxDistance extends Record { private final int horizontal; private final int vertical;
/*     */     
/* 123 */     public MaxDistance(int horizontal, int vertical) { this.horizontal = horizontal; this.vertical = vertical; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #123	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 123 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance; } public int horizontal() { return this.horizontal; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #123	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #123	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/JigsawStructure$MaxDistance;
/* 123 */       //   0	8	1	o	Ljava/lang/Object; } public int vertical() { return this.vertical; }
/*     */ 
/*     */ 
/*     */     
/* 127 */     private static final Codec<Integer> HORIZONTAL_VALUE_CODEC = Codec.intRange(1, 128);
/*     */     
/* 129 */     private static final Codec<MaxDistance> FULL_CODEC = RecordCodecBuilder.create(i -> i.group(HORIZONTAL_VALUE_CODEC
/* 130 */           .fieldOf("horizontal").forGetter(MaxDistance::horizontal), 
/* 131 */           ExtraCodecs.intRange(1, DimensionType.Y_SIZE).optionalFieldOf("vertical", Integer.valueOf(DimensionType.Y_SIZE)).forGetter(MaxDistance::vertical))
/* 132 */         .apply(i, MaxDistance::new));
/*     */     
/* 134 */     public static final Codec<MaxDistance> CODEC = Codec.either(FULL_CODEC, HORIZONTAL_VALUE_CODEC).xmap(either -> 
/* 135 */         (MaxDistance)either.map(Function.identity(), MaxDistance::new), distance -> 
/* 136 */         (distance.horizontal == distance.vertical) ? Either.right(Integer.valueOf(distance.horizontal)) : Either.left(distance));
/*     */ 
/*     */ 
/*     */     
/* 140 */     public MaxDistance(int value) { this(value, value); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\JigsawStructure.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */