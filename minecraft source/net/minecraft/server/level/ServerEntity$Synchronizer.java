package net.minecraft.server.level;

import java.util.function.Predicate;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public interface Synchronizer {
  void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> paramPacket);
  
  void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> paramPacket);
  
  void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> paramPacket, Predicate<ServerPlayer> paramPredicate);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ServerEntity$Synchronizer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */