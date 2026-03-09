/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetWalkTargetFromAttackTargetIfTargetOutOfReach
/*    */ {
/*    */   private static final int PROJECTILE_ATTACK_RANGE_BUFFER = 1;
/*    */   
/* 22 */   public static BehaviorControl<Mob> create(float speedModifier) { return create(mob -> Float.valueOf(speedModifier)); }
/*    */ 
/*    */   
/*    */   public static BehaviorControl<Mob> create(Function<LivingEntity, Float> speedModifier) {
/* 26 */     return BehaviorBuilder.create(i -> i.group(i
/* 27 */           .registered(MemoryModuleType.WALK_TARGET), i
/* 28 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 29 */           .present(MemoryModuleType.ATTACK_TARGET), i
/* 30 */           .registered(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
/* 31 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromAttackTargetIfTargetOutOfReach.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */