/*    */ package net.minecraft.network.protocol.handshake;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientIntentionPacket extends Record implements Packet<ServerHandshakePacketListener> {
/*    */   private final int protocolVersion;
/*    */   private final String hostName;
/*    */   private final int port;
/*    */   
/* 11 */   public int protocolVersion() { return this.protocolVersion; } private final ClientIntent intention; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public String hostName() { return this.hostName; } public int port() { return this.port; } public ClientIntent intention() { return this.intention; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final StreamCodec<FriendlyByteBuf, ClientIntentionPacket> STREAM_CODEC = Packet.codec(ClientIntentionPacket::write, ClientIntentionPacket::new);
/*    */ 
/*    */   
/*    */   private static final int MAX_HOST_LENGTH = 255;
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/* 25 */   public ClientIntentionPacket(int protocolVersion, String hostName, int port, ClientIntent intention) { this.protocolVersion = protocolVersion; this.hostName = hostName; this.port = port; this.intention = intention; }
/*    */ 
/*    */   
/*    */   private ClientIntentionPacket(FriendlyByteBuf input) {
/* 29 */     this(input
/* 30 */         .readVarInt(), input
/* 31 */         .readUtf(255), input
/* 32 */         .readUnsignedShort(), 
/* 33 */         ClientIntent.byId(input.readVarInt()));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 38 */     output.writeVarInt(this.protocolVersion);
/* 39 */     output.writeUtf(this.hostName);
/* 40 */     output.writeShort(this.port);
/* 41 */     output.writeVarInt(this.intention.id());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public PacketType<ClientIntentionPacket> type() { return HandshakePacketTypes.CLIENT_INTENTION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public void handle(ServerHandshakePacketListener listener) { listener.handleIntention(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public boolean isTerminal() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\handshake\ClientIntentionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */