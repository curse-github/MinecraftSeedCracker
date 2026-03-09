/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.Tag;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
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
/*     */ public class EntityBlockStateFix
/*     */   extends DataFix
/*     */ {
/*  34 */   public EntityBlockStateFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */   
/*  37 */   private static final Map<String, Integer> MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/*  38 */         map.put("minecraft:air", Integer.valueOf(0));
/*  39 */         map.put("minecraft:stone", Integer.valueOf(1));
/*  40 */         map.put("minecraft:grass", Integer.valueOf(2));
/*  41 */         map.put("minecraft:dirt", Integer.valueOf(3));
/*  42 */         map.put("minecraft:cobblestone", Integer.valueOf(4));
/*  43 */         map.put("minecraft:planks", Integer.valueOf(5));
/*  44 */         map.put("minecraft:sapling", Integer.valueOf(6));
/*  45 */         map.put("minecraft:bedrock", Integer.valueOf(7));
/*  46 */         map.put("minecraft:flowing_water", Integer.valueOf(8));
/*  47 */         map.put("minecraft:water", Integer.valueOf(9));
/*  48 */         map.put("minecraft:flowing_lava", Integer.valueOf(10));
/*  49 */         map.put("minecraft:lava", Integer.valueOf(11));
/*  50 */         map.put("minecraft:sand", Integer.valueOf(12));
/*  51 */         map.put("minecraft:gravel", Integer.valueOf(13));
/*  52 */         map.put("minecraft:gold_ore", Integer.valueOf(14));
/*  53 */         map.put("minecraft:iron_ore", Integer.valueOf(15));
/*  54 */         map.put("minecraft:coal_ore", Integer.valueOf(16));
/*  55 */         map.put("minecraft:log", Integer.valueOf(17));
/*  56 */         map.put("minecraft:leaves", Integer.valueOf(18));
/*  57 */         map.put("minecraft:sponge", Integer.valueOf(19));
/*  58 */         map.put("minecraft:glass", Integer.valueOf(20));
/*  59 */         map.put("minecraft:lapis_ore", Integer.valueOf(21));
/*  60 */         map.put("minecraft:lapis_block", Integer.valueOf(22));
/*  61 */         map.put("minecraft:dispenser", Integer.valueOf(23));
/*  62 */         map.put("minecraft:sandstone", Integer.valueOf(24));
/*  63 */         map.put("minecraft:noteblock", Integer.valueOf(25));
/*  64 */         map.put("minecraft:bed", Integer.valueOf(26));
/*  65 */         map.put("minecraft:golden_rail", Integer.valueOf(27));
/*  66 */         map.put("minecraft:detector_rail", Integer.valueOf(28));
/*  67 */         map.put("minecraft:sticky_piston", Integer.valueOf(29));
/*  68 */         map.put("minecraft:web", Integer.valueOf(30));
/*  69 */         map.put("minecraft:tallgrass", Integer.valueOf(31));
/*  70 */         map.put("minecraft:deadbush", Integer.valueOf(32));
/*  71 */         map.put("minecraft:piston", Integer.valueOf(33));
/*  72 */         map.put("minecraft:piston_head", Integer.valueOf(34));
/*  73 */         map.put("minecraft:wool", Integer.valueOf(35));
/*  74 */         map.put("minecraft:piston_extension", Integer.valueOf(36));
/*  75 */         map.put("minecraft:yellow_flower", Integer.valueOf(37));
/*  76 */         map.put("minecraft:red_flower", Integer.valueOf(38));
/*  77 */         map.put("minecraft:brown_mushroom", Integer.valueOf(39));
/*  78 */         map.put("minecraft:red_mushroom", Integer.valueOf(40));
/*  79 */         map.put("minecraft:gold_block", Integer.valueOf(41));
/*  80 */         map.put("minecraft:iron_block", Integer.valueOf(42));
/*  81 */         map.put("minecraft:double_stone_slab", Integer.valueOf(43));
/*  82 */         map.put("minecraft:stone_slab", Integer.valueOf(44));
/*  83 */         map.put("minecraft:brick_block", Integer.valueOf(45));
/*  84 */         map.put("minecraft:tnt", Integer.valueOf(46));
/*  85 */         map.put("minecraft:bookshelf", Integer.valueOf(47));
/*  86 */         map.put("minecraft:mossy_cobblestone", Integer.valueOf(48));
/*  87 */         map.put("minecraft:obsidian", Integer.valueOf(49));
/*  88 */         map.put("minecraft:torch", Integer.valueOf(50));
/*  89 */         map.put("minecraft:fire", Integer.valueOf(51));
/*  90 */         map.put("minecraft:mob_spawner", Integer.valueOf(52));
/*  91 */         map.put("minecraft:oak_stairs", Integer.valueOf(53));
/*  92 */         map.put("minecraft:chest", Integer.valueOf(54));
/*  93 */         map.put("minecraft:redstone_wire", Integer.valueOf(55));
/*  94 */         map.put("minecraft:diamond_ore", Integer.valueOf(56));
/*  95 */         map.put("minecraft:diamond_block", Integer.valueOf(57));
/*  96 */         map.put("minecraft:crafting_table", Integer.valueOf(58));
/*  97 */         map.put("minecraft:wheat", Integer.valueOf(59));
/*  98 */         map.put("minecraft:farmland", Integer.valueOf(60));
/*  99 */         map.put("minecraft:furnace", Integer.valueOf(61));
/* 100 */         map.put("minecraft:lit_furnace", Integer.valueOf(62));
/* 101 */         map.put("minecraft:standing_sign", Integer.valueOf(63));
/* 102 */         map.put("minecraft:wooden_door", Integer.valueOf(64));
/* 103 */         map.put("minecraft:ladder", Integer.valueOf(65));
/* 104 */         map.put("minecraft:rail", Integer.valueOf(66));
/* 105 */         map.put("minecraft:stone_stairs", Integer.valueOf(67));
/* 106 */         map.put("minecraft:wall_sign", Integer.valueOf(68));
/* 107 */         map.put("minecraft:lever", Integer.valueOf(69));
/* 108 */         map.put("minecraft:stone_pressure_plate", Integer.valueOf(70));
/* 109 */         map.put("minecraft:iron_door", Integer.valueOf(71));
/* 110 */         map.put("minecraft:wooden_pressure_plate", Integer.valueOf(72));
/* 111 */         map.put("minecraft:redstone_ore", Integer.valueOf(73));
/* 112 */         map.put("minecraft:lit_redstone_ore", Integer.valueOf(74));
/* 113 */         map.put("minecraft:unlit_redstone_torch", Integer.valueOf(75));
/* 114 */         map.put("minecraft:redstone_torch", Integer.valueOf(76));
/* 115 */         map.put("minecraft:stone_button", Integer.valueOf(77));
/* 116 */         map.put("minecraft:snow_layer", Integer.valueOf(78));
/* 117 */         map.put("minecraft:ice", Integer.valueOf(79));
/* 118 */         map.put("minecraft:snow", Integer.valueOf(80));
/* 119 */         map.put("minecraft:cactus", Integer.valueOf(81));
/* 120 */         map.put("minecraft:clay", Integer.valueOf(82));
/* 121 */         map.put("minecraft:reeds", Integer.valueOf(83));
/* 122 */         map.put("minecraft:jukebox", Integer.valueOf(84));
/* 123 */         map.put("minecraft:fence", Integer.valueOf(85));
/* 124 */         map.put("minecraft:pumpkin", Integer.valueOf(86));
/* 125 */         map.put("minecraft:netherrack", Integer.valueOf(87));
/* 126 */         map.put("minecraft:soul_sand", Integer.valueOf(88));
/* 127 */         map.put("minecraft:glowstone", Integer.valueOf(89));
/* 128 */         map.put("minecraft:portal", Integer.valueOf(90));
/* 129 */         map.put("minecraft:lit_pumpkin", Integer.valueOf(91));
/* 130 */         map.put("minecraft:cake", Integer.valueOf(92));
/* 131 */         map.put("minecraft:unpowered_repeater", Integer.valueOf(93));
/* 132 */         map.put("minecraft:powered_repeater", Integer.valueOf(94));
/* 133 */         map.put("minecraft:stained_glass", Integer.valueOf(95));
/* 134 */         map.put("minecraft:trapdoor", Integer.valueOf(96));
/* 135 */         map.put("minecraft:monster_egg", Integer.valueOf(97));
/* 136 */         map.put("minecraft:stonebrick", Integer.valueOf(98));
/* 137 */         map.put("minecraft:brown_mushroom_block", Integer.valueOf(99));
/* 138 */         map.put("minecraft:red_mushroom_block", Integer.valueOf(100));
/* 139 */         map.put("minecraft:iron_bars", Integer.valueOf(101));
/* 140 */         map.put("minecraft:glass_pane", Integer.valueOf(102));
/* 141 */         map.put("minecraft:melon_block", Integer.valueOf(103));
/* 142 */         map.put("minecraft:pumpkin_stem", Integer.valueOf(104));
/* 143 */         map.put("minecraft:melon_stem", Integer.valueOf(105));
/* 144 */         map.put("minecraft:vine", Integer.valueOf(106));
/* 145 */         map.put("minecraft:fence_gate", Integer.valueOf(107));
/* 146 */         map.put("minecraft:brick_stairs", Integer.valueOf(108));
/* 147 */         map.put("minecraft:stone_brick_stairs", Integer.valueOf(109));
/* 148 */         map.put("minecraft:mycelium", Integer.valueOf(110));
/* 149 */         map.put("minecraft:waterlily", Integer.valueOf(111));
/* 150 */         map.put("minecraft:nether_brick", Integer.valueOf(112));
/* 151 */         map.put("minecraft:nether_brick_fence", Integer.valueOf(113));
/* 152 */         map.put("minecraft:nether_brick_stairs", Integer.valueOf(114));
/* 153 */         map.put("minecraft:nether_wart", Integer.valueOf(115));
/* 154 */         map.put("minecraft:enchanting_table", Integer.valueOf(116));
/* 155 */         map.put("minecraft:brewing_stand", Integer.valueOf(117));
/* 156 */         map.put("minecraft:cauldron", Integer.valueOf(118));
/* 157 */         map.put("minecraft:end_portal", Integer.valueOf(119));
/* 158 */         map.put("minecraft:end_portal_frame", Integer.valueOf(120));
/* 159 */         map.put("minecraft:end_stone", Integer.valueOf(121));
/* 160 */         map.put("minecraft:dragon_egg", Integer.valueOf(122));
/* 161 */         map.put("minecraft:redstone_lamp", Integer.valueOf(123));
/* 162 */         map.put("minecraft:lit_redstone_lamp", Integer.valueOf(124));
/* 163 */         map.put("minecraft:double_wooden_slab", Integer.valueOf(125));
/* 164 */         map.put("minecraft:wooden_slab", Integer.valueOf(126));
/* 165 */         map.put("minecraft:cocoa", Integer.valueOf(127));
/* 166 */         map.put("minecraft:sandstone_stairs", Integer.valueOf(128));
/* 167 */         map.put("minecraft:emerald_ore", Integer.valueOf(129));
/* 168 */         map.put("minecraft:ender_chest", Integer.valueOf(130));
/* 169 */         map.put("minecraft:tripwire_hook", Integer.valueOf(131));
/* 170 */         map.put("minecraft:tripwire", Integer.valueOf(132));
/* 171 */         map.put("minecraft:emerald_block", Integer.valueOf(133));
/* 172 */         map.put("minecraft:spruce_stairs", Integer.valueOf(134));
/* 173 */         map.put("minecraft:birch_stairs", Integer.valueOf(135));
/* 174 */         map.put("minecraft:jungle_stairs", Integer.valueOf(136));
/* 175 */         map.put("minecraft:command_block", Integer.valueOf(137));
/* 176 */         map.put("minecraft:beacon", Integer.valueOf(138));
/* 177 */         map.put("minecraft:cobblestone_wall", Integer.valueOf(139));
/* 178 */         map.put("minecraft:flower_pot", Integer.valueOf(140));
/* 179 */         map.put("minecraft:carrots", Integer.valueOf(141));
/* 180 */         map.put("minecraft:potatoes", Integer.valueOf(142));
/* 181 */         map.put("minecraft:wooden_button", Integer.valueOf(143));
/* 182 */         map.put("minecraft:skull", Integer.valueOf(144));
/* 183 */         map.put("minecraft:anvil", Integer.valueOf(145));
/* 184 */         map.put("minecraft:trapped_chest", Integer.valueOf(146));
/* 185 */         map.put("minecraft:light_weighted_pressure_plate", Integer.valueOf(147));
/* 186 */         map.put("minecraft:heavy_weighted_pressure_plate", Integer.valueOf(148));
/* 187 */         map.put("minecraft:unpowered_comparator", Integer.valueOf(149));
/* 188 */         map.put("minecraft:powered_comparator", Integer.valueOf(150));
/* 189 */         map.put("minecraft:daylight_detector", Integer.valueOf(151));
/* 190 */         map.put("minecraft:redstone_block", Integer.valueOf(152));
/* 191 */         map.put("minecraft:quartz_ore", Integer.valueOf(153));
/* 192 */         map.put("minecraft:hopper", Integer.valueOf(154));
/* 193 */         map.put("minecraft:quartz_block", Integer.valueOf(155));
/* 194 */         map.put("minecraft:quartz_stairs", Integer.valueOf(156));
/* 195 */         map.put("minecraft:activator_rail", Integer.valueOf(157));
/* 196 */         map.put("minecraft:dropper", Integer.valueOf(158));
/* 197 */         map.put("minecraft:stained_hardened_clay", Integer.valueOf(159));
/* 198 */         map.put("minecraft:stained_glass_pane", Integer.valueOf(160));
/* 199 */         map.put("minecraft:leaves2", Integer.valueOf(161));
/* 200 */         map.put("minecraft:log2", Integer.valueOf(162));
/* 201 */         map.put("minecraft:acacia_stairs", Integer.valueOf(163));
/* 202 */         map.put("minecraft:dark_oak_stairs", Integer.valueOf(164));
/* 203 */         map.put("minecraft:slime", Integer.valueOf(165));
/* 204 */         map.put("minecraft:barrier", Integer.valueOf(166));
/* 205 */         map.put("minecraft:iron_trapdoor", Integer.valueOf(167));
/* 206 */         map.put("minecraft:prismarine", Integer.valueOf(168));
/* 207 */         map.put("minecraft:sea_lantern", Integer.valueOf(169));
/* 208 */         map.put("minecraft:hay_block", Integer.valueOf(170));
/* 209 */         map.put("minecraft:carpet", Integer.valueOf(171));
/* 210 */         map.put("minecraft:hardened_clay", Integer.valueOf(172));
/* 211 */         map.put("minecraft:coal_block", Integer.valueOf(173));
/* 212 */         map.put("minecraft:packed_ice", Integer.valueOf(174));
/* 213 */         map.put("minecraft:double_plant", Integer.valueOf(175));
/* 214 */         map.put("minecraft:standing_banner", Integer.valueOf(176));
/* 215 */         map.put("minecraft:wall_banner", Integer.valueOf(177));
/* 216 */         map.put("minecraft:daylight_detector_inverted", Integer.valueOf(178));
/* 217 */         map.put("minecraft:red_sandstone", Integer.valueOf(179));
/* 218 */         map.put("minecraft:red_sandstone_stairs", Integer.valueOf(180));
/* 219 */         map.put("minecraft:double_stone_slab2", Integer.valueOf(181));
/* 220 */         map.put("minecraft:stone_slab2", Integer.valueOf(182));
/* 221 */         map.put("minecraft:spruce_fence_gate", Integer.valueOf(183));
/* 222 */         map.put("minecraft:birch_fence_gate", Integer.valueOf(184));
/* 223 */         map.put("minecraft:jungle_fence_gate", Integer.valueOf(185));
/* 224 */         map.put("minecraft:dark_oak_fence_gate", Integer.valueOf(186));
/* 225 */         map.put("minecraft:acacia_fence_gate", Integer.valueOf(187));
/* 226 */         map.put("minecraft:spruce_fence", Integer.valueOf(188));
/* 227 */         map.put("minecraft:birch_fence", Integer.valueOf(189));
/* 228 */         map.put("minecraft:jungle_fence", Integer.valueOf(190));
/* 229 */         map.put("minecraft:dark_oak_fence", Integer.valueOf(191));
/* 230 */         map.put("minecraft:acacia_fence", Integer.valueOf(192));
/* 231 */         map.put("minecraft:spruce_door", Integer.valueOf(193));
/* 232 */         map.put("minecraft:birch_door", Integer.valueOf(194));
/* 233 */         map.put("minecraft:jungle_door", Integer.valueOf(195));
/* 234 */         map.put("minecraft:acacia_door", Integer.valueOf(196));
/* 235 */         map.put("minecraft:dark_oak_door", Integer.valueOf(197));
/* 236 */         map.put("minecraft:end_rod", Integer.valueOf(198));
/* 237 */         map.put("minecraft:chorus_plant", Integer.valueOf(199));
/* 238 */         map.put("minecraft:chorus_flower", Integer.valueOf(200));
/* 239 */         map.put("minecraft:purpur_block", Integer.valueOf(201));
/* 240 */         map.put("minecraft:purpur_pillar", Integer.valueOf(202));
/* 241 */         map.put("minecraft:purpur_stairs", Integer.valueOf(203));
/* 242 */         map.put("minecraft:purpur_double_slab", Integer.valueOf(204));
/* 243 */         map.put("minecraft:purpur_slab", Integer.valueOf(205));
/* 244 */         map.put("minecraft:end_bricks", Integer.valueOf(206));
/* 245 */         map.put("minecraft:beetroots", Integer.valueOf(207));
/* 246 */         map.put("minecraft:grass_path", Integer.valueOf(208));
/* 247 */         map.put("minecraft:end_gateway", Integer.valueOf(209));
/* 248 */         map.put("minecraft:repeating_command_block", Integer.valueOf(210));
/* 249 */         map.put("minecraft:chain_command_block", Integer.valueOf(211));
/* 250 */         map.put("minecraft:frosted_ice", Integer.valueOf(212));
/* 251 */         map.put("minecraft:magma", Integer.valueOf(213));
/* 252 */         map.put("minecraft:nether_wart_block", Integer.valueOf(214));
/* 253 */         map.put("minecraft:red_nether_brick", Integer.valueOf(215));
/* 254 */         map.put("minecraft:bone_block", Integer.valueOf(216));
/* 255 */         map.put("minecraft:structure_void", Integer.valueOf(217));
/* 256 */         map.put("minecraft:observer", Integer.valueOf(218));
/* 257 */         map.put("minecraft:white_shulker_box", Integer.valueOf(219));
/* 258 */         map.put("minecraft:orange_shulker_box", Integer.valueOf(220));
/* 259 */         map.put("minecraft:magenta_shulker_box", Integer.valueOf(221));
/* 260 */         map.put("minecraft:light_blue_shulker_box", Integer.valueOf(222));
/* 261 */         map.put("minecraft:yellow_shulker_box", Integer.valueOf(223));
/* 262 */         map.put("minecraft:lime_shulker_box", Integer.valueOf(224));
/* 263 */         map.put("minecraft:pink_shulker_box", Integer.valueOf(225));
/* 264 */         map.put("minecraft:gray_shulker_box", Integer.valueOf(226));
/* 265 */         map.put("minecraft:silver_shulker_box", Integer.valueOf(227));
/* 266 */         map.put("minecraft:cyan_shulker_box", Integer.valueOf(228));
/* 267 */         map.put("minecraft:purple_shulker_box", Integer.valueOf(229));
/* 268 */         map.put("minecraft:blue_shulker_box", Integer.valueOf(230));
/* 269 */         map.put("minecraft:brown_shulker_box", Integer.valueOf(231));
/* 270 */         map.put("minecraft:green_shulker_box", Integer.valueOf(232));
/* 271 */         map.put("minecraft:red_shulker_box", Integer.valueOf(233));
/* 272 */         map.put("minecraft:black_shulker_box", Integer.valueOf(234));
/* 273 */         map.put("minecraft:white_glazed_terracotta", Integer.valueOf(235));
/* 274 */         map.put("minecraft:orange_glazed_terracotta", Integer.valueOf(236));
/* 275 */         map.put("minecraft:magenta_glazed_terracotta", Integer.valueOf(237));
/* 276 */         map.put("minecraft:light_blue_glazed_terracotta", Integer.valueOf(238));
/* 277 */         map.put("minecraft:yellow_glazed_terracotta", Integer.valueOf(239));
/* 278 */         map.put("minecraft:lime_glazed_terracotta", Integer.valueOf(240));
/* 279 */         map.put("minecraft:pink_glazed_terracotta", Integer.valueOf(241));
/* 280 */         map.put("minecraft:gray_glazed_terracotta", Integer.valueOf(242));
/* 281 */         map.put("minecraft:silver_glazed_terracotta", Integer.valueOf(243));
/* 282 */         map.put("minecraft:cyan_glazed_terracotta", Integer.valueOf(244));
/* 283 */         map.put("minecraft:purple_glazed_terracotta", Integer.valueOf(245));
/* 284 */         map.put("minecraft:blue_glazed_terracotta", Integer.valueOf(246));
/* 285 */         map.put("minecraft:brown_glazed_terracotta", Integer.valueOf(247));
/* 286 */         map.put("minecraft:green_glazed_terracotta", Integer.valueOf(248));
/* 287 */         map.put("minecraft:red_glazed_terracotta", Integer.valueOf(249));
/* 288 */         map.put("minecraft:black_glazed_terracotta", Integer.valueOf(250));
/* 289 */         map.put("minecraft:concrete", Integer.valueOf(251));
/* 290 */         map.put("minecraft:concrete_powder", Integer.valueOf(252));
/* 291 */         map.put("minecraft:structure_block", Integer.valueOf(255));
/*     */       });
/*     */   
/*     */   public static int getBlockId(String name) {
/* 295 */     Integer result = (Integer)MAP.get(name);
/* 296 */     return (result == null) ? 0 : result.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 301 */     Schema inputSchema = getInputSchema();
/* 302 */     Schema outputSchema = getOutputSchema();
/*     */     
/* 304 */     Function<Typed<?>, Typed<?>> minecartUpdater = input -> updateBlockToBlockState(input, "DisplayTile", "DisplayData", "DisplayState");
/* 305 */     Function<Typed<?>, Typed<?>> arrowUpdater = input -> updateBlockToBlockState(input, "inTile", "inData", "inBlockState");
/*     */     
/* 307 */     Type<Pair<Either<Pair<String, Either<Integer, String>>, Unit>, Dynamic<?>>> oldProjectileType = DSL.and(
/* 308 */         DSL.optional(DSL.field("inTile", DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString())))), 
/* 309 */         DSL.remainderType());
/*     */ 
/*     */     
/* 312 */     Function<Typed<?>, Typed<?>> removeInTile = input -> input.update(oldProjectileType.finder(), DSL.remainderType(), Pair::getSecond);
/*     */     
/* 314 */     return fixTypeEverywhereTyped("EntityBlockStateFix", inputSchema.getType(References.ENTITY), outputSchema.getType(References.ENTITY), input -> {
/* 315 */           input = updateEntity(input, "minecraft:falling_block", this::updateFallingBlock);
/* 316 */           input = updateEntity(input, "minecraft:enderman", ());
/* 317 */           input = updateEntity(input, "minecraft:arrow", arrowUpdater);
/* 318 */           input = updateEntity(input, "minecraft:spectral_arrow", arrowUpdater);
/* 319 */           input = updateEntity(input, "minecraft:egg", removeInTile);
/* 320 */           input = updateEntity(input, "minecraft:ender_pearl", removeInTile);
/* 321 */           input = updateEntity(input, "minecraft:fireball", removeInTile);
/* 322 */           input = updateEntity(input, "minecraft:potion", removeInTile);
/* 323 */           input = updateEntity(input, "minecraft:small_fireball", removeInTile);
/* 324 */           input = updateEntity(input, "minecraft:snowball", removeInTile);
/* 325 */           input = updateEntity(input, "minecraft:wither_skull", removeInTile);
/* 326 */           input = updateEntity(input, "minecraft:xp_bottle", removeInTile);
/* 327 */           input = updateEntity(input, "minecraft:commandblock_minecart", minecartUpdater);
/* 328 */           input = updateEntity(input, "minecraft:minecart", minecartUpdater);
/* 329 */           input = updateEntity(input, "minecraft:chest_minecart", minecartUpdater);
/* 330 */           input = updateEntity(input, "minecraft:furnace_minecart", minecartUpdater);
/* 331 */           input = updateEntity(input, "minecraft:tnt_minecart", minecartUpdater);
/* 332 */           input = updateEntity(input, "minecraft:hopper_minecart", minecartUpdater);
/* 333 */           return updateEntity(input, "minecraft:spawner_minecart", minecartUpdater);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private Typed<?> updateFallingBlock(Typed<?> input) {
/* 339 */     Type<Either<Pair<String, Either<Integer, String>>, Unit>> oldType = DSL.optional(DSL.field("Block", DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString()))));
/* 340 */     Type<Either<Pair<String, Dynamic<?>>, Unit>> newType = DSL.optional(DSL.field("BlockState", DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType())));
/*     */     
/* 342 */     Dynamic<?> tag = (Dynamic)input.get(DSL.remainderFinder());
/*     */     
/* 344 */     return input.update(oldType.finder(), newType, tile -> {
/* 345 */           int block = ((Integer)tile.map((), ())).intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 353 */           int data = tag.get("Data").asInt(0) & 0xF;
/* 354 */           return Either.left(Pair.of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(block << 4 | data)));
/* 355 */         }).set(DSL.remainderFinder(), tag.remove("Data").remove("TileID").remove("Tile"));
/*     */   }
/*     */   
/*     */   private Typed<?> updateBlockToBlockState(Typed<?> input, String oldFieldName, String dataName, String newFieldName) {
/* 359 */     Tag.TagType tagType1 = DSL.field(oldFieldName, DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString())));
/* 360 */     Tag.TagType tagType2 = DSL.field(newFieldName, DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType()));
/*     */     
/* 362 */     Dynamic<?> tag = (Dynamic)input.getOrCreate(DSL.remainderFinder());
/*     */     
/* 364 */     return input.update(tagType1.finder(), tagType2, tile -> {
/* 365 */           int block = ((Integer)((Either)tile.getSecond()).map((), EntityBlockStateFix::getBlockId)).intValue();
/* 366 */           int data = tag.get(dataName).asInt(0) & 0xF;
/*     */           
/* 368 */           return Pair.of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(block << 4 | data));
/* 369 */         }).set(DSL.remainderFinder(), tag.remove(dataName));
/*     */   }
/*     */   
/*     */   private Typed<?> updateEntity(Typed<?> input, String name, Function<Typed<?>, Typed<?>> function) {
/* 373 */     Type<?> oldType = getInputSchema().getChoiceType(References.ENTITY, name);
/* 374 */     Type<?> newType = getOutputSchema().getChoiceType(References.ENTITY, name);
/* 375 */     return input.updateTyped(DSL.namedChoice(name, oldType), newType, function);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityBlockStateFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */