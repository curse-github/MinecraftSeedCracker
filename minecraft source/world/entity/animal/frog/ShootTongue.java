/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ public class ShootTongue
/*     */   extends Behavior<Frog>
/*     */ {
/*     */   public static final int TIME_OUT_DURATION = 100;
/*     */   public static final int CATCH_ANIMATION_DURATION = 6;
/*     */   public static final int TONGUE_ANIMATION_DURATION = 10;
/*     */   private static final float EATING_DISTANCE = 1.75F;
/*     */   private static final float EATING_MOVEMENT_FACTOR = 0.75F;
/*     */   public static final int UNREACHABLE_TONGUE_TARGETS_COOLDOWN_DURATION = 100;
/*     */   public static final int MAX_UNREACHBLE_TONGUE_TARGETS_IN_MEMORY = 5;
/*     */   private int eatAnimationTimer;
/*     */   private int calculatePathCounter;
/*     */   private final SoundEvent tongueSound;
/*     */   private final SoundEvent eatSound;
/*     */   
/*     */   private enum State
/*     */   {
/*  38 */     MOVE_TO_TARGET,
/*  39 */     CATCH_ANIMATION,
/*  40 */     EAT_ANIMATION,
/*  41 */     DONE;
/*     */   }
/*     */   
/*  44 */   private State state = State.DONE;
/*     */   
/*     */   public ShootTongue(SoundEvent tongueSound, SoundEvent eatSound) {
/*  47 */     super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), 100);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     this.tongueSound = tongueSound;
/*  55 */     this.eatSound = eatSound;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Frog body) {
/*  60 */     LivingEntity target = (LivingEntity)body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/*     */     
/*  62 */     boolean canPathfindToTarget = canPathfindToTarget(body, target);
/*  63 */     if (!canPathfindToTarget) {
/*  64 */       body.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/*  65 */       addUnreachableTargetToMemory(body, target);
/*     */     } 
/*  67 */     return (canPathfindToTarget && body
/*  68 */       .getPose() != Pose.CROAKING && 
/*  69 */       Frog.canEat(target));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Frog body, long timestamp) {
/*  74 */     return (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.state != State.DONE && 
/*     */       
/*  76 */       !body.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Frog body, long timestamp) {
/*  81 */     LivingEntity target = (LivingEntity)body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/*     */     
/*  83 */     BehaviorUtils.lookAtEntity(body, target);
/*  84 */     body.setTongueTarget(target);
/*     */     
/*  86 */     body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target.position(), 2.0F, 0));
/*  87 */     this.calculatePathCounter = 10;
/*  88 */     this.state = State.MOVE_TO_TARGET;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Frog body, long timestamp) {
/*  93 */     body.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/*  94 */     body.eraseTongueTarget();
/*  95 */     body.setPose(Pose.STANDING);
/*     */   }
/*     */   
/*     */   private void eatEntity(ServerLevel level, Frog body) {
/*  99 */     level.playSound(null, body, this.eatSound, SoundSource.NEUTRAL, 2.0F, 1.0F);
/*     */     
/* 101 */     Optional<Entity> tongueTarget = body.getTongueTarget();
/* 102 */     if (tongueTarget.isPresent()) {
/* 103 */       Entity target = (Entity)tongueTarget.get();
/* 104 */       if (target.isAlive()) {
/* 105 */         body.doHurtTarget(level, target);
/*     */         
/* 107 */         if (!target.isAlive()) {
/* 108 */           target.remove(Entity.RemovalReason.KILLED);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Frog body, long timestamp) {
/* 116 */     LivingEntity target = (LivingEntity)body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/* 117 */     body.setTongueTarget(target);
/*     */     
/* 119 */     switch (this.state.ordinal()) {
/*     */       case 0:
/* 121 */         if (target.distanceTo(body) < 1.75F) {
/* 122 */           level.playSound(null, body, this.tongueSound, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 123 */           body.setPose(Pose.USING_TONGUE);
/* 124 */           target.setDeltaMovement(target.position().vectorTo(body.position()).normalize().scale(0.75D));
/* 125 */           this.eatAnimationTimer = 0;
/* 126 */           this.state = State.CATCH_ANIMATION; break;
/*     */         } 
/* 128 */         if (this.calculatePathCounter <= 0) {
/* 129 */           body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target.position(), 2.0F, 0));
/* 130 */           this.calculatePathCounter = 10; break;
/*     */         } 
/* 132 */         this.calculatePathCounter--;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 137 */         if (this.eatAnimationTimer++ >= 6) {
/* 138 */           this.state = State.EAT_ANIMATION;
/* 139 */           eatEntity(level, body);
/*     */         } 
/*     */         break;
/*     */       case 2:
/* 143 */         if (this.eatAnimationTimer >= 10) {
/* 144 */           this.state = State.DONE; break;
/*     */         } 
/* 146 */         this.eatAnimationTimer++;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canPathfindToTarget(Frog body, LivingEntity target) {
/* 155 */     Path path = body.getNavigation().createPath(target, 0);
/* 156 */     return (path != null && path.getDistToTarget() < 1.75F);
/*     */   }
/*     */   
/*     */   private void addUnreachableTargetToMemory(Frog body, LivingEntity entity) {
/* 160 */     List<UUID> unreachableTargets = (List)body.getBrain().getMemory(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS).orElseGet(java.util.ArrayList::new);
/* 161 */     boolean shouldAddUnreachableTarget = !unreachableTargets.contains(entity.getUUID());
/*     */     
/* 163 */     if (unreachableTargets.size() == 5 && shouldAddUnreachableTarget) {
/* 164 */       unreachableTargets.remove(0);
/*     */     }
/*     */     
/* 167 */     if (shouldAddUnreachableTarget) {
/* 168 */       unreachableTargets.add(entity.getUUID());
/*     */     }
/* 170 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS, unreachableTargets, 100L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\frog\ShootTongue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */