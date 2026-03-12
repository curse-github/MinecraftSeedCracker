/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ 
/*    */ public class TrunkVineDecorator
/*    */   extends TreeDecorator
/*    */ {
/* 11 */   protected TreeDecoratorType<?> type() { return TreeDecoratorType.TRUNK_VINE; }
/*    */ 
/*    */   
/* 14 */   public static final MapCodec<TrunkVineDecorator> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 16 */   public static final TrunkVineDecorator INSTANCE = new TrunkVineDecorator();
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 20 */     RandomSource random = context.random();
/* 21 */     context.logs().forEach(pos -> {
/* 22 */           if (random.nextInt(3) > 0) {
/* 23 */             BlockPos west = pos.west();
/* 24 */             if (context.isAir(west)) {
/* 25 */               context.placeVine(west, VineBlock.EAST);
/*    */             }
/*    */           } 
/*    */           
/* 29 */           if (random.nextInt(3) > 0) {
/* 30 */             BlockPos east = pos.east();
/* 31 */             if (context.isAir(east)) {
/* 32 */               context.placeVine(east, VineBlock.WEST);
/*    */             }
/*    */           } 
/*    */           
/* 36 */           if (random.nextInt(3) > 0) {
/* 37 */             BlockPos north = pos.north();
/* 38 */             if (context.isAir(north)) {
/* 39 */               context.placeVine(north, VineBlock.SOUTH);
/*    */             }
/*    */           } 
/*    */           
/* 43 */           if (random.nextInt(3) > 0) {
/* 44 */             BlockPos south = pos.south();
/* 45 */             if (context.isAir(south))
/* 46 */               context.placeVine(south, VineBlock.NORTH); 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TrunkVineDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */