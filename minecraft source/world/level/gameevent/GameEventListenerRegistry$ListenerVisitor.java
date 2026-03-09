package net.minecraft.world.level.gameevent;

import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface ListenerVisitor {
  void visit(GameEventListener paramGameEventListener, Vec3 paramVec3);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\GameEventListenerRegistry$ListenerVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */