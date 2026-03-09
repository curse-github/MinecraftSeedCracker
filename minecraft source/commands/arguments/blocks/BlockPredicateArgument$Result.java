package net.minecraft.commands.arguments.blocks;

import java.util.function.Predicate;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public interface Result extends Predicate<BlockInWorld> {
  boolean requiresNbt();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockPredicateArgument$Result.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */