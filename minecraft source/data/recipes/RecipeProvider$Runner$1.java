/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.criterion.ImpossibleTrigger;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements RecipeOutput
/*     */ {
/*     */   null(RecipeProvider.Runner this$0) {}
/*     */   
/*     */   public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancementHolder) {
/*  96 */     if (!allRecipes.add(id)) {
/*  97 */       throw new IllegalStateException("Duplicate recipe " + String.valueOf(id.identifier()));
/*     */     }
/*  99 */     saveRecipe(id, recipe);
/* 100 */     if (advancementHolder != null) {
/* 101 */       saveAdvancement(advancementHolder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public Advancement.Builder advancement() { return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void includeRootAdvancement() {
/* 112 */     AdvancementHolder root = Advancement.Builder.recipeAdvancement().addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance())).build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/* 113 */     saveAdvancement(root);
/*     */   }
/*     */ 
/*     */   
/* 117 */   private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) { tasks.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(id.identifier()))); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   private void saveAdvancement(AdvancementHolder advancementHolder) { tasks.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancementHolder.value(), advancementPathProvider.json(advancementHolder.id()))); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\RecipeProvider$Runner$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */