/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.chat.RemoteChatSession;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ServerboundChatSessionUpdatePacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final RemoteChatSession.Data chatSession;
/*    */   
/*  9 */   public ServerboundChatSessionUpdatePacket(RemoteChatSession.Data chatSession) { this.chatSession = chatSession; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket; } public RemoteChatSession.Data chatSession() { return this.chatSession; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatSessionUpdatePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatSessionUpdatePacket> STREAM_CODEC = Packet.codec(ServerboundChatSessionUpdatePacket::write, ServerboundChatSessionUpdatePacket::new);
/*    */ 
/*    */   
/* 13 */   private ServerboundChatSessionUpdatePacket(FriendlyByteBuf input) { this(RemoteChatSession.Data.read(input)); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   private void write(FriendlyByteBuf output) { RemoteChatSession.Data.write(output, this.chatSession); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ServerboundChatSessionUpdatePacket> type() { return GamePacketTypes.SERVERBOUND_CHAT_SESSION_UPDATE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ServerGamePacketListener listener) { listener.handleChatSessionUpdate(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundChatSessionUpdatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */