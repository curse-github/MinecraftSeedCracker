/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class EmptyItemInVillagerTradeFix
/*    */   extends DataFix {
/* 12 */   public EmptyItemInVillagerTradeFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 17 */     Type<?> tradeType = getInputSchema().getType(References.VILLAGER_TRADE);
/* 18 */     return writeFixAndRead("EmptyItemInVillagerTradeFix", tradeType, tradeType, input -> {
/* 19 */           Dynamic<?> buyB = input.get("buyB").orElseEmptyMap();
/* 20 */           String id = NamespacedSchema.ensureNamespaced(buyB.get("id").asString("minecraft:air"));
/* 21 */           int count = buyB.get("count").asInt(0);
/* 22 */           if (id.equals("minecraft:air") || count == 0) {
/* 23 */             return input.remove("buyB");
/*    */           }
/* 25 */           return input;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EmptyItemInVillagerTradeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */