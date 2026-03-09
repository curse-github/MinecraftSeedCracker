/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CactusBlock extends Block {
/*  25 */   public static final MapCodec<CactusBlock> CODEC = simpleCodec(CactusBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  29 */   public MapCodec<CactusBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  32 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*     */   
/*     */   public static final int MAX_AGE = 15;
/*  35 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 16.0D);
/*     */   
/*  37 */   private static final VoxelShape SHAPE_COLLISION = Block.column(14.0D, 0.0D, 15.0D);
/*     */   private static final int MAX_CACTUS_GROWING_HEIGHT = 3;
/*     */   private static final int ATTEMPT_GROW_CACTUS_FLOWER_AGE = 8;
/*     */   private static final double ATTEMPT_GROW_CACTUS_FLOWER_SMALL_CACTUS_CHANCE = 0.1D;
/*     */   private static final double ATTEMPT_GROW_CACTUS_FLOWER_TALL_CACTUS_CHANCE = 0.25D;
/*     */   
/*     */   protected CactusBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  50 */     if (!state.canSurvive(level, pos)) {
/*  51 */       level.destroyBlock(pos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  57 */     BlockPos above = pos.above();
/*  58 */     if (!level.isEmptyBlock(above)) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     int height = 1;
/*  63 */     int age = ((Integer)state.getValue(AGE)).intValue();
/*  64 */     while (level.getBlockState(pos.below(height)).is(this)) {
/*  65 */       height++;
/*  66 */       if (height == 3 && age == 15) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/*  71 */     if (age == 8 && canSurvive(defaultBlockState(), level, pos.above())) {
/*  72 */       double chanceToGrowFlower = (height >= 3) ? 0.25D : 0.1D;
/*  73 */       if (random.nextDouble() <= chanceToGrowFlower) {
/*  74 */         level.setBlockAndUpdate(above, Blocks.CACTUS_FLOWER.defaultBlockState());
/*     */       }
/*  76 */     } else if (age == 15 && height < 3) {
/*  77 */       level.setBlockAndUpdate(above, defaultBlockState());
/*  78 */       BlockState aboveBlock = (BlockState)state.setValue(AGE, Integer.valueOf(0));
/*  79 */       level.setBlock(pos, aboveBlock, 260);
/*  80 */       level.neighborChanged(aboveBlock, above, this, null, false);
/*     */     } 
/*     */     
/*  83 */     if (age < 15) {
/*  84 */       level.setBlock(pos, (BlockState)state.setValue(AGE, Integer.valueOf(age + 1)), 260);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE_COLLISION; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 100 */     if (!state.canSurvive(level, pos)) {
/* 101 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*     */     
/* 104 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 109 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 110 */       BlockState neighbor = level.getBlockState(pos.relative(direction));
/*     */       
/* 112 */       if (neighbor.isSolid() || level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA)) {
/* 113 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 117 */     BlockState belowState = level.getBlockState(pos.below());
/* 118 */     return ((belowState.is(Blocks.CACTUS) || belowState.is(BlockTags.SAND)) && !level.getBlockState(pos.above()).liquid());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 123 */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) { entity.hurt(level.damageSources().cactus(), 1.0F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { AGE }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CactusBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */