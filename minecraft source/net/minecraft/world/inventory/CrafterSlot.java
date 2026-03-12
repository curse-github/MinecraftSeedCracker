/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CrafterSlot
/*    */   extends Slot {
/*    */   private final CrafterMenu menu;
/*    */   
/*    */   public CrafterSlot(Container container, int slot, int x, int y, CrafterMenu menu) {
/* 11 */     super(container, slot, x, y);
/* 12 */     this.menu = menu;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean mayPlace(ItemStack itemStack) { return (!this.menu.isSlotDisabled(this.index) && super.mayPlace(itemStack)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setChanged() {
/* 22 */     super.setChanged();
/* 23 */     this.menu.slotsChanged(this.container);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\CrafterSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */