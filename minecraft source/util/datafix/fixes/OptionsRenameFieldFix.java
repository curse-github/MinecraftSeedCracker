/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsRenameFieldFix extends DataFix {
/*    */   private final String fixName;
/*    */   
/*    */   public OptionsRenameFieldFix(Schema outputSchema, boolean changesType, String fixName, String fieldFrom, String fieldTo) {
/* 14 */     super(outputSchema, changesType);
/* 15 */     this.fixName = fixName;
/* 16 */     this.fieldFrom = fieldFrom;
/* 17 */     this.fieldTo = fieldTo;
/*    */   }
/*    */   private final String fieldFrom; private final String fieldTo;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.fixName, getInputSchema().getType(References.OPTIONS), input -> 
/* 23 */         input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\OptionsRenameFieldFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */