/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ObjectiveRenderTypeFix extends DataFix {
/* 13 */   public ObjectiveRenderTypeFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   private static String getRenderType(String criteriaName) { return criteriaName.equals("health") ? "hearts" : "integer"; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     Type<?> objectiveType = getInputSchema().getType(References.OBJECTIVE);
/* 23 */     return fixTypeEverywhereTyped("ObjectiveRenderTypeFix", objectiveType, typed -> 
/* 24 */         typed.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ObjectiveRenderTypeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */