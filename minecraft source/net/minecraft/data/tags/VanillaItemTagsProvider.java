/*     */ package net.minecraft.data.tags;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ 
/*     */ public class VanillaItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {
/*  20 */   public VanillaItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) { super(output, Registries.ITEM, lookupProvider, e -> e.builtInRegistryHolder().key()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTags(HolderLookup.Provider registries) {
/*  25 */     (new BlockItemTagsProvider()
/*     */       {
/*     */         protected TagAppender<Block, Block> tag(TagKey<Block> blockTag, TagKey<Item> itemTag) {
/*  28 */           return new VanillaItemTagsProvider.BlockToItemConverter(VanillaItemTagsProvider.this.tag(itemTag));
/*     */         }
/*  30 */       }).run();
/*     */     
/*  32 */     tag(ItemTags.BANNERS).add(new Item[] { Items.WHITE_BANNER, Items.ORANGE_BANNER, Items.MAGENTA_BANNER, Items.LIGHT_BLUE_BANNER, Items.YELLOW_BANNER, Items.LIME_BANNER, Items.PINK_BANNER, Items.GRAY_BANNER, Items.LIGHT_GRAY_BANNER, Items.CYAN_BANNER, Items.PURPLE_BANNER, Items.BLUE_BANNER, Items.BROWN_BANNER, Items.GREEN_BANNER, Items.RED_BANNER, Items.BLACK_BANNER });
/*  33 */     tag(ItemTags.BOATS).add(new Item[] { Items.OAK_BOAT, Items.SPRUCE_BOAT, Items.BIRCH_BOAT, Items.JUNGLE_BOAT, Items.ACACIA_BOAT, Items.DARK_OAK_BOAT, Items.PALE_OAK_BOAT, Items.MANGROVE_BOAT, Items.BAMBOO_RAFT, Items.CHERRY_BOAT }).addTag(ItemTags.CHEST_BOATS);
/*  34 */     tag(ItemTags.BUNDLES).add(new Item[] { Items.BUNDLE, Items.BLACK_BUNDLE, Items.BLUE_BUNDLE, Items.BROWN_BUNDLE, Items.CYAN_BUNDLE, Items.GRAY_BUNDLE, Items.GREEN_BUNDLE, Items.LIGHT_BLUE_BUNDLE, Items.LIGHT_GRAY_BUNDLE, Items.LIME_BUNDLE, Items.MAGENTA_BUNDLE, Items.ORANGE_BUNDLE, Items.PINK_BUNDLE, Items.PURPLE_BUNDLE, Items.RED_BUNDLE, Items.YELLOW_BUNDLE, Items.WHITE_BUNDLE });
/*     */ 
/*     */     
/*  37 */     tag(ItemTags.CHEST_BOATS).add(new Item[] { Items.OAK_CHEST_BOAT, Items.SPRUCE_CHEST_BOAT, Items.BIRCH_CHEST_BOAT, Items.JUNGLE_CHEST_BOAT, Items.ACACIA_CHEST_BOAT, Items.DARK_OAK_CHEST_BOAT, Items.PALE_OAK_CHEST_BOAT, Items.MANGROVE_CHEST_BOAT, Items.BAMBOO_CHEST_RAFT, Items.CHERRY_CHEST_BOAT });
/*  38 */     tag(ItemTags.EGGS).add(new Item[] { Items.EGG, Items.BLUE_EGG, Items.BROWN_EGG });
/*  39 */     tag(ItemTags.FISHES).add(new Item[] { Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.PUFFERFISH, Items.TROPICAL_FISH });
/*  40 */     tag(ItemTags.CREEPER_DROP_MUSIC_DISCS).add(new Item[] { Items.MUSIC_DISC_13, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CHIRP, Items.MUSIC_DISC_FAR, Items.MUSIC_DISC_MALL, Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD, Items.MUSIC_DISC_WARD, Items.MUSIC_DISC_11, Items.MUSIC_DISC_WAIT });
/*  41 */     tag(ItemTags.COALS).add(new Item[] { Items.COAL, Items.CHARCOAL });
/*  42 */     tag(ItemTags.ARROWS).add(new Item[] { Items.ARROW, Items.TIPPED_ARROW, Items.SPECTRAL_ARROW });
/*  43 */     tag(ItemTags.LECTERN_BOOKS).add(new Item[] { Items.WRITTEN_BOOK, Items.WRITABLE_BOOK });
/*  44 */     tag(ItemTags.BEACON_PAYMENT_ITEMS).add(new Item[] { Items.NETHERITE_INGOT, Items.EMERALD, Items.DIAMOND, Items.GOLD_INGOT, Items.IRON_INGOT });
/*  45 */     tag(ItemTags.PIGLIN_REPELLENTS).add(Items.SOUL_TORCH).add(Items.SOUL_LANTERN).add(Items.SOUL_CAMPFIRE);
/*  46 */     tag(ItemTags.PIGLIN_LOVED).addTag(ItemTags.GOLD_ORES).add(new Item[] { Items.GOLD_BLOCK, Items.GILDED_BLACKSTONE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE, Items.GOLD_INGOT, Items.BELL, Items.CLOCK, Items.GOLDEN_CARROT, Items.GLISTERING_MELON_SLICE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR, Items.GOLDEN_NAUTILUS_ARMOR, Items.GOLDEN_SWORD, Items.GOLDEN_SPEAR, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.RAW_GOLD, Items.RAW_GOLD_BLOCK });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     tag(ItemTags.IGNORED_BY_PIGLIN_BABIES).add(Items.LEATHER);
/*  53 */     tag(ItemTags.PIGLIN_FOOD).add(new Item[] { Items.PORKCHOP, Items.COOKED_PORKCHOP });
/*  54 */     tag(ItemTags.PIGLIN_SAFE_ARMOR).add(new Item[] { Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS });
/*  55 */     tag(ItemTags.FOX_FOOD).add(new Item[] { Items.SWEET_BERRIES, Items.GLOW_BERRIES });
/*  56 */     tag(ItemTags.DUPLICATES_ALLAYS).add(Items.AMETHYST_SHARD);
/*  57 */     tag(ItemTags.BREWING_FUEL).add(Items.BLAZE_POWDER);
/*     */     
/*  59 */     tag(ItemTags.NON_FLAMMABLE_WOOD).add(new Item[] { Items.WARPED_STEM, Items.STRIPPED_WARPED_STEM, Items.WARPED_HYPHAE, Items.STRIPPED_WARPED_HYPHAE, Items.CRIMSON_STEM, Items.STRIPPED_CRIMSON_STEM, Items.CRIMSON_HYPHAE, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, Items.WARPED_PLANKS, Items.CRIMSON_SLAB, Items.WARPED_SLAB, Items.CRIMSON_PRESSURE_PLATE, Items.WARPED_PRESSURE_PLATE, Items.CRIMSON_FENCE, Items.WARPED_FENCE, Items.CRIMSON_TRAPDOOR, Items.WARPED_TRAPDOOR, Items.CRIMSON_FENCE_GATE, Items.WARPED_FENCE_GATE, Items.CRIMSON_STAIRS, Items.WARPED_STAIRS, Items.CRIMSON_BUTTON, Items.WARPED_BUTTON, Items.CRIMSON_DOOR, Items.WARPED_DOOR, Items.CRIMSON_SIGN, Items.WARPED_SIGN, Items.WARPED_HANGING_SIGN, Items.CRIMSON_HANGING_SIGN, Items.WARPED_SHELF, Items.CRIMSON_SHELF });
/*  60 */     tag(ItemTags.WOODEN_TOOL_MATERIALS).addTag(ItemTags.PLANKS);
/*  61 */     tag(ItemTags.STONE_TOOL_MATERIALS).add(new Item[] { Items.COBBLESTONE, Items.BLACKSTONE, Items.COBBLED_DEEPSLATE });
/*  62 */     tag(ItemTags.COPPER_TOOL_MATERIALS).add(Items.COPPER_INGOT);
/*  63 */     tag(ItemTags.IRON_TOOL_MATERIALS).add(Items.IRON_INGOT);
/*  64 */     tag(ItemTags.GOLD_TOOL_MATERIALS).add(Items.GOLD_INGOT);
/*  65 */     tag(ItemTags.DIAMOND_TOOL_MATERIALS).add(Items.DIAMOND);
/*  66 */     tag(ItemTags.NETHERITE_TOOL_MATERIALS).add(Items.NETHERITE_INGOT);
/*  67 */     tag(ItemTags.REPAIRS_LEATHER_ARMOR).add(Items.LEATHER);
/*  68 */     tag(ItemTags.REPAIRS_COPPER_ARMOR).add(Items.COPPER_INGOT);
/*  69 */     tag(ItemTags.REPAIRS_CHAIN_ARMOR).add(Items.IRON_INGOT);
/*  70 */     tag(ItemTags.REPAIRS_IRON_ARMOR).add(Items.IRON_INGOT);
/*  71 */     tag(ItemTags.REPAIRS_GOLD_ARMOR).add(Items.GOLD_INGOT);
/*  72 */     tag(ItemTags.REPAIRS_DIAMOND_ARMOR).add(Items.DIAMOND);
/*  73 */     tag(ItemTags.REPAIRS_NETHERITE_ARMOR).add(Items.NETHERITE_INGOT);
/*  74 */     tag(ItemTags.REPAIRS_TURTLE_HELMET).add(Items.TURTLE_SCUTE);
/*  75 */     tag(ItemTags.REPAIRS_WOLF_ARMOR).add(Items.ARMADILLO_SCUTE);
/*  76 */     tag(ItemTags.STONE_CRAFTING_MATERIALS).add(new Item[] { Items.COBBLESTONE, Items.BLACKSTONE, Items.COBBLED_DEEPSLATE });
/*  77 */     tag(ItemTags.FREEZE_IMMUNE_WEARABLES).add(new Item[] { Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET, Items.LEATHER_HORSE_ARMOR });
/*  78 */     tag(ItemTags.AXOLOTL_FOOD).add(Items.TROPICAL_FISH_BUCKET);
/*  79 */     tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(new Item[] { Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.NETHERITE_PICKAXE, Items.STONE_PICKAXE, Items.WOODEN_PICKAXE, Items.COPPER_PICKAXE });
/*  80 */     tag(ItemTags.COMPASSES).add(Items.COMPASS).add(Items.RECOVERY_COMPASS);
/*  81 */     tag(ItemTags.CREEPER_IGNITERS).add(Items.FLINT_AND_STEEL).add(Items.FIRE_CHARGE);
/*     */     
/*  83 */     tag(ItemTags.SWORDS).add(Items.DIAMOND_SWORD).add(Items.STONE_SWORD).add(Items.GOLDEN_SWORD).add(Items.NETHERITE_SWORD).add(Items.WOODEN_SWORD).add(Items.IRON_SWORD).add(Items.COPPER_SWORD);
/*  84 */     tag(ItemTags.AXES).add(Items.DIAMOND_AXE).add(Items.STONE_AXE).add(Items.GOLDEN_AXE).add(Items.NETHERITE_AXE).add(Items.WOODEN_AXE).add(Items.IRON_AXE).add(Items.COPPER_AXE);
/*  85 */     tag(ItemTags.PICKAXES).add(Items.DIAMOND_PICKAXE).add(Items.STONE_PICKAXE).add(Items.GOLDEN_PICKAXE).add(Items.NETHERITE_PICKAXE).add(Items.WOODEN_PICKAXE).add(Items.IRON_PICKAXE).add(Items.COPPER_PICKAXE);
/*  86 */     tag(ItemTags.SHOVELS).add(Items.DIAMOND_SHOVEL).add(Items.STONE_SHOVEL).add(Items.GOLDEN_SHOVEL).add(Items.NETHERITE_SHOVEL).add(Items.WOODEN_SHOVEL).add(Items.IRON_SHOVEL).add(Items.COPPER_SHOVEL);
/*  87 */     tag(ItemTags.HOES).add(Items.DIAMOND_HOE).add(Items.STONE_HOE).add(Items.GOLDEN_HOE).add(Items.NETHERITE_HOE).add(Items.WOODEN_HOE).add(Items.IRON_HOE).add(Items.COPPER_HOE);
/*  88 */     tag(ItemTags.SPEARS).add(new Item[] { Items.DIAMOND_SPEAR, Items.STONE_SPEAR, Items.GOLDEN_SPEAR, Items.NETHERITE_SPEAR, Items.WOODEN_SPEAR, Items.IRON_SPEAR, Items.COPPER_SPEAR });
/*     */     
/*  90 */     tag(ItemTags.BREAKS_DECORATED_POTS).addTag(ItemTags.SWORDS).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES).add(Items.TRIDENT).add(Items.MACE);
/*     */     
/*  92 */     tag(ItemTags.SKELETON_PREFERRED_WEAPONS).add(Items.BOW);
/*  93 */     tag(ItemTags.DROWNED_PREFERRED_WEAPONS).add(Items.TRIDENT);
/*  94 */     tag(ItemTags.PIGLIN_PREFERRED_WEAPONS).add(new Item[] { Items.CROSSBOW, Items.GOLDEN_SPEAR });
/*  95 */     tag(ItemTags.PILLAGER_PREFERRED_WEAPONS).add(Items.CROSSBOW);
/*     */     
/*  97 */     tag(ItemTags.WITHER_SKELETON_DISLIKED_WEAPONS).add(Items.BOW).add(Items.CROSSBOW);
/*     */     
/*  99 */     tag(ItemTags.DECORATED_POT_SHERDS).add(new Item[] { Items.ANGLER_POTTERY_SHERD, Items.ARCHER_POTTERY_SHERD, Items.ARMS_UP_POTTERY_SHERD, Items.BLADE_POTTERY_SHERD, Items.BREWER_POTTERY_SHERD, Items.BURN_POTTERY_SHERD, Items.DANGER_POTTERY_SHERD, Items.EXPLORER_POTTERY_SHERD, Items.FRIEND_POTTERY_SHERD, Items.HEART_POTTERY_SHERD, Items.HEARTBREAK_POTTERY_SHERD, Items.HOWL_POTTERY_SHERD, Items.MINER_POTTERY_SHERD, Items.MOURNER_POTTERY_SHERD, Items.PLENTY_POTTERY_SHERD, Items.PRIZE_POTTERY_SHERD, Items.SHEAF_POTTERY_SHERD, Items.SHELTER_POTTERY_SHERD, Items.SKULL_POTTERY_SHERD, Items.SNORT_POTTERY_SHERD, Items.FLOW_POTTERY_SHERD, Items.GUSTER_POTTERY_SHERD, Items.SCRAPE_POTTERY_SHERD });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     tag(ItemTags.DECORATED_POT_INGREDIENTS)
/* 126 */       .add(Items.BRICK)
/* 127 */       .addTag(ItemTags.DECORATED_POT_SHERDS);
/*     */     
/* 129 */     tag(ItemTags.FOOT_ARMOR).add(new Item[] { Items.LEATHER_BOOTS, Items.COPPER_BOOTS, Items.CHAINMAIL_BOOTS, Items.GOLDEN_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS });
/* 130 */     tag(ItemTags.LEG_ARMOR).add(new Item[] { Items.LEATHER_LEGGINGS, Items.COPPER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS });
/* 131 */     tag(ItemTags.CHEST_ARMOR).add(new Item[] { Items.LEATHER_CHESTPLATE, Items.COPPER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE });
/* 132 */     tag(ItemTags.HEAD_ARMOR).add(new Item[] { Items.LEATHER_HELMET, Items.COPPER_HELMET, Items.CHAINMAIL_HELMET, Items.GOLDEN_HELMET, Items.IRON_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET, Items.TURTLE_HELMET });
/* 133 */     tag(ItemTags.SKULLS).add(new Item[] { Items.PLAYER_HEAD, Items.CREEPER_HEAD, Items.ZOMBIE_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.DRAGON_HEAD, Items.PIGLIN_HEAD });
/*     */     
/* 135 */     tag(ItemTags.TRIMMABLE_ARMOR).addTag(ItemTags.FOOT_ARMOR).addTag(ItemTags.LEG_ARMOR).addTag(ItemTags.CHEST_ARMOR).addTag(ItemTags.HEAD_ARMOR);
/*     */     
/* 137 */     tag(ItemTags.TRIM_MATERIALS).addAll(registries.lookupOrThrow(Registries.ITEM).listElements()
/* 138 */         .filter(item -> ((Item)item.value()).components().has(DataComponents.PROVIDES_TRIM_MATERIAL))
/* 139 */         .sorted(Comparator.comparing(holder -> holder.key().identifier()))
/* 140 */         .map(Holder.Reference::value));
/*     */ 
/*     */     
/* 143 */     tag(ItemTags.BOOKSHELF_BOOKS).add(new Item[] { Items.BOOK, Items.WRITTEN_BOOK, Items.ENCHANTED_BOOK, Items.WRITABLE_BOOK, Items.KNOWLEDGE_BOOK });
/* 144 */     tag(ItemTags.NOTE_BLOCK_TOP_INSTRUMENTS).add(new Item[] { Items.ZOMBIE_HEAD, Items.SKELETON_SKULL, Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.WITHER_SKELETON_SKULL, Items.PIGLIN_HEAD, Items.PLAYER_HEAD });
/* 145 */     tag(ItemTags.SNIFFER_FOOD).add(Items.TORCHFLOWER_SEEDS);
/* 146 */     tag(ItemTags.VILLAGER_PLANTABLE_SEEDS).add(new Item[] { Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD });
/* 147 */     tag(ItemTags.VILLAGER_PICKS_UP).addTag(ItemTags.VILLAGER_PLANTABLE_SEEDS).add(new Item[] { Items.BREAD, Items.WHEAT, Items.BEETROOT });
/* 148 */     tag(ItemTags.BOOK_CLONING_TARGET).add(Items.WRITABLE_BOOK);
/*     */ 
/*     */     
/* 151 */     tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).addTag(ItemTags.FOOT_ARMOR);
/* 152 */     tag(ItemTags.LEG_ARMOR_ENCHANTABLE).addTag(ItemTags.LEG_ARMOR);
/* 153 */     tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).addTag(ItemTags.CHEST_ARMOR);
/* 154 */     tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).addTag(ItemTags.HEAD_ARMOR);
/* 155 */     tag(ItemTags.ARMOR_ENCHANTABLE).addTag(ItemTags.FOOT_ARMOR_ENCHANTABLE).addTag(ItemTags.LEG_ARMOR_ENCHANTABLE).addTag(ItemTags.CHEST_ARMOR_ENCHANTABLE).addTag(ItemTags.HEAD_ARMOR_ENCHANTABLE);
/* 156 */     tag(ItemTags.SWEEPING_ENCHANTABLE).addTag(ItemTags.SWORDS);
/* 157 */     tag(ItemTags.MELEE_WEAPON_ENCHANTABLE).addTag(ItemTags.SWORDS).addTag(ItemTags.SPEARS);
/* 158 */     tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).addTag(ItemTags.MELEE_WEAPON_ENCHANTABLE).add(Items.MACE);
/* 159 */     tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).addTag(ItemTags.MELEE_WEAPON_ENCHANTABLE).addTag(ItemTags.AXES);
/* 160 */     tag(ItemTags.WEAPON_ENCHANTABLE).addTag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(Items.MACE);
/* 161 */     tag(ItemTags.MACE_ENCHANTABLE).add(Items.MACE);
/* 162 */     tag(ItemTags.MINING_ENCHANTABLE).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES).add(Items.SHEARS);
/* 163 */     tag(ItemTags.MINING_LOOT_ENCHANTABLE).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES);
/* 164 */     tag(ItemTags.FISHING_ENCHANTABLE).add(Items.FISHING_ROD);
/* 165 */     tag(ItemTags.TRIDENT_ENCHANTABLE).add(Items.TRIDENT);
/* 166 */     tag(ItemTags.LUNGE_ENCHANTABLE).addTag(ItemTags.SPEARS);
/* 167 */     tag(ItemTags.DURABILITY_ENCHANTABLE)
/* 168 */       .addTag(ItemTags.FOOT_ARMOR).addTag(ItemTags.LEG_ARMOR).addTag(ItemTags.CHEST_ARMOR).addTag(ItemTags.HEAD_ARMOR)
/* 169 */       .add(Items.ELYTRA).add(Items.SHIELD)
/* 170 */       .addTag(ItemTags.SWORDS).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES)
/* 171 */       .add(Items.BOW).add(Items.CROSSBOW).add(Items.TRIDENT)
/* 172 */       .add(Items.FLINT_AND_STEEL).add(Items.SHEARS).add(Items.BRUSH)
/* 173 */       .add(Items.FISHING_ROD).add(new Item[] { Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK
/* 174 */         }).add(Items.MACE).addTag(ItemTags.SPEARS);
/*     */     
/* 176 */     tag(ItemTags.BOW_ENCHANTABLE).add(Items.BOW);
/* 177 */     tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
/* 178 */       .addTag(ItemTags.FOOT_ARMOR).addTag(ItemTags.LEG_ARMOR).addTag(ItemTags.CHEST_ARMOR).addTag(ItemTags.HEAD_ARMOR)
/* 179 */       .add(Items.ELYTRA)
/* 180 */       .addTag(ItemTags.SKULLS)
/* 181 */       .add(Items.CARVED_PUMPKIN);
/*     */     
/* 183 */     tag(ItemTags.CROSSBOW_ENCHANTABLE).add(Items.CROSSBOW);
/* 184 */     tag(ItemTags.VANISHING_ENCHANTABLE)
/* 185 */       .addTag(ItemTags.DURABILITY_ENCHANTABLE)
/* 186 */       .add(Items.COMPASS)
/* 187 */       .add(Items.CARVED_PUMPKIN)
/* 188 */       .addTag(ItemTags.SKULLS);
/*     */ 
/*     */     
/* 191 */     tag(ItemTags.DYEABLE).add(new Item[] { Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS, Items.LEATHER_HORSE_ARMOR, Items.WOLF_ARMOR });
/*     */     
/* 193 */     tag(ItemTags.FURNACE_MINECART_FUEL).add(new Item[] { Items.COAL, Items.CHARCOAL });
/*     */     
/* 195 */     tag(ItemTags.MEAT).add(new Item[] { Items.BEEF, Items.CHICKEN, Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT, Items.MUTTON, Items.PORKCHOP, Items.RABBIT, Items.ROTTEN_FLESH });
/* 196 */     tag(ItemTags.WOLF_FOOD).addTag(ItemTags.MEAT).add(new Item[] { Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.RABBIT_STEW });
/* 197 */     tag(ItemTags.OCELOT_FOOD).add(new Item[] { Items.COD, Items.SALMON });
/* 198 */     tag(ItemTags.CAT_FOOD).add(new Item[] { Items.COD, Items.SALMON });
/* 199 */     tag(ItemTags.HORSE_FOOD).add(new Item[] { Items.WHEAT, Items.SUGAR, Items.HAY_BLOCK, Items.APPLE, Items.CARROT, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE });
/* 200 */     tag(ItemTags.ZOMBIE_HORSE_FOOD).add(Items.RED_MUSHROOM);
/* 201 */     tag(ItemTags.HORSE_TEMPT_ITEMS).add(new Item[] { Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE });
/* 202 */     tag(ItemTags.HARNESSES).add(new Item[] { Items.WHITE_HARNESS, Items.ORANGE_HARNESS, Items.MAGENTA_HARNESS, Items.LIGHT_BLUE_HARNESS, Items.YELLOW_HARNESS, Items.LIME_HARNESS, Items.PINK_HARNESS, Items.GRAY_HARNESS, Items.LIGHT_GRAY_HARNESS, Items.CYAN_HARNESS, Items.PURPLE_HARNESS, Items.BLUE_HARNESS, Items.BROWN_HARNESS, Items.GREEN_HARNESS, Items.RED_HARNESS, Items.BLACK_HARNESS });
/* 203 */     tag(ItemTags.HAPPY_GHAST_FOOD).add(Items.SNOWBALL);
/* 204 */     tag(ItemTags.HAPPY_GHAST_TEMPT_ITEMS).addTag(ItemTags.HAPPY_GHAST_FOOD).addTag(ItemTags.HARNESSES);
/* 205 */     tag(ItemTags.CAMEL_FOOD).add(Items.CACTUS);
/* 206 */     tag(ItemTags.CAMEL_HUSK_FOOD).add(Items.RABBIT_FOOT);
/* 207 */     tag(ItemTags.ARMADILLO_FOOD).add(Items.SPIDER_EYE);
/* 208 */     tag(ItemTags.CHICKEN_FOOD).add(new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD });
/* 209 */     tag(ItemTags.FROG_FOOD).add(Items.SLIME_BALL);
/* 210 */     tag(ItemTags.HOGLIN_FOOD).add(Items.CRIMSON_FUNGUS);
/* 211 */     tag(ItemTags.LLAMA_FOOD).add(new Item[] { Items.WHEAT, Items.HAY_BLOCK });
/* 212 */     tag(ItemTags.LLAMA_TEMPT_ITEMS).add(Items.HAY_BLOCK);
/* 213 */     tag(ItemTags.NAUTILUS_TAMING_ITEMS).add(new Item[] { Items.PUFFERFISH_BUCKET, Items.PUFFERFISH });
/* 214 */     tag(ItemTags.NAUTILUS_BUCKET_FOOD).add(new Item[] { Items.PUFFERFISH_BUCKET, Items.COD_BUCKET, Items.SALMON_BUCKET, Items.TROPICAL_FISH_BUCKET });
/* 215 */     tag(ItemTags.NAUTILUS_FOOD).addTag(ItemTags.FISHES).addTag(ItemTags.NAUTILUS_BUCKET_FOOD);
/* 216 */     tag(ItemTags.PANDA_FOOD).add(Items.BAMBOO);
/* 217 */     tag(ItemTags.PANDA_EATS_FROM_GROUND).addTag(ItemTags.PANDA_FOOD).add(Items.CAKE);
/* 218 */     tag(ItemTags.PIG_FOOD).add(new Item[] { Items.CARROT, Items.POTATO, Items.BEETROOT });
/* 219 */     tag(ItemTags.RABBIT_FOOD).add(new Item[] { Items.CARROT, Items.GOLDEN_CARROT, Items.DANDELION });
/* 220 */     tag(ItemTags.STRIDER_FOOD).add(Items.WARPED_FUNGUS);
/* 221 */     tag(ItemTags.STRIDER_TEMPT_ITEMS).addTag(ItemTags.STRIDER_FOOD).add(Items.WARPED_FUNGUS_ON_A_STICK);
/* 222 */     tag(ItemTags.TURTLE_FOOD).add(Items.SEAGRASS);
/* 223 */     tag(ItemTags.PARROT_FOOD).add(new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD });
/* 224 */     tag(ItemTags.PARROT_POISONOUS_FOOD).add(Items.COOKIE);
/* 225 */     tag(ItemTags.COW_FOOD).add(Items.WHEAT);
/* 226 */     tag(ItemTags.SHEEP_FOOD).add(Items.WHEAT);
/* 227 */     tag(ItemTags.GOAT_FOOD).add(Items.WHEAT);
/* 228 */     tag(ItemTags.MAP_INVISIBILITY_EQUIPMENT).add(Items.CARVED_PUMPKIN);
/* 229 */     tag(ItemTags.GAZE_DISGUISE_EQUIPMENT).add(Items.CARVED_PUMPKIN);
/* 230 */     tag(ItemTags.SHEARABLE_FROM_COPPER_GOLEM).add(Items.POPPY);
/*     */   }
/*     */   
/*     */   private static class BlockToItemConverter
/*     */     extends Object implements TagAppender<Block, Block> {
/*     */     private final TagAppender<Item, Item> itemAppender;
/*     */     
/* 237 */     public BlockToItemConverter(TagAppender<Item, Item> itemAppender) { this.itemAppender = itemAppender; }
/*     */ 
/*     */ 
/*     */     
/*     */     public TagAppender<Block, Block> add(Block element) {
/* 242 */       this.itemAppender.add((Item)Objects.requireNonNull(element.asItem()));
/* 243 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public TagAppender<Block, Block> addOptional(Block element) {
/* 248 */       this.itemAppender.addOptional((Item)Objects.requireNonNull(element.asItem()));
/* 249 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 253 */     private static TagKey<Item> blockTagToItemTag(TagKey<Block> blockTag) { return TagKey.create(Registries.ITEM, blockTag.location()); }
/*     */ 
/*     */ 
/*     */     
/*     */     public TagAppender<Block, Block> addTag(TagKey<Block> tag) {
/* 258 */       this.itemAppender.addTag(blockTagToItemTag(tag));
/* 259 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public TagAppender<Block, Block> addOptionalTag(TagKey<Block> tag) {
/* 264 */       this.itemAppender.addOptionalTag(blockTagToItemTag(tag));
/* 265 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\tags\VanillaItemTagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */