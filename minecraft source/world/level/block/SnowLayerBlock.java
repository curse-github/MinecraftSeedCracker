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
/*     */ public class SnowLayerBlock extends Block {
/*  24 */   public static final MapCodec<SnowLayerBlock> CODEC = simpleCodec(SnowLayerBlock::new);
/*     */   
/*     */   public static final int MAX_HEIGHT = 8;
/*     */   
/*  28 */   public MapCodec<SnowLayerBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
/*     */   
/*  34 */   private static final VoxelShape[] SHAPES = Block.boxes(8, height -> Block.column(16.0D, 0.0D, (height * 2)));
/*     */   
/*     */   public static final int HEIGHT_IMPASSABLE = 5;
/*     */   
/*     */   protected SnowLayerBlock(BlockBehaviour.Properties properties) {
/*  39 */     super(properties);
/*  40 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(LAYERS, Integer.valueOf(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/*  45 */     if (type == PathComputationType.LAND) {
/*  46 */       return (((Integer)state.getValue(LAYERS)).intValue() < 5);
/*     */     }
/*  48 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  53 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[((Integer)state.getValue(LAYERS)).intValue()]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[((Integer)state.getValue(LAYERS)).intValue() - 1]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) { return SHAPES[((Integer)state.getValue(LAYERS)).intValue()]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPES[((Integer)state.getValue(LAYERS)).intValue()]; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected boolean useShapeForLightOcclusion(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) { return (((Integer)state.getValue(LAYERS)).intValue() == 8) ? 0.2F : 1.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  83 */     BlockState belowState = level.getBlockState(pos.below());
/*     */     
/*  85 */     if (belowState.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
/*  86 */       return false;
/*     */     }
/*  88 */     if (belowState.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON)) {
/*  89 */       return true;
/*     */     }
/*     */     
/*  92 */     return (Block.isFaceFull(belowState.getCollisionShape(level, pos.below()), Direction.UP) || (belowState.is(this) && ((Integer)belowState.getValue(LAYERS)).intValue() == 8));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  97 */     if (!state.canSurvive(level, pos)) {
/*  98 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 100 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 105 */     if (level.getBrightness(LightLayer.BLOCK, pos) > 11) {
/* 106 */       dropResources(state, level, pos);
/* 107 */       level.removeBlock(pos, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 113 */     int layers = ((Integer)state.getValue(LAYERS)).intValue();
/*     */     
/* 115 */     if (context.getItemInHand().is(asItem()) && layers < 8) {
/* 116 */       if (context.replacingClickedOnBlock()) {
/* 117 */         return (context.getClickedFace() == Direction.UP);
/*     */       }
/* 119 */       return true;
/*     */     } 
/*     */     
/* 122 */     return (layers == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 127 */     BlockState state = context.getLevel().getBlockState(context.getClickedPos());
/* 128 */     if (state.is(this)) {
/* 129 */       int layers = ((Integer)state.getValue(LAYERS)).intValue();
/* 130 */       return (BlockState)state.setValue(LAYERS, Integer.valueOf(Math.min(8, layers + 1)));
/*     */     } 
/*     */     
/* 133 */     return super.getStateForPlacement(context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 138 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { LAYERS }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SnowLayerBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */