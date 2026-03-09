package net.minecraft.commands.arguments;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface Result<T> extends Predicate<Holder<T>> {
  Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap();
  
  <E> Optional<Result<E>> cast(ResourceKey<? extends Registry<E>> paramResourceKey);
  
  String asPrintable();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\ResourceOrTagArgument$Result.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */