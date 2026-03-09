/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityFurnaceBurnTimeFix
/*    */   extends NamedEntityFix
/*    */ {
/* 11 */   public BlockEntityFurnaceBurnTimeFix(Schema outputSchema, String entityType) { super(outputSchema, false, "BlockEntityFurnaceBurnTimeFix" + entityType, References.BLOCK_ENTITY, entityType); }
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixBurnTime(Dynamic<?> data) {
/* 15 */     data = data.renameField("CookTime", "cooking_time_spent");
/* 16 */     data = data.renameField("CookTimeTotal", "cooking_total_time");
/* 17 */     data = data.renameField("BurnTime", "lit_time_remaining");
/*    */     
/* 19 */     return data.setFieldIfPresent("lit_total_time", data.get("lit_time_remaining").result());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   protected Typed<?> fix(Typed<?> entity) { return entity.update(DSL.remainderFinder(), this::fixBurnTime); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityFurnaceBurnTimeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */