/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DragonHoldingPatternPhase
/*     */   extends AbstractDragonPhaseInstance
/*     */ {
/*  18 */   private static final TargetingConditions NEW_TARGET_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight();
/*     */   
/*     */   private Path currentPath;
/*     */   
/*     */   private Vec3 targetLocation;
/*     */   private boolean clockwise;
/*     */   
/*  25 */   public DragonHoldingPatternPhase(EnderDragon dragon) { super(dragon); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   public EnderDragonPhase<DragonHoldingPatternPhase> getPhase() { return EnderDragonPhase.HOLDING_PATTERN; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doServerTick(ServerLevel level) {
/*  35 */     double distToTarget = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*  36 */     if (distToTarget < 100.0D || distToTarget > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/*  37 */       findNewTarget(level);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/*  43 */     this.currentPath = null;
/*  44 */     this.targetLocation = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public Vec3 getFlyTargetLocation() { return this.targetLocation; }
/*     */ 
/*     */   
/*     */   private void findNewTarget(ServerLevel level) {
/*  53 */     if (this.currentPath != null && this.currentPath.isDone()) {
/*  54 */       double distSqr; BlockPos egg = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
/*     */ 
/*     */ 
/*     */       
/*  58 */       int crystals = (this.dragon.getDragonFight() == null) ? 0 : this.dragon.getDragonFight().getCrystalsAlive();
/*     */       
/*  60 */       if (this.dragon.getRandom().nextInt(crystals + 3) == 0) {
/*  61 */         this.dragon.getPhaseManager().setPhase(EnderDragonPhase.LANDING_APPROACH);
/*     */         
/*     */         return;
/*     */       } 
/*  65 */       Player playerNearestToEgg = level.getNearestPlayer(NEW_TARGET_TARGETING, this.dragon, egg.getX(), egg.getY(), egg.getZ());
/*  66 */       if (playerNearestToEgg != null) {
/*  67 */         distSqr = egg.distToCenterSqr(playerNearestToEgg.position()) / 512.0D;
/*     */       } else {
/*  69 */         distSqr = 64.0D;
/*     */       } 
/*  71 */       if (playerNearestToEgg != null && (this.dragon.getRandom().nextInt((int)(distSqr + 2.0D)) == 0 || this.dragon.getRandom().nextInt(crystals + 2) == 0)) {
/*     */         
/*  73 */         strafePlayer(playerNearestToEgg);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/*  79 */     if (this.currentPath == null || this.currentPath.isDone()) {
/*  80 */       int currentNodeIndex = this.dragon.findClosestNode();
/*  81 */       int targetNodeIndex = currentNodeIndex;
/*     */       
/*  83 */       if (this.dragon.getRandom().nextInt(8) == 0) {
/*  84 */         this.clockwise = !this.clockwise;
/*  85 */         targetNodeIndex += 6;
/*     */       } 
/*     */       
/*  88 */       if (this.clockwise) {
/*  89 */         targetNodeIndex++;
/*     */       } else {
/*  91 */         targetNodeIndex--;
/*     */       } 
/*     */       
/*  94 */       if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() < 0) {
/*     */         
/*  96 */         targetNodeIndex -= 12;
/*  97 */         targetNodeIndex &= 0x7;
/*  98 */         targetNodeIndex += 12;
/*     */       } else {
/*     */         
/* 101 */         targetNodeIndex %= 12;
/* 102 */         if (targetNodeIndex < 0) {
/* 103 */           targetNodeIndex += 12;
/*     */         }
/*     */       } 
/*     */       
/* 107 */       this.currentPath = this.dragon.findPath(currentNodeIndex, targetNodeIndex, null);
/* 108 */       if (this.currentPath != null) {
/* 109 */         this.currentPath.advance();
/*     */       }
/*     */     } 
/*     */     
/* 113 */     navigateToNextPathNode();
/*     */   }
/*     */   
/*     */   private void strafePlayer(Player playerNearestToEgg) {
/* 117 */     this.dragon.getPhaseManager().setPhase(EnderDragonPhase.STRAFE_PLAYER);
/* 118 */     ((DragonStrafePlayerPhase)this.dragon.getPhaseManager().getPhase(EnderDragonPhase.STRAFE_PLAYER)).setTarget(playerNearestToEgg);
/*     */   }
/*     */   
/*     */   private void navigateToNextPathNode() {
/* 122 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 123 */       double yTarget; BlockPos blockPos = this.currentPath.getNextNodePos();
/*     */       
/* 125 */       this.currentPath.advance();
/* 126 */       double xTarget = blockPos.getX();
/* 127 */       double zTarget = blockPos.getZ();
/*     */ 
/*     */       
/*     */       do {
/* 131 */         yTarget = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 132 */       } while (yTarget < blockPos.getY());
/*     */       
/* 134 */       this.targetLocation = new Vec3(xTarget, yTarget, zTarget);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrystalDestroyed(EndCrystal crystal, BlockPos pos, DamageSource source, Player player) {
/* 140 */     if (player != null && this.dragon.canAttack(player))
/* 141 */       strafePlayer(player); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonHoldingPatternPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */