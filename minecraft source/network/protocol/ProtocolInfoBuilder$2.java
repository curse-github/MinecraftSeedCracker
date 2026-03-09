/*     */ package net.minecraft.network.protocol;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.ProtocolInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   extends Object
/*     */   implements SimpleUnboundProtocol<T, B>
/*     */ {
/* 105 */   public ProtocolInfo<T> bind(Function<ByteBuf, B> contextWrapper) { return new ProtocolInfoBuilder.Implementation(ProtocolInfoBuilder.this.protocol, ProtocolInfoBuilder.this.flow, ProtocolInfoBuilder.this.buildPacketCodec(contextWrapper, codecs, context), bundlerInfo); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   public ProtocolInfo.Details details() { return details; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\ProtocolInfoBuilder$2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */