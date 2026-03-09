/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Slime;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ class OozingMobEffect
/*    */   extends MobEffect {
/*    */   private static final int RADIUS_TO_CHECK_SLIMES = 2;
/*    */   public static final int SLIME_SIZE = 2;
/*    */   private final ToIntFunction<RandomSource> spawnedCount;
/*    */   
/*    */   protected OozingMobEffect(MobEffectCategory category, int color, ToIntFunction<RandomSource> spawnedCount) {
/* 26 */     super(category, color, ParticleTypes.ITEM_SLIME);
/* 27 */     this.spawnedCount = spawnedCount;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   protected static int numberOfSlimesToSpawn(int maxEntityCramming, NearbySlimes nearbySlimes, int numberRequested) {
/* 32 */     if (maxEntityCramming < 1) {
/* 33 */       return numberRequested;
/*    */     }
/*    */ 
/*    */     
/* 37 */     return Mth.clamp(0, maxEntityCramming - nearbySlimes.count(maxEntityCramming), numberRequested);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
/* 42 */     if (reason != Entity.RemovalReason.KILLED) {
/*    */       return;
/*    */     }
/*    */     
/* 46 */     int requestedSlimesToSpawn = this.spawnedCount.applyAsInt(mob.getRandom());
/* 47 */     int maxEntityCramming = ((Integer)level.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING)).intValue();
/* 48 */     int numberOfSlimesToSpawn = numberOfSlimesToSpawn(maxEntityCramming, NearbySlimes.closeTo(mob), requestedSlimesToSpawn);
/*    */     
/* 50 */     for (int i = 0; i < numberOfSlimesToSpawn; i++)
/* 51 */       spawnSlimeOffspring(mob.level(), mob.getX(), mob.getY() + 0.5D, mob.getZ()); 
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   protected static interface NearbySlimes
/*    */   {
/*    */     int count(int param1Int);
/*    */     
/*    */     private static NearbySlimes closeTo(LivingEntity mob) {
/* 60 */       return maxResults -> {
/* 61 */           List<Slime> slimesNearby = new ArrayList<Slime>();
/* 62 */           mob.level().getEntities(EntityType.SLIME, mob.getBoundingBox().inflate(2.0D), (), slimesNearby, maxResults);
/* 63 */           return slimesNearby.size();
/*    */         };
/*    */     }
/*    */   }
/*    */   
/*    */   private void spawnSlimeOffspring(Level level, double x, double y, double z) {
/* 69 */     Slime slime = (Slime)EntityType.SLIME.create(level, EntitySpawnReason.TRIGGERED);
/*    */     
/* 71 */     if (slime == null) {
/*    */       return;
/*    */     }
/*    */     
/* 75 */     slime.setSize(2, true);
/* 76 */     slime.snapTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
/* 77 */     level.addFreshEntity(slime);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\OozingMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */