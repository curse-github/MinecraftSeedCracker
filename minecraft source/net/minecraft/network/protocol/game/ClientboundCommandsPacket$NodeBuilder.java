package net.minecraft.network.protocol.game;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.resources.Identifier;

public interface NodeBuilder<S> {
  ArgumentBuilder<S, ?> createLiteral(String paramString);
  
  ArgumentBuilder<S, ?> createArgument(String paramString, ArgumentType<?> paramArgumentType, Identifier paramIdentifier);
  
  ArgumentBuilder<S, ?> configure(ArgumentBuilder<S, ?> paramArgumentBuilder, boolean paramBoolean1, boolean paramBoolean2);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$NodeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */