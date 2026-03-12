package net.minecraft.network.protocol.game;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

interface NodeStub {
  <S> ArgumentBuilder<S, ?> build(CommandBuildContext paramCommandBuildContext, ClientboundCommandsPacket.NodeBuilder<S> paramNodeBuilder);
  
  void write(FriendlyByteBuf paramFriendlyByteBuf);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeStub.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */