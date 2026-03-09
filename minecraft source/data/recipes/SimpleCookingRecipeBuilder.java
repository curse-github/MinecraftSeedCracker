/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementRequirements;
/*     */ import net.minecraft.advancements.AdvancementRewards;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.CookingBookCategory;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class SimpleCookingRecipeBuilder
/*     */   implements RecipeBuilder
/*     */ {
/*     */   private final RecipeCategory category;
/*     */   private final CookingBookCategory bookCategory;
/*     */   private final Item result;
/*     */   private final Ingredient ingredient;
/*     */   private final float experience;
/*     */   private final int cookingTime;
/*     */   private final Map<String, Criterion<?>> criteria;
/*     */   private String group;
/*     */   private final AbstractCookingRecipe.Factory<?> factory;
/*     */   
/*     */   private SimpleCookingRecipeBuilder(RecipeCategory category, CookingBookCategory bookCategory, ItemLike result, Ingredient ingredient, float experience, int cookingTime, AbstractCookingRecipe.Factory<?> factory) {
/*  36 */     this.criteria = new LinkedHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     this.category = category;
/*  42 */     this.bookCategory = bookCategory;
/*  43 */     this.result = result.asItem();
/*  44 */     this.ingredient = ingredient;
/*  45 */     this.experience = experience;
/*  46 */     this.cookingTime = cookingTime;
/*  47 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */   
/*  51 */   public static <T extends AbstractCookingRecipe> SimpleCookingRecipeBuilder generic(Ingredient ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> factory) { return new SimpleCookingRecipeBuilder(category, determineRecipeCategory(serializer, result), result, ingredient, experience, cookingTime, factory); }
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static SimpleCookingRecipeBuilder campfireCooking(Ingredient ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime) { return new SimpleCookingRecipeBuilder(category, CookingBookCategory.FOOD, result, ingredient, experience, cookingTime, net.minecraft.world.item.crafting.CampfireCookingRecipe::new); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static SimpleCookingRecipeBuilder blasting(Ingredient ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime) { return new SimpleCookingRecipeBuilder(category, determineBlastingRecipeCategory(result), result, ingredient, experience, cookingTime, net.minecraft.world.item.crafting.BlastingRecipe::new); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static SimpleCookingRecipeBuilder smelting(Ingredient ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime) { return new SimpleCookingRecipeBuilder(category, determineSmeltingRecipeCategory(result), result, ingredient, experience, cookingTime, net.minecraft.world.item.crafting.SmeltingRecipe::new); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   public static SimpleCookingRecipeBuilder smoking(Ingredient ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime) { return new SimpleCookingRecipeBuilder(category, CookingBookCategory.FOOD, result, ingredient, experience, cookingTime, net.minecraft.world.item.crafting.SmokingRecipe::new); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCookingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/*  72 */     this.criteria.put(name, criterion);
/*  73 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleCookingRecipeBuilder group(String group) {
/*  78 */     this.group = group;
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public Item getResult() { return this.result; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/*  89 */     ensureValid(id);
/*     */ 
/*     */ 
/*     */     
/*  93 */     Advancement.Builder advancement = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
/*  94 */     Objects.requireNonNull(advancement); this.criteria.forEach(advancement::addCriterion);
/*  95 */     AbstractCookingRecipe recipe = this.factory.create(
/*  96 */         (String)Objects.requireNonNullElse(this.group, ""), this.bookCategory, this.ingredient, new ItemStack(this.result), this.experience, this.cookingTime);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 103 */     output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/")));
/*     */   }
/*     */   
/*     */   private static CookingBookCategory determineSmeltingRecipeCategory(ItemLike result) {
/* 107 */     if (result.asItem().components().has(DataComponents.FOOD)) {
/* 108 */       return CookingBookCategory.FOOD;
/*     */     }
/* 110 */     if (result.asItem() instanceof net.minecraft.world.item.BlockItem) {
/* 111 */       return CookingBookCategory.BLOCKS;
/*     */     }
/* 113 */     return CookingBookCategory.MISC;
/*     */   }
/*     */   
/*     */   private static CookingBookCategory determineBlastingRecipeCategory(ItemLike result) {
/* 117 */     if (result.asItem() instanceof net.minecraft.world.item.BlockItem) {
/* 118 */       return CookingBookCategory.BLOCKS;
/*     */     }
/* 120 */     return CookingBookCategory.MISC;
/*     */   }
/*     */   
/*     */   private static CookingBookCategory determineRecipeCategory(RecipeSerializer<? extends AbstractCookingRecipe> serializer, ItemLike result) {
/* 124 */     if (serializer == RecipeSerializer.SMELTING_RECIPE) {
/* 125 */       return determineSmeltingRecipeCategory(result);
/*     */     }
/* 127 */     if (serializer == RecipeSerializer.BLASTING_RECIPE) {
/* 128 */       return determineBlastingRecipeCategory(result);
/*     */     }
/* 130 */     if (serializer == RecipeSerializer.SMOKING_RECIPE || serializer == RecipeSerializer.CAMPFIRE_COOKING_RECIPE) {
/* 131 */       return CookingBookCategory.FOOD;
/*     */     }
/* 133 */     throw new IllegalStateException("Unknown cooking recipe type");
/*     */   }
/*     */   
/*     */   private void ensureValid(ResourceKey<Recipe<?>> id) {
/* 137 */     if (this.criteria.isEmpty())
/* 138 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier())); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\SimpleCookingRecipeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */