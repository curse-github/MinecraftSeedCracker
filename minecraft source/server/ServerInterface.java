package net.minecraft.server;

import net.minecraft.server.dedicated.DedicatedServerProperties;

public interface ServerInterface extends ServerInfo {
  DedicatedServerProperties getProperties();
  
  String getServerIp();
  
  int getServerPort();
  
  String getServerName();
  
  String[] getPlayerNames();
  
  String getLevelIdName();
  
  String getPluginNames();
  
  String runCommand(String paramString);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\ServerInterface.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */