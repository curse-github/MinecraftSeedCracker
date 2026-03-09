/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import java.util.zip.DataFormatException;
/*    */ import java.util.zip.Inflater;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompressionDecoder
/*    */   extends ByteToMessageDecoder
/*    */ {
/*    */   public static final int MAXIMUM_COMPRESSED_LENGTH = 2097152;
/*    */   public static final int MAXIMUM_UNCOMPRESSED_LENGTH = 8388608;
/*    */   private final Inflater inflater;
/*    */   private int threshold;
/*    */   private boolean validateDecompressed;
/*    */   
/*    */   public CompressionDecoder(int threshold, boolean validateDecompressed) {
/* 25 */     this.threshold = threshold;
/* 26 */     this.validateDecompressed = validateDecompressed;
/* 27 */     this.inflater = new Inflater();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/* 32 */     int uncompressedLength = VarInt.read(in);
/*    */     
/* 34 */     if (uncompressedLength == 0) {
/* 35 */       out.add(in.readBytes(in.readableBytes()));
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     if (this.validateDecompressed) {
/* 40 */       if (uncompressedLength < this.threshold)
/* 41 */         throw new DecoderException("Badly compressed packet - size of " + uncompressedLength + " is below server threshold of " + this.threshold); 
/* 42 */       if (uncompressedLength > 8388608) {
/* 43 */         throw new DecoderException("Badly compressed packet - size of " + uncompressedLength + " is larger than protocol maximum of 8388608");
/*    */       }
/*    */     } 
/*    */     
/* 47 */     setupInflaterInput(in);
/* 48 */     ByteBuf output = inflate(ctx, uncompressedLength);
/* 49 */     this.inflater.reset();
/*    */     
/* 51 */     out.add(output);
/*    */   }
/*    */   
/*    */   private void setupInflaterInput(ByteBuf in) {
/*    */     ByteBuffer input;
/* 56 */     if (in.nioBufferCount() > 0) {
/* 57 */       input = in.nioBuffer();
/* 58 */       in.skipBytes(in.readableBytes());
/*    */     } else {
/*    */       
/* 61 */       input = ByteBuffer.allocateDirect(in.readableBytes());
/* 62 */       in.readBytes(input);
/* 63 */       input.flip();
/*    */     } 
/* 65 */     this.inflater.setInput(input);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private ByteBuf inflate(ChannelHandlerContext ctx, int uncompressedLength) throws DataFormatException {
/* 71 */     ByteBuf output = ctx.alloc().directBuffer(uncompressedLength);
/*    */     try {
/* 73 */       ByteBuffer nioBuffer = output.internalNioBuffer(0, uncompressedLength);
/* 74 */       int pos = nioBuffer.position();
/* 75 */       this.inflater.inflate(nioBuffer);
/* 76 */       int actualUncompressedLength = nioBuffer.position() - pos;
/* 77 */       if (actualUncompressedLength != uncompressedLength) {
/* 78 */         throw new DecoderException("Badly compressed packet - actual length of uncompressed payload " + actualUncompressedLength + " is does not match declared size " + uncompressedLength);
/*    */       }
/* 80 */       output.writerIndex(output.writerIndex() + actualUncompressedLength);
/* 81 */       return output;
/* 82 */     } catch (Exception e) {
/* 83 */       output.release();
/* 84 */       throw e;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setThreshold(int threshold, boolean validateDecompressed) {
/* 89 */     this.threshold = threshold;
/* 90 */     this.validateDecompressed = validateDecompressed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\CompressionDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */