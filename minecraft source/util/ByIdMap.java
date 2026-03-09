/*    */ package net.minecraft.util;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import java.util.function.IntFunction;
/*    */ import java.util.function.ToIntFunction;
/*    */ 
/*    */ public class ByIdMap
/*    */ {
/*    */   public enum OutOfBoundsStrategy
/*    */   {
/* 13 */     ZERO,
/* 14 */     WRAP,
/* 15 */     CLAMP;
/*    */   }
/*    */   
/*    */   private static <T> IntFunction<T> createMap(ToIntFunction<T> idGetter, T[] values) {
/* 19 */     if (values.length == 0) {
/* 20 */       throw new IllegalArgumentException("Empty value list");
/*    */     }
/*    */     
/* 23 */     Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 24 */     for (T value : values) {
/* 25 */       int id = idGetter.applyAsInt(value);
/* 26 */       T previous = (T)int2ObjectOpenHashMap.put(id, value);
/* 27 */       if (previous != null) {
/* 28 */         throw new IllegalArgumentException("Duplicate entry on id " + id + ": current=" + String.valueOf(value) + ", previous=" + String.valueOf(previous));
/*    */       }
/*    */     } 
/* 31 */     return int2ObjectOpenHashMap;
/*    */   }
/*    */   
/*    */   public static <T> IntFunction<T> sparse(ToIntFunction<T> idGetter, T[] values, T _default) {
/* 35 */     IntFunction<T> idToObject = createMap(idGetter, values);
/* 36 */     return id -> Objects.requireNonNullElse(idToObject.apply(id), _default);
/*    */   }
/*    */   
/*    */   private static <T> T[] createSortedArray(ToIntFunction<T> idGetter, T[] values) {
/* 40 */     int length = values.length;
/* 41 */     if (length == 0) {
/* 42 */       throw new IllegalArgumentException("Empty value list");
/*    */     }
/*    */     
/* 45 */     T[] result = (T[])(Object[])values.clone();
/* 46 */     Arrays.fill(result, null);
/*    */     
/* 48 */     for (T value : values) {
/* 49 */       int id = idGetter.applyAsInt(value);
/* 50 */       if (id < 0 || id >= length) {
/* 51 */         throw new IllegalArgumentException("Values are not continous, found index " + id + " for value " + String.valueOf(value));
/*    */       }
/* 53 */       T previous = result[id];
/* 54 */       if (previous != null) {
/* 55 */         throw new IllegalArgumentException("Duplicate entry on id " + id + ": current=" + String.valueOf(value) + ", previous=" + String.valueOf(previous));
/*    */       }
/* 57 */       result[id] = value;
/*    */     } 
/*    */     
/* 60 */     for (int i = 0; i < length; i++) {
/* 61 */       if (result[i] == null) {
/* 62 */         throw new IllegalArgumentException("Missing value at index: " + i);
/*    */       }
/*    */     } 
/*    */     
/* 66 */     return result;
/*    */   }
/*    */   
/*    */   public static <T> IntFunction<T> continuous(ToIntFunction<T> idGetter, T[] values, OutOfBoundsStrategy strategy) {
/* 70 */     T zeroValue, sortedValues[] = (T[])createSortedArray(idGetter, values);
/* 71 */     int length = sortedValues.length;
/* 72 */     switch (strategy.ordinal()) { default: throw new MatchException(null, null);
/*    */       case 0:
/* 74 */         zeroValue = sortedValues[0];
/*    */       case 1:
/*    */       
/*    */       case 2:
/* 78 */         break; }  return id -> sortedValues[Mth.clamp(id, 0, length - 1)];
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ByIdMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */