/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockSetType;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DoorHingeSide;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DoorBlock extends Block {
/*  44 */   public static final MapCodec<DoorBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockSetType.CODEC
/*  45 */         .fieldOf("block_set_type").forGetter(DoorBlock::type), 
/*  46 */         propertiesCodec())
/*  47 */       .apply(i, DoorBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  51 */   public MapCodec<? extends DoorBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  54 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  55 */   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
/*  56 */   public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
/*  57 */   public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
/*  58 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*  60 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(16.0D, 13.0D, 16.0D));
/*     */   
/*     */   private final BlockSetType type;
/*     */   
/*     */   protected DoorBlock(BlockSetType type, BlockBehaviour.Properties properties) {
/*  65 */     super(properties.sound(type.soundType()));
/*  66 */     this.type = type;
/*  67 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(OPEN, Boolean.valueOf(false))).setValue(HINGE, DoorHingeSide.LEFT)).setValue(POWERED, Boolean.valueOf(false))).setValue(HALF, DoubleBlockHalf.LOWER));
/*     */   }
/*     */ 
/*     */   
/*  71 */   public BlockSetType type() { return this.type; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  76 */     Direction direction = (Direction)state.getValue(FACING);
/*  77 */     Direction doorDirection = ((Boolean)state.getValue(OPEN)).booleanValue() ? ((state.getValue(HINGE) == DoorHingeSide.RIGHT) ? direction.getCounterClockWise() : direction.getClockWise()) : direction;
/*     */     
/*  79 */     return (VoxelShape)SHAPES.get(doorDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  84 */     DoubleBlockHalf half = (DoubleBlockHalf)state.getValue(HALF);
/*  85 */     if (directionToNeighbour.getAxis() == Direction.Axis.Y) if (((half == DoubleBlockHalf.LOWER) ? 1 : 0) == ((directionToNeighbour == Direction.UP) ? 1 : 0)) {
/*     */         
/*  87 */         if (neighbourState.getBlock() instanceof DoorBlock && neighbourState.getValue(HALF) != half)
/*     */         {
/*  89 */           return (BlockState)neighbourState.setValue(HALF, half);
/*     */         }
/*  91 */         return Blocks.AIR.defaultBlockState();
/*     */       } 
/*     */ 
/*     */     
/*  95 */     if (half == DoubleBlockHalf.LOWER && directionToNeighbour == Direction.DOWN && !state.canSurvive(level, pos)) {
/*  96 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
/* 104 */     if (explosion.canTriggerBlocks() && state.getValue(HALF) == DoubleBlockHalf.LOWER && this.type.canOpenByWindCharge() && !((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 105 */       setOpen(null, level, state, pos, !isOpen(state));
/*     */     }
/* 107 */     super.onExplosionHit(state, level, pos, explosion, onHit);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 112 */     if (!level.isClientSide() && (player.preventsBlockDrops() || !player.hasCorrectToolForDrops(state))) {
/* 113 */       DoublePlantBlock.preventDropFromBottomPart(level, pos, state, player);
/*     */     }
/*     */     
/* 116 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 121 */     switch (type) { default: throw new MatchException(null, null);case LAND: case AIR: case WATER: break; }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 129 */     BlockPos pos = context.getClickedPos();
/* 130 */     Level level = context.getLevel();
/* 131 */     if (pos.getY() < level.getMaxY() && level.getBlockState(pos.above()).canBeReplaced(context)) {
/* 132 */       boolean powered = (level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above()));
/*     */       
/* 134 */       return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection())).setValue(HINGE, getHinge(context))).setValue(POWERED, Boolean.valueOf(powered))).setValue(OPEN, Boolean.valueOf(powered))).setValue(HALF, DoubleBlockHalf.LOWER);
/*     */     } 
/*     */     
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) { level.setBlock(pos.above(), (BlockState)state.setValue(HALF, DoubleBlockHalf.UPPER), 3); }
/*     */ 
/*     */   
/*     */   private DoorHingeSide getHinge(BlockPlaceContext context) {
/* 146 */     Level level1 = context.getLevel();
/* 147 */     BlockPos pos = context.getClickedPos();
/* 148 */     Direction placeDirection = context.getHorizontalDirection();
/* 149 */     BlockPos abovePos = pos.above();
/*     */     
/* 151 */     Direction leftDirection = placeDirection.getCounterClockWise();
/* 152 */     BlockPos leftPos = pos.relative(leftDirection);
/* 153 */     BlockState leftState = level1.getBlockState(leftPos);
/* 154 */     BlockPos leftAbovePos = abovePos.relative(leftDirection);
/* 155 */     BlockState leftAboveState = level1.getBlockState(leftAbovePos);
/*     */     
/* 157 */     Direction rightDirection = placeDirection.getClockWise();
/* 158 */     BlockPos rightPos = pos.relative(rightDirection);
/* 159 */     BlockState rightState = level1.getBlockState(rightPos);
/* 160 */     BlockPos rightAbovePos = abovePos.relative(rightDirection);
/* 161 */     BlockState rightAboveState = level1.getBlockState(rightAbovePos);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     int solidBlockBalance = (leftState.isCollisionShapeFullBlock(level1, leftPos) ? -1 : 0) + (leftAboveState.isCollisionShapeFullBlock(level1, leftAbovePos) ? -1 : 0) + (rightState.isCollisionShapeFullBlock(level1, rightPos) ? 1 : 0) + (rightAboveState.isCollisionShapeFullBlock(level1, rightAbovePos) ? 1 : 0);
/*     */     
/* 168 */     boolean doorLeft = (leftState.getBlock() instanceof DoorBlock && leftState.getValue(HALF) == DoubleBlockHalf.LOWER);
/* 169 */     boolean doorRight = (rightState.getBlock() instanceof DoorBlock && rightState.getValue(HALF) == DoubleBlockHalf.LOWER);
/*     */     
/* 171 */     if ((doorLeft && !doorRight) || solidBlockBalance > 0) {
/* 172 */       return DoorHingeSide.RIGHT;
/*     */     }
/* 174 */     if ((doorRight && !doorLeft) || solidBlockBalance < 0) {
/* 175 */       return DoorHingeSide.LEFT;
/*     */     }
/*     */     
/* 178 */     int stepX = placeDirection.getStepX();
/* 179 */     int stepZ = placeDirection.getStepZ();
/*     */     
/* 181 */     Vec3 clickLocation = context.getClickLocation();
/* 182 */     double clickX = clickLocation.x - pos.getX();
/* 183 */     double clickZ = clickLocation.z - pos.getZ();
/*     */     
/* 185 */     return ((stepX < 0 && clickZ < 0.5D) || (stepX > 0 && clickZ > 0.5D) || (stepZ < 0 && clickX > 0.5D) || (stepZ > 0 && clickX < 0.5D)) ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 190 */     if (!this.type.canOpenByHand()) {
/* 191 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 194 */     state = (BlockState)state.cycle(OPEN);
/* 195 */     level.setBlock(pos, state, 10);
/* 196 */     playSound(player, level, pos, ((Boolean)state.getValue(OPEN)).booleanValue());
/* 197 */     level.gameEvent(player, isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
/* 198 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   public boolean isOpen(BlockState state) { return ((Boolean)state.getValue(OPEN)).booleanValue(); }
/*     */ 
/*     */   
/*     */   public void setOpen(Entity sourceEntity, Level level, BlockState state, BlockPos pos, boolean shouldOpen) {
/* 210 */     if (!state.is(this) || ((Boolean)state.getValue(OPEN)).booleanValue() == shouldOpen) {
/*     */       return;
/*     */     }
/*     */     
/* 214 */     level.setBlock(pos, (BlockState)state.setValue(OPEN, Boolean.valueOf(shouldOpen)), 10);
/* 215 */     playSound(sourceEntity, level, pos, shouldOpen);
/* 216 */     level.gameEvent(sourceEntity, shouldOpen ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 221 */     boolean signal = (level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.relative((state.getValue(HALF) == DoubleBlockHalf.LOWER) ? Direction.UP : Direction.DOWN)));
/* 222 */     if (!defaultBlockState().is(block) && signal != ((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 223 */       if (signal != ((Boolean)state.getValue(OPEN)).booleanValue()) {
/* 224 */         playSound(null, level, pos, signal);
/* 225 */         level.gameEvent(null, signal ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
/*     */       } 
/* 227 */       level.setBlock(pos, (BlockState)((BlockState)state.setValue(POWERED, Boolean.valueOf(signal))).setValue(OPEN, Boolean.valueOf(signal)), 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 234 */     BlockPos below = pos.below();
/* 235 */     BlockState belowState = level.getBlockState(below);
/* 236 */     if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
/* 237 */       return belowState.isFaceSturdy(level, below, Direction.UP);
/*     */     }
/* 239 */     return belowState.is(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 244 */   private void playSound(Entity entity, Level level, BlockPos pos, boolean open) { level.playSound(entity, pos, open ? this.type.doorOpen() : this.type.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 249 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 254 */     if (mirror == Mirror.NONE) {
/* 255 */       return state;
/*     */     }
/* 257 */     return (BlockState)state.rotate(mirror.getRotation((Direction)state.getValue(FACING))).cycle(HINGE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 262 */   protected long getSeed(BlockState state, BlockPos pos) { return Mth.getSeed(pos.getX(), pos.below((state.getValue(HALF) == DoubleBlockHalf.LOWER) ? 0 : 1).getY(), pos.getZ()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 267 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { HALF, FACING, OPEN, HINGE, POWERED }); }
/*     */ 
/*     */ 
/*     */   
/* 271 */   public static boolean isWoodenDoor(Level level, BlockPos pos) { return isWoodenDoor(level.getBlockState(pos)); }
/*     */ 
/*     */   
/*     */   public static boolean isWoodenDoor(BlockState state) {
/* 275 */     Block block = state.getBlock(); if (block instanceof DoorBlock) { DoorBlock door = (DoorBlock)block; if (door.type().canOpenByHand()); }  return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DoorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */