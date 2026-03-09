/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CollectingNeighborUpdater
/*     */   implements NeighborUpdater {
/*  19 */   private static final Logger LOGGER = LogUtils.getLogger(); private final Level level; private final int maxChainedNeighborUpdates; private final ArrayDeque<NeighborUpdates> stack; private final List<NeighborUpdates> addedThisLayer; private int count;
/*     */   private Consumer<BlockPos> debugListener;
/*     */   
/*     */   public CollectingNeighborUpdater(Level level, int maxChainedNeighborUpdates) {
/*  23 */     this.stack = new ArrayDeque();
/*  24 */     this.addedThisLayer = new ArrayList();
/*  25 */     this.count = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  30 */     this.level = level;
/*  31 */     this.maxChainedNeighborUpdates = maxChainedNeighborUpdates;
/*     */   }
/*     */ 
/*     */   
/*  35 */   public void setDebugListener(Consumer<BlockPos> debugListener) { this.debugListener = debugListener; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   public void shapeUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, @UpdateFlags int updateFlags, int updateLimit) { addAndRun(pos, new ShapeUpdate(direction, neighborState, pos.immutable(), neighborPos.immutable(), updateFlags, updateLimit)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public void neighborChanged(BlockPos pos, Block block, Orientation orientation) { addAndRun(pos, new SimpleNeighborUpdate(pos, block, orientation)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public void neighborChanged(BlockState state, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) { addAndRun(pos, new FullNeighborUpdate(state, pos.immutable(), block, orientation, movedByPiston)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public void updateNeighborsAtExceptFromFacing(BlockPos pos, Block block, Direction skipDirection, Orientation orientation) { addAndRun(pos, new MultiNeighborUpdate(pos.immutable(), block, orientation, skipDirection)); }
/*     */ 
/*     */   
/*     */   private void addAndRun(BlockPos pos, NeighborUpdates update) {
/*  59 */     boolean runningAlready = (this.count > 0);
/*  60 */     boolean tooManyUpdates = (this.maxChainedNeighborUpdates >= 0 && this.count >= this.maxChainedNeighborUpdates);
/*     */     
/*  62 */     this.count++;
/*  63 */     if (!tooManyUpdates) {
/*  64 */       if (runningAlready) {
/*  65 */         this.addedThisLayer.add(update);
/*     */       } else {
/*  67 */         this.stack.push(update);
/*     */       } 
/*  69 */     } else if (this.count - 1 == this.maxChainedNeighborUpdates) {
/*  70 */       LOGGER.error("Too many chained neighbor updates. Skipping the rest. First skipped position: {}", pos.toShortString());
/*     */     } 
/*  72 */     if (!runningAlready) {
/*  73 */       runUpdates();
/*     */     }
/*     */   }
/*     */   
/*     */   private void runUpdates() {
/*     */     try {
/*  79 */       while (!this.stack.isEmpty() || !this.addedThisLayer.isEmpty()) {
/*  80 */         for (int i = this.addedThisLayer.size() - 1; i >= 0; i--) {
/*  81 */           this.stack.push((NeighborUpdates)this.addedThisLayer.get(i));
/*     */         }
/*  83 */         this.addedThisLayer.clear();
/*  84 */         NeighborUpdates nextUpdates = (NeighborUpdates)this.stack.peek();
/*  85 */         if (this.debugListener != null) {
/*  86 */           nextUpdates.forEachUpdatedPos(this.debugListener);
/*     */         }
/*  88 */         while (this.addedThisLayer.isEmpty()) {
/*  89 */           if (!nextUpdates.runNext(this.level)) {
/*  90 */             this.stack.pop();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/*  96 */       this.stack.clear();
/*  97 */       this.addedThisLayer.clear();
/*  98 */       this.count = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class SimpleNeighborUpdate
/*     */     extends Record implements NeighborUpdates {
/*     */     private final BlockPos pos;
/*     */     private final Block block;
/*     */     private final Orientation orientation;
/*     */     
/* 108 */     SimpleNeighborUpdate(BlockPos pos, Block block, Orientation orientation) { this.pos = pos; this.block = block; this.orientation = orientation; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$SimpleNeighborUpdate;
/* 108 */       //   0	8	1	o	Ljava/lang/Object; } public BlockPos pos() { return this.pos; } public Block block() { return this.block; } public Orientation orientation() { return this.orientation; }
/*     */     
/*     */     public boolean runNext(Level level) {
/* 111 */       BlockState state = level.getBlockState(this.pos);
/* 112 */       NeighborUpdater.executeUpdate(level, state, this.pos, this.block, this.orientation, false);
/* 113 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 118 */     public void forEachUpdatedPos(Consumer<BlockPos> output) { output.accept(this.pos); } }
/*     */   static final class FullNeighborUpdate extends Record implements NeighborUpdates { private final BlockState state; private final BlockPos pos; private final Block block; private final Orientation orientation;
/*     */     private final boolean movedByPiston;
/*     */     
/* 122 */     FullNeighborUpdate(BlockState state, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) { this.state = state; this.pos = pos; this.block = block; this.orientation = orientation; this.movedByPiston = movedByPiston; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #122	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #122	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #122	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$FullNeighborUpdate;
/* 122 */       //   0	8	1	o	Ljava/lang/Object; } public BlockState state() { return this.state; } public BlockPos pos() { return this.pos; } public Block block() { return this.block; } public Orientation orientation() { return this.orientation; } public boolean movedByPiston() { return this.movedByPiston; }
/*     */     
/*     */     public boolean runNext(Level level) {
/* 125 */       NeighborUpdater.executeUpdate(level, this.state, this.pos, this.block, this.orientation, this.movedByPiston);
/* 126 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 131 */     public void forEachUpdatedPos(Consumer<BlockPos> output) { output.accept(this.pos); } }
/*     */   
/*     */   static final class MultiNeighborUpdate implements NeighborUpdates { private final BlockPos sourcePos;
/*     */     private final Block sourceBlock;
/*     */     private Orientation orientation;
/*     */     private final Direction skipDirection;
/*     */     private int idx;
/*     */     
/*     */     MultiNeighborUpdate(BlockPos sourcePos, Block sourceBlock, Orientation orientation, Direction skipDirection) {
/* 140 */       this.idx = 0;
/*     */ 
/*     */       
/* 143 */       this.sourcePos = sourcePos;
/* 144 */       this.sourceBlock = sourceBlock;
/* 145 */       this.orientation = orientation;
/* 146 */       this.skipDirection = skipDirection;
/* 147 */       if (NeighborUpdater.UPDATE_ORDER[this.idx] == skipDirection) {
/* 148 */         this.idx++;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean runNext(Level level) {
/* 154 */       Direction direction = NeighborUpdater.UPDATE_ORDER[this.idx++];
/* 155 */       BlockPos neighborPos = this.sourcePos.relative(direction);
/* 156 */       BlockState state = level.getBlockState(neighborPos);
/* 157 */       Orientation orientation = null;
/* 158 */       if (level.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS)) {
/* 159 */         if (this.orientation == null) {
/* 160 */           this.orientation = ExperimentalRedstoneUtils.initialOrientation(level, (this.skipDirection == null) ? null : this.skipDirection.getOpposite(), null);
/*     */         }
/* 162 */         orientation = this.orientation.withFront(direction);
/*     */       } 
/* 164 */       NeighborUpdater.executeUpdate(level, state, neighborPos, this.sourceBlock, orientation, false);
/* 165 */       if (this.idx < NeighborUpdater.UPDATE_ORDER.length && NeighborUpdater.UPDATE_ORDER[this.idx] == this.skipDirection) {
/* 166 */         this.idx++;
/*     */       }
/* 168 */       return (this.idx < NeighborUpdater.UPDATE_ORDER.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEachUpdatedPos(Consumer<BlockPos> output) {
/* 173 */       for (Direction direction : NeighborUpdater.UPDATE_ORDER) {
/* 174 */         if (direction != this.skipDirection) {
/*     */ 
/*     */           
/* 177 */           BlockPos neighborPos = this.sourcePos.relative(direction);
/* 178 */           output.accept(neighborPos);
/*     */         } 
/*     */       } 
/*     */     } }
/*     */   private static final class ShapeUpdate extends Record implements NeighborUpdates { private final Direction direction; private final BlockState neighborState; private final BlockPos pos;
/* 183 */     private ShapeUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, @UpdateFlags int updateFlags, int updateLimit) { this.direction = direction; this.neighborState = neighborState; this.pos = pos; this.neighborPos = neighborPos; this.updateFlags = updateFlags; this.updateLimit = updateLimit; } private final BlockPos neighborPos; @UpdateFlags private final int updateFlags; private final int updateLimit; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/redstone/CollectingNeighborUpdater$ShapeUpdate;
/* 183 */       //   0	8	1	o	Ljava/lang/Object; } public Direction direction() { return this.direction; } public BlockState neighborState() { return this.neighborState; } public BlockPos pos() { return this.pos; } public BlockPos neighborPos() { return this.neighborPos; } @UpdateFlags public int updateFlags() { return this.updateFlags; } public int updateLimit() { return this.updateLimit; }
/*     */     
/*     */     public boolean runNext(Level level) {
/* 186 */       NeighborUpdater.executeShapeUpdate(level, this.direction, this.pos, this.neighborPos, this.neighborState, this.updateFlags, this.updateLimit);
/* 187 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 192 */     public void forEachUpdatedPos(Consumer<BlockPos> output) { output.accept(this.pos); } }
/*     */ 
/*     */   
/*     */   private static interface NeighborUpdates {
/*     */     boolean runNext(Level param1Level);
/*     */     
/*     */     void forEachUpdatedPos(Consumer<BlockPos> param1Consumer);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */