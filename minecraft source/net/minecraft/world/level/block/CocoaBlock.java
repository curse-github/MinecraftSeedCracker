/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
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
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class CocoaBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
/*  29 */   public static final MapCodec<CocoaBlock> CODEC = simpleCodec(CocoaBlock::new);
/*     */   
/*     */   public static final int MAX_AGE = 2;
/*     */   
/*  33 */   public MapCodec<CocoaBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */   
/*  37 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
/*     */   
/*  39 */   private static final List<Map<Direction, VoxelShape>> SHAPES = IntStream.rangeClosed(0, 2).mapToObj(i -> 
/*  40 */       Shapes.rotateHorizontal(Block.column((4 + i * 2), (7 - i * 2), 12.0D).move(0.0D, 0.0D, (i - 5) / 16.0D).optimize()))
/*  41 */     .toList();
/*     */   
/*     */   public CocoaBlock(BlockBehaviour.Properties properties) {
/*  44 */     super(properties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  50 */   protected boolean isRandomlyTicking(BlockState state) { return (((Integer)state.getValue(AGE)).intValue() < 2); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  55 */     if (level.random.nextInt(5) == 0) {
/*  56 */       int age = ((Integer)state.getValue(AGE)).intValue();
/*  57 */       if (age < 2) {
/*  58 */         level.setBlock(pos, (BlockState)state.setValue(AGE, Integer.valueOf(age + 1)), 2);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  65 */     BlockState relativeState = level.getBlockState(pos.relative((Direction)state.getValue(FACING)));
/*  66 */     return relativeState.is(BlockTags.JUNGLE_LOGS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)((Map)SHAPES.get(((Integer)state.getValue(AGE)).intValue())).get(state.getValue(FACING)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  76 */     BlockState state = defaultBlockState();
/*     */     
/*  78 */     Level level1 = context.getLevel();
/*  79 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  81 */     for (Direction direction : context.getNearestLookingDirections()) {
/*  82 */       if (direction.getAxis().isHorizontal()) {
/*  83 */         state = (BlockState)state.setValue(FACING, direction);
/*  84 */         if (state.canSurvive(level1, pos)) {
/*  85 */           return state;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  95 */     if (directionToNeighbour == state.getValue(FACING) && !state.canSurvive(level, pos)) {
/*  96 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return (((Integer)state.getValue(AGE)).intValue() < 2); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) { level.setBlock(pos, (BlockState)state.setValue(AGE, Integer.valueOf(((Integer)state.getValue(AGE)).intValue() + 1)), 2); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, AGE }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\CocoaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */