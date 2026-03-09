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
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class ItemCustomNameToComponentFix extends DataFix {
/* 14 */   public ItemCustomNameToComponentFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 19 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*    */     
/* 21 */     Type<Pair<String, String>> textComponentType = getInputSchema().getType(References.TEXT_COMPONENT);
/* 22 */     OpticFinder<?> tagFinder = itemStackType.findField("tag");
/* 23 */     OpticFinder<?> displayFinder = tagFinder.type().findField("display");
/* 24 */     OpticFinder<?> customNameFinder = displayFinder.type().findField("Name");
/* 25 */     OpticFinder<Pair<String, String>> textComponentFinder = DSL.typeFinder(textComponentType);
/*    */     
/* 27 */     return fixTypeEverywhereTyped("ItemCustomNameToComponentFix", itemStackType, itemStack -> 
/* 28 */         itemStack.updateTyped(tagFinder, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemCustomNameToComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */