/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
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
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class BaseRailBlock extends Block implements SimpleWaterloggedBlock {
/*  27 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  29 */   private static final VoxelShape SHAPE_FLAT = Block.column(16.0D, 0.0D, 2.0D);
/*  30 */   private static final VoxelShape SHAPE_SLOPE = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*     */   private final boolean isStraight;
/*     */ 
/*     */   
/*  35 */   public static boolean isRail(Level level, BlockPos pos) { return isRail(level.getBlockState(pos)); }
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static boolean isRail(BlockState state) { return (state.is(BlockTags.RAILS) && state.getBlock() instanceof BaseRailBlock); }
/*     */ 
/*     */   
/*     */   protected BaseRailBlock(boolean isStraight, BlockBehaviour.Properties properties) {
/*  43 */     super(properties);
/*  44 */     this.isStraight = isStraight;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract MapCodec<? extends BaseRailBlock> codec();
/*     */ 
/*     */   
/*  51 */   public boolean isStraight() { return this.isStraight; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return ((RailShape)state.getValue(getShapeProperty())).isSlope() ? SHAPE_SLOPE : SHAPE_FLAT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return canSupportRigidBlock(level, pos.below()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  66 */     if (oldState.is(state.getBlock())) {
/*     */       return;
/*     */     }
/*  69 */     updateState(state, level, pos, movedByPiston);
/*     */   }
/*     */   
/*     */   protected BlockState updateState(BlockState state, Level level, BlockPos pos, boolean movedByPiston) {
/*  73 */     state = updateDir(level, pos, state, true);
/*     */     
/*  75 */     if (this.isStraight) {
/*  76 */       level.neighborChanged(state, pos, this, null, movedByPiston);
/*     */     }
/*     */     
/*  79 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  84 */     if (level.isClientSide() || !level.getBlockState(pos).is(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  88 */     RailShape shape = (RailShape)state.getValue(getShapeProperty());
/*     */     
/*  90 */     if (shouldBeRemoved(pos, level, shape)) {
/*  91 */       dropResources(state, level, pos);
/*  92 */       level.removeBlock(pos, movedByPiston);
/*     */     } else {
/*  94 */       updateState(state, level, pos, block);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean shouldBeRemoved(BlockPos pos, Level level, RailShape shape) {
/*  99 */     if (!canSupportRigidBlock(level, pos.below())) {
/* 100 */       return true;
/*     */     }
/* 102 */     switch (shape) {
/*     */       case LEFT_RIGHT:
/* 104 */         return !canSupportRigidBlock(level, pos.east());
/*     */       case FRONT_BACK:
/* 106 */         return !canSupportRigidBlock(level, pos.west());
/*     */       case null:
/* 108 */         return !canSupportRigidBlock(level, pos.north());
/*     */       case null:
/* 110 */         return !canSupportRigidBlock(level, pos.south());
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(BlockState state, Level level, BlockPos pos, Block block) {}
/*     */ 
/*     */   
/*     */   protected BlockState updateDir(Level level, BlockPos pos, BlockState state, boolean first) {
/* 120 */     if (level.isClientSide()) {
/* 121 */       return state;
/*     */     }
/* 123 */     RailShape current = (RailShape)state.getValue(getShapeProperty());
/* 124 */     return (new RailState(level, pos, state)).place(level.hasNeighborSignal(pos), first, current).getState();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 129 */     if (movedByPiston) {
/*     */       return;
/*     */     }
/*     */     
/* 133 */     if (((RailShape)state.getValue(getShapeProperty())).isSlope()) {
/* 134 */       level.updateNeighborsAt(pos.above(), this);
/*     */     }
/*     */     
/* 137 */     if (this.isStraight) {
/* 138 */       level.updateNeighborsAt(pos, this);
/* 139 */       level.updateNeighborsAt(pos.below(), this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 145 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 146 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/* 147 */     BlockState state = defaultBlockState();
/* 148 */     Direction direction = context.getHorizontalDirection();
/* 149 */     boolean isEastWest = (direction == Direction.EAST || direction == Direction.WEST);
/* 150 */     return (BlockState)((BlockState)state.setValue(getShapeProperty(), isEastWest ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH)).setValue(WATERLOGGED, Boolean.valueOf(isWaterSource));
/*     */   }
/*     */   
/*     */   public abstract Property<RailShape> getShapeProperty();
/*     */   
/*     */   protected RailShape rotate(RailShape shape, Rotation rotation) {
/* 156 */     switch (rotation) { case LEFT_RIGHT:
/* 157 */         switch (shape) { default: throw new MatchException(null, null);
/*     */           case null: 
/*     */           case null: 
/*     */           case LEFT_RIGHT: 
/*     */           case FRONT_BACK: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: break; } 
/*     */       case FRONT_BACK:
/* 169 */         switch (shape) { default: throw new MatchException(null, null);
/*     */           case null: 
/*     */           case null: 
/*     */           case LEFT_RIGHT: 
/*     */           case FRONT_BACK: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: break; } 
/*     */       case null:
/* 181 */         switch (shape) { default: throw new MatchException(null, null);
/*     */           case null: 
/*     */           case null: 
/*     */           case LEFT_RIGHT: 
/*     */           case FRONT_BACK: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null:
/*     */           
/*     */           case null:
/* 193 */             break; }  }  return shape;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RailShape mirror(RailShape shape, Mirror mirror) {
/* 198 */     switch (mirror) { case LEFT_RIGHT:
/* 199 */         switch (shape) { case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null: 
/*     */           case null:
/*     */           
/*     */           case null:
/*     */            } 
/*     */       case FRONT_BACK:
/* 208 */         switch (shape) { case LEFT_RIGHT: 
/*     */           case FRONT_BACK: 
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/*     */           
/*     */           case null:
/* 217 */            }  }  return shape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 223 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 224 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 226 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 231 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 232 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 234 */     return super.getFluidState(state);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BaseRailBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */