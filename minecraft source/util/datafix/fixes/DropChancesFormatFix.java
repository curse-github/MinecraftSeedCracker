/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import java.util.List;
/*    */ 
/*    */ public class DropChancesFormatFix extends DataFix {
/* 13 */   private static final List<String> ARMOR_SLOT_NAMES = List.of("feet", "legs", "chest", "head");
/* 14 */   private static final List<String> HAND_SLOT_NAMES = List.of("mainhand", "offhand");
/*    */   
/*    */   private static final float DEFAULT_CHANCE = 0.085F;
/*    */ 
/*    */   
/* 19 */   public DropChancesFormatFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 25 */     return fixTypeEverywhereTyped("DropChancesFormatFix", getInputSchema().getType(References.ENTITY), input -> input.update(DSL.remainderFinder(), ()));
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
/*    */ 
/*    */ 
/*    */   
/*    */   private static Dynamic<?> addSlotChances(Dynamic<?> output, List<Float> chances, List<String> slotNames) {
/* 51 */     for (int i = 0; i < slotNames.size() && i < chances.size(); i++) {
/* 52 */       String slot = (String)slotNames.get(i);
/* 53 */       float chance = ((Float)chances.get(i)).floatValue();
/* 54 */       if (chance != 0.085F) {
/* 55 */         output = output.set(slot, output.createFloat(chance));
/*    */       }
/*    */     } 
/* 58 */     return output;
/*    */   }
/*    */ 
/*    */   
/* 62 */   private static List<Float> parseDropChances(OptionalDynamic<?> value) { return value.asStream()
/* 63 */       .map(dynamic -> Float.valueOf(dynamic.asFloat(0.085F)))
/* 64 */       .toList(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\DropChancesFormatFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */