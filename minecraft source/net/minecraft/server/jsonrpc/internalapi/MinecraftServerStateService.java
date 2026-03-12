package net.minecraft.server.jsonrpc.internalapi;

import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.level.ServerPlayer;

public interface MinecraftServerStateService {
  boolean isReady();
  
  boolean saveEverything(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, ClientInfo paramClientInfo);
  
  void halt(boolean paramBoolean, ClientInfo paramClientInfo);
  
  void sendSystemMessage(Component paramComponent, ClientInfo paramClientInfo);
  
  void sendSystemMessage(Component paramComponent, boolean paramBoolean, Collection<ServerPlayer> paramCollection, ClientInfo paramClientInfo);
  
  void broadcastSystemMessage(Component paramComponent, boolean paramBoolean, ClientInfo paramClientInfo);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftServerStateService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */