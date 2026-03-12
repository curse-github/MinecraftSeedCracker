/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SignalGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonBaseBlock extends DirectionalBlock {
/*  47 */   public static final MapCodec<PistonBaseBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/*  48 */         .fieldOf("sticky").forGetter(()), 
/*  49 */         propertiesCodec())
/*  50 */       .apply(i, PistonBaseBlock::new));
/*     */ 
/*     */ 
/*     */   
/*  54 */   public MapCodec<PistonBaseBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  57 */   public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;
/*     */   
/*     */   public static final int TRIGGER_EXTEND = 0;
/*     */   
/*     */   public static final int TRIGGER_CONTRACT = 1;
/*     */   public static final int TRIGGER_DROP = 2;
/*     */   public static final int PLATFORM_THICKNESS = 4;
/*  64 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Block.boxZ(16.0D, 4.0D, 16.0D));
/*     */   
/*     */   private final boolean isSticky;
/*     */   
/*     */   public PistonBaseBlock(boolean isSticky, BlockBehaviour.Properties properties) {
/*  69 */     super(properties);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(EXTENDED, Boolean.valueOf(false)));
/*  71 */     this.isSticky = isSticky;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  76 */     if (((Boolean)state.getValue(EXTENDED)).booleanValue()) {
/*  77 */       return (VoxelShape)SHAPES.get(state.getValue(FACING));
/*     */     }
/*  79 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/*  84 */     if (!level.isClientSide()) {
/*  85 */       checkIfExtend(level, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/*  91 */     if (!level.isClientSide()) {
/*  92 */       checkIfExtend(level, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/*  98 */     if (oldState.is(state.getBlock())) {
/*     */       return;
/*     */     }
/* 101 */     if (!level.isClientSide() && level.getBlockEntity(pos) == null) {
/* 102 */       checkIfExtend(level, pos, state);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)((BlockState)defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite())).setValue(EXTENDED, Boolean.valueOf(false)); }
/*     */ 
/*     */   
/*     */   private void checkIfExtend(Level level, BlockPos pos, BlockState state) {
/* 112 */     Direction direction = (Direction)state.getValue(FACING);
/*     */     
/* 114 */     boolean extend = getNeighborSignal(level, pos, direction);
/*     */     
/* 116 */     if (extend && !((Boolean)state.getValue(EXTENDED)).booleanValue()) {
/* 117 */       if ((new PistonStructureResolver(level, pos, direction, true)).resolve()) {
/* 118 */         level.blockEvent(pos, this, 0, direction.get3DDataValue());
/*     */       }
/* 120 */     } else if (!extend && ((Boolean)state.getValue(EXTENDED)).booleanValue()) {
/* 121 */       BlockPos pushedPos = pos.relative(direction, 2);
/* 122 */       BlockState pushedState = level.getBlockState(pushedPos);
/*     */       
/* 124 */       int event = 1;
/* 125 */       if (pushedState.is(Blocks.MOVING_PISTON) && pushedState.getValue(FACING) == direction) {
/* 126 */         BlockEntity entity = level.getBlockEntity(pushedPos);
/*     */         
/* 128 */         if (entity instanceof PistonMovingBlockEntity) { PistonMovingBlockEntity pistonEntity = (PistonMovingBlockEntity)entity;
/* 129 */           if (pistonEntity.isExtending() && (pistonEntity.getProgress(0.0F) < 0.5F || level.getGameTime() == pistonEntity.getLastTicked() || ((ServerLevel)level).isHandlingTick())) {
/* 130 */             event = 2;
/*     */           } }
/*     */       
/*     */       } 
/*     */       
/* 135 */       level.blockEvent(pos, this, event, direction.get3DDataValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean getNeighborSignal(SignalGetter level, BlockPos pos, Direction pushDirection) {
/* 146 */     for (Direction direction : Direction.values()) {
/* 147 */       if (direction != pushDirection && level.hasSignal(pos.relative(direction), direction)) {
/* 148 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 153 */     if (level.hasSignal(pos, Direction.DOWN)) {
/* 154 */       return true;
/*     */     }
/*     */     
/* 157 */     BlockPos above = pos.above();
/* 158 */     for (Direction direction : Direction.values()) {
/* 159 */       if (direction != Direction.DOWN && level.hasSignal(above.relative(direction), direction)) {
/* 160 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1) {
/* 169 */     Direction direction = (Direction)state.getValue(FACING);
/* 170 */     BlockState extendedState = (BlockState)state.setValue(EXTENDED, Boolean.valueOf(true));
/* 171 */     if (!level.isClientSide()) {
/* 172 */       boolean extend = getNeighborSignal(level, pos, direction);
/*     */       
/* 174 */       if (extend && (b0 == 1 || b0 == 2)) {
/*     */         
/* 176 */         level.setBlock(pos, extendedState, 2);
/* 177 */         return false;
/* 178 */       }  if (!extend && b0 == 0) {
/* 179 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 183 */     if (b0 == 0) {
/* 184 */       if (moveBlocks(level, pos, direction, true)) {
/* 185 */         level.setBlock(pos, extendedState, 67);
/* 186 */         level.playSound(null, pos, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.25F + 0.6F);
/* 187 */         level.gameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Context.of(extendedState));
/*     */       } else {
/* 189 */         return false;
/*     */       } 
/* 191 */     } else if (b0 == 1 || b0 == 2) {
/* 192 */       BlockEntity prevBlockEntity = level.getBlockEntity(pos.relative(direction));
/* 193 */       if (prevBlockEntity instanceof PistonMovingBlockEntity) {
/* 194 */         ((PistonMovingBlockEntity)prevBlockEntity).finalTick();
/*     */       }
/*     */       
/* 197 */       BlockState movingPistonState = (BlockState)((BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, direction)).setValue(MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
/* 198 */       level.setBlock(pos, movingPistonState, 276);
/* 199 */       level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(pos, movingPistonState, (BlockState)defaultBlockState().setValue(FACING, Direction.from3DDataValue(b1 & 0x7)), direction, false, true));
/*     */       
/* 201 */       level.updateNeighborsAt(pos, movingPistonState.getBlock());
/* 202 */       movingPistonState.updateNeighbourShapes(level, pos, 2);
/*     */ 
/*     */       
/* 205 */       if (this.isSticky) {
/* 206 */         BlockPos twoPos = pos.offset(direction.getStepX() * 2, direction.getStepY() * 2, direction.getStepZ() * 2);
/* 207 */         BlockState movingState = level.getBlockState(twoPos);
/* 208 */         boolean pistonPiece = false;
/*     */         
/* 210 */         if (movingState.is(Blocks.MOVING_PISTON)) {
/*     */ 
/*     */           
/* 213 */           BlockEntity blockEntity = level.getBlockEntity(twoPos);
/* 214 */           if (blockEntity instanceof PistonMovingBlockEntity) { PistonMovingBlockEntity entity = (PistonMovingBlockEntity)blockEntity;
/* 215 */             if (entity.getDirection() == direction && entity.isExtending()) {
/*     */               
/* 217 */               entity.finalTick();
/* 218 */               pistonPiece = true;
/*     */             }  }
/*     */         
/*     */         } 
/*     */         
/* 223 */         if (!pistonPiece) {
/* 224 */           if (b0 == 1 && !movingState.isAir() && isPushable(movingState, level, twoPos, direction.getOpposite(), false, direction) && (movingState.getPistonPushReaction() == PushReaction.NORMAL || movingState.is(Blocks.PISTON) || movingState.is(Blocks.STICKY_PISTON))) {
/* 225 */             moveBlocks(level, pos, direction, false);
/*     */           } else {
/* 227 */             level.removeBlock(pos.relative(direction), false);
/*     */           } 
/*     */         }
/*     */       } else {
/* 231 */         level.removeBlock(pos.relative(direction), false);
/*     */       } 
/*     */       
/* 234 */       level.playSound(null, pos, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.15F + 0.6F);
/* 235 */       level.gameEvent(GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Context.of(movingPistonState));
/*     */     } 
/* 237 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPushable(BlockState state, Level level, BlockPos pos, Direction direction, boolean allowDestroyable, Direction connectionDirection) {
/* 241 */     if (pos.getY() < level.getMinY() || pos.getY() > level.getMaxY() || !level.getWorldBorder().isWithinBounds(pos)) {
/* 242 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 246 */     if (state.isAir()) {
/* 247 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 251 */     if (state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN) || state.is(Blocks.RESPAWN_ANCHOR) || state.is(Blocks.REINFORCED_DEEPSLATE)) {
/* 252 */       return false;
/*     */     }
/*     */     
/* 255 */     if (direction == Direction.DOWN && pos.getY() == level.getMinY()) {
/* 256 */       return false;
/*     */     }
/*     */     
/* 259 */     if (direction == Direction.UP && pos.getY() == level.getMaxY()) {
/* 260 */       return false;
/*     */     }
/*     */     
/* 263 */     if (state.is(Blocks.PISTON) || state.is(Blocks.STICKY_PISTON)) {
/*     */       
/* 265 */       if (((Boolean)state.getValue(EXTENDED)).booleanValue()) {
/* 266 */         return false;
/*     */       }
/*     */     } else {
/* 269 */       if (state.getDestroySpeed(level, pos) == -1.0F) {
/* 270 */         return false;
/*     */       }
/*     */       
/* 273 */       switch (state.getPistonPushReaction()) {
/*     */         case BLOCK:
/* 275 */           return false;
/*     */         case DESTROY:
/* 277 */           return allowDestroyable;
/*     */         case PUSH_ONLY:
/* 279 */           return (direction == connectionDirection);
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 284 */     return !state.hasBlockEntity();
/*     */   }
/*     */   
/*     */   private boolean moveBlocks(Level level, BlockPos pistonPos, Direction direction, boolean extending) {
/* 288 */     BlockPos armPos = pistonPos.relative(direction);
/* 289 */     if (!extending && level.getBlockState(armPos).is(Blocks.PISTON_HEAD))
/*     */     {
/* 291 */       level.setBlock(armPos, Blocks.AIR.defaultBlockState(), 276);
/*     */     }
/*     */     
/* 294 */     PistonStructureResolver resolver = new PistonStructureResolver(level, pistonPos, direction, extending);
/* 295 */     if (!resolver.resolve()) {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     Map<BlockPos, BlockState> deleteAfterMove = Maps.newHashMap();
/* 300 */     List<BlockPos> toPush = resolver.getToPush();
/* 301 */     List<BlockState> toPushShapes = Lists.newArrayList();
/* 302 */     for (BlockPos pos : toPush) {
/* 303 */       BlockState state = level.getBlockState(pos);
/* 304 */       toPushShapes.add(state);
/* 305 */       deleteAfterMove.put(pos, state);
/*     */     } 
/* 307 */     List<BlockPos> toDestroy = resolver.getToDestroy();
/*     */     
/* 309 */     BlockState[] toUpdate = new BlockState[toPush.size() + toDestroy.size()];
/* 310 */     Direction pushDirection = extending ? direction : direction.getOpposite();
/*     */     
/* 312 */     int updateIndex = 0;
/*     */     
/* 314 */     for (int i = toDestroy.size() - 1; i >= 0; i--) {
/* 315 */       BlockPos pos = (BlockPos)toDestroy.get(i);
/*     */       
/* 317 */       BlockState state = level.getBlockState(pos);
/*     */       
/* 319 */       BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
/*     */       
/* 321 */       dropResources(state, level, pos, blockEntity);
/* 322 */       if (!state.is(BlockTags.FIRE) && level.isClientSide()) {
/* 323 */         level.levelEvent(2001, pos, getId(state));
/*     */       }
/* 325 */       level.setBlock(pos, Blocks.AIR.defaultBlockState(), 18);
/* 326 */       level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
/*     */       
/* 328 */       toUpdate[updateIndex++] = state;
/*     */     } 
/*     */ 
/*     */     
/* 332 */     for (int i = toPush.size() - 1; i >= 0; i--) {
/* 333 */       BlockPos pos = (BlockPos)toPush.get(i);
/* 334 */       BlockState blockState = level.getBlockState(pos);
/*     */       
/* 336 */       pos = pos.relative(pushDirection);
/*     */       
/* 338 */       deleteAfterMove.remove(pos);
/*     */       
/* 340 */       BlockState actualState = (BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue(FACING, direction);
/* 341 */       level.setBlock(pos, actualState, 324);
/* 342 */       level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(pos, actualState, (BlockState)toPushShapes.get(i), direction, extending, false));
/*     */       
/* 344 */       toUpdate[updateIndex++] = blockState;
/*     */     } 
/*     */     
/* 347 */     if (extending) {
/* 348 */       PistonType type = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
/* 349 */       BlockState state = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue(PistonHeadBlock.FACING, direction)).setValue(PistonHeadBlock.TYPE, type);
/*     */ 
/*     */ 
/*     */       
/* 353 */       BlockState blockState = (BlockState)((BlockState)Blocks.MOVING_PISTON.defaultBlockState().setValue(MovingPistonBlock.FACING, direction)).setValue(MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
/*     */       
/* 355 */       deleteAfterMove.remove(armPos);
/*     */       
/* 357 */       level.setBlock(armPos, blockState, 324);
/* 358 */       level.setBlockEntity(MovingPistonBlock.newMovingBlockEntity(armPos, blockState, state, direction, true, true));
/*     */     } 
/*     */     
/* 361 */     BlockState air = Blocks.AIR.defaultBlockState();
/* 362 */     for (BlockPos pos : deleteAfterMove.keySet()) {
/* 363 */       level.setBlock(pos, air, 82);
/*     */     }
/*     */     
/* 366 */     for (Map.Entry<BlockPos, BlockState> entry : deleteAfterMove.entrySet()) {
/* 367 */       BlockPos pos = (BlockPos)entry.getKey();
/* 368 */       BlockState oldState = (BlockState)entry.getValue();
/* 369 */       oldState.updateIndirectNeighbourShapes(level, pos, 2);
/* 370 */       air.updateNeighbourShapes(level, pos, 2);
/* 371 */       air.updateIndirectNeighbourShapes(level, pos, 2);
/*     */     } 
/*     */     
/* 374 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(level, resolver.getPushDirection(), null);
/* 375 */     updateIndex = 0;
/*     */     
/* 377 */     for (int i = toDestroy.size() - 1; i >= 0; i--) {
/* 378 */       BlockState state = toUpdate[updateIndex++];
/* 379 */       BlockPos pos = (BlockPos)toDestroy.get(i);
/* 380 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 381 */         state.affectNeighborsAfterRemoval(serverLevel, pos, false); }
/*     */       
/* 383 */       state.updateIndirectNeighbourShapes(level, pos, 2);
/* 384 */       level.updateNeighborsAt(pos, state.getBlock(), orientation);
/*     */     } 
/*     */ 
/*     */     
/* 388 */     for (int i = toPush.size() - 1; i >= 0; i--) {
/* 389 */       level.updateNeighborsAt((BlockPos)toPush.get(i), toUpdate[updateIndex++].getBlock(), orientation);
/*     */     }
/*     */     
/* 392 */     if (extending) {
/* 393 */       level.updateNeighborsAt(armPos, Blocks.PISTON_HEAD, orientation);
/*     */     }
/*     */     
/* 396 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 401 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 406 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 411 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, EXTENDED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 416 */   protected boolean useShapeForLightOcclusion(BlockState state) { return ((Boolean)state.getValue(EXTENDED)).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 421 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\piston\PistonBaseBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */