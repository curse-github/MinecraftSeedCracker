package net.minecraft.world.item.component;

import java.util.function.Consumer;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

public interface TooltipProvider {
  void addToTooltip(Item.TooltipContext paramTooltipContext, Consumer<Component> paramConsumer, TooltipFlag paramTooltipFlag, DataComponentGetter paramDataComponentGetter);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\component\TooltipProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */