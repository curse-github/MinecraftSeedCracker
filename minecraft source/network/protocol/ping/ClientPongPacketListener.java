package net.minecraft.network.protocol.ping;

import net.minecraft.network.PacketListener;

public interface ClientPongPacketListener extends PacketListener {
  void handlePongResponse(ClientboundPongResponsePacket paramClientboundPongResponsePacket);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\ping\ClientPongPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */