package net.minecraft.network;

import net.minecraft.network.protocol.PacketType;

@FunctionalInterface
public interface PacketVisitor {
  void accept(PacketType<?> paramPacketType, int paramInt);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\ProtocolInfo$Details$PacketVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */