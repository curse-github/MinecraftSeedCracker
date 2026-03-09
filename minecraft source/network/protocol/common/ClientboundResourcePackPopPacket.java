/*    */ package net.minecraft.network.protocol.common;
/*    */ import java.util.Optional;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.core.UUIDUtil;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundResourcePackPopPacket extends Record implements Packet<ClientCommonPacketListener> {
/* 12 */   public ClientboundResourcePackPopPacket(Optional<UUID> id) { this.id = id; } private final Optional<UUID> id; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket; } public Optional<UUID> id() { return this.id; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundResourcePackPopPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 15 */   public static final StreamCodec<FriendlyByteBuf, ClientboundResourcePackPopPacket> STREAM_CODEC = Packet.codec(ClientboundResourcePackPopPacket::write, ClientboundResourcePackPopPacket::new);
/*    */ 
/*    */   
/* 18 */   private ClientboundResourcePackPopPacket(FriendlyByteBuf input) { this(input
/* 19 */         .readOptional(UUIDUtil.STREAM_CODEC)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private void write(FriendlyByteBuf output) { output.writeOptional(this.id, UUIDUtil.STREAM_CODEC); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public PacketType<ClientboundResourcePackPopPacket> type() { return CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_POP; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public void handle(ClientCommonPacketListener listener) { listener.handleResourcePackPop(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundResourcePackPopPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */