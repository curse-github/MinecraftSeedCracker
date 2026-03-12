/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.CocoaBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CocoaDecorator extends TreeDecorator {
/* 14 */   public static final MapCodec<CocoaDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CocoaDecorator::new, d -> Float.valueOf(d.probability));
/*    */   
/*    */   private final float probability;
/*    */ 
/*    */   
/* 19 */   public CocoaDecorator(float probability) { this.probability = probability; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   protected TreeDecoratorType<?> type() { return TreeDecoratorType.COCOA; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 29 */     RandomSource random = context.random();
/* 30 */     if (random.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     ObjectArrayList objectArrayList = context.logs();
/* 35 */     if (objectArrayList.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 39 */     int treeY = ((BlockPos)objectArrayList.getFirst()).getY();
/* 40 */     objectArrayList.stream()
/* 41 */       .filter(pos -> (pos.getY() - treeY <= 2))
/* 42 */       .forEach(pos -> {
/* 43 */           for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 44 */             if (random.nextFloat() <= 0.25F) {
/* 45 */               Direction opposite = direction.getOpposite();
/* 46 */               BlockPos cocoaPos = pos.offset(opposite.getStepX(), 0, opposite.getStepZ());
/* 47 */               if (context.isAir(cocoaPos))
/* 48 */                 context.setBlock(cocoaPos, (BlockState)((BlockState)Blocks.COCOA.defaultBlockState().setValue(CocoaBlock.AGE, Integer.valueOf(random.nextInt(3)))).setValue(CocoaBlock.FACING, direction)); 
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\CocoaDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */