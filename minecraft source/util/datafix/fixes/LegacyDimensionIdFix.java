/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class LegacyDimensionIdFix extends DataFix {
/* 14 */   public LegacyDimensionIdFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 19 */     TypeRewriteRule playerRule = fixTypeEverywhereTyped("PlayerLegacyDimensionFix", getInputSchema().getType(References.PLAYER), input -> 
/* 20 */         input.update(DSL.remainderFinder(), this::fixPlayer));
/*    */ 
/*    */     
/* 23 */     Type<?> dataType = getInputSchema().getType(References.SAVED_DATA_MAP_DATA);
/* 24 */     OpticFinder<?> mapDataF = dataType.findField("data");
/*    */     
/* 26 */     TypeRewriteRule mapRule = fixTypeEverywhereTyped("MapLegacyDimensionFix", dataType, input -> 
/* 27 */         input.updateTyped(mapDataF, ()));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     return TypeRewriteRule.seq(playerRule, mapRule);
/*    */   }
/*    */ 
/*    */   
/* 36 */   private <T> Dynamic<T> fixMap(Dynamic<T> remainder) { return remainder.update("dimension", this::fixDimensionId); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   private <T> Dynamic<T> fixPlayer(Dynamic<T> remainder) { return remainder.update("Dimension", this::fixDimensionId); }
/*    */ 
/*    */   
/*    */   private <T> Dynamic<T> fixDimensionId(Dynamic<T> id) {
/* 44 */     return (Dynamic)DataFixUtils.orElse(id
/* 45 */         .asNumber().result().map(legacyId -> {
/* 46 */             switch (legacyId.intValue()) { case -1: case 1:  }  return 
/*    */ 
/*    */               
/* 49 */               id.createString("minecraft:overworld");
/*    */           }), id);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LegacyDimensionIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */