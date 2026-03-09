/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import java.util.BitSet;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class RegionBitmap {
/* 10 */   private final BitSet used = new BitSet();
/*    */ 
/*    */   
/* 13 */   public void force(int position, int size) { this.used.set(position, position + size); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public void free(int position, int size) { this.used.clear(position, position + size); }
/*    */ 
/*    */   
/*    */   public int allocate(int size) {
/* 21 */     int current = 0;
/*    */     while (true) {
/* 23 */       int freeStart = this.used.nextClearBit(current);
/* 24 */       int freeEnd = this.used.nextSetBit(freeStart);
/* 25 */       if (freeEnd == -1 || freeEnd - freeStart >= size) {
/* 26 */         force(freeStart, size);
/* 27 */         return freeStart;
/*    */       } 
/* 29 */       current = freeEnd;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 35 */   public IntSet getUsed() { return (IntSet)this.used.stream().collect(it.unimi.dsi.fastutil.ints.IntArraySet::new, IntCollection::add, IntCollection::addAll); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionBitmap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */