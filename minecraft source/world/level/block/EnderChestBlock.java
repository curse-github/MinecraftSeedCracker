/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.inventory.PlayerEnderChestContainer;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class EnderChestBlock
/*     */   extends AbstractChestBlock<EnderChestBlockEntity> implements SimpleWaterloggedBlock {
/*  46 */   public static final MapCodec<EnderChestBlock> CODEC = simpleCodec(EnderChestBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  50 */   public MapCodec<EnderChestBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  53 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/*  54 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  56 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 14.0D);
/*  57 */   private static final Component CONTAINER_TITLE = Component.translatable("container.enderchest");
/*     */   
/*     */   protected EnderChestBlock(BlockBehaviour.Properties properties) {
/*  60 */     super(properties, () -> BlockEntityType.ENDER_CHEST);
/*  61 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) { return DoubleBlockCombiner.Combiner::acceptNone; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return SHAPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  76 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/*  77 */     return (BlockState)((BlockState)defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())).setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*     */     EnderChestBlockEntity enderChest;
/*  82 */     PlayerEnderChestContainer container = player.getEnderChestInventory();
/*  83 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  84 */     if (container != null && blockEntity instanceof EnderChestBlockEntity) { enderChest = (EnderChestBlockEntity)blockEntity; }
/*  85 */     else { return InteractionResult.SUCCESS; }
/*     */ 
/*     */     
/*  88 */     BlockPos above = pos.above();
/*  89 */     if (level.getBlockState(above).isRedstoneConductor(level, above)) {
/*  90 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  93 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  94 */       container.setActiveChest(enderChest);
/*     */       
/*  96 */       player.openMenu(new SimpleMenuProvider((containerId, inventory, p) -> ChestMenu.threeRows(containerId, inventory, container), CONTAINER_TITLE));
/*  97 */       player.awardStat(Stats.OPEN_ENDERCHEST);
/*  98 */       PiglinAi.angerNearbyPiglins(serverLevel, player, true); }
/*     */     
/* 100 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new EnderChestBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return level.isClientSide() ? createTickerHelper(type, BlockEntityType.ENDER_CHEST, EnderChestBlockEntity::lidAnimateTick) : null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
/* 115 */     for (int i = 0; i < 3; i++) {
/* 116 */       int flipX = random.nextInt(2) * 2 - 1;
/* 117 */       int flipZ = random.nextInt(2) * 2 - 1;
/*     */       
/* 119 */       double x = pos.getX() + 0.5D + 0.25D * flipX;
/* 120 */       double y = (pos.getY() + random.nextFloat());
/* 121 */       double z = pos.getZ() + 0.5D + 0.25D * flipZ;
/* 122 */       double xa = (random.nextFloat() * flipX);
/* 123 */       double ya = (random.nextFloat() - 0.5D) * 0.125D;
/* 124 */       double za = (random.nextFloat() * flipZ);
/*     */       
/* 126 */       level.addParticle(ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 147 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 148 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 150 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 155 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 156 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 158 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 163 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 168 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*     */     
/* 170 */     if (blockEntity instanceof EnderChestBlockEntity)
/* 171 */       ((EnderChestBlockEntity)blockEntity).recheckOpen(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\EnderChestBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */