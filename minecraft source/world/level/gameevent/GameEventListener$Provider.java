package net.minecraft.world.level.gameevent;

public interface Provider<T extends GameEventListener> {
  T getListener();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\GameEventListener$Provider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */