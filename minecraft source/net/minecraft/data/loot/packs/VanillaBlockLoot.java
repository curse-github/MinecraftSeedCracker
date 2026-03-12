/*      */ package net.minecraft.data.loot.packs;
/*      */ 
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.advancements.criterion.ItemPredicate;
/*      */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.data.loot.BlockLootSubProvider;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.Enchantments;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.block.BedBlock;
/*      */ import net.minecraft.world.level.block.BeetrootBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CarrotBlock;
/*      */ import net.minecraft.world.level.block.CocoaBlock;
/*      */ import net.minecraft.world.level.block.ComposterBlock;
/*      */ import net.minecraft.world.level.block.CropBlock;
/*      */ import net.minecraft.world.level.block.DecoratedPotBlock;
/*      */ import net.minecraft.world.level.block.DoublePlantBlock;
/*      */ import net.minecraft.world.level.block.MangrovePropaguleBlock;
/*      */ import net.minecraft.world.level.block.NetherWartBlock;
/*      */ import net.minecraft.world.level.block.PitcherCropBlock;
/*      */ import net.minecraft.world.level.block.PotatoBlock;
/*      */ import net.minecraft.world.level.block.SeaPickleBlock;
/*      */ import net.minecraft.world.level.block.SnowLayerBlock;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.TntBlock;
/*      */ import net.minecraft.world.level.block.state.properties.BedPart;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.storage.loot.IntRange;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ import net.minecraft.world.level.storage.loot.LootPool;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
/*      */ import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootItem;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
/*      */ import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
/*      */ import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
/*      */ import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.LimitCount;
/*      */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*      */ import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
/*      */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*      */ import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class VanillaBlockLoot
/*      */   extends BlockLootSubProvider
/*      */ {
/*   70 */   private static final float[] JUNGLE_LEAVES_SAPLING_CHANGES = { 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F };
/*      */   
/*   72 */   private static final Set<Item> EXPLOSION_RESISTANT = (Set)Stream.of(new Block[] { Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.PIGLIN_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX
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
/*  103 */       }).map(ItemLike::asItem).collect(Collectors.toSet());
/*      */ 
/*      */ 
/*      */   
/*  107 */   public VanillaBlockLoot(HolderLookup.Provider registries) { super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), registries); }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void generate() {
/*  112 */     HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
/*  113 */     HolderLookup.RegistryLookup<Item> items = this.registries.lookupOrThrow(Registries.ITEM);
/*      */ 
/*      */     
/*  116 */     dropSelf(Blocks.GRANITE);
/*  117 */     dropSelf(Blocks.POLISHED_GRANITE);
/*  118 */     dropSelf(Blocks.DIORITE);
/*  119 */     dropSelf(Blocks.POLISHED_DIORITE);
/*  120 */     dropSelf(Blocks.ANDESITE);
/*  121 */     dropSelf(Blocks.POLISHED_ANDESITE);
/*  122 */     dropSelf(Blocks.DIRT);
/*  123 */     dropSelf(Blocks.COARSE_DIRT);
/*  124 */     dropSelf(Blocks.COBBLESTONE);
/*  125 */     dropSelf(Blocks.OAK_PLANKS);
/*  126 */     dropSelf(Blocks.SPRUCE_PLANKS);
/*  127 */     dropSelf(Blocks.BIRCH_PLANKS);
/*  128 */     dropSelf(Blocks.JUNGLE_PLANKS);
/*  129 */     dropSelf(Blocks.ACACIA_PLANKS);
/*  130 */     dropSelf(Blocks.DARK_OAK_PLANKS);
/*  131 */     dropSelf(Blocks.PALE_OAK_PLANKS);
/*  132 */     dropSelf(Blocks.MANGROVE_PLANKS);
/*  133 */     dropSelf(Blocks.CHERRY_PLANKS);
/*  134 */     dropSelf(Blocks.BAMBOO_PLANKS);
/*  135 */     dropSelf(Blocks.BAMBOO_MOSAIC);
/*  136 */     add(Blocks.DECORATED_POT, this::createDecoratedPotTable);
/*  137 */     dropSelf(Blocks.OAK_SAPLING);
/*  138 */     dropSelf(Blocks.SPRUCE_SAPLING);
/*  139 */     dropSelf(Blocks.BIRCH_SAPLING);
/*  140 */     dropSelf(Blocks.JUNGLE_SAPLING);
/*  141 */     dropSelf(Blocks.ACACIA_SAPLING);
/*  142 */     dropSelf(Blocks.DARK_OAK_SAPLING);
/*  143 */     dropSelf(Blocks.PALE_OAK_SAPLING);
/*  144 */     dropSelf(Blocks.CHERRY_SAPLING);
/*  145 */     dropSelf(Blocks.SAND);
/*  146 */     add(Blocks.SUSPICIOUS_SAND, noDrop());
/*  147 */     add(Blocks.SUSPICIOUS_GRAVEL, noDrop());
/*  148 */     dropSelf(Blocks.RED_SAND);
/*  149 */     dropSelf(Blocks.OAK_LOG);
/*  150 */     dropSelf(Blocks.SPRUCE_LOG);
/*  151 */     dropSelf(Blocks.BIRCH_LOG);
/*  152 */     dropSelf(Blocks.JUNGLE_LOG);
/*  153 */     dropSelf(Blocks.ACACIA_LOG);
/*  154 */     dropSelf(Blocks.DARK_OAK_LOG);
/*  155 */     dropSelf(Blocks.PALE_OAK_LOG);
/*  156 */     dropSelf(Blocks.CHERRY_LOG);
/*  157 */     dropSelf(Blocks.BAMBOO_BLOCK);
/*  158 */     dropSelf(Blocks.STRIPPED_OAK_LOG);
/*  159 */     dropSelf(Blocks.STRIPPED_SPRUCE_LOG);
/*  160 */     dropSelf(Blocks.STRIPPED_BIRCH_LOG);
/*  161 */     dropSelf(Blocks.STRIPPED_JUNGLE_LOG);
/*  162 */     dropSelf(Blocks.STRIPPED_ACACIA_LOG);
/*  163 */     dropSelf(Blocks.STRIPPED_DARK_OAK_LOG);
/*  164 */     dropSelf(Blocks.STRIPPED_PALE_OAK_LOG);
/*  165 */     dropSelf(Blocks.STRIPPED_MANGROVE_LOG);
/*  166 */     dropSelf(Blocks.STRIPPED_CHERRY_LOG);
/*  167 */     dropSelf(Blocks.STRIPPED_BAMBOO_BLOCK);
/*  168 */     dropSelf(Blocks.STRIPPED_WARPED_STEM);
/*  169 */     dropSelf(Blocks.STRIPPED_CRIMSON_STEM);
/*  170 */     dropSelf(Blocks.OAK_WOOD);
/*  171 */     dropSelf(Blocks.SPRUCE_WOOD);
/*  172 */     dropSelf(Blocks.BIRCH_WOOD);
/*  173 */     dropSelf(Blocks.JUNGLE_WOOD);
/*  174 */     dropSelf(Blocks.ACACIA_WOOD);
/*  175 */     dropSelf(Blocks.DARK_OAK_WOOD);
/*  176 */     dropSelf(Blocks.PALE_OAK_WOOD);
/*  177 */     dropSelf(Blocks.MANGROVE_WOOD);
/*  178 */     dropSelf(Blocks.CHERRY_WOOD);
/*  179 */     dropSelf(Blocks.STRIPPED_OAK_WOOD);
/*  180 */     dropSelf(Blocks.STRIPPED_SPRUCE_WOOD);
/*  181 */     dropSelf(Blocks.STRIPPED_BIRCH_WOOD);
/*  182 */     dropSelf(Blocks.STRIPPED_JUNGLE_WOOD);
/*  183 */     dropSelf(Blocks.STRIPPED_ACACIA_WOOD);
/*  184 */     dropSelf(Blocks.STRIPPED_DARK_OAK_WOOD);
/*  185 */     dropSelf(Blocks.STRIPPED_PALE_OAK_WOOD);
/*  186 */     dropSelf(Blocks.STRIPPED_MANGROVE_WOOD);
/*  187 */     dropSelf(Blocks.STRIPPED_CHERRY_WOOD);
/*  188 */     dropSelf(Blocks.STRIPPED_CRIMSON_HYPHAE);
/*  189 */     dropSelf(Blocks.STRIPPED_WARPED_HYPHAE);
/*  190 */     dropSelf(Blocks.SPONGE);
/*  191 */     dropSelf(Blocks.WET_SPONGE);
/*  192 */     dropSelf(Blocks.LAPIS_BLOCK);
/*  193 */     dropSelf(Blocks.RESIN_BLOCK);
/*  194 */     dropSelf(Blocks.SANDSTONE);
/*  195 */     dropSelf(Blocks.CHISELED_SANDSTONE);
/*  196 */     dropSelf(Blocks.CUT_SANDSTONE);
/*  197 */     dropSelf(Blocks.NOTE_BLOCK);
/*  198 */     dropSelf(Blocks.POWERED_RAIL);
/*  199 */     dropSelf(Blocks.DETECTOR_RAIL);
/*  200 */     dropSelf(Blocks.STICKY_PISTON);
/*  201 */     dropSelf(Blocks.PISTON);
/*  202 */     dropSelf(Blocks.WHITE_WOOL);
/*  203 */     dropSelf(Blocks.ORANGE_WOOL);
/*  204 */     dropSelf(Blocks.MAGENTA_WOOL);
/*  205 */     dropSelf(Blocks.LIGHT_BLUE_WOOL);
/*  206 */     dropSelf(Blocks.YELLOW_WOOL);
/*  207 */     dropSelf(Blocks.LIME_WOOL);
/*  208 */     dropSelf(Blocks.PINK_WOOL);
/*  209 */     dropSelf(Blocks.GRAY_WOOL);
/*  210 */     dropSelf(Blocks.LIGHT_GRAY_WOOL);
/*  211 */     dropSelf(Blocks.CYAN_WOOL);
/*  212 */     dropSelf(Blocks.PURPLE_WOOL);
/*  213 */     dropSelf(Blocks.BLUE_WOOL);
/*  214 */     dropSelf(Blocks.BROWN_WOOL);
/*  215 */     dropSelf(Blocks.GREEN_WOOL);
/*  216 */     dropSelf(Blocks.RED_WOOL);
/*  217 */     dropSelf(Blocks.BLACK_WOOL);
/*  218 */     dropSelf(Blocks.DANDELION);
/*  219 */     dropSelf(Blocks.OPEN_EYEBLOSSOM);
/*  220 */     dropSelf(Blocks.CLOSED_EYEBLOSSOM);
/*  221 */     dropSelf(Blocks.POPPY);
/*  222 */     dropSelf(Blocks.TORCHFLOWER);
/*  223 */     dropSelf(Blocks.BLUE_ORCHID);
/*  224 */     dropSelf(Blocks.ALLIUM);
/*  225 */     dropSelf(Blocks.AZURE_BLUET);
/*  226 */     dropSelf(Blocks.RED_TULIP);
/*  227 */     dropSelf(Blocks.ORANGE_TULIP);
/*  228 */     dropSelf(Blocks.WHITE_TULIP);
/*  229 */     dropSelf(Blocks.PINK_TULIP);
/*  230 */     dropSelf(Blocks.OXEYE_DAISY);
/*  231 */     dropSelf(Blocks.CORNFLOWER);
/*  232 */     dropSelf(Blocks.WITHER_ROSE);
/*  233 */     dropSelf(Blocks.LILY_OF_THE_VALLEY);
/*  234 */     dropSelf(Blocks.BROWN_MUSHROOM);
/*  235 */     dropSelf(Blocks.RED_MUSHROOM);
/*  236 */     dropSelf(Blocks.GOLD_BLOCK);
/*  237 */     dropSelf(Blocks.IRON_BLOCK);
/*  238 */     dropSelf(Blocks.BRICKS);
/*  239 */     dropSelf(Blocks.MOSSY_COBBLESTONE);
/*  240 */     dropSelf(Blocks.OBSIDIAN);
/*  241 */     dropSelf(Blocks.CRYING_OBSIDIAN);
/*  242 */     dropSelf(Blocks.TORCH);
/*  243 */     dropSelf(Blocks.OAK_STAIRS);
/*  244 */     dropSelf(Blocks.MANGROVE_STAIRS);
/*  245 */     dropSelf(Blocks.BAMBOO_STAIRS);
/*  246 */     dropSelf(Blocks.BAMBOO_MOSAIC_STAIRS);
/*  247 */     dropSelf(Blocks.REDSTONE_WIRE);
/*  248 */     dropSelf(Blocks.DIAMOND_BLOCK);
/*  249 */     dropSelf(Blocks.CRAFTING_TABLE);
/*  250 */     dropSelf(Blocks.OAK_SIGN);
/*  251 */     dropSelf(Blocks.SPRUCE_SIGN);
/*  252 */     dropSelf(Blocks.BIRCH_SIGN);
/*  253 */     dropSelf(Blocks.ACACIA_SIGN);
/*  254 */     dropSelf(Blocks.JUNGLE_SIGN);
/*  255 */     dropSelf(Blocks.DARK_OAK_SIGN);
/*  256 */     dropSelf(Blocks.PALE_OAK_SIGN);
/*  257 */     dropSelf(Blocks.MANGROVE_SIGN);
/*  258 */     dropSelf(Blocks.CHERRY_SIGN);
/*  259 */     dropSelf(Blocks.BAMBOO_SIGN);
/*  260 */     dropSelf(Blocks.OAK_HANGING_SIGN);
/*  261 */     dropSelf(Blocks.SPRUCE_HANGING_SIGN);
/*  262 */     dropSelf(Blocks.BIRCH_HANGING_SIGN);
/*  263 */     dropSelf(Blocks.ACACIA_HANGING_SIGN);
/*  264 */     dropSelf(Blocks.CHERRY_HANGING_SIGN);
/*  265 */     dropSelf(Blocks.JUNGLE_HANGING_SIGN);
/*  266 */     dropSelf(Blocks.DARK_OAK_HANGING_SIGN);
/*  267 */     dropSelf(Blocks.PALE_OAK_HANGING_SIGN);
/*  268 */     dropSelf(Blocks.MANGROVE_HANGING_SIGN);
/*  269 */     dropSelf(Blocks.CRIMSON_HANGING_SIGN);
/*  270 */     dropSelf(Blocks.WARPED_HANGING_SIGN);
/*  271 */     dropSelf(Blocks.BAMBOO_HANGING_SIGN);
/*  272 */     dropSelf(Blocks.LADDER);
/*  273 */     dropSelf(Blocks.RAIL);
/*  274 */     dropSelf(Blocks.COBBLESTONE_STAIRS);
/*  275 */     dropSelf(Blocks.LEVER);
/*  276 */     dropSelf(Blocks.STONE_PRESSURE_PLATE);
/*  277 */     dropSelf(Blocks.OAK_PRESSURE_PLATE);
/*  278 */     dropSelf(Blocks.SPRUCE_PRESSURE_PLATE);
/*  279 */     dropSelf(Blocks.BIRCH_PRESSURE_PLATE);
/*  280 */     dropSelf(Blocks.JUNGLE_PRESSURE_PLATE);
/*  281 */     dropSelf(Blocks.ACACIA_PRESSURE_PLATE);
/*  282 */     dropSelf(Blocks.DARK_OAK_PRESSURE_PLATE);
/*  283 */     dropSelf(Blocks.PALE_OAK_PRESSURE_PLATE);
/*  284 */     dropSelf(Blocks.MANGROVE_PRESSURE_PLATE);
/*  285 */     dropSelf(Blocks.CHERRY_PRESSURE_PLATE);
/*  286 */     dropSelf(Blocks.BAMBOO_PRESSURE_PLATE);
/*  287 */     dropSelf(Blocks.REDSTONE_TORCH);
/*  288 */     dropSelf(Blocks.STONE_BUTTON);
/*  289 */     dropSelf(Blocks.CACTUS);
/*  290 */     dropSelf(Blocks.SUGAR_CANE);
/*  291 */     dropSelf(Blocks.JUKEBOX);
/*  292 */     dropSelf(Blocks.OAK_FENCE);
/*  293 */     dropSelf(Blocks.MANGROVE_FENCE);
/*  294 */     dropSelf(Blocks.BAMBOO_FENCE);
/*  295 */     dropSelf(Blocks.PUMPKIN);
/*  296 */     dropSelf(Blocks.NETHERRACK);
/*  297 */     dropSelf(Blocks.SOUL_SAND);
/*  298 */     dropSelf(Blocks.SOUL_SOIL);
/*  299 */     dropSelf(Blocks.BASALT);
/*  300 */     dropSelf(Blocks.POLISHED_BASALT);
/*  301 */     dropSelf(Blocks.SMOOTH_BASALT);
/*  302 */     dropSelf(Blocks.SOUL_TORCH);
/*  303 */     dropSelf(Blocks.COPPER_TORCH);
/*  304 */     dropSelf(Blocks.CARVED_PUMPKIN);
/*  305 */     dropSelf(Blocks.JACK_O_LANTERN);
/*  306 */     dropSelf(Blocks.REPEATER);
/*  307 */     dropSelf(Blocks.OAK_TRAPDOOR);
/*  308 */     dropSelf(Blocks.SPRUCE_TRAPDOOR);
/*  309 */     dropSelf(Blocks.BIRCH_TRAPDOOR);
/*  310 */     dropSelf(Blocks.JUNGLE_TRAPDOOR);
/*  311 */     dropSelf(Blocks.ACACIA_TRAPDOOR);
/*  312 */     dropSelf(Blocks.DARK_OAK_TRAPDOOR);
/*  313 */     dropSelf(Blocks.PALE_OAK_TRAPDOOR);
/*  314 */     dropSelf(Blocks.MANGROVE_TRAPDOOR);
/*  315 */     dropSelf(Blocks.CHERRY_TRAPDOOR);
/*  316 */     dropSelf(Blocks.BAMBOO_TRAPDOOR);
/*  317 */     dropSelf(Blocks.COPPER_TRAPDOOR);
/*  318 */     dropSelf(Blocks.EXPOSED_COPPER_TRAPDOOR);
/*  319 */     dropSelf(Blocks.WEATHERED_COPPER_TRAPDOOR);
/*  320 */     dropSelf(Blocks.OXIDIZED_COPPER_TRAPDOOR);
/*  321 */     dropSelf(Blocks.WAXED_COPPER_TRAPDOOR);
/*  322 */     dropSelf(Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR);
/*  323 */     dropSelf(Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR);
/*  324 */     dropSelf(Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR);
/*  325 */     dropSelf(Blocks.STONE_BRICKS);
/*  326 */     dropSelf(Blocks.MOSSY_STONE_BRICKS);
/*  327 */     dropSelf(Blocks.CRACKED_STONE_BRICKS);
/*  328 */     dropSelf(Blocks.CHISELED_STONE_BRICKS);
/*  329 */     dropSelf(Blocks.IRON_BARS);
/*  330 */     Blocks.COPPER_BARS.forEach(x$0 -> rec$.dropSelf(x$0));
/*  331 */     dropSelf(Blocks.OAK_FENCE_GATE);
/*  332 */     dropSelf(Blocks.MANGROVE_FENCE_GATE);
/*  333 */     dropSelf(Blocks.BAMBOO_FENCE_GATE);
/*  334 */     dropSelf(Blocks.BRICK_STAIRS);
/*  335 */     dropSelf(Blocks.STONE_BRICK_STAIRS);
/*  336 */     dropSelf(Blocks.LILY_PAD);
/*  337 */     dropSelf(Blocks.RESIN_BRICKS);
/*  338 */     dropSelf(Blocks.RESIN_BRICK_WALL);
/*  339 */     dropSelf(Blocks.RESIN_BRICK_STAIRS);
/*  340 */     dropSelf(Blocks.CHISELED_RESIN_BRICKS);
/*  341 */     dropSelf(Blocks.NETHER_BRICKS);
/*  342 */     dropSelf(Blocks.NETHER_BRICK_FENCE);
/*  343 */     dropSelf(Blocks.NETHER_BRICK_STAIRS);
/*  344 */     dropSelf(Blocks.CAULDRON);
/*  345 */     dropSelf(Blocks.END_STONE);
/*  346 */     dropSelf(Blocks.REDSTONE_LAMP);
/*  347 */     dropSelf(Blocks.SANDSTONE_STAIRS);
/*  348 */     dropSelf(Blocks.TRIPWIRE_HOOK);
/*  349 */     dropSelf(Blocks.EMERALD_BLOCK);
/*  350 */     dropSelf(Blocks.SPRUCE_STAIRS);
/*  351 */     dropSelf(Blocks.BIRCH_STAIRS);
/*  352 */     dropSelf(Blocks.JUNGLE_STAIRS);
/*  353 */     dropSelf(Blocks.COBBLESTONE_WALL);
/*  354 */     dropSelf(Blocks.MOSSY_COBBLESTONE_WALL);
/*  355 */     dropSelf(Blocks.FLOWER_POT);
/*  356 */     dropSelf(Blocks.OAK_BUTTON);
/*  357 */     dropSelf(Blocks.SPRUCE_BUTTON);
/*  358 */     dropSelf(Blocks.BIRCH_BUTTON);
/*  359 */     dropSelf(Blocks.JUNGLE_BUTTON);
/*  360 */     dropSelf(Blocks.ACACIA_BUTTON);
/*  361 */     dropSelf(Blocks.DARK_OAK_BUTTON);
/*  362 */     dropSelf(Blocks.PALE_OAK_BUTTON);
/*  363 */     dropSelf(Blocks.MANGROVE_BUTTON);
/*  364 */     dropSelf(Blocks.CHERRY_BUTTON);
/*  365 */     dropSelf(Blocks.BAMBOO_BUTTON);
/*  366 */     dropSelf(Blocks.ANVIL);
/*  367 */     dropSelf(Blocks.CHIPPED_ANVIL);
/*  368 */     dropSelf(Blocks.DAMAGED_ANVIL);
/*  369 */     dropSelf(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE);
/*  370 */     dropSelf(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
/*  371 */     dropSelf(Blocks.COMPARATOR);
/*  372 */     dropSelf(Blocks.DAYLIGHT_DETECTOR);
/*  373 */     dropSelf(Blocks.REDSTONE_BLOCK);
/*  374 */     dropSelf(Blocks.QUARTZ_BLOCK);
/*  375 */     dropSelf(Blocks.CHISELED_QUARTZ_BLOCK);
/*  376 */     dropSelf(Blocks.QUARTZ_PILLAR);
/*  377 */     dropSelf(Blocks.QUARTZ_STAIRS);
/*  378 */     dropSelf(Blocks.ACTIVATOR_RAIL);
/*  379 */     dropSelf(Blocks.WHITE_TERRACOTTA);
/*  380 */     dropSelf(Blocks.ORANGE_TERRACOTTA);
/*  381 */     dropSelf(Blocks.MAGENTA_TERRACOTTA);
/*  382 */     dropSelf(Blocks.LIGHT_BLUE_TERRACOTTA);
/*  383 */     dropSelf(Blocks.YELLOW_TERRACOTTA);
/*  384 */     dropSelf(Blocks.LIME_TERRACOTTA);
/*  385 */     dropSelf(Blocks.PINK_TERRACOTTA);
/*  386 */     dropSelf(Blocks.GRAY_TERRACOTTA);
/*  387 */     dropSelf(Blocks.LIGHT_GRAY_TERRACOTTA);
/*  388 */     dropSelf(Blocks.CYAN_TERRACOTTA);
/*  389 */     dropSelf(Blocks.PURPLE_TERRACOTTA);
/*  390 */     dropSelf(Blocks.BLUE_TERRACOTTA);
/*  391 */     dropSelf(Blocks.BROWN_TERRACOTTA);
/*  392 */     dropSelf(Blocks.GREEN_TERRACOTTA);
/*  393 */     dropSelf(Blocks.RED_TERRACOTTA);
/*  394 */     dropSelf(Blocks.BLACK_TERRACOTTA);
/*  395 */     dropSelf(Blocks.ACACIA_STAIRS);
/*  396 */     dropSelf(Blocks.DARK_OAK_STAIRS);
/*  397 */     dropSelf(Blocks.PALE_OAK_STAIRS);
/*  398 */     dropSelf(Blocks.CHERRY_STAIRS);
/*  399 */     dropSelf(Blocks.SLIME_BLOCK);
/*  400 */     dropSelf(Blocks.IRON_TRAPDOOR);
/*  401 */     dropSelf(Blocks.PRISMARINE);
/*  402 */     dropSelf(Blocks.PRISMARINE_BRICKS);
/*  403 */     dropSelf(Blocks.DARK_PRISMARINE);
/*  404 */     dropSelf(Blocks.PRISMARINE_STAIRS);
/*  405 */     dropSelf(Blocks.PRISMARINE_BRICK_STAIRS);
/*  406 */     dropSelf(Blocks.DARK_PRISMARINE_STAIRS);
/*  407 */     dropSelf(Blocks.HAY_BLOCK);
/*  408 */     dropSelf(Blocks.WHITE_CARPET);
/*  409 */     dropSelf(Blocks.ORANGE_CARPET);
/*  410 */     dropSelf(Blocks.MAGENTA_CARPET);
/*  411 */     dropSelf(Blocks.LIGHT_BLUE_CARPET);
/*  412 */     dropSelf(Blocks.YELLOW_CARPET);
/*  413 */     dropSelf(Blocks.LIME_CARPET);
/*  414 */     dropSelf(Blocks.PINK_CARPET);
/*  415 */     dropSelf(Blocks.GRAY_CARPET);
/*  416 */     dropSelf(Blocks.LIGHT_GRAY_CARPET);
/*  417 */     dropSelf(Blocks.CYAN_CARPET);
/*  418 */     dropSelf(Blocks.PURPLE_CARPET);
/*  419 */     dropSelf(Blocks.BLUE_CARPET);
/*  420 */     dropSelf(Blocks.BROWN_CARPET);
/*  421 */     dropSelf(Blocks.GREEN_CARPET);
/*  422 */     dropSelf(Blocks.RED_CARPET);
/*  423 */     dropSelf(Blocks.BLACK_CARPET);
/*  424 */     dropSelf(Blocks.TERRACOTTA);
/*  425 */     dropSelf(Blocks.COAL_BLOCK);
/*  426 */     dropSelf(Blocks.RED_SANDSTONE);
/*  427 */     dropSelf(Blocks.CHISELED_RED_SANDSTONE);
/*  428 */     dropSelf(Blocks.CUT_RED_SANDSTONE);
/*  429 */     dropSelf(Blocks.RED_SANDSTONE_STAIRS);
/*  430 */     dropSelf(Blocks.SMOOTH_STONE);
/*  431 */     dropSelf(Blocks.SMOOTH_SANDSTONE);
/*  432 */     dropSelf(Blocks.SMOOTH_QUARTZ);
/*  433 */     dropSelf(Blocks.SMOOTH_RED_SANDSTONE);
/*  434 */     dropSelf(Blocks.SPRUCE_FENCE_GATE);
/*  435 */     dropSelf(Blocks.BIRCH_FENCE_GATE);
/*  436 */     dropSelf(Blocks.JUNGLE_FENCE_GATE);
/*  437 */     dropSelf(Blocks.ACACIA_FENCE_GATE);
/*  438 */     dropSelf(Blocks.DARK_OAK_FENCE_GATE);
/*  439 */     dropSelf(Blocks.PALE_OAK_FENCE_GATE);
/*  440 */     dropSelf(Blocks.CHERRY_FENCE_GATE);
/*  441 */     dropSelf(Blocks.SPRUCE_FENCE);
/*  442 */     dropSelf(Blocks.BIRCH_FENCE);
/*  443 */     dropSelf(Blocks.JUNGLE_FENCE);
/*  444 */     dropSelf(Blocks.ACACIA_FENCE);
/*  445 */     dropSelf(Blocks.DARK_OAK_FENCE);
/*  446 */     dropSelf(Blocks.PALE_OAK_FENCE);
/*  447 */     dropSelf(Blocks.CHERRY_FENCE);
/*  448 */     dropSelf(Blocks.END_ROD);
/*  449 */     dropSelf(Blocks.PURPUR_BLOCK);
/*  450 */     dropSelf(Blocks.PURPUR_PILLAR);
/*  451 */     dropSelf(Blocks.PURPUR_STAIRS);
/*  452 */     dropSelf(Blocks.END_STONE_BRICKS);
/*  453 */     dropSelf(Blocks.MAGMA_BLOCK);
/*  454 */     dropSelf(Blocks.NETHER_WART_BLOCK);
/*  455 */     dropSelf(Blocks.RED_NETHER_BRICKS);
/*  456 */     dropSelf(Blocks.BONE_BLOCK);
/*  457 */     dropSelf(Blocks.OBSERVER);
/*  458 */     dropSelf(Blocks.TARGET);
/*  459 */     dropSelf(Blocks.WHITE_GLAZED_TERRACOTTA);
/*  460 */     dropSelf(Blocks.ORANGE_GLAZED_TERRACOTTA);
/*  461 */     dropSelf(Blocks.MAGENTA_GLAZED_TERRACOTTA);
/*  462 */     dropSelf(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA);
/*  463 */     dropSelf(Blocks.YELLOW_GLAZED_TERRACOTTA);
/*  464 */     dropSelf(Blocks.LIME_GLAZED_TERRACOTTA);
/*  465 */     dropSelf(Blocks.PINK_GLAZED_TERRACOTTA);
/*  466 */     dropSelf(Blocks.GRAY_GLAZED_TERRACOTTA);
/*  467 */     dropSelf(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA);
/*  468 */     dropSelf(Blocks.CYAN_GLAZED_TERRACOTTA);
/*  469 */     dropSelf(Blocks.PURPLE_GLAZED_TERRACOTTA);
/*  470 */     dropSelf(Blocks.BLUE_GLAZED_TERRACOTTA);
/*  471 */     dropSelf(Blocks.BROWN_GLAZED_TERRACOTTA);
/*  472 */     dropSelf(Blocks.GREEN_GLAZED_TERRACOTTA);
/*  473 */     dropSelf(Blocks.RED_GLAZED_TERRACOTTA);
/*  474 */     dropSelf(Blocks.BLACK_GLAZED_TERRACOTTA);
/*  475 */     dropSelf(Blocks.WHITE_CONCRETE);
/*  476 */     dropSelf(Blocks.ORANGE_CONCRETE);
/*  477 */     dropSelf(Blocks.MAGENTA_CONCRETE);
/*  478 */     dropSelf(Blocks.LIGHT_BLUE_CONCRETE);
/*  479 */     dropSelf(Blocks.YELLOW_CONCRETE);
/*  480 */     dropSelf(Blocks.LIME_CONCRETE);
/*  481 */     dropSelf(Blocks.PINK_CONCRETE);
/*  482 */     dropSelf(Blocks.GRAY_CONCRETE);
/*  483 */     dropSelf(Blocks.LIGHT_GRAY_CONCRETE);
/*  484 */     dropSelf(Blocks.CYAN_CONCRETE);
/*  485 */     dropSelf(Blocks.PURPLE_CONCRETE);
/*  486 */     dropSelf(Blocks.BLUE_CONCRETE);
/*  487 */     dropSelf(Blocks.BROWN_CONCRETE);
/*  488 */     dropSelf(Blocks.GREEN_CONCRETE);
/*  489 */     dropSelf(Blocks.RED_CONCRETE);
/*  490 */     dropSelf(Blocks.BLACK_CONCRETE);
/*  491 */     dropSelf(Blocks.WHITE_CONCRETE_POWDER);
/*  492 */     dropSelf(Blocks.ORANGE_CONCRETE_POWDER);
/*  493 */     dropSelf(Blocks.MAGENTA_CONCRETE_POWDER);
/*  494 */     dropSelf(Blocks.LIGHT_BLUE_CONCRETE_POWDER);
/*  495 */     dropSelf(Blocks.YELLOW_CONCRETE_POWDER);
/*  496 */     dropSelf(Blocks.LIME_CONCRETE_POWDER);
/*  497 */     dropSelf(Blocks.PINK_CONCRETE_POWDER);
/*  498 */     dropSelf(Blocks.GRAY_CONCRETE_POWDER);
/*  499 */     dropSelf(Blocks.LIGHT_GRAY_CONCRETE_POWDER);
/*  500 */     dropSelf(Blocks.CYAN_CONCRETE_POWDER);
/*  501 */     dropSelf(Blocks.PURPLE_CONCRETE_POWDER);
/*  502 */     dropSelf(Blocks.BLUE_CONCRETE_POWDER);
/*  503 */     dropSelf(Blocks.BROWN_CONCRETE_POWDER);
/*  504 */     dropSelf(Blocks.GREEN_CONCRETE_POWDER);
/*  505 */     dropSelf(Blocks.RED_CONCRETE_POWDER);
/*  506 */     dropSelf(Blocks.BLACK_CONCRETE_POWDER);
/*  507 */     dropSelf(Blocks.KELP);
/*  508 */     dropSelf(Blocks.DRIED_KELP_BLOCK);
/*  509 */     dropSelf(Blocks.DEAD_TUBE_CORAL_BLOCK);
/*  510 */     dropSelf(Blocks.DEAD_BRAIN_CORAL_BLOCK);
/*  511 */     dropSelf(Blocks.DEAD_BUBBLE_CORAL_BLOCK);
/*  512 */     dropSelf(Blocks.DEAD_FIRE_CORAL_BLOCK);
/*  513 */     dropSelf(Blocks.DEAD_HORN_CORAL_BLOCK);
/*  514 */     dropSelf(Blocks.CONDUIT);
/*  515 */     dropSelf(Blocks.DRAGON_EGG);
/*  516 */     dropSelf(Blocks.BAMBOO);
/*  517 */     dropSelf(Blocks.POLISHED_GRANITE_STAIRS);
/*  518 */     dropSelf(Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
/*  519 */     dropSelf(Blocks.MOSSY_STONE_BRICK_STAIRS);
/*  520 */     dropSelf(Blocks.POLISHED_DIORITE_STAIRS);
/*  521 */     dropSelf(Blocks.MOSSY_COBBLESTONE_STAIRS);
/*  522 */     dropSelf(Blocks.END_STONE_BRICK_STAIRS);
/*  523 */     dropSelf(Blocks.STONE_STAIRS);
/*  524 */     dropSelf(Blocks.SMOOTH_SANDSTONE_STAIRS);
/*  525 */     dropSelf(Blocks.SMOOTH_QUARTZ_STAIRS);
/*  526 */     dropSelf(Blocks.GRANITE_STAIRS);
/*  527 */     dropSelf(Blocks.ANDESITE_STAIRS);
/*  528 */     dropSelf(Blocks.RED_NETHER_BRICK_STAIRS);
/*  529 */     dropSelf(Blocks.POLISHED_ANDESITE_STAIRS);
/*  530 */     dropSelf(Blocks.DIORITE_STAIRS);
/*  531 */     dropSelf(Blocks.BRICK_WALL);
/*  532 */     dropSelf(Blocks.PRISMARINE_WALL);
/*  533 */     dropSelf(Blocks.RED_SANDSTONE_WALL);
/*  534 */     dropSelf(Blocks.MOSSY_STONE_BRICK_WALL);
/*  535 */     dropSelf(Blocks.GRANITE_WALL);
/*  536 */     dropSelf(Blocks.STONE_BRICK_WALL);
/*  537 */     dropSelf(Blocks.NETHER_BRICK_WALL);
/*  538 */     dropSelf(Blocks.ANDESITE_WALL);
/*  539 */     dropSelf(Blocks.RED_NETHER_BRICK_WALL);
/*  540 */     dropSelf(Blocks.SANDSTONE_WALL);
/*  541 */     dropSelf(Blocks.END_STONE_BRICK_WALL);
/*  542 */     dropSelf(Blocks.DIORITE_WALL);
/*  543 */     dropSelf(Blocks.MUD_BRICK_WALL);
/*  544 */     dropSelf(Blocks.LOOM);
/*  545 */     dropSelf(Blocks.SCAFFOLDING);
/*  546 */     dropSelf(Blocks.HONEY_BLOCK);
/*  547 */     dropSelf(Blocks.HONEYCOMB_BLOCK);
/*  548 */     dropSelf(Blocks.RESPAWN_ANCHOR);
/*  549 */     dropSelf(Blocks.LODESTONE);
/*  550 */     dropSelf(Blocks.WARPED_STEM);
/*  551 */     dropSelf(Blocks.WARPED_HYPHAE);
/*  552 */     dropSelf(Blocks.WARPED_FUNGUS);
/*  553 */     dropSelf(Blocks.WARPED_WART_BLOCK);
/*  554 */     dropSelf(Blocks.CRIMSON_STEM);
/*  555 */     dropSelf(Blocks.CRIMSON_HYPHAE);
/*  556 */     dropSelf(Blocks.CRIMSON_FUNGUS);
/*  557 */     dropSelf(Blocks.SHROOMLIGHT);
/*  558 */     dropSelf(Blocks.CRIMSON_PLANKS);
/*  559 */     dropSelf(Blocks.WARPED_PLANKS);
/*  560 */     dropSelf(Blocks.WARPED_PRESSURE_PLATE);
/*  561 */     dropSelf(Blocks.WARPED_FENCE);
/*  562 */     dropSelf(Blocks.WARPED_TRAPDOOR);
/*  563 */     dropSelf(Blocks.WARPED_FENCE_GATE);
/*  564 */     dropSelf(Blocks.WARPED_STAIRS);
/*  565 */     dropSelf(Blocks.WARPED_BUTTON);
/*  566 */     dropSelf(Blocks.WARPED_SIGN);
/*  567 */     dropSelf(Blocks.CRIMSON_PRESSURE_PLATE);
/*  568 */     dropSelf(Blocks.CRIMSON_FENCE);
/*  569 */     dropSelf(Blocks.CRIMSON_TRAPDOOR);
/*  570 */     dropSelf(Blocks.CRIMSON_FENCE_GATE);
/*  571 */     dropSelf(Blocks.CRIMSON_STAIRS);
/*  572 */     dropSelf(Blocks.CRIMSON_BUTTON);
/*  573 */     dropSelf(Blocks.CRIMSON_SIGN);
/*  574 */     dropSelf(Blocks.NETHERITE_BLOCK);
/*  575 */     dropSelf(Blocks.ANCIENT_DEBRIS);
/*  576 */     dropSelf(Blocks.BLACKSTONE);
/*  577 */     dropSelf(Blocks.POLISHED_BLACKSTONE_BRICKS);
/*  578 */     dropSelf(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*  579 */     dropSelf(Blocks.BLACKSTONE_STAIRS);
/*  580 */     dropSelf(Blocks.BLACKSTONE_WALL);
/*  581 */     dropSelf(Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*  582 */     dropSelf(Blocks.CHISELED_POLISHED_BLACKSTONE);
/*  583 */     dropSelf(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*  584 */     dropSelf(Blocks.POLISHED_BLACKSTONE);
/*  585 */     dropSelf(Blocks.POLISHED_BLACKSTONE_STAIRS);
/*  586 */     dropSelf(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE);
/*  587 */     dropSelf(Blocks.POLISHED_BLACKSTONE_BUTTON);
/*  588 */     dropSelf(Blocks.POLISHED_BLACKSTONE_WALL);
/*  589 */     dropSelf(Blocks.CHISELED_NETHER_BRICKS);
/*  590 */     dropSelf(Blocks.CRACKED_NETHER_BRICKS);
/*  591 */     dropSelf(Blocks.QUARTZ_BRICKS);
/*  592 */     dropSelf(Blocks.IRON_CHAIN);
/*  593 */     Blocks.COPPER_CHAIN.forEach(x$0 -> rec$.dropSelf(x$0));
/*  594 */     dropSelf(Blocks.WARPED_ROOTS);
/*  595 */     dropSelf(Blocks.CRIMSON_ROOTS);
/*  596 */     dropSelf(Blocks.MUD_BRICKS);
/*  597 */     dropSelf(Blocks.MUDDY_MANGROVE_ROOTS);
/*  598 */     dropSelf(Blocks.MUD_BRICK_STAIRS);
/*  599 */     dropSelf(Blocks.AMETHYST_BLOCK);
/*  600 */     dropSelf(Blocks.CALCITE);
/*  601 */     dropSelf(Blocks.TUFF);
/*  602 */     dropSelf(Blocks.TINTED_GLASS);
/*  603 */     dropWhenSilkTouch(Blocks.SCULK_SENSOR);
/*  604 */     dropWhenSilkTouch(Blocks.CALIBRATED_SCULK_SENSOR);
/*  605 */     dropWhenSilkTouch(Blocks.SCULK);
/*  606 */     dropWhenSilkTouch(Blocks.SCULK_CATALYST);
/*  607 */     add(Blocks.SCULK_VEIN, block -> createMultifaceBlockDrops(block, hasSilkTouch()));
/*  608 */     dropWhenSilkTouch(Blocks.SCULK_SHRIEKER);
/*  609 */     dropWhenSilkTouch(Blocks.CHISELED_BOOKSHELF);
/*      */     
/*  611 */     dropSelf(Blocks.COPPER_BLOCK);
/*  612 */     dropSelf(Blocks.EXPOSED_COPPER);
/*  613 */     dropSelf(Blocks.WEATHERED_COPPER);
/*  614 */     dropSelf(Blocks.OXIDIZED_COPPER);
/*  615 */     dropSelf(Blocks.CUT_COPPER);
/*  616 */     dropSelf(Blocks.EXPOSED_CUT_COPPER);
/*  617 */     dropSelf(Blocks.WEATHERED_CUT_COPPER);
/*  618 */     dropSelf(Blocks.OXIDIZED_CUT_COPPER);
/*  619 */     dropSelf(Blocks.WAXED_COPPER_BLOCK);
/*  620 */     dropSelf(Blocks.WAXED_WEATHERED_COPPER);
/*  621 */     dropSelf(Blocks.WAXED_EXPOSED_COPPER);
/*  622 */     dropSelf(Blocks.WAXED_OXIDIZED_COPPER);
/*  623 */     dropSelf(Blocks.WAXED_CUT_COPPER);
/*  624 */     dropSelf(Blocks.WAXED_WEATHERED_CUT_COPPER);
/*  625 */     dropSelf(Blocks.WAXED_EXPOSED_CUT_COPPER);
/*  626 */     dropSelf(Blocks.WAXED_OXIDIZED_CUT_COPPER);
/*  627 */     dropSelf(Blocks.WAXED_CUT_COPPER_STAIRS);
/*  628 */     dropSelf(Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS);
/*  629 */     dropSelf(Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS);
/*  630 */     dropSelf(Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
/*  631 */     dropSelf(Blocks.CUT_COPPER_STAIRS);
/*  632 */     dropSelf(Blocks.EXPOSED_CUT_COPPER_STAIRS);
/*  633 */     dropSelf(Blocks.WEATHERED_CUT_COPPER_STAIRS);
/*  634 */     dropSelf(Blocks.OXIDIZED_CUT_COPPER_STAIRS);
/*  635 */     dropSelf(Blocks.LIGHTNING_ROD);
/*  636 */     dropSelf(Blocks.EXPOSED_LIGHTNING_ROD);
/*  637 */     dropSelf(Blocks.WEATHERED_LIGHTNING_ROD);
/*  638 */     dropSelf(Blocks.OXIDIZED_LIGHTNING_ROD);
/*  639 */     dropSelf(Blocks.WAXED_LIGHTNING_ROD);
/*  640 */     dropSelf(Blocks.WAXED_EXPOSED_LIGHTNING_ROD);
/*  641 */     dropSelf(Blocks.WAXED_WEATHERED_LIGHTNING_ROD);
/*  642 */     dropSelf(Blocks.WAXED_OXIDIZED_LIGHTNING_ROD);
/*  643 */     dropSelf(Blocks.POINTED_DRIPSTONE);
/*  644 */     dropSelf(Blocks.DRIPSTONE_BLOCK);
/*  645 */     dropSelf(Blocks.SPORE_BLOSSOM);
/*  646 */     dropSelf(Blocks.FLOWERING_AZALEA);
/*  647 */     dropSelf(Blocks.AZALEA);
/*  648 */     dropSelf(Blocks.MOSS_CARPET);
/*  649 */     add(Blocks.PINK_PETALS, createSegmentedBlockDrops(Blocks.PINK_PETALS));
/*  650 */     add(Blocks.WILDFLOWERS, createSegmentedBlockDrops(Blocks.WILDFLOWERS));
/*  651 */     add(Blocks.LEAF_LITTER, createSegmentedBlockDrops(Blocks.LEAF_LITTER));
/*  652 */     dropSelf(Blocks.BIG_DRIPLEAF);
/*  653 */     dropSelf(Blocks.MOSS_BLOCK);
/*  654 */     add(Blocks.PALE_MOSS_CARPET, x$0 -> rec$.createMossyCarpetBlockDrops(x$0));
/*  655 */     add(Blocks.PALE_HANGING_MOSS, x$0 -> rec$.createShearsOrSilkTouchOnlyDrop(x$0));
/*  656 */     dropSelf(Blocks.PALE_MOSS_BLOCK);
/*  657 */     dropSelf(Blocks.ROOTED_DIRT);
/*  658 */     dropSelf(Blocks.COBBLED_DEEPSLATE);
/*  659 */     dropSelf(Blocks.COBBLED_DEEPSLATE_STAIRS);
/*  660 */     dropSelf(Blocks.COBBLED_DEEPSLATE_WALL);
/*  661 */     dropSelf(Blocks.POLISHED_DEEPSLATE);
/*  662 */     dropSelf(Blocks.POLISHED_DEEPSLATE_STAIRS);
/*  663 */     dropSelf(Blocks.POLISHED_DEEPSLATE_WALL);
/*  664 */     dropSelf(Blocks.DEEPSLATE_TILES);
/*  665 */     dropSelf(Blocks.DEEPSLATE_TILE_STAIRS);
/*  666 */     dropSelf(Blocks.DEEPSLATE_TILE_WALL);
/*  667 */     dropSelf(Blocks.DEEPSLATE_BRICKS);
/*  668 */     dropSelf(Blocks.DEEPSLATE_BRICK_STAIRS);
/*  669 */     dropSelf(Blocks.DEEPSLATE_BRICK_WALL);
/*  670 */     dropSelf(Blocks.CHISELED_DEEPSLATE);
/*  671 */     dropSelf(Blocks.CRACKED_DEEPSLATE_BRICKS);
/*  672 */     dropSelf(Blocks.CRACKED_DEEPSLATE_TILES);
/*  673 */     dropSelf(Blocks.RAW_IRON_BLOCK);
/*  674 */     dropSelf(Blocks.RAW_COPPER_BLOCK);
/*  675 */     dropSelf(Blocks.RAW_GOLD_BLOCK);
/*  676 */     dropSelf(Blocks.OCHRE_FROGLIGHT);
/*  677 */     dropSelf(Blocks.VERDANT_FROGLIGHT);
/*  678 */     dropSelf(Blocks.PEARLESCENT_FROGLIGHT);
/*  679 */     dropSelf(Blocks.MANGROVE_ROOTS);
/*  680 */     dropSelf(Blocks.MANGROVE_LOG);
/*  681 */     dropSelf(Blocks.MUD);
/*  682 */     dropSelf(Blocks.PACKED_MUD);
/*  683 */     dropSelf(Blocks.CRAFTER);
/*  684 */     dropSelf(Blocks.CHISELED_TUFF);
/*  685 */     dropSelf(Blocks.TUFF_STAIRS);
/*  686 */     dropSelf(Blocks.TUFF_WALL);
/*  687 */     dropSelf(Blocks.POLISHED_TUFF);
/*  688 */     dropSelf(Blocks.POLISHED_TUFF_STAIRS);
/*  689 */     dropSelf(Blocks.POLISHED_TUFF_WALL);
/*  690 */     dropSelf(Blocks.TUFF_BRICKS);
/*  691 */     dropSelf(Blocks.TUFF_BRICK_STAIRS);
/*  692 */     dropSelf(Blocks.TUFF_BRICK_WALL);
/*  693 */     dropSelf(Blocks.CHISELED_TUFF_BRICKS);
/*  694 */     add(Blocks.TUFF_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  695 */     add(Blocks.TUFF_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  696 */     add(Blocks.POLISHED_TUFF_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  697 */     dropSelf(Blocks.CHISELED_COPPER);
/*  698 */     dropSelf(Blocks.EXPOSED_CHISELED_COPPER);
/*  699 */     dropSelf(Blocks.WEATHERED_CHISELED_COPPER);
/*  700 */     dropSelf(Blocks.OXIDIZED_CHISELED_COPPER);
/*  701 */     dropSelf(Blocks.WAXED_CHISELED_COPPER);
/*  702 */     dropSelf(Blocks.WAXED_EXPOSED_CHISELED_COPPER);
/*  703 */     dropSelf(Blocks.WAXED_WEATHERED_CHISELED_COPPER);
/*  704 */     dropSelf(Blocks.WAXED_OXIDIZED_CHISELED_COPPER);
/*  705 */     dropSelf(Blocks.COPPER_GRATE);
/*  706 */     dropSelf(Blocks.EXPOSED_COPPER_GRATE);
/*  707 */     dropSelf(Blocks.WEATHERED_COPPER_GRATE);
/*  708 */     dropSelf(Blocks.OXIDIZED_COPPER_GRATE);
/*  709 */     dropSelf(Blocks.WAXED_COPPER_GRATE);
/*  710 */     dropSelf(Blocks.WAXED_EXPOSED_COPPER_GRATE);
/*  711 */     dropSelf(Blocks.WAXED_WEATHERED_COPPER_GRATE);
/*  712 */     dropSelf(Blocks.WAXED_OXIDIZED_COPPER_GRATE);
/*  713 */     dropSelf(Blocks.COPPER_BULB);
/*  714 */     dropSelf(Blocks.EXPOSED_COPPER_BULB);
/*  715 */     dropSelf(Blocks.WEATHERED_COPPER_BULB);
/*  716 */     dropSelf(Blocks.OXIDIZED_COPPER_BULB);
/*  717 */     dropSelf(Blocks.WAXED_COPPER_BULB);
/*  718 */     dropSelf(Blocks.WAXED_EXPOSED_COPPER_BULB);
/*  719 */     dropSelf(Blocks.WAXED_WEATHERED_COPPER_BULB);
/*  720 */     dropSelf(Blocks.WAXED_OXIDIZED_COPPER_BULB);
/*  721 */     add(Blocks.COPPER_GOLEM_STATUE, x$0 -> rec$.createCopperGolemStatueBlock(x$0));
/*  722 */     add(Blocks.EXPOSED_COPPER_GOLEM_STATUE, x$0 -> rec$.createCopperGolemStatueBlock(x$0));
/*  723 */     add(Blocks.WEATHERED_COPPER_GOLEM_STATUE, x$0 -> rec$.createCopperGolemStatueBlock(x$0));
/*  724 */     add(Blocks.OXIDIZED_COPPER_GOLEM_STATUE, x$0 -> rec$.createCopperGolemStatueBlock(x$0));
/*  725 */     add(Blocks.WAXED_COPPER_GOLEM_STATUE, x$0 -> rec$.createCopperGolemStatueBlock(x$0));
/*  726 */     add(Blocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE, x$0 -> rec$.createCopperGolemStatueBlock(x$0));
/*  727 */     add(Blocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE, x$0 -> rec$.createCopperGolemStatueBlock(x$0));
/*  728 */     add(Blocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE, x$0 -> rec$.createCopperGolemStatueBlock(x$0));
/*  729 */     dropSelf(Blocks.HEAVY_CORE);
/*  730 */     dropSelf(Blocks.FIREFLY_BUSH);
/*  731 */     dropSelf(Blocks.CACTUS_FLOWER);
/*      */ 
/*      */     
/*  734 */     dropOther(Blocks.FARMLAND, Blocks.DIRT);
/*  735 */     dropOther(Blocks.TRIPWIRE, Items.STRING);
/*  736 */     dropOther(Blocks.DIRT_PATH, Blocks.DIRT);
/*  737 */     dropOther(Blocks.KELP_PLANT, Blocks.KELP);
/*  738 */     dropOther(Blocks.BAMBOO_SAPLING, Blocks.BAMBOO);
/*  739 */     dropOther(Blocks.WATER_CAULDRON, Blocks.CAULDRON);
/*  740 */     dropOther(Blocks.LAVA_CAULDRON, Blocks.CAULDRON);
/*  741 */     dropOther(Blocks.POWDER_SNOW_CAULDRON, Blocks.CAULDRON);
/*  742 */     dropOther(Blocks.BIG_DRIPLEAF_STEM, Blocks.BIG_DRIPLEAF);
/*      */ 
/*      */     
/*  745 */     add(Blocks.STONE, block -> createSingleItemTableWithSilkTouch(block, Blocks.COBBLESTONE));
/*  746 */     add(Blocks.DEEPSLATE, block -> createSingleItemTableWithSilkTouch(block, Blocks.COBBLED_DEEPSLATE));
/*  747 */     add(Blocks.GRASS_BLOCK, block -> createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
/*  748 */     add(Blocks.PODZOL, block -> createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
/*  749 */     add(Blocks.MYCELIUM, block -> createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
/*  750 */     add(Blocks.TUBE_CORAL_BLOCK, block -> createSingleItemTableWithSilkTouch(block, Blocks.DEAD_TUBE_CORAL_BLOCK));
/*  751 */     add(Blocks.BRAIN_CORAL_BLOCK, block -> createSingleItemTableWithSilkTouch(block, Blocks.DEAD_BRAIN_CORAL_BLOCK));
/*  752 */     add(Blocks.BUBBLE_CORAL_BLOCK, block -> createSingleItemTableWithSilkTouch(block, Blocks.DEAD_BUBBLE_CORAL_BLOCK));
/*  753 */     add(Blocks.FIRE_CORAL_BLOCK, block -> createSingleItemTableWithSilkTouch(block, Blocks.DEAD_FIRE_CORAL_BLOCK));
/*  754 */     add(Blocks.HORN_CORAL_BLOCK, block -> createSingleItemTableWithSilkTouch(block, Blocks.DEAD_HORN_CORAL_BLOCK));
/*  755 */     add(Blocks.CRIMSON_NYLIUM, block -> createSingleItemTableWithSilkTouch(block, Blocks.NETHERRACK));
/*  756 */     add(Blocks.WARPED_NYLIUM, block -> createSingleItemTableWithSilkTouch(block, Blocks.NETHERRACK));
/*      */ 
/*      */     
/*  759 */     add(Blocks.BOOKSHELF, block -> createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3.0F)));
/*  760 */     add(Blocks.CLAY, block -> createSingleItemTableWithSilkTouch(block, Items.CLAY_BALL, ConstantValue.exactly(4.0F)));
/*  761 */     add(Blocks.ENDER_CHEST, block -> createSingleItemTableWithSilkTouch(block, Blocks.OBSIDIAN, ConstantValue.exactly(8.0F)));
/*  762 */     add(Blocks.SNOW_BLOCK, block -> createSingleItemTableWithSilkTouch(block, Items.SNOWBALL, ConstantValue.exactly(4.0F)));
/*      */     
/*  764 */     add(Blocks.CHORUS_PLANT, createSingleItemTable(Items.CHORUS_FRUIT, UniformGenerator.between(0.0F, 1.0F)));
/*      */ 
/*      */     
/*  767 */     dropPottedContents(Blocks.POTTED_OAK_SAPLING);
/*  768 */     dropPottedContents(Blocks.POTTED_SPRUCE_SAPLING);
/*  769 */     dropPottedContents(Blocks.POTTED_BIRCH_SAPLING);
/*  770 */     dropPottedContents(Blocks.POTTED_JUNGLE_SAPLING);
/*  771 */     dropPottedContents(Blocks.POTTED_ACACIA_SAPLING);
/*  772 */     dropPottedContents(Blocks.POTTED_DARK_OAK_SAPLING);
/*  773 */     dropPottedContents(Blocks.POTTED_PALE_OAK_SAPLING);
/*  774 */     dropPottedContents(Blocks.POTTED_MANGROVE_PROPAGULE);
/*  775 */     dropPottedContents(Blocks.POTTED_CHERRY_SAPLING);
/*  776 */     dropPottedContents(Blocks.POTTED_FERN);
/*  777 */     dropPottedContents(Blocks.POTTED_DANDELION);
/*  778 */     dropPottedContents(Blocks.POTTED_POPPY);
/*  779 */     dropPottedContents(Blocks.POTTED_OPEN_EYEBLOSSOM);
/*  780 */     dropPottedContents(Blocks.POTTED_CLOSED_EYEBLOSSOM);
/*  781 */     dropPottedContents(Blocks.POTTED_BLUE_ORCHID);
/*  782 */     dropPottedContents(Blocks.POTTED_ALLIUM);
/*  783 */     dropPottedContents(Blocks.POTTED_AZURE_BLUET);
/*  784 */     dropPottedContents(Blocks.POTTED_RED_TULIP);
/*  785 */     dropPottedContents(Blocks.POTTED_ORANGE_TULIP);
/*  786 */     dropPottedContents(Blocks.POTTED_WHITE_TULIP);
/*  787 */     dropPottedContents(Blocks.POTTED_PINK_TULIP);
/*  788 */     dropPottedContents(Blocks.POTTED_OXEYE_DAISY);
/*  789 */     dropPottedContents(Blocks.POTTED_CORNFLOWER);
/*  790 */     dropPottedContents(Blocks.POTTED_LILY_OF_THE_VALLEY);
/*  791 */     dropPottedContents(Blocks.POTTED_WITHER_ROSE);
/*  792 */     dropPottedContents(Blocks.POTTED_RED_MUSHROOM);
/*  793 */     dropPottedContents(Blocks.POTTED_BROWN_MUSHROOM);
/*  794 */     dropPottedContents(Blocks.POTTED_DEAD_BUSH);
/*  795 */     dropPottedContents(Blocks.POTTED_CACTUS);
/*  796 */     dropPottedContents(Blocks.POTTED_BAMBOO);
/*  797 */     dropPottedContents(Blocks.POTTED_CRIMSON_FUNGUS);
/*  798 */     dropPottedContents(Blocks.POTTED_WARPED_FUNGUS);
/*  799 */     dropPottedContents(Blocks.POTTED_CRIMSON_ROOTS);
/*  800 */     dropPottedContents(Blocks.POTTED_WARPED_ROOTS);
/*  801 */     dropPottedContents(Blocks.POTTED_AZALEA);
/*  802 */     dropPottedContents(Blocks.POTTED_FLOWERING_AZALEA);
/*  803 */     dropPottedContents(Blocks.POTTED_TORCHFLOWER);
/*      */ 
/*      */     
/*  806 */     add(Blocks.OAK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  807 */     add(Blocks.PETRIFIED_OAK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  808 */     add(Blocks.SPRUCE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  809 */     add(Blocks.BIRCH_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  810 */     add(Blocks.JUNGLE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  811 */     add(Blocks.ACACIA_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  812 */     add(Blocks.DARK_OAK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  813 */     add(Blocks.PALE_OAK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  814 */     add(Blocks.MANGROVE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  815 */     add(Blocks.CHERRY_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  816 */     add(Blocks.BAMBOO_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  817 */     add(Blocks.BAMBOO_MOSAIC_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  818 */     add(Blocks.BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  819 */     add(Blocks.COBBLESTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  820 */     add(Blocks.DARK_PRISMARINE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  821 */     add(Blocks.NETHER_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  822 */     add(Blocks.PRISMARINE_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  823 */     add(Blocks.PRISMARINE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  824 */     add(Blocks.PURPUR_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  825 */     add(Blocks.QUARTZ_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  826 */     add(Blocks.RED_SANDSTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  827 */     add(Blocks.SANDSTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  828 */     add(Blocks.CUT_RED_SANDSTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  829 */     add(Blocks.CUT_SANDSTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  830 */     add(Blocks.STONE_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  831 */     add(Blocks.STONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  832 */     add(Blocks.SMOOTH_STONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  833 */     add(Blocks.POLISHED_GRANITE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  834 */     add(Blocks.SMOOTH_RED_SANDSTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  835 */     add(Blocks.MOSSY_STONE_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  836 */     add(Blocks.POLISHED_DIORITE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  837 */     add(Blocks.MOSSY_COBBLESTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  838 */     add(Blocks.END_STONE_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  839 */     add(Blocks.SMOOTH_SANDSTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  840 */     add(Blocks.SMOOTH_QUARTZ_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  841 */     add(Blocks.GRANITE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  842 */     add(Blocks.ANDESITE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  843 */     add(Blocks.RED_NETHER_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  844 */     add(Blocks.POLISHED_ANDESITE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  845 */     add(Blocks.DIORITE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  846 */     add(Blocks.CRIMSON_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  847 */     add(Blocks.WARPED_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  848 */     add(Blocks.BLACKSTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  849 */     add(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  850 */     add(Blocks.POLISHED_BLACKSTONE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  851 */     add(Blocks.OXIDIZED_CUT_COPPER_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  852 */     add(Blocks.WEATHERED_CUT_COPPER_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  853 */     add(Blocks.EXPOSED_CUT_COPPER_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  854 */     add(Blocks.CUT_COPPER_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  855 */     add(Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  856 */     add(Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  857 */     add(Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  858 */     add(Blocks.WAXED_CUT_COPPER_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  859 */     add(Blocks.COBBLED_DEEPSLATE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  860 */     add(Blocks.POLISHED_DEEPSLATE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  861 */     add(Blocks.DEEPSLATE_TILE_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  862 */     add(Blocks.DEEPSLATE_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  863 */     add(Blocks.MUD_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*  864 */     add(Blocks.RESIN_BRICK_SLAB, x$0 -> rec$.createSlabItemTable(x$0));
/*      */ 
/*      */     
/*  867 */     add(Blocks.OAK_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  868 */     add(Blocks.SPRUCE_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  869 */     add(Blocks.BIRCH_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  870 */     add(Blocks.JUNGLE_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  871 */     add(Blocks.ACACIA_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  872 */     add(Blocks.DARK_OAK_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  873 */     add(Blocks.PALE_OAK_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  874 */     add(Blocks.MANGROVE_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  875 */     add(Blocks.CHERRY_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  876 */     add(Blocks.BAMBOO_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  877 */     add(Blocks.WARPED_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  878 */     add(Blocks.CRIMSON_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  879 */     add(Blocks.IRON_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  880 */     add(Blocks.COPPER_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  881 */     add(Blocks.EXPOSED_COPPER_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  882 */     add(Blocks.WEATHERED_COPPER_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  883 */     add(Blocks.OXIDIZED_COPPER_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  884 */     add(Blocks.WAXED_COPPER_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  885 */     add(Blocks.WAXED_EXPOSED_COPPER_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  886 */     add(Blocks.WAXED_WEATHERED_COPPER_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*  887 */     add(Blocks.WAXED_OXIDIZED_COPPER_DOOR, x$0 -> rec$.createDoorTable(x$0));
/*      */ 
/*      */     
/*  890 */     add(Blocks.BLACK_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  891 */     add(Blocks.BLUE_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  892 */     add(Blocks.BROWN_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  893 */     add(Blocks.CYAN_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  894 */     add(Blocks.GRAY_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  895 */     add(Blocks.GREEN_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  896 */     add(Blocks.LIGHT_BLUE_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  897 */     add(Blocks.LIGHT_GRAY_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  898 */     add(Blocks.LIME_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  899 */     add(Blocks.MAGENTA_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  900 */     add(Blocks.PURPLE_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  901 */     add(Blocks.ORANGE_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  902 */     add(Blocks.PINK_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  903 */     add(Blocks.RED_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  904 */     add(Blocks.WHITE_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*  905 */     add(Blocks.YELLOW_BED, block -> createSinglePropConditionTable(block, BedBlock.PART, BedPart.HEAD));
/*      */ 
/*      */     
/*  908 */     add(Blocks.LILAC, block -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*  909 */     add(Blocks.SUNFLOWER, block -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*  910 */     add(Blocks.PEONY, block -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*  911 */     add(Blocks.ROSE_BUSH, block -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*      */ 
/*      */     
/*  914 */     add(Blocks.TNT, LootTable.lootTable()
/*  915 */         .withPool((LootPool.Builder)applyExplosionCondition(Blocks.TNT, LootPool.lootPool()
/*  916 */             .setRolls(ConstantValue.exactly(1.0F))
/*  917 */             .add(LootItem.lootTableItem(Blocks.TNT)
/*  918 */               .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TNT).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TntBlock.UNSTABLE, false)))))));
/*      */ 
/*      */ 
/*      */     
/*  922 */     add(Blocks.COCOA, block -> LootTable.lootTable()
/*  923 */         .withPool(LootPool.lootPool()
/*  924 */           .setRolls(ConstantValue.exactly(1.0F))
/*  925 */           .add((LootPoolEntryContainer.Builder)applyExplosionDecay(block, LootItem.lootTableItem(Items.COCOA_BEANS)
/*  926 */               .apply(
/*  927 */                 SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CocoaBlock.AGE, 2))))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  932 */     add(Blocks.SEA_PICKLE, block -> LootTable.lootTable()
/*  933 */         .withPool(LootPool.lootPool()
/*  934 */           .setRolls(ConstantValue.exactly(1.0F))
/*  935 */           .add((LootPoolEntryContainer.Builder)applyExplosionDecay(Blocks.SEA_PICKLE, LootItem.lootTableItem(block)
/*  936 */               .apply(List.of(Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)), ())))));
/*      */ 
/*      */ 
/*      */     
/*  940 */     add(Blocks.COMPOSTER, block -> LootTable.lootTable()
/*  941 */         .withPool(LootPool.lootPool()
/*  942 */           .add((LootPoolEntryContainer.Builder)applyExplosionDecay(block, LootItem.lootTableItem(Items.COMPOSTER))))
/*      */         
/*  944 */         .withPool(LootPool.lootPool()
/*  945 */           .add(LootItem.lootTableItem(Items.BONE_MEAL)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ComposterBlock.LEVEL, 8)))));
/*      */ 
/*      */ 
/*      */     
/*  949 */     add(Blocks.CAVE_VINES, x$0 -> rec$.createCaveVinesDrop(x$0));
/*  950 */     add(Blocks.CAVE_VINES_PLANT, x$0 -> rec$.createCaveVinesDrop(x$0));
/*      */     
/*  952 */     add(Blocks.CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  953 */     add(Blocks.WHITE_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  954 */     add(Blocks.ORANGE_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  955 */     add(Blocks.MAGENTA_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  956 */     add(Blocks.LIGHT_BLUE_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  957 */     add(Blocks.YELLOW_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  958 */     add(Blocks.LIME_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  959 */     add(Blocks.PINK_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  960 */     add(Blocks.GRAY_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  961 */     add(Blocks.LIGHT_GRAY_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  962 */     add(Blocks.CYAN_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  963 */     add(Blocks.PURPLE_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  964 */     add(Blocks.BLUE_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  965 */     add(Blocks.BROWN_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  966 */     add(Blocks.GREEN_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  967 */     add(Blocks.RED_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*  968 */     add(Blocks.BLACK_CANDLE, x$0 -> rec$.createCandleDrops(x$0));
/*      */ 
/*      */     
/*  971 */     add(Blocks.BEACON, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  972 */     add(Blocks.BREWING_STAND, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  973 */     add(Blocks.CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  974 */     add(Blocks.COPPER_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  975 */     add(Blocks.EXPOSED_COPPER_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  976 */     add(Blocks.WEATHERED_COPPER_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  977 */     add(Blocks.OXIDIZED_COPPER_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  978 */     add(Blocks.WAXED_COPPER_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  979 */     add(Blocks.WAXED_EXPOSED_COPPER_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  980 */     add(Blocks.WAXED_WEATHERED_COPPER_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  981 */     add(Blocks.WAXED_OXIDIZED_COPPER_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  982 */     add(Blocks.DISPENSER, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  983 */     add(Blocks.DROPPER, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  984 */     add(Blocks.ENCHANTING_TABLE, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  985 */     add(Blocks.FURNACE, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  986 */     add(Blocks.HOPPER, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  987 */     add(Blocks.TRAPPED_CHEST, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  988 */     add(Blocks.SMOKER, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  989 */     add(Blocks.BLAST_FURNACE, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*      */     
/*  991 */     add(Blocks.BARREL, x$0 -> rec$.createNameableBlockEntityTable(x$0));
/*  992 */     dropSelf(Blocks.CARTOGRAPHY_TABLE);
/*  993 */     dropSelf(Blocks.FLETCHING_TABLE);
/*  994 */     dropSelf(Blocks.GRINDSTONE);
/*  995 */     dropSelf(Blocks.LECTERN);
/*  996 */     dropSelf(Blocks.SMITHING_TABLE);
/*  997 */     dropSelf(Blocks.STONECUTTER);
/*      */     
/*  999 */     dropSelf(Blocks.ACACIA_SHELF);
/* 1000 */     dropSelf(Blocks.BAMBOO_SHELF);
/* 1001 */     dropSelf(Blocks.BIRCH_SHELF);
/* 1002 */     dropSelf(Blocks.CHERRY_SHELF);
/* 1003 */     dropSelf(Blocks.CRIMSON_SHELF);
/* 1004 */     dropSelf(Blocks.DARK_OAK_SHELF);
/* 1005 */     dropSelf(Blocks.JUNGLE_SHELF);
/* 1006 */     dropSelf(Blocks.MANGROVE_SHELF);
/* 1007 */     dropSelf(Blocks.OAK_SHELF);
/* 1008 */     dropSelf(Blocks.PALE_OAK_SHELF);
/* 1009 */     dropSelf(Blocks.SPRUCE_SHELF);
/* 1010 */     dropSelf(Blocks.WARPED_SHELF);
/*      */     
/* 1012 */     add(Blocks.BELL, this::createSingleItemTable);
/* 1013 */     add(Blocks.LANTERN, this::createSingleItemTable);
/* 1014 */     add(Blocks.SOUL_LANTERN, this::createSingleItemTable);
/* 1015 */     Blocks.COPPER_LANTERN.forEach(block -> add(block, this::createSingleItemTable));
/*      */ 
/*      */     
/* 1018 */     add(Blocks.SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1019 */     add(Blocks.BLACK_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1020 */     add(Blocks.BLUE_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1021 */     add(Blocks.BROWN_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1022 */     add(Blocks.CYAN_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1023 */     add(Blocks.GRAY_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1024 */     add(Blocks.GREEN_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1025 */     add(Blocks.LIGHT_BLUE_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1026 */     add(Blocks.LIGHT_GRAY_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1027 */     add(Blocks.LIME_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1028 */     add(Blocks.MAGENTA_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1029 */     add(Blocks.ORANGE_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1030 */     add(Blocks.PINK_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1031 */     add(Blocks.PURPLE_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1032 */     add(Blocks.RED_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1033 */     add(Blocks.WHITE_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/* 1034 */     add(Blocks.YELLOW_SHULKER_BOX, x$0 -> rec$.createShulkerBoxDrop(x$0));
/*      */ 
/*      */     
/* 1037 */     add(Blocks.BLACK_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1038 */     add(Blocks.BLUE_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1039 */     add(Blocks.BROWN_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1040 */     add(Blocks.CYAN_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1041 */     add(Blocks.GRAY_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1042 */     add(Blocks.GREEN_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1043 */     add(Blocks.LIGHT_BLUE_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1044 */     add(Blocks.LIGHT_GRAY_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1045 */     add(Blocks.LIME_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1046 */     add(Blocks.MAGENTA_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1047 */     add(Blocks.ORANGE_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1048 */     add(Blocks.PINK_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1049 */     add(Blocks.PURPLE_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1050 */     add(Blocks.RED_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1051 */     add(Blocks.WHITE_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/* 1052 */     add(Blocks.YELLOW_BANNER, x$0 -> rec$.createBannerDrop(x$0));
/*      */     
/* 1054 */     add(Blocks.PLAYER_HEAD, block -> LootTable.lootTable()
/* 1055 */         .withPool((LootPool.Builder)applyExplosionCondition(block, LootPool.lootPool()
/* 1056 */             .setRolls(ConstantValue.exactly(1.0F))
/* 1057 */             .add(LootItem.lootTableItem(block)
/* 1058 */               .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 1059 */                 .include(DataComponents.PROFILE)
/* 1060 */                 .include(DataComponents.NOTE_BLOCK_SOUND)
/* 1061 */                 .include(DataComponents.CUSTOM_NAME))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1066 */     add(Blocks.SKELETON_SKULL, this::createMobSkullDrop);
/* 1067 */     add(Blocks.WITHER_SKELETON_SKULL, this::createMobSkullDrop);
/* 1068 */     add(Blocks.ZOMBIE_HEAD, this::createMobSkullDrop);
/* 1069 */     add(Blocks.CREEPER_HEAD, this::createMobSkullDrop);
/* 1070 */     add(Blocks.PIGLIN_HEAD, this::createMobSkullDrop);
/* 1071 */     add(Blocks.DRAGON_HEAD, this::createMobSkullDrop);
/*      */     
/* 1073 */     add(Blocks.BEE_NEST, x$0 -> rec$.createBeeNestDrop(x$0));
/* 1074 */     add(Blocks.BEEHIVE, x$0 -> rec$.createBeeHiveDrop(x$0));
/*      */ 
/*      */     
/* 1077 */     add(Blocks.OAK_LEAVES, block -> createOakLeavesDrops(block, Blocks.OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1078 */     add(Blocks.SPRUCE_LEAVES, block -> createLeavesDrops(block, Blocks.SPRUCE_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1079 */     add(Blocks.BIRCH_LEAVES, block -> createLeavesDrops(block, Blocks.BIRCH_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1080 */     add(Blocks.JUNGLE_LEAVES, block -> createLeavesDrops(block, Blocks.JUNGLE_SAPLING, JUNGLE_LEAVES_SAPLING_CHANGES));
/* 1081 */     add(Blocks.ACACIA_LEAVES, block -> createLeavesDrops(block, Blocks.ACACIA_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1082 */     add(Blocks.DARK_OAK_LEAVES, block -> createOakLeavesDrops(block, Blocks.DARK_OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1083 */     add(Blocks.PALE_OAK_LEAVES, block -> createLeavesDrops(block, Blocks.PALE_OAK_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1084 */     add(Blocks.CHERRY_LEAVES, block -> createLeavesDrops(block, Blocks.CHERRY_SAPLING, NORMAL_LEAVES_SAPLING_CHANCES));
/*      */     
/* 1086 */     add(Blocks.AZALEA_LEAVES, block -> createLeavesDrops(block, Blocks.AZALEA, NORMAL_LEAVES_SAPLING_CHANCES));
/* 1087 */     add(Blocks.FLOWERING_AZALEA_LEAVES, block -> createLeavesDrops(block, Blocks.FLOWERING_AZALEA, NORMAL_LEAVES_SAPLING_CHANCES));
/*      */ 
/*      */     
/* 1090 */     LootItemBlockStatePropertyCondition.Builder builder1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BEETROOTS).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BeetrootBlock.AGE, 3));
/* 1091 */     add(Blocks.BEETROOTS, createCropDrops(Blocks.BEETROOTS, Items.BEETROOT, Items.BEETROOT_SEEDS, builder1));
/*      */     
/* 1093 */     LootItemBlockStatePropertyCondition.Builder builder2 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7));
/* 1094 */     add(Blocks.WHEAT, createCropDrops(Blocks.WHEAT, Items.WHEAT, Items.WHEAT_SEEDS, builder2));
/*      */     
/* 1096 */     LootItemBlockStatePropertyCondition.Builder builder3 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CARROTS).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CarrotBlock.AGE, 7));
/*      */     
/* 1098 */     LootItemBlockStatePropertyCondition.Builder builder4 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.MANGROVE_PROPAGULE).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MangrovePropaguleBlock.AGE, 4));
/* 1099 */     add(Blocks.MANGROVE_PROPAGULE, (LootTable.Builder)applyExplosionDecay(Blocks.MANGROVE_PROPAGULE, LootTable.lootTable()
/* 1100 */           .withPool(LootPool.lootPool()
/* 1101 */             .when(builder4)
/* 1102 */             .add(LootItem.lootTableItem(Items.MANGROVE_PROPAGULE)))));
/*      */ 
/*      */ 
/*      */     
/* 1106 */     add(Blocks.TORCHFLOWER_CROP, (LootTable.Builder)applyExplosionDecay(Blocks.TORCHFLOWER_CROP, LootTable.lootTable()
/* 1107 */           .withPool(LootPool.lootPool()
/* 1108 */             .add(LootItem.lootTableItem(Items.TORCHFLOWER_SEEDS)))));
/*      */ 
/*      */     
/* 1111 */     dropSelf(Blocks.SNIFFER_EGG);
/* 1112 */     dropSelf(Blocks.DRIED_GHAST);
/* 1113 */     add(Blocks.PITCHER_CROP, block -> createPitcherCropLoot());
/*      */     
/* 1115 */     dropSelf(Blocks.PITCHER_PLANT);
/* 1116 */     add(Blocks.PITCHER_PLANT, (LootTable.Builder)applyExplosionDecay(Blocks.PITCHER_PLANT, LootTable.lootTable()
/* 1117 */           .withPool(LootPool.lootPool()
/* 1118 */             .add(LootItem.lootTableItem(Items.PITCHER_PLANT)
/*      */               
/* 1120 */               .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.PITCHER_PLANT).setProperties(StatePropertiesPredicate.Builder.properties()
/* 1121 */                   .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1126 */     add(Blocks.CARROTS, (LootTable.Builder)applyExplosionDecay(Blocks.CARROTS, LootTable.lootTable()
/* 1127 */           .withPool(LootPool.lootPool()
/* 1128 */             .add(LootItem.lootTableItem(Items.CARROT)))
/*      */           
/* 1130 */           .withPool(LootPool.lootPool()
/* 1131 */             .when(builder3)
/* 1132 */             .add(LootItem.lootTableItem(Items.CARROT).apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))))));
/*      */ 
/*      */ 
/*      */     
/* 1136 */     LootItemBlockStatePropertyCondition.Builder builder5 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.POTATOES).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PotatoBlock.AGE, 7));
/* 1137 */     add(Blocks.POTATOES, (LootTable.Builder)applyExplosionDecay(Blocks.POTATOES, LootTable.lootTable()
/* 1138 */           .withPool(LootPool.lootPool()
/* 1139 */             .add(LootItem.lootTableItem(Items.POTATO)))
/*      */           
/* 1141 */           .withPool(LootPool.lootPool()
/* 1142 */             .when(builder5)
/* 1143 */             .add(LootItem.lootTableItem(Items.POTATO).apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))))
/*      */           
/* 1145 */           .withPool(LootPool.lootPool()
/* 1146 */             .when(builder5)
/* 1147 */             .add(LootItem.lootTableItem(Items.POISONOUS_POTATO).when(LootItemRandomChanceCondition.randomChance(0.02F))))));
/*      */ 
/*      */ 
/*      */     
/* 1151 */     add(Blocks.SWEET_BERRY_BUSH, block -> (LootTable.Builder)applyExplosionDecay(block, LootTable.lootTable()
/* 1152 */           .withPool(
/* 1153 */             LootPool.lootPool()
/* 1154 */             .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SWEET_BERRY_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 3)))
/* 1155 */             .add(LootItem.lootTableItem(Items.SWEET_BERRIES))
/* 1156 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
/* 1157 */             .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE))))
/*      */           
/* 1159 */           .withPool(
/* 1160 */             LootPool.lootPool()
/* 1161 */             .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SWEET_BERRY_BUSH).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SweetBerryBushBlock.AGE, 2)))
/* 1162 */             .add(LootItem.lootTableItem(Items.SWEET_BERRIES))
/* 1163 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
/* 1164 */             .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1169 */     add(Blocks.BROWN_MUSHROOM_BLOCK, block -> createMushroomBlockDrop(block, Blocks.BROWN_MUSHROOM));
/* 1170 */     add(Blocks.RED_MUSHROOM_BLOCK, block -> createMushroomBlockDrop(block, Blocks.RED_MUSHROOM));
/*      */ 
/*      */     
/* 1173 */     add(Blocks.COAL_ORE, block -> createOreDrop(block, Items.COAL));
/* 1174 */     add(Blocks.DEEPSLATE_COAL_ORE, block -> createOreDrop(block, Items.COAL));
/* 1175 */     add(Blocks.EMERALD_ORE, block -> createOreDrop(block, Items.EMERALD));
/* 1176 */     add(Blocks.DEEPSLATE_EMERALD_ORE, block -> createOreDrop(block, Items.EMERALD));
/* 1177 */     add(Blocks.NETHER_QUARTZ_ORE, block -> createOreDrop(block, Items.QUARTZ));
/* 1178 */     add(Blocks.DIAMOND_ORE, block -> createOreDrop(block, Items.DIAMOND));
/* 1179 */     add(Blocks.DEEPSLATE_DIAMOND_ORE, block -> createOreDrop(block, Items.DIAMOND));
/* 1180 */     add(Blocks.COPPER_ORE, x$0 -> rec$.createCopperOreDrops(x$0));
/* 1181 */     add(Blocks.DEEPSLATE_COPPER_ORE, x$0 -> rec$.createCopperOreDrops(x$0));
/* 1182 */     add(Blocks.IRON_ORE, block -> createOreDrop(block, Items.RAW_IRON));
/* 1183 */     add(Blocks.DEEPSLATE_IRON_ORE, block -> createOreDrop(block, Items.RAW_IRON));
/* 1184 */     add(Blocks.GOLD_ORE, block -> createOreDrop(block, Items.RAW_GOLD));
/* 1185 */     add(Blocks.DEEPSLATE_GOLD_ORE, block -> createOreDrop(block, Items.RAW_GOLD));
/*      */     
/* 1187 */     add(Blocks.NETHER_GOLD_ORE, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1188 */           applyExplosionDecay(block, LootItem.lootTableItem(Items.GOLD_NUGGET)
/* 1189 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F)))
/* 1190 */             .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE))))));
/*      */ 
/*      */ 
/*      */     
/* 1194 */     add(Blocks.LAPIS_ORE, x$0 -> rec$.createLapisOreDrops(x$0));
/* 1195 */     add(Blocks.DEEPSLATE_LAPIS_ORE, x$0 -> rec$.createLapisOreDrops(x$0));
/*      */ 
/*      */     
/* 1198 */     add(Blocks.COBWEB, block -> createSilkTouchOrShearsDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1199 */           applyExplosionCondition(block, LootItem.lootTableItem(Items.STRING))));
/*      */ 
/*      */     
/* 1202 */     add(Blocks.DEAD_BUSH, block -> createShearsDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1203 */           applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))))));
/*      */     
/* 1205 */     add(Blocks.SHORT_DRY_GRASS, x$0 -> rec$.createShearsOrSilkTouchOnlyDrop(x$0));
/* 1206 */     add(Blocks.TALL_DRY_GRASS, x$0 -> rec$.createShearsOrSilkTouchOnlyDrop(x$0));
/* 1207 */     add(Blocks.BUSH, x$0 -> rec$.createShearsOrSilkTouchOnlyDrop(x$0));
/* 1208 */     add(Blocks.NETHER_SPROUTS, x$0 -> rec$.createShearsOnlyDrop(x$0));
/* 1209 */     add(Blocks.SEAGRASS, x$0 -> rec$.createShearsOnlyDrop(x$0));
/* 1210 */     add(Blocks.VINE, x$0 -> rec$.createShearsOnlyDrop(x$0));
/* 1211 */     add(Blocks.GLOW_LICHEN, block -> createMultifaceBlockDrops(block, hasShears()));
/* 1212 */     add(Blocks.RESIN_CLUMP, x$0 -> rec$.createMultifaceBlockDrops(x$0));
/* 1213 */     add(Blocks.HANGING_ROOTS, x$0 -> rec$.createShearsOnlyDrop(x$0));
/* 1214 */     add(Blocks.SMALL_DRIPLEAF, x$0 -> rec$.createShearsOnlyDrop(x$0));
/*      */ 
/*      */     
/* 1217 */     add(Blocks.MANGROVE_LEAVES, x$0 -> rec$.createMangroveLeavesDrops(x$0));
/*      */     
/* 1219 */     add(Blocks.TALL_SEAGRASS, createDoublePlantShearsDrop(Blocks.SEAGRASS));
/* 1220 */     add(Blocks.LARGE_FERN, block -> createDoublePlantWithSeedDrops(block, Blocks.FERN));
/* 1221 */     add(Blocks.TALL_GRASS, block -> createDoublePlantWithSeedDrops(block, Blocks.SHORT_GRASS));
/*      */ 
/*      */     
/* 1224 */     add(Blocks.MELON_STEM, block -> createStemDrops(block, Items.MELON_SEEDS));
/* 1225 */     add(Blocks.ATTACHED_MELON_STEM, block -> createAttachedStemDrops(block, Items.MELON_SEEDS));
/* 1226 */     add(Blocks.PUMPKIN_STEM, block -> createStemDrops(block, Items.PUMPKIN_SEEDS));
/* 1227 */     add(Blocks.ATTACHED_PUMPKIN_STEM, block -> createAttachedStemDrops(block, Items.PUMPKIN_SEEDS));
/*      */ 
/*      */     
/* 1230 */     add(Blocks.CHORUS_FLOWER, block -> LootTable.lootTable()
/* 1231 */         .withPool(LootPool.lootPool()
/* 1232 */           .setRolls(ConstantValue.exactly(1.0F))
/* 1233 */           .add(((LootPoolSingletonContainer.Builder)applyExplosionCondition(block, LootItem.lootTableItem(block))).when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)))));
/*      */ 
/*      */ 
/*      */     
/* 1237 */     add(Blocks.FERN, x$0 -> rec$.createGrassDrops(x$0));
/* 1238 */     add(Blocks.SHORT_GRASS, x$0 -> rec$.createGrassDrops(x$0));
/*      */     
/* 1240 */     add(Blocks.GLOWSTONE, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1241 */           applyExplosionDecay(block, LootItem.lootTableItem(Items.GLOWSTONE_DUST)
/* 1242 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
/* 1243 */             .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
/* 1244 */             .apply(LimitCount.limitCount(IntRange.range(1, 4))))));
/*      */ 
/*      */ 
/*      */     
/* 1248 */     add(Blocks.MELON, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1249 */           applyExplosionDecay(block, LootItem.lootTableItem(Items.MELON_SLICE)
/* 1250 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F)))
/* 1251 */             .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
/* 1252 */             .apply(LimitCount.limitCount(IntRange.upperBound(9))))));
/*      */ 
/*      */ 
/*      */     
/* 1256 */     add(Blocks.REDSTONE_ORE, x$0 -> rec$.createRedstoneOreDrops(x$0));
/* 1257 */     add(Blocks.DEEPSLATE_REDSTONE_ORE, x$0 -> rec$.createRedstoneOreDrops(x$0));
/*      */     
/* 1259 */     add(Blocks.SEA_LANTERN, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1260 */           applyExplosionDecay(block, LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS)
/* 1261 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
/* 1262 */             .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
/* 1263 */             .apply(LimitCount.limitCount(IntRange.range(1, 5))))));
/*      */ 
/*      */ 
/*      */     
/* 1267 */     add(Blocks.CREAKING_HEART, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1268 */           applyExplosionDecay(block, LootItem.lootTableItem(Items.RESIN_CLUMP)
/* 1269 */             .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
/* 1270 */             .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
/* 1271 */             .apply(LimitCount.limitCount(IntRange.upperBound(9))))));
/*      */ 
/*      */ 
/*      */     
/* 1275 */     add(Blocks.NETHER_WART, block -> LootTable.lootTable()
/* 1276 */         .withPool((LootPool.Builder)applyExplosionDecay(block, LootPool.lootPool()
/* 1277 */             .setRolls(ConstantValue.exactly(1.0F))
/* 1278 */             .add(LootItem.lootTableItem(Items.NETHER_WART)
/* 1279 */               .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))
/* 1280 */                 .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(NetherWartBlock.AGE, 3))))
/*      */               
/* 1282 */               .apply(ApplyBonusCount.addUniformBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE))
/* 1283 */                 .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(NetherWartBlock.AGE, 3))))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1288 */     add(Blocks.SNOW, block -> LootTable.lootTable()
/* 1289 */         .withPool(LootPool.lootPool()
/* 1290 */           .when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS))
/* 1291 */           .add(
/* 1292 */             AlternativesEntry.alternatives(new LootPoolEntryContainer.Builder[] {
/* 1293 */                 AlternativesEntry.alternatives(SnowLayerBlock.LAYERS.getPossibleValues(), ())
/*      */                 
/* 1295 */                 .when(doesNotHaveSilkTouch()), 
/* 1296 */                 AlternativesEntry.alternatives(SnowLayerBlock.LAYERS.getPossibleValues(), ())
/*      */               }))));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1305 */     add(Blocks.GRAVEL, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1306 */           applyExplosionCondition(block, ((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(Items.FLINT)
/* 1307 */             .when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), new float[] { 0.1F, 0.14285715F, 0.25F, 1.0F
/* 1308 */                 }))).otherwise(LootItem.lootTableItem(block)))));
/*      */ 
/*      */ 
/*      */     
/* 1312 */     add(Blocks.CAMPFIRE, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1313 */           applyExplosionCondition(block, LootItem.lootTableItem(Items.CHARCOAL).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))));
/*      */ 
/*      */     
/* 1316 */     add(Blocks.GILDED_BLACKSTONE, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1317 */           applyExplosionCondition(block, ((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
/* 1318 */             .when(BonusLevelTableCondition.bonusLevelFlatChance(enchantments.getOrThrow(Enchantments.FORTUNE), new float[] { 0.1F, 0.14285715F, 0.25F, 1.0F
/* 1319 */                 }))).otherwise(LootItem.lootTableItem(block)))));
/*      */ 
/*      */ 
/*      */     
/* 1323 */     add(Blocks.SOUL_CAMPFIRE, block -> createSilkTouchDispatchTable(block, (LootPoolEntryContainer.Builder)
/* 1324 */           applyExplosionCondition(block, LootItem.lootTableItem(Items.SOUL_SOIL).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));
/*      */ 
/*      */     
/* 1327 */     add(Blocks.AMETHYST_CLUSTER, block -> createSilkTouchDispatchTable(block, (
/* 1328 */           (LootPoolSingletonContainer.Builder)LootItem.lootTableItem(Items.AMETHYST_SHARD)
/* 1329 */           .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
/* 1330 */           .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))
/* 1331 */           .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.CLUSTER_MAX_HARVESTABLES))))
/* 1332 */           .otherwise((LootPoolEntryContainer.Builder)
/* 1333 */             applyExplosionDecay(block, LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1338 */     dropWhenSilkTouch(Blocks.SMALL_AMETHYST_BUD);
/* 1339 */     dropWhenSilkTouch(Blocks.MEDIUM_AMETHYST_BUD);
/* 1340 */     dropWhenSilkTouch(Blocks.LARGE_AMETHYST_BUD);
/*      */     
/* 1342 */     dropWhenSilkTouch(Blocks.GLASS);
/* 1343 */     dropWhenSilkTouch(Blocks.WHITE_STAINED_GLASS);
/* 1344 */     dropWhenSilkTouch(Blocks.ORANGE_STAINED_GLASS);
/* 1345 */     dropWhenSilkTouch(Blocks.MAGENTA_STAINED_GLASS);
/* 1346 */     dropWhenSilkTouch(Blocks.LIGHT_BLUE_STAINED_GLASS);
/* 1347 */     dropWhenSilkTouch(Blocks.YELLOW_STAINED_GLASS);
/* 1348 */     dropWhenSilkTouch(Blocks.LIME_STAINED_GLASS);
/* 1349 */     dropWhenSilkTouch(Blocks.PINK_STAINED_GLASS);
/* 1350 */     dropWhenSilkTouch(Blocks.GRAY_STAINED_GLASS);
/* 1351 */     dropWhenSilkTouch(Blocks.LIGHT_GRAY_STAINED_GLASS);
/* 1352 */     dropWhenSilkTouch(Blocks.CYAN_STAINED_GLASS);
/* 1353 */     dropWhenSilkTouch(Blocks.PURPLE_STAINED_GLASS);
/* 1354 */     dropWhenSilkTouch(Blocks.BLUE_STAINED_GLASS);
/* 1355 */     dropWhenSilkTouch(Blocks.BROWN_STAINED_GLASS);
/* 1356 */     dropWhenSilkTouch(Blocks.GREEN_STAINED_GLASS);
/* 1357 */     dropWhenSilkTouch(Blocks.RED_STAINED_GLASS);
/* 1358 */     dropWhenSilkTouch(Blocks.BLACK_STAINED_GLASS);
/*      */     
/* 1360 */     dropWhenSilkTouch(Blocks.GLASS_PANE);
/* 1361 */     dropWhenSilkTouch(Blocks.WHITE_STAINED_GLASS_PANE);
/* 1362 */     dropWhenSilkTouch(Blocks.ORANGE_STAINED_GLASS_PANE);
/* 1363 */     dropWhenSilkTouch(Blocks.MAGENTA_STAINED_GLASS_PANE);
/* 1364 */     dropWhenSilkTouch(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
/* 1365 */     dropWhenSilkTouch(Blocks.YELLOW_STAINED_GLASS_PANE);
/* 1366 */     dropWhenSilkTouch(Blocks.LIME_STAINED_GLASS_PANE);
/* 1367 */     dropWhenSilkTouch(Blocks.PINK_STAINED_GLASS_PANE);
/* 1368 */     dropWhenSilkTouch(Blocks.GRAY_STAINED_GLASS_PANE);
/* 1369 */     dropWhenSilkTouch(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
/* 1370 */     dropWhenSilkTouch(Blocks.CYAN_STAINED_GLASS_PANE);
/* 1371 */     dropWhenSilkTouch(Blocks.PURPLE_STAINED_GLASS_PANE);
/* 1372 */     dropWhenSilkTouch(Blocks.BLUE_STAINED_GLASS_PANE);
/* 1373 */     dropWhenSilkTouch(Blocks.BROWN_STAINED_GLASS_PANE);
/* 1374 */     dropWhenSilkTouch(Blocks.GREEN_STAINED_GLASS_PANE);
/* 1375 */     dropWhenSilkTouch(Blocks.RED_STAINED_GLASS_PANE);
/* 1376 */     dropWhenSilkTouch(Blocks.BLACK_STAINED_GLASS_PANE);
/*      */     
/* 1378 */     dropWhenSilkTouch(Blocks.ICE);
/* 1379 */     dropWhenSilkTouch(Blocks.PACKED_ICE);
/* 1380 */     dropWhenSilkTouch(Blocks.BLUE_ICE);
/*      */     
/* 1382 */     dropWhenSilkTouch(Blocks.TURTLE_EGG);
/*      */     
/* 1384 */     dropWhenSilkTouch(Blocks.MUSHROOM_STEM);
/*      */     
/* 1386 */     dropWhenSilkTouch(Blocks.DEAD_TUBE_CORAL);
/* 1387 */     dropWhenSilkTouch(Blocks.DEAD_BRAIN_CORAL);
/* 1388 */     dropWhenSilkTouch(Blocks.DEAD_BUBBLE_CORAL);
/* 1389 */     dropWhenSilkTouch(Blocks.DEAD_FIRE_CORAL);
/* 1390 */     dropWhenSilkTouch(Blocks.DEAD_HORN_CORAL);
/*      */     
/* 1392 */     dropWhenSilkTouch(Blocks.TUBE_CORAL);
/* 1393 */     dropWhenSilkTouch(Blocks.BRAIN_CORAL);
/* 1394 */     dropWhenSilkTouch(Blocks.BUBBLE_CORAL);
/* 1395 */     dropWhenSilkTouch(Blocks.FIRE_CORAL);
/* 1396 */     dropWhenSilkTouch(Blocks.HORN_CORAL);
/*      */     
/* 1398 */     dropWhenSilkTouch(Blocks.DEAD_TUBE_CORAL_FAN);
/* 1399 */     dropWhenSilkTouch(Blocks.DEAD_BRAIN_CORAL_FAN);
/* 1400 */     dropWhenSilkTouch(Blocks.DEAD_BUBBLE_CORAL_FAN);
/* 1401 */     dropWhenSilkTouch(Blocks.DEAD_FIRE_CORAL_FAN);
/* 1402 */     dropWhenSilkTouch(Blocks.DEAD_HORN_CORAL_FAN);
/*      */     
/* 1404 */     dropWhenSilkTouch(Blocks.TUBE_CORAL_FAN);
/* 1405 */     dropWhenSilkTouch(Blocks.BRAIN_CORAL_FAN);
/* 1406 */     dropWhenSilkTouch(Blocks.BUBBLE_CORAL_FAN);
/* 1407 */     dropWhenSilkTouch(Blocks.FIRE_CORAL_FAN);
/* 1408 */     dropWhenSilkTouch(Blocks.HORN_CORAL_FAN);
/*      */     
/* 1410 */     otherWhenSilkTouch(Blocks.INFESTED_STONE, Blocks.STONE);
/* 1411 */     otherWhenSilkTouch(Blocks.INFESTED_COBBLESTONE, Blocks.COBBLESTONE);
/* 1412 */     otherWhenSilkTouch(Blocks.INFESTED_STONE_BRICKS, Blocks.STONE_BRICKS);
/* 1413 */     otherWhenSilkTouch(Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS);
/* 1414 */     otherWhenSilkTouch(Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);
/* 1415 */     otherWhenSilkTouch(Blocks.INFESTED_CHISELED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS);
/* 1416 */     otherWhenSilkTouch(Blocks.INFESTED_DEEPSLATE, Blocks.DEEPSLATE);
/*      */     
/* 1418 */     addNetherVinesDropTable(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
/* 1419 */     addNetherVinesDropTable(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT);
/*      */ 
/*      */     
/* 1422 */     add(Blocks.CAKE, noDrop());
/* 1423 */     add(Blocks.CANDLE_CAKE, createCandleCakeDrops(Blocks.CANDLE));
/* 1424 */     add(Blocks.WHITE_CANDLE_CAKE, createCandleCakeDrops(Blocks.WHITE_CANDLE));
/* 1425 */     add(Blocks.ORANGE_CANDLE_CAKE, createCandleCakeDrops(Blocks.ORANGE_CANDLE));
/* 1426 */     add(Blocks.MAGENTA_CANDLE_CAKE, createCandleCakeDrops(Blocks.MAGENTA_CANDLE));
/* 1427 */     add(Blocks.LIGHT_BLUE_CANDLE_CAKE, createCandleCakeDrops(Blocks.LIGHT_BLUE_CANDLE));
/* 1428 */     add(Blocks.YELLOW_CANDLE_CAKE, createCandleCakeDrops(Blocks.YELLOW_CANDLE));
/* 1429 */     add(Blocks.LIME_CANDLE_CAKE, createCandleCakeDrops(Blocks.LIME_CANDLE));
/* 1430 */     add(Blocks.PINK_CANDLE_CAKE, createCandleCakeDrops(Blocks.PINK_CANDLE));
/* 1431 */     add(Blocks.GRAY_CANDLE_CAKE, createCandleCakeDrops(Blocks.GRAY_CANDLE));
/* 1432 */     add(Blocks.LIGHT_GRAY_CANDLE_CAKE, createCandleCakeDrops(Blocks.LIGHT_GRAY_CANDLE));
/* 1433 */     add(Blocks.CYAN_CANDLE_CAKE, createCandleCakeDrops(Blocks.CYAN_CANDLE));
/* 1434 */     add(Blocks.PURPLE_CANDLE_CAKE, createCandleCakeDrops(Blocks.PURPLE_CANDLE));
/* 1435 */     add(Blocks.BLUE_CANDLE_CAKE, createCandleCakeDrops(Blocks.BLUE_CANDLE));
/* 1436 */     add(Blocks.BROWN_CANDLE_CAKE, createCandleCakeDrops(Blocks.BROWN_CANDLE));
/* 1437 */     add(Blocks.GREEN_CANDLE_CAKE, createCandleCakeDrops(Blocks.GREEN_CANDLE));
/* 1438 */     add(Blocks.RED_CANDLE_CAKE, createCandleCakeDrops(Blocks.RED_CANDLE));
/* 1439 */     add(Blocks.BLACK_CANDLE_CAKE, createCandleCakeDrops(Blocks.BLACK_CANDLE));
/* 1440 */     add(Blocks.FROSTED_ICE, noDrop());
/* 1441 */     add(Blocks.SPAWNER, noDrop());
/* 1442 */     add(Blocks.TRIAL_SPAWNER, noDrop());
/* 1443 */     add(Blocks.VAULT, noDrop());
/* 1444 */     add(Blocks.FIRE, noDrop());
/* 1445 */     add(Blocks.SOUL_FIRE, noDrop());
/* 1446 */     add(Blocks.NETHER_PORTAL, noDrop());
/* 1447 */     add(Blocks.BUDDING_AMETHYST, noDrop());
/* 1448 */     add(Blocks.POWDER_SNOW, noDrop());
/* 1449 */     add(Blocks.FROGSPAWN, noDrop());
/* 1450 */     add(Blocks.REINFORCED_DEEPSLATE, noDrop());
/*      */     
/* 1452 */     add(Blocks.SUSPICIOUS_SAND, noDrop());
/* 1453 */     add(Blocks.SUSPICIOUS_GRAVEL, noDrop());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private LootTable.Builder createDecoratedPotTable(Block original) {
/* 1461 */     return LootTable.lootTable()
/* 1462 */       .withPool(LootPool.lootPool()
/* 1463 */         .setRolls(ConstantValue.exactly(1.0F))
/* 1464 */         .add(((LootPoolSingletonContainer.Builder)DynamicLoot.dynamicEntry(DecoratedPotBlock.SHERDS_DYNAMIC_DROP_ID)
/* 1465 */           .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(original).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DecoratedPotBlock.CRACKED, true))))
/* 1466 */           .otherwise(LootItem.lootTableItem(original)
/* 1467 */             .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 1468 */               .include(DataComponents.POT_DECORATIONS)))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private LootTable.Builder createPitcherCropLoot() {
/* 1476 */     return (LootTable.Builder)applyExplosionDecay(Blocks.PITCHER_CROP, 
/* 1477 */         LootTable.lootTable()
/* 1478 */         .withPool(LootPool.lootPool()
/* 1479 */           .add(AlternativesEntry.alternatives(PitcherCropBlock.AGE.getPossibleValues(), age -> {
/*      */ 
/*      */ 
/*      */                 
/* 1483 */                 LootItemBlockStatePropertyCondition.Builder isLowerPart = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.PITCHER_CROP).setProperties(StatePropertiesPredicate.Builder.properties()
/* 1484 */                     .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
/*      */ 
/*      */                 
/* 1487 */                 LootItemBlockStatePropertyCondition.Builder isAge = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.PITCHER_CROP).setProperties(StatePropertiesPredicate.Builder.properties()
/* 1488 */                     .hasProperty(PitcherCropBlock.AGE, age.intValue()));
/*      */                 
/* 1490 */                 if (age.intValue() == 4) {
/* 1491 */                   return ((LootPoolSingletonContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(Items.PITCHER_PLANT)
/* 1492 */                     .when(isAge))
/* 1493 */                     .when(isLowerPart))
/* 1494 */                     .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)));
/*      */                 }
/*      */                 
/* 1497 */                 return ((LootPoolSingletonContainer.Builder)((LootPoolSingletonContainer.Builder)LootItem.lootTableItem(Items.PITCHER_POD)
/* 1498 */                   .when(isAge))
/* 1499 */                   .when(isLowerPart))
/* 1500 */                   .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)));
/*      */               }))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private LootTable.Builder createMobSkullDrop(Block block) {
/* 1507 */     return LootTable.lootTable()
/* 1508 */       .withPool((LootPool.Builder)applyExplosionCondition(block, LootPool.lootPool()
/* 1509 */           .setRolls(ConstantValue.exactly(1.0F))
/* 1510 */           .add(LootItem.lootTableItem(block)
/* 1511 */             .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
/* 1512 */               .include(DataComponents.CUSTOM_NAME)))));
/*      */   }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\packs\VanillaBlockLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */