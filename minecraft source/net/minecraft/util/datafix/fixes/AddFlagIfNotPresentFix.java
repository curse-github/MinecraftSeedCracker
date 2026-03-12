/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class AddFlagIfNotPresentFix extends DataFix {
/*    */   private final String name;
/*    */   private final boolean flagValue;
/*    */   
/*    */   public AddFlagIfNotPresentFix(Schema outputSchema, DSL.TypeReference typeReference, String flagKey, boolean flagValue) {
/* 17 */     super(outputSchema, true);
/* 18 */     this.flagValue = flagValue;
/* 19 */     this.flagKey = flagKey;
/* 20 */     this.name = "AddFlagIfNotPresentFix_" + this.flagKey + "=" + this.flagValue + " for " + outputSchema.getVersionKey();
/* 21 */     this.typeReference = typeReference;
/*    */   }
/*    */   private final String flagKey; private final DSL.TypeReference typeReference;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     Type<?> worldGenSettingsType = getInputSchema().getType(this.typeReference);
/*    */     
/* 28 */     return fixTypeEverywhereTyped(this.name, worldGenSettingsType, settings -> 
/* 29 */         settings.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\AddFlagIfNotPresentFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */