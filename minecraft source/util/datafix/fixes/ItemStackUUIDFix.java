/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ItemStackUUIDFix
/*    */   extends AbstractUUIDFix
/*    */ {
/* 15 */   public ItemStackUUIDFix(Schema outputSchema) { super(outputSchema, References.ITEM_STACK); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 20 */     OpticFinder<Pair<String, String>> idF = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*    */     
/* 22 */     return fixTypeEverywhereTyped("ItemStackUUIDFix", getInputSchema().getType(this.typeReference), input -> {
/* 23 */           OpticFinder<?> itemTagFinder = input.getType().findField("tag");
/* 24 */           return input.updateTyped(itemTagFinder, ());
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateAttributeModifiers(Dynamic<?> tag) {
/* 38 */     return tag.update("AttributeModifiers", modifiers -> 
/* 39 */         tag.createList(modifiers.asStream().map(())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateSkullOwner(Dynamic<?> tag) {
/* 46 */     return tag.update("SkullOwner", skullOwner -> 
/* 47 */         (Dynamic)replaceUUIDString(skullOwner, "Id", "Id").orElse(skullOwner));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemStackUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */