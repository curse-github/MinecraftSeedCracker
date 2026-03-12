/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ForcePoiRebuild
/*    */   extends DataFix
/*    */ {
/* 17 */   public ForcePoiRebuild(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type<Pair<String, Dynamic<?>>> poiChunkType = DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
/*    */     
/* 24 */     if (!Objects.equals(poiChunkType, getInputSchema().getType(References.POI_CHUNK))) {
/* 25 */       throw new IllegalStateException("Poi type is not what was expected.");
/*    */     }
/* 27 */     return fixTypeEverywhere("POI rebuild", poiChunkType, ops -> ());
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> cap(Dynamic<T> input) {
/* 31 */     return input.update("Sections", sections -> 
/* 32 */         sections.updateMapValues(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ForcePoiRebuild.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */