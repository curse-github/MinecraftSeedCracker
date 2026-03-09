/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ServerboundAcceptCodeOfConductPacket extends Record implements Packet<ServerConfigurationPacketListener> {
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket; }
/*    */   
/*  9 */   public static final ServerboundAcceptCodeOfConductPacket INSTANCE = new ServerboundAcceptCodeOfConductPacket(); public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundAcceptCodeOfConductPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */   
/* 14 */   public PacketType<ServerboundAcceptCodeOfConductPacket> type() { return ConfigurationPacketTypes.SERVERBOUND_ACCEPT_CODE_OF_CONDUCT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public void handle(ServerConfigurationPacketListener listener) { listener.handleAcceptCodeOfConduct(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\configuration\ServerboundAcceptCodeOfConductPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */