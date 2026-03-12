package net.minecraft.server.network;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

@FunctionalInterface
public interface MessageEncoder {
  JsonObject encode(GameProfile paramGameProfile, String paramString);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\network\ServerTextFilter$MessageEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */