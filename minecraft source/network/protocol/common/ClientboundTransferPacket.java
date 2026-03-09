/*    */ package net.minecraft.network.protocol.common;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class ClientboundTransferPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final String host;
/*    */   private final int port;
/*    */   
/*  8 */   public ClientboundTransferPacket(String host, int port) { this.host = host; this.port = port; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundTransferPacket; } public String host() { return this.host; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundTransferPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundTransferPacket;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public int port() { return this.port; }
/*    */ 
/*    */ 
/*    */   
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTransferPacket> STREAM_CODEC = Packet.codec(ClientboundTransferPacket::write, ClientboundTransferPacket::new);
/*    */ 
/*    */   
/* 15 */   private ClientboundTransferPacket(FriendlyByteBuf input) { this(input.readUtf(), input.readVarInt()); }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 19 */     output.writeUtf(this.host);
/* 20 */     output.writeVarInt(this.port);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public PacketType<ClientboundTransferPacket> type() { return CommonPacketTypes.CLIENTBOUND_TRANSFER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void handle(ClientCommonPacketListener listener) { listener.handleTransfer(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundTransferPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */