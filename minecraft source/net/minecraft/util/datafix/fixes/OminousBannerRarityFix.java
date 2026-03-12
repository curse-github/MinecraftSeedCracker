/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class OminousBannerRarityFix extends DataFix {
/* 18 */   public OminousBannerRarityFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 23 */     Type<?> blockEntityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 24 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 25 */     TaggedChoice.TaggedChoiceType<?> blockEntityIdFinder = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 26 */     OpticFinder<Pair<String, String>> itemStackIdFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 27 */     OpticFinder<?> blockEntityComponentsFieldFinder = blockEntityType.findField("components");
/* 28 */     OpticFinder<?> itemStackComponentsFieldFinder = itemStackType.findField("components");
/*    */     
/* 30 */     OpticFinder<?> itemNameFinder = blockEntityComponentsFieldFinder.type().findField("minecraft:item_name");
/*    */     
/* 32 */     OpticFinder<Pair<String, String>> textComponentFinder = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/*    */     
/* 34 */     return TypeRewriteRule.seq(
/* 35 */         fixTypeEverywhereTyped("Ominous Banner block entity common rarity to uncommon rarity fix", blockEntityType, input -> {
/* 36 */             Object blockEntityId = ((Pair)input.get(blockEntityIdFinder.finder())).getFirst();
/* 37 */             return blockEntityId.equals("minecraft:banner") ? fix(input, blockEntityComponentsFieldFinder, itemNameFinder, textComponentFinder) : input;
/*    */           
/* 39 */           }), fixTypeEverywhereTyped("Ominous Banner item stack common rarity to uncommon rarity fix", itemStackType, input -> {
/* 40 */             String itemStackId = (String)input.getOptional(itemStackIdFinder).map(Pair::getSecond).orElse("");
/* 41 */             return itemStackId.equals("minecraft:white_banner") ? fix(input, itemStackComponentsFieldFinder, itemNameFinder, textComponentFinder) : input;
/*    */           }));
/*    */   }
/*    */ 
/*    */   
/*    */   private Typed<?> fix(Typed<?> input, OpticFinder<?> componentsFieldFinder, OpticFinder<?> itemNameFinder, OpticFinder<Pair<String, String>> textComponentFinder) {
/* 47 */     return input.updateTyped(componentsFieldFinder, components -> {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 53 */           boolean isOminousBanner = components.getOptionalTyped(itemNameFinder).flatMap(()).map(Pair::getSecond).flatMap(LegacyComponentDataFixUtils::extractTranslationString).filter(()).isPresent();
/*    */           
/* 55 */           if (isOminousBanner) {
/* 56 */             return components
/* 57 */               .updateTyped(itemNameFinder, ())
/*    */ 
/*    */ 
/*    */               
/* 61 */               .update(DSL.remainderFinder(), ());
/*    */           }
/*    */           
/* 64 */           return components;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\OminousBannerRarityFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */