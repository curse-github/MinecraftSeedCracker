/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageBoundRandomStroll
/*    */ {
/*    */   private static final int MAX_XZ_DIST = 10;
/*    */   private static final int MAX_Y_DIST = 7;
/*    */   
/* 27 */   public static OneShot<PathfinderMob> create(float speedModifier) { return create(speedModifier, 10, 7); }
/*    */ 
/*    */   
/*    */   public static OneShot<PathfinderMob> create(float speedModifier, int maxXyDist, int maxYDist) {
/* 31 */     return BehaviorBuilder.create(i -> i.group(i
/* 32 */           .absent(MemoryModuleType.WALK_TARGET))
/* 33 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillageBoundRandomStroll.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */