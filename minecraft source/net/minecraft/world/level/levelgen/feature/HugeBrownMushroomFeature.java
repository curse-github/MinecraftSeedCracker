/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.HugeMushroomBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
/*    */ 
/*    */ public class HugeBrownMushroomFeature
/*    */   extends AbstractHugeMushroomFeature {
/* 13 */   public HugeBrownMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void makeCap(LevelAccessor level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos, HugeMushroomFeatureConfiguration config) {
/* 18 */     int radius = config.foliageRadius;
/* 19 */     for (int dx = -radius; dx <= radius; dx++) {
/* 20 */       for (int dz = -radius; dz <= radius; dz++) {
/* 21 */         boolean minX = (dx == -radius);
/* 22 */         boolean maxX = (dx == radius);
/* 23 */         boolean minZ = (dz == -radius);
/* 24 */         boolean maxZ = (dz == radius);
/*    */         
/* 26 */         boolean xEdge = (minX || maxX);
/* 27 */         boolean zEdge = (minZ || maxZ);
/* 28 */         if (!xEdge || !zEdge) {
/*    */ 
/*    */ 
/*    */           
/* 32 */           blockPos.setWithOffset(origin, dx, treeHeight, dz);
/* 33 */           boolean west = (minX || (zEdge && dx == 1 - radius));
/* 34 */           boolean east = (maxX || (zEdge && dx == radius - 1));
/* 35 */           boolean north = (minZ || (xEdge && dz == 1 - radius));
/* 36 */           boolean south = (maxZ || (xEdge && dz == radius - 1));
/* 37 */           BlockState state = config.capProvider.getState(random, origin);
/* 38 */           if (state.hasProperty(HugeMushroomBlock.WEST) && state
/* 39 */             .hasProperty(HugeMushroomBlock.EAST) && state
/* 40 */             .hasProperty(HugeMushroomBlock.NORTH) && state
/* 41 */             .hasProperty(HugeMushroomBlock.SOUTH))
/*    */           {
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 47 */             state = (BlockState)((BlockState)((BlockState)((BlockState)state.setValue(HugeMushroomBlock.WEST, Boolean.valueOf(west))).setValue(HugeMushroomBlock.EAST, Boolean.valueOf(east))).setValue(HugeMushroomBlock.NORTH, Boolean.valueOf(north))).setValue(HugeMushroomBlock.SOUTH, Boolean.valueOf(south));
/*    */           }
/* 49 */           placeMushroomBlock(level, blockPos, state);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 56 */   protected int getTreeRadiusForHeight(int trunkHeight, int treeHeight, int leafRadius, int yo) { return (yo <= 3) ? 0 : leafRadius; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\HugeBrownMushroomFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */