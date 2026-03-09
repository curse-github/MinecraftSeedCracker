/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class ChestLidController {
/*    */   private boolean shouldBeOpen;
/*    */   private float openness;
/*    */   private float oOpenness;
/*    */   
/*    */   public void tickLid() {
/* 11 */     this.oOpenness = this.openness;
/*    */     
/* 13 */     float speed = 0.1F;
/*    */     
/* 15 */     if (!this.shouldBeOpen && this.openness > 0.0F) {
/* 16 */       this.openness = Math.max(this.openness - 0.1F, 0.0F);
/* 17 */     } else if (this.shouldBeOpen && this.openness < 1.0F) {
/* 18 */       this.openness = Math.min(this.openness + 0.1F, 1.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 23 */   public float getOpenness(float a) { return Mth.lerp(a, this.oOpenness, this.openness); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void shouldBeOpen(boolean shouldBeOpen) { this.shouldBeOpen = shouldBeOpen; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\ChestLidController.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */