/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*    */ 
/*    */ public class EndGatewayFeature extends Feature<EndGatewayConfiguration> {
/* 13 */   public EndGatewayFeature(Codec<EndGatewayConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<EndGatewayConfiguration> context) {
/* 18 */     BlockPos origin = context.origin();
/* 19 */     WorldGenLevel level = context.level();
/* 20 */     EndGatewayConfiguration config = (EndGatewayConfiguration)context.config();
/* 21 */     for (Iterator iterator = BlockPos.betweenClosed(origin.offset(-1, -2, -1), origin.offset(1, 2, 1)).iterator(); iterator.hasNext(); ) { BlockPos pos = (BlockPos)iterator.next();
/* 22 */       boolean sameX = (pos.getX() == origin.getX());
/* 23 */       boolean sameY = (pos.getY() == origin.getY());
/* 24 */       boolean sameZ = (pos.getZ() == origin.getZ());
/* 25 */       boolean end = (Math.abs(pos.getY() - origin.getY()) == 2);
/*    */       
/* 27 */       if (sameX && sameY && sameZ) {
/* 28 */         BlockPos immutable = pos.immutable();
/* 29 */         setBlock(level, immutable, Blocks.END_GATEWAY.defaultBlockState());
/* 30 */         config.getExit().ifPresent(targetPos -> {
/* 31 */               BlockEntity exitEntity = level.getBlockEntity(immutable);
/* 32 */               if (exitEntity instanceof TheEndGatewayBlockEntity) { TheEndGatewayBlockEntity exitGateway = (TheEndGatewayBlockEntity)exitEntity;
/* 33 */                 exitGateway.setExitPosition(targetPos, config.isExitExact()); }
/*    */             
/*    */             }); continue;
/* 36 */       }  if (sameY) {
/* 37 */         setBlock(level, pos, Blocks.AIR.defaultBlockState()); continue;
/* 38 */       }  if (end && sameX && sameZ) {
/* 39 */         setBlock(level, pos, Blocks.BEDROCK.defaultBlockState()); continue;
/* 40 */       }  if ((!sameX && !sameZ) || end) {
/* 41 */         setBlock(level, pos, Blocks.AIR.defaultBlockState()); continue;
/*    */       } 
/* 43 */       setBlock(level, pos, Blocks.BEDROCK.defaultBlockState()); }
/*    */ 
/*    */     
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\EndGatewayFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */