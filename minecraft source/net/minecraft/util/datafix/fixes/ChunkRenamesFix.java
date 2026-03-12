/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ChunkRenamesFix
/*    */   extends DataFix {
/* 21 */   public ChunkRenamesFix(Schema outputSchema) { super(outputSchema, true); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/* 27 */     OpticFinder<?> levelFinder = chunkType.findField("Level");
/* 28 */     OpticFinder<?> structureFinder = levelFinder.type().findField("Structures");
/*    */     
/* 30 */     Type<?> newChunkType = getOutputSchema().getType(References.CHUNK);
/* 31 */     Type<?> newStructuresType = newChunkType.findFieldType("structures");
/*    */     
/* 33 */     return fixTypeEverywhereTyped("Chunk Renames; purge Level-tag", chunkType, newChunkType, chunk -> {
/* 34 */           Typed<?> level = chunk.getTyped(levelFinder);
/* 35 */           Typed<?> chunkTyped = appendChunkName(level);
/*    */           
/* 37 */           chunkTyped = chunkTyped.set(DSL.remainderFinder(), mergeRemainders(chunk, (Dynamic)level.get(DSL.remainderFinder())));
/*    */           
/* 39 */           chunkTyped = renameField(chunkTyped, "TileEntities", "block_entities");
/* 40 */           chunkTyped = renameField(chunkTyped, "TileTicks", "block_ticks");
/* 41 */           chunkTyped = renameField(chunkTyped, "Entities", "entities");
/* 42 */           chunkTyped = renameField(chunkTyped, "Sections", "sections");
/* 43 */           chunkTyped = chunkTyped.updateTyped(structureFinder, newStructuresType, ());
/* 44 */           chunkTyped = renameField(chunkTyped, "Structures", "structures");
/*    */           
/* 46 */           return chunkTyped.update(DSL.remainderFinder(), ());
/*    */         });
/*    */   }
/*    */ 
/*    */   
/* 51 */   private static Typed<?> renameField(Typed<?> input, String oldName, String newName) { return renameFieldHelper(input, oldName, newName, input.getType().findFieldType(oldName)).update(DSL.remainderFinder(), tag -> tag.remove(oldName)); }
/*    */ 
/*    */   
/*    */   private static <A> Typed<?> renameFieldHelper(Typed<?> input, String oldName, String newName, Type<A> fieldType) {
/* 55 */     Type<Either<A, Unit>> oldType = DSL.optional(DSL.field(oldName, fieldType));
/* 56 */     Type<Either<A, Unit>> newType = DSL.optional(DSL.field(newName, fieldType));
/* 57 */     return input.update(oldType.finder(), newType, Function.identity());
/*    */   }
/*    */ 
/*    */   
/* 61 */   private static <A> Typed<Pair<String, A>> appendChunkName(Typed<A> input) { return new Typed(DSL.named("chunk", input.getType()), input.getOps(), Pair.of("chunk", input.getValue())); }
/*    */ 
/*    */   
/*    */   private static <T> Dynamic<T> mergeRemainders(Typed<?> chunk, Dynamic<T> levelRemainder) {
/* 65 */     DynamicOps<T> ops = levelRemainder.getOps();
/* 66 */     Dynamic<T> chunkRemainder = ((Dynamic)chunk.get(DSL.remainderFinder())).convert(ops);
/* 67 */     DataResult<T> toMap = ops.getMap(levelRemainder.getValue()).flatMap(map -> ops.mergeToMap(chunkRemainder.getValue(), map));
/* 68 */     return (Dynamic)toMap.result().map(v -> new Dynamic(ops, v)).orElse(levelRemainder);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkRenamesFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */