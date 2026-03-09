/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class SimpleNeighborUpdate
/*     */   extends Record
/*     */   implements CollectingNeighborUpdater.NeighborUpdates
/*     */ {
/*     */   private final BlockPos pos;
/*     */   private final Block block;
/*     */   private final Orientation orientation;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #108	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 108 */   SimpleNeighborUpdate(BlockPos pos, Block block, Orientation orientation) { this.pos = pos; this.block = block; this.orientation = orientation; } public BlockPos pos() { return this.pos; } public Block block() { return this.block; } public Orientation orientation() { return this.orientation; }
/*     */   
/*     */   public boolean runNext(Level level) {
/* 111 */     BlockState state = level.getBlockState(this.pos);
/* 112 */     NeighborUpdater.executeUpdate(level, state, this.pos, this.block, this.orientation, false);
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public void forEachUpdatedPos(Consumer<BlockPos> output) { output.accept(this.pos); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$SimpleNeighborUpdate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */