/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class GoToTargetLocation {
/*    */   private static BlockPos getNearbyPos(Mob body, BlockPos pos) {
/* 11 */     RandomSource random = (body.level()).random;
/* 12 */     return pos.offset(getRandomOffset(random), 0, getRandomOffset(random));
/*    */   }
/*    */ 
/*    */   
/* 16 */   private static int getRandomOffset(RandomSource random) { return random.nextInt(3) - 1; }
/*    */ 
/*    */   
/*    */   public static <E extends Mob> OneShot<E> create(MemoryModuleType<BlockPos> locationMemory, int closeEnoughDist, float speedModifier) {
/* 20 */     return BehaviorBuilder.create(i -> i.group(i
/* 21 */           .present(locationMemory), i
/* 22 */           .absent(MemoryModuleType.ATTACK_TARGET), i
/* 23 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 24 */           .registered(MemoryModuleType.LOOK_TARGET))
/* 25 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoToTargetLocation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */