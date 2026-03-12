/*    */ package net.minecraft.world.entity.animal.frog;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*    */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*    */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*    */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*    */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*    */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*    */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*    */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class TadpoleAi
/*    */ {
/*    */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*    */   
/*    */   protected static Brain<?> makeBrain(Brain<Tadpole> brain) {
/* 31 */     initCoreActivity(brain);
/* 32 */     initIdleActivity(brain);
/*    */     
/* 34 */     brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
/* 35 */     brain.setDefaultActivity(Activity.IDLE);
/* 36 */     brain.useDefaultActivity();
/* 37 */     return brain;
/*    */   }
/*    */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F; private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*    */   
/* 41 */   private static void initCoreActivity(Brain<Tadpole> brain) { brain.addActivity(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS))); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void initIdleActivity(Brain<Tadpole> brain) {
/* 50 */     brain.addActivity(Activity.IDLE, ImmutableList.of(
/* 51 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 52 */           Pair.of(Integer.valueOf(1), new FollowTemptation(s -> Float.valueOf(1.25F))), 
/* 53 */           Pair.of(Integer.valueOf(2), new GateBehavior(
/* 54 */               ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*    */ 
/*    */               
/* 57 */               ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*    */ 
/*    */               
/* 60 */               ImmutableList.of(
/* 61 */                 Pair.of(RandomStroll.swim(0.5F), Integer.valueOf(2)), 
/* 62 */                 Pair.of(SetWalkTargetFromLookTarget.create(0.5F, 3), Integer.valueOf(3)), 
/* 63 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), Integer.valueOf(5)))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public static void updateActivity(Tadpole body) { body.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\frog\TadpoleAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */