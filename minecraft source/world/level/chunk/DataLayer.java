/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataLayer
/*     */ {
/*     */   public static final int LAYER_COUNT = 16;
/*     */   public static final int LAYER_SIZE = 128;
/*     */   public static final int SIZE = 2048;
/*     */   private static final int NIBBLE_SIZE = 4;
/*     */   protected byte[] data;
/*     */   private int defaultValue;
/*     */   
/*  21 */   public DataLayer() { this(0); }
/*     */ 
/*     */ 
/*     */   
/*  25 */   public DataLayer(int defaultValue) { this.defaultValue = defaultValue; }
/*     */ 
/*     */   
/*     */   public DataLayer(byte[] data) {
/*  29 */     this.data = data;
/*  30 */     this.defaultValue = 0;
/*     */     
/*  32 */     if (data.length != 2048) {
/*  33 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("DataLayer should be 2048 bytes not: " + data.length));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  38 */   public int get(int x, int y, int z) { return get(getIndex(x, y, z)); }
/*     */ 
/*     */ 
/*     */   
/*  42 */   public void set(int x, int y, int z, int val) { set(getIndex(x, y, z), val); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   private static int getIndex(int x, int y, int z) { return y << 8 | z << 4 | x; }
/*     */ 
/*     */   
/*     */   private int get(int index) {
/*  50 */     if (this.data == null) {
/*  51 */       return this.defaultValue;
/*     */     }
/*  53 */     int position = getByteIndex(index);
/*  54 */     int nibble = getNibbleIndex(index);
/*  55 */     return this.data[position] >> 4 * nibble & 0xF;
/*     */   }
/*     */   
/*     */   private void set(int index, int val) {
/*  59 */     byte[] data = getData();
/*  60 */     int position = getByteIndex(index);
/*  61 */     int nibble = getNibbleIndex(index);
/*     */     
/*  63 */     int mask = 15 << 4 * nibble ^ 0xFFFFFFFF;
/*  64 */     int valueToSet = (val & 0xF) << 4 * nibble;
/*  65 */     data[position] = (byte)(data[position] & mask | valueToSet);
/*     */   }
/*     */ 
/*     */   
/*  69 */   private static int getNibbleIndex(int index) { return index & true; }
/*     */ 
/*     */ 
/*     */   
/*  73 */   private static int getByteIndex(int position) { return position >> 1; }
/*     */ 
/*     */   
/*     */   public void fill(int value) {
/*  77 */     this.defaultValue = value;
/*  78 */     this.data = null;
/*     */   }
/*     */   
/*     */   private static byte packFilled(int value) {
/*  82 */     byte packed = (byte)value;
/*  83 */     for (int i = 4; i < 8; i += 4) {
/*  84 */       packed = (byte)(packed | value << i);
/*     */     }
/*  86 */     return packed;
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/*  90 */     if (this.data == null) {
/*  91 */       this.data = new byte[2048];
/*  92 */       if (this.defaultValue != 0) {
/*  93 */         Arrays.fill(this.data, packFilled(this.defaultValue));
/*     */       }
/*     */     } 
/*  96 */     return this.data;
/*     */   }
/*     */   
/*     */   public DataLayer copy() {
/* 100 */     if (this.data == null) {
/* 101 */       return new DataLayer(this.defaultValue);
/*     */     }
/* 103 */     return new DataLayer((byte[])this.data.clone());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     StringBuilder builder = new StringBuilder();
/* 109 */     for (int i = 0; i < 4096; i++) {
/* 110 */       builder.append(Integer.toHexString(get(i)));
/* 111 */       if ((i & 0xF) == 15) {
/* 112 */         builder.append("\n");
/*     */       }
/* 114 */       if ((i & 0xFF) == 255) {
/* 115 */         builder.append("\n");
/*     */       }
/*     */     } 
/* 118 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public String layerToString(int layer) {
/* 124 */     StringBuilder builder = new StringBuilder();
/* 125 */     for (int i = 0; i < 256; i++) {
/* 126 */       builder.append(Integer.toHexString(get(i)));
/* 127 */       if ((i & 0xF) == 15) {
/* 128 */         builder.append("\n");
/*     */       }
/*     */     } 
/* 131 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/* 135 */   public boolean isDefinitelyHomogenous() { return (this.data == null); }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public boolean isDefinitelyFilledWith(int value) { return (this.data == null && this.defaultValue == value); }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public boolean isEmpty() { return (this.data == null && this.defaultValue == 0); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\DataLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */