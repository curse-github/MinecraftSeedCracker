/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public class CarvingMask
/*    */ {
/*    */   private final int minY;
/*    */   
/*    */   public CarvingMask(int height, int minY) {
/* 13 */     this.additionalMask = ((x, y, z) -> false);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 20 */     this.minY = minY;
/* 21 */     this.mask = new BitSet(256 * height);
/*    */   }
/*    */   private final BitSet mask; private Mask additionalMask;
/*    */   
/* 25 */   public void setAdditionalMask(Mask additionalMask) { this.additionalMask = additionalMask; }
/*    */   
/*    */   public CarvingMask(long[] array, int minY) {
/*    */     this.additionalMask = ((x, y, z) -> false);
/* 29 */     this.minY = minY;
/* 30 */     this.mask = BitSet.valueOf(array);
/*    */   }
/*    */ 
/*    */   
/* 34 */   private int getIndex(int x, int y, int z) { return x & 0xF | (z & 0xF) << 4 | y - this.minY << 8; }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public void set(int x, int y, int z) { this.mask.set(getIndex(x, y, z)); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public boolean get(int x, int y, int z) { return (this.additionalMask.test(x, y, z) || this.mask.get(getIndex(x, y, z))); }
/*    */ 
/*    */   
/*    */   public Stream<BlockPos> stream(ChunkPos pos) {
/* 46 */     return this.mask.stream().mapToObj(i -> {
/* 47 */           int x = i & 0xF;
/* 48 */           int z = i >> 4 & 0xF;
/* 49 */           int y = i >> 8;
/* 50 */           return pos.getBlockAt(x, y + this.minY, z);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 55 */   public long[] toArray() { return this.mask.toLongArray(); }
/*    */   
/*    */   public static interface Mask {
/*    */     boolean test(int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\CarvingMask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */