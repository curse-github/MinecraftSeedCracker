/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class SlotDisplays {
/*    */   public static SlotDisplay.Type<?> bootstrap(Registry<SlotDisplay.Type<?>> registry) {
/*  7 */     Registry.register(registry, "empty", SlotDisplay.Empty.TYPE);
/*  8 */     Registry.register(registry, "any_fuel", SlotDisplay.AnyFuel.TYPE);
/*  9 */     Registry.register(registry, "item", SlotDisplay.ItemSlotDisplay.TYPE);
/* 10 */     Registry.register(registry, "item_stack", SlotDisplay.ItemStackSlotDisplay.TYPE);
/* 11 */     Registry.register(registry, "tag", SlotDisplay.TagSlotDisplay.TYPE);
/* 12 */     Registry.register(registry, "smithing_trim", SlotDisplay.SmithingTrimDemoSlotDisplay.TYPE);
/* 13 */     Registry.register(registry, "with_remainder", SlotDisplay.WithRemainder.TYPE);
/* 14 */     return (SlotDisplay.Type)Registry.register(registry, "composite", SlotDisplay.Composite.TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\SlotDisplays.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */