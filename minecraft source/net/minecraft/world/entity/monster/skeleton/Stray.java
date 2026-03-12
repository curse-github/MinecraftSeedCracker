/*    */ package net.minecraft.world.entity.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.monster.Monster;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ 
/*    */ public class Stray
/*    */   extends AbstractSkeleton
/*    */ {
/* 24 */   public Stray(EntityType<? extends Stray> type, Level level) { super(type, level); }
/*    */ 
/*    */   
/*    */   public static boolean checkStraySpawnRules(EntityType<Stray> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 28 */     BlockPos checkSkyPos = pos;
/*    */     do {
/* 30 */       checkSkyPos = checkSkyPos.above();
/* 31 */     } while (level.getBlockState(checkSkyPos).is(Blocks.POWDER_SNOW));
/* 32 */     return (Monster.checkMonsterSpawnRules(type, level, spawnReason, pos, random) && (
/* 33 */       EntitySpawnReason.isSpawner(spawnReason) || level.canSeeSky(checkSkyPos.below())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   protected SoundEvent getAmbientSound() { return SoundEvents.STRAY_AMBIENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.STRAY_HURT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   protected SoundEvent getDeathSound() { return SoundEvents.STRAY_DEATH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   SoundEvent getStepSound() { return SoundEvents.STRAY_STEP; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected AbstractArrow getArrow(ItemStack projectile, float power, ItemStack firingWeapon) {
/* 58 */     AbstractArrow arrow = super.getArrow(projectile, power, firingWeapon);
/* 59 */     if (arrow instanceof Arrow) {
/* 60 */       ((Arrow)arrow).addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 600));
/*    */     }
/* 62 */     return arrow;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\skeleton\Stray.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */