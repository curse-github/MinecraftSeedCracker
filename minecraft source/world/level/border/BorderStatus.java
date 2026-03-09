/*    */ package net.minecraft.world.level.border;
/*    */ 
/*    */ public static enum BorderStatus {
/*  4 */   GROWING(4259712),
/*  5 */   SHRINKING(16724016),
/*  6 */   STATIONARY(2138367);
/*    */ 
/*    */   
/*    */   private final int color;
/*    */ 
/*    */   
/* 12 */   BorderStatus(int color) { this.color = color; }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public int getColor() { return this.color; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\border\BorderStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */