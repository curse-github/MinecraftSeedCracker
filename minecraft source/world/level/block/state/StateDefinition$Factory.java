package net.minecraft.world.level.block.state;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.world.level.block.state.properties.Property;

public interface Factory<O, S> {
  S create(O paramO, Reference2ObjectArrayMap<Property<?>, Comparable<?>> paramReference2ObjectArrayMap, MapCodec<S> paramMapCodec);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\StateDefinition$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */