/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.SimpleContainer;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DispenserMenu
/*    */   extends AbstractContainerMenu
/*    */ {
/*    */   private static final int SLOT_COUNT = 9;
/*    */   private static final int INV_SLOT_START = 9;
/*    */   private static final int INV_SLOT_END = 36;
/*    */   private static final int USE_ROW_SLOT_START = 36;
/*    */   private static final int USE_ROW_SLOT_END = 45;
/*    */   private final Container dispenser;
/*    */   
/* 21 */   public DispenserMenu(int containerId, Inventory inventory) { this(containerId, inventory, new SimpleContainer(9)); }
/*    */ 
/*    */   
/*    */   public DispenserMenu(int containerId, Inventory inventory, Container dispenser) {
/* 25 */     super(MenuType.GENERIC_3x3, containerId);
/* 26 */     checkContainerSize(dispenser, 9);
/* 27 */     this.dispenser = dispenser;
/* 28 */     dispenser.startOpen(inventory.player);
/*    */     
/* 30 */     add3x3GridSlots(dispenser, 62, 17);
/*    */     
/* 32 */     addStandardInventorySlots(inventory, 8, 84);
/*    */   }
/*    */   
/*    */   protected void add3x3GridSlots(Container container, int left, int top) {
/* 36 */     for (int y = 0; y < 3; y++) {
/* 37 */       for (int x = 0; x < 3; x++) {
/* 38 */         int slot = x + y * 3;
/* 39 */         addSlot(new Slot(container, slot, left + x * 18, top + y * 18));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public boolean stillValid(Player player) { return this.dispenser.stillValid(player); }
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemStack quickMoveStack(Player player, int slotIndex) {
/* 51 */     ItemStack clicked = ItemStack.EMPTY;
/* 52 */     Slot slot = (Slot)this.slots.get(slotIndex);
/* 53 */     if (slot != null && slot.hasItem()) {
/* 54 */       ItemStack stack = slot.getItem();
/* 55 */       clicked = stack.copy();
/*    */       
/* 57 */       if (slotIndex < 9) {
/* 58 */         if (!moveItemStackTo(stack, 9, 45, true)) {
/* 59 */           return ItemStack.EMPTY;
/*    */         }
/*    */       }
/* 62 */       else if (!moveItemStackTo(stack, 0, 9, false)) {
/* 63 */         return ItemStack.EMPTY;
/*    */       } 
/*    */       
/* 66 */       if (stack.isEmpty()) {
/* 67 */         slot.setByPlayer(ItemStack.EMPTY);
/*    */       } else {
/* 69 */         slot.setChanged();
/*    */       } 
/* 71 */       if (stack.getCount() == clicked.getCount())
/*    */       {
/* 73 */         return ItemStack.EMPTY;
/*    */       }
/* 75 */       slot.onTake(player, stack);
/*    */     } 
/*    */     
/* 78 */     return clicked;
/*    */   }
/*    */ 
/*    */   
/*    */   public void removed(Player player) {
/* 83 */     super.removed(player);
/* 84 */     this.dispenser.stopOpen(player);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\DispenserMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */