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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class ObserverBlock extends DirectionalBlock {
/*  22 */   public static final MapCodec<ObserverBlock> CODEC = simpleCodec(ObserverBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  26 */   public MapCodec<ObserverBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  29 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   public ObserverBlock(BlockBehaviour.Properties properties) {
/*  32 */     super(properties);
/*     */     
/*  34 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.SOUTH)).setValue(POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  39 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, POWERED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  54 */     if (((Boolean)state.getValue(POWERED)).booleanValue()) {
/*  55 */       level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(false)), 2);
/*     */     } else {
/*  57 */       level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(true)), 2);
/*  58 */       level.scheduleTick(pos, this, 2);
/*     */     } 
/*  60 */     updateNeighborsInFront(level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  65 */     if (state.getValue(FACING) == directionToNeighbour && !((Boolean)state.getValue(POWERED)).booleanValue()) {
/*  66 */       startSignal(level, ticks, pos);
/*     */     }
/*     */     
/*  69 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private void startSignal(LevelReader level, ScheduledTickAccess ticks, BlockPos pos) {
/*  73 */     if (!level.isClientSide() && !ticks.getBlockTicks().hasScheduledTick(pos, this)) {
/*  74 */       ticks.scheduleTick(pos, this, 2);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateNeighborsInFront(Level level, BlockPos pos, BlockState state) {
/*  79 */     Direction direction = (Direction)state.getValue(FACING);
/*  80 */     BlockPos oppositePos = pos.relative(direction.getOpposite());
/*     */     
/*  82 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, direction.getOpposite(), null);
/*  83 */     level.neighborChanged(oppositePos, this, orientation);
/*  84 */     level.updateNeighborsAtExceptFromFacing(oppositePos, this, direction, orientation);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return state.getSignal(level, pos, direction); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  99 */     if (((Boolean)state.getValue(POWERED)).booleanValue() && state.getValue(FACING) == direction) {
/* 100 */       return 15;
/*     */     }
/* 102 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 107 */     if (state.is(oldState.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 111 */     if (!level.isClientSide() && ((Boolean)state.getValue(POWERED)).booleanValue() && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 112 */       BlockState newState = (BlockState)state.setValue(POWERED, Boolean.valueOf(false));
/*     */       
/* 114 */       level.setBlock(pos, newState, 18);
/* 115 */       updateNeighborsInFront(level, pos, newState);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 121 */     if (((Boolean)state.getValue(POWERED)).booleanValue() && level.getBlockTicks().hasScheduledTick(pos, this))
/*     */     {
/* 123 */       updateNeighborsInFront(level, pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(false)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ObserverBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */