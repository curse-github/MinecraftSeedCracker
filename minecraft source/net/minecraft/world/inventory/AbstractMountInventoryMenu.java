/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public abstract class AbstractMountInventoryMenu extends AbstractContainerMenu {
/*    */   protected final Container mountContainer;
/*    */   protected final LivingEntity mount;
/* 12 */   protected final int SLOT_SADDLE = 0;
/* 13 */   protected final int SLOT_BODY_ARMOR = 1;
/* 14 */   protected final int SLOT_INVENTORY_START = 2;
/*    */   protected static final int INVENTORY_ROWS = 3;
/*    */   
/*    */   protected AbstractMountInventoryMenu(int containerId, Inventory playerInventory, Container mountInventory, LivingEntity mount) {
/* 18 */     super(null, containerId);
/* 19 */     this.mountContainer = mountInventory;
/* 20 */     this.mount = mount;
/* 21 */     mountInventory.startOpen(playerInventory.player);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract boolean hasInventoryChanged(Container paramContainer);
/*    */ 
/*    */   
/* 28 */   public boolean stillValid(Player player) { return (!hasInventoryChanged(this.mountContainer) && this.mountContainer.stillValid(player) && this.mount.isAlive() && player.isWithinEntityInteractionRange(this.mount, 4.0D)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void removed(Player player) {
/* 33 */     super.removed(player);
/* 34 */     this.mountContainer.stopOpen(player);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 39 */     ItemStack clicked = ItemStack.EMPTY;
/* 40 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 41 */     if (slot != null && slot.hasItem()) {
/* 42 */       ItemStack stack = slot.getItem();
/* 43 */       clicked = stack.copy();
/*    */       
/* 45 */       int playerContainerStart = 2 + this.mountContainer.getContainerSize();
/*    */       
/* 47 */       if (slotIndex < playerContainerStart) {
/* 48 */         if (!moveItemStackTo(stack, playerContainerStart, this.slots.size(), true)) {
/* 49 */           return ItemStack.EMPTY;
/*    */         }
/* 51 */       } else if (getSlot(1).mayPlace(stack) && !getSlot(1).hasItem()) {
/* 52 */         if (!moveItemStackTo(stack, 1, 2, false)) {
/* 53 */           return ItemStack.EMPTY;
/*    */         }
/* 55 */       } else if (getSlot(0).mayPlace(stack) && !getSlot(0).hasItem()) {
/* 56 */         if (!moveItemStackTo(stack, 0, 1, false)) {
/* 57 */           return ItemStack.EMPTY;
/*    */         }
/* 59 */       } else if (this.mountContainer.getContainerSize() == 0 || !moveItemStackTo(stack, 2, playerContainerStart, false)) {
/* 60 */         int playerContainerEnd = playerContainerStart + 27;
/* 61 */         int playerHotBarStart = playerContainerEnd;
/* 62 */         int playerHotBarEnd = playerHotBarStart + 9;
/* 63 */         if (slotIndex >= playerHotBarStart && slotIndex < playerHotBarEnd) {
/* 64 */           if (!moveItemStackTo(stack, playerContainerStart, playerContainerEnd, false)) {
/* 65 */             return ItemStack.EMPTY;
/*    */           }
/* 67 */         } else if (slotIndex >= playerContainerStart && slotIndex < playerContainerEnd) {
/* 68 */           if (!moveItemStackTo(stack, playerHotBarStart, playerHotBarEnd, false)) {
/* 69 */             return ItemStack.EMPTY;
/*    */           }
/* 71 */         } else if (!moveItemStackTo(stack, playerHotBarStart, playerContainerEnd, false)) {
/* 72 */           return ItemStack.EMPTY;
/*    */         } 
/* 74 */         return ItemStack.EMPTY;
/*    */       } 
/* 76 */       if (stack.isEmpty()) {
/* 77 */         slot.setByPlayer(ItemStack.EMPTY);
/*    */       } else {
/* 79 */         slot.setChanged();
/*    */       } 
/*    */     } 
/* 82 */     return clicked;
/*    */   }
/*    */ 
/*    */   
/* 86 */   public static int getInventorySize(int inventoryColumns) { return inventoryColumns * 3; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\AbstractMountInventoryMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */