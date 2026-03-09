/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.EntityTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WalkTarget
/*    */ {
/*    */   private final PositionTracker target;
/*    */   private final float speedModifier;
/*    */   private final int closeEnoughDist;
/*    */   
/* 16 */   public WalkTarget(BlockPos target, float speedModifier, int closeEnoughDist) { this(new BlockPosTracker(target), speedModifier, closeEnoughDist); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public WalkTarget(Vec3 target, float speedModifier, int closeEnoughDist) { this(new BlockPosTracker(BlockPos.containing(target)), speedModifier, closeEnoughDist); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public WalkTarget(Entity target, float speedModifier, int closeEnoughDist) { this(new EntityTracker(target, false), speedModifier, closeEnoughDist); }
/*    */ 
/*    */   
/*    */   public WalkTarget(PositionTracker target, float speedModifier, int closeEnoughDist) {
/* 28 */     this.target = target;
/* 29 */     this.speedModifier = speedModifier;
/* 30 */     this.closeEnoughDist = closeEnoughDist;
/*    */   }
/*    */ 
/*    */   
/* 34 */   public PositionTracker getTarget() { return this.target; }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public float getSpeedModifier() { return this.speedModifier; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public int getCloseEnoughDist() { return this.closeEnoughDist; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\memory\WalkTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */