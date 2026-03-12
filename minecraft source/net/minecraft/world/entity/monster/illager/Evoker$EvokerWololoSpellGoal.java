/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EvokerWololoSpellGoal
/*     */   extends SpellcasterIllager.SpellcasterUseSpellGoal
/*     */ {
/*     */   private final TargetingConditions wololoTargeting;
/*     */   
/*     */   public EvokerWololoSpellGoal() {
/* 281 */     super(Evoker.this);
/* 282 */     this.wololoTargeting = TargetingConditions.forNonCombat().range(16.0D).selector((target, level) -> (((Sheep)target).getColor() == DyeColor.BLUE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 286 */     if (Evoker.this.getTarget() != null)
/*     */     {
/* 288 */       return false;
/*     */     }
/* 290 */     if (Evoker.this.isCastingSpell())
/*     */     {
/* 292 */       return false;
/*     */     }
/* 294 */     if (Evoker.this.tickCount < this.nextAttackTickCount) {
/* 295 */       return false;
/*     */     }
/* 297 */     ServerLevel level = getServerLevel(Evoker.this.level());
/* 298 */     if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 299 */       return false;
/*     */     }
/*     */     
/* 302 */     List<Sheep> entities = level.getNearbyEntities(Sheep.class, this.wololoTargeting, Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));
/*     */     
/* 304 */     if (entities.isEmpty()) {
/* 305 */       return false;
/*     */     }
/* 307 */     Evoker.this.setWololoTarget((Sheep)entities.get(Evoker.access$400(Evoker.this).nextInt(entities.size())));
/* 308 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 314 */   public boolean canContinueToUse() { return (Evoker.this.getWololoTarget() != null && this.attackWarmupDelay > 0); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 319 */     super.stop();
/* 320 */     Evoker.this.setWololoTarget(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void performSpellCasting() {
/* 325 */     Sheep wololoTarget = Evoker.this.getWololoTarget();
/* 326 */     if (wololoTarget != null && wololoTarget.isAlive()) {
/* 327 */       wololoTarget.setColor(DyeColor.RED);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 333 */   protected int getCastWarmupTime() { return 40; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   protected int getCastingTime() { return 60; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 343 */   protected int getCastingInterval() { return 140; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   protected SoundEvent getSpellPrepareSound() { return SoundEvents.EVOKER_PREPARE_WOLOLO; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.WOLOLO; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Evoker$EvokerWololoSpellGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */