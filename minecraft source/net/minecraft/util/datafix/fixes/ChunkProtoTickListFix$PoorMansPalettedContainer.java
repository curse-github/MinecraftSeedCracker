/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
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
/*     */ 
/*     */ public final class PoorMansPalettedContainer
/*     */ {
/*     */   private static final long SIZE_BITS = 4L;
/*     */   private final List<? extends Dynamic<?>> palette;
/*     */   private final long[] data;
/*     */   private final int bits;
/*     */   private final long mask;
/*     */   private final int valuesPerLong;
/*     */   
/*     */   public PoorMansPalettedContainer(List<? extends Dynamic<?>> palette, long[] data) {
/* 168 */     this.palette = palette;
/* 169 */     this.data = data;
/*     */     
/* 171 */     this.bits = Math.max(4, ChunkHeightAndBiomeFix.ceillog2(palette.size()));
/* 172 */     this.mask = (1L << this.bits) - 1L;
/* 173 */     this.valuesPerLong = (char)(64 / this.bits);
/*     */   }
/*     */   
/*     */   public Dynamic<?> get(int x, int y, int z) {
/* 177 */     int entryCount = this.palette.size();
/* 178 */     if (entryCount < 1) {
/* 179 */       return null;
/*     */     }
/* 181 */     if (entryCount == 1) {
/* 182 */       return (Dynamic)this.palette.getFirst();
/*     */     }
/*     */     
/* 185 */     int index = getIndex(x, y, z);
/* 186 */     int cellIndex = index / this.valuesPerLong;
/* 187 */     if (cellIndex < 0 || cellIndex >= this.data.length) {
/* 188 */       return null;
/*     */     }
/* 190 */     long cellValue = this.data[cellIndex];
/* 191 */     int bitIndex = (index - cellIndex * this.valuesPerLong) * this.bits;
/* 192 */     int paletteIndex = (int)(cellValue >> bitIndex & this.mask);
/* 193 */     if (paletteIndex < 0 || paletteIndex >= entryCount) {
/* 194 */       return null;
/*     */     }
/* 196 */     return (Dynamic)this.palette.get(paletteIndex);
/*     */   }
/*     */ 
/*     */   
/* 200 */   private int getIndex(int x, int y, int z) { return (y << 4 | z) << 4 | x; }
/*     */ 
/*     */ 
/*     */   
/* 204 */   public List<? extends Dynamic<?>> palette() { return this.palette; }
/*     */ 
/*     */ 
/*     */   
/* 208 */   public long[] data() { return this.data; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkProtoTickListFix$PoorMansPalettedContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */