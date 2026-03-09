package net.minecraft.world.level.block.entity.trialspawner;

import net.minecraft.world.level.Level;

public interface StateAccessor {
  void setState(Level paramLevel, TrialSpawnerState paramTrialSpawnerState);
  
  TrialSpawnerState getState();
  
  void markUpdated();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawner$StateAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */