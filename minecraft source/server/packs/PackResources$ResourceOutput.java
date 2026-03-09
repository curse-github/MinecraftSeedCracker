package net.minecraft.server.packs;

import java.io.InputStream;
import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.IoSupplier;

@FunctionalInterface
public interface ResourceOutput extends BiConsumer<Identifier, IoSupplier<InputStream>> {}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\PackResources$ResourceOutput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */