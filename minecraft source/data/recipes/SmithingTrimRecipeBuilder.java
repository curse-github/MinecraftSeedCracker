/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementRequirements;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.Criterion;
/*    */ import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.SmithingTrimRecipe;
/*    */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*    */ 
/*    */ public class SmithingTrimRecipeBuilder {
/*    */   private final RecipeCategory category;
/*    */   private final Ingredient template;
/*    */   private final Ingredient base;
/*    */   
/*    */   public SmithingTrimRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern) {
/* 24 */     this.criteria = new LinkedHashMap();
/*    */ 
/*    */     
/* 27 */     this.category = category;
/* 28 */     this.template = template;
/* 29 */     this.base = base;
/* 30 */     this.addition = addition;
/* 31 */     this.pattern = pattern;
/*    */   }
/*    */   private final Ingredient addition; private final Holder<TrimPattern> pattern; private final Map<String, Criterion<?>> criteria;
/*    */   
/* 35 */   public static SmithingTrimRecipeBuilder smithingTrim(Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern, RecipeCategory category) { return new SmithingTrimRecipeBuilder(category, template, base, addition, pattern); }
/*    */ 
/*    */   
/*    */   public SmithingTrimRecipeBuilder unlocks(String name, Criterion<?> criterion) {
/* 39 */     this.criteria.put(name, criterion);
/* 40 */     return this;
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 44 */     ensureValid(id);
/*    */ 
/*    */ 
/*    */     
/* 48 */     Advancement.Builder advancement = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
/* 49 */     Objects.requireNonNull(advancement); this.criteria.forEach(advancement::addCriterion);
/* 50 */     SmithingTrimRecipe recipe = new SmithingTrimRecipe(this.template, this.base, this.addition, this.pattern);
/* 51 */     output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*    */   }
/*    */   
/*    */   private void ensureValid(ResourceKey<Recipe<?>> id) {
/* 55 */     if (this.criteria.isEmpty())
/* 56 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\SmithingTrimRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */