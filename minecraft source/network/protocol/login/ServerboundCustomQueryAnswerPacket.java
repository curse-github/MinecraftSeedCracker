/*    */ package net.minecraft.network.protocol.login;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.login.custom.CustomQueryAnswerPayload;
/*    */ 
/*    */ public final class ServerboundCustomQueryAnswerPacket extends Record implements Packet<ServerLoginPacketListener> {
/*    */   private final int transactionId;
/*    */   private final CustomQueryAnswerPayload payload;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket; }
/*    */   
/* 15 */   public ServerboundCustomQueryAnswerPacket(int transactionId, CustomQueryAnswerPayload payload) { this.transactionId = transactionId; this.payload = payload; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ServerboundCustomQueryAnswerPacket;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public int transactionId() { return this.transactionId; } public CustomQueryAnswerPayload payload() { return this.payload; }
/* 16 */   public static final StreamCodec<FriendlyByteBuf, ServerboundCustomQueryAnswerPacket> STREAM_CODEC = Packet.codec(ServerboundCustomQueryAnswerPacket::write, ServerboundCustomQueryAnswerPacket::read);
/*    */   
/*    */   private static final int MAX_PAYLOAD_SIZE = 1048576;
/*    */   
/*    */   private static ServerboundCustomQueryAnswerPacket read(FriendlyByteBuf input) {
/* 21 */     int transactionId = input.readVarInt();
/* 22 */     return new ServerboundCustomQueryAnswerPacket(transactionId, 
/*    */         
/* 24 */         readPayload(transactionId, input));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   private static CustomQueryAnswerPayload readPayload(int transactionId, FriendlyByteBuf input) { return readUnknownPayload(input); }
/*    */ 
/*    */   
/*    */   private static CustomQueryAnswerPayload readUnknownPayload(FriendlyByteBuf input) {
/* 38 */     int length = input.readableBytes();
/* 39 */     if (length < 0 || length > 1048576) {
/* 40 */       throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
/*    */     }
/* 42 */     input.skipBytes(length);
/* 43 */     return DiscardedQueryAnswerPayload.INSTANCE;
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 47 */     output.writeVarInt(this.transactionId);
/* 48 */     output.writeNullable(this.payload, (buf, data) -> data.write(buf));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public PacketType<ServerboundCustomQueryAnswerPacket> type() { return LoginPacketTypes.SERVERBOUND_CUSTOM_QUERY_ANSWER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   public void handle(ServerLoginPacketListener listener) { listener.handleCustomQueryPacket(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\ServerboundCustomQueryAnswerPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */