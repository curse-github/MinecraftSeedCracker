/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ChunkStatusFix
/*    */   extends DataFix {
/* 15 */   public ChunkStatusFix(Schema schema, boolean changesType) { super(schema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 21 */     Type<?> levelType = chunkType.findFieldType("Level");
/*    */     
/* 23 */     OpticFinder<?> levelF = DSL.fieldFinder("Level", levelType);
/*    */     
/* 25 */     return fixTypeEverywhereTyped("ChunkStatusFix", chunkType, getOutputSchema().getType(References.CHUNK), input -> input.updateTyped(levelF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkStatusFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */