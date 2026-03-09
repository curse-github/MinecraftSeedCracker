/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.chunk.StructureAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheck;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ 
/*     */ public class StructureManager {
/*     */   private final LevelAccessor level;
/*     */   private final WorldOptions worldOptions;
/*     */   private final StructureCheck structureCheck;
/*     */   
/*     */   public StructureManager(LevelAccessor level, WorldOptions worldOptions, StructureCheck structureCheck) {
/*  36 */     this.level = level;
/*  37 */     this.worldOptions = worldOptions;
/*  38 */     this.structureCheck = structureCheck;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureManager forWorldGenRegion(WorldGenRegion region) {
/*  43 */     if (region.getLevel() != this.level) {
/*  44 */       throw new IllegalStateException("Using invalid structure manager (source level: " + String.valueOf(region.getLevel()) + ", region: " + String.valueOf(region));
/*     */     }
/*  46 */     return new StructureManager(region, this.worldOptions, this.structureCheck);
/*     */   }
/*     */   
/*     */   public List<StructureStart> startsForStructure(ChunkPos pos, Predicate<Structure> matcher) {
/*  50 */     Map<Structure, LongSet> allReferences = this.level.getChunk(pos.x, pos.z, ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
/*  51 */     ImmutableList.Builder<StructureStart> result = ImmutableList.builder();
/*     */     
/*  53 */     for (Map.Entry<Structure, LongSet> entry : allReferences.entrySet()) {
/*  54 */       Structure structure = (Structure)entry.getKey();
/*  55 */       if (matcher.test(structure)) {
/*  56 */         Objects.requireNonNull(result); fillStartsForStructure(structure, (LongSet)entry.getValue(), result::add);
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     return result.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<StructureStart> startsForStructure(SectionPos pos, Structure structure) {
/*  68 */     LongSet referencesForStructure = this.level.getChunk(pos.x(), pos.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(structure);
/*  69 */     ImmutableList.Builder<StructureStart> result = ImmutableList.builder();
/*  70 */     Objects.requireNonNull(result); fillStartsForStructure(structure, referencesForStructure, result::add);
/*  71 */     return result.build();
/*     */   }
/*     */   
/*     */   public void fillStartsForStructure(Structure structure, LongSet referencesForStructure, Consumer<StructureStart> consumer) {
/*  75 */     for (LongIterator longIterator = referencesForStructure.iterator(); longIterator.hasNext(); ) { long key = ((Long)longIterator.next()).longValue();
/*  76 */       SectionPos sectionPos = SectionPos.of(new ChunkPos(key), this.level.getMinSectionY());
/*  77 */       StructureStart start = getStartForStructure(sectionPos, structure, this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_STARTS));
/*  78 */       if (start != null && start.isValid()) {
/*  79 */         consumer.accept(start);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*  85 */   public StructureStart getStartForStructure(SectionPos pos, Structure structure, StructureAccess chunk) { return chunk.getStartForStructure(structure); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public void setStartForStructure(SectionPos pos, Structure structure, StructureStart start, StructureAccess chunk) { chunk.setStartForStructure(structure, start); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public void addReferenceForStructure(SectionPos pos, Structure structure, long reference, StructureAccess chunk) { chunk.addReferenceForStructure(structure, reference); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public boolean shouldGenerateStructures() { return this.worldOptions.generateStructures(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureStart getStructureAt(BlockPos blockPos, Structure structure) {
/* 104 */     for (StructureStart structureStart : startsForStructure(SectionPos.of(blockPos), structure)) {
/* 105 */       if (structureStart.getBoundingBox().isInside(blockPos)) {
/* 106 */         return structureStart;
/*     */       }
/*     */     } 
/* 109 */     return StructureStart.INVALID_START;
/*     */   }
/*     */ 
/*     */   
/* 113 */   public StructureStart getStructureWithPieceAt(BlockPos blockPos, TagKey<Structure> structureTag) { return getStructureWithPieceAt(blockPos, structure -> structure.is(structureTag)); }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public StructureStart getStructureWithPieceAt(BlockPos blockPos, HolderSet<Structure> structures) { Objects.requireNonNull(structures); return getStructureWithPieceAt(blockPos, structures::contains); }
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos blockPos, Predicate<Holder<Structure>> predicate) {
/* 122 */     Registry<Structure> structures = registryAccess().lookupOrThrow(Registries.STRUCTURE);
/* 123 */     for (StructureStart structureStart : startsForStructure(new ChunkPos(blockPos), s -> { Objects.requireNonNull(predicate); return ((Boolean)structures.get(structures.getId(s)).map(predicate::test).orElse(Boolean.valueOf(false))).booleanValue();
/* 124 */         })) { if (structureHasPieceAt(blockPos, structureStart)) {
/* 125 */         return structureStart;
/*     */       } }
/*     */     
/* 128 */     return StructureStart.INVALID_START;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StructureStart getStructureWithPieceAt(BlockPos blockPos, Structure structure) {
/* 135 */     for (StructureStart structureStart : startsForStructure(SectionPos.of(blockPos), structure)) {
/* 136 */       if (structureHasPieceAt(blockPos, structureStart)) {
/* 137 */         return structureStart;
/*     */       }
/*     */     } 
/* 140 */     return StructureStart.INVALID_START;
/*     */   }
/*     */   
/*     */   public boolean structureHasPieceAt(BlockPos blockPos, StructureStart structureStart) {
/* 144 */     for (StructurePiece piece : structureStart.getPieces()) {
/* 145 */       if (piece.getBoundingBox().isInside(blockPos)) {
/* 146 */         return true;
/*     */       }
/*     */     } 
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasAnyStructureAt(BlockPos pos) {
/* 153 */     SectionPos sectionPos = SectionPos.of(pos);
/* 154 */     return this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).hasAnyStructureReferences();
/*     */   }
/*     */   
/*     */   public Map<Structure, LongSet> getAllStructuresAt(BlockPos pos) {
/* 158 */     SectionPos sectionPos = SectionPos.of(pos);
/* 159 */     return this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
/*     */   }
/*     */ 
/*     */   
/* 163 */   public StructureCheckResult checkStructurePresence(ChunkPos pos, Structure structure, StructurePlacement placement, boolean createReference) { return this.structureCheck.checkStart(pos, structure, placement, createReference); }
/*     */ 
/*     */   
/*     */   public void addReference(StructureStart start) {
/* 167 */     start.addReference();
/* 168 */     this.structureCheck.incrementReference(start.getChunkPos(), start.getStructure());
/*     */   }
/*     */ 
/*     */   
/* 172 */   public RegistryAccess registryAccess() { return this.level.registryAccess(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\StructureManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */