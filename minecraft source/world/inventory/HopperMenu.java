/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class HopperMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   public static final int CONTAINER_SIZE = 5;
/*    */   private final Container hopper;
/*    */   
/* 16 */   public HopperMenu(int containerId, Inventory inventory) { this(containerId, inventory, new SimpleContainer(5)); }
/*    */ 
/*    */   
/*    */   public HopperMenu(int containerId, Inventory inventory, Container hopper) {
/* 20 */     super(MenuType.HOPPER, containerId);
/* 21 */     this.hopper = hopper;
/* 22 */     checkContainerSize(hopper, 5);
/*    */     
/* 24 */     hopper.startOpen(inventory.player);
/*    */     
/* 26 */     for (int x = 0; x < 5; x++) {
/* 27 */       addSlot(new Slot(hopper, x, 44 + x * 18, 20));
/*    */     }
/*    */     
/* 30 */     addStandardInventorySlots(inventory, 8, 51);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean stillValid(Player player) { return this.hopper.stillValid(player); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 40 */     ItemStack clicked = ItemStack.EMPTY;
/* 41 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 42 */     if (slot != null && slot.hasItem()) {
/* 43 */       ItemStack stack = slot.getItem();
/* 44 */       clicked = stack.copy();
/*    */       
/* 46 */       if (slotIndex < this.hopper.getContainerSize()) {
/* 47 */         if (!moveItemStackTo(stack, this.hopper.getContainerSize(), this.slots.size(), true)) {
/* 48 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 51 */       else if (!moveItemStackTo(stack, 0, this.hopper.getContainerSize(), false)) {
/* 52 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 55 */       if (stack.isEmpty()) {
/* 56 */         slot.setByPlayer(ItemStack.EMPTY);
/*    */       } else {
/* 58 */         slot.setChanged();
/*    */       } 
/*    */     } 
/* 61 */     return clicked;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player player) {
/* 66 */     super.removed(player);
/* 67 */     this.hopper.stopOpen(player);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\HopperMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */