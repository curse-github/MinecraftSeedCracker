/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import java.util.List;
/*    */ import javax.crypto.Cipher;
/*    */ 
/*    */ public class CipherDecoder
/*    */   extends MessageToMessageDecoder<ByteBuf>
/*    */ {
/*    */   private final CipherBase cipher;
/*    */   
/* 14 */   public CipherDecoder(Cipher cipher) { this.cipher = new CipherBase(cipher); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception { out.add(this.cipher.decipher(ctx, msg)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\CipherDecoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */