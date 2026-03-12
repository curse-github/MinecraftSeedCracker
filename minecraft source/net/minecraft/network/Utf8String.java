/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufUtil;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ 
/*    */ 
/*    */ public class Utf8String
/*    */ {
/*    */   public static String read(ByteBuf input, int maxLength) {
/* 13 */     int maxEncodedLength = ByteBufUtil.utf8MaxBytes(maxLength);
/*    */     
/* 15 */     int bufferLength = VarInt.read(input);
/* 16 */     if (bufferLength > maxEncodedLength) {
/* 17 */       throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + bufferLength + " > " + maxEncodedLength + ")");
/*    */     }
/* 19 */     if (bufferLength < 0) {
/* 20 */       throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
/*    */     }
/* 22 */     int availableBytes = input.readableBytes();
/* 23 */     if (bufferLength > availableBytes) {
/* 24 */       throw new DecoderException("Not enough bytes in buffer, expected " + bufferLength + ", but got " + availableBytes);
/*    */     }
/*    */     
/* 27 */     String result = input.toString(input.readerIndex(), bufferLength, StandardCharsets.UTF_8);
/* 28 */     input.readerIndex(input.readerIndex() + bufferLength);
/* 29 */     if (result.length() > maxLength) {
/* 30 */       throw new DecoderException("The received string length is longer than maximum allowed (" + result.length() + " > " + maxLength + ")");
/*    */     }
/*    */     
/* 33 */     return result;
/*    */   }
/*    */   
/*    */   public static void write(ByteBuf output, CharSequence value, int maxLength) {
/* 37 */     if (value.length() > maxLength) {
/* 38 */       throw new EncoderException("String too big (was " + value.length() + " characters, max " + maxLength + ")");
/*    */     }
/*    */     
/* 41 */     int maxEncodedValueLength = ByteBufUtil.utf8MaxBytes(value);
/* 42 */     tmp = output.alloc().buffer(maxEncodedValueLength);
/*    */     try {
/* 44 */       int bytesWritten = ByteBufUtil.writeUtf8(tmp, value);
/* 45 */       int maxAllowedEncodedLength = ByteBufUtil.utf8MaxBytes(maxLength);
/* 46 */       if (bytesWritten > maxAllowedEncodedLength) {
/* 47 */         throw new EncoderException("String too big (was " + bytesWritten + " bytes encoded, max " + maxAllowedEncodedLength + ")");
/*    */       }
/* 49 */       VarInt.write(output, bytesWritten);
/* 50 */       output.writeBytes(tmp);
/*    */     } finally {
/* 52 */       tmp.release();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\Utf8String.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */