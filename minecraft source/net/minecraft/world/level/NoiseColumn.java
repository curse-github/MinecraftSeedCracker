/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.BlockColumn;
/*    */ 
/*    */ public final class NoiseColumn implements BlockColumn {
/*    */   private final int minY;
/*    */   private final BlockState[] column;
/*    */   
/*    */   public NoiseColumn(int minY, BlockState[] column) {
/* 12 */     this.minY = minY;
/* 13 */     this.column = column;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlock(int blockY) {
/* 18 */     int yIndex = blockY - this.minY;
/* 19 */     if (yIndex < 0 || yIndex >= this.column.length) {
/* 20 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 22 */     return this.column[yIndex];
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBlock(int blockY, BlockState state) {
/* 27 */     int yIndex = blockY - this.minY;
/* 28 */     if (yIndex < 0 || yIndex >= this.column.length) {
/* 29 */       throw new IllegalArgumentException("Outside of column height: " + blockY);
/*    */     }
/* 31 */     this.column[yIndex] = state;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\NoiseColumn.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */