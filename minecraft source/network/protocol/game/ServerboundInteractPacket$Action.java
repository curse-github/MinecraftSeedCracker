package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;

interface Action {
  ServerboundInteractPacket.ActionType getType();
  
  void dispatch(ServerboundInteractPacket.Handler paramHandler);
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundInteractPacket$Action.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */