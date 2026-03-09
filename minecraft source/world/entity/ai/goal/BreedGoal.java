/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ 
/*    */ public class BreedGoal
/*    */   extends Goal
/*    */ {
/* 12 */   private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
/*    */   
/*    */   protected final Animal animal;
/*    */   
/*    */   private final Class<? extends Animal> partnerClass;
/*    */   protected final ServerLevel level;
/*    */   protected Animal partner;
/*    */   private int loveTime;
/*    */   private final double speedModifier;
/*    */   
/* 22 */   public BreedGoal(Animal animal, double speedModifier) { this(animal, speedModifier, animal.getClass()); }
/*    */ 
/*    */   
/*    */   public BreedGoal(Animal animal, double speedModifier, Class<? extends Animal> clazz) {
/* 26 */     this.animal = animal;
/* 27 */     this.level = getServerLevel(animal);
/* 28 */     this.partnerClass = clazz;
/* 29 */     this.speedModifier = speedModifier;
/* 30 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 35 */     if (!this.animal.isInLove()) {
/* 36 */       return false;
/*    */     }
/* 38 */     this.partner = getFreePartner();
/* 39 */     return (this.partner != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public boolean canContinueToUse() { return (this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60 && !this.partner.isPanicking()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 49 */     this.partner = null;
/* 50 */     this.loveTime = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 55 */     this.animal.getLookControl().setLookAt(this.partner, 10.0F, this.animal.getMaxHeadXRot());
/* 56 */     this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
/* 57 */     this.loveTime++;
/* 58 */     if (this.loveTime >= adjustedTickDelay(60) && this.animal.distanceToSqr(this.partner) < 9.0D) {
/* 59 */       breed();
/*    */     }
/*    */   }
/*    */   
/*    */   private Animal getFreePartner() {
/* 64 */     List<? extends Animal> animals = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0D));
/* 65 */     double dist = Double.MAX_VALUE;
/* 66 */     Animal partner = null;
/* 67 */     for (Animal potentialPartner : animals) {
/* 68 */       if (this.animal.canMate(potentialPartner) && !potentialPartner.isPanicking() && this.animal.distanceToSqr(potentialPartner) < dist) {
/* 69 */         partner = potentialPartner;
/* 70 */         dist = this.animal.distanceToSqr(potentialPartner);
/*    */       } 
/*    */     } 
/* 73 */     return partner;
/*    */   }
/*    */ 
/*    */   
/* 77 */   protected void breed() { this.animal.spawnChildFromBreeding(this.level, this.partner); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\BreedGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */