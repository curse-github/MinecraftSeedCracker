/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.Tilt;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BigDripleafBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, BonemealableBlock {
/*  47 */   public static final MapCodec<BigDripleafBlock> CODEC = simpleCodec(BigDripleafBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  51 */   public MapCodec<BigDripleafBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  54 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  55 */   private static final EnumProperty<Tilt> TILT = BlockStateProperties.TILT;
/*     */   
/*     */   private static final int NO_TICK = -1;
/*     */   
/*  59 */   private static final Object2IntMap<Tilt> DELAY_UNTIL_NEXT_TILT_STATE = (Object2IntMap)Util.make(new Object2IntArrayMap(), map -> {
/*  60 */         map.defaultReturnValue(-1);
/*  61 */         map.put(Tilt.UNSTABLE, 10);
/*  62 */         map.put(Tilt.PARTIAL, 10);
/*  63 */         map.put(Tilt.FULL, 100);
/*     */       });
/*     */   
/*     */   private static final int MAX_GEN_HEIGHT = 5;
/*     */   
/*     */   private static final int ENTITY_DETECTION_MIN_Y = 11;
/*     */   
/*     */   private static final int LOWEST_LEAF_TOP = 13;
/*  71 */   private static final Map<Tilt, VoxelShape> SHAPE_LEAF = Maps.newEnumMap(Map.of(Tilt.NONE, 
/*     */         
/*  73 */         Block.column(16.0D, 11.0D, 15.0D), Tilt.UNSTABLE, 
/*  74 */         Block.column(16.0D, 11.0D, 15.0D), Tilt.PARTIAL, 
/*  75 */         Block.column(16.0D, 11.0D, 13.0D), Tilt.FULL, 
/*  76 */         Shapes.empty()));
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */ 
/*     */   
/*     */   protected BigDripleafBlock(BlockBehaviour.Properties properties) {
/*  82 */     super(properties);
/*  83 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  84 */         .setValue(WATERLOGGED, Boolean.valueOf(false)))
/*  85 */         .setValue(FACING, Direction.NORTH))
/*  86 */         .setValue(TILT, Tilt.NONE));
/*     */     
/*  88 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  92 */     Map<Direction, VoxelShape> stems = Shapes.rotateHorizontal(Block.column(6.0D, 0.0D, 13.0D).move(0.0D, 0.0D, 0.25D).optimize());
/*     */     
/*  94 */     return getShapeForEachState(state -> Shapes.or((VoxelShape)SHAPE_LEAF
/*  95 */           .get(state.getValue(TILT)), (VoxelShape)stems
/*  96 */           .get(state.getValue(FACING))), new Property[] { WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void placeWithRandomHeight(LevelAccessor level, RandomSource random, BlockPos stemBottomPos, Direction facing) {
/* 101 */     int desiredHeight = Mth.nextInt(random, 2, 5);
/*     */     
/* 103 */     BlockPos.MutableBlockPos pos = stemBottomPos.mutable();
/*     */ 
/*     */     
/* 106 */     int height = 0;
/* 107 */     while (height < desiredHeight && canPlaceAt(level, pos, level.getBlockState(pos))) {
/* 108 */       height++;
/* 109 */       pos.move(Direction.UP);
/*     */     } 
/* 111 */     int leafY = stemBottomPos.getY() + height - 1;
/*     */ 
/*     */     
/* 114 */     pos.setY(stemBottomPos.getY());
/* 115 */     while (pos.getY() < leafY) {
/* 116 */       BigDripleafStemBlock.place(level, pos, level.getFluidState(pos), facing);
/* 117 */       pos.move(Direction.UP);
/*     */     } 
/*     */ 
/*     */     
/* 121 */     place(level, pos, level.getFluidState(pos), facing);
/*     */   }
/*     */ 
/*     */   
/* 125 */   private static boolean canReplace(BlockState oldState) { return (oldState.isAir() || oldState.is(Blocks.WATER) || oldState.is(Blocks.SMALL_DRIPLEAF)); }
/*     */ 
/*     */ 
/*     */   
/* 129 */   protected static boolean canPlaceAt(LevelHeightAccessor level, BlockPos pos, BlockState oldState) { return (!level.isOutsideBuildHeight(pos) && canReplace(oldState)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean place(LevelAccessor level, BlockPos pos, FluidState fluidState, Direction facing) {
/* 135 */     BlockState newState = (BlockState)((BlockState)Blocks.BIG_DRIPLEAF.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(fluidState.isSourceOfType(Fluids.WATER)))).setValue(FACING, facing);
/* 136 */     return level.setBlock(pos, newState, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected void onProjectileHit(Level level, BlockState state, BlockHitResult blockHit, Projectile projectile) { setTiltAndScheduleTick(state, level, blockHit.getBlockPos(), Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 146 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 147 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 149 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 154 */     BlockPos belowPos = pos.below();
/* 155 */     BlockState belowState = level.getBlockState(belowPos);
/* 156 */     return (belowState.is(this) || belowState.is(Blocks.BIG_DRIPLEAF_STEM) || belowState.is(BlockTags.BIG_DRIPLEAF_PLACEABLE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 161 */     if (directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/* 162 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/* 164 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 165 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/* 168 */     if (directionToNeighbour == Direction.UP && neighbourState.is(this)) {
/* 169 */       return Blocks.BIG_DRIPLEAF_STEM.withPropertiesOf(state);
/*     */     }
/*     */     
/* 172 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 177 */     BlockState aboveState = level.getBlockState(pos.above());
/* 178 */     return canReplace(aboveState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 183 */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 188 */     BlockPos abovePos = pos.above();
/* 189 */     BlockState aboveState = level.getBlockState(abovePos);
/* 190 */     if (canPlaceAt(level, abovePos, aboveState)) {
/* 191 */       Direction facing = (Direction)state.getValue(FACING);
/* 192 */       BigDripleafStemBlock.place(level, pos, state.getFluidState(), facing);
/* 193 */       place(level, abovePos, aboveState.getFluidState(), facing);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
/* 199 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 203 */     if (state.getValue(TILT) == Tilt.NONE && canEntityTilt(pos, entity) && !level.hasNeighborSignal(pos)) {
/* 204 */       setTiltAndScheduleTick(state, level, pos, Tilt.UNSTABLE, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 210 */     if (level.hasNeighborSignal(pos)) {
/* 211 */       resetTilt(state, level, pos);
/*     */       
/*     */       return;
/*     */     } 
/* 215 */     Tilt tilt = (Tilt)state.getValue(TILT);
/*     */     
/* 217 */     if (tilt == Tilt.UNSTABLE) {
/* 218 */       setTiltAndScheduleTick(state, level, pos, Tilt.PARTIAL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
/* 219 */     } else if (tilt == Tilt.PARTIAL) {
/* 220 */       setTiltAndScheduleTick(state, level, pos, Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN);
/* 221 */     } else if (tilt == Tilt.FULL) {
/* 222 */       resetTilt(state, level, pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 228 */     if (level.hasNeighborSignal(pos)) {
/* 229 */       resetTilt(state, level, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void playTiltSound(Level level, BlockPos pos, SoundEvent tiltSound) {
/* 234 */     float pitch = Mth.randomBetween(level.random, 0.8F, 1.2F);
/* 235 */     level.playSound(null, pos, tiltSound, SoundSource.BLOCKS, 1.0F, pitch);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 240 */   private static boolean canEntityTilt(BlockPos pos, Entity entity) { return (entity.onGround() && (entity.position()).y > (pos.getY() + 0.6875F)); }
/*     */ 
/*     */   
/*     */   private void setTiltAndScheduleTick(BlockState state, Level level, BlockPos pos, Tilt tilt, SoundEvent sound) {
/* 244 */     setTilt(state, level, pos, tilt);
/* 245 */     if (sound != null) {
/* 246 */       playTiltSound(level, pos, sound);
/*     */     }
/* 248 */     int tickDelay = DELAY_UNTIL_NEXT_TILT_STATE.getInt(tilt);
/* 249 */     if (tickDelay != -1) {
/* 250 */       level.scheduleTick(pos, this, tickDelay);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void resetTilt(BlockState state, Level level, BlockPos pos) {
/* 255 */     setTilt(state, level, pos, Tilt.NONE);
/* 256 */     if (state.getValue(TILT) != Tilt.NONE) {
/* 257 */       playTiltSound(level, pos, SoundEvents.BIG_DRIPLEAF_TILT_UP);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setTilt(BlockState state, Level level, BlockPos pos, Tilt tilt) {
/* 262 */     Tilt previousTilt = (Tilt)state.getValue(TILT);
/* 263 */     level.setBlock(pos, (BlockState)state.setValue(TILT, tilt), 2);
/* 264 */     if (tilt.causesVibration() && tilt != previousTilt) {
/* 265 */       level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 271 */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPE_LEAF.get(state.getValue(TILT)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 276 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)this.shapes.apply(state); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 281 */     BlockState belowState = context.getLevel().getBlockState(context.getClickedPos().below());
/* 282 */     FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 283 */     boolean belowIsDripleafPart = (belowState.is(Blocks.BIG_DRIPLEAF) || belowState.is(Blocks.BIG_DRIPLEAF_STEM));
/*     */     
/* 285 */     return (BlockState)((BlockState)defaultBlockState()
/* 286 */       .setValue(WATERLOGGED, Boolean.valueOf(fluidState.isSourceOfType(Fluids.WATER))))
/* 287 */       .setValue(FACING, belowIsDripleafPart ? (Direction)belowState.getValue(FACING) : context.getHorizontalDirection().getOpposite());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 292 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { WATERLOGGED, FACING, TILT }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\BigDripleafBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */