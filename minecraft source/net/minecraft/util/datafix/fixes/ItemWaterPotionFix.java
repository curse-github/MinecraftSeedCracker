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
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ 
/*    */ public class ItemWaterPotionFix
/*    */   extends DataFix
/*    */ {
/* 19 */   public ItemWaterPotionFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 24 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 26 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 27 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/*    */     
/* 29 */     return fixTypeEverywhereTyped("ItemWaterPotionFix", itemStackType, input -> {
/* 30 */           Optional<Pair<String, String>> idOpt = input.getOptional(idF);
/* 31 */           if (idOpt.isPresent()) {
/* 32 */             String id = (String)((Pair)idOpt.get()).getSecond();
/* 33 */             if ("minecraft:potion".equals(id) || "minecraft:splash_potion"
/* 34 */               .equals(id) || "minecraft:lingering_potion"
/* 35 */               .equals(id) || "minecraft:tipped_arrow"
/* 36 */               .equals(id)) {
/*    */               
/* 38 */               Typed<?> tag = input.getOrCreateTyped(tagF);
/* 39 */               Dynamic<?> tagRest = (Dynamic)tag.get(DSL.remainderFinder());
/* 40 */               if (tagRest.get("Potion").asString().result().isEmpty()) {
/* 41 */                 tagRest = tagRest.set("Potion", tagRest.createString("minecraft:water"));
/*    */               }
/* 43 */               return input.set(tagF, tag.set(DSL.remainderFinder(), tagRest));
/*    */             } 
/*    */           } 
/* 46 */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemWaterPotionFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */