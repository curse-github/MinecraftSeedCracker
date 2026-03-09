/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShipwreckPiece
/*     */   extends TemplateStructurePiece
/*     */ {
/*     */   private final boolean isBeached;
/*     */   
/*     */   public ShipwreckPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation, boolean isBeached) {
/*  90 */     super(StructurePieceType.SHIPWRECK_PIECE, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(rotation), position);
/*     */     
/*  92 */     this.isBeached = isBeached;
/*     */   }
/*     */   
/*     */   public ShipwreckPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/*  96 */     super(StructurePieceType.SHIPWRECK_PIECE, tag, structureTemplateManager, location -> makeSettings((Rotation)tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*     */     
/*  98 */     this.isBeached = tag.getBooleanOr("isBeached", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 103 */     super.addAdditionalSaveData(context, tag);
/* 104 */     tag.putBoolean("isBeached", this.isBeached);
/* 105 */     tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*     */   }
/*     */ 
/*     */   
/* 109 */   private static StructurePlaceSettings makeSettings(Rotation rotation) { return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).setRotationPivot(ShipwreckPieces.PIVOT).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/* 114 */     ResourceKey<LootTable> lootTable = (ResourceKey)ShipwreckPieces.MARKERS_TO_LOOT.get(markerId);
/* 115 */     if (lootTable != null) {
/* 116 */       RandomizableContainer.setBlockEntityLootTable(level, random, position.below(), lootTable);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 122 */     if (isTooBigToFitInWorldGenRegion()) {
/*     */       
/* 124 */       super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */       
/*     */       return;
/*     */     } 
/* 128 */     int minY = level.getMaxY() + 1;
/* 129 */     int mean = 0;
/* 130 */     Vec3i templateSize = this.template.getSize();
/* 131 */     Heightmap.Types heightmapType = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
/* 132 */     int baseSize = templateSize.getX() * templateSize.getZ();
/* 133 */     if (baseSize == 0) {
/* 134 */       mean = level.getHeight(heightmapType, this.templatePosition.getX(), this.templatePosition.getZ());
/*     */     } else {
/* 136 */       BlockPos corner = this.templatePosition.offset(templateSize.getX() - 1, 0, templateSize.getZ() - 1);
/* 137 */       for (BlockPos p : BlockPos.betweenClosed(this.templatePosition, corner)) {
/* 138 */         int heightmap = level.getHeight(heightmapType, p.getX(), p.getZ());
/* 139 */         mean += heightmap;
/* 140 */         minY = Math.min(minY, heightmap);
/*     */       } 
/* 142 */       mean /= baseSize;
/*     */     } 
/* 144 */     adjustPositionHeight(this.isBeached ? calculateBeachedPosition(minY, random) : mean);
/* 145 */     super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTooBigToFitInWorldGenRegion() {
/* 152 */     Vec3i size = this.template.getSize();
/* 153 */     return (size.getX() > 32 || size.getY() > 32);
/*     */   }
/*     */ 
/*     */   
/* 157 */   public int calculateBeachedPosition(int minY, RandomSource random) { return minY - this.template.getSize().getY() / 2 - random.nextInt(3); }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public void adjustPositionHeight(int newHeight) { this.templatePosition = new BlockPos(this.templatePosition.getX(), newHeight, this.templatePosition.getZ()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\ShipwreckPieces$ShipwreckPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */