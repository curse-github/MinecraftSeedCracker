/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class EraseMemoryIf {
/*    */   public static <E extends LivingEntity> BehaviorControl<E> create(Predicate<E> predicate, MemoryModuleType<?> memoryType) {
/* 11 */     return BehaviorBuilder.create(i -> i.group(i
/* 12 */           .present(memoryType))
/* 13 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\EraseMemoryIf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */