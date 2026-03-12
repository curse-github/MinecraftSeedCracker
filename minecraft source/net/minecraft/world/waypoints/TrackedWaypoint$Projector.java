package net.minecraft.world.waypoints;

import net.minecraft.world.phys.Vec3;

public interface Projector {
  Vec3 projectPointToScreen(Vec3 paramVec3);
  
  double projectHorizonToScreen();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\waypoints\TrackedWaypoint$Projector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */