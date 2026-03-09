/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ public class BitStorageAlignFix
/*     */   extends DataFix
/*     */ {
/*     */   private static final int BIT_TO_LONG_SHIFT = 6;
/*     */   private static final int SECTION_WIDTH = 16;
/*     */   private static final int SECTION_HEIGHT = 16;
/*     */   private static final int SECTION_SIZE = 4096;
/*     */   private static final int HEIGHTMAP_BITS = 9;
/*     */   private static final int HEIGHTMAP_SIZE = 256;
/*     */   
/*  29 */   public BitStorageAlignFix(Schema schema) { super(schema, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  34 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/*  35 */     Type<?> levelType = chunkType.findFieldType("Level");
/*     */     
/*  37 */     OpticFinder<?> levelFinder = DSL.fieldFinder("Level", levelType);
/*  38 */     OpticFinder<?> sectionsFinder = levelFinder.type().findField("Sections");
/*     */     
/*  40 */     Type<?> sectionType = ((List.ListType)sectionsFinder.type()).getElement();
/*  41 */     OpticFinder<?> sectionFinder = DSL.typeFinder(sectionType);
/*     */     
/*  43 */     Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/*  44 */     OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", DSL.list(blockStateType));
/*     */     
/*  46 */     return fixTypeEverywhereTyped("BitStorageAlignFix", chunkType, getOutputSchema().getType(References.CHUNK), chunk -> 
/*  47 */         chunk.updateTyped(levelFinder, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Typed<?> updateHeightmaps(Typed<?> level) {
/*  54 */     return level.update(DSL.remainderFinder(), tag -> 
/*  55 */         tag.update("Heightmaps", ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Typed<?> updateSections(OpticFinder<?> sectionsFinder, OpticFinder<?> sectionFinder, OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder, Typed<?> level) {
/*  66 */     return level.updateTyped(sectionsFinder, sections -> 
/*  67 */         sections.updateTyped(sectionFinder, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateBitStorage(Dynamic<?> tag, Dynamic<?> storage, int size, int bits) {
/*  82 */     long[] input = storage.asLongStream().toArray();
/*  83 */     long[] output = addPadding(size, bits, input);
/*  84 */     return tag.createLongList(LongStream.of(output));
/*     */   }
/*     */   
/*     */   public static long[] addPadding(int size, int bits, long[] data) {
/*  88 */     int dataLength = data.length;
/*  89 */     if (dataLength == 0) {
/*  90 */       return data;
/*     */     }
/*     */     
/*  93 */     long mask = (1L << bits) - 1L;
/*  94 */     int valuesPerLong = 64 / bits;
/*  95 */     int requiredLength = (size + valuesPerLong - 1) / valuesPerLong;
/*     */     
/*  97 */     long[] result = new long[requiredLength];
/*     */     
/*  99 */     int outputDataIndex = 0;
/* 100 */     int outputStart = 0;
/* 101 */     long outputData = 0L;
/*     */     
/* 103 */     int currentIndex = 0;
/* 104 */     long current = data[0];
/* 105 */     long next = (dataLength > 1) ? data[1] : 0L;
/*     */     
/* 107 */     for (int index = 0; index < size; index++) {
/* 108 */       long valueToInsert; int position = index * bits;
/* 109 */       int startData = position >> 6;
/* 110 */       int endData = (index + 1) * bits - 1 >> 6;
/* 111 */       int startBit = position ^ startData << 6;
/*     */       
/* 113 */       if (startData != currentIndex) {
/* 114 */         current = next;
/* 115 */         next = (startData + 1 < dataLength) ? data[startData + 1] : 0L;
/* 116 */         currentIndex = startData;
/*     */       } 
/*     */ 
/*     */       
/* 120 */       if (startData == endData) {
/* 121 */         valueToInsert = current >>> startBit & mask;
/*     */       } else {
/* 123 */         int shiftBits = 64 - startBit;
/* 124 */         valueToInsert = (current >>> startBit | next << shiftBits) & mask;
/*     */       } 
/*     */       
/* 127 */       int outputEnd = outputStart + bits;
/* 128 */       if (outputEnd >= 64) {
/* 129 */         result[outputDataIndex++] = outputData;
/* 130 */         outputData = valueToInsert;
/* 131 */         outputStart = bits;
/*     */       } else {
/* 133 */         outputData |= valueToInsert << outputStart;
/* 134 */         outputStart = outputEnd;
/*     */       } 
/*     */     } 
/* 137 */     if (outputData != 0L) {
/* 138 */       result[outputDataIndex] = outputData;
/*     */     }
/*     */     
/* 141 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\BitStorageAlignFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */