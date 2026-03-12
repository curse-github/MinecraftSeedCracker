package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface Writer {
  void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket.Entry paramEntry);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$Action$Writer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */