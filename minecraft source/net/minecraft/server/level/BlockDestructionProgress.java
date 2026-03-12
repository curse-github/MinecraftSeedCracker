/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class BlockDestructionProgress extends Object implements Comparable<BlockDestructionProgress> {
/*    */   private final int id;
/*    */   private final BlockPos pos;
/*    */   private int progress;
/*    */   private int updatedRenderTick;
/*    */   
/*    */   public BlockDestructionProgress(int id, BlockPos pos) {
/* 12 */     this.id = id;
/* 13 */     this.pos = pos;
/*    */   }
/*    */ 
/*    */   
/* 17 */   public int getId() { return this.id; }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public BlockPos getPos() { return this.pos; }
/*    */ 
/*    */   
/*    */   public void setProgress(int progress) {
/* 25 */     if (progress > 10) {
/* 26 */       progress = 10;
/*    */     }
/* 28 */     this.progress = progress;
/*    */   }
/*    */ 
/*    */   
/* 32 */   public int getProgress() { return this.progress; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void updateTick(int tick) { this.updatedRenderTick = tick; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int getUpdatedRenderTick() { return this.updatedRenderTick; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 45 */     if (this == o) {
/* 46 */       return true;
/*    */     }
/* 48 */     if (o == null || getClass() != o.getClass()) {
/* 49 */       return false;
/*    */     }
/* 51 */     BlockDestructionProgress that = (BlockDestructionProgress)o;
/* 52 */     return (this.id == that.id);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int hashCode() { return Integer.hashCode(this.id); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(BlockDestructionProgress o) {
/* 62 */     if (this.progress != o.progress) {
/* 63 */       return Integer.compare(this.progress, o.progress);
/*    */     }
/* 65 */     return Integer.compare(this.id, o.id);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\BlockDestructionProgress.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */