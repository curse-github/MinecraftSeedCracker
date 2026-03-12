/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class FoodToConsumableFix
/*    */   extends DataFix
/*    */ {
/* 13 */   public FoodToConsumableFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     return writeFixAndRead("Food to consumable fix", getInputSchema().getType(References.DATA_COMPONENTS), getOutputSchema().getType(References.DATA_COMPONENTS), components -> {
/* 19 */           Optional<? extends Dynamic<?>> foodComponent = components.get("minecraft:food").result();
/* 20 */           if (foodComponent.isPresent()) {
/* 21 */             float eatSeconds = ((Dynamic)foodComponent.get()).get("eat_seconds").asFloat(1.6F);
/* 22 */             Stream<? extends Dynamic<?>> effects = ((Dynamic)foodComponent.get()).get("effects").asStream();
/*    */             
/* 24 */             Stream<? extends Dynamic<?>> onConsumeEffects = effects.map(());
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 29 */             components = Dynamic.copyField((Dynamic)foodComponent.get(), "using_converts_to", components, "minecraft:use_remainder");
/* 30 */             components = components.set("minecraft:food", ((Dynamic)foodComponent.get()).remove("eat_seconds").remove("effects").remove("using_converts_to"));
/* 31 */             return components.set("minecraft:consumable", components.emptyMap()
/* 32 */                 .set("consume_seconds", components.createFloat(eatSeconds))
/* 33 */                 .set("on_consume_effects", components.createList(onConsumeEffects)));
/*    */           } 
/*    */ 
/*    */           
/* 37 */           return components;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\FoodToConsumableFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */