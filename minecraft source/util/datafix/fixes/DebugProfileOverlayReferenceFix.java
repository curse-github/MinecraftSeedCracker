/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class DebugProfileOverlayReferenceFix extends DataFix {
/* 10 */   public DebugProfileOverlayReferenceFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("DebugProfileOverlayReferenceFix", 
/* 16 */         getInputSchema().getType(References.DEBUG_PROFILE), typed -> 
/* 17 */         typed.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\DebugProfileOverlayReferenceFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */