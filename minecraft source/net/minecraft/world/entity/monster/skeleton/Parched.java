/*    */ package net.minecraft.world.entity.monster.skeleton;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ public class Parched
/*    */   extends AbstractSkeleton
/*    */ {
/* 20 */   public Parched(EntityType<? extends AbstractSkeleton> type, Level level) { super(type, level); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected AbstractArrow getArrow(ItemStack projectile, float power, ItemStack firingWeapon) {
/* 25 */     AbstractArrow arrow = super.getArrow(projectile, power, firingWeapon);
/* 26 */     if (arrow instanceof Arrow) {
/* 27 */       ((Arrow)arrow).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
/*    */     }
/* 29 */     return arrow;
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 33 */     return AbstractSkeleton.createAttributes()
/* 34 */       .add(Attributes.MAX_HEALTH, 16.0D);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   protected SoundEvent getAmbientSound() { return SoundEvents.PARCHED_AMBIENT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.PARCHED_HURT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   protected SoundEvent getDeathSound() { return SoundEvents.PARCHED_DEATH; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   SoundEvent getStepSound() { return SoundEvents.PARCHED_STEP; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 59 */   protected int getHardAttackInterval() { return 50; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 64 */   protected int getAttackInterval() { return 70; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 69 */     if (newEffect.getEffect() == MobEffects.WEAKNESS) {
/* 70 */       return false;
/*    */     }
/* 72 */     return super.canBeAffected(newEffect);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\skeleton\Parched.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */