/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerData;
/*    */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*    */ 
/*    */ public class ResetProfession {
/*    */   public static BehaviorControl<Villager> create() {
/* 15 */     return BehaviorBuilder.create(i -> i.group(i
/* 16 */           .absent(MemoryModuleType.JOB_SITE))
/* 17 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ResetProfession.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */