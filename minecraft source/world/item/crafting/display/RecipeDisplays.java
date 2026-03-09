/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class RecipeDisplays {
/*    */   public static RecipeDisplay.Type<?> bootstrap(Registry<RecipeDisplay.Type<?>> registry) {
/*  7 */     Registry.register(registry, "crafting_shapeless", ShapelessCraftingRecipeDisplay.TYPE);
/*  8 */     Registry.register(registry, "crafting_shaped", ShapedCraftingRecipeDisplay.TYPE);
/*  9 */     Registry.register(registry, "furnace", FurnaceRecipeDisplay.TYPE);
/* 10 */     Registry.register(registry, "stonecutter", StonecutterRecipeDisplay.TYPE);
/* 11 */     return (RecipeDisplay.Type)Registry.register(registry, "smithing", SmithingRecipeDisplay.TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\display\RecipeDisplays.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */