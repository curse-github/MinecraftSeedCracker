/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EquipmentFormatFix
/*     */   extends DataFix
/*     */ {
/*  27 */   public EquipmentFormatFix(Schema outputSchema) { super(outputSchema, true); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  32 */     Type<?> oldItemStackType = getInputSchema().getTypeRaw(References.ITEM_STACK);
/*  33 */     Type<?> newItemStackType = getOutputSchema().getTypeRaw(References.ITEM_STACK);
/*  34 */     OpticFinder<?> idFinder = oldItemStackType.findField("id");
/*  35 */     return fix(oldItemStackType, newItemStackType, idFinder);
/*     */   }
/*     */ 
/*     */   
/*     */   private <ItemStackOld, ItemStackNew> TypeRewriteRule fix(Type<ItemStackOld> oldItemStackType, Type<ItemStackNew> newItemStackType, OpticFinder<?> idFinder) {
/*  40 */     Type<Pair<String, Pair<Either<List<ItemStackOld>, Unit>, Pair<Either<List<ItemStackOld>, Unit>, Pair<Either<ItemStackOld, Unit>, Either<ItemStackOld, Unit>>>>>> oldEquipmentType = DSL.named(References.ENTITY_EQUIPMENT.typeName(), DSL.and(
/*  41 */           DSL.optional(DSL.field("ArmorItems", DSL.list(oldItemStackType))), 
/*  42 */           DSL.optional(DSL.field("HandItems", DSL.list(oldItemStackType))), 
/*  43 */           DSL.optional(DSL.field("body_armor_item", oldItemStackType)), 
/*  44 */           DSL.optional(DSL.field("saddle", oldItemStackType))));
/*     */ 
/*     */     
/*  47 */     Type<Pair<String, Either<Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Pair<Either<ItemStackNew, Unit>, Dynamic<?>>>>>>>>>, Unit>>> newEquipmentType = DSL.named(References.ENTITY_EQUIPMENT.typeName(), 
/*  48 */         DSL.optional(DSL.field("equipment", DSL.and(
/*  49 */               DSL.optional(DSL.field("mainhand", newItemStackType)), 
/*  50 */               DSL.optional(DSL.field("offhand", newItemStackType)), 
/*  51 */               DSL.optional(DSL.field("feet", newItemStackType)), 
/*  52 */               DSL.and(
/*  53 */                 DSL.optional(DSL.field("legs", newItemStackType)), 
/*  54 */                 DSL.optional(DSL.field("chest", newItemStackType)), 
/*  55 */                 DSL.optional(DSL.field("head", newItemStackType)), 
/*  56 */                 DSL.and(
/*  57 */                   DSL.optional(DSL.field("body", newItemStackType)), 
/*  58 */                   DSL.optional(DSL.field("saddle", newItemStackType)), 
/*  59 */                   DSL.remainderType()))))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     if (!oldEquipmentType.equals(getInputSchema().getType(References.ENTITY_EQUIPMENT))) {
/*  66 */       throw new IllegalStateException("Input entity_equipment type does not match expected");
/*     */     }
/*     */     
/*  69 */     if (!newEquipmentType.equals(getOutputSchema().getType(References.ENTITY_EQUIPMENT))) {
/*  70 */       throw new IllegalStateException("Output entity_equipment type does not match expected");
/*     */     }
/*     */     
/*  73 */     return fixTypeEverywhere("EquipmentFormatFix", oldEquipmentType, newEquipmentType, ops -> {
/*  74 */           Predicate<ItemStackOld> isPlaceholder = ();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  80 */           return ();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   private static boolean areAllEmpty(Either... fields) {
/* 131 */     for (Either<?, Unit> field : fields) {
/* 132 */       if (field.right().isEmpty()) {
/* 133 */         return false;
/*     */       }
/*     */     } 
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   private static <ItemStack> Either<ItemStack, Unit> getItemFromList(int index, List<ItemStack> items, Predicate<ItemStack> isPlaceholder) {
/* 140 */     if (index >= items.size()) {
/* 141 */       return Either.right(Unit.INSTANCE);
/*     */     }
/* 143 */     ItemStack item = (ItemStack)items.get(index);
/* 144 */     if (isPlaceholder.test(item)) {
/* 145 */       return Either.right(Unit.INSTANCE);
/*     */     }
/* 147 */     return Either.left(item);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EquipmentFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */