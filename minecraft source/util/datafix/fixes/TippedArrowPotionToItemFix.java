/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class TippedArrowPotionToItemFix
/*    */   extends NamedEntityWriteReadFix
/*    */ {
/* 10 */   public TippedArrowPotionToItemFix(Schema outputSchema) { super(outputSchema, false, "TippedArrowPotionToItemFix", References.ENTITY, "minecraft:arrow"); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 15 */     Optional<Dynamic<T>> potion = input.get("Potion").result();
/* 16 */     Optional<Dynamic<T>> customPotionEffects = input.get("custom_potion_effects").result();
/* 17 */     Optional<Dynamic<T>> color = input.get("Color").result();
/* 18 */     if (potion.isEmpty() && customPotionEffects.isEmpty() && color.isEmpty()) {
/* 19 */       return input;
/*    */     }
/*    */     
/* 22 */     return input
/* 23 */       .remove("Potion")
/* 24 */       .remove("custom_potion_effects")
/* 25 */       .remove("Color")
/* 26 */       .update("item", itemStack -> {
/* 27 */           Dynamic<?> tag = itemStack.get("tag").orElseEmptyMap();
/* 28 */           if (potion.isPresent()) {
/* 29 */             tag = tag.set("Potion", (Dynamic)potion.get());
/*    */           }
/* 31 */           if (customPotionEffects.isPresent()) {
/* 32 */             tag = tag.set("custom_potion_effects", (Dynamic)customPotionEffects.get());
/*    */           }
/* 34 */           if (color.isPresent()) {
/* 35 */             tag = tag.set("CustomPotionColor", (Dynamic)color.get());
/*    */           }
/* 37 */           return itemStack.set("tag", tag);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\TippedArrowPotionToItemFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */