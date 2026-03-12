/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityArmorStandSilentFix
/*    */   extends NamedEntityFix {
/* 10 */   public EntityArmorStandSilentFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType, "EntityArmorStandSilentFix", References.ENTITY, "ArmorStand"); }
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 14 */     if (input.get("Silent").asBoolean(false) && !input.get("Marker").asBoolean(false)) {
/* 15 */       return input.remove("Silent");
/*    */     }
/* 17 */     return input;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixTag); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityArmorStandSilentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */