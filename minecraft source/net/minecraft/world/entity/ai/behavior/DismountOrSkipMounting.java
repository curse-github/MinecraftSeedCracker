/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.function.BiPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class DismountOrSkipMounting
/*    */ {
/*    */   public static <E extends LivingEntity> BehaviorControl<E> create(int maxWalkDistToRideTarget, BiPredicate<E, Entity> dontRideIf) {
/* 16 */     return BehaviorBuilder.create(i -> i.group(i
/* 17 */           .registered(MemoryModuleType.RIDE_TARGET))
/* 18 */         .apply(i, ()));
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isVehicleValid(LivingEntity body, Entity vehicle, int maxWalkDistToRideTarget) {
/* 36 */     return (vehicle.isAlive() && vehicle
/* 37 */       .closerThan(body, maxWalkDistToRideTarget) && vehicle
/* 38 */       .level() == body.level());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\DismountOrSkipMounting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */