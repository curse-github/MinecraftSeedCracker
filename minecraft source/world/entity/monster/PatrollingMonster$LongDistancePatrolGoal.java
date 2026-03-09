/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LongDistancePatrolGoal<T extends PatrollingMonster>
/*     */   extends Goal
/*     */ {
/*     */   private static final int NAVIGATION_FAILED_COOLDOWN = 200;
/*     */   private final T mob;
/*     */   private final double speedModifier;
/*     */   private final double leaderSpeedModifier;
/*     */   private long cooldownUntil;
/*     */   
/*     */   public LongDistancePatrolGoal(T mob, double speedModifier, double leaderSpeedModifier) {
/* 153 */     this.mob = mob;
/* 154 */     this.speedModifier = speedModifier;
/* 155 */     this.leaderSpeedModifier = leaderSpeedModifier;
/* 156 */     this.cooldownUntil = -1L;
/* 157 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 162 */     boolean isOnCooldown = (this.mob.level().getGameTime() < this.cooldownUntil);
/* 163 */     return (this.mob.isPatrolling() && this.mob.getTarget() == null && !this.mob.hasControllingPassenger() && this.mob.hasPatrolTarget() && !isOnCooldown);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */   
/*     */   public void tick() {
/* 176 */     boolean patrolLeader = this.mob.isPatrolLeader();
/* 177 */     PathNavigation navigation = this.mob.getNavigation();
/* 178 */     if (navigation.isDone()) {
/* 179 */       List<PatrollingMonster> companions = findPatrolCompanions();
/* 180 */       if (this.mob.isPatrolling() && companions.isEmpty()) {
/* 181 */         this.mob.setPatrolling(false);
/* 182 */       } else if (!patrolLeader || !this.mob.getPatrolTarget().closerToCenterThan(this.mob.position(), 10.0D)) {
/* 183 */         Vec3 longDistanceTarget = Vec3.atBottomCenterOf(this.mob.getPatrolTarget());
/*     */ 
/*     */         
/* 186 */         Vec3 selfVector = this.mob.position();
/* 187 */         Vec3 distance = selfVector.subtract(longDistanceTarget);
/*     */         
/* 189 */         longDistanceTarget = distance.yRot(90.0F).scale(0.4D).add(longDistanceTarget);
/*     */         
/* 191 */         Vec3 moveTarget = longDistanceTarget.subtract(selfVector).normalize().scale(10.0D).add(selfVector);
/* 192 */         BlockPos pathTarget = BlockPos.containing(moveTarget);
/* 193 */         pathTarget = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pathTarget);
/*     */         
/* 195 */         if (!navigation.moveTo(pathTarget.getX(), pathTarget.getY(), pathTarget.getZ(), patrolLeader ? this.leaderSpeedModifier : this.speedModifier)) {
/*     */           
/* 197 */           moveRandomly();
/* 198 */           this.cooldownUntil = this.mob.level().getGameTime() + 200L;
/* 199 */         } else if (patrolLeader) {
/* 200 */           for (PatrollingMonster companion : companions) {
/* 201 */             companion.setPatrolTarget(pathTarget);
/*     */           }
/*     */         } 
/*     */       } else {
/* 205 */         this.mob.findPatrolTarget();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 211 */   private List<PatrollingMonster> findPatrolCompanions() { return this.mob.level().getEntitiesOfClass(PatrollingMonster.class, this.mob.getBoundingBox().inflate(16.0D), mob -> (mob.canJoinPatrol() && !mob.is(this.mob))); }
/*     */ 
/*     */   
/*     */   private boolean moveRandomly() {
/* 215 */     RandomSource random = this.mob.getRandom();
/* 216 */     BlockPos pathTarget = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + random.nextInt(16), 0, -8 + random.nextInt(16)));
/* 217 */     return this.mob.getNavigation().moveTo(pathTarget.getX(), pathTarget.getY(), pathTarget.getZ(), this.speedModifier);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\PatrollingMonster$LongDistancePatrolGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */