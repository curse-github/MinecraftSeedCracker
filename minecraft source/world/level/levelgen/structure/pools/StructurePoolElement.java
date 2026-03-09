/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ 
/*     */ public abstract class StructurePoolElement
/*     */ {
/*  31 */   public static final Codec<StructurePoolElement> CODEC = BuiltInRegistries.STRUCTURE_POOL_ELEMENT.byNameCodec().dispatch("element_type", StructurePoolElement::getType, StructurePoolElementType::codec);
/*     */   
/*  33 */   private static final Holder<StructureProcessorList> EMPTY = Holder.direct(new StructureProcessorList(List.of()));
/*     */ 
/*     */   
/*  36 */   protected static <E extends StructurePoolElement> RecordCodecBuilder<E, StructureTemplatePool.Projection> projectionCodec() { return StructureTemplatePool.Projection.CODEC.fieldOf("projection").forGetter(StructurePoolElement::getProjection); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   protected StructurePoolElement(StructureTemplatePool.Projection projection) { this.projection = projection; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleDataMarker(LevelAccessor level, StructureTemplate.StructureBlockInfo dataMarker, BlockPos position, Rotation rotation, RandomSource random, BoundingBox chunkBB) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructurePoolElement setProjection(StructureTemplatePool.Projection projection) {
/*  59 */     this.projection = projection;
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public StructureTemplatePool.Projection getProjection() {
/*  64 */     StructureTemplatePool.Projection projection = this.projection;
/*  65 */     if (projection == null) {
/*  66 */       throw new IllegalStateException();
/*     */     }
/*  68 */     return projection;
/*     */   }
/*     */ 
/*     */   
/*  72 */   public int getGroundLevelDelta() { return 1; }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public static Function<StructureTemplatePool.Projection, EmptyPoolElement> empty() { return p -> EmptyPoolElement.INSTANCE; }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(String location) { return p -> new LegacySinglePoolElement(Either.left(Identifier.parse(location)), EMPTY, p, Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacy(String location, Holder<StructureProcessorList> processors) { return p -> new LegacySinglePoolElement(Either.left(Identifier.parse(location)), processors, p, Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String location) { return p -> new SinglePoolElement(Either.left(Identifier.parse(location)), EMPTY, p, Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String location, Holder<StructureProcessorList> processors) { return p -> new SinglePoolElement(Either.left(Identifier.parse(location)), processors, p, Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String location, LiquidSettings overrideLiquidSettings) { return p -> new SinglePoolElement(Either.left(Identifier.parse(location)), EMPTY, p, Optional.of(overrideLiquidSettings)); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public static Function<StructureTemplatePool.Projection, SinglePoolElement> single(String location, Holder<StructureProcessorList> processors, LiquidSettings overrideLiquidSettings) { return p -> new SinglePoolElement(Either.left(Identifier.parse(location)), processors, p, Optional.of(overrideLiquidSettings)); }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static Function<StructureTemplatePool.Projection, FeaturePoolElement> feature(Holder<PlacedFeature> feature) { return p -> new FeaturePoolElement(feature, p); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public static Function<StructureTemplatePool.Projection, ListPoolElement> list(List<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>> elements) { return p -> new ListPoolElement((List)elements.stream().map(()).collect(Collectors.toList()), p); }
/*     */   
/*     */   public abstract Vec3i getSize(StructureTemplateManager paramStructureTemplateManager, Rotation paramRotation);
/*     */   
/*     */   public abstract List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation, RandomSource paramRandomSource);
/*     */   
/*     */   public abstract BoundingBox getBoundingBox(StructureTemplateManager paramStructureTemplateManager, BlockPos paramBlockPos, Rotation paramRotation);
/*     */   
/*     */   public abstract boolean place(StructureTemplateManager paramStructureTemplateManager, WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, BlockPos paramBlockPos1, BlockPos paramBlockPos2, Rotation paramRotation, BoundingBox paramBoundingBox, RandomSource paramRandomSource, LiquidSettings paramLiquidSettings, boolean paramBoolean);
/*     */   
/*     */   public abstract StructurePoolElementType<?> getType();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\StructurePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */