/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.time.Instant;
/*    */ import net.minecraft.commands.arguments.ArgumentSignatures;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.chat.LastSeenMessages;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ServerboundChatCommandSignedPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final String command;
/*    */   private final Instant timeStamp;
/*    */   
/* 12 */   public ServerboundChatCommandSignedPacket(String command, Instant timeStamp, long salt, ArgumentSignatures argumentSignatures, LastSeenMessages.Update lastSeenMessages) { this.command = command; this.timeStamp = timeStamp; this.salt = salt; this.argumentSignatures = argumentSignatures; this.lastSeenMessages = lastSeenMessages; } private final long salt; private final ArgumentSignatures argumentSignatures; private final LastSeenMessages.Update lastSeenMessages; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public String command() { return this.command; } public Instant timeStamp() { return this.timeStamp; } public long salt() { return this.salt; } public ArgumentSignatures argumentSignatures() { return this.argumentSignatures; } public LastSeenMessages.Update lastSeenMessages() { return this.lastSeenMessages; }
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatCommandSignedPacket> STREAM_CODEC = Packet.codec(ServerboundChatCommandSignedPacket::write, ServerboundChatCommandSignedPacket::new);
/*    */   
/*    */   private ServerboundChatCommandSignedPacket(FriendlyByteBuf input) {
/* 16 */     this(input
/* 17 */         .readUtf(), input
/* 18 */         .readInstant(), input
/* 19 */         .readLong(), new ArgumentSignatures(input), new LastSeenMessages.Update(input));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeUtf(this.command);
/* 27 */     output.writeInstant(this.timeStamp);
/* 28 */     output.writeLong(this.salt);
/* 29 */     this.argumentSignatures.write(output);
/* 30 */     this.lastSeenMessages.write(output);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public PacketType<ServerboundChatCommandSignedPacket> type() { return GamePacketTypes.SERVERBOUND_CHAT_COMMAND_SIGNED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public void handle(ServerGamePacketListener listener) { listener.handleSignedChatCommand(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundChatCommandSignedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */