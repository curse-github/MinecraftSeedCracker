package net.minecraft.world.attribute;

@FunctionalInterface
public interface TimeBased<Value> extends EnvironmentAttributeLayer<Value> {
  Value applyTimeBased(Value paramValue, int paramInt);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeLayer$TimeBased.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */