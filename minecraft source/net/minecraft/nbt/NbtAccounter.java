/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ 
/*    */ 
/*    */ public class NbtAccounter
/*    */ {
/*    */   public static final int DEFAULT_NBT_QUOTA = 2097152;
/*    */   public static final int UNCOMPRESSED_NBT_QUOTA = 104857600;
/*    */   private static final int MAX_STACK_DEPTH = 512;
/*    */   private final long quota;
/*    */   private long usage;
/*    */   private final int maxDepth;
/*    */   private int depth;
/*    */   
/*    */   public NbtAccounter(long quota, int maxDepth) {
/* 17 */     this.quota = quota;
/* 18 */     this.maxDepth = maxDepth;
/*    */   }
/*    */ 
/*    */   
/* 22 */   public static NbtAccounter create(long quota) { return new NbtAccounter(quota, 512); }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static NbtAccounter defaultQuota() { return new NbtAccounter(2097152L, 512); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static NbtAccounter uncompressedQuota() { return new NbtAccounter(104857600L, 512); }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public static NbtAccounter unlimitedHeap() { return new NbtAccounter(Float.MAX_VALUE, 512); }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public void accountBytes(long bytesPerEntry, long count) { accountBytes(bytesPerEntry * count); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void accountBytes(long size) {
/* 43 */     if (size < 0L) {
/* 44 */       throw new IllegalArgumentException("Tried to account NBT tag with negative size: " + size);
/*    */     }
/* 46 */     if (this.usage + size > this.quota) {
/* 47 */       throw new NbtAccounterException("Tried to read NBT tag that was too big; tried to allocate: " + this.usage + " + " + size + " bytes where max allowed: " + this.quota);
/*    */     }
/* 49 */     this.usage += size;
/*    */   }
/*    */   
/*    */   public void pushDepth() {
/* 53 */     if (this.depth >= this.maxDepth) {
/* 54 */       throw new NbtAccounterException("Tried to read NBT tag with too high complexity, depth > " + this.maxDepth);
/*    */     }
/* 56 */     this.depth++;
/*    */   }
/*    */   
/*    */   public void popDepth() {
/* 60 */     if (this.depth <= 0) {
/* 61 */       throw new NbtAccounterException("NBT-Accounter tried to pop stack-depth at top-level");
/*    */     }
/* 63 */     this.depth--;
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 68 */   public long getUsage() { return this.usage; }
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 73 */   public int getDepth() { return this.depth; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\NbtAccounter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */