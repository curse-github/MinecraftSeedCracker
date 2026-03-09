package net.minecraft.world.item.component;

import java.util.List;
import net.minecraft.server.network.Filterable;

public interface BookContent<T, C> {
  List<Filterable<T>> pages();
  
  C withReplacedPages(List<Filterable<T>> paramList);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\BookContent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */