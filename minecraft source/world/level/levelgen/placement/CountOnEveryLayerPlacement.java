/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.ConstantInt;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class CountOnEveryLayerPlacement
/*    */   extends PlacementModifier
/*    */ {
/* 20 */   public static final MapCodec<CountOnEveryLayerPlacement> CODEC = IntProvider.codec(0, 256).fieldOf("count")
/* 21 */     .xmap(CountOnEveryLayerPlacement::new, c -> c.count);
/*    */   
/*    */   private final IntProvider count;
/*    */ 
/*    */   
/* 26 */   private CountOnEveryLayerPlacement(IntProvider count) { this.count = count; }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static CountOnEveryLayerPlacement of(IntProvider count) { return new CountOnEveryLayerPlacement(count); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static CountOnEveryLayerPlacement of(int count) { return of(ConstantInt.of(count)); }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
/*    */     boolean foundAny;
/* 39 */     Stream.Builder<BlockPos> positions = Stream.builder();
/*    */     
/* 41 */     int layer = 0;
/*    */     do {
/* 43 */       foundAny = false;
/*    */       
/* 45 */       for (int i = 0; i < this.count.sample(random); i++) {
/* 46 */         int x = random.nextInt(16) + origin.getX();
/* 47 */         int z = random.nextInt(16) + origin.getZ();
/* 48 */         int startY = context.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
/* 49 */         int y = findOnGroundYPosition(context, x, startY, z, layer);
/* 50 */         if (y != Integer.MAX_VALUE) {
/* 51 */           positions.add(new BlockPos(x, y, z));
/* 52 */           foundAny = true;
/*    */         } 
/*    */       } 
/* 55 */       layer++;
/* 56 */     } while (foundAny);
/*    */     
/* 58 */     return positions.build();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 63 */   public PlacementModifierType<?> type() { return PlacementModifierType.COUNT_ON_EVERY_LAYER; }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int findOnGroundYPosition(PlacementContext context, int xStart, int yStart, int zStart, int layerToPlaceOn) {
/* 68 */     BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos(xStart, yStart, zStart);
/*    */     
/* 70 */     int currentLayer = 0;
/* 71 */     BlockState currentBlock = context.getBlockState(currentPos);
/* 72 */     for (int y = yStart; y >= context.getMinY() + 1; y--) {
/* 73 */       currentPos.setY(y - 1);
/* 74 */       BlockState belowBlock = context.getBlockState(currentPos);
/* 75 */       if (!isEmpty(belowBlock) && isEmpty(currentBlock) && !belowBlock.is(Blocks.BEDROCK)) {
/* 76 */         if (currentLayer == layerToPlaceOn) {
/* 77 */           return currentPos.getY() + 1;
/*    */         }
/* 79 */         currentLayer++;
/*    */       } 
/* 81 */       currentBlock = belowBlock;
/*    */     } 
/* 83 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */   
/* 87 */   private static boolean isEmpty(BlockState blockState) { return (blockState.isAir() || blockState.is(Blocks.WATER) || blockState.is(Blocks.LAVA)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\CountOnEveryLayerPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */