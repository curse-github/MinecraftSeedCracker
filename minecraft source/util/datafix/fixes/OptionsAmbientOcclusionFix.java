/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsAmbientOcclusionFix extends DataFix {
/* 11 */   public OptionsAmbientOcclusionFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 16 */     return fixTypeEverywhereTyped("OptionsAmbientOcclusionFix", getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static String updateValue(String value) {
/* 22 */     switch (value) { case "0": case "1": case "2":  }  return 
/*    */ 
/*    */       
/* 25 */       value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsAmbientOcclusionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */