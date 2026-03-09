/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class ShulkerBoxMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   private static final int CONTAINER_SIZE = 27;
/*    */   private final Container container;
/*    */   
/* 16 */   public ShulkerBoxMenu(int containerId, Inventory inventory) { this(containerId, inventory, new SimpleContainer(27)); }
/*    */ 
/*    */   
/*    */   public ShulkerBoxMenu(int containerId, Inventory inventory, Container container) {
/* 20 */     super(MenuType.SHULKER_BOX, containerId);
/* 21 */     checkContainerSize(container, 27);
/* 22 */     this.container = container;
/* 23 */     container.startOpen(inventory.player);
/*    */     
/* 25 */     int rows = 3;
/* 26 */     int columns = 9;
/*    */     
/* 28 */     for (int y = 0; y < 3; y++) {
/* 29 */       for (int x = 0; x < 9; x++) {
/* 30 */         addSlot(new ShulkerBoxSlot(container, x + y * 9, 8 + x * 18, 18 + y * 18));
/*    */       }
/*    */     } 
/*    */     
/* 34 */     addStandardInventorySlots(inventory, 8, 84);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public boolean stillValid(Player player) { return this.container.stillValid(player); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 44 */     ItemStack clicked = ItemStack.EMPTY;
/* 45 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 46 */     if (slot != null && slot.hasItem()) {
/* 47 */       ItemStack stack = slot.getItem();
/* 48 */       clicked = stack.copy();
/*    */       
/* 50 */       if (slotIndex < this.container.getContainerSize()) {
/* 51 */         if (!moveItemStackTo(stack, this.container.getContainerSize(), this.slots.size(), true)) {
/* 52 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 55 */       else if (!moveItemStackTo(stack, 0, this.container.getContainerSize(), false)) {
/* 56 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 59 */       if (stack.isEmpty()) {
/* 60 */         slot.setByPlayer(ItemStack.EMPTY);
/*    */       } else {
/* 62 */         slot.setChanged();
/*    */       } 
/*    */     } 
/* 65 */     return clicked;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player player) {
/* 70 */     super.removed(player);
/* 71 */     this.container.stopOpen(player);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ShulkerBoxMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */