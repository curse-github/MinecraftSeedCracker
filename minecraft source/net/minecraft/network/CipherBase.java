/*    */ package net.minecraft.network;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.ShortBufferException;
/*    */ 
/*    */ public class CipherBase {
/*    */   private final Cipher cipher;
/*    */   
/*    */   protected CipherBase(Cipher cipher) {
/* 11 */     this.heapIn = new byte[0];
/* 12 */     this.heapOut = new byte[0];
/*    */ 
/*    */     
/* 15 */     this.cipher = cipher;
/*    */   }
/*    */   private byte[] heapIn; private byte[] heapOut;
/*    */   private byte[] bufToByte(ByteBuf in) {
/* 19 */     int readableBytes = in.readableBytes();
/* 20 */     if (this.heapIn.length < readableBytes) {
/* 21 */       this.heapIn = new byte[readableBytes];
/*    */     }
/* 23 */     in.readBytes(this.heapIn, 0, readableBytes);
/* 24 */     return this.heapIn;
/*    */   }
/*    */   
/*    */   protected ByteBuf decipher(ChannelHandlerContext ctx, ByteBuf in) throws ShortBufferException {
/* 28 */     int readableBytes = in.readableBytes();
/* 29 */     byte[] heapIn = bufToByte(in);
/*    */     
/* 31 */     ByteBuf heapOut = ctx.alloc().heapBuffer(this.cipher.getOutputSize(readableBytes));
/* 32 */     heapOut.writerIndex(this.cipher.update(heapIn, 0, readableBytes, heapOut.array(), heapOut.arrayOffset()));
/*    */     
/* 34 */     return heapOut;
/*    */   }
/*    */   
/*    */   protected void encipher(ByteBuf in, ByteBuf out) throws ShortBufferException {
/* 38 */     int readableBytes = in.readableBytes();
/* 39 */     byte[] heapIn = bufToByte(in);
/*    */     
/* 41 */     int outputSize = this.cipher.getOutputSize(readableBytes);
/* 42 */     if (this.heapOut.length < outputSize) {
/* 43 */       this.heapOut = new byte[outputSize];
/*    */     }
/* 45 */     out.writeBytes(this.heapOut, 0, this.cipher.update(heapIn, 0, readableBytes, this.heapOut));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\CipherBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */