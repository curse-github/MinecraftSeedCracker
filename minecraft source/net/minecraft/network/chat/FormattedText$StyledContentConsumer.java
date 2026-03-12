package net.minecraft.network.chat;

import java.util.Optional;

public interface StyledContentConsumer<T> {
  Optional<T> accept(Style paramStyle, String paramString);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\FormattedText$StyledContentConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */