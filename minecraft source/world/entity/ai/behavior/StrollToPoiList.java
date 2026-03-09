/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class StrollToPoiList {
/*    */   public static BehaviorControl<Villager> create(MemoryModuleType<List<GlobalPos>> strollToMemoryType, float speedModifier, int closeEnoughDist, int maxDistanceFromPoi, MemoryModuleType<GlobalPos> mustBeCloseToMemoryType) {
/* 17 */     MutableLong nextOkStartTime = new MutableLong(0L);
/*    */     
/* 19 */     return BehaviorBuilder.create(i -> i.group(i
/* 20 */           .registered(MemoryModuleType.WALK_TARGET), i
/* 21 */           .present(strollToMemoryType), i
/* 22 */           .present(mustBeCloseToMemoryType))
/* 23 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StrollToPoiList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */