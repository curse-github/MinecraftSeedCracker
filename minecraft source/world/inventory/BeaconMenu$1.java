/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.SimpleContainer;
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
/*    */ class null
/*    */   extends SimpleContainer
/*    */ {
/* 30 */   null(BeaconMenu this$0, int size) { super(size); }
/*    */ 
/*    */   
/* 33 */   public boolean canPlaceItem(int slot, ItemStack itemStack) { return itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public int getMaxStackSize() { return 1; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\BeaconMenu$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */