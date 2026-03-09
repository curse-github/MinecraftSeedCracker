/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonIOException;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.io.Reader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LenientJsonParser
/*    */ {
/* 18 */   public static JsonElement parse(Reader reader) throws JsonIOException, JsonSyntaxException { return JsonParser.parseReader(reader); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static JsonElement parse(String json) throws JsonSyntaxException { return JsonParser.parseString(json); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\LenientJsonParser.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */