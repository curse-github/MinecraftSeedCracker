/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Section
/*    */ {
/*    */   private final int color;
/*    */   private int height;
/*    */   
/*    */   public Section(int color) {
/* 11 */     this.color = color;
/* 12 */     this.height = 1;
/*    */   }
/*    */ 
/*    */   
/* 16 */   public void increaseHeight() { this.height++; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public int getColor() { return this.color; }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public int getHeight() { return this.height; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BeaconBeamOwner$Section.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */