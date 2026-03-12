/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Silverfish;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ class InfestedMobEffect
/*    */   extends MobEffect {
/*    */   private final float chanceToSpawn;
/*    */   private final ToIntFunction<RandomSource> spawnedCount;
/*    */   
/*    */   protected InfestedMobEffect(MobEffectCategory category, int color, float chanceToSpawn, ToIntFunction<RandomSource> spawnedCount) {
/* 23 */     super(category, color, ParticleTypes.INFESTED);
/* 24 */     this.chanceToSpawn = chanceToSpawn;
/* 25 */     this.spawnedCount = spawnedCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMobHurt(ServerLevel level, LivingEntity mob, int amplifier, DamageSource source, float damage) {
/* 30 */     if (mob.getRandom().nextFloat() <= this.chanceToSpawn) {
/* 31 */       int count = this.spawnedCount.applyAsInt(mob.getRandom());
/* 32 */       for (int i = 0; i < count; i++) {
/* 33 */         spawnSilverfish(level, mob, mob.getX(), mob.getY() + mob.getBbHeight() / 2.0D, mob.getZ());
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private void spawnSilverfish(ServerLevel level, LivingEntity mob, double x, double y, double z) {
/* 39 */     Silverfish silverfish = (Silverfish)EntityType.SILVERFISH.create(level, EntitySpawnReason.TRIGGERED);
/*    */     
/* 41 */     if (silverfish == null) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     RandomSource random = mob.getRandom();
/* 46 */     float angle = 1.5707964F;
/* 47 */     float randomAngle = Mth.randomBetween(random, -1.5707964F, 1.5707964F);
/* 48 */     Vector3f viewDirection = mob.getLookAngle().toVector3f().mul(0.3F).mul(1.0F, 1.5F, 1.0F).rotateY(randomAngle);
/*    */     
/* 50 */     silverfish.snapTo(x, y, z, level.getRandom().nextFloat() * 360.0F, 0.0F);
/* 51 */     silverfish.setDeltaMovement(new Vec3(viewDirection));
/* 52 */     level.addFreshEntity(silverfish);
/* 53 */     silverfish.playSound(SoundEvents.SILVERFISH_HURT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\effect\InfestedMobEffect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */