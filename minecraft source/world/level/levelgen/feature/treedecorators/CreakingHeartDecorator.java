/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class CreakingHeartDecorator extends TreeDecorator {
/* 19 */   public static final MapCodec<CreakingHeartDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CreakingHeartDecorator::new, d -> Float.valueOf(d.probability));
/*    */   
/*    */   private final float probability;
/*    */ 
/*    */   
/* 24 */   public CreakingHeartDecorator(float probability) { this.probability = probability; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected TreeDecoratorType<?> type() { return TreeDecoratorType.CREAKING_HEART; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 34 */     RandomSource random = context.random();
/*    */     
/* 36 */     ObjectArrayList objectArrayList = context.logs();
/* 37 */     if (objectArrayList.isEmpty()) {
/*    */       return;
/*    */     }
/* 40 */     if (random.nextFloat() >= this.probability) {
/*    */       return;
/*    */     }
/*    */     
/* 44 */     List<BlockPos> heartPlacements = new ArrayList<BlockPos>(objectArrayList);
/* 45 */     Util.shuffle(heartPlacements, random);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     Optional<BlockPos> targetPos = heartPlacements.stream().filter(pos -> { for (Direction dir : Direction.values()) { if (!context.checkBlock(pos.relative(dir), ())) return false;  }  return true; }).findFirst();
/* 56 */     if (targetPos.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 60 */     context.setBlock((BlockPos)targetPos.get(), (BlockState)((BlockState)Blocks.CREAKING_HEART.defaultBlockState().setValue(CreakingHeartBlock.STATE, CreakingHeartState.DORMANT)).setValue(CreakingHeartBlock.NATURAL, Boolean.valueOf(true)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\CreakingHeartDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */