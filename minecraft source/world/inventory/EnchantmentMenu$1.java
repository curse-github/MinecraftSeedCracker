/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.world.SimpleContainer;
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
/*    */   extends SimpleContainer
/*    */ {
/* 36 */   null(int size) { super(size); }
/*    */   
/*    */   public void setChanged() {
/* 39 */     super.setChanged();
/* 40 */     EnchantmentMenu.this.slotsChanged(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\EnchantmentMenu$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */