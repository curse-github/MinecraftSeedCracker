package net.minecraft.server.players;

import java.util.Optional;
import java.util.UUID;

public interface UserNameToIdResolver {
  void add(NameAndId paramNameAndId);
  
  Optional<NameAndId> get(String paramString);
  
  Optional<NameAndId> get(UUID paramUUID);
  
  void resolveOfflineUsers(boolean paramBoolean);
  
  void save();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\players\UserNameToIdResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */