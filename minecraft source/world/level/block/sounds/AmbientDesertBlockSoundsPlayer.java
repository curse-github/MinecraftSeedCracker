/*     */ package net.minecraft.world.level.block.sounds;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AmbientDesertBlockSoundsPlayer
/*     */ {
/*     */   private static final int IDLE_SOUND_CHANCE = 2100;
/*     */   private static final int DRY_GRASS_SOUND_CHANCE = 200;
/*     */   private static final int DEAD_BUSH_SOUND_CHANCE = 130;
/*     */   private static final int DEAD_BUSH_SOUND_BADLANDS_DECREASED_CHANCE = 3;
/*     */   private static final int SURROUNDING_BLOCKS_PLAY_SOUND_THRESHOLD = 3;
/*     */   private static final int SURROUNDING_BLOCKS_DISTANCE_HORIZONTAL_CHECK = 8;
/*     */   private static final int SURROUNDING_BLOCKS_DISTANCE_VERTICAL_CHECK = 5;
/*     */   private static final int HORIZONTAL_DIRECTIONS = 4;
/*     */   
/*     */   public static void playAmbientSandSounds(Level level, BlockPos pos, RandomSource random) {
/*  28 */     if (!level.getBlockState(pos.above()).is(Blocks.AIR)) {
/*     */       return;
/*     */     }
/*     */     
/*  32 */     if (random.nextInt(2100) == 0 && 
/*  33 */       shouldPlayAmbientSandSound(level, pos)) {
/*  34 */       level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SAND_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void playAmbientDryGrassSounds(Level level, BlockPos pos, RandomSource random) {
/*  40 */     if (random.nextInt(200) == 0 && 
/*  41 */       shouldPlayDesertDryVegetationBlockSounds(level, pos.below())) {
/*  42 */       level.playPlayerSound(SoundEvents.DRY_GRASS, SoundSource.AMBIENT, 1.0F, 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void playAmbientDeadBushSounds(Level level, BlockPos pos, RandomSource random) {
/*  48 */     if (random.nextInt(130) == 0) {
/*  49 */       BlockState belowPos = level.getBlockState(pos.below());
/*  50 */       if ((belowPos.is(Blocks.RED_SAND) || belowPos.is(BlockTags.TERRACOTTA)) && random.nextInt(3) != 0) {
/*     */         return;
/*     */       }
/*  53 */       if (shouldPlayDesertDryVegetationBlockSounds(level, pos.below())) {
/*  54 */         level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.DEAD_BUSH_IDLE, SoundSource.AMBIENT, 1.0F, 1.0F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  60 */   public static boolean shouldPlayDesertDryVegetationBlockSounds(Level level, BlockPos belowPos) { return (level.getBlockState(belowPos).is(BlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS) && level.getBlockState(belowPos.below()).is(BlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS)); }
/*     */ 
/*     */   
/*     */   private static boolean shouldPlayAmbientSandSound(Level level, BlockPos pos) {
/*  64 */     int matchingBlocksFound = 0;
/*  65 */     int sidesChecked = 0;
/*  66 */     BlockPos.MutableBlockPos mutablePos = pos.mutable();
/*     */     
/*  68 */     for (Direction dir : Direction.Plane.HORIZONTAL) {
/*  69 */       mutablePos.set(pos).move(dir, 8);
/*     */       
/*  71 */       if (columnContainsTriggeringBlock(level, mutablePos) && matchingBlocksFound++ >= 3) {
/*  72 */         return true;
/*     */       }
/*     */       
/*  75 */       sidesChecked++;
/*  76 */       int remainingSides = 4 - sidesChecked;
/*  77 */       int potentialMatches = remainingSides + matchingBlocksFound;
/*  78 */       boolean canStillFindRequiredSoundTriggerBlocks = (potentialMatches >= 3);
/*     */       
/*  80 */       if (!canStillFindRequiredSoundTriggerBlocks) {
/*  81 */         return false;
/*     */       }
/*     */     } 
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean columnContainsTriggeringBlock(Level level, BlockPos.MutableBlockPos mutablePos) {
/*  88 */     int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, mutablePos) - 1;
/*     */     
/*  90 */     if (Math.abs(surfaceY - mutablePos.getY()) <= 5) {
/*  91 */       boolean hasAirAbove = level.getBlockState(mutablePos.setY(surfaceY + 1)).isAir();
/*  92 */       return (hasAirAbove && canTriggerAmbientDesertSandSounds(level.getBlockState(mutablePos.setY(surfaceY))));
/*     */     } 
/*     */     
/*  95 */     mutablePos.move(Direction.UP, 6);
/*  96 */     BlockState aboveBlockState = level.getBlockState(mutablePos);
/*  97 */     mutablePos.move(Direction.DOWN);
/*     */     
/*  99 */     for (int i = 0; i < 10; i++) {
/* 100 */       BlockState currentBlockState = level.getBlockState(mutablePos);
/* 101 */       if (aboveBlockState.isAir() && canTriggerAmbientDesertSandSounds(currentBlockState)) {
/* 102 */         return true;
/*     */       }
/* 104 */       aboveBlockState = currentBlockState;
/* 105 */       mutablePos.move(Direction.DOWN);
/*     */     } 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 111 */   private static boolean canTriggerAmbientDesertSandSounds(BlockState blockState) { return blockState.is(BlockTags.TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\sounds\AmbientDesertBlockSoundsPlayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */