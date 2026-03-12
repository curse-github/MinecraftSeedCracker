package net.minecraft.world.level.entity;

import java.util.UUID;

public interface UUIDLookup<IdentifiedType extends UniquelyIdentifyable> {
  IdentifiedType lookup(UUID paramUUID);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\UUIDLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */