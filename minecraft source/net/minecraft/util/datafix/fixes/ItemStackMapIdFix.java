/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ 
/*    */ public class ItemStackMapIdFix
/*    */   extends DataFix
/*    */ {
/* 20 */   public ItemStackMapIdFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 27 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 28 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/*    */     
/* 30 */     return fixTypeEverywhereTyped("ItemInstanceMapIdFix", itemStackType, input -> {
/* 31 */           Optional<Pair<String, String>> id = input.getOptional(idF);
/* 32 */           if (id.isPresent() && Objects.equals(((Pair)id.get()).getSecond(), "minecraft:filled_map")) {
/* 33 */             Dynamic<?> rest = (Dynamic)input.get(DSL.remainderFinder());
/* 34 */             Typed<?> tag = input.getOrCreateTyped(tagF);
/* 35 */             Dynamic<?> tagRest = (Dynamic)tag.get(DSL.remainderFinder());
/* 36 */             tagRest = tagRest.set("map", tagRest.createInt(rest.get("Damage").asInt(0)));
/* 37 */             return input.set(tagF, tag.set(DSL.remainderFinder(), tagRest));
/*    */           } 
/* 39 */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackMapIdFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */