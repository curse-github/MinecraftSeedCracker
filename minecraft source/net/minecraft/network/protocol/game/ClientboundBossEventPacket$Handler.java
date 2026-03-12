package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;

public interface Handler {
  default void add(UUID id, Component name, float progress, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {}
  
  default void remove(UUID id) {}
  
  default void updateProgress(UUID id, float progress) {}
  
  default void updateName(UUID id, Component name) {}
  
  default void updateStyle(UUID id, BossEvent.BossBarColor color, BossEvent.BossBarOverlay overlay) {}
  
  default void updateProperties(UUID id, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {}
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$Handler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */