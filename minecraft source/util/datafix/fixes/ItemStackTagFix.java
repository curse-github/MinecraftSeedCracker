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
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public abstract class ItemStackTagFix
/*    */   extends DataFix
/*    */ {
/*    */   private final String name;
/*    */   private final Predicate<String> idFilter;
/*    */   
/*    */   public ItemStackTagFix(Schema outputSchema, String name, Predicate<String> idFilter) {
/* 23 */     super(outputSchema, false);
/* 24 */     this.name = name;
/* 25 */     this.idFilter = idFilter;
/*    */   }
/*    */ 
/*    */   
/*    */   public final TypeRewriteRule makeRule() {
/* 30 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 31 */     return fixTypeEverywhereTyped(this.name, itemStackType, createFixer(itemStackType, this.idFilter, this::fixItemStackTag));
/*    */   }
/*    */   
/*    */   public static UnaryOperator<Typed<?>> createFixer(Type<?> itemStackType, Predicate<String> idFilter, UnaryOperator<Typed<?>> fixer) {
/* 35 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 36 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 37 */     return input -> {
/* 38 */         Optional<Pair<String, String>> idOpt = input.getOptional(idF);
/* 39 */         if (idOpt.isPresent() && idFilter.test((String)((Pair)idOpt.get()).getSecond())) {
/* 40 */           return input.updateTyped(tagF, fixer);
/*    */         }
/* 42 */         return input;
/*    */       };
/*    */   }
/*    */   
/*    */   protected abstract Typed<?> fixItemStackTag(Typed<?> paramTyped);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackTagFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */