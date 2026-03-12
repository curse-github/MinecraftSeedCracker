/*    */ package net.minecraft.network.protocol.common;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.network.protocol.common.custom.BrandPayload;
/*    */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*    */ import net.minecraft.network.protocol.common.custom.DiscardedPayload;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public final class ClientboundCustomPayloadPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final CustomPacketPayload payload;
/*    */   
/* 19 */   public ClientboundCustomPayloadPacket(CustomPacketPayload payload) { this.payload = payload; } private static final int MAX_PAYLOAD_SIZE = 1048576; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 19 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket; } public CustomPacketPayload payload() { return this.payload; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCustomPayloadPacket> GAMEPLAY_STREAM_CODEC = CustomPacketPayload.codec(id -> 
/* 23 */       DiscardedPayload.codec(id, 1048576), 
/* 24 */       (List)Util.make(Lists.newArrayList(new CustomPacketPayload.TypeAndCodec[] { new CustomPacketPayload.TypeAndCodec(BrandPayload.TYPE, BrandPayload.STREAM_CODEC) }), types -> {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 32 */         })).map(ClientboundCustomPayloadPacket::new, ClientboundCustomPayloadPacket::payload);
/*    */   
/* 34 */   public static final StreamCodec<FriendlyByteBuf, ClientboundCustomPayloadPacket> CONFIG_STREAM_CODEC = CustomPacketPayload.codec(id -> 
/* 35 */       DiscardedPayload.codec(id, 1048576), 
/* 36 */       List.of(new CustomPacketPayload.TypeAndCodec(BrandPayload.TYPE, BrandPayload.STREAM_CODEC)))
/*    */ 
/*    */ 
/*    */     
/* 40 */     .map(ClientboundCustomPayloadPacket::new, ClientboundCustomPayloadPacket::payload);
/*    */ 
/*    */ 
/*    */   
/* 44 */   public PacketType<ClientboundCustomPayloadPacket> type() { return CommonPacketTypes.CLIENTBOUND_CUSTOM_PAYLOAD; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   public void handle(ClientCommonPacketListener listener) { listener.handleCustomPayload(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundCustomPayloadPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */