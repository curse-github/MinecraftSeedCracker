/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundServerDataPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Component motd;
/*    */   private final Optional<byte[]> iconBytes;
/*    */   
/* 13 */   public ClientboundServerDataPacket(Component motd, Optional<byte[]> iconBytes) { this.motd = motd; this.iconBytes = iconBytes; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket; } public Component motd() { return this.motd; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<byte[]> iconBytes() { return this.iconBytes; }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final StreamCodec<ByteBuf, ClientboundServerDataPacket> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, ClientboundServerDataPacket::motd, ByteBufCodecs.BYTE_ARRAY
/*    */       
/* 19 */       .apply(ByteBufCodecs::optional), ClientboundServerDataPacket::iconBytes, ClientboundServerDataPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public PacketType<ClientboundServerDataPacket> type() { return GamePacketTypes.CLIENTBOUND_SERVER_DATA; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void handle(ClientGamePacketListener listener) { listener.handleServerData(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundServerDataPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */