/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class IllusionerBlindnessSpellGoal
/*     */   extends SpellcasterIllager.SpellcasterUseSpellGoal
/*     */ {
/*     */   private int lastTargetId;
/*     */   
/* 216 */   private IllusionerBlindnessSpellGoal() { super(paramIllusioner); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 221 */     if (!super.canUse()) {
/* 222 */       return false;
/*     */     }
/* 224 */     if (Illusioner.this.getTarget() == null) {
/* 225 */       return false;
/*     */     }
/* 227 */     if (Illusioner.this.getTarget().getId() == this.lastTargetId) {
/* 228 */       return false;
/*     */     }
/* 230 */     if (!getServerLevel(Illusioner.this).getCurrentDifficultyAt(Illusioner.this.blockPosition()).isHarderThan(Difficulty.NORMAL.ordinal())) {
/* 231 */       return false;
/*     */     }
/* 233 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 238 */     super.start();
/*     */     
/* 240 */     LivingEntity target = Illusioner.this.getTarget();
/* 241 */     if (target != null) {
/* 242 */       this.lastTargetId = target.getId();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 248 */   protected int getCastingTime() { return 20; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 253 */   protected int getCastingInterval() { return 180; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   protected void performSpellCasting() { Illusioner.this.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400), Illusioner.this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   protected SoundEvent getSpellPrepareSound() { return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 268 */   protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.BLINDNESS; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Illusioner$IllusionerBlindnessSpellGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */