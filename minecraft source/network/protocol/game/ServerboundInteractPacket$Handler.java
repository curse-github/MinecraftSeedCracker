package net.minecraft.network.protocol.game;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

public interface Handler {
  void onInteraction(InteractionHand paramInteractionHand);
  
  void onInteraction(InteractionHand paramInteractionHand, Vec3 paramVec3);
  
  void onAttack();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket$Handler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */