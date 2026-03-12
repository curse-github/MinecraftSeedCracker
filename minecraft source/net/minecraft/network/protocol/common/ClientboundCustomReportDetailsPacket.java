/*    */ package net.minecraft.network.protocol.common;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundCustomReportDetailsPacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final Map<String, String> details;
/*    */   private static final int MAX_DETAIL_KEY_LENGTH = 128;
/*    */   
/* 12 */   public ClientboundCustomReportDetailsPacket(Map<String, String> details) { this.details = details; } private static final int MAX_DETAIL_VALUE_LENGTH = 4096; private static final int MAX_DETAIL_COUNT = 32; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket; } public Map<String, String> details() { return this.details; }
/*    */ 
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket; }
/*    */ 
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundCustomReportDetailsPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 20 */   private static final StreamCodec<ByteBuf, Map<String, String>> DETAILS_STREAM_CODEC = ByteBufCodecs.map(java.util.HashMap::new, 
/*    */       
/* 22 */       ByteBufCodecs.stringUtf8(128), 
/* 23 */       ByteBufCodecs.stringUtf8(4096), 32);
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final StreamCodec<ByteBuf, ClientboundCustomReportDetailsPacket> STREAM_CODEC = StreamCodec.composite(DETAILS_STREAM_CODEC, ClientboundCustomReportDetailsPacket::details, ClientboundCustomReportDetailsPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public PacketType<ClientboundCustomReportDetailsPacket> type() { return CommonPacketTypes.CLIENTBOUND_CUSTOM_REPORT_DETAILS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public void handle(ClientCommonPacketListener listener) { listener.handleCustomReportDetails(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundCustomReportDetailsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */