/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ 
/*    */ public class StartHuntingHoglin {
/*    */   public static OneShot<Piglin> create() {
/* 10 */     return BehaviorBuilder.create(i -> i.group(i
/* 11 */           .present(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN), i
/* 12 */           .absent(MemoryModuleType.ANGRY_AT), i
/* 13 */           .absent(MemoryModuleType.HUNTED_RECENTLY), i
/* 14 */           .registered(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS))
/* 15 */         .apply(i, ()));
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
/*    */   
/* 33 */   private static boolean hasHuntedRecently(AbstractPiglin otherPiglin) { return otherPiglin.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StartHuntingHoglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */