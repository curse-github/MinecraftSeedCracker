/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class CampfireCookingRecipe
/*    */   extends AbstractCookingRecipe {
/*  9 */   public CampfireCookingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) { super(group, category, ingredient, result, experience, cookingTime); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected Item furnaceIcon() { return Items.CAMPFIRE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public RecipeSerializer<CampfireCookingRecipe> getSerializer() { return RecipeSerializer.CAMPFIRE_COOKING_RECIPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public RecipeType<CampfireCookingRecipe> getType() { return RecipeType.CAMPFIRE_COOKING; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.CAMPFIRE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\CampfireCookingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */