/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
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
/*     */ class MappingConstants
/*     */ {
/*  53 */   private static final BitSet VIRTUAL = new BitSet(256);
/*  54 */   private static final BitSet FIX = new BitSet(256);
/*  55 */   private static final Dynamic<?> PUMPKIN = ExtraDataFixUtils.blockState("minecraft:pumpkin");
/*  56 */   private static final Dynamic<?> SNOWY_PODZOL = ExtraDataFixUtils.blockState("minecraft:podzol", Map.of("snowy", "true"));
/*  57 */   private static final Dynamic<?> SNOWY_GRASS = ExtraDataFixUtils.blockState("minecraft:grass_block", Map.of("snowy", "true"));
/*  58 */   private static final Dynamic<?> SNOWY_MYCELIUM = ExtraDataFixUtils.blockState("minecraft:mycelium", Map.of("snowy", "true"));
/*  59 */   private static final Dynamic<?> UPPER_SUNFLOWER = ExtraDataFixUtils.blockState("minecraft:sunflower", Map.of("half", "upper"));
/*  60 */   private static final Dynamic<?> UPPER_LILAC = ExtraDataFixUtils.blockState("minecraft:lilac", Map.of("half", "upper"));
/*  61 */   private static final Dynamic<?> UPPER_TALL_GRASS = ExtraDataFixUtils.blockState("minecraft:tall_grass", Map.of("half", "upper"));
/*  62 */   private static final Dynamic<?> UPPER_LARGE_FERN = ExtraDataFixUtils.blockState("minecraft:large_fern", Map.of("half", "upper"));
/*  63 */   private static final Dynamic<?> UPPER_ROSE_BUSH = ExtraDataFixUtils.blockState("minecraft:rose_bush", Map.of("half", "upper"));
/*  64 */   private static final Dynamic<?> UPPER_PEONY = ExtraDataFixUtils.blockState("minecraft:peony", Map.of("half", "upper"));
/*     */   
/*  66 */   private static final Map<String, Dynamic<?>> FLOWER_POT_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/*  67 */         map.put("minecraft:air0", ExtraDataFixUtils.blockState("minecraft:flower_pot"));
/*  68 */         map.put("minecraft:red_flower0", ExtraDataFixUtils.blockState("minecraft:potted_poppy"));
/*  69 */         map.put("minecraft:red_flower1", ExtraDataFixUtils.blockState("minecraft:potted_blue_orchid"));
/*  70 */         map.put("minecraft:red_flower2", ExtraDataFixUtils.blockState("minecraft:potted_allium"));
/*  71 */         map.put("minecraft:red_flower3", ExtraDataFixUtils.blockState("minecraft:potted_azure_bluet"));
/*  72 */         map.put("minecraft:red_flower4", ExtraDataFixUtils.blockState("minecraft:potted_red_tulip"));
/*  73 */         map.put("minecraft:red_flower5", ExtraDataFixUtils.blockState("minecraft:potted_orange_tulip"));
/*  74 */         map.put("minecraft:red_flower6", ExtraDataFixUtils.blockState("minecraft:potted_white_tulip"));
/*  75 */         map.put("minecraft:red_flower7", ExtraDataFixUtils.blockState("minecraft:potted_pink_tulip"));
/*  76 */         map.put("minecraft:red_flower8", ExtraDataFixUtils.blockState("minecraft:potted_oxeye_daisy"));
/*  77 */         map.put("minecraft:yellow_flower0", ExtraDataFixUtils.blockState("minecraft:potted_dandelion"));
/*  78 */         map.put("minecraft:sapling0", ExtraDataFixUtils.blockState("minecraft:potted_oak_sapling"));
/*  79 */         map.put("minecraft:sapling1", ExtraDataFixUtils.blockState("minecraft:potted_spruce_sapling"));
/*  80 */         map.put("minecraft:sapling2", ExtraDataFixUtils.blockState("minecraft:potted_birch_sapling"));
/*  81 */         map.put("minecraft:sapling3", ExtraDataFixUtils.blockState("minecraft:potted_jungle_sapling"));
/*  82 */         map.put("minecraft:sapling4", ExtraDataFixUtils.blockState("minecraft:potted_acacia_sapling"));
/*  83 */         map.put("minecraft:sapling5", ExtraDataFixUtils.blockState("minecraft:potted_dark_oak_sapling"));
/*  84 */         map.put("minecraft:red_mushroom0", ExtraDataFixUtils.blockState("minecraft:potted_red_mushroom"));
/*  85 */         map.put("minecraft:brown_mushroom0", ExtraDataFixUtils.blockState("minecraft:potted_brown_mushroom"));
/*  86 */         map.put("minecraft:deadbush0", ExtraDataFixUtils.blockState("minecraft:potted_dead_bush"));
/*  87 */         map.put("minecraft:tallgrass2", ExtraDataFixUtils.blockState("minecraft:potted_fern"));
/*  88 */         map.put("minecraft:cactus0", ExtraDataFixUtils.blockState("minecraft:potted_cactus"));
/*     */       });
/*     */   
/*  91 */   private static final Map<String, Dynamic<?>> SKULL_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/*  92 */         mapSkull(map, 0, "skeleton", "skull");
/*  93 */         mapSkull(map, 1, "wither_skeleton", "skull");
/*  94 */         mapSkull(map, 2, "zombie", "head");
/*  95 */         mapSkull(map, 3, "player", "head");
/*  96 */         mapSkull(map, 4, "creeper", "head");
/*  97 */         mapSkull(map, 5, "dragon", "head");
/*     */       });
/*     */   
/*     */   private static void mapSkull(Map<String, Dynamic<?>> map, int i, String name, String type) {
/* 101 */     map.put("" + i + "north", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "north")));
/* 102 */     map.put("" + i + "east", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "east")));
/* 103 */     map.put("" + i + "south", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "south")));
/* 104 */     map.put("" + i + "west", ExtraDataFixUtils.blockState("minecraft:" + name + "_wall_" + type, Map.of("facing", "west")));
/* 105 */     for (int rot = 0; rot < 16; rot++) {
/* 106 */       map.put("" + i + i, ExtraDataFixUtils.blockState("minecraft:" + name + "_" + type, Map.of("rotation", String.valueOf(rot))));
/*     */     }
/*     */   }
/*     */   
/* 110 */   private static final Map<String, Dynamic<?>> DOOR_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/* 111 */         mapDoor(map, "oak_door");
/* 112 */         mapDoor(map, "iron_door");
/* 113 */         mapDoor(map, "spruce_door");
/* 114 */         mapDoor(map, "birch_door");
/* 115 */         mapDoor(map, "jungle_door");
/* 116 */         mapDoor(map, "acacia_door");
/* 117 */         mapDoor(map, "dark_oak_door");
/*     */       });
/*     */   
/*     */   private static void mapDoor(Map<String, Dynamic<?>> map, String type) {
/* 121 */     String id = "minecraft:" + type;
/* 122 */     map.put("minecraft:" + type + "eastlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/* 123 */     map.put("minecraft:" + type + "eastlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/* 124 */     map.put("minecraft:" + type + "eastlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/* 125 */     map.put("minecraft:" + type + "eastlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/* 126 */     map.put("minecraft:" + type + "eastlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/* 127 */     map.put("minecraft:" + type + "eastlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/* 128 */     map.put("minecraft:" + type + "eastlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/* 129 */     map.put("minecraft:" + type + "eastlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/* 130 */     map.put("minecraft:" + type + "eastupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/* 131 */     map.put("minecraft:" + type + "eastupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/* 132 */     map.put("minecraft:" + type + "eastupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/* 133 */     map.put("minecraft:" + type + "eastupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/* 134 */     map.put("minecraft:" + type + "eastupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/* 135 */     map.put("minecraft:" + type + "eastupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/* 136 */     map.put("minecraft:" + type + "eastupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/* 137 */     map.put("minecraft:" + type + "eastupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "east", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/* 138 */     map.put("minecraft:" + type + "northlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/* 139 */     map.put("minecraft:" + type + "northlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/* 140 */     map.put("minecraft:" + type + "northlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/* 141 */     map.put("minecraft:" + type + "northlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/* 142 */     map.put("minecraft:" + type + "northlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/* 143 */     map.put("minecraft:" + type + "northlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/* 144 */     map.put("minecraft:" + type + "northlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/* 145 */     map.put("minecraft:" + type + "northlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/* 146 */     map.put("minecraft:" + type + "northupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/* 147 */     map.put("minecraft:" + type + "northupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/* 148 */     map.put("minecraft:" + type + "northupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/* 149 */     map.put("minecraft:" + type + "northupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/* 150 */     map.put("minecraft:" + type + "northupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/* 151 */     map.put("minecraft:" + type + "northupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/* 152 */     map.put("minecraft:" + type + "northupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/* 153 */     map.put("minecraft:" + type + "northupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "north", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/* 154 */     map.put("minecraft:" + type + "southlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/* 155 */     map.put("minecraft:" + type + "southlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/* 156 */     map.put("minecraft:" + type + "southlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/* 157 */     map.put("minecraft:" + type + "southlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/* 158 */     map.put("minecraft:" + type + "southlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/* 159 */     map.put("minecraft:" + type + "southlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/* 160 */     map.put("minecraft:" + type + "southlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/* 161 */     map.put("minecraft:" + type + "southlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/* 162 */     map.put("minecraft:" + type + "southupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/* 163 */     map.put("minecraft:" + type + "southupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/* 164 */     map.put("minecraft:" + type + "southupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/* 165 */     map.put("minecraft:" + type + "southupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/* 166 */     map.put("minecraft:" + type + "southupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/* 167 */     map.put("minecraft:" + type + "southupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/* 168 */     map.put("minecraft:" + type + "southupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/* 169 */     map.put("minecraft:" + type + "southupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "south", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/* 170 */     map.put("minecraft:" + type + "westlowerleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "false")));
/* 171 */     map.put("minecraft:" + type + "westlowerleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "false", "powered", "true")));
/* 172 */     map.put("minecraft:" + type + "westlowerlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "false")));
/* 173 */     map.put("minecraft:" + type + "westlowerlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "left", "open", "true", "powered", "true")));
/* 174 */     map.put("minecraft:" + type + "westlowerrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "false")));
/* 175 */     map.put("minecraft:" + type + "westlowerrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "false", "powered", "true")));
/* 176 */     map.put("minecraft:" + type + "westlowerrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "false")));
/* 177 */     map.put("minecraft:" + type + "westlowerrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "lower", "hinge", "right", "open", "true", "powered", "true")));
/* 178 */     map.put("minecraft:" + type + "westupperleftfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "false")));
/* 179 */     map.put("minecraft:" + type + "westupperleftfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "false", "powered", "true")));
/* 180 */     map.put("minecraft:" + type + "westupperlefttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "false")));
/* 181 */     map.put("minecraft:" + type + "westupperlefttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "left", "open", "true", "powered", "true")));
/* 182 */     map.put("minecraft:" + type + "westupperrightfalsefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "false")));
/* 183 */     map.put("minecraft:" + type + "westupperrightfalsetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "false", "powered", "true")));
/* 184 */     map.put("minecraft:" + type + "westupperrighttruefalse", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "false")));
/* 185 */     map.put("minecraft:" + type + "westupperrighttruetrue", ExtraDataFixUtils.blockState(id, Map.of("facing", "west", "half", "upper", "hinge", "right", "open", "true", "powered", "true")));
/*     */   }
/*     */   
/* 188 */   private static final Map<String, Dynamic<?>> NOTE_BLOCK_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/* 189 */         for (int i = 0; i < 26; i++) {
/* 190 */           map.put("true" + i, ExtraDataFixUtils.blockState("minecraft:note_block", Map.of("powered", "true", "note", String.valueOf(i))));
/* 191 */           map.put("false" + i, ExtraDataFixUtils.blockState("minecraft:note_block", Map.of("powered", "false", "note", String.valueOf(i))));
/*     */         } 
/*     */       });
/*     */   
/* 195 */   private static final Int2ObjectMap<String> DYE_COLOR_MAP = (Int2ObjectMap)DataFixUtils.make(new Int2ObjectOpenHashMap(), map -> {
/* 196 */         map.put(0, "white");
/* 197 */         map.put(1, "orange");
/* 198 */         map.put(2, "magenta");
/* 199 */         map.put(3, "light_blue");
/* 200 */         map.put(4, "yellow");
/* 201 */         map.put(5, "lime");
/* 202 */         map.put(6, "pink");
/* 203 */         map.put(7, "gray");
/* 204 */         map.put(8, "light_gray");
/* 205 */         map.put(9, "cyan");
/* 206 */         map.put(10, "purple");
/* 207 */         map.put(11, "blue");
/* 208 */         map.put(12, "brown");
/* 209 */         map.put(13, "green");
/* 210 */         map.put(14, "red");
/* 211 */         map.put(15, "black");
/*     */       });
/*     */   
/* 214 */   private static final Map<String, Dynamic<?>> BED_BLOCK_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/* 215 */         for (ObjectIterator objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry<String> entry = (Int2ObjectMap.Entry)objectIterator.next();
/* 216 */           if (!Objects.equals(entry.getValue(), "red")) {
/* 217 */             addBeds(map, entry.getIntKey(), (String)entry.getValue());
/*     */           } }
/*     */       
/*     */       });
/*     */   
/*     */   private static void addBeds(Map<String, Dynamic<?>> map, int colorId, String color) {
/* 223 */     map.put("southfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "false", "part", "foot")));
/* 224 */     map.put("westfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "false", "part", "foot")));
/* 225 */     map.put("northfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "false", "part", "foot")));
/* 226 */     map.put("eastfalsefoot" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "false", "part", "foot")));
/* 227 */     map.put("southfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "false", "part", "head")));
/* 228 */     map.put("westfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "false", "part", "head")));
/* 229 */     map.put("northfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "false", "part", "head")));
/* 230 */     map.put("eastfalsehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "false", "part", "head")));
/* 231 */     map.put("southtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "south", "occupied", "true", "part", "head")));
/* 232 */     map.put("westtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "west", "occupied", "true", "part", "head")));
/* 233 */     map.put("northtruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "north", "occupied", "true", "part", "head")));
/* 234 */     map.put("easttruehead" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_bed", Map.of("facing", "east", "occupied", "true", "part", "head")));
/*     */   }
/*     */   
/* 237 */   private static final Map<String, Dynamic<?>> BANNER_BLOCK_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), map -> {
/* 238 */         for (ObjectIterator objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) { Int2ObjectMap.Entry<String> entry = (Int2ObjectMap.Entry)objectIterator.next();
/* 239 */           if (!Objects.equals(entry.getValue(), "white"))
/*     */           {
/* 241 */             addBanners(map, 15 - entry.getIntKey(), (String)entry.getValue()); }  }
/*     */       
/*     */       });
/*     */   private static final Dynamic<?> AIR;
/*     */   
/*     */   private static void addBanners(Map<String, Dynamic<?>> map, int colorId, String color) {
/* 247 */     for (int i = 0; i < 16; i++) {
/* 248 */       map.put("" + i + "_" + i, ExtraDataFixUtils.blockState("minecraft:" + color + "_banner", Map.of("rotation", String.valueOf(i))));
/*     */     }
/* 250 */     map.put("north_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "north")));
/* 251 */     map.put("south_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "south")));
/* 252 */     map.put("west_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "west")));
/* 253 */     map.put("east_" + colorId, ExtraDataFixUtils.blockState("minecraft:" + color + "_wall_banner", Map.of("facing", "east")));
/*     */   }
/*     */   
/*     */   static  {
/* 257 */     FIX.set(2);
/* 258 */     FIX.set(3);
/* 259 */     FIX.set(110);
/*     */     
/* 261 */     FIX.set(140);
/* 262 */     FIX.set(144);
/*     */     
/* 264 */     FIX.set(25);
/*     */     
/* 266 */     FIX.set(86);
/*     */ 
/*     */     
/* 269 */     FIX.set(26);
/* 270 */     FIX.set(176);
/* 271 */     FIX.set(177);
/*     */     
/* 273 */     FIX.set(175);
/*     */     
/* 275 */     FIX.set(64);
/* 276 */     FIX.set(71);
/* 277 */     FIX.set(193);
/* 278 */     FIX.set(194);
/* 279 */     FIX.set(195);
/* 280 */     FIX.set(196);
/* 281 */     FIX.set(197);
/*     */     
/* 283 */     VIRTUAL.set(54);
/* 284 */     VIRTUAL.set(146);
/*     */     
/* 286 */     VIRTUAL.set(25);
/*     */     
/* 288 */     VIRTUAL.set(26);
/*     */     
/* 290 */     VIRTUAL.set(51);
/*     */     
/* 292 */     VIRTUAL.set(53);
/* 293 */     VIRTUAL.set(67);
/* 294 */     VIRTUAL.set(108);
/* 295 */     VIRTUAL.set(109);
/* 296 */     VIRTUAL.set(114);
/* 297 */     VIRTUAL.set(128);
/* 298 */     VIRTUAL.set(134);
/* 299 */     VIRTUAL.set(135);
/* 300 */     VIRTUAL.set(136);
/* 301 */     VIRTUAL.set(156);
/* 302 */     VIRTUAL.set(163);
/* 303 */     VIRTUAL.set(164);
/* 304 */     VIRTUAL.set(180);
/* 305 */     VIRTUAL.set(203);
/*     */     
/* 307 */     VIRTUAL.set(55);
/*     */     
/* 309 */     VIRTUAL.set(85);
/* 310 */     VIRTUAL.set(113);
/* 311 */     VIRTUAL.set(188);
/* 312 */     VIRTUAL.set(189);
/* 313 */     VIRTUAL.set(190);
/* 314 */     VIRTUAL.set(191);
/* 315 */     VIRTUAL.set(192);
/*     */     
/* 317 */     VIRTUAL.set(93);
/* 318 */     VIRTUAL.set(94);
/*     */     
/* 320 */     VIRTUAL.set(101);
/* 321 */     VIRTUAL.set(102);
/* 322 */     VIRTUAL.set(160);
/*     */     
/* 324 */     VIRTUAL.set(106);
/*     */ 
/*     */     
/* 327 */     VIRTUAL.set(107);
/* 328 */     VIRTUAL.set(183);
/* 329 */     VIRTUAL.set(184);
/* 330 */     VIRTUAL.set(185);
/* 331 */     VIRTUAL.set(186);
/* 332 */     VIRTUAL.set(187);
/*     */     
/* 334 */     VIRTUAL.set(132);
/* 335 */     VIRTUAL.set(139);
/*     */     
/* 337 */     VIRTUAL.set(199);
/*     */ 
/*     */     
/* 340 */     AIR = ExtraDataFixUtils.blockState("minecraft:air");
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$MappingConstants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */