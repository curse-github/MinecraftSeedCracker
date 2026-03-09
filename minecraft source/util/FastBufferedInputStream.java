/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FastBufferedInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*    */   private final InputStream in;
/*    */   private final byte[] buffer;
/*    */   private int limit;
/*    */   private int position;
/*    */   
/* 21 */   public FastBufferedInputStream(InputStream in) { this(in, 8192); }
/*    */ 
/*    */   
/*    */   public FastBufferedInputStream(InputStream in, int bufferSize) {
/* 25 */     this.in = in;
/* 26 */     this.buffer = new byte[bufferSize];
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 31 */     if (this.position >= this.limit) {
/* 32 */       fill();
/* 33 */       if (this.position >= this.limit) {
/* 34 */         return -1;
/*    */       }
/*    */     } 
/* 37 */     return Byte.toUnsignedInt(this.buffer[this.position++]);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] output, int offset, int length) throws IOException {
/* 42 */     int availableInBuffer = bytesInBuffer();
/* 43 */     if (availableInBuffer <= 0) {
/* 44 */       if (length >= this.buffer.length) {
/* 45 */         return this.in.read(output, offset, length);
/*    */       }
/* 47 */       fill();
/* 48 */       availableInBuffer = bytesInBuffer();
/* 49 */       if (availableInBuffer <= 0) {
/* 50 */         return -1;
/*    */       }
/*    */     } 
/* 53 */     if (length > availableInBuffer) {
/* 54 */       length = availableInBuffer;
/*    */     }
/* 56 */     System.arraycopy(this.buffer, this.position, output, offset, length);
/* 57 */     this.position += length;
/* 58 */     return length;
/*    */   }
/*    */ 
/*    */   
/*    */   public long skip(long count) throws IOException {
/* 63 */     if (count <= 0L) {
/* 64 */       return 0L;
/*    */     }
/* 66 */     long availableInBuffer = bytesInBuffer();
/* 67 */     if (availableInBuffer <= 0L) {
/* 68 */       return this.in.skip(count);
/*    */     }
/* 70 */     if (count > availableInBuffer) {
/* 71 */       count = availableInBuffer;
/*    */     }
/* 73 */     this.position = (int)(this.position + count);
/* 74 */     return count;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public int available() throws IOException { return bytesInBuffer() + this.in.available(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 84 */   public void close() throws IOException { this.in.close(); }
/*    */ 
/*    */ 
/*    */   
/* 88 */   private int bytesInBuffer() throws IOException { return this.limit - this.position; }
/*    */ 
/*    */   
/*    */   private void fill() throws IOException {
/* 92 */     this.limit = 0;
/* 93 */     this.position = 0;
/* 94 */     int actuallyRead = this.in.read(this.buffer, 0, this.buffer.length);
/* 95 */     if (actuallyRead > 0)
/* 96 */       this.limit = actuallyRead; 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\FastBufferedInputStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */