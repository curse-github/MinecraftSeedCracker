/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ChunkStatusFix2 extends DataFix {
/* 16 */   private static final Map<String, String> RENAMES_AND_DOWNGRADES = ImmutableMap.builder()
/* 17 */     .put("structure_references", "empty")
/* 18 */     .put("biomes", "empty")
/* 19 */     .put("base", "surface")
/* 20 */     .put("carved", "carvers")
/* 21 */     .put("liquid_carved", "liquid_carvers")
/* 22 */     .put("decorated", "features")
/* 23 */     .put("lighted", "light")
/* 24 */     .put("mobs_spawned", "spawn")
/* 25 */     .put("finalized", "heightmaps")
/* 26 */     .put("fullchunk", "full")
/* 27 */     .build();
/*    */ 
/*    */   
/* 30 */   public ChunkStatusFix2(Schema schema, boolean changesType) { super(schema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 35 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 36 */     Type<?> levelType = chunkType.findFieldType("Level");
/*    */     
/* 38 */     OpticFinder<?> levelF = DSL.fieldFinder("Level", levelType);
/*    */     
/* 40 */     return fixTypeEverywhereTyped("ChunkStatusFix2", chunkType, getOutputSchema().getType(References.CHUNK), input -> input.updateTyped(levelF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkStatusFix2.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */