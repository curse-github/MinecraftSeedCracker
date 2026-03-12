/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Optionull
/*    */ {
/*    */   @Deprecated
/* 17 */   public static <T> T orElse(T t, T defaultValue) { return (T)Objects.requireNonNullElse(t, defaultValue); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static <T, R> R map(T t, Function<T, R> map) { return (R)((t == null) ? null : map.apply(t)); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static <T, R> R mapOrDefault(T t, Function<T, R> map, R defaultValue) { return (t == null) ? defaultValue : map.apply(t); }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static <T, R> R mapOrElse(T t, Function<T, R> map, Supplier<R> elseSupplier) { return (R)((t == null) ? elseSupplier.get() : map.apply(t)); }
/*    */ 
/*    */   
/*    */   public static <T> T first(Collection<T> collection) {
/* 33 */     Iterator<T> iterator = collection.iterator();
/* 34 */     return (T)(iterator.hasNext() ? iterator.next() : null);
/*    */   }
/*    */   
/*    */   public static <T> T firstOrDefault(Collection<T> collection, T defaultValue) {
/* 38 */     Iterator<T> iterator = collection.iterator();
/* 39 */     return (T)(iterator.hasNext() ? iterator.next() : defaultValue);
/*    */   }
/*    */   
/*    */   public static <T> T firstOrElse(Collection<T> collection, Supplier<T> elseSupplier) {
/* 43 */     Iterator<T> iterator = collection.iterator();
/* 44 */     return (T)(iterator.hasNext() ? iterator.next() : elseSupplier.get());
/*    */   }
/*    */ 
/*    */   
/* 48 */   public static <T> boolean isNullOrEmpty(T[] t) { return (t == null || t.length == 0); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public static boolean isNullOrEmpty(boolean[] t) { return (t == null || t.length == 0); }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public static boolean isNullOrEmpty(byte[] t) { return (t == null || t.length == 0); }
/*    */ 
/*    */ 
/*    */   
/* 60 */   public static boolean isNullOrEmpty(char[] t) { return (t == null || t.length == 0); }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public static boolean isNullOrEmpty(short[] t) { return (t == null || t.length == 0); }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public static boolean isNullOrEmpty(int[] t) { return (t == null || t.length == 0); }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public static boolean isNullOrEmpty(long[] t) { return (t == null || t.length == 0); }
/*    */ 
/*    */ 
/*    */   
/* 76 */   public static boolean isNullOrEmpty(float[] t) { return (t == null || t.length == 0); }
/*    */ 
/*    */ 
/*    */   
/* 80 */   public static boolean isNullOrEmpty(double[] t) { return (t == null || t.length == 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\Optionull.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */