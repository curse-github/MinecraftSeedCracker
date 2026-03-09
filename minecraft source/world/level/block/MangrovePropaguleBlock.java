/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.grower.TreeGrower;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MangrovePropaguleBlock extends SaplingBlock implements SimpleWaterloggedBlock {
/*  27 */   public static final MapCodec<MangrovePropaguleBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TreeGrower.CODEC
/*  28 */         .fieldOf("tree").forGetter(()), 
/*  29 */         propertiesCodec())
/*  30 */       .apply(i, MangrovePropaguleBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  34 */   public MapCodec<MangrovePropaguleBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  37 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
/*     */   
/*     */   public static final int MAX_AGE = 4;
/*  40 */   private static final int[] SHAPE_MIN_Y = { 13, 10, 7, 3, 0 };
/*  41 */   private static final VoxelShape[] SHAPE_PER_AGE = Block.boxes(4, age -> Block.column(2.0D, SHAPE_MIN_Y[age], 16.0D));
/*     */   
/*  43 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  44 */   public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
/*     */   
/*     */   public MangrovePropaguleBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
/*  47 */     super(treeGrower, properties);
/*  48 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  49 */         .setValue(STAGE, Integer.valueOf(0)))
/*  50 */         .setValue(AGE, Integer.valueOf(0)))
/*  51 */         .setValue(WATERLOGGED, Boolean.valueOf(false)))
/*  52 */         .setValue(HANGING, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { STAGE }).add(new Property[] { AGE }).add(new Property[] { WATERLOGGED }).add(new Property[] { HANGING }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) { return (super.mayPlaceOn(state, level, pos) || state.is(Blocks.CLAY)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  68 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  69 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/*  70 */     return (BlockState)((BlockState)super.getStateForPlacement(context).setValue(WATERLOGGED, Boolean.valueOf(isWaterSource))).setValue(AGE, Integer.valueOf(4));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  75 */     int age = ((Boolean)state.getValue(HANGING)).booleanValue() ? ((Integer)state.getValue(AGE)).intValue() : 4;
/*  76 */     return SHAPE_PER_AGE[age].move(state.getOffset(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  81 */     if (isHanging(state)) {
/*  82 */       return level.getBlockState(pos.above()).is(Blocks.MANGROVE_LEAVES);
/*     */     }
/*  84 */     return super.canSurvive(state, level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  89 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  90 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  92 */     if (directionToNeighbour == Direction.UP && !state.canSurvive(level, pos)) {
/*  93 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  95 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 100 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 101 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 103 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 108 */     if (!isHanging(state)) {
/*     */ 
/*     */       
/* 111 */       if (random.nextInt(7) == 0) {
/* 112 */         advanceTree(level, pos, state, random);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 117 */     if (!isFullyGrown(state)) {
/* 118 */       level.setBlock(pos, (BlockState)state.cycle(AGE), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return (!isHanging(state) || !isFullyGrown(state)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return isHanging(state) ? (!isFullyGrown(state)) : super.isBonemealSuccess(level, random, pos, state); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 134 */     if (isHanging(state) && !isFullyGrown(state)) {
/* 135 */       level.setBlock(pos, (BlockState)state.cycle(AGE), 2);
/*     */     } else {
/* 137 */       super.performBonemeal(level, random, pos, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 142 */   private static boolean isHanging(BlockState state) { return ((Boolean)state.getValue(HANGING)).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 146 */   private static boolean isFullyGrown(BlockState state) { return (((Integer)state.getValue(AGE)).intValue() == 4); }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public static BlockState createNewHangingPropagule() { return createNewHangingPropagule(0); }
/*     */ 
/*     */   
/*     */   public static BlockState createNewHangingPropagule(int age) {
/* 154 */     return (BlockState)((BlockState)Blocks.MANGROVE_PROPAGULE.defaultBlockState()
/* 155 */       .setValue(HANGING, Boolean.valueOf(true)))
/* 156 */       .setValue(AGE, Integer.valueOf(age));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\MangrovePropaguleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */