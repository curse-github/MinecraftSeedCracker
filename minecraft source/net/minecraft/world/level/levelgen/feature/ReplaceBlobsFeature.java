/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
/*    */ 
/*    */ public class ReplaceBlobsFeature
/*    */   extends Feature<ReplaceSphereConfiguration>
/*    */ {
/* 16 */   public ReplaceBlobsFeature(Codec<ReplaceSphereConfiguration> codec) { super(codec); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(FeaturePlaceContext<ReplaceSphereConfiguration> context) {
/* 21 */     ReplaceSphereConfiguration config = (ReplaceSphereConfiguration)context.config();
/* 22 */     WorldGenLevel level = context.level();
/* 23 */     RandomSource random = context.random();
/* 24 */     Block targetBlock = config.targetState.getBlock();
/* 25 */     BlockPos centerPos = findTarget(level, context.origin().mutable().clamp(Direction.Axis.Y, level.getMinY() + 1, level.getMaxY()), targetBlock);
/* 26 */     if (centerPos == null) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     int radiusX = config.radius().sample(random);
/* 31 */     int radiusY = config.radius().sample(random);
/* 32 */     int radiusZ = config.radius().sample(random);
/* 33 */     int maximumRadius = Math.max(radiusX, Math.max(radiusY, radiusZ));
/*    */     
/* 35 */     boolean replacedAny = false;
/* 36 */     for (BlockPos pos : BlockPos.withinManhattan(centerPos, radiusX, radiusY, radiusZ)) {
/* 37 */       if (pos.distManhattan(centerPos) > maximumRadius) {
/*    */         break;
/*    */       }
/*    */ 
/*    */       
/* 42 */       BlockState blockState = level.getBlockState(pos);
/* 43 */       if (blockState.is(targetBlock)) {
/* 44 */         setBlock(level, pos, config.replaceState);
/* 45 */         replacedAny = true;
/*    */       } 
/*    */     } 
/*    */     
/* 49 */     return replacedAny;
/*    */   }
/*    */   
/*    */   private static BlockPos findTarget(LevelAccessor level, BlockPos.MutableBlockPos cursor, Block target) {
/* 53 */     while (cursor.getY() > level.getMinY() + 1) {
/* 54 */       BlockState blockState = level.getBlockState(cursor);
/* 55 */       if (blockState.is(target)) {
/* 56 */         return cursor;
/*    */       }
/*    */       
/* 59 */       cursor.move(Direction.DOWN);
/*    */     } 
/* 61 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\ReplaceBlobsFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */