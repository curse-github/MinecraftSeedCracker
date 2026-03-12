/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.RandomizableContainer;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MonsterRoomFeature
/*     */   extends Feature<NoneFeatureConfiguration>
/*     */ {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  27 */   private static final EntityType<?>[] MOBS = { EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER };
/*  28 */   private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */ 
/*     */   
/*  31 */   public MonsterRoomFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/*  36 */     Predicate<BlockState> replaceableTag = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
/*  37 */     BlockPos origin = context.origin();
/*  38 */     RandomSource random = context.random();
/*  39 */     WorldGenLevel level = context.level();
/*  40 */     int hr = 3;
/*  41 */     int xr = random.nextInt(2) + 2;
/*  42 */     int minX = -xr - 1;
/*  43 */     int maxX = xr + 1;
/*     */     
/*  45 */     int minY = -1;
/*  46 */     int maxY = 4;
/*     */     
/*  48 */     int zr = random.nextInt(2) + 2;
/*  49 */     int minZ = -zr - 1;
/*  50 */     int maxZ = zr + 1;
/*     */     
/*  52 */     int holeCount = 0;
/*  53 */     for (int dx = minX; dx <= maxX; dx++) {
/*  54 */       for (int dy = -1; dy <= 4; dy++) {
/*  55 */         for (int dz = minZ; dz <= maxZ; dz++) {
/*  56 */           BlockPos holePos = origin.offset(dx, dy, dz);
/*  57 */           boolean solid = level.getBlockState(holePos).isSolid();
/*     */           
/*  59 */           if (dy == -1 && !solid) {
/*  60 */             return false;
/*     */           }
/*  62 */           if (dy == 4 && !solid) {
/*  63 */             return false;
/*     */           }
/*     */           
/*  66 */           if ((dx == minX || dx == maxX || dz == minZ || dz == maxZ) && 
/*  67 */             dy == 0 && level.isEmptyBlock(holePos) && level.isEmptyBlock(holePos.above())) {
/*  68 */             holeCount++;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  75 */     if (holeCount < 1 || holeCount > 5) {
/*  76 */       return false;
/*     */     }
/*     */     
/*  79 */     for (int dx = minX; dx <= maxX; dx++) {
/*  80 */       for (int dy = 3; dy >= -1; dy--) {
/*  81 */         for (int dz = minZ; dz <= maxZ; dz++) {
/*  82 */           BlockPos wallBlock = origin.offset(dx, dy, dz);
/*     */           
/*  84 */           BlockState wallState = level.getBlockState(wallBlock);
/*  85 */           if (dx == minX || dy == -1 || dz == minZ || dx == maxX || dy == 4 || dz == maxZ) {
/*  86 */             if (wallBlock.getY() >= level.getMinY() && !level.getBlockState(wallBlock.below()).isSolid()) {
/*  87 */               level.setBlock(wallBlock, AIR, 2);
/*  88 */             } else if (wallState.isSolid() && 
/*  89 */               !wallState.is(Blocks.CHEST)) {
/*  90 */               if (dy == -1 && random.nextInt(4) != 0) {
/*  91 */                 safeSetBlock(level, wallBlock, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), replaceableTag);
/*     */               } else {
/*  93 */                 safeSetBlock(level, wallBlock, Blocks.COBBLESTONE.defaultBlockState(), replaceableTag);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*  98 */           } else if (!wallState.is(Blocks.CHEST) && !wallState.is(Blocks.SPAWNER)) {
/*  99 */             safeSetBlock(level, wallBlock, AIR, replaceableTag);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 106 */     for (int cc = 0; cc < 2; cc++) {
/* 107 */       for (int i = 0; i < 3; i++) {
/* 108 */         int xc = origin.getX() + random.nextInt(xr * 2 + 1) - xr;
/* 109 */         int yc = origin.getY();
/* 110 */         int zc = origin.getZ() + random.nextInt(zr * 2 + 1) - zr;
/* 111 */         BlockPos chestPos = new BlockPos(xc, yc, zc);
/*     */         
/* 113 */         if (level.isEmptyBlock(chestPos)) {
/*     */ 
/*     */ 
/*     */           
/* 117 */           int wallCount = 0;
/* 118 */           for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 119 */             if (level.getBlockState(chestPos.relative(direction)).isSolid()) {
/* 120 */               wallCount++;
/*     */             }
/*     */           } 
/*     */           
/* 124 */           if (wallCount == 1) {
/*     */ 
/*     */ 
/*     */             
/* 128 */             safeSetBlock(level, chestPos, StructurePiece.reorient(level, chestPos, Blocks.CHEST.defaultBlockState()), replaceableTag);
/* 129 */             RandomizableContainer.setBlockEntityLootTable(level, random, chestPos, BuiltInLootTables.SIMPLE_DUNGEON);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 135 */     safeSetBlock(level, origin, Blocks.SPAWNER.defaultBlockState(), replaceableTag);
/* 136 */     BlockEntity blockEntity = level.getBlockEntity(origin);
/*     */     
/* 138 */     if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 139 */       spawner.setEntityId(randomEntityId(random), random); }
/*     */     else
/* 141 */     { LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", new Object[] { Integer.valueOf(origin.getX()), Integer.valueOf(origin.getY()), Integer.valueOf(origin.getZ()) }); }
/*     */ 
/*     */     
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 148 */   private EntityType<?> randomEntityId(RandomSource random) { return (EntityType)Util.getRandom(MOBS, random); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\MonsterRoomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */