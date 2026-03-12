/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.SideChainPart;
/*     */ 
/*     */ public interface SideChainPartBlock {
/*     */   SideChainPart getSideChainPart(BlockState paramBlockState);
/*     */   
/*     */   BlockState setSideChainPart(BlockState paramBlockState, SideChainPart paramSideChainPart);
/*     */   
/*     */   Direction getFacing(BlockState paramBlockState);
/*     */   
/*     */   boolean isConnectable(BlockState paramBlockState);
/*     */   
/*     */   int getMaxChainLength();
/*     */   
/*     */   default List<BlockPos> getAllBlocksConnectedTo(LevelAccessor level, BlockPos pos) {
/*  28 */     BlockState state = level.getBlockState(pos);
/*  29 */     if (!isConnectable(state)) {
/*  30 */       return List.of();
/*     */     }
/*     */     
/*  33 */     Neighbors neighbors = getNeighbors(level, pos, getFacing(state));
/*  34 */     List<BlockPos> results = new LinkedList<BlockPos>();
/*  35 */     results.add(pos);
/*     */     
/*  37 */     Objects.requireNonNull(neighbors); Objects.requireNonNull(results); addBlocksConnectingTowards(neighbors::left, SideChainPart.LEFT, results::addFirst);
/*  38 */     Objects.requireNonNull(neighbors); Objects.requireNonNull(results); addBlocksConnectingTowards(neighbors::right, SideChainPart.RIGHT, results::addLast);
/*     */     
/*  40 */     return results;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addBlocksConnectingTowards(IntFunction<Neighbor> getNeighbor, SideChainPart endPart, Consumer<BlockPos> accumulator) {
/*  48 */     for (int steps = 1; steps < getMaxChainLength(); steps++) {
/*  49 */       Neighbor neighbor = (Neighbor)getNeighbor.apply(steps);
/*  50 */       if (neighbor.connectsTowards(endPart)) {
/*  51 */         accumulator.accept(neighbor.pos());
/*     */       }
/*  53 */       if (neighbor.isUnconnectableOrChainEnd()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   default void updateNeighborsAfterPoweringDown(LevelAccessor level, BlockPos pos, BlockState state) {
/*  60 */     Neighbors neighbors = getNeighbors(level, pos, getFacing(state));
/*  61 */     neighbors.left().disconnectFromRight();
/*  62 */     neighbors.right().disconnectFromLeft();
/*     */   }
/*     */   
/*     */   default void updateSelfAndNeighborsOnPoweringUp(LevelAccessor level, BlockPos pos, BlockState state, BlockState oldState) {
/*  66 */     if (!isConnectable(state)) {
/*     */       return;
/*     */     }
/*     */     
/*  70 */     if (isBeingUpdatedByNeighbor(state, oldState)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  75 */     Neighbors neighbors = getNeighbors(level, pos, getFacing(state));
/*  76 */     SideChainPart newPartForSelf = SideChainPart.UNCONNECTED;
/*     */     
/*  78 */     int existingChainOnTheLeft = neighbors.left().isConnectable() ? getAllBlocksConnectedTo(level, neighbors.left().pos()).size() : 0;
/*  79 */     int existingChainOnTheRight = neighbors.right().isConnectable() ? getAllBlocksConnectedTo(level, neighbors.right().pos()).size() : 0;
/*  80 */     int currentChainLength = 1;
/*     */     
/*  82 */     if (canConnect(existingChainOnTheLeft, currentChainLength)) {
/*  83 */       newPartForSelf = newPartForSelf.whenConnectedToTheLeft();
/*  84 */       neighbors.left().connectToTheRight();
/*  85 */       currentChainLength += existingChainOnTheLeft;
/*     */     } 
/*     */     
/*  88 */     if (canConnect(existingChainOnTheRight, currentChainLength)) {
/*  89 */       newPartForSelf = newPartForSelf.whenConnectedToTheRight();
/*  90 */       neighbors.right().connectToTheLeft();
/*     */     } 
/*     */     
/*  93 */     setPart(level, pos, newPartForSelf);
/*     */   }
/*     */ 
/*     */   
/*  97 */   private boolean canConnect(int newBlocksToConnectTo, int currentChainLength) { return (newBlocksToConnectTo > 0 && currentChainLength + newBlocksToConnectTo <= getMaxChainLength()); }
/*     */ 
/*     */   
/*     */   private boolean isBeingUpdatedByNeighbor(BlockState state, BlockState oldState) {
/* 101 */     boolean isGettingConnected = getSideChainPart(state).isConnected();
/* 102 */     boolean hasBeenConnectedBefore = (isConnectable(oldState) && getSideChainPart(oldState).isConnected());
/* 103 */     return (isGettingConnected || hasBeenConnectedBefore);
/*     */   }
/*     */ 
/*     */   
/* 107 */   private Neighbors getNeighbors(LevelAccessor level, BlockPos center, Direction facing) { return new Neighbors(this, level, facing, center, new HashMap()); }
/*     */ 
/*     */   
/*     */   private void setPart(LevelAccessor level, BlockPos pos, SideChainPart newPart) {
/* 111 */     BlockState state = level.getBlockState(pos);
/* 112 */     if (getSideChainPart(state) != newPart)
/* 113 */       level.setBlock(pos, setSideChainPart(state, newPart), 3); 
/*     */   }
/*     */   public static final class Neighbors extends Record { private final SideChainPartBlock block; private final LevelAccessor level; private final Direction facing; private final BlockPos center; private final Map<BlockPos, SideChainPartBlock.Neighbor> cache;
/*     */     
/* 117 */     public Neighbors(SideChainPartBlock block, LevelAccessor level, Direction facing, BlockPos center, Map<BlockPos, SideChainPartBlock.Neighbor> cache) { this.block = block; this.level = level; this.facing = facing; this.center = center; this.cache = cache; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 117 */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors; } public SideChainPartBlock block() { return this.block; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #117	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$Neighbors;
/* 117 */       //   0	8	1	o	Ljava/lang/Object; } public LevelAccessor level() { return this.level; } public Direction facing() { return this.facing; } public BlockPos center() { return this.center; } public Map<BlockPos, SideChainPartBlock.Neighbor> cache() { return this.cache; }
/*     */ 
/*     */     
/* 120 */     private boolean isConnectableToThisBlock(BlockState neighbor) { return (this.block.isConnectable(neighbor) && this.block.getFacing(neighbor) == this.facing); }
/*     */ 
/*     */     
/*     */     private SideChainPartBlock.Neighbor createNewNeighbor(BlockPos pos) {
/* 124 */       BlockState neighbor = this.level.getBlockState(pos);
/* 125 */       SideChainPart part = isConnectableToThisBlock(neighbor) ? this.block.getSideChainPart(neighbor) : null;
/* 126 */       return (part == null) ? new SideChainPartBlock.EmptyNeighbor(pos) : new SideChainPartBlock.SideChainNeighbor(this.level, this.block, pos, part);
/*     */     }
/*     */ 
/*     */     
/* 130 */     private SideChainPartBlock.Neighbor getOrCreateNeighbor(Direction dir, Integer steps) { return (SideChainPartBlock.Neighbor)this.cache.computeIfAbsent(this.center.relative(dir, steps.intValue()), this::createNewNeighbor); }
/*     */ 
/*     */ 
/*     */     
/* 134 */     public SideChainPartBlock.Neighbor left(int steps) { return getOrCreateNeighbor(this.facing.getClockWise(), Integer.valueOf(steps)); }
/*     */ 
/*     */ 
/*     */     
/* 138 */     public SideChainPartBlock.Neighbor right(int steps) { return getOrCreateNeighbor(this.facing.getCounterClockWise(), Integer.valueOf(steps)); }
/*     */ 
/*     */ 
/*     */     
/* 142 */     public SideChainPartBlock.Neighbor left() { return left(1); }
/*     */ 
/*     */ 
/*     */     
/* 146 */     public SideChainPartBlock.Neighbor right() { return right(1); } }
/*     */   public static interface Neighbor { BlockPos pos();
/*     */     
/*     */     boolean isConnectable();
/*     */     
/*     */     boolean isUnconnectableOrChainEnd();
/*     */     
/*     */     boolean connectsTowards(SideChainPart param1SideChainPart);
/*     */     
/*     */     default void connectToTheRight() {}
/*     */     
/*     */     default void connectToTheLeft() {}
/*     */     
/*     */     default void disconnectFromRight() {}
/*     */     
/*     */     default void disconnectFromLeft() {} }
/*     */   
/*     */   public static final class EmptyNeighbor extends Record implements Neighbor { private final BlockPos pos;
/*     */     
/* 165 */     public EmptyNeighbor(BlockPos pos) { this.pos = pos; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #165	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$EmptyNeighbor;
/* 165 */       //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; }
/*     */ 
/*     */     
/* 168 */     public boolean isConnectable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     public boolean isUnconnectableOrChainEnd() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     public boolean connectsTowards(SideChainPart endPart) { return false; } }
/*     */   public static final class SideChainNeighbor extends Record implements Neighbor { private final LevelAccessor level; private final SideChainPartBlock block; private final BlockPos pos;
/*     */     private final SideChainPart part;
/*     */     
/* 182 */     public SideChainNeighbor(LevelAccessor level, SideChainPartBlock block, BlockPos pos, SideChainPart part) { this.level = level; this.block = block; this.pos = pos; this.part = part; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/SideChainPartBlock$SideChainNeighbor;
/* 182 */       //   0	8	1	o	Ljava/lang/Object; } public LevelAccessor level() { return this.level; } public SideChainPartBlock block() { return this.block; } public BlockPos pos() { return this.pos; } public SideChainPart part() { return this.part; }
/*     */ 
/*     */     
/* 185 */     public boolean isConnectable() { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     public boolean isUnconnectableOrChainEnd() { return this.part.isChainEnd(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     public boolean connectsTowards(SideChainPart endPart) { return this.part.isConnectionTowards(endPart); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     public void connectToTheRight() { this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheRight()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     public void connectToTheLeft() { this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheLeft()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     public void disconnectFromRight() { this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheRight()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     public void disconnectFromLeft() { this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheLeft()); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SideChainPartBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */