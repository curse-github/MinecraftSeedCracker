/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class SetEntityLookTarget {
/* 18 */   public static BehaviorControl<LivingEntity> create(MobCategory category, float maxDist) { return create(mob -> category.equals(mob.getType().getCategory()), maxDist); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static OneShot<LivingEntity> create(EntityType<?> type, float maxDist) { return create(mob -> type.equals(mob.getType()), maxDist); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static OneShot<LivingEntity> create(float maxDist) { return create(mob -> true, maxDist); }
/*    */ 
/*    */   
/*    */   public static OneShot<LivingEntity> create(Predicate<LivingEntity> predicate, float maxDist) {
/* 30 */     float maxDistSqr = maxDist * maxDist;
/*    */     
/* 32 */     return BehaviorBuilder.create(i -> i.group(i
/* 33 */           .absent(MemoryModuleType.LOOK_TARGET), i
/* 34 */           .present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
/* 35 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */