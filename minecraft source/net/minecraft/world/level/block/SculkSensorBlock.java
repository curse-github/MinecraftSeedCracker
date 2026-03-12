/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustColorTransitionOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SculkSensorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/*  45 */   public static final MapCodec<SculkSensorBlock> CODEC = simpleCodec(SculkSensorBlock::new);
/*     */   public static final int ACTIVE_TICKS = 30;
/*     */   public static final int COOLDOWN_TICKS = 10;
/*     */   
/*  49 */   public MapCodec<? extends SculkSensorBlock> codec() { return CODEC; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final EnumProperty<SculkSensorPhase> PHASE = BlockStateProperties.SCULK_SENSOR_PHASE;
/*  56 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*  57 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  59 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 8.0D);
/*     */   
/*  61 */   private static final float[] RESONANCE_PITCH_BEND = (float[])Util.make(new float[16], arr -> {
/*  62 */         int[] toneMap = { 0, 0, 2, 4, 6, 7, 9, 10, 12, 14, 15, 18, 19, 21, 22, 24 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  69 */         for (int i = 0; i < 16; i++) {
/*  70 */           arr[i] = NoteBlock.getPitchFromNote(toneMap[i]);
/*     */         }
/*     */       });
/*     */   
/*     */   public SculkSensorBlock(BlockBehaviour.Properties properties) {
/*  75 */     super(properties);
/*  76 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(PHASE, SculkSensorPhase.INACTIVE)).setValue(POWER, Integer.valueOf(0))).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  81 */     BlockPos pos = context.getClickedPos();
/*  82 */     FluidState replacedFluidState = context.getLevel().getFluidState(pos);
/*     */     
/*  84 */     return (BlockState)defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/*  89 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/*  90 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  92 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  97 */     if (getPhase(state) != SculkSensorPhase.ACTIVE) {
/*  98 */       if (getPhase(state) == SculkSensorPhase.COOLDOWN) {
/*  99 */         level.setBlock(pos, (BlockState)state.setValue(PHASE, SculkSensorPhase.INACTIVE), 3);
/* 100 */         if (!((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 101 */           level.playSound(null, pos, SoundEvents.SCULK_CLICKING_STOP, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 108 */     deactivate(level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
/* 113 */     if (!level.isClientSide() && canActivate(onState) && entity.getType() != EntityType.WARDEN) {
/* 114 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 115 */       if (blockEntity instanceof SculkSensorBlockEntity) { SculkSensorBlockEntity sculkSensor = (SculkSensorBlockEntity)blockEntity; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 116 */           if (sculkSensor.getVibrationUser().canReceiveVibration(serverLevel, pos, GameEvent.STEP, GameEvent.Context.of(onState)))
/* 117 */             sculkSensor.getListener().forceScheduleVibration(serverLevel, GameEvent.STEP, GameEvent.Context.of(entity), entity.position());  }
/*     */          }
/*     */     
/*     */     } 
/* 121 */     super.stepOn(level, pos, onState, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 126 */     if (level.isClientSide() || state.is(oldState.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     if (((Integer)state.getValue(POWER)).intValue() > 0 && !level.getBlockTicks().hasScheduledTick(pos, this)) {
/* 131 */       level.setBlock(pos, (BlockState)state.setValue(POWER, Integer.valueOf(0)), 18);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 137 */     if (getPhase(state) == SculkSensorPhase.ACTIVE) {
/* 138 */       updateNeighbours(level, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 144 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 145 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 147 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */   
/*     */   private static void updateNeighbours(Level level, BlockPos pos, BlockState state) {
/* 151 */     Block block = state.getBlock();
/* 152 */     level.updateNeighborsAt(pos, block);
/* 153 */     level.updateNeighborsAt(pos.below(), block);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new SculkSensorBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 163 */     if (!level.isClientSide()) {
/* 164 */       return createTickerHelper(type, BlockEntityType.SCULK_SENSOR, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser()));
/*     */     }
/*     */     
/* 167 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 172 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return ((Integer)state.getValue(POWER)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 187 */     if (direction == Direction.UP) {
/* 188 */       return state.getSignal(level, pos, direction);
/*     */     }
/*     */     
/* 191 */     return 0;
/*     */   }
/*     */ 
/*     */   
/* 195 */   public static SculkSensorPhase getPhase(BlockState state) { return (SculkSensorPhase)state.getValue(PHASE); }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public static boolean canActivate(BlockState state) { return (getPhase(state) == SculkSensorPhase.INACTIVE); }
/*     */ 
/*     */   
/*     */   public static void deactivate(Level level, BlockPos pos, BlockState state) {
/* 203 */     level.setBlock(pos, (BlockState)((BlockState)state.setValue(PHASE, SculkSensorPhase.COOLDOWN)).setValue(POWER, Integer.valueOf(0)), 3);
/* 204 */     level.scheduleTick(pos, state.getBlock(), 10);
/* 205 */     updateNeighbours(level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 210 */   public int getActiveTicks() { return 30; }
/*     */ 
/*     */   
/*     */   public void activate(Entity sourceEntity, Level level, BlockPos pos, BlockState state, int calculatedPower, int vibrationFrequency) {
/* 214 */     level.setBlock(pos, (BlockState)((BlockState)state.setValue(PHASE, SculkSensorPhase.ACTIVE)).setValue(POWER, Integer.valueOf(calculatedPower)), 3);
/*     */     
/* 216 */     level.scheduleTick(pos, state.getBlock(), getActiveTicks());
/* 217 */     updateNeighbours(level, pos, state);
/* 218 */     tryResonateVibration(sourceEntity, level, pos, vibrationFrequency);
/* 219 */     level.gameEvent(sourceEntity, GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos);
/* 220 */     if (!((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 221 */       level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void tryResonateVibration(Entity sourceEntity, Level level, BlockPos pos, int vibrationFrequency) {
/* 226 */     for (Direction direction : Direction.values()) {
/* 227 */       BlockPos relativePos = pos.relative(direction);
/* 228 */       BlockState blockState = level.getBlockState(relativePos);
/* 229 */       if (blockState.is(BlockTags.VIBRATION_RESONATORS)) {
/* 230 */         level.gameEvent(VibrationSystem.getResonanceEventByFrequency(vibrationFrequency), relativePos, GameEvent.Context.of(sourceEntity, blockState));
/* 231 */         float pitch = RESONANCE_PITCH_BEND[vibrationFrequency];
/* 232 */         level.playSound(null, relativePos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, pitch);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 239 */     if (getPhase(state) != SculkSensorPhase.ACTIVE) {
/*     */       return;
/*     */     }
/*     */     
/* 243 */     Direction dir = Direction.getRandom(random);
/*     */     
/* 245 */     if (dir == Direction.UP || dir == Direction.DOWN) {
/*     */       return;
/*     */     }
/*     */     
/* 249 */     double x = pos.getX() + 0.5D + ((dir.getStepX() == 0) ? (0.5D - random.nextDouble()) : (dir.getStepX() * 0.6D));
/* 250 */     double y = pos.getY() + 0.25D;
/* 251 */     double z = pos.getZ() + 0.5D + ((dir.getStepZ() == 0) ? (0.5D - random.nextDouble()) : (dir.getStepZ() * 0.6D));
/* 252 */     double ya = random.nextFloat() * 0.04D;
/* 253 */     level.addParticle(DustColorTransitionOptions.SCULK_TO_REDSTONE, x, y, z, 0.0D, ya, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 258 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { PHASE, POWER, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 268 */     BlockEntity entity = level.getBlockEntity(pos);
/*     */     
/* 270 */     if (entity instanceof SculkSensorBlockEntity) { SculkSensorBlockEntity sculk = (SculkSensorBlockEntity)entity;
/* 271 */       return (getPhase(state) == SculkSensorPhase.ACTIVE) ? sculk.getLastVibrationFrequency() : 0; }
/*     */ 
/*     */     
/* 274 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 279 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 284 */   protected boolean useShapeForLightOcclusion(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
/* 289 */     super.spawnAfterBreak(state, level, pos, tool, dropExperience);
/* 290 */     if (dropExperience)
/* 291 */       tryDropExperience(level, pos, tool, ConstantInt.of(5)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SculkSensorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */