/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPattern;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
/*     */ import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EndPortalFrameBlock extends Block {
/*  27 */   public static final MapCodec<EndPortalFrameBlock> CODEC = simpleCodec(EndPortalFrameBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  31 */   public MapCodec<EndPortalFrameBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  34 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  35 */   public static final BooleanProperty HAS_EYE = BlockStateProperties.EYE;
/*     */   
/*  37 */   private static final VoxelShape SHAPE_EMPTY = Block.column(16.0D, 0.0D, 13.0D);
/*  38 */   private static final VoxelShape SHAPE_FULL = Shapes.or(SHAPE_EMPTY, 
/*     */       
/*  40 */       Block.column(8.0D, 13.0D, 16.0D));
/*     */   
/*     */   private static BlockPattern portalShape;
/*     */ 
/*     */   
/*     */   public EndPortalFrameBlock(BlockBehaviour.Properties properties) {
/*  46 */     super(properties);
/*  47 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(HAS_EYE, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  52 */   protected boolean useShapeForLightOcclusion(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return ((Boolean)state.getValue(HAS_EYE)).booleanValue() ? SHAPE_FULL : SHAPE_EMPTY; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)((BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())).setValue(HAS_EYE, Boolean.valueOf(false)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/*  72 */     if (((Boolean)state.getValue(HAS_EYE)).booleanValue()) {
/*  73 */       return 15;
/*     */     }
/*     */     
/*  76 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, HAS_EYE }); }
/*     */ 
/*     */   
/*     */   public static BlockPattern getOrCreatePortalShape() {
/*  95 */     if (portalShape == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 109 */       portalShape = BlockPatternBuilder.start().aisle(new String[] { "?vvv?", ">???<", ">???<", ">???<", "?^^^?" }).where('?', BlockInWorld.hasState(BlockStatePredicate.ANY)).where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where(HAS_EYE, Predicates.equalTo(Boolean.valueOf(true))).where(FACING, Predicates.equalTo(Direction.SOUTH)))).where('>', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where(HAS_EYE, Predicates.equalTo(Boolean.valueOf(true))).where(FACING, Predicates.equalTo(Direction.WEST)))).where('v', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where(HAS_EYE, Predicates.equalTo(Boolean.valueOf(true))).where(FACING, Predicates.equalTo(Direction.NORTH)))).where('<', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where(HAS_EYE, Predicates.equalTo(Boolean.valueOf(true))).where(FACING, Predicates.equalTo(Direction.EAST)))).build();
/*     */     }
/* 111 */     return portalShape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 116 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\EndPortalFrameBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */