/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundPlayerRotationPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final float yRot;
/*    */   private final boolean relativeY;
/*    */   
/*  9 */   public ClientboundPlayerRotationPacket(float yRot, boolean relativeY, float xRot, boolean relativeX) { this.yRot = yRot; this.relativeY = relativeY; this.xRot = xRot; this.relativeX = relativeX; } private final float xRot; private final boolean relativeX; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerRotationPacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public float yRot() { return this.yRot; } public boolean relativeY() { return this.relativeY; } public float xRot() { return this.xRot; } public boolean relativeX() { return this.relativeX; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerRotationPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, ClientboundPlayerRotationPacket::yRot, ByteBufCodecs.BOOL, ClientboundPlayerRotationPacket::relativeY, ByteBufCodecs.FLOAT, ClientboundPlayerRotationPacket::xRot, ByteBufCodecs.BOOL, ClientboundPlayerRotationPacket::relativeX, ClientboundPlayerRotationPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public PacketType<ClientboundPlayerRotationPacket> type() { return GamePacketTypes.CLIENTBOUND_PLAYER_ROTATION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public void handle(ClientGamePacketListener listener) { listener.handleRotatePlayer(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerRotationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */