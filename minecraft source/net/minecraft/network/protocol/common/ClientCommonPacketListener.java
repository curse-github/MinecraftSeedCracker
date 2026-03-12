package net.minecraft.network.protocol.common;

import net.minecraft.network.protocol.cookie.ClientCookiePacketListener;

public interface ClientCommonPacketListener extends ClientCookiePacketListener {
  void handleKeepAlive(ClientboundKeepAlivePacket paramClientboundKeepAlivePacket);
  
  void handlePing(ClientboundPingPacket paramClientboundPingPacket);
  
  void handleCustomPayload(ClientboundCustomPayloadPacket paramClientboundCustomPayloadPacket);
  
  void handleDisconnect(ClientboundDisconnectPacket paramClientboundDisconnectPacket);
  
  void handleResourcePackPush(ClientboundResourcePackPushPacket paramClientboundResourcePackPushPacket);
  
  void handleResourcePackPop(ClientboundResourcePackPopPacket paramClientboundResourcePackPopPacket);
  
  void handleUpdateTags(ClientboundUpdateTagsPacket paramClientboundUpdateTagsPacket);
  
  void handleStoreCookie(ClientboundStoreCookiePacket paramClientboundStoreCookiePacket);
  
  void handleTransfer(ClientboundTransferPacket paramClientboundTransferPacket);
  
  void handleCustomReportDetails(ClientboundCustomReportDetailsPacket paramClientboundCustomReportDetailsPacket);
  
  void handleServerLinks(ClientboundServerLinksPacket paramClientboundServerLinksPacket);
  
  void handleClearDialog(ClientboundClearDialogPacket paramClientboundClearDialogPacket);
  
  void handleShowDialog(ClientboundShowDialogPacket paramClientboundShowDialogPacket);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientCommonPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */