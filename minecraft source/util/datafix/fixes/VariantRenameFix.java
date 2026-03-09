/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class VariantRenameFix extends NamedEntityFix {
/*    */   private final Map<String, String> renames;
/*    */   
/*    */   public VariantRenameFix(Schema outputSchema, String name, DSL.TypeReference type, String entityName, Map<String, String> renames) {
/* 14 */     super(outputSchema, false, name, type, entityName);
/* 15 */     this.renames = renames;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> typed) {
/* 20 */     return typed.update(DSL.remainderFinder(), remainder -> 
/* 21 */         remainder.update("variant", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\VariantRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */