/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundDeleteChatPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final MessageSignature.Packed messageSignature;
/*    */   
/*  9 */   public ClientboundDeleteChatPacket(MessageSignature.Packed messageSignature) { this.messageSignature = messageSignature; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket; } public MessageSignature.Packed messageSignature() { return this.messageSignature; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDeleteChatPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundDeleteChatPacket> STREAM_CODEC = Packet.codec(ClientboundDeleteChatPacket::write, ClientboundDeleteChatPacket::new);
/*    */ 
/*    */   
/* 13 */   private ClientboundDeleteChatPacket(FriendlyByteBuf input) { this(MessageSignature.Packed.read(input)); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   private void write(FriendlyByteBuf output) { MessageSignature.Packed.write(output, this.messageSignature); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ClientboundDeleteChatPacket> type() { return GamePacketTypes.CLIENTBOUND_DELETE_CHAT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ClientGamePacketListener listener) { listener.handleDeleteChat(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundDeleteChatPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */