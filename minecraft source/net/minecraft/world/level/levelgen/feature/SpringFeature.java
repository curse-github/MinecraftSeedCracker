/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
/*    */ 
/*    */ public class SpringFeature
/*    */   extends Feature<SpringConfiguration>
/*    */ {
/* 12 */   public SpringFeature(Codec<SpringConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SpringConfiguration> context) {
/* 17 */     SpringConfiguration config = (SpringConfiguration)context.config();
/* 18 */     WorldGenLevel level = context.level();
/* 19 */     BlockPos origin = context.origin();
/* 20 */     if (!level.getBlockState(origin.above()).is(config.validBlocks)) {
/* 21 */       return false;
/*    */     }
/* 23 */     if (config.requiresBlockBelow && !level.getBlockState(origin.below()).is(config.validBlocks)) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     BlockState currentState = level.getBlockState(origin);
/* 28 */     if (!currentState.isAir() && !currentState.is(config.validBlocks)) {
/* 29 */       return false;
/*    */     }
/*    */     
/* 32 */     int placed = 0;
/*    */     
/* 34 */     int rockCount = 0;
/* 35 */     if (level.getBlockState(origin.west()).is(config.validBlocks)) {
/* 36 */       rockCount++;
/*    */     }
/* 38 */     if (level.getBlockState(origin.east()).is(config.validBlocks)) {
/* 39 */       rockCount++;
/*    */     }
/* 41 */     if (level.getBlockState(origin.north()).is(config.validBlocks)) {
/* 42 */       rockCount++;
/*    */     }
/* 44 */     if (level.getBlockState(origin.south()).is(config.validBlocks)) {
/* 45 */       rockCount++;
/*    */     }
/* 47 */     if (level.getBlockState(origin.below()).is(config.validBlocks)) {
/* 48 */       rockCount++;
/*    */     }
/*    */     
/* 51 */     int holeCount = 0;
/* 52 */     if (level.isEmptyBlock(origin.west())) {
/* 53 */       holeCount++;
/*    */     }
/* 55 */     if (level.isEmptyBlock(origin.east())) {
/* 56 */       holeCount++;
/*    */     }
/* 58 */     if (level.isEmptyBlock(origin.north())) {
/* 59 */       holeCount++;
/*    */     }
/* 61 */     if (level.isEmptyBlock(origin.south())) {
/* 62 */       holeCount++;
/*    */     }
/* 64 */     if (level.isEmptyBlock(origin.below())) {
/* 65 */       holeCount++;
/*    */     }
/*    */     
/* 68 */     if (rockCount == config.rockCount && holeCount == config.holeCount) {
/* 69 */       level.setBlock(origin, config.state.createLegacyBlock(), 2);
/* 70 */       level.scheduleTick(origin, config.state.getType(), 0);
/* 71 */       placed++;
/*    */     } 
/*    */     
/* 74 */     return (placed > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SpringFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */