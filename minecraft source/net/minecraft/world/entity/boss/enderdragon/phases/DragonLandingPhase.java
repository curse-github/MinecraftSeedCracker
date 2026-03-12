/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.core.particles.PowerParticleOption;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class DragonLandingPhase
/*    */   extends AbstractDragonPhaseInstance
/*    */ {
/*    */   private Vec3 targetLocation;
/*    */   
/* 18 */   public DragonLandingPhase(EnderDragon dragon) { super(dragon); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doClientTick() {
/* 23 */     Vec3 look = this.dragon.getHeadLookVector(1.0F).normalize();
/* 24 */     look.yRot(-0.7853982F);
/*    */     
/* 26 */     double particleX = this.dragon.head.getX();
/* 27 */     double particleY = this.dragon.head.getY(0.5D);
/* 28 */     double particleZ = this.dragon.head.getZ();
/* 29 */     for (int i = 0; i < 8; i++) {
/* 30 */       RandomSource random = this.dragon.getRandom();
/* 31 */       double px = particleX + random.nextGaussian() / 2.0D;
/* 32 */       double py = particleY + random.nextGaussian() / 2.0D;
/* 33 */       double pz = particleZ + random.nextGaussian() / 2.0D;
/* 34 */       Vec3 movement = this.dragon.getDeltaMovement();
/* 35 */       this.dragon.level().addParticle(PowerParticleOption.create(ParticleTypes.DRAGON_BREATH, 1.0F), px, py, pz, -look.x * 0.07999999821186066D + movement.x, -look.y * 0.30000001192092896D + movement.y, -look.z * 0.07999999821186066D + movement.z);
/* 36 */       look.yRot(0.19634955F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 42 */     if (this.targetLocation == null) {
/* 43 */       this.targetLocation = Vec3.atBottomCenterOf(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(this.dragon.getFightOrigin())));
/*    */     }
/*    */     
/* 46 */     if (this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ()) < 1.0D) {
/* 47 */       ((DragonSittingFlamingPhase)this.dragon.getPhaseManager().getPhase(EnderDragonPhase.SITTING_FLAMING)).resetFlameCount();
/* 48 */       this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public float getFlySpeed() { return 1.5F; }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getTurnSpeed() {
/* 59 */     float rotSpeed = (float)this.dragon.getDeltaMovement().horizontalDistance() + 1.0F;
/* 60 */     float dist = Math.min(rotSpeed, 40.0F);
/*    */     
/* 62 */     return dist / rotSpeed;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 67 */   public void begin() { this.targetLocation = null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   public Vec3 getFlyTargetLocation() { return this.targetLocation; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public EnderDragonPhase<DragonLandingPhase> getPhase() { return EnderDragonPhase.LANDING; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonLandingPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */