/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.FilterMask;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ import net.minecraft.network.chat.SignedMessageBody;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundPlayerChatPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int globalIndex;
/*    */   private final UUID sender;
/*    */   private final int index;
/*    */   private final MessageSignature signature;
/*    */   
/* 18 */   public ClientboundPlayerChatPacket(int globalIndex, UUID sender, int index, MessageSignature signature, SignedMessageBody.Packed body, Component unsignedContent, FilterMask filterMask, ChatType.Bound chatType) { this.globalIndex = globalIndex; this.sender = sender; this.index = index; this.signature = signature; this.body = body; this.unsignedContent = unsignedContent; this.filterMask = filterMask; this.chatType = chatType; } private final SignedMessageBody.Packed body; private final Component unsignedContent; private final FilterMask filterMask; private final ChatType.Bound chatType; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public int globalIndex() { return this.globalIndex; } public UUID sender() { return this.sender; } public int index() { return this.index; } public MessageSignature signature() { return this.signature; } public SignedMessageBody.Packed body() { return this.body; } public Component unsignedContent() { return this.unsignedContent; } public FilterMask filterMask() { return this.filterMask; } public ChatType.Bound chatType() { return this.chatType; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerChatPacket> STREAM_CODEC = Packet.codec(ClientboundPlayerChatPacket::write, ClientboundPlayerChatPacket::new);
/*    */   
/*    */   private ClientboundPlayerChatPacket(RegistryFriendlyByteBuf input) {
/* 32 */     this(input
/* 33 */         .readVarInt(), input
/* 34 */         .readUUID(), input
/* 35 */         .readVarInt(), (MessageSignature)input
/* 36 */         .readNullable(MessageSignature::read), new SignedMessageBody.Packed(input), 
/*    */         
/* 38 */         (Component)FriendlyByteBuf.readNullable(input, ComponentSerialization.TRUSTED_STREAM_CODEC), 
/* 39 */         FilterMask.read(input), (ChatType.Bound)ChatType.Bound.STREAM_CODEC
/* 40 */         .decode(input));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 45 */     output.writeVarInt(this.globalIndex);
/* 46 */     output.writeUUID(this.sender);
/* 47 */     output.writeVarInt(this.index);
/* 48 */     output.writeNullable(this.signature, MessageSignature::write);
/* 49 */     this.body.write(output);
/* 50 */     FriendlyByteBuf.writeNullable(output, this.unsignedContent, ComponentSerialization.TRUSTED_STREAM_CODEC);
/* 51 */     FilterMask.write(output, this.filterMask);
/* 52 */     ChatType.Bound.STREAM_CODEC.encode(output, this.chatType);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public PacketType<ClientboundPlayerChatPacket> type() { return GamePacketTypes.CLIENTBOUND_PLAYER_CHAT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 62 */   public void handle(ClientGamePacketListener listener) { listener.handlePlayerChat(this); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public boolean isSkippable() { return true; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerChatPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */