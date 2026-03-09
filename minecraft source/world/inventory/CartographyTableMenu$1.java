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
/*    */ class null
/*    */   extends SimpleContainer
/*    */ {
/* 29 */   null(int size) { super(size); }
/*    */   
/*    */   public void setChanged() {
/* 32 */     CartographyTableMenu.this.slotsChanged(this);
/* 33 */     super.setChanged();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\inventory\CartographyTableMenu$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */