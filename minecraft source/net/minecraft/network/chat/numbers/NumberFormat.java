package net.minecraft.network.chat.numbers;

import net.minecraft.network.chat.MutableComponent;

public interface NumberFormat {
  MutableComponent format(int paramInt);
  
  NumberFormatType<? extends NumberFormat> type();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\numbers\NumberFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */