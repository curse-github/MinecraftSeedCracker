/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.CraftingBookCategory;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ 
/*    */ public class SpecialRecipeBuilder
/*    */ {
/*    */   private final Function<CraftingBookCategory, Recipe<?>> factory;
/*    */   
/* 15 */   public SpecialRecipeBuilder(Function<CraftingBookCategory, Recipe<?>> factory) { this.factory = factory; }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static SpecialRecipeBuilder special(Function<CraftingBookCategory, Recipe<?>> factory) { return new SpecialRecipeBuilder(factory); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public void save(RecipeOutput output, String name) { save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(name))); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) { output.accept(id, (Recipe)this.factory.apply(CraftingBookCategory.MISC), null); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\SpecialRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */