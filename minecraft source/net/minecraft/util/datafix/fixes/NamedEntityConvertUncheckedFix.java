/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class NamedEntityConvertUncheckedFix
/*    */   extends NamedEntityFix {
/* 11 */   public NamedEntityConvertUncheckedFix(Schema outputSchema, String name, DSL.TypeReference type, String entityName) { super(outputSchema, true, name, type, entityName); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 17 */     Type<?> outputType = getOutputSchema().getChoiceType(this.type, this.entityName);
/* 18 */     return ExtraDataFixUtils.cast(outputType, entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\NamedEntityConvertUncheckedFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */