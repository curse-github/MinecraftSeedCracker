/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class SmokingRecipe
/*    */   extends AbstractCookingRecipe {
/*  9 */   public SmokingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) { super(group, category, ingredient, result, experience, cookingTime); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected Item furnaceIcon() { return Items.SMOKER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public RecipeType<SmokingRecipe> getType() { return RecipeType.SMOKING; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public RecipeSerializer<SmokingRecipe> getSerializer() { return RecipeSerializer.SMOKING_RECIPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.SMOKER_FOOD; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SmokingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */