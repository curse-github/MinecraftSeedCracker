/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SculkVeinSpreaderConfig
/*     */   extends MultifaceSpreader.DefaultSpreaderConfig
/*     */ {
/*     */   private final MultifaceSpreader.SpreadType[] spreadTypes;
/*     */   
/*     */   public SculkVeinSpreaderConfig(SculkVeinBlock paramSculkVeinBlock, SpreadType... spreadTypes) {
/* 156 */     super(paramSculkVeinBlock);
/* 157 */     this.spreadTypes = spreadTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stateCanBeReplaced(BlockGetter level, BlockPos sourcePos, BlockPos placementPos, Direction placementDirection, BlockState existingState) {
/* 162 */     BlockState againstState = level.getBlockState(placementPos.relative(placementDirection));
/*     */ 
/*     */ 
/*     */     
/* 166 */     if (againstState.is(Blocks.SCULK) || againstState.is(Blocks.SCULK_CATALYST) || againstState.is(Blocks.MOVING_PISTON)) {
/* 167 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 172 */     if (sourcePos.distManhattan(placementPos) == 2) {
/* 173 */       BlockPos neighourPos = sourcePos.relative(placementDirection.getOpposite());
/* 174 */       if (level.getBlockState(neighourPos).isFaceSturdy(level, neighourPos, placementDirection)) {
/* 175 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 179 */     FluidState fluidState = existingState.getFluidState();
/* 180 */     if (!fluidState.isEmpty() && !fluidState.is(Fluids.WATER)) {
/* 181 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 185 */     if (existingState.is(BlockTags.FIRE)) {
/* 186 */       return false;
/*     */     }
/*     */     
/* 189 */     return (existingState.canBeReplaced() || super.stateCanBeReplaced(level, sourcePos, placementPos, placementDirection, existingState));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 194 */   public MultifaceSpreader.SpreadType[] getSpreadTypes() { return this.spreadTypes; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   public boolean isOtherBlockValidAsSource(BlockState state) { return !state.is(Blocks.SCULK_VEIN); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SculkVeinBlock$SculkVeinSpreaderConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */