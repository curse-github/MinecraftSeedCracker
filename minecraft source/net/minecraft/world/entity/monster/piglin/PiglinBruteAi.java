/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWith;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.StrollAroundPoi;
/*     */ import net.minecraft.world.entity.ai.behavior.StrollToPoi;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinBruteAi
/*     */ {
/*     */   private static final int ANGER_DURATION = 600;
/*     */   private static final int MELEE_ATTACK_COOLDOWN = 20;
/*     */   private static final double ACTIVITY_SOUND_LIKELIHOOD_PER_TICK = 0.0125D;
/*     */   private static final int MAX_LOOK_DIST = 8;
/*     */   private static final int INTERACTION_RANGE = 8;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;
/*     */   private static final int HOME_CLOSE_ENOUGH_DISTANCE = 2;
/*     */   private static final int HOME_TOO_FAR_DISTANCE = 100;
/*     */   private static final int HOME_STROLL_AROUND_DISTANCE = 5;
/*     */   
/*     */   protected static Brain<?> makeBrain(PiglinBrute piglin, Brain<PiglinBrute> brain) {
/*  54 */     initCoreActivity(piglin, brain);
/*     */     
/*  56 */     initIdleActivity(piglin, brain);
/*  57 */     initFightActivity(piglin, brain);
/*     */     
/*  59 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/*  60 */     brain.setDefaultActivity(Activity.IDLE);
/*  61 */     brain.useDefaultActivity();
/*     */     
/*  63 */     return brain;
/*     */   }
/*     */   
/*     */   protected static void initMemories(PiglinBrute body) {
/*  67 */     GlobalPos currentGlobalPos = GlobalPos.of(body.level().dimension(), body.blockPosition());
/*  68 */     body.getBrain().setMemory(MemoryModuleType.HOME, currentGlobalPos);
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(PiglinBrute body, Brain<PiglinBrute> brain) {
/*  72 */     brain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */ 
/*     */           
/*  75 */           InteractWithDoor.create(), 
/*  76 */           StopBeingAngryIfTargetDead.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(PiglinBrute body, Brain<PiglinBrute> brain) {
/*  81 */     brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/*  82 */           StartAttacking.create(PiglinBruteAi::findNearestValidAttackTarget), 
/*  83 */           createIdleLookBehaviors(), 
/*  84 */           createIdleMovementBehaviors(), 
/*  85 */           SetLookAndInteract.create(EntityType.PLAYER, 4)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  90 */   private static void initFightActivity(PiglinBrute body, Brain<PiglinBrute> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
/*  91 */           StopAttackingIfTargetInvalid.create((level, target) -> !isNearestValidAttackTarget(level, body, target)), 
/*  92 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/*  93 */           MeleeAttack.create(20)), MemoryModuleType.ATTACK_TARGET); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static RunOne<PiglinBrute> createIdleLookBehaviors() {
/*  98 */     return new RunOne(ImmutableList.of(
/*  99 */           Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), Integer.valueOf(1)), 
/* 100 */           Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 101 */           Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN_BRUTE, 8.0F), Integer.valueOf(1)), 
/* 102 */           Pair.of(SetEntityLookTarget.create(8.0F), Integer.valueOf(1)), 
/* 103 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<PiglinBrute> createIdleMovementBehaviors() {
/* 108 */     return new RunOne(ImmutableList.of(
/* 109 */           Pair.of(RandomStroll.stroll(0.6F), Integer.valueOf(2)), 
/* 110 */           Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 111 */           Pair.of(InteractWith.of(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 112 */           Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 0.6F, 2, 100), Integer.valueOf(2)), 
/* 113 */           Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 0.6F, 5), Integer.valueOf(2)), 
/* 114 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void updateActivity(PiglinBrute body) {
/* 119 */     Brain<PiglinBrute> brain = body.getBrain();
/*     */ 
/*     */ 
/*     */     
/* 123 */     Activity oldActivity = (Activity)brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */ 
/*     */     
/* 127 */     brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     Activity newActivity = (Activity)brain.getActiveNonCoreActivity().orElse(null);
/* 133 */     if (oldActivity != newActivity)
/*     */     {
/* 135 */       playActivitySound(body);
/*     */     }
/*     */ 
/*     */     
/* 139 */     body.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */ 
/*     */   
/* 143 */   private static boolean isNearestValidAttackTarget(ServerLevel level, AbstractPiglin body, LivingEntity target) { return findNearestValidAttackTarget(level, body)
/* 144 */       .filter(nearestValidTarget -> (nearestValidTarget == target))
/* 145 */       .isPresent(); }
/*     */ 
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, AbstractPiglin body) {
/* 149 */     Optional<LivingEntity> angryAt = BehaviorUtils.getLivingEntityFromUUIDMemory(body, MemoryModuleType.ANGRY_AT);
/* 150 */     if (angryAt.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(level, body, (LivingEntity)angryAt.get())) {
/* 151 */       return angryAt;
/*     */     }
/*     */     
/* 154 */     Optional<? extends LivingEntity> player = body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/* 155 */     if (player.isPresent()) {
/* 156 */       return player;
/*     */     }
/*     */     
/* 159 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel level, PiglinBrute body, LivingEntity attacker) {
/* 164 */     if (attacker instanceof AbstractPiglin) {
/*     */       return;
/*     */     }
/*     */     
/* 168 */     PiglinAi.maybeRetaliate(level, body, attacker);
/*     */   }
/*     */   
/*     */   protected static void setAngerTarget(PiglinBrute body, LivingEntity target) {
/* 172 */     body.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 173 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600L);
/*     */   }
/*     */   
/*     */   protected static void maybePlayActivitySound(PiglinBrute body) {
/* 177 */     if ((body.level()).random.nextFloat() < 0.0125D) {
/* 178 */       playActivitySound(body);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void playActivitySound(PiglinBrute body) {
/* 184 */     body.getBrain().getActiveNonCoreActivity().ifPresent(activity -> {
/* 185 */           if (activity == Activity.FIGHT)
/* 186 */             body.playAngrySound(); 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\PiglinBruteAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */