/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class EndPlatformFeature
/*    */   extends Feature<NoneFeatureConfiguration> {
/* 12 */   public EndPlatformFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
/* 17 */     createEndPlatform(context.level(), context.origin(), false);
/* 18 */     return true;
/*    */   }
/*    */   
/*    */   public static void createEndPlatform(ServerLevelAccessor newLevel, BlockPos origin, boolean dropResources) {
/* 22 */     BlockPos.MutableBlockPos pos = origin.mutable();
/*    */     
/* 24 */     for (int dz = -2; dz <= 2; dz++) {
/* 25 */       for (int dx = -2; dx <= 2; dx++) {
/* 26 */         for (int dy = -1; dy < 3; dy++) {
/* 27 */           BlockPos.MutableBlockPos mutableBlockPos = pos.set(origin).move(dx, dy, dz);
/* 28 */           Block block = (dy == -1) ? Blocks.OBSIDIAN : Blocks.AIR;
/* 29 */           if (!newLevel.getBlockState(mutableBlockPos).is(block)) {
/* 30 */             if (dropResources) {
/* 31 */               newLevel.destroyBlock(mutableBlockPos, true, null);
/*    */             }
/* 33 */             newLevel.setBlock(mutableBlockPos, block.defaultBlockState(), 3);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\EndPlatformFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */