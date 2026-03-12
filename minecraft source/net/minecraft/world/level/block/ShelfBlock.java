/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.UseEffects;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ShelfBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ShelfBlock extends BaseEntityBlock implements SelectableSlotContainer, SideChainPartBlock, SimpleWaterloggedBlock {
/*  51 */   public static final MapCodec<ShelfBlock> CODEC = simpleCodec(ShelfBlock::new);
/*     */ 
/*     */ 
/*     */   
/*  55 */   public MapCodec<ShelfBlock> codec() { return CODEC; }
/*     */ 
/*     */   
/*  58 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  59 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  60 */   public static final EnumProperty<SideChainPart> SIDE_CHAIN_PART = BlockStateProperties.SIDE_CHAIN_PART;
/*  61 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  63 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Shapes.or(
/*  64 */         Block.box(0.0D, 12.0D, 11.0D, 16.0D, 16.0D, 13.0D), new VoxelShape[] {
/*  65 */           Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D), 
/*  66 */           Block.box(0.0D, 0.0D, 11.0D, 16.0D, 4.0D, 13.0D) }));
/*     */   
/*     */   public ShelfBlock(BlockBehaviour.Properties properties) {
/*  69 */     super(properties);
/*  70 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any())
/*  71 */         .setValue(FACING, Direction.NORTH))
/*  72 */         .setValue(POWERED, Boolean.valueOf(false)))
/*  73 */         .setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED))
/*  74 */         .setValue(WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { return (VoxelShape)SHAPES.get(state.getValue(FACING)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   protected boolean useShapeForLightOcclusion(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   protected boolean isPathfindable(BlockState state, PathComputationType type) { return (type == PathComputationType.WATER && state.getFluidState().is(FluidTags.WATER)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) { return new ShelfBlockEntity(worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(new Property[] { FACING, POWERED, SIDE_CHAIN_PART, WATERLOGGED }); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 106 */     Containers.updateNeighboursAfterDestroy(state, level, pos);
/* 107 */     updateNeighborsAfterPoweringDown(level, pos, state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 112 */     if (level.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 116 */     boolean signal = level.hasNeighborSignal(pos);
/* 117 */     if (((Boolean)state.getValue(POWERED)).booleanValue() != signal) {
/*     */       
/* 119 */       BlockState newState = (BlockState)state.setValue(POWERED, Boolean.valueOf(signal));
/* 120 */       if (!signal) {
/* 121 */         newState = (BlockState)newState.setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED);
/*     */       }
/*     */       
/* 124 */       level.setBlock(pos, newState, 3);
/* 125 */       playSound(level, pos, signal ? SoundEvents.SHELF_ACTIVATE : SoundEvents.SHELF_DEACTIVATE);
/* 126 */       level.gameEvent(signal ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Context.of(newState));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 132 */     FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
/* 133 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/* 134 */       .setValue(FACING, context.getHorizontalDirection().getOpposite()))
/* 135 */       .setValue(POWERED, Boolean.valueOf(context.getLevel().hasNeighborSignal(context.getClickedPos()))))
/* 136 */       .setValue(WATERLOGGED, Boolean.valueOf((replacedFluidState.getType() == Fluids.WATER)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public BlockState rotate(BlockState state, Rotation rotation) { return (BlockState)state.setValue(FACING, rotation.rotate((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation((Direction)state.getValue(FACING))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public int getRows() { return 1; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public int getColumns() { return 3; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/* 163 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof ShelfBlockEntity) { ShelfBlockEntity shelfBlockEntity = (ShelfBlockEntity)blockEntity; if (!hand.equals(InteractionHand.OFF_HAND)) {
/*     */ 
/*     */ 
/*     */         
/* 167 */         OptionalInt hitSlot = getHitSlot(hitResult, (Direction)state.getValue(FACING));
/* 168 */         if (hitSlot.isEmpty()) {
/* 169 */           return InteractionResult.PASS;
/*     */         }
/*     */         
/* 172 */         Inventory inventory = player.getInventory();
/* 173 */         if (level.isClientSide()) {
/* 174 */           return inventory.getSelectedItem().isEmpty() ? InteractionResult.PASS : InteractionResult.SUCCESS;
/*     */         }
/*     */         
/* 177 */         if (!((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 178 */           boolean itemRemoved = swapSingleItem(itemStack, player, shelfBlockEntity, hitSlot.getAsInt(), inventory);
/*     */           
/* 180 */           if (itemRemoved) {
/* 181 */             playSound(level, pos, itemStack.isEmpty() ? SoundEvents.SHELF_TAKE_ITEM : SoundEvents.SHELF_SINGLE_SWAP);
/* 182 */           } else if (!itemStack.isEmpty()) {
/* 183 */             playSound(level, pos, SoundEvents.SHELF_PLACE_ITEM);
/*     */           } else {
/* 185 */             return InteractionResult.PASS;
/*     */           } 
/*     */           
/* 188 */           return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
/*     */         } 
/* 190 */         ItemStack previousItem = inventory.getSelectedItem();
/* 191 */         boolean anySwapped = swapHotbar(level, pos, inventory);
/* 192 */         if (!anySwapped) {
/* 193 */           return InteractionResult.CONSUME;
/*     */         }
/* 195 */         playSound(level, pos, SoundEvents.SHELF_MULTI_SWAP);
/* 196 */         if (previousItem == inventory.getSelectedItem()) {
/* 197 */           return InteractionResult.SUCCESS;
/*     */         }
/* 199 */         return InteractionResult.SUCCESS.heldItemTransformedTo(inventory.getSelectedItem());
/*     */       }  }
/*     */     
/*     */     return InteractionResult.PASS;
/*     */   } private static boolean swapSingleItem(ItemStack itemStack, Player player, ShelfBlockEntity shelfBlockEntity, int hitSlot, Inventory inventory) {
/* 204 */     ItemStack removedItem = shelfBlockEntity.swapItemNoUpdate(hitSlot, itemStack);
/* 205 */     ItemStack newInventoryItem = (player.hasInfiniteMaterials() && removedItem.isEmpty()) ? itemStack.copy() : removedItem;
/*     */     
/* 207 */     inventory.setItem(inventory.getSelectedSlot(), newInventoryItem);
/*     */     
/* 209 */     inventory.setChanged();
/* 210 */     shelfBlockEntity.setChanged((newInventoryItem.has(DataComponents.USE_EFFECTS) && !((UseEffects)newInventoryItem.get(DataComponents.USE_EFFECTS)).interactVibrations()) ? null : GameEvent.ITEM_INTERACT_FINISH);
/*     */     
/* 212 */     return !removedItem.isEmpty();
/*     */   }
/*     */   
/*     */   private boolean swapHotbar(Level level, BlockPos pos, Inventory inventory) {
/* 216 */     List<BlockPos> connectedBlocks = getAllBlocksConnectedTo(level, pos);
/* 217 */     if (connectedBlocks.isEmpty()) {
/* 218 */       return false;
/*     */     }
/*     */     
/* 221 */     boolean anySwapped = false;
/* 222 */     for (int shelfPartIndex = 0; shelfPartIndex < connectedBlocks.size(); shelfPartIndex++) {
/* 223 */       ShelfBlockEntity shelfPart = (ShelfBlockEntity)level.getBlockEntity((BlockPos)connectedBlocks.get(shelfPartIndex));
/* 224 */       if (shelfPart != null) {
/*     */ 
/*     */ 
/*     */         
/* 228 */         for (int slot = 0; slot < shelfPart.getContainerSize(); slot++) {
/* 229 */           int inventorySlot = 9 - (connectedBlocks.size() - shelfPartIndex) * shelfPart.getContainerSize() + slot;
/* 230 */           if (inventorySlot >= 0 && inventorySlot <= inventory.getContainerSize()) {
/*     */ 
/*     */ 
/*     */             
/* 234 */             ItemStack placedInventoryItem = inventory.removeItemNoUpdate(inventorySlot);
/* 235 */             ItemStack removedShelfItem = shelfPart.swapItemNoUpdate(slot, placedInventoryItem);
/* 236 */             if (!placedInventoryItem.isEmpty() || !removedShelfItem.isEmpty()) {
/* 237 */               inventory.setItem(inventorySlot, removedShelfItem);
/* 238 */               anySwapped = true;
/*     */             } 
/*     */           } 
/*     */         } 
/* 242 */         inventory.setChanged();
/* 243 */         shelfPart.setChanged(GameEvent.ENTITY_INTERACT);
/*     */       } 
/* 245 */     }  return anySwapped;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 250 */   public SideChainPart getSideChainPart(BlockState state) { return (SideChainPart)state.getValue(SIDE_CHAIN_PART); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 255 */   public BlockState setSideChainPart(BlockState state, SideChainPart newPart) { return (BlockState)state.setValue(SIDE_CHAIN_PART, newPart); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 260 */   public Direction getFacing(BlockState state) { return (Direction)state.getValue(FACING); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 265 */   public boolean isConnectable(BlockState state) { return (state.is(BlockTags.WOODEN_SHELVES) && state.hasProperty(POWERED) && ((Boolean)state.getValue(POWERED)).booleanValue()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 270 */   public int getMaxChainLength() { return 3; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 275 */     if (((Boolean)state.getValue(POWERED)).booleanValue()) {
/* 276 */       updateSelfAndNeighborsOnPoweringUp(level, pos, state, oldState);
/*     */     } else {
/* 278 */       updateNeighborsAfterPoweringDown(level, pos, state);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 283 */   private void playSound(LevelAccessor level, BlockPos pos, SoundEvent sound) { level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 288 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 289 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 291 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 296 */     if (((Boolean)state.getValue(WATERLOGGED)).booleanValue()) {
/* 297 */       ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/* 299 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 304 */   protected boolean hasAnalogOutputSignal(BlockState state) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 309 */     if (level.isClientSide())
/*     */     {
/* 311 */       return 0;
/*     */     }
/*     */     
/* 314 */     if (direction != ((Direction)state.getValue(FACING)).getOpposite())
/*     */     {
/* 316 */       return 0;
/*     */     }
/*     */     
/* 319 */     BlockEntity blockEntity1 = level.getBlockEntity(pos); if (blockEntity1 instanceof ShelfBlockEntity) { ShelfBlockEntity blockEntity = (ShelfBlockEntity)blockEntity1;
/* 320 */       int item1Bit = blockEntity.getItem(0).isEmpty() ? 0 : 1;
/* 321 */       int item2Bit = blockEntity.getItem(1).isEmpty() ? 0 : 1;
/* 322 */       int item3Bit = blockEntity.getItem(2).isEmpty() ? 0 : 1;
/* 323 */       return item1Bit | item2Bit << 1 | item3Bit << 2; }
/*     */ 
/*     */     
/* 326 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ShelfBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */