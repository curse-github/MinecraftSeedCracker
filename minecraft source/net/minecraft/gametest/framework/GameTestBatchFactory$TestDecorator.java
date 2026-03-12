package net.minecraft.gametest.framework;

import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface TestDecorator {
  Stream<GameTestInfo> decorate(Holder.Reference<GameTestInstance> paramReference, ServerLevel paramServerLevel);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestBatchFactory$TestDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */