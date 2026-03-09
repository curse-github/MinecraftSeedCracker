/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BlockNameFlatteningFix
/*    */   extends DataFix
/*    */ {
/* 18 */   public BlockNameFlatteningFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     Type<?> blockType = getInputSchema().getType(References.BLOCK_NAME);
/* 24 */     Type<?> newBlockType = getOutputSchema().getType(References.BLOCK_NAME);
/*    */     
/* 26 */     Type<Pair<String, Either<Integer, String>>> expectedBlockType = DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), NamespacedSchema.namespacedString()));
/* 27 */     Type<Pair<String, String>> expectedNewBlockType = DSL.named(References.BLOCK_NAME.typeName(), NamespacedSchema.namespacedString());
/*    */     
/* 29 */     if (!Objects.equals(blockType, expectedBlockType) || !Objects.equals(newBlockType, expectedNewBlockType)) {
/* 30 */       throw new IllegalStateException("Expected and actual types don't match.");
/*    */     }
/* 32 */     return fixTypeEverywhere("BlockNameFlatteningFix", expectedBlockType, expectedNewBlockType, ops -> ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockNameFlatteningFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */