/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.ShapelessRecipe;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class ShapelessRecipeBuilder implements RecipeBuilder {
/*     */   private final HolderGetter<Item> items;
/*     */   private final RecipeCategory category;
/*     */   private final ItemStack result;
/*     */   
/*     */   private ShapelessRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemStack result) {
/*  29 */     this.ingredients = new ArrayList();
/*  30 */     this.criteria = new LinkedHashMap();
/*     */ 
/*     */ 
/*     */     
/*  34 */     this.items = items;
/*  35 */     this.category = category;
/*  36 */     this.result = result;
/*     */   }
/*     */   private final List<Ingredient> ingredients; private final Map<String, Criterion<?>> criteria; private String group;
/*     */   
/*  40 */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemStack result) { return new ShapelessRecipeBuilder(items, category, result); }
/*     */ 
/*     */ 
/*     */   
/*  44 */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemLike item) { return shapeless(items, category, item, 1); }
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static ShapelessRecipeBuilder shapeless(HolderGetter<Item> items, RecipeCategory category, ItemLike item, int count) { return new ShapelessRecipeBuilder(items, category, item.asItem().getDefaultInstance().copyWithCount(count)); }
/*     */ 
/*     */ 
/*     */   
/*  52 */   public ShapelessRecipeBuilder requires(TagKey<Item> tag) { return requires(Ingredient.of(this.items.getOrThrow(tag))); }
/*     */ 
/*     */ 
/*     */   
/*  56 */   public ShapelessRecipeBuilder requires(ItemLike item) { return requires(item, 1); }
/*     */ 
/*     */   
/*     */   public ShapelessRecipeBuilder requires(ItemLike item, int count) {
/*  60 */     for (int i = 0; i < count; i++) {
/*  61 */       requires(Ingredient.of(item));
/*     */     }
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */   
/*  67 */   public ShapelessRecipeBuilder requires(Ingredient ingredient) { return requires(ingredient, 1); }
/*     */ 
/*     */   
/*     */   public ShapelessRecipeBuilder requires(Ingredient ingredient, int count) {
/*  71 */     for (int i = 0; i < count; i++) {
/*  72 */       this.ingredients.add(ingredient);
/*     */     }
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShapelessRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/*  79 */     this.criteria.put(name, criterion);
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ShapelessRecipeBuilder group(String group) {
/*  85 */     this.group = group;
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Item getResult() { return this.result.getItem(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/*  96 */     ensureValid(id);
/*     */ 
/*     */ 
/*     */     
/* 100 */     Advancement.Builder advancement = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
/* 101 */     Objects.requireNonNull(advancement); this.criteria.forEach(advancement::addCriterion);
/*     */ 
/*     */     
/* 104 */     ShapelessRecipe recipe = new ShapelessRecipe((String)Objects.requireNonNullElse(this.group, ""), RecipeBuilder.determineBookCategory(this.category), this.result, this.ingredients);
/*     */ 
/*     */ 
/*     */     
/* 108 */     output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*     */   }
/*     */   
/*     */   private void ensureValid(ResourceKey<Recipe<?>> id) {
/* 112 */     if (this.criteria.isEmpty())
/* 113 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier())); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\ShapelessRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */