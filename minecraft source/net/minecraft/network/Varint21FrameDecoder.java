/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.CorruptedFrameException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Varint21FrameDecoder extends ByteToMessageDecoder {
/*    */   private static final int MAX_VARINT21_BYTES = 3;
/*    */   private final ByteBuf helperBuf;
/*    */   private final BandwidthDebugMonitor monitor;
/*    */   
/*    */   public Varint21FrameDecoder(BandwidthDebugMonitor monitor) {
/* 16 */     this.helperBuf = Unpooled.directBuffer(3);
/*    */ 
/*    */ 
/*    */     
/* 20 */     this.monitor = monitor;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected void handlerRemoved0(ChannelHandlerContext ctx) { this.helperBuf.release(); }
/*    */ 
/*    */   
/*    */   private static boolean copyVarint(ByteBuf in, ByteBuf out) {
/* 29 */     for (int i = 0; i < 3; i++) {
/* 30 */       if (!in.isReadable()) {
/* 31 */         return false;
/*    */       }
/*    */       
/* 34 */       byte b = in.readByte();
/* 35 */       out.writeByte(b);
/*    */       
/* 37 */       if (!VarInt.hasContinuationBit(b)) {
/* 38 */         return true;
/*    */       }
/*    */     } 
/*    */     
/* 42 */     throw new CorruptedFrameException("length wider than 21-bit");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
/* 48 */     in.markReaderIndex();
/*    */     
/* 50 */     this.helperBuf.clear();
/*    */     
/* 52 */     if (!copyVarint(in, this.helperBuf)) {
/* 53 */       in.resetReaderIndex();
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     int length = VarInt.read(this.helperBuf);
/* 58 */     if (length == 0) {
/* 59 */       throw new CorruptedFrameException("Frame length cannot be zero");
/*    */     }
/*    */     
/* 62 */     if (in.readableBytes() < length) {
/* 63 */       in.resetReaderIndex();
/*    */       
/*    */       return;
/*    */     } 
/* 67 */     if (this.monitor != null) {
/* 68 */       this.monitor.onReceive(length + VarInt.getByteSize(length));
/*    */     }
/*    */     
/* 71 */     out.add(in.readBytes(length));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\Varint21FrameDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */