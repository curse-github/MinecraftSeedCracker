/*     */ package net.minecraft.world.entity.ai.goal.target;
/*     */ 
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TargetGoal
/*     */   extends Goal
/*     */ {
/*     */   private static final int EMPTY_REACH_CACHE = 0;
/*     */   private static final int CAN_REACH_CACHE = 1;
/*     */   private static final int CANT_REACH_CACHE = 2;
/*     */   protected final Mob mob;
/*     */   protected final boolean mustSee;
/*     */   private final boolean mustReach;
/*     */   private int reachCache;
/*     */   private int reachCacheTime;
/*     */   private int unseenTicks;
/*     */   protected LivingEntity targetMob;
/*     */   protected int unseenMemoryTicks;
/*     */   
/*  30 */   public TargetGoal(Mob mob, boolean mustSee) { this(mob, mustSee, false); }
/*     */   
/*     */   public TargetGoal(Mob mob, boolean mustSee, boolean mustReach) {
/*     */     this.unseenMemoryTicks = 60;
/*  34 */     this.mob = mob;
/*  35 */     this.mustSee = mustSee;
/*  36 */     this.mustReach = mustReach;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  41 */     LivingEntity target = this.mob.getTarget();
/*  42 */     if (target == null) {
/*  43 */       target = this.targetMob;
/*     */     }
/*  45 */     if (target == null) {
/*  46 */       return false;
/*     */     }
/*  48 */     if (!this.mob.canAttack(target)) {
/*  49 */       return false;
/*     */     }
/*     */     
/*  52 */     PlayerTeam playerTeam1 = this.mob.getTeam();
/*  53 */     PlayerTeam playerTeam2 = target.getTeam();
/*  54 */     if (playerTeam1 != null && playerTeam2 == playerTeam1) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     double within = getFollowDistance();
/*  59 */     if (this.mob.distanceToSqr(target) > within * within) {
/*  60 */       return false;
/*     */     }
/*  62 */     if (this.mustSee) {
/*  63 */       if (this.mob.getSensing().hasLineOfSight(target)) {
/*  64 */         this.unseenTicks = 0;
/*     */       }
/*  66 */       else if (++this.unseenTicks > reducedTickDelay(this.unseenMemoryTicks)) {
/*  67 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*  71 */     this.mob.setTarget(target);
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   
/*  76 */   protected double getFollowDistance() { return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  81 */     this.reachCache = 0;
/*  82 */     this.reachCacheTime = 0;
/*  83 */     this.unseenTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  88 */     this.mob.setTarget(null);
/*  89 */     this.targetMob = null;
/*     */   }
/*     */   
/*     */   protected boolean canAttack(LivingEntity target, TargetingConditions targetConditions) {
/*  93 */     if (target == null) {
/*  94 */       return false;
/*     */     }
/*  96 */     if (!targetConditions.test(getServerLevel(this.mob), this.mob, target)) {
/*  97 */       return false;
/*     */     }
/*  99 */     if (!this.mob.isWithinHome(target.blockPosition())) {
/* 100 */       return false;
/*     */     }
/*     */     
/* 103 */     if (this.mustReach) {
/* 104 */       if (--this.reachCacheTime <= 0) {
/* 105 */         this.reachCache = 0;
/*     */       }
/* 107 */       if (this.reachCache == 0) {
/* 108 */         this.reachCache = canReach(target) ? 1 : 2;
/*     */       }
/* 110 */       if (this.reachCache == 2) {
/* 111 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canReach(LivingEntity target) {
/* 119 */     this.reachCacheTime = reducedTickDelay(10 + this.mob.getRandom().nextInt(5));
/* 120 */     Path path = this.mob.getNavigation().createPath(target, 0);
/* 121 */     if (path == null) {
/* 122 */       return false;
/*     */     }
/* 124 */     Node last = path.getEndNode();
/* 125 */     if (last == null) {
/* 126 */       return false;
/*     */     }
/* 128 */     int xx = last.x - target.getBlockX();
/* 129 */     int zz = last.z - target.getBlockZ();
/* 130 */     return ((xx * xx + zz * zz) <= 2.25D);
/*     */   }
/*     */   
/*     */   public TargetGoal setUnseenMemoryTicks(int unseenMemoryTicks) {
/* 134 */     this.unseenMemoryTicks = unseenMemoryTicks;
/* 135 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\TargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */