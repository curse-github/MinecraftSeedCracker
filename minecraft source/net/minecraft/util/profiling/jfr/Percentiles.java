/*    */ package net.minecraft.util.profiling.jfr;
/*    */ 
/*    */ import com.google.common.math.Quantiles;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleRBTreeMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleSortedMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2DoubleSortedMaps;
/*    */ import java.util.Comparator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Percentiles
/*    */ {
/* 13 */   public static final Quantiles.ScaleAndIndexes DEFAULT_INDEXES = Quantiles.scale(100).indexes(new int[] { 50, 75, 90, 99 });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static Map<Integer, Double> evaluate(long[] dataset) { return (dataset.length == 0) ? Map.of() : sorted(DEFAULT_INDEXES.compute(dataset)); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static Map<Integer, Double> evaluate(int[] dataset) { return (dataset.length == 0) ? Map.of() : sorted(DEFAULT_INDEXES.compute(dataset)); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static Map<Integer, Double> evaluate(double[] dataset) { return (dataset.length == 0) ? Map.of() : sorted(DEFAULT_INDEXES.compute(dataset)); }
/*    */ 
/*    */   
/*    */   private static Map<Integer, Double> sorted(Map<Integer, Double> percentiles) {
/* 31 */     Int2DoubleSortedMap sorted = (Int2DoubleSortedMap)Util.make(new Int2DoubleRBTreeMap(Comparator.reverseOrder()), it -> it.putAll(percentiles));
/* 32 */     return Int2DoubleSortedMaps.unmodifiable(sorted);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\jfr\Percentiles.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */