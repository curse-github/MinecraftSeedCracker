/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class TextComponentStringifiedFlagsFix
/*    */   extends DataFix
/*    */ {
/* 17 */   public TextComponentStringifiedFlagsFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 23 */     Type<Pair<String, Either<?, Pair<?, Pair<?, Pair<?, Dynamic<?>>>>>>> textComponentType = getInputSchema().getType(References.TEXT_COMPONENT);
/* 24 */     return fixTypeEverywhere("TextComponentStringyFlagsFix", textComponentType, ops -> ());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> stringToBool(Dynamic<T> input) {
/* 36 */     Optional<String> string = input.asString().result();
/* 37 */     if (string.isPresent()) {
/* 38 */       return input.createBoolean(Boolean.parseBoolean((String)string.get()));
/*    */     }
/* 40 */     return input;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\TextComponentStringifiedFlagsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */