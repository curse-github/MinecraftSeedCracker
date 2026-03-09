/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonSittingScanningPhase extends AbstractDragonSittingPhase {
/*    */   private static final int SITTING_SCANNING_IDLE_TICKS = 100;
/*    */   private static final int SITTING_ATTACK_Y_VIEW_RANGE = 10;
/*    */   private static final int SITTING_ATTACK_VIEW_RANGE = 20;
/*    */   private static final int SITTING_CHARGE_VIEW_RANGE = 150;
/* 16 */   private static final TargetingConditions CHARGE_TARGETING = TargetingConditions.forCombat().range(150.0D);
/*    */   
/*    */   private final TargetingConditions scanTargeting;
/*    */   private int scanningTime;
/*    */   
/*    */   public DragonSittingScanningPhase(EnderDragon dragon) {
/* 22 */     super(dragon);
/*    */     
/* 24 */     this.scanTargeting = TargetingConditions.forCombat().range(20.0D).selector((target, level) -> (Math.abs(target.getY() - dragon.getY()) <= 10.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 29 */     this.scanningTime++;
/* 30 */     Player player = level.getNearestPlayer(this.scanTargeting, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*    */     
/* 32 */     if (player != null) {
/* 33 */       if (this.scanningTime > 25) {
/* 34 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_ATTACKING);
/*    */       } else {
/* 36 */         Vec3 aim = (new Vec3(player.getX() - this.dragon.getX(), 0.0D, player.getZ() - this.dragon.getZ())).normalize();
/* 37 */         Vec3 dir = (new Vec3(Mth.sin((this.dragon.getYRot() * 0.017453292F)), 0.0D, -Mth.cos((this.dragon.getYRot() * 0.017453292F)))).normalize();
/* 38 */         float dot = (float)dir.dot(aim);
/* 39 */         float angle = (float)(Math.acos(dot) * 57.2957763671875D) + 0.5F;
/*    */         
/* 41 */         if (angle < 0.0F || angle > 10.0F) {
/* 42 */           double xAttackDist = player.getX() - this.dragon.head.getX();
/* 43 */           double zAttackDist = player.getZ() - this.dragon.head.getZ();
/* 44 */           double yRotDelta = Mth.clamp(Mth.wrapDegrees(180.0D - Mth.atan2(xAttackDist, zAttackDist) * 57.2957763671875D - this.dragon.getYRot()), -100.0D, 100.0D);
/*    */           
/* 46 */           this.dragon.yRotA *= 0.8F;
/*    */           
/* 48 */           float dist = (float)Math.sqrt(xAttackDist * xAttackDist + zAttackDist * zAttackDist) + 1.0F;
/* 49 */           float rotSpeed = dist;
/* 50 */           if (dist > 40.0F) {
/* 51 */             dist = 40.0F;
/*    */           }
/* 53 */           this.dragon.yRotA += (float)yRotDelta * 0.7F / dist / rotSpeed;
/* 54 */           this.dragon.setYRot(this.dragon.getYRot() + this.dragon.yRotA);
/*    */         } 
/*    */       } 
/* 57 */     } else if (this.scanningTime >= 100) {
/* 58 */       player = level.getNearestPlayer(CHARGE_TARGETING, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 59 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
/* 60 */       if (player != null) {
/* 61 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.CHARGING_PLAYER);
/* 62 */         ((DragonChargePlayerPhase)this.dragon.getPhaseManager().getPhase(EnderDragonPhase.CHARGING_PLAYER)).setTarget(new Vec3(player.getX(), player.getY(), player.getZ()));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public void begin() { this.scanningTime = 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public EnderDragonPhase<DragonSittingScanningPhase> getPhase() { return EnderDragonPhase.SITTING_SCANNING; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonSittingScanningPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */