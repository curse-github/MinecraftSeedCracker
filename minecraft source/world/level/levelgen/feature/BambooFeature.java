/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.BambooStalkBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class BambooFeature
/*    */   extends Feature<ProbabilityFeatureConfiguration> {
/* 17 */   private static final BlockState BAMBOO_TRUNK = (BlockState)((BlockState)((BlockState)Blocks.BAMBOO.defaultBlockState().setValue(BambooStalkBlock.AGE, Integer.valueOf(1))).setValue(BambooStalkBlock.LEAVES, BambooLeaves.NONE)).setValue(BambooStalkBlock.STAGE, Integer.valueOf(0));
/* 18 */   private static final BlockState BAMBOO_FINAL_LARGE = (BlockState)((BlockState)BAMBOO_TRUNK.setValue(BambooStalkBlock.LEAVES, BambooLeaves.LARGE)).setValue(BambooStalkBlock.STAGE, Integer.valueOf(1));
/* 19 */   private static final BlockState BAMBOO_TOP_LARGE = (BlockState)BAMBOO_TRUNK.setValue(BambooStalkBlock.LEAVES, BambooLeaves.LARGE);
/* 20 */   private static final BlockState BAMBOO_TOP_SMALL = (BlockState)BAMBOO_TRUNK.setValue(BambooStalkBlock.LEAVES, BambooLeaves.SMALL);
/*    */ 
/*    */   
/* 23 */   public BambooFeature(Codec<ProbabilityFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> context) {
/* 28 */     int placed = 0;
/*    */     
/* 30 */     BlockPos origin = context.origin();
/* 31 */     WorldGenLevel level = context.level();
/* 32 */     RandomSource random = context.random();
/* 33 */     ProbabilityFeatureConfiguration config = (ProbabilityFeatureConfiguration)context.config();
/* 34 */     BlockPos.MutableBlockPos bambooPos = origin.mutable();
/* 35 */     BlockPos.MutableBlockPos podzolPos = origin.mutable();
/* 36 */     if (level.isEmptyBlock(bambooPos)) {
/* 37 */       if (Blocks.BAMBOO.defaultBlockState().canSurvive(level, bambooPos)) {
/* 38 */         int height = random.nextInt(12) + 5;
/*    */ 
/*    */         
/* 41 */         if (random.nextFloat() < config.probability) {
/* 42 */           int r = random.nextInt(4) + 1;
/* 43 */           for (int xx = origin.getX() - r; xx <= origin.getX() + r; xx++) {
/* 44 */             for (int zz = origin.getZ() - r; zz <= origin.getZ() + r; zz++) {
/* 45 */               int xd = xx - origin.getX();
/* 46 */               int zd = zz - origin.getZ();
/* 47 */               if (xd * xd + zd * zd <= r * r) {
/*    */ 
/*    */ 
/*    */                 
/* 51 */                 podzolPos.set(xx, level.getHeight(Heightmap.Types.WORLD_SURFACE, xx, zz) - 1, zz);
/* 52 */                 if (isDirt(level.getBlockState(podzolPos))) {
/* 53 */                   level.setBlock(podzolPos, Blocks.PODZOL.defaultBlockState(), 2);
/*    */                 }
/*    */               } 
/*    */             } 
/*    */           } 
/*    */         } 
/* 59 */         for (int i = 0; i < height && 
/* 60 */           level.isEmptyBlock(bambooPos); i++) {
/* 61 */           level.setBlock(bambooPos, BAMBOO_TRUNK, 2);
/*    */ 
/*    */ 
/*    */           
/* 65 */           bambooPos.move(Direction.UP, 1);
/*    */         } 
/*    */         
/* 68 */         if (bambooPos.getY() - origin.getY() >= 3) {
/* 69 */           level.setBlock(bambooPos, BAMBOO_FINAL_LARGE, 2);
/* 70 */           level.setBlock(bambooPos.move(Direction.DOWN, 1), BAMBOO_TOP_LARGE, 2);
/* 71 */           level.setBlock(bambooPos.move(Direction.DOWN, 1), BAMBOO_TOP_SMALL, 2);
/*    */         } 
/*    */       } 
/* 74 */       placed++;
/*    */     } 
/*    */     
/* 77 */     return (placed > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BambooFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */