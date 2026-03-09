/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class ClientboundLoginPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int playerId;
/*    */   private final boolean hardcore;
/*    */   private final Set<ResourceKey<Level>> levels;
/*    */   private final int maxPlayers;
/*    */   private final int chunkRadius;
/*    */   
/* 17 */   public ClientboundLoginPacket(int playerId, boolean hardcore, Set<ResourceKey<Level>> levels, int maxPlayers, int chunkRadius, int simulationDistance, boolean reducedDebugInfo, boolean showDeathScreen, boolean doLimitedCrafting, CommonPlayerSpawnInfo commonPlayerSpawnInfo, boolean enforcesSecureChat) { this.playerId = playerId; this.hardcore = hardcore; this.levels = levels; this.maxPlayers = maxPlayers; this.chunkRadius = chunkRadius; this.simulationDistance = simulationDistance; this.reducedDebugInfo = reducedDebugInfo; this.showDeathScreen = showDeathScreen; this.doLimitedCrafting = doLimitedCrafting; this.commonPlayerSpawnInfo = commonPlayerSpawnInfo; this.enforcesSecureChat = enforcesSecureChat; } private final int simulationDistance; private final boolean reducedDebugInfo; private final boolean showDeathScreen; private final boolean doLimitedCrafting; private final CommonPlayerSpawnInfo commonPlayerSpawnInfo; private final boolean enforcesSecureChat; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public int playerId() { return this.playerId; } public boolean hardcore() { return this.hardcore; } public Set<ResourceKey<Level>> levels() { return this.levels; } public int maxPlayers() { return this.maxPlayers; } public int chunkRadius() { return this.chunkRadius; } public int simulationDistance() { return this.simulationDistance; } public boolean reducedDebugInfo() { return this.reducedDebugInfo; } public boolean showDeathScreen() { return this.showDeathScreen; } public boolean doLimitedCrafting() { return this.doLimitedCrafting; } public CommonPlayerSpawnInfo commonPlayerSpawnInfo() { return this.commonPlayerSpawnInfo; } public boolean enforcesSecureChat() { return this.enforcesSecureChat; }
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
/*    */ 
/*    */   
/* 30 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLoginPacket> STREAM_CODEC = Packet.codec(ClientboundLoginPacket::write, ClientboundLoginPacket::new);
/*    */   
/*    */   private ClientboundLoginPacket(RegistryFriendlyByteBuf input) {
/* 33 */     this(input
/* 34 */         .readInt(), input
/* 35 */         .readBoolean(), (Set)input
/* 36 */         .readCollection(Sets::newHashSetWithExpectedSize, buf -> buf.readResourceKey(Registries.DIMENSION)), input
/* 37 */         .readVarInt(), input
/* 38 */         .readVarInt(), input
/* 39 */         .readVarInt(), input
/* 40 */         .readBoolean(), input
/* 41 */         .readBoolean(), input
/* 42 */         .readBoolean(), new CommonPlayerSpawnInfo(input), input
/*    */         
/* 44 */         .readBoolean());
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 49 */     output.writeInt(this.playerId);
/* 50 */     output.writeBoolean(this.hardcore);
/* 51 */     output.writeCollection(this.levels, FriendlyByteBuf::writeResourceKey);
/* 52 */     output.writeVarInt(this.maxPlayers);
/* 53 */     output.writeVarInt(this.chunkRadius);
/* 54 */     output.writeVarInt(this.simulationDistance);
/* 55 */     output.writeBoolean(this.reducedDebugInfo);
/* 56 */     output.writeBoolean(this.showDeathScreen);
/* 57 */     output.writeBoolean(this.doLimitedCrafting);
/* 58 */     this.commonPlayerSpawnInfo.write(output);
/* 59 */     output.writeBoolean(this.enforcesSecureChat);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 64 */   public PacketType<ClientboundLoginPacket> type() { return GamePacketTypes.CLIENTBOUND_LOGIN; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 69 */   public void handle(ClientGamePacketListener listener) { listener.handleLogin(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundLoginPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */