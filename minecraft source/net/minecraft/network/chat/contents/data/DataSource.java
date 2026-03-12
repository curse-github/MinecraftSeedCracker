package net.minecraft.network.chat.contents.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;

public interface DataSource {
  Stream<CompoundTag> getData(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException;
  
  MapCodec<? extends DataSource> codec();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\data\DataSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */