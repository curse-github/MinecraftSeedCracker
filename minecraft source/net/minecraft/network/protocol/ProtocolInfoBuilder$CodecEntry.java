/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class CodecEntry<T extends PacketListener, P extends Packet<? super T>, B extends ByteBuf, C>
/*    */   extends Record
/*    */ {
/*    */   private final PacketType<P> type;
/*    */   private final StreamCodec<? super B, P> serializer;
/*    */   private final CodecModifier<B, P, C> modifier;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry<TT;TP;TB;TC;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry<TT;TP;TB;TC;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/ProtocolInfoBuilder$CodecEntry<TT;TP;TB;TC;>; }
/*    */   
/* 29 */   private CodecEntry(PacketType<P> type, StreamCodec<? super B, P> serializer, CodecModifier<B, P, C> modifier) { this.type = type; this.serializer = serializer; this.modifier = modifier; } public PacketType<P> type() { return this.type; } public StreamCodec<? super B, P> serializer() { return this.serializer; } public CodecModifier<B, P, C> modifier() { return this.modifier; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addToBuilder(ProtocolCodecBuilder<ByteBuf, T> codecBuilder, Function<ByteBuf, B> contextWrapper, C context) {
/*    */     StreamCodec<? super B, P> finalSerializer;
/* 36 */     if (this.modifier != null) {
/* 37 */       finalSerializer = this.modifier.apply(this.serializer, context);
/*    */     } else {
/* 39 */       finalSerializer = this.serializer;
/*    */     } 
/* 41 */     StreamCodec<ByteBuf, P> baseCodec = finalSerializer.mapStream(contextWrapper);
/* 42 */     codecBuilder.add(this.type, baseCodec);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\ProtocolInfoBuilder$CodecEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */