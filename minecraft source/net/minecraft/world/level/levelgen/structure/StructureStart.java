/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StructureStart
/*     */ {
/*     */   public static final String INVALID_START_ID = "INVALID";
/*  26 */   public static final StructureStart INVALID_START = new StructureStart(null, new ChunkPos(0, 0), 0, new PiecesContainer(List.of()));
/*     */   
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Structure structure;
/*     */   
/*     */   private final PiecesContainer pieceContainer;
/*     */   
/*     */   private final ChunkPos chunkPos;
/*     */   
/*     */   private int references;
/*     */   
/*     */   public StructureStart(Structure structure, ChunkPos chunkPos, int references, PiecesContainer pieceContainer) {
/*  39 */     this.structure = structure;
/*  40 */     this.chunkPos = chunkPos;
/*  41 */     this.references = references;
/*  42 */     this.pieceContainer = pieceContainer;
/*     */   }
/*     */   
/*     */   public static StructureStart loadStaticStart(StructurePieceSerializationContext context, CompoundTag tag, long seed) {
/*  46 */     String id = tag.getStringOr("id", "");
/*  47 */     if ("INVALID".equals(id)) {
/*  48 */       return INVALID_START;
/*     */     }
/*     */ 
/*     */     
/*  52 */     Registry<Structure> structuresRegistry = context.registryAccess().lookupOrThrow(Registries.STRUCTURE);
/*  53 */     Structure stucture = (Structure)structuresRegistry.getValue(Identifier.parse(id));
/*  54 */     if (stucture == null) {
/*  55 */       LOGGER.error("Unknown stucture id: {}", id);
/*  56 */       return null;
/*     */     } 
/*     */     
/*  59 */     ChunkPos chunkPos = new ChunkPos(tag.getIntOr("ChunkX", 0), tag.getIntOr("ChunkZ", 0));
/*  60 */     int references = tag.getIntOr("references", 0);
/*  61 */     ListTag children = tag.getListOrEmpty("Children");
/*     */     
/*     */     try {
/*  64 */       PiecesContainer pieces = PiecesContainer.load(children, context);
/*  65 */       if (stucture instanceof OceanMonumentStructure)
/*     */       {
/*  67 */         pieces = OceanMonumentStructure.regeneratePiecesAfterLoad(chunkPos, seed, pieces);
/*     */       }
/*  69 */       return new StructureStart(stucture, chunkPos, references, pieces);
/*  70 */     } catch (Exception e) {
/*  71 */       LOGGER.error("Failed Start with id {}", id, e);
/*  72 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/*  77 */     BoundingBox boundingBox = this.cachedBoundingBox;
/*  78 */     if (boundingBox == null) {
/*  79 */       boundingBox = this.structure.adjustBoundingBox(this.pieceContainer.calculateBoundingBox());
/*  80 */       this.cachedBoundingBox = boundingBox;
/*     */     } 
/*  82 */     return boundingBox;
/*     */   }
/*     */   
/*     */   public void placeInChunk(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos) {
/*  86 */     List<StructurePiece> pieces = this.pieceContainer.pieces();
/*  87 */     if (pieces.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  92 */     BoundingBox centerBB = ((StructurePiece)pieces.get(0)).boundingBox;
/*  93 */     BlockPos centerPos = centerBB.getCenter();
/*  94 */     BlockPos referencePos = new BlockPos(centerPos.getX(), centerBB.minY(), centerPos.getZ());
/*  95 */     for (StructurePiece next : pieces) {
/*  96 */       if (next.getBoundingBox().intersects(chunkBB)) {
/*  97 */         next.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */       }
/*     */     } 
/*     */     
/* 101 */     this.structure.afterPlace(level, structureManager, generator, random, chunkBB, chunkPos, this.pieceContainer);
/*     */   }
/*     */   
/*     */   public CompoundTag createTag(StructurePieceSerializationContext context, ChunkPos chunkPos) {
/* 105 */     CompoundTag tag = new CompoundTag();
/*     */     
/* 107 */     if (isValid()) {
/* 108 */       tag.putString("id", context.registryAccess().lookupOrThrow(Registries.STRUCTURE).getKey(this.structure).toString());
/*     */     } else {
/* 110 */       tag.putString("id", "INVALID");
/* 111 */       return tag;
/*     */     } 
/* 113 */     tag.putInt("ChunkX", chunkPos.x);
/* 114 */     tag.putInt("ChunkZ", chunkPos.z);
/* 115 */     tag.putInt("references", this.references);
/* 116 */     tag.put("Children", this.pieceContainer.save(context));
/*     */     
/* 118 */     return tag;
/*     */   }
/*     */ 
/*     */   
/* 122 */   public boolean isValid() { return !this.pieceContainer.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public ChunkPos getChunkPos() { return this.chunkPos; }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public boolean canBeReferenced() { return (this.references < getMaxReferences()); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public void addReference() { this.references++; }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public int getReferences() { return this.references; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   protected int getMaxReferences() { return 1; }
/*     */ 
/*     */ 
/*     */   
/* 147 */   public Structure getStructure() { return this.structure; }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public List<StructurePiece> getPieces() { return this.pieceContainer.pieces(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\StructureStart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */