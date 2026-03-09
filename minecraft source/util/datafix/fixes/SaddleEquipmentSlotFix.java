/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class SaddleEquipmentSlotFix
/*    */   extends DataFix {
/* 20 */   private static final Set<String> ENTITIES_WITH_SADDLE_ITEM = Set.of("minecraft:horse", "minecraft:skeleton_horse", "minecraft:zombie_horse", "minecraft:donkey", "minecraft:mule", "minecraft:camel", "minecraft:llama", "minecraft:trader_llama");
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
/* 31 */   private static final Set<String> ENTITIES_WITH_SADDLE_FLAG = Set.of("minecraft:pig", "minecraft:strider");
/*    */ 
/*    */   
/*    */   private static final String SADDLE_FLAG = "Saddle";
/*    */ 
/*    */   
/*    */   private static final String NEW_SADDLE = "saddle";
/*    */ 
/*    */   
/* 40 */   public SaddleEquipmentSlotFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 45 */     TaggedChoice.TaggedChoiceType<String> entityIdType = getInputSchema().findChoiceType(References.ENTITY);
/* 46 */     OpticFinder<Pair<String, ?>> entityIdF = DSL.typeFinder(entityIdType);
/*    */     
/* 48 */     Type<?> inputType = getInputSchema().getType(References.ENTITY);
/* 49 */     Type<?> outputType = getOutputSchema().getType(References.ENTITY);
/* 50 */     Type<?> patchedInputType = ExtraDataFixUtils.patchSubType(inputType, inputType, outputType);
/*    */     
/* 52 */     return fixTypeEverywhereTyped("SaddleEquipmentSlotFix", inputType, outputType, input -> {
/* 53 */           String entityId = (String)input.getOptional(entityIdF).map(Pair::getFirst).map(NamespacedSchema::ensureNamespaced).orElse("");
/* 54 */           Typed<?> fixedInput = ExtraDataFixUtils.cast(patchedInputType, input);
/* 55 */           if (ENTITIES_WITH_SADDLE_ITEM.contains(entityId))
/* 56 */             return Util.writeAndReadTypedOrThrow(fixedInput, outputType, SaddleEquipmentSlotFix::fixEntityWithSaddleItem); 
/* 57 */           if (ENTITIES_WITH_SADDLE_FLAG.contains(entityId)) {
/* 58 */             return Util.writeAndReadTypedOrThrow(fixedInput, outputType, SaddleEquipmentSlotFix::fixEntityWithSaddleFlag);
/*    */           }
/*    */           
/* 61 */           return ExtraDataFixUtils.cast(outputType, input);
/*    */         });
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixEntityWithSaddleItem(Dynamic<?> input) {
/* 66 */     if (input.get("SaddleItem").result().isEmpty()) {
/* 67 */       return input;
/*    */     }
/* 69 */     return fixDropChances(input.renameField("SaddleItem", "saddle"));
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixEntityWithSaddleFlag(Dynamic<?> tag) {
/* 73 */     boolean hasSaddle = tag.get("Saddle").asBoolean(false);
/* 74 */     tag = tag.remove("Saddle");
/* 75 */     if (!hasSaddle) {
/* 76 */       return tag;
/*    */     }
/*    */ 
/*    */     
/* 80 */     Dynamic<?> saddleItem = tag.emptyMap().set("id", tag.createString("minecraft:saddle")).set("count", tag.createInt(1));
/* 81 */     return fixDropChances(tag.set("saddle", saddleItem));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> fixDropChances(Dynamic<?> tag) {
/* 87 */     Dynamic<?> dropChances = tag.get("drop_chances").orElseEmptyMap().set("saddle", tag.createFloat(2.0F));
/* 88 */     return tag.set("drop_chances", dropChances);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\SaddleEquipmentSlotFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */