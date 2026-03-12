package net.minecraft.server.advancements;

import net.minecraft.advancements.AdvancementNode;

@FunctionalInterface
public interface Output {
  void accept(AdvancementNode paramAdvancementNode, boolean paramBoolean);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\advancements\AdvancementVisibilityEvaluator$Output.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */