/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructureUtils
/*     */ {
/*     */   public static final int DEFAULT_Y_SEARCH_RADIUS = 10;
/*     */   public static final String DEFAULT_TEST_STRUCTURES_DIR = "Minecraft.Server/src/test/convertables/data";
/*  40 */   public static Path testStructuresDir = Paths.get("Minecraft.Server/src/test/convertables/data", new String[0]);
/*     */   
/*     */   public static Rotation getRotationForRotationSteps(int rotationSteps) {
/*  43 */     switch (rotationSteps) {
/*     */       case 0:
/*  45 */         return Rotation.NONE;
/*     */       case 1:
/*  47 */         return Rotation.CLOCKWISE_90;
/*     */       case 2:
/*  49 */         return Rotation.CLOCKWISE_180;
/*     */       case 3:
/*  51 */         return Rotation.COUNTERCLOCKWISE_90;
/*     */     } 
/*  53 */     throw new IllegalArgumentException("rotationSteps must be a value from 0-3. Got value " + rotationSteps);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getRotationStepsForRotation(Rotation rotation) {
/*  58 */     switch (rotation) {
/*     */       case NONE:
/*  60 */         return 0;
/*     */       case CLOCKWISE_90:
/*  62 */         return 1;
/*     */       case CLOCKWISE_180:
/*  64 */         return 2;
/*     */       case COUNTERCLOCKWISE_90:
/*  66 */         return 3;
/*     */     } 
/*  68 */     throw new IllegalArgumentException("Unknown rotation value, don't know how many steps it represents: " + String.valueOf(rotation));
/*     */   }
/*     */ 
/*     */   
/*     */   public static TestInstanceBlockEntity createNewEmptyTest(Identifier id, BlockPos structurePos, Vec3i size, Rotation rotation, ServerLevel level) {
/*  73 */     BoundingBox structureBoundingBox = getStructureBoundingBox(TestInstanceBlockEntity.getStructurePos(structurePos), size, rotation);
/*  74 */     clearSpaceForStructure(structureBoundingBox, level);
/*     */     
/*  76 */     level.setBlockAndUpdate(structurePos, Blocks.TEST_INSTANCE_BLOCK.defaultBlockState());
/*     */     
/*  78 */     TestInstanceBlockEntity test = (TestInstanceBlockEntity)level.getBlockEntity(structurePos);
/*  79 */     ResourceKey<GameTestInstance> key = ResourceKey.create(Registries.TEST_INSTANCE, id);
/*  80 */     test.set(new TestInstanceBlockEntity.Data(
/*  81 */           Optional.of(key), size, rotation, false, TestInstanceBlockEntity.Status.CLEARED, Optional.empty()));
/*     */ 
/*     */     
/*  84 */     return test;
/*     */   }
/*     */   
/*     */   public static void clearSpaceForStructure(BoundingBox structureBoundingBox, ServerLevel level) {
/*  88 */     int groundHeight = structureBoundingBox.minY() - 1;
/*     */     
/*  90 */     BlockPos.betweenClosedStream(structureBoundingBox).forEach(pos -> clearBlock(groundHeight, pos, level));
/*  91 */     level.getBlockTicks().clearArea(structureBoundingBox);
/*  92 */     level.clearBlockEvents(structureBoundingBox);
/*  93 */     AABB bounds = AABB.of(structureBoundingBox);
/*  94 */     List<Entity> livingEntities = level.getEntitiesOfClass(Entity.class, bounds, mob -> !(mob instanceof net.minecraft.world.entity.player.Player));
/*  95 */     livingEntities.forEach(Entity::discard);
/*     */   }
/*     */   
/*     */   public static BlockPos getTransformedFarCorner(BlockPos structurePosition, Vec3i size, Rotation rotation) {
/*  99 */     BlockPos farCornerBeforeTransform = structurePosition.offset(size).offset(-1, -1, -1);
/* 100 */     return StructureTemplate.transform(farCornerBeforeTransform, Mirror.NONE, rotation, structurePosition);
/*     */   }
/*     */   
/*     */   public static BoundingBox getStructureBoundingBox(BlockPos northWestCorner, Vec3i size, Rotation rotation) {
/* 104 */     BlockPos farCorner = getTransformedFarCorner(northWestCorner, size, rotation);
/* 105 */     BoundingBox boundingBox = BoundingBox.fromCorners(northWestCorner, farCorner);
/*     */     
/* 107 */     int currentNorthWestCornerX = Math.min(boundingBox.minX(), boundingBox.maxX());
/* 108 */     int currentNorthWestCornerZ = Math.min(boundingBox.minZ(), boundingBox.maxZ());
/*     */ 
/*     */     
/* 111 */     return boundingBox.move(northWestCorner.getX() - currentNorthWestCornerX, 0, northWestCorner.getZ() - currentNorthWestCornerZ);
/*     */   }
/*     */ 
/*     */   
/* 115 */   public static Optional<BlockPos> findTestContainingPos(BlockPos pos, int searchRadius, ServerLevel level) { return findTestBlocks(pos, searchRadius, level)
/* 116 */       .filter(testBlockPosToCheck -> doesStructureContain(testBlockPosToCheck, pos, level))
/* 117 */       .findFirst(); }
/*     */ 
/*     */   
/*     */   public static Optional<BlockPos> findNearestTest(BlockPos relativeToPos, int searchRadius, ServerLevel level) {
/* 121 */     Comparator<BlockPos> distanceToPlayer = Comparator.comparingInt(pos -> pos.distManhattan(relativeToPos));
/*     */     
/* 123 */     return findTestBlocks(relativeToPos, searchRadius, level).min(distanceToPlayer);
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> findTestBlocks(BlockPos centerPos, int searchRadius, ServerLevel level) {
/* 127 */     return level.getPoiManager().findAll(p -> 
/* 128 */         p.is(PoiTypes.TEST_INSTANCE), p -> true, centerPos, searchRadius, PoiManager.Occupancy.ANY)
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       .map(BlockPos::immutable);
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> lookedAtTestPos(BlockPos pos, Entity camera, ServerLevel level) {
/* 137 */     int radius = 250;
/* 138 */     Vec3 start = camera.getEyePosition();
/* 139 */     Vec3 end = start.add(camera.getLookAngle().scale(250.0D));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     Objects.requireNonNull(pos); return findTestBlocks(pos, 250, level).map(blockPos -> level.getBlockEntity(blockPos, BlockEntityType.TEST_INSTANCE_BLOCK)).flatMap(Optional::stream).filter(blockEntity -> blockEntity.getStructureBounds().clip(start, end).isPresent()).map(BlockEntity::getBlockPos).sorted(Comparator.comparing(pos::distSqr))
/* 147 */       .limit(1L);
/*     */   }
/*     */   
/*     */   private static void clearBlock(int airIfAboveThisY, BlockPos pos, ServerLevel level) {
/*     */     BlockState blockState;
/* 152 */     if (pos.getY() < airIfAboveThisY) {
/* 153 */       blockState = Blocks.STONE.defaultBlockState();
/*     */     } else {
/* 155 */       blockState = Blocks.AIR.defaultBlockState();
/*     */     } 
/* 157 */     BlockInput blockInput = new BlockInput(blockState, Collections.emptySet(), null);
/* 158 */     blockInput.place(level, pos, 818);
/* 159 */     level.updateNeighborsAt(pos, blockState.getBlock());
/*     */   }
/*     */   
/*     */   private static boolean doesStructureContain(BlockPos testInstanceBlockPos, BlockPos pos, ServerLevel level) {
/* 163 */     BlockEntity blockEntity1 = level.getBlockEntity(testInstanceBlockPos); if (blockEntity1 instanceof TestInstanceBlockEntity) { TestInstanceBlockEntity blockEntity = (TestInstanceBlockEntity)blockEntity1;
/* 164 */       return blockEntity.getStructureBoundingBox().isInside(pos); }
/*     */     
/* 166 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\StructureUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */