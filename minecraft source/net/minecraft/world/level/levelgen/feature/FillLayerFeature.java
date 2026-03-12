/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
/*    */ 
/*    */ public class FillLayerFeature
/*    */   extends Feature<LayerConfiguration>
/*    */ {
/* 11 */   public FillLayerFeature(Codec<LayerConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<LayerConfiguration> context) {
/* 16 */     BlockPos origin = context.origin();
/* 17 */     LayerConfiguration config = (LayerConfiguration)context.config();
/* 18 */     WorldGenLevel level = context.level();
/* 19 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*    */     
/* 21 */     for (int dx = 0; dx < 16; dx++) {
/* 22 */       for (int dz = 0; dz < 16; dz++) {
/* 23 */         int x = origin.getX() + dx;
/* 24 */         int z = origin.getZ() + dz;
/* 25 */         int y = level.getMinY() + config.height;
/* 26 */         pos.set(x, y, z);
/*    */         
/* 28 */         if (level.getBlockState(pos).isAir()) {
/* 29 */           level.setBlock(pos, config.state, 2);
/*    */         }
/*    */       } 
/*    */     } 
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\FillLayerFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */