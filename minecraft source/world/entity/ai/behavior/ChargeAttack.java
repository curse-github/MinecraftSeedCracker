/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
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
/*     */ public class ChargeAttack
/*     */   extends Behavior<Animal>
/*     */ {
/*     */   private final int timeBetweenAttacks;
/*     */   private final TargetingConditions chargeTargeting;
/*     */   private final float speed;
/*     */   private final float knockbackForce;
/*     */   private final double maxTargetDetectionDistance;
/*     */   private final double maxChargeDistance;
/*     */   private final SoundEvent chargeSound;
/*     */   private Vec3 chargeVelocityVector;
/*     */   private Vec3 startPosition;
/*     */   
/*     */   public ChargeAttack(int timeBetweenAttacks, TargetingConditions chargeTargeting, float speed, float knockbackForce, double maxChargeDistance, double maxTargetDetectionDistance, SoundEvent chargeSound) {
/*  53 */     super(ImmutableMap.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT));
/*     */ 
/*     */ 
/*     */     
/*  57 */     this.timeBetweenAttacks = timeBetweenAttacks;
/*  58 */     this.chargeTargeting = chargeTargeting;
/*  59 */     this.speed = speed;
/*  60 */     this.knockbackForce = knockbackForce;
/*  61 */     this.maxChargeDistance = maxChargeDistance;
/*  62 */     this.maxTargetDetectionDistance = maxTargetDetectionDistance;
/*  63 */     this.chargeSound = chargeSound;
/*  64 */     this.chargeVelocityVector = Vec3.ZERO;
/*  65 */     this.startPosition = Vec3.ZERO;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected boolean checkExtraStartConditions(ServerLevel level, Animal body) { return body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Animal body, long timestamp) {
/*  75 */     Brain<?> brain = body.getBrain();
/*  76 */     Optional<LivingEntity> attackCandidate = brain.getMemory(MemoryModuleType.ATTACK_TARGET);
/*  77 */     if (attackCandidate.isEmpty()) {
/*  78 */       return false;
/*     */     }
/*  80 */     LivingEntity attackTarget = (LivingEntity)attackCandidate.get();
/*     */ 
/*     */     
/*  83 */     if (body instanceof TamableAnimal) { TamableAnimal tamedAnimal = (TamableAnimal)body; if (tamedAnimal.isTame()) {
/*  84 */         return false;
/*     */       } }
/*     */     
/*  87 */     if (body.position().subtract(this.startPosition).lengthSqr() >= this.maxChargeDistance * this.maxChargeDistance) {
/*  88 */       return false;
/*     */     }
/*  90 */     if (attackTarget.position().subtract(body.position()).lengthSqr() >= this.maxTargetDetectionDistance * this.maxTargetDetectionDistance) {
/*  91 */       return false;
/*     */     }
/*     */     
/*  94 */     if (!body.hasLineOfSight(attackTarget)) {
/*  95 */       return false;
/*     */     }
/*  97 */     return !brain.hasMemoryValue(MemoryModuleType.CHARGE_COOLDOWN_TICKS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Animal body, long timestamp) {
/* 102 */     Brain<?> brain = body.getBrain();
/* 103 */     this.startPosition = body.position();
/* 104 */     LivingEntity attackCandidate = (LivingEntity)brain.getMemory(MemoryModuleType.ATTACK_TARGET).get();
/* 105 */     Vec3 direction = attackCandidate.position().subtract(body.position()).normalize();
/* 106 */     this.chargeVelocityVector = direction.scale(this.speed);
/* 107 */     if (canStillUse(level, body, timestamp)) {
/* 108 */       body.playSound(this.chargeSound);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Animal body, long timestamp) {
/* 114 */     Brain<?> brain = body.getBrain();
/* 115 */     LivingEntity attackTarget = (LivingEntity)brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
/* 116 */     body.lookAt(attackTarget, 360.0F, 360.0F);
/* 117 */     body.setDeltaMovement(this.chargeVelocityVector);
/*     */     
/* 119 */     List<LivingEntity> collidingEntities = new ArrayList<LivingEntity>(1);
/* 120 */     level.getEntities(EntityTypeTest.forClass(LivingEntity.class), body.getBoundingBox(), e -> this.chargeTargeting.test(level, body, e), collidingEntities, 1);
/* 121 */     if (!collidingEntities.isEmpty()) {
/* 122 */       LivingEntity closestAttackTarget = (LivingEntity)collidingEntities.get(0);
/* 123 */       if (body.hasPassenger(closestAttackTarget)) {
/*     */         return;
/*     */       }
/* 126 */       dealDamageToTarget(level, body, closestAttackTarget);
/* 127 */       dealKnockBack(body, closestAttackTarget);
/* 128 */       stop(level, body, timestamp);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void dealDamageToTarget(ServerLevel level, Animal body, LivingEntity target) {
/* 133 */     DamageSource damageSource = level.damageSources().mobAttack(body);
/* 134 */     float damage = (float)body.getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 135 */     if (target.hurtServer(level, damageSource, damage)) {
/* 136 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void dealKnockBack(Animal body, LivingEntity target) {
/* 142 */     int movementSpeedLevel = body.hasEffect(MobEffects.SPEED) ? (body.getEffect(MobEffects.SPEED).getAmplifier() + 1) : 0;
/* 143 */     int movementSlowdownLevel = body.hasEffect(MobEffects.SLOWNESS) ? (body.getEffect(MobEffects.SLOWNESS).getAmplifier() + 1) : 0;
/* 144 */     float speedBoostPower = 0.25F * (movementSpeedLevel - movementSlowdownLevel);
/* 145 */     float speedFactor = Mth.clamp(this.speed * (float)body.getAttributeValue(Attributes.MOVEMENT_SPEED), 0.2F, 2.0F) + speedBoostPower;
/* 146 */     body.causeExtraKnockback(target, speedFactor * this.knockbackForce, body.getDeltaMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Animal body, long timestamp) {
/* 151 */     body.getBrain().setMemory(MemoryModuleType.CHARGE_COOLDOWN_TICKS, Integer.valueOf(this.timeBetweenAttacks));
/* 152 */     body.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ChargeAttack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */