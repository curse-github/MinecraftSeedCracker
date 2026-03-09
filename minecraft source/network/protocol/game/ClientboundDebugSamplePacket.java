/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.util.debugchart.RemoteDebugSampleType;
/*    */ 
/*    */ public final class ClientboundDebugSamplePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final long[] sample;
/*    */   private final RemoteDebugSampleType debugSampleType;
/*    */   
/*  9 */   public ClientboundDebugSamplePacket(long[] sample, RemoteDebugSampleType debugSampleType) { this.sample = sample; this.debugSampleType = debugSampleType; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket; } public long[] sample() { return this.sample; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public RemoteDebugSampleType debugSampleType() { return this.debugSampleType; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundDebugSamplePacket> STREAM_CODEC = Packet.codec(ClientboundDebugSamplePacket::write, ClientboundDebugSamplePacket::new);
/*    */ 
/*    */   
/* 13 */   private ClientboundDebugSamplePacket(FriendlyByteBuf input) { this(input.readLongArray(), (RemoteDebugSampleType)input.readEnum(RemoteDebugSampleType.class)); }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     output.writeLongArray(this.sample);
/* 18 */     output.writeEnum(this.debugSampleType);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public PacketType<ClientboundDebugSamplePacket> type() { return GamePacketTypes.CLIENTBOUND_DEBUG_SAMPLE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void handle(ClientGamePacketListener listener) { listener.handleDebugSample(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundDebugSamplePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */