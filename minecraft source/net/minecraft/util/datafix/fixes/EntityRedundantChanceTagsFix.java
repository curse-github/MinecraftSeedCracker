/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityRedundantChanceTagsFix extends DataFix {
/* 13 */   private static final Codec<List<Float>> FLOAT_LIST_CODEC = Codec.FLOAT.listOf();
/*    */ 
/*    */   
/* 16 */   public EntityRedundantChanceTagsFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 21 */     return fixTypeEverywhereTyped("EntityRedundantChanceTagsFix", getInputSchema().getType(References.ENTITY), input -> input.update(DSL.remainderFinder(), ()));
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
/*    */ 
/*    */   
/* 34 */   private static boolean isZeroList(OptionalDynamic<?> element, int size) { Objects.requireNonNull(FLOAT_LIST_CODEC); return ((Boolean)element.flatMap(FLOAT_LIST_CODEC::parse).map(floats -> Boolean.valueOf((floats.size() == size && floats.stream().allMatch(())))).result().orElse(Boolean.valueOf(false))).booleanValue(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityRedundantChanceTagsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */