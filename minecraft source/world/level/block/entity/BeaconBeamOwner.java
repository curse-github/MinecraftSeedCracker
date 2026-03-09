/*    */ package net.minecraft.world.level.block.entity;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface BeaconBeamOwner {
/*    */   List<Section> getBeamSections();
/*    */   
/*    */   public static class Section {
/*    */     private final int color;
/*    */     
/*    */     public Section(int color) {
/* 11 */       this.color = color;
/* 12 */       this.height = 1;
/*    */     }
/*    */     private int height;
/*    */     
/* 16 */     public void increaseHeight() { this.height++; }
/*    */ 
/*    */ 
/*    */     
/* 20 */     public int getColor() { return this.color; }
/*    */ 
/*    */ 
/*    */     
/* 24 */     public int getHeight() { return this.height; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BeaconBeamOwner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */