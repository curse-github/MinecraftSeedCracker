/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class BedItemColorFix
/*    */   extends DataFix
/*    */ {
/* 18 */   public BedItemColorFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 25 */     return fixTypeEverywhereTyped("BedItemColorFix", getInputSchema().getType(References.ITEM_STACK), input -> {
/* 26 */           Optional<Pair<String, String>> idOpt = input.getOptional(idF);
/* 27 */           if (idOpt.isPresent() && Objects.equals(((Pair)idOpt.get()).getSecond(), "minecraft:bed")) {
/* 28 */             Dynamic<?> tag = (Dynamic)input.get(DSL.remainderFinder());
/* 29 */             if (tag.get("Damage").asInt(0) == 0) {
/* 30 */               return input.set(DSL.remainderFinder(), tag.set("Damage", tag.createShort((short)14)));
/*    */             }
/*    */           } 
/* 33 */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BedItemColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */