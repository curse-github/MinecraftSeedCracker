package net.minecraft.gametest.framework;

public interface TestReporter {
  void onTestFailed(GameTestInfo paramGameTestInfo);
  
  void onTestSuccess(GameTestInfo paramGameTestInfo);
  
  default void finish() {}
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\TestReporter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */