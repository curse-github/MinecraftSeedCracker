/*    */ package net.minecraft.util;
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
/*    */ 
/*    */ public final class PngInfo extends Record {
/*    */   private final int width;
/*    */   private final int height;
/*    */   
/* 11 */   public PngInfo(int width, int height) { this.width = width; this.height = height; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/PngInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/util/PngInfo; } public int width() { return this.width; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/PngInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/PngInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/PngInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/PngInfo;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; }
/* 12 */   private static final HexFormat FORMAT = HexFormat.of().withUpperCase().withPrefix("0x");
/*    */   
/*    */   private static final long PNG_HEADER = -8552249625308161526L;
/*    */   private static final int IHDR_TYPE = 1229472850;
/*    */   private static final int IHDR_SIZE = 13;
/*    */   
/*    */   public static PngInfo fromStream(InputStream inputStream) throws IOException {
/* 19 */     DataInputStream stream = new DataInputStream(inputStream);
/* 20 */     long magic = stream.readLong();
/* 21 */     if (magic != -8552249625308161526L) {
/* 22 */       throw new IOException("Bad PNG Signature: " + FORMAT.toHexDigits(magic));
/*    */     }
/*    */ 
/*    */     
/* 26 */     int headerSize = stream.readInt();
/* 27 */     if (headerSize != 13) {
/* 28 */       throw new IOException("Bad length for IHDR chunk: " + headerSize);
/*    */     }
/*    */     
/* 31 */     int headerType = stream.readInt();
/* 32 */     if (headerType != 1229472850) {
/* 33 */       throw new IOException("Bad type for IHDR chunk: " + FORMAT.toHexDigits(headerType));
/*    */     }
/*    */     
/* 36 */     int width = stream.readInt();
/* 37 */     int height = stream.readInt();
/*    */     
/* 39 */     return new PngInfo(width, height);
/*    */   }
/*    */ 
/*    */   
/* 43 */   public static PngInfo fromBytes(byte[] bytes) throws IOException { return fromStream(new ByteArrayInputStream(bytes)); }
/*    */ 
/*    */   
/*    */   public static void validateHeader(ByteBuffer buffer) throws IOException {
/* 47 */     ByteOrder order = buffer.order();
/* 48 */     buffer.order(ByteOrder.BIG_ENDIAN);
/* 49 */     if (buffer.limit() < 16) {
/* 50 */       throw new IOException("PNG header missing");
/*    */     }
/* 52 */     if (buffer.getLong(0) != -8552249625308161526L) {
/* 53 */       throw new IOException("Bad PNG Signature");
/*    */     }
/*    */ 
/*    */     
/* 57 */     if (buffer.getInt(8) != 13) {
/* 58 */       throw new IOException("Bad length for IHDR chunk!");
/*    */     }
/*    */     
/* 61 */     if (buffer.getInt(12) != 1229472850) {
/* 62 */       throw new IOException("Bad type for IHDR chunk!");
/*    */     }
/* 64 */     buffer.order(order);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\PngInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */