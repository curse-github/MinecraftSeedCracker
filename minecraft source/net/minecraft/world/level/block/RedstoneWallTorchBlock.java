/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class RedstoneWallTorchBlock extends RedstoneTorchBlock {
/*  25 */   public static final MapCodec<RedstoneWallTorchBlock> CODEC = simpleCodec(RedstoneWallTorchBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  29 */   public MapCodec<RedstoneWallTorchBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  32 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  33 */   public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
/*     */   
/*     */   protected RedstoneWallTorchBlock(BlockBehaviour.Properties properties) {
/*  36 */     super(properties);
/*  37 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(LIT, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  42 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return WallTorchBlock.getShape(state); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) { return WallTorchBlock.canSurvive(level, pos, (Direction)state.getValue(FACING)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  52 */     if (directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
/*  53 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  55 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  60 */     BlockState state = Blocks.WALL_TORCH.getStateForPlacement(context);
/*  61 */     return (state == null) ? null : (BlockState)defaultBlockState().setValue(FACING, (Direction)state.getValue(FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/*  66 */     if (!((Boolean)state.getValue(LIT)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  70 */     Direction opposite = ((Direction)state.getValue(FACING)).getOpposite();
/*  71 */     double r = 0.27D;
/*  72 */     double x = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D + 0.27D * opposite.getStepX();
/*  73 */     double y = pos.getY() + 0.7D + (random.nextDouble() - 0.5D) * 0.2D + 0.22D;
/*  74 */     double z = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D + 0.27D * opposite.getStepZ();
/*     */     
/*  76 */     level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
/*  81 */     Direction opposite = ((Direction)state.getValue(FACING)).getOpposite();
/*     */     
/*  83 */     return level.hasSignal(pos.relative(opposite), opposite);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  88 */     if (((Boolean)state.getValue(LIT)).booleanValue() && state.getValue(FACING) != direction) {
/*  89 */       return 15;
/*     */     }
/*     */     
/*  92 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, LIT }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected Orientation randomOrientation(Level level, BlockState state) { return ExperimentalRedstoneUtils.initialOrientation(level, ((Direction)state.getValue(FACING)).getOpposite(), Direction.UP); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\RedstoneWallTorchBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */