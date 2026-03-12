/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ServerboundChatCommandPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final String command;
/*    */   
/*  8 */   public ServerboundChatCommandPacket(String command) { this.command = command; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket; } public String command() { return this.command; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChatCommandPacket> STREAM_CODEC = Packet.codec(ServerboundChatCommandPacket::write, ServerboundChatCommandPacket::new);
/*    */ 
/*    */   
/* 12 */   private ServerboundChatCommandPacket(FriendlyByteBuf input) { this(input.readUtf()); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   private void write(FriendlyByteBuf output) { output.writeUtf(this.command); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ServerboundChatCommandPacket> type() { return GamePacketTypes.SERVERBOUND_CHAT_COMMAND; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ServerGamePacketListener listener) { listener.handleChatCommand(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundChatCommandPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */