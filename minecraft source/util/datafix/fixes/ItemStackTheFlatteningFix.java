/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ 
/*     */ public class ItemStackTheFlatteningFix
/*     */   extends DataFix
/*     */ {
/*  26 */   public ItemStackTheFlatteningFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */   
/*  29 */   private static final Map<String, String> MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/*  30 */         map.put("minecraft:stone.0", "minecraft:stone");
/*  31 */         map.put("minecraft:stone.1", "minecraft:granite");
/*  32 */         map.put("minecraft:stone.2", "minecraft:polished_granite");
/*  33 */         map.put("minecraft:stone.3", "minecraft:diorite");
/*  34 */         map.put("minecraft:stone.4", "minecraft:polished_diorite");
/*  35 */         map.put("minecraft:stone.5", "minecraft:andesite");
/*  36 */         map.put("minecraft:stone.6", "minecraft:polished_andesite");
/*  37 */         map.put("minecraft:dirt.0", "minecraft:dirt");
/*  38 */         map.put("minecraft:dirt.1", "minecraft:coarse_dirt");
/*  39 */         map.put("minecraft:dirt.2", "minecraft:podzol");
/*  40 */         map.put("minecraft:leaves.0", "minecraft:oak_leaves");
/*  41 */         map.put("minecraft:leaves.1", "minecraft:spruce_leaves");
/*  42 */         map.put("minecraft:leaves.2", "minecraft:birch_leaves");
/*  43 */         map.put("minecraft:leaves.3", "minecraft:jungle_leaves");
/*  44 */         map.put("minecraft:leaves2.0", "minecraft:acacia_leaves");
/*  45 */         map.put("minecraft:leaves2.1", "minecraft:dark_oak_leaves");
/*  46 */         map.put("minecraft:log.0", "minecraft:oak_log");
/*  47 */         map.put("minecraft:log.1", "minecraft:spruce_log");
/*  48 */         map.put("minecraft:log.2", "minecraft:birch_log");
/*  49 */         map.put("minecraft:log.3", "minecraft:jungle_log");
/*  50 */         map.put("minecraft:log2.0", "minecraft:acacia_log");
/*  51 */         map.put("minecraft:log2.1", "minecraft:dark_oak_log");
/*  52 */         map.put("minecraft:sapling.0", "minecraft:oak_sapling");
/*  53 */         map.put("minecraft:sapling.1", "minecraft:spruce_sapling");
/*  54 */         map.put("minecraft:sapling.2", "minecraft:birch_sapling");
/*  55 */         map.put("minecraft:sapling.3", "minecraft:jungle_sapling");
/*  56 */         map.put("minecraft:sapling.4", "minecraft:acacia_sapling");
/*  57 */         map.put("minecraft:sapling.5", "minecraft:dark_oak_sapling");
/*  58 */         map.put("minecraft:planks.0", "minecraft:oak_planks");
/*  59 */         map.put("minecraft:planks.1", "minecraft:spruce_planks");
/*  60 */         map.put("minecraft:planks.2", "minecraft:birch_planks");
/*  61 */         map.put("minecraft:planks.3", "minecraft:jungle_planks");
/*  62 */         map.put("minecraft:planks.4", "minecraft:acacia_planks");
/*  63 */         map.put("minecraft:planks.5", "minecraft:dark_oak_planks");
/*  64 */         map.put("minecraft:sand.0", "minecraft:sand");
/*  65 */         map.put("minecraft:sand.1", "minecraft:red_sand");
/*  66 */         map.put("minecraft:quartz_block.0", "minecraft:quartz_block");
/*  67 */         map.put("minecraft:quartz_block.1", "minecraft:chiseled_quartz_block");
/*  68 */         map.put("minecraft:quartz_block.2", "minecraft:quartz_pillar");
/*  69 */         map.put("minecraft:anvil.0", "minecraft:anvil");
/*  70 */         map.put("minecraft:anvil.1", "minecraft:chipped_anvil");
/*  71 */         map.put("minecraft:anvil.2", "minecraft:damaged_anvil");
/*  72 */         map.put("minecraft:wool.0", "minecraft:white_wool");
/*  73 */         map.put("minecraft:wool.1", "minecraft:orange_wool");
/*  74 */         map.put("minecraft:wool.2", "minecraft:magenta_wool");
/*  75 */         map.put("minecraft:wool.3", "minecraft:light_blue_wool");
/*  76 */         map.put("minecraft:wool.4", "minecraft:yellow_wool");
/*  77 */         map.put("minecraft:wool.5", "minecraft:lime_wool");
/*  78 */         map.put("minecraft:wool.6", "minecraft:pink_wool");
/*  79 */         map.put("minecraft:wool.7", "minecraft:gray_wool");
/*  80 */         map.put("minecraft:wool.8", "minecraft:light_gray_wool");
/*  81 */         map.put("minecraft:wool.9", "minecraft:cyan_wool");
/*  82 */         map.put("minecraft:wool.10", "minecraft:purple_wool");
/*  83 */         map.put("minecraft:wool.11", "minecraft:blue_wool");
/*  84 */         map.put("minecraft:wool.12", "minecraft:brown_wool");
/*  85 */         map.put("minecraft:wool.13", "minecraft:green_wool");
/*  86 */         map.put("minecraft:wool.14", "minecraft:red_wool");
/*  87 */         map.put("minecraft:wool.15", "minecraft:black_wool");
/*  88 */         map.put("minecraft:carpet.0", "minecraft:white_carpet");
/*  89 */         map.put("minecraft:carpet.1", "minecraft:orange_carpet");
/*  90 */         map.put("minecraft:carpet.2", "minecraft:magenta_carpet");
/*  91 */         map.put("minecraft:carpet.3", "minecraft:light_blue_carpet");
/*  92 */         map.put("minecraft:carpet.4", "minecraft:yellow_carpet");
/*  93 */         map.put("minecraft:carpet.5", "minecraft:lime_carpet");
/*  94 */         map.put("minecraft:carpet.6", "minecraft:pink_carpet");
/*  95 */         map.put("minecraft:carpet.7", "minecraft:gray_carpet");
/*  96 */         map.put("minecraft:carpet.8", "minecraft:light_gray_carpet");
/*  97 */         map.put("minecraft:carpet.9", "minecraft:cyan_carpet");
/*  98 */         map.put("minecraft:carpet.10", "minecraft:purple_carpet");
/*  99 */         map.put("minecraft:carpet.11", "minecraft:blue_carpet");
/* 100 */         map.put("minecraft:carpet.12", "minecraft:brown_carpet");
/* 101 */         map.put("minecraft:carpet.13", "minecraft:green_carpet");
/* 102 */         map.put("minecraft:carpet.14", "minecraft:red_carpet");
/* 103 */         map.put("minecraft:carpet.15", "minecraft:black_carpet");
/* 104 */         map.put("minecraft:hardened_clay.0", "minecraft:terracotta");
/* 105 */         map.put("minecraft:stained_hardened_clay.0", "minecraft:white_terracotta");
/* 106 */         map.put("minecraft:stained_hardened_clay.1", "minecraft:orange_terracotta");
/* 107 */         map.put("minecraft:stained_hardened_clay.2", "minecraft:magenta_terracotta");
/* 108 */         map.put("minecraft:stained_hardened_clay.3", "minecraft:light_blue_terracotta");
/* 109 */         map.put("minecraft:stained_hardened_clay.4", "minecraft:yellow_terracotta");
/* 110 */         map.put("minecraft:stained_hardened_clay.5", "minecraft:lime_terracotta");
/* 111 */         map.put("minecraft:stained_hardened_clay.6", "minecraft:pink_terracotta");
/* 112 */         map.put("minecraft:stained_hardened_clay.7", "minecraft:gray_terracotta");
/* 113 */         map.put("minecraft:stained_hardened_clay.8", "minecraft:light_gray_terracotta");
/* 114 */         map.put("minecraft:stained_hardened_clay.9", "minecraft:cyan_terracotta");
/* 115 */         map.put("minecraft:stained_hardened_clay.10", "minecraft:purple_terracotta");
/* 116 */         map.put("minecraft:stained_hardened_clay.11", "minecraft:blue_terracotta");
/* 117 */         map.put("minecraft:stained_hardened_clay.12", "minecraft:brown_terracotta");
/* 118 */         map.put("minecraft:stained_hardened_clay.13", "minecraft:green_terracotta");
/* 119 */         map.put("minecraft:stained_hardened_clay.14", "minecraft:red_terracotta");
/* 120 */         map.put("minecraft:stained_hardened_clay.15", "minecraft:black_terracotta");
/* 121 */         map.put("minecraft:silver_glazed_terracotta.0", "minecraft:light_gray_glazed_terracotta");
/* 122 */         map.put("minecraft:stained_glass.0", "minecraft:white_stained_glass");
/* 123 */         map.put("minecraft:stained_glass.1", "minecraft:orange_stained_glass");
/* 124 */         map.put("minecraft:stained_glass.2", "minecraft:magenta_stained_glass");
/* 125 */         map.put("minecraft:stained_glass.3", "minecraft:light_blue_stained_glass");
/* 126 */         map.put("minecraft:stained_glass.4", "minecraft:yellow_stained_glass");
/* 127 */         map.put("minecraft:stained_glass.5", "minecraft:lime_stained_glass");
/* 128 */         map.put("minecraft:stained_glass.6", "minecraft:pink_stained_glass");
/* 129 */         map.put("minecraft:stained_glass.7", "minecraft:gray_stained_glass");
/* 130 */         map.put("minecraft:stained_glass.8", "minecraft:light_gray_stained_glass");
/* 131 */         map.put("minecraft:stained_glass.9", "minecraft:cyan_stained_glass");
/* 132 */         map.put("minecraft:stained_glass.10", "minecraft:purple_stained_glass");
/* 133 */         map.put("minecraft:stained_glass.11", "minecraft:blue_stained_glass");
/* 134 */         map.put("minecraft:stained_glass.12", "minecraft:brown_stained_glass");
/* 135 */         map.put("minecraft:stained_glass.13", "minecraft:green_stained_glass");
/* 136 */         map.put("minecraft:stained_glass.14", "minecraft:red_stained_glass");
/* 137 */         map.put("minecraft:stained_glass.15", "minecraft:black_stained_glass");
/* 138 */         map.put("minecraft:stained_glass_pane.0", "minecraft:white_stained_glass_pane");
/* 139 */         map.put("minecraft:stained_glass_pane.1", "minecraft:orange_stained_glass_pane");
/* 140 */         map.put("minecraft:stained_glass_pane.2", "minecraft:magenta_stained_glass_pane");
/* 141 */         map.put("minecraft:stained_glass_pane.3", "minecraft:light_blue_stained_glass_pane");
/* 142 */         map.put("minecraft:stained_glass_pane.4", "minecraft:yellow_stained_glass_pane");
/* 143 */         map.put("minecraft:stained_glass_pane.5", "minecraft:lime_stained_glass_pane");
/* 144 */         map.put("minecraft:stained_glass_pane.6", "minecraft:pink_stained_glass_pane");
/* 145 */         map.put("minecraft:stained_glass_pane.7", "minecraft:gray_stained_glass_pane");
/* 146 */         map.put("minecraft:stained_glass_pane.8", "minecraft:light_gray_stained_glass_pane");
/* 147 */         map.put("minecraft:stained_glass_pane.9", "minecraft:cyan_stained_glass_pane");
/* 148 */         map.put("minecraft:stained_glass_pane.10", "minecraft:purple_stained_glass_pane");
/* 149 */         map.put("minecraft:stained_glass_pane.11", "minecraft:blue_stained_glass_pane");
/* 150 */         map.put("minecraft:stained_glass_pane.12", "minecraft:brown_stained_glass_pane");
/* 151 */         map.put("minecraft:stained_glass_pane.13", "minecraft:green_stained_glass_pane");
/* 152 */         map.put("minecraft:stained_glass_pane.14", "minecraft:red_stained_glass_pane");
/* 153 */         map.put("minecraft:stained_glass_pane.15", "minecraft:black_stained_glass_pane");
/* 154 */         map.put("minecraft:prismarine.0", "minecraft:prismarine");
/* 155 */         map.put("minecraft:prismarine.1", "minecraft:prismarine_bricks");
/* 156 */         map.put("minecraft:prismarine.2", "minecraft:dark_prismarine");
/* 157 */         map.put("minecraft:concrete.0", "minecraft:white_concrete");
/* 158 */         map.put("minecraft:concrete.1", "minecraft:orange_concrete");
/* 159 */         map.put("minecraft:concrete.2", "minecraft:magenta_concrete");
/* 160 */         map.put("minecraft:concrete.3", "minecraft:light_blue_concrete");
/* 161 */         map.put("minecraft:concrete.4", "minecraft:yellow_concrete");
/* 162 */         map.put("minecraft:concrete.5", "minecraft:lime_concrete");
/* 163 */         map.put("minecraft:concrete.6", "minecraft:pink_concrete");
/* 164 */         map.put("minecraft:concrete.7", "minecraft:gray_concrete");
/* 165 */         map.put("minecraft:concrete.8", "minecraft:light_gray_concrete");
/* 166 */         map.put("minecraft:concrete.9", "minecraft:cyan_concrete");
/* 167 */         map.put("minecraft:concrete.10", "minecraft:purple_concrete");
/* 168 */         map.put("minecraft:concrete.11", "minecraft:blue_concrete");
/* 169 */         map.put("minecraft:concrete.12", "minecraft:brown_concrete");
/* 170 */         map.put("minecraft:concrete.13", "minecraft:green_concrete");
/* 171 */         map.put("minecraft:concrete.14", "minecraft:red_concrete");
/* 172 */         map.put("minecraft:concrete.15", "minecraft:black_concrete");
/* 173 */         map.put("minecraft:concrete_powder.0", "minecraft:white_concrete_powder");
/* 174 */         map.put("minecraft:concrete_powder.1", "minecraft:orange_concrete_powder");
/* 175 */         map.put("minecraft:concrete_powder.2", "minecraft:magenta_concrete_powder");
/* 176 */         map.put("minecraft:concrete_powder.3", "minecraft:light_blue_concrete_powder");
/* 177 */         map.put("minecraft:concrete_powder.4", "minecraft:yellow_concrete_powder");
/* 178 */         map.put("minecraft:concrete_powder.5", "minecraft:lime_concrete_powder");
/* 179 */         map.put("minecraft:concrete_powder.6", "minecraft:pink_concrete_powder");
/* 180 */         map.put("minecraft:concrete_powder.7", "minecraft:gray_concrete_powder");
/* 181 */         map.put("minecraft:concrete_powder.8", "minecraft:light_gray_concrete_powder");
/* 182 */         map.put("minecraft:concrete_powder.9", "minecraft:cyan_concrete_powder");
/* 183 */         map.put("minecraft:concrete_powder.10", "minecraft:purple_concrete_powder");
/* 184 */         map.put("minecraft:concrete_powder.11", "minecraft:blue_concrete_powder");
/* 185 */         map.put("minecraft:concrete_powder.12", "minecraft:brown_concrete_powder");
/* 186 */         map.put("minecraft:concrete_powder.13", "minecraft:green_concrete_powder");
/* 187 */         map.put("minecraft:concrete_powder.14", "minecraft:red_concrete_powder");
/* 188 */         map.put("minecraft:concrete_powder.15", "minecraft:black_concrete_powder");
/* 189 */         map.put("minecraft:cobblestone_wall.0", "minecraft:cobblestone_wall");
/* 190 */         map.put("minecraft:cobblestone_wall.1", "minecraft:mossy_cobblestone_wall");
/* 191 */         map.put("minecraft:sandstone.0", "minecraft:sandstone");
/* 192 */         map.put("minecraft:sandstone.1", "minecraft:chiseled_sandstone");
/* 193 */         map.put("minecraft:sandstone.2", "minecraft:cut_sandstone");
/* 194 */         map.put("minecraft:red_sandstone.0", "minecraft:red_sandstone");
/* 195 */         map.put("minecraft:red_sandstone.1", "minecraft:chiseled_red_sandstone");
/* 196 */         map.put("minecraft:red_sandstone.2", "minecraft:cut_red_sandstone");
/* 197 */         map.put("minecraft:stonebrick.0", "minecraft:stone_bricks");
/* 198 */         map.put("minecraft:stonebrick.1", "minecraft:mossy_stone_bricks");
/* 199 */         map.put("minecraft:stonebrick.2", "minecraft:cracked_stone_bricks");
/* 200 */         map.put("minecraft:stonebrick.3", "minecraft:chiseled_stone_bricks");
/* 201 */         map.put("minecraft:monster_egg.0", "minecraft:infested_stone");
/* 202 */         map.put("minecraft:monster_egg.1", "minecraft:infested_cobblestone");
/* 203 */         map.put("minecraft:monster_egg.2", "minecraft:infested_stone_bricks");
/* 204 */         map.put("minecraft:monster_egg.3", "minecraft:infested_mossy_stone_bricks");
/* 205 */         map.put("minecraft:monster_egg.4", "minecraft:infested_cracked_stone_bricks");
/* 206 */         map.put("minecraft:monster_egg.5", "minecraft:infested_chiseled_stone_bricks");
/* 207 */         map.put("minecraft:yellow_flower.0", "minecraft:dandelion");
/* 208 */         map.put("minecraft:red_flower.0", "minecraft:poppy");
/* 209 */         map.put("minecraft:red_flower.1", "minecraft:blue_orchid");
/* 210 */         map.put("minecraft:red_flower.2", "minecraft:allium");
/* 211 */         map.put("minecraft:red_flower.3", "minecraft:azure_bluet");
/* 212 */         map.put("minecraft:red_flower.4", "minecraft:red_tulip");
/* 213 */         map.put("minecraft:red_flower.5", "minecraft:orange_tulip");
/* 214 */         map.put("minecraft:red_flower.6", "minecraft:white_tulip");
/* 215 */         map.put("minecraft:red_flower.7", "minecraft:pink_tulip");
/* 216 */         map.put("minecraft:red_flower.8", "minecraft:oxeye_daisy");
/* 217 */         map.put("minecraft:double_plant.0", "minecraft:sunflower");
/* 218 */         map.put("minecraft:double_plant.1", "minecraft:lilac");
/* 219 */         map.put("minecraft:double_plant.2", "minecraft:tall_grass");
/* 220 */         map.put("minecraft:double_plant.3", "minecraft:large_fern");
/* 221 */         map.put("minecraft:double_plant.4", "minecraft:rose_bush");
/* 222 */         map.put("minecraft:double_plant.5", "minecraft:peony");
/* 223 */         map.put("minecraft:deadbush.0", "minecraft:dead_bush");
/* 224 */         map.put("minecraft:tallgrass.0", "minecraft:dead_bush");
/* 225 */         map.put("minecraft:tallgrass.1", "minecraft:grass");
/* 226 */         map.put("minecraft:tallgrass.2", "minecraft:fern");
/* 227 */         map.put("minecraft:sponge.0", "minecraft:sponge");
/* 228 */         map.put("minecraft:sponge.1", "minecraft:wet_sponge");
/* 229 */         map.put("minecraft:purpur_slab.0", "minecraft:purpur_slab");
/* 230 */         map.put("minecraft:stone_slab.0", "minecraft:stone_slab");
/* 231 */         map.put("minecraft:stone_slab.1", "minecraft:sandstone_slab");
/* 232 */         map.put("minecraft:stone_slab.2", "minecraft:petrified_oak_slab");
/* 233 */         map.put("minecraft:stone_slab.3", "minecraft:cobblestone_slab");
/* 234 */         map.put("minecraft:stone_slab.4", "minecraft:brick_slab");
/* 235 */         map.put("minecraft:stone_slab.5", "minecraft:stone_brick_slab");
/* 236 */         map.put("minecraft:stone_slab.6", "minecraft:nether_brick_slab");
/* 237 */         map.put("minecraft:stone_slab.7", "minecraft:quartz_slab");
/* 238 */         map.put("minecraft:stone_slab2.0", "minecraft:red_sandstone_slab");
/* 239 */         map.put("minecraft:wooden_slab.0", "minecraft:oak_slab");
/* 240 */         map.put("minecraft:wooden_slab.1", "minecraft:spruce_slab");
/* 241 */         map.put("minecraft:wooden_slab.2", "minecraft:birch_slab");
/* 242 */         map.put("minecraft:wooden_slab.3", "minecraft:jungle_slab");
/* 243 */         map.put("minecraft:wooden_slab.4", "minecraft:acacia_slab");
/* 244 */         map.put("minecraft:wooden_slab.5", "minecraft:dark_oak_slab");
/* 245 */         map.put("minecraft:coal.0", "minecraft:coal");
/* 246 */         map.put("minecraft:coal.1", "minecraft:charcoal");
/* 247 */         map.put("minecraft:fish.0", "minecraft:cod");
/* 248 */         map.put("minecraft:fish.1", "minecraft:salmon");
/* 249 */         map.put("minecraft:fish.2", "minecraft:clownfish");
/* 250 */         map.put("minecraft:fish.3", "minecraft:pufferfish");
/* 251 */         map.put("minecraft:cooked_fish.0", "minecraft:cooked_cod");
/* 252 */         map.put("minecraft:cooked_fish.1", "minecraft:cooked_salmon");
/* 253 */         map.put("minecraft:skull.0", "minecraft:skeleton_skull");
/* 254 */         map.put("minecraft:skull.1", "minecraft:wither_skeleton_skull");
/* 255 */         map.put("minecraft:skull.2", "minecraft:zombie_head");
/* 256 */         map.put("minecraft:skull.3", "minecraft:player_head");
/* 257 */         map.put("minecraft:skull.4", "minecraft:creeper_head");
/* 258 */         map.put("minecraft:skull.5", "minecraft:dragon_head");
/* 259 */         map.put("minecraft:golden_apple.0", "minecraft:golden_apple");
/* 260 */         map.put("minecraft:golden_apple.1", "minecraft:enchanted_golden_apple");
/* 261 */         map.put("minecraft:fireworks.0", "minecraft:firework_rocket");
/* 262 */         map.put("minecraft:firework_charge.0", "minecraft:firework_star");
/* 263 */         map.put("minecraft:dye.0", "minecraft:ink_sac");
/* 264 */         map.put("minecraft:dye.1", "minecraft:rose_red");
/* 265 */         map.put("minecraft:dye.2", "minecraft:cactus_green");
/* 266 */         map.put("minecraft:dye.3", "minecraft:cocoa_beans");
/* 267 */         map.put("minecraft:dye.4", "minecraft:lapis_lazuli");
/* 268 */         map.put("minecraft:dye.5", "minecraft:purple_dye");
/* 269 */         map.put("minecraft:dye.6", "minecraft:cyan_dye");
/* 270 */         map.put("minecraft:dye.7", "minecraft:light_gray_dye");
/* 271 */         map.put("minecraft:dye.8", "minecraft:gray_dye");
/* 272 */         map.put("minecraft:dye.9", "minecraft:pink_dye");
/* 273 */         map.put("minecraft:dye.10", "minecraft:lime_dye");
/* 274 */         map.put("minecraft:dye.11", "minecraft:dandelion_yellow");
/* 275 */         map.put("minecraft:dye.12", "minecraft:light_blue_dye");
/* 276 */         map.put("minecraft:dye.13", "minecraft:magenta_dye");
/* 277 */         map.put("minecraft:dye.14", "minecraft:orange_dye");
/* 278 */         map.put("minecraft:dye.15", "minecraft:bone_meal");
/* 279 */         map.put("minecraft:silver_shulker_box.0", "minecraft:light_gray_shulker_box");
/* 280 */         map.put("minecraft:fence.0", "minecraft:oak_fence");
/* 281 */         map.put("minecraft:fence_gate.0", "minecraft:oak_fence_gate");
/* 282 */         map.put("minecraft:wooden_door.0", "minecraft:oak_door");
/* 283 */         map.put("minecraft:boat.0", "minecraft:oak_boat");
/* 284 */         map.put("minecraft:lit_pumpkin.0", "minecraft:jack_o_lantern");
/* 285 */         map.put("minecraft:pumpkin.0", "minecraft:carved_pumpkin");
/* 286 */         map.put("minecraft:trapdoor.0", "minecraft:oak_trapdoor");
/* 287 */         map.put("minecraft:nether_brick.0", "minecraft:nether_bricks");
/* 288 */         map.put("minecraft:red_nether_brick.0", "minecraft:red_nether_bricks");
/* 289 */         map.put("minecraft:netherbrick.0", "minecraft:nether_brick");
/* 290 */         map.put("minecraft:wooden_button.0", "minecraft:oak_button");
/* 291 */         map.put("minecraft:wooden_pressure_plate.0", "minecraft:oak_pressure_plate");
/* 292 */         map.put("minecraft:noteblock.0", "minecraft:note_block");
/*     */         
/* 294 */         map.put("minecraft:bed.0", "minecraft:white_bed");
/* 295 */         map.put("minecraft:bed.1", "minecraft:orange_bed");
/* 296 */         map.put("minecraft:bed.2", "minecraft:magenta_bed");
/* 297 */         map.put("minecraft:bed.3", "minecraft:light_blue_bed");
/* 298 */         map.put("minecraft:bed.4", "minecraft:yellow_bed");
/* 299 */         map.put("minecraft:bed.5", "minecraft:lime_bed");
/* 300 */         map.put("minecraft:bed.6", "minecraft:pink_bed");
/* 301 */         map.put("minecraft:bed.7", "minecraft:gray_bed");
/* 302 */         map.put("minecraft:bed.8", "minecraft:light_gray_bed");
/* 303 */         map.put("minecraft:bed.9", "minecraft:cyan_bed");
/* 304 */         map.put("minecraft:bed.10", "minecraft:purple_bed");
/* 305 */         map.put("minecraft:bed.11", "minecraft:blue_bed");
/* 306 */         map.put("minecraft:bed.12", "minecraft:brown_bed");
/* 307 */         map.put("minecraft:bed.13", "minecraft:green_bed");
/* 308 */         map.put("minecraft:bed.14", "minecraft:red_bed");
/* 309 */         map.put("minecraft:bed.15", "minecraft:black_bed");
/* 310 */         map.put("minecraft:banner.15", "minecraft:white_banner");
/* 311 */         map.put("minecraft:banner.14", "minecraft:orange_banner");
/* 312 */         map.put("minecraft:banner.13", "minecraft:magenta_banner");
/* 313 */         map.put("minecraft:banner.12", "minecraft:light_blue_banner");
/* 314 */         map.put("minecraft:banner.11", "minecraft:yellow_banner");
/* 315 */         map.put("minecraft:banner.10", "minecraft:lime_banner");
/* 316 */         map.put("minecraft:banner.9", "minecraft:pink_banner");
/* 317 */         map.put("minecraft:banner.8", "minecraft:gray_banner");
/* 318 */         map.put("minecraft:banner.7", "minecraft:light_gray_banner");
/* 319 */         map.put("minecraft:banner.6", "minecraft:cyan_banner");
/* 320 */         map.put("minecraft:banner.5", "minecraft:purple_banner");
/* 321 */         map.put("minecraft:banner.4", "minecraft:blue_banner");
/* 322 */         map.put("minecraft:banner.3", "minecraft:brown_banner");
/* 323 */         map.put("minecraft:banner.2", "minecraft:green_banner");
/* 324 */         map.put("minecraft:banner.1", "minecraft:red_banner");
/* 325 */         map.put("minecraft:banner.0", "minecraft:black_banner");
/* 326 */         map.put("minecraft:grass.0", "minecraft:grass_block");
/* 327 */         map.put("minecraft:brick_block.0", "minecraft:bricks");
/* 328 */         map.put("minecraft:end_bricks.0", "minecraft:end_stone_bricks");
/* 329 */         map.put("minecraft:golden_rail.0", "minecraft:powered_rail");
/* 330 */         map.put("minecraft:magma.0", "minecraft:magma_block");
/* 331 */         map.put("minecraft:quartz_ore.0", "minecraft:nether_quartz_ore");
/* 332 */         map.put("minecraft:reeds.0", "minecraft:sugar_cane");
/* 333 */         map.put("minecraft:slime.0", "minecraft:slime_block");
/* 334 */         map.put("minecraft:stone_stairs.0", "minecraft:cobblestone_stairs");
/* 335 */         map.put("minecraft:waterlily.0", "minecraft:lily_pad");
/* 336 */         map.put("minecraft:web.0", "minecraft:cobweb");
/* 337 */         map.put("minecraft:snow.0", "minecraft:snow_block");
/* 338 */         map.put("minecraft:snow_layer.0", "minecraft:snow");
/* 339 */         map.put("minecraft:record_11.0", "minecraft:music_disc_11");
/* 340 */         map.put("minecraft:record_13.0", "minecraft:music_disc_13");
/* 341 */         map.put("minecraft:record_blocks.0", "minecraft:music_disc_blocks");
/* 342 */         map.put("minecraft:record_cat.0", "minecraft:music_disc_cat");
/* 343 */         map.put("minecraft:record_chirp.0", "minecraft:music_disc_chirp");
/* 344 */         map.put("minecraft:record_far.0", "minecraft:music_disc_far");
/* 345 */         map.put("minecraft:record_mall.0", "minecraft:music_disc_mall");
/* 346 */         map.put("minecraft:record_mellohi.0", "minecraft:music_disc_mellohi");
/* 347 */         map.put("minecraft:record_stal.0", "minecraft:music_disc_stal");
/* 348 */         map.put("minecraft:record_strad.0", "minecraft:music_disc_strad");
/* 349 */         map.put("minecraft:record_wait.0", "minecraft:music_disc_wait");
/* 350 */         map.put("minecraft:record_ward.0", "minecraft:music_disc_ward");
/*     */       });
/*     */   
/* 353 */   private static final Set<String> IDS = (Set)MAP.keySet().stream().map(k -> k.substring(0, k.indexOf('.'))).collect(Collectors.toSet());
/*     */   
/* 355 */   private static final Set<String> DAMAGE_IDS = Sets.newHashSet(new String[] { "minecraft:bow", "minecraft:carrot_on_a_stick", "minecraft:chainmail_boots", "minecraft:chainmail_chestplate", "minecraft:chainmail_helmet", "minecraft:chainmail_leggings", "minecraft:diamond_axe", "minecraft:diamond_boots", "minecraft:diamond_chestplate", "minecraft:diamond_helmet", "minecraft:diamond_hoe", "minecraft:diamond_leggings", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel", "minecraft:diamond_sword", "minecraft:elytra", "minecraft:fishing_rod", "minecraft:flint_and_steel", "minecraft:golden_axe", "minecraft:golden_boots", "minecraft:golden_chestplate", "minecraft:golden_helmet", "minecraft:golden_hoe", "minecraft:golden_leggings", "minecraft:golden_pickaxe", "minecraft:golden_shovel", "minecraft:golden_sword", "minecraft:iron_axe", "minecraft:iron_boots", "minecraft:iron_chestplate", "minecraft:iron_helmet", "minecraft:iron_hoe", "minecraft:iron_leggings", "minecraft:iron_pickaxe", "minecraft:iron_shovel", "minecraft:iron_sword", "minecraft:leather_boots", "minecraft:leather_chestplate", "minecraft:leather_helmet", "minecraft:leather_leggings", "minecraft:shears", "minecraft:shield", "minecraft:stone_axe", "minecraft:stone_hoe", "minecraft:stone_pickaxe", "minecraft:stone_shovel", "minecraft:stone_sword", "minecraft:wooden_axe", "minecraft:wooden_hoe", "minecraft:wooden_pickaxe", "minecraft:wooden_shovel", "minecraft:wooden_sword" });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 412 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*     */     
/* 414 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 415 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/*     */     
/* 417 */     return fixTypeEverywhereTyped("ItemInstanceTheFlatteningFix", itemStackType, input -> {
/* 418 */           Optional<Pair<String, String>> id = input.getOptional(idF);
/* 419 */           if (id.isEmpty()) {
/* 420 */             return input;
/*     */           }
/* 422 */           output = input;
/* 423 */           Dynamic<?> rest = (Dynamic)input.get(DSL.remainderFinder());
/* 424 */           int data = rest.get("Damage").asInt(0);
/*     */           
/* 426 */           String newValue = updateItem((String)((Pair)id.get()).getSecond(), data);
/* 427 */           if (newValue != null) {
/* 428 */             output = output.set(idF, Pair.of(References.ITEM_NAME.typeName(), newValue));
/*     */           }
/*     */           
/* 431 */           if (DAMAGE_IDS.contains(((Pair)id.get()).getSecond())) {
/* 432 */             Typed<?> tag = input.getOrCreateTyped(tagF);
/* 433 */             Dynamic<?> tagRest = (Dynamic)tag.get(DSL.remainderFinder());
/* 434 */             tagRest = tagRest.set("Damage", tagRest.createInt(data));
/* 435 */             output = output.set(tagF, tag.set(DSL.remainderFinder(), tagRest));
/*     */           } 
/*     */           
/* 438 */           return output.set(DSL.remainderFinder(), rest.remove("Damage"));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String updateItem(String name, int data) {
/* 445 */     if (IDS.contains(name)) {
/* 446 */       String newName = (String)MAP.get(name + "." + name);
/* 447 */       return (newName == null) ? (String)MAP.get(name + ".0") : newName;
/*     */     } 
/* 449 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackTheFlatteningFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */