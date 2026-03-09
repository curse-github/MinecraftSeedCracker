/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultRedstoneWireEvaluator
/*    */   extends RedstoneWireEvaluator
/*    */ {
/* 16 */   public DefaultRedstoneWireEvaluator(RedStoneWireBlock wireBlock) { super(wireBlock); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void updatePowerStrength(Level level, BlockPos pos, BlockState state, Orientation orientation, boolean skipShapeUpdates) {
/* 21 */     int targetStrength = calculateTargetStrength(level, pos);
/*    */     
/* 23 */     if (((Integer)state.getValue(RedStoneWireBlock.POWER)).intValue() != targetStrength) {
/* 24 */       if (level.getBlockState(pos) == state) {
/* 25 */         level.setBlock(pos, (BlockState)state.setValue(RedStoneWireBlock.POWER, Integer.valueOf(targetStrength)), 2);
/*    */       }
/*    */ 
/*    */       
/* 29 */       Set<BlockPos> toUpdate = Sets.newHashSet();
/* 30 */       toUpdate.add(pos);
/* 31 */       for (Direction direction : Direction.values()) {
/* 32 */         toUpdate.add(pos.relative(direction));
/*    */       }
/* 34 */       for (BlockPos blockPos : toUpdate) {
/* 35 */         level.updateNeighborsAt(blockPos, this.wireBlock);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private int calculateTargetStrength(Level level, BlockPos pos) {
/* 41 */     int blockSignal = getBlockSignal(level, pos);
/* 42 */     if (blockSignal == 15) {
/* 43 */       return blockSignal;
/*    */     }
/*    */     
/* 46 */     return Math.max(blockSignal, getIncomingWireSignal(level, pos));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\DefaultRedstoneWireEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */