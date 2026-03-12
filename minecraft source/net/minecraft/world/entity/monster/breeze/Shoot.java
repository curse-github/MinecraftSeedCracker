/*     */ package net.minecraft.world.entity.monster.breeze;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.BreezeWindCharge;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Shoot
/*     */   extends Behavior<Breeze>
/*     */ {
/*     */   private static final int ATTACK_RANGE_MAX_SQRT = 256;
/*     */   private static final int UNCERTAINTY_BASE = 5;
/*     */   private static final int UNCERTAINTY_MULTIPLIER = 4;
/*     */   private static final float PROJECTILE_MOVEMENT_SCALE = 0.7F;
/*  28 */   private static final int SHOOT_INITIAL_DELAY_TICKS = Math.round(15.0F);
/*  29 */   private static final int SHOOT_RECOVER_DELAY_TICKS = Math.round(4.0F);
/*  30 */   private static final int SHOOT_COOLDOWN_TICKS = Math.round(10.0F);
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  34 */   public Shoot() { super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREEZE_SHOOT_COOLDOWN, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_SHOOT_CHARGING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_SHOOT_RECOVERING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_SHOOT, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREEZE_JUMP_TARGET, MemoryStatus.VALUE_ABSENT), SHOOT_INITIAL_DELAY_TICKS + 1 + SHOOT_RECOVER_DELAY_TICKS); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Breeze breeze) {
/*  47 */     if (breeze.getPose() != Pose.STANDING) {
/*  48 */       return false;
/*     */     }
/*  50 */     return ((Boolean)breeze.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET)
/*  51 */       .map(target -> Boolean.valueOf(isTargetWithinRange(breeze, target)))
/*  52 */       .map(withinRange -> {
/*  53 */           if (!withinRange.booleanValue()) {
/*  54 */             breeze.getBrain().eraseMemory(MemoryModuleType.BREEZE_SHOOT);
/*     */           }
/*  56 */           return withinRange;
/*  57 */         }).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected boolean canStillUse(ServerLevel level, Breeze body, long timestamp) { return (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && body.getBrain().hasMemoryValue(MemoryModuleType.BREEZE_SHOOT)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Breeze breeze, long timestamp) {
/*  67 */     breeze.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target -> breeze.setPose(Pose.SHOOTING));
/*  68 */     breeze.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT_CHARGING, Unit.INSTANCE, SHOOT_INITIAL_DELAY_TICKS);
/*  69 */     breeze.playSound(SoundEvents.BREEZE_INHALE, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Breeze breeze, long timestamp) {
/*  74 */     if (breeze.getPose() == Pose.SHOOTING) {
/*  75 */       breeze.setPose(Pose.STANDING);
/*     */     }
/*  77 */     breeze.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT_COOLDOWN, Unit.INSTANCE, SHOOT_COOLDOWN_TICKS);
/*  78 */     breeze.getBrain().eraseMemory(MemoryModuleType.BREEZE_SHOOT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Breeze breeze, long timestamp) {
/*  83 */     Brain<Breeze> brain = breeze.getBrain();
/*  84 */     LivingEntity target = (LivingEntity)brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
/*  85 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  90 */     breeze.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
/*     */     
/*  92 */     if (brain.getMemory(MemoryModuleType.BREEZE_SHOOT_CHARGING).isPresent() || brain.getMemory(MemoryModuleType.BREEZE_SHOOT_RECOVERING).isPresent()) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     brain.setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT_RECOVERING, Unit.INSTANCE, SHOOT_RECOVER_DELAY_TICKS);
/*     */     
/*  98 */     double xd = target.getX() - breeze.getX();
/*  99 */     double yd = target.getY(target.isPassenger() ? 0.8D : 0.3D) - breeze.getFiringYPosition();
/* 100 */     double zd = target.getZ() - breeze.getZ();
/* 101 */     Projectile.spawnProjectileUsingShoot(new BreezeWindCharge(breeze, level), level, ItemStack.EMPTY, xd, yd, zd, 0.7F, (5 - level
/*     */ 
/*     */ 
/*     */         
/* 105 */         .getDifficulty().getId() * 4));
/*     */     
/* 107 */     breeze.playSound(SoundEvents.BREEZE_SHOOT, 1.5F, 1.0F);
/*     */   }
/*     */   
/*     */   private static boolean isTargetWithinRange(Breeze body, LivingEntity target) {
/* 111 */     double distanceSqrt = body.position().distanceToSqr(target.position());
/* 112 */     return (distanceSqrt < 256.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\breeze\Shoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */