package net.minecraft.world.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;

@FunctionalInterface
public interface PositionFunction {
  Position getDispensePosition(BlockSource paramBlockSource, Direction paramDirection);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\ProjectileItem$PositionFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */