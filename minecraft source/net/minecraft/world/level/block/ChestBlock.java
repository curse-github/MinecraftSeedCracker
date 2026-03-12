/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiPredicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.CompoundContainer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.LidBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ChestBlock extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {
/*  64 */   public static final MapCodec<ChestBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.SOUND_EVENT
/*  65 */         .byNameCodec().fieldOf("open_sound").forGetter(ChestBlock::getOpenChestSound), BuiltInRegistries.SOUND_EVENT
/*  66 */         .byNameCodec().fieldOf("close_sound").forGetter(ChestBlock::getCloseChestSound), 
/*  67 */         propertiesCodec())
/*  68 */       .apply(i, ()));
/*     */ 
/*     */ 
/*     */   
/*  72 */   public MapCodec<? extends ChestBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  75 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  76 */   public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;
/*  77 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   public static final int EVENT_SET_OPEN_COUNT = 1;
/*  80 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 14.0D);
/*  81 */   private static final Map<Direction, VoxelShape> HALF_SHAPES = Shapes.rotateHorizontal(Block.boxZ(14.0D, 0.0D, 14.0D, 0.0D, 15.0D));
/*     */   
/*     */   private final SoundEvent openSound;
/*     */   private final SoundEvent closeSound;
/*     */   
/*     */   protected ChestBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> blockEntityType, SoundEvent openSound, SoundEvent closeSound, BlockBehaviour.Properties properties) {
/*  87 */     super(properties, blockEntityType);
/*  88 */     this.openSound = openSound;
/*  89 */     this.closeSound = closeSound;
/*  90 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(TYPE, ChestType.SINGLE)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
/*  94 */     ChestType type = (ChestType)state.getValue(TYPE);
/*  95 */     if (type == ChestType.SINGLE) {
/*  96 */       return DoubleBlockCombiner.BlockType.SINGLE;
/*     */     }
/*  98 */     if (type == ChestType.RIGHT) {
/*  99 */       return DoubleBlockCombiner.BlockType.FIRST;
/*     */     }
/* 101 */     return DoubleBlockCombiner.BlockType.SECOND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 106 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 107 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 109 */     if (chestCanConnectTo(neighbourState) && directionToNeighbour.getAxis().isHorizontal()) {
/* 110 */       ChestType neighbourType = (ChestType)neighbourState.getValue(TYPE);
/* 111 */       if (state.getValue(TYPE) == ChestType.SINGLE && neighbourType != ChestType.SINGLE && 
/* 112 */         state.getValue(FACING) == neighbourState.getValue(FACING) && getConnectedDirection(neighbourState) == directionToNeighbour.getOpposite()) {
/* 113 */         return (BlockState)state.setValue(TYPE, neighbourType.getOpposite());
/*     */       }
/*     */     }
/* 116 */     else if (getConnectedDirection(state) == directionToNeighbour) {
/* 117 */       return (BlockState)state.setValue(TYPE, ChestType.SINGLE);
/*     */     } 
/* 119 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/* 123 */   public boolean chestCanConnectTo(BlockState blockState) { return blockState.is(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 128 */     switch ((ChestType)state.getValue(TYPE)) { default: throw new MatchException(null, null);case SINGLE: case LEFT: case RIGHT: break; }  return 
/*     */       
/* 130 */       (VoxelShape)HALF_SHAPES.get(getConnectedDirection(state));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Direction getConnectedDirection(BlockState state) {
/* 135 */     Direction facing = (Direction)state.getValue(FACING);
/* 136 */     return (state.getValue(TYPE) == ChestType.LEFT) ? facing.getClockWise() : facing.getCounterClockWise();
/*     */   }
/*     */   
/*     */   public static BlockPos getConnectedBlockPos(BlockPos pos, BlockState state) {
/* 140 */     Direction connectedDirection = getConnectedDirection(state);
/* 141 */     return pos.relative(connectedDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 146 */     ChestType type = ChestType.SINGLE;
/* 147 */     Direction facingDirection = context.getHorizontalDirection().getOpposite();
/* 148 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*     */     
/* 150 */     boolean secondaryUse = context.isSecondaryUseActive();
/* 151 */     Direction clickedFace = context.getClickedFace();
/*     */     
/* 153 */     if (clickedFace.getAxis().isHorizontal() && secondaryUse) {
/* 154 */       Direction neighbourFacing = candidatePartnerFacing(context.getLevel(), context.getClickedPos(), clickedFace.getOpposite());
/* 155 */       if (neighbourFacing != null && neighbourFacing.getAxis() != clickedFace.getAxis()) {
/* 156 */         facingDirection = neighbourFacing;
/* 157 */         type = (facingDirection.getCounterClockWise() == clickedFace.getOpposite()) ? ChestType.RIGHT : ChestType.LEFT;
/*     */       } 
/*     */     } 
/* 160 */     if (type == ChestType.SINGLE && !secondaryUse) {
/* 161 */       type = getChestType(context.getLevel(), context.getClickedPos(), facingDirection);
/*     */     }
/*     */     
/* 164 */     return (BlockState)((BlockState)((BlockState)defaultBlockState().setValue(FACING, facingDirection)).setValue(TYPE, type)).setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected ChestType getChestType(Level level, BlockPos pos, Direction facingDirection) {
/* 168 */     if (facingDirection == candidatePartnerFacing(level, pos, facingDirection.getClockWise()))
/* 169 */       return ChestType.LEFT; 
/* 170 */     if (facingDirection == candidatePartnerFacing(level, pos, facingDirection.getCounterClockWise())) {
/* 171 */       return ChestType.RIGHT;
/*     */     }
/* 173 */     return ChestType.SINGLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 178 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 179 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 181 */     return super.getFluidState(state);
/*     */   }
/*     */   
/*     */   private Direction candidatePartnerFacing(Level level, BlockPos pos, Direction neighbourDirection) {
/* 185 */     BlockState state = level.getBlockState(pos.relative(neighbourDirection));
/* 186 */     return (chestCanConnectTo(state) && state.getValue(TYPE) == ChestType.SINGLE) ? (Direction)state.getValue(FACING) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 191 */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) { Containers.updateNeighboursAfterDestroy(state, level, pos); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 196 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 197 */       MenuProvider menuProvider = getMenuProvider(state, level, pos);
/* 198 */       if (menuProvider != null) {
/* 199 */         player.openMenu(menuProvider);
/* 200 */         player.awardStat(getOpenChestStat());
/* 201 */         PiglinAi.angerNearbyPiglins(serverLevel, player, true);
/*     */       }  }
/*     */     
/* 204 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/* 208 */   protected Stat<Identifier> getOpenChestStat() { return Stats.CUSTOM.get(Stats.OPEN_CHEST); }
/*     */ 
/*     */ 
/*     */   
/* 212 */   public BlockEntityType<? extends ChestBlockEntity> blockEntityType() { return (BlockEntityType)this.blockEntityType.get(); }
/*     */ 
/*     */   
/* 215 */   private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> CHEST_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>>()
/*     */     {
/*     */       public Optional<Container> acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
/* 218 */         return Optional.of(new CompoundContainer(first, second));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 223 */       public Optional<Container> acceptSingle(ChestBlockEntity single) { return Optional.of(single); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 228 */       public Optional<Container> acceptNone() { return Optional.empty(); }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 233 */   public static Container getContainer(ChestBlock block, BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) { return (Container)((Optional)block.combine(state, level, pos, ignoreBeingBlocked).apply(CHEST_COMBINER)).orElse(null); }
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) {
/*     */     BiPredicate<LevelAccessor, BlockPos> predicate;
/* 239 */     if (ignoreBeingBlocked) {
/* 240 */       predicate = ((levelAccessor, blockPos) -> false);
/*     */     } else {
/* 242 */       predicate = ChestBlock::isChestBlockedAt;
/*     */     } 
/* 244 */     return DoubleBlockCombiner.combineWithNeigbour((BlockEntityType)this.blockEntityType.get(), ChestBlock::getBlockType, ChestBlock::getConnectedDirection, FACING, state, level, pos, predicate);
/*     */   }
/*     */   
/* 247 */   private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>()
/*     */     {
/*     */       public Optional<MenuProvider> acceptDouble(final ChestBlockEntity first, final ChestBlockEntity second) {
/* 250 */         final CompoundContainer container = new CompoundContainer(first, second);
/* 251 */         return Optional.of(new MenuProvider(this)
/*     */             {
/*     */               public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
/* 254 */                 if (first.canOpen(player) && second.canOpen(player)) {
/* 255 */                   first.unpackLootTable(inventory.player);
/* 256 */                   second.unpackLootTable(inventory.player);
/*     */                   
/* 258 */                   return ChestMenu.sixRows(containerId, inventory, container);
/*     */                 } 
/* 260 */                 Direction connectedDirection = ChestBlock.getConnectedDirection(first.getBlockState());
/* 261 */                 Vec3 firstCenter = first.getBlockPos().getCenter();
/* 262 */                 Vec3 centerBetweenChests = firstCenter.add(connectedDirection.getStepX() / 2.0D, 0.0D, connectedDirection.getStepZ() / 2.0D);
/* 263 */                 BaseContainerBlockEntity.sendChestLockedNotifications(centerBetweenChests, player, getDisplayName());
/*     */                 
/* 265 */                 return null;
/*     */               }
/*     */ 
/*     */               
/*     */               public Component getDisplayName() {
/* 270 */                 if (first.hasCustomName()) {
/* 271 */                   return first.getDisplayName();
/*     */                 }
/* 273 */                 if (second.hasCustomName()) {
/* 274 */                   return second.getDisplayName();
/*     */                 }
/* 276 */                 return Component.translatable("container.chestDouble");
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 283 */       public Optional<MenuProvider> acceptSingle(ChestBlockEntity single) { return Optional.of(single); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 288 */       public Optional<MenuProvider> acceptNone() { return Optional.empty(); }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 294 */   protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) { return (MenuProvider)((Optional)combine(state, level, pos, false).apply(MENU_PROVIDER_COMBINER)).orElse(null); }
/*     */ 
/*     */   
/*     */   public static DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction> opennessCombiner(final LidBlockEntity entity) {
/* 298 */     return new DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction>()
/*     */       {
/*     */         public Float2FloatFunction acceptDouble(ChestBlockEntity first, ChestBlockEntity second) {
/* 301 */           return partialTickTime -> Math.max(first.getOpenNess(partialTickTime), second.getOpenNess(partialTickTime));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 306 */         public Float2FloatFunction acceptSingle(ChestBlockEntity single) { Objects.requireNonNull(single); return single::getOpenNess; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 311 */         public Float2FloatFunction acceptNone() { Objects.requireNonNull(entity); return entity::getOpenNess; }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 318 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new ChestBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 323 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return level.isClientSide() ? createTickerHelper(type, blockEntityType(), ChestBlockEntity::lidAnimateTick) : null; }
/*     */ 
/*     */ 
/*     */   
/* 327 */   public static boolean isChestBlockedAt(LevelAccessor level, BlockPos pos) { return (isBlockedChestByBlock(level, pos) || isCatSittingOnChest(level, pos)); }
/*     */ 
/*     */   
/*     */   private static boolean isBlockedChestByBlock(BlockGetter level, BlockPos pos) {
/* 331 */     BlockPos above = pos.above();
/* 332 */     return level.getBlockState(above).isRedstoneConductor(level, above);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isCatSittingOnChest(LevelAccessor level, BlockPos pos) {
/* 337 */     List<Cat> cats = level.getEntitiesOfClass(Cat.class, new AABB(pos.getX(), (pos.getY() + 1), pos.getZ(), (pos.getX() + 1), (pos.getY() + 2), (pos.getZ() + 1)));
/* 338 */     if (!cats.isEmpty()) {
/* 339 */       for (Cat cat : cats) {
/* 340 */         if (cat.isInSittingPose()) {
/* 341 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 350 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 355 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, state, level, pos, false)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 360 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 365 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 370 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, TYPE, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 375 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 380 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*     */     
/* 382 */     if (blockEntity instanceof ChestBlockEntity) {
/* 383 */       ((ChestBlockEntity)blockEntity).recheckOpen();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 388 */   public SoundEvent getOpenChestSound() { return this.openSound; }
/*     */ 
/*     */ 
/*     */   
/* 392 */   public SoundEvent getCloseChestSound() { return this.closeSound; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */