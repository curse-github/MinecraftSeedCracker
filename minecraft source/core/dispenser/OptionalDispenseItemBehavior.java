/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ 
/*    */ public abstract class OptionalDispenseItemBehavior
/*    */   extends DefaultDispenseItemBehavior
/*    */ {
/*    */   private boolean success = true;
/*    */   
/*  9 */   public boolean isSuccess() { return this.success; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public void setSuccess(boolean success) { this.success = success; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   protected void playSound(BlockSource source) { source.level().levelEvent(isSuccess() ? 1000 : 1001, source.pos(), 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\dispenser\OptionalDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */