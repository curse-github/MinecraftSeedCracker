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
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ 
/*    */ public class VillagerTradeFix
/*    */   extends DataFix
/*    */ {
/* 19 */   public VillagerTradeFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type<?> recipeType = getInputSchema().getType(References.VILLAGER_TRADE);
/* 25 */     OpticFinder<?> buyFinder = recipeType.findField("buy");
/* 26 */     OpticFinder<?> buyBFinder = recipeType.findField("buyB");
/* 27 */     OpticFinder<?> sellFinder = recipeType.findField("sell");
/*    */     
/* 29 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 30 */     Function<Typed<?>, Typed<?>> itemStackUpdater = itemStack -> updateItemStack(idF, itemStack);
/*    */     
/* 32 */     return fixTypeEverywhereTyped("Villager trade fix", recipeType, recipe -> 
/* 33 */         recipe
/* 34 */         .updateTyped(buyFinder, itemStackUpdater)
/* 35 */         .updateTyped(buyBFinder, itemStackUpdater)
/* 36 */         .updateTyped(sellFinder, itemStackUpdater));
/*    */   }
/*    */ 
/*    */   
/*    */   private Typed<?> updateItemStack(OpticFinder<Pair<String, String>> idF, Typed<?> itemStack) {
/* 41 */     return itemStack.update(idF, pair -> 
/* 42 */         pair.mapSecond(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\VillagerTradeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */