/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class SimplestEntityRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public SimplestEntityRenameFix(String name, Schema outputSchema, boolean changesType) {
/* 20 */     super(outputSchema, changesType);
/* 21 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     TaggedChoice.TaggedChoiceType<String> oldType = getInputSchema().findChoiceType(References.ENTITY);
/* 28 */     TaggedChoice.TaggedChoiceType<String> newType = getOutputSchema().findChoiceType(References.ENTITY);
/*    */     
/* 30 */     Type<Pair<String, String>> entityNameType = DSL.named(References.ENTITY_NAME.typeName(), NamespacedSchema.namespacedString());
/* 31 */     if (!Objects.equals(getOutputSchema().getType(References.ENTITY_NAME), entityNameType)) {
/* 32 */       throw new IllegalStateException("Entity name type is not what was expected.");
/*    */     }
/*    */     
/* 35 */     return TypeRewriteRule.seq(
/* 36 */         fixTypeEverywhere(this.name, oldType, newType, ops -> ()), 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 48 */         fixTypeEverywhere(this.name + " for entity name", entityNameType, ops -> ()));
/*    */   }
/*    */   
/*    */   protected abstract String rename(String paramString);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\SimplestEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */