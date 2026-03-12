package net.minecraft.util;

import java.security.Key;

interface ByteArrayToKeyFunction<T extends Key> {
  T apply(byte[] paramArrayOfByte) throws CryptException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\Crypt$ByteArrayToKeyFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */