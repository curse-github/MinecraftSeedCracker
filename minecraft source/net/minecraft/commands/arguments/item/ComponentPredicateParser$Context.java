package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.resources.Identifier;

public interface Context<T, C, P> {
  T forElementType(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws CommandSyntaxException;
  
  Stream<Identifier> listElementTypes();
  
  T forTagType(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws CommandSyntaxException;
  
  Stream<Identifier> listTagTypes();
  
  C lookupComponentType(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws CommandSyntaxException;
  
  Stream<Identifier> listComponentTypes();
  
  T createComponentTest(ImmutableStringReader paramImmutableStringReader, C paramC, Dynamic<?> paramDynamic) throws CommandSyntaxException;
  
  T createComponentTest(ImmutableStringReader paramImmutableStringReader, C paramC);
  
  P lookupPredicateType(ImmutableStringReader paramImmutableStringReader, Identifier paramIdentifier) throws CommandSyntaxException;
  
  Stream<Identifier> listPredicateTypes();
  
  T createPredicateTest(ImmutableStringReader paramImmutableStringReader, P paramP, Dynamic<?> paramDynamic) throws CommandSyntaxException;
  
  T negate(T paramT);
  
  T anyOf(List<T> paramList);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser$Context.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */