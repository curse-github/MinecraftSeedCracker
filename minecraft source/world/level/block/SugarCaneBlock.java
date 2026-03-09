/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SugarCaneBlock extends Block {
/*  22 */   public static final MapCodec<SugarCaneBlock> CODEC = simpleCodec(SugarCaneBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  26 */   public MapCodec<SugarCaneBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  29 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*     */   
/*  31 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 16.0D);
/*     */   
/*     */   protected SugarCaneBlock(BlockBehaviour.Properties properties) {
/*  34 */     super(properties);
/*  35 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  40 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  45 */     if (!state.canSurvive(level, pos)) {
/*  46 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  52 */     if (level.isEmptyBlock(pos.above())) {
/*  53 */       int height = 1;
/*  54 */       while (level.getBlockState(pos.below(height)).is(this)) {
/*  55 */         height++;
/*     */       }
/*  57 */       if (height < 3) {
/*  58 */         int age = ((Integer)state.getValue(AGE)).intValue();
/*  59 */         if (age == 15) {
/*  60 */           level.setBlockAndUpdate(pos.above(), defaultBlockState());
/*  61 */           level.setBlock(pos, (BlockState)state.setValue(AGE, Integer.valueOf(0)), 260);
/*     */         } else {
/*  63 */           level.setBlock(pos, (BlockState)state.setValue(AGE, Integer.valueOf(age + 1)), 260);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  71 */     if (!state.canSurvive(level, pos)) {
/*  72 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*     */     
/*  75 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  80 */     BlockState stateBelow = level.getBlockState(pos.below());
/*  81 */     if (stateBelow.is(this)) {
/*  82 */       return true;
/*     */     }
/*     */     
/*  85 */     if (stateBelow.is(BlockTags.DIRT) || stateBelow.is(BlockTags.SAND)) {
/*  86 */       BlockPos below = pos.below();
/*  87 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  88 */         BlockState blockState = level.getBlockState(below.relative(direction));
/*  89 */         FluidState fluidState = level.getFluidState(below.relative(direction));
/*  90 */         if (fluidState.is(FluidTags.WATER) || blockState.is(Blocks.FROSTED_ICE)) {
/*  91 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 101 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SugarCaneBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */