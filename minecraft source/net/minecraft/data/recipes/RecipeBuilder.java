/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.crafting.CraftingBookCategory;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public interface RecipeBuilder
/*    */ {
/* 15 */   public static final Identifier ROOT_RECIPE_ADVANCEMENT = Identifier.withDefaultNamespace("recipes/root");
/*    */ 
/*    */   
/*    */   RecipeBuilder unlockedBy(String paramString, Criterion<?> paramCriterion);
/*    */   
/*    */   RecipeBuilder group(String paramString);
/*    */   
/*    */   Item getResult();
/*    */   
/*    */   void save(RecipeOutput paramRecipeOutput, ResourceKey<Recipe<?>> paramResourceKey);
/*    */   
/* 26 */   default void save(RecipeOutput output) { save(output, ResourceKey.create(Registries.RECIPE, getDefaultRecipeId(getResult()))); }
/*    */ 
/*    */   
/*    */   default void save(RecipeOutput output, String id) {
/* 30 */     Identifier key = getDefaultRecipeId(getResult());
/* 31 */     Identifier resourceId = Identifier.parse(id);
/* 32 */     if (resourceId.equals(key)) {
/* 33 */       throw new IllegalStateException("Recipe " + id + " should remove its 'save' argument as it is equal to default one");
/*    */     }
/* 35 */     save(output, ResourceKey.create(Registries.RECIPE, resourceId));
/*    */   }
/*    */ 
/*    */   
/* 39 */   static Identifier getDefaultRecipeId(ItemLike itemLike) { return BuiltInRegistries.ITEM.getKey(itemLike.asItem()); }
/*    */ 
/*    */   
/*    */   static CraftingBookCategory determineBookCategory(RecipeCategory category) {
/* 43 */     switch (category) { case BUILDING_BLOCKS: case TOOLS: case COMBAT: case REDSTONE:  }  return 
/*    */ 
/*    */ 
/*    */       
/* 47 */       CraftingBookCategory.MISC;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\RecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */