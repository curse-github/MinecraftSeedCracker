/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ForcedChunkToTicketFix extends DataFix {
/* 11 */   public ForcedChunkToTicketFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("ForcedChunkToTicketFix", getInputSchema().getType(References.SAVED_DATA_TICKETS), input -> 
/* 17 */         input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ForcedChunkToTicketFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */