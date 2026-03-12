/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SeaPickleBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ 
/*    */ public class SeaPickleFeature
/*    */   extends Feature<CountConfiguration>
/*    */ {
/* 16 */   public SeaPickleFeature(Codec<CountConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<CountConfiguration> context) {
/* 21 */     int placed = 0;
/* 22 */     RandomSource random = context.random();
/* 23 */     WorldGenLevel level = context.level();
/* 24 */     BlockPos origin = context.origin();
/* 25 */     int count = ((CountConfiguration)context.config()).count().sample(random);
/* 26 */     for (int i = 0; i < count; i++) {
/* 27 */       int x = random.nextInt(8) - random.nextInt(8);
/* 28 */       int z = random.nextInt(8) - random.nextInt(8);
/* 29 */       int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX() + x, origin.getZ() + z);
/* 30 */       BlockPos picklePos = new BlockPos(origin.getX() + x, y, origin.getZ() + z);
/*    */       
/* 32 */       BlockState pickleState = (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, Integer.valueOf(random.nextInt(4) + 1));
/* 33 */       if (level.getBlockState(picklePos).is(Blocks.WATER) && pickleState.canSurvive(level, picklePos)) {
/* 34 */         level.setBlock(picklePos, pickleState, 2);
/* 35 */         placed++;
/*    */       } 
/*    */     } 
/* 38 */     return (placed > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SeaPickleFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */