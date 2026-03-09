/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TriggerGate
/*    */ {
/* 15 */   public static <E extends LivingEntity> OneShot<E> triggerOneShuffled(List<Pair<? extends Trigger<? super E>, Integer>> weightedTriggers) { return triggerGate(weightedTriggers, GateBehavior.OrderPolicy.SHUFFLED, GateBehavior.RunningPolicy.RUN_ONE); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E extends LivingEntity> OneShot<E> triggerGate(List<Pair<? extends Trigger<? super E>, Integer>> weightedBehaviors, GateBehavior.OrderPolicy orderPolicy, GateBehavior.RunningPolicy runningPolicy) {
/* 21 */     ShufflingList<Trigger<? super E>> behaviors = new ShufflingList<Trigger<? super E>>();
/* 22 */     weightedBehaviors.forEach(entry -> behaviors.add((Trigger)entry.getFirst(), ((Integer)entry.getSecond()).intValue()));
/*    */     
/* 24 */     return BehaviorBuilder.create(i -> i.point(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TriggerGate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */