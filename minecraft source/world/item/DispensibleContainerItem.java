package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public interface DispensibleContainerItem {
  default void checkExtraContent(LivingEntity user, Level level, ItemStack itemStack, BlockPos pos) {}
  
  boolean emptyContents(LivingEntity paramLivingEntity, Level paramLevel, BlockPos paramBlockPos, BlockHitResult paramBlockHitResult);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\DispensibleContainerItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */