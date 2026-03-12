/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public interface SignalGetter
/*     */   extends BlockGetter {
/*  12 */   public static final Direction[] DIRECTIONS = Direction.values();
/*     */ 
/*     */   
/*  15 */   default int getDirectSignal(BlockPos pos, Direction direction) { return getBlockState(pos).getDirectSignal(this, pos, direction); }
/*     */ 
/*     */   
/*     */   default int getDirectSignalTo(BlockPos pos) {
/*  19 */     int result = 0;
/*  20 */     result = Math.max(result, getDirectSignal(pos.below(), Direction.DOWN));
/*  21 */     if (result >= 15) {
/*  22 */       return result;
/*     */     }
/*  24 */     result = Math.max(result, getDirectSignal(pos.above(), Direction.UP));
/*  25 */     if (result >= 15) {
/*  26 */       return result;
/*     */     }
/*  28 */     result = Math.max(result, getDirectSignal(pos.north(), Direction.NORTH));
/*  29 */     if (result >= 15) {
/*  30 */       return result;
/*     */     }
/*  32 */     result = Math.max(result, getDirectSignal(pos.south(), Direction.SOUTH));
/*  33 */     if (result >= 15) {
/*  34 */       return result;
/*     */     }
/*  36 */     result = Math.max(result, getDirectSignal(pos.west(), Direction.WEST));
/*  37 */     if (result >= 15) {
/*  38 */       return result;
/*     */     }
/*  40 */     result = Math.max(result, getDirectSignal(pos.east(), Direction.EAST));
/*  41 */     if (result >= 15) {
/*  42 */       return result;
/*     */     }
/*  44 */     return result;
/*     */   }
/*     */   
/*     */   default int getControlInputSignal(BlockPos pos, Direction direction, boolean onlyDiodes) {
/*  48 */     BlockState blockState = getBlockState(pos);
/*  49 */     if (onlyDiodes) {
/*  50 */       return DiodeBlock.isDiode(blockState) ? getDirectSignal(pos, direction) : 0;
/*     */     }
/*     */     
/*  53 */     if (blockState.is(Blocks.REDSTONE_BLOCK)) {
/*  54 */       return 15;
/*     */     }
/*     */     
/*  57 */     if (blockState.is(Blocks.REDSTONE_WIRE)) {
/*  58 */       return ((Integer)blockState.getValue(RedStoneWireBlock.POWER)).intValue();
/*     */     }
/*  60 */     if (blockState.isSignalSource()) {
/*  61 */       return getDirectSignal(pos, direction);
/*     */     }
/*  63 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*  67 */   default boolean hasSignal(BlockPos pos, Direction direction) { return (getSignal(pos, direction) > 0); }
/*     */ 
/*     */   
/*     */   default int getSignal(BlockPos pos, Direction direction) {
/*  71 */     BlockState state = getBlockState(pos);
/*     */     
/*  73 */     int signal = state.getSignal(this, pos, direction);
/*  74 */     if (state.isRedstoneConductor(this, pos)) {
/*  75 */       return Math.max(signal, getDirectSignalTo(pos));
/*     */     }
/*  77 */     return signal;
/*     */   }
/*     */   
/*     */   default boolean hasNeighborSignal(BlockPos blockPos) {
/*  81 */     if (getSignal(blockPos.below(), Direction.DOWN) > 0) {
/*  82 */       return true;
/*     */     }
/*  84 */     if (getSignal(blockPos.above(), Direction.UP) > 0) {
/*  85 */       return true;
/*     */     }
/*  87 */     if (getSignal(blockPos.north(), Direction.NORTH) > 0) {
/*  88 */       return true;
/*     */     }
/*  90 */     if (getSignal(blockPos.south(), Direction.SOUTH) > 0) {
/*  91 */       return true;
/*     */     }
/*  93 */     if (getSignal(blockPos.west(), Direction.WEST) > 0) {
/*  94 */       return true;
/*     */     }
/*  96 */     return (getSignal(blockPos.east(), Direction.EAST) > 0);
/*     */   }
/*     */   
/*     */   default int getBestNeighborSignal(BlockPos pos) {
/* 100 */     int best = 0;
/*     */     
/* 102 */     for (Direction direction : DIRECTIONS) {
/* 103 */       int signal = getSignal(pos.relative(direction), direction);
/*     */       
/* 105 */       if (signal >= 15) {
/* 106 */         return 15;
/*     */       }
/* 108 */       if (signal > best) {
/* 109 */         best = signal;
/*     */       }
/*     */     } 
/*     */     
/* 113 */     return best;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\SignalGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */