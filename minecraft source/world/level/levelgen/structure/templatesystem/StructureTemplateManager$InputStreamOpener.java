package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
interface InputStreamOpener {
  InputStream open() throws IOException;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplateManager$InputStreamOpener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */