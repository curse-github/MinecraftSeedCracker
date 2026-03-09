/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.Hash;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements Hash.Strategy<ItemStack>
/*    */ {
/* 13 */   public int hashCode(ItemStack item) { return ItemStack.hashItemAndComponents(item); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public boolean equals(ItemStack a, ItemStack b) { return (a == b || (a != null && b != null && a.isEmpty() == b.isEmpty() && ItemStack.isSameItemSameComponents(a, b))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemStackLinkedSet$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */