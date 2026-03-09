/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import net.minecraft.network.codec.IdDispatchCodec;
/*    */ 
/*    */ public class SkipPacketEncoderException
/*    */   extends EncoderException implements IdDispatchCodec.DontDecorateException, SkipPacketException {
/*  8 */   public SkipPacketEncoderException(String message) { super(message); }
/*    */ 
/*    */ 
/*    */   
/* 12 */   public SkipPacketEncoderException(Throwable cause) { super(cause); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\SkipPacketEncoderException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */