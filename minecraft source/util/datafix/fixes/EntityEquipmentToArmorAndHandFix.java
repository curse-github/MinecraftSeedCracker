/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityEquipmentToArmorAndHandFix
/*     */   extends DataFix
/*     */ {
/*  29 */   public EntityEquipmentToArmorAndHandFix(Schema outputSchema) { super(outputSchema, true); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   public TypeRewriteRule makeRule() { return cap(getInputSchema().getTypeRaw(References.ITEM_STACK), getOutputSchema().getTypeRaw(References.ITEM_STACK)); }
/*     */ 
/*     */ 
/*     */   
/*     */   private <ItemStackOld, ItemStackNew> TypeRewriteRule cap(Type<ItemStackOld> oldItemStackType, Type<ItemStackNew> newItemStackType) {
/*  39 */     Type<Pair<String, Either<List<ItemStackOld>, Unit>>> oldEquipmentType = DSL.named(References.ENTITY_EQUIPMENT.typeName(), DSL.optional(DSL.field("Equipment", DSL.list(oldItemStackType))));
/*  40 */     Type<Pair<String, Pair<Either<List<ItemStackNew>, Unit>, Pair<Either<List<ItemStackNew>, Unit>, Pair<Either<ItemStackNew, Unit>, Either<ItemStackNew, Unit>>>>>> newEquipmentType = DSL.named(References.ENTITY_EQUIPMENT.typeName(), DSL.and(
/*  41 */           DSL.optional(DSL.field("ArmorItems", DSL.list(newItemStackType))), 
/*  42 */           DSL.optional(DSL.field("HandItems", DSL.list(newItemStackType))), 
/*  43 */           DSL.optional(DSL.field("body_armor_item", newItemStackType)), 
/*  44 */           DSL.optional(DSL.field("saddle", newItemStackType))));
/*     */ 
/*     */     
/*  47 */     if (!oldEquipmentType.equals(getInputSchema().getType(References.ENTITY_EQUIPMENT))) {
/*  48 */       throw new IllegalStateException("Input entity_equipment type does not match expected");
/*     */     }
/*     */     
/*  51 */     if (!newEquipmentType.equals(getOutputSchema().getType(References.ENTITY_EQUIPMENT))) {
/*  52 */       throw new IllegalStateException("Output entity_equipment type does not match expected");
/*     */     }
/*     */     
/*  55 */     return TypeRewriteRule.seq(
/*     */         
/*  57 */         fixTypeEverywhereTyped("EntityEquipmentToArmorAndHandFix - drop chances", getInputSchema().getType(References.ENTITY), typed -> 
/*  58 */           typed.update(DSL.remainderFinder(), EntityEquipmentToArmorAndHandFix::fixDropChances)), 
/*     */         
/*  60 */         fixTypeEverywhere("EntityEquipmentToArmorAndHandFix - equipment", oldEquipmentType, newEquipmentType, ops -> {
/*  61 */             ItemStackNew emptyStack = (ItemStackNew)((Pair)newItemStackType.read((new Dynamic(ops)).emptyMap()).result().orElseThrow(())).getFirst();
/*  62 */             Either<ItemStackNew, Unit> noItem = Either.right(DSL.unit());
/*     */             
/*  64 */             return ();
/*     */           }));
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
/*     */   private static Dynamic<?> fixDropChances(Dynamic<?> tag) {
/*  88 */     Optional<? extends Stream<? extends Dynamic<?>>> dropChances = tag.get("DropChances").asStreamOpt().result();
/*  89 */     tag = tag.remove("DropChances");
/*     */     
/*  91 */     if (dropChances.isPresent()) {
/*     */ 
/*     */ 
/*     */       
/*  95 */       Iterator<Float> chances = Stream.concat(((Stream)dropChances.get()).map(value -> Float.valueOf(value.asFloat(0.0F))), Stream.generate(() -> Float.valueOf(0.0F))).iterator();
/*  96 */       float handChance = ((Float)chances.next()).floatValue();
/*  97 */       if (tag.get("HandDropChances").result().isEmpty()) {
/*  98 */         Objects.requireNonNull(tag); tag = tag.set("HandDropChances", tag.createList(Stream.of(new Float[] { null, (new Float[2][0] = Float.valueOf(handChance)).valueOf(0.0F) }).map(tag::createFloat)));
/*     */       } 
/*     */       
/* 101 */       if (tag.get("ArmorDropChances").result().isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 107 */         Objects.requireNonNull(tag); tag = tag.set("ArmorDropChances", tag.createList(Stream.of(new Float[] { (Float)chances.next(), (Float)chances.next(), (Float)chances.next(), (Float)chances.next() }).map(tag::createFloat)));
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     return tag;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityEquipmentToArmorAndHandFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */