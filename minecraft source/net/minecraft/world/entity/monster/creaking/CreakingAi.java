/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ 
/*     */ public class CreakingAi
/*     */ {
/*  34 */   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Creaking>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
/*     */ 
/*     */ 
/*     */   
/*  38 */   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   static void initCoreActivity(Brain<Creaking> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim<Creaking>(0.8F)
/*     */           {
/*     */             protected boolean checkExtraStartConditions(ServerLevel level, Creaking body)
/*     */             {
/*  57 */               return (body.canMove() && super.checkExtraStartConditions(level, body));
/*     */             }
/*     */           }new LookAtTargetSink(45, 90), new MoveToTargetSink())); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void initIdleActivity(Brain<Creaking> brain) {
/*  66 */     brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/*  67 */           StartAttacking.create((level, creaking) -> creaking.isActive(), (level, creaking) -> creaking.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)), 
/*  68 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), new RunOne(
/*  69 */             ImmutableList.of(
/*  70 */               Pair.of(RandomStroll.stroll(0.3F), Integer.valueOf(2)), 
/*  71 */               Pair.of(SetWalkTargetFromLookTarget.create(0.3F, 3), Integer.valueOf(2)), 
/*  72 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   static void initFightActivity(Creaking body, Brain<Creaking> brain) { brain.addActivityWithConditions(Activity.FIGHT, 10, ImmutableList.of(
/*  79 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/*  80 */           MeleeAttack.create(Creaking::canMove, 40), 
/*  81 */           StopAttackingIfTargetInvalid.create((level, target) -> !isAttackTargetStillReachable(body, target))), 
/*  82 */         ImmutableSet.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT))); }
/*     */ 
/*     */   
/*     */   private static boolean isAttackTargetStillReachable(Creaking creaking, LivingEntity target) {
/*  86 */     Optional<List<Player>> visibleAttackablePlayers = creaking.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS);
/*  87 */     return ((Boolean)visibleAttackablePlayers.map(players -> { if (target instanceof Player) { Player player = (Player)target; if (players.contains(player)); }  return Boolean.valueOf(false); }).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*  91 */   public static Brain.Provider<Creaking> brainProvider() { return Brain.provider(MEMORY_TYPES, SENSOR_TYPES); }
/*     */ 
/*     */   
/*     */   public static Brain<Creaking> makeBrain(Creaking creaking, Brain<Creaking> brain) {
/*  95 */     initCoreActivity(brain);
/*  96 */     initIdleActivity(brain);
/*  97 */     initFightActivity(creaking, brain);
/*     */     
/*  99 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/* 100 */     brain.setDefaultActivity(Activity.IDLE);
/* 101 */     brain.useDefaultActivity();
/* 102 */     return brain;
/*     */   }
/*     */   
/*     */   public static void updateActivity(Creaking creaking) {
/* 106 */     if (!creaking.canMove()) {
/* 107 */       creaking.getBrain().useDefaultActivity();
/*     */     } else {
/* 109 */       creaking.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\creaking\CreakingAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */