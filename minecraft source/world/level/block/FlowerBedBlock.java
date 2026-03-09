/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FlowerBedBlock extends VegetationBlock implements BonemealableBlock, SegmentableBlock {
/*  24 */   public static final MapCodec<FlowerBedBlock> CODEC = simpleCodec(FlowerBedBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  28 */   public MapCodec<FlowerBedBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  31 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  32 */   public static final IntegerProperty AMOUNT = BlockStateProperties.FLOWER_AMOUNT;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected FlowerBedBlock(BlockBehaviour.Properties properties) {
/*  37 */     super(properties);
/*  38 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(AMOUNT, Integer.valueOf(1)));
/*     */     
/*  40 */     this.shapes = makeShapes();
/*     */   }
/*     */ 
/*     */   
/*  44 */   private Function<BlockState, VoxelShape> makeShapes() { return getShapeForEachState(getShapeCalculator(FACING, AMOUNT)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/*  59 */     if (canBeReplaced(state, context, AMOUNT)) {
/*  60 */       return true;
/*     */     }
/*  62 */     return super.canBeReplaced(state, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public double getShapeHeight() { return 3.0D; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public IntegerProperty getSegmentAmountProperty() { return AMOUNT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return getStateForPlacement(context, this, AMOUNT, FACING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, AMOUNT }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 102 */     int currentAmount = ((Integer)state.getValue(AMOUNT)).intValue();
/* 103 */     if (currentAmount < 4) {
/* 104 */       level.setBlock(pos, (BlockState)state.setValue(AMOUNT, Integer.valueOf(currentAmount + 1)), 2);
/*     */     } else {
/* 106 */       popResource(level, pos, new ItemStack(this));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\FlowerBedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */