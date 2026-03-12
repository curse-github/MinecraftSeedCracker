/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.SculkCatalystBlock;
/*     */ import net.minecraft.world.level.block.SculkSpreader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CatalystListener
/*     */   implements GameEventListener
/*     */ {
/*     */   public static final int PULSE_TICKS = 8;
/*     */   private final SculkSpreader sculkSpreader;
/*     */   private final BlockState blockState;
/*     */   private final PositionSource positionSource;
/*     */   
/*     */   public CatalystListener(BlockState blockState, PositionSource positionSource) {
/*  67 */     this.blockState = blockState;
/*  68 */     this.positionSource = positionSource;
/*  69 */     this.sculkSpreader = SculkSpreader.createLevelSpreader();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  74 */   public PositionSource getListenerSource() { return this.positionSource; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public int getListenerRadius() { return 8; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public GameEventListener.DeliveryMode getDeliveryMode() { return GameEventListener.DeliveryMode.BY_DISTANCE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/*  89 */     if (event.is(GameEvent.ENTITY_DIE)) { Entity entity = context.sourceEntity(); if (entity instanceof LivingEntity) { LivingEntity mob = (LivingEntity)entity;
/*  90 */         if (!mob.wasExperienceConsumed()) {
/*  91 */           DamageSource lastDamageSource = mob.getLastDamageSource();
/*  92 */           int experienceWouldDrop = mob.getExperienceReward(level, (Entity)Optionull.map(lastDamageSource, DamageSource::getEntity));
/*  93 */           if (mob.shouldDropExperience() && experienceWouldDrop > 0) {
/*  94 */             this.sculkSpreader.addCursors(BlockPos.containing(sourcePosition.relative(Direction.UP, 0.5D)), experienceWouldDrop);
/*  95 */             tryAwardItSpreadsAdvancement(level, mob);
/*     */           } 
/*  97 */           mob.skipDropExperience();
/*  98 */           this.positionSource.getPosition(level).ifPresent(vec3 -> bloom(level, BlockPos.containing(vec3), this.blockState, level.getRandom()));
/*     */         } 
/* 100 */         return true; }
/*     */        }
/*     */     
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 108 */   public SculkSpreader getSculkSpreader() { return this.sculkSpreader; }
/*     */ 
/*     */   
/*     */   private void bloom(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
/* 112 */     level.setBlock(pos, (BlockState)state.setValue(SculkCatalystBlock.PULSE, Boolean.valueOf(true)), 3);
/* 113 */     level.scheduleTick(pos, state.getBlock(), 8);
/*     */     
/* 115 */     level.sendParticles(ParticleTypes.SCULK_SOUL, pos.getX() + 0.5D, pos.getY() + 1.15D, pos.getZ() + 0.5D, 2, 0.2D, 0.0D, 0.2D, 0.0D);
/*     */     
/* 117 */     level.playSound(null, pos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0F, 0.6F + random.nextFloat() * 0.4F);
/*     */   }
/*     */   
/*     */   private void tryAwardItSpreadsAdvancement(Level level, LivingEntity mob) {
/* 121 */     LivingEntity lastHurtByMob = mob.getLastHurtByMob();
/* 122 */     if (lastHurtByMob instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)lastHurtByMob;
/* 123 */       DamageSource damageSource = (mob.getLastDamageSource() == null) ? level.damageSources().playerAttack(player) : mob.getLastDamageSource();
/* 124 */       CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.trigger(player, mob, damageSource); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\SculkCatalystBlockEntity$CatalystListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */