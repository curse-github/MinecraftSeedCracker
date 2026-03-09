/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.zombie.Drowned;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
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
/*     */ public class OceanRuinPiece
/*     */   extends TemplateStructurePiece
/*     */ {
/*     */   private final OceanRuinStructure.Type biomeType;
/*     */   private final float integrity;
/*     */   private final boolean isLarge;
/*     */   
/*     */   public OceanRuinPiece(StructureTemplateManager structureTemplateManager, Identifier templateLocation, BlockPos position, Rotation rotation, float integrity, OceanRuinStructure.Type biomeType, boolean isLarge) {
/* 233 */     super(StructurePieceType.OCEAN_RUIN, 0, structureTemplateManager, templateLocation, templateLocation.toString(), makeSettings(rotation, integrity, biomeType), position);
/*     */     
/* 235 */     this.integrity = integrity;
/* 236 */     this.biomeType = biomeType;
/* 237 */     this.isLarge = isLarge;
/*     */   }
/*     */   
/*     */   private OceanRuinPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag, Rotation rotation, float integrity, OceanRuinStructure.Type biomeType, boolean isLarge) {
/* 241 */     super(StructurePieceType.OCEAN_RUIN, tag, structureTemplateManager, location -> makeSettings(rotation, integrity, biomeType));
/*     */     
/* 243 */     this.integrity = integrity;
/* 244 */     this.biomeType = biomeType;
/* 245 */     this.isLarge = isLarge;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static StructurePlaceSettings makeSettings(Rotation rotation, float integrity, OceanRuinStructure.Type biomeType) {
/* 251 */     StructureProcessor suspiciousBlockProcessor = (biomeType == OceanRuinStructure.Type.COLD) ? OceanRuinPieces.COLD_SUSPICIOUS_BLOCK_PROCESSOR : OceanRuinPieces.WARM_SUSPICIOUS_BLOCK_PROCESSOR;
/*     */     
/* 253 */     return (new StructurePlaceSettings())
/* 254 */       .setRotation(rotation)
/* 255 */       .setMirror(Mirror.NONE)
/* 256 */       .addProcessor(new BlockRotProcessor(integrity))
/* 257 */       .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR)
/* 258 */       .addProcessor(suspiciousBlockProcessor);
/*     */   }
/*     */   
/*     */   public static OceanRuinPiece create(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/* 262 */     Rotation rotation = (Rotation)tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow();
/* 263 */     float integrity = tag.getFloatOr("Integrity", 0.0F);
/* 264 */     OceanRuinStructure.Type biomeType = (OceanRuinStructure.Type)tag.read("BiomeType", OceanRuinStructure.Type.LEGACY_CODEC).orElseThrow();
/* 265 */     boolean isLarge = tag.getBooleanOr("IsLarge", false);
/* 266 */     return new OceanRuinPiece(structureTemplateManager, tag, rotation, integrity, biomeType, isLarge);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 271 */     super.addAdditionalSaveData(context, tag);
/* 272 */     tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/* 273 */     tag.putFloat("Integrity", this.integrity);
/* 274 */     tag.store("BiomeType", OceanRuinStructure.Type.LEGACY_CODEC, this.biomeType);
/* 275 */     tag.putBoolean("IsLarge", this.isLarge);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/* 280 */     if ("chest".equals(markerId)) {
/* 281 */       level.setBlock(position, (BlockState)Blocks.CHEST.defaultBlockState().setValue(ChestBlock.WATERLOGGED, Boolean.valueOf(level.getFluidState(position).is(FluidTags.WATER))), 2);
/*     */       
/* 283 */       BlockEntity chest = level.getBlockEntity(position);
/* 284 */       if (chest instanceof ChestBlockEntity) {
/* 285 */         ((ChestBlockEntity)chest).setLootTable(this.isLarge ? BuiltInLootTables.UNDERWATER_RUIN_BIG : BuiltInLootTables.UNDERWATER_RUIN_SMALL, random.nextLong());
/*     */       }
/*     */     }
/* 288 */     else if ("drowned".equals(markerId)) {
/* 289 */       Drowned drowned = (Drowned)EntityType.DROWNED.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
/* 290 */       if (drowned != null) {
/* 291 */         drowned.setPersistenceRequired();
/* 292 */         drowned.snapTo(position, 0.0F, 0.0F);
/* 293 */         drowned.finalizeSpawn(level, level.getCurrentDifficultyAt(position), EntitySpawnReason.STRUCTURE, null);
/* 294 */         level.addFreshEntityWithPassengers(drowned);
/* 295 */         if (position.getY() > level.getSeaLevel()) {
/* 296 */           level.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
/*     */         } else {
/* 298 */           level.setBlock(position, Blocks.WATER.defaultBlockState(), 2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 306 */     int height = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.templatePosition.getX(), this.templatePosition.getZ());
/* 307 */     this.templatePosition = new BlockPos(this.templatePosition.getX(), height, this.templatePosition.getZ());
/* 308 */     BlockPos corner = StructureTemplate.transform(new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1), Mirror.NONE, this.placeSettings.getRotation(), BlockPos.ZERO).offset(this.templatePosition);
/* 309 */     this.templatePosition = new BlockPos(this.templatePosition.getX(), getHeight(this.templatePosition, level, corner), this.templatePosition.getZ());
/*     */     
/* 311 */     super.postProcess(level, structureManager, generator, random, chunkBB, chunkPos, referencePos);
/*     */   }
/*     */   
/*     */   private int getHeight(BlockPos pos, BlockGetter level, BlockPos corner) {
/* 315 */     int newY = pos.getY();
/* 316 */     int minY = 512;
/* 317 */     int topY = newY - 1;
/* 318 */     int area = 0;
/* 319 */     for (BlockPos p : BlockPos.betweenClosed(pos, corner)) {
/* 320 */       int x = p.getX();
/* 321 */       int z = p.getZ();
/* 322 */       int floorY = pos.getY() - 1;
/* 323 */       BlockPos.MutableBlockPos tempPos = new BlockPos.MutableBlockPos(x, floorY, z);
/* 324 */       BlockState tempState = level.getBlockState(tempPos);
/* 325 */       FluidState tempFluid = level.getFluidState(tempPos);
/* 326 */       while ((tempState.isAir() || tempFluid.is(FluidTags.WATER) || tempState.is(BlockTags.ICE)) && floorY > level.getMinY() + 1) {
/* 327 */         floorY--;
/* 328 */         tempPos.set(x, floorY, z);
/* 329 */         tempState = level.getBlockState(tempPos);
/* 330 */         tempFluid = level.getFluidState(tempPos);
/*     */       } 
/*     */       
/* 333 */       minY = Math.min(minY, floorY);
/* 334 */       if (floorY < topY - 2) {
/* 335 */         area++;
/*     */       }
/*     */     } 
/*     */     
/* 339 */     int width = Math.abs(pos.getX() - corner.getX());
/* 340 */     if (topY - minY > 2 && area > width - 2) {
/* 341 */       newY = minY + 1;
/*     */     }
/*     */     
/* 344 */     return newY;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\OceanRuinPieces$OceanRuinPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */