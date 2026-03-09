/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class LegacyDragonFightFix extends DataFix {
/* 13 */   public LegacyDragonFightFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   private static <T> Dynamic<T> fixDragonFight(Dynamic<T> tag) { return tag.update("ExitPortalLocation", ExtraDataFixUtils::fixBlockPos); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped("LegacyDragonFightFix", getInputSchema().getType(References.LEVEL), input -> 
/* 23 */         input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LegacyDragonFightFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */