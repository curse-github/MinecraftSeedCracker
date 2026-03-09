/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.SlabType;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SlabBlock extends Block implements SimpleWaterloggedBlock {
/*  31 */   public static final MapCodec<SlabBlock> CODEC = simpleCodec(SlabBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  35 */   public MapCodec<? extends SlabBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  38 */   public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
/*  39 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  41 */   private static final VoxelShape SHAPE_BOTTOM = Block.column(16.0D, 0.0D, 8.0D);
/*  42 */   private static final VoxelShape SHAPE_TOP = Block.column(16.0D, 8.0D, 16.0D);
/*     */   
/*     */   public SlabBlock(BlockBehaviour.Properties properties) {
/*  45 */     super(properties);
/*     */     
/*  47 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue(TYPE, SlabType.BOTTOM)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  52 */   protected boolean useShapeForLightOcclusion(BlockState state) { return (state.getValue(TYPE) != SlabType.DOUBLE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { TYPE, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  62 */     switch ((SlabType)state.getValue(TYPE)) { default: throw new MatchException(null, null);case LAND: case WATER: case AIR: break; }  return 
/*     */ 
/*     */       
/*  65 */       Shapes.block();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  71 */     BlockPos pos = context.getClickedPos();
/*  72 */     BlockState replacedBlockState = context.getLevel().getBlockState(pos);
/*  73 */     if (replacedBlockState.is(this)) {
/*  74 */       return (BlockState)((BlockState)replacedBlockState.setValue(TYPE, SlabType.DOUBLE)).setValue(WATERLOGGED, Boolean.valueOf(false));
/*     */     }
/*     */     
/*  77 */     FluidState replacedFluidState = context.getLevel().getFluidState(pos);
/*  78 */     BlockState result = (BlockState)((BlockState)defaultBlockState().setValue(TYPE, SlabType.BOTTOM)).setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */     
/*  80 */     Direction clickedFace = context.getClickedFace();
/*  81 */     if (clickedFace == Direction.DOWN || (clickedFace != Direction.UP && (context.getClickLocation()).y - pos.getY() > 0.5D)) {
/*  82 */       return (BlockState)result.setValue(TYPE, SlabType.TOP);
/*     */     }
/*  84 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/*  89 */     ItemStack itemStack = context.getItemInHand();
/*     */     
/*  91 */     SlabType type = (SlabType)state.getValue(TYPE);
/*  92 */     if (type == SlabType.DOUBLE || !itemStack.is(asItem())) {
/*  93 */       return false;
/*     */     }
/*     */     
/*  96 */     if (context.replacingClickedOnBlock()) {
/*  97 */       boolean above = ((context.getClickLocation()).y - context.getClickedPos().getY() > 0.5D);
/*  98 */       Direction clickedFace = context.getClickedFace();
/*  99 */       if (type == SlabType.BOTTOM) {
/* 100 */         return (clickedFace == Direction.UP || (above && clickedFace.getAxis().isHorizontal()));
/*     */       }
/* 102 */       return (clickedFace == Direction.DOWN || (!above && clickedFace.getAxis().isHorizontal()));
/*     */     } 
/*     */     
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 110 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 111 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 113 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
/* 118 */     if (state.getValue(TYPE) != SlabType.DOUBLE) {
/* 119 */       return super.placeLiquid(level, pos, state, fluidState);
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceLiquid(LivingEntity user, BlockGetter level, BlockPos pos, BlockState state, Fluid type) {
/* 126 */     if (state.getValue(TYPE) != SlabType.DOUBLE) {
/* 127 */       return super.canPlaceLiquid(user, level, pos, state, type);
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 134 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 135 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 137 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 142 */     switch (type) {
/*     */       case LAND:
/* 144 */         return false;
/*     */       case WATER:
/* 146 */         return state.getFluidState().is(FluidTags.WATER);
/*     */       case AIR:
/* 148 */         return false;
/*     */     } 
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SlabBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */