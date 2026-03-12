/*      */ package net.minecraft.world.item;
/*      */ 
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.IntStream;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.BuiltInRegistries;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.RegistryOps;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.tags.InstrumentTags;
/*      */ import net.minecraft.tags.PaintingVariantTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.world.entity.decoration.painting.PaintingVariant;
/*      */ import net.minecraft.world.entity.raid.Raid;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.item.alchemy.Potion;
/*      */ import net.minecraft.world.item.alchemy.PotionContents;
/*      */ import net.minecraft.world.item.component.Fireworks;
/*      */ import net.minecraft.world.item.component.OminousBottleAmplifier;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentInstance;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.LightBlock;
/*      */ import net.minecraft.world.level.block.SuspiciousEffectHolder;
/*      */ import net.minecraft.world.level.block.TestBlock;
/*      */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*      */ 
/*      */ public class CreativeModeTabs {
/*   46 */   private static final Identifier INVENTORY_BACKGROUND = CreativeModeTab.createTextureLocation("inventory");
/*   47 */   private static final Identifier SEARCH_BACKGROUND = CreativeModeTab.createTextureLocation("item_search");
/*      */ 
/*      */   
/*   50 */   private static ResourceKey<CreativeModeTab> createKey(String id) { return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace(id)); }
/*      */ 
/*      */   
/*   53 */   private static final ResourceKey<CreativeModeTab> BUILDING_BLOCKS = createKey("building_blocks");
/*   54 */   private static final ResourceKey<CreativeModeTab> COLORED_BLOCKS = createKey("colored_blocks");
/*   55 */   private static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS = createKey("natural_blocks");
/*   56 */   private static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = createKey("functional_blocks");
/*   57 */   private static final ResourceKey<CreativeModeTab> REDSTONE_BLOCKS = createKey("redstone_blocks");
/*   58 */   private static final ResourceKey<CreativeModeTab> HOTBAR = createKey("hotbar");
/*   59 */   private static final ResourceKey<CreativeModeTab> SEARCH = createKey("search");
/*   60 */   private static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES = createKey("tools_and_utilities");
/*   61 */   private static final ResourceKey<CreativeModeTab> COMBAT = createKey("combat");
/*   62 */   private static final ResourceKey<CreativeModeTab> FOOD_AND_DRINKS = createKey("food_and_drinks");
/*   63 */   private static final ResourceKey<CreativeModeTab> INGREDIENTS = createKey("ingredients");
/*   64 */   private static final ResourceKey<CreativeModeTab> SPAWN_EGGS = createKey("spawn_eggs");
/*   65 */   private static final ResourceKey<CreativeModeTab> OP_BLOCKS = createKey("op_blocks");
/*   66 */   private static final ResourceKey<CreativeModeTab> INVENTORY = createKey("inventory");
/*      */   
/*      */   public static CreativeModeTab bootstrap(Registry<CreativeModeTab> registry) {
/*   69 */     Registry.register(registry, BUILDING_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
/*   70 */         .title(Component.translatable("itemGroup.buildingBlocks"))
/*   71 */         .icon(() -> new ItemStack(Blocks.BRICKS))
/*   72 */         .displayItems((parameters, buildingBlocks) -> {
/*      */ 
/*      */ 
/*      */             
/*   76 */             buildingBlocks.accept(Items.OAK_LOG);
/*   77 */             buildingBlocks.accept(Items.OAK_WOOD);
/*   78 */             buildingBlocks.accept(Items.STRIPPED_OAK_LOG);
/*   79 */             buildingBlocks.accept(Items.STRIPPED_OAK_WOOD);
/*   80 */             buildingBlocks.accept(Items.OAK_PLANKS);
/*   81 */             buildingBlocks.accept(Items.OAK_STAIRS);
/*   82 */             buildingBlocks.accept(Items.OAK_SLAB);
/*   83 */             buildingBlocks.accept(Items.OAK_FENCE);
/*   84 */             buildingBlocks.accept(Items.OAK_FENCE_GATE);
/*   85 */             buildingBlocks.accept(Items.OAK_DOOR);
/*   86 */             buildingBlocks.accept(Items.OAK_TRAPDOOR);
/*   87 */             buildingBlocks.accept(Items.OAK_PRESSURE_PLATE);
/*   88 */             buildingBlocks.accept(Items.OAK_BUTTON);
/*      */             
/*   90 */             buildingBlocks.accept(Items.SPRUCE_LOG);
/*   91 */             buildingBlocks.accept(Items.SPRUCE_WOOD);
/*   92 */             buildingBlocks.accept(Items.STRIPPED_SPRUCE_LOG);
/*   93 */             buildingBlocks.accept(Items.STRIPPED_SPRUCE_WOOD);
/*   94 */             buildingBlocks.accept(Items.SPRUCE_PLANKS);
/*   95 */             buildingBlocks.accept(Items.SPRUCE_STAIRS);
/*   96 */             buildingBlocks.accept(Items.SPRUCE_SLAB);
/*   97 */             buildingBlocks.accept(Items.SPRUCE_FENCE);
/*   98 */             buildingBlocks.accept(Items.SPRUCE_FENCE_GATE);
/*   99 */             buildingBlocks.accept(Items.SPRUCE_DOOR);
/*  100 */             buildingBlocks.accept(Items.SPRUCE_TRAPDOOR);
/*  101 */             buildingBlocks.accept(Items.SPRUCE_PRESSURE_PLATE);
/*  102 */             buildingBlocks.accept(Items.SPRUCE_BUTTON);
/*      */             
/*  104 */             buildingBlocks.accept(Items.BIRCH_LOG);
/*  105 */             buildingBlocks.accept(Items.BIRCH_WOOD);
/*  106 */             buildingBlocks.accept(Items.STRIPPED_BIRCH_LOG);
/*  107 */             buildingBlocks.accept(Items.STRIPPED_BIRCH_WOOD);
/*  108 */             buildingBlocks.accept(Items.BIRCH_PLANKS);
/*  109 */             buildingBlocks.accept(Items.BIRCH_STAIRS);
/*  110 */             buildingBlocks.accept(Items.BIRCH_SLAB);
/*  111 */             buildingBlocks.accept(Items.BIRCH_FENCE);
/*  112 */             buildingBlocks.accept(Items.BIRCH_FENCE_GATE);
/*  113 */             buildingBlocks.accept(Items.BIRCH_DOOR);
/*  114 */             buildingBlocks.accept(Items.BIRCH_TRAPDOOR);
/*  115 */             buildingBlocks.accept(Items.BIRCH_PRESSURE_PLATE);
/*  116 */             buildingBlocks.accept(Items.BIRCH_BUTTON);
/*      */             
/*  118 */             buildingBlocks.accept(Items.JUNGLE_LOG);
/*  119 */             buildingBlocks.accept(Items.JUNGLE_WOOD);
/*  120 */             buildingBlocks.accept(Items.STRIPPED_JUNGLE_LOG);
/*  121 */             buildingBlocks.accept(Items.STRIPPED_JUNGLE_WOOD);
/*  122 */             buildingBlocks.accept(Items.JUNGLE_PLANKS);
/*  123 */             buildingBlocks.accept(Items.JUNGLE_STAIRS);
/*  124 */             buildingBlocks.accept(Items.JUNGLE_SLAB);
/*  125 */             buildingBlocks.accept(Items.JUNGLE_FENCE);
/*  126 */             buildingBlocks.accept(Items.JUNGLE_FENCE_GATE);
/*  127 */             buildingBlocks.accept(Items.JUNGLE_DOOR);
/*  128 */             buildingBlocks.accept(Items.JUNGLE_TRAPDOOR);
/*  129 */             buildingBlocks.accept(Items.JUNGLE_PRESSURE_PLATE);
/*  130 */             buildingBlocks.accept(Items.JUNGLE_BUTTON);
/*      */             
/*  132 */             buildingBlocks.accept(Items.ACACIA_LOG);
/*  133 */             buildingBlocks.accept(Items.ACACIA_WOOD);
/*  134 */             buildingBlocks.accept(Items.STRIPPED_ACACIA_LOG);
/*  135 */             buildingBlocks.accept(Items.STRIPPED_ACACIA_WOOD);
/*  136 */             buildingBlocks.accept(Items.ACACIA_PLANKS);
/*  137 */             buildingBlocks.accept(Items.ACACIA_STAIRS);
/*  138 */             buildingBlocks.accept(Items.ACACIA_SLAB);
/*  139 */             buildingBlocks.accept(Items.ACACIA_FENCE);
/*  140 */             buildingBlocks.accept(Items.ACACIA_FENCE_GATE);
/*  141 */             buildingBlocks.accept(Items.ACACIA_DOOR);
/*  142 */             buildingBlocks.accept(Items.ACACIA_TRAPDOOR);
/*  143 */             buildingBlocks.accept(Items.ACACIA_PRESSURE_PLATE);
/*  144 */             buildingBlocks.accept(Items.ACACIA_BUTTON);
/*      */             
/*  146 */             buildingBlocks.accept(Items.DARK_OAK_LOG);
/*  147 */             buildingBlocks.accept(Items.DARK_OAK_WOOD);
/*  148 */             buildingBlocks.accept(Items.STRIPPED_DARK_OAK_LOG);
/*  149 */             buildingBlocks.accept(Items.STRIPPED_DARK_OAK_WOOD);
/*  150 */             buildingBlocks.accept(Items.DARK_OAK_PLANKS);
/*  151 */             buildingBlocks.accept(Items.DARK_OAK_STAIRS);
/*  152 */             buildingBlocks.accept(Items.DARK_OAK_SLAB);
/*  153 */             buildingBlocks.accept(Items.DARK_OAK_FENCE);
/*  154 */             buildingBlocks.accept(Items.DARK_OAK_FENCE_GATE);
/*  155 */             buildingBlocks.accept(Items.DARK_OAK_DOOR);
/*  156 */             buildingBlocks.accept(Items.DARK_OAK_TRAPDOOR);
/*  157 */             buildingBlocks.accept(Items.DARK_OAK_PRESSURE_PLATE);
/*  158 */             buildingBlocks.accept(Items.DARK_OAK_BUTTON);
/*      */             
/*  160 */             buildingBlocks.accept(Items.MANGROVE_LOG);
/*  161 */             buildingBlocks.accept(Items.MANGROVE_WOOD);
/*  162 */             buildingBlocks.accept(Items.STRIPPED_MANGROVE_LOG);
/*  163 */             buildingBlocks.accept(Items.STRIPPED_MANGROVE_WOOD);
/*  164 */             buildingBlocks.accept(Items.MANGROVE_PLANKS);
/*  165 */             buildingBlocks.accept(Items.MANGROVE_STAIRS);
/*  166 */             buildingBlocks.accept(Items.MANGROVE_SLAB);
/*  167 */             buildingBlocks.accept(Items.MANGROVE_FENCE);
/*  168 */             buildingBlocks.accept(Items.MANGROVE_FENCE_GATE);
/*  169 */             buildingBlocks.accept(Items.MANGROVE_DOOR);
/*  170 */             buildingBlocks.accept(Items.MANGROVE_TRAPDOOR);
/*  171 */             buildingBlocks.accept(Items.MANGROVE_PRESSURE_PLATE);
/*  172 */             buildingBlocks.accept(Items.MANGROVE_BUTTON);
/*      */             
/*  174 */             buildingBlocks.accept(Items.CHERRY_LOG);
/*  175 */             buildingBlocks.accept(Items.CHERRY_WOOD);
/*  176 */             buildingBlocks.accept(Items.STRIPPED_CHERRY_LOG);
/*  177 */             buildingBlocks.accept(Items.STRIPPED_CHERRY_WOOD);
/*  178 */             buildingBlocks.accept(Items.CHERRY_PLANKS);
/*  179 */             buildingBlocks.accept(Items.CHERRY_STAIRS);
/*  180 */             buildingBlocks.accept(Items.CHERRY_SLAB);
/*  181 */             buildingBlocks.accept(Items.CHERRY_FENCE);
/*  182 */             buildingBlocks.accept(Items.CHERRY_FENCE_GATE);
/*  183 */             buildingBlocks.accept(Items.CHERRY_DOOR);
/*  184 */             buildingBlocks.accept(Items.CHERRY_TRAPDOOR);
/*  185 */             buildingBlocks.accept(Items.CHERRY_PRESSURE_PLATE);
/*  186 */             buildingBlocks.accept(Items.CHERRY_BUTTON);
/*      */             
/*  188 */             buildingBlocks.accept(Items.PALE_OAK_LOG);
/*  189 */             buildingBlocks.accept(Items.PALE_OAK_WOOD);
/*  190 */             buildingBlocks.accept(Items.STRIPPED_PALE_OAK_LOG);
/*  191 */             buildingBlocks.accept(Items.STRIPPED_PALE_OAK_WOOD);
/*  192 */             buildingBlocks.accept(Items.PALE_OAK_PLANKS);
/*  193 */             buildingBlocks.accept(Items.PALE_OAK_STAIRS);
/*  194 */             buildingBlocks.accept(Items.PALE_OAK_SLAB);
/*  195 */             buildingBlocks.accept(Items.PALE_OAK_FENCE);
/*  196 */             buildingBlocks.accept(Items.PALE_OAK_FENCE_GATE);
/*  197 */             buildingBlocks.accept(Items.PALE_OAK_DOOR);
/*  198 */             buildingBlocks.accept(Items.PALE_OAK_TRAPDOOR);
/*  199 */             buildingBlocks.accept(Items.PALE_OAK_PRESSURE_PLATE);
/*  200 */             buildingBlocks.accept(Items.PALE_OAK_BUTTON);
/*      */             
/*  202 */             buildingBlocks.accept(Items.BAMBOO_BLOCK);
/*  203 */             buildingBlocks.accept(Items.STRIPPED_BAMBOO_BLOCK);
/*  204 */             buildingBlocks.accept(Items.BAMBOO_PLANKS);
/*  205 */             buildingBlocks.accept(Items.BAMBOO_MOSAIC);
/*  206 */             buildingBlocks.accept(Items.BAMBOO_STAIRS);
/*  207 */             buildingBlocks.accept(Items.BAMBOO_MOSAIC_STAIRS);
/*  208 */             buildingBlocks.accept(Items.BAMBOO_SLAB);
/*  209 */             buildingBlocks.accept(Items.BAMBOO_MOSAIC_SLAB);
/*  210 */             buildingBlocks.accept(Items.BAMBOO_FENCE);
/*  211 */             buildingBlocks.accept(Items.BAMBOO_FENCE_GATE);
/*  212 */             buildingBlocks.accept(Items.BAMBOO_DOOR);
/*  213 */             buildingBlocks.accept(Items.BAMBOO_TRAPDOOR);
/*  214 */             buildingBlocks.accept(Items.BAMBOO_PRESSURE_PLATE);
/*  215 */             buildingBlocks.accept(Items.BAMBOO_BUTTON);
/*      */             
/*  217 */             buildingBlocks.accept(Items.CRIMSON_STEM);
/*  218 */             buildingBlocks.accept(Items.CRIMSON_HYPHAE);
/*  219 */             buildingBlocks.accept(Items.STRIPPED_CRIMSON_STEM);
/*  220 */             buildingBlocks.accept(Items.STRIPPED_CRIMSON_HYPHAE);
/*  221 */             buildingBlocks.accept(Items.CRIMSON_PLANKS);
/*  222 */             buildingBlocks.accept(Items.CRIMSON_STAIRS);
/*  223 */             buildingBlocks.accept(Items.CRIMSON_SLAB);
/*  224 */             buildingBlocks.accept(Items.CRIMSON_FENCE);
/*  225 */             buildingBlocks.accept(Items.CRIMSON_FENCE_GATE);
/*  226 */             buildingBlocks.accept(Items.CRIMSON_DOOR);
/*  227 */             buildingBlocks.accept(Items.CRIMSON_TRAPDOOR);
/*  228 */             buildingBlocks.accept(Items.CRIMSON_PRESSURE_PLATE);
/*  229 */             buildingBlocks.accept(Items.CRIMSON_BUTTON);
/*      */             
/*  231 */             buildingBlocks.accept(Items.WARPED_STEM);
/*  232 */             buildingBlocks.accept(Items.WARPED_HYPHAE);
/*  233 */             buildingBlocks.accept(Items.STRIPPED_WARPED_STEM);
/*  234 */             buildingBlocks.accept(Items.STRIPPED_WARPED_HYPHAE);
/*  235 */             buildingBlocks.accept(Items.WARPED_PLANKS);
/*  236 */             buildingBlocks.accept(Items.WARPED_STAIRS);
/*  237 */             buildingBlocks.accept(Items.WARPED_SLAB);
/*  238 */             buildingBlocks.accept(Items.WARPED_FENCE);
/*  239 */             buildingBlocks.accept(Items.WARPED_FENCE_GATE);
/*  240 */             buildingBlocks.accept(Items.WARPED_DOOR);
/*  241 */             buildingBlocks.accept(Items.WARPED_TRAPDOOR);
/*  242 */             buildingBlocks.accept(Items.WARPED_PRESSURE_PLATE);
/*  243 */             buildingBlocks.accept(Items.WARPED_BUTTON);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  248 */             buildingBlocks.accept(Items.STONE);
/*  249 */             buildingBlocks.accept(Items.STONE_STAIRS);
/*  250 */             buildingBlocks.accept(Items.STONE_SLAB);
/*  251 */             buildingBlocks.accept(Items.STONE_PRESSURE_PLATE);
/*  252 */             buildingBlocks.accept(Items.STONE_BUTTON);
/*  253 */             buildingBlocks.accept(Items.COBBLESTONE);
/*  254 */             buildingBlocks.accept(Items.COBBLESTONE_STAIRS);
/*  255 */             buildingBlocks.accept(Items.COBBLESTONE_SLAB);
/*  256 */             buildingBlocks.accept(Items.COBBLESTONE_WALL);
/*  257 */             buildingBlocks.accept(Items.MOSSY_COBBLESTONE);
/*  258 */             buildingBlocks.accept(Items.MOSSY_COBBLESTONE_STAIRS);
/*  259 */             buildingBlocks.accept(Items.MOSSY_COBBLESTONE_SLAB);
/*  260 */             buildingBlocks.accept(Items.MOSSY_COBBLESTONE_WALL);
/*      */             
/*  262 */             buildingBlocks.accept(Items.SMOOTH_STONE);
/*  263 */             buildingBlocks.accept(Items.SMOOTH_STONE_SLAB);
/*      */             
/*  265 */             buildingBlocks.accept(Items.STONE_BRICKS);
/*  266 */             buildingBlocks.accept(Items.CRACKED_STONE_BRICKS);
/*  267 */             buildingBlocks.accept(Items.STONE_BRICK_STAIRS);
/*  268 */             buildingBlocks.accept(Items.STONE_BRICK_SLAB);
/*  269 */             buildingBlocks.accept(Items.STONE_BRICK_WALL);
/*  270 */             buildingBlocks.accept(Items.CHISELED_STONE_BRICKS);
/*  271 */             buildingBlocks.accept(Items.MOSSY_STONE_BRICKS);
/*  272 */             buildingBlocks.accept(Items.MOSSY_STONE_BRICK_STAIRS);
/*  273 */             buildingBlocks.accept(Items.MOSSY_STONE_BRICK_SLAB);
/*  274 */             buildingBlocks.accept(Items.MOSSY_STONE_BRICK_WALL);
/*      */             
/*  276 */             buildingBlocks.accept(Items.GRANITE);
/*  277 */             buildingBlocks.accept(Items.GRANITE_STAIRS);
/*  278 */             buildingBlocks.accept(Items.GRANITE_SLAB);
/*  279 */             buildingBlocks.accept(Items.GRANITE_WALL);
/*  280 */             buildingBlocks.accept(Items.POLISHED_GRANITE);
/*  281 */             buildingBlocks.accept(Items.POLISHED_GRANITE_STAIRS);
/*  282 */             buildingBlocks.accept(Items.POLISHED_GRANITE_SLAB);
/*      */             
/*  284 */             buildingBlocks.accept(Items.DIORITE);
/*  285 */             buildingBlocks.accept(Items.DIORITE_STAIRS);
/*  286 */             buildingBlocks.accept(Items.DIORITE_SLAB);
/*  287 */             buildingBlocks.accept(Items.DIORITE_WALL);
/*  288 */             buildingBlocks.accept(Items.POLISHED_DIORITE);
/*  289 */             buildingBlocks.accept(Items.POLISHED_DIORITE_STAIRS);
/*  290 */             buildingBlocks.accept(Items.POLISHED_DIORITE_SLAB);
/*      */             
/*  292 */             buildingBlocks.accept(Items.ANDESITE);
/*  293 */             buildingBlocks.accept(Items.ANDESITE_STAIRS);
/*  294 */             buildingBlocks.accept(Items.ANDESITE_SLAB);
/*  295 */             buildingBlocks.accept(Items.ANDESITE_WALL);
/*  296 */             buildingBlocks.accept(Items.POLISHED_ANDESITE);
/*  297 */             buildingBlocks.accept(Items.POLISHED_ANDESITE_STAIRS);
/*  298 */             buildingBlocks.accept(Items.POLISHED_ANDESITE_SLAB);
/*      */             
/*  300 */             buildingBlocks.accept(Items.DEEPSLATE);
/*  301 */             buildingBlocks.accept(Items.COBBLED_DEEPSLATE);
/*  302 */             buildingBlocks.accept(Items.COBBLED_DEEPSLATE_STAIRS);
/*  303 */             buildingBlocks.accept(Items.COBBLED_DEEPSLATE_SLAB);
/*  304 */             buildingBlocks.accept(Items.COBBLED_DEEPSLATE_WALL);
/*  305 */             buildingBlocks.accept(Items.CHISELED_DEEPSLATE);
/*  306 */             buildingBlocks.accept(Items.POLISHED_DEEPSLATE);
/*  307 */             buildingBlocks.accept(Items.POLISHED_DEEPSLATE_STAIRS);
/*  308 */             buildingBlocks.accept(Items.POLISHED_DEEPSLATE_SLAB);
/*  309 */             buildingBlocks.accept(Items.POLISHED_DEEPSLATE_WALL);
/*  310 */             buildingBlocks.accept(Items.DEEPSLATE_BRICKS);
/*  311 */             buildingBlocks.accept(Items.CRACKED_DEEPSLATE_BRICKS);
/*  312 */             buildingBlocks.accept(Items.DEEPSLATE_BRICK_STAIRS);
/*  313 */             buildingBlocks.accept(Items.DEEPSLATE_BRICK_SLAB);
/*  314 */             buildingBlocks.accept(Items.DEEPSLATE_BRICK_WALL);
/*  315 */             buildingBlocks.accept(Items.DEEPSLATE_TILES);
/*  316 */             buildingBlocks.accept(Items.CRACKED_DEEPSLATE_TILES);
/*  317 */             buildingBlocks.accept(Items.DEEPSLATE_TILE_STAIRS);
/*  318 */             buildingBlocks.accept(Items.DEEPSLATE_TILE_SLAB);
/*  319 */             buildingBlocks.accept(Items.DEEPSLATE_TILE_WALL);
/*  320 */             buildingBlocks.accept(Items.REINFORCED_DEEPSLATE);
/*      */             
/*  322 */             buildingBlocks.accept(Items.TUFF);
/*  323 */             buildingBlocks.accept(Items.TUFF_STAIRS);
/*  324 */             buildingBlocks.accept(Items.TUFF_SLAB);
/*  325 */             buildingBlocks.accept(Items.TUFF_WALL);
/*  326 */             buildingBlocks.accept(Items.CHISELED_TUFF);
/*  327 */             buildingBlocks.accept(Items.POLISHED_TUFF);
/*  328 */             buildingBlocks.accept(Items.POLISHED_TUFF_STAIRS);
/*  329 */             buildingBlocks.accept(Items.POLISHED_TUFF_SLAB);
/*  330 */             buildingBlocks.accept(Items.POLISHED_TUFF_WALL);
/*  331 */             buildingBlocks.accept(Items.TUFF_BRICKS);
/*  332 */             buildingBlocks.accept(Items.TUFF_BRICK_STAIRS);
/*  333 */             buildingBlocks.accept(Items.TUFF_BRICK_SLAB);
/*  334 */             buildingBlocks.accept(Items.TUFF_BRICK_WALL);
/*  335 */             buildingBlocks.accept(Items.CHISELED_TUFF_BRICKS);
/*      */             
/*  337 */             buildingBlocks.accept(Items.BRICKS);
/*  338 */             buildingBlocks.accept(Items.BRICK_STAIRS);
/*  339 */             buildingBlocks.accept(Items.BRICK_SLAB);
/*  340 */             buildingBlocks.accept(Items.BRICK_WALL);
/*      */             
/*  342 */             buildingBlocks.accept(Items.PACKED_MUD);
/*  343 */             buildingBlocks.accept(Items.MUD_BRICKS);
/*  344 */             buildingBlocks.accept(Items.MUD_BRICK_STAIRS);
/*  345 */             buildingBlocks.accept(Items.MUD_BRICK_SLAB);
/*  346 */             buildingBlocks.accept(Items.MUD_BRICK_WALL);
/*      */             
/*  348 */             buildingBlocks.accept(Items.RESIN_BRICKS);
/*  349 */             buildingBlocks.accept(Items.RESIN_BRICK_STAIRS);
/*  350 */             buildingBlocks.accept(Items.RESIN_BRICK_SLAB);
/*  351 */             buildingBlocks.accept(Items.RESIN_BRICK_WALL);
/*  352 */             buildingBlocks.accept(Items.CHISELED_RESIN_BRICKS);
/*      */             
/*  354 */             buildingBlocks.accept(Items.SANDSTONE);
/*  355 */             buildingBlocks.accept(Items.SANDSTONE_STAIRS);
/*  356 */             buildingBlocks.accept(Items.SANDSTONE_SLAB);
/*  357 */             buildingBlocks.accept(Items.SANDSTONE_WALL);
/*  358 */             buildingBlocks.accept(Items.CHISELED_SANDSTONE);
/*  359 */             buildingBlocks.accept(Items.SMOOTH_SANDSTONE);
/*  360 */             buildingBlocks.accept(Items.SMOOTH_SANDSTONE_STAIRS);
/*  361 */             buildingBlocks.accept(Items.SMOOTH_SANDSTONE_SLAB);
/*  362 */             buildingBlocks.accept(Items.CUT_SANDSTONE);
/*  363 */             buildingBlocks.accept(Items.CUT_STANDSTONE_SLAB);
/*      */             
/*  365 */             buildingBlocks.accept(Items.RED_SANDSTONE);
/*  366 */             buildingBlocks.accept(Items.RED_SANDSTONE_STAIRS);
/*  367 */             buildingBlocks.accept(Items.RED_SANDSTONE_SLAB);
/*  368 */             buildingBlocks.accept(Items.RED_SANDSTONE_WALL);
/*  369 */             buildingBlocks.accept(Items.CHISELED_RED_SANDSTONE);
/*  370 */             buildingBlocks.accept(Items.SMOOTH_RED_SANDSTONE);
/*  371 */             buildingBlocks.accept(Items.SMOOTH_RED_SANDSTONE_STAIRS);
/*  372 */             buildingBlocks.accept(Items.SMOOTH_RED_SANDSTONE_SLAB);
/*  373 */             buildingBlocks.accept(Items.CUT_RED_SANDSTONE);
/*  374 */             buildingBlocks.accept(Items.CUT_RED_SANDSTONE_SLAB);
/*      */             
/*  376 */             buildingBlocks.accept(Items.SEA_LANTERN);
/*  377 */             buildingBlocks.accept(Items.PRISMARINE);
/*  378 */             buildingBlocks.accept(Items.PRISMARINE_STAIRS);
/*  379 */             buildingBlocks.accept(Items.PRISMARINE_SLAB);
/*  380 */             buildingBlocks.accept(Items.PRISMARINE_WALL);
/*  381 */             buildingBlocks.accept(Items.PRISMARINE_BRICKS);
/*  382 */             buildingBlocks.accept(Items.PRISMARINE_BRICK_STAIRS);
/*  383 */             buildingBlocks.accept(Items.PRISMARINE_BRICK_SLAB);
/*  384 */             buildingBlocks.accept(Items.DARK_PRISMARINE);
/*  385 */             buildingBlocks.accept(Items.DARK_PRISMARINE_STAIRS);
/*  386 */             buildingBlocks.accept(Items.DARK_PRISMARINE_SLAB);
/*      */             
/*  388 */             buildingBlocks.accept(Items.NETHERRACK);
/*      */             
/*  390 */             buildingBlocks.accept(Items.NETHER_BRICKS);
/*  391 */             buildingBlocks.accept(Items.CRACKED_NETHER_BRICKS);
/*  392 */             buildingBlocks.accept(Items.NETHER_BRICK_STAIRS);
/*  393 */             buildingBlocks.accept(Items.NETHER_BRICK_SLAB);
/*  394 */             buildingBlocks.accept(Items.NETHER_BRICK_WALL);
/*  395 */             buildingBlocks.accept(Items.NETHER_BRICK_FENCE);
/*  396 */             buildingBlocks.accept(Items.CHISELED_NETHER_BRICKS);
/*      */             
/*  398 */             buildingBlocks.accept(Items.RED_NETHER_BRICKS);
/*  399 */             buildingBlocks.accept(Items.RED_NETHER_BRICK_STAIRS);
/*  400 */             buildingBlocks.accept(Items.RED_NETHER_BRICK_SLAB);
/*  401 */             buildingBlocks.accept(Items.RED_NETHER_BRICK_WALL);
/*      */             
/*  403 */             buildingBlocks.accept(Items.BASALT);
/*  404 */             buildingBlocks.accept(Items.SMOOTH_BASALT);
/*  405 */             buildingBlocks.accept(Items.POLISHED_BASALT);
/*      */             
/*  407 */             buildingBlocks.accept(Items.BLACKSTONE);
/*  408 */             buildingBlocks.accept(Items.GILDED_BLACKSTONE);
/*  409 */             buildingBlocks.accept(Items.BLACKSTONE_STAIRS);
/*  410 */             buildingBlocks.accept(Items.BLACKSTONE_SLAB);
/*  411 */             buildingBlocks.accept(Items.BLACKSTONE_WALL);
/*  412 */             buildingBlocks.accept(Items.CHISELED_POLISHED_BLACKSTONE);
/*  413 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE);
/*  414 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_STAIRS);
/*  415 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_SLAB);
/*  416 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_WALL);
/*  417 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_PRESSURE_PLATE);
/*  418 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_BUTTON);
/*  419 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_BRICKS);
/*  420 */             buildingBlocks.accept(Items.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*  421 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*  422 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_BRICK_SLAB);
/*  423 */             buildingBlocks.accept(Items.POLISHED_BLACKSTONE_BRICK_WALL);
/*      */             
/*  425 */             buildingBlocks.accept(Items.END_STONE);
/*  426 */             buildingBlocks.accept(Items.END_STONE_BRICKS);
/*  427 */             buildingBlocks.accept(Items.END_STONE_BRICK_STAIRS);
/*  428 */             buildingBlocks.accept(Items.END_STONE_BRICK_SLAB);
/*  429 */             buildingBlocks.accept(Items.END_STONE_BRICK_WALL);
/*      */             
/*  431 */             buildingBlocks.accept(Items.PURPUR_BLOCK);
/*  432 */             buildingBlocks.accept(Items.PURPUR_PILLAR);
/*  433 */             buildingBlocks.accept(Items.PURPUR_STAIRS);
/*  434 */             buildingBlocks.accept(Items.PURPUR_SLAB);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  439 */             buildingBlocks.accept(Items.COAL_BLOCK);
/*      */ 
/*      */             
/*  442 */             buildingBlocks.accept(Items.IRON_BLOCK);
/*  443 */             buildingBlocks.accept(Items.IRON_BARS);
/*  444 */             buildingBlocks.accept(Items.IRON_DOOR);
/*  445 */             buildingBlocks.accept(Items.IRON_TRAPDOOR);
/*  446 */             buildingBlocks.accept(Items.HEAVY_WEIGHTED_PRESSURE_PLATE);
/*  447 */             buildingBlocks.accept(Items.IRON_CHAIN);
/*      */             
/*  449 */             buildingBlocks.accept(Items.GOLD_BLOCK);
/*  450 */             buildingBlocks.accept(Items.LIGHT_WEIGHTED_PRESSURE_PLATE);
/*  451 */             buildingBlocks.accept(Items.REDSTONE_BLOCK);
/*  452 */             buildingBlocks.accept(Items.EMERALD_BLOCK);
/*  453 */             buildingBlocks.accept(Items.LAPIS_BLOCK);
/*  454 */             buildingBlocks.accept(Items.DIAMOND_BLOCK);
/*  455 */             buildingBlocks.accept(Items.NETHERITE_BLOCK);
/*      */ 
/*      */             
/*  458 */             buildingBlocks.accept(Items.QUARTZ_BLOCK);
/*  459 */             buildingBlocks.accept(Items.QUARTZ_STAIRS);
/*  460 */             buildingBlocks.accept(Items.QUARTZ_SLAB);
/*  461 */             buildingBlocks.accept(Items.CHISELED_QUARTZ_BLOCK);
/*  462 */             buildingBlocks.accept(Items.QUARTZ_BRICKS);
/*  463 */             buildingBlocks.accept(Items.QUARTZ_PILLAR);
/*  464 */             buildingBlocks.accept(Items.SMOOTH_QUARTZ);
/*  465 */             buildingBlocks.accept(Items.SMOOTH_QUARTZ_STAIRS);
/*  466 */             buildingBlocks.accept(Items.SMOOTH_QUARTZ_SLAB);
/*      */             
/*  468 */             buildingBlocks.accept(Items.AMETHYST_BLOCK);
/*      */ 
/*      */             
/*  471 */             buildingBlocks.accept(Items.COPPER_BLOCK);
/*  472 */             buildingBlocks.accept(Items.CHISELED_COPPER);
/*  473 */             buildingBlocks.accept(Items.COPPER_GRATE);
/*  474 */             buildingBlocks.accept(Items.CUT_COPPER);
/*  475 */             buildingBlocks.accept(Items.CUT_COPPER_STAIRS);
/*  476 */             buildingBlocks.accept(Items.CUT_COPPER_SLAB);
/*  477 */             buildingBlocks.accept(Items.COPPER_BARS.unaffected());
/*      */             
/*  479 */             buildingBlocks.accept(Items.COPPER_DOOR);
/*  480 */             buildingBlocks.accept(Items.COPPER_TRAPDOOR);
/*  481 */             buildingBlocks.accept(Items.COPPER_BULB);
/*  482 */             buildingBlocks.accept(Items.COPPER_CHAIN.unaffected());
/*      */             
/*  484 */             buildingBlocks.accept(Items.EXPOSED_COPPER);
/*  485 */             buildingBlocks.accept(Items.EXPOSED_CHISELED_COPPER);
/*  486 */             buildingBlocks.accept(Items.EXPOSED_COPPER_GRATE);
/*  487 */             buildingBlocks.accept(Items.EXPOSED_CUT_COPPER);
/*  488 */             buildingBlocks.accept(Items.EXPOSED_CUT_COPPER_STAIRS);
/*  489 */             buildingBlocks.accept(Items.EXPOSED_CUT_COPPER_SLAB);
/*  490 */             buildingBlocks.accept(Items.COPPER_BARS.exposed());
/*  491 */             buildingBlocks.accept(Items.EXPOSED_COPPER_DOOR);
/*  492 */             buildingBlocks.accept(Items.EXPOSED_COPPER_TRAPDOOR);
/*  493 */             buildingBlocks.accept(Items.EXPOSED_COPPER_BULB);
/*  494 */             buildingBlocks.accept(Items.COPPER_CHAIN.exposed());
/*      */             
/*  496 */             buildingBlocks.accept(Items.WEATHERED_COPPER);
/*  497 */             buildingBlocks.accept(Items.WEATHERED_CHISELED_COPPER);
/*  498 */             buildingBlocks.accept(Items.WEATHERED_COPPER_GRATE);
/*  499 */             buildingBlocks.accept(Items.WEATHERED_CUT_COPPER);
/*  500 */             buildingBlocks.accept(Items.WEATHERED_CUT_COPPER_STAIRS);
/*  501 */             buildingBlocks.accept(Items.WEATHERED_CUT_COPPER_SLAB);
/*  502 */             buildingBlocks.accept(Items.COPPER_BARS.weathered());
/*  503 */             buildingBlocks.accept(Items.WEATHERED_COPPER_DOOR);
/*  504 */             buildingBlocks.accept(Items.WEATHERED_COPPER_TRAPDOOR);
/*  505 */             buildingBlocks.accept(Items.WEATHERED_COPPER_BULB);
/*  506 */             buildingBlocks.accept(Items.COPPER_CHAIN.weathered());
/*      */             
/*  508 */             buildingBlocks.accept(Items.OXIDIZED_COPPER);
/*  509 */             buildingBlocks.accept(Items.OXIDIZED_CHISELED_COPPER);
/*  510 */             buildingBlocks.accept(Items.OXIDIZED_COPPER_GRATE);
/*  511 */             buildingBlocks.accept(Items.OXIDIZED_CUT_COPPER);
/*  512 */             buildingBlocks.accept(Items.OXIDIZED_CUT_COPPER_STAIRS);
/*  513 */             buildingBlocks.accept(Items.OXIDIZED_CUT_COPPER_SLAB);
/*  514 */             buildingBlocks.accept(Items.COPPER_BARS.oxidized());
/*  515 */             buildingBlocks.accept(Items.OXIDIZED_COPPER_DOOR);
/*  516 */             buildingBlocks.accept(Items.OXIDIZED_COPPER_TRAPDOOR);
/*  517 */             buildingBlocks.accept(Items.OXIDIZED_COPPER_BULB);
/*  518 */             buildingBlocks.accept(Items.COPPER_CHAIN.oxidized());
/*      */             
/*  520 */             buildingBlocks.accept(Items.WAXED_COPPER_BLOCK);
/*  521 */             buildingBlocks.accept(Items.WAXED_CHISELED_COPPER);
/*  522 */             buildingBlocks.accept(Items.WAXED_COPPER_GRATE);
/*  523 */             buildingBlocks.accept(Items.WAXED_CUT_COPPER);
/*  524 */             buildingBlocks.accept(Items.WAXED_CUT_COPPER_STAIRS);
/*  525 */             buildingBlocks.accept(Items.WAXED_CUT_COPPER_SLAB);
/*  526 */             buildingBlocks.accept(Items.COPPER_BARS.waxed());
/*  527 */             buildingBlocks.accept(Items.WAXED_COPPER_DOOR);
/*  528 */             buildingBlocks.accept(Items.WAXED_COPPER_TRAPDOOR);
/*  529 */             buildingBlocks.accept(Items.WAXED_COPPER_BULB);
/*  530 */             buildingBlocks.accept(Items.COPPER_CHAIN.waxed());
/*      */             
/*  532 */             buildingBlocks.accept(Items.WAXED_EXPOSED_COPPER);
/*  533 */             buildingBlocks.accept(Items.WAXED_EXPOSED_CHISELED_COPPER);
/*  534 */             buildingBlocks.accept(Items.WAXED_EXPOSED_COPPER_GRATE);
/*  535 */             buildingBlocks.accept(Items.WAXED_EXPOSED_CUT_COPPER);
/*  536 */             buildingBlocks.accept(Items.WAXED_EXPOSED_CUT_COPPER_STAIRS);
/*  537 */             buildingBlocks.accept(Items.WAXED_EXPOSED_CUT_COPPER_SLAB);
/*  538 */             buildingBlocks.accept(Items.COPPER_BARS.waxedExposed());
/*  539 */             buildingBlocks.accept(Items.WAXED_EXPOSED_COPPER_DOOR);
/*  540 */             buildingBlocks.accept(Items.WAXED_EXPOSED_COPPER_TRAPDOOR);
/*  541 */             buildingBlocks.accept(Items.WAXED_EXPOSED_COPPER_BULB);
/*  542 */             buildingBlocks.accept(Items.COPPER_CHAIN.waxedExposed());
/*      */             
/*  544 */             buildingBlocks.accept(Items.WAXED_WEATHERED_COPPER);
/*  545 */             buildingBlocks.accept(Items.WAXED_WEATHERED_CHISELED_COPPER);
/*  546 */             buildingBlocks.accept(Items.WAXED_WEATHERED_COPPER_GRATE);
/*  547 */             buildingBlocks.accept(Items.WAXED_WEATHERED_CUT_COPPER);
/*  548 */             buildingBlocks.accept(Items.WAXED_WEATHERED_CUT_COPPER_STAIRS);
/*  549 */             buildingBlocks.accept(Items.WAXED_WEATHERED_CUT_COPPER_SLAB);
/*  550 */             buildingBlocks.accept(Items.COPPER_BARS.waxedWeathered());
/*  551 */             buildingBlocks.accept(Items.WAXED_WEATHERED_COPPER_DOOR);
/*  552 */             buildingBlocks.accept(Items.WAXED_WEATHERED_COPPER_TRAPDOOR);
/*  553 */             buildingBlocks.accept(Items.WAXED_WEATHERED_COPPER_BULB);
/*  554 */             buildingBlocks.accept(Items.COPPER_CHAIN.waxedWeathered());
/*      */             
/*  556 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_COPPER);
/*  557 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_CHISELED_COPPER);
/*  558 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_COPPER_GRATE);
/*  559 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_CUT_COPPER);
/*  560 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
/*  561 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB);
/*  562 */             buildingBlocks.accept(Items.COPPER_BARS.waxedOxidized());
/*  563 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_COPPER_DOOR);
/*  564 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_COPPER_TRAPDOOR);
/*  565 */             buildingBlocks.accept(Items.WAXED_OXIDIZED_COPPER_BULB);
/*  566 */             buildingBlocks.accept(Items.COPPER_CHAIN.waxedOxidized());
/*      */           
/*  568 */           }).build());
/*  569 */     Registry.register(registry, COLORED_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
/*  570 */         .title(Component.translatable("itemGroup.coloredBlocks"))
/*  571 */         .icon(() -> new ItemStack(Blocks.CYAN_WOOL))
/*  572 */         .displayItems((parameters, coloredBlocks) -> {
/*      */ 
/*      */ 
/*      */             
/*  576 */             coloredBlocks.accept(Items.WHITE_WOOL);
/*  577 */             coloredBlocks.accept(Items.LIGHT_GRAY_WOOL);
/*  578 */             coloredBlocks.accept(Items.GRAY_WOOL);
/*  579 */             coloredBlocks.accept(Items.BLACK_WOOL);
/*  580 */             coloredBlocks.accept(Items.BROWN_WOOL);
/*  581 */             coloredBlocks.accept(Items.RED_WOOL);
/*  582 */             coloredBlocks.accept(Items.ORANGE_WOOL);
/*  583 */             coloredBlocks.accept(Items.YELLOW_WOOL);
/*  584 */             coloredBlocks.accept(Items.LIME_WOOL);
/*  585 */             coloredBlocks.accept(Items.GREEN_WOOL);
/*  586 */             coloredBlocks.accept(Items.CYAN_WOOL);
/*  587 */             coloredBlocks.accept(Items.LIGHT_BLUE_WOOL);
/*  588 */             coloredBlocks.accept(Items.BLUE_WOOL);
/*  589 */             coloredBlocks.accept(Items.PURPLE_WOOL);
/*  590 */             coloredBlocks.accept(Items.MAGENTA_WOOL);
/*  591 */             coloredBlocks.accept(Items.PINK_WOOL);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  596 */             coloredBlocks.accept(Items.WHITE_CARPET);
/*  597 */             coloredBlocks.accept(Items.LIGHT_GRAY_CARPET);
/*  598 */             coloredBlocks.accept(Items.GRAY_CARPET);
/*  599 */             coloredBlocks.accept(Items.BLACK_CARPET);
/*  600 */             coloredBlocks.accept(Items.BROWN_CARPET);
/*  601 */             coloredBlocks.accept(Items.RED_CARPET);
/*  602 */             coloredBlocks.accept(Items.ORANGE_CARPET);
/*  603 */             coloredBlocks.accept(Items.YELLOW_CARPET);
/*  604 */             coloredBlocks.accept(Items.LIME_CARPET);
/*  605 */             coloredBlocks.accept(Items.GREEN_CARPET);
/*  606 */             coloredBlocks.accept(Items.CYAN_CARPET);
/*  607 */             coloredBlocks.accept(Items.LIGHT_BLUE_CARPET);
/*  608 */             coloredBlocks.accept(Items.BLUE_CARPET);
/*  609 */             coloredBlocks.accept(Items.PURPLE_CARPET);
/*  610 */             coloredBlocks.accept(Items.MAGENTA_CARPET);
/*  611 */             coloredBlocks.accept(Items.PINK_CARPET);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  616 */             coloredBlocks.accept(Items.TERRACOTTA);
/*  617 */             coloredBlocks.accept(Items.WHITE_TERRACOTTA);
/*  618 */             coloredBlocks.accept(Items.LIGHT_GRAY_TERRACOTTA);
/*  619 */             coloredBlocks.accept(Items.GRAY_TERRACOTTA);
/*  620 */             coloredBlocks.accept(Items.BLACK_TERRACOTTA);
/*  621 */             coloredBlocks.accept(Items.BROWN_TERRACOTTA);
/*  622 */             coloredBlocks.accept(Items.RED_TERRACOTTA);
/*  623 */             coloredBlocks.accept(Items.ORANGE_TERRACOTTA);
/*  624 */             coloredBlocks.accept(Items.YELLOW_TERRACOTTA);
/*  625 */             coloredBlocks.accept(Items.LIME_TERRACOTTA);
/*  626 */             coloredBlocks.accept(Items.GREEN_TERRACOTTA);
/*  627 */             coloredBlocks.accept(Items.CYAN_TERRACOTTA);
/*  628 */             coloredBlocks.accept(Items.LIGHT_BLUE_TERRACOTTA);
/*  629 */             coloredBlocks.accept(Items.BLUE_TERRACOTTA);
/*  630 */             coloredBlocks.accept(Items.PURPLE_TERRACOTTA);
/*  631 */             coloredBlocks.accept(Items.MAGENTA_TERRACOTTA);
/*  632 */             coloredBlocks.accept(Items.PINK_TERRACOTTA);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  637 */             coloredBlocks.accept(Items.WHITE_CONCRETE);
/*  638 */             coloredBlocks.accept(Items.LIGHT_GRAY_CONCRETE);
/*  639 */             coloredBlocks.accept(Items.GRAY_CONCRETE);
/*  640 */             coloredBlocks.accept(Items.BLACK_CONCRETE);
/*  641 */             coloredBlocks.accept(Items.BROWN_CONCRETE);
/*  642 */             coloredBlocks.accept(Items.RED_CONCRETE);
/*  643 */             coloredBlocks.accept(Items.ORANGE_CONCRETE);
/*  644 */             coloredBlocks.accept(Items.YELLOW_CONCRETE);
/*  645 */             coloredBlocks.accept(Items.LIME_CONCRETE);
/*  646 */             coloredBlocks.accept(Items.GREEN_CONCRETE);
/*  647 */             coloredBlocks.accept(Items.CYAN_CONCRETE);
/*  648 */             coloredBlocks.accept(Items.LIGHT_BLUE_CONCRETE);
/*  649 */             coloredBlocks.accept(Items.BLUE_CONCRETE);
/*  650 */             coloredBlocks.accept(Items.PURPLE_CONCRETE);
/*  651 */             coloredBlocks.accept(Items.MAGENTA_CONCRETE);
/*  652 */             coloredBlocks.accept(Items.PINK_CONCRETE);
/*  653 */             coloredBlocks.accept(Items.WHITE_CONCRETE_POWDER);
/*  654 */             coloredBlocks.accept(Items.LIGHT_GRAY_CONCRETE_POWDER);
/*  655 */             coloredBlocks.accept(Items.GRAY_CONCRETE_POWDER);
/*  656 */             coloredBlocks.accept(Items.BLACK_CONCRETE_POWDER);
/*  657 */             coloredBlocks.accept(Items.BROWN_CONCRETE_POWDER);
/*  658 */             coloredBlocks.accept(Items.RED_CONCRETE_POWDER);
/*  659 */             coloredBlocks.accept(Items.ORANGE_CONCRETE_POWDER);
/*  660 */             coloredBlocks.accept(Items.YELLOW_CONCRETE_POWDER);
/*  661 */             coloredBlocks.accept(Items.LIME_CONCRETE_POWDER);
/*  662 */             coloredBlocks.accept(Items.GREEN_CONCRETE_POWDER);
/*  663 */             coloredBlocks.accept(Items.CYAN_CONCRETE_POWDER);
/*  664 */             coloredBlocks.accept(Items.LIGHT_BLUE_CONCRETE_POWDER);
/*  665 */             coloredBlocks.accept(Items.BLUE_CONCRETE_POWDER);
/*  666 */             coloredBlocks.accept(Items.PURPLE_CONCRETE_POWDER);
/*  667 */             coloredBlocks.accept(Items.MAGENTA_CONCRETE_POWDER);
/*  668 */             coloredBlocks.accept(Items.PINK_CONCRETE_POWDER);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  673 */             coloredBlocks.accept(Items.WHITE_GLAZED_TERRACOTTA);
/*  674 */             coloredBlocks.accept(Items.LIGHT_GRAY_GLAZED_TERRACOTTA);
/*  675 */             coloredBlocks.accept(Items.GRAY_GLAZED_TERRACOTTA);
/*  676 */             coloredBlocks.accept(Items.BLACK_GLAZED_TERRACOTTA);
/*  677 */             coloredBlocks.accept(Items.BROWN_GLAZED_TERRACOTTA);
/*  678 */             coloredBlocks.accept(Items.RED_GLAZED_TERRACOTTA);
/*  679 */             coloredBlocks.accept(Items.ORANGE_GLAZED_TERRACOTTA);
/*  680 */             coloredBlocks.accept(Items.YELLOW_GLAZED_TERRACOTTA);
/*  681 */             coloredBlocks.accept(Items.LIME_GLAZED_TERRACOTTA);
/*  682 */             coloredBlocks.accept(Items.GREEN_GLAZED_TERRACOTTA);
/*  683 */             coloredBlocks.accept(Items.CYAN_GLAZED_TERRACOTTA);
/*  684 */             coloredBlocks.accept(Items.LIGHT_BLUE_GLAZED_TERRACOTTA);
/*  685 */             coloredBlocks.accept(Items.BLUE_GLAZED_TERRACOTTA);
/*  686 */             coloredBlocks.accept(Items.PURPLE_GLAZED_TERRACOTTA);
/*  687 */             coloredBlocks.accept(Items.MAGENTA_GLAZED_TERRACOTTA);
/*  688 */             coloredBlocks.accept(Items.PINK_GLAZED_TERRACOTTA);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  693 */             coloredBlocks.accept(Items.GLASS);
/*  694 */             coloredBlocks.accept(Items.TINTED_GLASS);
/*  695 */             coloredBlocks.accept(Items.WHITE_STAINED_GLASS);
/*  696 */             coloredBlocks.accept(Items.LIGHT_GRAY_STAINED_GLASS);
/*  697 */             coloredBlocks.accept(Items.GRAY_STAINED_GLASS);
/*  698 */             coloredBlocks.accept(Items.BLACK_STAINED_GLASS);
/*  699 */             coloredBlocks.accept(Items.BROWN_STAINED_GLASS);
/*  700 */             coloredBlocks.accept(Items.RED_STAINED_GLASS);
/*  701 */             coloredBlocks.accept(Items.ORANGE_STAINED_GLASS);
/*  702 */             coloredBlocks.accept(Items.YELLOW_STAINED_GLASS);
/*  703 */             coloredBlocks.accept(Items.LIME_STAINED_GLASS);
/*  704 */             coloredBlocks.accept(Items.GREEN_STAINED_GLASS);
/*  705 */             coloredBlocks.accept(Items.CYAN_STAINED_GLASS);
/*  706 */             coloredBlocks.accept(Items.LIGHT_BLUE_STAINED_GLASS);
/*  707 */             coloredBlocks.accept(Items.BLUE_STAINED_GLASS);
/*  708 */             coloredBlocks.accept(Items.PURPLE_STAINED_GLASS);
/*  709 */             coloredBlocks.accept(Items.MAGENTA_STAINED_GLASS);
/*  710 */             coloredBlocks.accept(Items.PINK_STAINED_GLASS);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  715 */             coloredBlocks.accept(Items.GLASS_PANE);
/*  716 */             coloredBlocks.accept(Items.WHITE_STAINED_GLASS_PANE);
/*  717 */             coloredBlocks.accept(Items.LIGHT_GRAY_STAINED_GLASS_PANE);
/*  718 */             coloredBlocks.accept(Items.GRAY_STAINED_GLASS_PANE);
/*  719 */             coloredBlocks.accept(Items.BLACK_STAINED_GLASS_PANE);
/*  720 */             coloredBlocks.accept(Items.BROWN_STAINED_GLASS_PANE);
/*  721 */             coloredBlocks.accept(Items.RED_STAINED_GLASS_PANE);
/*  722 */             coloredBlocks.accept(Items.ORANGE_STAINED_GLASS_PANE);
/*  723 */             coloredBlocks.accept(Items.YELLOW_STAINED_GLASS_PANE);
/*  724 */             coloredBlocks.accept(Items.LIME_STAINED_GLASS_PANE);
/*  725 */             coloredBlocks.accept(Items.GREEN_STAINED_GLASS_PANE);
/*  726 */             coloredBlocks.accept(Items.CYAN_STAINED_GLASS_PANE);
/*  727 */             coloredBlocks.accept(Items.LIGHT_BLUE_STAINED_GLASS_PANE);
/*  728 */             coloredBlocks.accept(Items.BLUE_STAINED_GLASS_PANE);
/*  729 */             coloredBlocks.accept(Items.PURPLE_STAINED_GLASS_PANE);
/*  730 */             coloredBlocks.accept(Items.MAGENTA_STAINED_GLASS_PANE);
/*  731 */             coloredBlocks.accept(Items.PINK_STAINED_GLASS_PANE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  736 */             coloredBlocks.accept(Items.SHULKER_BOX);
/*  737 */             coloredBlocks.accept(Items.WHITE_SHULKER_BOX);
/*  738 */             coloredBlocks.accept(Items.LIGHT_GRAY_SHULKER_BOX);
/*  739 */             coloredBlocks.accept(Items.GRAY_SHULKER_BOX);
/*  740 */             coloredBlocks.accept(Items.BLACK_SHULKER_BOX);
/*  741 */             coloredBlocks.accept(Items.BROWN_SHULKER_BOX);
/*  742 */             coloredBlocks.accept(Items.RED_SHULKER_BOX);
/*  743 */             coloredBlocks.accept(Items.ORANGE_SHULKER_BOX);
/*  744 */             coloredBlocks.accept(Items.YELLOW_SHULKER_BOX);
/*  745 */             coloredBlocks.accept(Items.LIME_SHULKER_BOX);
/*  746 */             coloredBlocks.accept(Items.GREEN_SHULKER_BOX);
/*  747 */             coloredBlocks.accept(Items.CYAN_SHULKER_BOX);
/*  748 */             coloredBlocks.accept(Items.LIGHT_BLUE_SHULKER_BOX);
/*  749 */             coloredBlocks.accept(Items.BLUE_SHULKER_BOX);
/*  750 */             coloredBlocks.accept(Items.PURPLE_SHULKER_BOX);
/*  751 */             coloredBlocks.accept(Items.MAGENTA_SHULKER_BOX);
/*  752 */             coloredBlocks.accept(Items.PINK_SHULKER_BOX);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  757 */             coloredBlocks.accept(Items.WHITE_BED);
/*  758 */             coloredBlocks.accept(Items.LIGHT_GRAY_BED);
/*  759 */             coloredBlocks.accept(Items.GRAY_BED);
/*  760 */             coloredBlocks.accept(Items.BLACK_BED);
/*  761 */             coloredBlocks.accept(Items.BROWN_BED);
/*  762 */             coloredBlocks.accept(Items.RED_BED);
/*  763 */             coloredBlocks.accept(Items.ORANGE_BED);
/*  764 */             coloredBlocks.accept(Items.YELLOW_BED);
/*  765 */             coloredBlocks.accept(Items.LIME_BED);
/*  766 */             coloredBlocks.accept(Items.GREEN_BED);
/*  767 */             coloredBlocks.accept(Items.CYAN_BED);
/*  768 */             coloredBlocks.accept(Items.LIGHT_BLUE_BED);
/*  769 */             coloredBlocks.accept(Items.BLUE_BED);
/*  770 */             coloredBlocks.accept(Items.PURPLE_BED);
/*  771 */             coloredBlocks.accept(Items.MAGENTA_BED);
/*  772 */             coloredBlocks.accept(Items.PINK_BED);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  777 */             coloredBlocks.accept(Items.CANDLE);
/*  778 */             coloredBlocks.accept(Items.WHITE_CANDLE);
/*  779 */             coloredBlocks.accept(Items.LIGHT_GRAY_CANDLE);
/*  780 */             coloredBlocks.accept(Items.GRAY_CANDLE);
/*  781 */             coloredBlocks.accept(Items.BLACK_CANDLE);
/*  782 */             coloredBlocks.accept(Items.BROWN_CANDLE);
/*  783 */             coloredBlocks.accept(Items.RED_CANDLE);
/*  784 */             coloredBlocks.accept(Items.ORANGE_CANDLE);
/*  785 */             coloredBlocks.accept(Items.YELLOW_CANDLE);
/*  786 */             coloredBlocks.accept(Items.LIME_CANDLE);
/*  787 */             coloredBlocks.accept(Items.GREEN_CANDLE);
/*  788 */             coloredBlocks.accept(Items.CYAN_CANDLE);
/*  789 */             coloredBlocks.accept(Items.LIGHT_BLUE_CANDLE);
/*  790 */             coloredBlocks.accept(Items.BLUE_CANDLE);
/*  791 */             coloredBlocks.accept(Items.PURPLE_CANDLE);
/*  792 */             coloredBlocks.accept(Items.MAGENTA_CANDLE);
/*  793 */             coloredBlocks.accept(Items.PINK_CANDLE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  798 */             coloredBlocks.accept(Items.WHITE_BANNER);
/*  799 */             coloredBlocks.accept(Items.LIGHT_GRAY_BANNER);
/*  800 */             coloredBlocks.accept(Items.GRAY_BANNER);
/*  801 */             coloredBlocks.accept(Items.BLACK_BANNER);
/*  802 */             coloredBlocks.accept(Items.BROWN_BANNER);
/*  803 */             coloredBlocks.accept(Items.RED_BANNER);
/*  804 */             coloredBlocks.accept(Items.ORANGE_BANNER);
/*  805 */             coloredBlocks.accept(Items.YELLOW_BANNER);
/*  806 */             coloredBlocks.accept(Items.LIME_BANNER);
/*  807 */             coloredBlocks.accept(Items.GREEN_BANNER);
/*  808 */             coloredBlocks.accept(Items.CYAN_BANNER);
/*  809 */             coloredBlocks.accept(Items.LIGHT_BLUE_BANNER);
/*  810 */             coloredBlocks.accept(Items.BLUE_BANNER);
/*  811 */             coloredBlocks.accept(Items.PURPLE_BANNER);
/*  812 */             coloredBlocks.accept(Items.MAGENTA_BANNER);
/*  813 */             coloredBlocks.accept(Items.PINK_BANNER);
/*      */           
/*  815 */           }).build());
/*  816 */     Registry.register(registry, NATURAL_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
/*  817 */         .title(Component.translatable("itemGroup.natural"))
/*  818 */         .icon(() -> new ItemStack(Blocks.GRASS_BLOCK))
/*  819 */         .displayItems((parameters, naturalBlocks) -> {
/*      */ 
/*      */ 
/*      */             
/*  823 */             naturalBlocks.accept(Items.GRASS_BLOCK);
/*  824 */             naturalBlocks.accept(Items.PODZOL);
/*  825 */             naturalBlocks.accept(Items.MYCELIUM);
/*  826 */             naturalBlocks.accept(Items.DIRT_PATH);
/*  827 */             naturalBlocks.accept(Items.DIRT);
/*  828 */             naturalBlocks.accept(Items.COARSE_DIRT);
/*  829 */             naturalBlocks.accept(Items.ROOTED_DIRT);
/*  830 */             naturalBlocks.accept(Items.FARMLAND);
/*  831 */             naturalBlocks.accept(Items.MUD);
/*  832 */             naturalBlocks.accept(Items.CLAY);
/*  833 */             naturalBlocks.accept(Items.GRAVEL);
/*  834 */             naturalBlocks.accept(Items.SAND);
/*  835 */             naturalBlocks.accept(Items.SANDSTONE);
/*  836 */             naturalBlocks.accept(Items.RED_SAND);
/*  837 */             naturalBlocks.accept(Items.RED_SANDSTONE);
/*  838 */             naturalBlocks.accept(Items.ICE);
/*  839 */             naturalBlocks.accept(Items.PACKED_ICE);
/*  840 */             naturalBlocks.accept(Items.BLUE_ICE);
/*  841 */             naturalBlocks.accept(Items.SNOW_BLOCK);
/*  842 */             naturalBlocks.accept(Items.SNOW);
/*  843 */             naturalBlocks.accept(Items.MOSS_BLOCK);
/*  844 */             naturalBlocks.accept(Items.MOSS_CARPET);
/*  845 */             naturalBlocks.accept(Items.PALE_MOSS_BLOCK);
/*  846 */             naturalBlocks.accept(Items.PALE_MOSS_CARPET);
/*  847 */             naturalBlocks.accept(Items.PALE_HANGING_MOSS);
/*      */ 
/*      */ 
/*      */             
/*  851 */             naturalBlocks.accept(Items.STONE);
/*  852 */             naturalBlocks.accept(Items.DEEPSLATE);
/*  853 */             naturalBlocks.accept(Items.GRANITE);
/*  854 */             naturalBlocks.accept(Items.DIORITE);
/*  855 */             naturalBlocks.accept(Items.ANDESITE);
/*  856 */             naturalBlocks.accept(Items.CALCITE);
/*  857 */             naturalBlocks.accept(Items.TUFF);
/*  858 */             naturalBlocks.accept(Items.DRIPSTONE_BLOCK);
/*  859 */             naturalBlocks.accept(Items.POINTED_DRIPSTONE);
/*  860 */             naturalBlocks.accept(Items.PRISMARINE);
/*  861 */             naturalBlocks.accept(Items.MAGMA_BLOCK);
/*  862 */             naturalBlocks.accept(Items.OBSIDIAN);
/*  863 */             naturalBlocks.accept(Items.CRYING_OBSIDIAN);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  868 */             naturalBlocks.accept(Items.NETHERRACK);
/*  869 */             naturalBlocks.accept(Items.CRIMSON_NYLIUM);
/*  870 */             naturalBlocks.accept(Items.WARPED_NYLIUM);
/*  871 */             naturalBlocks.accept(Items.SOUL_SAND);
/*  872 */             naturalBlocks.accept(Items.SOUL_SOIL);
/*  873 */             naturalBlocks.accept(Items.BONE_BLOCK);
/*  874 */             naturalBlocks.accept(Items.BLACKSTONE);
/*  875 */             naturalBlocks.accept(Items.BASALT);
/*  876 */             naturalBlocks.accept(Items.SMOOTH_BASALT);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  881 */             naturalBlocks.accept(Items.END_STONE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  886 */             naturalBlocks.accept(Items.COAL_ORE);
/*  887 */             naturalBlocks.accept(Items.DEEPSLATE_COAL_ORE);
/*  888 */             naturalBlocks.accept(Items.IRON_ORE);
/*  889 */             naturalBlocks.accept(Items.DEEPSLATE_IRON_ORE);
/*  890 */             naturalBlocks.accept(Items.COPPER_ORE);
/*  891 */             naturalBlocks.accept(Items.DEEPSLATE_COPPER_ORE);
/*  892 */             naturalBlocks.accept(Items.GOLD_ORE);
/*  893 */             naturalBlocks.accept(Items.DEEPSLATE_GOLD_ORE);
/*  894 */             naturalBlocks.accept(Items.REDSTONE_ORE);
/*  895 */             naturalBlocks.accept(Items.DEEPSLATE_REDSTONE_ORE);
/*  896 */             naturalBlocks.accept(Items.EMERALD_ORE);
/*  897 */             naturalBlocks.accept(Items.DEEPSLATE_EMERALD_ORE);
/*  898 */             naturalBlocks.accept(Items.LAPIS_ORE);
/*  899 */             naturalBlocks.accept(Items.DEEPSLATE_LAPIS_ORE);
/*  900 */             naturalBlocks.accept(Items.DIAMOND_ORE);
/*  901 */             naturalBlocks.accept(Items.DEEPSLATE_DIAMOND_ORE);
/*  902 */             naturalBlocks.accept(Items.NETHER_GOLD_ORE);
/*  903 */             naturalBlocks.accept(Items.NETHER_QUARTZ_ORE);
/*  904 */             naturalBlocks.accept(Items.ANCIENT_DEBRIS);
/*  905 */             naturalBlocks.accept(Items.RAW_IRON_BLOCK);
/*  906 */             naturalBlocks.accept(Items.RAW_COPPER_BLOCK);
/*  907 */             naturalBlocks.accept(Items.RAW_GOLD_BLOCK);
/*  908 */             naturalBlocks.accept(Items.GLOWSTONE);
/*  909 */             naturalBlocks.accept(Items.AMETHYST_BLOCK);
/*  910 */             naturalBlocks.accept(Items.BUDDING_AMETHYST);
/*  911 */             naturalBlocks.accept(Items.SMALL_AMETHYST_BUD);
/*  912 */             naturalBlocks.accept(Items.MEDIUM_AMETHYST_BUD);
/*  913 */             naturalBlocks.accept(Items.LARGE_AMETHYST_BUD);
/*  914 */             naturalBlocks.accept(Items.AMETHYST_CLUSTER);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  921 */             naturalBlocks.accept(Items.OAK_LOG);
/*  922 */             naturalBlocks.accept(Items.SPRUCE_LOG);
/*  923 */             naturalBlocks.accept(Items.BIRCH_LOG);
/*  924 */             naturalBlocks.accept(Items.JUNGLE_LOG);
/*  925 */             naturalBlocks.accept(Items.ACACIA_LOG);
/*  926 */             naturalBlocks.accept(Items.DARK_OAK_LOG);
/*  927 */             naturalBlocks.accept(Items.MANGROVE_LOG);
/*  928 */             naturalBlocks.accept(Items.MANGROVE_ROOTS);
/*  929 */             naturalBlocks.accept(Items.MUDDY_MANGROVE_ROOTS);
/*  930 */             naturalBlocks.accept(Items.CHERRY_LOG);
/*  931 */             naturalBlocks.accept(Items.PALE_OAK_LOG);
/*  932 */             naturalBlocks.accept(Items.MUSHROOM_STEM);
/*  933 */             naturalBlocks.accept(Items.CRIMSON_STEM);
/*  934 */             naturalBlocks.accept(Items.WARPED_STEM);
/*      */ 
/*      */             
/*  937 */             naturalBlocks.accept(Items.OAK_LEAVES);
/*  938 */             naturalBlocks.accept(Items.SPRUCE_LEAVES);
/*  939 */             naturalBlocks.accept(Items.BIRCH_LEAVES);
/*  940 */             naturalBlocks.accept(Items.JUNGLE_LEAVES);
/*  941 */             naturalBlocks.accept(Items.ACACIA_LEAVES);
/*  942 */             naturalBlocks.accept(Items.DARK_OAK_LEAVES);
/*  943 */             naturalBlocks.accept(Items.MANGROVE_LEAVES);
/*  944 */             naturalBlocks.accept(Items.CHERRY_LEAVES);
/*  945 */             naturalBlocks.accept(Items.PALE_OAK_LEAVES);
/*  946 */             naturalBlocks.accept(Items.AZALEA_LEAVES);
/*  947 */             naturalBlocks.accept(Items.FLOWERING_AZALEA_LEAVES);
/*  948 */             naturalBlocks.accept(Items.BROWN_MUSHROOM_BLOCK);
/*  949 */             naturalBlocks.accept(Items.RED_MUSHROOM_BLOCK);
/*  950 */             naturalBlocks.accept(Items.NETHER_WART_BLOCK);
/*  951 */             naturalBlocks.accept(Items.WARPED_WART_BLOCK);
/*  952 */             naturalBlocks.accept(Items.SHROOMLIGHT);
/*      */ 
/*      */             
/*  955 */             naturalBlocks.accept(Items.OAK_SAPLING);
/*  956 */             naturalBlocks.accept(Items.SPRUCE_SAPLING);
/*  957 */             naturalBlocks.accept(Items.BIRCH_SAPLING);
/*  958 */             naturalBlocks.accept(Items.JUNGLE_SAPLING);
/*  959 */             naturalBlocks.accept(Items.ACACIA_SAPLING);
/*  960 */             naturalBlocks.accept(Items.DARK_OAK_SAPLING);
/*  961 */             naturalBlocks.accept(Items.MANGROVE_PROPAGULE);
/*  962 */             naturalBlocks.accept(Items.CHERRY_SAPLING);
/*  963 */             naturalBlocks.accept(Items.PALE_OAK_SAPLING);
/*  964 */             naturalBlocks.accept(Items.AZALEA);
/*  965 */             naturalBlocks.accept(Items.FLOWERING_AZALEA);
/*  966 */             naturalBlocks.accept(Items.BROWN_MUSHROOM);
/*  967 */             naturalBlocks.accept(Items.RED_MUSHROOM);
/*  968 */             naturalBlocks.accept(Items.CRIMSON_FUNGUS);
/*  969 */             naturalBlocks.accept(Items.WARPED_FUNGUS);
/*      */ 
/*      */             
/*  972 */             naturalBlocks.accept(Items.SHORT_GRASS);
/*  973 */             naturalBlocks.accept(Items.FERN);
/*  974 */             naturalBlocks.accept(Items.DRY_SHORT_GRASS);
/*  975 */             naturalBlocks.accept(Items.BUSH);
/*  976 */             naturalBlocks.accept(Items.DEAD_BUSH);
/*  977 */             naturalBlocks.accept(Items.DANDELION);
/*  978 */             naturalBlocks.accept(Items.POPPY);
/*  979 */             naturalBlocks.accept(Items.BLUE_ORCHID);
/*  980 */             naturalBlocks.accept(Items.ALLIUM);
/*  981 */             naturalBlocks.accept(Items.AZURE_BLUET);
/*  982 */             naturalBlocks.accept(Items.RED_TULIP);
/*  983 */             naturalBlocks.accept(Items.ORANGE_TULIP);
/*  984 */             naturalBlocks.accept(Items.WHITE_TULIP);
/*  985 */             naturalBlocks.accept(Items.PINK_TULIP);
/*  986 */             naturalBlocks.accept(Items.OXEYE_DAISY);
/*  987 */             naturalBlocks.accept(Items.CORNFLOWER);
/*  988 */             naturalBlocks.accept(Items.LILY_OF_THE_VALLEY);
/*  989 */             naturalBlocks.accept(Items.TORCHFLOWER);
/*  990 */             naturalBlocks.accept(Items.CACTUS_FLOWER);
/*  991 */             naturalBlocks.accept(Items.CLOSED_EYEBLOSSOM);
/*  992 */             naturalBlocks.accept(Items.OPEN_EYEBLOSSOM);
/*  993 */             naturalBlocks.accept(Items.WITHER_ROSE);
/*  994 */             naturalBlocks.accept(Items.PINK_PETALS);
/*  995 */             naturalBlocks.accept(Items.WILDFLOWERS);
/*  996 */             naturalBlocks.accept(Items.LEAF_LITTER);
/*  997 */             naturalBlocks.accept(Items.SPORE_BLOSSOM);
/*  998 */             naturalBlocks.accept(Items.FIREFLY_BUSH);
/*  999 */             naturalBlocks.accept(Items.BAMBOO);
/* 1000 */             naturalBlocks.accept(Items.SUGAR_CANE);
/* 1001 */             naturalBlocks.accept(Items.CACTUS);
/* 1002 */             naturalBlocks.accept(Items.CRIMSON_ROOTS);
/* 1003 */             naturalBlocks.accept(Items.WARPED_ROOTS);
/* 1004 */             naturalBlocks.accept(Items.NETHER_SPROUTS);
/*      */ 
/*      */             
/* 1007 */             naturalBlocks.accept(Items.WEEPING_VINES);
/* 1008 */             naturalBlocks.accept(Items.TWISTING_VINES);
/* 1009 */             naturalBlocks.accept(Items.VINE);
/*      */ 
/*      */             
/* 1012 */             naturalBlocks.accept(Items.TALL_GRASS);
/* 1013 */             naturalBlocks.accept(Items.LARGE_FERN);
/* 1014 */             naturalBlocks.accept(Items.DRY_TALL_GRASS);
/* 1015 */             naturalBlocks.accept(Items.SUNFLOWER);
/* 1016 */             naturalBlocks.accept(Items.LILAC);
/* 1017 */             naturalBlocks.accept(Items.ROSE_BUSH);
/* 1018 */             naturalBlocks.accept(Items.PEONY);
/* 1019 */             naturalBlocks.accept(Items.PITCHER_PLANT);
/* 1020 */             naturalBlocks.accept(Items.BIG_DRIPLEAF);
/* 1021 */             naturalBlocks.accept(Items.SMALL_DRIPLEAF);
/* 1022 */             naturalBlocks.accept(Items.CHORUS_PLANT);
/* 1023 */             naturalBlocks.accept(Items.CHORUS_FLOWER);
/*      */ 
/*      */             
/* 1026 */             naturalBlocks.accept(Items.GLOW_LICHEN);
/* 1027 */             naturalBlocks.accept(Items.HANGING_ROOTS);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1032 */             naturalBlocks.accept(Items.FROGSPAWN);
/* 1033 */             naturalBlocks.accept(Items.TURTLE_EGG);
/* 1034 */             naturalBlocks.accept(Items.SNIFFER_EGG);
/* 1035 */             naturalBlocks.accept(Items.DRIED_GHAST);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1040 */             naturalBlocks.accept(Items.WHEAT_SEEDS);
/* 1041 */             naturalBlocks.accept(Items.COCOA_BEANS);
/* 1042 */             naturalBlocks.accept(Items.PUMPKIN_SEEDS);
/* 1043 */             naturalBlocks.accept(Items.MELON_SEEDS);
/* 1044 */             naturalBlocks.accept(Items.BEETROOT_SEEDS);
/* 1045 */             naturalBlocks.accept(Items.TORCHFLOWER_SEEDS);
/* 1046 */             naturalBlocks.accept(Items.PITCHER_POD);
/* 1047 */             naturalBlocks.accept(Items.GLOW_BERRIES);
/* 1048 */             naturalBlocks.accept(Items.SWEET_BERRIES);
/* 1049 */             naturalBlocks.accept(Items.NETHER_WART);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1054 */             naturalBlocks.accept(Items.LILY_PAD);
/* 1055 */             naturalBlocks.accept(Items.SEAGRASS);
/* 1056 */             naturalBlocks.accept(Items.SEA_PICKLE);
/* 1057 */             naturalBlocks.accept(Items.KELP);
/* 1058 */             naturalBlocks.accept(Items.DRIED_KELP_BLOCK);
/* 1059 */             naturalBlocks.accept(Items.TUBE_CORAL_BLOCK);
/* 1060 */             naturalBlocks.accept(Items.BRAIN_CORAL_BLOCK);
/* 1061 */             naturalBlocks.accept(Items.BUBBLE_CORAL_BLOCK);
/* 1062 */             naturalBlocks.accept(Items.FIRE_CORAL_BLOCK);
/* 1063 */             naturalBlocks.accept(Items.HORN_CORAL_BLOCK);
/* 1064 */             naturalBlocks.accept(Items.DEAD_TUBE_CORAL_BLOCK);
/* 1065 */             naturalBlocks.accept(Items.DEAD_BRAIN_CORAL_BLOCK);
/* 1066 */             naturalBlocks.accept(Items.DEAD_BUBBLE_CORAL_BLOCK);
/* 1067 */             naturalBlocks.accept(Items.DEAD_FIRE_CORAL_BLOCK);
/* 1068 */             naturalBlocks.accept(Items.DEAD_HORN_CORAL_BLOCK);
/* 1069 */             naturalBlocks.accept(Items.TUBE_CORAL);
/* 1070 */             naturalBlocks.accept(Items.BRAIN_CORAL);
/* 1071 */             naturalBlocks.accept(Items.BUBBLE_CORAL);
/* 1072 */             naturalBlocks.accept(Items.FIRE_CORAL);
/* 1073 */             naturalBlocks.accept(Items.HORN_CORAL);
/* 1074 */             naturalBlocks.accept(Items.DEAD_TUBE_CORAL);
/* 1075 */             naturalBlocks.accept(Items.DEAD_BRAIN_CORAL);
/* 1076 */             naturalBlocks.accept(Items.DEAD_BUBBLE_CORAL);
/* 1077 */             naturalBlocks.accept(Items.DEAD_FIRE_CORAL);
/* 1078 */             naturalBlocks.accept(Items.DEAD_HORN_CORAL);
/* 1079 */             naturalBlocks.accept(Items.TUBE_CORAL_FAN);
/* 1080 */             naturalBlocks.accept(Items.BRAIN_CORAL_FAN);
/* 1081 */             naturalBlocks.accept(Items.BUBBLE_CORAL_FAN);
/* 1082 */             naturalBlocks.accept(Items.FIRE_CORAL_FAN);
/* 1083 */             naturalBlocks.accept(Items.HORN_CORAL_FAN);
/* 1084 */             naturalBlocks.accept(Items.DEAD_TUBE_CORAL_FAN);
/* 1085 */             naturalBlocks.accept(Items.DEAD_BRAIN_CORAL_FAN);
/* 1086 */             naturalBlocks.accept(Items.DEAD_BUBBLE_CORAL_FAN);
/* 1087 */             naturalBlocks.accept(Items.DEAD_FIRE_CORAL_FAN);
/* 1088 */             naturalBlocks.accept(Items.DEAD_HORN_CORAL_FAN);
/* 1089 */             naturalBlocks.accept(Items.SPONGE);
/* 1090 */             naturalBlocks.accept(Items.WET_SPONGE);
/*      */ 
/*      */             
/* 1093 */             naturalBlocks.accept(Items.MELON);
/* 1094 */             naturalBlocks.accept(Items.PUMPKIN);
/* 1095 */             naturalBlocks.accept(Items.CARVED_PUMPKIN);
/* 1096 */             naturalBlocks.accept(Items.JACK_O_LANTERN);
/* 1097 */             naturalBlocks.accept(Items.HAY_BLOCK);
/* 1098 */             naturalBlocks.accept(Items.BEE_NEST);
/* 1099 */             naturalBlocks.accept(Items.HONEYCOMB_BLOCK);
/* 1100 */             naturalBlocks.accept(Items.SLIME_BLOCK);
/* 1101 */             naturalBlocks.accept(Items.HONEY_BLOCK);
/* 1102 */             naturalBlocks.accept(Items.RESIN_BLOCK);
/* 1103 */             naturalBlocks.accept(Items.OCHRE_FROGLIGHT);
/* 1104 */             naturalBlocks.accept(Items.VERDANT_FROGLIGHT);
/* 1105 */             naturalBlocks.accept(Items.PEARLESCENT_FROGLIGHT);
/* 1106 */             naturalBlocks.accept(Items.SCULK);
/* 1107 */             naturalBlocks.accept(Items.SCULK_VEIN);
/* 1108 */             naturalBlocks.accept(Items.SCULK_CATALYST);
/* 1109 */             naturalBlocks.accept(Items.SCULK_SHRIEKER);
/* 1110 */             naturalBlocks.accept(Items.SCULK_SENSOR);
/* 1111 */             naturalBlocks.accept(Items.COBWEB);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1116 */             naturalBlocks.accept(Items.BEDROCK);
/*      */           
/* 1118 */           }).build());
/* 1119 */     Registry.register(registry, FUNCTIONAL_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 3)
/* 1120 */         .title(Component.translatable("itemGroup.functional"))
/* 1121 */         .icon(() -> new ItemStack(Items.OAK_SIGN))
/* 1122 */         .displayItems((parameters, functionalBlocks) -> {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1127 */             functionalBlocks.accept(Items.TORCH);
/* 1128 */             functionalBlocks.accept(Items.SOUL_TORCH);
/* 1129 */             functionalBlocks.accept(Items.COPPER_TORCH);
/* 1130 */             functionalBlocks.accept(Items.REDSTONE_TORCH);
/* 1131 */             functionalBlocks.accept(Items.LANTERN);
/* 1132 */             functionalBlocks.accept(Items.SOUL_LANTERN);
/* 1133 */             Objects.requireNonNull(functionalBlocks); Items.COPPER_LANTERN.forEach(functionalBlocks::accept);
/* 1134 */             functionalBlocks.accept(Items.IRON_CHAIN);
/* 1135 */             Objects.requireNonNull(functionalBlocks); Items.COPPER_CHAIN.forEach(functionalBlocks::accept);
/* 1136 */             functionalBlocks.accept(Items.END_ROD);
/* 1137 */             functionalBlocks.accept(Items.SEA_LANTERN);
/* 1138 */             functionalBlocks.accept(Items.REDSTONE_LAMP);
/* 1139 */             functionalBlocks.accept(Items.COPPER_BULB);
/* 1140 */             functionalBlocks.accept(Items.EXPOSED_COPPER_BULB);
/* 1141 */             functionalBlocks.accept(Items.WEATHERED_COPPER_BULB);
/* 1142 */             functionalBlocks.accept(Items.OXIDIZED_COPPER_BULB);
/* 1143 */             functionalBlocks.accept(Items.WAXED_COPPER_BULB);
/* 1144 */             functionalBlocks.accept(Items.WAXED_EXPOSED_COPPER_BULB);
/* 1145 */             functionalBlocks.accept(Items.WAXED_WEATHERED_COPPER_BULB);
/* 1146 */             functionalBlocks.accept(Items.WAXED_OXIDIZED_COPPER_BULB);
/* 1147 */             functionalBlocks.accept(Items.GLOWSTONE);
/* 1148 */             functionalBlocks.accept(Items.SHROOMLIGHT);
/* 1149 */             functionalBlocks.accept(Items.OCHRE_FROGLIGHT);
/* 1150 */             functionalBlocks.accept(Items.VERDANT_FROGLIGHT);
/* 1151 */             functionalBlocks.accept(Items.PEARLESCENT_FROGLIGHT);
/* 1152 */             functionalBlocks.accept(Items.CRYING_OBSIDIAN);
/* 1153 */             functionalBlocks.accept(Items.GLOW_LICHEN);
/* 1154 */             functionalBlocks.accept(Items.MAGMA_BLOCK);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1159 */             functionalBlocks.accept(Items.CRAFTING_TABLE);
/* 1160 */             functionalBlocks.accept(Items.STONECUTTER);
/* 1161 */             functionalBlocks.accept(Items.CARTOGRAPHY_TABLE);
/* 1162 */             functionalBlocks.accept(Items.FLETCHING_TABLE);
/* 1163 */             functionalBlocks.accept(Items.SMITHING_TABLE);
/* 1164 */             functionalBlocks.accept(Items.GRINDSTONE);
/* 1165 */             functionalBlocks.accept(Items.LOOM);
/* 1166 */             functionalBlocks.accept(Items.FURNACE);
/* 1167 */             functionalBlocks.accept(Items.SMOKER);
/* 1168 */             functionalBlocks.accept(Items.BLAST_FURNACE);
/* 1169 */             functionalBlocks.accept(Items.CAMPFIRE);
/* 1170 */             functionalBlocks.accept(Items.SOUL_CAMPFIRE);
/* 1171 */             functionalBlocks.accept(Items.ANVIL);
/* 1172 */             functionalBlocks.accept(Items.CHIPPED_ANVIL);
/* 1173 */             functionalBlocks.accept(Items.DAMAGED_ANVIL);
/* 1174 */             functionalBlocks.accept(Items.COMPOSTER);
/* 1175 */             functionalBlocks.accept(Items.NOTE_BLOCK);
/* 1176 */             functionalBlocks.accept(Items.JUKEBOX);
/* 1177 */             functionalBlocks.accept(Items.ENCHANTING_TABLE);
/* 1178 */             functionalBlocks.accept(Items.END_CRYSTAL);
/* 1179 */             functionalBlocks.accept(Items.BREWING_STAND);
/* 1180 */             functionalBlocks.accept(Items.CAULDRON);
/* 1181 */             functionalBlocks.accept(Items.BELL);
/* 1182 */             functionalBlocks.accept(Items.BEACON);
/* 1183 */             functionalBlocks.accept(Items.CONDUIT);
/* 1184 */             functionalBlocks.accept(Items.LODESTONE);
/* 1185 */             functionalBlocks.accept(Items.LADDER);
/* 1186 */             functionalBlocks.accept(Items.SCAFFOLDING);
/* 1187 */             functionalBlocks.accept(Items.BEE_NEST);
/* 1188 */             functionalBlocks.accept(Items.BEEHIVE);
/* 1189 */             functionalBlocks.accept(Items.SUSPICIOUS_SAND);
/* 1190 */             functionalBlocks.accept(Items.SUSPICIOUS_GRAVEL);
/* 1191 */             functionalBlocks.accept(Items.LIGHTNING_ROD);
/* 1192 */             functionalBlocks.accept(Items.EXPOSED_LIGHTNING_ROD);
/* 1193 */             functionalBlocks.accept(Items.WEATHERED_LIGHTNING_ROD);
/* 1194 */             functionalBlocks.accept(Items.OXIDIZED_LIGHTNING_ROD);
/* 1195 */             functionalBlocks.accept(Items.WAXED_LIGHTNING_ROD);
/* 1196 */             functionalBlocks.accept(Items.WAXED_EXPOSED_LIGHTNING_ROD);
/* 1197 */             functionalBlocks.accept(Items.WAXED_WEATHERED_LIGHTNING_ROD);
/* 1198 */             functionalBlocks.accept(Items.WAXED_OXIDIZED_LIGHTNING_ROD);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1203 */             functionalBlocks.accept(Items.FLOWER_POT);
/* 1204 */             functionalBlocks.accept(Items.DECORATED_POT);
/* 1205 */             functionalBlocks.accept(Items.ARMOR_STAND);
/* 1206 */             functionalBlocks.accept(Items.ITEM_FRAME);
/* 1207 */             functionalBlocks.accept(Items.GLOW_ITEM_FRAME);
/* 1208 */             functionalBlocks.accept(Items.PAINTING);
/*      */             
/* 1210 */             parameters.holders().lookup(Registries.PAINTING_VARIANT).ifPresent(());
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1215 */             functionalBlocks.accept(Items.BOOKSHELF);
/* 1216 */             functionalBlocks.accept(Items.CHISELED_BOOKSHELF);
/* 1217 */             functionalBlocks.accept(Items.OAK_SHELF);
/* 1218 */             functionalBlocks.accept(Items.SPRUCE_SHELF);
/* 1219 */             functionalBlocks.accept(Items.BIRCH_SHELF);
/* 1220 */             functionalBlocks.accept(Items.JUNGLE_SHELF);
/* 1221 */             functionalBlocks.accept(Items.ACACIA_SHELF);
/* 1222 */             functionalBlocks.accept(Items.DARK_OAK_SHELF);
/* 1223 */             functionalBlocks.accept(Items.MANGROVE_SHELF);
/* 1224 */             functionalBlocks.accept(Items.CHERRY_SHELF);
/* 1225 */             functionalBlocks.accept(Items.PALE_OAK_SHELF);
/* 1226 */             functionalBlocks.accept(Items.BAMBOO_SHELF);
/* 1227 */             functionalBlocks.accept(Items.CRIMSON_SHELF);
/* 1228 */             functionalBlocks.accept(Items.WARPED_SHELF);
/* 1229 */             functionalBlocks.accept(Items.LECTERN);
/* 1230 */             functionalBlocks.accept(Items.TINTED_GLASS);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1235 */             functionalBlocks.accept(Items.OAK_SIGN);
/* 1236 */             functionalBlocks.accept(Items.OAK_HANGING_SIGN);
/* 1237 */             functionalBlocks.accept(Items.SPRUCE_SIGN);
/* 1238 */             functionalBlocks.accept(Items.SPRUCE_HANGING_SIGN);
/* 1239 */             functionalBlocks.accept(Items.BIRCH_SIGN);
/* 1240 */             functionalBlocks.accept(Items.BIRCH_HANGING_SIGN);
/* 1241 */             functionalBlocks.accept(Items.JUNGLE_SIGN);
/* 1242 */             functionalBlocks.accept(Items.JUNGLE_HANGING_SIGN);
/* 1243 */             functionalBlocks.accept(Items.ACACIA_SIGN);
/* 1244 */             functionalBlocks.accept(Items.ACACIA_HANGING_SIGN);
/* 1245 */             functionalBlocks.accept(Items.DARK_OAK_SIGN);
/* 1246 */             functionalBlocks.accept(Items.DARK_OAK_HANGING_SIGN);
/* 1247 */             functionalBlocks.accept(Items.MANGROVE_SIGN);
/* 1248 */             functionalBlocks.accept(Items.MANGROVE_HANGING_SIGN);
/* 1249 */             functionalBlocks.accept(Items.CHERRY_SIGN);
/* 1250 */             functionalBlocks.accept(Items.CHERRY_HANGING_SIGN);
/* 1251 */             functionalBlocks.accept(Items.PALE_OAK_SIGN);
/* 1252 */             functionalBlocks.accept(Items.PALE_OAK_HANGING_SIGN);
/* 1253 */             functionalBlocks.accept(Items.BAMBOO_SIGN);
/* 1254 */             functionalBlocks.accept(Items.BAMBOO_HANGING_SIGN);
/* 1255 */             functionalBlocks.accept(Items.CRIMSON_SIGN);
/* 1256 */             functionalBlocks.accept(Items.CRIMSON_HANGING_SIGN);
/* 1257 */             functionalBlocks.accept(Items.WARPED_SIGN);
/* 1258 */             functionalBlocks.accept(Items.WARPED_HANGING_SIGN);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1263 */             functionalBlocks.accept(Items.CHEST);
/* 1264 */             functionalBlocks.accept(Items.COPPER_CHEST);
/* 1265 */             functionalBlocks.accept(Items.EXPOSED_COPPER_CHEST);
/* 1266 */             functionalBlocks.accept(Items.WEATHERED_COPPER_CHEST);
/* 1267 */             functionalBlocks.accept(Items.OXIDIZED_COPPER_CHEST);
/* 1268 */             functionalBlocks.accept(Items.WAXED_COPPER_CHEST);
/* 1269 */             functionalBlocks.accept(Items.WAXED_EXPOSED_COPPER_CHEST);
/* 1270 */             functionalBlocks.accept(Items.WAXED_WEATHERED_COPPER_CHEST);
/* 1271 */             functionalBlocks.accept(Items.WAXED_OXIDIZED_COPPER_CHEST);
/* 1272 */             functionalBlocks.accept(Items.BARREL);
/* 1273 */             functionalBlocks.accept(Items.ENDER_CHEST);
/* 1274 */             functionalBlocks.accept(Items.SHULKER_BOX);
/* 1275 */             functionalBlocks.accept(Items.WHITE_SHULKER_BOX);
/* 1276 */             functionalBlocks.accept(Items.LIGHT_GRAY_SHULKER_BOX);
/* 1277 */             functionalBlocks.accept(Items.GRAY_SHULKER_BOX);
/* 1278 */             functionalBlocks.accept(Items.BLACK_SHULKER_BOX);
/* 1279 */             functionalBlocks.accept(Items.BROWN_SHULKER_BOX);
/* 1280 */             functionalBlocks.accept(Items.RED_SHULKER_BOX);
/* 1281 */             functionalBlocks.accept(Items.ORANGE_SHULKER_BOX);
/* 1282 */             functionalBlocks.accept(Items.YELLOW_SHULKER_BOX);
/* 1283 */             functionalBlocks.accept(Items.LIME_SHULKER_BOX);
/* 1284 */             functionalBlocks.accept(Items.GREEN_SHULKER_BOX);
/* 1285 */             functionalBlocks.accept(Items.CYAN_SHULKER_BOX);
/* 1286 */             functionalBlocks.accept(Items.LIGHT_BLUE_SHULKER_BOX);
/* 1287 */             functionalBlocks.accept(Items.BLUE_SHULKER_BOX);
/* 1288 */             functionalBlocks.accept(Items.PURPLE_SHULKER_BOX);
/* 1289 */             functionalBlocks.accept(Items.MAGENTA_SHULKER_BOX);
/* 1290 */             functionalBlocks.accept(Items.PINK_SHULKER_BOX);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1295 */             functionalBlocks.accept(Items.RESPAWN_ANCHOR);
/* 1296 */             functionalBlocks.accept(Items.WHITE_BED);
/* 1297 */             functionalBlocks.accept(Items.LIGHT_GRAY_BED);
/* 1298 */             functionalBlocks.accept(Items.GRAY_BED);
/* 1299 */             functionalBlocks.accept(Items.BLACK_BED);
/* 1300 */             functionalBlocks.accept(Items.BROWN_BED);
/* 1301 */             functionalBlocks.accept(Items.RED_BED);
/* 1302 */             functionalBlocks.accept(Items.ORANGE_BED);
/* 1303 */             functionalBlocks.accept(Items.YELLOW_BED);
/* 1304 */             functionalBlocks.accept(Items.LIME_BED);
/* 1305 */             functionalBlocks.accept(Items.GREEN_BED);
/* 1306 */             functionalBlocks.accept(Items.CYAN_BED);
/* 1307 */             functionalBlocks.accept(Items.LIGHT_BLUE_BED);
/* 1308 */             functionalBlocks.accept(Items.BLUE_BED);
/* 1309 */             functionalBlocks.accept(Items.PURPLE_BED);
/* 1310 */             functionalBlocks.accept(Items.MAGENTA_BED);
/* 1311 */             functionalBlocks.accept(Items.PINK_BED);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1316 */             functionalBlocks.accept(Items.CANDLE);
/* 1317 */             functionalBlocks.accept(Items.WHITE_CANDLE);
/* 1318 */             functionalBlocks.accept(Items.LIGHT_GRAY_CANDLE);
/* 1319 */             functionalBlocks.accept(Items.GRAY_CANDLE);
/* 1320 */             functionalBlocks.accept(Items.BLACK_CANDLE);
/* 1321 */             functionalBlocks.accept(Items.BROWN_CANDLE);
/* 1322 */             functionalBlocks.accept(Items.RED_CANDLE);
/* 1323 */             functionalBlocks.accept(Items.ORANGE_CANDLE);
/* 1324 */             functionalBlocks.accept(Items.YELLOW_CANDLE);
/* 1325 */             functionalBlocks.accept(Items.LIME_CANDLE);
/* 1326 */             functionalBlocks.accept(Items.GREEN_CANDLE);
/* 1327 */             functionalBlocks.accept(Items.CYAN_CANDLE);
/* 1328 */             functionalBlocks.accept(Items.LIGHT_BLUE_CANDLE);
/* 1329 */             functionalBlocks.accept(Items.BLUE_CANDLE);
/* 1330 */             functionalBlocks.accept(Items.PURPLE_CANDLE);
/* 1331 */             functionalBlocks.accept(Items.MAGENTA_CANDLE);
/* 1332 */             functionalBlocks.accept(Items.PINK_CANDLE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1337 */             functionalBlocks.accept(Items.WHITE_BANNER);
/* 1338 */             functionalBlocks.accept(Items.LIGHT_GRAY_BANNER);
/* 1339 */             functionalBlocks.accept(Items.GRAY_BANNER);
/* 1340 */             functionalBlocks.accept(Items.BLACK_BANNER);
/* 1341 */             functionalBlocks.accept(Items.BROWN_BANNER);
/* 1342 */             functionalBlocks.accept(Items.RED_BANNER);
/* 1343 */             functionalBlocks.accept(Items.ORANGE_BANNER);
/* 1344 */             functionalBlocks.accept(Items.YELLOW_BANNER);
/* 1345 */             functionalBlocks.accept(Items.LIME_BANNER);
/* 1346 */             functionalBlocks.accept(Items.GREEN_BANNER);
/* 1347 */             functionalBlocks.accept(Items.CYAN_BANNER);
/* 1348 */             functionalBlocks.accept(Items.LIGHT_BLUE_BANNER);
/* 1349 */             functionalBlocks.accept(Items.BLUE_BANNER);
/* 1350 */             functionalBlocks.accept(Items.PURPLE_BANNER);
/* 1351 */             functionalBlocks.accept(Items.MAGENTA_BANNER);
/* 1352 */             functionalBlocks.accept(Items.PINK_BANNER);
/* 1353 */             functionalBlocks.accept(Raid.getOminousBannerInstance(parameters.holders().lookupOrThrow(Registries.BANNER_PATTERN)));
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1358 */             functionalBlocks.accept(Items.SKELETON_SKULL);
/* 1359 */             functionalBlocks.accept(Items.WITHER_SKELETON_SKULL);
/* 1360 */             functionalBlocks.accept(Items.PLAYER_HEAD);
/* 1361 */             functionalBlocks.accept(Items.ZOMBIE_HEAD);
/* 1362 */             functionalBlocks.accept(Items.CREEPER_HEAD);
/* 1363 */             functionalBlocks.accept(Items.PIGLIN_HEAD);
/* 1364 */             functionalBlocks.accept(Items.DRAGON_HEAD);
/*      */ 
/*      */             
/* 1367 */             functionalBlocks.accept(Items.DRAGON_EGG);
/* 1368 */             functionalBlocks.accept(Items.END_PORTAL_FRAME);
/* 1369 */             functionalBlocks.accept(Items.VAULT);
/* 1370 */             functionalBlocks.accept(Items.ENDER_EYE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1375 */             functionalBlocks.accept(Items.COPPER_GOLEM_STATUE);
/* 1376 */             functionalBlocks.accept(Items.EXPOSED_COPPER_GOLEM_STATUE);
/* 1377 */             functionalBlocks.accept(Items.WEATHERED_COPPER_GOLEM_STATUE);
/* 1378 */             functionalBlocks.accept(Items.OXIDIZED_COPPER_GOLEM_STATUE);
/* 1379 */             functionalBlocks.accept(Items.WAXED_COPPER_GOLEM_STATUE);
/* 1380 */             functionalBlocks.accept(Items.WAXED_EXPOSED_COPPER_GOLEM_STATUE);
/* 1381 */             functionalBlocks.accept(Items.WAXED_WEATHERED_COPPER_GOLEM_STATUE);
/* 1382 */             functionalBlocks.accept(Items.WAXED_OXIDIZED_COPPER_GOLEM_STATUE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1387 */             functionalBlocks.accept(Items.INFESTED_STONE);
/* 1388 */             functionalBlocks.accept(Items.INFESTED_COBBLESTONE);
/* 1389 */             functionalBlocks.accept(Items.INFESTED_STONE_BRICKS);
/* 1390 */             functionalBlocks.accept(Items.INFESTED_MOSSY_STONE_BRICKS);
/* 1391 */             functionalBlocks.accept(Items.INFESTED_CRACKED_STONE_BRICKS);
/* 1392 */             functionalBlocks.accept(Items.INFESTED_CHISELED_STONE_BRICKS);
/* 1393 */             functionalBlocks.accept(Items.INFESTED_DEEPSLATE);
/*      */           
/* 1395 */           }).build());
/* 1396 */     Registry.register(registry, REDSTONE_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 4)
/* 1397 */         .title(Component.translatable("itemGroup.redstone"))
/* 1398 */         .icon(() -> new ItemStack(Items.REDSTONE))
/* 1399 */         .displayItems((parameters, redstoneBlocks) -> {
/*      */             
/* 1401 */             redstoneBlocks.accept(Items.REDSTONE);
/* 1402 */             redstoneBlocks.accept(Items.REDSTONE_TORCH);
/* 1403 */             redstoneBlocks.accept(Items.REDSTONE_BLOCK);
/* 1404 */             redstoneBlocks.accept(Items.REPEATER);
/* 1405 */             redstoneBlocks.accept(Items.COMPARATOR);
/* 1406 */             redstoneBlocks.accept(Items.TARGET);
/* 1407 */             redstoneBlocks.accept(Items.WAXED_COPPER_BULB);
/* 1408 */             redstoneBlocks.accept(Items.WAXED_EXPOSED_COPPER_BULB);
/* 1409 */             redstoneBlocks.accept(Items.WAXED_WEATHERED_COPPER_BULB);
/* 1410 */             redstoneBlocks.accept(Items.WAXED_OXIDIZED_COPPER_BULB);
/*      */ 
/*      */             
/* 1413 */             redstoneBlocks.accept(Items.LEVER);
/*      */ 
/*      */             
/* 1416 */             redstoneBlocks.accept(Items.OAK_BUTTON);
/* 1417 */             redstoneBlocks.accept(Items.STONE_BUTTON);
/*      */ 
/*      */             
/* 1420 */             redstoneBlocks.accept(Items.OAK_PRESSURE_PLATE);
/* 1421 */             redstoneBlocks.accept(Items.STONE_PRESSURE_PLATE);
/* 1422 */             redstoneBlocks.accept(Items.LIGHT_WEIGHTED_PRESSURE_PLATE);
/* 1423 */             redstoneBlocks.accept(Items.HEAVY_WEIGHTED_PRESSURE_PLATE);
/*      */ 
/*      */             
/* 1426 */             redstoneBlocks.accept(Items.SCULK_SENSOR);
/* 1427 */             redstoneBlocks.accept(Items.CALIBRATED_SCULK_SENSOR);
/* 1428 */             redstoneBlocks.accept(Items.SCULK_SHRIEKER);
/* 1429 */             redstoneBlocks.accept(Items.AMETHYST_BLOCK);
/* 1430 */             redstoneBlocks.accept(Items.WHITE_WOOL);
/* 1431 */             redstoneBlocks.accept(Items.TRIPWIRE_HOOK);
/* 1432 */             redstoneBlocks.accept(Items.STRING);
/* 1433 */             redstoneBlocks.accept(Items.LECTERN);
/* 1434 */             redstoneBlocks.accept(Items.DAYLIGHT_DETECTOR);
/* 1435 */             redstoneBlocks.accept(Items.WAXED_LIGHTNING_ROD);
/*      */ 
/*      */             
/* 1438 */             redstoneBlocks.accept(Items.PISTON);
/* 1439 */             redstoneBlocks.accept(Items.STICKY_PISTON);
/* 1440 */             redstoneBlocks.accept(Items.SLIME_BLOCK);
/* 1441 */             redstoneBlocks.accept(Items.HONEY_BLOCK);
/* 1442 */             redstoneBlocks.accept(Items.DISPENSER);
/* 1443 */             redstoneBlocks.accept(Items.DROPPER);
/* 1444 */             redstoneBlocks.accept(Items.CRAFTER);
/* 1445 */             redstoneBlocks.accept(Items.HOPPER);
/*      */ 
/*      */             
/* 1448 */             redstoneBlocks.accept(Items.CHEST);
/* 1449 */             redstoneBlocks.accept(Items.WAXED_COPPER_CHEST);
/* 1450 */             redstoneBlocks.accept(Items.BARREL);
/* 1451 */             redstoneBlocks.accept(Items.CHISELED_BOOKSHELF);
/* 1452 */             redstoneBlocks.accept(Items.OAK_SHELF);
/* 1453 */             redstoneBlocks.accept(Items.FURNACE);
/* 1454 */             redstoneBlocks.accept(Items.TRAPPED_CHEST);
/* 1455 */             redstoneBlocks.accept(Items.JUKEBOX);
/* 1456 */             redstoneBlocks.accept(Items.DECORATED_POT);
/*      */ 
/*      */             
/* 1459 */             redstoneBlocks.accept(Items.OBSERVER);
/* 1460 */             redstoneBlocks.accept(Items.NOTE_BLOCK);
/* 1461 */             redstoneBlocks.accept(Items.COMPOSTER);
/* 1462 */             redstoneBlocks.accept(Items.CAULDRON);
/*      */ 
/*      */ 
/*      */             
/* 1466 */             redstoneBlocks.accept(Items.RAIL);
/* 1467 */             redstoneBlocks.accept(Items.POWERED_RAIL);
/* 1468 */             redstoneBlocks.accept(Items.DETECTOR_RAIL);
/* 1469 */             redstoneBlocks.accept(Items.ACTIVATOR_RAIL);
/*      */ 
/*      */             
/* 1472 */             redstoneBlocks.accept(Items.MINECART);
/* 1473 */             redstoneBlocks.accept(Items.HOPPER_MINECART);
/* 1474 */             redstoneBlocks.accept(Items.CHEST_MINECART);
/* 1475 */             redstoneBlocks.accept(Items.FURNACE_MINECART);
/* 1476 */             redstoneBlocks.accept(Items.TNT_MINECART);
/*      */ 
/*      */             
/* 1479 */             redstoneBlocks.accept(Items.OAK_CHEST_BOAT);
/* 1480 */             redstoneBlocks.accept(Items.BAMBOO_CHEST_RAFT);
/*      */ 
/*      */             
/* 1483 */             redstoneBlocks.accept(Items.OAK_DOOR);
/* 1484 */             redstoneBlocks.accept(Items.IRON_DOOR);
/* 1485 */             redstoneBlocks.accept(Items.OAK_FENCE_GATE);
/* 1486 */             redstoneBlocks.accept(Items.OAK_TRAPDOOR);
/* 1487 */             redstoneBlocks.accept(Items.IRON_TRAPDOOR);
/* 1488 */             redstoneBlocks.accept(Items.TNT);
/* 1489 */             redstoneBlocks.accept(Items.REDSTONE_LAMP);
/* 1490 */             redstoneBlocks.accept(Items.BELL);
/* 1491 */             redstoneBlocks.accept(Items.BIG_DRIPLEAF);
/*      */ 
/*      */             
/* 1494 */             redstoneBlocks.accept(Items.ARMOR_STAND);
/* 1495 */             redstoneBlocks.accept(Items.REDSTONE_ORE);
/*      */           
/* 1497 */           }).build());
/* 1498 */     Registry.register(registry, HOTBAR, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 5)
/* 1499 */         .title(Component.translatable("itemGroup.hotbar"))
/* 1500 */         .icon(() -> new ItemStack(Blocks.BOOKSHELF))
/* 1501 */         .alignedRight()
/* 1502 */         .type(CreativeModeTab.Type.HOTBAR)
/* 1503 */         .build());
/* 1504 */     Registry.register(registry, SEARCH, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 6)
/* 1505 */         .title(Component.translatable("itemGroup.search"))
/* 1506 */         .icon(() -> new ItemStack(Items.COMPASS))
/* 1507 */         .displayItems((parameters, search) -> {
/* 1508 */             Set<ItemStack> tempItems = ItemStackLinkedSet.createTypeAndComponentsSet();
/* 1509 */             for (CreativeModeTab tab : registry) {
/* 1510 */               if (tab.getType() != CreativeModeTab.Type.SEARCH) {
/* 1511 */                 tempItems.addAll(tab.getSearchTabDisplayItems());
/*      */               }
/*      */             } 
/* 1514 */             search.acceptAll(tempItems);
/*      */           
/* 1516 */           }).backgroundTexture(SEARCH_BACKGROUND)
/* 1517 */         .alignedRight()
/* 1518 */         .type(CreativeModeTab.Type.SEARCH)
/* 1519 */         .build());
/* 1520 */     Registry.register(registry, TOOLS_AND_UTILITIES, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
/* 1521 */         .title(Component.translatable("itemGroup.tools"))
/* 1522 */         .icon(() -> new ItemStack(Items.DIAMOND_PICKAXE))
/* 1523 */         .displayItems((parameters, toolsAndUtilities) -> {
/*      */ 
/*      */ 
/*      */             
/* 1527 */             toolsAndUtilities.accept(Items.WOODEN_SHOVEL);
/* 1528 */             toolsAndUtilities.accept(Items.WOODEN_PICKAXE);
/* 1529 */             toolsAndUtilities.accept(Items.WOODEN_AXE);
/* 1530 */             toolsAndUtilities.accept(Items.WOODEN_HOE);
/* 1531 */             toolsAndUtilities.accept(Items.STONE_SHOVEL);
/* 1532 */             toolsAndUtilities.accept(Items.STONE_PICKAXE);
/* 1533 */             toolsAndUtilities.accept(Items.STONE_AXE);
/* 1534 */             toolsAndUtilities.accept(Items.STONE_HOE);
/* 1535 */             toolsAndUtilities.accept(Items.COPPER_SHOVEL);
/* 1536 */             toolsAndUtilities.accept(Items.COPPER_PICKAXE);
/* 1537 */             toolsAndUtilities.accept(Items.COPPER_AXE);
/* 1538 */             toolsAndUtilities.accept(Items.COPPER_HOE);
/* 1539 */             toolsAndUtilities.accept(Items.IRON_SHOVEL);
/* 1540 */             toolsAndUtilities.accept(Items.IRON_PICKAXE);
/* 1541 */             toolsAndUtilities.accept(Items.IRON_AXE);
/* 1542 */             toolsAndUtilities.accept(Items.IRON_HOE);
/* 1543 */             toolsAndUtilities.accept(Items.GOLDEN_SHOVEL);
/* 1544 */             toolsAndUtilities.accept(Items.GOLDEN_PICKAXE);
/* 1545 */             toolsAndUtilities.accept(Items.GOLDEN_AXE);
/* 1546 */             toolsAndUtilities.accept(Items.GOLDEN_HOE);
/* 1547 */             toolsAndUtilities.accept(Items.DIAMOND_SHOVEL);
/* 1548 */             toolsAndUtilities.accept(Items.DIAMOND_PICKAXE);
/* 1549 */             toolsAndUtilities.accept(Items.DIAMOND_AXE);
/* 1550 */             toolsAndUtilities.accept(Items.DIAMOND_HOE);
/* 1551 */             toolsAndUtilities.accept(Items.NETHERITE_SHOVEL);
/* 1552 */             toolsAndUtilities.accept(Items.NETHERITE_PICKAXE);
/* 1553 */             toolsAndUtilities.accept(Items.NETHERITE_AXE);
/* 1554 */             toolsAndUtilities.accept(Items.NETHERITE_HOE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1559 */             toolsAndUtilities.accept(Items.BUCKET);
/* 1560 */             toolsAndUtilities.accept(Items.WATER_BUCKET);
/* 1561 */             toolsAndUtilities.accept(Items.COD_BUCKET);
/* 1562 */             toolsAndUtilities.accept(Items.SALMON_BUCKET);
/* 1563 */             toolsAndUtilities.accept(Items.TROPICAL_FISH_BUCKET);
/* 1564 */             toolsAndUtilities.accept(Items.PUFFERFISH_BUCKET);
/* 1565 */             toolsAndUtilities.accept(Items.AXOLOTL_BUCKET);
/* 1566 */             toolsAndUtilities.accept(Items.TADPOLE_BUCKET);
/* 1567 */             toolsAndUtilities.accept(Items.LAVA_BUCKET);
/* 1568 */             toolsAndUtilities.accept(Items.POWDER_SNOW_BUCKET);
/* 1569 */             toolsAndUtilities.accept(Items.MILK_BUCKET);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1574 */             toolsAndUtilities.accept(Items.FISHING_ROD);
/* 1575 */             toolsAndUtilities.accept(Items.FLINT_AND_STEEL);
/* 1576 */             toolsAndUtilities.accept(Items.FIRE_CHARGE);
/* 1577 */             toolsAndUtilities.accept(Items.BONE_MEAL);
/* 1578 */             toolsAndUtilities.accept(Items.SHEARS);
/* 1579 */             toolsAndUtilities.accept(Items.BRUSH);
/* 1580 */             toolsAndUtilities.accept(Items.NAME_TAG);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1585 */             toolsAndUtilities.accept(Items.LEAD);
/* 1586 */             toolsAndUtilities.accept(Items.BUNDLE);
/* 1587 */             toolsAndUtilities.accept(Items.WHITE_BUNDLE);
/* 1588 */             toolsAndUtilities.accept(Items.LIGHT_GRAY_BUNDLE);
/* 1589 */             toolsAndUtilities.accept(Items.GRAY_BUNDLE);
/* 1590 */             toolsAndUtilities.accept(Items.BLACK_BUNDLE);
/* 1591 */             toolsAndUtilities.accept(Items.BROWN_BUNDLE);
/* 1592 */             toolsAndUtilities.accept(Items.RED_BUNDLE);
/* 1593 */             toolsAndUtilities.accept(Items.ORANGE_BUNDLE);
/* 1594 */             toolsAndUtilities.accept(Items.YELLOW_BUNDLE);
/* 1595 */             toolsAndUtilities.accept(Items.LIME_BUNDLE);
/* 1596 */             toolsAndUtilities.accept(Items.GREEN_BUNDLE);
/* 1597 */             toolsAndUtilities.accept(Items.CYAN_BUNDLE);
/* 1598 */             toolsAndUtilities.accept(Items.LIGHT_BLUE_BUNDLE);
/* 1599 */             toolsAndUtilities.accept(Items.BLUE_BUNDLE);
/* 1600 */             toolsAndUtilities.accept(Items.PURPLE_BUNDLE);
/* 1601 */             toolsAndUtilities.accept(Items.MAGENTA_BUNDLE);
/* 1602 */             toolsAndUtilities.accept(Items.PINK_BUNDLE);
/* 1603 */             toolsAndUtilities.accept(Items.COMPASS);
/* 1604 */             toolsAndUtilities.accept(Items.RECOVERY_COMPASS);
/* 1605 */             toolsAndUtilities.accept(Items.CLOCK);
/* 1606 */             toolsAndUtilities.accept(Items.SPYGLASS);
/* 1607 */             toolsAndUtilities.accept(Items.MAP);
/* 1608 */             toolsAndUtilities.accept(Items.WRITABLE_BOOK);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1613 */             toolsAndUtilities.accept(Items.WIND_CHARGE);
/* 1614 */             toolsAndUtilities.accept(Items.ENDER_PEARL);
/* 1615 */             toolsAndUtilities.accept(Items.ENDER_EYE);
/* 1616 */             toolsAndUtilities.accept(Items.ELYTRA);
/* 1617 */             generateFireworksAllDurations(toolsAndUtilities, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/* 1618 */             toolsAndUtilities.accept(Items.SADDLE);
/*      */             
/* 1620 */             toolsAndUtilities.accept(Items.WHITE_HARNESS);
/* 1621 */             toolsAndUtilities.accept(Items.LIGHT_GRAY_HARNESS);
/* 1622 */             toolsAndUtilities.accept(Items.GRAY_HARNESS);
/* 1623 */             toolsAndUtilities.accept(Items.BLACK_HARNESS);
/* 1624 */             toolsAndUtilities.accept(Items.BROWN_HARNESS);
/* 1625 */             toolsAndUtilities.accept(Items.RED_HARNESS);
/* 1626 */             toolsAndUtilities.accept(Items.ORANGE_HARNESS);
/* 1627 */             toolsAndUtilities.accept(Items.YELLOW_HARNESS);
/* 1628 */             toolsAndUtilities.accept(Items.LIME_HARNESS);
/* 1629 */             toolsAndUtilities.accept(Items.GREEN_HARNESS);
/* 1630 */             toolsAndUtilities.accept(Items.CYAN_HARNESS);
/* 1631 */             toolsAndUtilities.accept(Items.LIGHT_BLUE_HARNESS);
/* 1632 */             toolsAndUtilities.accept(Items.BLUE_HARNESS);
/* 1633 */             toolsAndUtilities.accept(Items.PURPLE_HARNESS);
/* 1634 */             toolsAndUtilities.accept(Items.MAGENTA_HARNESS);
/* 1635 */             toolsAndUtilities.accept(Items.PINK_HARNESS);
/*      */             
/* 1637 */             toolsAndUtilities.accept(Items.CARROT_ON_A_STICK);
/* 1638 */             toolsAndUtilities.accept(Items.WARPED_FUNGUS_ON_A_STICK);
/* 1639 */             toolsAndUtilities.accept(Items.OAK_BOAT);
/* 1640 */             toolsAndUtilities.accept(Items.OAK_CHEST_BOAT);
/* 1641 */             toolsAndUtilities.accept(Items.SPRUCE_BOAT);
/* 1642 */             toolsAndUtilities.accept(Items.SPRUCE_CHEST_BOAT);
/* 1643 */             toolsAndUtilities.accept(Items.BIRCH_BOAT);
/* 1644 */             toolsAndUtilities.accept(Items.BIRCH_CHEST_BOAT);
/* 1645 */             toolsAndUtilities.accept(Items.JUNGLE_BOAT);
/* 1646 */             toolsAndUtilities.accept(Items.JUNGLE_CHEST_BOAT);
/* 1647 */             toolsAndUtilities.accept(Items.ACACIA_BOAT);
/* 1648 */             toolsAndUtilities.accept(Items.ACACIA_CHEST_BOAT);
/* 1649 */             toolsAndUtilities.accept(Items.DARK_OAK_BOAT);
/* 1650 */             toolsAndUtilities.accept(Items.DARK_OAK_CHEST_BOAT);
/* 1651 */             toolsAndUtilities.accept(Items.MANGROVE_BOAT);
/* 1652 */             toolsAndUtilities.accept(Items.MANGROVE_CHEST_BOAT);
/* 1653 */             toolsAndUtilities.accept(Items.CHERRY_BOAT);
/* 1654 */             toolsAndUtilities.accept(Items.CHERRY_CHEST_BOAT);
/* 1655 */             toolsAndUtilities.accept(Items.PALE_OAK_BOAT);
/* 1656 */             toolsAndUtilities.accept(Items.PALE_OAK_CHEST_BOAT);
/* 1657 */             toolsAndUtilities.accept(Items.BAMBOO_RAFT);
/* 1658 */             toolsAndUtilities.accept(Items.BAMBOO_CHEST_RAFT);
/* 1659 */             toolsAndUtilities.accept(Items.RAIL);
/* 1660 */             toolsAndUtilities.accept(Items.POWERED_RAIL);
/* 1661 */             toolsAndUtilities.accept(Items.DETECTOR_RAIL);
/* 1662 */             toolsAndUtilities.accept(Items.ACTIVATOR_RAIL);
/* 1663 */             toolsAndUtilities.accept(Items.MINECART);
/* 1664 */             toolsAndUtilities.accept(Items.HOPPER_MINECART);
/* 1665 */             toolsAndUtilities.accept(Items.CHEST_MINECART);
/* 1666 */             toolsAndUtilities.accept(Items.FURNACE_MINECART);
/* 1667 */             toolsAndUtilities.accept(Items.TNT_MINECART);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1672 */             parameters.holders().lookup(Registries.INSTRUMENT).ifPresent(());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1679 */             toolsAndUtilities.accept(Items.MUSIC_DISC_13);
/* 1680 */             toolsAndUtilities.accept(Items.MUSIC_DISC_CAT);
/* 1681 */             toolsAndUtilities.accept(Items.MUSIC_DISC_BLOCKS);
/* 1682 */             toolsAndUtilities.accept(Items.MUSIC_DISC_CHIRP);
/* 1683 */             toolsAndUtilities.accept(Items.MUSIC_DISC_FAR);
/* 1684 */             toolsAndUtilities.accept(Items.MUSIC_DISC_MALL);
/* 1685 */             toolsAndUtilities.accept(Items.MUSIC_DISC_MELLOHI);
/* 1686 */             toolsAndUtilities.accept(Items.MUSIC_DISC_STAL);
/* 1687 */             toolsAndUtilities.accept(Items.MUSIC_DISC_STRAD);
/* 1688 */             toolsAndUtilities.accept(Items.MUSIC_DISC_WARD);
/* 1689 */             toolsAndUtilities.accept(Items.MUSIC_DISC_11);
/* 1690 */             toolsAndUtilities.accept(Items.MUSIC_DISC_CREATOR_MUSIC_BOX);
/* 1691 */             toolsAndUtilities.accept(Items.MUSIC_DISC_WAIT);
/* 1692 */             toolsAndUtilities.accept(Items.MUSIC_DISC_CREATOR);
/* 1693 */             toolsAndUtilities.accept(Items.MUSIC_DISC_PRECIPICE);
/* 1694 */             toolsAndUtilities.accept(Items.MUSIC_DISC_OTHERSIDE);
/* 1695 */             toolsAndUtilities.accept(Items.MUSIC_DISC_RELIC);
/* 1696 */             toolsAndUtilities.accept(Items.MUSIC_DISC_5);
/* 1697 */             toolsAndUtilities.accept(Items.MUSIC_DISC_PIGSTEP);
/* 1698 */             toolsAndUtilities.accept(Items.MUSIC_DISC_TEARS);
/* 1699 */             toolsAndUtilities.accept(Items.MUSIC_DISC_LAVA_CHICKEN);
/*      */           
/* 1701 */           }).build());
/* 1702 */     Registry.register(registry, COMBAT, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 1)
/* 1703 */         .title(Component.translatable("itemGroup.combat"))
/* 1704 */         .icon(() -> new ItemStack(Items.NETHERITE_SWORD))
/* 1705 */         .displayItems((parameters, combat) -> {
/*      */ 
/*      */ 
/*      */             
/* 1709 */             combat.accept(Items.WOODEN_SWORD);
/* 1710 */             combat.accept(Items.STONE_SWORD);
/* 1711 */             combat.accept(Items.COPPER_SWORD);
/* 1712 */             combat.accept(Items.IRON_SWORD);
/* 1713 */             combat.accept(Items.GOLDEN_SWORD);
/* 1714 */             combat.accept(Items.DIAMOND_SWORD);
/* 1715 */             combat.accept(Items.NETHERITE_SWORD);
/*      */             
/* 1717 */             combat.accept(Items.WOODEN_SPEAR);
/* 1718 */             combat.accept(Items.STONE_SPEAR);
/* 1719 */             combat.accept(Items.COPPER_SPEAR);
/* 1720 */             combat.accept(Items.IRON_SPEAR);
/* 1721 */             combat.accept(Items.GOLDEN_SPEAR);
/* 1722 */             combat.accept(Items.DIAMOND_SPEAR);
/* 1723 */             combat.accept(Items.NETHERITE_SPEAR);
/*      */             
/* 1725 */             combat.accept(Items.WOODEN_AXE);
/* 1726 */             combat.accept(Items.STONE_AXE);
/* 1727 */             combat.accept(Items.COPPER_AXE);
/* 1728 */             combat.accept(Items.IRON_AXE);
/* 1729 */             combat.accept(Items.GOLDEN_AXE);
/* 1730 */             combat.accept(Items.DIAMOND_AXE);
/* 1731 */             combat.accept(Items.NETHERITE_AXE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1736 */             combat.accept(Items.TRIDENT);
/* 1737 */             combat.accept(Items.MACE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1742 */             combat.accept(Items.SHIELD);
/* 1743 */             combat.accept(Items.LEATHER_HELMET);
/* 1744 */             combat.accept(Items.LEATHER_CHESTPLATE);
/* 1745 */             combat.accept(Items.LEATHER_LEGGINGS);
/* 1746 */             combat.accept(Items.LEATHER_BOOTS);
/* 1747 */             combat.accept(Items.COPPER_HELMET);
/* 1748 */             combat.accept(Items.COPPER_CHESTPLATE);
/* 1749 */             combat.accept(Items.COPPER_LEGGINGS);
/* 1750 */             combat.accept(Items.COPPER_BOOTS);
/* 1751 */             combat.accept(Items.CHAINMAIL_HELMET);
/* 1752 */             combat.accept(Items.CHAINMAIL_CHESTPLATE);
/* 1753 */             combat.accept(Items.CHAINMAIL_LEGGINGS);
/* 1754 */             combat.accept(Items.CHAINMAIL_BOOTS);
/* 1755 */             combat.accept(Items.IRON_HELMET);
/* 1756 */             combat.accept(Items.IRON_CHESTPLATE);
/* 1757 */             combat.accept(Items.IRON_LEGGINGS);
/* 1758 */             combat.accept(Items.IRON_BOOTS);
/* 1759 */             combat.accept(Items.GOLDEN_HELMET);
/* 1760 */             combat.accept(Items.GOLDEN_CHESTPLATE);
/* 1761 */             combat.accept(Items.GOLDEN_LEGGINGS);
/* 1762 */             combat.accept(Items.GOLDEN_BOOTS);
/* 1763 */             combat.accept(Items.DIAMOND_HELMET);
/* 1764 */             combat.accept(Items.DIAMOND_CHESTPLATE);
/* 1765 */             combat.accept(Items.DIAMOND_LEGGINGS);
/* 1766 */             combat.accept(Items.DIAMOND_BOOTS);
/* 1767 */             combat.accept(Items.NETHERITE_HELMET);
/* 1768 */             combat.accept(Items.NETHERITE_CHESTPLATE);
/* 1769 */             combat.accept(Items.NETHERITE_LEGGINGS);
/* 1770 */             combat.accept(Items.NETHERITE_BOOTS);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1775 */             combat.accept(Items.TURTLE_HELMET);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1780 */             combat.accept(Items.LEATHER_HORSE_ARMOR);
/* 1781 */             combat.accept(Items.COPPER_HORSE_ARMOR);
/* 1782 */             combat.accept(Items.IRON_HORSE_ARMOR);
/* 1783 */             combat.accept(Items.GOLDEN_HORSE_ARMOR);
/* 1784 */             combat.accept(Items.DIAMOND_HORSE_ARMOR);
/* 1785 */             combat.accept(Items.NETHERITE_HORSE_ARMOR);
/* 1786 */             combat.accept(Items.WOLF_ARMOR);
/* 1787 */             combat.accept(Items.COPPER_NAUTILUS_ARMOR);
/* 1788 */             combat.accept(Items.IRON_NAUTILUS_ARMOR);
/* 1789 */             combat.accept(Items.GOLDEN_NAUTILUS_ARMOR);
/* 1790 */             combat.accept(Items.DIAMOND_NAUTILUS_ARMOR);
/* 1791 */             combat.accept(Items.NETHERITE_NAUTILUS_ARMOR);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1796 */             combat.accept(Items.TOTEM_OF_UNDYING);
/* 1797 */             combat.accept(Items.TNT);
/* 1798 */             combat.accept(Items.END_CRYSTAL);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1803 */             combat.accept(Items.SNOWBALL);
/* 1804 */             combat.accept(Items.EGG);
/* 1805 */             combat.accept(Items.BROWN_EGG);
/* 1806 */             combat.accept(Items.BLUE_EGG);
/* 1807 */             combat.accept(Items.WIND_CHARGE);
/* 1808 */             combat.accept(Items.BOW);
/* 1809 */             combat.accept(Items.CROSSBOW);
/* 1810 */             generateFireworksAllDurations(combat, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/* 1811 */             combat.accept(Items.ARROW);
/* 1812 */             combat.accept(Items.SPECTRAL_ARROW);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1817 */             parameters.holders().lookup(Registries.POTION).ifPresent(());
/*      */ 
/*      */ 
/*      */           
/* 1821 */           }).build());
/* 1822 */     Registry.register(registry, FOOD_AND_DRINKS, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 2)
/* 1823 */         .title(Component.translatable("itemGroup.foodAndDrink"))
/* 1824 */         .icon(() -> new ItemStack(Items.GOLDEN_APPLE))
/* 1825 */         .displayItems((parameters, consumables) -> {
/*      */             
/* 1827 */             consumables.accept(Items.APPLE);
/* 1828 */             consumables.accept(Items.GOLDEN_APPLE);
/* 1829 */             consumables.accept(Items.ENCHANTED_GOLDEN_APPLE);
/* 1830 */             consumables.accept(Items.MELON_SLICE);
/* 1831 */             consumables.accept(Items.SWEET_BERRIES);
/* 1832 */             consumables.accept(Items.GLOW_BERRIES);
/* 1833 */             consumables.accept(Items.CHORUS_FRUIT);
/*      */ 
/*      */             
/* 1836 */             consumables.accept(Items.CARROT);
/* 1837 */             consumables.accept(Items.GOLDEN_CARROT);
/* 1838 */             consumables.accept(Items.POTATO);
/* 1839 */             consumables.accept(Items.BAKED_POTATO);
/* 1840 */             consumables.accept(Items.POISONOUS_POTATO);
/* 1841 */             consumables.accept(Items.BEETROOT);
/* 1842 */             consumables.accept(Items.DRIED_KELP);
/*      */ 
/*      */             
/* 1845 */             consumables.accept(Items.BEEF);
/* 1846 */             consumables.accept(Items.COOKED_BEEF);
/* 1847 */             consumables.accept(Items.PORKCHOP);
/* 1848 */             consumables.accept(Items.COOKED_PORKCHOP);
/* 1849 */             consumables.accept(Items.MUTTON);
/* 1850 */             consumables.accept(Items.COOKED_MUTTON);
/* 1851 */             consumables.accept(Items.CHICKEN);
/* 1852 */             consumables.accept(Items.COOKED_CHICKEN);
/* 1853 */             consumables.accept(Items.RABBIT);
/* 1854 */             consumables.accept(Items.COOKED_RABBIT);
/*      */ 
/*      */             
/* 1857 */             consumables.accept(Items.COD);
/* 1858 */             consumables.accept(Items.COOKED_COD);
/* 1859 */             consumables.accept(Items.SALMON);
/* 1860 */             consumables.accept(Items.COOKED_SALMON);
/* 1861 */             consumables.accept(Items.TROPICAL_FISH);
/* 1862 */             consumables.accept(Items.PUFFERFISH);
/*      */ 
/*      */             
/* 1865 */             consumables.accept(Items.BREAD);
/* 1866 */             consumables.accept(Items.COOKIE);
/* 1867 */             consumables.accept(Items.CAKE);
/* 1868 */             consumables.accept(Items.PUMPKIN_PIE);
/*      */ 
/*      */             
/* 1871 */             consumables.accept(Items.ROTTEN_FLESH);
/* 1872 */             consumables.accept(Items.SPIDER_EYE);
/*      */ 
/*      */             
/* 1875 */             consumables.accept(Items.MUSHROOM_STEW);
/* 1876 */             consumables.accept(Items.BEETROOT_SOUP);
/* 1877 */             consumables.accept(Items.RABBIT_STEW);
/* 1878 */             generateSuspiciousStews(consumables, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/* 1879 */             consumables.accept(Items.MILK_BUCKET);
/* 1880 */             consumables.accept(Items.HONEY_BOTTLE);
/* 1881 */             generateOminousBottles(consumables, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1886 */             parameters.holders().lookup(Registries.POTION).ifPresent(());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1892 */           }).build());
/* 1893 */     Registry.register(registry, INGREDIENTS, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 3)
/* 1894 */         .title(Component.translatable("itemGroup.ingredients"))
/* 1895 */         .icon(() -> new ItemStack(Items.IRON_INGOT))
/* 1896 */         .displayItems((parameters, ingredients) -> {
/*      */ 
/*      */ 
/*      */             
/* 1900 */             ingredients.accept(Items.COAL);
/* 1901 */             ingredients.accept(Items.CHARCOAL);
/* 1902 */             ingredients.accept(Items.RAW_COPPER);
/* 1903 */             ingredients.accept(Items.RAW_IRON);
/* 1904 */             ingredients.accept(Items.RAW_GOLD);
/* 1905 */             ingredients.accept(Items.EMERALD);
/* 1906 */             ingredients.accept(Items.LAPIS_LAZULI);
/* 1907 */             ingredients.accept(Items.DIAMOND);
/* 1908 */             ingredients.accept(Items.ANCIENT_DEBRIS);
/* 1909 */             ingredients.accept(Items.QUARTZ);
/* 1910 */             ingredients.accept(Items.AMETHYST_SHARD);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1915 */             ingredients.accept(Items.COPPER_NUGGET);
/* 1916 */             ingredients.accept(Items.IRON_NUGGET);
/* 1917 */             ingredients.accept(Items.GOLD_NUGGET);
/* 1918 */             ingredients.accept(Items.COPPER_INGOT);
/* 1919 */             ingredients.accept(Items.IRON_INGOT);
/* 1920 */             ingredients.accept(Items.GOLD_INGOT);
/* 1921 */             ingredients.accept(Items.NETHERITE_SCRAP);
/* 1922 */             ingredients.accept(Items.NETHERITE_INGOT);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1927 */             ingredients.accept(Items.STICK);
/* 1928 */             ingredients.accept(Items.FLINT);
/* 1929 */             ingredients.accept(Items.WHEAT);
/* 1930 */             ingredients.accept(Items.BONE);
/* 1931 */             ingredients.accept(Items.BONE_MEAL);
/* 1932 */             ingredients.accept(Items.STRING);
/* 1933 */             ingredients.accept(Items.FEATHER);
/* 1934 */             ingredients.accept(Items.SNOWBALL);
/* 1935 */             ingredients.accept(Items.EGG);
/* 1936 */             ingredients.accept(Items.BROWN_EGG);
/* 1937 */             ingredients.accept(Items.BLUE_EGG);
/* 1938 */             ingredients.accept(Items.LEATHER);
/* 1939 */             ingredients.accept(Items.RABBIT_HIDE);
/* 1940 */             ingredients.accept(Items.HONEYCOMB);
/* 1941 */             ingredients.accept(Items.RESIN_CLUMP);
/* 1942 */             ingredients.accept(Items.INK_SAC);
/* 1943 */             ingredients.accept(Items.GLOW_INK_SAC);
/* 1944 */             ingredients.accept(Items.TURTLE_SCUTE);
/* 1945 */             ingredients.accept(Items.ARMADILLO_SCUTE);
/* 1946 */             ingredients.accept(Items.SLIME_BALL);
/* 1947 */             ingredients.accept(Items.CLAY_BALL);
/* 1948 */             ingredients.accept(Items.PRISMARINE_SHARD);
/* 1949 */             ingredients.accept(Items.PRISMARINE_CRYSTALS);
/* 1950 */             ingredients.accept(Items.NAUTILUS_SHELL);
/* 1951 */             ingredients.accept(Items.HEART_OF_THE_SEA);
/* 1952 */             ingredients.accept(Items.FIRE_CHARGE);
/* 1953 */             ingredients.accept(Items.BLAZE_ROD);
/* 1954 */             ingredients.accept(Items.BREEZE_ROD);
/* 1955 */             ingredients.accept(Items.HEAVY_CORE);
/* 1956 */             ingredients.accept(Items.NETHER_STAR);
/* 1957 */             ingredients.accept(Items.ENDER_PEARL);
/* 1958 */             ingredients.accept(Items.ENDER_EYE);
/* 1959 */             ingredients.accept(Items.SHULKER_SHELL);
/* 1960 */             ingredients.accept(Items.POPPED_CHORUS_FRUIT);
/* 1961 */             ingredients.accept(Items.ECHO_SHARD);
/* 1962 */             ingredients.accept(Items.DISC_FRAGMENT_5);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1967 */             ingredients.accept(Items.WHITE_DYE);
/* 1968 */             ingredients.accept(Items.LIGHT_GRAY_DYE);
/* 1969 */             ingredients.accept(Items.GRAY_DYE);
/* 1970 */             ingredients.accept(Items.BLACK_DYE);
/* 1971 */             ingredients.accept(Items.BROWN_DYE);
/* 1972 */             ingredients.accept(Items.RED_DYE);
/* 1973 */             ingredients.accept(Items.ORANGE_DYE);
/* 1974 */             ingredients.accept(Items.YELLOW_DYE);
/* 1975 */             ingredients.accept(Items.LIME_DYE);
/* 1976 */             ingredients.accept(Items.GREEN_DYE);
/* 1977 */             ingredients.accept(Items.CYAN_DYE);
/* 1978 */             ingredients.accept(Items.LIGHT_BLUE_DYE);
/* 1979 */             ingredients.accept(Items.BLUE_DYE);
/* 1980 */             ingredients.accept(Items.PURPLE_DYE);
/* 1981 */             ingredients.accept(Items.MAGENTA_DYE);
/* 1982 */             ingredients.accept(Items.PINK_DYE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1987 */             ingredients.accept(Items.BOWL);
/* 1988 */             ingredients.accept(Items.BRICK);
/* 1989 */             ingredients.accept(Items.NETHER_BRICK);
/* 1990 */             ingredients.accept(Items.RESIN_BRICK);
/* 1991 */             ingredients.accept(Items.PAPER);
/* 1992 */             ingredients.accept(Items.BOOK);
/* 1993 */             ingredients.accept(Items.FIREWORK_STAR);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1998 */             ingredients.accept(Items.GLASS_BOTTLE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2003 */             ingredients.accept(Items.NETHER_WART);
/* 2004 */             ingredients.accept(Items.REDSTONE);
/* 2005 */             ingredients.accept(Items.GLOWSTONE_DUST);
/* 2006 */             ingredients.accept(Items.GUNPOWDER);
/* 2007 */             ingredients.accept(Items.DRAGON_BREATH);
/* 2008 */             ingredients.accept(Items.FERMENTED_SPIDER_EYE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2013 */             ingredients.accept(Items.BLAZE_POWDER);
/* 2014 */             ingredients.accept(Items.SUGAR);
/* 2015 */             ingredients.accept(Items.RABBIT_FOOT);
/* 2016 */             ingredients.accept(Items.GLISTERING_MELON_SLICE);
/* 2017 */             ingredients.accept(Items.SPIDER_EYE);
/* 2018 */             ingredients.accept(Items.PUFFERFISH);
/* 2019 */             ingredients.accept(Items.MAGMA_CREAM);
/* 2020 */             ingredients.accept(Items.GOLDEN_CARROT);
/* 2021 */             ingredients.accept(Items.GHAST_TEAR);
/* 2022 */             ingredients.accept(Items.TURTLE_HELMET);
/* 2023 */             ingredients.accept(Items.PHANTOM_MEMBRANE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2028 */             ingredients.accept(Items.FIELD_MASONED_BANNER_PATTERN);
/* 2029 */             ingredients.accept(Items.BORDURE_INDENTED_BANNER_PATTERN);
/* 2030 */             ingredients.accept(Items.FLOWER_BANNER_PATTERN);
/* 2031 */             ingredients.accept(Items.CREEPER_BANNER_PATTERN);
/* 2032 */             ingredients.accept(Items.SKULL_BANNER_PATTERN);
/* 2033 */             ingredients.accept(Items.MOJANG_BANNER_PATTERN);
/* 2034 */             ingredients.accept(Items.GLOBE_BANNER_PATTERN);
/* 2035 */             ingredients.accept(Items.PIGLIN_BANNER_PATTERN);
/* 2036 */             ingredients.accept(Items.FLOW_BANNER_PATTERN);
/* 2037 */             ingredients.accept(Items.GUSTER_BANNER_PATTERN);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2042 */             ingredients.accept(Items.ANGLER_POTTERY_SHERD);
/* 2043 */             ingredients.accept(Items.ARCHER_POTTERY_SHERD);
/* 2044 */             ingredients.accept(Items.ARMS_UP_POTTERY_SHERD);
/* 2045 */             ingredients.accept(Items.BLADE_POTTERY_SHERD);
/* 2046 */             ingredients.accept(Items.BREWER_POTTERY_SHERD);
/* 2047 */             ingredients.accept(Items.BURN_POTTERY_SHERD);
/* 2048 */             ingredients.accept(Items.DANGER_POTTERY_SHERD);
/* 2049 */             ingredients.accept(Items.EXPLORER_POTTERY_SHERD);
/* 2050 */             ingredients.accept(Items.FLOW_POTTERY_SHERD);
/* 2051 */             ingredients.accept(Items.FRIEND_POTTERY_SHERD);
/* 2052 */             ingredients.accept(Items.GUSTER_POTTERY_SHERD);
/* 2053 */             ingredients.accept(Items.HEART_POTTERY_SHERD);
/* 2054 */             ingredients.accept(Items.HEARTBREAK_POTTERY_SHERD);
/* 2055 */             ingredients.accept(Items.HOWL_POTTERY_SHERD);
/* 2056 */             ingredients.accept(Items.MINER_POTTERY_SHERD);
/* 2057 */             ingredients.accept(Items.MOURNER_POTTERY_SHERD);
/* 2058 */             ingredients.accept(Items.PLENTY_POTTERY_SHERD);
/* 2059 */             ingredients.accept(Items.PRIZE_POTTERY_SHERD);
/* 2060 */             ingredients.accept(Items.SCRAPE_POTTERY_SHERD);
/* 2061 */             ingredients.accept(Items.SHEAF_POTTERY_SHERD);
/* 2062 */             ingredients.accept(Items.SHELTER_POTTERY_SHERD);
/* 2063 */             ingredients.accept(Items.SKULL_POTTERY_SHERD);
/* 2064 */             ingredients.accept(Items.SNORT_POTTERY_SHERD);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2069 */             ingredients.accept(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
/* 2070 */             ingredients.accept(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2071 */             ingredients.accept(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2072 */             ingredients.accept(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2073 */             ingredients.accept(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2074 */             ingredients.accept(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2075 */             ingredients.accept(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2076 */             ingredients.accept(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2077 */             ingredients.accept(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2078 */             ingredients.accept(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2079 */             ingredients.accept(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2080 */             ingredients.accept(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2081 */             ingredients.accept(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2082 */             ingredients.accept(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2083 */             ingredients.accept(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2084 */             ingredients.accept(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2085 */             ingredients.accept(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2086 */             ingredients.accept(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE);
/* 2087 */             ingredients.accept(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2092 */             ingredients.accept(Items.EXPERIENCE_BOTTLE);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2097 */             ingredients.accept(Items.TRIAL_KEY);
/* 2098 */             ingredients.accept(Items.OMINOUS_TRIAL_KEY);
/*      */             
/* 2100 */             parameters.holders().lookup(Registries.ENCHANTMENT).ifPresent(());
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2105 */           }).build());
/* 2106 */     Registry.register(registry, SPAWN_EGGS, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 4)
/* 2107 */         .title(Component.translatable("itemGroup.spawnEggs"))
/* 2108 */         .icon(() -> new ItemStack(Items.CREEPER_SPAWN_EGG))
/* 2109 */         .displayItems((parameters, spawnEggs) -> {
/*      */ 
/*      */ 
/*      */             
/* 2113 */             spawnEggs.accept(Items.SPAWNER);
/* 2114 */             spawnEggs.accept(Items.TRIAL_SPAWNER);
/* 2115 */             spawnEggs.accept(Items.CREAKING_HEART);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2121 */             spawnEggs.accept(Items.CHICKEN_SPAWN_EGG);
/* 2122 */             spawnEggs.accept(Items.COW_SPAWN_EGG);
/* 2123 */             spawnEggs.accept(Items.PIG_SPAWN_EGG);
/* 2124 */             spawnEggs.accept(Items.SHEEP_SPAWN_EGG);
/*      */ 
/*      */             
/* 2127 */             spawnEggs.accept(Items.CAMEL_SPAWN_EGG);
/* 2128 */             spawnEggs.accept(Items.DONKEY_SPAWN_EGG);
/* 2129 */             spawnEggs.accept(Items.HORSE_SPAWN_EGG);
/* 2130 */             spawnEggs.accept(Items.MULE_SPAWN_EGG);
/*      */ 
/*      */             
/* 2133 */             spawnEggs.accept(Items.CAT_SPAWN_EGG);
/* 2134 */             spawnEggs.accept(Items.PARROT_SPAWN_EGG);
/* 2135 */             spawnEggs.accept(Items.WOLF_SPAWN_EGG);
/*      */ 
/*      */             
/* 2138 */             spawnEggs.accept(Items.ARMADILLO_SPAWN_EGG);
/* 2139 */             spawnEggs.accept(Items.BAT_SPAWN_EGG);
/* 2140 */             spawnEggs.accept(Items.BEE_SPAWN_EGG);
/* 2141 */             spawnEggs.accept(Items.FOX_SPAWN_EGG);
/* 2142 */             spawnEggs.accept(Items.GOAT_SPAWN_EGG);
/* 2143 */             spawnEggs.accept(Items.LLAMA_SPAWN_EGG);
/* 2144 */             spawnEggs.accept(Items.OCELOT_SPAWN_EGG);
/* 2145 */             spawnEggs.accept(Items.PANDA_SPAWN_EGG);
/* 2146 */             spawnEggs.accept(Items.POLAR_BEAR_SPAWN_EGG);
/* 2147 */             spawnEggs.accept(Items.RABBIT_SPAWN_EGG);
/*      */ 
/*      */             
/* 2150 */             spawnEggs.accept(Items.AXOLOTL_SPAWN_EGG);
/* 2151 */             spawnEggs.accept(Items.COD_SPAWN_EGG);
/* 2152 */             spawnEggs.accept(Items.DOLPHIN_SPAWN_EGG);
/* 2153 */             spawnEggs.accept(Items.FROG_SPAWN_EGG);
/* 2154 */             spawnEggs.accept(Items.GLOW_SQUID_SPAWN_EGG);
/* 2155 */             spawnEggs.accept(Items.NAUTILUS_SPAWN_EGG);
/* 2156 */             spawnEggs.accept(Items.PUFFERFISH_SPAWN_EGG);
/* 2157 */             spawnEggs.accept(Items.SALMON_SPAWN_EGG);
/* 2158 */             spawnEggs.accept(Items.SQUID_SPAWN_EGG);
/* 2159 */             spawnEggs.accept(Items.TADPOLE_SPAWN_EGG);
/* 2160 */             spawnEggs.accept(Items.TROPICAL_FISH_SPAWN_EGG);
/* 2161 */             spawnEggs.accept(Items.TURTLE_SPAWN_EGG);
/*      */ 
/*      */             
/* 2164 */             spawnEggs.accept(Items.ALLAY_SPAWN_EGG);
/* 2165 */             spawnEggs.accept(Items.MOOSHROOM_SPAWN_EGG);
/* 2166 */             spawnEggs.accept(Items.SNIFFER_SPAWN_EGG);
/*      */ 
/*      */             
/* 2169 */             spawnEggs.accept(Items.COPPER_GOLEM_SPAWN_EGG);
/* 2170 */             spawnEggs.accept(Items.IRON_GOLEM_SPAWN_EGG);
/* 2171 */             spawnEggs.accept(Items.SNOW_GOLEM_SPAWN_EGG);
/*      */ 
/*      */             
/* 2174 */             spawnEggs.accept(Items.TRADER_LLAMA_SPAWN_EGG);
/* 2175 */             spawnEggs.accept(Items.VILLAGER_SPAWN_EGG);
/* 2176 */             spawnEggs.accept(Items.WANDERING_TRADER_SPAWN_EGG);
/*      */ 
/*      */             
/* 2179 */             spawnEggs.accept(Items.BOGGED_SPAWN_EGG);
/* 2180 */             spawnEggs.accept(Items.CAMEL_HUSK_SPAWN_EGG);
/* 2181 */             spawnEggs.accept(Items.DROWNED_SPAWN_EGG);
/* 2182 */             spawnEggs.accept(Items.HUSK_SPAWN_EGG);
/* 2183 */             spawnEggs.accept(Items.PARCHED_SPAWN_EGG);
/* 2184 */             spawnEggs.accept(Items.SKELETON_SPAWN_EGG);
/* 2185 */             spawnEggs.accept(Items.SKELETON_HORSE_SPAWN_EGG);
/* 2186 */             spawnEggs.accept(Items.STRAY_SPAWN_EGG);
/* 2187 */             spawnEggs.accept(Items.ZOMBIE_SPAWN_EGG);
/* 2188 */             spawnEggs.accept(Items.ZOMBIE_HORSE_SPAWN_EGG);
/* 2189 */             spawnEggs.accept(Items.ZOMBIE_NAUTILUS_SPAWN_EGG);
/* 2190 */             spawnEggs.accept(Items.ZOMBIE_VILLAGER_SPAWN_EGG);
/*      */ 
/*      */             
/* 2193 */             spawnEggs.accept(Items.CAVE_SPIDER_SPAWN_EGG);
/* 2194 */             spawnEggs.accept(Items.SPIDER_SPAWN_EGG);
/*      */ 
/*      */             
/* 2197 */             spawnEggs.accept(Items.BREEZE_SPAWN_EGG);
/* 2198 */             spawnEggs.accept(Items.CREAKING_SPAWN_EGG);
/* 2199 */             spawnEggs.accept(Items.CREEPER_SPAWN_EGG);
/* 2200 */             spawnEggs.accept(Items.ELDER_GUARDIAN_SPAWN_EGG);
/* 2201 */             spawnEggs.accept(Items.GUARDIAN_SPAWN_EGG);
/* 2202 */             spawnEggs.accept(Items.PHANTOM_SPAWN_EGG);
/* 2203 */             spawnEggs.accept(Items.SILVERFISH_SPAWN_EGG);
/* 2204 */             spawnEggs.accept(Items.SLIME_SPAWN_EGG);
/* 2205 */             spawnEggs.accept(Items.WARDEN_SPAWN_EGG);
/* 2206 */             spawnEggs.accept(Items.WITCH_SPAWN_EGG);
/*      */ 
/*      */             
/* 2209 */             spawnEggs.accept(Items.EVOKER_SPAWN_EGG);
/* 2210 */             spawnEggs.accept(Items.PILLAGER_SPAWN_EGG);
/* 2211 */             spawnEggs.accept(Items.RAVAGER_SPAWN_EGG);
/* 2212 */             spawnEggs.accept(Items.VEX_SPAWN_EGG);
/* 2213 */             spawnEggs.accept(Items.VINDICATOR_SPAWN_EGG);
/*      */ 
/*      */             
/* 2216 */             spawnEggs.accept(Items.BLAZE_SPAWN_EGG);
/* 2217 */             spawnEggs.accept(Items.GHAST_SPAWN_EGG);
/* 2218 */             spawnEggs.accept(Items.HAPPY_GHAST_SPAWN_EGG);
/* 2219 */             spawnEggs.accept(Items.HOGLIN_SPAWN_EGG);
/* 2220 */             spawnEggs.accept(Items.MAGMA_CUBE_SPAWN_EGG);
/* 2221 */             spawnEggs.accept(Items.PIGLIN_SPAWN_EGG);
/* 2222 */             spawnEggs.accept(Items.PIGLIN_BRUTE_SPAWN_EGG);
/* 2223 */             spawnEggs.accept(Items.STRIDER_SPAWN_EGG);
/* 2224 */             spawnEggs.accept(Items.WITHER_SKELETON_SPAWN_EGG);
/* 2225 */             spawnEggs.accept(Items.ZOGLIN_SPAWN_EGG);
/* 2226 */             spawnEggs.accept(Items.ZOMBIFIED_PIGLIN_SPAWN_EGG);
/*      */ 
/*      */             
/* 2229 */             spawnEggs.accept(Items.ENDERMAN_SPAWN_EGG);
/* 2230 */             spawnEggs.accept(Items.ENDERMITE_SPAWN_EGG);
/* 2231 */             spawnEggs.accept(Items.SHULKER_SPAWN_EGG);
/*      */           
/* 2233 */           }).build());
/* 2234 */     Registry.register(registry, OP_BLOCKS, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 5)
/* 2235 */         .title(Component.translatable("itemGroup.op"))
/* 2236 */         .icon(() -> new ItemStack(Items.COMMAND_BLOCK))
/* 2237 */         .alignedRight()
/* 2238 */         .displayItems((parameters, opBlocks) -> {
/*      */             
/* 2240 */             if (parameters.hasPermissions()) {
/* 2241 */               opBlocks.accept(Items.COMMAND_BLOCK);
/* 2242 */               opBlocks.accept(Items.CHAIN_COMMAND_BLOCK);
/* 2243 */               opBlocks.accept(Items.REPEATING_COMMAND_BLOCK);
/* 2244 */               opBlocks.accept(Items.COMMAND_BLOCK_MINECART);
/* 2245 */               opBlocks.accept(Items.JIGSAW);
/* 2246 */               opBlocks.accept(Items.STRUCTURE_BLOCK);
/* 2247 */               opBlocks.accept(Items.STRUCTURE_VOID);
/* 2248 */               opBlocks.accept(Items.BARRIER);
/* 2249 */               opBlocks.accept(Items.DEBUG_STICK);
/* 2250 */               opBlocks.accept(Items.TEST_INSTANCE_BLOCK);
/* 2251 */               for (TestBlockMode mode : TestBlockMode.values()) {
/* 2252 */                 opBlocks.accept(TestBlock.setModeOnStack(new ItemStack(Items.TEST_BLOCK), mode));
/*      */               }
/* 2254 */               for (int lightLevel = 15; lightLevel >= 0; lightLevel--) {
/* 2255 */                 opBlocks.accept(LightBlock.setLightOnStack(new ItemStack(Items.LIGHT), lightLevel));
/*      */               }
/* 2257 */               parameters.holders().lookup(Registries.PAINTING_VARIANT).ifPresent(());
/*      */             }
/*      */ 
/*      */ 
/*      */           
/* 2262 */           }).build());
/* 2263 */     return (CreativeModeTab)Registry.register(registry, INVENTORY, CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 6)
/* 2264 */         .title(Component.translatable("itemGroup.inventory"))
/* 2265 */         .icon(() -> new ItemStack(Blocks.CHEST))
/* 2266 */         .backgroundTexture(INVENTORY_BACKGROUND)
/* 2267 */         .hideTitle()
/* 2268 */         .alignedRight()
/* 2269 */         .type(CreativeModeTab.Type.INVENTORY)
/* 2270 */         .noScrollBar()
/* 2271 */         .build());
/*      */   }
/*      */   
/*      */   public static void validate() {
/* 2275 */     positions = new HashMap();
/* 2276 */     for (ResourceKey<CreativeModeTab> tabKey : BuiltInRegistries.CREATIVE_MODE_TAB.registryKeySet()) {
/* 2277 */       CreativeModeTab tab = (CreativeModeTab)BuiltInRegistries.CREATIVE_MODE_TAB.getValueOrThrow(tabKey);
/* 2278 */       String current = tab.getDisplayName().getString();
/* 2279 */       String previous = (String)positions.put(Pair.of(tab.row(), Integer.valueOf(tab.column())), current);
/* 2280 */       if (previous != null) {
/* 2281 */         throw new IllegalArgumentException("Duplicate position: " + current + " vs. " + previous);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/* 2286 */   private static final Comparator<Holder<PaintingVariant>> PAINTING_COMPARATOR = Comparator.comparing(Holder::value, Comparator.comparingInt(PaintingVariant::area).thenComparing(PaintingVariant::width));
/*      */   
/*      */   private static CreativeModeTab.ItemDisplayParameters CACHED_PARAMETERS;
/*      */ 
/*      */   
/* 2291 */   public static CreativeModeTab getDefaultTab() { return (CreativeModeTab)BuiltInRegistries.CREATIVE_MODE_TAB.getValueOrThrow(BUILDING_BLOCKS); }
/*      */ 
/*      */ 
/*      */   
/* 2295 */   private static void generatePotionEffectTypes(CreativeModeTab.Output output, HolderLookup<Potion> potions, Item item, CreativeModeTab.TabVisibility tabVisibility, FeatureFlagSet enabledFeatures) { potions.listElements()
/* 2296 */       .filter(potion -> ((Potion)potion.value()).isEnabled(enabledFeatures))
/* 2297 */       .map(potion -> PotionContents.createItemStack(item, potion))
/* 2298 */       .forEach(stack -> output.accept(stack, tabVisibility)); }
/*      */ 
/*      */ 
/*      */   
/* 2302 */   private static void generateEnchantmentBookTypesOnlyMaxLevel(CreativeModeTab.Output output, HolderLookup<Enchantment> enchantments, CreativeModeTab.TabVisibility tabVisibility) { enchantments.listElements()
/* 2303 */       .map(enchantment -> EnchantmentHelper.createBook(new EnchantmentInstance(enchantment, ((Enchantment)enchantment.value()).getMaxLevel())))
/* 2304 */       .forEach(stack -> output.accept(stack, tabVisibility)); }
/*      */ 
/*      */ 
/*      */   
/* 2308 */   private static void generateEnchantmentBookTypesAllLevels(CreativeModeTab.Output output, HolderLookup<Enchantment> enchantments, CreativeModeTab.TabVisibility tabVisibility) { enchantments.listElements()
/* 2309 */       .flatMap(enchantment -> IntStream.rangeClosed(((Enchantment)enchantment.value()).getMinLevel(), ((Enchantment)enchantment.value()).getMaxLevel()).mapToObj(()))
/* 2310 */       .forEach(stack -> output.accept(stack, tabVisibility)); }
/*      */ 
/*      */   
/*      */   private static void generateInstrumentTypes(CreativeModeTab.Output output, HolderLookup<Instrument> instruments, Item instrumentItem, TagKey<Instrument> instrumentTagKey, CreativeModeTab.TabVisibility tabVisibility) {
/* 2314 */     instruments.get(instrumentTagKey).ifPresent(tag -> 
/* 2315 */         tag.stream()
/* 2316 */         .map(())
/* 2317 */         .forEach(()));
/*      */   }
/*      */ 
/*      */   
/*      */   private static void generateSuspiciousStews(CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility) {
/* 2322 */     List<SuspiciousEffectHolder> effectHolders = SuspiciousEffectHolder.getAllEffectHolders();
/* 2323 */     Set<ItemStack> stewItems = ItemStackLinkedSet.createTypeAndComponentsSet();
/* 2324 */     for (SuspiciousEffectHolder effectHolder : effectHolders) {
/* 2325 */       ItemStack stack = new ItemStack(Items.SUSPICIOUS_STEW);
/* 2326 */       stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effectHolder.getSuspiciousEffects());
/* 2327 */       stewItems.add(stack);
/*      */     } 
/* 2329 */     output.acceptAll(stewItems, tabVisibility);
/*      */   }
/*      */   
/*      */   private static void generateOminousBottles(CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility) {
/* 2333 */     for (int i = 0; i <= 4; i++) {
/* 2334 */       ItemStack stack = new ItemStack(Items.OMINOUS_BOTTLE);
/* 2335 */       stack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, new OminousBottleAmplifier(i));
/* 2336 */       output.accept(stack, tabVisibility);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void generateFireworksAllDurations(CreativeModeTab.Output output, CreativeModeTab.TabVisibility tabVisibility) {
/* 2341 */     for (byte duration : FireworkRocketItem.CRAFTABLE_DURATIONS) {
/* 2342 */       ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
/* 2343 */       firework.set(DataComponents.FIREWORKS, new Fireworks(duration, List.of()));
/* 2344 */       output.accept(firework, tabVisibility);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void generatePresetPaintings(CreativeModeTab.Output output, HolderLookup.Provider context, HolderLookup.RegistryLookup<PaintingVariant> paintings, Predicate<Holder<PaintingVariant>> filter, CreativeModeTab.TabVisibility tabVisibility) {
/* 2349 */     RegistryOps<Tag> ops = context.createSerializationContext(NbtOps.INSTANCE);
/* 2350 */     paintings.listElements()
/* 2351 */       .filter(filter)
/* 2352 */       .sorted(PAINTING_COMPARATOR)
/* 2353 */       .forEach(painting -> {
/* 2354 */           ItemStack stack = new ItemStack(Items.PAINTING);
/* 2355 */           stack.set(DataComponents.PAINTING_VARIANT, painting);
/* 2356 */           output.accept(stack, tabVisibility);
/*      */         });
/*      */   }
/*      */ 
/*      */   
/* 2361 */   public static List<CreativeModeTab> tabs() { return streamAllTabs().filter(CreativeModeTab::shouldDisplay).toList(); }
/*      */ 
/*      */ 
/*      */   
/* 2365 */   public static List<CreativeModeTab> allTabs() { return streamAllTabs().toList(); }
/*      */ 
/*      */ 
/*      */   
/* 2369 */   private static Stream<CreativeModeTab> streamAllTabs() { return BuiltInRegistries.CREATIVE_MODE_TAB.stream(); }
/*      */ 
/*      */ 
/*      */   
/* 2373 */   public static CreativeModeTab searchTab() { return (CreativeModeTab)BuiltInRegistries.CREATIVE_MODE_TAB.getValueOrThrow(SEARCH); }
/*      */ 
/*      */   
/*      */   private static void buildAllTabContents(CreativeModeTab.ItemDisplayParameters parameters) {
/* 2377 */     streamAllTabs().filter(tab -> (tab.getType() == CreativeModeTab.Type.CATEGORY)).forEach(tab -> tab.buildContents(parameters));
/*      */     
/* 2379 */     streamAllTabs().filter(tab -> (tab.getType() != CreativeModeTab.Type.CATEGORY)).forEach(tab -> tab.buildContents(parameters));
/*      */   }
/*      */   
/*      */   public static boolean tryRebuildTabContents(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider lookup) {
/* 2383 */     if (CACHED_PARAMETERS != null && !CACHED_PARAMETERS.needsUpdate(enabledFeatures, hasPermissions, lookup)) {
/* 2384 */       return false;
/*      */     }
/*      */     
/* 2387 */     CACHED_PARAMETERS = new CreativeModeTab.ItemDisplayParameters(enabledFeatures, hasPermissions, lookup);
/* 2388 */     buildAllTabContents(CACHED_PARAMETERS);
/* 2389 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\CreativeModeTabs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */