/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.ParticleUtils;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class LeavesBlock
/*     */   extends Block
/*     */   implements SimpleWaterloggedBlock
/*     */ {
/*     */   public static final int DECAY_DISTANCE = 7;
/*  35 */   public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
/*  36 */   public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
/*  37 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   protected final float leafParticleChance;
/*     */   private static final int TICK_DELAY = 1;
/*     */   private static boolean cutoutLeaves = true;
/*     */   
/*     */   public abstract MapCodec<? extends LeavesBlock> codec();
/*     */   
/*     */   public LeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) {
/*  45 */     super(properties);
/*  46 */     this.leafParticleChance = leafParticleChance;
/*  47 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(DISTANCE, Integer.valueOf(7))).setValue(PERSISTENT, Boolean.valueOf(false))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
/*  52 */     if (!cutoutLeaves && neighborState.getBlock() instanceof LeavesBlock) {
/*  53 */       return true;
/*     */     }
/*  55 */     return super.skipRendering(state, neighborState, direction);
/*     */   }
/*     */ 
/*     */   
/*  59 */   public static void setCutoutLeaves(boolean cutoutLeaves) { LeavesBlock.cutoutLeaves = cutoutLeaves; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) { return Shapes.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   protected boolean isRandomlyTicking(BlockState state) { return (((Integer)state.getValue(DISTANCE)).intValue() == 7 && !((Boolean)state.getValue(PERSISTENT)).booleanValue()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  74 */     if (decaying(state)) {
/*  75 */       dropResources(state, level, pos);
/*  76 */       level.removeBlock(pos, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  81 */   protected boolean decaying(BlockState state) { return (!((Boolean)state.getValue(PERSISTENT)).booleanValue() && ((Integer)state.getValue(DISTANCE)).intValue() == 7); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { level.setBlock(pos, updateDistance(state, level, pos), 3); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   protected int getLightBlock(BlockState state) { return 1; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  96 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  97 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*  99 */     int distanceFromNeighbor = getDistanceAt(neighbourState) + 1;
/* 100 */     if (distanceFromNeighbor != 1 || ((Integer)state.getValue(DISTANCE)).intValue() != distanceFromNeighbor) {
/* 101 */       ticks.scheduleTick(pos, this, 1);
/*     */     }
/* 103 */     return state;
/*     */   }
/*     */   
/*     */   private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
/* 107 */     int newDistance = 7;
/* 108 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 109 */     for (Direction direction : Direction.values()) {
/* 110 */       neighborPos.setWithOffset(pos, direction);
/* 111 */       newDistance = Math.min(newDistance, getDistanceAt(level.getBlockState(neighborPos)) + 1);
/* 112 */       if (newDistance == 1) {
/*     */         break;
/*     */       }
/*     */     } 
/* 116 */     return (BlockState)state.setValue(DISTANCE, Integer.valueOf(newDistance));
/*     */   }
/*     */ 
/*     */   
/* 120 */   private static int getDistanceAt(BlockState state) { return getOptionalDistanceAt(state).orElse(7); }
/*     */ 
/*     */   
/*     */   public static OptionalInt getOptionalDistanceAt(BlockState state) {
/* 124 */     if (state.is(BlockTags.LOGS)) {
/* 125 */       return OptionalInt.of(0);
/*     */     }
/* 127 */     if (state.hasProperty(DISTANCE)) {
/* 128 */       return OptionalInt.of(((Integer)state.getValue(DISTANCE)).intValue());
/*     */     }
/* 130 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 135 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 136 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 138 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 143 */     super.animateTick(state, level, pos, random);
/* 144 */     BlockPos below = pos.below();
/* 145 */     BlockState belowState = level.getBlockState(below);
/*     */     
/* 147 */     makeDrippingWaterParticles(level, pos, random, belowState, below);
/* 148 */     makeFallingLeavesParticles(level, pos, random, belowState, below);
/*     */   }
/*     */   
/*     */   private static void makeDrippingWaterParticles(Level level, BlockPos pos, RandomSource random, BlockState belowState, BlockPos below) {
/* 152 */     if (!level.isRainingAt(pos.above())) {
/*     */       return;
/*     */     }
/*     */     
/* 156 */     if (random.nextInt(15) != 1) {
/*     */       return;
/*     */     }
/*     */     
/* 160 */     if (belowState.canOcclude() && belowState.isFaceSturdy(level, below, Direction.UP)) {
/*     */       return;
/*     */     }
/*     */     
/* 164 */     ParticleUtils.spawnParticleBelow(level, pos, random, ParticleTypes.DRIPPING_WATER);
/*     */   }
/*     */   
/*     */   private void makeFallingLeavesParticles(Level level, BlockPos pos, RandomSource random, BlockState belowState, BlockPos below) {
/* 168 */     if (random.nextFloat() >= this.leafParticleChance) {
/*     */       return;
/*     */     }
/*     */     
/* 172 */     if (isFaceFull(belowState.getCollisionShape(level, below), Direction.UP)) {
/*     */       return;
/*     */     }
/*     */     
/* 176 */     spawnFallingLeavesParticle(level, pos, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void spawnFallingLeavesParticle(Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource);
/*     */ 
/*     */   
/* 183 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { DISTANCE, PERSISTENT, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 188 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 189 */     BlockState state = (BlockState)((BlockState)defaultBlockState().setValue(PERSISTENT, Boolean.valueOf(true))).setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/* 190 */     return updateDistance(state, context.getLevel(), context.getClickedPos());
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\LeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */