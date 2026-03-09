/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ public abstract class RepeatingPlacement
/*    */   extends PlacementModifier
/*    */ {
/*    */   protected abstract int count(RandomSource paramRandomSource, BlockPos paramBlockPos);
/*    */   
/* 14 */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) { return IntStream.range(0, count(random, origin)).mapToObj(i -> origin); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\RepeatingPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */