/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NearestAttackableTargetGoal<T extends LivingEntity>
/*    */   extends TargetGoal
/*    */ {
/*    */   private static final int DEFAULT_RANDOM_INTERVAL = 10;
/*    */   protected final Class<T> targetType;
/*    */   protected final int randomInterval;
/*    */   protected LivingEntity target;
/*    */   protected TargetingConditions targetConditions;
/*    */   
/* 24 */   public NearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee) { this(mob, targetType, 10, mustSee, false, null); }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public NearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee, TargetingConditions.Selector selector) { this(mob, targetType, 10, mustSee, false, selector); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public NearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee, boolean mustReach) { this(mob, targetType, 10, mustSee, mustReach, null); }
/*    */ 
/*    */   
/*    */   public NearestAttackableTargetGoal(Mob mob, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, TargetingConditions.Selector selector) {
/* 36 */     super(mob, mustSee, mustReach);
/* 37 */     this.targetType = targetType;
/* 38 */     this.randomInterval = reducedTickDelay(randomInterval);
/* 39 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*    */     
/* 41 */     this.targetConditions = TargetingConditions.forCombat().range(getFollowDistance()).selector(selector);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 46 */     if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     findTarget();
/* 51 */     return (this.target != null);
/*    */   }
/*    */ 
/*    */   
/* 55 */   protected AABB getTargetSearchArea(double followDistance) { return this.mob.getBoundingBox().inflate(followDistance, followDistance, followDistance); }
/*    */ 
/*    */   
/*    */   protected void findTarget() {
/* 59 */     ServerLevel level = getServerLevel(this.mob);
/* 60 */     if (this.targetType == net.minecraft.world.entity.player.Player.class || this.targetType == net.minecraft.server.level.ServerPlayer.class) {
/* 61 */       this.target = level.getNearestPlayer(getTargetConditions(), this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*    */     } else {
/* 63 */       this.target = level.getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, getTargetSearchArea(getFollowDistance()), entity -> true), getTargetConditions(), this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 69 */     this.mob.setTarget(this.target);
/* 70 */     super.start();
/*    */   }
/*    */ 
/*    */   
/* 74 */   public void setTarget(LivingEntity target) { this.target = target; }
/*    */ 
/*    */ 
/*    */   
/* 78 */   private TargetingConditions getTargetConditions() { return this.targetConditions.range(getFollowDistance()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\NearestAttackableTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */