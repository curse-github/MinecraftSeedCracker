package net.minecraft.world.attribute;

import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface Positional<Value> extends EnvironmentAttributeLayer<Value> {
  Value applyPositional(Value paramValue, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeLayer$Positional.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */