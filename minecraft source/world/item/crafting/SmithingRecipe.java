/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface SmithingRecipe
/*    */   extends Recipe<SmithingRecipeInput>
/*    */ {
/* 11 */   default RecipeType<SmithingRecipe> getType() { return RecipeType.SMITHING; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean matches(SmithingRecipeInput input, Level level) {
/* 19 */     return (Ingredient.testOptionalIngredient(templateIngredient(), input.template()) && 
/* 20 */       baseIngredient().test(input.base()) && 
/* 21 */       Ingredient.testOptionalIngredient(additionIngredient(), input.addition()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   default RecipeBookCategory recipeBookCategory() { return RecipeBookCategories.SMITHING; }
/*    */   
/*    */   RecipeSerializer<? extends SmithingRecipe> getSerializer();
/*    */   
/*    */   Optional<Ingredient> templateIngredient();
/*    */   
/*    */   Ingredient baseIngredient();
/*    */   
/*    */   Optional<Ingredient> additionIngredient();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\SmithingRecipe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */