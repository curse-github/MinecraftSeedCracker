/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundCodeOfConductPacket extends Record implements Packet<ClientConfigurationPacketListener> {
/*    */   private final String codeOfConduct;
/*    */   
/*  9 */   public ClientboundCodeOfConductPacket(String codeOfConduct) { this.codeOfConduct = codeOfConduct; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket; } public String codeOfConduct() { return this.codeOfConduct; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundCodeOfConductPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ClientboundCodeOfConductPacket::codeOfConduct, ClientboundCodeOfConductPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public PacketType<ClientboundCodeOfConductPacket> type() { return ConfigurationPacketTypes.CLIENTBOUND_CODE_OF_CONDUCT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public void handle(ClientConfigurationPacketListener listener) { listener.handleCodeOfConduct(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\configuration\ClientboundCodeOfConductPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */