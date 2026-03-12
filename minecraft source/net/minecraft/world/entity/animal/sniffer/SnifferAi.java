/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SnifferAi
/*     */ {
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_LOOK_DISTANCE = 6;
/*     */   
/*  47 */   static final List<SensorType<? extends Sensor<? super Sniffer>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_PLAYERS, SensorType.FOOD_TEMPTATIONS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   static final List<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.IS_PANICKING, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryModuleType.SNIFFER_DIGGING, MemoryModuleType.SNIFFER_HAPPY, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.SNIFFER_EXPLORED_POSITIONS, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.BREED_TARGET, new MemoryModuleType[] { MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED });
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SNIFFING_COOLDOWN_TICKS = 9600;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_SNIFFING = 1.25F;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Sniffer> brain) {
/*  80 */     initCoreActivity(brain);
/*  81 */     initIdleActivity(brain);
/*  82 */     initSniffingActivity(brain);
/*  83 */     initDigActivity(brain);
/*     */     
/*  85 */     brain.setCoreActivities(Set.of(Activity.CORE));
/*  86 */     brain.setDefaultActivity(Activity.IDLE);
/*  87 */     brain.useDefaultActivity();
/*  88 */     return brain;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Sniffer resetSniffing(Sniffer body) {
/*  93 */     body.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
/*  94 */     body.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/*     */     
/*  96 */     return body.transitionTo(Sniffer.State.IDLING);
/*     */   }
/*     */ 
/*     */   
/* 100 */   private static void initCoreActivity(Brain<Sniffer> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic<Sniffer>(2.0F)
/*     */           {
/*     */             
/*     */             protected void start(ServerLevel level, Sniffer body, long timestamp)
/*     */             {
/* 105 */               SnifferAi.resetSniffing(body);
/* 106 */               super.start(level, body, timestamp);
/*     */             }
/*     */           }new MoveToTargetSink(500, 700), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initSniffingActivity(Brain<Sniffer> brain) {
/* 115 */     brain.addActivityWithConditions(Activity.SNIFF, 
/* 116 */         ImmutableList.of(
/* 117 */           Pair.of(Integer.valueOf(0), new Searching())), 
/* 118 */         Set.of(
/* 119 */           Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), 
/* 120 */           Pair.of(MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT), 
/* 121 */           Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initDigActivity(Brain<Sniffer> brain) {
/* 126 */     brain.addActivityWithConditions(Activity.DIG, 
/* 127 */         ImmutableList.of(
/* 128 */           Pair.of(Integer.valueOf(0), new Digging(160, 180)), 
/* 129 */           Pair.of(Integer.valueOf(0), new FinishedDigging(40))), 
/*     */         
/* 131 */         Set.of(
/* 132 */           Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), 
/* 133 */           Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 134 */           Pair.of(MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   private static void initIdleActivity(Brain<Sniffer> brain) { brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
/* 141 */           Pair.of(Integer.valueOf(0), new AnimalMakeLove(EntityType.SNIFFER)
/*     */             {
/*     */               protected void start(ServerLevel level, Animal body, long timestamp) {
/* 144 */                 SnifferAi.resetSniffing((Sniffer)body);
/* 145 */                 super.start(level, body, timestamp);
/*     */               }
/* 148 */             }), Pair.of(Integer.valueOf(1), new FollowTemptation(sniffer -> Float.valueOf(1.25F), sniffer -> 
/* 149 */               Double.valueOf(sniffer.isBaby() ? 2.5D : 3.5D))
/*     */             {
/*     */               protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/* 152 */                 SnifferAi.resetSniffing((Sniffer)body);
/* 153 */                 super.start(level, body, timestamp);
/*     */               }
/* 156 */             }), Pair.of(Integer.valueOf(2), new LookAtTargetSink(45, 90)), 
/* 157 */           Pair.of(Integer.valueOf(3), new FeelingHappy(40, 100)), 
/* 158 */           Pair.of(Integer.valueOf(4), new RunOne(ImmutableList.of(
/* 159 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(2)), 
/* 160 */                 Pair.of(new Scenting(40, 80), Integer.valueOf(1)), 
/* 161 */                 Pair.of(new Sniffing(40, 80), Integer.valueOf(1)), 
/* 162 */                 Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 6.0F), Integer.valueOf(1)), 
/* 163 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(1)), 
/* 164 */                 Pair.of(new DoNothing(5, 20), Integer.valueOf(2)))))), 
/*     */ 
/*     */         
/* 167 */         Set.of(
/* 168 */           Pair.of(MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_ABSENT))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   static void updateActivity(Sniffer body) { body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.DIG, Activity.SNIFF, Activity.IDLE)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Sniffing
/*     */     extends Behavior<Sniffer>
/*     */   {
/* 182 */     private Sniffing(int min, int max) { super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), min, max); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer body) { return (!body.isBaby() && body.canSniff()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     protected boolean canStillUse(ServerLevel level, Sniffer body, long timestamp) { return body.canSniff(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.SNIFFING); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 207 */       boolean finished = timedOut(timestamp);
/* 208 */       sniffer.transitionTo(Sniffer.State.IDLING);
/*     */       
/* 210 */       if (finished)
/* 211 */         sniffer.calculateDigPosition().ifPresent(position -> {
/* 212 */               sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET, position);
/* 213 */               sniffer.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(position, 1.25F, 0));
/*     */             }); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Searching
/*     */     extends Behavior<Sniffer>
/*     */   {
/* 221 */     private Searching() { super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT), 600); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) { return sniffer.canSniff(); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 237 */       if (!sniffer.canSniff()) {
/* 238 */         sniffer.transitionTo(Sniffer.State.IDLING);
/* 239 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 245 */       Optional<BlockPos> walkTarget = sniffer.getBrain().getMemory(MemoryModuleType.WALK_TARGET).map(WalkTarget::getTarget).map(PositionTracker::currentBlockPosition);
/*     */       
/* 247 */       Optional<BlockPos> sniffingTarget = sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/*     */       
/* 249 */       if (walkTarget.isEmpty() || sniffingTarget.isEmpty()) {
/* 250 */         return false;
/*     */       }
/*     */       
/* 253 */       return ((BlockPos)sniffingTarget.get()).equals(walkTarget.get());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 258 */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.SEARCHING); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 264 */       if (sniffer.canDig() && sniffer.canSniff()) {
/* 265 */         sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_DIGGING, Boolean.valueOf(true));
/*     */       }
/*     */ 
/*     */       
/* 269 */       sniffer.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 270 */       sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Digging
/*     */     extends Behavior<Sniffer> {
/* 276 */     private Digging(int min, int max) { super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), min, max); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 286 */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) { return sniffer.canSniff(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) { return (sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent() && sniffer.canDig() && !sniffer.isInLove()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 296 */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.DIGGING); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 301 */       boolean finished = timedOut(timestamp);
/*     */       
/* 303 */       if (finished) {
/* 304 */         sniffer.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 9600L);
/*     */       } else {
/* 306 */         SnifferAi.resetSniffing(sniffer);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FinishedDigging
/*     */     extends Behavior<Sniffer> {
/* 313 */     private FinishedDigging(int duration) { super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_PRESENT), duration, duration); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 328 */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) { return sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 333 */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.RISING); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 338 */       boolean finished = timedOut(timestamp);
/*     */       
/* 340 */       sniffer
/* 341 */         .transitionTo(Sniffer.State.IDLING)
/* 342 */         .onDiggingComplete(finished);
/*     */ 
/*     */       
/* 345 */       sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
/* 346 */       sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_HAPPY, Boolean.valueOf(true));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FeelingHappy
/*     */     extends Behavior<Sniffer> {
/* 352 */     private FeelingHappy(int min, int max) { super(Map.of(MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_PRESENT), min, max); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 359 */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 364 */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.FEELING_HAPPY); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 369 */       sniffer.transitionTo(Sniffer.State.IDLING);
/* 370 */       sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_HAPPY);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Scenting
/*     */     extends Behavior<Sniffer> {
/* 376 */     private Scenting(int min, int max) { super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), min, max); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 388 */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) { return !sniffer.isTempted(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 393 */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 398 */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.SCENTING); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 403 */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) { sniffer.transitionTo(Sniffer.State.IDLING); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */