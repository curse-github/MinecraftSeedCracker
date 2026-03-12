/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.level.pathfinder.Node;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonLandingApproachPhase
/*    */   extends AbstractDragonPhaseInstance
/*    */ {
/* 17 */   private static final TargetingConditions NEAR_EGG_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();
/*    */   
/*    */   private Path currentPath;
/*    */   
/*    */   private Vec3 targetLocation;
/*    */   
/* 23 */   public DragonLandingApproachPhase(EnderDragon dragon) { super(dragon); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public EnderDragonPhase<DragonLandingApproachPhase> getPhase() { return EnderDragonPhase.LANDING_APPROACH; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void begin() {
/* 33 */     this.currentPath = null;
/* 34 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 39 */     double distToTarget = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 40 */     if (distToTarget < 100.0D || distToTarget > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/* 41 */       findNewTarget(level);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Vec3 getFlyTargetLocation() { return this.targetLocation; }
/*    */ 
/*    */   
/*    */   private void findNewTarget(ServerLevel level) {
/* 51 */     if (this.currentPath == null || this.currentPath.isDone()) {
/* 52 */       int targetNodeIndex, currentNodeIndex = this.dragon.findClosestNode();
/* 53 */       BlockPos egg = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
/* 54 */       Player playerNearestToEgg = level.getNearestPlayer(NEAR_EGG_TARGETING, this.dragon, egg.getX(), egg.getY(), egg.getZ());
/*    */ 
/*    */       
/* 57 */       if (playerNearestToEgg != null) {
/* 58 */         Vec3 aim = (new Vec3(playerNearestToEgg.getX(), 0.0D, playerNearestToEgg.getZ())).normalize();
/* 59 */         targetNodeIndex = this.dragon.findClosestNode(-aim.x * 40.0D, 105.0D, -aim.z * 40.0D);
/*    */       } else {
/* 61 */         targetNodeIndex = this.dragon.findClosestNode(40.0D, egg.getY(), 0.0D);
/*    */       } 
/*    */       
/* 64 */       Node finalNode = new Node(egg.getX(), egg.getY(), egg.getZ());
/*    */       
/* 66 */       this.currentPath = this.dragon.findPath(currentNodeIndex, targetNodeIndex, finalNode);
/*    */       
/* 68 */       if (this.currentPath != null) {
/* 69 */         this.currentPath.advance();
/*    */       }
/*    */     } 
/*    */     
/* 73 */     navigateToNextPathNode();
/*    */     
/* 75 */     if (this.currentPath != null && this.currentPath.isDone()) {
/* 76 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING);
/*    */     }
/*    */   }
/*    */   
/*    */   private void navigateToNextPathNode() {
/* 81 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 82 */       double yTarget; BlockPos blockPos = this.currentPath.getNextNodePos();
/*    */       
/* 84 */       this.currentPath.advance();
/* 85 */       double xTarget = blockPos.getX();
/* 86 */       double zTarget = blockPos.getZ();
/*    */ 
/*    */       
/*    */       do {
/* 90 */         yTarget = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 91 */       } while (yTarget < blockPos.getY());
/*    */       
/* 93 */       this.targetLocation = new Vec3(xTarget, yTarget, zTarget);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonLandingApproachPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */