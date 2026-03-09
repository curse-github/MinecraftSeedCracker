/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StrollAroundPoi
/*    */ {
/*    */   private static final int MIN_TIME_BETWEEN_STROLLS = 180;
/*    */   private static final int STROLL_MAX_XZ_DIST = 8;
/*    */   private static final int STROLL_MAX_Y_DIST = 6;
/*    */   
/*    */   public static OneShot<PathfinderMob> create(MemoryModuleType<GlobalPos> memoryType, float speedModifier, int maxDistanceFromPoi) {
/* 27 */     MutableLong nextOkStartTime = new MutableLong(0L);
/*    */     
/* 29 */     return BehaviorBuilder.create(i -> i.group(i
/* 30 */           .registered(MemoryModuleType.WALK_TARGET), i
/* 31 */           .present(memoryType))
/* 32 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StrollAroundPoi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */