/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ParticleUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
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
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ 
/*     */ public class LightningRodBlock extends RodBlock implements SimpleWaterloggedBlock {
/*  28 */   public static final MapCodec<LightningRodBlock> CODEC = simpleCodec(LightningRodBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  32 */   public MapCodec<? extends LightningRodBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  35 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  36 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   private static final int ACTIVATION_TICKS = 8;
/*     */   public static final int RANGE = 128;
/*     */   private static final int SPARK_CYCLE = 200;
/*     */   
/*     */   public LightningRodBlock(BlockBehaviour.Properties properties) {
/*  42 */     super(properties);
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.UP)).setValue(WATERLOGGED, Boolean.valueOf(false))).setValue(POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  48 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  49 */     boolean isWaterSource = (replacedFluidState.getType() == Fluids.WATER);
/*  50 */     return (BlockState)((BlockState)defaultBlockState().setValue(FACING, context.getClickedFace())).setValue(WATERLOGGED, Boolean.valueOf(isWaterSource));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  55 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  56 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  58 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  63 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  64 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  66 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  76 */     if (((Boolean)state.getValue(POWERED)).booleanValue() && state.getValue(FACING) == direction) {
/*  77 */       return 15;
/*     */     }
/*  79 */     return 0;
/*     */   }
/*     */   
/*     */   public void onLightningStrike(BlockState state, Level level, BlockPos pos) {
/*  83 */     level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(true)), 3);
/*  84 */     updateNeighbours(state, level, pos);
/*  85 */     level.scheduleTick(pos, this, 8);
/*     */     
/*  87 */     level.levelEvent(3002, pos, ((Direction)state.getValue(FACING)).getAxis().ordinal());
/*     */   }
/*     */   
/*     */   private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
/*  91 */     Direction front = ((Direction)state.getValue(FACING)).getOpposite();
/*  92 */     level.updateNeighborsAt(pos.relative(front), this, ExperimentalRedstoneUtils.initialOrientation(level, front, null));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  97 */     level.setBlock(pos, (BlockState)state.setValue(POWERED, Boolean.valueOf(false)), 3);
/*  98 */     updateNeighbours(state, level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 103 */     if (!level.isThundering() || level.random
/* 104 */       .nextInt(200) > level.getGameTime() % 200L || pos
/* 105 */       .getY() != level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()) - 1) {
/*     */       return;
/*     */     }
/*     */     
/* 109 */     ParticleUtils.spawnParticlesAlongAxis(((Direction)state.getValue(FACING)).getAxis(), level, pos, 0.125D, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 114 */     if (((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 115 */       updateNeighbours(state, level, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 121 */     if (state.is(oldState.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 125 */     if (((Boolean)state.getValue(POWERED)).booleanValue() && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 126 */       level.scheduleTick(pos, this, 8);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, POWERED, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LightningRodBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */