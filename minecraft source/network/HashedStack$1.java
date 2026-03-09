/*    */ package net.minecraft.network;
/*    */ 
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
/*    */   implements HashedStack
/*    */ {
/* 17 */   public String toString() { return "<empty>"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public boolean matches(ItemStack stack, HashedPatchMap.HashGenerator hasher) { return stack.isEmpty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\HashedStack$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */