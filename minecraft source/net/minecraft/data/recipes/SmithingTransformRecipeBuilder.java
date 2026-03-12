/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementRequirements;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.SmithingTransformRecipe;
/*    */ import net.minecraft.world.item.crafting.TransmuteResult;
/*    */ 
/*    */ public class SmithingTransformRecipeBuilder {
/*    */   private final Ingredient template;
/*    */   private final Ingredient base;
/*    */   private final Ingredient addition;
/*    */   
/*    */   public SmithingTransformRecipeBuilder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result) {
/* 27 */     this.criteria = new LinkedHashMap();
/*    */ 
/*    */     
/* 30 */     this.category = category;
/* 31 */     this.template = template;
/* 32 */     this.base = base;
/* 33 */     this.addition = addition;
/* 34 */     this.result = result;
/*    */   }
/*    */   private final RecipeCategory category; private final Item result; private final Map<String, Criterion<?>> criteria;
/*    */   
/* 38 */   public static SmithingTransformRecipeBuilder smithing(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result) { return new SmithingTransformRecipeBuilder(template, base, addition, category, result); }
/*    */ 
/*    */   
/*    */   public SmithingTransformRecipeBuilder unlocks(String name, Criterion<?> criterion) {
/* 42 */     this.criteria.put(name, criterion);
/* 43 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 47 */   public void save(RecipeOutput output, String id) { save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(id))); }
/*    */ 
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 51 */     ensureValid(id);
/*    */ 
/*    */ 
/*    */     
/* 55 */     Advancement.Builder advancement = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
/* 56 */     Objects.requireNonNull(advancement); this.criteria.forEach(advancement::addCriterion);
/*    */ 
/*    */ 
/*    */     
/* 60 */     SmithingTransformRecipe recipe = new SmithingTransformRecipe(Optional.of(this.template), this.base, Optional.of(this.addition), new TransmuteResult(this.result));
/*    */ 
/*    */     
/* 63 */     output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*    */   }
/*    */   
/*    */   private void ensureValid(ResourceKey<Recipe<?>> id) {
/* 67 */     if (this.criteria.isEmpty())
/* 68 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\SmithingTransformRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */