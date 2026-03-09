/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class DragonTakeoffPhase
/*    */   extends AbstractDragonPhaseInstance
/*    */ {
/*    */   private boolean firstTick;
/*    */   private Path currentPath;
/*    */   private Vec3 targetLocation;
/*    */   
/* 19 */   public DragonTakeoffPhase(EnderDragon dragon) { super(dragon); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 24 */     if (this.firstTick || this.currentPath == null) {
/* 25 */       this.firstTick = false;
/* 26 */       findNewTarget();
/*    */     } else {
/* 28 */       BlockPos egg = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
/* 29 */       if (!egg.closerToCenterThan(this.dragon.position(), 10.0D)) {
/* 30 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 37 */     this.firstTick = true;
/* 38 */     this.currentPath = null;
/* 39 */     this.targetLocation = null;
/*    */   }
/*    */   
/*    */   private void findNewTarget() {
/* 43 */     int currentNodeIndex = this.dragon.findClosestNode();
/* 44 */     Vec3 lookVector = this.dragon.getHeadLookVector(1.0F);
/* 45 */     int targetNodeIndex = this.dragon.findClosestNode(-lookVector.x * 40.0D, 105.0D, -lookVector.z * 40.0D);
/*    */     
/* 47 */     if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() <= 0) {
/*    */       
/* 49 */       targetNodeIndex -= 12;
/* 50 */       targetNodeIndex &= 0x7;
/* 51 */       targetNodeIndex += 12;
/*    */     } else {
/*    */       
/* 54 */       targetNodeIndex %= 12;
/* 55 */       if (targetNodeIndex < 0) {
/* 56 */         targetNodeIndex += 12;
/*    */       }
/*    */     } 
/*    */     
/* 60 */     this.currentPath = this.dragon.findPath(currentNodeIndex, targetNodeIndex, null);
/*    */     
/* 62 */     navigateToNextPathNode();
/*    */   }
/*    */   
/*    */   private void navigateToNextPathNode() {
/* 66 */     if (this.currentPath != null) {
/* 67 */       this.currentPath.advance();
/* 68 */       if (!this.currentPath.isDone()) {
/* 69 */         double yTarget; BlockPos blockPos = this.currentPath.getNextNodePos();
/* 70 */         this.currentPath.advance();
/*    */ 
/*    */         
/*    */         do {
/* 74 */           yTarget = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 75 */         } while (yTarget < blockPos.getY());
/*    */         
/* 77 */         this.targetLocation = new Vec3(blockPos.getX(), yTarget, blockPos.getZ());
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 84 */   public Vec3 getFlyTargetLocation() { return this.targetLocation; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 89 */   public EnderDragonPhase<DragonTakeoffPhase> getPhase() { return EnderDragonPhase.TAKEOFF; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonTakeoffPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */