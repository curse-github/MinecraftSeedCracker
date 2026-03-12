package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;

interface Operation {
  ClientboundBossEventPacket.OperationType getType();
  
  void dispatch(UUID paramUUID, ClientboundBossEventPacket.Handler paramHandler);
  
  void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$Operation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */