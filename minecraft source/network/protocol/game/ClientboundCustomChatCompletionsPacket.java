/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundCustomChatCompletionsPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Action action;
/*    */   private final List<String> entries;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket; }
/*    */   
/* 14 */   public ClientboundCustomChatCompletionsPacket(Action action, List<String> entries) { this.action = action; this.entries = entries; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Action action() { return this.action; } public List<String> entries() { return this.entries; }
/* 15 */   public static final StreamCodec<FriendlyByteBuf, ClientboundCustomChatCompletionsPacket> STREAM_CODEC = Packet.codec(ClientboundCustomChatCompletionsPacket::write, ClientboundCustomChatCompletionsPacket::new);
/*    */   
/*    */   public enum Action {
/* 18 */     ADD,
/* 19 */     REMOVE,
/* 20 */     SET;
/*    */   }
/*    */   
/*    */   private ClientboundCustomChatCompletionsPacket(FriendlyByteBuf input) {
/* 24 */     this((Action)input
/* 25 */         .readEnum(Action.class), input
/* 26 */         .readList(FriendlyByteBuf::readUtf));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 31 */     output.writeEnum(this.action);
/* 32 */     output.writeCollection(this.entries, FriendlyByteBuf::writeUtf);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public PacketType<ClientboundCustomChatCompletionsPacket> type() { return GamePacketTypes.CLIENTBOUND_CUSTOM_CHAT_COMPLETIONS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public void handle(ClientGamePacketListener listener) { listener.handleCustomChatCompletions(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCustomChatCompletionsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */