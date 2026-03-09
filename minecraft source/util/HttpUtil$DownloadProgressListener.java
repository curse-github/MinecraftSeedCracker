package net.minecraft.util;

import java.util.OptionalLong;

public interface DownloadProgressListener {
  void requestStart();
  
  void downloadStart(OptionalLong paramOptionalLong);
  
  void downloadedBytes(long paramLong);
  
  void requestFinished(boolean paramBoolean);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\HttpUtil$DownloadProgressListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */