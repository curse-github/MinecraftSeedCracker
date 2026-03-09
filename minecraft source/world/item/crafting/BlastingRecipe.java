/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class BlastingRecipe
/*    */   extends AbstractCookingRecipe {
/*  9 */   public BlastingRecipe(String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) { super(group, category, ingredient, result, experience, cookingTime); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   protected Item furnaceIcon() { return Items.BLAST_FURNACE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public RecipeSerializer<BlastingRecipe> getSerializer() { return RecipeSerializer.BLASTING_RECIPE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public RecipeType<BlastingRecipe> getType() { return RecipeType.BLASTING; }
/*    */ 
/*    */ 
/*    */   
/*    */   public RecipeBookCategory recipeBookCategory() {
/* 29 */     switch (category()) { default: throw new MatchException(null, null);case BLOCKS: case FOOD: case MISC: break; }  return 
/*    */       
/* 31 */       RecipeBookCategories.BLAST_FURNACE_MISC;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\BlastingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */