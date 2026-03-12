/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
/*    */ 
/*    */ public class FollowFlockLeaderGoal
/*    */   extends Goal
/*    */ {
/*    */   private static final int INTERVAL_TICKS = 200;
/*    */   private final AbstractSchoolingFish mob;
/*    */   private int timeToRecalcPath;
/*    */   private int nextStartTick;
/*    */   
/*    */   public FollowFlockLeaderGoal(AbstractSchoolingFish mob) {
/* 17 */     this.mob = mob;
/* 18 */     this.nextStartTick = nextStartTick(mob);
/*    */   }
/*    */ 
/*    */   
/* 22 */   protected int nextStartTick(AbstractSchoolingFish mob) { return reducedTickDelay(200 + mob.getRandom().nextInt(200) % 20); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     if (this.mob.hasFollowers()) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     if (this.mob.isFollower()) {
/* 32 */       return true;
/*    */     }
/*    */     
/* 35 */     if (this.nextStartTick > 0) {
/* 36 */       this.nextStartTick--;
/* 37 */       return false;
/*    */     } 
/*    */     
/* 40 */     this.nextStartTick = nextStartTick(this.mob);
/*    */     
/* 42 */     Predicate<AbstractSchoolingFish> predicate = fish -> (fish.canBeFollowed() || !fish.isFollower());
/* 43 */     List<? extends AbstractSchoolingFish> leadersWithSpaceOrNotFollowers = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
/*    */     
/* 45 */     AbstractSchoolingFish leader = (AbstractSchoolingFish)DataFixUtils.orElse(leadersWithSpaceOrNotFollowers.stream().filter(AbstractSchoolingFish::canBeFollowed).findAny(), this.mob);
/*    */     
/* 47 */     leader.addFollowers(leadersWithSpaceOrNotFollowers.stream().filter(fish -> !fish.isFollower()));
/*    */     
/* 49 */     return this.mob.isFollower();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public boolean canContinueToUse() { return (this.mob.isFollower() && this.mob.inRangeOfLeader()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   public void start() { this.timeToRecalcPath = 0; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   public void stop() { this.mob.stopFollowing(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 69 */     if (--this.timeToRecalcPath > 0) {
/*    */       return;
/*    */     }
/* 72 */     this.timeToRecalcPath = adjustedTickDelay(10);
/*    */     
/* 74 */     this.mob.pathToLeader();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowFlockLeaderGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */