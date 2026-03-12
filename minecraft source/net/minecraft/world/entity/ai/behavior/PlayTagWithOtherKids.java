/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayTagWithOtherKids
/*     */ {
/*     */   private static final int MAX_FLEE_XZ_DIST = 20;
/*     */   private static final int MAX_FLEE_Y_DIST = 8;
/*     */   private static final float FLEE_SPEED_MODIFIER = 0.6F;
/*     */   private static final float CHASE_SPEED_MODIFIER = 0.6F;
/*     */   private static final int MAX_CHASERS_PER_TARGET = 5;
/*     */   private static final int AVERAGE_WAIT_TIME_BETWEEN_RUNS = 10;
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create() {
/*  34 */     return BehaviorBuilder.create(i -> i.group(i
/*  35 */           .present(MemoryModuleType.VISIBLE_VILLAGER_BABIES), i
/*  36 */           .absent(MemoryModuleType.WALK_TARGET), i
/*  37 */           .registered(MemoryModuleType.LOOK_TARGET), i
/*  38 */           .registered(MemoryModuleType.INTERACTION_TARGET))
/*  39 */         .apply(i, ()));
/*     */   }
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
/*     */ 
/*     */   
/*     */   private static void chaseKid(MemoryAccessor<?, LivingEntity> interactionTarget, MemoryAccessor<?, PositionTracker> lookTarget, MemoryAccessor<?, WalkTarget> walkTarget, LivingEntity kidToChase) {
/*  76 */     interactionTarget.set(kidToChase);
/*  77 */     lookTarget.set(new EntityTracker(kidToChase, true));
/*  78 */     walkTarget.set(new WalkTarget(new EntityTracker(kidToChase, false), 0.6F, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Optional<LivingEntity> findSomeoneBeingChased(List<LivingEntity> friendsNearby) {
/*  83 */     Map<LivingEntity, Integer> chasedKids = checkHowManyChasersEachFriendHas(friendsNearby);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     return chasedKids.entrySet().stream()
/*  89 */       .sorted(Comparator.comparingInt(Map.Entry::getValue))
/*  90 */       .filter(entry -> (((Integer)entry.getValue()).intValue() > 0 && ((Integer)entry.getValue()).intValue() <= 5))
/*  91 */       .map(Map.Entry::getKey)
/*  92 */       .findFirst();
/*     */   }
/*     */   
/*     */   private static Map<LivingEntity, Integer> checkHowManyChasersEachFriendHas(List<LivingEntity> friendsNearby) {
/*  96 */     Map<LivingEntity, Integer> chasedKids = Maps.newHashMap();
/*     */     
/*  98 */     friendsNearby.stream()
/*  99 */       .filter(PlayTagWithOtherKids::isChasingSomeone)
/* 100 */       .forEach(chaser -> 
/* 101 */         chasedKids.compute(whoAreYouChasing(chaser), ()));
/*     */ 
/*     */     
/* 104 */     return chasedKids;
/*     */   }
/*     */ 
/*     */   
/* 108 */   private static LivingEntity whoAreYouChasing(LivingEntity friend) { return (LivingEntity)friend.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get(); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   private static boolean isChasingSomeone(LivingEntity friend) { return friend.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent(); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   private static boolean isFriendChasingMe(LivingEntity me, LivingEntity friend) { return friend.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET)
/* 117 */       .filter(mob -> (mob == me))
/* 118 */       .isPresent(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\PlayTagWithOtherKids.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */