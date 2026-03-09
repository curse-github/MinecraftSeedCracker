/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Slot
/*    */ {
/* 54 */   null(CartographyTableMenu this$0, Container container, int slot, int x, int y) { super(container, slot, x, y); }
/*    */ 
/*    */   
/* 57 */   public boolean mayPlace(ItemStack itemStack) { return itemStack.has(DataComponents.MAP_ID); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\CartographyTableMenu$3.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */