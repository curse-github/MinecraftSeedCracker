/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ public class PlacementInfo
/*    */ {
/*    */   public static final int EMPTY_SLOT = -1;
/* 13 */   public static final PlacementInfo NOT_PLACEABLE = new PlacementInfo(
/* 14 */       List.of(), 
/* 15 */       IntList.of());
/*    */   
/*    */   private final List<Ingredient> ingredients;
/*    */   
/*    */   private final IntList slotsToIngredientIndex;
/*    */   
/*    */   private PlacementInfo(List<Ingredient> ingredients, IntList slotsToIngredientIndex) {
/* 22 */     this.ingredients = ingredients;
/* 23 */     this.slotsToIngredientIndex = slotsToIngredientIndex;
/*    */   }
/*    */   
/*    */   public static PlacementInfo create(Ingredient ingredient) {
/* 27 */     if (ingredient.isEmpty()) {
/* 28 */       return NOT_PLACEABLE;
/*    */     }
/* 30 */     return new PlacementInfo(List.of(ingredient), IntList.of(0));
/*    */   }
/*    */   
/*    */   public static PlacementInfo createFromOptionals(List<Optional<Ingredient>> ingredients) {
/* 34 */     int ingredientCount = ingredients.size();
/* 35 */     List<Ingredient> presentIngredients = new ArrayList<Ingredient>(ingredientCount);
/* 36 */     IntArrayList intArrayList = new IntArrayList(ingredientCount);
/*    */     
/* 38 */     int placementIndex = 0;
/* 39 */     for (Optional<Ingredient> maybeIngredient : ingredients) {
/* 40 */       if (maybeIngredient.isPresent()) {
/* 41 */         Ingredient ingredient = (Ingredient)maybeIngredient.get();
/* 42 */         if (ingredient.isEmpty()) {
/* 43 */           return NOT_PLACEABLE;
/*    */         }
/* 45 */         presentIngredients.add(ingredient);
/* 46 */         intArrayList.add(placementIndex++); continue;
/*    */       } 
/* 48 */       intArrayList.add(-1);
/*    */     } 
/*    */ 
/*    */     
/* 52 */     return new PlacementInfo(presentIngredients, intArrayList);
/*    */   }
/*    */   
/*    */   public static PlacementInfo create(List<Ingredient> ingredients) {
/* 56 */     int ingredientCount = ingredients.size();
/* 57 */     IntArrayList intArrayList = new IntArrayList(ingredientCount);
/*    */     
/* 59 */     for (int i = 0; i < ingredientCount; i++) {
/* 60 */       Ingredient ingredient = (Ingredient)ingredients.get(i);
/* 61 */       if (ingredient.isEmpty()) {
/* 62 */         return NOT_PLACEABLE;
/*    */       }
/* 64 */       intArrayList.add(i);
/*    */     } 
/*    */     
/* 67 */     return new PlacementInfo(ingredients, intArrayList);
/*    */   }
/*    */ 
/*    */   
/* 71 */   public IntList slotsToIngredientIndex() { return this.slotsToIngredientIndex; }
/*    */ 
/*    */ 
/*    */   
/* 75 */   public List<Ingredient> ingredients() { return this.ingredients; }
/*    */ 
/*    */ 
/*    */   
/* 79 */   public boolean isImpossibleToPlace() { return this.slotsToIngredientIndex.isEmpty(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\PlacementInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */