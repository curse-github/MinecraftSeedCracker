/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonIOException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import com.google.gson.Strictness;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonToken;
/*    */ import com.google.gson.stream.MalformedJsonException;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.io.StringReader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StrictJsonParser
/*    */ {
/*    */   public static JsonElement parse(Reader reader) throws JsonIOException, JsonSyntaxException {
/*    */     try {
/* 29 */       JsonReader jsonReader = new JsonReader(reader);
/* 30 */       jsonReader.setStrictness(Strictness.STRICT);
/* 31 */       JsonElement element = JsonParser.parseReader(jsonReader);
/* 32 */       if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
/* 33 */         throw new JsonSyntaxException("Did not consume the entire document.");
/*    */       }
/* 35 */       return element;
/* 36 */     } catch (MalformedJsonException|NumberFormatException e) {
/* 37 */       throw new JsonSyntaxException(e);
/* 38 */     } catch (IOException e) {
/* 39 */       throw new JsonIOException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public static JsonElement parse(String json) throws JsonSyntaxException { return parse(new StringReader(json)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\StrictJsonParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */