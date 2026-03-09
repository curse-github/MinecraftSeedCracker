package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
interface Callback {
  void accept(List<ItemStack> paramList) throws CommandSyntaxException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\LootCommand$Callback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */