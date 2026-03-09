/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.CompoundContainer;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ChestMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class ChestBlockEntity
/*     */   extends RandomizableContainerBlockEntity implements LidBlockEntity {
/*     */   private static final int EVENT_SET_OPEN_COUNT = 1;
/*  31 */   private static final Component DEFAULT_NAME = Component.translatable("container.chest");
/*  32 */   private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
/*     */   
/*  34 */   private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter()
/*     */     {
/*     */       protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
/*  37 */         Block block = blockState.getBlock(); if (block instanceof ChestBlock) { ChestBlock chestBlock = (ChestBlock)block;
/*  38 */           ChestBlockEntity.playSound(level, pos, blockState, chestBlock.getOpenChestSound()); }
/*     */       
/*     */       }
/*     */ 
/*     */       
/*     */       protected void onClose(Level level, BlockPos pos, BlockState blockState) {
/*  44 */         Block block = blockState.getBlock(); if (block instanceof ChestBlock) { ChestBlock chestBlock = (ChestBlock)block;
/*  45 */           ChestBlockEntity.playSound(level, pos, blockState, chestBlock.getCloseChestSound()); }
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  51 */       protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) { ChestBlockEntity.this.signalOpenCount(level, pos, blockState, previous, current); }
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean isOwnContainer(Player player) {
/*  56 */         if (player.containerMenu instanceof ChestMenu) {
/*  57 */           Container container = ((ChestMenu)player.containerMenu).getContainer();
/*  58 */           return (container == ChestBlockEntity.this || (container instanceof CompoundContainer && ((CompoundContainer)container).contains(ChestBlockEntity.this)));
/*     */         } 
/*  60 */         return false;
/*     */       }
/*     */     };
/*     */   
/*  64 */   private final ChestLidController chestLidController = new ChestLidController();
/*     */ 
/*     */   
/*  67 */   protected ChestBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) { super(type, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public ChestBlockEntity(BlockPos worldPosition, BlockState blockState) { this(BlockEntityType.CHEST, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public int getContainerSize() { return 27; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  86 */     super.loadAdditional(input);
/*     */     
/*  88 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  89 */     if (!tryLoadLootTable(input)) {
/*  90 */       ContainerHelper.loadAllItems(input, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  96 */     super.saveAdditional(output);
/*     */     
/*  98 */     if (!trySaveLootTable(output)) {
/*  99 */       ContainerHelper.saveAllItems(output, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 104 */   public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, ChestBlockEntity entity) { entity.chestLidController.tickLid(); }
/*     */ 
/*     */   
/*     */   private static void playSound(Level level, BlockPos worldPosition, BlockState blockState, SoundEvent event) {
/* 108 */     ChestType type = (ChestType)blockState.getValue(ChestBlock.TYPE);
/* 109 */     if (type == ChestType.LEFT) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 114 */     double x = worldPosition.getX() + 0.5D;
/* 115 */     double y = worldPosition.getY() + 0.5D;
/* 116 */     double z = worldPosition.getZ() + 0.5D;
/*     */     
/* 118 */     if (type == ChestType.RIGHT) {
/* 119 */       Direction direction = ChestBlock.getConnectedDirection(blockState);
/* 120 */       x += direction.getStepX() * 0.5D;
/* 121 */       z += direction.getStepZ() * 0.5D;
/*     */     } 
/*     */     
/* 124 */     level.playSound(null, x, y, z, event, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(int b0, int b1) {
/* 129 */     if (b0 == 1) {
/* 130 */       this.chestLidController.shouldBeOpen((b1 > 0));
/* 131 */       return true;
/*     */     } 
/* 133 */     return super.triggerEvent(b0, b1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startOpen(ContainerUser containerUser) {
/* 138 */     if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
/* 139 */       this.openersCounter.incrementOpeners(containerUser.getLivingEntity(), getLevel(), getBlockPos(), getBlockState(), containerUser.getContainerInteractionRange());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopOpen(ContainerUser containerUser) {
/* 145 */     if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
/* 146 */       this.openersCounter.decrementOpeners(containerUser.getLivingEntity(), getLevel(), getBlockPos(), getBlockState());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public List<ContainerUser> getEntitiesWithContainerOpen() { return this.openersCounter.getEntitiesWithContainerOpen(getLevel(), getBlockPos()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   protected NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   protected void setItems(NonNullList<ItemStack> items) { this.items = items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public float getOpenNess(float a) { return this.chestLidController.getOpenness(a); }
/*     */ 
/*     */   
/*     */   public static int getOpenCount(BlockGetter level, BlockPos pos) {
/* 171 */     BlockState state = level.getBlockState(pos);
/* 172 */     if (state.hasBlockEntity()) {
/* 173 */       BlockEntity blockEntity = level.getBlockEntity(pos);
/* 174 */       if (blockEntity instanceof ChestBlockEntity) {
/* 175 */         return ((ChestBlockEntity)blockEntity).openersCounter.getOpenerCount();
/*     */       }
/*     */     } 
/* 178 */     return 0;
/*     */   }
/*     */   
/*     */   public static void swapContents(ChestBlockEntity one, ChestBlockEntity two) {
/* 182 */     NonNullList<ItemStack> items = one.getItems();
/* 183 */     one.setItems(two.getItems());
/* 184 */     two.setItems(items);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 189 */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return ChestMenu.threeRows(containerId, inventory, this); }
/*     */ 
/*     */   
/*     */   public void recheckOpen() {
/* 193 */     if (!this.remove) {
/* 194 */       this.openersCounter.recheckOpeners(getLevel(), getBlockPos(), getBlockState());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void signalOpenCount(Level level, BlockPos pos, BlockState blockState, int previous, int current) {
/* 199 */     Block block = blockState.getBlock();
/*     */     
/* 201 */     level.blockEvent(pos, block, 1, current);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ChestBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */