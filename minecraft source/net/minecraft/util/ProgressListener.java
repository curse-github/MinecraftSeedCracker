package net.minecraft.util;

import net.minecraft.network.chat.Component;

public interface ProgressListener {
  void progressStartNoAbort(Component paramComponent);
  
  void progressStart(Component paramComponent);
  
  void progressStage(Component paramComponent);
  
  void progressStagePercentage(int paramInt);
  
  void stop();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ProgressListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */