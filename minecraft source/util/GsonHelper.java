/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.Strictness;
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.item.Item;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ 
/*     */ public class GsonHelper
/*     */ {
/*  39 */   private static final Gson GSON = (new GsonBuilder()).create();
/*     */   
/*     */   public static boolean isStringValue(JsonObject node, String name) {
/*  42 */     if (!isValidPrimitive(node, name)) {
/*  43 */       return false;
/*     */     }
/*  45 */     return node.getAsJsonPrimitive(name).isString();
/*     */   }
/*     */   
/*     */   public static boolean isStringValue(JsonElement node) {
/*  49 */     if (!node.isJsonPrimitive()) {
/*  50 */       return false;
/*     */     }
/*  52 */     return node.getAsJsonPrimitive().isString();
/*     */   }
/*     */   
/*     */   public static boolean isNumberValue(JsonObject node, String name) {
/*  56 */     if (!isValidPrimitive(node, name)) {
/*  57 */       return false;
/*     */     }
/*  59 */     return node.getAsJsonPrimitive(name).isNumber();
/*     */   }
/*     */   
/*     */   public static boolean isNumberValue(JsonElement node) {
/*  63 */     if (!node.isJsonPrimitive()) {
/*  64 */       return false;
/*     */     }
/*  66 */     return node.getAsJsonPrimitive().isNumber();
/*     */   }
/*     */   
/*     */   public static boolean isBooleanValue(JsonObject node, String name) {
/*  70 */     if (!isValidPrimitive(node, name)) {
/*  71 */       return false;
/*     */     }
/*  73 */     return node.getAsJsonPrimitive(name).isBoolean();
/*     */   }
/*     */   
/*     */   public static boolean isBooleanValue(JsonElement node) {
/*  77 */     if (!node.isJsonPrimitive()) {
/*  78 */       return false;
/*     */     }
/*  80 */     return node.getAsJsonPrimitive().isBoolean();
/*     */   }
/*     */   
/*     */   public static boolean isArrayNode(JsonObject node, String name) {
/*  84 */     if (!isValidNode(node, name)) {
/*  85 */       return false;
/*     */     }
/*  87 */     return node.get(name).isJsonArray();
/*     */   }
/*     */   
/*     */   public static boolean isObjectNode(JsonObject node, String name) {
/*  91 */     if (!isValidNode(node, name)) {
/*  92 */       return false;
/*     */     }
/*  94 */     return node.get(name).isJsonObject();
/*     */   }
/*     */   
/*     */   public static boolean isValidPrimitive(JsonObject node, String name) {
/*  98 */     if (!isValidNode(node, name)) {
/*  99 */       return false;
/*     */     }
/* 101 */     return node.get(name).isJsonPrimitive();
/*     */   }
/*     */   
/*     */   public static boolean isValidNode(JsonObject node, String name) {
/* 105 */     if (node == null) {
/* 106 */       return false;
/*     */     }
/* 108 */     return (node.get(name) != null);
/*     */   }
/*     */   
/*     */   public static JsonElement getNonNull(JsonObject object, String name) {
/* 112 */     JsonElement result = object.get(name);
/* 113 */     if (result == null || result.isJsonNull()) {
/* 114 */       throw new JsonSyntaxException("Missing field " + name);
/*     */     }
/* 116 */     return result;
/*     */   }
/*     */   
/*     */   public static String convertToString(JsonElement element, String name) {
/* 120 */     if (element.isJsonPrimitive()) {
/* 121 */       return element.getAsString();
/*     */     }
/* 123 */     throw new JsonSyntaxException("Expected " + name + " to be a string, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getAsString(JsonObject object, String name) {
/* 128 */     if (object.has(name)) {
/* 129 */       return convertToString(object.get(name), name);
/*     */     }
/* 131 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a string");
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null->!null;_,_,null->_")
/*     */   public static String getAsString(JsonObject object, String name, String def) {
/* 137 */     if (object.has(name)) {
/* 138 */       return convertToString(object.get(name), name);
/*     */     }
/* 140 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Holder<Item> convertToItem(JsonElement element, String name) {
/* 145 */     if (element.isJsonPrimitive()) {
/* 146 */       String itemName = element.getAsString();
/* 147 */       return (Holder)BuiltInRegistries.ITEM.get(Identifier.parse(itemName))
/* 148 */         .orElseThrow(() -> new JsonSyntaxException("Expected " + name + " to be an item, was unknown string '" + itemName + "'"));
/*     */     } 
/* 150 */     throw new JsonSyntaxException("Expected " + name + " to be an item, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Holder<Item> getAsItem(JsonObject object, String name) {
/* 155 */     if (object.has(name)) {
/* 156 */       return convertToItem(object.get(name), name);
/*     */     }
/* 158 */     throw new JsonSyntaxException("Missing " + name + ", expected to find an item");
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null->!null;_,_,null->_")
/*     */   public static Holder<Item> getAsItem(JsonObject object, String name, Holder<Item> def) {
/* 164 */     if (object.has(name)) {
/* 165 */       return convertToItem(object.get(name), name);
/*     */     }
/* 167 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean convertToBoolean(JsonElement element, String name) {
/* 172 */     if (element.isJsonPrimitive()) {
/* 173 */       return element.getAsBoolean();
/*     */     }
/* 175 */     throw new JsonSyntaxException("Expected " + name + " to be a Boolean, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getAsBoolean(JsonObject object, String name) {
/* 180 */     if (object.has(name)) {
/* 181 */       return convertToBoolean(object.get(name), name);
/*     */     }
/* 183 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a Boolean");
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean getAsBoolean(JsonObject object, String name, boolean def) {
/* 188 */     if (object.has(name)) {
/* 189 */       return convertToBoolean(object.get(name), name);
/*     */     }
/* 191 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double convertToDouble(JsonElement element, String name) {
/* 196 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 197 */       return element.getAsDouble();
/*     */     }
/* 199 */     throw new JsonSyntaxException("Expected " + name + " to be a Double, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getAsDouble(JsonObject object, String name) {
/* 204 */     if (object.has(name)) {
/* 205 */       return convertToDouble(object.get(name), name);
/*     */     }
/* 207 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a Double");
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getAsDouble(JsonObject object, String name, double def) {
/* 212 */     if (object.has(name)) {
/* 213 */       return convertToDouble(object.get(name), name);
/*     */     }
/* 215 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float convertToFloat(JsonElement element, String name) {
/* 220 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 221 */       return element.getAsFloat();
/*     */     }
/* 223 */     throw new JsonSyntaxException("Expected " + name + " to be a Float, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getAsFloat(JsonObject object, String name) {
/* 228 */     if (object.has(name)) {
/* 229 */       return convertToFloat(object.get(name), name);
/*     */     }
/* 231 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a Float");
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getAsFloat(JsonObject object, String name, float def) {
/* 236 */     if (object.has(name)) {
/* 237 */       return convertToFloat(object.get(name), name);
/*     */     }
/* 239 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static long convertToLong(JsonElement element, String name) {
/* 244 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 245 */       return element.getAsLong();
/*     */     }
/* 247 */     throw new JsonSyntaxException("Expected " + name + " to be a Long, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getAsLong(JsonObject object, String name) {
/* 252 */     if (object.has(name)) {
/* 253 */       return convertToLong(object.get(name), name);
/*     */     }
/* 255 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a Long");
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getAsLong(JsonObject object, String name, long def) {
/* 260 */     if (object.has(name)) {
/* 261 */       return convertToLong(object.get(name), name);
/*     */     }
/* 263 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int convertToInt(JsonElement element, String name) {
/* 268 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 269 */       return element.getAsInt();
/*     */     }
/* 271 */     throw new JsonSyntaxException("Expected " + name + " to be a Int, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAsInt(JsonObject object, String name) {
/* 276 */     if (object.has(name)) {
/* 277 */       return convertToInt(object.get(name), name);
/*     */     }
/* 279 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a Int");
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAsInt(JsonObject object, String name, int def) {
/* 284 */     if (object.has(name)) {
/* 285 */       return convertToInt(object.get(name), name);
/*     */     }
/* 287 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte convertToByte(JsonElement element, String name) {
/* 292 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 293 */       return element.getAsByte();
/*     */     }
/* 295 */     throw new JsonSyntaxException("Expected " + name + " to be a Byte, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getAsByte(JsonObject object, String name) {
/* 300 */     if (object.has(name)) {
/* 301 */       return convertToByte(object.get(name), name);
/*     */     }
/* 303 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a Byte");
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getAsByte(JsonObject object, String name, byte def) {
/* 308 */     if (object.has(name)) {
/* 309 */       return convertToByte(object.get(name), name);
/*     */     }
/* 311 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static char convertToCharacter(JsonElement element, String name) {
/* 316 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 317 */       return element.getAsCharacter();
/*     */     }
/* 319 */     throw new JsonSyntaxException("Expected " + name + " to be a Character, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static char getAsCharacter(JsonObject object, String name) {
/* 324 */     if (object.has(name)) {
/* 325 */       return convertToCharacter(object.get(name), name);
/*     */     }
/* 327 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a Character");
/*     */   }
/*     */ 
/*     */   
/*     */   public static char getAsCharacter(JsonObject object, String name, char def) {
/* 332 */     if (object.has(name)) {
/* 333 */       return convertToCharacter(object.get(name), name);
/*     */     }
/* 335 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal convertToBigDecimal(JsonElement element, String name) {
/* 340 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 341 */       return element.getAsBigDecimal();
/*     */     }
/* 343 */     throw new JsonSyntaxException("Expected " + name + " to be a BigDecimal, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAsBigDecimal(JsonObject object, String name) {
/* 348 */     if (object.has(name)) {
/* 349 */       return convertToBigDecimal(object.get(name), name);
/*     */     }
/* 351 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a BigDecimal");
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAsBigDecimal(JsonObject object, String name, BigDecimal def) {
/* 356 */     if (object.has(name)) {
/* 357 */       return convertToBigDecimal(object.get(name), name);
/*     */     }
/* 359 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger convertToBigInteger(JsonElement element, String name) {
/* 364 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 365 */       return element.getAsBigInteger();
/*     */     }
/* 367 */     throw new JsonSyntaxException("Expected " + name + " to be a BigInteger, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger getAsBigInteger(JsonObject object, String name) {
/* 372 */     if (object.has(name)) {
/* 373 */       return convertToBigInteger(object.get(name), name);
/*     */     }
/* 375 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a BigInteger");
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigInteger getAsBigInteger(JsonObject object, String name, BigInteger def) {
/* 380 */     if (object.has(name)) {
/* 381 */       return convertToBigInteger(object.get(name), name);
/*     */     }
/* 383 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static short convertToShort(JsonElement element, String name) {
/* 388 */     if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
/* 389 */       return element.getAsShort();
/*     */     }
/* 391 */     throw new JsonSyntaxException("Expected " + name + " to be a Short, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static short getAsShort(JsonObject object, String name) {
/* 396 */     if (object.has(name)) {
/* 397 */       return convertToShort(object.get(name), name);
/*     */     }
/* 399 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a Short");
/*     */   }
/*     */ 
/*     */   
/*     */   public static short getAsShort(JsonObject object, String name, short def) {
/* 404 */     if (object.has(name)) {
/* 405 */       return convertToShort(object.get(name), name);
/*     */     }
/* 407 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonObject convertToJsonObject(JsonElement element, String name) {
/* 412 */     if (element.isJsonObject()) {
/* 413 */       return element.getAsJsonObject();
/*     */     }
/* 415 */     throw new JsonSyntaxException("Expected " + name + " to be a JsonObject, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonObject getAsJsonObject(JsonObject object, String name) {
/* 420 */     if (object.has(name)) {
/* 421 */       return convertToJsonObject(object.get(name), name);
/*     */     }
/* 423 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a JsonObject");
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null->!null;_,_,null->_")
/*     */   public static JsonObject getAsJsonObject(JsonObject object, String name, JsonObject def) {
/* 429 */     if (object.has(name)) {
/* 430 */       return convertToJsonObject(object.get(name), name);
/*     */     }
/* 432 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonArray convertToJsonArray(JsonElement element, String name) {
/* 437 */     if (element.isJsonArray()) {
/* 438 */       return element.getAsJsonArray();
/*     */     }
/* 440 */     throw new JsonSyntaxException("Expected " + name + " to be a JsonArray, was " + getType(element));
/*     */   }
/*     */ 
/*     */   
/*     */   public static JsonArray getAsJsonArray(JsonObject object, String name) {
/* 445 */     if (object.has(name)) {
/* 446 */       return convertToJsonArray(object.get(name), name);
/*     */     }
/* 448 */     throw new JsonSyntaxException("Missing " + name + ", expected to find a JsonArray");
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null->!null;_,_,null->_")
/*     */   public static JsonArray getAsJsonArray(JsonObject object, String name, JsonArray def) {
/* 454 */     if (object.has(name)) {
/* 455 */       return convertToJsonArray(object.get(name), name);
/*     */     }
/* 457 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T convertToObject(JsonElement element, String name, JsonDeserializationContext context, Class<? extends T> clazz) {
/* 462 */     if (element != null) {
/* 463 */       return (T)context.deserialize(element, clazz);
/*     */     }
/* 465 */     throw new JsonSyntaxException("Missing " + name);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T getAsObject(JsonObject object, String name, JsonDeserializationContext context, Class<? extends T> clazz) {
/* 470 */     if (object.has(name)) {
/* 471 */       return (T)convertToObject(object.get(name), name, context, clazz);
/*     */     }
/* 473 */     throw new JsonSyntaxException("Missing " + name);
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("_,_,!null,_,_->!null;_,_,null,_,_->_")
/*     */   public static <T> T getAsObject(JsonObject object, String name, T def, JsonDeserializationContext context, Class<? extends T> clazz) {
/* 479 */     if (object.has(name)) {
/* 480 */       return (T)convertToObject(object.get(name), name, context, clazz);
/*     */     }
/* 482 */     return def;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getType(JsonElement element) {
/* 487 */     String value = StringUtils.abbreviateMiddle(String.valueOf(element), "...", 10);
/* 488 */     if (element == null) {
/* 489 */       return "null (missing)";
/*     */     }
/* 491 */     if (element.isJsonNull()) {
/* 492 */       return "null (json)";
/*     */     }
/* 494 */     if (element.isJsonArray()) {
/* 495 */       return "an array (" + value + ")";
/*     */     }
/* 497 */     if (element.isJsonObject()) {
/* 498 */       return "an object (" + value + ")";
/*     */     }
/* 500 */     if (element.isJsonPrimitive()) {
/* 501 */       JsonPrimitive primitive = element.getAsJsonPrimitive();
/* 502 */       if (primitive.isNumber()) {
/* 503 */         return "a number (" + value + ")";
/*     */       }
/* 505 */       if (primitive.isBoolean()) {
/* 506 */         return "a boolean (" + value + ")";
/*     */       }
/*     */     } 
/* 509 */     return value;
/*     */   }
/*     */   
/*     */   public static <T> T fromJson(Gson gson, Reader reader, Class<T> type) {
/*     */     try {
/* 514 */       JsonReader jsonReader = new JsonReader(reader);
/* 515 */       jsonReader.setStrictness(Strictness.STRICT);
/* 516 */       T result = (T)gson.getAdapter(type).read(jsonReader);
/* 517 */       if (result == null) {
/* 518 */         throw new JsonParseException("JSON data was null or empty");
/*     */       }
/* 520 */       return result;
/* 521 */     } catch (IOException e) {
/* 522 */       throw new JsonParseException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> T fromNullableJson(Gson gson, Reader reader, TypeToken<T> type) {
/*     */     try {
/* 528 */       JsonReader jsonReader = new JsonReader(reader);
/* 529 */       jsonReader.setStrictness(Strictness.STRICT);
/* 530 */       return (T)gson.getAdapter(type).read(jsonReader);
/* 531 */     } catch (IOException e) {
/* 532 */       throw new JsonParseException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> T fromJson(Gson gson, Reader reader, TypeToken<T> type) {
/* 537 */     T result = (T)fromNullableJson(gson, reader, type);
/* 538 */     if (result == null) {
/* 539 */       throw new JsonParseException("JSON data was null or empty");
/*     */     }
/* 541 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 545 */   public static <T> T fromNullableJson(Gson gson, String string, TypeToken<T> type) { return (T)fromNullableJson(gson, new StringReader(string), type); }
/*     */ 
/*     */ 
/*     */   
/* 549 */   public static <T> T fromJson(Gson gson, String string, Class<T> type) { return (T)fromJson(gson, new StringReader(string), type); }
/*     */ 
/*     */ 
/*     */   
/* 553 */   public static JsonObject parse(String string) { return parse(new StringReader(string)); }
/*     */ 
/*     */ 
/*     */   
/* 557 */   public static JsonObject parse(Reader reader) { return (JsonObject)fromJson(GSON, reader, JsonObject.class); }
/*     */ 
/*     */ 
/*     */   
/* 561 */   public static JsonArray parseArray(String string) { return parseArray(new StringReader(string)); }
/*     */ 
/*     */ 
/*     */   
/* 565 */   public static JsonArray parseArray(Reader reader) { return (JsonArray)fromJson(GSON, reader, JsonArray.class); }
/*     */ 
/*     */   
/*     */   public static String toStableString(JsonElement jsonElement) {
/* 569 */     StringWriter stringWriter = new StringWriter();
/* 570 */     JsonWriter jsonWriter = new JsonWriter(stringWriter);
/*     */     try {
/* 572 */       writeValue(jsonWriter, jsonElement, Comparator.naturalOrder());
/* 573 */     } catch (IOException e) {
/*     */       
/* 575 */       throw new AssertionError(e);
/*     */     } 
/* 577 */     return stringWriter.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeValue(JsonWriter out, JsonElement value, Comparator<String> keyComparator) throws IOException {
/* 584 */     if (value == null || value.isJsonNull()) {
/* 585 */       out.nullValue();
/* 586 */     } else if (value.isJsonPrimitive()) {
/* 587 */       JsonPrimitive primitive = value.getAsJsonPrimitive();
/* 588 */       if (primitive.isNumber()) {
/* 589 */         out.value(primitive.getAsNumber());
/* 590 */       } else if (primitive.isBoolean()) {
/* 591 */         out.value(primitive.getAsBoolean());
/*     */       } else {
/* 593 */         out.value(primitive.getAsString());
/*     */       } 
/* 595 */     } else if (value.isJsonArray()) {
/* 596 */       out.beginArray();
/* 597 */       for (JsonElement e : value.getAsJsonArray()) {
/* 598 */         writeValue(out, e, keyComparator);
/*     */       }
/* 600 */       out.endArray();
/* 601 */     } else if (value.isJsonObject()) {
/* 602 */       out.beginObject();
/* 603 */       for (Map.Entry<String, JsonElement> e : sortByKeyIfNeeded(value.getAsJsonObject().entrySet(), keyComparator)) {
/* 604 */         out.name((String)e.getKey());
/* 605 */         writeValue(out, (JsonElement)e.getValue(), keyComparator);
/*     */       } 
/* 607 */       out.endObject();
/*     */     } else {
/* 609 */       throw new IllegalArgumentException("Couldn't write " + String.valueOf(value.getClass()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Collection<Map.Entry<String, JsonElement>> sortByKeyIfNeeded(Collection<Map.Entry<String, JsonElement>> elements, Comparator<String> keyComparator) {
/* 614 */     if (keyComparator == null) {
/* 615 */       return elements;
/*     */     }
/* 617 */     List<Map.Entry<String, JsonElement>> sorted = new ArrayList<Map.Entry<String, JsonElement>>(elements);
/* 618 */     sorted.sort(Map.Entry.comparingByKey(keyComparator));
/* 619 */     return sorted;
/*     */   }
/*     */   
/*     */   public static boolean encodesLongerThan(JsonElement element, int limit) {
/*     */     try {
/* 624 */       Streams.write(element, new JsonWriter(Streams.writerForAppendable(new CountedAppendable(limit))));
/* 625 */     } catch (IllegalStateException e) {
/* 626 */       return true;
/* 627 */     } catch (IOException e) {
/* 628 */       throw new UncheckedIOException(e);
/*     */     } 
/* 630 */     return false;
/*     */   }
/*     */   
/*     */   private static class CountedAppendable
/*     */     implements Appendable {
/*     */     private int totalCount;
/*     */     private final int limit;
/*     */     
/* 638 */     public CountedAppendable(int limit) { this.limit = limit; }
/*     */ 
/*     */     
/*     */     private Appendable accountChars(int count) {
/* 642 */       this.totalCount += count;
/* 643 */       if (this.totalCount > this.limit) {
/* 644 */         throw new IllegalStateException("Character count over limit: " + this.totalCount + " > " + this.limit);
/*     */       }
/* 646 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 651 */     public Appendable append(CharSequence csq) { return accountChars(csq.length()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 656 */     public Appendable append(CharSequence csq, int start, int end) { return accountChars(end - start); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 661 */     public Appendable append(char c) { return accountChars(1); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\GsonHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */