/*      */ package net.minecraft.data.recipes.packs;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.advancements.criterion.InventoryChangeTrigger;
/*      */ import net.minecraft.advancements.criterion.MinMaxBounds;
/*      */ import net.minecraft.advancements.criterion.PlayerTrigger;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.data.PackOutput;
/*      */ import net.minecraft.data.recipes.RecipeCategory;
/*      */ import net.minecraft.data.recipes.RecipeOutput;
/*      */ import net.minecraft.data.recipes.RecipeProvider;
/*      */ import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
/*      */ import net.minecraft.data.recipes.SingleItemRecipeBuilder;
/*      */ import net.minecraft.data.recipes.SpecialRecipeBuilder;
/*      */ import net.minecraft.data.recipes.TransmuteRecipeBuilder;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.BundleItem;
/*      */ import net.minecraft.world.item.DyeColor;
/*      */ import net.minecraft.world.item.DyeItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*      */ import net.minecraft.world.item.crafting.Ingredient;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.crafting.RecipeSerializer;
/*      */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*      */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ShulkerBoxBlock;
/*      */ import net.minecraft.world.level.block.SuspiciousEffectHolder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class VanillaRecipeProvider
/*      */   extends RecipeProvider
/*      */ {
/*   64 */   private static final ImmutableList<ItemLike> COAL_SMELTABLES = ImmutableList.of(Items.COAL_ORE, Items.DEEPSLATE_COAL_ORE);
/*   65 */   private static final ImmutableList<ItemLike> IRON_SMELTABLES = ImmutableList.of(Items.IRON_ORE, Items.DEEPSLATE_IRON_ORE, Items.RAW_IRON);
/*   66 */   private static final ImmutableList<ItemLike> COPPER_SMELTABLES = ImmutableList.of(Items.COPPER_ORE, Items.DEEPSLATE_COPPER_ORE, Items.RAW_COPPER);
/*   67 */   private static final ImmutableList<ItemLike> GOLD_SMELTABLES = ImmutableList.of(Items.GOLD_ORE, Items.DEEPSLATE_GOLD_ORE, Items.NETHER_GOLD_ORE, Items.RAW_GOLD);
/*   68 */   private static final ImmutableList<ItemLike> DIAMOND_SMELTABLES = ImmutableList.of(Items.DIAMOND_ORE, Items.DEEPSLATE_DIAMOND_ORE);
/*   69 */   private static final ImmutableList<ItemLike> LAPIS_SMELTABLES = ImmutableList.of(Items.LAPIS_ORE, Items.DEEPSLATE_LAPIS_ORE);
/*   70 */   private static final ImmutableList<ItemLike> REDSTONE_SMELTABLES = ImmutableList.of(Items.REDSTONE_ORE, Items.DEEPSLATE_REDSTONE_ORE);
/*   71 */   private static final ImmutableList<ItemLike> EMERALD_SMELTABLES = ImmutableList.of(Items.EMERALD_ORE, Items.DEEPSLATE_EMERALD_ORE);
/*      */   
/*      */   public static class Runner
/*      */     extends RecipeProvider.Runner {
/*   75 */     public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) { super(packOutput, registries); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   80 */     protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) { return new VanillaRecipeProvider(registries, output); }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   85 */     public String getName() { return "Vanilla Recipes"; }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*   90 */   private VanillaRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) { super(registries, output); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void buildRecipes() {
/*   95 */     this.output.includeRootAdvancement();
/*      */     
/*   97 */     generateForEnabledBlockFamilies(FeatureFlagSet.of(FeatureFlags.VANILLA));
/*      */     
/*   99 */     planksFromLog(Blocks.ACACIA_PLANKS, ItemTags.ACACIA_LOGS, 4);
/*  100 */     planksFromLogs(Blocks.BIRCH_PLANKS, ItemTags.BIRCH_LOGS, 4);
/*  101 */     planksFromLogs(Blocks.CRIMSON_PLANKS, ItemTags.CRIMSON_STEMS, 4);
/*  102 */     planksFromLog(Blocks.DARK_OAK_PLANKS, ItemTags.DARK_OAK_LOGS, 4);
/*  103 */     planksFromLog(Blocks.PALE_OAK_PLANKS, ItemTags.PALE_OAK_LOGS, 4);
/*  104 */     planksFromLogs(Blocks.JUNGLE_PLANKS, ItemTags.JUNGLE_LOGS, 4);
/*  105 */     planksFromLogs(Blocks.OAK_PLANKS, ItemTags.OAK_LOGS, 4);
/*  106 */     planksFromLogs(Blocks.SPRUCE_PLANKS, ItemTags.SPRUCE_LOGS, 4);
/*  107 */     planksFromLogs(Blocks.WARPED_PLANKS, ItemTags.WARPED_STEMS, 4);
/*  108 */     planksFromLogs(Blocks.MANGROVE_PLANKS, ItemTags.MANGROVE_LOGS, 4);
/*      */     
/*  110 */     woodFromLogs(Blocks.ACACIA_WOOD, Blocks.ACACIA_LOG);
/*  111 */     woodFromLogs(Blocks.BIRCH_WOOD, Blocks.BIRCH_LOG);
/*  112 */     woodFromLogs(Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_LOG);
/*  113 */     woodFromLogs(Blocks.PALE_OAK_WOOD, Blocks.PALE_OAK_LOG);
/*  114 */     woodFromLogs(Blocks.JUNGLE_WOOD, Blocks.JUNGLE_LOG);
/*  115 */     woodFromLogs(Blocks.OAK_WOOD, Blocks.OAK_LOG);
/*  116 */     woodFromLogs(Blocks.SPRUCE_WOOD, Blocks.SPRUCE_LOG);
/*  117 */     woodFromLogs(Blocks.CRIMSON_HYPHAE, Blocks.CRIMSON_STEM);
/*  118 */     woodFromLogs(Blocks.WARPED_HYPHAE, Blocks.WARPED_STEM);
/*  119 */     woodFromLogs(Blocks.MANGROVE_WOOD, Blocks.MANGROVE_LOG);
/*      */     
/*  121 */     woodFromLogs(Blocks.STRIPPED_ACACIA_WOOD, Blocks.STRIPPED_ACACIA_LOG);
/*  122 */     woodFromLogs(Blocks.STRIPPED_BIRCH_WOOD, Blocks.STRIPPED_BIRCH_LOG);
/*  123 */     woodFromLogs(Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_LOG);
/*  124 */     woodFromLogs(Blocks.STRIPPED_PALE_OAK_WOOD, Blocks.STRIPPED_PALE_OAK_LOG);
/*  125 */     woodFromLogs(Blocks.STRIPPED_JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_LOG);
/*  126 */     woodFromLogs(Blocks.STRIPPED_OAK_WOOD, Blocks.STRIPPED_OAK_LOG);
/*  127 */     woodFromLogs(Blocks.STRIPPED_SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_LOG);
/*  128 */     woodFromLogs(Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_STEM);
/*  129 */     woodFromLogs(Blocks.STRIPPED_WARPED_HYPHAE, Blocks.STRIPPED_WARPED_STEM);
/*  130 */     woodFromLogs(Blocks.STRIPPED_MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_LOG);
/*      */     
/*  132 */     woodenBoat(Items.ACACIA_BOAT, Blocks.ACACIA_PLANKS);
/*  133 */     woodenBoat(Items.BIRCH_BOAT, Blocks.BIRCH_PLANKS);
/*  134 */     woodenBoat(Items.DARK_OAK_BOAT, Blocks.DARK_OAK_PLANKS);
/*  135 */     woodenBoat(Items.PALE_OAK_BOAT, Blocks.PALE_OAK_PLANKS);
/*  136 */     woodenBoat(Items.JUNGLE_BOAT, Blocks.JUNGLE_PLANKS);
/*  137 */     woodenBoat(Items.OAK_BOAT, Blocks.OAK_PLANKS);
/*  138 */     woodenBoat(Items.SPRUCE_BOAT, Blocks.SPRUCE_PLANKS);
/*  139 */     woodenBoat(Items.MANGROVE_BOAT, Blocks.MANGROVE_PLANKS);
/*      */     
/*  141 */     shelf(Blocks.ACACIA_SHELF, Items.STRIPPED_ACACIA_LOG);
/*  142 */     shelf(Blocks.BAMBOO_SHELF, Items.STRIPPED_BAMBOO_BLOCK);
/*  143 */     shelf(Blocks.BIRCH_SHELF, Items.STRIPPED_BIRCH_LOG);
/*  144 */     shelf(Blocks.CHERRY_SHELF, Items.STRIPPED_CHERRY_LOG);
/*  145 */     shelf(Blocks.CRIMSON_SHELF, Items.STRIPPED_CRIMSON_STEM);
/*  146 */     shelf(Blocks.DARK_OAK_SHELF, Items.STRIPPED_DARK_OAK_LOG);
/*  147 */     shelf(Blocks.JUNGLE_SHELF, Items.STRIPPED_JUNGLE_LOG);
/*  148 */     shelf(Blocks.MANGROVE_SHELF, Items.STRIPPED_MANGROVE_LOG);
/*  149 */     shelf(Blocks.OAK_SHELF, Items.STRIPPED_OAK_LOG);
/*  150 */     shelf(Blocks.PALE_OAK_SHELF, Items.STRIPPED_PALE_OAK_LOG);
/*  151 */     shelf(Blocks.SPRUCE_SHELF, Items.STRIPPED_SPRUCE_LOG);
/*  152 */     shelf(Blocks.WARPED_SHELF, Items.STRIPPED_WARPED_STEM);
/*      */     
/*  154 */     List<Item> dyes = List.of(new Item[] { Items.BLACK_DYE, Items.BLUE_DYE, Items.BROWN_DYE, Items.CYAN_DYE, Items.GRAY_DYE, Items.GREEN_DYE, Items.LIGHT_BLUE_DYE, Items.LIGHT_GRAY_DYE, Items.LIME_DYE, Items.MAGENTA_DYE, Items.ORANGE_DYE, Items.PINK_DYE, Items.PURPLE_DYE, Items.RED_DYE, Items.YELLOW_DYE, Items.WHITE_DYE });
/*      */ 
/*      */     
/*  157 */     List<Item> wools = List.of(new Item[] { Items.BLACK_WOOL, Items.BLUE_WOOL, Items.BROWN_WOOL, Items.CYAN_WOOL, Items.GRAY_WOOL, Items.GREEN_WOOL, Items.LIGHT_BLUE_WOOL, Items.LIGHT_GRAY_WOOL, Items.LIME_WOOL, Items.MAGENTA_WOOL, Items.ORANGE_WOOL, Items.PINK_WOOL, Items.PURPLE_WOOL, Items.RED_WOOL, Items.YELLOW_WOOL, Items.WHITE_WOOL });
/*      */ 
/*      */     
/*  160 */     List<Item> beds = List.of(new Item[] { Items.BLACK_BED, Items.BLUE_BED, Items.BROWN_BED, Items.CYAN_BED, Items.GRAY_BED, Items.GREEN_BED, Items.LIGHT_BLUE_BED, Items.LIGHT_GRAY_BED, Items.LIME_BED, Items.MAGENTA_BED, Items.ORANGE_BED, Items.PINK_BED, Items.PURPLE_BED, Items.RED_BED, Items.YELLOW_BED, Items.WHITE_BED });
/*      */ 
/*      */     
/*  163 */     List<Item> carpets = List.of(new Item[] { Items.BLACK_CARPET, Items.BLUE_CARPET, Items.BROWN_CARPET, Items.CYAN_CARPET, Items.GRAY_CARPET, Items.GREEN_CARPET, Items.LIGHT_BLUE_CARPET, Items.LIGHT_GRAY_CARPET, Items.LIME_CARPET, Items.MAGENTA_CARPET, Items.ORANGE_CARPET, Items.PINK_CARPET, Items.PURPLE_CARPET, Items.RED_CARPET, Items.YELLOW_CARPET, Items.WHITE_CARPET });
/*      */ 
/*      */     
/*  166 */     List<Item> harnesses = List.of(new Item[] { Items.BLACK_HARNESS, Items.BLUE_HARNESS, Items.BROWN_HARNESS, Items.CYAN_HARNESS, Items.GRAY_HARNESS, Items.GREEN_HARNESS, Items.LIGHT_BLUE_HARNESS, Items.LIGHT_GRAY_HARNESS, Items.LIME_HARNESS, Items.MAGENTA_HARNESS, Items.ORANGE_HARNESS, Items.PINK_HARNESS, Items.PURPLE_HARNESS, Items.RED_HARNESS, Items.YELLOW_HARNESS, Items.WHITE_HARNESS });
/*      */ 
/*      */ 
/*      */     
/*  170 */     colorItemWithDye(dyes, wools, "wool", RecipeCategory.BUILDING_BLOCKS);
/*  171 */     colorItemWithDye(dyes, beds, "bed_dye", RecipeCategory.DECORATIONS);
/*  172 */     colorItemWithDye(dyes, carpets, "carpet_dye", RecipeCategory.DECORATIONS);
/*  173 */     colorItemWithDye(dyes, harnesses, "harness_dye", RecipeCategory.COMBAT);
/*      */     
/*  175 */     carpet(Blocks.BLACK_CARPET, Blocks.BLACK_WOOL);
/*  176 */     bedFromPlanksAndWool(Items.BLACK_BED, Blocks.BLACK_WOOL);
/*  177 */     banner(Items.BLACK_BANNER, Blocks.BLACK_WOOL);
/*      */     
/*  179 */     carpet(Blocks.BLUE_CARPET, Blocks.BLUE_WOOL);
/*  180 */     bedFromPlanksAndWool(Items.BLUE_BED, Blocks.BLUE_WOOL);
/*  181 */     banner(Items.BLUE_BANNER, Blocks.BLUE_WOOL);
/*      */     
/*  183 */     carpet(Blocks.BROWN_CARPET, Blocks.BROWN_WOOL);
/*  184 */     bedFromPlanksAndWool(Items.BROWN_BED, Blocks.BROWN_WOOL);
/*  185 */     banner(Items.BROWN_BANNER, Blocks.BROWN_WOOL);
/*      */     
/*  187 */     carpet(Blocks.CYAN_CARPET, Blocks.CYAN_WOOL);
/*  188 */     bedFromPlanksAndWool(Items.CYAN_BED, Blocks.CYAN_WOOL);
/*  189 */     banner(Items.CYAN_BANNER, Blocks.CYAN_WOOL);
/*      */     
/*  191 */     carpet(Blocks.GRAY_CARPET, Blocks.GRAY_WOOL);
/*  192 */     bedFromPlanksAndWool(Items.GRAY_BED, Blocks.GRAY_WOOL);
/*  193 */     banner(Items.GRAY_BANNER, Blocks.GRAY_WOOL);
/*      */     
/*  195 */     carpet(Blocks.GREEN_CARPET, Blocks.GREEN_WOOL);
/*  196 */     bedFromPlanksAndWool(Items.GREEN_BED, Blocks.GREEN_WOOL);
/*  197 */     banner(Items.GREEN_BANNER, Blocks.GREEN_WOOL);
/*      */     
/*  199 */     carpet(Blocks.LIGHT_BLUE_CARPET, Blocks.LIGHT_BLUE_WOOL);
/*  200 */     bedFromPlanksAndWool(Items.LIGHT_BLUE_BED, Blocks.LIGHT_BLUE_WOOL);
/*  201 */     banner(Items.LIGHT_BLUE_BANNER, Blocks.LIGHT_BLUE_WOOL);
/*      */     
/*  203 */     carpet(Blocks.LIGHT_GRAY_CARPET, Blocks.LIGHT_GRAY_WOOL);
/*  204 */     bedFromPlanksAndWool(Items.LIGHT_GRAY_BED, Blocks.LIGHT_GRAY_WOOL);
/*  205 */     banner(Items.LIGHT_GRAY_BANNER, Blocks.LIGHT_GRAY_WOOL);
/*      */     
/*  207 */     carpet(Blocks.LIME_CARPET, Blocks.LIME_WOOL);
/*  208 */     bedFromPlanksAndWool(Items.LIME_BED, Blocks.LIME_WOOL);
/*  209 */     banner(Items.LIME_BANNER, Blocks.LIME_WOOL);
/*      */     
/*  211 */     carpet(Blocks.MAGENTA_CARPET, Blocks.MAGENTA_WOOL);
/*  212 */     bedFromPlanksAndWool(Items.MAGENTA_BED, Blocks.MAGENTA_WOOL);
/*  213 */     banner(Items.MAGENTA_BANNER, Blocks.MAGENTA_WOOL);
/*      */     
/*  215 */     carpet(Blocks.ORANGE_CARPET, Blocks.ORANGE_WOOL);
/*  216 */     bedFromPlanksAndWool(Items.ORANGE_BED, Blocks.ORANGE_WOOL);
/*  217 */     banner(Items.ORANGE_BANNER, Blocks.ORANGE_WOOL);
/*      */     
/*  219 */     carpet(Blocks.PINK_CARPET, Blocks.PINK_WOOL);
/*  220 */     bedFromPlanksAndWool(Items.PINK_BED, Blocks.PINK_WOOL);
/*  221 */     banner(Items.PINK_BANNER, Blocks.PINK_WOOL);
/*      */     
/*  223 */     carpet(Blocks.PURPLE_CARPET, Blocks.PURPLE_WOOL);
/*  224 */     bedFromPlanksAndWool(Items.PURPLE_BED, Blocks.PURPLE_WOOL);
/*  225 */     banner(Items.PURPLE_BANNER, Blocks.PURPLE_WOOL);
/*      */     
/*  227 */     carpet(Blocks.RED_CARPET, Blocks.RED_WOOL);
/*  228 */     bedFromPlanksAndWool(Items.RED_BED, Blocks.RED_WOOL);
/*  229 */     banner(Items.RED_BANNER, Blocks.RED_WOOL);
/*      */     
/*  231 */     carpet(Blocks.WHITE_CARPET, Blocks.WHITE_WOOL);
/*  232 */     bedFromPlanksAndWool(Items.WHITE_BED, Blocks.WHITE_WOOL);
/*  233 */     banner(Items.WHITE_BANNER, Blocks.WHITE_WOOL);
/*      */     
/*  235 */     carpet(Blocks.YELLOW_CARPET, Blocks.YELLOW_WOOL);
/*  236 */     bedFromPlanksAndWool(Items.YELLOW_BED, Blocks.YELLOW_WOOL);
/*  237 */     banner(Items.YELLOW_BANNER, Blocks.YELLOW_WOOL);
/*      */     
/*  239 */     carpet(Blocks.MOSS_CARPET, Blocks.MOSS_BLOCK);
/*  240 */     carpet(Blocks.PALE_MOSS_CARPET, Blocks.PALE_MOSS_BLOCK);
/*      */     
/*  242 */     harness(Items.WHITE_HARNESS, Blocks.WHITE_WOOL);
/*  243 */     harness(Items.ORANGE_HARNESS, Blocks.ORANGE_WOOL);
/*  244 */     harness(Items.MAGENTA_HARNESS, Blocks.MAGENTA_WOOL);
/*  245 */     harness(Items.LIGHT_BLUE_HARNESS, Blocks.LIGHT_BLUE_WOOL);
/*  246 */     harness(Items.YELLOW_HARNESS, Blocks.YELLOW_WOOL);
/*  247 */     harness(Items.LIME_HARNESS, Blocks.LIME_WOOL);
/*  248 */     harness(Items.PINK_HARNESS, Blocks.PINK_WOOL);
/*  249 */     harness(Items.GRAY_HARNESS, Blocks.GRAY_WOOL);
/*  250 */     harness(Items.LIGHT_GRAY_HARNESS, Blocks.LIGHT_GRAY_WOOL);
/*  251 */     harness(Items.CYAN_HARNESS, Blocks.CYAN_WOOL);
/*  252 */     harness(Items.PURPLE_HARNESS, Blocks.PURPLE_WOOL);
/*  253 */     harness(Items.BLUE_HARNESS, Blocks.BLUE_WOOL);
/*  254 */     harness(Items.BROWN_HARNESS, Blocks.BROWN_WOOL);
/*  255 */     harness(Items.GREEN_HARNESS, Blocks.GREEN_WOOL);
/*  256 */     harness(Items.RED_HARNESS, Blocks.RED_WOOL);
/*  257 */     harness(Items.BLACK_HARNESS, Blocks.BLACK_WOOL);
/*      */     
/*  259 */     stainedGlassFromGlassAndDye(Blocks.BLACK_STAINED_GLASS, Items.BLACK_DYE);
/*  260 */     stainedGlassPaneFromStainedGlass(Blocks.BLACK_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS);
/*  261 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.BLACK_STAINED_GLASS_PANE, Items.BLACK_DYE);
/*      */     
/*  263 */     stainedGlassFromGlassAndDye(Blocks.BLUE_STAINED_GLASS, Items.BLUE_DYE);
/*  264 */     stainedGlassPaneFromStainedGlass(Blocks.BLUE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS);
/*  265 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.BLUE_STAINED_GLASS_PANE, Items.BLUE_DYE);
/*      */     
/*  267 */     stainedGlassFromGlassAndDye(Blocks.BROWN_STAINED_GLASS, Items.BROWN_DYE);
/*  268 */     stainedGlassPaneFromStainedGlass(Blocks.BROWN_STAINED_GLASS_PANE, Blocks.BROWN_STAINED_GLASS);
/*  269 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.BROWN_STAINED_GLASS_PANE, Items.BROWN_DYE);
/*      */     
/*  271 */     stainedGlassFromGlassAndDye(Blocks.CYAN_STAINED_GLASS, Items.CYAN_DYE);
/*  272 */     stainedGlassPaneFromStainedGlass(Blocks.CYAN_STAINED_GLASS_PANE, Blocks.CYAN_STAINED_GLASS);
/*  273 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.CYAN_STAINED_GLASS_PANE, Items.CYAN_DYE);
/*      */     
/*  275 */     stainedGlassFromGlassAndDye(Blocks.GRAY_STAINED_GLASS, Items.GRAY_DYE);
/*  276 */     stainedGlassPaneFromStainedGlass(Blocks.GRAY_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS);
/*  277 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.GRAY_STAINED_GLASS_PANE, Items.GRAY_DYE);
/*      */     
/*  279 */     stainedGlassFromGlassAndDye(Blocks.GREEN_STAINED_GLASS, Items.GREEN_DYE);
/*  280 */     stainedGlassPaneFromStainedGlass(Blocks.GREEN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS);
/*  281 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.GREEN_STAINED_GLASS_PANE, Items.GREEN_DYE);
/*      */     
/*  283 */     stainedGlassFromGlassAndDye(Blocks.LIGHT_BLUE_STAINED_GLASS, Items.LIGHT_BLUE_DYE);
/*  284 */     stainedGlassPaneFromStainedGlass(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Blocks.LIGHT_BLUE_STAINED_GLASS);
/*  285 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Items.LIGHT_BLUE_DYE);
/*      */     
/*  287 */     stainedGlassFromGlassAndDye(Blocks.LIGHT_GRAY_STAINED_GLASS, Items.LIGHT_GRAY_DYE);
/*  288 */     stainedGlassPaneFromStainedGlass(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_STAINED_GLASS);
/*  289 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Items.LIGHT_GRAY_DYE);
/*      */     
/*  291 */     stainedGlassFromGlassAndDye(Blocks.LIME_STAINED_GLASS, Items.LIME_DYE);
/*  292 */     stainedGlassPaneFromStainedGlass(Blocks.LIME_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS);
/*  293 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.LIME_STAINED_GLASS_PANE, Items.LIME_DYE);
/*      */     
/*  295 */     stainedGlassFromGlassAndDye(Blocks.MAGENTA_STAINED_GLASS, Items.MAGENTA_DYE);
/*  296 */     stainedGlassPaneFromStainedGlass(Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS);
/*  297 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.MAGENTA_STAINED_GLASS_PANE, Items.MAGENTA_DYE);
/*      */     
/*  299 */     stainedGlassFromGlassAndDye(Blocks.ORANGE_STAINED_GLASS, Items.ORANGE_DYE);
/*  300 */     stainedGlassPaneFromStainedGlass(Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.ORANGE_STAINED_GLASS);
/*  301 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.ORANGE_STAINED_GLASS_PANE, Items.ORANGE_DYE);
/*      */     
/*  303 */     stainedGlassFromGlassAndDye(Blocks.PINK_STAINED_GLASS, Items.PINK_DYE);
/*  304 */     stainedGlassPaneFromStainedGlass(Blocks.PINK_STAINED_GLASS_PANE, Blocks.PINK_STAINED_GLASS);
/*  305 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.PINK_STAINED_GLASS_PANE, Items.PINK_DYE);
/*      */     
/*  307 */     stainedGlassFromGlassAndDye(Blocks.PURPLE_STAINED_GLASS, Items.PURPLE_DYE);
/*  308 */     stainedGlassPaneFromStainedGlass(Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.PURPLE_STAINED_GLASS);
/*  309 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.PURPLE_STAINED_GLASS_PANE, Items.PURPLE_DYE);
/*      */     
/*  311 */     stainedGlassFromGlassAndDye(Blocks.RED_STAINED_GLASS, Items.RED_DYE);
/*  312 */     stainedGlassPaneFromStainedGlass(Blocks.RED_STAINED_GLASS_PANE, Blocks.RED_STAINED_GLASS);
/*  313 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.RED_STAINED_GLASS_PANE, Items.RED_DYE);
/*      */     
/*  315 */     stainedGlassFromGlassAndDye(Blocks.WHITE_STAINED_GLASS, Items.WHITE_DYE);
/*  316 */     stainedGlassPaneFromStainedGlass(Blocks.WHITE_STAINED_GLASS_PANE, Blocks.WHITE_STAINED_GLASS);
/*  317 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.WHITE_STAINED_GLASS_PANE, Items.WHITE_DYE);
/*      */     
/*  319 */     stainedGlassFromGlassAndDye(Blocks.YELLOW_STAINED_GLASS, Items.YELLOW_DYE);
/*  320 */     stainedGlassPaneFromStainedGlass(Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.YELLOW_STAINED_GLASS);
/*  321 */     stainedGlassPaneFromGlassPaneAndDye(Blocks.YELLOW_STAINED_GLASS_PANE, Items.YELLOW_DYE);
/*      */     
/*  323 */     coloredTerracottaFromTerracottaAndDye(Blocks.BLACK_TERRACOTTA, Items.BLACK_DYE);
/*  324 */     coloredTerracottaFromTerracottaAndDye(Blocks.BLUE_TERRACOTTA, Items.BLUE_DYE);
/*  325 */     coloredTerracottaFromTerracottaAndDye(Blocks.BROWN_TERRACOTTA, Items.BROWN_DYE);
/*  326 */     coloredTerracottaFromTerracottaAndDye(Blocks.CYAN_TERRACOTTA, Items.CYAN_DYE);
/*  327 */     coloredTerracottaFromTerracottaAndDye(Blocks.GRAY_TERRACOTTA, Items.GRAY_DYE);
/*  328 */     coloredTerracottaFromTerracottaAndDye(Blocks.GREEN_TERRACOTTA, Items.GREEN_DYE);
/*  329 */     coloredTerracottaFromTerracottaAndDye(Blocks.LIGHT_BLUE_TERRACOTTA, Items.LIGHT_BLUE_DYE);
/*  330 */     coloredTerracottaFromTerracottaAndDye(Blocks.LIGHT_GRAY_TERRACOTTA, Items.LIGHT_GRAY_DYE);
/*  331 */     coloredTerracottaFromTerracottaAndDye(Blocks.LIME_TERRACOTTA, Items.LIME_DYE);
/*  332 */     coloredTerracottaFromTerracottaAndDye(Blocks.MAGENTA_TERRACOTTA, Items.MAGENTA_DYE);
/*  333 */     coloredTerracottaFromTerracottaAndDye(Blocks.ORANGE_TERRACOTTA, Items.ORANGE_DYE);
/*  334 */     coloredTerracottaFromTerracottaAndDye(Blocks.PINK_TERRACOTTA, Items.PINK_DYE);
/*  335 */     coloredTerracottaFromTerracottaAndDye(Blocks.PURPLE_TERRACOTTA, Items.PURPLE_DYE);
/*  336 */     coloredTerracottaFromTerracottaAndDye(Blocks.RED_TERRACOTTA, Items.RED_DYE);
/*  337 */     coloredTerracottaFromTerracottaAndDye(Blocks.WHITE_TERRACOTTA, Items.WHITE_DYE);
/*  338 */     coloredTerracottaFromTerracottaAndDye(Blocks.YELLOW_TERRACOTTA, Items.YELLOW_DYE);
/*      */     
/*  340 */     concretePowder(Blocks.BLACK_CONCRETE_POWDER, Items.BLACK_DYE);
/*  341 */     concretePowder(Blocks.BLUE_CONCRETE_POWDER, Items.BLUE_DYE);
/*  342 */     concretePowder(Blocks.BROWN_CONCRETE_POWDER, Items.BROWN_DYE);
/*  343 */     concretePowder(Blocks.CYAN_CONCRETE_POWDER, Items.CYAN_DYE);
/*  344 */     concretePowder(Blocks.GRAY_CONCRETE_POWDER, Items.GRAY_DYE);
/*  345 */     concretePowder(Blocks.GREEN_CONCRETE_POWDER, Items.GREEN_DYE);
/*  346 */     concretePowder(Blocks.LIGHT_BLUE_CONCRETE_POWDER, Items.LIGHT_BLUE_DYE);
/*  347 */     concretePowder(Blocks.LIGHT_GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_DYE);
/*  348 */     concretePowder(Blocks.LIME_CONCRETE_POWDER, Items.LIME_DYE);
/*  349 */     concretePowder(Blocks.MAGENTA_CONCRETE_POWDER, Items.MAGENTA_DYE);
/*  350 */     concretePowder(Blocks.ORANGE_CONCRETE_POWDER, Items.ORANGE_DYE);
/*  351 */     concretePowder(Blocks.PINK_CONCRETE_POWDER, Items.PINK_DYE);
/*  352 */     concretePowder(Blocks.PURPLE_CONCRETE_POWDER, Items.PURPLE_DYE);
/*  353 */     concretePowder(Blocks.RED_CONCRETE_POWDER, Items.RED_DYE);
/*  354 */     concretePowder(Blocks.WHITE_CONCRETE_POWDER, Items.WHITE_DYE);
/*  355 */     concretePowder(Blocks.YELLOW_CONCRETE_POWDER, Items.YELLOW_DYE);
/*      */     
/*  357 */     dryGhast(Blocks.DRIED_GHAST);
/*      */     
/*  359 */     shaped(RecipeCategory.DECORATIONS, Items.CANDLE)
/*  360 */       .define(Character.valueOf('S'), Items.STRING)
/*  361 */       .define(Character.valueOf('H'), Items.HONEYCOMB)
/*  362 */       .pattern("S")
/*  363 */       .pattern("H")
/*  364 */       .unlockedBy("has_string", has(Items.STRING))
/*  365 */       .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
/*  366 */       .save(this.output);
/*      */     
/*  368 */     candle(Blocks.BLACK_CANDLE, Items.BLACK_DYE);
/*  369 */     candle(Blocks.BLUE_CANDLE, Items.BLUE_DYE);
/*  370 */     candle(Blocks.BROWN_CANDLE, Items.BROWN_DYE);
/*  371 */     candle(Blocks.CYAN_CANDLE, Items.CYAN_DYE);
/*  372 */     candle(Blocks.GRAY_CANDLE, Items.GRAY_DYE);
/*  373 */     candle(Blocks.GREEN_CANDLE, Items.GREEN_DYE);
/*  374 */     candle(Blocks.LIGHT_BLUE_CANDLE, Items.LIGHT_BLUE_DYE);
/*  375 */     candle(Blocks.LIGHT_GRAY_CANDLE, Items.LIGHT_GRAY_DYE);
/*  376 */     candle(Blocks.LIME_CANDLE, Items.LIME_DYE);
/*  377 */     candle(Blocks.MAGENTA_CANDLE, Items.MAGENTA_DYE);
/*  378 */     candle(Blocks.ORANGE_CANDLE, Items.ORANGE_DYE);
/*  379 */     candle(Blocks.PINK_CANDLE, Items.PINK_DYE);
/*  380 */     candle(Blocks.PURPLE_CANDLE, Items.PURPLE_DYE);
/*  381 */     candle(Blocks.RED_CANDLE, Items.RED_DYE);
/*  382 */     candle(Blocks.WHITE_CANDLE, Items.WHITE_DYE);
/*  383 */     candle(Blocks.YELLOW_CANDLE, Items.YELLOW_DYE);
/*      */     
/*  385 */     shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.PACKED_MUD, 1)
/*  386 */       .requires(Blocks.MUD)
/*  387 */       .requires(Items.WHEAT)
/*  388 */       .unlockedBy("has_mud", has(Blocks.MUD))
/*  389 */       .save(this.output);
/*      */     
/*  391 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.MUD_BRICKS, 4)
/*  392 */       .define(Character.valueOf('#'), Blocks.PACKED_MUD)
/*  393 */       .pattern("##")
/*  394 */       .pattern("##")
/*  395 */       .unlockedBy("has_packed_mud", has(Blocks.PACKED_MUD))
/*  396 */       .save(this.output);
/*      */     
/*  398 */     shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.MUDDY_MANGROVE_ROOTS, 1)
/*  399 */       .requires(Blocks.MUD)
/*  400 */       .requires(Items.MANGROVE_ROOTS)
/*  401 */       .unlockedBy("has_mangrove_roots", has(Blocks.MANGROVE_ROOTS))
/*  402 */       .save(this.output);
/*      */     
/*  404 */     shaped(RecipeCategory.TRANSPORTATION, Blocks.ACTIVATOR_RAIL, 6)
/*  405 */       .define(Character.valueOf('#'), Blocks.REDSTONE_TORCH)
/*  406 */       .define(Character.valueOf('S'), Items.STICK)
/*  407 */       .define(Character.valueOf('X'), Items.IRON_INGOT)
/*  408 */       .pattern("XSX")
/*  409 */       .pattern("X#X")
/*  410 */       .pattern("XSX")
/*  411 */       .unlockedBy("has_rail", has(Blocks.RAIL))
/*  412 */       .save(this.output);
/*      */     
/*  414 */     shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.ANDESITE, 2)
/*  415 */       .requires(Blocks.DIORITE)
/*  416 */       .requires(Blocks.COBBLESTONE)
/*  417 */       .unlockedBy("has_stone", has(Blocks.DIORITE))
/*  418 */       .save(this.output);
/*      */     
/*  420 */     shaped(RecipeCategory.DECORATIONS, Blocks.ANVIL)
/*  421 */       .define(Character.valueOf('I'), Blocks.IRON_BLOCK)
/*  422 */       .define(Character.valueOf('i'), Items.IRON_INGOT)
/*  423 */       .pattern("III")
/*  424 */       .pattern(" i ")
/*  425 */       .pattern("iii")
/*  426 */       .unlockedBy("has_iron_block", has(Blocks.IRON_BLOCK))
/*  427 */       .save(this.output);
/*      */     
/*  429 */     shaped(RecipeCategory.DECORATIONS, Items.ARMOR_STAND)
/*  430 */       .define(Character.valueOf('/'), Items.STICK)
/*  431 */       .define(Character.valueOf('_'), Blocks.SMOOTH_STONE_SLAB)
/*  432 */       .pattern("///")
/*  433 */       .pattern(" / ")
/*  434 */       .pattern("/_/")
/*  435 */       .unlockedBy("has_stone_slab", has(Blocks.SMOOTH_STONE_SLAB))
/*  436 */       .save(this.output);
/*      */     
/*  438 */     shaped(RecipeCategory.COMBAT, Items.ARROW, 4)
/*  439 */       .define(Character.valueOf('#'), Items.STICK)
/*  440 */       .define(Character.valueOf('X'), Items.FLINT)
/*  441 */       .define(Character.valueOf('Y'), Items.FEATHER)
/*  442 */       .pattern("X")
/*  443 */       .pattern("#")
/*  444 */       .pattern("Y")
/*  445 */       .unlockedBy("has_feather", has(Items.FEATHER))
/*  446 */       .unlockedBy("has_flint", has(Items.FLINT))
/*  447 */       .save(this.output);
/*      */     
/*  449 */     shaped(RecipeCategory.DECORATIONS, Blocks.BARREL, 1)
/*  450 */       .define(Character.valueOf('P'), ItemTags.PLANKS)
/*  451 */       .define(Character.valueOf('S'), ItemTags.WOODEN_SLABS)
/*  452 */       .pattern("PSP")
/*  453 */       .pattern("P P")
/*  454 */       .pattern("PSP")
/*  455 */       .unlockedBy("has_planks", has(ItemTags.PLANKS))
/*  456 */       .unlockedBy("has_wood_slab", has(ItemTags.WOODEN_SLABS))
/*  457 */       .save(this.output);
/*      */     
/*  459 */     shaped(RecipeCategory.MISC, Blocks.BEACON)
/*  460 */       .define(Character.valueOf('S'), Items.NETHER_STAR)
/*  461 */       .define(Character.valueOf('G'), Blocks.GLASS)
/*  462 */       .define(Character.valueOf('O'), Blocks.OBSIDIAN)
/*  463 */       .pattern("GGG")
/*  464 */       .pattern("GSG")
/*  465 */       .pattern("OOO")
/*  466 */       .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
/*  467 */       .save(this.output);
/*      */     
/*  469 */     shaped(RecipeCategory.DECORATIONS, Blocks.BEEHIVE)
/*  470 */       .define(Character.valueOf('P'), ItemTags.PLANKS)
/*  471 */       .define(Character.valueOf('H'), Items.HONEYCOMB)
/*  472 */       .pattern("PPP")
/*  473 */       .pattern("HHH")
/*  474 */       .pattern("PPP")
/*  475 */       .unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
/*  476 */       .save(this.output);
/*      */     
/*  478 */     shapeless(RecipeCategory.FOOD, Items.BEETROOT_SOUP)
/*  479 */       .requires(Items.BOWL)
/*  480 */       .requires(Items.BEETROOT, 6)
/*  481 */       .unlockedBy("has_beetroot", has(Items.BEETROOT))
/*  482 */       .save(this.output);
/*      */     
/*  484 */     shapeless(RecipeCategory.MISC, Items.BLACK_DYE)
/*  485 */       .requires(Items.INK_SAC)
/*  486 */       .group("black_dye")
/*  487 */       .unlockedBy("has_ink_sac", has(Items.INK_SAC))
/*  488 */       .save(this.output);
/*      */     
/*  490 */     oneToOneConversionRecipe(Items.BLACK_DYE, Blocks.WITHER_ROSE, "black_dye");
/*      */     
/*  492 */     shapeless(RecipeCategory.BREWING, Items.BLAZE_POWDER, 2)
/*  493 */       .requires(Items.BLAZE_ROD)
/*  494 */       .unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD))
/*  495 */       .save(this.output);
/*      */     
/*  497 */     shapeless(RecipeCategory.MISC, Items.BLUE_DYE)
/*  498 */       .requires(Items.LAPIS_LAZULI)
/*  499 */       .group("blue_dye")
/*  500 */       .unlockedBy("has_lapis_lazuli", has(Items.LAPIS_LAZULI))
/*  501 */       .save(this.output);
/*      */     
/*  503 */     oneToOneConversionRecipe(Items.BLUE_DYE, Blocks.CORNFLOWER, "blue_dye");
/*      */     
/*  505 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, Blocks.BLUE_ICE, Blocks.PACKED_ICE);
/*      */     
/*  507 */     shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 3)
/*  508 */       .requires(Items.BONE)
/*  509 */       .group("bonemeal")
/*  510 */       .unlockedBy("has_bone", has(Items.BONE))
/*  511 */       .save(this.output);
/*      */     
/*  513 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, Items.BONE_MEAL, RecipeCategory.BUILDING_BLOCKS, Items.BONE_BLOCK, "bone_meal_from_bone_block", "bonemeal");
/*      */     
/*  515 */     shapeless(RecipeCategory.MISC, Items.BOOK)
/*  516 */       .requires(Items.PAPER, 3)
/*  517 */       .requires(Items.LEATHER)
/*  518 */       .unlockedBy("has_paper", has(Items.PAPER))
/*  519 */       .save(this.output);
/*      */     
/*  521 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.BOOKSHELF)
/*  522 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  523 */       .define(Character.valueOf('X'), Items.BOOK)
/*  524 */       .pattern("###")
/*  525 */       .pattern("XXX")
/*  526 */       .pattern("###")
/*  527 */       .unlockedBy("has_book", has(Items.BOOK))
/*  528 */       .save(this.output);
/*      */     
/*  530 */     shaped(RecipeCategory.COMBAT, Items.BOW)
/*  531 */       .define(Character.valueOf('#'), Items.STICK)
/*  532 */       .define(Character.valueOf('X'), Items.STRING)
/*  533 */       .pattern(" #X")
/*  534 */       .pattern("# X")
/*  535 */       .pattern(" #X")
/*  536 */       .unlockedBy("has_string", has(Items.STRING))
/*  537 */       .save(this.output);
/*      */     
/*  539 */     shaped(RecipeCategory.MISC, Items.BOWL, 4)
/*  540 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  541 */       .pattern("# #")
/*  542 */       .pattern(" # ")
/*  543 */       .unlockedBy("has_brown_mushroom", has(Blocks.BROWN_MUSHROOM))
/*  544 */       .unlockedBy("has_red_mushroom", has(Blocks.RED_MUSHROOM))
/*  545 */       .unlockedBy("has_mushroom_stew", has(Items.MUSHROOM_STEW))
/*  546 */       .save(this.output);
/*      */     
/*  548 */     shaped(RecipeCategory.FOOD, Items.BREAD)
/*  549 */       .define(Character.valueOf('#'), Items.WHEAT)
/*  550 */       .pattern("###")
/*  551 */       .unlockedBy("has_wheat", has(Items.WHEAT))
/*  552 */       .save(this.output);
/*      */     
/*  554 */     shaped(RecipeCategory.BREWING, Blocks.BREWING_STAND)
/*  555 */       .define(Character.valueOf('B'), Items.BLAZE_ROD)
/*  556 */       .define(Character.valueOf('#'), ItemTags.STONE_CRAFTING_MATERIALS)
/*  557 */       .pattern(" B ")
/*  558 */       .pattern("###")
/*  559 */       .unlockedBy("has_blaze_rod", has(Items.BLAZE_ROD))
/*  560 */       .save(this.output);
/*      */     
/*  562 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.BRICKS)
/*  563 */       .define(Character.valueOf('#'), Items.BRICK)
/*  564 */       .pattern("##")
/*  565 */       .pattern("##")
/*  566 */       .unlockedBy("has_brick", has(Items.BRICK))
/*  567 */       .save(this.output);
/*      */     
/*  569 */     shapeless(RecipeCategory.MISC, Items.BROWN_DYE)
/*  570 */       .requires(Items.COCOA_BEANS)
/*  571 */       .group("brown_dye")
/*  572 */       .unlockedBy("has_cocoa_beans", has(Items.COCOA_BEANS))
/*  573 */       .save(this.output);
/*      */     
/*  575 */     shaped(RecipeCategory.MISC, Items.BUCKET)
/*  576 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/*  577 */       .pattern("# #")
/*  578 */       .pattern(" # ")
/*  579 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/*  580 */       .save(this.output);
/*      */     
/*  582 */     shaped(RecipeCategory.FOOD, Blocks.CAKE)
/*  583 */       .define(Character.valueOf('A'), Items.MILK_BUCKET)
/*  584 */       .define(Character.valueOf('B'), Items.SUGAR)
/*  585 */       .define(Character.valueOf('C'), Items.WHEAT)
/*  586 */       .define(Character.valueOf('E'), ItemTags.EGGS)
/*  587 */       .pattern("AAA")
/*  588 */       .pattern("BEB")
/*  589 */       .pattern("CCC")
/*  590 */       .unlockedBy("has_egg", has(ItemTags.EGGS))
/*  591 */       .save(this.output);
/*      */     
/*  593 */     shaped(RecipeCategory.DECORATIONS, Blocks.CAMPFIRE)
/*  594 */       .define(Character.valueOf('L'), ItemTags.LOGS)
/*  595 */       .define(Character.valueOf('S'), Items.STICK)
/*  596 */       .define(Character.valueOf('C'), ItemTags.COALS)
/*  597 */       .pattern(" S ")
/*  598 */       .pattern("SCS")
/*  599 */       .pattern("LLL")
/*  600 */       .unlockedBy("has_stick", has(Items.STICK))
/*  601 */       .unlockedBy("has_coal", has(ItemTags.COALS))
/*  602 */       .save(this.output);
/*      */     
/*  604 */     shaped(RecipeCategory.TRANSPORTATION, Items.CARROT_ON_A_STICK)
/*  605 */       .define(Character.valueOf('#'), Items.FISHING_ROD)
/*  606 */       .define(Character.valueOf('X'), Items.CARROT)
/*  607 */       .pattern("# ")
/*  608 */       .pattern(" X")
/*  609 */       .unlockedBy("has_carrot", has(Items.CARROT))
/*  610 */       .save(this.output);
/*      */     
/*  612 */     shaped(RecipeCategory.TRANSPORTATION, Items.WARPED_FUNGUS_ON_A_STICK)
/*  613 */       .define(Character.valueOf('#'), Items.FISHING_ROD)
/*  614 */       .define(Character.valueOf('X'), Items.WARPED_FUNGUS)
/*  615 */       .pattern("# ")
/*  616 */       .pattern(" X")
/*  617 */       .unlockedBy("has_warped_fungus", has(Items.WARPED_FUNGUS))
/*  618 */       .save(this.output);
/*      */     
/*  620 */     shaped(RecipeCategory.BREWING, Blocks.CAULDRON)
/*  621 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/*  622 */       .pattern("# #")
/*  623 */       .pattern("# #")
/*  624 */       .pattern("###")
/*  625 */       .unlockedBy("has_water_bucket", has(Items.WATER_BUCKET))
/*  626 */       .save(this.output);
/*      */     
/*  628 */     shaped(RecipeCategory.DECORATIONS, Blocks.COMPOSTER)
/*  629 */       .define(Character.valueOf('#'), ItemTags.WOODEN_SLABS)
/*  630 */       .pattern("# #")
/*  631 */       .pattern("# #")
/*  632 */       .pattern("###")
/*  633 */       .unlockedBy("has_wood_slab", has(ItemTags.WOODEN_SLABS))
/*  634 */       .save(this.output);
/*      */     
/*  636 */     shaped(RecipeCategory.DECORATIONS, Blocks.CHEST)
/*  637 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  638 */       .pattern("###")
/*  639 */       .pattern("# #")
/*  640 */       .pattern("###")
/*  641 */       .unlockedBy("has_lots_of_items", CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new InventoryChangeTrigger.TriggerInstance(Optional.empty(), new InventoryChangeTrigger.TriggerInstance.Slots(MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY), List.of())))
/*  642 */       .save(this.output);
/*      */     
/*  644 */     shaped(RecipeCategory.DECORATIONS, Blocks.COPPER_CHEST)
/*  645 */       .define(Character.valueOf('#'), Items.COPPER_INGOT)
/*  646 */       .define(Character.valueOf('X'), Items.CHEST)
/*  647 */       .pattern("###")
/*  648 */       .pattern("#X#")
/*  649 */       .pattern("###")
/*  650 */       .unlockedBy("has_copper_chest", has(Items.COPPER_CHEST))
/*  651 */       .save(this.output);
/*      */     
/*  653 */     shapeless(RecipeCategory.TRANSPORTATION, Items.CHEST_MINECART)
/*  654 */       .requires(Blocks.CHEST)
/*  655 */       .requires(Items.MINECART)
/*  656 */       .unlockedBy("has_minecart", has(Items.MINECART))
/*  657 */       .save(this.output);
/*      */     
/*  659 */     chestBoat(Items.ACACIA_CHEST_BOAT, Items.ACACIA_BOAT);
/*  660 */     chestBoat(Items.BIRCH_CHEST_BOAT, Items.BIRCH_BOAT);
/*  661 */     chestBoat(Items.DARK_OAK_CHEST_BOAT, Items.DARK_OAK_BOAT);
/*  662 */     chestBoat(Items.PALE_OAK_CHEST_BOAT, Items.PALE_OAK_BOAT);
/*  663 */     chestBoat(Items.JUNGLE_CHEST_BOAT, Items.JUNGLE_BOAT);
/*  664 */     chestBoat(Items.OAK_CHEST_BOAT, Items.OAK_BOAT);
/*  665 */     chestBoat(Items.SPRUCE_CHEST_BOAT, Items.SPRUCE_BOAT);
/*  666 */     chestBoat(Items.MANGROVE_CHEST_BOAT, Items.MANGROVE_BOAT);
/*      */     
/*  668 */     chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_QUARTZ_BLOCK, Ingredient.of(Blocks.QUARTZ_SLAB))
/*  669 */       .unlockedBy("has_chiseled_quartz_block", has(Blocks.CHISELED_QUARTZ_BLOCK))
/*  670 */       .unlockedBy("has_quartz_block", has(Blocks.QUARTZ_BLOCK))
/*  671 */       .unlockedBy("has_quartz_pillar", has(Blocks.QUARTZ_PILLAR))
/*  672 */       .save(this.output);
/*      */     
/*  674 */     chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_STONE_BRICKS, Ingredient.of(Blocks.STONE_BRICK_SLAB))
/*  675 */       .unlockedBy("has_tag", has(ItemTags.STONE_BRICKS))
/*  676 */       .save(this.output);
/*      */     
/*  678 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.CLAY, Items.CLAY_BALL);
/*      */     
/*  680 */     shaped(RecipeCategory.TOOLS, Items.CLOCK)
/*  681 */       .define(Character.valueOf('#'), Items.GOLD_INGOT)
/*  682 */       .define(Character.valueOf('X'), Items.REDSTONE)
/*  683 */       .pattern(" # ")
/*  684 */       .pattern("#X#")
/*  685 */       .pattern(" # ")
/*  686 */       .unlockedBy("has_redstone", has(Items.REDSTONE))
/*  687 */       .save(this.output);
/*      */     
/*  689 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.COAL, RecipeCategory.BUILDING_BLOCKS, Items.COAL_BLOCK);
/*      */     
/*  691 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.COARSE_DIRT, 4)
/*  692 */       .define(Character.valueOf('D'), Blocks.DIRT)
/*  693 */       .define(Character.valueOf('G'), Blocks.GRAVEL)
/*  694 */       .pattern("DG")
/*  695 */       .pattern("GD")
/*  696 */       .unlockedBy("has_gravel", has(Blocks.GRAVEL))
/*  697 */       .save(this.output);
/*      */     
/*  699 */     shaped(RecipeCategory.REDSTONE, Blocks.COMPARATOR)
/*  700 */       .define(Character.valueOf('#'), Blocks.REDSTONE_TORCH)
/*  701 */       .define(Character.valueOf('X'), Items.QUARTZ)
/*  702 */       .define(Character.valueOf('I'), Blocks.STONE)
/*  703 */       .pattern(" # ")
/*  704 */       .pattern("#X#")
/*  705 */       .pattern("III")
/*  706 */       .unlockedBy("has_quartz", has(Items.QUARTZ))
/*  707 */       .save(this.output);
/*      */     
/*  709 */     shaped(RecipeCategory.TOOLS, Items.COMPASS)
/*  710 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/*  711 */       .define(Character.valueOf('X'), Items.REDSTONE)
/*  712 */       .pattern(" # ")
/*  713 */       .pattern("#X#")
/*  714 */       .pattern(" # ")
/*  715 */       .unlockedBy("has_redstone", has(Items.REDSTONE))
/*  716 */       .save(this.output);
/*      */     
/*  718 */     shaped(RecipeCategory.FOOD, Items.COOKIE, 8)
/*  719 */       .define(Character.valueOf('#'), Items.WHEAT)
/*  720 */       .define(Character.valueOf('X'), Items.COCOA_BEANS)
/*  721 */       .pattern("#X#")
/*  722 */       .unlockedBy("has_cocoa", has(Items.COCOA_BEANS))
/*  723 */       .save(this.output);
/*      */     
/*  725 */     shaped(RecipeCategory.DECORATIONS, Blocks.CRAFTING_TABLE)
/*  726 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  727 */       .pattern("##")
/*  728 */       .pattern("##")
/*  729 */       .unlockedBy("unlock_right_away", PlayerTrigger.TriggerInstance.tick())
/*  730 */       .showNotification(false)
/*  731 */       .save(this.output);
/*      */     
/*  733 */     shaped(RecipeCategory.COMBAT, Items.CROSSBOW)
/*  734 */       .define(Character.valueOf('~'), Items.STRING)
/*  735 */       .define(Character.valueOf('#'), Items.STICK)
/*  736 */       .define(Character.valueOf('&'), Items.IRON_INGOT)
/*  737 */       .define(Character.valueOf('$'), Blocks.TRIPWIRE_HOOK)
/*  738 */       .pattern("#&#")
/*  739 */       .pattern("~$~")
/*  740 */       .pattern(" # ")
/*  741 */       .unlockedBy("has_string", has(Items.STRING))
/*  742 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/*  743 */       .unlockedBy("has_tripwire_hook", has(Blocks.TRIPWIRE_HOOK))
/*  744 */       .save(this.output);
/*      */     
/*  746 */     shaped(RecipeCategory.DECORATIONS, Blocks.LOOM)
/*  747 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/*  748 */       .define(Character.valueOf('@'), Items.STRING)
/*  749 */       .pattern("@@")
/*  750 */       .pattern("##")
/*  751 */       .unlockedBy("has_string", has(Items.STRING))
/*  752 */       .save(this.output);
/*      */     
/*  754 */     chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_RED_SANDSTONE, Ingredient.of(Blocks.RED_SANDSTONE_SLAB))
/*  755 */       .unlockedBy("has_red_sandstone", has(Blocks.RED_SANDSTONE))
/*  756 */       .unlockedBy("has_chiseled_red_sandstone", has(Blocks.CHISELED_RED_SANDSTONE))
/*  757 */       .unlockedBy("has_cut_red_sandstone", has(Blocks.CUT_RED_SANDSTONE))
/*  758 */       .save(this.output);
/*      */     
/*  760 */     chiseled(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_SANDSTONE, Blocks.SANDSTONE_SLAB);
/*      */     
/*  762 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, Items.COPPER_INGOT, RecipeCategory.BUILDING_BLOCKS, Items.COPPER_BLOCK, getSimpleRecipeName(Items.COPPER_INGOT), getItemName(Items.COPPER_INGOT));
/*      */     
/*  764 */     shapeless(RecipeCategory.MISC, Items.COPPER_INGOT, 9)
/*  765 */       .requires(Blocks.WAXED_COPPER_BLOCK)
/*  766 */       .group(getItemName(Items.COPPER_INGOT))
/*  767 */       .unlockedBy(getHasName(Blocks.WAXED_COPPER_BLOCK), has(Blocks.WAXED_COPPER_BLOCK))
/*  768 */       .save(this.output, getConversionRecipeName(Items.COPPER_INGOT, Blocks.WAXED_COPPER_BLOCK));
/*      */     
/*  770 */     waxRecipes(FeatureFlagSet.of(FeatureFlags.VANILLA));
/*      */     
/*  772 */     shapeless(RecipeCategory.MISC, Items.CYAN_DYE, 2)
/*  773 */       .requires(Items.BLUE_DYE)
/*  774 */       .requires(Items.GREEN_DYE)
/*  775 */       .group("cyan_dye")
/*  776 */       .unlockedBy("has_green_dye", has(Items.GREEN_DYE))
/*  777 */       .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
/*  778 */       .save(this.output);
/*      */     
/*  780 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.DARK_PRISMARINE)
/*  781 */       .define(Character.valueOf('S'), Items.PRISMARINE_SHARD)
/*  782 */       .define(Character.valueOf('I'), Items.BLACK_DYE)
/*  783 */       .pattern("SSS")
/*  784 */       .pattern("SIS")
/*  785 */       .pattern("SSS")
/*  786 */       .unlockedBy("has_prismarine_shard", has(Items.PRISMARINE_SHARD))
/*  787 */       .save(this.output);
/*      */     
/*  789 */     shaped(RecipeCategory.REDSTONE, Blocks.DAYLIGHT_DETECTOR)
/*  790 */       .define(Character.valueOf('Q'), Items.QUARTZ)
/*  791 */       .define(Character.valueOf('G'), Blocks.GLASS)
/*  792 */       .define(Character.valueOf('W'), ItemTags.WOODEN_SLABS)
/*  793 */       .pattern("GGG")
/*  794 */       .pattern("QQQ")
/*  795 */       .pattern("WWW")
/*  796 */       .unlockedBy("has_quartz", has(Items.QUARTZ))
/*  797 */       .save(this.output);
/*      */     
/*  799 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICKS, 4)
/*  800 */       .define(Character.valueOf('S'), Blocks.POLISHED_DEEPSLATE)
/*  801 */       .pattern("SS")
/*  802 */       .pattern("SS")
/*  803 */       .unlockedBy("has_polished_deepslate", has(Blocks.POLISHED_DEEPSLATE))
/*  804 */       .save(this.output);
/*      */     
/*  806 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, 4)
/*  807 */       .define(Character.valueOf('S'), Blocks.DEEPSLATE_BRICKS)
/*  808 */       .pattern("SS")
/*  809 */       .pattern("SS")
/*  810 */       .unlockedBy("has_deepslate_bricks", has(Blocks.DEEPSLATE_BRICKS))
/*  811 */       .save(this.output);
/*      */     
/*  813 */     shaped(RecipeCategory.TRANSPORTATION, Blocks.DETECTOR_RAIL, 6)
/*  814 */       .define(Character.valueOf('R'), Items.REDSTONE)
/*  815 */       .define(Character.valueOf('#'), Blocks.STONE_PRESSURE_PLATE)
/*  816 */       .define(Character.valueOf('X'), Items.IRON_INGOT)
/*  817 */       .pattern("X X")
/*  818 */       .pattern("X#X")
/*  819 */       .pattern("XRX")
/*  820 */       .unlockedBy("has_rail", has(Blocks.RAIL))
/*  821 */       .save(this.output);
/*      */     
/*  823 */     shaped(RecipeCategory.TOOLS, Items.DIAMOND_AXE)
/*  824 */       .define(Character.valueOf('#'), Items.STICK)
/*  825 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  826 */       .pattern("XX")
/*  827 */       .pattern("X#")
/*  828 */       .pattern(" #")
/*  829 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  830 */       .save(this.output);
/*      */     
/*  832 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.DIAMOND, RecipeCategory.BUILDING_BLOCKS, Items.DIAMOND_BLOCK);
/*      */     
/*  834 */     shaped(RecipeCategory.COMBAT, Items.DIAMOND_BOOTS)
/*  835 */       .define(Character.valueOf('X'), Items.DIAMOND)
/*  836 */       .pattern("X X")
/*  837 */       .pattern("X X")
/*  838 */       .unlockedBy("has_diamond", has(Items.DIAMOND))
/*  839 */       .save(this.output);
/*      */     
/*  841 */     shaped(RecipeCategory.COMBAT, Items.DIAMOND_CHESTPLATE)
/*  842 */       .define(Character.valueOf('X'), Items.DIAMOND)
/*  843 */       .pattern("X X")
/*  844 */       .pattern("XXX")
/*  845 */       .pattern("XXX")
/*  846 */       .unlockedBy("has_diamond", has(Items.DIAMOND))
/*  847 */       .save(this.output);
/*      */     
/*  849 */     shaped(RecipeCategory.COMBAT, Items.DIAMOND_HELMET)
/*  850 */       .define(Character.valueOf('X'), Items.DIAMOND)
/*  851 */       .pattern("XXX")
/*  852 */       .pattern("X X")
/*  853 */       .unlockedBy("has_diamond", has(Items.DIAMOND))
/*  854 */       .save(this.output);
/*      */     
/*  856 */     shaped(RecipeCategory.TOOLS, Items.DIAMOND_HOE)
/*  857 */       .define(Character.valueOf('#'), Items.STICK)
/*  858 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  859 */       .pattern("XX")
/*  860 */       .pattern(" #")
/*  861 */       .pattern(" #")
/*  862 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  863 */       .save(this.output);
/*      */     
/*  865 */     shaped(RecipeCategory.COMBAT, Items.DIAMOND_LEGGINGS)
/*  866 */       .define(Character.valueOf('X'), Items.DIAMOND)
/*  867 */       .pattern("XXX")
/*  868 */       .pattern("X X")
/*  869 */       .pattern("X X")
/*  870 */       .unlockedBy("has_diamond", has(Items.DIAMOND))
/*  871 */       .save(this.output);
/*      */     
/*  873 */     shaped(RecipeCategory.TOOLS, Items.DIAMOND_PICKAXE)
/*  874 */       .define(Character.valueOf('#'), Items.STICK)
/*  875 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  876 */       .pattern("XXX")
/*  877 */       .pattern(" # ")
/*  878 */       .pattern(" # ")
/*  879 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  880 */       .save(this.output);
/*      */     
/*  882 */     shaped(RecipeCategory.TOOLS, Items.DIAMOND_SHOVEL)
/*  883 */       .define(Character.valueOf('#'), Items.STICK)
/*  884 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  885 */       .pattern("X")
/*  886 */       .pattern("#")
/*  887 */       .pattern("#")
/*  888 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  889 */       .save(this.output);
/*      */     
/*  891 */     shaped(RecipeCategory.COMBAT, Items.DIAMOND_SWORD)
/*  892 */       .define(Character.valueOf('#'), Items.STICK)
/*  893 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  894 */       .pattern("X")
/*  895 */       .pattern("X")
/*  896 */       .pattern("#")
/*  897 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  898 */       .save(this.output);
/*      */     
/*  900 */     shaped(RecipeCategory.COMBAT, Items.DIAMOND_SPEAR)
/*  901 */       .define(Character.valueOf('#'), Items.STICK)
/*  902 */       .define(Character.valueOf('X'), ItemTags.DIAMOND_TOOL_MATERIALS)
/*  903 */       .pattern("  X")
/*  904 */       .pattern(" # ")
/*  905 */       .pattern("#  ")
/*  906 */       .unlockedBy("has_diamond", has(ItemTags.DIAMOND_TOOL_MATERIALS))
/*  907 */       .save(this.output);
/*      */     
/*  909 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.DIORITE, 2)
/*  910 */       .define(Character.valueOf('Q'), Items.QUARTZ)
/*  911 */       .define(Character.valueOf('C'), Blocks.COBBLESTONE)
/*  912 */       .pattern("CQ")
/*  913 */       .pattern("QC")
/*  914 */       .unlockedBy("has_quartz", has(Items.QUARTZ))
/*  915 */       .save(this.output);
/*      */     
/*  917 */     shaped(RecipeCategory.REDSTONE, Blocks.DISPENSER)
/*  918 */       .define(Character.valueOf('R'), Items.REDSTONE)
/*  919 */       .define(Character.valueOf('#'), Blocks.COBBLESTONE)
/*  920 */       .define(Character.valueOf('X'), Items.BOW)
/*  921 */       .pattern("###")
/*  922 */       .pattern("#X#")
/*  923 */       .pattern("#R#")
/*  924 */       .unlockedBy("has_bow", has(Items.BOW))
/*  925 */       .save(this.output);
/*      */     
/*  927 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.DRIPSTONE_BLOCK, Items.POINTED_DRIPSTONE);
/*      */     
/*  929 */     shaped(RecipeCategory.REDSTONE, Blocks.DROPPER)
/*  930 */       .define(Character.valueOf('R'), Items.REDSTONE)
/*  931 */       .define(Character.valueOf('#'), Blocks.COBBLESTONE)
/*  932 */       .pattern("###")
/*  933 */       .pattern("# #")
/*  934 */       .pattern("#R#")
/*  935 */       .unlockedBy("has_redstone", has(Items.REDSTONE))
/*  936 */       .save(this.output);
/*      */     
/*  938 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.EMERALD, RecipeCategory.BUILDING_BLOCKS, Items.EMERALD_BLOCK);
/*      */     
/*  940 */     shaped(RecipeCategory.DECORATIONS, Blocks.ENCHANTING_TABLE)
/*  941 */       .define(Character.valueOf('B'), Items.BOOK)
/*  942 */       .define(Character.valueOf('#'), Blocks.OBSIDIAN)
/*  943 */       .define(Character.valueOf('D'), Items.DIAMOND)
/*  944 */       .pattern(" B ")
/*  945 */       .pattern("D#D")
/*  946 */       .pattern("###")
/*  947 */       .unlockedBy("has_obsidian", has(Blocks.OBSIDIAN))
/*  948 */       .save(this.output);
/*      */     
/*  950 */     shaped(RecipeCategory.DECORATIONS, Blocks.ENDER_CHEST)
/*  951 */       .define(Character.valueOf('#'), Blocks.OBSIDIAN)
/*  952 */       .define(Character.valueOf('E'), Items.ENDER_EYE)
/*  953 */       .pattern("###")
/*  954 */       .pattern("#E#")
/*  955 */       .pattern("###")
/*  956 */       .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
/*  957 */       .save(this.output);
/*      */     
/*  959 */     shapeless(RecipeCategory.MISC, Items.ENDER_EYE)
/*  960 */       .requires(Items.ENDER_PEARL)
/*  961 */       .requires(Items.BLAZE_POWDER)
/*  962 */       .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
/*  963 */       .save(this.output);
/*      */     
/*  965 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE_BRICKS, 4)
/*  966 */       .define(Character.valueOf('#'), Blocks.END_STONE)
/*  967 */       .pattern("##")
/*  968 */       .pattern("##")
/*  969 */       .unlockedBy("has_end_stone", has(Blocks.END_STONE))
/*  970 */       .save(this.output);
/*      */     
/*  972 */     shaped(RecipeCategory.DECORATIONS, Items.END_CRYSTAL)
/*  973 */       .define(Character.valueOf('T'), Items.GHAST_TEAR)
/*  974 */       .define(Character.valueOf('E'), Items.ENDER_EYE)
/*  975 */       .define(Character.valueOf('G'), Blocks.GLASS)
/*  976 */       .pattern("GGG")
/*  977 */       .pattern("GEG")
/*  978 */       .pattern("GTG")
/*  979 */       .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))
/*  980 */       .save(this.output);
/*      */     
/*  982 */     shaped(RecipeCategory.DECORATIONS, Blocks.END_ROD, 4)
/*  983 */       .define(Character.valueOf('#'), Items.POPPED_CHORUS_FRUIT)
/*  984 */       .define(Character.valueOf('/'), Items.BLAZE_ROD)
/*  985 */       .pattern("/")
/*  986 */       .pattern("#")
/*  987 */       .unlockedBy("has_chorus_fruit_popped", has(Items.POPPED_CHORUS_FRUIT))
/*  988 */       .save(this.output);
/*      */     
/*  990 */     shapeless(RecipeCategory.BREWING, Items.FERMENTED_SPIDER_EYE)
/*  991 */       .requires(Items.SPIDER_EYE)
/*  992 */       .requires(Blocks.BROWN_MUSHROOM)
/*  993 */       .requires(Items.SUGAR)
/*  994 */       .unlockedBy("has_spider_eye", has(Items.SPIDER_EYE))
/*  995 */       .save(this.output);
/*      */     
/*  997 */     shapeless(RecipeCategory.MISC, Items.FIRE_CHARGE, 3)
/*  998 */       .requires(Items.GUNPOWDER)
/*  999 */       .requires(Items.BLAZE_POWDER)
/* 1000 */       .requires(Ingredient.of(new ItemLike[] { Items.COAL, Items.CHARCOAL
/* 1001 */           })).unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
/* 1002 */       .save(this.output);
/*      */     
/* 1004 */     shapeless(RecipeCategory.MISC, Items.FIREWORK_ROCKET, 3)
/* 1005 */       .requires(Items.GUNPOWDER)
/* 1006 */       .requires(Items.PAPER)
/* 1007 */       .unlockedBy("has_gunpowder", has(Items.GUNPOWDER))
/* 1008 */       .save(this.output, "firework_rocket_simple");
/*      */     
/* 1010 */     shaped(RecipeCategory.TOOLS, Items.FISHING_ROD)
/* 1011 */       .define(Character.valueOf('#'), Items.STICK)
/* 1012 */       .define(Character.valueOf('X'), Items.STRING)
/* 1013 */       .pattern("  #")
/* 1014 */       .pattern(" #X")
/* 1015 */       .pattern("# X")
/* 1016 */       .unlockedBy("has_string", has(Items.STRING))
/* 1017 */       .save(this.output);
/*      */     
/* 1019 */     shapeless(RecipeCategory.TOOLS, Items.FLINT_AND_STEEL)
/* 1020 */       .requires(Items.IRON_INGOT)
/* 1021 */       .requires(Items.FLINT)
/* 1022 */       .unlockedBy("has_flint", has(Items.FLINT))
/* 1023 */       .unlockedBy("has_obsidian", has(Blocks.OBSIDIAN))
/* 1024 */       .save(this.output);
/*      */     
/* 1026 */     shaped(RecipeCategory.DECORATIONS, Blocks.FLOWER_POT)
/* 1027 */       .define(Character.valueOf('#'), Items.BRICK)
/* 1028 */       .pattern("# #")
/* 1029 */       .pattern(" # ")
/* 1030 */       .unlockedBy("has_brick", has(Items.BRICK))
/* 1031 */       .save(this.output);
/*      */     
/* 1033 */     shaped(RecipeCategory.DECORATIONS, Blocks.FURNACE)
/* 1034 */       .define(Character.valueOf('#'), ItemTags.STONE_CRAFTING_MATERIALS)
/* 1035 */       .pattern("###")
/* 1036 */       .pattern("# #")
/* 1037 */       .pattern("###")
/* 1038 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_CRAFTING_MATERIALS))
/* 1039 */       .save(this.output);
/*      */     
/* 1041 */     shapeless(RecipeCategory.TRANSPORTATION, Items.FURNACE_MINECART)
/* 1042 */       .requires(Blocks.FURNACE)
/* 1043 */       .requires(Items.MINECART)
/* 1044 */       .unlockedBy("has_minecart", has(Items.MINECART))
/* 1045 */       .save(this.output);
/*      */     
/* 1047 */     shaped(RecipeCategory.BREWING, Items.GLASS_BOTTLE, 3)
/* 1048 */       .define(Character.valueOf('#'), Blocks.GLASS)
/* 1049 */       .pattern("# #")
/* 1050 */       .pattern(" # ")
/* 1051 */       .unlockedBy("has_glass", has(Blocks.GLASS))
/* 1052 */       .save(this.output);
/*      */     
/* 1054 */     shaped(RecipeCategory.DECORATIONS, Blocks.GLASS_PANE, 16)
/* 1055 */       .define(Character.valueOf('#'), Blocks.GLASS)
/* 1056 */       .pattern("###")
/* 1057 */       .pattern("###")
/* 1058 */       .unlockedBy("has_glass", has(Blocks.GLASS))
/* 1059 */       .save(this.output);
/*      */     
/* 1061 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.GLOWSTONE, Items.GLOWSTONE_DUST);
/*      */     
/* 1063 */     shapeless(RecipeCategory.DECORATIONS, Items.GLOW_ITEM_FRAME)
/* 1064 */       .requires(Items.ITEM_FRAME)
/* 1065 */       .requires(Items.GLOW_INK_SAC)
/* 1066 */       .unlockedBy("has_item_frame", has(Items.ITEM_FRAME))
/* 1067 */       .unlockedBy("has_glow_ink_sac", has(Items.GLOW_INK_SAC))
/* 1068 */       .save(this.output);
/*      */     
/* 1070 */     shaped(RecipeCategory.FOOD, Items.GOLDEN_APPLE)
/* 1071 */       .define(Character.valueOf('#'), Items.GOLD_INGOT)
/* 1072 */       .define(Character.valueOf('X'), Items.APPLE)
/* 1073 */       .pattern("###")
/* 1074 */       .pattern("#X#")
/* 1075 */       .pattern("###")
/* 1076 */       .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
/* 1077 */       .save(this.output);
/*      */     
/* 1079 */     shaped(RecipeCategory.TOOLS, Items.GOLDEN_AXE)
/* 1080 */       .define(Character.valueOf('#'), Items.STICK)
/* 1081 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/* 1082 */       .pattern("XX")
/* 1083 */       .pattern("X#")
/* 1084 */       .pattern(" #")
/* 1085 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/* 1086 */       .save(this.output);
/*      */     
/* 1088 */     shaped(RecipeCategory.COMBAT, Items.GOLDEN_BOOTS)
/* 1089 */       .define(Character.valueOf('X'), Items.GOLD_INGOT)
/* 1090 */       .pattern("X X")
/* 1091 */       .pattern("X X")
/* 1092 */       .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
/* 1093 */       .save(this.output);
/*      */     
/* 1095 */     shaped(RecipeCategory.BREWING, Items.GOLDEN_CARROT)
/* 1096 */       .define(Character.valueOf('#'), Items.GOLD_NUGGET)
/* 1097 */       .define(Character.valueOf('X'), Items.CARROT)
/* 1098 */       .pattern("###")
/* 1099 */       .pattern("#X#")
/* 1100 */       .pattern("###")
/* 1101 */       .unlockedBy("has_gold_nugget", has(Items.GOLD_NUGGET))
/* 1102 */       .save(this.output);
/*      */     
/* 1104 */     shaped(RecipeCategory.COMBAT, Items.GOLDEN_CHESTPLATE)
/* 1105 */       .define(Character.valueOf('X'), Items.GOLD_INGOT)
/* 1106 */       .pattern("X X")
/* 1107 */       .pattern("XXX")
/* 1108 */       .pattern("XXX")
/* 1109 */       .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
/* 1110 */       .save(this.output);
/*      */     
/* 1112 */     shaped(RecipeCategory.COMBAT, Items.GOLDEN_HELMET)
/* 1113 */       .define(Character.valueOf('X'), Items.GOLD_INGOT)
/* 1114 */       .pattern("XXX")
/* 1115 */       .pattern("X X")
/* 1116 */       .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
/* 1117 */       .save(this.output);
/*      */     
/* 1119 */     shaped(RecipeCategory.TOOLS, Items.GOLDEN_HOE)
/* 1120 */       .define(Character.valueOf('#'), Items.STICK)
/* 1121 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/* 1122 */       .pattern("XX")
/* 1123 */       .pattern(" #")
/* 1124 */       .pattern(" #")
/* 1125 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/* 1126 */       .save(this.output);
/*      */     
/* 1128 */     shaped(RecipeCategory.COMBAT, Items.GOLDEN_LEGGINGS)
/* 1129 */       .define(Character.valueOf('X'), Items.GOLD_INGOT)
/* 1130 */       .pattern("XXX")
/* 1131 */       .pattern("X X")
/* 1132 */       .pattern("X X")
/* 1133 */       .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
/* 1134 */       .save(this.output);
/*      */     
/* 1136 */     shaped(RecipeCategory.TOOLS, Items.GOLDEN_PICKAXE)
/* 1137 */       .define(Character.valueOf('#'), Items.STICK)
/* 1138 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/* 1139 */       .pattern("XXX")
/* 1140 */       .pattern(" # ")
/* 1141 */       .pattern(" # ")
/* 1142 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/* 1143 */       .save(this.output);
/*      */     
/* 1145 */     shaped(RecipeCategory.TRANSPORTATION, Blocks.POWERED_RAIL, 6)
/* 1146 */       .define(Character.valueOf('R'), Items.REDSTONE)
/* 1147 */       .define(Character.valueOf('#'), Items.STICK)
/* 1148 */       .define(Character.valueOf('X'), Items.GOLD_INGOT)
/* 1149 */       .pattern("X X")
/* 1150 */       .pattern("X#X")
/* 1151 */       .pattern("XRX")
/* 1152 */       .unlockedBy("has_rail", has(Blocks.RAIL))
/* 1153 */       .save(this.output);
/*      */     
/* 1155 */     shaped(RecipeCategory.TOOLS, Items.GOLDEN_SHOVEL)
/* 1156 */       .define(Character.valueOf('#'), Items.STICK)
/* 1157 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/* 1158 */       .pattern("X")
/* 1159 */       .pattern("#")
/* 1160 */       .pattern("#")
/* 1161 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/* 1162 */       .save(this.output);
/*      */     
/* 1164 */     shaped(RecipeCategory.COMBAT, Items.GOLDEN_SWORD)
/* 1165 */       .define(Character.valueOf('#'), Items.STICK)
/* 1166 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/* 1167 */       .pattern("X")
/* 1168 */       .pattern("X")
/* 1169 */       .pattern("#")
/* 1170 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/* 1171 */       .save(this.output);
/*      */     
/* 1173 */     shaped(RecipeCategory.COMBAT, Items.GOLDEN_SPEAR)
/* 1174 */       .define(Character.valueOf('#'), Items.STICK)
/* 1175 */       .define(Character.valueOf('X'), ItemTags.GOLD_TOOL_MATERIALS)
/* 1176 */       .pattern("  X")
/* 1177 */       .pattern(" # ")
/* 1178 */       .pattern("#  ")
/* 1179 */       .unlockedBy("has_gold_ingot", has(ItemTags.GOLD_TOOL_MATERIALS))
/* 1180 */       .save(this.output);
/*      */     
/* 1182 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, Items.GOLD_INGOT, RecipeCategory.BUILDING_BLOCKS, Items.GOLD_BLOCK, "gold_ingot_from_gold_block", "gold_ingot");
/* 1183 */     nineBlockStorageRecipesWithCustomPacking(RecipeCategory.MISC, Items.GOLD_NUGGET, RecipeCategory.MISC, Items.GOLD_INGOT, "gold_ingot_from_nuggets", "gold_ingot");
/*      */     
/* 1185 */     shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.GRANITE)
/* 1186 */       .requires(Blocks.DIORITE)
/* 1187 */       .requires(Items.QUARTZ)
/* 1188 */       .unlockedBy("has_quartz", has(Items.QUARTZ))
/* 1189 */       .save(this.output);
/*      */     
/* 1191 */     shapeless(RecipeCategory.MISC, Items.GRAY_DYE, 2)
/* 1192 */       .requires(Items.BLACK_DYE)
/* 1193 */       .requires(Items.WHITE_DYE)
/* 1194 */       .group("gray_dye")
/* 1195 */       .unlockedBy("has_white_dye", has(Items.WHITE_DYE))
/* 1196 */       .unlockedBy("has_black_dye", has(Items.BLACK_DYE))
/* 1197 */       .save(this.output);
/*      */     
/* 1199 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, Blocks.HAY_BLOCK, Items.WHEAT);
/*      */     
/* 1201 */     pressurePlate(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Items.IRON_INGOT);
/*      */     
/* 1203 */     shapeless(RecipeCategory.FOOD, Items.HONEY_BOTTLE, 4)
/* 1204 */       .requires(Items.HONEY_BLOCK)
/* 1205 */       .requires(Items.GLASS_BOTTLE, 4)
/* 1206 */       .unlockedBy("has_honey_block", has(Blocks.HONEY_BLOCK))
/* 1207 */       .save(this.output);
/*      */     
/* 1209 */     twoByTwoPacker(RecipeCategory.REDSTONE, Blocks.HONEY_BLOCK, Items.HONEY_BOTTLE);
/*      */     
/* 1211 */     twoByTwoPacker(RecipeCategory.DECORATIONS, Blocks.HONEYCOMB_BLOCK, Items.HONEYCOMB);
/*      */     
/* 1213 */     shaped(RecipeCategory.REDSTONE, Blocks.HOPPER)
/* 1214 */       .define(Character.valueOf('C'), Blocks.CHEST)
/* 1215 */       .define(Character.valueOf('I'), Items.IRON_INGOT)
/* 1216 */       .pattern("I I")
/* 1217 */       .pattern("ICI")
/* 1218 */       .pattern(" I ")
/* 1219 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1220 */       .save(this.output);
/*      */     
/* 1222 */     shapeless(RecipeCategory.TRANSPORTATION, Items.HOPPER_MINECART)
/* 1223 */       .requires(Blocks.HOPPER)
/* 1224 */       .requires(Items.MINECART)
/* 1225 */       .unlockedBy("has_minecart", has(Items.MINECART))
/* 1226 */       .save(this.output);
/*      */     
/* 1228 */     shaped(RecipeCategory.TOOLS, Items.IRON_AXE)
/* 1229 */       .define(Character.valueOf('#'), Items.STICK)
/* 1230 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1231 */       .pattern("XX")
/* 1232 */       .pattern("X#")
/* 1233 */       .pattern(" #")
/* 1234 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1235 */       .save(this.output);
/*      */     
/* 1237 */     shaped(RecipeCategory.DECORATIONS, Blocks.IRON_BARS, 16)
/* 1238 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/* 1239 */       .pattern("###")
/* 1240 */       .pattern("###")
/* 1241 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1242 */       .save(this.output);
/*      */     
/* 1244 */     shaped(RecipeCategory.DECORATIONS, Blocks.COPPER_BARS.unaffected(), 16)
/* 1245 */       .define(Character.valueOf('#'), Items.COPPER_INGOT)
/* 1246 */       .pattern("###")
/* 1247 */       .pattern("###")
/* 1248 */       .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
/* 1249 */       .save(this.output);
/*      */     
/* 1251 */     shaped(RecipeCategory.COMBAT, Items.IRON_BOOTS)
/* 1252 */       .define(Character.valueOf('X'), Items.IRON_INGOT)
/* 1253 */       .pattern("X X")
/* 1254 */       .pattern("X X")
/* 1255 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1256 */       .save(this.output);
/*      */     
/* 1258 */     shaped(RecipeCategory.COMBAT, Items.IRON_CHESTPLATE)
/* 1259 */       .define(Character.valueOf('X'), Items.IRON_INGOT)
/* 1260 */       .pattern("X X")
/* 1261 */       .pattern("XXX")
/* 1262 */       .pattern("XXX")
/* 1263 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1264 */       .save(this.output);
/*      */     
/* 1266 */     doorBuilder(Blocks.IRON_DOOR, Ingredient.of(Items.IRON_INGOT))
/* 1267 */       .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
/* 1268 */       .save(this.output);
/*      */     
/* 1270 */     shaped(RecipeCategory.COMBAT, Items.IRON_HELMET)
/* 1271 */       .define(Character.valueOf('X'), Items.IRON_INGOT)
/* 1272 */       .pattern("XXX")
/* 1273 */       .pattern("X X")
/* 1274 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1275 */       .save(this.output);
/*      */     
/* 1277 */     shaped(RecipeCategory.TOOLS, Items.IRON_HOE)
/* 1278 */       .define(Character.valueOf('#'), Items.STICK)
/* 1279 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1280 */       .pattern("XX")
/* 1281 */       .pattern(" #")
/* 1282 */       .pattern(" #")
/* 1283 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1284 */       .save(this.output);
/*      */     
/* 1286 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, Items.IRON_INGOT, RecipeCategory.BUILDING_BLOCKS, Items.IRON_BLOCK, "iron_ingot_from_iron_block", "iron_ingot");
/* 1287 */     nineBlockStorageRecipesWithCustomPacking(RecipeCategory.MISC, Items.IRON_NUGGET, RecipeCategory.MISC, Items.IRON_INGOT, "iron_ingot_from_nuggets", "iron_ingot");
/*      */     
/* 1289 */     shaped(RecipeCategory.COMBAT, Items.IRON_LEGGINGS)
/* 1290 */       .define(Character.valueOf('X'), Items.IRON_INGOT)
/* 1291 */       .pattern("XXX")
/* 1292 */       .pattern("X X")
/* 1293 */       .pattern("X X")
/* 1294 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1295 */       .save(this.output);
/*      */     
/* 1297 */     shaped(RecipeCategory.TOOLS, Items.IRON_PICKAXE)
/* 1298 */       .define(Character.valueOf('#'), Items.STICK)
/* 1299 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1300 */       .pattern("XXX")
/* 1301 */       .pattern(" # ")
/* 1302 */       .pattern(" # ")
/* 1303 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1304 */       .save(this.output);
/*      */     
/* 1306 */     shaped(RecipeCategory.TOOLS, Items.IRON_SHOVEL)
/* 1307 */       .define(Character.valueOf('#'), Items.STICK)
/* 1308 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1309 */       .pattern("X")
/* 1310 */       .pattern("#")
/* 1311 */       .pattern("#")
/* 1312 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1313 */       .save(this.output);
/*      */     
/* 1315 */     shaped(RecipeCategory.COMBAT, Items.IRON_SWORD)
/* 1316 */       .define(Character.valueOf('#'), Items.STICK)
/* 1317 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1318 */       .pattern("X")
/* 1319 */       .pattern("X")
/* 1320 */       .pattern("#")
/* 1321 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1322 */       .save(this.output);
/*      */     
/* 1324 */     shaped(RecipeCategory.COMBAT, Items.IRON_SPEAR)
/* 1325 */       .define(Character.valueOf('#'), Items.STICK)
/* 1326 */       .define(Character.valueOf('X'), ItemTags.IRON_TOOL_MATERIALS)
/* 1327 */       .pattern("  X")
/* 1328 */       .pattern(" # ")
/* 1329 */       .pattern("#  ")
/* 1330 */       .unlockedBy("has_iron_ingot", has(ItemTags.IRON_TOOL_MATERIALS))
/* 1331 */       .save(this.output);
/*      */     
/* 1333 */     twoByTwoPacker(RecipeCategory.REDSTONE, Blocks.IRON_TRAPDOOR, Items.IRON_INGOT);
/*      */     
/* 1335 */     shaped(RecipeCategory.DECORATIONS, Items.ITEM_FRAME)
/* 1336 */       .define(Character.valueOf('#'), Items.STICK)
/* 1337 */       .define(Character.valueOf('X'), Items.LEATHER)
/* 1338 */       .pattern("###")
/* 1339 */       .pattern("#X#")
/* 1340 */       .pattern("###")
/* 1341 */       .unlockedBy("has_leather", has(Items.LEATHER))
/* 1342 */       .save(this.output);
/*      */     
/* 1344 */     shaped(RecipeCategory.DECORATIONS, Blocks.JUKEBOX)
/* 1345 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 1346 */       .define(Character.valueOf('X'), Items.DIAMOND)
/* 1347 */       .pattern("###")
/* 1348 */       .pattern("#X#")
/* 1349 */       .pattern("###")
/* 1350 */       .unlockedBy("has_diamond", has(Items.DIAMOND))
/* 1351 */       .save(this.output);
/*      */     
/* 1353 */     shaped(RecipeCategory.DECORATIONS, Blocks.LADDER, 3)
/* 1354 */       .define(Character.valueOf('#'), Items.STICK)
/* 1355 */       .pattern("# #")
/* 1356 */       .pattern("###")
/* 1357 */       .pattern("# #")
/* 1358 */       .unlockedBy("has_stick", has(Items.STICK))
/* 1359 */       .save(this.output);
/*      */     
/* 1361 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.LAPIS_LAZULI, RecipeCategory.BUILDING_BLOCKS, Items.LAPIS_BLOCK);
/*      */     
/* 1363 */     shaped(RecipeCategory.TOOLS, Items.LEAD, 2)
/* 1364 */       .define(Character.valueOf('~'), Items.STRING)
/* 1365 */       .pattern("~~ ")
/* 1366 */       .pattern("~~ ")
/* 1367 */       .pattern("  ~")
/* 1368 */       .unlockedBy("has_string", has(Items.STRING))
/* 1369 */       .save(this.output);
/*      */     
/* 1371 */     twoByTwoPacker(RecipeCategory.MISC, Items.LEATHER, Items.RABBIT_HIDE);
/*      */     
/* 1373 */     shaped(RecipeCategory.COMBAT, Items.LEATHER_BOOTS)
/* 1374 */       .define(Character.valueOf('X'), Items.LEATHER)
/* 1375 */       .pattern("X X")
/* 1376 */       .pattern("X X")
/* 1377 */       .unlockedBy("has_leather", has(Items.LEATHER))
/* 1378 */       .save(this.output);
/*      */     
/* 1380 */     shaped(RecipeCategory.COMBAT, Items.LEATHER_CHESTPLATE)
/* 1381 */       .define(Character.valueOf('X'), Items.LEATHER)
/* 1382 */       .pattern("X X")
/* 1383 */       .pattern("XXX")
/* 1384 */       .pattern("XXX")
/* 1385 */       .unlockedBy("has_leather", has(Items.LEATHER))
/* 1386 */       .save(this.output);
/*      */     
/* 1388 */     shaped(RecipeCategory.COMBAT, Items.LEATHER_HELMET)
/* 1389 */       .define(Character.valueOf('X'), Items.LEATHER)
/* 1390 */       .pattern("XXX")
/* 1391 */       .pattern("X X")
/* 1392 */       .unlockedBy("has_leather", has(Items.LEATHER))
/* 1393 */       .save(this.output);
/*      */     
/* 1395 */     shaped(RecipeCategory.COMBAT, Items.LEATHER_LEGGINGS)
/* 1396 */       .define(Character.valueOf('X'), Items.LEATHER)
/* 1397 */       .pattern("XXX")
/* 1398 */       .pattern("X X")
/* 1399 */       .pattern("X X")
/* 1400 */       .unlockedBy("has_leather", has(Items.LEATHER))
/* 1401 */       .save(this.output);
/*      */     
/* 1403 */     shaped(RecipeCategory.MISC, Items.LEATHER_HORSE_ARMOR)
/* 1404 */       .define(Character.valueOf('X'), Items.LEATHER)
/* 1405 */       .pattern("X X")
/* 1406 */       .pattern("XXX")
/* 1407 */       .pattern("X X")
/* 1408 */       .unlockedBy("has_leather", has(Items.LEATHER))
/* 1409 */       .save(this.output);
/*      */ 
/*      */     
/* 1412 */     shaped(RecipeCategory.COMBAT, Items.SADDLE)
/* 1413 */       .define(Character.valueOf('X'), Items.LEATHER)
/* 1414 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/* 1415 */       .pattern(" X ")
/* 1416 */       .pattern("X#X")
/* 1417 */       .unlockedBy("has_leather", has(Items.LEATHER))
/* 1418 */       .save(this.output);
/*      */     
/* 1420 */     nineBlockStorageRecipesWithCustomPacking(RecipeCategory.MISC, Items.COPPER_NUGGET, RecipeCategory.MISC, Items.COPPER_INGOT, "copper_ingot_from_nuggets", "copper_ingot");
/*      */     
/* 1422 */     shaped(RecipeCategory.TOOLS, Items.COPPER_AXE)
/* 1423 */       .define(Character.valueOf('#'), Items.STICK)
/* 1424 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1425 */       .pattern("XX")
/* 1426 */       .pattern("X#")
/* 1427 */       .pattern(" #")
/* 1428 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1429 */       .save(this.output);
/*      */     
/* 1431 */     shaped(RecipeCategory.TOOLS, Items.COPPER_HOE)
/* 1432 */       .define(Character.valueOf('#'), Items.STICK)
/* 1433 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1434 */       .pattern("XX")
/* 1435 */       .pattern(" #")
/* 1436 */       .pattern(" #")
/* 1437 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1438 */       .save(this.output);
/*      */     
/* 1440 */     shaped(RecipeCategory.TOOLS, Items.COPPER_PICKAXE)
/* 1441 */       .define(Character.valueOf('#'), Items.STICK)
/* 1442 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1443 */       .pattern("XXX")
/* 1444 */       .pattern(" # ")
/* 1445 */       .pattern(" # ")
/* 1446 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1447 */       .save(this.output);
/*      */     
/* 1449 */     shaped(RecipeCategory.TOOLS, Items.COPPER_SHOVEL)
/* 1450 */       .define(Character.valueOf('#'), Items.STICK)
/* 1451 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1452 */       .pattern("X")
/* 1453 */       .pattern("#")
/* 1454 */       .pattern("#")
/* 1455 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1456 */       .save(this.output);
/*      */     
/* 1458 */     shaped(RecipeCategory.COMBAT, Items.COPPER_SWORD)
/* 1459 */       .define(Character.valueOf('#'), Items.STICK)
/* 1460 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1461 */       .pattern("X")
/* 1462 */       .pattern("X")
/* 1463 */       .pattern("#")
/* 1464 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1465 */       .save(this.output);
/*      */     
/* 1467 */     shaped(RecipeCategory.COMBAT, Items.COPPER_SPEAR)
/* 1468 */       .define(Character.valueOf('#'), Items.STICK)
/* 1469 */       .define(Character.valueOf('X'), ItemTags.COPPER_TOOL_MATERIALS)
/* 1470 */       .pattern("  X")
/* 1471 */       .pattern(" # ")
/* 1472 */       .pattern("#  ")
/* 1473 */       .unlockedBy("has_copper_ingot", has(ItemTags.COPPER_TOOL_MATERIALS))
/* 1474 */       .save(this.output);
/*      */     
/* 1476 */     shaped(RecipeCategory.COMBAT, Items.COPPER_BOOTS)
/* 1477 */       .define(Character.valueOf('X'), Items.COPPER_INGOT)
/* 1478 */       .pattern("X X")
/* 1479 */       .pattern("X X")
/* 1480 */       .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
/* 1481 */       .save(this.output);
/*      */     
/* 1483 */     shaped(RecipeCategory.COMBAT, Items.COPPER_CHESTPLATE)
/* 1484 */       .define(Character.valueOf('X'), Items.COPPER_INGOT)
/* 1485 */       .pattern("X X")
/* 1486 */       .pattern("XXX")
/* 1487 */       .pattern("XXX")
/* 1488 */       .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
/* 1489 */       .save(this.output);
/*      */     
/* 1491 */     shaped(RecipeCategory.COMBAT, Items.COPPER_HELMET)
/* 1492 */       .define(Character.valueOf('X'), Items.COPPER_INGOT)
/* 1493 */       .pattern("XXX")
/* 1494 */       .pattern("X X")
/* 1495 */       .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
/* 1496 */       .save(this.output);
/*      */     
/* 1498 */     shaped(RecipeCategory.COMBAT, Items.COPPER_LEGGINGS)
/* 1499 */       .define(Character.valueOf('X'), Items.COPPER_INGOT)
/* 1500 */       .pattern("XXX")
/* 1501 */       .pattern("X X")
/* 1502 */       .pattern("X X")
/* 1503 */       .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
/* 1504 */       .save(this.output);
/*      */     
/* 1506 */     shaped(RecipeCategory.REDSTONE, Blocks.LECTERN)
/* 1507 */       .define(Character.valueOf('S'), ItemTags.WOODEN_SLABS)
/* 1508 */       .define(Character.valueOf('B'), Blocks.BOOKSHELF)
/* 1509 */       .pattern("SSS")
/* 1510 */       .pattern(" B ")
/* 1511 */       .pattern(" S ")
/* 1512 */       .unlockedBy("has_book", has(Items.BOOK))
/* 1513 */       .save(this.output);
/*      */     
/* 1515 */     shaped(RecipeCategory.REDSTONE, Blocks.LEVER)
/* 1516 */       .define(Character.valueOf('#'), Blocks.COBBLESTONE)
/* 1517 */       .define(Character.valueOf('X'), Items.STICK)
/* 1518 */       .pattern("X")
/* 1519 */       .pattern("#")
/* 1520 */       .unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
/* 1521 */       .save(this.output);
/*      */     
/* 1523 */     oneToOneConversionRecipe(Items.LIGHT_BLUE_DYE, Blocks.BLUE_ORCHID, "light_blue_dye");
/*      */     
/* 1525 */     shapeless(RecipeCategory.MISC, Items.LIGHT_BLUE_DYE, 2)
/* 1526 */       .requires(Items.BLUE_DYE)
/* 1527 */       .requires(Items.WHITE_DYE)
/* 1528 */       .group("light_blue_dye")
/* 1529 */       .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
/* 1530 */       .unlockedBy("has_white_dye", has(Items.WHITE_DYE))
/* 1531 */       .save(this.output, "light_blue_dye_from_blue_white_dye");
/*      */     
/* 1533 */     oneToOneConversionRecipe(Items.LIGHT_GRAY_DYE, Blocks.AZURE_BLUET, "light_gray_dye");
/*      */     
/* 1535 */     shapeless(RecipeCategory.MISC, Items.LIGHT_GRAY_DYE, 2)
/* 1536 */       .requires(Items.GRAY_DYE)
/* 1537 */       .requires(Items.WHITE_DYE)
/* 1538 */       .group("light_gray_dye")
/* 1539 */       .unlockedBy("has_gray_dye", has(Items.GRAY_DYE))
/* 1540 */       .unlockedBy("has_white_dye", has(Items.WHITE_DYE))
/* 1541 */       .save(this.output, "light_gray_dye_from_gray_white_dye");
/*      */     
/* 1543 */     shapeless(RecipeCategory.MISC, Items.LIGHT_GRAY_DYE, 3)
/* 1544 */       .requires(Items.BLACK_DYE)
/* 1545 */       .requires(Items.WHITE_DYE, 2)
/* 1546 */       .group("light_gray_dye")
/* 1547 */       .unlockedBy("has_white_dye", has(Items.WHITE_DYE))
/* 1548 */       .unlockedBy("has_black_dye", has(Items.BLACK_DYE))
/* 1549 */       .save(this.output, "light_gray_dye_from_black_white_dye");
/*      */     
/* 1551 */     oneToOneConversionRecipe(Items.LIGHT_GRAY_DYE, Blocks.OXEYE_DAISY, "light_gray_dye");
/*      */     
/* 1553 */     oneToOneConversionRecipe(Items.LIGHT_GRAY_DYE, Blocks.WHITE_TULIP, "light_gray_dye");
/*      */     
/* 1555 */     pressurePlate(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Items.GOLD_INGOT);
/*      */     
/* 1557 */     shaped(RecipeCategory.REDSTONE, Blocks.LIGHTNING_ROD)
/* 1558 */       .define(Character.valueOf('#'), Items.COPPER_INGOT)
/* 1559 */       .pattern("#")
/* 1560 */       .pattern("#")
/* 1561 */       .pattern("#")
/* 1562 */       .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
/* 1563 */       .save(this.output);
/*      */     
/* 1565 */     shapeless(RecipeCategory.MISC, Items.LIME_DYE, 2)
/* 1566 */       .requires(Items.GREEN_DYE)
/* 1567 */       .requires(Items.WHITE_DYE)
/* 1568 */       .unlockedBy("has_green_dye", has(Items.GREEN_DYE))
/* 1569 */       .unlockedBy("has_white_dye", has(Items.WHITE_DYE))
/* 1570 */       .save(this.output);
/*      */     
/* 1572 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.JACK_O_LANTERN)
/* 1573 */       .define(Character.valueOf('A'), Blocks.CARVED_PUMPKIN)
/* 1574 */       .define(Character.valueOf('B'), Blocks.TORCH)
/* 1575 */       .pattern("A")
/* 1576 */       .pattern("B")
/* 1577 */       .unlockedBy("has_carved_pumpkin", has(Blocks.CARVED_PUMPKIN))
/* 1578 */       .save(this.output);
/*      */     
/* 1580 */     oneToOneConversionRecipe(Items.MAGENTA_DYE, Blocks.ALLIUM, "magenta_dye");
/*      */     
/* 1582 */     shapeless(RecipeCategory.MISC, Items.MAGENTA_DYE, 4)
/* 1583 */       .requires(Items.BLUE_DYE)
/* 1584 */       .requires(Items.RED_DYE, 2)
/* 1585 */       .requires(Items.WHITE_DYE)
/* 1586 */       .group("magenta_dye")
/* 1587 */       .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
/* 1588 */       .unlockedBy("has_rose_red", has(Items.RED_DYE))
/* 1589 */       .unlockedBy("has_white_dye", has(Items.WHITE_DYE))
/* 1590 */       .save(this.output, "magenta_dye_from_blue_red_white_dye");
/*      */     
/* 1592 */     shapeless(RecipeCategory.MISC, Items.MAGENTA_DYE, 3)
/* 1593 */       .requires(Items.BLUE_DYE)
/* 1594 */       .requires(Items.RED_DYE)
/* 1595 */       .requires(Items.PINK_DYE)
/* 1596 */       .group("magenta_dye")
/* 1597 */       .unlockedBy("has_pink_dye", has(Items.PINK_DYE))
/* 1598 */       .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
/* 1599 */       .unlockedBy("has_red_dye", has(Items.RED_DYE))
/* 1600 */       .save(this.output, "magenta_dye_from_blue_red_pink");
/*      */     
/* 1602 */     oneToOneConversionRecipe(Items.MAGENTA_DYE, Blocks.LILAC, "magenta_dye", 2);
/*      */     
/* 1604 */     shapeless(RecipeCategory.MISC, Items.MAGENTA_DYE, 2)
/* 1605 */       .requires(Items.PURPLE_DYE)
/* 1606 */       .requires(Items.PINK_DYE)
/* 1607 */       .group("magenta_dye")
/* 1608 */       .unlockedBy("has_pink_dye", has(Items.PINK_DYE))
/* 1609 */       .unlockedBy("has_purple_dye", has(Items.PURPLE_DYE))
/* 1610 */       .save(this.output, "magenta_dye_from_purple_and_pink");
/*      */     
/* 1612 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.MAGMA_BLOCK, Items.MAGMA_CREAM);
/*      */     
/* 1614 */     shapeless(RecipeCategory.BREWING, Items.MAGMA_CREAM)
/* 1615 */       .requires(Items.BLAZE_POWDER)
/* 1616 */       .requires(Items.SLIME_BALL)
/* 1617 */       .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
/* 1618 */       .save(this.output);
/*      */     
/* 1620 */     shaped(RecipeCategory.MISC, Items.MAP)
/* 1621 */       .define(Character.valueOf('#'), Items.PAPER)
/* 1622 */       .define(Character.valueOf('X'), Items.COMPASS)
/* 1623 */       .pattern("###")
/* 1624 */       .pattern("#X#")
/* 1625 */       .pattern("###")
/* 1626 */       .unlockedBy("has_compass", has(Items.COMPASS))
/* 1627 */       .save(this.output);
/*      */     
/* 1629 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, Blocks.MELON, Items.MELON_SLICE, "has_melon");
/*      */     
/* 1631 */     shapeless(RecipeCategory.MISC, Items.MELON_SEEDS)
/* 1632 */       .requires(Items.MELON_SLICE)
/* 1633 */       .unlockedBy("has_melon", has(Items.MELON_SLICE))
/* 1634 */       .save(this.output);
/*      */     
/* 1636 */     shaped(RecipeCategory.TRANSPORTATION, Items.MINECART)
/* 1637 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/* 1638 */       .pattern("# #")
/* 1639 */       .pattern("###")
/* 1640 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1641 */       .save(this.output);
/*      */     
/* 1643 */     shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_COBBLESTONE)
/* 1644 */       .requires(Blocks.COBBLESTONE)
/* 1645 */       .requires(Blocks.VINE)
/* 1646 */       .group("mossy_cobblestone")
/* 1647 */       .unlockedBy("has_vine", has(Blocks.VINE))
/* 1648 */       .save(this.output, getConversionRecipeName(Blocks.MOSSY_COBBLESTONE, Blocks.VINE));
/*      */     
/* 1650 */     shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_STONE_BRICKS)
/* 1651 */       .requires(Blocks.STONE_BRICKS)
/* 1652 */       .requires(Blocks.VINE)
/* 1653 */       .group("mossy_stone_bricks")
/* 1654 */       .unlockedBy("has_vine", has(Blocks.VINE))
/* 1655 */       .save(this.output, getConversionRecipeName(Blocks.MOSSY_STONE_BRICKS, Blocks.VINE));
/*      */     
/* 1657 */     shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_COBBLESTONE)
/* 1658 */       .requires(Blocks.COBBLESTONE)
/* 1659 */       .requires(Blocks.MOSS_BLOCK)
/* 1660 */       .group("mossy_cobblestone")
/* 1661 */       .unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK))
/* 1662 */       .save(this.output, getConversionRecipeName(Blocks.MOSSY_COBBLESTONE, Blocks.MOSS_BLOCK));
/*      */     
/* 1664 */     shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_STONE_BRICKS)
/* 1665 */       .requires(Blocks.STONE_BRICKS)
/* 1666 */       .requires(Blocks.MOSS_BLOCK)
/* 1667 */       .group("mossy_stone_bricks")
/* 1668 */       .unlockedBy("has_moss_block", has(Blocks.MOSS_BLOCK))
/* 1669 */       .save(this.output, getConversionRecipeName(Blocks.MOSSY_STONE_BRICKS, Blocks.MOSS_BLOCK));
/*      */     
/* 1671 */     shapeless(RecipeCategory.FOOD, Items.MUSHROOM_STEW)
/* 1672 */       .requires(Blocks.BROWN_MUSHROOM)
/* 1673 */       .requires(Blocks.RED_MUSHROOM)
/* 1674 */       .requires(Items.BOWL)
/* 1675 */       .unlockedBy("has_mushroom_stew", has(Items.MUSHROOM_STEW))
/* 1676 */       .unlockedBy("has_bowl", has(Items.BOWL))
/* 1677 */       .unlockedBy("has_brown_mushroom", has(Blocks.BROWN_MUSHROOM))
/* 1678 */       .unlockedBy("has_red_mushroom", has(Blocks.RED_MUSHROOM))
/* 1679 */       .save(this.output);
/*      */     
/* 1681 */     BuiltInRegistries.ITEM.stream().forEach(item -> {
/* 1682 */           SuspiciousEffectHolder effectHolder = SuspiciousEffectHolder.tryGet(item);
/* 1683 */           if (effectHolder != null) {
/* 1684 */             suspiciousStew(item, effectHolder);
/*      */           }
/*      */         });
/*      */     
/* 1688 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.NETHER_BRICKS, Items.NETHER_BRICK);
/* 1689 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.RESIN_BRICKS, Items.RESIN_BRICK);
/* 1690 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.RESIN_CLUMP, RecipeCategory.BUILDING_BLOCKS, Items.RESIN_BLOCK);
/*      */     
/* 1692 */     shaped(RecipeCategory.MISC, Blocks.CREAKING_HEART)
/* 1693 */       .define(Character.valueOf('R'), Items.RESIN_BLOCK)
/* 1694 */       .define(Character.valueOf('L'), Blocks.PALE_OAK_LOG)
/* 1695 */       .pattern(" L ")
/* 1696 */       .pattern(" R ")
/* 1697 */       .pattern(" L ")
/* 1698 */       .unlockedBy("has_resin_block", has(Items.RESIN_BLOCK))
/* 1699 */       .save(this.output);
/*      */     
/* 1701 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, Blocks.NETHER_WART_BLOCK, Items.NETHER_WART);
/*      */     
/* 1703 */     shaped(RecipeCategory.REDSTONE, Blocks.NOTE_BLOCK)
/* 1704 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 1705 */       .define(Character.valueOf('X'), Items.REDSTONE)
/* 1706 */       .pattern("###")
/* 1707 */       .pattern("#X#")
/* 1708 */       .pattern("###")
/* 1709 */       .unlockedBy("has_redstone", has(Items.REDSTONE))
/* 1710 */       .save(this.output);
/*      */     
/* 1712 */     shaped(RecipeCategory.REDSTONE, Blocks.OBSERVER)
/* 1713 */       .define(Character.valueOf('Q'), Items.QUARTZ)
/* 1714 */       .define(Character.valueOf('R'), Items.REDSTONE)
/* 1715 */       .define(Character.valueOf('#'), Blocks.COBBLESTONE)
/* 1716 */       .pattern("###")
/* 1717 */       .pattern("RRQ")
/* 1718 */       .pattern("###")
/* 1719 */       .unlockedBy("has_quartz", has(Items.QUARTZ))
/* 1720 */       .save(this.output);
/*      */     
/* 1722 */     oneToOneConversionRecipe(Items.ORANGE_DYE, Blocks.ORANGE_TULIP, "orange_dye");
/*      */     
/* 1724 */     shapeless(RecipeCategory.MISC, Items.ORANGE_DYE, 2)
/* 1725 */       .requires(Items.RED_DYE)
/* 1726 */       .requires(Items.YELLOW_DYE)
/* 1727 */       .group("orange_dye")
/* 1728 */       .unlockedBy("has_red_dye", has(Items.RED_DYE))
/* 1729 */       .unlockedBy("has_yellow_dye", has(Items.YELLOW_DYE))
/* 1730 */       .save(this.output, "orange_dye_from_red_yellow");
/*      */     
/* 1732 */     shaped(RecipeCategory.DECORATIONS, Items.PAINTING)
/* 1733 */       .define(Character.valueOf('#'), Items.STICK)
/* 1734 */       .define(Character.valueOf('X'), ItemTags.WOOL)
/* 1735 */       .pattern("###")
/* 1736 */       .pattern("#X#")
/* 1737 */       .pattern("###")
/* 1738 */       .unlockedBy("has_wool", has(ItemTags.WOOL))
/* 1739 */       .save(this.output);
/*      */     
/* 1741 */     shaped(RecipeCategory.MISC, Items.PAPER, 3)
/* 1742 */       .define(Character.valueOf('#'), Blocks.SUGAR_CANE)
/* 1743 */       .pattern("###")
/* 1744 */       .unlockedBy("has_reeds", has(Blocks.SUGAR_CANE))
/* 1745 */       .save(this.output);
/*      */     
/* 1747 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.QUARTZ_PILLAR, 2)
/* 1748 */       .define(Character.valueOf('#'), Blocks.QUARTZ_BLOCK)
/* 1749 */       .pattern("#")
/* 1750 */       .pattern("#")
/* 1751 */       .unlockedBy("has_chiseled_quartz_block", has(Blocks.CHISELED_QUARTZ_BLOCK))
/* 1752 */       .unlockedBy("has_quartz_block", has(Blocks.QUARTZ_BLOCK))
/* 1753 */       .unlockedBy("has_quartz_pillar", has(Blocks.QUARTZ_PILLAR))
/* 1754 */       .save(this.output);
/*      */     
/* 1756 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, Blocks.PACKED_ICE, Blocks.ICE);
/*      */     
/* 1758 */     oneToOneConversionRecipe(Items.PINK_DYE, Blocks.PEONY, "pink_dye", 2);
/* 1759 */     oneToOneConversionRecipe(Items.PINK_DYE, Blocks.PINK_TULIP, "pink_dye");
/* 1760 */     oneToOneConversionRecipe(Items.PINK_DYE, Blocks.CACTUS_FLOWER, "pink_dye");
/*      */     
/* 1762 */     shapeless(RecipeCategory.MISC, Items.PINK_DYE, 2)
/* 1763 */       .requires(Items.RED_DYE)
/* 1764 */       .requires(Items.WHITE_DYE)
/* 1765 */       .group("pink_dye")
/* 1766 */       .unlockedBy("has_white_dye", has(Items.WHITE_DYE))
/* 1767 */       .unlockedBy("has_red_dye", has(Items.RED_DYE))
/* 1768 */       .save(this.output, "pink_dye_from_red_white_dye");
/*      */     
/* 1770 */     shaped(RecipeCategory.REDSTONE, Blocks.PISTON)
/* 1771 */       .define(Character.valueOf('R'), Items.REDSTONE)
/* 1772 */       .define(Character.valueOf('#'), Blocks.COBBLESTONE)
/* 1773 */       .define(Character.valueOf('T'), ItemTags.PLANKS)
/* 1774 */       .define(Character.valueOf('X'), Items.IRON_INGOT)
/* 1775 */       .pattern("TTT")
/* 1776 */       .pattern("#X#")
/* 1777 */       .pattern("#R#")
/* 1778 */       .unlockedBy("has_redstone", has(Items.REDSTONE))
/* 1779 */       .save(this.output);
/*      */     
/* 1781 */     polished(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BASALT, Blocks.BASALT);
/*      */     
/* 1783 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.PRISMARINE, Items.PRISMARINE_SHARD);
/*      */     
/* 1785 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, Blocks.PRISMARINE_BRICKS, Items.PRISMARINE_SHARD);
/*      */     
/* 1787 */     shapeless(RecipeCategory.FOOD, Items.PUMPKIN_PIE)
/* 1788 */       .requires(Blocks.PUMPKIN)
/* 1789 */       .requires(Items.SUGAR)
/* 1790 */       .requires(ItemTags.EGGS)
/* 1791 */       .unlockedBy("has_carved_pumpkin", has(Blocks.CARVED_PUMPKIN))
/* 1792 */       .unlockedBy("has_pumpkin", has(Blocks.PUMPKIN))
/* 1793 */       .save(this.output);
/*      */     
/* 1795 */     shapeless(RecipeCategory.MISC, Items.PUMPKIN_SEEDS, 4)
/* 1796 */       .requires(Blocks.PUMPKIN)
/* 1797 */       .unlockedBy("has_pumpkin", has(Blocks.PUMPKIN))
/* 1798 */       .save(this.output);
/*      */     
/* 1800 */     shapeless(RecipeCategory.MISC, Items.PURPLE_DYE, 2)
/* 1801 */       .requires(Items.BLUE_DYE)
/* 1802 */       .requires(Items.RED_DYE)
/* 1803 */       .unlockedBy("has_blue_dye", has(Items.BLUE_DYE))
/* 1804 */       .unlockedBy("has_red_dye", has(Items.RED_DYE))
/* 1805 */       .save(this.output);
/*      */     
/* 1807 */     shaped(RecipeCategory.DECORATIONS, Blocks.SHULKER_BOX)
/* 1808 */       .define(Character.valueOf('#'), Blocks.CHEST)
/* 1809 */       .define(Character.valueOf('-'), Items.SHULKER_SHELL)
/* 1810 */       .pattern("-")
/* 1811 */       .pattern("#")
/* 1812 */       .pattern("-")
/* 1813 */       .unlockedBy("has_shulker_shell", has(Items.SHULKER_SHELL))
/* 1814 */       .save(this.output);
/*      */     
/* 1816 */     shulkerBoxRecipes();
/*      */     
/* 1818 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.PURPUR_BLOCK, 4)
/* 1819 */       .define(Character.valueOf('F'), Items.POPPED_CHORUS_FRUIT)
/* 1820 */       .pattern("FF")
/* 1821 */       .pattern("FF")
/* 1822 */       .unlockedBy("has_chorus_fruit_popped", has(Items.POPPED_CHORUS_FRUIT))
/* 1823 */       .save(this.output);
/*      */     
/* 1825 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.PURPUR_PILLAR)
/* 1826 */       .define(Character.valueOf('#'), Blocks.PURPUR_SLAB)
/* 1827 */       .pattern("#")
/* 1828 */       .pattern("#")
/* 1829 */       .unlockedBy("has_purpur_block", has(Blocks.PURPUR_BLOCK))
/* 1830 */       .save(this.output);
/*      */     
/* 1832 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.PURPUR_SLAB, Ingredient.of(new ItemLike[] { Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR
/* 1833 */           })).unlockedBy("has_purpur_block", has(Blocks.PURPUR_BLOCK))
/* 1834 */       .save(this.output);
/*      */     
/* 1836 */     stairBuilder(Blocks.PURPUR_STAIRS, Ingredient.of(new ItemLike[] { Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR
/* 1837 */           })).unlockedBy("has_purpur_block", has(Blocks.PURPUR_BLOCK))
/* 1838 */       .save(this.output);
/*      */     
/* 1840 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.QUARTZ_BLOCK, Items.QUARTZ);
/*      */     
/* 1842 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.QUARTZ_BRICKS, 4)
/* 1843 */       .define(Character.valueOf('#'), Blocks.QUARTZ_BLOCK)
/* 1844 */       .pattern("##")
/* 1845 */       .pattern("##")
/* 1846 */       .unlockedBy("has_quartz_block", has(Blocks.QUARTZ_BLOCK))
/* 1847 */       .save(this.output);
/*      */     
/* 1849 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.QUARTZ_SLAB, Ingredient.of(new ItemLike[] { Blocks.CHISELED_QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR
/* 1850 */           })).unlockedBy("has_chiseled_quartz_block", has(Blocks.CHISELED_QUARTZ_BLOCK))
/* 1851 */       .unlockedBy("has_quartz_block", has(Blocks.QUARTZ_BLOCK))
/* 1852 */       .unlockedBy("has_quartz_pillar", has(Blocks.QUARTZ_PILLAR))
/* 1853 */       .save(this.output);
/*      */     
/* 1855 */     stairBuilder(Blocks.QUARTZ_STAIRS, Ingredient.of(new ItemLike[] { Blocks.CHISELED_QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR
/* 1856 */           })).unlockedBy("has_chiseled_quartz_block", has(Blocks.CHISELED_QUARTZ_BLOCK))
/* 1857 */       .unlockedBy("has_quartz_block", has(Blocks.QUARTZ_BLOCK))
/* 1858 */       .unlockedBy("has_quartz_pillar", has(Blocks.QUARTZ_PILLAR))
/* 1859 */       .save(this.output);
/*      */     
/* 1861 */     shapeless(RecipeCategory.FOOD, Items.RABBIT_STEW)
/* 1862 */       .requires(Items.BAKED_POTATO)
/* 1863 */       .requires(Items.COOKED_RABBIT)
/* 1864 */       .requires(Items.BOWL)
/* 1865 */       .requires(Items.CARROT)
/* 1866 */       .requires(Blocks.BROWN_MUSHROOM)
/* 1867 */       .group("rabbit_stew")
/* 1868 */       .unlockedBy("has_cooked_rabbit", has(Items.COOKED_RABBIT))
/* 1869 */       .save(this.output, getConversionRecipeName(Items.RABBIT_STEW, Items.BROWN_MUSHROOM));
/*      */     
/* 1871 */     shapeless(RecipeCategory.FOOD, Items.RABBIT_STEW)
/* 1872 */       .requires(Items.BAKED_POTATO)
/* 1873 */       .requires(Items.COOKED_RABBIT)
/* 1874 */       .requires(Items.BOWL)
/* 1875 */       .requires(Items.CARROT)
/* 1876 */       .requires(Blocks.RED_MUSHROOM)
/* 1877 */       .group("rabbit_stew")
/* 1878 */       .unlockedBy("has_cooked_rabbit", has(Items.COOKED_RABBIT))
/* 1879 */       .save(this.output, getConversionRecipeName(Items.RABBIT_STEW, Items.RED_MUSHROOM));
/*      */     
/* 1881 */     shaped(RecipeCategory.TRANSPORTATION, Blocks.RAIL, 16)
/* 1882 */       .define(Character.valueOf('#'), Items.STICK)
/* 1883 */       .define(Character.valueOf('X'), Items.IRON_INGOT)
/* 1884 */       .pattern("X X")
/* 1885 */       .pattern("X#X")
/* 1886 */       .pattern("X X")
/* 1887 */       .unlockedBy("has_minecart", has(Items.MINECART))
/* 1888 */       .save(this.output);
/*      */     
/* 1890 */     nineBlockStorageRecipes(RecipeCategory.REDSTONE, Items.REDSTONE, RecipeCategory.REDSTONE, Items.REDSTONE_BLOCK);
/*      */     
/* 1892 */     shaped(RecipeCategory.REDSTONE, Blocks.REDSTONE_LAMP)
/* 1893 */       .define(Character.valueOf('R'), Items.REDSTONE)
/* 1894 */       .define(Character.valueOf('G'), Blocks.GLOWSTONE)
/* 1895 */       .pattern(" R ")
/* 1896 */       .pattern("RGR")
/* 1897 */       .pattern(" R ")
/* 1898 */       .unlockedBy("has_glowstone", has(Blocks.GLOWSTONE))
/* 1899 */       .save(this.output);
/*      */     
/* 1901 */     shaped(RecipeCategory.REDSTONE, Blocks.REDSTONE_TORCH)
/* 1902 */       .define(Character.valueOf('#'), Items.STICK)
/* 1903 */       .define(Character.valueOf('X'), Items.REDSTONE)
/* 1904 */       .pattern("X")
/* 1905 */       .pattern("#")
/* 1906 */       .unlockedBy("has_redstone", has(Items.REDSTONE))
/* 1907 */       .save(this.output);
/*      */     
/* 1909 */     oneToOneConversionRecipe(Items.RED_DYE, Items.BEETROOT, "red_dye");
/* 1910 */     oneToOneConversionRecipe(Items.RED_DYE, Blocks.POPPY, "red_dye");
/* 1911 */     oneToOneConversionRecipe(Items.RED_DYE, Blocks.ROSE_BUSH, "red_dye", 2);
/*      */     
/* 1913 */     oneToOneConversionRecipe(Items.ORANGE_DYE, Blocks.OPEN_EYEBLOSSOM, "orange_dye");
/* 1914 */     oneToOneConversionRecipe(Items.GRAY_DYE, Blocks.CLOSED_EYEBLOSSOM, "gray_dye");
/*      */     
/* 1916 */     shapeless(RecipeCategory.MISC, Items.RED_DYE)
/* 1917 */       .requires(Blocks.RED_TULIP)
/* 1918 */       .group("red_dye")
/* 1919 */       .unlockedBy("has_red_flower", has(Blocks.RED_TULIP))
/* 1920 */       .save(this.output, "red_dye_from_tulip");
/*      */     
/* 1922 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_NETHER_BRICKS)
/* 1923 */       .define(Character.valueOf('W'), Items.NETHER_WART)
/* 1924 */       .define(Character.valueOf('N'), Items.NETHER_BRICK)
/* 1925 */       .pattern("NW")
/* 1926 */       .pattern("WN")
/* 1927 */       .unlockedBy("has_nether_wart", has(Items.NETHER_WART))
/* 1928 */       .save(this.output);
/*      */     
/* 1930 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_SANDSTONE)
/* 1931 */       .define(Character.valueOf('#'), Blocks.RED_SAND)
/* 1932 */       .pattern("##")
/* 1933 */       .pattern("##")
/* 1934 */       .unlockedBy("has_sand", has(Blocks.RED_SAND))
/* 1935 */       .save(this.output);
/*      */     
/* 1937 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_SANDSTONE_SLAB, Ingredient.of(new ItemLike[] { Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE
/* 1938 */           })).unlockedBy("has_red_sandstone", has(Blocks.RED_SANDSTONE))
/* 1939 */       .unlockedBy("has_chiseled_red_sandstone", has(Blocks.CHISELED_RED_SANDSTONE))
/* 1940 */       .save(this.output);
/*      */     
/* 1942 */     stairBuilder(Blocks.RED_SANDSTONE_STAIRS, Ingredient.of(new ItemLike[] { Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE
/* 1943 */           })).unlockedBy("has_red_sandstone", has(Blocks.RED_SANDSTONE))
/* 1944 */       .unlockedBy("has_chiseled_red_sandstone", has(Blocks.CHISELED_RED_SANDSTONE))
/* 1945 */       .unlockedBy("has_cut_red_sandstone", has(Blocks.CUT_RED_SANDSTONE))
/* 1946 */       .save(this.output);
/*      */     
/* 1948 */     shaped(RecipeCategory.REDSTONE, Blocks.REPEATER)
/* 1949 */       .define(Character.valueOf('#'), Blocks.REDSTONE_TORCH)
/* 1950 */       .define(Character.valueOf('X'), Items.REDSTONE)
/* 1951 */       .define(Character.valueOf('I'), Blocks.STONE)
/* 1952 */       .pattern("#X#")
/* 1953 */       .pattern("III")
/* 1954 */       .unlockedBy("has_redstone_torch", has(Blocks.REDSTONE_TORCH))
/* 1955 */       .save(this.output);
/*      */     
/* 1957 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.SANDSTONE, Blocks.SAND);
/*      */     
/* 1959 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.SANDSTONE_SLAB, Ingredient.of(new ItemLike[] { Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE
/* 1960 */           })).unlockedBy("has_sandstone", has(Blocks.SANDSTONE))
/* 1961 */       .unlockedBy("has_chiseled_sandstone", has(Blocks.CHISELED_SANDSTONE))
/* 1962 */       .save(this.output);
/*      */     
/* 1964 */     stairBuilder(Blocks.SANDSTONE_STAIRS, Ingredient.of(new ItemLike[] { Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.CUT_SANDSTONE
/* 1965 */           })).unlockedBy("has_sandstone", has(Blocks.SANDSTONE))
/* 1966 */       .unlockedBy("has_chiseled_sandstone", has(Blocks.CHISELED_SANDSTONE))
/* 1967 */       .unlockedBy("has_cut_sandstone", has(Blocks.CUT_SANDSTONE))
/* 1968 */       .save(this.output);
/*      */     
/* 1970 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.SEA_LANTERN)
/* 1971 */       .define(Character.valueOf('S'), Items.PRISMARINE_SHARD)
/* 1972 */       .define(Character.valueOf('C'), Items.PRISMARINE_CRYSTALS)
/* 1973 */       .pattern("SCS")
/* 1974 */       .pattern("CCC")
/* 1975 */       .pattern("SCS")
/* 1976 */       .unlockedBy("has_prismarine_crystals", has(Items.PRISMARINE_CRYSTALS))
/* 1977 */       .save(this.output);
/*      */     
/* 1979 */     shaped(RecipeCategory.TOOLS, Items.SHEARS)
/* 1980 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/* 1981 */       .pattern(" #")
/* 1982 */       .pattern("# ")
/* 1983 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1984 */       .save(this.output);
/*      */     
/* 1986 */     shaped(RecipeCategory.COMBAT, Items.SHIELD)
/* 1987 */       .define(Character.valueOf('W'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 1988 */       .define(Character.valueOf('o'), Items.IRON_INGOT)
/* 1989 */       .pattern("WoW")
/* 1990 */       .pattern("WWW")
/* 1991 */       .pattern(" W ")
/* 1992 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 1993 */       .save(this.output);
/*      */     
/* 1995 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.SLIME_BALL, RecipeCategory.REDSTONE, Items.SLIME_BLOCK);
/*      */     
/* 1997 */     cut(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_RED_SANDSTONE, Blocks.RED_SANDSTONE);
/* 1998 */     cut(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_SANDSTONE, Blocks.SANDSTONE);
/*      */     
/* 2000 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.SNOW_BLOCK, Items.SNOWBALL);
/*      */     
/* 2002 */     shaped(RecipeCategory.DECORATIONS, Blocks.SNOW, 6)
/* 2003 */       .define(Character.valueOf('#'), Blocks.SNOW_BLOCK)
/* 2004 */       .pattern("###")
/* 2005 */       .unlockedBy("has_snowball", has(Items.SNOWBALL))
/* 2006 */       .save(this.output);
/*      */     
/* 2008 */     shaped(RecipeCategory.DECORATIONS, Blocks.SOUL_CAMPFIRE)
/* 2009 */       .define(Character.valueOf('L'), ItemTags.LOGS)
/* 2010 */       .define(Character.valueOf('S'), Items.STICK)
/* 2011 */       .define(Character.valueOf('#'), ItemTags.SOUL_FIRE_BASE_BLOCKS)
/* 2012 */       .pattern(" S ")
/* 2013 */       .pattern("S#S")
/* 2014 */       .pattern("LLL")
/* 2015 */       .unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
/* 2016 */       .save(this.output);
/*      */     
/* 2018 */     shaped(RecipeCategory.BREWING, Items.GLISTERING_MELON_SLICE)
/* 2019 */       .define(Character.valueOf('#'), Items.GOLD_NUGGET)
/* 2020 */       .define(Character.valueOf('X'), Items.MELON_SLICE)
/* 2021 */       .pattern("###")
/* 2022 */       .pattern("#X#")
/* 2023 */       .pattern("###")
/* 2024 */       .unlockedBy("has_melon", has(Items.MELON_SLICE))
/* 2025 */       .save(this.output);
/*      */     
/* 2027 */     shaped(RecipeCategory.COMBAT, Items.SPECTRAL_ARROW, 2)
/* 2028 */       .define(Character.valueOf('#'), Items.GLOWSTONE_DUST)
/* 2029 */       .define(Character.valueOf('X'), Items.ARROW)
/* 2030 */       .pattern(" # ")
/* 2031 */       .pattern("#X#")
/* 2032 */       .pattern(" # ")
/* 2033 */       .unlockedBy("has_glowstone_dust", has(Items.GLOWSTONE_DUST))
/* 2034 */       .save(this.output);
/*      */     
/* 2036 */     shaped(RecipeCategory.TOOLS, Items.SPYGLASS)
/* 2037 */       .define(Character.valueOf('#'), Items.AMETHYST_SHARD)
/* 2038 */       .define(Character.valueOf('X'), Items.COPPER_INGOT)
/* 2039 */       .pattern(" # ")
/* 2040 */       .pattern(" X ")
/* 2041 */       .pattern(" X ")
/* 2042 */       .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
/* 2043 */       .save(this.output);
/*      */     
/* 2045 */     shaped(RecipeCategory.MISC, Items.STICK, 4)
/* 2046 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2047 */       .pattern("#")
/* 2048 */       .pattern("#")
/* 2049 */       .group("sticks")
/* 2050 */       .unlockedBy("has_planks", has(ItemTags.PLANKS))
/* 2051 */       .save(this.output);
/*      */     
/* 2053 */     shaped(RecipeCategory.MISC, Items.STICK, 1)
/* 2054 */       .define(Character.valueOf('#'), Blocks.BAMBOO)
/* 2055 */       .pattern("#")
/* 2056 */       .pattern("#")
/* 2057 */       .group("sticks")
/* 2058 */       .unlockedBy("has_bamboo", has(Blocks.BAMBOO))
/* 2059 */       .save(this.output, "stick_from_bamboo_item");
/*      */     
/* 2061 */     shaped(RecipeCategory.REDSTONE, Blocks.STICKY_PISTON)
/* 2062 */       .define(Character.valueOf('P'), Blocks.PISTON)
/* 2063 */       .define(Character.valueOf('S'), Items.SLIME_BALL)
/* 2064 */       .pattern("S")
/* 2065 */       .pattern("P")
/* 2066 */       .unlockedBy("has_slime_ball", has(Items.SLIME_BALL))
/* 2067 */       .save(this.output);
/*      */     
/* 2069 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICKS, 4)
/* 2070 */       .define(Character.valueOf('#'), Blocks.STONE)
/* 2071 */       .pattern("##")
/* 2072 */       .pattern("##")
/* 2073 */       .unlockedBy("has_stone", has(Blocks.STONE))
/* 2074 */       .save(this.output);
/*      */     
/* 2076 */     shaped(RecipeCategory.TOOLS, Items.STONE_AXE)
/* 2077 */       .define(Character.valueOf('#'), Items.STICK)
/* 2078 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 2079 */       .pattern("XX")
/* 2080 */       .pattern("X#")
/* 2081 */       .pattern(" #")
/* 2082 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 2083 */       .save(this.output);
/*      */     
/* 2085 */     slabBuilder(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICK_SLAB, Ingredient.of(Blocks.STONE_BRICKS))
/* 2086 */       .unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
/* 2087 */       .save(this.output);
/*      */     
/* 2089 */     stairBuilder(Blocks.STONE_BRICK_STAIRS, Ingredient.of(Blocks.STONE_BRICKS))
/* 2090 */       .unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
/* 2091 */       .save(this.output);
/*      */     
/* 2093 */     shaped(RecipeCategory.TOOLS, Items.STONE_HOE)
/* 2094 */       .define(Character.valueOf('#'), Items.STICK)
/* 2095 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 2096 */       .pattern("XX")
/* 2097 */       .pattern(" #")
/* 2098 */       .pattern(" #")
/* 2099 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 2100 */       .save(this.output);
/*      */     
/* 2102 */     shaped(RecipeCategory.TOOLS, Items.STONE_PICKAXE)
/* 2103 */       .define(Character.valueOf('#'), Items.STICK)
/* 2104 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 2105 */       .pattern("XXX")
/* 2106 */       .pattern(" # ")
/* 2107 */       .pattern(" # ")
/* 2108 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 2109 */       .save(this.output);
/*      */     
/* 2111 */     shaped(RecipeCategory.TOOLS, Items.STONE_SHOVEL)
/* 2112 */       .define(Character.valueOf('#'), Items.STICK)
/* 2113 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 2114 */       .pattern("X")
/* 2115 */       .pattern("#")
/* 2116 */       .pattern("#")
/* 2117 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 2118 */       .save(this.output);
/*      */     
/* 2120 */     slab(RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_STONE_SLAB, Blocks.SMOOTH_STONE);
/*      */     
/* 2122 */     shaped(RecipeCategory.COMBAT, Items.STONE_SWORD)
/* 2123 */       .define(Character.valueOf('#'), Items.STICK)
/* 2124 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 2125 */       .pattern("X")
/* 2126 */       .pattern("X")
/* 2127 */       .pattern("#")
/* 2128 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 2129 */       .save(this.output);
/*      */     
/* 2131 */     shaped(RecipeCategory.COMBAT, Items.STONE_SPEAR)
/* 2132 */       .define(Character.valueOf('#'), Items.STICK)
/* 2133 */       .define(Character.valueOf('X'), ItemTags.STONE_TOOL_MATERIALS)
/* 2134 */       .pattern("  X")
/* 2135 */       .pattern(" # ")
/* 2136 */       .pattern("#  ")
/* 2137 */       .unlockedBy("has_cobblestone", has(ItemTags.STONE_TOOL_MATERIALS))
/* 2138 */       .save(this.output);
/*      */     
/* 2140 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.WHITE_WOOL)
/* 2141 */       .define(Character.valueOf('#'), Items.STRING)
/* 2142 */       .pattern("##")
/* 2143 */       .pattern("##")
/* 2144 */       .unlockedBy("has_string", has(Items.STRING))
/* 2145 */       .save(this.output, getConversionRecipeName(Blocks.WHITE_WOOL, Items.STRING));
/*      */     
/* 2147 */     oneToOneConversionRecipe(Items.SUGAR, Blocks.SUGAR_CANE, "sugar");
/*      */     
/* 2149 */     shapeless(RecipeCategory.MISC, Items.SUGAR, 3)
/* 2150 */       .requires(Items.HONEY_BOTTLE)
/* 2151 */       .group("sugar")
/* 2152 */       .unlockedBy("has_honey_bottle", has(Items.HONEY_BOTTLE))
/* 2153 */       .save(this.output, getConversionRecipeName(Items.SUGAR, Items.HONEY_BOTTLE));
/*      */     
/* 2155 */     shaped(RecipeCategory.REDSTONE, Blocks.TARGET)
/* 2156 */       .define(Character.valueOf('H'), Items.HAY_BLOCK)
/* 2157 */       .define(Character.valueOf('R'), Items.REDSTONE)
/* 2158 */       .pattern(" R ")
/* 2159 */       .pattern("RHR")
/* 2160 */       .pattern(" R ")
/* 2161 */       .unlockedBy("has_redstone", has(Items.REDSTONE))
/* 2162 */       .unlockedBy("has_hay_block", has(Blocks.HAY_BLOCK))
/* 2163 */       .save(this.output);
/*      */     
/* 2165 */     shaped(RecipeCategory.REDSTONE, Blocks.TNT)
/* 2166 */       .define(Character.valueOf('#'), Ingredient.of(new ItemLike[] { Blocks.SAND, Blocks.RED_SAND
/* 2167 */           })).define(Character.valueOf('X'), Items.GUNPOWDER)
/* 2168 */       .pattern("X#X")
/* 2169 */       .pattern("#X#")
/* 2170 */       .pattern("X#X")
/* 2171 */       .unlockedBy("has_gunpowder", has(Items.GUNPOWDER))
/* 2172 */       .save(this.output);
/*      */     
/* 2174 */     shapeless(RecipeCategory.TRANSPORTATION, Items.TNT_MINECART)
/* 2175 */       .requires(Blocks.TNT)
/* 2176 */       .requires(Items.MINECART)
/* 2177 */       .unlockedBy("has_minecart", has(Items.MINECART))
/* 2178 */       .save(this.output);
/*      */     
/* 2180 */     shaped(RecipeCategory.DECORATIONS, Blocks.TORCH, 4)
/* 2181 */       .define(Character.valueOf('#'), Items.STICK)
/* 2182 */       .define(Character.valueOf('X'), Ingredient.of(new ItemLike[] { Items.COAL, Items.CHARCOAL
/* 2183 */           })).pattern("X")
/* 2184 */       .pattern("#")
/* 2185 */       .unlockedBy("has_stone_pickaxe", has(Items.STONE_PICKAXE))
/* 2186 */       .save(this.output);
/*      */     
/* 2188 */     shaped(RecipeCategory.DECORATIONS, Blocks.SOUL_TORCH, 4)
/* 2189 */       .define(Character.valueOf('X'), Ingredient.of(new ItemLike[] { Items.COAL, Items.CHARCOAL
/* 2190 */           })).define(Character.valueOf('#'), Items.STICK)
/* 2191 */       .define(Character.valueOf('S'), ItemTags.SOUL_FIRE_BASE_BLOCKS)
/* 2192 */       .pattern("X")
/* 2193 */       .pattern("#")
/* 2194 */       .pattern("S")
/* 2195 */       .unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
/* 2196 */       .save(this.output);
/*      */     
/* 2198 */     shaped(RecipeCategory.DECORATIONS, Blocks.COPPER_TORCH, 4)
/* 2199 */       .define(Character.valueOf('X'), Ingredient.of(new ItemLike[] { Items.COAL, Items.CHARCOAL
/* 2200 */           })).define(Character.valueOf('#'), Items.STICK)
/* 2201 */       .define(Character.valueOf('C'), Items.COPPER_NUGGET)
/* 2202 */       .pattern("C")
/* 2203 */       .pattern("X")
/* 2204 */       .pattern("#")
/* 2205 */       .unlockedBy("has_copper_nugget", has(Items.COPPER_NUGGET))
/* 2206 */       .save(this.output);
/*      */     
/* 2208 */     shaped(RecipeCategory.DECORATIONS, Blocks.LANTERN)
/* 2209 */       .define(Character.valueOf('#'), Items.TORCH)
/* 2210 */       .define(Character.valueOf('X'), Items.IRON_NUGGET)
/* 2211 */       .pattern("XXX")
/* 2212 */       .pattern("X#X")
/* 2213 */       .pattern("XXX")
/* 2214 */       .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
/* 2215 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 2216 */       .save(this.output);
/*      */     
/* 2218 */     shaped(RecipeCategory.DECORATIONS, Blocks.SOUL_LANTERN)
/* 2219 */       .define(Character.valueOf('#'), Items.SOUL_TORCH)
/* 2220 */       .define(Character.valueOf('X'), Items.IRON_NUGGET)
/* 2221 */       .pattern("XXX")
/* 2222 */       .pattern("X#X")
/* 2223 */       .pattern("XXX")
/* 2224 */       .unlockedBy("has_soul_torch", has(Items.SOUL_TORCH))
/* 2225 */       .save(this.output);
/*      */     
/* 2227 */     shaped(RecipeCategory.DECORATIONS, Blocks.COPPER_LANTERN.unaffected())
/* 2228 */       .define(Character.valueOf('#'), Items.COPPER_TORCH)
/* 2229 */       .define(Character.valueOf('X'), Items.COPPER_NUGGET)
/* 2230 */       .pattern("XXX")
/* 2231 */       .pattern("X#X")
/* 2232 */       .pattern("XXX")
/* 2233 */       .unlockedBy("has_copper_torch", has(Items.COPPER_TORCH))
/* 2234 */       .save(this.output);
/*      */     
/* 2236 */     shapeless(RecipeCategory.REDSTONE, Blocks.TRAPPED_CHEST)
/* 2237 */       .requires(Blocks.CHEST)
/* 2238 */       .requires(Blocks.TRIPWIRE_HOOK)
/* 2239 */       .unlockedBy("has_tripwire_hook", has(Blocks.TRIPWIRE_HOOK))
/* 2240 */       .save(this.output);
/*      */     
/* 2242 */     shaped(RecipeCategory.REDSTONE, Blocks.TRIPWIRE_HOOK, 2)
/* 2243 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2244 */       .define(Character.valueOf('S'), Items.STICK)
/* 2245 */       .define(Character.valueOf('I'), Items.IRON_INGOT)
/* 2246 */       .pattern("I")
/* 2247 */       .pattern("S")
/* 2248 */       .pattern("#")
/* 2249 */       .unlockedBy("has_string", has(Items.STRING))
/* 2250 */       .save(this.output);
/*      */     
/* 2252 */     shaped(RecipeCategory.COMBAT, Items.TURTLE_HELMET)
/* 2253 */       .define(Character.valueOf('X'), Items.TURTLE_SCUTE)
/* 2254 */       .pattern("XXX")
/* 2255 */       .pattern("X X")
/* 2256 */       .unlockedBy("has_turtle_scute", has(Items.TURTLE_SCUTE))
/* 2257 */       .save(this.output);
/*      */     
/* 2259 */     shaped(RecipeCategory.COMBAT, Items.WOLF_ARMOR)
/* 2260 */       .define(Character.valueOf('X'), Items.ARMADILLO_SCUTE)
/* 2261 */       .pattern("X  ")
/* 2262 */       .pattern("XXX")
/* 2263 */       .pattern("X X")
/* 2264 */       .unlockedBy("has_armadillo_scute", has(Items.ARMADILLO_SCUTE))
/* 2265 */       .save(this.output);
/*      */     
/* 2267 */     shapeless(RecipeCategory.MISC, Items.WHEAT, 9)
/* 2268 */       .requires(Blocks.HAY_BLOCK)
/* 2269 */       .unlockedBy("has_hay_block", has(Blocks.HAY_BLOCK))
/* 2270 */       .save(this.output);
/*      */     
/* 2272 */     shapeless(RecipeCategory.MISC, Items.WHITE_DYE)
/* 2273 */       .requires(Items.BONE_MEAL)
/* 2274 */       .group("white_dye")
/* 2275 */       .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
/* 2276 */       .save(this.output);
/*      */     
/* 2278 */     oneToOneConversionRecipe(Items.WHITE_DYE, Blocks.LILY_OF_THE_VALLEY, "white_dye");
/*      */     
/* 2280 */     shaped(RecipeCategory.TOOLS, Items.WOODEN_AXE)
/* 2281 */       .define(Character.valueOf('#'), Items.STICK)
/* 2282 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2283 */       .pattern("XX")
/* 2284 */       .pattern("X#")
/* 2285 */       .pattern(" #")
/* 2286 */       .unlockedBy("has_stick", has(Items.STICK))
/* 2287 */       .save(this.output);
/*      */     
/* 2289 */     shaped(RecipeCategory.TOOLS, Items.WOODEN_HOE)
/* 2290 */       .define(Character.valueOf('#'), Items.STICK)
/* 2291 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2292 */       .pattern("XX")
/* 2293 */       .pattern(" #")
/* 2294 */       .pattern(" #")
/* 2295 */       .unlockedBy("has_stick", has(Items.STICK))
/* 2296 */       .save(this.output);
/*      */     
/* 2298 */     shaped(RecipeCategory.TOOLS, Items.WOODEN_PICKAXE)
/* 2299 */       .define(Character.valueOf('#'), Items.STICK)
/* 2300 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2301 */       .pattern("XXX")
/* 2302 */       .pattern(" # ")
/* 2303 */       .pattern(" # ")
/* 2304 */       .unlockedBy("has_stick", has(Items.STICK))
/* 2305 */       .save(this.output);
/*      */     
/* 2307 */     shaped(RecipeCategory.TOOLS, Items.WOODEN_SHOVEL)
/* 2308 */       .define(Character.valueOf('#'), Items.STICK)
/* 2309 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2310 */       .pattern("X")
/* 2311 */       .pattern("#")
/* 2312 */       .pattern("#")
/* 2313 */       .unlockedBy("has_stick", has(Items.STICK))
/* 2314 */       .save(this.output);
/*      */     
/* 2316 */     shaped(RecipeCategory.COMBAT, Items.WOODEN_SWORD)
/* 2317 */       .define(Character.valueOf('#'), Items.STICK)
/* 2318 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2319 */       .pattern("X")
/* 2320 */       .pattern("X")
/* 2321 */       .pattern("#")
/* 2322 */       .unlockedBy("has_stick", has(Items.STICK))
/* 2323 */       .save(this.output);
/*      */     
/* 2325 */     shaped(RecipeCategory.COMBAT, Items.WOODEN_SPEAR)
/* 2326 */       .define(Character.valueOf('#'), Items.STICK)
/* 2327 */       .define(Character.valueOf('X'), ItemTags.WOODEN_TOOL_MATERIALS)
/* 2328 */       .pattern("  X")
/* 2329 */       .pattern(" # ")
/* 2330 */       .pattern("#  ")
/* 2331 */       .unlockedBy("has_stick", has(Items.STICK))
/* 2332 */       .save(this.output);
/*      */     
/* 2334 */     shapeless(RecipeCategory.MISC, Items.WRITABLE_BOOK)
/* 2335 */       .requires(Items.BOOK)
/* 2336 */       .requires(Items.INK_SAC)
/* 2337 */       .requires(Items.FEATHER)
/* 2338 */       .unlockedBy("has_book", has(Items.BOOK))
/* 2339 */       .save(this.output);
/*      */     
/* 2341 */     oneToOneConversionRecipe(Items.YELLOW_DYE, Blocks.DANDELION, "yellow_dye");
/* 2342 */     oneToOneConversionRecipe(Items.YELLOW_DYE, Blocks.SUNFLOWER, "yellow_dye", 2);
/* 2343 */     oneToOneConversionRecipe(Items.YELLOW_DYE, Blocks.WILDFLOWERS, "yellow_dye");
/*      */     
/* 2345 */     nineBlockStorageRecipes(RecipeCategory.FOOD, Items.DRIED_KELP, RecipeCategory.BUILDING_BLOCKS, Items.DRIED_KELP_BLOCK);
/*      */     
/* 2347 */     shaped(RecipeCategory.MISC, Blocks.CONDUIT)
/* 2348 */       .define(Character.valueOf('#'), Items.NAUTILUS_SHELL)
/* 2349 */       .define(Character.valueOf('X'), Items.HEART_OF_THE_SEA)
/* 2350 */       .pattern("###")
/* 2351 */       .pattern("#X#")
/* 2352 */       .pattern("###")
/* 2353 */       .unlockedBy("has_nautilus_core", has(Items.HEART_OF_THE_SEA))
/* 2354 */       .unlockedBy("has_nautilus_shell", has(Items.NAUTILUS_SHELL))
/* 2355 */       .save(this.output);
/*      */     
/* 2357 */     wall(RecipeCategory.DECORATIONS, Blocks.RED_SANDSTONE_WALL, Blocks.RED_SANDSTONE);
/* 2358 */     wall(RecipeCategory.DECORATIONS, Blocks.STONE_BRICK_WALL, Blocks.STONE_BRICKS);
/* 2359 */     wall(RecipeCategory.DECORATIONS, Blocks.SANDSTONE_WALL, Blocks.SANDSTONE);
/*      */     
/* 2361 */     shapeless(RecipeCategory.MISC, Items.FIELD_MASONED_BANNER_PATTERN)
/* 2362 */       .requires(Items.PAPER)
/* 2363 */       .requires(Blocks.BRICKS)
/* 2364 */       .unlockedBy("has_bricks", has(Blocks.BRICKS))
/* 2365 */       .save(this.output);
/*      */     
/* 2367 */     shapeless(RecipeCategory.MISC, Items.BORDURE_INDENTED_BANNER_PATTERN)
/* 2368 */       .requires(Items.PAPER)
/* 2369 */       .requires(Blocks.VINE)
/* 2370 */       .unlockedBy("has_vines", has(Blocks.VINE))
/* 2371 */       .save(this.output);
/*      */     
/* 2373 */     shapeless(RecipeCategory.MISC, Items.CREEPER_BANNER_PATTERN)
/* 2374 */       .requires(Items.PAPER)
/* 2375 */       .requires(Items.CREEPER_HEAD)
/* 2376 */       .unlockedBy("has_creeper_head", has(Items.CREEPER_HEAD))
/* 2377 */       .save(this.output);
/*      */     
/* 2379 */     shapeless(RecipeCategory.MISC, Items.SKULL_BANNER_PATTERN)
/* 2380 */       .requires(Items.PAPER)
/* 2381 */       .requires(Items.WITHER_SKELETON_SKULL)
/* 2382 */       .unlockedBy("has_wither_skeleton_skull", has(Items.WITHER_SKELETON_SKULL))
/* 2383 */       .save(this.output);
/*      */     
/* 2385 */     shapeless(RecipeCategory.MISC, Items.FLOWER_BANNER_PATTERN)
/* 2386 */       .requires(Items.PAPER)
/* 2387 */       .requires(Blocks.OXEYE_DAISY)
/* 2388 */       .unlockedBy("has_oxeye_daisy", has(Blocks.OXEYE_DAISY))
/* 2389 */       .save(this.output);
/*      */     
/* 2391 */     shapeless(RecipeCategory.MISC, Items.MOJANG_BANNER_PATTERN)
/* 2392 */       .requires(Items.PAPER)
/* 2393 */       .requires(Items.ENCHANTED_GOLDEN_APPLE)
/* 2394 */       .unlockedBy("has_enchanted_golden_apple", has(Items.ENCHANTED_GOLDEN_APPLE))
/* 2395 */       .save(this.output);
/*      */     
/* 2397 */     shaped(RecipeCategory.DECORATIONS, Blocks.SCAFFOLDING, 6)
/* 2398 */       .define(Character.valueOf('~'), Items.STRING)
/* 2399 */       .define(Character.valueOf('I'), Blocks.BAMBOO)
/* 2400 */       .pattern("I~I")
/* 2401 */       .pattern("I I")
/* 2402 */       .pattern("I I")
/* 2403 */       .unlockedBy("has_bamboo", has(Blocks.BAMBOO))
/* 2404 */       .save(this.output);
/*      */     
/* 2406 */     shaped(RecipeCategory.DECORATIONS, Blocks.GRINDSTONE)
/* 2407 */       .define(Character.valueOf('I'), Items.STICK)
/* 2408 */       .define(Character.valueOf('-'), Blocks.STONE_SLAB)
/* 2409 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2410 */       .pattern("I-I")
/* 2411 */       .pattern("# #")
/* 2412 */       .unlockedBy("has_stone_slab", has(Blocks.STONE_SLAB))
/* 2413 */       .save(this.output);
/*      */     
/* 2415 */     shaped(RecipeCategory.DECORATIONS, Blocks.BLAST_FURNACE)
/* 2416 */       .define(Character.valueOf('#'), Blocks.SMOOTH_STONE)
/* 2417 */       .define(Character.valueOf('X'), Blocks.FURNACE)
/* 2418 */       .define(Character.valueOf('I'), Items.IRON_INGOT)
/* 2419 */       .pattern("III")
/* 2420 */       .pattern("IXI")
/* 2421 */       .pattern("###")
/* 2422 */       .unlockedBy("has_smooth_stone", has(Blocks.SMOOTH_STONE))
/* 2423 */       .save(this.output);
/*      */     
/* 2425 */     shaped(RecipeCategory.DECORATIONS, Blocks.SMOKER)
/* 2426 */       .define(Character.valueOf('#'), ItemTags.LOGS)
/* 2427 */       .define(Character.valueOf('X'), Blocks.FURNACE)
/* 2428 */       .pattern(" # ")
/* 2429 */       .pattern("#X#")
/* 2430 */       .pattern(" # ")
/* 2431 */       .unlockedBy("has_furnace", has(Blocks.FURNACE))
/* 2432 */       .save(this.output);
/*      */     
/* 2434 */     shaped(RecipeCategory.DECORATIONS, Blocks.CARTOGRAPHY_TABLE)
/* 2435 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2436 */       .define(Character.valueOf('@'), Items.PAPER)
/* 2437 */       .pattern("@@")
/* 2438 */       .pattern("##")
/* 2439 */       .pattern("##")
/* 2440 */       .unlockedBy("has_paper", has(Items.PAPER))
/* 2441 */       .save(this.output);
/*      */     
/* 2443 */     shaped(RecipeCategory.DECORATIONS, Blocks.SMITHING_TABLE)
/* 2444 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2445 */       .define(Character.valueOf('@'), Items.IRON_INGOT)
/* 2446 */       .pattern("@@")
/* 2447 */       .pattern("##")
/* 2448 */       .pattern("##")
/* 2449 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 2450 */       .save(this.output);
/*      */     
/* 2452 */     shaped(RecipeCategory.DECORATIONS, Blocks.FLETCHING_TABLE)
/* 2453 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 2454 */       .define(Character.valueOf('@'), Items.FLINT)
/* 2455 */       .pattern("@@")
/* 2456 */       .pattern("##")
/* 2457 */       .pattern("##")
/* 2458 */       .unlockedBy("has_flint", has(Items.FLINT))
/* 2459 */       .save(this.output);
/*      */     
/* 2461 */     shaped(RecipeCategory.DECORATIONS, Blocks.STONECUTTER)
/* 2462 */       .define(Character.valueOf('I'), Items.IRON_INGOT)
/* 2463 */       .define(Character.valueOf('#'), Blocks.STONE)
/* 2464 */       .pattern(" I ")
/* 2465 */       .pattern("###")
/* 2466 */       .unlockedBy("has_stone", has(Blocks.STONE))
/* 2467 */       .save(this.output);
/*      */     
/* 2469 */     shaped(RecipeCategory.DECORATIONS, Blocks.LODESTONE)
/* 2470 */       .define(Character.valueOf('S'), Items.CHISELED_STONE_BRICKS)
/* 2471 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/* 2472 */       .pattern("SSS")
/* 2473 */       .pattern("S#S")
/* 2474 */       .pattern("SSS")
/* 2475 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 2476 */       .unlockedBy("has_lodestone", has(Items.LODESTONE))
/* 2477 */       .save(this.output);
/*      */     
/* 2479 */     nineBlockStorageRecipesRecipesWithCustomUnpacking(RecipeCategory.MISC, Items.NETHERITE_INGOT, RecipeCategory.BUILDING_BLOCKS, Items.NETHERITE_BLOCK, "netherite_ingot_from_netherite_block", "netherite_ingot");
/*      */     
/* 2481 */     shapeless(RecipeCategory.MISC, Items.NETHERITE_INGOT)
/* 2482 */       .requires(Items.NETHERITE_SCRAP, 4)
/* 2483 */       .requires(Items.GOLD_INGOT, 4)
/* 2484 */       .group("netherite_ingot")
/* 2485 */       .unlockedBy("has_netherite_scrap", has(Items.NETHERITE_SCRAP))
/* 2486 */       .save(this.output);
/*      */     
/* 2488 */     shaped(RecipeCategory.DECORATIONS, Blocks.RESPAWN_ANCHOR)
/* 2489 */       .define(Character.valueOf('O'), Blocks.CRYING_OBSIDIAN)
/* 2490 */       .define(Character.valueOf('G'), Blocks.GLOWSTONE)
/* 2491 */       .pattern("OOO")
/* 2492 */       .pattern("GGG")
/* 2493 */       .pattern("OOO")
/* 2494 */       .unlockedBy("has_obsidian", has(Blocks.CRYING_OBSIDIAN))
/* 2495 */       .save(this.output);
/*      */     
/* 2497 */     shaped(RecipeCategory.DECORATIONS, Blocks.IRON_CHAIN)
/* 2498 */       .define(Character.valueOf('I'), Items.IRON_INGOT)
/* 2499 */       .define(Character.valueOf('N'), Items.IRON_NUGGET)
/* 2500 */       .pattern("N")
/* 2501 */       .pattern("I")
/* 2502 */       .pattern("N")
/* 2503 */       .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
/* 2504 */       .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
/* 2505 */       .save(this.output);
/*      */     
/* 2507 */     shaped(RecipeCategory.DECORATIONS, Blocks.COPPER_CHAIN.unaffected())
/* 2508 */       .define(Character.valueOf('I'), Items.COPPER_INGOT)
/* 2509 */       .define(Character.valueOf('N'), Items.COPPER_NUGGET)
/* 2510 */       .pattern("N")
/* 2511 */       .pattern("I")
/* 2512 */       .pattern("N")
/* 2513 */       .unlockedBy("has_copper_nugget", has(Items.COPPER_NUGGET))
/* 2514 */       .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
/* 2515 */       .save(this.output);
/*      */     
/* 2517 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.TINTED_GLASS, 2)
/* 2518 */       .define(Character.valueOf('G'), Blocks.GLASS)
/* 2519 */       .define(Character.valueOf('S'), Items.AMETHYST_SHARD)
/* 2520 */       .pattern(" S ")
/* 2521 */       .pattern("SGS")
/* 2522 */       .pattern(" S ")
/* 2523 */       .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
/* 2524 */       .save(this.output);
/*      */     
/* 2526 */     twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, Blocks.AMETHYST_BLOCK, Items.AMETHYST_SHARD);
/*      */     
/* 2528 */     shaped(RecipeCategory.TOOLS, Items.RECOVERY_COMPASS)
/* 2529 */       .define(Character.valueOf('C'), Items.COMPASS)
/* 2530 */       .define(Character.valueOf('S'), Items.ECHO_SHARD)
/* 2531 */       .pattern("SSS")
/* 2532 */       .pattern("SCS")
/* 2533 */       .pattern("SSS")
/* 2534 */       .unlockedBy("has_echo_shard", has(Items.ECHO_SHARD))
/* 2535 */       .save(this.output);
/*      */     
/* 2537 */     shaped(RecipeCategory.REDSTONE, Items.CALIBRATED_SCULK_SENSOR)
/* 2538 */       .define(Character.valueOf('#'), Items.AMETHYST_SHARD)
/* 2539 */       .define(Character.valueOf('X'), Items.SCULK_SENSOR)
/* 2540 */       .pattern(" # ")
/* 2541 */       .pattern("#X#")
/* 2542 */       .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
/* 2543 */       .save(this.output);
/*      */     
/* 2545 */     threeByThreePacker(RecipeCategory.MISC, Items.MUSIC_DISC_5, Items.DISC_FRAGMENT_5);
/*      */     
/* 2547 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.ArmorDyeRecipe::new)
/* 2548 */       .save(this.output, "armor_dye");
/*      */     
/* 2550 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.BannerDuplicateRecipe::new)
/* 2551 */       .save(this.output, "banner_duplicate");
/*      */     
/* 2553 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.BookCloningRecipe::new)
/* 2554 */       .save(this.output, "book_cloning");
/*      */     
/* 2556 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.FireworkRocketRecipe::new)
/* 2557 */       .save(this.output, "firework_rocket");
/*      */     
/* 2559 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.FireworkStarRecipe::new)
/* 2560 */       .save(this.output, "firework_star");
/*      */     
/* 2562 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.FireworkStarFadeRecipe::new)
/* 2563 */       .save(this.output, "firework_star_fade");
/*      */     
/* 2565 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.MapCloningRecipe::new)
/* 2566 */       .save(this.output, "map_cloning");
/*      */     
/* 2568 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.MapExtendingRecipe::new)
/* 2569 */       .save(this.output, "map_extending");
/*      */     
/* 2571 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.RepairItemRecipe::new)
/* 2572 */       .save(this.output, "repair_item");
/*      */     
/* 2574 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.ShieldDecorationRecipe::new)
/* 2575 */       .save(this.output, "shield_decoration");
/*      */     
/* 2577 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.TippedArrowRecipe::new)
/* 2578 */       .save(this.output, "tipped_arrow");
/*      */ 
/*      */     
/* 2581 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.POTATO), RecipeCategory.FOOD, Items.BAKED_POTATO, 0.35F, 200)
/* 2582 */       .unlockedBy("has_potato", has(Items.POTATO))
/* 2583 */       .save(this.output);
/*      */     
/* 2585 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.CLAY_BALL), RecipeCategory.MISC, Items.BRICK, 0.3F, 200)
/* 2586 */       .unlockedBy("has_clay_ball", has(Items.CLAY_BALL))
/* 2587 */       .save(this.output);
/*      */     
/* 2589 */     SimpleCookingRecipeBuilder.smelting(tag(ItemTags.LOGS_THAT_BURN), RecipeCategory.MISC, Items.CHARCOAL, 0.15F, 200)
/* 2590 */       .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
/* 2591 */       .save(this.output);
/*      */     
/* 2593 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.CHORUS_FRUIT), RecipeCategory.MISC, Items.POPPED_CHORUS_FRUIT, 0.1F, 200)
/* 2594 */       .unlockedBy("has_chorus_fruit", has(Items.CHORUS_FRUIT))
/* 2595 */       .save(this.output);
/*      */     
/* 2597 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.BEEF), RecipeCategory.FOOD, Items.COOKED_BEEF, 0.35F, 200)
/* 2598 */       .unlockedBy("has_beef", has(Items.BEEF))
/* 2599 */       .save(this.output);
/*      */     
/* 2601 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.CHICKEN), RecipeCategory.FOOD, Items.COOKED_CHICKEN, 0.35F, 200)
/* 2602 */       .unlockedBy("has_chicken", has(Items.CHICKEN))
/* 2603 */       .save(this.output);
/*      */     
/* 2605 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.COD), RecipeCategory.FOOD, Items.COOKED_COD, 0.35F, 200)
/* 2606 */       .unlockedBy("has_cod", has(Items.COD))
/* 2607 */       .save(this.output);
/*      */     
/* 2609 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.KELP), RecipeCategory.FOOD, Items.DRIED_KELP, 0.1F, 200)
/* 2610 */       .unlockedBy("has_kelp", has(Blocks.KELP))
/* 2611 */       .save(this.output, getSmeltingRecipeName(Items.DRIED_KELP));
/*      */     
/* 2613 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.SALMON), RecipeCategory.FOOD, Items.COOKED_SALMON, 0.35F, 200)
/* 2614 */       .unlockedBy("has_salmon", has(Items.SALMON))
/* 2615 */       .save(this.output);
/*      */     
/* 2617 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.MUTTON), RecipeCategory.FOOD, Items.COOKED_MUTTON, 0.35F, 200)
/* 2618 */       .unlockedBy("has_mutton", has(Items.MUTTON))
/* 2619 */       .save(this.output);
/*      */     
/* 2621 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.PORKCHOP), RecipeCategory.FOOD, Items.COOKED_PORKCHOP, 0.35F, 200)
/* 2622 */       .unlockedBy("has_porkchop", has(Items.PORKCHOP))
/* 2623 */       .save(this.output);
/*      */     
/* 2625 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.RABBIT), RecipeCategory.FOOD, Items.COOKED_RABBIT, 0.35F, 200)
/* 2626 */       .unlockedBy("has_rabbit", has(Items.RABBIT))
/* 2627 */       .save(this.output);
/*      */     
/* 2629 */     oreSmelting(COAL_SMELTABLES, RecipeCategory.MISC, Items.COAL, 0.1F, 200, "coal");
/* 2630 */     oreSmelting(IRON_SMELTABLES, RecipeCategory.MISC, Items.IRON_INGOT, 0.7F, 200, "iron_ingot");
/* 2631 */     oreSmelting(COPPER_SMELTABLES, RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 200, "copper_ingot");
/* 2632 */     oreSmelting(GOLD_SMELTABLES, RecipeCategory.MISC, Items.GOLD_INGOT, 1.0F, 200, "gold_ingot");
/* 2633 */     oreSmelting(DIAMOND_SMELTABLES, RecipeCategory.MISC, Items.DIAMOND, 1.0F, 200, "diamond");
/* 2634 */     oreSmelting(LAPIS_SMELTABLES, RecipeCategory.MISC, Items.LAPIS_LAZULI, 0.2F, 200, "lapis_lazuli");
/* 2635 */     oreSmelting(REDSTONE_SMELTABLES, RecipeCategory.REDSTONE, Items.REDSTONE, 0.7F, 200, "redstone");
/* 2636 */     oreSmelting(EMERALD_SMELTABLES, RecipeCategory.MISC, Items.EMERALD, 1.0F, 200, "emerald");
/*      */     
/* 2638 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.RAW_IRON, RecipeCategory.BUILDING_BLOCKS, Items.RAW_IRON_BLOCK);
/* 2639 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.RAW_COPPER, RecipeCategory.BUILDING_BLOCKS, Items.RAW_COPPER_BLOCK);
/* 2640 */     nineBlockStorageRecipes(RecipeCategory.MISC, Items.RAW_GOLD, RecipeCategory.BUILDING_BLOCKS, Items.RAW_GOLD_BLOCK);
/*      */     
/* 2642 */     SimpleCookingRecipeBuilder.smelting(tag(ItemTags.SMELTS_TO_GLASS), RecipeCategory.BUILDING_BLOCKS, Blocks.GLASS.asItem(), 0.1F, 200)
/* 2643 */       .unlockedBy("has_smelts_to_glass", has(ItemTags.SMELTS_TO_GLASS))
/* 2644 */       .save(this.output);
/*      */     
/* 2646 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.SEA_PICKLE), RecipeCategory.MISC, Items.LIME_DYE, 0.1F, 200)
/* 2647 */       .unlockedBy("has_sea_pickle", has(Blocks.SEA_PICKLE))
/* 2648 */       .save(this.output, getSmeltingRecipeName(Items.LIME_DYE));
/*      */     
/* 2650 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.CACTUS.asItem()), RecipeCategory.MISC, Items.GREEN_DYE, 1.0F, 200)
/* 2651 */       .unlockedBy("has_cactus", has(Blocks.CACTUS))
/* 2652 */       .save(this.output);
/*      */     
/* 2654 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_SPEAR, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, Items.GOLD_NUGGET, 0.1F, 200)
/* 2655 */       .unlockedBy("has_golden_pickaxe", has(Items.GOLDEN_PICKAXE))
/* 2656 */       .unlockedBy("has_golden_shovel", has(Items.GOLDEN_SHOVEL))
/* 2657 */       .unlockedBy("has_golden_axe", has(Items.GOLDEN_AXE))
/* 2658 */       .unlockedBy("has_golden_hoe", has(Items.GOLDEN_HOE))
/* 2659 */       .unlockedBy("has_golden_sword", has(Items.GOLDEN_SWORD))
/* 2660 */       .unlockedBy("has_golden_spear", has(Items.GOLDEN_SPEAR))
/* 2661 */       .unlockedBy("has_golden_helmet", has(Items.GOLDEN_HELMET))
/* 2662 */       .unlockedBy("has_golden_chestplate", has(Items.GOLDEN_CHESTPLATE))
/* 2663 */       .unlockedBy("has_golden_leggings", has(Items.GOLDEN_LEGGINGS))
/* 2664 */       .unlockedBy("has_golden_boots", has(Items.GOLDEN_BOOTS))
/* 2665 */       .unlockedBy("has_golden_horse_armor", has(Items.GOLDEN_HORSE_ARMOR))
/* 2666 */       .unlockedBy("has_golden_nautilus_armor", has(Items.GOLDEN_NAUTILUS_ARMOR))
/* 2667 */       .save(this.output, getSmeltingRecipeName(Items.GOLD_NUGGET));
/*      */     
/* 2669 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { Items.COPPER_PICKAXE, Items.COPPER_SHOVEL, Items.COPPER_AXE, Items.COPPER_HOE, Items.COPPER_SWORD, Items.COPPER_SPEAR, Items.COPPER_HELMET, Items.COPPER_CHESTPLATE, Items.COPPER_LEGGINGS, Items.COPPER_BOOTS, Items.COPPER_HORSE_ARMOR, Items.COPPER_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, Items.COPPER_NUGGET, 0.1F, 200)
/* 2670 */       .unlockedBy("has_copper_pickaxe", has(Items.COPPER_PICKAXE))
/* 2671 */       .unlockedBy("has_copper_shovel", has(Items.COPPER_SHOVEL))
/* 2672 */       .unlockedBy("has_copper_axe", has(Items.COPPER_AXE))
/* 2673 */       .unlockedBy("has_copper_hoe", has(Items.COPPER_HOE))
/* 2674 */       .unlockedBy("has_copper_sword", has(Items.COPPER_SWORD))
/* 2675 */       .unlockedBy("has_copper_spear", has(Items.COPPER_SPEAR))
/* 2676 */       .unlockedBy("has_copper_helmet", has(Items.COPPER_HELMET))
/* 2677 */       .unlockedBy("has_copper_chestplate", has(Items.COPPER_CHESTPLATE))
/* 2678 */       .unlockedBy("has_copper_leggings", has(Items.COPPER_LEGGINGS))
/* 2679 */       .unlockedBy("has_copper_boots", has(Items.COPPER_BOOTS))
/* 2680 */       .unlockedBy("has_copper_horse_armor", has(Items.COPPER_HORSE_ARMOR))
/* 2681 */       .unlockedBy("has_copper_nautilus_armor", has(Items.COPPER_NAUTILUS_ARMOR))
/* 2682 */       .save(this.output, getSmeltingRecipeName(Items.COPPER_NUGGET));
/*      */     
/* 2684 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[] { Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD, Items.IRON_SPEAR, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.IRON_HORSE_ARMOR, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS, Items.IRON_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, Items.IRON_NUGGET, 0.1F, 200)
/* 2685 */       .unlockedBy("has_iron_pickaxe", has(Items.IRON_PICKAXE))
/* 2686 */       .unlockedBy("has_iron_shovel", has(Items.IRON_SHOVEL))
/* 2687 */       .unlockedBy("has_iron_axe", has(Items.IRON_AXE))
/* 2688 */       .unlockedBy("has_iron_hoe", has(Items.IRON_HOE))
/* 2689 */       .unlockedBy("has_iron_sword", has(Items.IRON_SWORD))
/* 2690 */       .unlockedBy("has_iron_spear", has(Items.IRON_SPEAR))
/* 2691 */       .unlockedBy("has_iron_helmet", has(Items.IRON_HELMET))
/* 2692 */       .unlockedBy("has_iron_chestplate", has(Items.IRON_CHESTPLATE))
/* 2693 */       .unlockedBy("has_iron_leggings", has(Items.IRON_LEGGINGS))
/* 2694 */       .unlockedBy("has_iron_boots", has(Items.IRON_BOOTS))
/* 2695 */       .unlockedBy("has_iron_horse_armor", has(Items.IRON_HORSE_ARMOR))
/* 2696 */       .unlockedBy("has_chainmail_helmet", has(Items.CHAINMAIL_HELMET))
/* 2697 */       .unlockedBy("has_chainmail_chestplate", has(Items.CHAINMAIL_CHESTPLATE))
/* 2698 */       .unlockedBy("has_chainmail_leggings", has(Items.CHAINMAIL_LEGGINGS))
/* 2699 */       .unlockedBy("has_chainmail_boots", has(Items.CHAINMAIL_BOOTS))
/* 2700 */       .unlockedBy("has_iron_nautilus_armor", has(Items.IRON_NAUTILUS_ARMOR))
/* 2701 */       .save(this.output, getSmeltingRecipeName(Items.IRON_NUGGET));
/*      */     
/* 2703 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.CLAY), RecipeCategory.BUILDING_BLOCKS, Blocks.TERRACOTTA.asItem(), 0.35F, 200)
/* 2704 */       .unlockedBy("has_clay_block", has(Blocks.CLAY))
/* 2705 */       .save(this.output);
/*      */     
/* 2707 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.NETHERRACK), RecipeCategory.MISC, Items.NETHER_BRICK, 0.1F, 200)
/* 2708 */       .unlockedBy("has_netherrack", has(Blocks.NETHERRACK))
/* 2709 */       .save(this.output);
/*      */     
/* 2711 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.RESIN_CLUMP), RecipeCategory.MISC, Items.RESIN_BRICK, 0.1F, 200)
/* 2712 */       .unlockedBy("has_resin_clump", has(Blocks.RESIN_CLUMP))
/* 2713 */       .save(this.output);
/*      */     
/* 2715 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.NETHER_QUARTZ_ORE), RecipeCategory.MISC, Items.QUARTZ, 0.2F, 200)
/* 2716 */       .unlockedBy("has_nether_quartz_ore", has(Blocks.NETHER_QUARTZ_ORE))
/* 2717 */       .save(this.output);
/*      */     
/* 2719 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.WET_SPONGE), RecipeCategory.BUILDING_BLOCKS, Blocks.SPONGE.asItem(), 0.15F, 200)
/* 2720 */       .unlockedBy("has_wet_sponge", has(Blocks.WET_SPONGE))
/* 2721 */       .save(this.output);
/*      */     
/* 2723 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.COBBLESTONE), RecipeCategory.BUILDING_BLOCKS, Blocks.STONE.asItem(), 0.1F, 200)
/* 2724 */       .unlockedBy("has_cobblestone", has(Blocks.COBBLESTONE))
/* 2725 */       .save(this.output);
/*      */     
/* 2727 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.STONE), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_STONE.asItem(), 0.1F, 200)
/* 2728 */       .unlockedBy("has_stone", has(Blocks.STONE))
/* 2729 */       .save(this.output);
/*      */     
/* 2731 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.SANDSTONE), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_SANDSTONE.asItem(), 0.1F, 200)
/* 2732 */       .unlockedBy("has_sandstone", has(Blocks.SANDSTONE))
/* 2733 */       .save(this.output);
/*      */     
/* 2735 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.RED_SANDSTONE), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_RED_SANDSTONE.asItem(), 0.1F, 200)
/* 2736 */       .unlockedBy("has_red_sandstone", has(Blocks.RED_SANDSTONE))
/* 2737 */       .save(this.output);
/*      */     
/* 2739 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.QUARTZ_BLOCK), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_QUARTZ.asItem(), 0.1F, 200)
/* 2740 */       .unlockedBy("has_quartz_block", has(Blocks.QUARTZ_BLOCK))
/* 2741 */       .save(this.output);
/*      */     
/* 2743 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.STONE_BRICKS), RecipeCategory.BUILDING_BLOCKS, Blocks.CRACKED_STONE_BRICKS.asItem(), 0.1F, 200)
/* 2744 */       .unlockedBy("has_stone_bricks", has(Blocks.STONE_BRICKS))
/* 2745 */       .save(this.output);
/*      */     
/* 2747 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.BLACK_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BLACK_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2748 */       .unlockedBy("has_black_terracotta", has(Blocks.BLACK_TERRACOTTA))
/* 2749 */       .save(this.output);
/*      */     
/* 2751 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.BLUE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BLUE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2752 */       .unlockedBy("has_blue_terracotta", has(Blocks.BLUE_TERRACOTTA))
/* 2753 */       .save(this.output);
/*      */     
/* 2755 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.BROWN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.BROWN_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2756 */       .unlockedBy("has_brown_terracotta", has(Blocks.BROWN_TERRACOTTA))
/* 2757 */       .save(this.output);
/*      */     
/* 2759 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.CYAN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.CYAN_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2760 */       .unlockedBy("has_cyan_terracotta", has(Blocks.CYAN_TERRACOTTA))
/* 2761 */       .save(this.output);
/*      */     
/* 2763 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.GRAY_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.GRAY_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2764 */       .unlockedBy("has_gray_terracotta", has(Blocks.GRAY_TERRACOTTA))
/* 2765 */       .save(this.output);
/*      */     
/* 2767 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.GREEN_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.GREEN_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2768 */       .unlockedBy("has_green_terracotta", has(Blocks.GREEN_TERRACOTTA))
/* 2769 */       .save(this.output);
/*      */     
/* 2771 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.LIGHT_BLUE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2772 */       .unlockedBy("has_light_blue_terracotta", has(Blocks.LIGHT_BLUE_TERRACOTTA))
/* 2773 */       .save(this.output);
/*      */     
/* 2775 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.LIGHT_GRAY_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2776 */       .unlockedBy("has_light_gray_terracotta", has(Blocks.LIGHT_GRAY_TERRACOTTA))
/* 2777 */       .save(this.output);
/*      */     
/* 2779 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.LIME_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.LIME_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2780 */       .unlockedBy("has_lime_terracotta", has(Blocks.LIME_TERRACOTTA))
/* 2781 */       .save(this.output);
/*      */     
/* 2783 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.MAGENTA_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.MAGENTA_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2784 */       .unlockedBy("has_magenta_terracotta", has(Blocks.MAGENTA_TERRACOTTA))
/* 2785 */       .save(this.output);
/*      */     
/* 2787 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.ORANGE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.ORANGE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2788 */       .unlockedBy("has_orange_terracotta", has(Blocks.ORANGE_TERRACOTTA))
/* 2789 */       .save(this.output);
/*      */     
/* 2791 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.PINK_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.PINK_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2792 */       .unlockedBy("has_pink_terracotta", has(Blocks.PINK_TERRACOTTA))
/* 2793 */       .save(this.output);
/*      */     
/* 2795 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.PURPLE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.PURPLE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2796 */       .unlockedBy("has_purple_terracotta", has(Blocks.PURPLE_TERRACOTTA))
/* 2797 */       .save(this.output);
/*      */     
/* 2799 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.RED_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.RED_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2800 */       .unlockedBy("has_red_terracotta", has(Blocks.RED_TERRACOTTA))
/* 2801 */       .save(this.output);
/*      */     
/* 2803 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.WHITE_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.WHITE_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2804 */       .unlockedBy("has_white_terracotta", has(Blocks.WHITE_TERRACOTTA))
/* 2805 */       .save(this.output);
/*      */     
/* 2807 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.YELLOW_TERRACOTTA), RecipeCategory.DECORATIONS, Blocks.YELLOW_GLAZED_TERRACOTTA.asItem(), 0.1F, 200)
/* 2808 */       .unlockedBy("has_yellow_terracotta", has(Blocks.YELLOW_TERRACOTTA))
/* 2809 */       .save(this.output);
/*      */     
/* 2811 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.ANCIENT_DEBRIS), RecipeCategory.MISC, Items.NETHERITE_SCRAP, 2.0F, 200)
/* 2812 */       .unlockedBy("has_ancient_debris", has(Blocks.ANCIENT_DEBRIS))
/* 2813 */       .save(this.output);
/*      */     
/* 2815 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.BASALT), RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_BASALT, 0.1F, 200)
/* 2816 */       .unlockedBy("has_basalt", has(Blocks.BASALT))
/* 2817 */       .save(this.output);
/*      */     
/* 2819 */     SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.COBBLED_DEEPSLATE), RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE, 0.1F, 200)
/* 2820 */       .unlockedBy("has_cobbled_deepslate", has(Blocks.COBBLED_DEEPSLATE))
/* 2821 */       .save(this.output);
/*      */     
/* 2823 */     SimpleCookingRecipeBuilder.smelting(tag(ItemTags.LEAVES), RecipeCategory.MISC, Blocks.LEAF_LITTER, 0.1F, 200)
/* 2824 */       .unlockedBy("has_leaves", has(ItemTags.LEAVES))
/* 2825 */       .save(this.output);
/*      */ 
/*      */     
/* 2828 */     oreBlasting(COAL_SMELTABLES, RecipeCategory.MISC, Items.COAL, 0.1F, 100, "coal");
/* 2829 */     oreBlasting(IRON_SMELTABLES, RecipeCategory.MISC, Items.IRON_INGOT, 0.7F, 100, "iron_ingot");
/* 2830 */     oreBlasting(COPPER_SMELTABLES, RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 100, "copper_ingot");
/* 2831 */     oreBlasting(GOLD_SMELTABLES, RecipeCategory.MISC, Items.GOLD_INGOT, 1.0F, 100, "gold_ingot");
/* 2832 */     oreBlasting(DIAMOND_SMELTABLES, RecipeCategory.MISC, Items.DIAMOND, 1.0F, 100, "diamond");
/* 2833 */     oreBlasting(LAPIS_SMELTABLES, RecipeCategory.MISC, Items.LAPIS_LAZULI, 0.2F, 100, "lapis_lazuli");
/* 2834 */     oreBlasting(REDSTONE_SMELTABLES, RecipeCategory.REDSTONE, Items.REDSTONE, 0.7F, 100, "redstone");
/* 2835 */     oreBlasting(EMERALD_SMELTABLES, RecipeCategory.MISC, Items.EMERALD, 1.0F, 100, "emerald");
/*      */     
/* 2837 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(Blocks.NETHER_QUARTZ_ORE), RecipeCategory.MISC, Items.QUARTZ, 0.2F, 100)
/* 2838 */       .unlockedBy("has_nether_quartz_ore", has(Blocks.NETHER_QUARTZ_ORE))
/* 2839 */       .save(this.output, getBlastingRecipeName(Items.QUARTZ));
/*      */     
/* 2841 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_SPEAR, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, Items.GOLD_NUGGET, 0.1F, 100)
/* 2842 */       .unlockedBy("has_golden_pickaxe", has(Items.GOLDEN_PICKAXE))
/* 2843 */       .unlockedBy("has_golden_shovel", has(Items.GOLDEN_SHOVEL))
/* 2844 */       .unlockedBy("has_golden_axe", has(Items.GOLDEN_AXE))
/* 2845 */       .unlockedBy("has_golden_hoe", has(Items.GOLDEN_HOE))
/* 2846 */       .unlockedBy("has_golden_sword", has(Items.GOLDEN_SWORD))
/* 2847 */       .unlockedBy("has_golden_spear", has(Items.GOLDEN_SPEAR))
/* 2848 */       .unlockedBy("has_golden_helmet", has(Items.GOLDEN_HELMET))
/* 2849 */       .unlockedBy("has_golden_chestplate", has(Items.GOLDEN_CHESTPLATE))
/* 2850 */       .unlockedBy("has_golden_leggings", has(Items.GOLDEN_LEGGINGS))
/* 2851 */       .unlockedBy("has_golden_boots", has(Items.GOLDEN_BOOTS))
/* 2852 */       .unlockedBy("has_golden_horse_armor", has(Items.GOLDEN_HORSE_ARMOR))
/* 2853 */       .unlockedBy("has_golden_nautilus_armor", has(Items.GOLDEN_NAUTILUS_ARMOR))
/* 2854 */       .save(this.output, getBlastingRecipeName(Items.GOLD_NUGGET));
/*      */     
/* 2856 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { Items.COPPER_PICKAXE, Items.COPPER_SHOVEL, Items.COPPER_AXE, Items.COPPER_HOE, Items.COPPER_SWORD, Items.COPPER_SPEAR, Items.COPPER_HELMET, Items.COPPER_CHESTPLATE, Items.COPPER_LEGGINGS, Items.COPPER_BOOTS, Items.COPPER_HORSE_ARMOR, Items.COPPER_NAUTILUS_ARMOR }, ), RecipeCategory.MISC, Items.COPPER_NUGGET, 0.1F, 100)
/* 2857 */       .unlockedBy("has_copper_pickaxe", has(Items.COPPER_PICKAXE))
/* 2858 */       .unlockedBy("has_copper_shovel", has(Items.COPPER_SHOVEL))
/* 2859 */       .unlockedBy("has_copper_axe", has(Items.COPPER_AXE))
/* 2860 */       .unlockedBy("has_copper_hoe", has(Items.COPPER_HOE))
/* 2861 */       .unlockedBy("has_copper_sword", has(Items.COPPER_SWORD))
/* 2862 */       .unlockedBy("has_copper_spear", has(Items.COPPER_SPEAR))
/* 2863 */       .unlockedBy("has_copper_helmet", has(Items.COPPER_HELMET))
/* 2864 */       .unlockedBy("has_copper_chestplate", has(Items.COPPER_CHESTPLATE))
/* 2865 */       .unlockedBy("has_copper_leggings", has(Items.COPPER_LEGGINGS))
/* 2866 */       .unlockedBy("has_copper_boots", has(Items.COPPER_BOOTS))
/* 2867 */       .unlockedBy("has_copper_horse_armor", has(Items.COPPER_HORSE_ARMOR))
/* 2868 */       .unlockedBy("has_copper_nautilus_armor", has(Items.COPPER_NAUTILUS_ARMOR))
/* 2869 */       .save(this.output, getBlastingRecipeName(Items.COPPER_NUGGET));
/*      */     
/* 2871 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(new ItemLike[] { Items.IRON_PICKAXE, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_SWORD, Items.IRON_SPEAR, Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.IRON_HORSE_ARMOR, Items.IRON_NAUTILUS_ARMOR, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS }, ), RecipeCategory.MISC, Items.IRON_NUGGET, 0.1F, 100)
/* 2872 */       .unlockedBy("has_iron_pickaxe", has(Items.IRON_PICKAXE))
/* 2873 */       .unlockedBy("has_iron_shovel", has(Items.IRON_SHOVEL))
/* 2874 */       .unlockedBy("has_iron_axe", has(Items.IRON_AXE))
/* 2875 */       .unlockedBy("has_iron_hoe", has(Items.IRON_HOE))
/* 2876 */       .unlockedBy("has_iron_sword", has(Items.IRON_SWORD))
/* 2877 */       .unlockedBy("has_iron_spear", has(Items.IRON_SPEAR))
/* 2878 */       .unlockedBy("has_iron_helmet", has(Items.IRON_HELMET))
/* 2879 */       .unlockedBy("has_iron_chestplate", has(Items.IRON_CHESTPLATE))
/* 2880 */       .unlockedBy("has_iron_leggings", has(Items.IRON_LEGGINGS))
/* 2881 */       .unlockedBy("has_iron_boots", has(Items.IRON_BOOTS))
/* 2882 */       .unlockedBy("has_iron_horse_armor", has(Items.IRON_HORSE_ARMOR))
/* 2883 */       .unlockedBy("has_chainmail_helmet", has(Items.CHAINMAIL_HELMET))
/* 2884 */       .unlockedBy("has_chainmail_chestplate", has(Items.CHAINMAIL_CHESTPLATE))
/* 2885 */       .unlockedBy("has_chainmail_leggings", has(Items.CHAINMAIL_LEGGINGS))
/* 2886 */       .unlockedBy("has_chainmail_boots", has(Items.CHAINMAIL_BOOTS))
/* 2887 */       .unlockedBy("has_iron_nautilus_armor", has(Items.IRON_NAUTILUS_ARMOR))
/* 2888 */       .save(this.output, getBlastingRecipeName(Items.IRON_NUGGET));
/*      */     
/* 2890 */     SimpleCookingRecipeBuilder.blasting(Ingredient.of(Blocks.ANCIENT_DEBRIS), RecipeCategory.MISC, Items.NETHERITE_SCRAP, 2.0F, 100)
/* 2891 */       .unlockedBy("has_ancient_debris", has(Blocks.ANCIENT_DEBRIS))
/* 2892 */       .save(this.output, getBlastingRecipeName(Items.NETHERITE_SCRAP));
/*      */     
/* 2894 */     cookRecipes("smoking", RecipeSerializer.SMOKING_RECIPE, net.minecraft.world.item.crafting.SmokingRecipe::new, 100);
/* 2895 */     cookRecipes("campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, net.minecraft.world.item.crafting.CampfireCookingRecipe::new, 600);
/*      */ 
/*      */     
/* 2898 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_SLAB, Blocks.STONE, 2);
/* 2899 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_STAIRS, Blocks.STONE);
/* 2900 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICKS, Blocks.STONE);
/* 2901 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICK_SLAB, Blocks.STONE, 2);
/* 2902 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICK_STAIRS, Blocks.STONE);
/*      */     
/* 2904 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.STONE), RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_STONE_BRICKS)
/* 2905 */       .unlockedBy("has_stone", has(Blocks.STONE))
/* 2906 */       .save(this.output, "chiseled_stone_bricks_stone_from_stonecutting");
/*      */     
/* 2908 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.STONE), RecipeCategory.DECORATIONS, Blocks.STONE_BRICK_WALL)
/* 2909 */       .unlockedBy("has_stone", has(Blocks.STONE))
/* 2910 */       .save(this.output, "stone_brick_walls_from_stone_stonecutting");
/*      */     
/* 2912 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_SANDSTONE, Blocks.SANDSTONE);
/* 2913 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SANDSTONE_SLAB, Blocks.SANDSTONE, 2);
/* 2914 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_SANDSTONE_SLAB, Blocks.SANDSTONE, 2);
/*      */     
/* 2916 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_SANDSTONE_SLAB, Blocks.CUT_SANDSTONE, 2);
/*      */     
/* 2918 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SANDSTONE_STAIRS, Blocks.SANDSTONE);
/* 2919 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.SANDSTONE_WALL, Blocks.SANDSTONE);
/* 2920 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_SANDSTONE, Blocks.SANDSTONE);
/*      */     
/* 2922 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_RED_SANDSTONE, Blocks.RED_SANDSTONE);
/* 2923 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_SANDSTONE_SLAB, Blocks.RED_SANDSTONE, 2);
/* 2924 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_RED_SANDSTONE_SLAB, Blocks.RED_SANDSTONE, 2);
/*      */     
/* 2926 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_RED_SANDSTONE_SLAB, Blocks.CUT_RED_SANDSTONE, 2);
/*      */     
/* 2928 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_SANDSTONE_STAIRS, Blocks.RED_SANDSTONE);
/* 2929 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.RED_SANDSTONE_WALL, Blocks.RED_SANDSTONE);
/* 2930 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_RED_SANDSTONE, Blocks.RED_SANDSTONE);
/*      */     
/* 2932 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.QUARTZ_BLOCK), RecipeCategory.BUILDING_BLOCKS, Blocks.QUARTZ_SLAB, 2)
/* 2933 */       .unlockedBy("has_quartz_block", has(Blocks.QUARTZ_BLOCK))
/* 2934 */       .save(this.output, "quartz_slab_from_stonecutting");
/*      */     
/* 2936 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.QUARTZ_STAIRS, Blocks.QUARTZ_BLOCK);
/* 2937 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.QUARTZ_PILLAR, Blocks.QUARTZ_BLOCK);
/* 2938 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_QUARTZ_BLOCK, Blocks.QUARTZ_BLOCK);
/* 2939 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.QUARTZ_BRICKS, Blocks.QUARTZ_BLOCK);
/*      */     
/* 2941 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE_STAIRS, Blocks.COBBLESTONE);
/* 2942 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE_SLAB, Blocks.COBBLESTONE, 2);
/* 2943 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.COBBLESTONE_WALL, Blocks.COBBLESTONE);
/*      */     
/* 2945 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICK_SLAB, Blocks.STONE_BRICKS, 2);
/* 2946 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICKS);
/*      */     
/* 2948 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.STONE_BRICKS), RecipeCategory.DECORATIONS, Blocks.STONE_BRICK_WALL)
/* 2949 */       .unlockedBy("has_stone_bricks", has(Blocks.STONE_BRICKS))
/* 2950 */       .save(this.output, "stone_brick_wall_from_stone_bricks_stonecutting");
/*      */     
/* 2952 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_STONE_BRICKS, Blocks.STONE_BRICKS);
/* 2953 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.BRICK_SLAB, Blocks.BRICKS, 2);
/* 2954 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.BRICK_STAIRS, Blocks.BRICKS);
/* 2955 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.BRICK_WALL, Blocks.BRICKS);
/*      */     
/* 2957 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.MUD_BRICK_SLAB, Blocks.MUD_BRICKS, 2);
/* 2958 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.MUD_BRICK_STAIRS, Blocks.MUD_BRICKS);
/* 2959 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.MUD_BRICK_WALL, Blocks.MUD_BRICKS);
/*      */     
/* 2961 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.NETHER_BRICK_SLAB, Blocks.NETHER_BRICKS, 2);
/* 2962 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_BRICKS);
/* 2963 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.NETHER_BRICK_WALL, Blocks.NETHER_BRICKS);
/* 2964 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_NETHER_BRICKS, Blocks.NETHER_BRICKS);
/*      */     
/* 2966 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.RESIN_BRICK_SLAB, Blocks.RESIN_BRICKS, 2);
/* 2967 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.RESIN_BRICK_STAIRS, Blocks.RESIN_BRICKS);
/* 2968 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.RESIN_BRICK_WALL, Blocks.RESIN_BRICKS);
/* 2969 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_RESIN_BRICKS, Blocks.RESIN_BRICKS);
/*      */     
/* 2971 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_NETHER_BRICK_SLAB, Blocks.RED_NETHER_BRICKS, 2);
/* 2972 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_NETHER_BRICK_STAIRS, Blocks.RED_NETHER_BRICKS);
/* 2973 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.RED_NETHER_BRICK_WALL, Blocks.RED_NETHER_BRICKS);
/*      */     
/* 2975 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.PURPUR_SLAB, Blocks.PURPUR_BLOCK, 2);
/* 2976 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.PURPUR_STAIRS, Blocks.PURPUR_BLOCK);
/* 2977 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.PURPUR_PILLAR, Blocks.PURPUR_BLOCK);
/*      */     
/* 2979 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.PRISMARINE_SLAB, Blocks.PRISMARINE, 2);
/* 2980 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.PRISMARINE_STAIRS, Blocks.PRISMARINE);
/* 2981 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.PRISMARINE_WALL, Blocks.PRISMARINE);
/*      */     
/* 2983 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.PRISMARINE_BRICKS), RecipeCategory.BUILDING_BLOCKS, Blocks.PRISMARINE_BRICK_SLAB, 2)
/* 2984 */       .unlockedBy("has_prismarine_brick", has(Blocks.PRISMARINE_BRICKS))
/* 2985 */       .save(this.output, "prismarine_brick_slab_from_prismarine_stonecutting");
/*      */     
/* 2987 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.PRISMARINE_BRICKS), RecipeCategory.BUILDING_BLOCKS, Blocks.PRISMARINE_BRICK_STAIRS)
/* 2988 */       .unlockedBy("has_prismarine_brick", has(Blocks.PRISMARINE_BRICKS))
/* 2989 */       .save(this.output, "prismarine_brick_stairs_from_prismarine_stonecutting");
/*      */     
/* 2991 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DARK_PRISMARINE_SLAB, Blocks.DARK_PRISMARINE, 2);
/* 2992 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DARK_PRISMARINE_STAIRS, Blocks.DARK_PRISMARINE);
/*      */     
/* 2994 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.ANDESITE_SLAB, Blocks.ANDESITE, 2);
/* 2995 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.ANDESITE_STAIRS, Blocks.ANDESITE);
/*      */     
/* 2997 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.ANDESITE_WALL, Blocks.ANDESITE);
/* 2998 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_ANDESITE, Blocks.ANDESITE);
/*      */     
/* 3000 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_ANDESITE_SLAB, Blocks.ANDESITE, 2);
/* 3001 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_ANDESITE_STAIRS, Blocks.ANDESITE);
/*      */     
/* 3003 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_ANDESITE_SLAB, Blocks.POLISHED_ANDESITE, 2);
/* 3004 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_ANDESITE_STAIRS, Blocks.POLISHED_ANDESITE);
/*      */     
/* 3006 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BASALT, Blocks.BASALT);
/*      */     
/* 3008 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.GRANITE_SLAB, Blocks.GRANITE, 2);
/* 3009 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.GRANITE_STAIRS, Blocks.GRANITE);
/* 3010 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.GRANITE_WALL, Blocks.GRANITE);
/* 3011 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_GRANITE, Blocks.GRANITE);
/*      */     
/* 3013 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_GRANITE_SLAB, Blocks.GRANITE, 2);
/* 3014 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_GRANITE_STAIRS, Blocks.GRANITE);
/*      */     
/* 3016 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_GRANITE_SLAB, Blocks.POLISHED_GRANITE, 2);
/* 3017 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_GRANITE_STAIRS, Blocks.POLISHED_GRANITE);
/*      */     
/* 3019 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DIORITE_SLAB, Blocks.DIORITE, 2);
/* 3020 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DIORITE_STAIRS, Blocks.DIORITE);
/* 3021 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.DIORITE_WALL, Blocks.DIORITE);
/*      */     
/* 3023 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DIORITE, Blocks.DIORITE);
/* 3024 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DIORITE_SLAB, Blocks.DIORITE, 2);
/* 3025 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DIORITE_STAIRS, Blocks.DIORITE);
/*      */     
/* 3027 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DIORITE_SLAB, Blocks.POLISHED_DIORITE, 2);
/* 3028 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DIORITE_STAIRS, Blocks.POLISHED_DIORITE);
/*      */     
/* 3030 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.MOSSY_STONE_BRICKS), RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_STONE_BRICK_SLAB, 2)
/* 3031 */       .unlockedBy("has_mossy_stone_bricks", has(Blocks.MOSSY_STONE_BRICKS))
/* 3032 */       .save(this.output, "mossy_stone_brick_slab_from_mossy_stone_brick_stonecutting");
/*      */     
/* 3034 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.MOSSY_STONE_BRICKS), RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_STONE_BRICK_STAIRS)
/* 3035 */       .unlockedBy("has_mossy_stone_bricks", has(Blocks.MOSSY_STONE_BRICKS))
/* 3036 */       .save(this.output, "mossy_stone_brick_stairs_from_mossy_stone_brick_stonecutting");
/*      */     
/* 3038 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.MOSSY_STONE_BRICKS), RecipeCategory.DECORATIONS, Blocks.MOSSY_STONE_BRICK_WALL)
/* 3039 */       .unlockedBy("has_mossy_stone_bricks", has(Blocks.MOSSY_STONE_BRICKS))
/* 3040 */       .save(this.output, "mossy_stone_brick_wall_from_mossy_stone_brick_stonecutting");
/*      */     
/* 3042 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE, 2);
/* 3043 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE);
/* 3044 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.MOSSY_COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE);
/*      */     
/* 3046 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_SANDSTONE, 2);
/* 3047 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_SANDSTONE_STAIRS, Blocks.SMOOTH_SANDSTONE);
/*      */     
/* 3049 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.SMOOTH_RED_SANDSTONE, 2);
/* 3050 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_RED_SANDSTONE_STAIRS, Blocks.SMOOTH_RED_SANDSTONE);
/*      */     
/* 3052 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ, 2);
/* 3053 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_QUARTZ_STAIRS, Blocks.SMOOTH_QUARTZ);
/*      */     
/* 3055 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.END_STONE_BRICKS), RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE_BRICK_SLAB, 2)
/* 3056 */       .unlockedBy("has_end_stone_brick", has(Blocks.END_STONE_BRICKS))
/* 3057 */       .save(this.output, "end_stone_brick_slab_from_end_stone_brick_stonecutting");
/*      */     
/* 3059 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.END_STONE_BRICKS), RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE_BRICK_STAIRS)
/* 3060 */       .unlockedBy("has_end_stone_brick", has(Blocks.END_STONE_BRICKS))
/* 3061 */       .save(this.output, "end_stone_brick_stairs_from_end_stone_brick_stonecutting");
/*      */     
/* 3063 */     SingleItemRecipeBuilder.stonecutting(Ingredient.of(Blocks.END_STONE_BRICKS), RecipeCategory.DECORATIONS, Blocks.END_STONE_BRICK_WALL)
/* 3064 */       .unlockedBy("has_end_stone_brick", has(Blocks.END_STONE_BRICKS))
/* 3065 */       .save(this.output, "end_stone_brick_wall_from_end_stone_brick_stonecutting");
/*      */     
/* 3067 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE_BRICKS, Blocks.END_STONE);
/* 3068 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE_BRICK_SLAB, Blocks.END_STONE, 2);
/* 3069 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE_BRICK_STAIRS, Blocks.END_STONE);
/* 3070 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.END_STONE_BRICK_WALL, Blocks.END_STONE);
/*      */     
/* 3072 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.SMOOTH_STONE_SLAB, Blocks.SMOOTH_STONE, 2);
/*      */     
/* 3074 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.BLACKSTONE_SLAB, Blocks.BLACKSTONE, 2);
/* 3075 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.BLACKSTONE_STAIRS, Blocks.BLACKSTONE);
/* 3076 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.BLACKSTONE_WALL, Blocks.BLACKSTONE);
/* 3077 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE, Blocks.BLACKSTONE);
/* 3078 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_BLACKSTONE_WALL, Blocks.BLACKSTONE);
/* 3079 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.BLACKSTONE, 2);
/* 3080 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_STAIRS, Blocks.BLACKSTONE);
/* 3081 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_POLISHED_BLACKSTONE, Blocks.BLACKSTONE);
/* 3082 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.BLACKSTONE);
/* 3083 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, Blocks.BLACKSTONE, 2);
/* 3084 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.BLACKSTONE);
/* 3085 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_BLACKSTONE_BRICK_WALL, Blocks.BLACKSTONE);
/*      */     
/* 3087 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE, 2);
/* 3088 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_STAIRS, Blocks.POLISHED_BLACKSTONE);
/* 3089 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE);
/* 3090 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_BLACKSTONE_WALL, Blocks.POLISHED_BLACKSTONE);
/* 3091 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE, 2);
/* 3092 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE);
/* 3093 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_BLACKSTONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE);
/* 3094 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE);
/*      */     
/* 3096 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICKS, 2);
/* 3097 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/* 3098 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_BLACKSTONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*      */     
/* 3100 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_COPPER_SLAB, Blocks.CUT_COPPER, 2);
/* 3101 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_COPPER_STAIRS, Blocks.CUT_COPPER);
/* 3102 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER, 2);
/* 3103 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER);
/* 3104 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER, 2);
/* 3105 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER);
/* 3106 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER, 2);
/* 3107 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.OXIDIZED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER);
/* 3108 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_CUT_COPPER, 2);
/* 3109 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_CUT_COPPER);
/* 3110 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER, 2);
/* 3111 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER);
/* 3112 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER, 2);
/* 3113 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER);
/* 3114 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER, 2);
/* 3115 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER);
/* 3116 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_COPPER, Blocks.COPPER_BLOCK, 4);
/* 3117 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_COPPER_STAIRS, Blocks.COPPER_BLOCK, 4);
/* 3118 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CUT_COPPER_SLAB, Blocks.COPPER_BLOCK, 8);
/* 3119 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.EXPOSED_CUT_COPPER, Blocks.EXPOSED_COPPER, 4);
/* 3120 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.EXPOSED_COPPER, 4);
/* 3121 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.EXPOSED_COPPER, 8);
/* 3122 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WEATHERED_CUT_COPPER, Blocks.WEATHERED_COPPER, 4);
/* 3123 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.WEATHERED_COPPER, 4);
/* 3124 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.WEATHERED_COPPER, 8);
/* 3125 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.OXIDIZED_CUT_COPPER, Blocks.OXIDIZED_COPPER, 4);
/* 3126 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.OXIDIZED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_COPPER, 4);
/* 3127 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.OXIDIZED_COPPER, 8);
/* 3128 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_CUT_COPPER, Blocks.WAXED_COPPER_BLOCK, 4);
/* 3129 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_COPPER_BLOCK, 4);
/* 3130 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_COPPER_BLOCK, 8);
/* 3131 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_EXPOSED_COPPER, 4);
/* 3132 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_COPPER, 4);
/* 3133 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_COPPER, 8);
/* 3134 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_WEATHERED_COPPER, 4);
/* 3135 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_COPPER, 4);
/* 3136 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_COPPER, 8);
/* 3137 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_OXIDIZED_CUT_COPPER, Blocks.WAXED_OXIDIZED_COPPER, 4);
/* 3138 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_COPPER, 4);
/* 3139 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_COPPER, 8);
/*      */     
/* 3141 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.COBBLED_DEEPSLATE, 2);
/* 3142 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLED_DEEPSLATE_STAIRS, Blocks.COBBLED_DEEPSLATE);
/* 3143 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.COBBLED_DEEPSLATE_WALL, Blocks.COBBLED_DEEPSLATE);
/* 3144 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
/* 3145 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE);
/* 3146 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.COBBLED_DEEPSLATE, 2);
/* 3147 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE_STAIRS, Blocks.COBBLED_DEEPSLATE);
/* 3148 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_DEEPSLATE_WALL, Blocks.COBBLED_DEEPSLATE);
/* 3149 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICKS, Blocks.COBBLED_DEEPSLATE);
/* 3150 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.COBBLED_DEEPSLATE, 2);
/* 3151 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.COBBLED_DEEPSLATE);
/* 3152 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.DEEPSLATE_BRICK_WALL, Blocks.COBBLED_DEEPSLATE);
/* 3153 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, Blocks.COBBLED_DEEPSLATE);
/* 3154 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, Blocks.COBBLED_DEEPSLATE, 2);
/* 3155 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, Blocks.COBBLED_DEEPSLATE);
/* 3156 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.DEEPSLATE_TILE_WALL, Blocks.COBBLED_DEEPSLATE);
/*      */     
/* 3158 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE, 2);
/* 3159 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_DEEPSLATE_STAIRS, Blocks.POLISHED_DEEPSLATE);
/* 3160 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_DEEPSLATE_WALL, Blocks.POLISHED_DEEPSLATE);
/* 3161 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICKS, Blocks.POLISHED_DEEPSLATE);
/* 3162 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.POLISHED_DEEPSLATE, 2);
/* 3163 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.POLISHED_DEEPSLATE);
/* 3164 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.DEEPSLATE_BRICK_WALL, Blocks.POLISHED_DEEPSLATE);
/* 3165 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, Blocks.POLISHED_DEEPSLATE);
/* 3166 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, Blocks.POLISHED_DEEPSLATE, 2);
/* 3167 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, Blocks.POLISHED_DEEPSLATE);
/* 3168 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.DEEPSLATE_TILE_WALL, Blocks.POLISHED_DEEPSLATE);
/*      */     
/* 3170 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.DEEPSLATE_BRICKS, 2);
/* 3171 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.DEEPSLATE_BRICKS);
/* 3172 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.DEEPSLATE_BRICK_WALL, Blocks.DEEPSLATE_BRICKS);
/* 3173 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, Blocks.DEEPSLATE_BRICKS);
/* 3174 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, Blocks.DEEPSLATE_BRICKS, 2);
/* 3175 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, Blocks.DEEPSLATE_BRICKS);
/* 3176 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.DEEPSLATE_TILE_WALL, Blocks.DEEPSLATE_BRICKS);
/*      */     
/* 3178 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, Blocks.DEEPSLATE_TILES, 2);
/* 3179 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, Blocks.DEEPSLATE_TILES);
/* 3180 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.DEEPSLATE_TILE_WALL, Blocks.DEEPSLATE_TILES);
/*      */     
/* 3182 */     smithingTrims().forEach(trim -> trimSmithing(trim.template(), trim.patternId(), trim.recipeId()));
/*      */     
/* 3184 */     netheriteSmithing(Items.DIAMOND_CHESTPLATE, RecipeCategory.COMBAT, Items.NETHERITE_CHESTPLATE);
/* 3185 */     netheriteSmithing(Items.DIAMOND_LEGGINGS, RecipeCategory.COMBAT, Items.NETHERITE_LEGGINGS);
/* 3186 */     netheriteSmithing(Items.DIAMOND_HELMET, RecipeCategory.COMBAT, Items.NETHERITE_HELMET);
/* 3187 */     netheriteSmithing(Items.DIAMOND_BOOTS, RecipeCategory.COMBAT, Items.NETHERITE_BOOTS);
/* 3188 */     netheriteSmithing(Items.DIAMOND_NAUTILUS_ARMOR, RecipeCategory.COMBAT, Items.NETHERITE_NAUTILUS_ARMOR);
/* 3189 */     netheriteSmithing(Items.DIAMOND_HORSE_ARMOR, RecipeCategory.COMBAT, Items.NETHERITE_HORSE_ARMOR);
/* 3190 */     netheriteSmithing(Items.DIAMOND_SWORD, RecipeCategory.COMBAT, Items.NETHERITE_SWORD);
/* 3191 */     netheriteSmithing(Items.DIAMOND_SPEAR, RecipeCategory.COMBAT, Items.NETHERITE_SPEAR);
/* 3192 */     netheriteSmithing(Items.DIAMOND_AXE, RecipeCategory.TOOLS, Items.NETHERITE_AXE);
/* 3193 */     netheriteSmithing(Items.DIAMOND_PICKAXE, RecipeCategory.TOOLS, Items.NETHERITE_PICKAXE);
/* 3194 */     netheriteSmithing(Items.DIAMOND_HOE, RecipeCategory.TOOLS, Items.NETHERITE_HOE);
/* 3195 */     netheriteSmithing(Items.DIAMOND_SHOVEL, RecipeCategory.TOOLS, Items.NETHERITE_SHOVEL);
/*      */     
/* 3197 */     copySmithingTemplate(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.NETHERRACK);
/* 3198 */     copySmithingTemplate(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
/* 3199 */     copySmithingTemplate(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SANDSTONE);
/* 3200 */     copySmithingTemplate(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
/* 3201 */     copySmithingTemplate(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.MOSSY_COBBLESTONE);
/* 3202 */     copySmithingTemplate(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE);
/* 3203 */     copySmithingTemplate(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.END_STONE);
/* 3204 */     copySmithingTemplate(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
/* 3205 */     copySmithingTemplate(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.PRISMARINE);
/* 3206 */     copySmithingTemplate(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Items.BLACKSTONE);
/* 3207 */     copySmithingTemplate(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Items.NETHERRACK);
/* 3208 */     copySmithingTemplate(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.PURPUR_BLOCK);
/* 3209 */     copySmithingTemplate(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE);
/* 3210 */     copySmithingTemplate(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
/* 3211 */     copySmithingTemplate(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
/* 3212 */     copySmithingTemplate(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
/* 3213 */     copySmithingTemplate(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
/* 3214 */     copySmithingTemplate(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, Items.BREEZE_ROD);
/* 3215 */     copySmithingTemplate(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, Ingredient.of(new ItemLike[] { Items.COPPER_BLOCK, Items.WAXED_COPPER_BLOCK }));
/*      */     
/* 3217 */     threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, Blocks.BAMBOO_BLOCK, Items.BAMBOO);
/*      */     
/* 3219 */     planksFromLogs(Blocks.BAMBOO_PLANKS, ItemTags.BAMBOO_BLOCKS, 2);
/*      */     
/* 3221 */     mosaicBuilder(RecipeCategory.DECORATIONS, Blocks.BAMBOO_MOSAIC, Blocks.BAMBOO_SLAB);
/* 3222 */     woodenBoat(Items.BAMBOO_RAFT, Blocks.BAMBOO_PLANKS);
/* 3223 */     chestBoat(Items.BAMBOO_CHEST_RAFT, Items.BAMBOO_RAFT);
/*      */     
/* 3225 */     hangingSign(Items.OAK_HANGING_SIGN, Blocks.STRIPPED_OAK_LOG);
/* 3226 */     hangingSign(Items.SPRUCE_HANGING_SIGN, Blocks.STRIPPED_SPRUCE_LOG);
/* 3227 */     hangingSign(Items.BIRCH_HANGING_SIGN, Blocks.STRIPPED_BIRCH_LOG);
/* 3228 */     hangingSign(Items.JUNGLE_HANGING_SIGN, Blocks.STRIPPED_JUNGLE_LOG);
/* 3229 */     hangingSign(Items.ACACIA_HANGING_SIGN, Blocks.STRIPPED_ACACIA_LOG);
/* 3230 */     hangingSign(Items.CHERRY_HANGING_SIGN, Blocks.STRIPPED_CHERRY_LOG);
/* 3231 */     hangingSign(Items.DARK_OAK_HANGING_SIGN, Blocks.STRIPPED_DARK_OAK_LOG);
/* 3232 */     hangingSign(Items.PALE_OAK_HANGING_SIGN, Blocks.STRIPPED_PALE_OAK_LOG);
/* 3233 */     hangingSign(Items.MANGROVE_HANGING_SIGN, Blocks.STRIPPED_MANGROVE_LOG);
/* 3234 */     hangingSign(Items.BAMBOO_HANGING_SIGN, Items.STRIPPED_BAMBOO_BLOCK);
/* 3235 */     hangingSign(Items.CRIMSON_HANGING_SIGN, Blocks.STRIPPED_CRIMSON_STEM);
/* 3236 */     hangingSign(Items.WARPED_HANGING_SIGN, Blocks.STRIPPED_WARPED_STEM);
/*      */     
/* 3238 */     shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_BOOKSHELF)
/* 3239 */       .define(Character.valueOf('#'), ItemTags.PLANKS)
/* 3240 */       .define(Character.valueOf('X'), ItemTags.WOODEN_SLABS)
/* 3241 */       .pattern("###")
/* 3242 */       .pattern("XXX")
/* 3243 */       .pattern("###")
/* 3244 */       .unlockedBy("has_book", has(Items.BOOK))
/* 3245 */       .save(this.output);
/*      */     
/* 3247 */     oneToOneConversionRecipe(Items.ORANGE_DYE, Blocks.TORCHFLOWER, "orange_dye");
/* 3248 */     oneToOneConversionRecipe(Items.CYAN_DYE, Blocks.PITCHER_PLANT, "cyan_dye", 2);
/*      */     
/* 3250 */     planksFromLog(Blocks.CHERRY_PLANKS, ItemTags.CHERRY_LOGS, 4);
/* 3251 */     woodFromLogs(Blocks.CHERRY_WOOD, Blocks.CHERRY_LOG);
/* 3252 */     woodFromLogs(Blocks.STRIPPED_CHERRY_WOOD, Blocks.STRIPPED_CHERRY_LOG);
/* 3253 */     woodenBoat(Items.CHERRY_BOAT, Blocks.CHERRY_PLANKS);
/* 3254 */     chestBoat(Items.CHERRY_CHEST_BOAT, Items.CHERRY_BOAT);
/* 3255 */     oneToOneConversionRecipe(Items.PINK_DYE, Items.PINK_PETALS, "pink_dye", 1);
/*      */     
/* 3257 */     shaped(RecipeCategory.TOOLS, Items.BRUSH)
/* 3258 */       .define(Character.valueOf('X'), Items.FEATHER)
/* 3259 */       .define(Character.valueOf('#'), Items.COPPER_INGOT)
/* 3260 */       .define(Character.valueOf('I'), Items.STICK)
/* 3261 */       .pattern("X")
/* 3262 */       .pattern("#")
/* 3263 */       .pattern("I")
/* 3264 */       .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
/* 3265 */       .save(this.output);
/*      */     
/* 3267 */     shaped(RecipeCategory.DECORATIONS, Items.DECORATED_POT)
/* 3268 */       .define(Character.valueOf('#'), Items.BRICK)
/* 3269 */       .pattern(" # ")
/* 3270 */       .pattern("# #")
/* 3271 */       .pattern(" # ")
/* 3272 */       .unlockedBy("has_brick", has(ItemTags.DECORATED_POT_INGREDIENTS))
/* 3273 */       .save(this.output, "decorated_pot_simple");
/*      */     
/* 3275 */     SpecialRecipeBuilder.special(net.minecraft.world.item.crafting.DecoratedPotRecipe::new).save(this.output, "decorated_pot");
/*      */ 
/*      */     
/* 3278 */     shaped(RecipeCategory.REDSTONE, Blocks.CRAFTER)
/* 3279 */       .define(Character.valueOf('#'), Items.IRON_INGOT)
/* 3280 */       .define(Character.valueOf('C'), Items.CRAFTING_TABLE)
/* 3281 */       .define(Character.valueOf('R'), Items.REDSTONE)
/* 3282 */       .define(Character.valueOf('D'), Items.DROPPER)
/* 3283 */       .pattern("###")
/* 3284 */       .pattern("#C#")
/* 3285 */       .pattern("RDR")
/* 3286 */       .unlockedBy("has_dropper", has(Items.DROPPER))
/* 3287 */       .save(this.output);
/*      */     
/* 3289 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_SLAB, Blocks.TUFF, 2);
/* 3290 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_STAIRS, Blocks.TUFF);
/* 3291 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.TUFF_WALL, Blocks.TUFF);
/* 3292 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_TUFF, Blocks.TUFF);
/* 3293 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_TUFF, Blocks.TUFF);
/* 3294 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_TUFF_SLAB, Blocks.TUFF, 2);
/* 3295 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_TUFF_STAIRS, Blocks.TUFF);
/* 3296 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_TUFF_WALL, Blocks.TUFF);
/* 3297 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICKS, Blocks.TUFF);
/* 3298 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICK_SLAB, Blocks.TUFF, 2);
/* 3299 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICK_STAIRS, Blocks.TUFF);
/* 3300 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.TUFF_BRICK_WALL, Blocks.TUFF);
/* 3301 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_TUFF_BRICKS, Blocks.TUFF);
/*      */     
/* 3303 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_TUFF_SLAB, Blocks.POLISHED_TUFF, 2);
/* 3304 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.POLISHED_TUFF_STAIRS, Blocks.POLISHED_TUFF);
/* 3305 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.POLISHED_TUFF_WALL, Blocks.POLISHED_TUFF);
/* 3306 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICKS, Blocks.POLISHED_TUFF);
/* 3307 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICK_SLAB, Blocks.POLISHED_TUFF, 2);
/* 3308 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICK_STAIRS, Blocks.POLISHED_TUFF);
/* 3309 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.TUFF_BRICK_WALL, Blocks.POLISHED_TUFF);
/* 3310 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_TUFF_BRICKS, Blocks.POLISHED_TUFF);
/*      */     
/* 3312 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICK_SLAB, Blocks.TUFF_BRICKS, 2);
/* 3313 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.TUFF_BRICK_STAIRS, Blocks.TUFF_BRICKS);
/* 3314 */     stonecutterResultFromBase(RecipeCategory.DECORATIONS, Blocks.TUFF_BRICK_WALL, Blocks.TUFF_BRICKS);
/* 3315 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_TUFF_BRICKS, Blocks.TUFF_BRICKS);
/*      */     
/* 3317 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_COPPER, Blocks.COPPER_BLOCK, 4);
/* 3318 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.EXPOSED_CHISELED_COPPER, Blocks.EXPOSED_COPPER, 4);
/* 3319 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WEATHERED_CHISELED_COPPER, Blocks.WEATHERED_COPPER, 4);
/* 3320 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.OXIDIZED_CHISELED_COPPER, Blocks.OXIDIZED_COPPER, 4);
/* 3321 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_CHISELED_COPPER, Blocks.WAXED_COPPER_BLOCK, 4);
/* 3322 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_EXPOSED_CHISELED_COPPER, Blocks.WAXED_EXPOSED_COPPER, 4);
/* 3323 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_WEATHERED_CHISELED_COPPER, Blocks.WAXED_WEATHERED_COPPER, 4);
/* 3324 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_OXIDIZED_CHISELED_COPPER, Blocks.WAXED_OXIDIZED_COPPER, 4);
/*      */     
/* 3326 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_COPPER, Blocks.CUT_COPPER, 1);
/* 3327 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.EXPOSED_CHISELED_COPPER, Blocks.EXPOSED_CUT_COPPER, 1);
/* 3328 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WEATHERED_CHISELED_COPPER, Blocks.WEATHERED_CUT_COPPER, 1);
/* 3329 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.OXIDIZED_CHISELED_COPPER, Blocks.OXIDIZED_CUT_COPPER, 1);
/* 3330 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_CHISELED_COPPER, Blocks.WAXED_CUT_COPPER, 1);
/* 3331 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_EXPOSED_CHISELED_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, 1);
/* 3332 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_WEATHERED_CHISELED_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, 1);
/* 3333 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_OXIDIZED_CHISELED_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER, 1);
/*      */     
/* 3335 */     grate(Blocks.COPPER_GRATE, Blocks.COPPER_BLOCK);
/* 3336 */     grate(Blocks.EXPOSED_COPPER_GRATE, Blocks.EXPOSED_COPPER);
/* 3337 */     grate(Blocks.WEATHERED_COPPER_GRATE, Blocks.WEATHERED_COPPER);
/* 3338 */     grate(Blocks.OXIDIZED_COPPER_GRATE, Blocks.OXIDIZED_COPPER);
/* 3339 */     grate(Blocks.WAXED_COPPER_GRATE, Blocks.WAXED_COPPER_BLOCK);
/* 3340 */     grate(Blocks.WAXED_EXPOSED_COPPER_GRATE, Blocks.WAXED_EXPOSED_COPPER);
/* 3341 */     grate(Blocks.WAXED_WEATHERED_COPPER_GRATE, Blocks.WAXED_WEATHERED_COPPER);
/* 3342 */     grate(Blocks.WAXED_OXIDIZED_COPPER_GRATE, Blocks.WAXED_OXIDIZED_COPPER);
/*      */     
/* 3344 */     copperBulb(Blocks.COPPER_BULB, Blocks.COPPER_BLOCK);
/* 3345 */     copperBulb(Blocks.EXPOSED_COPPER_BULB, Blocks.EXPOSED_COPPER);
/* 3346 */     copperBulb(Blocks.WEATHERED_COPPER_BULB, Blocks.WEATHERED_COPPER);
/* 3347 */     copperBulb(Blocks.OXIDIZED_COPPER_BULB, Blocks.OXIDIZED_COPPER);
/* 3348 */     copperBulb(Blocks.WAXED_COPPER_BULB, Blocks.WAXED_COPPER_BLOCK);
/* 3349 */     copperBulb(Blocks.WAXED_EXPOSED_COPPER_BULB, Blocks.WAXED_EXPOSED_COPPER);
/* 3350 */     copperBulb(Blocks.WAXED_WEATHERED_COPPER_BULB, Blocks.WAXED_WEATHERED_COPPER);
/* 3351 */     copperBulb(Blocks.WAXED_OXIDIZED_COPPER_BULB, Blocks.WAXED_OXIDIZED_COPPER);
/*      */     
/* 3353 */     waxedChiseled(Blocks.WAXED_CHISELED_COPPER, Blocks.WAXED_CUT_COPPER_SLAB);
/* 3354 */     waxedChiseled(Blocks.WAXED_EXPOSED_CHISELED_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB);
/* 3355 */     waxedChiseled(Blocks.WAXED_WEATHERED_CHISELED_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB);
/* 3356 */     waxedChiseled(Blocks.WAXED_OXIDIZED_CHISELED_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB);
/*      */     
/* 3358 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.COPPER_GRATE, Blocks.COPPER_BLOCK, 4);
/* 3359 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.EXPOSED_COPPER_GRATE, Blocks.EXPOSED_COPPER, 4);
/* 3360 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WEATHERED_COPPER_GRATE, Blocks.WEATHERED_COPPER, 4);
/* 3361 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.OXIDIZED_COPPER_GRATE, Blocks.OXIDIZED_COPPER, 4);
/* 3362 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_COPPER_GRATE, Blocks.WAXED_COPPER_BLOCK, 4);
/* 3363 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_EXPOSED_COPPER_GRATE, Blocks.WAXED_EXPOSED_COPPER, 4);
/* 3364 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_WEATHERED_COPPER_GRATE, Blocks.WAXED_WEATHERED_COPPER, 4);
/* 3365 */     stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.WAXED_OXIDIZED_COPPER_GRATE, Blocks.WAXED_OXIDIZED_COPPER, 4);
/*      */     
/* 3367 */     shapeless(RecipeCategory.MISC, Items.WIND_CHARGE, 4)
/* 3368 */       .requires(Items.BREEZE_ROD)
/* 3369 */       .unlockedBy("has_breeze_rod", has(Items.BREEZE_ROD))
/* 3370 */       .save(this.output);
/*      */     
/* 3372 */     shaped(RecipeCategory.COMBAT, Items.MACE, 1)
/* 3373 */       .define(Character.valueOf('I'), Items.BREEZE_ROD)
/* 3374 */       .define(Character.valueOf('#'), Blocks.HEAVY_CORE)
/* 3375 */       .pattern(" # ")
/* 3376 */       .pattern(" I ")
/* 3377 */       .unlockedBy("has_breeze_rod", has(Items.BREEZE_ROD))
/* 3378 */       .unlockedBy("has_heavy_core", has(Blocks.HEAVY_CORE))
/* 3379 */       .save(this.output);
/*      */     
/* 3381 */     doorBuilder(Blocks.COPPER_DOOR, Ingredient.of(Items.COPPER_INGOT))
/* 3382 */       .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
/* 3383 */       .save(this.output);
/*      */     
/* 3385 */     twoByTwoPacker(RecipeCategory.REDSTONE, Blocks.COPPER_TRAPDOOR, Items.COPPER_INGOT);
/*      */     
/* 3387 */     shaped(RecipeCategory.TOOLS, Items.BUNDLE)
/* 3388 */       .define(Character.valueOf('-'), Items.STRING)
/* 3389 */       .define(Character.valueOf('#'), Items.LEATHER)
/* 3390 */       .pattern("-")
/* 3391 */       .pattern("#")
/* 3392 */       .unlockedBy("has_string", has(Items.STRING))
/* 3393 */       .save(this.output);
/*      */     
/* 3395 */     bundleRecipes();
/*      */   }
/*      */   public static final class TrimTemplate extends Record { private final Item template; private final ResourceKey<TrimPattern> patternId; private final ResourceKey<Recipe<?>> recipeId;
/* 3398 */     public TrimTemplate(Item template, ResourceKey<TrimPattern> patternId, ResourceKey<Recipe<?>> recipeId) { this.template = template; this.patternId = patternId; this.recipeId = recipeId; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3398	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/* 3398 */       //   0	7	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate; } public Item template() { return this.template; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3398	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #3398	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;
/* 3398 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<TrimPattern> patternId() { return this.patternId; } public ResourceKey<Recipe<?>> recipeId() { return this.recipeId; } }
/*      */   
/*      */   public static Stream<TrimTemplate> smithingTrims() {
/* 3401 */     return Stream.of(new Pair[] { 
/* 3402 */           Pair.of(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.BOLT), 
/* 3403 */           Pair.of(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.COAST), 
/* 3404 */           Pair.of(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.DUNE), 
/* 3405 */           Pair.of(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.EYE), 
/* 3406 */           Pair.of(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.FLOW), 
/* 3407 */           Pair.of(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.HOST), 
/* 3408 */           Pair.of(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.RAISER), 
/* 3409 */           Pair.of(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.RIB), 
/* 3410 */           Pair.of(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SENTRY), 
/* 3411 */           Pair.of(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SHAPER), 
/* 3412 */           Pair.of(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SILENCE), 
/* 3413 */           Pair.of(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SNOUT), 
/* 3414 */           Pair.of(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.SPIRE), 
/* 3415 */           Pair.of(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.TIDE), 
/* 3416 */           Pair.of(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.VEX), 
/* 3417 */           Pair.of(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.WARD), 
/* 3418 */           Pair.of(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.WAYFINDER), 
/* 3419 */           Pair.of(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPatterns.WILD)
/* 3420 */         }).map(itemAndPattern -> {
/* 3421 */           Item item = (Item)itemAndPattern.getFirst();
/* 3422 */           ResourceKey<TrimPattern> patternId = (ResourceKey)itemAndPattern.getSecond();
/* 3423 */           ResourceKey<Recipe<?>> recipeId = ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace(getItemName(item) + "_smithing_trim"));
/* 3424 */           return new TrimTemplate(item, patternId, recipeId);
/*      */         });
/*      */   }
/*      */   
/*      */   private void shulkerBoxRecipes() {
/* 3429 */     Ingredient input = tag(ItemTags.SHULKER_BOXES);
/* 3430 */     for (DyeColor value : DyeColor.values()) {
/* 3431 */       TransmuteRecipeBuilder.transmute(RecipeCategory.DECORATIONS, input, 
/*      */ 
/*      */           
/* 3434 */           Ingredient.of(DyeItem.byColor(value)), 
/* 3435 */           ShulkerBoxBlock.getBlockByColor(value).asItem())
/*      */         
/* 3437 */         .group("shulker_box_dye")
/* 3438 */         .unlockedBy("has_shulker_box", has(ItemTags.SHULKER_BOXES))
/* 3439 */         .save(this.output);
/*      */     }
/*      */   }
/*      */   
/*      */   private void bundleRecipes() {
/* 3444 */     Ingredient input = tag(ItemTags.BUNDLES);
/* 3445 */     for (DyeColor value : DyeColor.values()) {
/* 3446 */       DyeItem dye = DyeItem.byColor(value);
/* 3447 */       TransmuteRecipeBuilder.transmute(RecipeCategory.TOOLS, input, 
/*      */ 
/*      */           
/* 3450 */           Ingredient.of(dye), 
/* 3451 */           BundleItem.getByColor(value))
/*      */         
/* 3453 */         .group("bundle_dye")
/* 3454 */         .unlockedBy(getHasName(dye), has(dye))
/* 3455 */         .save(this.output);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\recipes\packs\VanillaRecipeProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */