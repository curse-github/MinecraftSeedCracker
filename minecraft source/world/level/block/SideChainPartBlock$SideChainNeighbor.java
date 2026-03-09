/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SideChainNeighbor
/*     */   extends Record
/*     */   implements SideChainPartBlock.Neighbor
/*     */ {
/*     */   private final LevelAccessor level;
/*     */   private final SideChainPartBlock block;
/*     */   private final BlockPos pos;
/*     */   private final SideChainPart part;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 182 */   public SideChainNeighbor(LevelAccessor level, SideChainPartBlock block, BlockPos pos, SideChainPart part) { this.level = level; this.block = block; this.pos = pos; this.part = part; } public LevelAccessor level() { return this.level; } public SideChainPartBlock block() { return this.block; } public BlockPos pos() { return this.pos; } public SideChainPart part() { return this.part; }
/*     */ 
/*     */   
/* 185 */   public boolean isConnectable() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   public boolean isUnconnectableOrChainEnd() { return this.part.isChainEnd(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   public boolean connectsTowards(SideChainPart endPart) { return this.part.isConnectionTowards(endPart); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public void connectToTheRight() { this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheRight()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   public void connectToTheLeft() { this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheLeft()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   public void disconnectFromRight() { this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheRight()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   public void disconnectFromLeft() { this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheLeft()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SideChainPartBlock$SideChainNeighbor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */