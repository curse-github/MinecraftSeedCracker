/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public interface RecipeInput {
/*    */   ItemStack getItem(int paramInt);
/*    */   
/*    */   int size();
/*    */   
/*    */   default boolean isEmpty() {
/* 11 */     for (int i = 0; i < size(); i++) {
/* 12 */       if (!getItem(i).isEmpty()) {
/* 13 */         return false;
/*    */       }
/*    */     } 
/* 16 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */