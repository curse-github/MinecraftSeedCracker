/*    */ package net.minecraft.network.protocol.game;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.stats.Stat;
/*    */ 
/*    */ public final class ClientboundAwardStatsPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Object2IntMap<Stat<?>> stats;
/*    */   
/* 12 */   public ClientboundAwardStatsPacket(Object2IntMap<Stat<?>> stats) { this.stats = stats; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket; } public Object2IntMap<Stat<?>> stats() { return this.stats; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   private static final StreamCodec<RegistryFriendlyByteBuf, Object2IntMap<Stat<?>>> STAT_VALUES_STREAM_CODEC = ByteBufCodecs.map(it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap::new, Stat.STREAM_CODEC, ByteBufCodecs.VAR_INT);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAwardStatsPacket> STREAM_CODEC = STAT_VALUES_STREAM_CODEC
/* 20 */     .map(ClientboundAwardStatsPacket::new, ClientboundAwardStatsPacket::stats);
/*    */ 
/*    */ 
/*    */   
/* 24 */   public PacketType<ClientboundAwardStatsPacket> type() { return GamePacketTypes.CLIENTBOUND_AWARD_STATS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void handle(ClientGamePacketListener listener) { listener.handleAwardStats(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundAwardStatsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */