/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.monster.Witch;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class SwampHutPiece
/*     */   extends ScatteredFeaturePiece {
/*     */   private boolean spawnedWitch;
/*     */   private boolean spawnedCat;
/*     */   
/*  30 */   public SwampHutPiece(RandomSource random, int west, int north) { super(StructurePieceType.SWAMPLAND_HUT, west, 64, north, 7, 7, 9, getRandomHorizontalDirection(random)); }
/*     */ 
/*     */   
/*     */   public SwampHutPiece(CompoundTag tag) {
/*  34 */     super(StructurePieceType.SWAMPLAND_HUT, tag);
/*  35 */     this.spawnedWitch = tag.getBooleanOr("Witch", false);
/*  36 */     this.spawnedCat = tag.getBooleanOr("Cat", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  41 */     super.addAdditionalSaveData(context, tag);
/*  42 */     tag.putBoolean("Witch", this.spawnedWitch);
/*  43 */     tag.putBoolean("Cat", this.spawnedCat);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  48 */     if (!updateAverageGroundHeight(level, chunkBB, 0)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  53 */     generateBox(level, chunkBB, 1, 1, 1, 5, 1, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  54 */     generateBox(level, chunkBB, 1, 4, 2, 5, 4, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  55 */     generateBox(level, chunkBB, 2, 1, 0, 4, 1, 0, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*     */ 
/*     */     
/*  58 */     generateBox(level, chunkBB, 2, 2, 2, 3, 3, 2, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  59 */     generateBox(level, chunkBB, 1, 2, 3, 1, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  60 */     generateBox(level, chunkBB, 5, 2, 3, 5, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*  61 */     generateBox(level, chunkBB, 2, 2, 7, 4, 3, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
/*     */ 
/*     */     
/*  64 */     generateBox(level, chunkBB, 1, 0, 2, 1, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  65 */     generateBox(level, chunkBB, 5, 0, 2, 5, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  66 */     generateBox(level, chunkBB, 1, 0, 7, 1, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*  67 */     generateBox(level, chunkBB, 5, 0, 7, 5, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
/*     */ 
/*     */     
/*  70 */     placeBlock(level, Blocks.OAK_FENCE.defaultBlockState(), 2, 3, 2, chunkBB);
/*  71 */     placeBlock(level, Blocks.OAK_FENCE.defaultBlockState(), 3, 3, 7, chunkBB);
/*  72 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 1, 3, 4, chunkBB);
/*  73 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 5, 3, 4, chunkBB);
/*  74 */     placeBlock(level, Blocks.AIR.defaultBlockState(), 5, 3, 5, chunkBB);
/*  75 */     placeBlock(level, Blocks.POTTED_RED_MUSHROOM.defaultBlockState(), 1, 3, 5, chunkBB);
/*     */ 
/*     */     
/*  78 */     placeBlock(level, Blocks.CRAFTING_TABLE.defaultBlockState(), 3, 2, 6, chunkBB);
/*  79 */     placeBlock(level, Blocks.CAULDRON.defaultBlockState(), 4, 2, 6, chunkBB);
/*     */ 
/*     */     
/*  82 */     placeBlock(level, Blocks.OAK_FENCE.defaultBlockState(), 1, 2, 1, chunkBB);
/*  83 */     placeBlock(level, Blocks.OAK_FENCE.defaultBlockState(), 5, 2, 1, chunkBB);
/*     */ 
/*     */     
/*  86 */     BlockState northStairs = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
/*  87 */     BlockState eastStairs = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST);
/*  88 */     BlockState westStairs = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST);
/*  89 */     BlockState southStairs = (BlockState)Blocks.SPRUCE_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);
/*     */     
/*  91 */     generateBox(level, chunkBB, 0, 4, 1, 6, 4, 1, northStairs, northStairs, false);
/*  92 */     generateBox(level, chunkBB, 0, 4, 2, 0, 4, 7, eastStairs, eastStairs, false);
/*  93 */     generateBox(level, chunkBB, 6, 4, 2, 6, 4, 7, westStairs, westStairs, false);
/*  94 */     generateBox(level, chunkBB, 0, 4, 8, 6, 4, 8, southStairs, southStairs, false);
/*  95 */     placeBlock(level, (BlockState)northStairs.setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 0, 4, 1, chunkBB);
/*  96 */     placeBlock(level, (BlockState)northStairs.setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 6, 4, 1, chunkBB);
/*  97 */     placeBlock(level, (BlockState)southStairs.setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 0, 4, 8, chunkBB);
/*  98 */     placeBlock(level, (BlockState)southStairs.setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 6, 4, 8, chunkBB);
/*     */ 
/*     */     
/* 101 */     for (int z = 2; z <= 7; z += 5) {
/* 102 */       for (int x = 1; x <= 5; x += 4) {
/* 103 */         fillColumnDown(level, Blocks.OAK_LOG.defaultBlockState(), x, -1, z, chunkBB);
/*     */       }
/*     */     } 
/*     */     
/* 107 */     if (!this.spawnedWitch) {
/* 108 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(2, 2, 5);
/* 109 */       if (chunkBB.isInside(mutableBlockPos)) {
/* 110 */         this.spawnedWitch = true;
/*     */         
/* 112 */         Witch witch = (Witch)EntityType.WITCH.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
/* 113 */         if (witch != null) {
/* 114 */           witch.setPersistenceRequired();
/* 115 */           witch.snapTo(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 116 */           witch.finalizeSpawn(level, level.getCurrentDifficultyAt(mutableBlockPos), EntitySpawnReason.STRUCTURE, null);
/* 117 */           level.addFreshEntityWithPassengers(witch);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     spawnCat(level, chunkBB);
/*     */   }
/*     */   
/*     */   private void spawnCat(ServerLevelAccessor level, BoundingBox chunkBB) {
/* 126 */     if (!this.spawnedCat) {
/* 127 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(2, 2, 5);
/* 128 */       if (chunkBB.isInside(mutableBlockPos)) {
/* 129 */         this.spawnedCat = true;
/*     */         
/* 131 */         Cat cat = (Cat)EntityType.CAT.create(level.getLevel(), EntitySpawnReason.STRUCTURE);
/* 132 */         if (cat != null) {
/* 133 */           cat.setPersistenceRequired();
/* 134 */           cat.snapTo(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY(), mutableBlockPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 135 */           cat.finalizeSpawn(level, level.getCurrentDifficultyAt(mutableBlockPos), EntitySpawnReason.STRUCTURE, null);
/* 136 */           level.addFreshEntityWithPassengers(cat);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\SwampHutPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */