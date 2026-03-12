/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.WallTorchBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class EndPodiumFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public static final int PODIUM_RADIUS = 4;
/*    */   public static final int PODIUM_PILLAR_HEIGHT = 4;
/*    */   public static final int RIM_RADIUS = 1;
/*    */   public static final float CORNER_ROUNDING = 0.5F;
/* 17 */   private static final BlockPos END_PODIUM_LOCATION = BlockPos.ZERO;
/*    */   private final boolean active;
/*    */   
/* 20 */   public static BlockPos getLocation(BlockPos offset) { return END_PODIUM_LOCATION.offset(offset); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EndPodiumFeature(boolean active) {
/* 26 */     super(NoneFeatureConfiguration.CODEC);
/* 27 */     this.active = active;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 32 */     BlockPos origin = context.origin();
/* 33 */     WorldGenLevel level = context.level();
/* 34 */     for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(origin.getX() - 4, origin.getY() - 1, origin.getZ() - 4), new BlockPos(origin.getX() + 4, origin.getY() + 32, origin.getZ() + 4))) {
/* 35 */       boolean insideRim = pos.closerThan(origin, 2.5D);
/*    */       
/* 37 */       if (insideRim || pos.closerThan(origin, 3.5D)) {
/* 38 */         if (pos.getY() < origin.getY()) {
/* 39 */           if (insideRim) {
/*    */             
/* 41 */             setBlock(level, pos, Blocks.BEDROCK.defaultBlockState()); continue;
/* 42 */           }  if (pos.getY() < origin.getY()) {
/*    */             
/* 44 */             if (this.active) {
/* 45 */               dropPreviousAndSetBlock(level, pos, Blocks.END_STONE); continue;
/*    */             } 
/* 47 */             setBlock(level, pos, Blocks.END_STONE.defaultBlockState());
/*    */           }  continue;
/*    */         } 
/* 50 */         if (pos.getY() > origin.getY()) {
/*    */           
/* 52 */           if (this.active) {
/* 53 */             dropPreviousAndSetBlock(level, pos, Blocks.AIR); continue;
/*    */           } 
/* 55 */           setBlock(level, pos, Blocks.AIR.defaultBlockState()); continue;
/*    */         } 
/* 57 */         if (!insideRim) {
/*    */           
/* 59 */           setBlock(level, pos, Blocks.BEDROCK.defaultBlockState()); continue;
/* 60 */         }  if (this.active) {
/*    */           
/* 62 */           dropPreviousAndSetBlock(level, new BlockPos(pos), Blocks.END_PORTAL); continue;
/*    */         } 
/* 64 */         setBlock(level, new BlockPos(pos), Blocks.AIR.defaultBlockState());
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 70 */     for (int y = 0; y < 4; y++) {
/* 71 */       setBlock(level, origin.above(y), Blocks.BEDROCK.defaultBlockState());
/*    */     }
/*    */     
/* 74 */     BlockPos centerOfPillar = origin.above(2);
/* 75 */     for (Direction face : Direction.Plane.HORIZONTAL) {
/* 76 */       setBlock(level, centerOfPillar.relative(face), (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, face));
/*    */     }
/*    */     
/* 79 */     return true;
/*    */   }
/*    */   
/*    */   private void dropPreviousAndSetBlock(WorldGenLevel level, BlockPos pos, Block block) {
/* 83 */     if (!level.getBlockState(pos).is(block)) {
/* 84 */       level.destroyBlock(pos, true, null);
/* 85 */       setBlock(level, pos, block.defaultBlockState());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\EndPodiumFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */