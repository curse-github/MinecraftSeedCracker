/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EvokerSummonSpellGoal
/*     */   extends SpellcasterIllager.SpellcasterUseSpellGoal
/*     */ {
/*     */   private final TargetingConditions vexCountTargeting;
/*     */   
/*     */   private EvokerSummonSpellGoal() {
/* 224 */     super(paramEvoker);
/* 225 */     this.vexCountTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 229 */     if (!super.canUse()) {
/* 230 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 234 */     int vexes = getServerLevel(Evoker.this.level()).getNearbyEntities(Vex.class, this.vexCountTargeting, Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D)).size();
/* 235 */     return (Evoker.access$000(Evoker.this).nextInt(8) + 1 > vexes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 240 */   protected int getCastingTime() { return 100; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 245 */   protected int getCastingInterval() { return 340; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void performSpellCasting() {
/* 250 */     ServerLevel serverLevel = (ServerLevel)Evoker.this.level();
/* 251 */     PlayerTeam evokerTeam = Evoker.this.getTeam();
/* 252 */     for (int i = 0; i < 3; i++) {
/* 253 */       BlockPos pos = Evoker.this.blockPosition().offset(-2 + Evoker.access$100(Evoker.this).nextInt(5), 1, -2 + Evoker.access$200(Evoker.this).nextInt(5));
/* 254 */       Vex vex = (Vex)EntityType.VEX.create(Evoker.this.level(), EntitySpawnReason.MOB_SUMMONED);
/* 255 */       if (vex != null) {
/* 256 */         vex.snapTo(pos, 0.0F, 0.0F);
/* 257 */         vex.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
/* 258 */         vex.setOwner(Evoker.this);
/* 259 */         vex.setBoundOrigin(pos);
/* 260 */         vex.setLimitedLife(20 * (30 + Evoker.access$300(Evoker.this).nextInt(90)));
/* 261 */         if (evokerTeam != null) {
/* 262 */           serverLevel.getScoreboard().addPlayerToTeam(vex.getScoreboardName(), evokerTeam);
/*     */         }
/* 264 */         serverLevel.addFreshEntityWithPassengers(vex);
/* 265 */         serverLevel.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(Evoker.this));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 272 */   protected SoundEvent getSpellPrepareSound() { return SoundEvents.EVOKER_PREPARE_SUMMON; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 277 */   protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.SUMMON_VEX; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Evoker$EvokerSummonSpellGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */