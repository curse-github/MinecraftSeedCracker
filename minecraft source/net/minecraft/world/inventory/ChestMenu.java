/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class ChestMenu
/*     */   extends AbstractContainerMenu {
/*     */   private final Container container;
/*     */   private final int containerRows;
/*     */   
/*  14 */   private ChestMenu(MenuType<?> menuType, int containerId, Inventory inventory, int rows) { this(menuType, containerId, inventory, new SimpleContainer(9 * rows), rows); }
/*     */ 
/*     */ 
/*     */   
/*  18 */   public static ChestMenu oneRow(int containerId, Inventory inventory) { return new ChestMenu(MenuType.GENERIC_9x1, containerId, inventory, 1); }
/*     */ 
/*     */ 
/*     */   
/*  22 */   public static ChestMenu twoRows(int containerId, Inventory inventory) { return new ChestMenu(MenuType.GENERIC_9x2, containerId, inventory, 2); }
/*     */ 
/*     */ 
/*     */   
/*  26 */   public static ChestMenu threeRows(int containerId, Inventory inventory) { return new ChestMenu(MenuType.GENERIC_9x3, containerId, inventory, 3); }
/*     */ 
/*     */ 
/*     */   
/*  30 */   public static ChestMenu fourRows(int containerId, Inventory inventory) { return new ChestMenu(MenuType.GENERIC_9x4, containerId, inventory, 4); }
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static ChestMenu fiveRows(int containerId, Inventory inventory) { return new ChestMenu(MenuType.GENERIC_9x5, containerId, inventory, 5); }
/*     */ 
/*     */ 
/*     */   
/*  38 */   public static ChestMenu sixRows(int containerId, Inventory inventory) { return new ChestMenu(MenuType.GENERIC_9x6, containerId, inventory, 6); }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static ChestMenu threeRows(int containerId, Inventory inventory, Container container) { return new ChestMenu(MenuType.GENERIC_9x3, containerId, inventory, container, 3); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static ChestMenu sixRows(int containerId, Inventory inventory, Container container) { return new ChestMenu(MenuType.GENERIC_9x6, containerId, inventory, container, 6); }
/*     */ 
/*     */   
/*     */   public ChestMenu(MenuType<?> menuType, int containerId, Inventory inventory, Container container, int rows) {
/*  50 */     super(menuType, containerId);
/*  51 */     checkContainerSize(container, rows * 9);
/*  52 */     this.container = container;
/*  53 */     this.containerRows = rows;
/*  54 */     container.startOpen(inventory.player);
/*     */     
/*  56 */     int chestGridTop = 18;
/*  57 */     addChestGrid(container, 8, 18);
/*     */     
/*  59 */     int inventoryTop = 18 + this.containerRows * 18 + 13;
/*  60 */     addStandardInventorySlots(inventory, 8, inventoryTop);
/*     */   }
/*     */   
/*     */   private void addChestGrid(Container container, int left, int top) {
/*  64 */     for (int y = 0; y < this.containerRows; y++) {
/*  65 */       for (int x = 0; x < 9; x++) {
/*  66 */         addSlot(new Slot(container, x + y * 9, left + x * 18, top + y * 18));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public boolean stillValid(Player player) { return this.container.stillValid(player); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/*  78 */     ItemStack clicked = ItemStack.EMPTY;
/*  79 */     Slot slot = (Slot)this.slots.get(slotIndex);
/*  80 */     if (slot != null && slot.hasItem()) {
/*  81 */       ItemStack stack = slot.getItem();
/*  82 */       clicked = stack.copy();
/*     */       
/*  84 */       if (slotIndex < this.containerRows * 9) {
/*  85 */         if (!moveItemStackTo(stack, this.containerRows * 9, this.slots.size(), true)) {
/*  86 */           return ItemStack.EMPTY;
/*     */         }
/*     */       }
/*  89 */       else if (!moveItemStackTo(stack, 0, this.containerRows * 9, false)) {
/*  90 */         return ItemStack.EMPTY;
/*     */       } 
/*     */       
/*  93 */       if (stack.isEmpty()) {
/*  94 */         slot.setByPlayer(ItemStack.EMPTY);
/*     */       } else {
/*  96 */         slot.setChanged();
/*     */       } 
/*     */     } 
/*  99 */     return clicked;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed(Player player) {
/* 104 */     super.removed(player);
/* 105 */     this.container.stopOpen(player);
/*     */   }
/*     */ 
/*     */   
/* 109 */   public Container getContainer() { return this.container; }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public int getRowCount() { return this.containerRows; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ChestMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */