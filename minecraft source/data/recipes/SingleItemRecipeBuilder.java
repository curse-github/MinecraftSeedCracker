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
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.SingleItemRecipe;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class SingleItemRecipeBuilder
/*    */   implements RecipeBuilder
/*    */ {
/*    */   private final RecipeCategory category;
/*    */   private final Item result;
/*    */   private final Ingredient ingredient;
/*    */   
/*    */   public SingleItemRecipeBuilder(RecipeCategory category, SingleItemRecipe.Factory<?> factory, Ingredient ingredient, ItemLike result, int count) {
/* 27 */     this.criteria = new LinkedHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     this.category = category;
/* 33 */     this.factory = factory;
/* 34 */     this.result = result.asItem();
/* 35 */     this.ingredient = ingredient;
/* 36 */     this.count = count;
/*    */   }
/*    */   private final int count; private final Map<String, Criterion<?>> criteria; private String group; private final SingleItemRecipe.Factory<?> factory;
/*    */   
/* 40 */   public static SingleItemRecipeBuilder stonecutting(Ingredient ingredient, RecipeCategory category, ItemLike result) { return new SingleItemRecipeBuilder(category, net.minecraft.world.item.crafting.StonecutterRecipe::new, ingredient, result, 1); }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public static SingleItemRecipeBuilder stonecutting(Ingredient ingredient, RecipeCategory category, ItemLike result, int count) { return new SingleItemRecipeBuilder(category, net.minecraft.world.item.crafting.StonecutterRecipe::new, ingredient, result, count); }
/*    */ 
/*    */ 
/*    */   
/*    */   public SingleItemRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/* 49 */     this.criteria.put(name, criterion);
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public SingleItemRecipeBuilder group(String group) {
/* 55 */     this.group = group;
/* 56 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public Item getResult() { return this.result; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 66 */     ensureValid(id);
/*    */ 
/*    */ 
/*    */     
/* 70 */     Advancement.Builder advancement = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
/* 71 */     Objects.requireNonNull(advancement); this.criteria.forEach(advancement::addCriterion);
/* 72 */     SingleItemRecipe recipe = this.factory.create(
/* 73 */         (String)Objects.requireNonNullElse(this.group, ""), this.ingredient, new ItemStack(this.result, this.count));
/*    */ 
/*    */ 
/*    */     
/* 77 */     output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*    */   }
/*    */   
/*    */   private void ensureValid(ResourceKey<Recipe<?>> id) {
/* 81 */     if (this.criteria.isEmpty())
/* 82 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\SingleItemRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */