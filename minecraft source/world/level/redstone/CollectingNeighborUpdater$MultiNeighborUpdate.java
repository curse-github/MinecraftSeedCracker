/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.flag.FeatureFlags;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MultiNeighborUpdate
/*     */   implements CollectingNeighborUpdater.NeighborUpdates
/*     */ {
/*     */   private final BlockPos sourcePos;
/*     */   private final Block sourceBlock;
/*     */   private Orientation orientation;
/*     */   private final Direction skipDirection;
/*     */   private int idx;
/*     */   
/*     */   MultiNeighborUpdate(BlockPos sourcePos, Block sourceBlock, Orientation orientation, Direction skipDirection) {
/* 140 */     this.idx = 0;
/*     */ 
/*     */     
/* 143 */     this.sourcePos = sourcePos;
/* 144 */     this.sourceBlock = sourceBlock;
/* 145 */     this.orientation = orientation;
/* 146 */     this.skipDirection = skipDirection;
/* 147 */     if (NeighborUpdater.UPDATE_ORDER[this.idx] == skipDirection) {
/* 148 */       this.idx++;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean runNext(Level level) {
/* 154 */     Direction direction = NeighborUpdater.UPDATE_ORDER[this.idx++];
/* 155 */     BlockPos neighborPos = this.sourcePos.relative(direction);
/* 156 */     BlockState state = level.getBlockState(neighborPos);
/* 157 */     Orientation orientation = null;
/* 158 */     if (level.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS)) {
/* 159 */       if (this.orientation == null) {
/* 160 */         this.orientation = ExperimentalRedstoneUtils.initialOrientation(level, (this.skipDirection == null) ? null : this.skipDirection.getOpposite(), null);
/*     */       }
/* 162 */       orientation = this.orientation.withFront(direction);
/*     */     } 
/* 164 */     NeighborUpdater.executeUpdate(level, state, neighborPos, this.sourceBlock, orientation, false);
/* 165 */     if (this.idx < NeighborUpdater.UPDATE_ORDER.length && NeighborUpdater.UPDATE_ORDER[this.idx] == this.skipDirection) {
/* 166 */       this.idx++;
/*     */     }
/* 168 */     return (this.idx < NeighborUpdater.UPDATE_ORDER.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachUpdatedPos(Consumer<BlockPos> output) {
/* 173 */     for (Direction direction : NeighborUpdater.UPDATE_ORDER) {
/* 174 */       if (direction != this.skipDirection) {
/*     */ 
/*     */         
/* 177 */         BlockPos neighborPos = this.sourcePos.relative(direction);
/* 178 */         output.accept(neighborPos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$MultiNeighborUpdate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */