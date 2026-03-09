/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StaticCache2D<T>
/*    */   extends Object
/*    */ {
/*    */   private final int minX;
/*    */   private final int minZ;
/*    */   private final int sizeX;
/*    */   private final int sizeZ;
/*    */   private final Object[] cache;
/*    */   
/*    */   public static <T> StaticCache2D<T> create(int centerX, int centerZ, int range, Initializer<T> initializer) {
/* 19 */     int minX = centerX - range;
/* 20 */     int minZ = centerZ - range;
/* 21 */     int size = 2 * range + 1;
/* 22 */     return new StaticCache2D(minX, minZ, size, size, initializer);
/*    */   }
/*    */   
/*    */   private StaticCache2D(int minX, int minZ, int sizeX, int sizeZ, Initializer<T> initializer) {
/* 26 */     this.minX = minX;
/* 27 */     this.minZ = minZ;
/* 28 */     this.sizeX = sizeX;
/* 29 */     this.sizeZ = sizeZ;
/* 30 */     this.cache = new Object[this.sizeX * this.sizeZ];
/* 31 */     for (int x = minX; x < minX + sizeX; x++) {
/* 32 */       for (int z = minZ; z < minZ + sizeZ; z++) {
/* 33 */         this.cache[getIndex(x, z)] = initializer.get(x, z);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<T> consumer) {
/* 40 */     for (Object o : this.cache) {
/* 41 */       consumer.accept(o);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(int x, int z) {
/* 47 */     if (!contains(x, z)) {
/* 48 */       throw new IllegalArgumentException("Requested out of range value (" + x + "," + z + ") from " + String.valueOf(this));
/*    */     }
/* 50 */     return (T)this.cache[getIndex(x, z)];
/*    */   }
/*    */   
/*    */   public boolean contains(int x, int z) {
/* 54 */     int deltaX = x - this.minX;
/* 55 */     int deltaZ = z - this.minZ;
/* 56 */     return (deltaX >= 0 && deltaX < this.sizeX && deltaZ >= 0 && deltaZ < this.sizeZ);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public String toString() { return String.format(Locale.ROOT, "StaticCache2D[%d, %d, %d, %d]", new Object[] { Integer.valueOf(this.minX), Integer.valueOf(this.minZ), Integer.valueOf(this.minX + this.sizeX), Integer.valueOf(this.minZ + this.sizeZ) }); }
/*    */ 
/*    */   
/*    */   private int getIndex(int x, int z) {
/* 66 */     int deltaX = x - this.minX;
/* 67 */     int deltaZ = z - this.minZ;
/* 68 */     return deltaX * this.sizeZ + deltaZ;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Initializer<T> {
/*    */     T get(int param1Int1, int param1Int2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\StaticCache2D.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */