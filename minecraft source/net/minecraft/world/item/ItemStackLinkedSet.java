/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.Hash;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenCustomHashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public class ItemStackLinkedSet
/*    */ {
/* 10 */   private static final Hash.Strategy<? super ItemStack> TYPE_AND_TAG = new Hash.Strategy<ItemStack>()
/*    */     {
/*    */       public int hashCode(ItemStack item) {
/* 13 */         return ItemStack.hashItemAndComponents(item);
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 18 */       public boolean equals(ItemStack a, ItemStack b) { return (a == b || (a != null && b != null && a.isEmpty() == b.isEmpty() && ItemStack.isSameItemSameComponents(a, b))); }
/*    */     };
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static Set<ItemStack> createTypeAndComponentsSet() { return new ObjectLinkedOpenCustomHashSet(TYPE_AND_TAG); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ItemStackLinkedSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */