/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.LongStream;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class StructuresBecomeConfiguredFix
/*     */   extends DataFix {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*  29 */   public StructuresBecomeConfiguredFix(Schema outputSchema) { super(outputSchema, false); }
/*     */   private static final class Conversion extends Record { private final Map<String, String> biomeMapping; private final String fallback;
/*     */     
/*  32 */     private Conversion(Map<String, String> biomeMapping, String fallback) { this.biomeMapping = biomeMapping; this.fallback = fallback; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #32	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  32 */       //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion; } public Map<String, String> biomeMapping() { return this.biomeMapping; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #32	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #32	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/datafix/fixes/StructuresBecomeConfiguredFix$Conversion;
/*  32 */       //   0	8	1	o	Ljava/lang/Object; } public String fallback() { return this.fallback; }
/*     */     
/*  34 */     public static Conversion trivial(String result) { return new Conversion(Map.of(), result); }
/*     */ 
/*     */ 
/*     */     
/*  38 */     public static Conversion biomeMapped(Map<List<String>, String> mapping, String fallback) { return new Conversion(unpack(mapping), fallback); }
/*     */ 
/*     */     
/*     */     private static Map<String, String> unpack(Map<List<String>, String> packed) {
/*  42 */       ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/*  43 */       for (Iterator iterator = packed.entrySet().iterator(); iterator.hasNext(); ) { Map.Entry<List<String>, String> entry = (Map.Entry)iterator.next();
/*  44 */         ((List)entry.getKey()).forEach(k -> builder.put(k, (String)entry.getValue())); }
/*     */       
/*  46 */       return builder.build();
/*     */     } }
/*     */ 
/*     */   
/*  50 */   private static final Map<String, Conversion> CONVERSION_MAP = ImmutableMap.builder()
/*  51 */     .put("mineshaft", Conversion.biomeMapped(
/*  52 */         Map.of(
/*  53 */           List.of("minecraft:badlands", "minecraft:eroded_badlands", "minecraft:wooded_badlands"), "minecraft:mineshaft_mesa"), "minecraft:mineshaft"))
/*     */ 
/*     */ 
/*     */     
/*  57 */     .put("shipwreck", Conversion.biomeMapped(
/*  58 */         Map.of(
/*  59 */           List.of("minecraft:beach", "minecraft:snowy_beach"), "minecraft:shipwreck_beached"), "minecraft:shipwreck"))
/*     */ 
/*     */ 
/*     */     
/*  63 */     .put("ocean_ruin", Conversion.biomeMapped(
/*  64 */         Map.of(
/*  65 */           List.of("minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:deep_lukewarm_ocean"), "minecraft:ocean_ruin_warm"), "minecraft:ocean_ruin_cold"))
/*     */ 
/*     */ 
/*     */     
/*  69 */     .put("village", Conversion.biomeMapped(
/*  70 */         Map.of(
/*  71 */           List.of("minecraft:desert"), "minecraft:village_desert", 
/*  72 */           List.of("minecraft:savanna"), "minecraft:village_savanna", 
/*  73 */           List.of("minecraft:snowy_plains"), "minecraft:village_snowy", 
/*  74 */           List.of("minecraft:taiga"), "minecraft:village_taiga"), "minecraft:village_plains"))
/*     */ 
/*     */ 
/*     */     
/*  78 */     .put("ruined_portal", Conversion.biomeMapped(
/*  79 */         Map.of(
/*  80 */           List.of("minecraft:desert"), "minecraft:ruined_portal_desert", 
/*  81 */           List.of(new String[] { "minecraft:badlands", "minecraft:eroded_badlands", "minecraft:wooded_badlands", "minecraft:windswept_hills", "minecraft:windswept_forest", "minecraft:windswept_gravelly_hills", "minecraft:savanna_plateau", "minecraft:windswept_savanna", "minecraft:stony_shore", "minecraft:meadow", "minecraft:frozen_peaks", "minecraft:jagged_peaks", "minecraft:stony_peaks", "minecraft:snowy_slopes" }, ), "minecraft:ruined_portal_mountain", 
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
/*  97 */           List.of("minecraft:bamboo_jungle", "minecraft:jungle", "minecraft:sparse_jungle"), "minecraft:ruined_portal_jungle", 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 102 */           List.of("minecraft:deep_frozen_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_ocean", "minecraft:deep_lukewarm_ocean", "minecraft:frozen_ocean", "minecraft:ocean", "minecraft:cold_ocean", "minecraft:lukewarm_ocean", "minecraft:warm_ocean"), "minecraft:ruined_portal_ocean"), "minecraft:ruined_portal"))
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
/* 116 */     .put("pillager_outpost", Conversion.trivial("minecraft:pillager_outpost"))
/* 117 */     .put("mansion", Conversion.trivial("minecraft:mansion"))
/* 118 */     .put("jungle_pyramid", Conversion.trivial("minecraft:jungle_pyramid"))
/* 119 */     .put("desert_pyramid", Conversion.trivial("minecraft:desert_pyramid"))
/* 120 */     .put("igloo", Conversion.trivial("minecraft:igloo"))
/* 121 */     .put("swamp_hut", Conversion.trivial("minecraft:swamp_hut"))
/* 122 */     .put("stronghold", Conversion.trivial("minecraft:stronghold"))
/* 123 */     .put("monument", Conversion.trivial("minecraft:monument"))
/* 124 */     .put("fortress", Conversion.trivial("minecraft:fortress"))
/* 125 */     .put("endcity", Conversion.trivial("minecraft:end_city"))
/* 126 */     .put("buried_treasure", Conversion.trivial("minecraft:buried_treasure"))
/* 127 */     .put("nether_fossil", Conversion.trivial("minecraft:nether_fossil"))
/* 128 */     .put("bastion_remnant", Conversion.trivial("minecraft:bastion_remnant"))
/* 129 */     .build();
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 133 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 134 */     Type<?> newChunkType = getInputSchema().getType(References.CHUNK);
/*     */     
/* 136 */     return writeFixAndRead("StucturesToConfiguredStructures", chunkType, newChunkType, this::fix);
/*     */   }
/*     */   
/*     */   private Dynamic<?> fix(Dynamic<?> chunk) {
/* 140 */     return chunk.update("structures", structures -> 
/* 141 */         structures
/* 142 */         .update("starts", ())
/* 143 */         .update("References", ()));
/*     */   }
/*     */ 
/*     */   
/*     */   private Dynamic<?> updateStarts(Dynamic<?> starts, Dynamic<?> chunk) {
/* 148 */     Map<? extends Dynamic<?>, ? extends Dynamic<?>> values = (Map)starts.getMapValues().result().orElse(Map.of());
/* 149 */     HashMap<Dynamic<?>, Dynamic<?>> newMap = Maps.newHashMap();
/* 150 */     values.forEach((key, start) -> {
/*     */           
/* 152 */           if (start.get("id").asString("INVALID").equals("INVALID")) {
/*     */             return;
/*     */           }
/* 155 */           Dynamic<?> newKey = findUpdatedStructureType(key, chunk);
/* 156 */           if (newKey == null) {
/* 157 */             LOGGER.warn("Encountered unknown structure in datafixer: {}", key.asString("<missing key>"));
/*     */             
/*     */             return;
/*     */           } 
/* 161 */           newMap.computeIfAbsent(newKey, ());
/*     */         });
/*     */     
/* 164 */     return chunk.createMap(newMap);
/*     */   }
/*     */   
/*     */   private Dynamic<?> updateReferences(Dynamic<?> references, Dynamic<?> chunk) {
/* 168 */     Map<? extends Dynamic<?>, ? extends Dynamic<?>> values = (Map)references.getMapValues().result().orElse(Map.of());
/* 169 */     HashMap<Dynamic<?>, Dynamic<?>> newMap = Maps.newHashMap();
/* 170 */     values.forEach((key, refList) -> {
/*     */           
/* 172 */           if (refList.asLongStream().count() == 0L) {
/*     */             return;
/*     */           }
/* 175 */           Dynamic<?> newKey = findUpdatedStructureType(key, chunk);
/* 176 */           if (newKey == null) {
/* 177 */             LOGGER.warn("Encountered unknown structure in datafixer: {}", key.asString("<missing key>"));
/*     */             return;
/*     */           } 
/* 180 */           newMap.compute(newKey, ());
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     return chunk.createMap(newMap);
/*     */   }
/*     */   
/*     */   private Dynamic<?> findUpdatedStructureType(Dynamic<?> dynamicKey, Dynamic<?> chunk) {
/* 192 */     String key = dynamicKey.asString("UNKNOWN").toLowerCase(Locale.ROOT);
/* 193 */     Conversion conversion = (Conversion)CONVERSION_MAP.get(key);
/* 194 */     if (conversion == null) {
/* 195 */       return null;
/*     */     }
/* 197 */     String resultingId = conversion.fallback;
/* 198 */     if (!conversion.biomeMapping().isEmpty()) {
/* 199 */       Optional<String> result = guessConfiguration(chunk, conversion);
/* 200 */       if (result.isPresent()) {
/* 201 */         resultingId = (String)result.get();
/*     */       }
/*     */     } 
/* 204 */     return chunk.createString(resultingId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<String> guessConfiguration(Dynamic<?> chunk, Conversion conversion) {
/* 211 */     Object2IntArrayMap<String> matches = new Object2IntArrayMap<String>();
/* 212 */     chunk.get("sections").asList(Function.identity()).forEach(s -> 
/* 213 */         s.get("biomes").get("palette").asList(Function.identity()).forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     return matches.object2IntEntrySet().stream()
/* 224 */       .max(Comparator.comparingInt(Object2IntMap.Entry::getIntValue))
/* 225 */       .map(Map.Entry::getKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\StructuresBecomeConfiguredFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */