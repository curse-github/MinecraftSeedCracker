/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
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
/*     */ final class ShapeUpdate
/*     */   extends Record
/*     */   implements CollectingNeighborUpdater.NeighborUpdates
/*     */ {
/*     */   private final Direction direction;
/*     */   private final BlockState neighborState;
/*     */   private final BlockPos pos;
/*     */   private final BlockPos neighborPos;
/*     */   @UpdateFlags
/*     */   private final int updateFlags;
/*     */   private final int updateLimit;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #183	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #183	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #183	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 183 */   private ShapeUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, @UpdateFlags int updateFlags, int updateLimit) { this.direction = direction; this.neighborState = neighborState; this.pos = pos; this.neighborPos = neighborPos; this.updateFlags = updateFlags; this.updateLimit = updateLimit; } public Direction direction() { return this.direction; } public BlockState neighborState() { return this.neighborState; } public BlockPos pos() { return this.pos; } public BlockPos neighborPos() { return this.neighborPos; } @UpdateFlags public int updateFlags() { return this.updateFlags; } public int updateLimit() { return this.updateLimit; }
/*     */   
/*     */   public boolean runNext(Level level) {
/* 186 */     NeighborUpdater.executeShapeUpdate(level, this.direction, this.pos, this.neighborPos, this.neighborState, this.updateFlags, this.updateLimit);
/* 187 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public void forEachUpdatedPos(Consumer<BlockPos> output) { output.accept(this.pos); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$ShapeUpdate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */