/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BlockCounter
/*     */   extends Object
/*     */   implements PalettedContainer.CountConsumer<BlockState>
/*     */ {
/*     */   public int nonEmptyBlockCount;
/*     */   public int tickingBlockCount;
/*     */   public int tickingFluidCount;
/*     */   
/*     */   BlockCounter(LevelChunkSection this$0) {}
/*     */   
/*     */   public void accept(BlockState state, int count) {
/* 125 */     FluidState fluid = state.getFluidState();
/*     */     
/* 127 */     if (!state.isAir()) {
/* 128 */       this.nonEmptyBlockCount += count;
/* 129 */       if (state.isRandomlyTicking()) {
/* 130 */         this.tickingBlockCount += count;
/*     */       }
/*     */     } 
/* 133 */     if (!fluid.isEmpty()) {
/* 134 */       this.nonEmptyBlockCount += count;
/* 135 */       if (fluid.isRandomlyTicking())
/* 136 */         this.tickingFluidCount += count; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\LevelChunkSection$1BlockCounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */