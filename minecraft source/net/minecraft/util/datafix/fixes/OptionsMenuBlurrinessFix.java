/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsMenuBlurrinessFix extends DataFix {
/* 10 */   public OptionsMenuBlurrinessFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 15 */     return fixTypeEverywhereTyped("OptionsMenuBlurrinessFix", 
/* 16 */         getInputSchema().getType(References.OPTIONS), input -> 
/* 17 */         input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int convertToIntRange(String floatBlurriness) {
/*    */     try {
/* 28 */       return Math.round(Float.parseFloat(floatBlurriness) * 10.0F);
/* 29 */     } catch (NumberFormatException e) {
/* 30 */       return 5;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsMenuBlurrinessFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */