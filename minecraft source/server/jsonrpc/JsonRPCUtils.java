/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.GsonHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonRPCUtils
/*    */ {
/*    */   public static final String JSON_RPC_VERSION = "2.0";
/*    */   public static final String OPEN_RPC_VERSION = "1.3.2";
/*    */   
/*    */   public static JsonObject createSuccessResult(JsonElement id, JsonElement result) {
/* 18 */     JsonObject response = new JsonObject();
/* 19 */     response.addProperty("jsonrpc", "2.0");
/* 20 */     response.add("id", id);
/* 21 */     response.add("result", result);
/* 22 */     return response;
/*    */   }
/*    */   
/*    */   public static JsonObject createRequest(Integer id, Identifier method, List<JsonElement> params) {
/* 26 */     JsonObject request = new JsonObject();
/* 27 */     request.addProperty("jsonrpc", "2.0");
/* 28 */     if (id != null) {
/* 29 */       request.addProperty("id", id);
/*    */     }
/* 31 */     request.addProperty("method", method.toString());
/* 32 */     if (!params.isEmpty()) {
/* 33 */       JsonArray jsonArray = new JsonArray(params.size());
/* 34 */       for (JsonElement param : params) {
/* 35 */         jsonArray.add(param);
/*    */       }
/* 37 */       request.add("params", jsonArray);
/*    */     } 
/* 39 */     return request;
/*    */   }
/*    */   
/*    */   public static JsonObject createError(JsonElement id, String message, int errorCode, String data) {
/* 43 */     JsonObject errorResponse = new JsonObject();
/* 44 */     errorResponse.addProperty("jsonrpc", "2.0");
/* 45 */     errorResponse.add("id", id);
/* 46 */     JsonObject error = new JsonObject();
/* 47 */     error.addProperty("code", Integer.valueOf(errorCode));
/* 48 */     error.addProperty("message", message);
/* 49 */     if (data != null && !data.isBlank()) {
/* 50 */       error.addProperty("data", data);
/*    */     }
/* 52 */     errorResponse.add("error", error);
/* 53 */     return errorResponse;
/*    */   }
/*    */ 
/*    */   
/* 57 */   public static JsonElement getRequestId(JsonObject jsonObject) { return jsonObject.get("id"); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static String getMethodName(JsonObject jsonObject) { return GsonHelper.getAsString(jsonObject, "method", null); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public static JsonElement getParams(JsonObject jsonObject) { return jsonObject.get("params"); }
/*    */ 
/*    */ 
/*    */   
/* 69 */   public static JsonElement getResult(JsonObject jsonObject) { return jsonObject.get("result"); }
/*    */ 
/*    */ 
/*    */   
/* 73 */   public static JsonObject getError(JsonObject jsonObject) { return GsonHelper.getAsJsonObject(jsonObject, "error", null); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\JsonRPCUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */