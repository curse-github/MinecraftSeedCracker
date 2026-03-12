package net.minecraft.network;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.util.VisibleForDebug;

public interface Details {
  ConnectionProtocol id();
  
  PacketFlow flow();
  
  @VisibleForDebug
  void listPackets(PacketVisitor paramPacketVisitor);
  
  @FunctionalInterface
  public static interface PacketVisitor {
    void accept(PacketType<?> param2PacketType, int param2Int);
  }
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\ProtocolInfo$Details.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */