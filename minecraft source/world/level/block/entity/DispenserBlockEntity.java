/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.DispenserMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class DispenserBlockEntity extends RandomizableContainerBlockEntity {
/*     */   public static final int CONTAINER_SIZE = 9;
/*  18 */   private static final Component DEFAULT_NAME = Component.translatable("container.dispenser");
/*     */   
/*  20 */   private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
/*     */ 
/*     */   
/*  23 */   protected DispenserBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) { super(type, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */   
/*  27 */   public DispenserBlockEntity(BlockPos worldPosition, BlockState blockState) { this(BlockEntityType.DISPENSER, worldPosition, blockState); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   public int getContainerSize() { return 9; }
/*     */ 
/*     */   
/*     */   public int getRandomSlot(RandomSource random) {
/*  36 */     unpackLootTable(null);
/*  37 */     int replaceSlot = -1;
/*  38 */     int replaceOdds = 1;
/*     */     
/*  40 */     for (int i = 0; i < this.items.size(); i++) {
/*  41 */       if (!((ItemStack)this.items.get(i)).isEmpty() && random.nextInt(replaceOdds++) == 0) {
/*  42 */         replaceSlot = i;
/*     */       }
/*     */     } 
/*     */     
/*  46 */     return replaceSlot;
/*     */   }
/*     */   
/*     */   public ItemStack insertItem(ItemStack itemStack) {
/*  50 */     int maxStackSize = getMaxStackSize(itemStack);
/*  51 */     for (int i = 0; i < this.items.size(); i++) {
/*  52 */       ItemStack targetStack = (ItemStack)this.items.get(i);
/*  53 */       if (targetStack.isEmpty() || ItemStack.isSameItemSameComponents(itemStack, targetStack)) {
/*     */ 
/*     */         
/*  56 */         int transferCount = Math.min(itemStack.getCount(), maxStackSize - targetStack.getCount());
/*  57 */         if (transferCount > 0) {
/*  58 */           if (targetStack.isEmpty()) {
/*  59 */             setItem(i, itemStack.split(transferCount));
/*     */           } else {
/*  61 */             itemStack.shrink(transferCount);
/*  62 */             targetStack.grow(transferCount);
/*     */           } 
/*     */         }
/*  65 */         if (itemStack.isEmpty())
/*     */           break; 
/*     */       } 
/*     */     } 
/*  69 */     return itemStack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  74 */   protected Component getDefaultName() { return DEFAULT_NAME; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/*  79 */     super.loadAdditional(input);
/*     */     
/*  81 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/*  82 */     if (!tryLoadLootTable(input)) {
/*  83 */       ContainerHelper.loadAllItems(input, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/*  89 */     super.saveAdditional(output);
/*     */     
/*  91 */     if (!trySaveLootTable(output)) {
/*  92 */       ContainerHelper.saveAllItems(output, this.items);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected NonNullList<ItemStack> getItems() { return this.items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected void setItems(NonNullList<ItemStack> items) { this.items = items; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) { return new DispenserMenu(containerId, inventory, this); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\DispenserBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */