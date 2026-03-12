/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.SideChainPart;
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
/*     */ public final class Neighbors
/*     */   extends Record
/*     */ {
/*     */   private final SideChainPartBlock block;
/*     */   private final LevelAccessor level;
/*     */   private final Direction facing;
/*     */   private final BlockPos center;
/*     */   private final Map<BlockPos, SideChainPartBlock.Neighbor> cache;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 117 */   public Neighbors(SideChainPartBlock block, LevelAccessor level, Direction facing, BlockPos center, Map<BlockPos, SideChainPartBlock.Neighbor> cache) { this.block = block; this.level = level; this.facing = facing; this.center = center; this.cache = cache; } public SideChainPartBlock block() { return this.block; } public LevelAccessor level() { return this.level; } public Direction facing() { return this.facing; } public BlockPos center() { return this.center; } public Map<BlockPos, SideChainPartBlock.Neighbor> cache() { return this.cache; }
/*     */ 
/*     */   
/* 120 */   private boolean isConnectableToThisBlock(BlockState neighbor) { return (this.block.isConnectable(neighbor) && this.block.getFacing(neighbor) == this.facing); }
/*     */ 
/*     */   
/*     */   private SideChainPartBlock.Neighbor createNewNeighbor(BlockPos pos) {
/* 124 */     BlockState neighbor = this.level.getBlockState(pos);
/* 125 */     SideChainPart part = isConnectableToThisBlock(neighbor) ? this.block.getSideChainPart(neighbor) : null;
/* 126 */     return (part == null) ? new SideChainPartBlock.EmptyNeighbor(pos) : new SideChainPartBlock.SideChainNeighbor(this.level, this.block, pos, part);
/*     */   }
/*     */ 
/*     */   
/* 130 */   private SideChainPartBlock.Neighbor getOrCreateNeighbor(Direction dir, Integer steps) { return (SideChainPartBlock.Neighbor)this.cache.computeIfAbsent(this.center.relative(dir, steps.intValue()), this::createNewNeighbor); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public SideChainPartBlock.Neighbor left(int steps) { return getOrCreateNeighbor(this.facing.getClockWise(), Integer.valueOf(steps)); }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public SideChainPartBlock.Neighbor right(int steps) { return getOrCreateNeighbor(this.facing.getCounterClockWise(), Integer.valueOf(steps)); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public SideChainPartBlock.Neighbor left() { return left(1); }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public SideChainPartBlock.Neighbor right() { return right(1); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SideChainPartBlock$Neighbors.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */