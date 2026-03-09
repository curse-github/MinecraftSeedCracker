/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class JumpControl
/*    */   implements Control {
/*    */   private final Mob mob;
/*    */   protected boolean jump;
/*    */   
/* 10 */   public JumpControl(Mob mob) { this.mob = mob; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public void jump() { this.jump = true; }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 18 */     this.mob.setJumping(this.jump);
/* 19 */     this.jump = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\control\JumpControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */