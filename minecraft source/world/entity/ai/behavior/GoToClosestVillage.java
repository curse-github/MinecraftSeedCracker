/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GoToClosestVillage {
/*    */   public static BehaviorControl<Villager> create(float speedModifier, int closeEnoughDistance) {
/* 15 */     return BehaviorBuilder.create(i -> i.group(i
/* 16 */           .absent(MemoryModuleType.WALK_TARGET))
/* 17 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoToClosestVillage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */