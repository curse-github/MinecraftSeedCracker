/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.lighting.LightEngine;
/*    */ 
/*    */ public abstract class SpreadingSnowyDirtBlock
/*    */   extends SnowyDirtBlock
/*    */ {
/* 17 */   protected SpreadingSnowyDirtBlock(BlockBehaviour.Properties properties) { super(properties); }
/*    */ 
/*    */   
/*    */   private static boolean canBeGrass(BlockState state, LevelReader level, BlockPos pos) {
/* 21 */     BlockPos above = pos.above();
/* 22 */     BlockState aboveState = level.getBlockState(above);
/* 23 */     if (aboveState.is(Blocks.SNOW) && ((Integer)aboveState.getValue(SnowLayerBlock.LAYERS)).intValue() == 1) {
/* 24 */       return true;
/*    */     }
/*    */     
/* 27 */     if (aboveState.getFluidState().getAmount() == 8) {
/* 28 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 32 */     int lightBlockInto = LightEngine.getLightBlockInto(state, aboveState, Direction.UP, aboveState.getLightBlock());
/*    */     
/* 34 */     return (lightBlockInto < 15);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends SpreadingSnowyDirtBlock> codec();
/*    */   
/*    */   private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
/* 41 */     BlockPos above = pos.above();
/* 42 */     return (canBeGrass(state, level, pos) && !level.getFluidState(above).is(FluidTags.WATER));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 47 */     if (!canBeGrass(state, level, pos)) {
/* 48 */       level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
/* 53 */       BlockState defaultBlockState = defaultBlockState();
/*    */       
/* 55 */       for (int i = 0; i < 4; i++) {
/* 56 */         BlockPos testPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
/* 57 */         if (level.getBlockState(testPos).is(Blocks.DIRT) && canPropagate(defaultBlockState, level, testPos))
/* 58 */           level.setBlockAndUpdate(testPos, (BlockState)defaultBlockState.setValue(SNOWY, Boolean.valueOf(isSnowySetting(level.getBlockState(testPos.above()))))); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SpreadingSnowyDirtBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */