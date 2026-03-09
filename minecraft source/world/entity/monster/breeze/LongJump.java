/*     */ package net.minecraft.world.entity.monster.breeze;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpUtil;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class LongJump
/*     */   extends Behavior<Breeze>
/*     */ {
/*     */   private static final int REQUIRED_AIR_BLOCKS_ABOVE = 4;
/*     */   private static final int JUMP_COOLDOWN_TICKS = 10;
/*     */   private static final int JUMP_COOLDOWN_WHEN_HURT_TICKS = 2;
/*  43 */   private static final int INHALING_DURATION_TICKS = Math.round(10.0F);
/*     */   
/*     */   private static final float DEFAULT_FOLLOW_RANGE = 24.0F;
/*     */   private static final float DEFAULT_MAX_JUMP_VELOCITY = 1.4F;
/*     */   private static final float MAX_JUMP_VELOCITY_MULTIPLIER = 0.058333334F;
/*  48 */   private static final ObjectArrayList<Integer> ALLOWED_ANGLES = new ObjectArrayList(Lists.newArrayList(new Integer[] { null, null, null, null, (new Integer[5][3] = (new Integer[5][2] = (new Integer[5][1] = (new Integer[5][0] = Integer.valueOf(40)).valueOf(55)).valueOf(60)).valueOf(75)).valueOf(80) }));
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  52 */   public LongJump() { super(Map.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREEZE_JUMP_COOLDOWN, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_JUMP_INHALING, MemoryStatus.REGISTERED, MemoryModuleType.BREEZE_JUMP_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.BREEZE_SHOOT, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_LEAVING_WATER, MemoryStatus.REGISTERED), 200); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canRun(ServerLevel level, Breeze breeze) {
/*  64 */     if (!breeze.onGround() && !breeze.isInWater()) {
/*  65 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  69 */     if (Swim.shouldSwim(breeze)) {
/*  70 */       return false;
/*     */     }
/*     */     
/*  73 */     if (breeze.getBrain().checkMemory(MemoryModuleType.BREEZE_JUMP_TARGET, MemoryStatus.VALUE_PRESENT)) {
/*  74 */       return true;
/*     */     }
/*     */     
/*  77 */     LivingEntity attackTarget = (LivingEntity)breeze.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
/*  78 */     if (attackTarget == null) {
/*  79 */       return false;
/*     */     }
/*     */     
/*  82 */     if (outOfAggroRange(breeze, attackTarget)) {
/*  83 */       breeze.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/*  84 */       return false;
/*     */     } 
/*     */     
/*  87 */     if (tooCloseForJump(breeze, attackTarget)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     if (!canJumpFromCurrentPosition(level, breeze)) {
/*  92 */       return false;
/*     */     }
/*     */     
/*  95 */     BlockPos targetPos = snapToSurface(breeze, BreezeUtil.randomPointBehindTarget(attackTarget, breeze.getRandom()));
/*  96 */     if (targetPos == null) {
/*  97 */       return false;
/*     */     }
/*     */     
/* 100 */     BlockState bs = level.getBlockState(targetPos.below());
/* 101 */     if (breeze.getType().isBlockDangerous(bs)) {
/* 102 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 106 */     if (!BreezeUtil.hasLineOfSight(breeze, targetPos.getCenter()) && !BreezeUtil.hasLineOfSight(breeze, targetPos.above(4).getCenter())) {
/* 107 */       return false;
/*     */     }
/*     */     
/* 110 */     breeze.getBrain().setMemory(MemoryModuleType.BREEZE_JUMP_TARGET, targetPos);
/* 111 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 116 */   protected boolean checkExtraStartConditions(ServerLevel level, Breeze breeze) { return canRun(level, breeze); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   protected boolean canStillUse(ServerLevel level, Breeze breeze, long timestamp) { return (breeze.getPose() != Pose.STANDING && !breeze.getBrain().hasMemoryValue(MemoryModuleType.BREEZE_JUMP_COOLDOWN)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Breeze breeze, long timestamp) {
/* 126 */     if (breeze.getBrain().checkMemory(MemoryModuleType.BREEZE_JUMP_INHALING, MemoryStatus.VALUE_ABSENT)) {
/* 127 */       breeze.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_JUMP_INHALING, Unit.INSTANCE, INHALING_DURATION_TICKS);
/*     */     }
/*     */     
/* 130 */     breeze.setPose(Pose.INHALING);
/* 131 */     level.playSound(null, breeze, SoundEvents.BREEZE_CHARGE, SoundSource.HOSTILE, 1.0F, 1.0F);
/* 132 */     breeze.getBrain().getMemory(MemoryModuleType.BREEZE_JUMP_TARGET)
/* 133 */       .ifPresent(targetPos -> breeze.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos.getCenter()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Breeze breeze, long timestamp) {
/* 138 */     boolean inWater = breeze.isInWater();
/* 139 */     if (!inWater && breeze.getBrain().checkMemory(MemoryModuleType.BREEZE_LEAVING_WATER, MemoryStatus.VALUE_PRESENT)) {
/* 140 */       breeze.getBrain().eraseMemory(MemoryModuleType.BREEZE_LEAVING_WATER);
/*     */     }
/* 142 */     if (isFinishedInhaling(breeze)) {
/*     */ 
/*     */ 
/*     */       
/* 146 */       Vec3 velocityVector = (Vec3)breeze.getBrain().getMemory(MemoryModuleType.BREEZE_JUMP_TARGET).flatMap(targetPos -> calculateOptimalJumpVector(breeze, breeze.getRandom(), Vec3.atBottomCenterOf(targetPos))).orElse(null);
/*     */       
/* 148 */       if (velocityVector == null) {
/* 149 */         breeze.setPose(Pose.STANDING);
/*     */         
/*     */         return;
/*     */       } 
/* 153 */       if (inWater) {
/* 154 */         breeze.getBrain().setMemory(MemoryModuleType.BREEZE_LEAVING_WATER, Unit.INSTANCE);
/*     */       }
/* 156 */       breeze.playSound(SoundEvents.BREEZE_JUMP, 1.0F, 1.0F);
/* 157 */       breeze.setPose(Pose.LONG_JUMPING);
/* 158 */       breeze.setYRot(breeze.yBodyRot);
/* 159 */       breeze.setDiscardFriction(true);
/* 160 */       breeze.setDeltaMovement(velocityVector);
/* 161 */     } else if (isFinishedJumping(breeze)) {
/* 162 */       breeze.playSound(SoundEvents.BREEZE_LAND, 1.0F, 1.0F);
/* 163 */       breeze.setPose(Pose.STANDING);
/* 164 */       breeze.setDiscardFriction(false);
/*     */       
/* 166 */       boolean wasHurt = breeze.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
/* 167 */       breeze.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_JUMP_COOLDOWN, Unit.INSTANCE, wasHurt ? 2L : 10L);
/*     */       
/* 169 */       breeze.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT, Unit.INSTANCE, 100L);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Breeze breeze, long timestamp) {
/* 175 */     if (breeze.getPose() == Pose.LONG_JUMPING || breeze.getPose() == Pose.INHALING) {
/* 176 */       breeze.setPose(Pose.STANDING);
/*     */     }
/* 178 */     breeze.getBrain().eraseMemory(MemoryModuleType.BREEZE_JUMP_TARGET);
/* 179 */     breeze.getBrain().eraseMemory(MemoryModuleType.BREEZE_JUMP_INHALING);
/* 180 */     breeze.getBrain().eraseMemory(MemoryModuleType.BREEZE_LEAVING_WATER);
/*     */   }
/*     */ 
/*     */   
/* 184 */   private static boolean isFinishedInhaling(Breeze breeze) { return (breeze.getBrain().getMemory(MemoryModuleType.BREEZE_JUMP_INHALING).isEmpty() && breeze.getPose() == Pose.INHALING); }
/*     */ 
/*     */   
/*     */   private static boolean isFinishedJumping(Breeze breeze) {
/* 188 */     boolean isJumping = (breeze.getPose() == Pose.LONG_JUMPING);
/* 189 */     boolean landedOnGround = breeze.onGround();
/* 190 */     boolean landedInWater = (breeze.isInWater() && breeze.getBrain().checkMemory(MemoryModuleType.BREEZE_LEAVING_WATER, MemoryStatus.VALUE_ABSENT));
/* 191 */     return (isJumping && (landedOnGround || landedInWater));
/*     */   }
/*     */   
/*     */   private static BlockPos snapToSurface(LivingEntity entity, Vec3 target) {
/* 195 */     ClipContext collisionBelow = new ClipContext(target, target.relative(Direction.DOWN, 10.0D), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
/* 196 */     BlockHitResult blockHitResult1 = entity.level().clip(collisionBelow);
/* 197 */     if (blockHitResult1.getType() == HitResult.Type.BLOCK) {
/* 198 */       return BlockPos.containing(blockHitResult1.getLocation()).above();
/*     */     }
/*     */     
/* 201 */     ClipContext collisionAbove = new ClipContext(target, target.relative(Direction.UP, 10.0D), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
/* 202 */     BlockHitResult blockHitResult2 = entity.level().clip(collisionAbove);
/* 203 */     if (blockHitResult2.getType() == HitResult.Type.BLOCK) {
/* 204 */       return BlockPos.containing(blockHitResult2.getLocation()).above();
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 210 */   private static boolean outOfAggroRange(Breeze breeze, LivingEntity attackTarget) { return !attackTarget.closerThan(breeze, breeze.getAttributeValue(Attributes.FOLLOW_RANGE)); }
/*     */ 
/*     */ 
/*     */   
/* 214 */   private static boolean tooCloseForJump(Breeze breeze, LivingEntity attackTarget) { return (attackTarget.distanceTo(breeze) - 4.0F <= 0.0F); }
/*     */ 
/*     */   
/*     */   private static boolean canJumpFromCurrentPosition(ServerLevel level, Breeze breeze) {
/* 218 */     BlockPos currentPos = breeze.blockPosition();
/*     */     
/* 220 */     if (level.getBlockState(currentPos).is(Blocks.HONEY_BLOCK)) {
/* 221 */       return false;
/*     */     }
/*     */     
/* 224 */     for (int i = 1; i <= 4; i++) {
/* 225 */       BlockPos offsetPos = currentPos.relative(Direction.UP, i);
/* 226 */       if (!level.getBlockState(offsetPos).isAir() && !level.getFluidState(offsetPos).is(FluidTags.WATER)) {
/* 227 */         return false;
/*     */       }
/*     */     } 
/* 230 */     return true;
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> calculateOptimalJumpVector(Breeze body, RandomSource random, Vec3 targetPos) {
/* 234 */     List<Integer> allowedAngles = Util.shuffledCopy(ALLOWED_ANGLES, random);
/*     */     
/* 236 */     for (Iterator iterator = allowedAngles.iterator(); iterator.hasNext(); ) { int angle = ((Integer)iterator.next()).intValue();
/* 237 */       float maxJumpVelocity = 0.058333334F * (float)body.getAttributeValue(Attributes.FOLLOW_RANGE);
/*     */       
/* 239 */       Optional<Vec3> velocityVector = LongJumpUtil.calculateJumpVectorForAngle(body, targetPos, maxJumpVelocity, angle, false);
/*     */       
/* 241 */       if (velocityVector.isPresent()) {
/* 242 */         if (body.hasEffect(MobEffects.JUMP_BOOST)) {
/* 243 */           double jumpEffectAmplifier = (((Vec3)velocityVector.get()).normalize()).y * body.getJumpBoostPower();
/* 244 */           return velocityVector.map(v -> v.add(0.0D, jumpEffectAmplifier, 0.0D));
/*     */         } 
/* 246 */         return velocityVector;
/*     */       }  }
/*     */     
/* 249 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\breeze\LongJump.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */