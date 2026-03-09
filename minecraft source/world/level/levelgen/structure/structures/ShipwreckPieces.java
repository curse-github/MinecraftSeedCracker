/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
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
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class ShipwreckPieces
/*     */ {
/*     */   private static final int NUMBER_OF_BLOCKS_ALLOWED_IN_WORLD_GEN_REGION = 32;
/*  34 */   private static final BlockPos PIVOT = new BlockPos(4, 0, 15);
/*     */   
/*     */   private static final Identifier[] STRUCTURE_LOCATION_BEACHED = { 
/*  37 */       Identifier.withDefaultNamespace("shipwreck/with_mast"), 
/*  38 */       Identifier.withDefaultNamespace("shipwreck/sideways_full"), 
/*  39 */       Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf"), 
/*  40 */       Identifier.withDefaultNamespace("shipwreck/sideways_backhalf"), 
/*  41 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_full"), 
/*  42 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf"), 
/*  43 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf"), 
/*  44 */       Identifier.withDefaultNamespace("shipwreck/with_mast_degraded"), 
/*  45 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_full_degraded"), 
/*  46 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf_degraded"), 
/*  47 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf_degraded") };
/*     */ 
/*     */   
/*     */   private static final Identifier[] STRUCTURE_LOCATION_OCEAN = { 
/*  51 */       Identifier.withDefaultNamespace("shipwreck/with_mast"), 
/*  52 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_full"), 
/*  53 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_fronthalf"), 
/*  54 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_backhalf"), 
/*  55 */       Identifier.withDefaultNamespace("shipwreck/sideways_full"), 
/*  56 */       Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf"), 
/*  57 */       Identifier.withDefaultNamespace("shipwreck/sideways_backhalf"), 
/*  58 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_full"), 
/*  59 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf"), 
/*  60 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf"), 
/*  61 */       Identifier.withDefaultNamespace("shipwreck/with_mast_degraded"), 
/*  62 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_full_degraded"), 
/*  63 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_fronthalf_degraded"), 
/*  64 */       Identifier.withDefaultNamespace("shipwreck/upsidedown_backhalf_degraded"), 
/*  65 */       Identifier.withDefaultNamespace("shipwreck/sideways_full_degraded"), 
/*  66 */       Identifier.withDefaultNamespace("shipwreck/sideways_fronthalf_degraded"), 
/*  67 */       Identifier.withDefaultNamespace("shipwreck/sideways_backhalf_degraded"), 
/*  68 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_full_degraded"), 
/*  69 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_fronthalf_degraded"), 
/*  70 */       Identifier.withDefaultNamespace("shipwreck/rightsideup_backhalf_degraded") };
/*     */ 
/*     */   
/*  73 */   private static final Map<String, ResourceKey<LootTable>> MARKERS_TO_LOOT = Map.of("map_chest", BuiltInLootTables.SHIPWRECK_MAP, "treasure_chest", BuiltInLootTables.SHIPWRECK_TREASURE, "supply_chest", BuiltInLootTables.SHIPWRECK_SUPPLY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShipwreckPiece addRandomPiece(StructureTemplateManager structureTemplateManager, BlockPos position, Rotation rotation, StructurePieceAccessor structurePieceAccessor, RandomSource random, boolean isBeached) {
/*  80 */     Identifier identifier = (Identifier)Util.getRandom(isBeached ? STRUCTURE_LOCATION_BEACHED : STRUCTURE_LOCATION_OCEAN, random);
/*  81 */     ShipwreckPiece piece = new ShipwreckPiece(structureTemplateManager, identifier, position, rotation, isBeached);
/*  82 */     structurePieceAccessor.addPiece(piece);
/*  83 */     return piece;
/*     */   }
/*     */   
/*     */   public static class ShipwreckPiece extends TemplateStructurePiece {
/*     */     private final boolean isBeached;
/*     */     
/*     */     public ShipwreckPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation, boolean isBeached) {
/*  90 */       super(StructurePieceType.SHIPWRECK_PIECE, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(rotation), position);
/*     */       
/*  92 */       this.isBeached = isBeached;
/*     */     }
/*     */     
/*     */     public ShipwreckPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/*  96 */       super(StructurePieceType.SHIPWRECK_PIECE, tag, structureTemplateManager, location -> makeSettings((Rotation)tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*     */       
/*  98 */       this.isBeached = tag.getBooleanOr("isBeached", false);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 103 */       super.addAdditionalSaveData(context, tag);
/* 104 */       tag.putBoolean("isBeached", this.isBeached);
/* 105 */       tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*     */     }
/*     */ 
/*     */     
/* 109 */     private static StructurePlaceSettings makeSettings(Rotation rotation) { return (new StructurePlaceSettings()).setRotation(rotation).setMirror(Mirror.NONE).setRotationPivot(ShipwreckPieces.PIVOT).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/* 114 */       ResourceKey<LootTable> lootTable = (ResourceKey)ShipwreckPieces.MARKERS_TO_LOOT.get(markerId);
/* 115 */       if (lootTable != null) {
/* 116 */         RandomizableContainer.setBlockEntityLootTable(level, random, position.below(), lootTable);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 122 */       if (isTooBigToFitInWorldGenRegion()) {
/*     */         
/* 124 */         super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */         
/*     */         return;
/*     */       } 
/* 128 */       int minY = level.getMaxY() + 1;
/* 129 */       int mean = 0;
/* 130 */       Vec3i templateSize = this.template.getSize();
/* 131 */       Heightmap.Types heightmapType = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
/* 132 */       int baseSize = templateSize.getX() * templateSize.getZ();
/* 133 */       if (baseSize == 0) {
/* 134 */         mean = level.getHeight(heightmapType, this.templatePosition.getX(), this.templatePosition.getZ());
/*     */       } else {
/* 136 */         BlockPos corner = this.templatePosition.offset(templateSize.getX() - 1, 0, templateSize.getZ() - 1);
/* 137 */         for (BlockPos p : BlockPos.betweenClosed(this.templatePosition, corner)) {
/* 138 */           int heightmap = level.getHeight(heightmapType, p.getX(), p.getZ());
/* 139 */           mean += heightmap;
/* 140 */           minY = Math.min(minY, heightmap);
/*     */         } 
/* 142 */         mean /= baseSize;
/*     */       } 
/* 144 */       adjustPositionHeight(this.isBeached ? calculateBeachedPosition(minY, random) : mean);
/* 145 */       super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isTooBigToFitInWorldGenRegion() {
/* 152 */       Vec3i size = this.template.getSize();
/* 153 */       return (size.getX() > 32 || size.getY() > 32);
/*     */     }
/*     */ 
/*     */     
/* 157 */     public int calculateBeachedPosition(int minY, RandomSource random) { return minY - this.template.getSize().getY() / 2 - random.nextInt(3); }
/*     */ 
/*     */ 
/*     */     
/* 161 */     public void adjustPositionHeight(int newHeight) { this.templatePosition = new BlockPos(this.templatePosition.getX(), newHeight, this.templatePosition.getZ()); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\ShipwreckPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */