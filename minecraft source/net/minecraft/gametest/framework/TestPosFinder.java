package net.minecraft.gametest.framework;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface TestPosFinder {
  Stream<BlockPos> findTestPos();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestPosFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */