/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsGraphicsModeSplitFix extends DataFix {
/*    */   private final String newFieldName;
/*    */   private final String valueIfFast;
/*    */   private final String valueIfFancy;
/*    */   private final String valueIfFabulous;
/*    */   
/*    */   public OptionsGraphicsModeSplitFix(Schema outputSchema, String newFieldName, String valueIfFast, String valueIfFancy, String valueIfFabulous) {
/* 16 */     super(outputSchema, true);
/* 17 */     this.newFieldName = newFieldName;
/* 18 */     this.valueIfFast = valueIfFast;
/* 19 */     this.valueIfFancy = valueIfFancy;
/* 20 */     this.valueIfFabulous = valueIfFabulous;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     return fixTypeEverywhereTyped("graphicsMode split to " + this.newFieldName, getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String getValue(String mode) {
/* 34 */     switch (mode) { case "2": case "0":  }  return 
/*    */ 
/*    */       
/* 37 */       this.valueIfFancy;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsGraphicsModeSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */