/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*    */ import net.minecraft.world.entity.ai.util.HoverRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class WaterAvoidingRandomFlyingGoal
/*    */   extends WaterAvoidingRandomStrollGoal
/*    */ {
/* 12 */   public WaterAvoidingRandomFlyingGoal(PathfinderMob mob, double speedModifier) { super(mob, speedModifier); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Vec3 getPosition() {
/* 17 */     Vec3 wanderDirection = this.mob.getViewVector(0.0F);
/*    */     
/* 19 */     int xzDist = 8;
/* 20 */     Vec3 groundBasedPosition = HoverRandomPos.getPos(this.mob, 8, 7, wanderDirection.x, wanderDirection.z, 1.5707964F, 3, 1);
/* 21 */     if (groundBasedPosition != null) {
/* 22 */       return groundBasedPosition;
/*    */     }
/*    */ 
/*    */     
/* 26 */     return AirAndWaterRandomPos.getPos(this.mob, 8, 4, -2, wanderDirection.x, wanderDirection.z, 1.5707963705062866D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\WaterAvoidingRandomFlyingGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */