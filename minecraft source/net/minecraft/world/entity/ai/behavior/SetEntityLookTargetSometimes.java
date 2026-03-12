/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ @Deprecated
/*    */ public class SetEntityLookTargetSometimes {
/* 20 */   public static BehaviorControl<LivingEntity> create(float maxDist, UniformInt interval) { return create(maxDist, interval, mob -> true); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static BehaviorControl<LivingEntity> create(EntityType<?> type, float maxDist, UniformInt interval) { return create(maxDist, interval, mob -> type.equals(mob.getType())); }
/*    */ 
/*    */   
/*    */   private static BehaviorControl<LivingEntity> create(float maxDist, UniformInt interval, Predicate<LivingEntity> predicate) {
/* 28 */     float maxDistSqr = maxDist * maxDist;
/*    */     
/* 30 */     Ticker ticker = new Ticker(interval);
/*    */     
/* 32 */     return BehaviorBuilder.create(i -> i.group(i
/* 33 */           .absent(MemoryModuleType.LOOK_TARGET), i
/* 34 */           .present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
/* 35 */         .apply(i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class Ticker
/*    */   {
/*    */     private final UniformInt interval;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private int ticksUntilNextStart;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Ticker(UniformInt interval) {
/* 56 */       if (interval.getMinValue() <= 1) {
/* 57 */         throw new IllegalArgumentException();
/*    */       }
/* 59 */       this.interval = interval;
/*    */     }
/*    */     
/*    */     public boolean tickDownAndCheck(RandomSource random) {
/* 63 */       if (this.ticksUntilNextStart == 0) {
/* 64 */         this.ticksUntilNextStart = this.interval.sample(random) - 1;
/* 65 */         return false;
/*    */       } 
/*    */       
/* 68 */       return (--this.ticksUntilNextStart == 0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTargetSometimes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */