/*     */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DragonStrafePlayerPhase
/*     */   extends AbstractDragonPhaseInstance
/*     */ {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int FIREBALL_CHARGE_AMOUNT = 5;
/*     */   
/*     */   private int fireballCharge;
/*     */   private Path currentPath;
/*     */   private Vec3 targetLocation;
/*     */   private LivingEntity attackTarget;
/*     */   private boolean holdingPatternClockwise;
/*     */   
/*  28 */   public DragonStrafePlayerPhase(EnderDragon dragon) { super(dragon); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doServerTick(ServerLevel level) {
/*  33 */     if (this.attackTarget == null) {
/*  34 */       LOGGER.warn("Skipping player strafe phase because no player was found");
/*  35 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*     */       
/*     */       return;
/*     */     } 
/*  39 */     if (this.currentPath != null && this.currentPath.isDone()) {
/*  40 */       double xTarget = this.attackTarget.getX();
/*  41 */       double zTarget = this.attackTarget.getZ();
/*     */       
/*  43 */       double xTargetDist = xTarget - this.dragon.getX();
/*  44 */       double zTargetDist = zTarget - this.dragon.getZ();
/*  45 */       double dist = Math.sqrt(xTargetDist * xTargetDist + zTargetDist * zTargetDist);
/*  46 */       double heightOffset = Math.min(0.4000000059604645D + dist / 80.0D - 1.0D, 10.0D);
/*     */       
/*  48 */       this.targetLocation = new Vec3(xTarget, this.attackTarget.getY() + heightOffset, zTarget);
/*     */     } 
/*     */     
/*  51 */     double distToTarget = (this.targetLocation == null) ? 0.0D : this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/*  52 */     if (distToTarget < 100.0D || distToTarget > 22500.0D) {
/*  53 */       findNewTarget();
/*     */     }
/*     */     
/*  56 */     double maxDist = 64.0D;
/*  57 */     if (this.attackTarget.distanceToSqr(this.dragon) < 4096.0D) {
/*  58 */       if (this.dragon.hasLineOfSight(this.attackTarget)) {
/*  59 */         this.fireballCharge++;
/*  60 */         Vec3 aim = (new Vec3(this.attackTarget.getX() - this.dragon.getX(), 0.0D, this.attackTarget.getZ() - this.dragon.getZ())).normalize();
/*  61 */         Vec3 dir = (new Vec3(Mth.sin((this.dragon.getYRot() * 0.017453292F)), 0.0D, -Mth.cos((this.dragon.getYRot() * 0.017453292F)))).normalize();
/*  62 */         float dot = (float)dir.dot(aim);
/*  63 */         float angleDegs = (float)(Math.acos(dot) * 57.2957763671875D);
/*  64 */         angleDegs += 0.5F;
/*     */         
/*  66 */         if (this.fireballCharge >= 5 && angleDegs >= 0.0F && angleDegs < 10.0F) {
/*  67 */           double d = 1.0D;
/*  68 */           Vec3 viewVector = this.dragon.getViewVector(1.0F);
/*  69 */           double startingX = this.dragon.head.getX() - viewVector.x * 1.0D;
/*  70 */           double startingY = this.dragon.head.getY(0.5D) + 0.5D;
/*  71 */           double startingZ = this.dragon.head.getZ() - viewVector.z * 1.0D;
/*     */           
/*  73 */           double xdd = this.attackTarget.getX() - startingX;
/*  74 */           double ydd = this.attackTarget.getY(0.5D) - startingY;
/*  75 */           double zdd = this.attackTarget.getZ() - startingZ;
/*  76 */           Vec3 direction = new Vec3(xdd, ydd, zdd);
/*     */           
/*  78 */           if (!this.dragon.isSilent()) {
/*  79 */             level.levelEvent(null, 1017, this.dragon.blockPosition(), 0);
/*     */           }
/*  81 */           DragonFireball entity = new DragonFireball(level, this.dragon, direction.normalize());
/*  82 */           entity.snapTo(startingX, startingY, startingZ, 0.0F, 0.0F);
/*  83 */           level.addFreshEntity(entity);
/*  84 */           this.fireballCharge = 0;
/*     */           
/*  86 */           if (this.currentPath != null) {
/*  87 */             while (!this.currentPath.isDone()) {
/*  88 */               this.currentPath.advance();
/*     */             }
/*     */           }
/*     */           
/*  92 */           this.dragon.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
/*     */         }
/*     */       
/*  95 */       } else if (this.fireballCharge > 0) {
/*  96 */         this.fireballCharge--;
/*     */       }
/*     */     
/*     */     }
/* 100 */     else if (this.fireballCharge > 0) {
/* 101 */       this.fireballCharge--;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void findNewTarget() {
/* 107 */     if (this.currentPath == null || this.currentPath.isDone()) {
/* 108 */       int currentNodeIndex = this.dragon.findClosestNode();
/* 109 */       int targetNodeIndex = currentNodeIndex;
/*     */       
/* 111 */       if (this.dragon.getRandom().nextInt(8) == 0) {
/* 112 */         this.holdingPatternClockwise = !this.holdingPatternClockwise;
/* 113 */         targetNodeIndex += 6;
/*     */       } 
/*     */       
/* 116 */       if (this.holdingPatternClockwise) {
/* 117 */         targetNodeIndex++;
/*     */       } else {
/* 119 */         targetNodeIndex--;
/*     */       } 
/*     */       
/* 122 */       if (this.dragon.getDragonFight() == null || this.dragon.getDragonFight().getCrystalsAlive() <= 0) {
/*     */         
/* 124 */         targetNodeIndex -= 12;
/* 125 */         targetNodeIndex &= 0x7;
/* 126 */         targetNodeIndex += 12;
/*     */       } else {
/*     */         
/* 129 */         targetNodeIndex %= 12;
/* 130 */         if (targetNodeIndex < 0) {
/* 131 */           targetNodeIndex += 12;
/*     */         }
/*     */       } 
/*     */       
/* 135 */       this.currentPath = this.dragon.findPath(currentNodeIndex, targetNodeIndex, null);
/*     */       
/* 137 */       if (this.currentPath != null) {
/* 138 */         this.currentPath.advance();
/*     */       }
/*     */     } 
/*     */     
/* 142 */     navigateToNextPathNode();
/*     */   }
/*     */   
/*     */   private void navigateToNextPathNode() {
/* 146 */     if (this.currentPath != null && !this.currentPath.isDone()) {
/* 147 */       double yTarget; BlockPos blockPos = this.currentPath.getNextNodePos();
/*     */       
/* 149 */       this.currentPath.advance();
/* 150 */       double xTarget = blockPos.getX();
/*     */       
/* 152 */       double zTarget = blockPos.getZ();
/*     */       
/*     */       do {
/* 155 */         yTarget = (blockPos.getY() + this.dragon.getRandom().nextFloat() * 20.0F);
/* 156 */       } while (yTarget < blockPos.getY());
/*     */       
/* 158 */       this.targetLocation = new Vec3(xTarget, yTarget, zTarget);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void begin() {
/* 164 */     this.fireballCharge = 0;
/* 165 */     this.targetLocation = null;
/* 166 */     this.currentPath = null;
/* 167 */     this.attackTarget = null;
/*     */   }
/*     */   
/*     */   public void setTarget(LivingEntity target) {
/* 171 */     this.attackTarget = target;
/*     */     
/* 173 */     int currentNodeIndex = this.dragon.findClosestNode();
/* 174 */     int targetNodeIndex = this.dragon.findClosestNode(this.attackTarget.getX(), this.attackTarget.getY(), this.attackTarget.getZ());
/*     */     
/* 176 */     int finalXTarget = this.attackTarget.getBlockX();
/* 177 */     int finalZTarget = this.attackTarget.getBlockZ();
/*     */     
/* 179 */     double xd = finalXTarget - this.dragon.getX();
/* 180 */     double zd = finalZTarget - this.dragon.getZ();
/* 181 */     double sd = Math.sqrt(xd * xd + zd * zd);
/* 182 */     double ho = Math.min(0.4000000059604645D + sd / 80.0D - 1.0D, 10.0D);
/* 183 */     int finalYTarget = Mth.floor(this.attackTarget.getY() + ho);
/*     */     
/* 185 */     Node finalNode = new Node(finalXTarget, finalYTarget, finalZTarget);
/*     */     
/* 187 */     this.currentPath = this.dragon.findPath(currentNodeIndex, targetNodeIndex, finalNode);
/*     */     
/* 189 */     if (this.currentPath != null) {
/* 190 */       this.currentPath.advance();
/*     */       
/* 192 */       navigateToNextPathNode();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public Vec3 getFlyTargetLocation() { return this.targetLocation; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public EnderDragonPhase<DragonStrafePlayerPhase> getPhase() { return EnderDragonPhase.STRAFE_PLAYER; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonStrafePlayerPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */