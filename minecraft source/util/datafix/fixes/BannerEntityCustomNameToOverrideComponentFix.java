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
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class BannerEntityCustomNameToOverrideComponentFix
/*    */   extends DataFix
/*    */ {
/* 21 */   public BannerEntityCustomNameToOverrideComponentFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 26 */     Type<?> blockEntityType = getInputSchema().getType(References.BLOCK_ENTITY);
/* 27 */     TaggedChoice.TaggedChoiceType<?> blockEntityIdFinder = getInputSchema().findChoiceType(References.BLOCK_ENTITY);
/* 28 */     OpticFinder<?> customNameFinder = blockEntityType.findField("CustomName");
/*    */     
/* 30 */     OpticFinder<Pair<String, String>> textComponentFinder = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/*    */     
/* 32 */     return fixTypeEverywhereTyped("Banner entity custom_name to item_name component fix", blockEntityType, input -> {
/* 33 */           Object blockEntityId = ((Pair)input.get(blockEntityIdFinder.finder())).getFirst();
/* 34 */           return blockEntityId.equals("minecraft:banner") ? fix(input, textComponentFinder, customNameFinder) : input;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> fix(Typed<?> input, OpticFinder<Pair<String, String>> textComponentFinder, OpticFinder<?> customNameFinder) {
/* 44 */     Optional<String> customName = input.getOptionalTyped(customNameFinder).flatMap(name -> name.getOptional(textComponentFinder).map(Pair::getSecond));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 49 */     boolean isOminousBanner = customName.flatMap(LegacyComponentDataFixUtils::extractTranslationString).filter(e -> e.equals("block.minecraft.ominous_banner")).isPresent();
/*    */     
/* 51 */     if (isOminousBanner) {
/* 52 */       return Util.writeAndReadTypedOrThrow(input, input.getType(), dynamic -> {
/* 53 */             Dynamic<?> components = dynamic.createMap(Map.of(dynamic
/* 54 */                   .createString("minecraft:item_name"), dynamic.createString((String)customName.get()), dynamic
/* 55 */                   .createString("minecraft:hide_additional_tooltip"), dynamic.emptyMap()));
/*    */             
/* 57 */             return dynamic.set("components", components).remove("CustomName");
/*    */           });
/*    */     }
/* 60 */     return input;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BannerEntityCustomNameToOverrideComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */