/*    */ package net.minecraft.network.protocol.status;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ 
/*    */ public final class ClientboundStatusResponsePacket extends Record implements Packet<ClientStatusPacketListener> {
/*    */   private final ServerStatus status;
/*    */   
/* 14 */   public ClientboundStatusResponsePacket(ServerStatus status) { this.status = status; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket; } public ServerStatus status() { return this.status; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 15 */   private static final RegistryOps<JsonElement> OPS = RegistryAccess.EMPTY.createSerializationContext(JsonOps.INSTANCE);
/*    */   
/* 17 */   public static final StreamCodec<ByteBuf, ClientboundStatusResponsePacket> STREAM_CODEC = StreamCodec.composite(
/* 18 */       ByteBufCodecs.lenientJson(32767).apply(ByteBufCodecs.fromCodec(OPS, ServerStatus.CODEC)), ClientboundStatusResponsePacket::status, ClientboundStatusResponsePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public PacketType<ClientboundStatusResponsePacket> type() { return StatusPacketTypes.CLIENTBOUND_STATUS_RESPONSE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void handle(ClientStatusPacketListener listener) { listener.handleStatusResponse(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\status\ClientboundStatusResponsePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */