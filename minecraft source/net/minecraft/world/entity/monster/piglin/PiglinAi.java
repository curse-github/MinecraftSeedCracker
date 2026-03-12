/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.BackUpIfTooClose;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.CopyMemoryWithExpiry;
/*     */ import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.DismountOrSkipMounting;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToTargetLocation;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWith;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.Mount;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearApproach;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearRetreat;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StartCelebratingIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.TriggerGate;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinAi
/*     */ {
/*     */   public static final int REPELLENT_DETECTION_RANGE_HORIZONTAL = 8;
/*     */   public static final int REPELLENT_DETECTION_RANGE_VERTICAL = 4;
/*  88 */   public static final Item BARTERING_ITEM = Items.GOLD_INGOT;
/*     */   
/*     */   private static final int PLAYER_ANGER_RANGE = 16;
/*     */   private static final int ANGER_DURATION = 600;
/*     */   private static final int ADMIRE_DURATION = 119;
/*     */   private static final int MAX_DISTANCE_TO_WALK_TO_ITEM = 9;
/*     */   private static final int MAX_TIME_TO_WALK_TO_ITEM = 200;
/*     */   private static final int HOW_LONG_TIME_TO_DISABLE_ADMIRE_WALKING_IF_CANT_REACH_ITEM = 200;
/*     */   private static final int CELEBRATION_TIME = 300;
/*  97 */   protected static final UniformInt TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);
/*     */   private static final int BABY_FLEE_DURATION_AFTER_GETTING_HIT = 100;
/*     */   private static final int HIT_BY_PLAYER_MEMORY_TIMEOUT = 400;
/*     */   private static final int MAX_WALK_DISTANCE_TO_START_RIDING = 8;
/* 101 */   private static final UniformInt RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
/* 102 */   private static final UniformInt RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
/* 103 */   private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
/*     */   private static final int MELEE_ATTACK_COOLDOWN = 20;
/*     */   private static final int EAT_COOLDOWN = 200;
/*     */   private static final int DESIRED_DISTANCE_FROM_ENTITY_WHEN_AVOIDING = 12;
/*     */   private static final int MAX_LOOK_DIST = 8;
/*     */   private static final int MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM = 14;
/*     */   private static final int INTERACTION_RANGE = 8;
/*     */   private static final int MIN_DESIRED_DIST_FROM_TARGET_WHEN_HOLDING_CROSSBOW = 5;
/*     */   private static final float SPEED_WHEN_STRAFING_BACK_FROM_TARGET = 0.75F;
/*     */   private static final int DESIRED_DISTANCE_FROM_ZOMBIFIED = 6;
/* 113 */   private static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
/* 114 */   private static final UniformInt BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);
/*     */   
/*     */   private static final float PROBABILITY_OF_CELEBRATION_DANCE = 0.1F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_AVOIDING = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETREATING = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MOUNTING = 0.8F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_WANTED_ITEM = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_CELEBRATE_LOCATION = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_DANCING = 0.6F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;
/*     */   
/*     */   protected static Brain<?> makeBrain(Piglin piglin, Brain<Piglin> brain) {
/* 127 */     initCoreActivity(brain);
/*     */     
/* 129 */     initIdleActivity(brain);
/*     */     
/* 131 */     initAdmireItemActivity(brain);
/*     */     
/* 133 */     initFightActivity(piglin, brain);
/* 134 */     initCelebrateActivity(brain);
/*     */     
/* 136 */     initRetreatActivity(brain);
/* 137 */     initRideHoglinActivity(brain);
/*     */     
/* 139 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/* 140 */     brain.setDefaultActivity(Activity.IDLE);
/* 141 */     brain.useDefaultActivity();
/*     */     
/* 143 */     return brain;
/*     */   }
/*     */   
/*     */   protected static void initMemories(Piglin body, RandomSource random) {
/* 147 */     int delayUntilFirstHunt = TIME_BETWEEN_HUNTS.sample(random);
/* 148 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, Boolean.valueOf(true), delayUntilFirstHunt);
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Piglin> brain) {
/* 152 */     brain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */ 
/*     */           
/* 155 */           InteractWithDoor.create(), 
/* 156 */           babyAvoidNemesis(), 
/* 157 */           avoidZombified(), 
/* 158 */           StopHoldingItemIfNoLongerAdmiring.create(), 
/* 159 */           StartAdmiringItemIfSeen.create(119), 
/* 160 */           StartCelebratingIfTargetDead.create(300, PiglinAi::wantsToDance), 
/* 161 */           StopBeingAngryIfTargetDead.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Piglin> brain) {
/* 166 */     brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/* 167 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 14.0F), 
/* 168 */           StartAttacking.create((level, piglin) -> piglin.isAdult(), PiglinAi::findNearestValidAttackTarget), 
/* 169 */           BehaviorBuilder.triggerIf(Piglin::canHunt, StartHuntingHoglin.create()), 
/* 170 */           avoidRepellent(), 
/* 171 */           babySometimesRideBabyHoglin(), 
/* 172 */           createIdleLookBehaviors(), 
/* 173 */           createIdleMovementBehaviors(), 
/* 174 */           SetLookAndInteract.create(EntityType.PLAYER, 4)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 179 */   private static void initFightActivity(Piglin body, Brain<Piglin> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
/* 180 */           StopAttackingIfTargetInvalid.create((level, target) -> !isNearestValidAttackTarget(level, body, target)), 
/* 181 */           BehaviorBuilder.triggerIf(PiglinAi::hasCrossbow, BackUpIfTooClose.create(5, 0.75F)), 
/* 182 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), new SpearApproach(1.0D, 10.0F), new SpearAttack(1.0D, 1.0D, 10.0F, 2.0F), new SpearRetreat(1.0D), 
/*     */ 
/*     */ 
/*     */           
/* 186 */           MeleeAttack.create(20), new CrossbowAttack(), 
/*     */           
/* 188 */           RememberIfHoglinWasKilled.create(), 
/* 189 */           EraseMemoryIf.create(PiglinAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   private static void initCelebrateActivity(Brain<Piglin> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10, ImmutableList.of(
/* 195 */           avoidRepellent(), 
/* 196 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 14.0F), 
/* 197 */           StartAttacking.create((level, piglin) -> piglin.isAdult(), PiglinAi::findNearestValidAttackTarget), 
/* 198 */           BehaviorBuilder.triggerIf(body -> !body.isDancing(), GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 2, 1.0F)), 
/* 199 */           BehaviorBuilder.triggerIf(Piglin::isDancing, GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 4, 0.6F)), new RunOne(
/* 200 */             ImmutableList.of(
/* 201 */               Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 202 */               Pair.of(RandomStroll.stroll(0.6F, 2, 1), Integer.valueOf(1)), 
/* 203 */               Pair.of(new DoNothing(10, 20), Integer.valueOf(1))))), MemoryModuleType.CELEBRATE_LOCATION); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   private static void initAdmireItemActivity(Brain<Piglin> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10, ImmutableList.of(
/* 210 */           GoToWantedItem.create(PiglinAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9), 
/* 211 */           StopAdmiringIfItemTooFarAway.create(9), 
/* 212 */           StopAdmiringIfTiredOfTryingToReachItem.create(200, 200)), MemoryModuleType.ADMIRING_ITEM); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   private static void initRetreatActivity(Brain<Piglin> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(
/* 218 */           SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), 
/* 219 */           createIdleLookBehaviors(), 
/* 220 */           createIdleMovementBehaviors(), 
/* 221 */           EraseMemoryIf.create(PiglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   private static void initRideHoglinActivity(Brain<Piglin> brain) { brain.addActivityAndRemoveMemoryWhenStopped(Activity.RIDE, 10, ImmutableList.of(
/* 227 */           Mount.create(0.8F), 
/* 228 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 8.0F), 
/* 229 */           BehaviorBuilder.sequence(
/* 230 */             BehaviorBuilder.triggerIf(Entity::isPassenger), 
/* 231 */             TriggerGate.triggerOneShuffled(
/* 232 */               ImmutableList.builder()
/* 233 */               .addAll(createLookBehaviors())
/* 234 */               .add(Pair.of(BehaviorBuilder.triggerIf(e -> true), Integer.valueOf(1)))
/* 235 */               .build())), 
/*     */ 
/*     */           
/* 238 */           DismountOrSkipMounting.create(8, PiglinAi::wantsToStopRiding)), MemoryModuleType.RIDE_TARGET); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImmutableList<Pair<OneShot<LivingEntity>, Integer>> createLookBehaviors() {
/* 244 */     return ImmutableList.of(
/* 245 */         Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), Integer.valueOf(1)), 
/* 246 */         Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 247 */         Pair.of(SetEntityLookTarget.create(8.0F), Integer.valueOf(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<LivingEntity> createIdleLookBehaviors() {
/* 252 */     return new RunOne(
/* 253 */         ImmutableList.builder()
/* 254 */         .addAll(createLookBehaviors())
/* 255 */         .add(Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))
/* 256 */         .build());
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<Piglin> createIdleMovementBehaviors() {
/* 261 */     return new RunOne(ImmutableList.of(
/* 262 */           Pair.of(RandomStroll.stroll(0.6F), Integer.valueOf(2)), 
/*     */           
/* 264 */           Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 265 */           Pair.of(BehaviorBuilder.triggerIf(PiglinAi::doesntSeeAnyPlayerHoldingLovedItem, SetWalkTargetFromLookTarget.create(0.6F, 3)), Integer.valueOf(2)), 
/* 266 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 271 */   private static BehaviorControl<PathfinderMob> avoidRepellent() { return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false); }
/*     */ 
/*     */ 
/*     */   
/* 275 */   private static BehaviorControl<Piglin> babyAvoidNemesis() { return CopyMemoryWithExpiry.create(Piglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, BABY_AVOID_NEMESIS_DURATION); }
/*     */ 
/*     */ 
/*     */   
/* 279 */   private static BehaviorControl<Piglin> avoidZombified() { return CopyMemoryWithExpiry.create(PiglinAi::isNearZombified, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIFIED_DURATION); }
/*     */ 
/*     */   
/*     */   protected static void updateActivity(Piglin body) {
/* 283 */     Brain<Piglin> brain = body.getBrain();
/*     */     
/* 285 */     Activity oldActivity = (Activity)brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */ 
/*     */     
/* 289 */     brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.RIDE, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     Activity newActivity = (Activity)brain.getActiveNonCoreActivity().orElse(null);
/* 299 */     if (oldActivity != newActivity) {
/*     */       
/* 301 */       Objects.requireNonNull(body); getSoundForCurrentActivity(body).ifPresent(body::makeSound);
/*     */     } 
/*     */ 
/*     */     
/* 305 */     body.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */     
/* 307 */     if (!brain.hasMemoryValue(MemoryModuleType.RIDE_TARGET) && isBabyRidingBaby(body))
/*     */     {
/*     */ 
/*     */       
/* 311 */       body.stopRiding();
/*     */     }
/*     */     
/* 314 */     if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION))
/*     */     {
/*     */       
/* 317 */       brain.eraseMemory(MemoryModuleType.DANCING);
/*     */     }
/* 319 */     body.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
/*     */   }
/*     */   
/*     */   private static boolean isBabyRidingBaby(Piglin body) {
/* 323 */     if (!body.isBaby()) {
/* 324 */       return false;
/*     */     }
/* 326 */     Entity vehicle = body.getVehicle();
/* 327 */     return ((vehicle instanceof Piglin && ((Piglin)vehicle).isBaby()) || (vehicle instanceof Hoglin && ((Hoglin)vehicle)
/* 328 */       .isBaby()));
/*     */   }
/*     */   protected static void pickUpItem(ServerLevel level, Piglin body, ItemEntity itemEntity) {
/*     */     ItemStack taken;
/* 332 */     stopWalking(body);
/*     */ 
/*     */ 
/*     */     
/* 336 */     if (itemEntity.getItem().is(Items.GOLD_NUGGET)) {
/*     */ 
/*     */       
/* 339 */       body.take(itemEntity, itemEntity.getItem().getCount());
/* 340 */       taken = itemEntity.getItem();
/* 341 */       itemEntity.discard();
/*     */     } else {
/* 343 */       body.take(itemEntity, 1);
/* 344 */       taken = removeOneItemFromItemEntity(itemEntity);
/*     */     } 
/*     */     
/* 347 */     if (isLovedItem(taken)) {
/* 348 */       body.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
/* 349 */       holdInOffhand(level, body, taken);
/* 350 */       admireGoldItem(body);
/*     */       
/*     */       return;
/*     */     } 
/* 354 */     if (isFood(taken) && !hasEatenRecently(body)) {
/* 355 */       eat(body);
/*     */       
/*     */       return;
/*     */     } 
/* 359 */     boolean itemEquipped = !body.equipItemIfPossible(level, taken).equals(ItemStack.EMPTY);
/* 360 */     if (itemEquipped) {
/*     */       return;
/*     */     }
/*     */     
/* 364 */     putInInventory(body, taken);
/*     */   }
/*     */   
/*     */   private static void holdInOffhand(ServerLevel level, Piglin body, ItemStack itemStack) {
/* 368 */     if (isHoldingItemInOffHand(body)) {
/* 369 */       body.spawnAtLocation(level, body.getItemInHand(InteractionHand.OFF_HAND));
/*     */     }
/* 371 */     body.holdInOffHand(itemStack);
/*     */   }
/*     */   
/*     */   private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
/* 375 */     ItemStack sourceStack = itemEntity.getItem();
/* 376 */     ItemStack removedStack = sourceStack.split(1);
/* 377 */     if (sourceStack.isEmpty()) {
/* 378 */       itemEntity.discard();
/*     */     } else {
/* 380 */       itemEntity.setItem(sourceStack);
/*     */     } 
/* 382 */     return removedStack;
/*     */   }
/*     */   
/*     */   protected static void stopHoldingOffHandItem(ServerLevel level, Piglin body, boolean barteringEnabled) {
/* 386 */     ItemStack itemStack = body.getItemInHand(InteractionHand.OFF_HAND);
/* 387 */     body.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*     */     
/* 389 */     if (body.isAdult()) {
/* 390 */       boolean barterCurrency = isBarterCurrency(itemStack);
/* 391 */       if (barteringEnabled && barterCurrency) {
/* 392 */         throwItems(body, getBarterResponseItems(body));
/* 393 */       } else if (!barterCurrency) {
/* 394 */         boolean equipped = !body.equipItemIfPossible(level, itemStack).isEmpty();
/* 395 */         if (!equipped) {
/* 396 */           putInInventory(body, itemStack);
/*     */         }
/*     */       } 
/*     */     } else {
/* 400 */       boolean equipped = !body.equipItemIfPossible(level, itemStack).isEmpty();
/* 401 */       if (!equipped) {
/*     */ 
/*     */ 
/*     */         
/* 405 */         ItemStack mainHandItem = body.getMainHandItem();
/* 406 */         if (isLovedItem(mainHandItem)) {
/* 407 */           putInInventory(body, mainHandItem);
/*     */         } else {
/* 409 */           throwItems(body, Collections.singletonList(mainHandItem));
/*     */         } 
/* 411 */         body.holdInMainHand(itemStack);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void cancelAdmiring(ServerLevel level, Piglin body) {
/* 417 */     if (isAdmiringItem(body) && !body.getOffhandItem().isEmpty()) {
/* 418 */       body.spawnAtLocation(level, body.getOffhandItem());
/* 419 */       body.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void putInInventory(Piglin body, ItemStack itemStack) {
/* 424 */     ItemStack stuffThatCouldntFitInMyInventory = body.addToInventory(itemStack);
/* 425 */     throwItemsTowardRandomPos(body, Collections.singletonList(stuffThatCouldntFitInMyInventory));
/*     */   }
/*     */   
/*     */   private static void throwItems(Piglin body, List<ItemStack> itemStacks) {
/* 429 */     Optional<Player> player = body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
/* 430 */     if (player.isPresent()) {
/* 431 */       throwItemsTowardPlayer(body, (Player)player.get(), itemStacks);
/*     */     } else {
/* 433 */       throwItemsTowardRandomPos(body, itemStacks);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 438 */   private static void throwItemsTowardRandomPos(Piglin body, List<ItemStack> itemStacks) { throwItemsTowardPos(body, itemStacks, getRandomNearbyPos(body)); }
/*     */ 
/*     */ 
/*     */   
/* 442 */   private static void throwItemsTowardPlayer(Piglin body, Player player, List<ItemStack> itemStacks) { throwItemsTowardPos(body, itemStacks, player.position()); }
/*     */ 
/*     */   
/*     */   private static void throwItemsTowardPos(Piglin body, List<ItemStack> itemStacks, Vec3 targetPos) {
/* 446 */     if (!itemStacks.isEmpty()) {
/* 447 */       body.swing(InteractionHand.OFF_HAND);
/* 448 */       for (ItemStack itemStack : itemStacks) {
/* 449 */         BehaviorUtils.throwItem(body, itemStack, targetPos.add(0.0D, 1.0D, 0.0D));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<ItemStack> getBarterResponseItems(Piglin body) {
/* 455 */     LootTable lootTable = body.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
/* 456 */     return lootTable.getRandomItems((new LootParams.Builder((ServerLevel)body.level()))
/* 457 */         .withParameter(LootContextParams.THIS_ENTITY, body)
/* 458 */         .create(LootContextParamSets.PIGLIN_BARTER));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean wantsToDance(LivingEntity body, LivingEntity killedTarget) {
/* 463 */     if (killedTarget.getType() != EntityType.HOGLIN) {
/* 464 */       return false;
/*     */     }
/*     */     
/* 467 */     return (RandomSource.create(body.level().getGameTime()).nextFloat() < 0.1F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean wantsToPickup(Piglin body, ItemStack itemStack) {
/* 476 */     if (body.isBaby() && itemStack.is(ItemTags.IGNORED_BY_PIGLIN_BABIES)) {
/* 477 */       return false;
/*     */     }
/*     */     
/* 480 */     if (itemStack.is(ItemTags.PIGLIN_REPELLENTS)) {
/* 481 */       return false;
/*     */     }
/* 483 */     if (isAdmiringDisabled(body) && body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
/* 484 */       return false;
/*     */     }
/* 486 */     if (isBarterCurrency(itemStack)) {
/* 487 */       return isNotHoldingLovedItemInOffHand(body);
/*     */     }
/*     */     
/* 490 */     boolean hasSpace = body.canAddToInventory(itemStack);
/* 491 */     if (itemStack.is(Items.GOLD_NUGGET)) {
/* 492 */       return hasSpace;
/*     */     }
/* 494 */     if (isFood(itemStack)) {
/* 495 */       return (!hasEatenRecently(body) && hasSpace);
/*     */     }
/* 497 */     if (isLovedItem(itemStack)) {
/* 498 */       return (isNotHoldingLovedItemInOffHand(body) && hasSpace);
/*     */     }
/* 500 */     return body.canReplaceCurrentItem(itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 505 */   protected static boolean isLovedItem(ItemStack itemStack) { return itemStack.is(ItemTags.PIGLIN_LOVED); }
/*     */ 
/*     */   
/*     */   private static boolean wantsToStopRiding(Piglin body, Entity entityBeingRidden) {
/* 509 */     if (entityBeingRidden instanceof Mob) { Mob mobBeingRidden = (Mob)entityBeingRidden;
/* 510 */       return (!mobBeingRidden.isBaby() || 
/* 511 */         !mobBeingRidden.isAlive() || 
/* 512 */         wasHurtRecently(body) || 
/* 513 */         wasHurtRecently(mobBeingRidden) || (mobBeingRidden instanceof Piglin && mobBeingRidden
/* 514 */         .getVehicle() == null)); }
/*     */     
/* 516 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 520 */   private static boolean isNearestValidAttackTarget(ServerLevel level, Piglin body, LivingEntity target) { return findNearestValidAttackTarget(level, body)
/* 521 */       .filter(nearestValidTarget -> (nearestValidTarget == target))
/* 522 */       .isPresent(); }
/*     */ 
/*     */   
/*     */   private static boolean isNearZombified(Piglin body) {
/* 526 */     Brain<Piglin> brain = body.getBrain();
/* 527 */     if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
/* 528 */       LivingEntity zombified = (LivingEntity)brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
/* 529 */       return body.closerThan(zombified, 6.0D);
/*     */     } 
/* 531 */     return false;
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, Piglin body) {
/* 535 */     Brain<Piglin> brain = body.getBrain();
/*     */     
/* 537 */     if (isNearZombified(body)) {
/* 538 */       return Optional.empty();
/*     */     }
/*     */     
/* 541 */     Optional<LivingEntity> angryAt = BehaviorUtils.getLivingEntityFromUUIDMemory(body, MemoryModuleType.ANGRY_AT);
/* 542 */     if (angryAt.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(level, body, (LivingEntity)angryAt.get())) {
/* 543 */       return angryAt;
/*     */     }
/*     */     
/* 546 */     if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
/* 547 */       Optional<Player> player = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/* 548 */       if (player.isPresent()) {
/* 549 */         return player;
/*     */       }
/*     */     } 
/*     */     
/* 553 */     Optional<Mob> nemesis = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
/* 554 */     if (nemesis.isPresent()) {
/* 555 */       return nemesis;
/*     */     }
/*     */     
/* 558 */     Optional<Player> playerNotWearingGold = brain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
/* 559 */     if (playerNotWearingGold.isPresent() && Sensor.isEntityAttackable(level, body, (LivingEntity)playerNotWearingGold.get())) {
/* 560 */       return playerNotWearingGold;
/*     */     }
/*     */     
/* 563 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static void angerNearbyPiglins(ServerLevel level, Player player, boolean onlyIfTheySeeThePlayer) {
/* 567 */     List<Piglin> nearbyPiglins = player.level().getEntitiesOfClass(Piglin.class, player.getBoundingBox().inflate(16.0D));
/* 568 */     nearbyPiglins.stream()
/* 569 */       .filter(PiglinAi::isIdle)
/* 570 */       .filter(piglin -> (!onlyIfTheySeeThePlayer || BehaviorUtils.canSee(piglin, player)))
/* 571 */       .forEach(piglin -> {
/* 572 */           if (((Boolean)level.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue()) {
/* 573 */             setAngerTargetToNearestTargetablePlayerIfFound(level, piglin, player);
/*     */           } else {
/* 575 */             setAngerTarget(level, piglin, player);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static InteractionResult mobInteract(ServerLevel level, Piglin body, Player player, InteractionHand hand) {
/* 581 */     ItemStack playerHeldItemStack = player.getItemInHand(hand);
/* 582 */     if (canAdmire(body, playerHeldItemStack)) {
/* 583 */       ItemStack taken = playerHeldItemStack.consumeAndReturn(1, player);
/* 584 */       holdInOffhand(level, body, taken);
/* 585 */       admireGoldItem(body);
/* 586 */       stopWalking(body);
/* 587 */       return InteractionResult.SUCCESS;
/*     */     } 
/* 589 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/* 593 */   protected static boolean canAdmire(Piglin body, ItemStack playerHeldItemStack) { return (!isAdmiringDisabled(body) && !isAdmiringItem(body) && body.isAdult() && isBarterCurrency(playerHeldItemStack)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel level, Piglin body, LivingEntity attacker) {
/* 598 */     if (attacker instanceof Piglin) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 603 */     if (isHoldingItemInOffHand(body)) {
/* 604 */       stopHoldingOffHandItem(level, body, false);
/*     */     }
/* 606 */     Brain<Piglin> brain = body.getBrain();
/* 607 */     brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
/* 608 */     brain.eraseMemory(MemoryModuleType.DANCING);
/* 609 */     brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
/*     */     
/* 611 */     if (attacker instanceof Player)
/*     */     {
/* 613 */       brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, Boolean.valueOf(true), 400L);
/*     */     }
/*     */     
/* 616 */     getAvoidTarget(body).ifPresent(avoidTarget -> {
/* 617 */           if (avoidTarget.getType() != attacker.getType())
/*     */           {
/* 619 */             brain.eraseMemory(MemoryModuleType.AVOID_TARGET);
/*     */           }
/*     */         });
/*     */     
/* 623 */     if (body.isBaby()) {
/*     */       
/* 625 */       brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, attacker, 100L);
/* 626 */       if (Sensor.isEntityAttackableIgnoringLineOfSight(level, body, attacker)) {
/* 627 */         broadcastAngerTarget(level, body, attacker);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 632 */     if (attacker.getType() == EntityType.HOGLIN && hoglinsOutnumberPiglins(body)) {
/*     */       
/* 634 */       setAvoidTargetAndDontHuntForAWhile(body, attacker);
/* 635 */       broadcastRetreat(body, attacker);
/*     */       
/*     */       return;
/*     */     } 
/* 639 */     maybeRetaliate(level, body, attacker);
/*     */   }
/*     */   
/*     */   protected static void maybeRetaliate(ServerLevel level, AbstractPiglin body, LivingEntity attacker) {
/* 643 */     if (body.getBrain().isActive(Activity.AVOID)) {
/*     */       return;
/*     */     }
/* 646 */     if (!Sensor.isEntityAttackableIgnoringLineOfSight(level, body, attacker)) {
/*     */       return;
/*     */     }
/* 649 */     if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(body, attacker, 4.0D)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 655 */     if (attacker.getType() == EntityType.PLAYER && ((Boolean)level.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue()) {
/*     */ 
/*     */       
/* 658 */       setAngerTargetToNearestTargetablePlayerIfFound(level, body, attacker);
/* 659 */       broadcastUniversalAnger(level, body);
/*     */     } else {
/* 661 */       setAngerTarget(level, body, attacker);
/* 662 */       broadcastAngerTarget(level, body, attacker);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 667 */   public static Optional<SoundEvent> getSoundForCurrentActivity(Piglin body) { return body.getBrain().getActiveNonCoreActivity().map(activity -> getSoundForActivity(body, activity)); }
/*     */ 
/*     */   
/*     */   private static SoundEvent getSoundForActivity(Piglin body, Activity activity) {
/* 671 */     if (activity == Activity.FIGHT)
/* 672 */       return SoundEvents.PIGLIN_ANGRY; 
/* 673 */     if (body.isConverting())
/* 674 */       return SoundEvents.PIGLIN_RETREAT; 
/* 675 */     if (activity == Activity.AVOID && isNearAvoidTarget(body))
/* 676 */       return SoundEvents.PIGLIN_RETREAT; 
/* 677 */     if (activity == Activity.ADMIRE_ITEM)
/* 678 */       return SoundEvents.PIGLIN_ADMIRING_ITEM; 
/* 679 */     if (activity == Activity.CELEBRATE)
/* 680 */       return SoundEvents.PIGLIN_CELEBRATE; 
/* 681 */     if (seesPlayerHoldingLovedItem(body))
/* 682 */       return SoundEvents.PIGLIN_JEALOUS; 
/* 683 */     if (isNearRepellent(body)) {
/* 684 */       return SoundEvents.PIGLIN_RETREAT;
/*     */     }
/* 686 */     return SoundEvents.PIGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isNearAvoidTarget(Piglin body) {
/* 691 */     Brain<Piglin> brain = body.getBrain();
/* 692 */     if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 693 */       return false;
/*     */     }
/* 695 */     return ((LivingEntity)brain.getMemory(MemoryModuleType.AVOID_TARGET).get()).closerThan(body, 12.0D);
/*     */   }
/*     */ 
/*     */   
/* 699 */   protected static List<AbstractPiglin> getVisibleAdultPiglins(Piglin body) { return (List)body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of()); }
/*     */ 
/*     */ 
/*     */   
/* 703 */   private static List<AbstractPiglin> getAdultPiglins(AbstractPiglin body) { return (List)body.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of()); }
/*     */ 
/*     */   
/*     */   public static boolean isWearingSafeArmor(LivingEntity livingEntity) {
/* 707 */     for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
/* 708 */       if (livingEntity.getItemBySlot(slot).is(ItemTags.PIGLIN_SAFE_ARMOR)) {
/* 709 */         return true;
/*     */       }
/*     */     } 
/* 712 */     return false;
/*     */   }
/*     */   
/*     */   private static void stopWalking(Piglin body) {
/* 716 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 717 */     body.getNavigation().stop();
/*     */   }
/*     */   
/*     */   private static BehaviorControl<LivingEntity> babySometimesRideBabyHoglin() {
/* 721 */     ticker = new SetEntityLookTargetSometimes.Ticker(RIDE_START_INTERVAL);
/* 722 */     return CopyMemoryWithExpiry.create(e -> (e.isBaby() && ticker.tickDownAndCheck((e.level()).random)), MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.RIDE_TARGET, RIDE_DURATION);
/*     */   }
/*     */   
/*     */   protected static void broadcastAngerTarget(ServerLevel level, AbstractPiglin body, LivingEntity target) {
/* 726 */     getAdultPiglins(body).forEach(piglin -> {
/* 727 */           if (target.getType() == EntityType.HOGLIN && (!piglin.canHunt() || !((Hoglin)target).canBeHunted())) {
/*     */             return;
/*     */           }
/* 730 */           setAngerTargetIfCloserThanCurrent(level, piglin, target);
/*     */         });
/*     */   }
/*     */   
/*     */   protected static void broadcastUniversalAnger(ServerLevel level, AbstractPiglin body) {
/* 735 */     getAdultPiglins(body).forEach(piglin -> getNearestVisibleTargetablePlayer(piglin).ifPresent(()));
/*     */   }
/*     */   
/*     */   protected static void setAngerTarget(ServerLevel level, AbstractPiglin body, LivingEntity target) {
/* 739 */     if (!Sensor.isEntityAttackableIgnoringLineOfSight(level, body, target)) {
/*     */       return;
/*     */     }
/*     */     
/* 743 */     body.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 744 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600L);
/* 745 */     if (target.getType() == EntityType.HOGLIN && body.canHunt()) {
/* 746 */       dontKillAnyMoreHoglinsForAWhile(body);
/*     */     }
/* 748 */     if (target.getType() == EntityType.PLAYER && ((Boolean)level.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue()) {
/* 749 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, Boolean.valueOf(true), 600L);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setAngerTargetToNearestTargetablePlayerIfFound(ServerLevel level, AbstractPiglin body, LivingEntity targetIfNoPlayerFound) {
/* 754 */     Optional<Player> nearestPlayer = getNearestVisibleTargetablePlayer(body);
/* 755 */     if (nearestPlayer.isPresent()) {
/* 756 */       setAngerTarget(level, body, (LivingEntity)nearestPlayer.get());
/*     */     } else {
/* 758 */       setAngerTarget(level, body, targetIfNoPlayerFound);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setAngerTargetIfCloserThanCurrent(ServerLevel level, AbstractPiglin body, LivingEntity newTarget) {
/* 763 */     Optional<LivingEntity> currentTarget = getAngerTarget(body);
/* 764 */     LivingEntity nearest = BehaviorUtils.getNearestTarget(body, currentTarget, newTarget);
/* 765 */     if (currentTarget.isPresent() && currentTarget.get() == nearest) {
/*     */       return;
/*     */     }
/* 768 */     setAngerTarget(level, body, nearest);
/*     */   }
/*     */ 
/*     */   
/* 772 */   private static Optional<LivingEntity> getAngerTarget(AbstractPiglin body) { return BehaviorUtils.getLivingEntityFromUUIDMemory(body, MemoryModuleType.ANGRY_AT); }
/*     */ 
/*     */   
/*     */   public static Optional<LivingEntity> getAvoidTarget(Piglin body) {
/* 776 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 777 */       return body.getBrain().getMemory(MemoryModuleType.AVOID_TARGET);
/*     */     }
/* 779 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static Optional<Player> getNearestVisibleTargetablePlayer(AbstractPiglin body) {
/* 783 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)) {
/* 784 */       return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/*     */     }
/* 786 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/* 790 */   private static void broadcastRetreat(Piglin body, LivingEntity target) { getVisibleAdultPiglins(body).stream()
/* 791 */       .filter(abstractPiglin -> abstractPiglin instanceof Piglin)
/* 792 */       .forEach(piglin -> retreatFromNearestTarget((Piglin)piglin, target)); }
/*     */ 
/*     */   
/*     */   private static void retreatFromNearestTarget(Piglin body, LivingEntity newAvoidTarget) {
/* 796 */     Brain<Piglin> brain = body.getBrain();
/* 797 */     LivingEntity nearest = newAvoidTarget;
/* 798 */     nearest = BehaviorUtils.getNearestTarget(body, brain.getMemory(MemoryModuleType.AVOID_TARGET), nearest);
/* 799 */     nearest = BehaviorUtils.getNearestTarget(body, brain.getMemory(MemoryModuleType.ATTACK_TARGET), nearest);
/* 800 */     setAvoidTargetAndDontHuntForAWhile(body, nearest);
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopFleeing(Piglin body) {
/* 804 */     Brain<Piglin> brain = body.getBrain();
/* 805 */     if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 806 */       return true;
/*     */     }
/* 808 */     LivingEntity entity = (LivingEntity)brain.getMemory(MemoryModuleType.AVOID_TARGET).get();
/* 809 */     EntityType<?> avoidedEntityType = entity.getType();
/*     */     
/* 811 */     if (avoidedEntityType == EntityType.HOGLIN) {
/* 812 */       return piglinsEqualOrOutnumberHoglins(body);
/*     */     }
/* 814 */     if (isZombified(avoidedEntityType)) {
/* 815 */       return !brain.isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, entity);
/*     */     }
/* 817 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 821 */   private static boolean piglinsEqualOrOutnumberHoglins(Piglin body) { return !hoglinsOutnumberPiglins(body); }
/*     */ 
/*     */   
/*     */   private static boolean hoglinsOutnumberPiglins(Piglin body) {
/* 825 */     int piglinCount = ((Integer)body.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(Integer.valueOf(0))).intValue() + 1;
/* 826 */     int hoglinCount = ((Integer)body.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(Integer.valueOf(0))).intValue();
/* 827 */     return (hoglinCount > piglinCount);
/*     */   }
/*     */   
/*     */   private static void setAvoidTargetAndDontHuntForAWhile(Piglin body, LivingEntity target) {
/* 831 */     body.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
/* 832 */     body.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 833 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 834 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, RETREAT_DURATION.sample((body.level()).random));
/* 835 */     dontKillAnyMoreHoglinsForAWhile(body);
/*     */   }
/*     */ 
/*     */   
/* 839 */   protected static void dontKillAnyMoreHoglinsForAWhile(AbstractPiglin body) { body.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, Boolean.valueOf(true), TIME_BETWEEN_HUNTS.sample((body.level()).random)); }
/*     */ 
/*     */ 
/*     */   
/* 843 */   private static void eat(Piglin body) { body.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, Boolean.valueOf(true), 200L); }
/*     */ 
/*     */   
/*     */   private static Vec3 getRandomNearbyPos(Piglin body) {
/* 847 */     Vec3 targetVec = LandRandomPos.getPos(body, 4, 2);
/* 848 */     return (targetVec == null) ? body.position() : targetVec;
/*     */   }
/*     */ 
/*     */   
/* 852 */   private static boolean hasEatenRecently(Piglin body) { return body.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY); }
/*     */ 
/*     */ 
/*     */   
/* 856 */   protected static boolean isIdle(AbstractPiglin body) { return body.getBrain().isActive(Activity.IDLE); }
/*     */ 
/*     */ 
/*     */   
/* 860 */   private static boolean hasCrossbow(LivingEntity body) { return body.isHolding(Items.CROSSBOW); }
/*     */ 
/*     */ 
/*     */   
/* 864 */   private static void admireGoldItem(LivingEntity body) { body.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, Boolean.valueOf(true), 119L); }
/*     */ 
/*     */ 
/*     */   
/* 868 */   private static boolean isAdmiringItem(Piglin body) { return body.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM); }
/*     */ 
/*     */ 
/*     */   
/* 872 */   private static boolean isBarterCurrency(ItemStack itemStack) { return itemStack.is(BARTERING_ITEM); }
/*     */ 
/*     */ 
/*     */   
/* 876 */   private static boolean isFood(ItemStack itemStack) { return itemStack.is(ItemTags.PIGLIN_FOOD); }
/*     */ 
/*     */ 
/*     */   
/* 880 */   private static boolean isNearRepellent(Piglin body) { return body.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT); }
/*     */ 
/*     */ 
/*     */   
/* 884 */   private static boolean seesPlayerHoldingLovedItem(LivingEntity body) { return body.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM); }
/*     */ 
/*     */ 
/*     */   
/* 888 */   private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity body) { return !seesPlayerHoldingLovedItem(body); }
/*     */ 
/*     */ 
/*     */   
/* 892 */   public static boolean isPlayerHoldingLovedItem(LivingEntity entity) { return (entity.getType() == EntityType.PLAYER && entity.isHolding(PiglinAi::isLovedItem)); }
/*     */ 
/*     */ 
/*     */   
/* 896 */   private static boolean isAdmiringDisabled(Piglin body) { return body.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED); }
/*     */ 
/*     */ 
/*     */   
/* 900 */   private static boolean wasHurtRecently(LivingEntity body) { return body.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY); }
/*     */ 
/*     */ 
/*     */   
/* 904 */   private static boolean isHoldingItemInOffHand(Piglin body) { return !body.getOffhandItem().isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 908 */   private static boolean isNotHoldingLovedItemInOffHand(Piglin body) { return (body.getOffhandItem().isEmpty() || !isLovedItem(body.getOffhandItem())); }
/*     */ 
/*     */ 
/*     */   
/* 912 */   public static boolean isZombified(EntityType<?> type) { return (type == EntityType.ZOMBIFIED_PIGLIN || type == EntityType.ZOGLIN); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\PiglinAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */