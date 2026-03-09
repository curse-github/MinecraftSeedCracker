/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class AvoidEntityGoal<T extends LivingEntity>
/*    */   extends Goal
/*    */ {
/*    */   protected final PathfinderMob mob;
/*    */   private final double walkSpeedModifier;
/*    */   private final double sprintSpeedModifier;
/*    */   protected T toAvoid;
/*    */   protected final float maxDist;
/*    */   protected Path path;
/*    */   protected final PathNavigation pathNav;
/*    */   protected final Class<T> avoidClass;
/*    */   protected final Predicate<? super LivingEntity> avoidPredicate;
/*    */   protected final Predicate<? super LivingEntity> predicateOnAvoidEntity;
/*    */   private final TargetingConditions avoidEntityTargeting;
/*    */   
/* 30 */   public AvoidEntityGoal(PathfinderMob mob, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) { this(mob, avoidClass, t -> true, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR); }
/*    */ 
/*    */   
/*    */   public AvoidEntityGoal(PathfinderMob mob, Class<T> avoidClass, Predicate<LivingEntity> avoidPredicate, float maxDist, double walkSpeedModifier, double sprintSpeedModifier, Predicate<? super LivingEntity> predicateOnAvoidEntity) {
/* 34 */     this.mob = mob;
/* 35 */     this.avoidClass = avoidClass;
/* 36 */     this.avoidPredicate = avoidPredicate;
/* 37 */     this.maxDist = maxDist;
/* 38 */     this.walkSpeedModifier = walkSpeedModifier;
/* 39 */     this.sprintSpeedModifier = sprintSpeedModifier;
/* 40 */     this.predicateOnAvoidEntity = predicateOnAvoidEntity;
/* 41 */     this.pathNav = mob.getNavigation();
/* 42 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */     
/* 44 */     this.avoidEntityTargeting = TargetingConditions.forCombat().range(maxDist).selector((target, level) -> (predicateOnAvoidEntity.test(target) && avoidPredicate.test(target)));
/*    */   }
/*    */ 
/*    */   
/* 48 */   public AvoidEntityGoal(PathfinderMob mob, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier, Predicate<? super LivingEntity> predicateOnAvoidEntity) { this(mob, avoidClass, t -> true, maxDist, walkSpeedModifier, sprintSpeedModifier, predicateOnAvoidEntity); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 53 */     this.toAvoid = getServerLevel(this.mob).getNearestEntity(this.mob.level().getEntitiesOfClass(this.avoidClass, this.mob.getBoundingBox().inflate(this.maxDist, 3.0D, this.maxDist), entity -> true), this.avoidEntityTargeting, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
/* 54 */     if (this.toAvoid == null) {
/* 55 */       return false;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 61 */     Vec3 pos = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.position());
/* 62 */     if (pos == null) {
/* 63 */       return false;
/*    */     }
/* 65 */     if (this.toAvoid.distanceToSqr(pos.x, pos.y, pos.z) < this.toAvoid.distanceToSqr(this.mob)) {
/* 66 */       return false;
/*    */     }
/* 68 */     this.path = this.pathNav.createPath(pos.x, pos.y, pos.z, 0);
/* 69 */     return (this.path != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 74 */   public boolean canContinueToUse() { return !this.pathNav.isDone(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public void start() { this.pathNav.moveTo(this.path, this.walkSpeedModifier); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   public void stop() { this.toAvoid = null; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 89 */     if (this.mob.distanceToSqr(this.toAvoid) < 49.0D) {
/* 90 */       this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
/*    */     } else {
/* 92 */       this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\AvoidEntityGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */