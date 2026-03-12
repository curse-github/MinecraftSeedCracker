/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ShulkerBoxBlock extends BaseEntityBlock {
/*  43 */   public static final MapCodec<ShulkerBoxBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(DyeColor.CODEC
/*  44 */         .optionalFieldOf("color").forGetter(()), 
/*  45 */         propertiesCodec())
/*  46 */       .apply(i, ()));
/*     */ 
/*     */ 
/*     */   
/*  50 */   public MapCodec<ShulkerBoxBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  53 */   public static final Map<Direction, VoxelShape> SHAPES_OPEN_SUPPORT = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */   
/*  55 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*     */   
/*  57 */   public static final Identifier CONTENTS = Identifier.withDefaultNamespace("contents");
/*     */   
/*     */   private final DyeColor color;
/*     */   
/*     */   public ShulkerBoxBlock(DyeColor color, BlockBehaviour.Properties properties) {
/*  62 */     super(properties);
/*  63 */     this.color = color;
/*  64 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.UP));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new ShulkerBoxBlockEntity(this.color, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) { return createTickerHelper(type, BlockEntityType.SHULKER_BOX, ShulkerBoxBlockEntity::tick); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  79 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity; if (canOpen(state, level, pos, shulkerBoxBlockEntity))
/*  80 */         { player.openMenu(shulkerBoxBlockEntity);
/*  81 */           player.awardStat(Stats.OPEN_SHULKER_BOX);
/*  82 */           PiglinAi.angerNearbyPiglins(serverLevel, player, true); }  }
/*     */        }
/*  84 */      return InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static boolean canOpen(BlockState state, Level level, BlockPos pos, ShulkerBoxBlockEntity blockEntity) {
/*  88 */     if (blockEntity.getAnimationStatus() != ShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
/*  89 */       return true;
/*     */     }
/*     */     
/*  92 */     AABB lidOpenBoundingBox = Shulker.getProgressDeltaAabb(1.0F, (Direction)state.getValue(FACING), 0.0F, 0.5F, pos.getBottomCenter()).deflate(1.0E-6D);
/*  93 */     return level.noCollision(lidOpenBoundingBox);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public BlockState getStateForPlacement(BlockPlaceContext context) { return (BlockState)defaultBlockState().setValue(FACING, context.getClickedFace()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING }); }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 108 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 109 */     if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 110 */       if (!level.isClientSide() && player.preventsBlockDrops() && !shulkerBoxBlockEntity.isEmpty()) {
/*     */         
/* 112 */         ItemStack itemStack = getColoredItemStack(getColor());
/* 113 */         itemStack.applyComponents(blockEntity.collectComponents());
/*     */         
/* 115 */         ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
/* 116 */         entity.setDefaultPickUpDelay();
/* 117 */         level.addFreshEntity(entity);
/*     */       } else {
/* 119 */         shulkerBoxBlockEntity.unpackLootTable(player);
/*     */       }  }
/*     */     
/* 122 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/* 127 */     BlockEntity blockEntity = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/*     */     
/* 129 */     if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 130 */       params = params.withDynamicDrop(CONTENTS, output -> {
/* 131 */             for (int i = 0; i < shulkerBoxBlockEntity.getContainerSize(); i++) {
/* 132 */               output.accept(shulkerBoxBlockEntity.getItem(i));
/*     */             }
/*     */           }); }
/*     */ 
/*     */     
/* 137 */     return super.getDrops(state, params);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 142 */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) { Containers.updateNeighboursAfterDestroy(state, level, pos); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 147 */     BlockEntity entity = level.getBlockEntity(pos);
/* 148 */     if (entity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulker = (ShulkerBoxBlockEntity)entity; if (!shulker.isClosed())
/* 149 */         return (VoxelShape)SHAPES_OPEN_SUPPORT.get(((Direction)state.getValue(FACING)).getOpposite());  }
/*     */     
/* 151 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 156 */     BlockEntity entity = level.getBlockEntity(pos);
/* 157 */     if (entity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)entity;
/* 158 */       return Shapes.create(shulkerBoxBlockEntity.getBoundingBox(state)); }
/*     */     
/* 160 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 165 */   protected boolean propagatesSkylightDown(BlockState state) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) { return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos)); }
/*     */ 
/*     */   
/*     */   public static Block getBlockByColor(DyeColor color) {
/* 179 */     if (color == null) {
/* 180 */       return Blocks.SHULKER_BOX;
/*     */     }
/* 182 */     switch (color) { default: throw new MatchException(null, null);case WHITE: case ORANGE: case MAGENTA: case LIGHT_BLUE: case YELLOW: case LIME: case PINK: case GRAY: case LIGHT_GRAY: case CYAN: case BLUE: case BROWN: case GREEN: case RED: case BLACK: case PURPLE: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       Blocks.PURPLE_SHULKER_BOX;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 203 */   public DyeColor getColor() { return this.color; }
/*     */ 
/*     */ 
/*     */   
/* 207 */   public static ItemStack getColoredItemStack(DyeColor color) { return new ItemStack(getBlockByColor(color)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   protected BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ShulkerBoxBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */