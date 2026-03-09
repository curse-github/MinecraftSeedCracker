/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundRespawnPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final CommonPlayerSpawnInfo commonPlayerSpawnInfo;
/*    */   private final byte dataToKeep;
/*    */   
/*  9 */   public ClientboundRespawnPacket(CommonPlayerSpawnInfo commonPlayerSpawnInfo, byte dataToKeep) { this.commonPlayerSpawnInfo = commonPlayerSpawnInfo; this.dataToKeep = dataToKeep; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket; } public CommonPlayerSpawnInfo commonPlayerSpawnInfo() { return this.commonPlayerSpawnInfo; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public byte dataToKeep() { return this.dataToKeep; }
/*    */ 
/*    */ 
/*    */   
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRespawnPacket> STREAM_CODEC = Packet.codec(ClientboundRespawnPacket::write, ClientboundRespawnPacket::new);
/*    */ 
/*    */   
/*    */   public static final byte KEEP_ATTRIBUTE_MODIFIERS = 1;
/*    */ 
/*    */   
/*    */   public static final byte KEEP_ENTITY_DATA = 2;
/*    */ 
/*    */   
/*    */   public static final byte KEEP_ALL_DATA = 3;
/*    */ 
/*    */   
/*    */   private ClientboundRespawnPacket(RegistryFriendlyByteBuf input) {
/* 26 */     this(new CommonPlayerSpawnInfo(input), input
/*    */         
/* 28 */         .readByte());
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 33 */     this.commonPlayerSpawnInfo.write(output);
/* 34 */     output.writeByte(this.dataToKeep);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public PacketType<ClientboundRespawnPacket> type() { return GamePacketTypes.CLIENTBOUND_RESPAWN; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public void handle(ClientGamePacketListener listener) { listener.handleRespawn(this); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public boolean shouldKeep(byte mask) { return ((this.dataToKeep & mask) != 0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRespawnPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */