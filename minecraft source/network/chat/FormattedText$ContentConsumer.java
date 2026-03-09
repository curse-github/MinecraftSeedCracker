package net.minecraft.network.chat;

import java.util.Optional;

public interface ContentConsumer<T> {
  Optional<T> accept(String paramString);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\FormattedText$ContentConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */