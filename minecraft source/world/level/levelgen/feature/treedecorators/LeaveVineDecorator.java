/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ 
/*    */ public class LeaveVineDecorator
/*    */   extends TreeDecorator {
/* 13 */   protected TreeDecoratorType<?> type() { return TreeDecoratorType.LEAVE_VINE; }
/*    */ 
/*    */   
/* 16 */   public static final MapCodec<LeaveVineDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(LeaveVineDecorator::new, d -> Float.valueOf(d.probability));
/*    */   
/*    */   private final float probability;
/*    */ 
/*    */   
/* 21 */   public LeaveVineDecorator(float probability) { this.probability = probability; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void place(TreeDecorator.Context context) {
/* 26 */     RandomSource random = context.random();
/* 27 */     context.leaves().forEach(pos -> {
/* 28 */           if (random.nextFloat() < this.probability) {
/* 29 */             BlockPos west = pos.west();
/* 30 */             if (context.isAir(west)) {
/* 31 */               addHangingVine(west, VineBlock.EAST, context);
/*    */             }
/*    */           } 
/*    */           
/* 35 */           if (random.nextFloat() < this.probability) {
/* 36 */             BlockPos east = pos.east();
/* 37 */             if (context.isAir(east)) {
/* 38 */               addHangingVine(east, VineBlock.WEST, context);
/*    */             }
/*    */           } 
/*    */           
/* 42 */           if (random.nextFloat() < this.probability) {
/* 43 */             BlockPos north = pos.north();
/* 44 */             if (context.isAir(north)) {
/* 45 */               addHangingVine(north, VineBlock.SOUTH, context);
/*    */             }
/*    */           } 
/*    */           
/* 49 */           if (random.nextFloat() < this.probability) {
/* 50 */             BlockPos south = pos.south();
/* 51 */             if (context.isAir(south)) {
/* 52 */               addHangingVine(south, VineBlock.NORTH, context);
/*    */             }
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   private static void addHangingVine(BlockPos pos, BooleanProperty direction, TreeDecorator.Context context) {
/* 59 */     context.placeVine(pos, direction);
/* 60 */     int maxDir = 4;
/*    */     
/* 62 */     pos = pos.below();
/* 63 */     while (context.isAir(pos) && maxDir > 0) {
/* 64 */       context.placeVine(pos, direction);
/* 65 */       pos = pos.below();
/* 66 */       maxDir--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\LeaveVineDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */