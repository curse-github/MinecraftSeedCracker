package net.minecraft.advancements;

public interface Listener {
  void onAddAdvancementRoot(AdvancementNode paramAdvancementNode);
  
  void onRemoveAdvancementRoot(AdvancementNode paramAdvancementNode);
  
  void onAddAdvancementTask(AdvancementNode paramAdvancementNode);
  
  void onRemoveAdvancementTask(AdvancementNode paramAdvancementNode);
  
  void onAdvancementsCleared();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\AdvancementTree$Listener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */