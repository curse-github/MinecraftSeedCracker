/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class DragonDeathPhase
/*    */   extends AbstractDragonPhaseInstance
/*    */ {
/*    */   private Vec3 targetLocation;
/*    */   private int time;
/*    */   
/* 17 */   public DragonDeathPhase(EnderDragon dragon) { super(dragon); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doClientTick() {
/* 22 */     if (this.time++ % 10 == 0) {
/* 23 */       float xo = (this.dragon.getRandom().nextFloat() - 0.5F) * 8.0F;
/* 24 */       float yo = (this.dragon.getRandom().nextFloat() - 0.5F) * 4.0F;
/* 25 */       float zo = (this.dragon.getRandom().nextFloat() - 0.5F) * 8.0F;
/* 26 */       this.dragon.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.dragon.getX() + xo, this.dragon.getY() + 2.0D + yo, this.dragon.getZ() + zo, 0.0D, 0.0D, 0.0D);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void doServerTick(ServerLevel level) {
/* 32 */     this.time++;
/*    */     
/* 34 */     if (this.targetLocation == null) {
/* 35 */       BlockPos egg = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(this.dragon.getFightOrigin()));
/* 36 */       this.targetLocation = Vec3.atBottomCenterOf(egg);
/*    */     } 
/*    */     
/* 39 */     double distToTarget = this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
/* 40 */     if (distToTarget < 100.0D || distToTarget > 22500.0D || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
/* 41 */       this.dragon.setHealth(0.0F);
/*    */     } else {
/* 43 */       this.dragon.setHealth(1.0F);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin() {
/* 49 */     this.targetLocation = null;
/* 50 */     this.time = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 55 */   public float getFlySpeed() { return 3.0F; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public Vec3 getFlyTargetLocation() { return this.targetLocation; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public EnderDragonPhase<DragonDeathPhase> getPhase() { return EnderDragonPhase.DYING; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\DragonDeathPhase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */