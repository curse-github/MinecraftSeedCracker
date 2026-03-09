/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class LevelUUIDFix
/*    */   extends AbstractUUIDFix {
/* 16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/* 19 */   public LevelUUIDFix(Schema outputSchema) { super(outputSchema, References.LEVEL); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type<?> type = getInputSchema().getType(this.typeReference);
/*    */     
/* 26 */     OpticFinder<?> customBossEventsF = type.findField("CustomBossEvents");
/* 27 */     OpticFinder<?> customBossEventF = DSL.typeFinder(DSL.and(
/* 28 */           DSL.optional(DSL.field("Name", getInputSchema().getTypeRaw(References.TEXT_COMPONENT))), 
/* 29 */           DSL.remainderType()));
/*    */ 
/*    */     
/* 32 */     return fixTypeEverywhereTyped("LevelUUIDFix", type, input -> 
/* 33 */         input
/* 34 */         .update(DSL.remainderFinder(), ())
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 39 */         .updateTyped(customBossEventsF, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 46 */   private Dynamic<?> updateWanderingTrader(Dynamic<?> tag) { return (Dynamic)replaceUUIDString(tag, "WanderingTraderId", "WanderingTraderId").orElse(tag); }
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateDragonFight(Dynamic<?> tag) {
/* 50 */     return tag.update("DimensionData", dimensionDataMap -> 
/* 51 */         dimensionDataMap.updateMapValues(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateCustomBossEvent(Dynamic<?> tag) {
/* 60 */     return tag.update("Players", players -> 
/* 61 */         tag.createList(players.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LevelUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */