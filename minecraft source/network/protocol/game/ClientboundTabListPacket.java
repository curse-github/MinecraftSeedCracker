/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundTabListPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Component header;
/*    */   private final Component footer;
/*    */   
/* 11 */   public ClientboundTabListPacket(Component header, Component footer) { this.header = header; this.footer = footer; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTabListPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTabListPacket; } public Component header() { return this.header; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTabListPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTabListPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTabListPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTabListPacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public Component footer() { return this.footer; }
/* 12 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTabListPacket> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundTabListPacket::header, ComponentSerialization.TRUSTED_STREAM_CODEC, ClientboundTabListPacket::footer, ClientboundTabListPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public PacketType<ClientboundTabListPacket> type() { return GamePacketTypes.CLIENTBOUND_TAB_LIST; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void handle(ClientGamePacketListener listener) { listener.handleTabListCustomisation(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTabListPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */