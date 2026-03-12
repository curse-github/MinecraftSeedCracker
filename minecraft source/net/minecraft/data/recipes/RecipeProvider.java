/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.advancements.criterion.EnterBlockTrigger;
/*     */ import net.minecraft.advancements.criterion.ImpossibleTrigger;
/*     */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate;
/*     */ import net.minecraft.advancements.criterion.ItemPredicate.Builder;
/*     */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.BlockFamilies;
/*     */ import net.minecraft.data.BlockFamily;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.SuspiciousEffectHolder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RecipeProvider
/*     */ {
/*     */   protected final HolderLookup.Provider registries;
/*     */   private final HolderGetter<Item> items;
/*     */   protected final RecipeOutput output;
/*     */   
/*     */   protected RecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
/*  70 */     this.registries = registries;
/*  71 */     this.items = registries.lookupOrThrow(Registries.ITEM);
/*  72 */     this.output = output;
/*     */   }
/*     */   @FunctionalInterface
/*     */   private static interface FamilyRecipeProvider {
/*     */     RecipeBuilder create(RecipeProvider param1RecipeProvider, ItemLike param1ItemLike1, ItemLike param1ItemLike2); }
/*     */   protected static abstract class Runner implements DataProvider { private final PackOutput packOutput; private final CompletableFuture<HolderLookup.Provider> registries;
/*     */     
/*     */     protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
/*  80 */       this.packOutput = packOutput;
/*  81 */       this.registries = registries;
/*     */     }
/*     */     
/*     */     public final CompletableFuture<?> run(final CachedOutput cache)
/*     */     {
/*  86 */       return this.registries.thenCompose(registries -> {
/*  87 */             final PackOutput.PathProvider recipePathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
/*  88 */             final PackOutput.PathProvider advancementPathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
/*     */             
/*  90 */             final Set<ResourceKey<Recipe<?>>> allRecipes = Sets.newHashSet();
/*  91 */             final List<CompletableFuture<?>> tasks = new ArrayList<CompletableFuture<?>>();
/*     */             
/*  93 */             RecipeOutput recipeOutput = new RecipeOutput(this)
/*     */               {
/*     */                 public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancementHolder) {
/*  96 */                   if (!allRecipes.add(id)) {
/*  97 */                     throw new IllegalStateException("Duplicate recipe " + String.valueOf(id.identifier()));
/*     */                   }
/*  99 */                   saveRecipe(id, recipe);
/* 100 */                   if (advancementHolder != null) {
/* 101 */                     saveAdvancement(advancementHolder);
/*     */                   }
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/* 107 */                 public Advancement.Builder advancement() { return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT); }
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void includeRootAdvancement() {
/* 112 */                   AdvancementHolder root = Advancement.Builder.recipeAdvancement().addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance())).build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/* 113 */                   saveAdvancement(root);
/*     */                 }
/*     */ 
/*     */                 
/* 117 */                 private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) { tasks.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(id.identifier()))); }
/*     */ 
/*     */                 
/*     */                 private void saveAdvancement(AdvancementHolder advancementHolder) {
/* 121 */                   tasks.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancementHolder.value(), advancementPathProvider.json(advancementHolder.id())));
/*     */                 }
/*     */               };
/*     */             
/* 125 */             createRecipeProvider(registries, recipeOutput).buildRecipes();
/* 126 */             return CompletableFuture.allOf((CompletableFuture[])tasks.toArray(()));
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract RecipeProvider createRecipeProvider(HolderLookup.Provider param1Provider, RecipeOutput param1RecipeOutput); }
/*     */ 
/*     */ 
/*     */   
/* 136 */   protected void generateForEnabledBlockFamilies(FeatureFlagSet flagSet) { BlockFamilies.getAllFamilies()
/* 137 */       .filter(BlockFamily::shouldGenerateRecipe)
/* 138 */       .forEach(family -> generateRecipes(family, flagSet)); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   protected void oneToOneConversionRecipe(ItemLike product, ItemLike resource, String group) { oneToOneConversionRecipe(product, resource, group, 1); }
/*     */   class null implements RecipeOutput {
/*     */     null(RecipeProvider.Runner this$0) {}
/*     */     public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancementHolder) { if (!allRecipes.add(id)) throw new IllegalStateException("Duplicate recipe " + String.valueOf(id.identifier()));  saveRecipe(id, recipe); if (advancementHolder != null)
/* 146 */         saveAdvancement(advancementHolder);  } public Advancement.Builder advancement() { return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT); } public void includeRootAdvancement() { AdvancementHolder root = Advancement.Builder.recipeAdvancement().addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance())).build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT); saveAdvancement(root); } private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) { tasks.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(id.identifier()))); } private void saveAdvancement(AdvancementHolder advancementHolder) { tasks.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancementHolder.value(), advancementPathProvider.json(advancementHolder.id()))); } } protected void oneToOneConversionRecipe(ItemLike product, ItemLike resource, String group, int productCount) { shapeless(RecipeCategory.MISC, product, productCount)
/* 147 */       .requires(resource)
/* 148 */       .group(group)
/* 149 */       .unlockedBy(getHasName(resource), has(resource))
/* 150 */       .save(this.output, getConversionRecipeName(product, resource)); }
/*     */ 
/*     */ 
/*     */   
/* 154 */   protected void oreSmelting(List<ItemLike> smeltables, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) { oreCooking(RecipeSerializer.SMELTING_RECIPE, net.minecraft.world.item.crafting.SmeltingRecipe::new, smeltables, category, result, experience, cookingTime, group, "_from_smelting"); }
/*     */ 
/*     */ 
/*     */   
/* 158 */   protected void oreBlasting(List<ItemLike> smeltables, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) { oreCooking(RecipeSerializer.BLASTING_RECIPE, net.minecraft.world.item.crafting.BlastingRecipe::new, smeltables, category, result, experience, cookingTime, group, "_from_blasting"); }
/*     */ 
/*     */   
/*     */   private <T extends AbstractCookingRecipe> void oreCooking(RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> factory, List<ItemLike> smeltables, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String fromDesc) {
/* 162 */     for (ItemLike item : smeltables) {
/* 163 */       SimpleCookingRecipeBuilder.generic(Ingredient.of(item), category, result, experience, cookingTime, serializer, factory)
/* 164 */         .group(group)
/* 165 */         .unlockedBy(getHasName(item), has(item))
/* 166 */         .save(this.output, getItemName(result) + getItemName(result) + "_" + fromDesc);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void netheriteSmithing(Item base, RecipeCategory category, Item result) {
/* 171 */     SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(base), tag(ItemTags.NETHERITE_TOOL_MATERIALS), category, result)
/* 172 */       .unlocks("has_netherite_ingot", has(ItemTags.NETHERITE_TOOL_MATERIALS))
/* 173 */       .save(this.output, getItemName(result) + "_smithing");
/*     */   }
/*     */   
/*     */   protected void trimSmithing(Item trimTemplate, ResourceKey<TrimPattern> patternId, ResourceKey<Recipe<?>> id) {
/* 177 */     Holder.Reference<TrimPattern> pattern = this.registries.lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(patternId);
/* 178 */     SmithingTrimRecipeBuilder.smithingTrim(Ingredient.of(trimTemplate), tag(ItemTags.TRIMMABLE_ARMOR), tag(ItemTags.TRIM_MATERIALS), pattern, RecipeCategory.MISC)
/* 179 */       .unlocks("has_smithing_trim_template", has(trimTemplate))
/* 180 */       .save(this.output, id);
/*     */   }
/*     */   
/*     */   protected void twoByTwoPacker(RecipeCategory category, ItemLike result, ItemLike ingredient) {
/* 184 */     shaped(category, result, 1)
/* 185 */       .define(Character.valueOf('#'), ingredient)
/* 186 */       .pattern("##")
/* 187 */       .pattern("##")
/* 188 */       .unlockedBy(getHasName(ingredient), has(ingredient))
/* 189 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void threeByThreePacker(RecipeCategory category, ItemLike result, ItemLike ingredient, String unlockedBy) {
/* 193 */     shapeless(category, result)
/* 194 */       .requires(ingredient, 9)
/* 195 */       .unlockedBy(unlockedBy, has(ingredient))
/* 196 */       .save(this.output);
/*     */   }
/*     */ 
/*     */   
/* 200 */   protected void threeByThreePacker(RecipeCategory category, ItemLike result, ItemLike ingredient) { threeByThreePacker(category, result, ingredient, getHasName(ingredient)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void planksFromLog(ItemLike result, TagKey<Item> logs, int count) {
/* 205 */     shapeless(RecipeCategory.BUILDING_BLOCKS, result, count)
/* 206 */       .requires(logs)
/* 207 */       .group("planks")
/* 208 */       .unlockedBy("has_log", has(logs))
/* 209 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void planksFromLogs(ItemLike result, TagKey<Item> logs, int count) {
/* 213 */     shapeless(RecipeCategory.BUILDING_BLOCKS, result, count)
/* 214 */       .requires(logs)
/* 215 */       .group("planks")
/* 216 */       .unlockedBy("has_logs", has(logs))
/* 217 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void woodFromLogs(ItemLike result, ItemLike log) {
/* 221 */     shaped(RecipeCategory.BUILDING_BLOCKS, result, 3)
/* 222 */       .define(Character.valueOf('#'), log)
/* 223 */       .pattern("##")
/* 224 */       .pattern("##")
/* 225 */       .group("bark")
/* 226 */       .unlockedBy("has_log", has(log))
/* 227 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void woodenBoat(ItemLike result, ItemLike planks) {
/* 231 */     shaped(RecipeCategory.TRANSPORTATION, result)
/* 232 */       .define(Character.valueOf('#'), planks)
/* 233 */       .pattern("# #")
/* 234 */       .pattern("###")
/* 235 */       .group("boat")
/* 236 */       .unlockedBy("in_water", insideOf(Blocks.WATER))
/* 237 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void chestBoat(ItemLike chestBoat, ItemLike boat) {
/* 241 */     shapeless(RecipeCategory.TRANSPORTATION, chestBoat)
/* 242 */       .requires(Blocks.CHEST)
/* 243 */       .requires(boat)
/* 244 */       .group("chest_boat")
/* 245 */       .unlockedBy("has_boat", has(ItemTags.BOATS))
/* 246 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private RecipeBuilder buttonBuilder(ItemLike result, Ingredient base) {
/* 250 */     return shapeless(RecipeCategory.REDSTONE, result)
/* 251 */       .requires(base);
/*     */   }
/*     */   
/*     */   protected RecipeBuilder doorBuilder(ItemLike result, Ingredient base) {
/* 255 */     return shaped(RecipeCategory.REDSTONE, result, 3)
/* 256 */       .define(Character.valueOf('#'), base)
/* 257 */       .pattern("##")
/* 258 */       .pattern("##")
/* 259 */       .pattern("##");
/*     */   }
/*     */   
/*     */   private RecipeBuilder fenceBuilder(ItemLike result, Ingredient base) {
/* 263 */     int count = (result == Blocks.NETHER_BRICK_FENCE) ? 6 : 3;
/* 264 */     Item base2 = (result == Blocks.NETHER_BRICK_FENCE) ? Items.NETHER_BRICK : Items.STICK;
/* 265 */     return shaped(RecipeCategory.DECORATIONS, result, count)
/* 266 */       .define(Character.valueOf('W'), base)
/* 267 */       .define(Character.valueOf('#'), base2)
/* 268 */       .pattern("W#W")
/* 269 */       .pattern("W#W");
/*     */   }
/*     */   
/*     */   private RecipeBuilder fenceGateBuilder(ItemLike result, Ingredient planks) {
/* 273 */     return shaped(RecipeCategory.REDSTONE, result)
/* 274 */       .define(Character.valueOf('#'), Items.STICK)
/* 275 */       .define(Character.valueOf('W'), planks)
/* 276 */       .pattern("#W#")
/* 277 */       .pattern("#W#");
/*     */   }
/*     */   
/*     */   protected void pressurePlate(ItemLike result, ItemLike base) {
/* 281 */     pressurePlateBuilder(RecipeCategory.REDSTONE, result, Ingredient.of(base))
/* 282 */       .unlockedBy(getHasName(base), has(base))
/* 283 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private RecipeBuilder pressurePlateBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 287 */     return shaped(category, result)
/* 288 */       .define(Character.valueOf('#'), base)
/* 289 */       .pattern("##");
/*     */   }
/*     */   
/*     */   protected void slab(RecipeCategory category, ItemLike result, ItemLike base) {
/* 293 */     slabBuilder(category, result, Ingredient.of(base))
/* 294 */       .unlockedBy(getHasName(base), has(base))
/* 295 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void shelf(ItemLike result, ItemLike strippedLogs) {
/* 299 */     shaped(RecipeCategory.DECORATIONS, result, 6)
/* 300 */       .define(Character.valueOf('#'), strippedLogs)
/* 301 */       .pattern("###")
/* 302 */       .pattern("   ")
/* 303 */       .pattern("###")
/* 304 */       .group("shelf")
/* 305 */       .unlockedBy(getHasName(strippedLogs), has(strippedLogs))
/* 306 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected RecipeBuilder slabBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 310 */     return shaped(category, result, 6)
/* 311 */       .define(Character.valueOf('#'), base)
/* 312 */       .pattern("###");
/*     */   }
/*     */   
/*     */   protected RecipeBuilder stairBuilder(ItemLike result, Ingredient base) {
/* 316 */     return shaped(RecipeCategory.BUILDING_BLOCKS, result, 4)
/* 317 */       .define(Character.valueOf('#'), base)
/* 318 */       .pattern("#  ")
/* 319 */       .pattern("## ")
/* 320 */       .pattern("###");
/*     */   }
/*     */   
/*     */   protected RecipeBuilder trapdoorBuilder(ItemLike result, Ingredient base) {
/* 324 */     return shaped(RecipeCategory.REDSTONE, result, 2)
/* 325 */       .define(Character.valueOf('#'), base)
/* 326 */       .pattern("###")
/* 327 */       .pattern("###");
/*     */   }
/*     */   
/*     */   private RecipeBuilder signBuilder(ItemLike result, Ingredient planks) {
/* 331 */     return shaped(RecipeCategory.DECORATIONS, result, 3)
/* 332 */       .group("sign")
/* 333 */       .define(Character.valueOf('#'), planks)
/* 334 */       .define(Character.valueOf('X'), Items.STICK)
/* 335 */       .pattern("###")
/* 336 */       .pattern("###")
/* 337 */       .pattern(" X ");
/*     */   }
/*     */   
/*     */   protected void hangingSign(ItemLike result, ItemLike ingredient) {
/* 341 */     shaped(RecipeCategory.DECORATIONS, result, 6)
/* 342 */       .group("hanging_sign")
/* 343 */       .define(Character.valueOf('#'), ingredient)
/* 344 */       .define(Character.valueOf('X'), Items.IRON_CHAIN)
/* 345 */       .pattern("X X")
/* 346 */       .pattern("###")
/* 347 */       .pattern("###")
/* 348 */       .unlockedBy("has_stripped_logs", has(ingredient))
/* 349 */       .save(this.output);
/*     */   }
/*     */ 
/*     */   
/* 353 */   protected void colorItemWithDye(List<Item> dyes, List<Item> items, String groupName, RecipeCategory category) { colorWithDye(dyes, items, null, groupName, category); }
/*     */ 
/*     */   
/*     */   protected void colorWithDye(List<Item> dyes, List<Item> dyedItems, Item uncoloredItem, String groupName, RecipeCategory category) {
/* 357 */     for (int dyeIndex = 0; dyeIndex < dyes.size(); dyeIndex++) {
/* 358 */       Item dye = (Item)dyes.get(dyeIndex);
/* 359 */       Item dyedItem = (Item)dyedItems.get(dyeIndex);
/*     */       
/* 361 */       Stream<Item> sourceItems = dyedItems.stream().filter(b -> !b.equals(dyedItem));
/* 362 */       if (uncoloredItem != null) {
/* 363 */         sourceItems = Stream.concat(sourceItems, Stream.of(uncoloredItem));
/*     */       }
/*     */       
/* 366 */       shapeless(category, dyedItem)
/* 367 */         .requires(dye)
/* 368 */         .requires(Ingredient.of(sourceItems))
/* 369 */         .group(groupName)
/* 370 */         .unlockedBy("has_needed_dye", has(dye))
/* 371 */         .save(this.output, "dye_" + getItemName(dyedItem));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void carpet(ItemLike result, ItemLike sourceItem) {
/* 376 */     shaped(RecipeCategory.DECORATIONS, result, 3)
/* 377 */       .define(Character.valueOf('#'), sourceItem)
/* 378 */       .pattern("##")
/* 379 */       .group("carpet")
/* 380 */       .unlockedBy(getHasName(sourceItem), has(sourceItem))
/* 381 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void bedFromPlanksAndWool(ItemLike result, ItemLike wool) {
/* 385 */     shaped(RecipeCategory.DECORATIONS, result)
/* 386 */       .define(Character.valueOf('#'), wool)
/* 387 */       .define(Character.valueOf('X'), ItemTags.PLANKS)
/* 388 */       .pattern("###")
/* 389 */       .pattern("XXX")
/* 390 */       .group("bed")
/* 391 */       .unlockedBy(getHasName(wool), has(wool))
/* 392 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void banner(ItemLike result, ItemLike wool) {
/* 396 */     shaped(RecipeCategory.DECORATIONS, result)
/* 397 */       .define(Character.valueOf('#'), wool)
/* 398 */       .define(Character.valueOf('|'), Items.STICK)
/* 399 */       .pattern("###")
/* 400 */       .pattern("###")
/* 401 */       .pattern(" | ")
/* 402 */       .group("banner")
/* 403 */       .unlockedBy(getHasName(wool), has(wool))
/* 404 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void stainedGlassFromGlassAndDye(ItemLike result, ItemLike dye) {
/* 408 */     shaped(RecipeCategory.BUILDING_BLOCKS, result, 8)
/* 409 */       .define(Character.valueOf('#'), Blocks.GLASS)
/* 410 */       .define(Character.valueOf('X'), dye)
/* 411 */       .pattern("###")
/* 412 */       .pattern("#X#")
/* 413 */       .pattern("###")
/* 414 */       .group("stained_glass")
/* 415 */       .unlockedBy("has_glass", has(Blocks.GLASS))
/* 416 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void dryGhast(ItemLike result) {
/* 420 */     shaped(RecipeCategory.BUILDING_BLOCKS, result, 1)
/* 421 */       .define(Character.valueOf('#'), Items.GHAST_TEAR)
/* 422 */       .define(Character.valueOf('X'), Items.SOUL_SAND)
/* 423 */       .pattern("###")
/* 424 */       .pattern("#X#")
/* 425 */       .pattern("###")
/* 426 */       .group("dry_ghast")
/* 427 */       .unlockedBy(getHasName(Items.GHAST_TEAR), has(Items.GHAST_TEAR))
/* 428 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void harness(ItemLike result, ItemLike wool) {
/* 432 */     shaped(RecipeCategory.COMBAT, result)
/* 433 */       .define(Character.valueOf('#'), wool)
/* 434 */       .define(Character.valueOf('G'), Items.GLASS)
/* 435 */       .define(Character.valueOf('L'), Items.LEATHER)
/* 436 */       .pattern("LLL")
/* 437 */       .pattern("G#G")
/* 438 */       .group("harness")
/* 439 */       .unlockedBy("has_dried_ghast", has(Blocks.DRIED_GHAST))
/* 440 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void stainedGlassPaneFromStainedGlass(ItemLike result, ItemLike stainedGlass) {
/* 444 */     shaped(RecipeCategory.DECORATIONS, result, 16)
/* 445 */       .define(Character.valueOf('#'), stainedGlass)
/* 446 */       .pattern("###")
/* 447 */       .pattern("###")
/* 448 */       .group("stained_glass_pane")
/* 449 */       .unlockedBy("has_glass", has(stainedGlass))
/* 450 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void stainedGlassPaneFromGlassPaneAndDye(ItemLike result, ItemLike dye) {
/* 454 */     shaped(RecipeCategory.DECORATIONS, result, 8)
/* 455 */       .define(Character.valueOf('#'), Blocks.GLASS_PANE)
/* 456 */       .define(Character.valueOf('$'), dye)
/* 457 */       .pattern("###")
/* 458 */       .pattern("#$#")
/* 459 */       .pattern("###")
/* 460 */       .group("stained_glass_pane")
/* 461 */       .unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE))
/* 462 */       .unlockedBy(getHasName(dye), has(dye))
/* 463 */       .save(this.output, getConversionRecipeName(result, Blocks.GLASS_PANE));
/*     */   }
/*     */   
/*     */   protected void coloredTerracottaFromTerracottaAndDye(ItemLike result, ItemLike dye) {
/* 467 */     shaped(RecipeCategory.BUILDING_BLOCKS, result, 8)
/* 468 */       .define(Character.valueOf('#'), Blocks.TERRACOTTA)
/* 469 */       .define(Character.valueOf('X'), dye)
/* 470 */       .pattern("###")
/* 471 */       .pattern("#X#")
/* 472 */       .pattern("###")
/* 473 */       .group("stained_terracotta")
/* 474 */       .unlockedBy("has_terracotta", has(Blocks.TERRACOTTA))
/* 475 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void concretePowder(ItemLike result, ItemLike dye) {
/* 479 */     shapeless(RecipeCategory.BUILDING_BLOCKS, result, 8)
/* 480 */       .requires(dye)
/* 481 */       .requires(Blocks.SAND, 4)
/* 482 */       .requires(Blocks.GRAVEL, 4)
/* 483 */       .group("concrete_powder")
/* 484 */       .unlockedBy("has_sand", has(Blocks.SAND))
/* 485 */       .unlockedBy("has_gravel", has(Blocks.GRAVEL))
/* 486 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void candle(ItemLike result, ItemLike dye) {
/* 490 */     shapeless(RecipeCategory.DECORATIONS, result)
/* 491 */       .requires(Blocks.CANDLE)
/* 492 */       .requires(dye)
/* 493 */       .group("dyed_candle")
/* 494 */       .unlockedBy(getHasName(dye), has(dye))
/* 495 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void wall(RecipeCategory category, ItemLike result, ItemLike base) {
/* 499 */     wallBuilder(category, result, Ingredient.of(base))
/* 500 */       .unlockedBy(getHasName(base), has(base))
/* 501 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private RecipeBuilder wallBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 505 */     return shaped(category, result, 6)
/* 506 */       .define(Character.valueOf('#'), base)
/* 507 */       .pattern("###")
/* 508 */       .pattern("###");
/*     */   }
/*     */   
/*     */   protected void polished(RecipeCategory category, ItemLike result, ItemLike base) {
/* 512 */     polishedBuilder(category, result, Ingredient.of(base))
/* 513 */       .unlockedBy(getHasName(base), has(base))
/* 514 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private RecipeBuilder polishedBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 518 */     return shaped(category, result, 4)
/* 519 */       .define(Character.valueOf('S'), base)
/* 520 */       .pattern("SS")
/* 521 */       .pattern("SS");
/*     */   }
/*     */   
/*     */   protected void cut(RecipeCategory category, ItemLike result, ItemLike base) {
/* 525 */     cutBuilder(category, result, Ingredient.of(base))
/* 526 */       .unlockedBy(getHasName(base), has(base))
/* 527 */       .save(this.output);
/*     */   }
/*     */   
/*     */   private ShapedRecipeBuilder cutBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 531 */     return shaped(category, result, 4)
/* 532 */       .define(Character.valueOf('#'), base)
/* 533 */       .pattern("##")
/* 534 */       .pattern("##");
/*     */   }
/*     */   
/*     */   protected void chiseled(RecipeCategory category, ItemLike result, ItemLike base) {
/* 538 */     chiseledBuilder(category, result, Ingredient.of(base))
/* 539 */       .unlockedBy(getHasName(base), has(base))
/* 540 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void mosaicBuilder(RecipeCategory category, ItemLike result, ItemLike base) {
/* 544 */     shaped(category, result)
/* 545 */       .define(Character.valueOf('#'), base)
/* 546 */       .pattern("#")
/* 547 */       .pattern("#")
/* 548 */       .unlockedBy(getHasName(base), has(base))
/* 549 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected ShapedRecipeBuilder chiseledBuilder(RecipeCategory category, ItemLike result, Ingredient base) {
/* 553 */     return shaped(category, result)
/* 554 */       .define(Character.valueOf('#'), base)
/* 555 */       .pattern("#")
/* 556 */       .pattern("#");
/*     */   }
/*     */ 
/*     */   
/* 560 */   protected void stonecutterResultFromBase(RecipeCategory category, ItemLike result, ItemLike base) { stonecutterResultFromBase(category, result, base, 1); }
/*     */ 
/*     */   
/*     */   protected void stonecutterResultFromBase(RecipeCategory category, ItemLike result, ItemLike base, int count) {
/* 564 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), category, result, count)
/* 565 */       .unlockedBy(getHasName(base), has(base))
/* 566 */       .save(this.output, getConversionRecipeName(result, base) + "_stonecutting");
/*     */   }
/*     */   
/*     */   private void smeltingResultFromBase(ItemLike result, ItemLike base) {
/* 570 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, result, 0.1F, 200)
/* 571 */       .unlockedBy(getHasName(base), has(base))
/* 572 */       .save(this.output);
/*     */   }
/*     */ 
/*     */   
/* 576 */   protected void nineBlockStorageRecipes(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm) { nineBlockStorageRecipes(unpackedFormCategory, unpackedForm, packedFormCategory, packedForm, getSimpleRecipeName(packedForm), null, getSimpleRecipeName(unpackedForm), null); }
/*     */ 
/*     */ 
/*     */   
/* 580 */   protected void nineBlockStorageRecipesWithCustomPacking(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm, String packingRecipeId, String packingRecipeGroup) { nineBlockStorageRecipes(unpackedFormCategory, unpackedForm, packedFormCategory, packedForm, packingRecipeId, packingRecipeGroup, getSimpleRecipeName(unpackedForm), null); }
/*     */ 
/*     */ 
/*     */   
/* 584 */   protected void nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm, String unpackingRecipeId, String unpackingRecipeGroup) { nineBlockStorageRecipes(unpackedFormCategory, unpackedForm, packedFormCategory, packedForm, getSimpleRecipeName(packedForm), null, unpackingRecipeId, unpackingRecipeGroup); }
/*     */ 
/*     */   
/*     */   private void nineBlockStorageRecipes(RecipeCategory unpackedFormCategory, ItemLike unpackedForm, RecipeCategory packedFormCategory, ItemLike packedForm, String packingRecipeId, String packingRecipeGroup, String unpackingRecipeId, String unpackingRecipeGroup) {
/* 588 */     shapeless(unpackedFormCategory, unpackedForm, 9)
/* 589 */       .requires(packedForm)
/* 590 */       .group(unpackingRecipeGroup)
/* 591 */       .unlockedBy(getHasName(packedForm), has(packedForm))
/* 592 */       .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse(unpackingRecipeId)));
/*     */     
/* 594 */     shaped(packedFormCategory, packedForm)
/* 595 */       .define(Character.valueOf('#'), unpackedForm)
/* 596 */       .pattern("###")
/* 597 */       .pattern("###")
/* 598 */       .pattern("###")
/* 599 */       .group(packingRecipeGroup)
/* 600 */       .unlockedBy(getHasName(unpackedForm), has(unpackedForm))
/* 601 */       .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.parse(packingRecipeId)));
/*     */   }
/*     */   
/*     */   protected void copySmithingTemplate(ItemLike smithingTemplate, ItemLike baseMaterial) {
/* 605 */     shaped(RecipeCategory.MISC, smithingTemplate, 2)
/* 606 */       .define(Character.valueOf('#'), Items.DIAMOND)
/* 607 */       .define(Character.valueOf('C'), baseMaterial)
/* 608 */       .define(Character.valueOf('S'), smithingTemplate)
/* 609 */       .pattern("#S#")
/* 610 */       .pattern("#C#")
/* 611 */       .pattern("###")
/* 612 */       .unlockedBy(getHasName(smithingTemplate), has(smithingTemplate))
/* 613 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void copySmithingTemplate(ItemLike smithingTemplate, Ingredient baseMaterials) {
/* 617 */     shaped(RecipeCategory.MISC, smithingTemplate, 2)
/* 618 */       .define(Character.valueOf('#'), Items.DIAMOND)
/* 619 */       .define(Character.valueOf('C'), baseMaterials)
/* 620 */       .define(Character.valueOf('S'), smithingTemplate)
/* 621 */       .pattern("#S#")
/* 622 */       .pattern("#C#")
/* 623 */       .pattern("###")
/* 624 */       .unlockedBy(getHasName(smithingTemplate), has(smithingTemplate))
/* 625 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected <T extends AbstractCookingRecipe> void cookRecipes(String source, RecipeSerializer<T> type, AbstractCookingRecipe.Factory<T> factory, int cookingTime) {
/* 629 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.BEEF, Items.COOKED_BEEF, 0.35F);
/* 630 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.CHICKEN, Items.COOKED_CHICKEN, 0.35F);
/* 631 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.COD, Items.COOKED_COD, 0.35F);
/* 632 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.KELP, Items.DRIED_KELP, 0.1F);
/* 633 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.SALMON, Items.COOKED_SALMON, 0.35F);
/* 634 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.MUTTON, Items.COOKED_MUTTON, 0.35F);
/* 635 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.PORKCHOP, Items.COOKED_PORKCHOP, 0.35F);
/* 636 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.POTATO, Items.BAKED_POTATO, 0.35F);
/* 637 */     simpleCookingRecipe(source, type, factory, cookingTime, Items.RABBIT, Items.COOKED_RABBIT, 0.35F);
/*     */   }
/*     */   
/*     */   private <T extends AbstractCookingRecipe> void simpleCookingRecipe(String source, RecipeSerializer<T> type, AbstractCookingRecipe.Factory<T> factory, int cookingTime, ItemLike base, ItemLike result, float experience) {
/* 641 */     SimpleCookingRecipeBuilder.generic(Ingredient.of(base), RecipeCategory.FOOD, result, experience, cookingTime, type, factory)
/* 642 */       .unlockedBy(getHasName(base), has(base))
/* 643 */       .save(this.output, getItemName(result) + "_from_" + getItemName(result));
/*     */   }
/*     */   
/*     */   protected void waxRecipes(FeatureFlagSet flagSet) {
/* 647 */     ((BiMap)HoneycombItem.WAXABLES.get()).forEach((block, waxedBlock) -> {
/* 648 */           if (!waxedBlock.requiredFeatures().isSubsetOf(flagSet)) {
/*     */             return;
/*     */           }
/*     */           
/* 652 */           Pair<RecipeCategory, String> pair = (Pair)HoneycombItem.WAXED_RECIPES.getOrDefault(waxedBlock, Pair.of(RecipeCategory.BUILDING_BLOCKS, getItemName(waxedBlock)));
/*     */           
/* 654 */           RecipeCategory recipeCategory = (RecipeCategory)pair.getFirst();
/* 655 */           String group = (String)pair.getSecond();
/*     */           
/* 657 */           shapeless(recipeCategory, waxedBlock)
/* 658 */             .requires(block)
/* 659 */             .requires(Items.HONEYCOMB)
/* 660 */             .group(group)
/* 661 */             .unlockedBy(getHasName(block), has(block))
/* 662 */             .save(this.output, getConversionRecipeName(waxedBlock, Items.HONEYCOMB));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void grate(Block grateBlock, Block material) {
/* 668 */     shaped(RecipeCategory.BUILDING_BLOCKS, grateBlock, 4)
/* 669 */       .define(Character.valueOf('M'), material)
/* 670 */       .pattern(" M ")
/* 671 */       .pattern("M M")
/* 672 */       .pattern(" M ")
/* 673 */       .group(getItemName(grateBlock))
/* 674 */       .unlockedBy(getHasName(material), has(material))
/* 675 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void copperBulb(Block copperBulb, Block copperMaterial) {
/* 679 */     shaped(RecipeCategory.REDSTONE, copperBulb, 4)
/* 680 */       .define(Character.valueOf('C'), copperMaterial)
/* 681 */       .define(Character.valueOf('R'), Items.REDSTONE)
/* 682 */       .define(Character.valueOf('B'), Items.BLAZE_ROD)
/* 683 */       .pattern(" C ")
/* 684 */       .pattern("CBC")
/* 685 */       .pattern(" R ")
/* 686 */       .unlockedBy(getHasName(copperMaterial), has(copperMaterial))
/* 687 */       .group(getItemName(copperBulb))
/* 688 */       .save(this.output);
/*     */   }
/*     */   
/*     */   protected void waxedChiseled(Block result, Block material) {
/* 692 */     shaped(RecipeCategory.BUILDING_BLOCKS, result)
/* 693 */       .define(Character.valueOf('M'), material)
/* 694 */       .pattern(" M ")
/* 695 */       .pattern(" M ")
/* 696 */       .group(getItemName(result))
/* 697 */       .unlockedBy(getHasName(material), has(material))
/* 698 */       .save(this.output);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void suspiciousStew(Item item, SuspiciousEffectHolder effectHolder) {
/* 704 */     ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effectHolder.getSuspiciousEffects()).build());
/*     */     
/* 706 */     shapeless(RecipeCategory.FOOD, stew)
/* 707 */       .requires(Items.BOWL)
/* 708 */       .requires(Items.BROWN_MUSHROOM)
/* 709 */       .requires(Items.RED_MUSHROOM)
/* 710 */       .requires(item)
/* 711 */       .group("suspicious_stew")
/* 712 */       .unlockedBy(getHasName(item), has(item))
/* 713 */       .save(this.output, getItemName(stew.getItem()) + "_from_" + getItemName(stew.getItem()));
/*     */   }
/*     */   
/*     */   protected void generateRecipes(BlockFamily family, FeatureFlagSet flagSet) {
/* 717 */     family.getVariants().forEach((variant, result) -> {
/* 718 */           if (!result.requiredFeatures().isSubsetOf(flagSet)) {
/*     */             return;
/*     */           }
/* 721 */           FamilyRecipeProvider recipeFunction = (FamilyRecipeProvider)SHAPE_BUILDERS.get(variant);
/* 722 */           Block block = getBaseBlock(family, variant);
/* 723 */           if (recipeFunction != null) {
/* 724 */             RecipeBuilder builder = recipeFunction.create(this, result, block);
/* 725 */             family.getRecipeGroupPrefix().ifPresent(());
/* 726 */             builder.unlockedBy((String)family.getRecipeUnlockedBy().orElseGet(()), has(block));
/* 727 */             builder.save(this.output);
/*     */           } 
/*     */           
/* 730 */           if (variant == BlockFamily.Variant.CRACKED) {
/* 731 */             smeltingResultFromBase(result, block);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private Block getBaseBlock(BlockFamily family, BlockFamily.Variant variant) {
/* 737 */     if (variant == BlockFamily.Variant.CHISELED) {
/* 738 */       if (!family.getVariants().containsKey(BlockFamily.Variant.SLAB)) {
/* 739 */         throw new IllegalStateException("Slab is not defined for the family.");
/*     */       }
/* 741 */       return family.get(BlockFamily.Variant.SLAB);
/*     */     } 
/* 743 */     return family.getBaseBlock();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 752 */   private static final Map<BlockFamily.Variant, FamilyRecipeProvider> SHAPE_BUILDERS = ImmutableMap.builder()
/* 753 */     .put(BlockFamily.Variant.BUTTON, (context, result, base) -> context.buttonBuilder(result, Ingredient.of(base)))
/* 754 */     .put(BlockFamily.Variant.CHISELED, (context, result, base) -> context.chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base)))
/* 755 */     .put(BlockFamily.Variant.CUT, (context, result, base) -> context.cutBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base)))
/* 756 */     .put(BlockFamily.Variant.DOOR, (context, result, base) -> context.doorBuilder(result, Ingredient.of(base)))
/* 757 */     .put(BlockFamily.Variant.CUSTOM_FENCE, (context, result, base) -> context.fenceBuilder(result, Ingredient.of(base)))
/* 758 */     .put(BlockFamily.Variant.FENCE, (context, result, base) -> context.fenceBuilder(result, Ingredient.of(base)))
/* 759 */     .put(BlockFamily.Variant.CUSTOM_FENCE_GATE, (context, result, base) -> context.fenceGateBuilder(result, Ingredient.of(base)))
/* 760 */     .put(BlockFamily.Variant.FENCE_GATE, (context, result, base) -> context.fenceGateBuilder(result, Ingredient.of(base)))
/* 761 */     .put(BlockFamily.Variant.SIGN, (context, result, base) -> context.signBuilder(result, Ingredient.of(base)))
/* 762 */     .put(BlockFamily.Variant.SLAB, (context, result, base) -> context.slabBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base)))
/* 763 */     .put(BlockFamily.Variant.STAIRS, (context, result, base) -> context.stairBuilder(result, Ingredient.of(base)))
/* 764 */     .put(BlockFamily.Variant.PRESSURE_PLATE, (context, result, base) -> context.pressurePlateBuilder(RecipeCategory.REDSTONE, result, Ingredient.of(base)))
/* 765 */     .put(BlockFamily.Variant.POLISHED, (context, result, base) -> context.polishedBuilder(RecipeCategory.BUILDING_BLOCKS, result, Ingredient.of(base)))
/* 766 */     .put(BlockFamily.Variant.TRAPDOOR, (context, result, base) -> context.trapdoorBuilder(result, Ingredient.of(base)))
/* 767 */     .put(BlockFamily.Variant.WALL, (context, result, base) -> context.wallBuilder(RecipeCategory.DECORATIONS, result, Ingredient.of(base)))
/* 768 */     .build();
/*     */ 
/*     */ 
/*     */   
/* 772 */   private static Criterion<EnterBlockTrigger.TriggerInstance> insideOf(Block block) { return CriteriaTriggers.ENTER_BLOCK.createCriterion(new EnterBlockTrigger.TriggerInstance(Optional.empty(), Optional.of(block.builtInRegistryHolder()), Optional.empty())); }
/*     */ 
/*     */ 
/*     */   
/* 776 */   private Criterion<InventoryChangeTrigger.TriggerInstance> has(MinMaxBounds.Ints count, ItemLike item) { return inventoryTrigger(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(this.items, new ItemLike[] { item }).withCount(count) }); }
/*     */ 
/*     */ 
/*     */   
/* 780 */   protected Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) { return inventoryTrigger(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(this.items, new ItemLike[] { item }) }); }
/*     */ 
/*     */ 
/*     */   
/* 784 */   protected Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) { return inventoryTrigger(new ItemPredicate.Builder[] { ItemPredicate.Builder.item().of(this.items, tag) }); }
/*     */ 
/*     */ 
/*     */   
/* 788 */   private static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(Builder... predicates) { return inventoryTrigger((ItemPredicate[])Arrays.stream(predicates).map(ItemPredicate.Builder::build).toArray(x$0 -> new ItemPredicate[x$0])); }
/*     */ 
/*     */ 
/*     */   
/* 792 */   private static Criterion<InventoryChangeTrigger.TriggerInstance> inventoryTrigger(ItemPredicate... predicates) { return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), InventoryChangeTrigger.TriggerInstance.Slots.ANY, List.of(predicates))); }
/*     */ 
/*     */ 
/*     */   
/* 796 */   protected static String getHasName(ItemLike baseBlock) { return "has_" + getItemName(baseBlock); }
/*     */ 
/*     */ 
/*     */   
/* 800 */   protected static String getItemName(ItemLike itemLike) { return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath(); }
/*     */ 
/*     */ 
/*     */   
/* 804 */   protected static String getSimpleRecipeName(ItemLike itemLike) { return getItemName(itemLike); }
/*     */ 
/*     */ 
/*     */   
/* 808 */   protected static String getConversionRecipeName(ItemLike product, ItemLike material) { return getItemName(product) + "_from_" + getItemName(product); }
/*     */ 
/*     */   
/*     */   protected static String getSmeltingRecipeName(ItemLike product) {
/* 812 */     return getItemName(product) + "_from_smelting";
/*     */   }
/*     */   
/*     */   protected static String getBlastingRecipeName(ItemLike product) {
/* 816 */     return getItemName(product) + "_from_blasting";
/*     */   }
/*     */ 
/*     */   
/* 820 */   protected Ingredient tag(TagKey<Item> id) { return Ingredient.of(this.items.getOrThrow(id)); }
/*     */ 
/*     */ 
/*     */   
/* 824 */   protected ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike item) { return ShapedRecipeBuilder.shaped(this.items, category, item); }
/*     */ 
/*     */ 
/*     */   
/* 828 */   protected ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike item, int count) { return ShapedRecipeBuilder.shaped(this.items, category, item, count); }
/*     */ 
/*     */ 
/*     */   
/* 832 */   protected ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemStack result) { return ShapelessRecipeBuilder.shapeless(this.items, category, result); }
/*     */ 
/*     */ 
/*     */   
/* 836 */   protected ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike item) { return ShapelessRecipeBuilder.shapeless(this.items, category, item); }
/*     */ 
/*     */ 
/*     */   
/* 840 */   protected ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike item, int count) { return ShapelessRecipeBuilder.shapeless(this.items, category, item, count); }
/*     */   
/*     */   protected abstract void buildRecipes();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\RecipeProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */