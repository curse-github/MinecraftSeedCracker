/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
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
/*     */ class IllusionerMirrorSpellGoal
/*     */   extends SpellcasterIllager.SpellcasterUseSpellGoal
/*     */ {
/* 178 */   private IllusionerMirrorSpellGoal() { super(paramIllusioner); }
/*     */   
/*     */   public boolean canUse() {
/* 181 */     if (!super.canUse()) {
/* 182 */       return false;
/*     */     }
/* 184 */     if (Illusioner.this.hasEffect(MobEffects.INVISIBILITY)) {
/* 185 */       return false;
/*     */     }
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 192 */   protected int getCastingTime() { return 20; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   protected int getCastingInterval() { return 340; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   protected void performSpellCasting() { Illusioner.this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   protected SoundEvent getSpellPrepareSound() { return SoundEvents.ILLUSIONER_PREPARE_MIRROR; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.DISAPPEAR; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Illusioner$IllusionerMirrorSpellGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */