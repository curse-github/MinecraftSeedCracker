/*     */ package net.minecraft.world.entity.ai.goal.target;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ public class HurtByTargetGoal
/*     */   extends TargetGoal
/*     */ {
/*  20 */   private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */   
/*     */   private static final int ALERT_RANGE_Y = 10;
/*     */   
/*     */   private boolean alertSameType;
/*     */   
/*     */   private int timestamp;
/*     */   private final Class<?>[] toIgnoreDamage;
/*     */   private Class<?>[] toIgnoreAlert;
/*     */   
/*     */   public HurtByTargetGoal(PathfinderMob mob, Class... ignoreDamageFromTheseTypes) {
/*  31 */     super(mob, true);
/*  32 */     this.toIgnoreDamage = ignoreDamageFromTheseTypes;
/*  33 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  38 */     int timestamp = this.mob.getLastHurtByMobTimestamp();
/*  39 */     LivingEntity lastHurtByMob = this.mob.getLastHurtByMob();
/*     */     
/*  41 */     if (timestamp == this.timestamp || lastHurtByMob == null) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     if (lastHurtByMob.getType() == EntityType.PLAYER && ((Boolean)getServerLevel(this.mob).getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue())
/*     */     {
/*  47 */       return false;
/*     */     }
/*     */     
/*  50 */     for (Class<?> ignoreClass : this.toIgnoreDamage) {
/*  51 */       if (ignoreClass.isAssignableFrom(lastHurtByMob.getClass())) {
/*  52 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  56 */     return canAttack(lastHurtByMob, HURT_BY_TARGETING);
/*     */   }
/*     */   
/*     */   public HurtByTargetGoal setAlertOthers(Class... exceptTheseTypes) {
/*  60 */     this.alertSameType = true;
/*  61 */     this.toIgnoreAlert = exceptTheseTypes;
/*  62 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  67 */     this.mob.setTarget(this.mob.getLastHurtByMob());
/*  68 */     this.targetMob = this.mob.getTarget();
/*  69 */     this.timestamp = this.mob.getLastHurtByMobTimestamp();
/*  70 */     this.unseenMemoryTicks = 300;
/*     */     
/*  72 */     if (this.alertSameType) {
/*  73 */       alertOthers();
/*     */     }
/*     */     
/*  76 */     super.start();
/*     */   }
/*     */   
/*     */   protected void alertOthers() {
/*  80 */     double within = getFollowDistance();
/*     */     
/*  82 */     AABB searchAabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(within, 10.0D, within);
/*  83 */     List<? extends Mob> nearby = this.mob.level().getEntitiesOfClass(this.mob.getClass(), searchAabb, EntitySelector.NO_SPECTATORS);
/*  84 */     for (Mob other : nearby) {
/*  85 */       if (this.mob == other) {
/*     */         continue;
/*     */       }
/*  88 */       if (other.getTarget() != null) {
/*     */         continue;
/*     */       }
/*  91 */       if (this.mob instanceof TamableAnimal && ((TamableAnimal)this.mob).getOwner() != ((TamableAnimal)other).getOwner()) {
/*     */         continue;
/*     */       }
/*  94 */       if (other.isAlliedTo(this.mob.getLastHurtByMob())) {
/*     */         continue;
/*     */       }
/*  97 */       if (this.toIgnoreAlert != null) {
/*  98 */         boolean ignore = false;
/*  99 */         for (Class<?> ignoreClass : this.toIgnoreAlert) {
/* 100 */           if (other.getClass() == ignoreClass) {
/* 101 */             ignore = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 105 */         if (ignore) {
/*     */           continue;
/*     */         }
/*     */       } 
/*     */       
/* 110 */       alertOther(other, this.mob.getLastHurtByMob());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 115 */   protected void alertOther(Mob other, LivingEntity hurtByMob) { other.setTarget(hurtByMob); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\HurtByTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */