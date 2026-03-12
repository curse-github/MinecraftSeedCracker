/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class PlacementFilter
/*    */   extends PlacementModifier
/*    */ {
/*    */   public final Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/* 11 */     if (shouldPlace(context, random, origin)) {
/* 12 */       return Stream.of(origin);
/*    */     }
/* 14 */     return Stream.of(new BlockPos[0]);
/*    */   }
/*    */   
/*    */   protected abstract boolean shouldPlace(PlacementContext paramPlacementContext, RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\PlacementFilter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */