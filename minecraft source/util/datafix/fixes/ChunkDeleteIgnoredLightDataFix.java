/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChunkDeleteIgnoredLightDataFix extends DataFix {
/* 12 */   public ChunkDeleteIgnoredLightDataFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 17 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 18 */     OpticFinder<?> sectionsFinder = chunkType.findField("sections");
/*    */     
/* 20 */     return fixTypeEverywhereTyped("ChunkDeleteIgnoredLightDataFix", chunkType, chunk -> {
/* 21 */           boolean isLightOn = ((Dynamic)chunk.get(DSL.remainderFinder())).get("isLightOn").asBoolean(false);
/* 22 */           if (!isLightOn) {
/* 23 */             return chunk.updateTyped(sectionsFinder, ());
/*    */           }
/*    */ 
/*    */           
/* 27 */           return chunk;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkDeleteIgnoredLightDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */