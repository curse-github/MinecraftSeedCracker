package net.minecraft.commands.arguments;

import net.minecraft.network.chat.MessageSignature;

@FunctionalInterface
public interface Signer {
  MessageSignature sign(String paramString);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ArgumentSignatures$Signer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */