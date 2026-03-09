/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ 
/*     */ public abstract class AbstractSchoolingFish
/*     */   extends AbstractFish
/*     */ {
/*     */   private AbstractSchoolingFish leader;
/*  17 */   private int schoolSize = 1;
/*     */ 
/*     */   
/*  20 */   public AbstractSchoolingFish(EntityType<? extends AbstractSchoolingFish> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  25 */     super.registerGoals();
/*     */     
/*  27 */     this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  32 */   public int getMaxSpawnClusterSize() { return getMaxSchoolSize(); }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public int getMaxSchoolSize() { return super.getMaxSpawnClusterSize(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   protected boolean canRandomSwim() { return !isFollower(); }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public boolean isFollower() { return (this.leader != null && this.leader.isAlive()); }
/*     */ 
/*     */   
/*     */   public AbstractSchoolingFish startFollowing(AbstractSchoolingFish leader) {
/*  49 */     this.leader = leader;
/*  50 */     leader.addFollower();
/*     */     
/*  52 */     return leader;
/*     */   }
/*     */   
/*     */   public void stopFollowing() {
/*  56 */     this.leader.removeFollower();
/*  57 */     this.leader = null;
/*     */   }
/*     */ 
/*     */   
/*  61 */   private void addFollower() { this.schoolSize++; }
/*     */ 
/*     */ 
/*     */   
/*  65 */   private void removeFollower() { this.schoolSize--; }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public boolean canBeFollowed() { return (hasFollowers() && this.schoolSize < getMaxSchoolSize()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  74 */     super.tick();
/*     */ 
/*     */     
/*  77 */     if (hasFollowers() && (level()).random.nextInt(200) == 1) {
/*  78 */       List<? extends AbstractFish> neighbors = level().getEntitiesOfClass(getClass(), getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/*  79 */       if (neighbors.size() <= 1) {
/*  80 */         this.schoolSize = 1;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  86 */   public boolean hasFollowers() { return (this.schoolSize > 1); }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public boolean inRangeOfLeader() { return (distanceToSqr(this.leader) <= 121.0D); }
/*     */ 
/*     */   
/*     */   public void pathToLeader() {
/*  94 */     if (isFollower()) {
/*  95 */       getNavigation().moveTo(this.leader, 1.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 100 */   public void addFollowers(Stream<? extends AbstractSchoolingFish> abstractSchoolingFishStream) { abstractSchoolingFishStream.limit((getMaxSchoolSize() - this.schoolSize)).filter(f -> (f != this)).forEach(otherFish -> otherFish.startFollowing(this)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 105 */     super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */     
/* 107 */     if (groupData == null) {
/* 108 */       groupData = new SchoolSpawnGroupData(this);
/*     */     } else {
/* 110 */       startFollowing(((SchoolSpawnGroupData)groupData).leader);
/*     */     } 
/*     */     
/* 113 */     return groupData;
/*     */   }
/*     */   
/*     */   public static class SchoolSpawnGroupData
/*     */     implements SpawnGroupData {
/*     */     public final AbstractSchoolingFish leader;
/*     */     
/* 120 */     public SchoolSpawnGroupData(AbstractSchoolingFish leader) { this.leader = leader; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\fish\AbstractSchoolingFish.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */