/*    */ package net.minecraft.network.protocol.common;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundDisconnectPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final Component reason;
/*    */   
/* 10 */   public ClientboundDisconnectPacket(Component reason) { this.reason = reason; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundDisconnectPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundDisconnectPacket; } public Component reason() { return this.reason; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundDisconnectPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundDisconnectPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundDisconnectPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundDisconnectPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 11 */   public static final StreamCodec<ByteBuf, ClientboundDisconnectPacket> STREAM_CODEC = ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC.map(ClientboundDisconnectPacket::new, ClientboundDisconnectPacket::reason);
/*    */ 
/*    */ 
/*    */   
/* 15 */   public PacketType<ClientboundDisconnectPacket> type() { return CommonPacketTypes.CLIENTBOUND_DISCONNECT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public void handle(ClientCommonPacketListener listener) { listener.handleDisconnect(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundDisconnectPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */