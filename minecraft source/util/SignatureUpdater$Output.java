package net.minecraft.util;

import java.security.SignatureException;

@FunctionalInterface
public interface Output {
  void update(byte[] paramArrayOfByte) throws SignatureException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SignatureUpdater$Output.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */