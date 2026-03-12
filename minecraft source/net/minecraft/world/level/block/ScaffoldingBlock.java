/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
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
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ScaffoldingBlock extends Block implements SimpleWaterloggedBlock {
/*  26 */   public static final MapCodec<ScaffoldingBlock> CODEC = simpleCodec(ScaffoldingBlock::new);
/*     */   
/*     */   private static final int TICK_DELAY = 1;
/*     */   
/*  30 */   public MapCodec<ScaffoldingBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   private static final VoxelShape SHAPE_STABLE = Shapes.or(
/*  36 */       Block.column(16.0D, 14.0D, 16.0D), 
/*  37 */       (VoxelShape)Shapes.rotateHorizontal(Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D)).values().stream().reduce(Shapes.empty(), Shapes::or));
/*     */   
/*  39 */   private static final VoxelShape SHAPE_UNSTABLE_BOTTOM = Block.column(16.0D, 0.0D, 2.0D);
/*  40 */   private static final VoxelShape SHAPE_UNSTABLE = Shapes.or(SHAPE_STABLE, new VoxelShape[] { SHAPE_UNSTABLE_BOTTOM, 
/*     */ 
/*     */         
/*  43 */         (VoxelShape)Shapes.rotateHorizontal(Block.boxZ(16.0D, 0.0D, 2.0D, 0.0D, 2.0D)).values().stream().reduce(Shapes.empty(), Shapes::or) });
/*     */ 
/*     */   
/*  46 */   private static final VoxelShape SHAPE_BELOW_BLOCK = Shapes.block().move(0.0D, -1.0D, 0.0D).optimize();
/*     */   
/*     */   public static final int STABILITY_MAX_DISTANCE = 7;
/*  49 */   public static final IntegerProperty DISTANCE = BlockStateProperties.STABILITY_DISTANCE;
/*  50 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  51 */   public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;
/*     */   
/*     */   protected ScaffoldingBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(DISTANCE, Integer.valueOf(7))).setValue(WATERLOGGED, Boolean.valueOf(false))).setValue(BOTTOM, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  60 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { DISTANCE, WATERLOGGED, BOTTOM }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  65 */     if (!context.isHoldingItem(state.getBlock().asItem())) {
/*  66 */       return ((Boolean)state.getValue(BOTTOM)).booleanValue() ? SHAPE_UNSTABLE : SHAPE_STABLE;
/*     */     }
/*  68 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) { return Shapes.block(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) { return context.getItemInHand().is(asItem()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  83 */     BlockPos pos = context.getClickedPos();
/*  84 */     Level level = context.getLevel();
/*     */     
/*  86 */     int distance = getDistance(level, pos);
/*  87 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/*  88 */       .setValue(WATERLOGGED, Boolean.valueOf((level.getFluidState(pos).getType() == Fluids.WATER))))
/*  89 */       .setValue(DISTANCE, Integer.valueOf(distance)))
/*  90 */       .setValue(BOTTOM, Boolean.valueOf(isBottom(level, pos, distance)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  95 */     if (!level.isClientSide()) {
/*  96 */       level.scheduleTick(pos, this, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 102 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 103 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 106 */     if (!level.isClientSide()) {
/* 107 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/*     */     
/* 110 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 115 */     int distance = getDistance(level, pos);
/*     */ 
/*     */     
/* 118 */     BlockState newState = (BlockState)((BlockState)state.setValue(DISTANCE, Integer.valueOf(distance))).setValue(BOTTOM, Boolean.valueOf(isBottom(level, pos, distance)));
/*     */     
/* 120 */     if (((Integer)newState.getValue(DISTANCE)).intValue() == 7) {
/* 121 */       if (((Integer)state.getValue(DISTANCE)).intValue() == 7) {
/*     */         
/* 123 */         FallingBlockEntity.fall(level, pos, newState);
/*     */       } else {
/*     */         
/* 126 */         level.destroyBlock(pos, true);
/*     */       } 
/* 128 */     } else if (state != newState) {
/* 129 */       level.setBlock(pos, newState, 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 135 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return (getDistance(level, pos) < 7); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 140 */     if (context.isPlacement()) {
/* 141 */       return Shapes.empty();
/*     */     }
/*     */     
/* 144 */     if (!context.isAbove(Shapes.block(), pos, true) || context.isDescending()) {
/* 145 */       if (((Integer)state.getValue(DISTANCE)).intValue() != 0 && ((Boolean)state.getValue(BOTTOM)).booleanValue() && context.isAbove(SHAPE_BELOW_BLOCK, pos, true)) {
/* 146 */         return SHAPE_UNSTABLE_BOTTOM;
/*     */       }
/* 148 */       return Shapes.empty();
/*     */     } 
/* 150 */     return SHAPE_STABLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 155 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 156 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 158 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/* 162 */   private boolean isBottom(BlockGetter level, BlockPos pos, int distance) { return (distance > 0 && !level.getBlockState(pos.below()).is(this)); }
/*     */ 
/*     */   
/*     */   public static int getDistance(BlockGetter level, BlockPos pos) {
/* 166 */     BlockPos.MutableBlockPos relativePos = pos.mutable().move(Direction.DOWN);
/* 167 */     BlockState belowState = level.getBlockState(relativePos);
/*     */     
/* 169 */     int distance = 7;
/* 170 */     if (belowState.is(Blocks.SCAFFOLDING)) {
/* 171 */       distance = ((Integer)belowState.getValue(DISTANCE)).intValue();
/*     */     }
/* 173 */     else if (belowState.isFaceSturdy(level, relativePos, Direction.UP)) {
/* 174 */       return 0;
/*     */     } 
/*     */     
/* 177 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 178 */       BlockState relativeState = level.getBlockState(relativePos.setWithOffset(pos, direction));
/* 179 */       if (!relativeState.is(Blocks.SCAFFOLDING)) {
/*     */         continue;
/*     */       }
/*     */       
/* 183 */       distance = Math.min(distance, ((Integer)relativeState.getValue(DISTANCE)).intValue() + 1);
/*     */       
/* 185 */       if (distance == 1) {
/*     */         break;
/*     */       }
/*     */     } 
/* 189 */     return distance;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ScaffoldingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */