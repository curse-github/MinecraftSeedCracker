/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class WorldBorderWarningTimeFix
/*    */   extends DataFix {
/* 10 */   public WorldBorderWarningTimeFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 15 */     return writeFixAndRead("WorldBorderWarningTimeFix", getInputSchema().getType(References.SAVED_DATA_WORLD_BORDER), getOutputSchema().getType(References.SAVED_DATA_WORLD_BORDER), input -> 
/* 16 */         input.update("data", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\WorldBorderWarningTimeFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */