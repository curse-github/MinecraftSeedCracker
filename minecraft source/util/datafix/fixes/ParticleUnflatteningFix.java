/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ParticleUnflatteningFix
/*     */   extends DataFix {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*  28 */   public ParticleUnflatteningFix(Schema outputSchema) { super(outputSchema, true); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  33 */     Type<?> oldType = getInputSchema().getType(References.PARTICLE);
/*  34 */     Type<?> newType = getOutputSchema().getType(References.PARTICLE);
/*  35 */     return writeFixAndRead("ParticleUnflatteningFix", oldType, newType, this::fix);
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> fix(Dynamic<T> input) {
/*  39 */     Optional<String> maybeString = input.asString().result();
/*  40 */     if (maybeString.isEmpty()) {
/*  41 */       return input;
/*     */     }
/*     */     
/*  44 */     String particleDescription = (String)maybeString.get();
/*  45 */     String[] parts = particleDescription.split(" ", 2);
/*  46 */     String id = NamespacedSchema.ensureNamespaced(parts[0]);
/*  47 */     Dynamic<T> result = input.createMap(Map.of(input.createString("type"), input.createString(id)));
/*  48 */     switch (id) { case "minecraft:item": return 
/*  49 */           (parts.length > 1) ? updateItem(result, parts[1]) : result;
/*     */       case "minecraft:block": case "minecraft:block_marker": case "minecraft:falling_dust": case "minecraft:dust_pillar":
/*  51 */         return (parts.length > 1) ? updateBlock(result, parts[1]) : result;
/*  52 */       case "minecraft:dust": return (parts.length > 1) ? updateDust(result, parts[1]) : result;
/*     */       case "minecraft:dust_color_transition":
/*  54 */         return (parts.length > 1) ? updateDustTransition(result, parts[1]) : result;
/*  55 */       case "minecraft:sculk_charge": return (parts.length > 1) ? updateSculkCharge(result, parts[1]) : result;
/*  56 */       case "minecraft:vibration": return (parts.length > 1) ? updateVibration(result, parts[1]) : result;
/*  57 */       case "minecraft:shriek": return (parts.length > 1) ? updateShriek(result, parts[1]) : result; }
/*  58 */      return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Dynamic<T> updateItem(Dynamic<T> result, String contents) {
/*  63 */     int tagPartStart = contents.indexOf("{");
/*  64 */     Dynamic<T> itemStack = result.createMap(
/*     */         
/*  66 */         Map.of(result.createString("Count"), result.createInt(1)));
/*     */     
/*  68 */     if (tagPartStart == -1) {
/*  69 */       itemStack = itemStack.set("id", result.createString(contents));
/*     */     } else {
/*  71 */       itemStack = itemStack.set("id", result.createString(contents.substring(0, tagPartStart)));
/*  72 */       Dynamic<T> itemTag = parseTag(result.getOps(), contents.substring(tagPartStart));
/*  73 */       if (itemTag != null) {
/*  74 */         itemStack = itemStack.set("tag", itemTag);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  79 */     return result.set("item", itemStack);
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> parseTag(DynamicOps<T> ops, String contents) {
/*     */     try {
/*  84 */       return new Dynamic(ops, TagParser.create(ops).parseFully(contents));
/*  85 */     } catch (Exception e) {
/*  86 */       LOGGER.warn("Failed to parse tag: {}", contents, e);
/*     */       
/*  88 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateBlock(Dynamic<T> result, String contents) {
/*  93 */     int statePartStart = contents.indexOf("[");
/*  94 */     Dynamic<T> blockState = result.emptyMap();
/*  95 */     if (statePartStart == -1) {
/*  96 */       blockState = blockState.set("Name", result.createString(NamespacedSchema.ensureNamespaced(contents)));
/*     */     } else {
/*  98 */       blockState = blockState.set("Name", result.createString(NamespacedSchema.ensureNamespaced(contents.substring(0, statePartStart))));
/*  99 */       Map<Dynamic<T>, Dynamic<T>> properties = parseBlockProperties(result, contents.substring(statePartStart));
/* 100 */       if (!properties.isEmpty()) {
/* 101 */         blockState = blockState.set("Properties", result
/* 102 */             .createMap(properties));
/*     */       }
/*     */     } 
/*     */     
/* 106 */     return result.set("block_state", blockState);
/*     */   }
/*     */   
/*     */   private static <T> Map<Dynamic<T>, Dynamic<T>> parseBlockProperties(Dynamic<T> dynamic, String contents) {
/*     */     try {
/* 111 */       Map<Dynamic<T>, Dynamic<T>> result = new HashMap<Dynamic<T>, Dynamic<T>>();
/* 112 */       StringReader reader = new StringReader(contents);
/*     */       
/* 114 */       reader.expect('[');
/* 115 */       reader.skipWhitespace();
/* 116 */       while (reader.canRead() && reader.peek() != ']') {
/* 117 */         reader.skipWhitespace();
/* 118 */         String key = reader.readString();
/* 119 */         reader.skipWhitespace();
/* 120 */         reader.expect('=');
/* 121 */         reader.skipWhitespace();
/* 122 */         String value = reader.readString();
/* 123 */         reader.skipWhitespace();
/* 124 */         result.put(dynamic.createString(key), dynamic.createString(value));
/*     */         
/* 126 */         if (reader.canRead()) {
/* 127 */           if (reader.peek() == ',') {
/* 128 */             reader.skip();
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/* 134 */       reader.expect(']');
/* 135 */       return result;
/* 136 */     } catch (Exception e) {
/* 137 */       LOGGER.warn("Failed to parse block properties: {}", contents, e);
/* 138 */       return Map.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> readVector(Dynamic<T> result, StringReader reader) throws CommandSyntaxException {
/* 143 */     float x = reader.readFloat();
/* 144 */     reader.expect(' ');
/* 145 */     float y = reader.readFloat();
/* 146 */     reader.expect(' ');
/* 147 */     float z = reader.readFloat();
/* 148 */     Objects.requireNonNull(result); return result.createList(Stream.of(new Float[] { null, null, (new Float[3][1] = (new Float[3][0] = Float.valueOf(x)).valueOf(y)).valueOf(z) }).map(result::createFloat));
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateDust(Dynamic<T> result, String contents) {
/*     */     try {
/* 153 */       StringReader reader = new StringReader(contents);
/* 154 */       Dynamic<T> vector = readVector(result, reader);
/* 155 */       reader.expect(' ');
/* 156 */       float scale = reader.readFloat();
/*     */       
/* 158 */       return result
/* 159 */         .set("color", vector)
/* 160 */         .set("scale", result.createFloat(scale));
/* 161 */     } catch (Exception e) {
/* 162 */       LOGGER.warn("Failed to parse particle options: {}", contents, e);
/* 163 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateDustTransition(Dynamic<T> result, String contents) {
/*     */     try {
/* 169 */       StringReader reader = new StringReader(contents);
/* 170 */       Dynamic<T> from = readVector(result, reader);
/* 171 */       reader.expect(' ');
/* 172 */       float scale = reader.readFloat();
/* 173 */       reader.expect(' ');
/* 174 */       Dynamic<T> to = readVector(result, reader);
/*     */       
/* 176 */       return result
/* 177 */         .set("from_color", from)
/* 178 */         .set("to_color", to)
/* 179 */         .set("scale", result.createFloat(scale));
/* 180 */     } catch (Exception e) {
/* 181 */       LOGGER.warn("Failed to parse particle options: {}", contents, e);
/* 182 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateSculkCharge(Dynamic<T> result, String contents) {
/*     */     try {
/* 188 */       StringReader reader = new StringReader(contents);
/* 189 */       float roll = reader.readFloat();
/* 190 */       return result.set("roll", result.createFloat(roll));
/* 191 */     } catch (Exception e) {
/* 192 */       LOGGER.warn("Failed to parse particle options: {}", contents, e);
/* 193 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateVibration(Dynamic<T> result, String contents) {
/*     */     try {
/* 199 */       StringReader reader = new StringReader(contents);
/* 200 */       float destX = (float)reader.readDouble();
/* 201 */       reader.expect(' ');
/* 202 */       float destY = (float)reader.readDouble();
/* 203 */       reader.expect(' ');
/* 204 */       float destZ = (float)reader.readDouble();
/* 205 */       reader.expect(' ');
/* 206 */       int arrivalInTicks = reader.readInt();
/*     */ 
/*     */       
/* 209 */       Dynamic<T> blockPos = result.createIntList(IntStream.of(new int[] { Mth.floor(destX), Mth.floor(destY), Mth.floor(destZ) }));
/* 210 */       Dynamic<T> positionSource = result.createMap(Map.of(result
/* 211 */             .createString("type"), result.createString("minecraft:block"), result
/* 212 */             .createString("pos"), blockPos));
/*     */ 
/*     */       
/* 215 */       return result
/* 216 */         .set("destination", positionSource)
/* 217 */         .set("arrival_in_ticks", result.createInt(arrivalInTicks));
/* 218 */     } catch (Exception e) {
/* 219 */       LOGGER.warn("Failed to parse particle options: {}", contents, e);
/* 220 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> updateShriek(Dynamic<T> result, String contents) {
/*     */     try {
/* 226 */       StringReader reader = new StringReader(contents);
/* 227 */       int delay = reader.readInt();
/* 228 */       return result.set("delay", result.createInt(delay));
/* 229 */     } catch (Exception e) {
/* 230 */       LOGGER.warn("Failed to parse particle options: {}", contents, e);
/* 231 */       return result;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ParticleUnflatteningFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */