/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RenameEnchantmentsFix extends DataFix {
/*    */   final String name;
/*    */   
/*    */   public RenameEnchantmentsFix(Schema outputSchema, String name, Map<String, String> renames) {
/* 20 */     super(outputSchema, false);
/* 21 */     this.name = name;
/* 22 */     this.renames = renames;
/*    */   }
/*    */   final Map<String, String> renames;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> item = getInputSchema().getType(References.ITEM_STACK);
/* 28 */     OpticFinder<?> tagFinder = item.findField("tag");
/* 29 */     return fixTypeEverywhereTyped(this.name, item, input -> input.updateTyped(tagFinder, ()));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> tag) {
/* 33 */     tag = fixEnchantmentList(tag, "Enchantments");
/* 34 */     return fixEnchantmentList(tag, "StoredEnchantments");
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixEnchantmentList(Dynamic<?> itemStack, String field) {
/* 39 */     return itemStack.update(field, tag -> {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 46 */           Objects.requireNonNull(tag); return (Dynamic)tag.asStreamOpt().map(()).map(tag::createList).mapOrElse(Function.identity(), ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\RenameEnchantmentsFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */