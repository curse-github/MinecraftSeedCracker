/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class AttributesRenameLegacy extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AttributesRenameLegacy(Schema outputSchema, String name, UnaryOperator<String> renames) {
/* 20 */     super(outputSchema, false);
/* 21 */     this.name = name;
/* 22 */     this.renames = renames;
/*    */   }
/*    */   private final UnaryOperator<String> renames;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/* 28 */     OpticFinder<?> tagF = itemStackType.findField("tag");
/* 29 */     return TypeRewriteRule.seq(
/* 30 */         fixTypeEverywhereTyped(this.name + " (ItemStack)", itemStackType, itemStack -> 
/* 31 */           itemStack.updateTyped(tagF, this::fixItemStackTag)), new TypeRewriteRule[] {
/*    */           
/* 33 */           fixTypeEverywhereTyped(this.name + " (Entity)", getInputSchema().getType(References.ENTITY), this::fixEntity), 
/* 34 */           fixTypeEverywhereTyped(this.name + " (Player)", getInputSchema().getType(References.PLAYER), this::fixEntity)
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 39 */   private Dynamic<?> fixName(Dynamic<?> name) { Objects.requireNonNull(name); return (Dynamic)DataFixUtils.orElse(name.asString().result().map(this.renames).map(name::createString), name); }
/*    */ 
/*    */   
/*    */   private Typed<?> fixItemStackTag(Typed<?> itemStack) {
/* 43 */     return itemStack.update(DSL.remainderFinder(), tag -> 
/* 44 */         tag.update("AttributeModifiers", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> fixEntity(Typed<?> entity) {
/* 51 */     return entity.update(DSL.remainderFinder(), tag -> 
/* 52 */         tag.update("Attributes", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AttributesRenameLegacy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */