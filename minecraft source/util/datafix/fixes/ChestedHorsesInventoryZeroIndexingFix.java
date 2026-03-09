/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ChestedHorsesInventoryZeroIndexingFix extends DataFix {
/* 16 */   public ChestedHorsesInventoryZeroIndexingFix(Schema v3807) { super(v3807, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 21 */     OpticFinder<Pair<String, Pair<Either<Pair<String, String>, Unit>, Pair<Either<?, Unit>, Dynamic<?>>>>> itemStackFinder = DSL.typeFinder(getInputSchema().getType(References.ITEM_STACK));
/* 22 */     Type<?> entityType = getInputSchema().getType(References.ENTITY);
/*    */     
/* 24 */     return TypeRewriteRule.seq(
/* 25 */         horseLikeInventoryIndexingFixer(itemStackFinder, entityType, "minecraft:llama"), new TypeRewriteRule[] {
/* 26 */           horseLikeInventoryIndexingFixer(itemStackFinder, entityType, "minecraft:trader_llama"), 
/* 27 */           horseLikeInventoryIndexingFixer(itemStackFinder, entityType, "minecraft:mule"), 
/* 28 */           horseLikeInventoryIndexingFixer(itemStackFinder, entityType, "minecraft:donkey")
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private TypeRewriteRule horseLikeInventoryIndexingFixer(OpticFinder<Pair<String, Pair<Either<Pair<String, String>, Unit>, Pair<Either<?, Unit>, Dynamic<?>>>>> itemStackFinder, Type<?> schema, String horseId) {
/* 34 */     Type<?> choiceType = getInputSchema().getChoiceType(References.ENTITY, horseId);
/* 35 */     OpticFinder<?> entityFinder = DSL.namedChoice(horseId, choiceType);
/* 36 */     OpticFinder<?> itemsFieldFinder = choiceType.findField("Items");
/* 37 */     return fixTypeEverywhereTyped("Fix non-zero indexing in chest horse type " + horseId, schema, input -> 
/* 38 */         input.updateTyped(entityFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChestedHorsesInventoryZeroIndexingFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */