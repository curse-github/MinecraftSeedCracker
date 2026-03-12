/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.permissions.Permissions;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.item.component.ItemContainerContents;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ 
/*     */ public class BlockItem
/*     */   extends Item
/*     */ {
/*     */   @Deprecated
/*     */   private final Block block;
/*     */   
/*     */   public BlockItem(Block block, Item.Properties properties) {
/*  38 */     super(properties);
/*  39 */     this.block = block;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext context) {
/*  44 */     InteractionResult placeResult = place(new BlockPlaceContext(context));
/*     */     
/*  46 */     if (!placeResult.consumesAction() && context.getItemInHand().has(DataComponents.CONSUMABLE)) {
/*  47 */       return use(context.getLevel(), context.getPlayer(), context.getHand());
/*     */     }
/*  49 */     return placeResult;
/*     */   }
/*     */   
/*     */   public InteractionResult place(BlockPlaceContext placeContext) {
/*  53 */     if (!getBlock().isEnabled(placeContext.getLevel().enabledFeatures())) {
/*  54 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  57 */     if (!placeContext.canPlace()) {
/*  58 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  61 */     BlockPlaceContext updatedPlaceContext = updatePlacementContext(placeContext);
/*  62 */     if (updatedPlaceContext == null) {
/*  63 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  66 */     BlockState placementState = getPlacementState(updatedPlaceContext);
/*  67 */     if (placementState == null) {
/*  68 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  71 */     if (!placeBlock(updatedPlaceContext, placementState)) {
/*  72 */       return InteractionResult.FAIL;
/*     */     }
/*     */     
/*  75 */     BlockPos pos = updatedPlaceContext.getClickedPos();
/*  76 */     Level level = updatedPlaceContext.getLevel();
/*  77 */     Player player = updatedPlaceContext.getPlayer();
/*  78 */     ItemStack itemStack = updatedPlaceContext.getItemInHand();
/*     */ 
/*     */     
/*  81 */     BlockState placedState = level.getBlockState(pos);
/*  82 */     if (placedState.is(placementState.getBlock())) {
/*  83 */       placedState = updateBlockStateFromTag(pos, level, itemStack, placedState);
/*  84 */       updateCustomBlockEntityTag(pos, level, player, itemStack, placedState);
/*  85 */       updateBlockEntityComponents(level, pos, itemStack);
/*  86 */       placedState.getBlock().setPlacedBy(level, pos, placedState, player, itemStack);
/*  87 */       if (player instanceof ServerPlayer) {
/*  88 */         CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, pos, itemStack);
/*     */       }
/*     */     } 
/*  91 */     SoundType soundType = placedState.getSoundType();
/*  92 */     level.playSound(player, pos, getPlaceSound(placedState), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
/*  93 */     level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placedState));
/*  94 */     itemStack.consume(1, player);
/*  95 */     return InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*  99 */   protected SoundEvent getPlaceSound(BlockState blockState) { return blockState.getSoundType().getPlaceSound(); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) { return context; }
/*     */ 
/*     */   
/*     */   private static void updateBlockEntityComponents(Level level, BlockPos pos, ItemStack itemStack) {
/* 107 */     BlockEntity entity = level.getBlockEntity(pos);
/* 108 */     if (entity != null) {
/* 109 */       entity.applyComponentsFromItemStack(itemStack);
/* 110 */       entity.setChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 115 */   protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack itemStack, BlockState placedState) { return updateCustomBlockEntityTag(level, player, pos, itemStack); }
/*     */ 
/*     */   
/*     */   protected BlockState getPlacementState(BlockPlaceContext context) {
/* 119 */     BlockState stateForPlacement = getBlock().getStateForPlacement(context);
/* 120 */     return (stateForPlacement != null && canPlace(context, stateForPlacement)) ? stateForPlacement : null;
/*     */   }
/*     */   
/*     */   private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack itemStack, BlockState placedState) {
/* 124 */     BlockItemStateProperties blockState = (BlockItemStateProperties)itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
/* 125 */     if (blockState.isEmpty()) {
/* 126 */       return placedState;
/*     */     }
/* 128 */     BlockState modifiedState = blockState.apply(placedState);
/* 129 */     if (modifiedState != placedState) {
/* 130 */       level.setBlock(pos, modifiedState, 2);
/*     */     }
/* 132 */     return modifiedState;
/*     */   }
/*     */   
/*     */   protected boolean canPlace(BlockPlaceContext context, BlockState stateForPlacement) {
/* 136 */     Player player = context.getPlayer();
/* 137 */     return ((!mustSurvive() || stateForPlacement.canSurvive(context.getLevel(), context.getClickedPos())) && context
/* 138 */       .getLevel().isUnobstructed(stateForPlacement, context.getClickedPos(), CollisionContext.placementContext(player)));
/*     */   }
/*     */ 
/*     */   
/* 142 */   protected boolean mustSurvive() { return true; }
/*     */ 
/*     */ 
/*     */   
/* 146 */   protected boolean placeBlock(BlockPlaceContext context, BlockState placementState) { return context.getLevel().setBlock(context.getClickedPos(), placementState, 11); }
/*     */ 
/*     */   
/*     */   public static boolean updateCustomBlockEntityTag(Level level, Player player, BlockPos pos, ItemStack itemStack) {
/* 150 */     if (level.isClientSide()) {
/* 151 */       return false;
/*     */     }
/*     */     
/* 154 */     TypedEntityData<BlockEntityType<?>> customData = (TypedEntityData)itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/* 155 */     if (customData != null) {
/* 156 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/*     */       
/* 158 */       if (blockEntity != null) {
/* 159 */         BlockEntityType<?> type = blockEntity.getType();
/* 160 */         if (type != customData.type()) {
/* 161 */           return false;
/*     */         }
/* 163 */         if (type.onlyOpCanSetNbt() && (player == null || !player.canUseGameMasterBlocks())) {
/* 164 */           return false;
/*     */         }
/* 166 */         return customData.loadInto(blockEntity, level.registryAccess());
/*     */       } 
/*     */     } 
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldPrintOpWarning(ItemStack stack, Player player) {
/* 174 */     if (player != null && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
/* 175 */       TypedEntityData<BlockEntityType<?>> blockEntityData = (TypedEntityData)stack.get(DataComponents.BLOCK_ENTITY_DATA);
/* 176 */       if (blockEntityData != null) {
/* 177 */         return ((BlockEntityType)blockEntityData.type()).onlyOpCanSetNbt();
/*     */       }
/*     */     } 
/*     */     
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 185 */   public Block getBlock() { return this.block; }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public void registerBlocks(Map<Block, Item> map, Item item) { map.put(getBlock(), item); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   public boolean canFitInsideContainerItems() { return !(getBlock() instanceof net.minecraft.world.level.block.ShulkerBoxBlock); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDestroyed(ItemEntity entity) {
/* 200 */     ItemContainerContents container = (ItemContainerContents)entity.getItem().set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
/* 201 */     if (container != null) {
/* 202 */       ItemUtils.onContainerDestroyed(entity, container.nonEmptyItemsCopy());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setBlockEntityData(ItemStack stack, BlockEntityType<?> type, TagValueOutput output) {
/* 207 */     output.discard("id");
/* 208 */     if (output.isEmpty()) {
/* 209 */       stack.remove(DataComponents.BLOCK_ENTITY_DATA);
/*     */     } else {
/* 211 */       BlockEntity.addEntityType(output, type);
/* 212 */       stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(type, output.buildResult()));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 218 */   public FeatureFlagSet requiredFeatures() { return getBlock().requiredFeatures(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\BlockItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */