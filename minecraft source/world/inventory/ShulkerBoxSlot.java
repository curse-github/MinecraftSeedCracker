/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ShulkerBoxSlot
/*    */   extends Slot {
/*  8 */   public ShulkerBoxSlot(Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 13 */   public boolean mayPlace(ItemStack itemStack) { return itemStack.getItem().canFitInsideContainerItems(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\ShulkerBoxSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */