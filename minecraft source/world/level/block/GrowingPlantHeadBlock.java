/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class GrowingPlantHeadBlock extends GrowingPlantBlock implements BonemealableBlock {
/*  19 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_25;
/*     */   
/*     */   public static final int MAX_AGE = 25;
/*     */   private final double growPerTickProbability;
/*     */   
/*     */   protected GrowingPlantHeadBlock(BlockBehaviour.Properties properties, Direction growthDirection, VoxelShape shape, boolean scheduleFluidTicks, double growPerTickProbability) {
/*  25 */     super(properties, growthDirection, shape, scheduleFluidTicks);
/*  26 */     this.growPerTickProbability = growPerTickProbability;
/*  27 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract MapCodec<? extends GrowingPlantHeadBlock> codec();
/*     */ 
/*     */   
/*  35 */   public BlockState getStateForPlacement(RandomSource random) { return (BlockState)defaultBlockState().setValue(AGE, Integer.valueOf(random.nextInt(25))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected boolean isRandomlyTicking(BlockState state) { return (((Integer)state.getValue(AGE)).intValue() < 25); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  45 */     if (((Integer)state.getValue(AGE)).intValue() < 25 && random.nextDouble() < this.growPerTickProbability) {
/*  46 */       BlockPos growthPos = pos.relative(this.growthDirection);
/*  47 */       if (canGrowInto(level.getBlockState(growthPos))) {
/*  48 */         level.setBlockAndUpdate(growthPos, getGrowIntoState(state, level.random));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  54 */   protected BlockState getGrowIntoState(BlockState growFromState, RandomSource random) { return (BlockState)growFromState.cycle(AGE); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public BlockState getMaxAgeState(BlockState fromState) { return (BlockState)fromState.setValue(AGE, Integer.valueOf(25)); }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public boolean isMaxAge(BlockState state) { return (((Integer)state.getValue(AGE)).intValue() == 25); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   protected BlockState updateBodyAfterConvertedFromHead(BlockState headState, BlockState bodyState) { return bodyState; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  74 */     if (directionToNeighbour == this.growthDirection.getOpposite()) {
/*  75 */       if (!state.canSurvive(level, pos)) {
/*  76 */         ticks.scheduleTick(pos, this, 1);
/*     */       } else {
/*     */         
/*  79 */         BlockState neighborInGrowthDirection = level.getBlockState(pos.relative(this.growthDirection));
/*  80 */         if (neighborInGrowthDirection.is(this) || neighborInGrowthDirection.is(getBodyBlock())) {
/*  81 */           return updateBodyAfterConvertedFromHead(state, getBodyBlock().defaultBlockState());
/*     */         }
/*     */       } 
/*     */     }
/*  85 */     if (directionToNeighbour == this.growthDirection && (neighbourState.is(this) || neighbourState.is(getBodyBlock())))
/*     */     {
/*  87 */       return updateBodyAfterConvertedFromHead(state, getBodyBlock().defaultBlockState());
/*     */     }
/*  89 */     if (this.scheduleFluidTicks) {
/*  90 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  93 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return canGrowInto(level.getBlockState(pos.relative(this.growthDirection))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 113 */     BlockPos forwardPos = pos.relative(this.growthDirection);
/* 114 */     int nextAge = Math.min(((Integer)state.getValue(AGE)).intValue() + 1, 25);
/*     */     
/* 116 */     int blocksToGrow = getBlocksToGrowWhenBonemealed(random);
/* 117 */     for (int i = 0; i < blocksToGrow && 
/* 118 */       canGrowInto(level.getBlockState(forwardPos)); i++) {
/*     */ 
/*     */       
/* 121 */       level.setBlockAndUpdate(forwardPos, (BlockState)state.setValue(AGE, Integer.valueOf(nextAge)));
/*     */       
/* 123 */       forwardPos = forwardPos.relative(this.growthDirection);
/* 124 */       nextAge = Math.min(nextAge + 1, 25);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract int getBlocksToGrowWhenBonemealed(RandomSource paramRandomSource);
/*     */ 
/*     */   
/*     */   protected abstract boolean canGrowInto(BlockState paramBlockState);
/*     */   
/* 134 */   protected GrowingPlantHeadBlock getHeadBlock() { return this; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\GrowingPlantHeadBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */