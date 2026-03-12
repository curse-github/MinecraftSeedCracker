/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChunkBedBlockEntityInjecterFix
/*    */   extends DataFix
/*    */ {
/* 26 */   public ChunkBedBlockEntityInjecterFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 31 */     Type<?> chunkType = getOutputSchema().getType(References.CHUNK);
/* 32 */     Type<?> levelType = chunkType.findFieldType("Level");
/* 33 */     Type<?> tileEntitiesType = levelType.findFieldType("TileEntities");
/* 34 */     if (!(tileEntitiesType instanceof List.ListType)) {
/* 35 */       throw new IllegalStateException("Tile entity type is not a list type.");
/*    */     }
/* 37 */     List.ListType<?> tileEntityListType = (List.ListType)tileEntitiesType;
/*    */     
/* 39 */     return cap(levelType, tileEntityListType);
/*    */   }
/*    */   
/*    */   private <TE> TypeRewriteRule cap(Type<?> levelType, List.ListType<TE> tileEntityListType) {
/* 43 */     Type<TE> tileEntityType = tileEntityListType.getElement();
/* 44 */     OpticFinder<?> levelF = DSL.fieldFinder("Level", levelType);
/* 45 */     OpticFinder<List<TE>> tileEntitiesF = DSL.fieldFinder("TileEntities", tileEntityListType);
/*    */ 
/*    */     
/* 48 */     int bedId = 416;
/*    */     
/* 50 */     return TypeRewriteRule.seq(
/* 51 */         fixTypeEverywhere("InjectBedBlockEntityType", getInputSchema().findChoiceType(References.BLOCK_ENTITY), getOutputSchema().findChoiceType(References.BLOCK_ENTITY), ops -> ()), 
/* 52 */         fixTypeEverywhereTyped("BedBlockEntityInjecter", getOutputSchema().getType(References.CHUNK), input -> {
/* 53 */             Typed<?> level = input.getTyped(levelF);
/* 54 */             Dynamic<?> levelTag = (Dynamic)level.get(DSL.remainderFinder());
/* 55 */             int chunkX = levelTag.get("xPos").asInt(0);
/* 56 */             int chunkZ = levelTag.get("zPos").asInt(0);
/*    */             
/* 58 */             List<TE> tileEntities = Lists.newArrayList((Iterable)level.getOrCreate(tileEntitiesF));
/*    */             
/* 60 */             List<? extends Dynamic<?>> sectionTags = levelTag.get("Sections").asList(Function.identity());
/* 61 */             for (Dynamic<?> sectionTag : sectionTags) {
/* 62 */               int pos = sectionTag.get("Y").asInt(0);
/* 63 */               Streams.mapWithIndex(sectionTag.get("Blocks").asIntStream(), ())
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */                 
/* 79 */                 .forEachOrdered(());
/*    */             } 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 85 */             if (!tileEntities.isEmpty()) {
/* 86 */               return input.set(levelF, level.set(tileEntitiesF, tileEntities));
/*    */             }
/* 88 */             return input;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkBedBlockEntityInjecterFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */