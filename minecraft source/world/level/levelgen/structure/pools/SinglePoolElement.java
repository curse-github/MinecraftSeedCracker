/*     */ package net.minecraft.world.level.levelgen.structure.pools;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ public class SinglePoolElement extends StructurePoolElement {
/*  41 */   private static final Comparator<StructureTemplate.JigsawBlockInfo> HIGHEST_SELECTION_PRIORITY_FIRST = Comparator.comparingInt(StructureTemplate.JigsawBlockInfo::selectionPriority).reversed();
/*     */   
/*     */   private static <T> DataResult<T> encodeTemplate(Either<Identifier, StructureTemplate> template, DynamicOps<T> ops, T prefix) {
/*  44 */     Optional<Identifier> location = template.left();
/*  45 */     if (location.isEmpty()) {
/*  46 */       return DataResult.error(() -> "Can not serialize a runtime pool element");
/*     */     }
/*  48 */     return Identifier.CODEC.encode((Identifier)location.get(), ops, prefix);
/*     */   }
/*     */   
/*  51 */   private static final Codec<Either<Identifier, StructureTemplate>> TEMPLATE_CODEC = Codec.of(SinglePoolElement::encodeTemplate, Identifier.CODEC
/*     */       
/*  53 */       .map(Either::left));
/*     */ 
/*     */   
/*  56 */   public static final MapCodec<SinglePoolElement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/*  57 */         templateCodec(), 
/*  58 */         processorsCodec(), 
/*  59 */         projectionCodec(), 
/*  60 */         overrideLiquidSettingsCodec())
/*  61 */       .apply(i, SinglePoolElement::new)); protected final Either<Identifier, StructureTemplate> template; protected final Holder<StructureProcessorList> processors;
/*     */   protected final Optional<LiquidSettings> overrideLiquidSettings;
/*     */   
/*  64 */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Holder<StructureProcessorList>> processorsCodec() { return StructureProcessorType.LIST_CODEC.fieldOf("processors").forGetter(t -> t.processors); }
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Optional<LiquidSettings>> overrideLiquidSettingsCodec() { return LiquidSettings.CODEC.optionalFieldOf("override_liquid_settings").forGetter(t -> t.overrideLiquidSettings); }
/*     */ 
/*     */ 
/*     */   
/*  72 */   protected static <E extends SinglePoolElement> RecordCodecBuilder<E, Either<Identifier, StructureTemplate>> templateCodec() { return TEMPLATE_CODEC.fieldOf("location").forGetter(t -> t.template); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SinglePoolElement(Either<Identifier, StructureTemplate> template, Holder<StructureProcessorList> processors, StructureTemplatePool.Projection projection, Optional<LiquidSettings> overrideLiquidSettings) {
/*  80 */     super(projection);
/*  81 */     this.template = template;
/*  82 */     this.processors = processors;
/*  83 */     this.overrideLiquidSettings = overrideLiquidSettings;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3i getSize(StructureTemplateManager structureTemplateManager, Rotation rotation) {
/*  88 */     StructureTemplate template = getTemplate(structureTemplateManager);
/*  89 */     return template.getSize(rotation);
/*     */   }
/*     */ 
/*     */   
/*  93 */   private StructureTemplate getTemplate(StructureTemplateManager structureTemplateManager) { Objects.requireNonNull(structureTemplateManager); return (StructureTemplate)this.template.map(structureTemplateManager::getOrCreate, Function.identity()); }
/*     */ 
/*     */   
/*     */   public List<StructureTemplate.StructureBlockInfo> getDataMarkers(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, boolean absolute) {
/*  97 */     StructureTemplate template = getTemplate(structureTemplateManager);
/*  98 */     ObjectArrayList objectArrayList = template.filterBlocks(position, (new StructurePlaceSettings()).setRotation(rotation), Blocks.STRUCTURE_BLOCK, absolute);
/*  99 */     List<StructureTemplate.StructureBlockInfo> dataMarkers = Lists.newArrayList();
/* 100 */     for (StructureTemplate.StructureBlockInfo info : objectArrayList) {
/* 101 */       CompoundTag nbt = info.nbt();
/* 102 */       if (nbt == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 106 */       StructureMode mode = (StructureMode)nbt.read("mode", StructureMode.LEGACY_CODEC).orElseThrow();
/* 107 */       if (mode != StructureMode.DATA) {
/*     */         continue;
/*     */       }
/*     */       
/* 111 */       dataMarkers.add(info);
/*     */     } 
/*     */     
/* 114 */     return dataMarkers;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<StructureTemplate.JigsawBlockInfo> getShuffledJigsawBlocks(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, RandomSource random) {
/* 119 */     List<StructureTemplate.JigsawBlockInfo> jigsaws = getTemplate(structureTemplateManager).getJigsaws(position, rotation);
/* 120 */     Util.shuffle(jigsaws, random);
/* 121 */     sortBySelectionPriority(jigsaws);
/* 122 */     return jigsaws;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 127 */   static void sortBySelectionPriority(List<StructureTemplate.JigsawBlockInfo> blocks) { blocks.sort(HIGHEST_SELECTION_PRIORITY_FIRST); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation) {
/* 132 */     StructureTemplate template = getTemplate(structureTemplateManager);
/* 133 */     return template.getBoundingBox((new StructurePlaceSettings()).setRotation(rotation), position);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(StructureTemplateManager structureTemplateManager, WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, BlockPos position, BlockPos referencePos, Rotation rotation, BoundingBox chunkBB, RandomSource random, LiquidSettings liquidSettings, boolean keepJigsaws) {
/* 138 */     StructureTemplate template = getTemplate(structureTemplateManager);
/* 139 */     StructurePlaceSettings settings = getSettings(rotation, chunkBB, liquidSettings, keepJigsaws);
/*     */     
/* 141 */     if (template.placeInWorld(level, position, referencePos, settings, random, 18)) {
/* 142 */       List<StructureTemplate.StructureBlockInfo> dataMarkers = StructureTemplate.processBlockInfos(level, position, referencePos, settings, getDataMarkers(structureTemplateManager, position, rotation, false));
/* 143 */       for (StructureTemplate.StructureBlockInfo dataMarker : dataMarkers) {
/* 144 */         handleDataMarker(level, dataMarker, position, rotation, random, chunkBB);
/*     */       }
/*     */       
/* 147 */       return true;
/*     */     } 
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   protected StructurePlaceSettings getSettings(Rotation rotation, BoundingBox chunkBB, LiquidSettings liquidSettings, boolean keepJigsaws) {
/* 153 */     StructurePlaceSettings settings = new StructurePlaceSettings();
/* 154 */     settings.setBoundingBox(chunkBB);
/* 155 */     settings.setRotation(rotation);
/* 156 */     settings.setKnownShape(true);
/* 157 */     settings.setIgnoreEntities(false);
/* 158 */     settings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 159 */     settings.setFinalizeEntities(true);
/* 160 */     settings.setLiquidSettings((LiquidSettings)this.overrideLiquidSettings.orElse(liquidSettings));
/* 161 */     if (!keepJigsaws) {
/* 162 */       settings.addProcessor(JigsawReplacementProcessor.INSTANCE);
/*     */     }
/* 164 */     Objects.requireNonNull(settings); ((StructureProcessorList)this.processors.value()).list().forEach(settings::addProcessor);
/* 165 */     Objects.requireNonNull(settings); getProjection().getProcessors().forEach(settings::addProcessor);
/* 166 */     return settings;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 171 */   public StructurePoolElementType<?> getType() { return StructurePoolElementType.SINGLE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   public String toString() { return "Single[" + String.valueOf(this.template) + "]"; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 181 */   public Identifier getTemplateLocation() { return (Identifier)this.template.orThrow(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\SinglePoolElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */