package net.minecraft.commands.synchronization;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

public interface ArgumentTypeInfo<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> {
  void serializeToNetwork(T paramT, FriendlyByteBuf paramFriendlyByteBuf);
  
  T deserializeFromNetwork(FriendlyByteBuf paramFriendlyByteBuf);
  
  void serializeToJson(T paramT, JsonObject paramJsonObject);
  
  T unpack(A paramA);
  
  public static interface Template<A extends ArgumentType<?>> {
    A instantiate(CommandBuildContext param1CommandBuildContext);
    
    ArgumentTypeInfo<A, ?> type();
  }
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\synchronization\ArgumentTypeInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */