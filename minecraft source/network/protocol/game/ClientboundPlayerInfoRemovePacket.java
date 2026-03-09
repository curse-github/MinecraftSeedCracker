/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.List;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundPlayerInfoRemovePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final List<UUID> profileIds;
/*    */   
/* 12 */   public ClientboundPlayerInfoRemovePacket(List<UUID> profileIds) { this.profileIds = profileIds; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket; } public List<UUID> profileIds() { return this.profileIds; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoRemovePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerInfoRemovePacket> STREAM_CODEC = Packet.codec(ClientboundPlayerInfoRemovePacket::write, ClientboundPlayerInfoRemovePacket::new);
/*    */ 
/*    */   
/* 16 */   private ClientboundPlayerInfoRemovePacket(FriendlyByteBuf input) { this(input.readList(UUIDUtil.STREAM_CODEC)); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   private void write(FriendlyByteBuf output) { output.writeCollection(this.profileIds, UUIDUtil.STREAM_CODEC); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public PacketType<ClientboundPlayerInfoRemovePacket> type() { return GamePacketTypes.CLIENTBOUND_PLAYER_INFO_REMOVE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void handle(ClientGamePacketListener listener) { listener.handlePlayerInfoRemove(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoRemovePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */