package net.minecraft.network.protocol.common;

import net.minecraft.network.protocol.cookie.ServerCookiePacketListener;

public interface ServerCommonPacketListener extends ServerCookiePacketListener {
  void handleKeepAlive(ServerboundKeepAlivePacket paramServerboundKeepAlivePacket);
  
  void handlePong(ServerboundPongPacket paramServerboundPongPacket);
  
  void handleCustomPayload(ServerboundCustomPayloadPacket paramServerboundCustomPayloadPacket);
  
  void handleResourcePackResponse(ServerboundResourcePackPacket paramServerboundResourcePackPacket);
  
  void handleClientInformation(ServerboundClientInformationPacket paramServerboundClientInformationPacket);
  
  void handleCustomClickAction(ServerboundCustomClickActionPacket paramServerboundCustomClickActionPacket);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ServerCommonPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */