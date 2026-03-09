/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class GoToWantedItem {
/* 13 */   public static BehaviorControl<LivingEntity> create(float speedModifier, boolean interruptOngoingWalk, int maxDistToWalk) { return create(body -> true, speedModifier, interruptOngoingWalk, maxDistToWalk); }
/*    */ 
/*    */   
/*    */   public static <E extends LivingEntity> BehaviorControl<E> create(Predicate<E> predicate, float speedModifier, boolean interruptOngoingWalk, int maxDistToWalk) {
/* 17 */     return BehaviorBuilder.create(i -> {
/* 18 */           BehaviorBuilder<E, ? extends MemoryAccessor<? extends K1, WalkTarget>> walkCondition = interruptOngoingWalk ? i.registered(MemoryModuleType.WALK_TARGET) : i.absent(MemoryModuleType.WALK_TARGET);
/* 19 */           return i.group(i
/* 20 */               .registered(MemoryModuleType.LOOK_TARGET), walkCondition, i
/*    */               
/* 22 */               .present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), i
/* 23 */               .registered(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS))
/* 24 */             .apply(i, ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoToWantedItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */