/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class PlayerEquipmentFix
/*    */   extends DataFix
/*    */ {
/* 14 */   public PlayerEquipmentFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */   
/* 17 */   private static final Map<Integer, String> SLOT_TRANSLATIONS = Map.of(
/* 18 */       Integer.valueOf(100), "feet", 
/* 19 */       Integer.valueOf(101), "legs", 
/* 20 */       Integer.valueOf(102), "chest", 
/* 21 */       Integer.valueOf(103), "head", 
/* 22 */       Integer.valueOf(-106), "offhand");
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 27 */     Type<?> oldPlayerType = getInputSchema().getTypeRaw(References.PLAYER);
/* 28 */     Type<?> newPlayerType = getOutputSchema().getTypeRaw(References.PLAYER);
/*    */     
/* 30 */     return writeFixAndRead("Player Equipment Fix", oldPlayerType, newPlayerType, tag -> {
/* 31 */           Map<Dynamic<?>, Dynamic<?>> equipment = new HashMap<Dynamic<?>, Dynamic<?>>();
/* 32 */           tag = tag.update("Inventory", ());
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
/* 44 */           return tag.set("equipment", tag.createMap(equipment));
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\PlayerEquipmentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */