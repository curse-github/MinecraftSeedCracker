/*     */ package net.minecraft.world.entity.animal.allay;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EntityTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.GoAndGiveItemsToTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StayCloseToTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ 
/*     */ public class AllayAi
/*     */ {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_DEPOSIT_TARGET = 2.25F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETRIEVING_ITEM = 1.75F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.5F;
/*     */   private static final int CLOSE_ENOUGH_TO_TARGET = 4;
/*     */   private static final int TOO_FAR_FROM_TARGET = 16;
/*     */   private static final int MAX_LOOK_DISTANCE = 6;
/*     */   private static final int MIN_WAIT_DURATION = 30;
/*     */   private static final int MAX_WAIT_DURATION = 60;
/*     */   private static final int TIME_TO_FORGET_NOTEBLOCK = 600;
/*     */   private static final int DISTANCE_TO_WANTED_ITEM = 32;
/*     */   private static final int GIVE_ITEM_TIMEOUT_DURATION = 20;
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Allay> brain) {
/*  55 */     initCoreActivity(brain);
/*  56 */     initIdleActivity(brain);
/*     */     
/*  58 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/*  59 */     brain.setDefaultActivity(Activity.IDLE);
/*  60 */     brain.useDefaultActivity();
/*  61 */     return brain;
/*     */   }
/*     */ 
/*     */   
/*  65 */   private static void initCoreActivity(Brain<Allay> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.5F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS))); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Allay> brain) {
/*  76 */     brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
/*  77 */           Pair.of(Integer.valueOf(0), GoToWantedItem.create(mob -> true, 1.75F, true, 32)), 
/*  78 */           Pair.of(Integer.valueOf(1), new GoAndGiveItemsToTarget(AllayAi::getItemDepositPosition, 2.25F, 20)), 
/*  79 */           Pair.of(Integer.valueOf(2), StayCloseToTarget.create(AllayAi::getItemDepositPosition, Predicate.not(AllayAi::hasWantedItem), 4, 16, 2.25F)), 
/*  80 */           Pair.of(Integer.valueOf(3), SetEntityLookTargetSometimes.create(6.0F, UniformInt.of(30, 60))), 
/*  81 */           Pair.of(Integer.valueOf(4), new RunOne(ImmutableList.of(
/*  82 */                 Pair.of(RandomStroll.fly(1.0F), Integer.valueOf(2)), 
/*  83 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(2)), 
/*  84 */                 Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))))), 
/*     */         
/*  86 */         ImmutableSet.of());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public static void updateActivity(Allay body) { body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void hearNoteblock(LivingEntity allay, BlockPos pos) {
/*  97 */     Brain<?> brain = allay.getBrain();
/*  98 */     GlobalPos globalPos = GlobalPos.of(allay.level().dimension(), pos);
/*  99 */     Optional<GlobalPos> likedNoteblockPos = brain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 100 */     if (likedNoteblockPos.isEmpty()) {
/* 101 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION, globalPos);
/* 102 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, Integer.valueOf(600));
/* 103 */     } else if (((GlobalPos)likedNoteblockPos.get()).equals(globalPos)) {
/* 104 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, Integer.valueOf(600));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<PositionTracker> getItemDepositPosition(LivingEntity allay) {
/* 109 */     Brain<?> brain = allay.getBrain();
/*     */     
/* 111 */     Optional<GlobalPos> likedNoteblockPos = brain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 112 */     if (likedNoteblockPos.isPresent()) {
/* 113 */       GlobalPos position = (GlobalPos)likedNoteblockPos.get();
/* 114 */       if (shouldDepositItemsAtLikedNoteblock(allay, brain, position)) {
/* 115 */         return Optional.of(new BlockPosTracker(position.pos().above()));
/*     */       }
/* 117 */       brain.eraseMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/*     */     } 
/*     */     
/* 120 */     return getLikedPlayerPositionTracker(allay);
/*     */   }
/*     */   
/*     */   private static boolean hasWantedItem(LivingEntity allay) {
/* 124 */     Brain<?> brain = allay.getBrain();
/* 125 */     return brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean shouldDepositItemsAtLikedNoteblock(LivingEntity allay, Brain<?> brain, GlobalPos likedNoteblockPos) {
/* 129 */     Optional<Integer> likedNoteblockCooldown = brain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS);
/* 130 */     Level level = allay.level();
/* 131 */     return (likedNoteblockPos.isCloseEnough(level.dimension(), allay.blockPosition(), 1024) && level
/* 132 */       .getBlockState(likedNoteblockPos.pos()).is(Blocks.NOTE_BLOCK) && likedNoteblockCooldown
/* 133 */       .isPresent());
/*     */   }
/*     */ 
/*     */   
/* 137 */   private static Optional<PositionTracker> getLikedPlayerPositionTracker(LivingEntity allay) { return getLikedPlayer(allay).map(serverPlayer -> new EntityTracker(serverPlayer, true)); }
/*     */ 
/*     */   
/*     */   public static Optional<ServerPlayer> getLikedPlayer(LivingEntity allay) {
/* 141 */     Level level = allay.level();
/* 142 */     if (!level.isClientSide() && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 143 */       Optional<UUID> likedPlayer = allay.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
/* 144 */       if (likedPlayer.isPresent()) {
/* 145 */         Entity entity = serverLevel.getEntity((UUID)likedPlayer.get());
/*     */         
/* 147 */         if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity; if ((serverPlayer.gameMode
/* 148 */             .isSurvival() || serverPlayer.gameMode.isCreative()) && serverPlayer
/* 149 */             .closerThan(allay, 64.0D))
/*     */           {
/* 151 */             return Optional.of(serverPlayer); }  }
/*     */         
/* 153 */         return Optional.empty();
/*     */       }  }
/*     */ 
/*     */     
/* 157 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\allay\AllayAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */