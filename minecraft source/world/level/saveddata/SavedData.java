/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ public abstract class SavedData
/*    */ {
/*    */   private boolean dirty;
/*    */   
/*  7 */   public void setDirty() { setDirty(true); }
/*    */ 
/*    */ 
/*    */   
/* 11 */   public void setDirty(boolean dirty) { this.dirty = dirty; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public boolean isDirty() { return this.dirty; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\saveddata\SavedData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */