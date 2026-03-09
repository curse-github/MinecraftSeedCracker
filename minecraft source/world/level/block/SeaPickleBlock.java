/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
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
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SeaPickleBlock extends VegetationBlock implements SimpleWaterloggedBlock, BonemealableBlock {
/*  27 */   public static final MapCodec<SeaPickleBlock> CODEC = simpleCodec(SeaPickleBlock::new);
/*     */   
/*     */   public static final int MAX_PICKLES = 4;
/*     */   
/*  31 */   public MapCodec<SeaPickleBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  35 */   public static final IntegerProperty PICKLES = BlockStateProperties.PICKLES;
/*  36 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  38 */   private static final VoxelShape SHAPE_ONE = Block.column(4.0D, 0.0D, 6.0D);
/*  39 */   private static final VoxelShape SHAPE_TWO = Block.column(10.0D, 0.0D, 6.0D);
/*  40 */   private static final VoxelShape SHAPE_THREE = Block.column(12.0D, 0.0D, 6.0D);
/*  41 */   private static final VoxelShape SHAPE_FOUR = Block.column(12.0D, 0.0D, 7.0D);
/*     */   
/*     */   protected SeaPickleBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(PICKLES, Integer.valueOf(1))).setValue(WATERLOGGED, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  50 */     BlockState state = context.getLevel().getBlockState(context.getClickedPos());
/*  51 */     if (state.is(this)) {
/*  52 */       return (BlockState)state.setValue(PICKLES, Integer.valueOf(Math.min(4, ((Integer)state.getValue(PICKLES)).intValue() + 1)));
/*     */     }
/*     */     
/*  55 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  56 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/*  57 */     return (BlockState)super.getStateForPlacement(context).setValue(WATERLOGGED, Boolean.valueOf(isWaterSource));
/*     */   }
/*     */ 
/*     */   
/*  61 */   public static boolean isDead(BlockState state) { return !((Boolean)state.getValue(WATERLOGGED)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return (!state.getCollisionShape(level, pos).getFaceShape(Direction.UP).isEmpty() || state.isFaceSturdy(level, pos, Direction.UP)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  71 */     BlockPos belowPos = pos.below();
/*  72 */     return mayPlaceOn(level.getBlockState(belowPos), level, belowPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  77 */     if (!state.canSurvive(level, pos)) {
/*  78 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  81 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  82 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  85 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/*  90 */     if (!context.isSecondaryUseActive() && context.getItemInHand().is(asItem()) && ((Integer)state.getValue(PICKLES)).intValue() < 4) {
/*  91 */       return true;
/*     */     }
/*  93 */     return super.canBeReplaced(state, context);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  98 */     switch (((Integer)state.getValue(PICKLES)).intValue()) { default: case 2: case 3: case 4: break; }  return 
/*     */ 
/*     */ 
/*     */       
/* 102 */       SHAPE_FOUR;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 108 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 109 */       return Fluids.WATER.getSource(false);
/*     */     }
/*     */     
/* 112 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 117 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { PICKLES, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return (!isDead(state) && level.getBlockState(pos.below()).is(BlockTags.CORAL_BLOCKS)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 132 */     int span = 5;
/* 133 */     int zSpan = 1;
/* 134 */     int height = 2;
/* 135 */     int count = 0;
/*     */     
/* 137 */     int xStart = pos.getX() - 2;
/* 138 */     int zOffSet = 0;
/*     */     
/* 140 */     for (int x = 0; x < 5; x++) {
/* 141 */       for (int z = 0; z < zSpan; z++) {
/* 142 */         int endY = 2 + pos.getY() - 1;
/* 143 */         for (int startY = endY - 2; startY < endY; startY++) {
/* 144 */           BlockPos position = new BlockPos(xStart + x, startY, pos.getZ() - zOffSet + z);
/* 145 */           if (!position.equals(pos))
/*     */           {
/*     */ 
/*     */             
/* 149 */             if (random.nextInt(6) == 0 && level.getBlockState(position).is(Blocks.WATER)) {
/* 150 */               BlockState belowState = level.getBlockState(position.below());
/* 151 */               if (belowState.is(BlockTags.CORAL_BLOCKS)) {
/* 152 */                 level.setBlock(position, (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue(PICKLES, Integer.valueOf(random.nextInt(4) + 1)), 3);
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 158 */       if (count < 2) {
/* 159 */         zSpan += 2;
/* 160 */         zOffSet++;
/*     */       } else {
/* 162 */         zSpan -= 2;
/* 163 */         zOffSet--;
/*     */       } 
/* 165 */       count++;
/*     */     } 
/*     */     
/* 168 */     level.setBlock(pos, (BlockState)state.setValue(PICKLES, Integer.valueOf(4)), 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 173 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SeaPickleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */