package net.minecraft.server.commands.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

public interface DataAccessor {
  void setData(CompoundTag paramCompoundTag) throws CommandSyntaxException;
  
  CompoundTag getData() throws CommandSyntaxException;
  
  Component getModifiedSuccess();
  
  Component getPrintSuccess(Tag paramTag);
  
  Component getPrintSuccess(NbtPathArgument.NbtPath paramNbtPath, double paramDouble, int paramInt);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\data\DataAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */