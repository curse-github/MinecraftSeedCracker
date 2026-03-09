/*    */ package net.minecraft.network.protocol.common;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.custom.BrandPayload;
/*    */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*    */ import net.minecraft.network.protocol.common.custom.DiscardedPayload;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class ServerboundCustomPayloadPacket extends Record implements Packet<ServerCommonPacketListener> {
/*    */   private final CustomPacketPayload payload;
/*    */   
/* 15 */   public ServerboundCustomPayloadPacket(CustomPacketPayload payload) { this.payload = payload; } private static final int MAX_PAYLOAD_SIZE = 32767; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket; } public CustomPacketPayload payload() { return this.payload; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ServerboundCustomPayloadPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 18 */   public static final StreamCodec<FriendlyByteBuf, ServerboundCustomPayloadPacket> STREAM_CODEC = CustomPacketPayload.codec(id -> 
/* 19 */       DiscardedPayload.codec(id, 32767), 
/* 20 */       (List)Util.make(Lists.newArrayList(new CustomPacketPayload.TypeAndCodec[] { new CustomPacketPayload.TypeAndCodec(BrandPayload.TYPE, BrandPayload.STREAM_CODEC) }), types -> {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 27 */         })).map(ServerboundCustomPayloadPacket::new, ServerboundCustomPayloadPacket::payload);
/*    */ 
/*    */ 
/*    */   
/* 31 */   public PacketType<ServerboundCustomPayloadPacket> type() { return CommonPacketTypes.SERVERBOUND_CUSTOM_PAYLOAD; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void handle(ServerCommonPacketListener listener) { listener.handleCustomPayload(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ServerboundCustomPayloadPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */