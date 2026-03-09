/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundSetSubtitleTextPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Component text;
/*    */   
/* 10 */   public ClientboundSetSubtitleTextPacket(Component text) { this.text = text; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetSubtitleTextPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSubtitleTextPacket; } public Component text() { return this.text; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetSubtitleTextPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSubtitleTextPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetSubtitleTextPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSubtitleTextPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 11 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetSubtitleTextPacket> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundSetSubtitleTextPacket::text, ClientboundSetSubtitleTextPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public PacketType<ClientboundSetSubtitleTextPacket> type() { return GamePacketTypes.CLIENTBOUND_SET_SUBTITLE_TEXT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void handle(ClientGamePacketListener listener) { listener.setSubtitleText(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetSubtitleTextPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */