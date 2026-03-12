/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Set;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.entity.PositionMoveRotation;
/*    */ import net.minecraft.world.entity.Relative;
/*    */ 
/*    */ public final class ClientboundPlayerPositionPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   private final PositionMoveRotation change;
/*    */   private final Set<Relative> relatives;
/*    */   
/* 13 */   public ClientboundPlayerPositionPacket(int id, PositionMoveRotation change, Set<Relative> relatives) { this.id = id; this.change = change; this.relatives = relatives; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public PositionMoveRotation change() { return this.change; } public Set<Relative> relatives() { return this.relatives; }
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerPositionPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundPlayerPositionPacket::id, PositionMoveRotation.STREAM_CODEC, ClientboundPlayerPositionPacket::change, Relative.SET_STREAM_CODEC, ClientboundPlayerPositionPacket::relatives, ClientboundPlayerPositionPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static ClientboundPlayerPositionPacket of(int id, PositionMoveRotation values, Set<Relative> relatives) { return new ClientboundPlayerPositionPacket(id, values, relatives); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ClientboundPlayerPositionPacket> type() { return GamePacketTypes.CLIENTBOUND_PLAYER_POSITION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ClientGamePacketListener listener) { listener.handleMovePlayer(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerPositionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */