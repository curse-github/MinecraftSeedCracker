/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PatrollingMonster
/*     */   extends Monster
/*     */ {
/*     */   private static final boolean DEFAULT_PATROL_LEADER = false;
/*     */   private static final boolean DEFAULT_PATROLLING = false;
/*     */   private BlockPos patrolTarget;
/*     */   private boolean patrolLeader = false;
/*     */   private boolean patrolling = false;
/*     */   
/*  37 */   protected PatrollingMonster(EntityType<? extends PatrollingMonster> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  42 */     super.registerGoals();
/*  43 */     this.goalSelector.addGoal(4, new LongDistancePatrolGoal(this, 0.7D, 0.595D));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  48 */     super.addAdditionalSaveData(output);
/*     */     
/*  50 */     output.storeNullable("patrol_target", BlockPos.CODEC, this.patrolTarget);
/*     */     
/*  52 */     output.putBoolean("PatrolLeader", this.patrolLeader);
/*  53 */     output.putBoolean("Patrolling", this.patrolling);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  58 */     super.readAdditionalSaveData(input);
/*     */     
/*  60 */     this.patrolTarget = (BlockPos)input.read("patrol_target", BlockPos.CODEC).orElse(null);
/*     */     
/*  62 */     this.patrolLeader = input.getBooleanOr("PatrolLeader", false);
/*  63 */     this.patrolling = input.getBooleanOr("Patrolling", false);
/*     */   }
/*     */ 
/*     */   
/*  67 */   public boolean canBeLeader() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  74 */     if (spawnReason != EntitySpawnReason.PATROL && spawnReason != EntitySpawnReason.EVENT && spawnReason != EntitySpawnReason.STRUCTURE && 
/*  75 */       level.getRandom().nextFloat() < 0.06F && canBeLeader()) {
/*  76 */       this.patrolLeader = true;
/*     */     }
/*     */ 
/*     */     
/*  80 */     if (isPatrolLeader()) {
/*  81 */       setItemSlot(EquipmentSlot.HEAD, Raid.getOminousBannerInstance(registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
/*  82 */       setDropChance(EquipmentSlot.HEAD, 2.0F);
/*     */     } 
/*     */     
/*  85 */     if (spawnReason == EntitySpawnReason.PATROL) {
/*  86 */       this.patrolling = true;
/*     */     }
/*     */     
/*  89 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public static boolean checkPatrollingMonsterSpawnRules(EntityType<? extends PatrollingMonster> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  93 */     if (level.getBrightness(LightLayer.BLOCK, pos) > 8) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     return checkAnyLightMonsterSpawnRules(type, level, spawnReason, pos, random);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 102 */   public boolean removeWhenFarAway(double distSqr) { return (!this.patrolling || distSqr > 16384.0D); }
/*     */ 
/*     */   
/*     */   public void setPatrolTarget(BlockPos target) {
/* 106 */     this.patrolTarget = target;
/* 107 */     this.patrolling = true;
/*     */   }
/*     */ 
/*     */   
/* 111 */   public BlockPos getPatrolTarget() { return this.patrolTarget; }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public boolean hasPatrolTarget() { return (this.patrolTarget != null); }
/*     */ 
/*     */   
/*     */   public void setPatrolLeader(boolean isLeader) {
/* 119 */     this.patrolLeader = isLeader;
/* 120 */     this.patrolling = true;
/*     */   }
/*     */ 
/*     */   
/* 124 */   public boolean isPatrolLeader() { return this.patrolLeader; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public boolean canJoinPatrol() { return true; }
/*     */ 
/*     */   
/*     */   public void findPatrolTarget() {
/* 132 */     this.patrolTarget = blockPosition().offset(-500 + this.random.nextInt(1000), 0, -500 + this.random.nextInt(1000));
/* 133 */     this.patrolling = true;
/*     */   }
/*     */ 
/*     */   
/* 137 */   protected boolean isPatrolling() { return this.patrolling; }
/*     */ 
/*     */ 
/*     */   
/* 141 */   protected void setPatrolling(boolean value) { this.patrolling = value; }
/*     */   
/*     */   public static class LongDistancePatrolGoal<T extends PatrollingMonster>
/*     */     extends Goal
/*     */   {
/*     */     private static final int NAVIGATION_FAILED_COOLDOWN = 200;
/*     */     private final T mob;
/*     */     private final double speedModifier;
/*     */     private final double leaderSpeedModifier;
/*     */     private long cooldownUntil;
/*     */     
/*     */     public LongDistancePatrolGoal(T mob, double speedModifier, double leaderSpeedModifier) {
/* 153 */       this.mob = mob;
/* 154 */       this.speedModifier = speedModifier;
/* 155 */       this.leaderSpeedModifier = leaderSpeedModifier;
/* 156 */       this.cooldownUntil = -1L;
/* 157 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 162 */       boolean isOnCooldown = (this.mob.level().getGameTime() < this.cooldownUntil);
/* 163 */       return (this.mob.isPatrolling() && this.mob.getTarget() == null && !this.mob.hasControllingPassenger() && this.mob.hasPatrolTarget() && !isOnCooldown);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {}
/*     */ 
/*     */     
/*     */     public void tick() {
/* 176 */       boolean patrolLeader = this.mob.isPatrolLeader();
/* 177 */       PathNavigation navigation = this.mob.getNavigation();
/* 178 */       if (navigation.isDone()) {
/* 179 */         List<PatrollingMonster> companions = findPatrolCompanions();
/* 180 */         if (this.mob.isPatrolling() && companions.isEmpty()) {
/* 181 */           this.mob.setPatrolling(false);
/* 182 */         } else if (!patrolLeader || !this.mob.getPatrolTarget().closerToCenterThan(this.mob.position(), 10.0D)) {
/* 183 */           Vec3 longDistanceTarget = Vec3.atBottomCenterOf(this.mob.getPatrolTarget());
/*     */ 
/*     */           
/* 186 */           Vec3 selfVector = this.mob.position();
/* 187 */           Vec3 distance = selfVector.subtract(longDistanceTarget);
/*     */           
/* 189 */           longDistanceTarget = distance.yRot(90.0F).scale(0.4D).add(longDistanceTarget);
/*     */           
/* 191 */           Vec3 moveTarget = longDistanceTarget.subtract(selfVector).normalize().scale(10.0D).add(selfVector);
/* 192 */           BlockPos pathTarget = BlockPos.containing(moveTarget);
/* 193 */           pathTarget = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pathTarget);
/*     */           
/* 195 */           if (!navigation.moveTo(pathTarget.getX(), pathTarget.getY(), pathTarget.getZ(), patrolLeader ? this.leaderSpeedModifier : this.speedModifier)) {
/*     */             
/* 197 */             moveRandomly();
/* 198 */             this.cooldownUntil = this.mob.level().getGameTime() + 200L;
/* 199 */           } else if (patrolLeader) {
/* 200 */             for (PatrollingMonster companion : companions) {
/* 201 */               companion.setPatrolTarget(pathTarget);
/*     */             }
/*     */           } 
/*     */         } else {
/* 205 */           this.mob.findPatrolTarget();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 211 */     private List<PatrollingMonster> findPatrolCompanions() { return this.mob.level().getEntitiesOfClass(PatrollingMonster.class, this.mob.getBoundingBox().inflate(16.0D), mob -> (mob.canJoinPatrol() && !mob.is(this.mob))); }
/*     */ 
/*     */     
/*     */     private boolean moveRandomly() {
/* 215 */       RandomSource random = this.mob.getRandom();
/* 216 */       BlockPos pathTarget = this.mob.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + random.nextInt(16), 0, -8 + random.nextInt(16)));
/* 217 */       return this.mob.getNavigation().moveTo(pathTarget.getX(), pathTarget.getY(), pathTarget.getZ(), this.speedModifier);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\PatrollingMonster.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */