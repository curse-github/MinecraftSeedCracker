/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
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
/*    */ class null
/*    */   implements SlotAccess
/*    */ {
/* 19 */   public ItemStack get() { return (ItemStack)getter.get(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean set(ItemStack itemStack) {
/* 24 */     setter.accept(itemStack);
/* 25 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\SlotAccess$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */