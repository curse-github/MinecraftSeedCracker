/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class NamespacedTypeRenameFix
/*    */   extends DataFix {
/*    */   private final String name;
/*    */   private final DSL.TypeReference type;
/*    */   private final UnaryOperator<String> renamer;
/*    */   
/*    */   public NamespacedTypeRenameFix(Schema outputSchema, String name, DSL.TypeReference type, UnaryOperator<String> renamer) {
/* 22 */     super(outputSchema, false);
/* 23 */     this.name = name;
/* 24 */     this.type = type;
/* 25 */     this.renamer = renamer;
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 30 */     Type<Pair<String, String>> fieldType = DSL.named(this.type.typeName(), NamespacedSchema.namespacedString());
/* 31 */     if (!Objects.equals(fieldType, getInputSchema().getType(this.type))) {
/* 32 */       throw new IllegalStateException("\"" + this.type.typeName() + "\" is not what was expected.");
/*    */     }
/* 34 */     return fixTypeEverywhere(this.name, fieldType, ops -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\NamespacedTypeRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */