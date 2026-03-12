/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class SmeltingRecipe
/*    */   extends AbstractCookingRecipe {
/*  9 */   public SmeltingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) { super(group, category, ingredient, result, experience, cookingTime); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected Item furnaceIcon() { return Items.FURNACE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public RecipeSerializer<SmeltingRecipe> getSerializer() { return RecipeSerializer.SMELTING_RECIPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public RecipeType<SmeltingRecipe> getType() { return RecipeType.SMELTING; }
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     switch (category()) { default: throw new MatchException(null, null);case BLOCKS: case FOOD: case MISC: break; }  return 
/*    */ 
/*    */       
/* 32 */       RecipeBookCategories.FURNACE_MISC;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SmeltingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */