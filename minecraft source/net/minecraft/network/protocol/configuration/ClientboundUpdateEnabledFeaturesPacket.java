/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import java.util.Set;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ClientboundUpdateEnabledFeaturesPacket extends Record implements Packet<ClientConfigurationPacketListener> {
/*    */   private final Set<Identifier> features;
/*    */   
/* 12 */   public ClientboundUpdateEnabledFeaturesPacket(Set<Identifier> features) { this.features = features; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket; } public Set<Identifier> features() { return this.features; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundUpdateEnabledFeaturesPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final StreamCodec<FriendlyByteBuf, ClientboundUpdateEnabledFeaturesPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateEnabledFeaturesPacket::write, ClientboundUpdateEnabledFeaturesPacket::new);
/*    */ 
/*    */   
/* 16 */   private ClientboundUpdateEnabledFeaturesPacket(FriendlyByteBuf input) { this((Set)input.readCollection(java.util.HashSet::new, FriendlyByteBuf::readIdentifier)); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   private void write(FriendlyByteBuf output) { output.writeCollection(this.features, FriendlyByteBuf::writeIdentifier); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public PacketType<ClientboundUpdateEnabledFeaturesPacket> type() { return ConfigurationPacketTypes.CLIENTBOUND_UPDATE_ENABLED_FEATURES; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void handle(ClientConfigurationPacketListener listener) { listener.handleEnabledFeatures(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\configuration\ClientboundUpdateEnabledFeaturesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */