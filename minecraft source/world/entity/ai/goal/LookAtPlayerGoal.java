/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LookAtPlayerGoal
/*     */   extends Goal
/*     */ {
/*     */   public static final float DEFAULT_PROBABILITY = 0.02F;
/*     */   protected final Mob mob;
/*     */   protected Entity lookAt;
/*     */   protected final float lookDistance;
/*     */   private int lookTime;
/*     */   protected final float probability;
/*     */   private final boolean onlyHorizontal;
/*     */   protected final Class<? extends LivingEntity> lookAtType;
/*     */   protected final TargetingConditions lookAtContext;
/*     */   
/*  28 */   public LookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) { this(mob, lookAtType, lookDistance, 0.02F); }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public LookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance, float probability) { this(mob, lookAtType, lookDistance, probability, false); }
/*     */ 
/*     */   
/*     */   public LookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance, float probability, boolean onlyHorizontal) {
/*  36 */     this.mob = mob;
/*  37 */     this.lookAtType = lookAtType;
/*  38 */     this.lookDistance = lookDistance;
/*  39 */     this.probability = probability;
/*  40 */     this.onlyHorizontal = onlyHorizontal;
/*  41 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     
/*  43 */     if (lookAtType == net.minecraft.world.entity.player.Player.class) {
/*  44 */       Predicate<Entity> selector = EntitySelector.notRiding(mob);
/*  45 */       this.lookAtContext = TargetingConditions.forNonCombat().range(lookDistance).selector((target, level) -> selector.test(target));
/*     */     } else {
/*  47 */       this.lookAtContext = TargetingConditions.forNonCombat().range(lookDistance);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  53 */     if (this.mob.getRandom().nextFloat() >= this.probability) {
/*  54 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  58 */     if (this.mob.getTarget() != null) {
/*  59 */       this.lookAt = this.mob.getTarget();
/*     */     }
/*     */     
/*  62 */     ServerLevel level = getServerLevel(this.mob);
/*  63 */     if (this.lookAtType == net.minecraft.world.entity.player.Player.class) {
/*  64 */       this.lookAt = level.getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*     */     } else {
/*  66 */       this.lookAt = level.getNearestEntity(this.mob.level().getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance), entity -> true), this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*     */     } 
/*     */     
/*  69 */     return (this.lookAt != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  74 */     if (!this.lookAt.isAlive()) {
/*  75 */       return false;
/*     */     }
/*  77 */     if (this.mob.distanceToSqr(this.lookAt) > (this.lookDistance * this.lookDistance)) {
/*  78 */       return false;
/*     */     }
/*  80 */     return (this.lookTime > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public void start() { this.lookTime = adjustedTickDelay(40 + this.mob.getRandom().nextInt(40)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public void stop() { this.lookAt = null; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  95 */     if (!this.lookAt.isAlive()) {
/*     */       return;
/*     */     }
/*  98 */     double targetY = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
/*  99 */     this.mob.getLookControl().setLookAt(this.lookAt.getX(), targetY, this.lookAt.getZ());
/* 100 */     this.lookTime--;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\LookAtPlayerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */