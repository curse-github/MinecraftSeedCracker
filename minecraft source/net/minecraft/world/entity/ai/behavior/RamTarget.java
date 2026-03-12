/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.goat.Goat;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
/*     */ public class RamTarget
/*     */   extends Behavior<Goat>
/*     */ {
/*     */   public static final int TIME_OUT_DURATION = 200;
/*     */   public static final float RAM_SPEED_FORCE_FACTOR = 1.65F;
/*     */   private final Function<Goat, UniformInt> getTimeBetweenRams;
/*     */   private final TargetingConditions ramTargeting;
/*     */   private final float speed;
/*     */   private final ToDoubleFunction<Goat> getKnockbackForce;
/*     */   private Vec3 ramDirection;
/*     */   private final Function<Goat, SoundEvent> getImpactSound;
/*     */   private final Function<Goat, SoundEvent> getHornBreakSound;
/*     */   
/*     */   public RamTarget(Function<Goat, UniformInt> getTimeBetweenRams, TargetingConditions ramTargeting, float speed, ToDoubleFunction<Goat> getKnockbackForce, Function<Goat, SoundEvent> getImpactSound, Function<Goat, SoundEvent> getHornBreakSound) {
/*  52 */     super(ImmutableMap.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.RAM_TARGET, MemoryStatus.VALUE_PRESENT), 200);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     this.getTimeBetweenRams = getTimeBetweenRams;
/*  58 */     this.ramTargeting = ramTargeting;
/*  59 */     this.speed = speed;
/*  60 */     this.getKnockbackForce = getKnockbackForce;
/*  61 */     this.getImpactSound = getImpactSound;
/*  62 */     this.getHornBreakSound = getHornBreakSound;
/*     */     
/*  64 */     this.ramDirection = Vec3.ZERO;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  69 */   protected boolean checkExtraStartConditions(ServerLevel level, Goat body) { return body.getBrain().hasMemoryValue(MemoryModuleType.RAM_TARGET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   protected boolean canStillUse(ServerLevel level, Goat body, long timestamp) { return body.getBrain().hasMemoryValue(MemoryModuleType.RAM_TARGET); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Goat body, long timestamp) {
/*  79 */     BlockPos curPos = body.blockPosition();
/*  80 */     Brain<?> brain = body.getBrain();
/*  81 */     Vec3 ramTargetPos = (Vec3)brain.getMemory(MemoryModuleType.RAM_TARGET).get();
/*     */ 
/*     */     
/*  84 */     this.ramDirection = (new Vec3(curPos.getX() - ramTargetPos.x(), 0.0D, curPos.getZ() - ramTargetPos.z())).normalize();
/*  85 */     brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(ramTargetPos, this.speed, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Goat body, long timestamp) {
/*  90 */     List<LivingEntity> nearbyEntities = level.getNearbyEntities(LivingEntity.class, this.ramTargeting, body, body.getBoundingBox());
/*     */     
/*  92 */     Brain<?> brain = body.getBrain();
/*  93 */     if (!nearbyEntities.isEmpty()) {
/*  94 */       LivingEntity ramTarget = (LivingEntity)nearbyEntities.get(0);
/*  95 */       DamageSource damageSource = level.damageSources().noAggroMobAttack(body);
/*  96 */       float damage = (float)body.getAttributeValue(Attributes.ATTACK_DAMAGE);
/*  97 */       if (ramTarget.hurtServer(level, damageSource, damage)) {
/*  98 */         EnchantmentHelper.doPostAttackEffects(level, ramTarget, damageSource);
/*     */       }
/*     */       
/* 101 */       int movementSpeedLevel = body.hasEffect(MobEffects.SPEED) ? (body.getEffect(MobEffects.SPEED).getAmplifier() + 1) : 0;
/* 102 */       int movementSlowdownLevel = body.hasEffect(MobEffects.SLOWNESS) ? (body.getEffect(MobEffects.SLOWNESS).getAmplifier() + 1) : 0;
/* 103 */       float speedBoostPower = 0.25F * (movementSpeedLevel - movementSlowdownLevel);
/* 104 */       float speedFactor = Mth.clamp(body.getSpeed() * 1.65F, 0.2F, 3.0F) + speedBoostPower;
/*     */       
/* 106 */       DamageSource source = level.damageSources().mobAttack(body);
/* 107 */       float blockedDamage = ramTarget.applyItemBlocking(level, source, damage);
/* 108 */       float blockingFactor = (blockedDamage > 0.0F) ? 0.5F : 1.0F;
/* 109 */       ramTarget.knockback((blockingFactor * speedFactor) * this.getKnockbackForce.applyAsDouble(body), this.ramDirection.x(), this.ramDirection.z());
/*     */       
/* 111 */       finishRam(level, body);
/* 112 */       level.playSound(null, body, (SoundEvent)this.getImpactSound.apply(body), SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 113 */     } else if (hasRammedHornBreakingBlock(level, body)) {
/* 114 */       level.playSound(null, body, (SoundEvent)this.getImpactSound.apply(body), SoundSource.NEUTRAL, 1.0F, 1.0F);
/* 115 */       boolean dropped = body.dropHorn();
/* 116 */       if (dropped) {
/* 117 */         level.playSound(null, body, (SoundEvent)this.getHornBreakSound.apply(body), SoundSource.NEUTRAL, 1.0F, 1.0F);
/*     */       }
/* 119 */       finishRam(level, body);
/*     */     } else {
/* 121 */       Optional<WalkTarget> walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET);
/* 122 */       Optional<Vec3> ramTarget = brain.getMemory(MemoryModuleType.RAM_TARGET);
/*     */ 
/*     */       
/* 125 */       boolean lostOrReachedTarget = (walkTarget.isEmpty() || ramTarget.isEmpty() || ((WalkTarget)walkTarget.get()).getTarget().currentPosition().closerThan((Position)ramTarget.get(), 0.25D));
/* 126 */       if (lostOrReachedTarget) {
/* 127 */         finishRam(level, body);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasRammedHornBreakingBlock(ServerLevel level, Goat body) {
/* 133 */     Vec3 horizontalMovementNormalized = body.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize();
/* 134 */     BlockPos facingBlockPosition = BlockPos.containing(body.position().add(horizontalMovementNormalized));
/* 135 */     return (level.getBlockState(facingBlockPosition).is(BlockTags.SNAPS_GOAT_HORN) || level.getBlockState(facingBlockPosition.above()).is(BlockTags.SNAPS_GOAT_HORN));
/*     */   }
/*     */   
/*     */   protected void finishRam(ServerLevel level, Goat body) {
/* 139 */     level.broadcastEntityEvent(body, (byte)59);
/* 140 */     body.getBrain().setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, Integer.valueOf(((UniformInt)this.getTimeBetweenRams.apply(body)).sample(level.random)));
/* 141 */     body.getBrain().eraseMemory(MemoryModuleType.RAM_TARGET);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RamTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */