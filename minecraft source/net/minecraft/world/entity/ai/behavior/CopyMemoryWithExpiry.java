/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class CopyMemoryWithExpiry {
/*    */   public static <E extends LivingEntity, T> BehaviorControl<E> create(Predicate<E> copyIfTrue, MemoryModuleType<? extends T> sourceMemory, MemoryModuleType<T> targetMemory, UniformInt durationOfCopy) {
/* 12 */     return BehaviorBuilder.create(i -> i.group(i
/* 13 */           .present(sourceMemory), i
/* 14 */           .absent(targetMemory))
/* 15 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\CopyMemoryWithExpiry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */