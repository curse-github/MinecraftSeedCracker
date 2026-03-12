package net.minecraft.server.packs.repository;

import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;

public interface ResourcesSupplier {
  PackResources openPrimary(PackLocationInfo paramPackLocationInfo);
  
  PackResources openFull(PackLocationInfo paramPackLocationInfo, Pack.Metadata paramMetadata);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\Pack$ResourcesSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */