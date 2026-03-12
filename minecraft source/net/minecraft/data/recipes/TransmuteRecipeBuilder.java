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
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.TransmuteRecipe;
/*    */ import net.minecraft.world.item.crafting.TransmuteResult;
/*    */ 
/*    */ public class TransmuteRecipeBuilder
/*    */   implements RecipeBuilder {
/*    */   private final RecipeCategory category;
/*    */   private final Holder<Item> result;
/*    */   private final Ingredient input;
/*    */   
/*    */   private TransmuteRecipeBuilder(RecipeCategory category, Holder<Item> result, Ingredient input, Ingredient material) {
/* 26 */     this.criteria = new LinkedHashMap();
/*    */ 
/*    */ 
/*    */     
/* 30 */     this.category = category;
/* 31 */     this.result = result;
/* 32 */     this.input = input;
/* 33 */     this.material = material;
/*    */   }
/*    */   private final Ingredient material; private final Map<String, Criterion<?>> criteria; private String group;
/*    */   
/* 37 */   public static TransmuteRecipeBuilder transmute(RecipeCategory category, Ingredient input, Ingredient material, Item result) { return new TransmuteRecipeBuilder(category, result.builtInRegistryHolder(), input, material); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TransmuteRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/* 42 */     this.criteria.put(name, criterion);
/* 43 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public TransmuteRecipeBuilder group(String group) {
/* 48 */     this.group = group;
/* 49 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public Item getResult() { return (Item)this.result.value(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 59 */     ensureValid(id);
/*    */ 
/*    */ 
/*    */     
/* 63 */     Advancement.Builder advancement = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
/* 64 */     Objects.requireNonNull(advancement); this.criteria.forEach(advancement::addCriterion);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 70 */     TransmuteRecipe recipe = new TransmuteRecipe((String)Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), this.input, this.material, new TransmuteResult((Item)this.result.value()));
/*    */     
/* 72 */     output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*    */   }
/*    */   
/*    */   private void ensureValid(ResourceKey<Recipe<?>> id) {
/* 76 */     if (this.criteria.isEmpty())
/* 77 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\TransmuteRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */