/*    */ package net.minecraft.network.protocol.login;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.login.custom.CustomQueryPayload;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ClientboundCustomQueryPacket extends Record implements Packet<ClientLoginPacketListener> {
/*    */   private final int transactionId;
/*    */   private final CustomQueryPayload payload;
/*    */   
/* 11 */   public ClientboundCustomQueryPacket(int transactionId, CustomQueryPayload payload) { this.transactionId = transactionId; this.payload = payload; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ClientboundCustomQueryPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundCustomQueryPacket; } public int transactionId() { return this.transactionId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ClientboundCustomQueryPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundCustomQueryPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ClientboundCustomQueryPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ClientboundCustomQueryPacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public CustomQueryPayload payload() { return this.payload; }
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundCustomQueryPacket> STREAM_CODEC = Packet.codec(ClientboundCustomQueryPacket::write, ClientboundCustomQueryPacket::new);
/*    */   
/*    */   private static final int MAX_PAYLOAD_SIZE = 1048576;
/*    */   
/*    */   private ClientboundCustomQueryPacket(FriendlyByteBuf input) {
/* 17 */     this(input
/* 18 */         .readVarInt(), 
/* 19 */         readPayload(input
/* 20 */           .readIdentifier(), input));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   private static CustomQueryPayload readPayload(Identifier identifier, FriendlyByteBuf input) { return readUnknownPayload(identifier, input); }
/*    */ 
/*    */   
/*    */   private static DiscardedQueryPayload readUnknownPayload(Identifier identifier, FriendlyByteBuf input) {
/* 32 */     int length = input.readableBytes();
/* 33 */     if (length < 0 || length > 1048576) {
/* 34 */       throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
/*    */     }
/* 36 */     input.skipBytes(length);
/* 37 */     return new DiscardedQueryPayload(identifier);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 41 */     output.writeVarInt(this.transactionId);
/* 42 */     output.writeIdentifier(this.payload.id());
/* 43 */     this.payload.write(output);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public PacketType<ClientboundCustomQueryPacket> type() { return LoginPacketTypes.CLIENTBOUND_CUSTOM_QUERY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   public void handle(ClientLoginPacketListener listener) { listener.handleCustomQuery(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ClientboundCustomQueryPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */