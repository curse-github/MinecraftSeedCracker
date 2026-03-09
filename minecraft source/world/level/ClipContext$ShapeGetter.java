package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface ShapeGetter {
  VoxelShape get(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ClipContext$ShapeGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */