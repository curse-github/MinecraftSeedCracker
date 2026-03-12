/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
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
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class TripWireHookBlock extends Block {
/*  36 */   public static final MapCodec<TripWireHookBlock> CODEC = simpleCodec(TripWireHookBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  40 */   public MapCodec<TripWireHookBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  43 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  44 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  45 */   public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
/*     */   
/*     */   protected static final int WIRE_DIST_MIN = 1;
/*     */   
/*     */   protected static final int WIRE_DIST_MAX = 42;
/*     */   private static final int RECHECK_PERIOD = 10;
/*  51 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, 10.0D, 10.0D, 16.0D));
/*     */   
/*     */   public TripWireHookBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(POWERED, Boolean.valueOf(false))).setValue(ATTACHED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  60 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  65 */     Direction direction = (Direction)state.getValue(FACING);
/*  66 */     BlockPos relative = pos.relative(direction.getOpposite());
/*  67 */     BlockState blockState = level.getBlockState(relative);
/*  68 */     return (direction.getAxis().isHorizontal() && blockState.isFaceSturdy(level, relative, direction));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  73 */     if (directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos)) {
/*  74 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  76 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  81 */     BlockState state = (BlockState)((BlockState)defaultBlockState().setValue(POWERED, Boolean.valueOf(false))).setValue(ATTACHED, Boolean.valueOf(false));
/*     */     
/*  83 */     Level level1 = context.getLevel();
/*  84 */     BlockPos pos = context.getClickedPos();
/*     */     
/*  86 */     Direction[] directions = context.getNearestLookingDirections();
/*  87 */     for (Direction direction : directions) {
/*  88 */       if (direction.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  92 */         Direction facing = direction.getOpposite();
/*     */         
/*  94 */         state = (BlockState)state.setValue(FACING, facing);
/*  95 */         if (state.canSurvive(level1, pos)) {
/*  96 */           return state;
/*     */         }
/*     */       } 
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) { calculateState(level, pos, state, false, false, -1, null); }
/*     */ 
/*     */   
/*     */   public static void calculateState(Level level, BlockPos pos, BlockState state, boolean isBeingDestroyed, boolean canUpdate, int wireSource, BlockState wireSourceState) {
/* 109 */     Optional<Direction> facingOptional = state.getOptionalValue(FACING);
/* 110 */     if (!facingOptional.isPresent()) {
/*     */       return;
/*     */     }
/*     */     
/* 114 */     Direction direction = (Direction)facingOptional.get();
/* 115 */     boolean wasAttached = ((Boolean)state.getOptionalValue(ATTACHED).orElse(Boolean.valueOf(false))).booleanValue();
/* 116 */     boolean wasPowered = ((Boolean)state.getOptionalValue(POWERED).orElse(Boolean.valueOf(false))).booleanValue();
/*     */     
/* 118 */     Block block = state.getBlock();
/* 119 */     boolean attached = !isBeingDestroyed;
/* 120 */     boolean powered = false;
/* 121 */     int receiverPos = 0;
/*     */     
/* 123 */     BlockState[] wireStates = new BlockState[42];
/* 124 */     for (int i = 1; i < 42; i++) {
/* 125 */       BlockPos testPos = pos.relative(direction, i);
/* 126 */       BlockState wireState = level.getBlockState(testPos);
/*     */       
/* 128 */       if (wireState.is(Blocks.TRIPWIRE_HOOK)) {
/* 129 */         if (wireState.getValue(FACING) == direction.getOpposite()) {
/* 130 */           receiverPos = i;
/*     */         }
/*     */         break;
/*     */       } 
/* 134 */       if (wireState.is(Blocks.TRIPWIRE) || i == wireSource) {
/* 135 */         if (i == wireSource) {
/* 136 */           wireState = (BlockState)MoreObjects.firstNonNull(wireSourceState, wireState);
/*     */         }
/* 138 */         boolean wireArmed = !((Boolean)wireState.getValue(TripWireBlock.DISARMED)).booleanValue();
/* 139 */         boolean wirePowered = ((Boolean)wireState.getValue(TripWireBlock.POWERED)).booleanValue();
/* 140 */         powered |= ((wireArmed && wirePowered));
/*     */         
/* 142 */         wireStates[i] = wireState;
/*     */         
/* 144 */         if (i == wireSource) {
/* 145 */           level.scheduleTick(pos, block, 10);
/* 146 */           attached &= wireArmed;
/*     */         } 
/*     */       } else {
/* 149 */         wireStates[i] = null;
/* 150 */         attached = false;
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     attached &= ((receiverPos > 1));
/* 155 */     powered &= attached;
/* 156 */     BlockState newState = (BlockState)((BlockState)block.defaultBlockState().trySetValue(ATTACHED, Boolean.valueOf(attached))).trySetValue(POWERED, Boolean.valueOf(powered));
/*     */     
/* 158 */     if (receiverPos > 0) {
/* 159 */       BlockPos testPos = pos.relative(direction, receiverPos);
/* 160 */       Direction opposite = direction.getOpposite();
/* 161 */       level.setBlock(testPos, (BlockState)newState.setValue(FACING, opposite), 3);
/* 162 */       notifyNeighbors(block, level, testPos, opposite);
/*     */       
/* 164 */       emitState(level, testPos, attached, powered, wasAttached, wasPowered);
/*     */     } 
/*     */     
/* 167 */     emitState(level, pos, attached, powered, wasAttached, wasPowered);
/*     */     
/* 169 */     if (!isBeingDestroyed) {
/* 170 */       level.setBlock(pos, (BlockState)newState.setValue(FACING, direction), 3);
/* 171 */       if (canUpdate) {
/* 172 */         notifyNeighbors(block, level, pos, direction);
/*     */       }
/*     */     } 
/*     */     
/* 176 */     if (wasAttached != attached) {
/* 177 */       for (int i = 1; i < receiverPos; i++) {
/* 178 */         BlockPos testPos = pos.relative(direction, i);
/* 179 */         BlockState wireData = wireStates[i];
/* 180 */         if (wireData != null) {
/*     */ 
/*     */ 
/*     */           
/* 184 */           BlockState testPosState = level.getBlockState(testPos);
/* 185 */           if (testPosState.is(Blocks.TRIPWIRE) || testPosState.is(Blocks.TRIPWIRE_HOOK)) {
/* 186 */             level.setBlock(testPos, (BlockState)wireData.trySetValue(ATTACHED, Boolean.valueOf(attached)), 3);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 194 */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { calculateState(level, pos, state, false, true, -1, null); }
/*     */ 
/*     */   
/*     */   private static void emitState(Level level, BlockPos pos, boolean attached, boolean powered, boolean wasAttached, boolean wasPowered) {
/* 198 */     if (powered && !wasPowered) {
/* 199 */       level.playSound(null, pos, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.4F, 0.6F);
/* 200 */       level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, pos);
/* 201 */     } else if (!powered && wasPowered) {
/* 202 */       level.playSound(null, pos, SoundEvents.TRIPWIRE_CLICK_OFF, SoundSource.BLOCKS, 0.4F, 0.5F);
/* 203 */       level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, pos);
/* 204 */     } else if (attached && !wasAttached) {
/* 205 */       level.playSound(null, pos, SoundEvents.TRIPWIRE_ATTACH, SoundSource.BLOCKS, 0.4F, 0.7F);
/* 206 */       level.gameEvent(null, GameEvent.BLOCK_ATTACH, pos);
/* 207 */     } else if (!attached && wasAttached) {
/* 208 */       level.playSound(null, pos, SoundEvents.TRIPWIRE_DETACH, SoundSource.BLOCKS, 0.4F, 1.2F / (level.random.nextFloat() * 0.2F + 0.9F));
/* 209 */       level.gameEvent(null, GameEvent.BLOCK_DETACH, pos);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void notifyNeighbors(Block block, Level level, BlockPos pos, Direction direction) {
/* 214 */     Direction front = direction.getOpposite();
/* 215 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, front, Direction.UP);
/* 216 */     level.updateNeighborsAt(pos, block, orientation);
/* 217 */     level.updateNeighborsAt(pos.relative(front), block, orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 222 */     if (movedByPiston) {
/*     */       return;
/*     */     }
/* 225 */     boolean attached = ((Boolean)state.getValue(ATTACHED)).booleanValue();
/* 226 */     boolean powered = ((Boolean)state.getValue(POWERED)).booleanValue();
/*     */     
/* 228 */     if (attached || powered) {
/* 229 */       calculateState(level, pos, state, true, false, -1, null);
/*     */     }
/*     */     
/* 232 */     if (powered) {
/* 233 */       notifyNeighbors(this, level, pos, (Direction)state.getValue(FACING));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 239 */   protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 244 */     if (!((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 245 */       return 0;
/*     */     }
/*     */     
/* 248 */     if (state.getValue(FACING) == direction) {
/* 249 */       return 15;
/*     */     }
/*     */     
/* 252 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 257 */   protected boolean isSignalSource(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 267 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 272 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, POWERED, ATTACHED }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\TripWireHookBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */