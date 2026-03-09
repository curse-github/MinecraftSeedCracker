/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class LevelDataGeneratorOptionsFix extends DataFix {
/*  29 */   static final Map<String, String> MAP = (Map)Util.make(Maps.newHashMap(), map -> {
/*  30 */         map.put("0", "minecraft:ocean");
/*  31 */         map.put("1", "minecraft:plains");
/*  32 */         map.put("2", "minecraft:desert");
/*  33 */         map.put("3", "minecraft:mountains");
/*  34 */         map.put("4", "minecraft:forest");
/*  35 */         map.put("5", "minecraft:taiga");
/*  36 */         map.put("6", "minecraft:swamp");
/*  37 */         map.put("7", "minecraft:river");
/*  38 */         map.put("8", "minecraft:nether");
/*  39 */         map.put("9", "minecraft:the_end");
/*  40 */         map.put("10", "minecraft:frozen_ocean");
/*  41 */         map.put("11", "minecraft:frozen_river");
/*  42 */         map.put("12", "minecraft:snowy_tundra");
/*  43 */         map.put("13", "minecraft:snowy_mountains");
/*  44 */         map.put("14", "minecraft:mushroom_fields");
/*  45 */         map.put("15", "minecraft:mushroom_field_shore");
/*  46 */         map.put("16", "minecraft:beach");
/*  47 */         map.put("17", "minecraft:desert_hills");
/*  48 */         map.put("18", "minecraft:wooded_hills");
/*  49 */         map.put("19", "minecraft:taiga_hills");
/*  50 */         map.put("20", "minecraft:mountain_edge");
/*  51 */         map.put("21", "minecraft:jungle");
/*  52 */         map.put("22", "minecraft:jungle_hills");
/*  53 */         map.put("23", "minecraft:jungle_edge");
/*  54 */         map.put("24", "minecraft:deep_ocean");
/*  55 */         map.put("25", "minecraft:stone_shore");
/*  56 */         map.put("26", "minecraft:snowy_beach");
/*  57 */         map.put("27", "minecraft:birch_forest");
/*  58 */         map.put("28", "minecraft:birch_forest_hills");
/*  59 */         map.put("29", "minecraft:dark_forest");
/*  60 */         map.put("30", "minecraft:snowy_taiga");
/*  61 */         map.put("31", "minecraft:snowy_taiga_hills");
/*  62 */         map.put("32", "minecraft:giant_tree_taiga");
/*  63 */         map.put("33", "minecraft:giant_tree_taiga_hills");
/*  64 */         map.put("34", "minecraft:wooded_mountains");
/*  65 */         map.put("35", "minecraft:savanna");
/*  66 */         map.put("36", "minecraft:savanna_plateau");
/*  67 */         map.put("37", "minecraft:badlands");
/*  68 */         map.put("38", "minecraft:wooded_badlands_plateau");
/*  69 */         map.put("39", "minecraft:badlands_plateau");
/*  70 */         map.put("40", "minecraft:small_end_islands");
/*  71 */         map.put("41", "minecraft:end_midlands");
/*  72 */         map.put("42", "minecraft:end_highlands");
/*  73 */         map.put("43", "minecraft:end_barrens");
/*  74 */         map.put("44", "minecraft:warm_ocean");
/*  75 */         map.put("45", "minecraft:lukewarm_ocean");
/*  76 */         map.put("46", "minecraft:cold_ocean");
/*  77 */         map.put("47", "minecraft:deep_warm_ocean");
/*  78 */         map.put("48", "minecraft:deep_lukewarm_ocean");
/*  79 */         map.put("49", "minecraft:deep_cold_ocean");
/*  80 */         map.put("50", "minecraft:deep_frozen_ocean");
/*     */         
/*  82 */         map.put("127", "minecraft:the_void");
/*     */         
/*  84 */         map.put("129", "minecraft:sunflower_plains");
/*  85 */         map.put("130", "minecraft:desert_lakes");
/*  86 */         map.put("131", "minecraft:gravelly_mountains");
/*  87 */         map.put("132", "minecraft:flower_forest");
/*  88 */         map.put("133", "minecraft:taiga_mountains");
/*  89 */         map.put("134", "minecraft:swamp_hills");
/*  90 */         map.put("140", "minecraft:ice_spikes");
/*  91 */         map.put("149", "minecraft:modified_jungle");
/*  92 */         map.put("151", "minecraft:modified_jungle_edge");
/*     */         
/*  94 */         map.put("155", "minecraft:tall_birch_forest");
/*  95 */         map.put("156", "minecraft:tall_birch_hills");
/*  96 */         map.put("157", "minecraft:dark_forest_hills");
/*  97 */         map.put("158", "minecraft:snowy_taiga_mountains");
/*  98 */         map.put("160", "minecraft:giant_spruce_taiga");
/*  99 */         map.put("161", "minecraft:giant_spruce_taiga_hills");
/* 100 */         map.put("162", "minecraft:modified_gravelly_mountains");
/* 101 */         map.put("163", "minecraft:shattered_savanna");
/* 102 */         map.put("164", "minecraft:shattered_savanna_plateau");
/* 103 */         map.put("165", "minecraft:eroded_badlands");
/* 104 */         map.put("166", "minecraft:modified_wooded_badlands_plateau");
/* 105 */         map.put("167", "minecraft:modified_badlands_plateau");
/*     */       });
/*     */   
/*     */   public static final String GENERATOR_OPTIONS = "generatorOptions";
/*     */   
/* 110 */   public LevelDataGeneratorOptionsFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 115 */     Type<?> resultType = getOutputSchema().getType(References.LEVEL);
/* 116 */     return fixTypeEverywhereTyped("LevelDataGeneratorOptionsFix", getInputSchema().getType(References.LEVEL), resultType, input -> 
/* 117 */         Util.writeAndReadTypedOrThrow(input, resultType, ()));
/*     */   }
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
/*     */   private static <T> Dynamic<T> convert(String flatOptionString, DynamicOps<T> ops) {
/*     */     List<Pair<Integer, String>> layerList;
/* 134 */     Iterator<String> parts = Splitter.on(';').split(flatOptionString).iterator();
/*     */ 
/*     */     
/* 137 */     String biome = "minecraft:plains";
/* 138 */     Map<String, Map<String, String>> structuresOptions = Maps.newHashMap();
/*     */     
/* 140 */     if (!flatOptionString.isEmpty() && parts.hasNext()) {
/* 141 */       layerList = getLayersInfoFromString((String)parts.next());
/*     */       
/* 143 */       if (!layerList.isEmpty()) {
/* 144 */         if (parts.hasNext()) {
/* 145 */           biome = (String)MAP.getOrDefault(parts.next(), "minecraft:plains");
/*     */         }
/*     */         
/* 148 */         if (parts.hasNext()) {
/* 149 */           String[] structures1 = ((String)parts.next()).toLowerCase(Locale.ROOT).split(",");
/*     */           
/* 151 */           for (String structure : structures1) {
/* 152 */             String[] separated = structure.split("\\(", 2);
/*     */             
/* 154 */             if (!separated[0].isEmpty()) {
/* 155 */               structuresOptions.put(separated[0], Maps.newHashMap());
/*     */               
/* 157 */               if (separated.length > 1 && separated[1].endsWith(")") && separated[1].length() > 1) {
/* 158 */                 String[] options = separated[1].substring(0, separated[1].length() - 1).split(" ");
/*     */                 
/* 160 */                 for (String part : options) {
/* 161 */                   String[] split = part.split("=", 2);
/* 162 */                   if (split.length == 2) {
/* 163 */                     ((Map)structuresOptions.get(separated[0])).put(split[0], split[1]);
/*     */                   }
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } else {
/* 170 */           structuresOptions.put("village", Maps.newHashMap());
/*     */         } 
/*     */       } 
/*     */     } else {
/* 174 */       layerList = Lists.newArrayList();
/* 175 */       layerList.add(Pair.of(Integer.valueOf(1), "minecraft:bedrock"));
/* 176 */       layerList.add(Pair.of(Integer.valueOf(2), "minecraft:dirt"));
/* 177 */       layerList.add(Pair.of(Integer.valueOf(1), "minecraft:grass_block"));
/* 178 */       structuresOptions.put("village", Maps.newHashMap());
/*     */     } 
/*     */     
/* 181 */     T layers = (T)ops.createList(layerList.stream().map(layer -> ops.createMap(ImmutableMap.of(ops
/* 182 */               .createString("height"), ops.createInt(((Integer)layer.getFirst()).intValue()), ops
/* 183 */               .createString("block"), ops.createString((String)layer.getSecond())))));
/*     */ 
/*     */     
/* 186 */     T structures = (T)ops.createMap((Map)structuresOptions.entrySet().stream().map(entry -> 
/* 187 */           Pair.of(ops
/* 188 */             .createString(((String)entry.getKey()).toLowerCase(Locale.ROOT)), ops
/* 189 */             .createMap((Map)((Map)entry.getValue()).entrySet().stream().map(())
/*     */               
/* 191 */               .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)))))
/*     */         
/* 193 */         .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
/*     */     
/* 195 */     return new Dynamic(ops, ops.createMap(ImmutableMap.of(ops
/* 196 */             .createString("layers"), layers, ops
/* 197 */             .createString("biome"), ops.createString(biome), ops
/* 198 */             .createString("structures"), structures)));
/*     */   }
/*     */   
/*     */   private static Pair<Integer, String> getLayerInfoFromString(String input) {
/*     */     int height;
/* 203 */     String[] parts = input.split("\\*", 2);
/*     */ 
/*     */     
/* 206 */     if (parts.length == 2) {
/*     */       try {
/* 208 */         height = Integer.parseInt(parts[0]);
/* 209 */       } catch (NumberFormatException ignored) {
/* 210 */         return null;
/*     */       } 
/*     */     } else {
/* 213 */       height = 1;
/*     */     } 
/*     */     
/* 216 */     String block = parts[parts.length - 1];
/* 217 */     return Pair.of(Integer.valueOf(height), block);
/*     */   }
/*     */   
/*     */   private static List<Pair<Integer, String>> getLayersInfoFromString(String input) {
/* 221 */     List<Pair<Integer, String>> result = Lists.newArrayList();
/* 222 */     String[] depths = input.split(",");
/*     */     
/* 224 */     for (String depth : depths) {
/* 225 */       Pair<Integer, String> layer = getLayerInfoFromString(depth);
/* 226 */       if (layer == null) {
/* 227 */         return Collections.emptyList();
/*     */       }
/* 229 */       result.add(layer);
/*     */     } 
/*     */     
/* 232 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LevelDataGeneratorOptionsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */