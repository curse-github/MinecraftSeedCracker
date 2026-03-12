package net.minecraft.world.waypoints;

public interface Connection {
  void connect();
  
  void disconnect();
  
  void update();
  
  boolean isBroken();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\WaypointTransmitter$Connection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */