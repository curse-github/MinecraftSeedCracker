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
/*    */ public class ChunkTicketUnpackPosFix extends DataFix {
/*    */   private static final long CHUNK_COORD_BITS = 32L;
/*    */   private static final long CHUNK_COORD_MASK = 4294967295L;
/*    */   
/* 15 */   public ChunkTicketUnpackPosFix(Schema outputSchema) { super(outputSchema, false); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     return fixTypeEverywhereTyped("ChunkTicketUnpackPosFix", getInputSchema().getType(References.SAVED_DATA_TICKETS), input -> 
/* 21 */         input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkTicketUnpackPosFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */