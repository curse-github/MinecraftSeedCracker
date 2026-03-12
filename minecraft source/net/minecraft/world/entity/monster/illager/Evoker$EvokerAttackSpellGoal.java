/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.EvokerFangs;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EvokerAttackSpellGoal
/*     */   extends SpellcasterIllager.SpellcasterUseSpellGoal
/*     */ {
/* 147 */   private EvokerAttackSpellGoal() { super(paramEvoker); }
/*     */ 
/*     */   
/* 150 */   protected int getCastingTime() { return 40; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   protected int getCastingInterval() { return 100; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void performSpellCasting() {
/* 161 */     LivingEntity target = Evoker.this.getTarget();
/* 162 */     double minY = Math.min(target.getY(), Evoker.this.getY());
/* 163 */     double maxY = Math.max(target.getY(), Evoker.this.getY()) + 1.0D;
/* 164 */     float angleTowardsTarget = (float)Mth.atan2(target.getZ() - Evoker.this.getZ(), target.getX() - Evoker.this.getX());
/* 165 */     if (Evoker.this.distanceToSqr(target) < 9.0D) {
/*     */       
/* 167 */       for (int i = 0; i < 5; i++) {
/* 168 */         float angle = angleTowardsTarget + i * 3.1415927F * 0.4F;
/* 169 */         createSpellEntity(Evoker.this.getX() + Mth.cos(angle) * 1.5D, Evoker.this.getZ() + Mth.sin(angle) * 1.5D, minY, maxY, angle, 0);
/*     */       } 
/*     */       
/* 172 */       for (int i = 0; i < 8; i++) {
/* 173 */         float angle = angleTowardsTarget + i * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
/* 174 */         createSpellEntity(Evoker.this.getX() + Mth.cos(angle) * 2.5D, Evoker.this.getZ() + Mth.sin(angle) * 2.5D, minY, maxY, angle, 3);
/*     */       } 
/*     */     } else {
/*     */       
/* 178 */       for (int i = 0; i < 16; i++) {
/* 179 */         double reach = 1.25D * (i + 1);
/* 180 */         int spellSpeed = 1 * i;
/* 181 */         createSpellEntity(Evoker.this.getX() + Mth.cos(angleTowardsTarget) * reach, Evoker.this.getZ() + Mth.sin(angleTowardsTarget) * reach, minY, maxY, angleTowardsTarget, spellSpeed);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void createSpellEntity(double x, double z, double minY, double maxY, float angle, int delayTicks) {
/* 188 */     BlockPos pos = BlockPos.containing(x, maxY, z);
/* 189 */     boolean success = false;
/* 190 */     double topOffset = 0.0D;
/*     */     do {
/* 192 */       BlockPos below = pos.below();
/* 193 */       BlockState belowState = Evoker.this.level().getBlockState(below);
/* 194 */       if (belowState.isFaceSturdy(Evoker.this.level(), below, Direction.UP)) {
/* 195 */         if (!Evoker.this.level().isEmptyBlock(pos)) {
/* 196 */           BlockState blockState = Evoker.this.level().getBlockState(pos);
/* 197 */           VoxelShape shape = blockState.getCollisionShape(Evoker.this.level(), pos);
/* 198 */           if (!shape.isEmpty()) {
/* 199 */             topOffset = shape.max(Direction.Axis.Y);
/*     */           }
/*     */         } 
/* 202 */         success = true;
/*     */         break;
/*     */       } 
/* 205 */       pos = pos.below();
/* 206 */     } while (pos.getY() >= Mth.floor(minY) - 1);
/* 207 */     if (success) {
/* 208 */       Evoker.this.level().addFreshEntity(new EvokerFangs(Evoker.this.level(), x, pos.getY() + topOffset, z, angle, delayTicks, Evoker.this));
/* 209 */       Evoker.this.level().gameEvent(GameEvent.ENTITY_PLACE, new Vec3(x, pos.getY() + topOffset, z), GameEvent.Context.of(Evoker.this));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 215 */   protected SoundEvent getSpellPrepareSound() { return SoundEvents.EVOKER_PREPARE_ATTACK; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   protected SpellcasterIllager.IllagerSpell getSpell() { return SpellcasterIllager.IllagerSpell.FANGS; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\illager\Evoker$EvokerAttackSpellGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */