/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ 
/*     */ public class EntitySpawnerItemVariantComponentFix
/*     */   extends DataFix
/*     */ {
/*  22 */   public EntitySpawnerItemVariantComponentFix(Schema outputSchema) { super(outputSchema, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeRewriteRule makeRule() {
/*  27 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*     */     
/*  29 */     OpticFinder<Pair<String, String>> idFinder = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/*     */     
/*  31 */     OpticFinder<?> componentsFinder = itemStackType.findField("components");
/*     */     
/*  33 */     return fixTypeEverywhereTyped("ItemStack bucket_entity_data variants to separate components", itemStackType, input -> {
/*  34 */           String id = (String)input.getOptional(idFinder).map(Pair::getSecond).orElse("");
/*  35 */           switch (id) { case "minecraft:salmon_bucket": case "minecraft:axolotl_bucket": case "minecraft:tropical_fish_bucket": case "minecraft:painting":  }  return 
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
/*  46 */             input;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface Fixer
/*     */     extends Function<Typed<?>, Typed<?>>
/*     */   {
/*  55 */     default Typed<?> apply(Typed<?> components) { return components.update(DSL.remainderFinder(), this::fixRemainder); }
/*     */ 
/*     */ 
/*     */     
/*  59 */     default <T> Dynamic<T> fixRemainder(Dynamic<T> remainder) { return (Dynamic)remainder.get("minecraft:bucket_entity_data").result().map(bucketData -> fixRemainder(remainder, bucketData)).orElse(remainder); }
/*     */ 
/*     */     
/*     */     <T> Dynamic<T> fixRemainder(Dynamic<T> param1Dynamic1, Dynamic<T> param1Dynamic2);
/*     */   }
/*     */ 
/*     */   
/*  66 */   private static String getBaseColor(int packedVariant) { return ExtraDataFixUtils.dyeColorIdToName(packedVariant >> 16 & 0xFF); }
/*     */ 
/*     */ 
/*     */   
/*  70 */   private static String getPatternColor(int packedVariant) { return ExtraDataFixUtils.dyeColorIdToName(packedVariant >> 24 & 0xFF); }
/*     */ 
/*     */   
/*     */   private static String getPattern(int packedVariant) {
/*  74 */     switch (packedVariant & 0xFFFF) { default: case 256: case 512: case 768: case 1024: case 1280: case 1: case 257: case 513: case 769: case 1025: case 1281: break; }  return 
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
/*  86 */       "clayfish";
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> Dynamic<T> fixTropicalFishBucket(Dynamic<T> remainder, Dynamic<T> bucketData) {
/*  91 */     Optional<Number> oldVariant = bucketData.get("BucketVariantTag").asNumber().result();
/*  92 */     if (oldVariant.isEmpty()) {
/*  93 */       return remainder;
/*     */     }
/*     */     
/*  96 */     int packedVariant = ((Number)oldVariant.get()).intValue();
/*     */     
/*  98 */     String pattern = getPattern(packedVariant);
/*  99 */     String baseColor = getBaseColor(packedVariant);
/* 100 */     String patternColor = getPatternColor(packedVariant);
/*     */     
/* 102 */     return remainder
/* 103 */       .update("minecraft:bucket_entity_data", b -> b.remove("BucketVariantTag"))
/* 104 */       .set("minecraft:tropical_fish/pattern", remainder.createString(pattern))
/* 105 */       .set("minecraft:tropical_fish/base_color", remainder.createString(baseColor))
/* 106 */       .set("minecraft:tropical_fish/pattern_color", remainder.createString(patternColor));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixAxolotlBucket(Dynamic<T> remainder, Dynamic<T> bucketData) {
/* 110 */     Optional<Number> oldVariant = bucketData.get("Variant").asNumber().result();
/* 111 */     if (oldVariant.isEmpty()) {
/* 112 */       return remainder;
/*     */     }
/*     */     
/* 115 */     switch (((Number)oldVariant.get()).intValue()) { default: 
/*     */       case 1: 
/*     */       case 2: 
/*     */       case 3: 
/*     */       case 4:
/* 120 */         break; }  String newVariant = "blue";
/*     */ 
/*     */     
/* 123 */     return remainder
/* 124 */       .update("minecraft:bucket_entity_data", b -> b.remove("Variant"))
/* 125 */       .set("minecraft:axolotl/variant", remainder.createString(newVariant));
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixSalmonBucket(Dynamic<T> remainder, Dynamic<T> bucketData) {
/* 129 */     Optional<Dynamic<T>> type = bucketData.get("type").result();
/* 130 */     if (type.isEmpty()) {
/* 131 */       return remainder;
/*     */     }
/*     */     
/* 134 */     return remainder
/* 135 */       .update("minecraft:bucket_entity_data", b -> b.remove("type"))
/* 136 */       .set("minecraft:salmon/size", (Dynamic)type.get());
/*     */   }
/*     */   
/*     */   private static <T> Dynamic<T> fixPainting(Dynamic<T> components) {
/* 140 */     Optional<Dynamic<T>> entityData = components.get("minecraft:entity_data").result();
/* 141 */     if (entityData.isEmpty()) {
/* 142 */       return components;
/*     */     }
/*     */     
/* 145 */     if (((Dynamic)entityData.get()).get("id").asString().result().filter(id -> id.equals("minecraft:painting")).isEmpty()) {
/* 146 */       return components;
/*     */     }
/*     */     
/* 149 */     Optional<Dynamic<T>> result = ((Dynamic)entityData.get()).get("variant").result();
/*     */ 
/*     */     
/* 152 */     Dynamic<T> entityDataRemainder = ((Dynamic)entityData.get()).remove("variant");
/* 153 */     if (entityDataRemainder.remove("id").equals(entityDataRemainder.emptyMap())) {
/* 154 */       components = components.remove("minecraft:entity_data");
/*     */     } else {
/* 156 */       components = components.set("minecraft:entity_data", entityDataRemainder);
/*     */     } 
/*     */     
/* 159 */     if (result.isPresent()) {
/* 160 */       components = components.set("minecraft:painting/variant", (Dynamic)result.get());
/*     */     }
/*     */     
/* 163 */     return components;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntitySpawnerItemVariantComponentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */