/*    */ package net.minecraft.recipebook;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.ShapedRecipe;
/*    */ 
/*    */ public interface PlaceRecipeHelper
/*    */ {
/*    */   static <T> void placeRecipe(int gridWidth, int gridHeight, Recipe<?> recipe, Iterable<T> entries, Output<T> output) {
/* 11 */     if (recipe instanceof ShapedRecipe) { ShapedRecipe shapedRecipe = (ShapedRecipe)recipe;
/* 12 */       placeRecipe(gridWidth, gridHeight, shapedRecipe.getWidth(), shapedRecipe.getHeight(), entries, output); }
/*    */     else
/* 14 */     { placeRecipe(gridWidth, gridHeight, gridWidth, gridHeight, entries, output); }
/*    */   
/*    */   }
/*    */   
/*    */   static <T> void placeRecipe(int gridWidth, int gridHeight, int recipeWidth, int recipeHeight, Iterable<T> entries, Output<T> output) {
/* 19 */     Iterator<T> iterator = entries.iterator();
/*    */     
/* 21 */     int gridIndex = 0;
/* 22 */     for (int gridYPos = 0; gridYPos < gridHeight; gridYPos++) {
/* 23 */       boolean shouldCenterRecipe = (recipeHeight < gridHeight / 2.0F);
/* 24 */       int startPosCenterRecipe = Mth.floor(gridHeight / 2.0F - recipeHeight / 2.0F);
/*    */       
/* 26 */       if (shouldCenterRecipe && startPosCenterRecipe > gridYPos) {
/* 27 */         gridIndex += gridWidth;
/* 28 */         gridYPos++;
/*    */       } 
/*    */       
/* 31 */       for (int gridXPos = 0; gridXPos < gridWidth; gridXPos++) {
/* 32 */         if (!iterator.hasNext()) {
/*    */           return;
/*    */         }
/*    */         
/* 36 */         shouldCenterRecipe = (recipeWidth < gridWidth / 2.0F);
/* 37 */         startPosCenterRecipe = Mth.floor(gridWidth / 2.0F - recipeWidth / 2.0F);
/* 38 */         int totalRecipeWidthInGrid = recipeWidth;
/* 39 */         boolean addIngredientToSlot = (gridXPos < recipeWidth);
/* 40 */         if (shouldCenterRecipe) {
/* 41 */           totalRecipeWidthInGrid = startPosCenterRecipe + recipeWidth;
/* 42 */           addIngredientToSlot = (startPosCenterRecipe <= gridXPos && gridXPos < startPosCenterRecipe + recipeWidth);
/*    */         } 
/*    */ 
/*    */         
/* 46 */         if (addIngredientToSlot) {
/* 47 */           output.addItemToSlot(iterator.next(), gridIndex, gridXPos, gridYPos);
/* 48 */         } else if (totalRecipeWidthInGrid == gridXPos) {
/* 49 */           gridIndex += gridWidth - gridXPos;
/*    */           
/*    */           break;
/*    */         } 
/* 53 */         gridIndex++;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Output<T> {
/*    */     void addItemToSlot(T param1T, int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\recipebook\PlaceRecipeHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */