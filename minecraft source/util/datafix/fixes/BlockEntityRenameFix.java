/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class BlockEntityRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private BlockEntityRenameFix(Schema outputSchema, String name, UnaryOperator<String> nameChangeLookup) {
/* 16 */     super(outputSchema, true);
/* 17 */     this.name = name;
/* 18 */     this.nameChangeLookup = nameChangeLookup;
/*    */   }
/*    */   private final UnaryOperator<String> nameChangeLookup;
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     TaggedChoice.TaggedChoiceType<String> oldType = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 24 */     TaggedChoice.TaggedChoiceType<String> newType = getOutputSchema().findChoiceType(References.BLOCK_ENTITY);
/*    */     
/* 26 */     return fixTypeEverywhere(this.name, oldType, newType, ops -> ());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static DataFix create(Schema outputSchema, String name, UnaryOperator<String> nameChangeLookup) { return new BlockEntityRenameFix(outputSchema, name, nameChangeLookup); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */