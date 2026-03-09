/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class OpenDoorGoal extends DoorInteractGoal {
/*    */   private final boolean closeDoor;
/*    */   private int forgetTime;
/*    */   
/*    */   public OpenDoorGoal(Mob mob, boolean closeDoorAfter) {
/* 10 */     super(mob);
/* 11 */     this.mob = mob;
/* 12 */     this.closeDoor = closeDoorAfter;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean canContinueToUse() { return (this.closeDoor && this.forgetTime > 0 && super.canContinueToUse()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {
/* 22 */     this.forgetTime = 20;
/* 23 */     setOpen(true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void stop() { setOpen(false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 33 */     this.forgetTime--;
/* 34 */     super.tick();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\OpenDoorGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */