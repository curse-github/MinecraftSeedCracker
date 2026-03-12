/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.base.Suppliers;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class ThrownPotionSplitFix extends EntityRenameFix {
/*    */   private final Supplier<ItemIdFinder> itemIdFinder;
/*    */   
/*    */   public ThrownPotionSplitFix(Schema outputSchema) {
/* 19 */     super("ThrownPotionSplitFix", outputSchema, true);
/* 20 */     this.itemIdFinder = Suppliers.memoize(() -> {
/* 21 */           Type<?> potionType = getInputSchema().getChoiceType(References.ENTITY, "minecraft:potion");
/*    */           
/* 23 */           Type<?> patchedPotionType = ExtraDataFixUtils.patchSubType(potionType, getInputSchema().getType(References.ENTITY), getOutputSchema().getType(References.ENTITY));
/* 24 */           OpticFinder<?> itemFinder = patchedPotionType.findField("Item");
/* 25 */           OpticFinder<Pair<String, String>> itemIdFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 26 */           return new ItemIdFinder(itemFinder, itemIdFinder);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Typed<?>> fix(String name, Typed<?> entity) {
/* 32 */     if (!name.equals("minecraft:potion")) {
/* 33 */       return Pair.of(name, entity);
/*    */     }
/* 35 */     String itemId = ((ItemIdFinder)this.itemIdFinder.get()).getItemId(entity);
/* 36 */     if ("minecraft:lingering_potion".equals(itemId)) {
/* 37 */       return Pair.of("minecraft:lingering_potion", entity);
/*    */     }
/* 39 */     return Pair.of("minecraft:splash_potion", entity);
/*    */   }
/*    */   private static final class ItemIdFinder extends Record { private final OpticFinder<?> itemFinder; private final OpticFinder<Pair<String, String>> itemIdFinder;
/*    */     
/* 43 */     private ItemIdFinder(OpticFinder<?> itemFinder, OpticFinder<Pair<String, String>> itemIdFinder) { this.itemFinder = itemFinder; this.itemIdFinder = itemIdFinder; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 43 */       //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder; } public OpticFinder<?> itemFinder() { return this.itemFinder; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/datafix/fixes/ThrownPotionSplitFix$ItemIdFinder;
/* 43 */       //   0	8	1	o	Ljava/lang/Object; } public OpticFinder<Pair<String, String>> itemIdFinder() { return this.itemIdFinder; }
/*    */     public String getItemId(Typed<?> entity) {
/* 45 */       return (String)entity.getOptionalTyped(this.itemFinder)
/* 46 */         .flatMap(item -> item.getOptional(this.itemIdFinder))
/* 47 */         .map(Pair::getSecond)
/* 48 */         .map(NamespacedSchema::ensureNamespaced)
/* 49 */         .orElse("");
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ThrownPotionSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */