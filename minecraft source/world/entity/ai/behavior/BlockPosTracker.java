/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockPosTracker implements PositionTracker {
/*    */   private final BlockPos blockPos;
/*    */   private final Vec3 centerPosition;
/*    */   
/*    */   public BlockPosTracker(BlockPos blockPos) {
/* 12 */     this.blockPos = blockPos.immutable();
/* 13 */     this.centerPosition = Vec3.atCenterOf(blockPos);
/*    */   }
/*    */   
/*    */   public BlockPosTracker(Vec3 vec) {
/* 17 */     this.blockPos = BlockPos.containing(vec);
/* 18 */     this.centerPosition = vec;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public Vec3 currentPosition() { return this.centerPosition; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public BlockPos currentBlockPosition() { return this.blockPos; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 33 */   public boolean isVisibleBy(LivingEntity body) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public String toString() { return "BlockPosTracker{blockPos=" + String.valueOf(this.blockPos) + ", centerPosition=" + String.valueOf(this.centerPosition) + "}"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BlockPosTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */