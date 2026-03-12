package net.minecraft.world.level.gamerules;

public interface GameRuleTypeVisitor {
  default <T> void visit(GameRule<T> gameRule) {}
  
  default void visitBoolean(GameRule<Boolean> gameRule) {}
  
  default void visitInteger(GameRule<Integer> gameRule) {}
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gamerules\GameRuleTypeVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */