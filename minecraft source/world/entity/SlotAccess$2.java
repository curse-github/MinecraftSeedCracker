/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Predicate;
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
/*    */ class null
/*    */   implements SlotAccess
/*    */ {
/* 34 */   public ItemStack get() { return entity.getItemBySlot(slot); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean set(ItemStack itemStack) {
/* 39 */     if (!validator.test(itemStack)) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     entity.setItemSlot(slot, itemStack);
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\SlotAccess$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */