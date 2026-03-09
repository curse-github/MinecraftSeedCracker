package net.minecraft.network.protocol.game;

import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.resources.Identifier;

public interface NodeInspector<S> {
  Identifier suggestionId(ArgumentCommandNode<S, ?> paramArgumentCommandNode);
  
  boolean isExecutable(CommandNode<S> paramCommandNode);
  
  boolean isRestricted(CommandNode<S> paramCommandNode);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeInspector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */