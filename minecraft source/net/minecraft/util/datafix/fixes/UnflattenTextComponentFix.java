/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class UnflattenTextComponentFix extends DataFix {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/* 21 */   public UnflattenTextComponentFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<Pair<String, String>> textComponentType = getInputSchema().getType(References.TEXT_COMPONENT);
/* 28 */     Type<?> newTextComponentType = getOutputSchema().getType(References.TEXT_COMPONENT);
/* 29 */     return createFixer(textComponentType, newTextComponentType);
/*    */   }
/*    */   
/*    */   private <T> TypeRewriteRule createFixer(Type<Pair<String, String>> textComponentType, Type<T> newTextComponentType) {
/* 33 */     return fixTypeEverywhere("UnflattenTextComponentFix", textComponentType, newTextComponentType, ops -> ());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> unflattenJson(DynamicOps<T> ops, String jsonString) {
/*    */     try {
/* 40 */       JsonElement json = LenientJsonParser.parse(jsonString);
/* 41 */       if (!json.isJsonNull()) {
/* 42 */         return new Dynamic(ops, JsonOps.INSTANCE.convertTo(ops, json));
/*    */       }
/* 44 */     } catch (Exception e) {
/* 45 */       LOGGER.error("Failed to unflatten text component json: {}", jsonString, e);
/*    */     } 
/* 47 */     return new Dynamic(ops, ops.createString(jsonString));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\UnflattenTextComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */