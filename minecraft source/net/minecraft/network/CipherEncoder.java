/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import javax.crypto.Cipher;
/*    */ 
/*    */ public class CipherEncoder
/*    */   extends MessageToByteEncoder<ByteBuf>
/*    */ {
/*    */   private final CipherBase cipher;
/*    */   
/* 13 */   public CipherEncoder(Cipher cipher) { this.cipher = new CipherBase(cipher); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception { this.cipher.encipher(msg, out); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\CipherEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */