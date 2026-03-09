package net.minecraft.util.eventlog;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public interface File {
  Path path();
  
  EventLogDirectory.FileId id();
  
  Reader openReader() throws IOException;
  
  EventLogDirectory.CompressedFile compress() throws IOException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\eventlog\EventLogDirectory$File.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */