/*   */ package net.minecraft.network.protocol.configuration;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.common.ClientCommonPacketListener;
/*   */ 
/*   */ public interface ClientConfigurationPacketListener
/*   */   extends ClientCommonPacketListener
/*   */ {
/* 9 */   default ConnectionProtocol protocol() { return ConnectionProtocol.CONFIGURATION; }
/*   */   
/*   */   void handleCodeOfConduct(ClientboundCodeOfConductPacket paramClientboundCodeOfConductPacket);
/*   */   
/*   */   void handleConfigurationFinished(ClientboundFinishConfigurationPacket paramClientboundFinishConfigurationPacket);
/*   */   
/*   */   void handleRegistryData(ClientboundRegistryDataPacket paramClientboundRegistryDataPacket);
/*   */   
/*   */   void handleEnabledFeatures(ClientboundUpdateEnabledFeaturesPacket paramClientboundUpdateEnabledFeaturesPacket);
/*   */   
/*   */   void handleSelectKnownPacks(ClientboundSelectKnownPacks paramClientboundSelectKnownPacks);
/*   */   
/*   */   void handleResetChat(ClientboundResetChatPacket paramClientboundResetChatPacket);
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\configuration\ClientConfigurationPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */