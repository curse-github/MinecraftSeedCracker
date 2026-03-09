/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import net.minecraft.network.codec.IdDispatchCodec;
/*    */ 
/*    */ public class SkipPacketDecoderException
/*    */   extends DecoderException implements IdDispatchCodec.DontDecorateException, SkipPacketException {
/*  8 */   public SkipPacketDecoderException(String message) { super(message); }
/*    */ 
/*    */ 
/*    */   
/* 12 */   public SkipPacketDecoderException(Throwable cause) { super(cause); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\SkipPacketDecoderException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */