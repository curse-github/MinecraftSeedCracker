/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DragonChargePlayerPhase
/*    */   extends AbstractDragonPhaseInstance {
/* 11 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static final int CHARGE_RECOVERY_TIME = 10;
/*    */   
/*    */   private Vec3 targetLocation;
/*    */   private int timeSinceCharge;
/*    */   
/* 18 */   public DragonChargePlayerPhase(EnderDragon dragon) { super(dragon); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 23 */     if (this.targetLocation == null) {
/* 24 */       LOGGER.warn("Aborting charge player as no target was set.");
/* 25 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     if (this.timeSinceCharge > 0 && 
/* 30 */       this.timeSinceCharge++ >= 10) {
/* 31 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 36 */     double distToTarget = this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 37 */     if (distToTarget < 100.0D || distToTarget > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/* 38 */       this.timeSinceCharge++;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 44 */     this.targetLocation = null;
/* 45 */     this.timeSinceCharge = 0;
/*    */   }
/*    */ 
/*    */   
/* 49 */   public void setTarget(Vec3 target) { this.targetLocation = target; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   public float getFlySpeed() { return 3.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public Vec3 getFlyTargetLocation() { return this.targetLocation; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public EnderDragonPhase<DragonChargePlayerPhase> getPhase() { return EnderDragonPhase.CHARGING_PLAYER; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonChargePlayerPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */