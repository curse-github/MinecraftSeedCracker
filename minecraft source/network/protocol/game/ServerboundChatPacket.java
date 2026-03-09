/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.time.Instant;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.chat.LastSeenMessages;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ 
/*    */ public final class ServerboundChatPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final String message;
/*    */   private final Instant timeStamp;
/*    */   private final long salt;
/*    */   private final MessageSignature signature;
/*    */   private final LastSeenMessages.Update lastSeenMessages;
/*    */   
/* 14 */   public ServerboundChatPacket(String message, Instant timeStamp, long salt, MessageSignature signature, LastSeenMessages.Update lastSeenMessages) { this.message = message; this.timeStamp = timeStamp; this.salt = salt; this.signature = signature; this.lastSeenMessages = lastSeenMessages; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatPacket; } public String message() { return this.message; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatPacket;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Instant timeStamp() { return this.timeStamp; } public long salt() { return this.salt; } public MessageSignature signature() { return this.signature; } public LastSeenMessages.Update lastSeenMessages() { return this.lastSeenMessages; }
/* 15 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatPacket> STREAM_CODEC = Packet.codec(ServerboundChatPacket::write, ServerboundChatPacket::new);
/*    */   
/*    */   private ServerboundChatPacket(FriendlyByteBuf input) {
/* 18 */     this(input
/* 19 */         .readUtf(256), input
/* 20 */         .readInstant(), input
/* 21 */         .readLong(), (MessageSignature)input
/* 22 */         .readNullable(MessageSignature::read), new LastSeenMessages.Update(input));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeUtf(this.message, 256);
/* 29 */     output.writeInstant(this.timeStamp);
/* 30 */     output.writeLong(this.salt);
/* 31 */     output.writeNullable(this.signature, MessageSignature::write);
/* 32 */     this.lastSeenMessages.write(output);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public PacketType<ServerboundChatPacket> type() { return GamePacketTypes.SERVERBOUND_CHAT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void handle(ServerGamePacketListener listener) { listener.handleChat(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundChatPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */