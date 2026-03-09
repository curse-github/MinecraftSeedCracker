/*     */ package net.minecraft.world.item.crafting;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplayId;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RecipeManager extends SimplePreparableReloadListener<RecipeMap> implements RecipeAccess {
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  43 */   private static final Map<ResourceKey<RecipePropertySet>, IngredientExtractor> RECIPE_PROPERTY_SETS = Map.of(RecipePropertySet.SMITHING_ADDITION, recipe -> {
/*  44 */         SmithingRecipe smithingRecipe = (SmithingRecipe)recipe; return (recipe instanceof SmithingRecipe) ? smithingRecipe.additionIngredient() : Optional.empty();
/*  45 */       }RecipePropertySet.SMITHING_BASE, recipe -> { SmithingRecipe smithingRecipe = (SmithingRecipe)recipe; return (recipe instanceof SmithingRecipe) ? Optional.of(smithingRecipe.baseIngredient()) : Optional.empty();
/*  46 */       }RecipePropertySet.SMITHING_TEMPLATE, recipe -> { SmithingRecipe smithingRecipe = (SmithingRecipe)recipe; return (recipe instanceof SmithingRecipe) ? smithingRecipe.templateIngredient() : Optional.empty();
/*     */       
/*  48 */       }RecipePropertySet.FURNACE_INPUT, forSingleInput(RecipeType.SMELTING), RecipePropertySet.BLAST_FURNACE_INPUT, 
/*  49 */       forSingleInput(RecipeType.BLASTING), RecipePropertySet.SMOKER_INPUT, 
/*  50 */       forSingleInput(RecipeType.SMOKING), RecipePropertySet.CAMPFIRE_INPUT, 
/*  51 */       forSingleInput(RecipeType.CAMPFIRE_COOKING));
/*     */ 
/*     */   
/*  54 */   private static final FileToIdConverter RECIPE_LISTER = FileToIdConverter.registry(Registries.RECIPE); private final HolderLookup.Provider registries; private RecipeMap recipes; private Map<ResourceKey<RecipePropertySet>, RecipePropertySet> propertySets; private SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes; private List<ServerDisplayInfo> allDisplays; private Map<ResourceKey<Recipe<?>>, List<ServerDisplayInfo>> recipeToDisplay;
/*     */   
/*     */   public RecipeManager(HolderLookup.Provider registries) {
/*  57 */     this.recipes = RecipeMap.EMPTY;
/*  58 */     this.propertySets = Map.of();
/*  59 */     this.stonecutterRecipes = SelectableRecipe.SingleInputSet.empty();
/*     */     
/*  61 */     this.allDisplays = List.of();
/*  62 */     this.recipeToDisplay = Map.of();
/*     */ 
/*     */     
/*  65 */     this.registries = registries;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RecipeMap prepare(ResourceManager manager, ProfilerFiller profiler) {
/*  71 */     SortedMap<Identifier, Recipe<?>> recipes = new TreeMap<Identifier, Recipe<?>>();
/*  72 */     SimpleJsonResourceReloadListener.scanDirectory(manager, RECIPE_LISTER, this.registries.createSerializationContext(JsonOps.INSTANCE), Recipe.CODEC, recipes);
/*     */     
/*  74 */     List<RecipeHolder<?>> recipeHolders = new ArrayList<RecipeHolder<?>>(recipes.size());
/*  75 */     recipes.forEach((id, recipe) -> {
/*  76 */           ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, id);
/*  77 */           RecipeHolder<?> holder = new RecipeHolder<?>(key, recipe);
/*  78 */           recipeHolders.add(holder);
/*     */         });
/*     */     
/*  81 */     return RecipeMap.create(recipeHolders);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void apply(RecipeMap recipes, ResourceManager manager, ProfilerFiller profiler) {
/*  86 */     this.recipes = recipes;
/*  87 */     LOGGER.info("Loaded {} recipes", Integer.valueOf(recipes.values().size()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void finalizeRecipeLoading(FeatureFlagSet enabledFlags) {
/*  93 */     List<SelectableRecipe.SingleInputEntry<StonecutterRecipe>> stonecutterRecipes = new ArrayList<SelectableRecipe.SingleInputEntry<StonecutterRecipe>>();
/*     */     
/*  95 */     List<IngredientCollector> propertySetCollectors = RECIPE_PROPERTY_SETS.entrySet().stream().map(e -> new IngredientCollector((ResourceKey)e.getKey(), (IngredientExtractor)e.getValue())).toList();
/*     */     
/*  97 */     this.recipes.values().forEach(recipeHolder -> {
/*  98 */           Recipe<?> recipe = recipeHolder.value();
/*  99 */           if (!recipe.isSpecial() && recipe.placementInfo().isImpossibleToPlace()) {
/* 100 */             LOGGER.warn("Recipe {} can't be placed due to empty ingredients and will be ignored", recipeHolder.id().identifier());
/*     */             
/*     */             return;
/*     */           } 
/* 104 */           propertySetCollectors.forEach(());
/*     */           
/* 106 */           if (recipe instanceof StonecutterRecipe) { StonecutterRecipe stonecutterRecipe = (StonecutterRecipe)recipe;
/* 107 */             RecipeHolder<StonecutterRecipe> castHolder = recipeHolder;
/* 108 */             if (isIngredientEnabled(enabledFlags, stonecutterRecipe.input()) && stonecutterRecipe.resultDisplay().isEnabled(enabledFlags)) {
/* 109 */               stonecutterRecipes.add(new SelectableRecipe.SingleInputEntry(stonecutterRecipe
/* 110 */                     .input(), new SelectableRecipe(stonecutterRecipe
/*     */                       
/* 112 */                       .resultDisplay(), 
/* 113 */                       Optional.of(castHolder))));
/*     */             } }
/*     */         
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 120 */     this.propertySets = (Map)propertySetCollectors.stream().collect(Collectors.toUnmodifiableMap(c -> 
/* 121 */           c.key, c -> 
/* 122 */           c.asPropertySet(enabledFlags)));
/*     */     
/* 124 */     this.stonecutterRecipes = new SelectableRecipe.SingleInputSet(stonecutterRecipes);
/*     */     
/* 126 */     this.allDisplays = unpackRecipeInfo(this.recipes.values(), enabledFlags);
/* 127 */     this.recipeToDisplay = (Map)this.allDisplays.stream().collect(Collectors.groupingBy(r -> r.parent.id(), java.util.IdentityHashMap::new, Collectors.toList()));
/*     */   }
/*     */   
/*     */   private static List<Ingredient> filterDisabled(FeatureFlagSet enabledFlags, List<Ingredient> ingredients) {
/* 131 */     ingredients.removeIf(e -> !isIngredientEnabled(enabledFlags, e));
/* 132 */     return ingredients;
/*     */   }
/*     */ 
/*     */   
/* 136 */   private static boolean isIngredientEnabled(FeatureFlagSet enabledFlags, Ingredient ingredient) { return ingredient.items().allMatch(i -> ((Item)i.value()).isEnabled(enabledFlags)); }
/*     */ 
/*     */   
/*     */   public <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(RecipeType<T> type, I input, Level level, ResourceKey<Recipe<?>> recipeHint) {
/* 140 */     RecipeHolder<T> hintedRecipe = (recipeHint != null) ? byKeyTyped(type, recipeHint) : null;
/* 141 */     return getRecipeFor(type, input, level, hintedRecipe);
/*     */   }
/*     */   
/*     */   public <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(RecipeType<T> type, I input, Level level, RecipeHolder<T> recipeHint) {
/* 145 */     if (recipeHint != null && recipeHint.value().matches(input, level)) {
/* 146 */       return Optional.of(recipeHint);
/*     */     }
/* 148 */     return getRecipeFor(type, input, level);
/*     */   }
/*     */ 
/*     */   
/* 152 */   public <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(RecipeType<T> type, I input, Level level) { return this.recipes.getRecipesFor(type, input, level).findFirst(); }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public Optional<RecipeHolder<?>> byKey(ResourceKey<Recipe<?>> recipeId) { return Optional.ofNullable(this.recipes.byKey(recipeId)); }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T extends Recipe<?>> RecipeHolder<T> byKeyTyped(RecipeType<T> type, ResourceKey<Recipe<?>> recipeId) {
/* 161 */     RecipeHolder<?> recipe = this.recipes.byKey(recipeId);
/* 162 */     if (recipe != null && recipe.value().getType().equals(type)) {
/* 163 */       return recipe;
/*     */     }
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 169 */   public Map<ResourceKey<RecipePropertySet>, RecipePropertySet> getSynchronizedItemProperties() { return this.propertySets; }
/*     */ 
/*     */ 
/*     */   
/* 173 */   public SelectableRecipe.SingleInputSet<StonecutterRecipe> getSynchronizedStonecutterRecipes() { return this.stonecutterRecipes; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 178 */   public RecipePropertySet propertySet(ResourceKey<RecipePropertySet> id) { return (RecipePropertySet)this.propertySets.getOrDefault(id, RecipePropertySet.EMPTY); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 183 */   public SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes() { return this.stonecutterRecipes; }
/*     */ 
/*     */ 
/*     */   
/* 187 */   public Collection<RecipeHolder<?>> getRecipes() { return this.recipes.values(); }
/*     */ 
/*     */   
/*     */   public ServerDisplayInfo getRecipeFromDisplay(RecipeDisplayId id) {
/* 191 */     int index = id.index();
/* 192 */     return (index >= 0 && index < this.allDisplays.size()) ? (ServerDisplayInfo)this.allDisplays.get(index) : null;
/*     */   }
/*     */   
/*     */   public void listDisplaysForRecipe(ResourceKey<Recipe<?>> id, Consumer<RecipeDisplayEntry> output) {
/* 196 */     List<ServerDisplayInfo> recipes = (List)this.recipeToDisplay.get(id);
/*     */     
/* 198 */     if (recipes != null) {
/* 199 */       recipes.forEach(e -> output.accept(e.display));
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static RecipeHolder<?> fromJson(ResourceKey<Recipe<?>> id, JsonObject object, HolderLookup.Provider registries) {
/* 205 */     Recipe<?> recipe = (Recipe)Recipe.CODEC.parse(registries.createSerializationContext(JsonOps.INSTANCE), object).getOrThrow(com.google.gson.JsonParseException::new);
/* 206 */     return new RecipeHolder(id, recipe);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <I extends RecipeInput, T extends Recipe<I>> CachedCheck<I, T> createCheck(final RecipeType<T> type) {
/* 214 */     return new CachedCheck<I, T>()
/*     */       {
/*     */         private ResourceKey<Recipe<?>> lastRecipe;
/*     */         
/*     */         public Optional<RecipeHolder<T>> getRecipeFor(I input, ServerLevel level) {
/* 219 */           RecipeManager recipeManager = level.recipeAccess();
/* 220 */           Optional<RecipeHolder<T>> result = recipeManager.getRecipeFor(type, input, level, this.lastRecipe);
/* 221 */           if (result.isPresent()) {
/* 222 */             RecipeHolder<T> unpackedResult = (RecipeHolder)result.get();
/* 223 */             this.lastRecipe = unpackedResult.id();
/* 224 */             return Optional.of(unpackedResult);
/*     */           } 
/* 226 */           return Optional.empty();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static List<ServerDisplayInfo> unpackRecipeInfo(Iterable<RecipeHolder<?>> recipes, FeatureFlagSet enabledFeatures) {
/* 232 */     List<ServerDisplayInfo> result = new ArrayList<ServerDisplayInfo>();
/* 233 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 234 */     for (RecipeHolder<?> recipeHolder : recipes) {
/* 235 */       Optional<List<Ingredient>> placementCheck; OptionalInt groupId; Recipe<?> recipe = recipeHolder.value();
/*     */       
/* 237 */       if (recipe.group().isEmpty()) {
/* 238 */         groupId = OptionalInt.empty();
/*     */       } else {
/* 240 */         groupId = OptionalInt.of(object2IntOpenHashMap.computeIfAbsent(recipe.group(), id -> recipeGroups.size()));
/*     */       } 
/*     */ 
/*     */       
/* 244 */       if (recipe.isSpecial()) {
/* 245 */         placementCheck = Optional.empty();
/*     */       } else {
/* 247 */         placementCheck = Optional.of(recipe.placementInfo().ingredients());
/*     */       } 
/*     */       
/* 250 */       for (RecipeDisplay recipeDisplay : recipe.display()) {
/* 251 */         if (!recipeDisplay.isEnabled(enabledFeatures)) {
/*     */           continue;
/*     */         }
/* 254 */         int nextDisplayId = result.size();
/* 255 */         RecipeDisplayId id = new RecipeDisplayId(nextDisplayId);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 261 */         RecipeDisplayEntry entry = new RecipeDisplayEntry(id, recipeDisplay, groupId, recipe.recipeBookCategory(), placementCheck);
/*     */ 
/*     */ 
/*     */         
/* 265 */         result.add(new ServerDisplayInfo(entry, recipeHolder));
/*     */       } 
/*     */     } 
/* 268 */     return result;
/*     */   }
/*     */   public static final class ServerDisplayInfo extends Record { private final RecipeDisplayEntry display; private final RecipeHolder<?> parent;
/* 271 */     public ServerDisplayInfo(RecipeDisplayEntry display, RecipeHolder<?> parent) { this.display = display; this.parent = parent; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #271	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 271 */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo; } public RecipeDisplayEntry display() { return this.display; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #271	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #271	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/RecipeManager$ServerDisplayInfo;
/* 271 */       //   0	8	1	o	Ljava/lang/Object; } public RecipeHolder<?> parent() { return this.parent; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 280 */   private static IngredientExtractor forSingleInput(RecipeType<? extends SingleItemRecipe> type) { return recipe -> { SingleItemRecipe singleItemRecipe = (SingleItemRecipe)recipe; return (recipe.getType() == type && recipe instanceof SingleItemRecipe) ? Optional.of(singleItemRecipe.input()) : Optional.empty();
/*     */       }; }
/*     */   public static class IngredientCollector extends Object implements Consumer<Recipe<?>> { private final ResourceKey<RecipePropertySet> key; private final RecipeManager.IngredientExtractor extractor;
/*     */     private final List<Ingredient> ingredients;
/*     */     
/*     */     protected IngredientCollector(ResourceKey<RecipePropertySet> key, RecipeManager.IngredientExtractor extractor) {
/* 286 */       this.ingredients = new ArrayList();
/*     */ 
/*     */       
/* 289 */       this.key = key;
/* 290 */       this.extractor = extractor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 295 */     public void accept(Recipe<?> recipe) { Objects.requireNonNull(this.ingredients); this.extractor.apply(recipe).ifPresent(this.ingredients::add); }
/*     */ 
/*     */ 
/*     */     
/* 299 */     public RecipePropertySet asPropertySet(FeatureFlagSet enabledFeatures) { return RecipePropertySet.create(RecipeManager.filterDisabled(enabledFeatures, this.ingredients)); } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface IngredientExtractor {
/*     */     Optional<Ingredient> apply(Recipe<?> param1Recipe);
/*     */   }
/*     */   
/*     */   public static interface CachedCheck<I extends RecipeInput, T extends Recipe<I>> {
/*     */     Optional<RecipeHolder<T>> getRecipeFor(I param1I, ServerLevel param1ServerLevel);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\crafting\RecipeManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */