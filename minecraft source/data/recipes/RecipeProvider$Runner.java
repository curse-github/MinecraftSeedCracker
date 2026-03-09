/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.criterion.ImpossibleTrigger;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
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
/*     */ public abstract class Runner
/*     */   implements DataProvider
/*     */ {
/*     */   private final PackOutput packOutput;
/*     */   private final CompletableFuture<HolderLookup.Provider> registries;
/*     */   
/*     */   protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
/*  80 */     this.packOutput = packOutput;
/*  81 */     this.registries = registries;
/*     */   }
/*     */ 
/*     */   
/*     */   public final CompletableFuture<?> run(final CachedOutput cache) {
/*  86 */     return this.registries.thenCompose(registries -> {
/*  87 */           final PackOutput.PathProvider recipePathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
/*  88 */           final PackOutput.PathProvider advancementPathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
/*     */           
/*  90 */           final Set<ResourceKey<Recipe<?>>> allRecipes = Sets.newHashSet();
/*  91 */           final List<CompletableFuture<?>> tasks = new ArrayList<CompletableFuture<?>>();
/*     */           
/*  93 */           RecipeOutput recipeOutput = new RecipeOutput(this)
/*     */             {
/*     */               public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancementHolder) {
/*  96 */                 if (!allRecipes.add(id)) {
/*  97 */                   throw new IllegalStateException("Duplicate recipe " + String.valueOf(id.identifier()));
/*     */                 }
/*  99 */                 saveRecipe(id, recipe);
/* 100 */                 if (advancementHolder != null) {
/* 101 */                   saveAdvancement(advancementHolder);
/*     */                 }
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 107 */               public Advancement.Builder advancement() { return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT); }
/*     */ 
/*     */ 
/*     */               
/*     */               public void includeRootAdvancement() {
/* 112 */                 AdvancementHolder root = Advancement.Builder.recipeAdvancement().addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance())).build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/* 113 */                 saveAdvancement(root);
/*     */               }
/*     */ 
/*     */               
/* 117 */               private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) { tasks.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(id.identifier()))); }
/*     */ 
/*     */               
/*     */               private void saveAdvancement(AdvancementHolder advancementHolder) {
/* 121 */                 tasks.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancementHolder.value(), advancementPathProvider.json(advancementHolder.id())));
/*     */               }
/*     */             };
/*     */           
/* 125 */           createRecipeProvider(registries, recipeOutput).buildRecipes();
/* 126 */           return CompletableFuture.allOf((CompletableFuture[])tasks.toArray(()));
/*     */         });
/*     */   }
/*     */   
/*     */   protected abstract RecipeProvider createRecipeProvider(HolderLookup.Provider paramProvider, RecipeOutput paramRecipeOutput);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\RecipeProvider$Runner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */