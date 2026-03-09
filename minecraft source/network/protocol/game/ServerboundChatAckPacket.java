/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ServerboundChatAckPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int offset;
/*    */   
/*  8 */   public ServerboundChatAckPacket(int offset) { this.offset = offset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket; } public int offset() { return this.offset; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatAckPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatAckPacket> STREAM_CODEC = Packet.codec(ServerboundChatAckPacket::write, ServerboundChatAckPacket::new);
/*    */ 
/*    */   
/* 12 */   private ServerboundChatAckPacket(FriendlyByteBuf input) { this(input.readVarInt()); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.offset); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ServerboundChatAckPacket> type() { return GamePacketTypes.SERVERBOUND_CHAT_ACK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ServerGamePacketListener listener) { listener.handleChatAck(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundChatAckPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */