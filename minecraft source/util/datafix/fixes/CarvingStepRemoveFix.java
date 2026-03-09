/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class CarvingStepRemoveFix
/*    */   extends DataFix
/*    */ {
/* 14 */   public CarvingStepRemoveFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   protected TypeRewriteRule makeRule() { return fixTypeEverywhereTyped("CarvingStepRemoveFix", getInputSchema().getType(References.CHUNK), CarvingStepRemoveFix::fixChunk); }
/*    */ 
/*    */   
/*    */   private static Typed<?> fixChunk(Typed<?> input) {
/* 23 */     return input.update(DSL.remainderFinder(), chunkIn -> {
/* 24 */           Dynamic<?> chunk = chunkIn;
/* 25 */           Optional<? extends Dynamic<?>> carvingMasks = chunk.get("CarvingMasks").result();
/* 26 */           if (carvingMasks.isPresent()) {
/* 27 */             Optional<? extends Dynamic<?>> mask = ((Dynamic)carvingMasks.get()).get("AIR").result();
/* 28 */             if (mask.isPresent()) {
/* 29 */               chunk = chunk.set("carving_mask", (Dynamic)mask.get());
/*    */             }
/*    */           } 
/* 32 */           return chunk.remove("CarvingMasks");
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\CarvingStepRemoveFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */