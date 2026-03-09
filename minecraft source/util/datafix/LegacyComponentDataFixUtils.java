/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import net.minecraft.util.StrictJsonParser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LegacyComponentDataFixUtils
/*    */ {
/* 23 */   private static final String EMPTY_CONTENTS = createTextComponentJson("");
/*    */   
/*    */   public static <T> Dynamic<T> createPlainTextComponent(DynamicOps<T> ops, String text) {
/* 26 */     String stableString = createTextComponentJson(text);
/* 27 */     return new Dynamic(ops, ops.createString(stableString));
/*    */   }
/*    */ 
/*    */   
/* 31 */   public static <T> Dynamic<T> createEmptyComponent(DynamicOps<T> ops) { return new Dynamic(ops, ops.createString(EMPTY_CONTENTS)); }
/*    */ 
/*    */   
/*    */   public static String createTextComponentJson(String text) {
/* 35 */     JsonObject result = new JsonObject();
/* 36 */     result.addProperty("text", text);
/* 37 */     return GsonHelper.toStableString(result);
/*    */   }
/*    */   
/*    */   public static String createTranslatableComponentJson(String key) {
/* 41 */     JsonObject result = new JsonObject();
/* 42 */     result.addProperty("translate", key);
/* 43 */     return GsonHelper.toStableString(result);
/*    */   }
/*    */   
/*    */   public static <T> Dynamic<T> createTranslatableComponent(DynamicOps<T> ops, String key) {
/* 47 */     String stableString = createTranslatableComponentJson(key);
/* 48 */     return new Dynamic(ops, ops.createString(stableString));
/*    */   }
/*    */   
/*    */   public static String rewriteFromLenient(String string) {
/* 52 */     if (string.isEmpty() || string.equals("null")) {
/* 53 */       return EMPTY_CONTENTS;
/*    */     }
/*    */     
/* 56 */     char firstChar = string.charAt(0);
/* 57 */     char lastChar = string.charAt(string.length() - 1);
/* 58 */     if ((firstChar == '"' && lastChar == '"') || (firstChar == '{' && lastChar == '}') || (firstChar == '[' && lastChar == ']')) {
/*    */       try {
/* 60 */         JsonElement json = LenientJsonParser.parse(string);
/* 61 */         if (json.isJsonPrimitive()) {
/* 62 */           return createTextComponentJson(json.getAsString());
/*    */         }
/* 64 */         return GsonHelper.toStableString(json);
/* 65 */       } catch (JsonParseException jsonParseException) {}
/*    */     }
/*    */ 
/*    */     
/* 69 */     return createTextComponentJson(string);
/*    */   }
/*    */ 
/*    */   
/* 73 */   public static boolean isStrictlyValidJson(Dynamic<?> component) { return component.asString().result()
/* 74 */       .filter(string -> {
/*    */           try {
/* 76 */             StrictJsonParser.parse(string);
/* 77 */             return true;
/* 78 */           } catch (JsonParseException ignored) {
/* 79 */             return false;
/*    */           }
/*    */         
/* 82 */         }).isPresent(); }
/*    */ 
/*    */   
/*    */   public static Optional<String> extractTranslationString(String component) {
/*    */     try {
/* 87 */       JsonElement parsed = LenientJsonParser.parse(component);
/* 88 */       if (parsed.isJsonObject()) {
/* 89 */         JsonObject parsedObject = parsed.getAsJsonObject();
/* 90 */         JsonElement key = parsedObject.get("translate");
/* 91 */         if (key != null && key.isJsonPrimitive()) {
/* 92 */           return Optional.of(key.getAsString());
/*    */         }
/*    */       } 
/* 95 */     } catch (JsonParseException jsonParseException) {}
/*    */ 
/*    */     
/* 98 */     return Optional.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\LegacyComponentDataFixUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */