/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.TreeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class AlterGroundDecorator extends TreeDecorator {
/* 12 */   public static final MapCodec<AlterGroundDecorator> CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundDecorator::new, d -> d.provider);
/*    */   
/*    */   private final BlockStateProvider provider;
/*    */ 
/*    */   
/* 17 */   public AlterGroundDecorator(BlockStateProvider provider) { this.provider = provider; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected TreeDecoratorType<?> type() { return TreeDecoratorType.ALTER_GROUND; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 27 */     List<BlockPos> blockPositions = TreeFeature.getLowestTrunkOrRootOfTree(context);
/*    */     
/* 29 */     if (blockPositions.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     int minY = ((BlockPos)blockPositions.get(0)).getY();
/* 34 */     blockPositions.stream().filter(pos -> (pos.getY() == minY)).forEach(pos -> {
/* 35 */           placeCircle(context, pos.west().north());
/* 36 */           placeCircle(context, pos.east(2).north());
/* 37 */           placeCircle(context, pos.west().south(2));
/* 38 */           placeCircle(context, pos.east(2).south(2));
/*    */           
/* 40 */           for (int i = 0; i < 5; i++) {
/* 41 */             int placement = context.random().nextInt(64);
/* 42 */             int xx = placement % 8;
/* 43 */             int zz = placement / 8;
/* 44 */             if (xx == 0 || xx == 7 || zz == 0 || zz == 7) {
/* 45 */               placeCircle(context, pos.offset(-3 + xx, 0, -3 + zz));
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private void placeCircle(TreeDecorator.Context context, BlockPos pos) {
/* 52 */     for (int xx = -2; xx <= 2; xx++) {
/* 53 */       for (int zz = -2; zz <= 2; zz++) {
/* 54 */         if (Math.abs(xx) != 2 || Math.abs(zz) != 2) {
/* 55 */           placeBlockAt(context, pos.offset(xx, 0, zz));
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void placeBlockAt(TreeDecorator.Context context, BlockPos pos) {
/* 62 */     for (int dy = 2; dy >= -3; dy--) {
/* 63 */       BlockPos blockPos = pos.above(dy);
/* 64 */       if (Feature.isGrassOrDirt(context.level(), blockPos)) {
/* 65 */         context.setBlock(blockPos, this.provider.getState(context.random(), pos)); break;
/*    */       } 
/* 67 */       if (!context.isAir(blockPos) && dy < 0)
/*    */         break; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\AlterGroundDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */