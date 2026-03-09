/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class WaterAvoidingRandomStrollGoal
/*    */   extends RandomStrollGoal
/*    */ {
/*    */   public static final float PROBABILITY = 0.001F;
/*    */   protected final float probability;
/*    */   
/* 14 */   public WaterAvoidingRandomStrollGoal(PathfinderMob mob, double speedModifier) { this(mob, speedModifier, 0.001F); }
/*    */ 
/*    */   
/*    */   public WaterAvoidingRandomStrollGoal(PathfinderMob mob, double speedModifier, float probability) {
/* 18 */     super(mob, speedModifier);
/* 19 */     this.probability = probability;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getPosition() {
/* 24 */     if (this.mob.isInWater()) {
/*    */       
/* 26 */       Vec3 pos = LandRandomPos.getPos(this.mob, 15, 7);
/* 27 */       return (pos == null) ? super.getPosition() : pos;
/*    */     } 
/* 29 */     if (this.mob.getRandom().nextFloat() >= this.probability) {
/* 30 */       return LandRandomPos.getPos(this.mob, 10, 7);
/*    */     }
/* 32 */     return super.getPosition();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\WaterAvoidingRandomStrollGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */