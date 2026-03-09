/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ public class WorldSpawnDataFix
/*    */   extends DataFix {
/* 13 */   public WorldSpawnDataFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 18 */     return fixTypeEverywhereTyped("WorldSpawnDataFix", getInputSchema().getType(References.LEVEL), input -> 
/* 19 */         input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\WorldSpawnDataFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */