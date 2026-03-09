/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*    */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RandomStroll
/*    */ {
/*    */   private static final int MAX_XZ_DIST = 10;
/*    */   private static final int MAX_Y_DIST = 7;
/* 28 */   private static final int[][] SWIM_XY_DISTANCE_TIERS = { { 1, 1 }, { 3, 3 }, { 5, 5 }, { 6, 5 }, { 7, 7 }, { 10, 7 } };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static OneShot<PathfinderMob> stroll(float speedModifier) { return stroll(speedModifier, true); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static OneShot<PathfinderMob> stroll(float speedModifier, boolean mayStrollFromWater) { return strollFlyOrSwim(speedModifier, body -> LandRandomPos.getPos(body, 10, 7), mayStrollFromWater ? (b -> true) : (b -> !b.isInWater())); }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public static BehaviorControl<PathfinderMob> stroll(float speedModifier, int maxHorizontalDistance, int maxVerticalDistance) { return strollFlyOrSwim(speedModifier, body -> LandRandomPos.getPos(body, maxHorizontalDistance, maxVerticalDistance), b -> true); }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static BehaviorControl<PathfinderMob> fly(float speedModifier) { return strollFlyOrSwim(speedModifier, body -> getTargetFlyPos(body, 10, 7), b -> true); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public static BehaviorControl<PathfinderMob> swim(float speedModifier) { return strollFlyOrSwim(speedModifier, RandomStroll::getTargetSwimPos, Entity::isInWater); }
/*    */ 
/*    */   
/*    */   private static OneShot<PathfinderMob> strollFlyOrSwim(float speedModifier, Function<PathfinderMob, Vec3> fetchTargetPos, Predicate<PathfinderMob> canRun) {
/* 58 */     return BehaviorBuilder.create(i -> i.group(i
/* 59 */           .absent(MemoryModuleType.WALK_TARGET))
/* 60 */         .apply(i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Vec3 getTargetSwimPos(PathfinderMob body) {
/* 72 */     Vec3 fallback = null;
/* 73 */     Vec3 targetPos = null;
/*    */     
/* 75 */     for (int[] distance : SWIM_XY_DISTANCE_TIERS) {
/*    */       
/* 77 */       if (fallback == null) {
/* 78 */         targetPos = BehaviorUtils.getRandomSwimmablePos(body, distance[0], distance[1]);
/*    */       } else {
/* 80 */         targetPos = body.position().add(body.position().vectorTo(fallback).normalize().multiply(distance[0], distance[1], distance[0]));
/*    */       } 
/*    */       
/* 83 */       boolean restrict = GoalUtils.mobRestricted(body, distance[0]);
/* 84 */       if (targetPos == null || body.level().getFluidState(BlockPos.containing(targetPos)).isEmpty() || GoalUtils.isRestricted(restrict, body, targetPos)) {
/* 85 */         return fallback;
/*    */       }
/* 87 */       fallback = targetPos;
/*    */     } 
/*    */ 
/*    */     
/* 91 */     return targetPos;
/*    */   }
/*    */   
/*    */   private static Vec3 getTargetFlyPos(PathfinderMob body, int maxHorizontalDistance, int maxVerticalDistance) {
/* 95 */     Vec3 wanderDirection = body.getViewVector(0.0F);
/*    */     
/* 97 */     return AirAndWaterRandomPos.getPos(body, maxHorizontalDistance, maxVerticalDistance, -2, wanderDirection.x, wanderDirection.z, 1.5707963705062866D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RandomStroll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */