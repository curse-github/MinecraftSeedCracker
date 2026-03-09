/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ 
/*    */ public class FixProjectileStoredItem
/*    */   extends DataFix
/*    */ {
/*    */   private static final String EMPTY_POTION = "minecraft:empty";
/*    */   
/* 22 */   public FixProjectileStoredItem(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> inputEntityType = getInputSchema().getType(References.ENTITY);
/* 28 */     Type<?> outputEntityType = getOutputSchema().getType(References.ENTITY);
/*    */     
/* 30 */     return fixTypeEverywhereTyped("Fix AbstractArrow item type", inputEntityType, outputEntityType, ExtraDataFixUtils.chainAllFilters(new Function[] {
/* 31 */             fixChoice("minecraft:trident", FixProjectileStoredItem::castUnchecked), 
/* 32 */             fixChoice("minecraft:arrow", FixProjectileStoredItem::fixArrow), 
/* 33 */             fixChoice("minecraft:spectral_arrow", FixProjectileStoredItem::fixSpectralArrow)
/*    */           }));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Function<Typed<?>, Typed<?>> fixChoice(String entityName, SubFixer<?> fixer) {
/* 42 */     Type<?> inputEntityChoiceType = getInputSchema().getChoiceType(References.ENTITY, entityName);
/* 43 */     Type<?> outputEntityChoiceType = getOutputSchema().getChoiceType(References.ENTITY, entityName);
/*    */     
/* 45 */     return fixChoiceCap(entityName, fixer, inputEntityChoiceType, outputEntityChoiceType);
/*    */   }
/*    */   
/*    */   private static <T> Function<Typed<?>, Typed<?>> fixChoiceCap(String entityName, SubFixer<?> fixer, Type<?> inputEntityChoiceType, Type<T> outputEntityChoiceType) {
/* 49 */     OpticFinder<?> entityF = DSL.namedChoice(entityName, inputEntityChoiceType);
/* 50 */     SubFixer<T> typedFixer = fixer;
/* 51 */     return input -> input.updateTyped(entityF, outputEntityChoiceType, ());
/*    */   }
/*    */   
/*    */   private static <T> Typed<T> fixArrow(Typed<?> typed, Type<T> outputType) {
/* 55 */     return Util.writeAndReadTypedOrThrow(typed, outputType, input -> 
/* 56 */         input.set("item", createItemStack(input, getArrowType(input))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 61 */   private static String getArrowType(Dynamic<?> input) { return input.get("Potion").asString("minecraft:empty").equals("minecraft:empty") ? "minecraft:arrow" : "minecraft:tipped_arrow"; }
/*    */ 
/*    */   
/*    */   private static <T> Typed<T> fixSpectralArrow(Typed<?> typed, Type<T> outputType) {
/* 65 */     return Util.writeAndReadTypedOrThrow(typed, outputType, input -> 
/* 66 */         input.set("item", createItemStack(input, "minecraft:spectral_arrow")));
/*    */   }
/*    */ 
/*    */   
/*    */   private static Dynamic<?> createItemStack(Dynamic<?> input, String itemName) {
/* 71 */     return input.createMap(ImmutableMap.of(input
/* 72 */           .createString("id"), input.createString(itemName), input
/* 73 */           .createString("Count"), input.createInt(1)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   private static <T> Typed<T> castUnchecked(Typed<?> input, Type<T> outputType) { return new Typed(outputType, input.getOps(), input.getValue()); }
/*    */   
/*    */   private static interface SubFixer<F> {
/*    */     Typed<F> fix(Typed<?> param1Typed, Type<F> param1Type);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\FixProjectileStoredItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */