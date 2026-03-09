/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ import net.minecraft.world.entity.raid.Raids;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class PathfindToRaidGoal<T extends Raider>
/*    */   extends Goal
/*    */ {
/*    */   private static final int RECRUITMENT_SEARCH_TICK_DELAY = 20;
/*    */   private static final float SPEED_MODIFIER = 1.0F;
/*    */   private final T mob;
/*    */   private int recruitmentTick;
/*    */   
/*    */   public PathfindToRaidGoal(T mob) {
/* 24 */     this.mob = mob;
/* 25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 30 */     return (this.mob.getTarget() == null && 
/* 31 */       !this.mob.hasControllingPassenger() && this.mob
/* 32 */       .hasActiveRaid() && 
/* 33 */       !this.mob.getCurrentRaid().isOver() && 
/* 34 */       !getServerLevel(this.mob.level()).isVillage(this.mob.blockPosition()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 39 */     return (this.mob.hasActiveRaid() && 
/* 40 */       !this.mob.getCurrentRaid().isOver() && 
/* 41 */       !getServerLevel(this.mob.level()).isVillage(this.mob.blockPosition()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 46 */     if (this.mob.hasActiveRaid()) {
/* 47 */       Raid raid = this.mob.getCurrentRaid();
/* 48 */       if (this.mob.tickCount > this.recruitmentTick) {
/* 49 */         this.recruitmentTick = this.mob.tickCount + 20;
/* 50 */         recruitNearby(raid);
/*    */       } 
/*    */       
/* 53 */       if (!this.mob.isPathFinding()) {
/* 54 */         Vec3 posTowards = DefaultRandomPos.getPosTowards(this.mob, 15, 4, Vec3.atBottomCenterOf(raid.getCenter()), 1.5707963705062866D);
/* 55 */         if (posTowards != null) {
/* 56 */           this.mob.getNavigation().moveTo(posTowards.x, posTowards.y, posTowards.z, 1.0D);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void recruitNearby(Raid raid) {
/* 63 */     if (raid.isActive()) {
/* 64 */       ServerLevel level = getServerLevel(this.mob.level());
/* 65 */       Set<Raider> raidersToAdd = Sets.newHashSet();
/*    */       
/* 67 */       List<Raider> raidersNearby = level.getEntitiesOfClass(Raider.class, this.mob.getBoundingBox().inflate(16.0D), mob -> (!mob.hasActiveRaid() && Raids.canJoinRaid(mob)));
/* 68 */       raidersToAdd.addAll(raidersNearby);
/*    */       
/* 70 */       for (Raider raider : raidersToAdd)
/* 71 */         raid.joinRaid(level, raid.getGroupsSpawned(), raider, null, true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\PathfindToRaidGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */