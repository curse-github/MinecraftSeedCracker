/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class BlockUtil
/*     */ {
/*     */   public static class IntBounds
/*     */   {
/*     */     public final int min;
/*     */     public final int max;
/*     */     
/*     */     public IntBounds(int min, int max) {
/*  22 */       this.min = min;
/*  23 */       this.max = max;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  28 */     public String toString() { return "IntBounds{min=" + this.min + ", max=" + this.max + "}"; }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class FoundRectangle
/*     */   {
/*     */     public final BlockPos minCorner;
/*     */     
/*     */     public final int axis1Size;
/*     */     
/*     */     public final int axis2Size;
/*     */     
/*     */     public FoundRectangle(BlockPos minCorner, int axis1Size, int axis2Size) {
/*  41 */       this.minCorner = minCorner;
/*  42 */       this.axis1Size = axis1Size;
/*  43 */       this.axis2Size = axis2Size;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FoundRectangle getLargestRectangleAround(BlockPos center, Direction.Axis axis1, int limit1, Direction.Axis axis2, int limit2, Predicate<BlockPos> test) {
/*  61 */     BlockPos.MutableBlockPos pos = center.mutable();
/*     */     
/*  63 */     Direction negativeDirection1 = Direction.get(Direction.AxisDirection.NEGATIVE, axis1);
/*  64 */     Direction positiveDirection1 = negativeDirection1.getOpposite();
/*     */     
/*  66 */     Direction negativeDirection2 = Direction.get(Direction.AxisDirection.NEGATIVE, axis2);
/*  67 */     Direction positiveDirection2 = negativeDirection2.getOpposite();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     int negativeDelta1 = getLimit(test, pos.set(center), negativeDirection1, limit1);
/*  84 */     int positiveDelta1 = getLimit(test, pos.set(center), positiveDirection1, limit1);
/*     */     
/*  86 */     int centerIndex1 = negativeDelta1;
/*  87 */     IntBounds[] arrayOfIntBounds = new IntBounds[centerIndex1 + 1 + positiveDelta1];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     arrayOfIntBounds[centerIndex1] = new IntBounds(
/* 104 */         getLimit(test, pos.set(center), negativeDirection2, limit2), 
/* 105 */         getLimit(test, pos.set(center), positiveDirection2, limit2));
/*     */ 
/*     */     
/* 108 */     int centerIndex2 = (arrayOfIntBounds[centerIndex1]).min;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     for (int i = 1; i <= negativeDelta1; i++) {
/* 129 */       IntBounds lastBounds = arrayOfIntBounds[centerIndex1 - i - 1];
/* 130 */       arrayOfIntBounds[centerIndex1 - i] = new IntBounds(
/* 131 */           getLimit(test, pos.set(center).move(negativeDirection1, i), negativeDirection2, lastBounds.min), 
/* 132 */           getLimit(test, pos.set(center).move(negativeDirection1, i), positiveDirection2, lastBounds.max));
/*     */     } 
/*     */ 
/*     */     
/* 136 */     for (int i = 1; i <= positiveDelta1; i++) {
/* 137 */       IntBounds lastBounds = arrayOfIntBounds[centerIndex1 + i - 1];
/* 138 */       arrayOfIntBounds[centerIndex1 + i] = new IntBounds(
/* 139 */           getLimit(test, pos.set(center).move(positiveDirection1, i), negativeDirection2, lastBounds.min), 
/* 140 */           getLimit(test, pos.set(center).move(positiveDirection1, i), positiveDirection2, lastBounds.max));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     int minAxis1 = 0;
/* 159 */     int minAxis2 = 0;
/* 160 */     int sizeAxis1 = 0;
/* 161 */     int sizeAxis2 = 0;
/*     */     
/* 163 */     int[] columns = new int[arrayOfIntBounds.length];
/*     */     
/* 165 */     for (int i2 = centerIndex2; i2 >= 0; i2--) {
/* 166 */       for (int i1 = 0; i1 < arrayOfIntBounds.length; i1++) {
/* 167 */         IntBounds bounds2 = arrayOfIntBounds[i1];
/* 168 */         int min2 = centerIndex2 - bounds2.min;
/* 169 */         int max2 = centerIndex2 + bounds2.max;
/*     */         
/* 171 */         columns[i1] = (i2 >= min2 && i2 <= max2) ? (max2 + 1 - i2) : 0;
/*     */       } 
/*     */       
/* 174 */       Pair<IntBounds, Integer> rectangle = getMaxRectangleLocation(columns);
/* 175 */       IntBounds boundsAxis1 = (IntBounds)rectangle.getFirst();
/* 176 */       int newSizeAxis1 = 1 + boundsAxis1.max - boundsAxis1.min;
/* 177 */       int newSizeAxis2 = ((Integer)rectangle.getSecond()).intValue();
/*     */       
/* 179 */       if (newSizeAxis1 * newSizeAxis2 > sizeAxis1 * sizeAxis2) {
/* 180 */         minAxis1 = boundsAxis1.min;
/* 181 */         minAxis2 = i2;
/* 182 */         sizeAxis1 = newSizeAxis1;
/* 183 */         sizeAxis2 = newSizeAxis2;
/*     */       } 
/*     */     } 
/*     */     
/* 187 */     return new FoundRectangle(center
/* 188 */         .relative(axis1, minAxis1 - centerIndex1).relative(axis2, minAxis2 - centerIndex2), sizeAxis1, sizeAxis2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getLimit(Predicate<BlockPos> test, BlockPos.MutableBlockPos pos, Direction direction, int limit) {
/* 195 */     int max = 0;
/* 196 */     while (max < limit && test.test(pos.move(direction))) {
/* 197 */       max++;
/*     */     }
/* 199 */     return max;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static Pair<IntBounds, Integer> getMaxRectangleLocation(int[] columns) {
/* 204 */     int maxStart = 0;
/* 205 */     int maxEnd = 0;
/* 206 */     int maxHeight = 0;
/*     */     
/* 208 */     IntArrayList intArrayList = new IntArrayList();
/* 209 */     intArrayList.push(0);
/* 210 */     for (int column = 1; column <= columns.length; column++) {
/* 211 */       int height = (column == columns.length) ? 0 : columns[column];
/* 212 */       while (!intArrayList.isEmpty()) {
/* 213 */         int stackHeight = columns[intArrayList.topInt()];
/* 214 */         if (height >= stackHeight) {
/* 215 */           intArrayList.push(column);
/*     */           
/*     */           break;
/*     */         } 
/* 219 */         intArrayList.popInt();
/* 220 */         int start = intArrayList.isEmpty() ? 0 : (intArrayList.topInt() + 1);
/*     */         
/* 222 */         if (stackHeight * (column - start) > maxHeight * (maxEnd - maxStart)) {
/* 223 */           maxEnd = column;
/* 224 */           maxStart = start;
/* 225 */           maxHeight = stackHeight;
/*     */         } 
/*     */       } 
/*     */       
/* 229 */       if (intArrayList.isEmpty()) {
/* 230 */         intArrayList.push(column);
/*     */       }
/*     */     } 
/*     */     
/* 234 */     return new Pair(new IntBounds(maxStart, maxEnd - 1), Integer.valueOf(maxHeight));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<BlockPos> getTopConnectedBlock(BlockGetter level, BlockPos pos, Block bodyBlock, Direction growthDirection, Block headBlock) {
/*     */     BlockState forwardState;
/* 243 */     BlockPos.MutableBlockPos forwardPos = pos.mutable();
/*     */     
/*     */     do {
/* 246 */       forwardPos.move(growthDirection);
/* 247 */       forwardState = level.getBlockState(forwardPos);
/* 248 */     } while (forwardState.is(bodyBlock));
/*     */     
/* 250 */     if (forwardState.is(headBlock)) {
/* 251 */       return Optional.of(forwardPos);
/*     */     }
/* 253 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\BlockUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */