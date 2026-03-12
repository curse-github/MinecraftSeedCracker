/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public abstract class NamedEntityFix extends DataFix {
/*    */   private final String name;
/*    */   protected final String entityName;
/*    */   protected final DSL.TypeReference type;
/*    */   
/*    */   public NamedEntityFix(Schema outputSchema, boolean changesType, String name, DSL.TypeReference type, String entityName) {
/* 16 */     super(outputSchema, changesType);
/* 17 */     this.name = name;
/* 18 */     this.type = type;
/* 19 */     this.entityName = entityName;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 24 */     OpticFinder<?> entityF = DSL.namedChoice(this.entityName, getInputSchema().getChoiceType(this.type, this.entityName));
/*    */     
/* 26 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(this.type), getOutputSchema().getType(this.type), input -> 
/* 27 */         input.updateTyped(entityF, getOutputSchema().getChoiceType(this.type, this.entityName), this::fix));
/*    */   }
/*    */   
/*    */   protected abstract Typed<?> fix(Typed<?> paramTyped);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\NamedEntityFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */