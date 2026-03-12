/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
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
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class ChunkProtoTickListFix
/*     */   extends DataFix
/*     */ {
/*     */   private static final int SECTION_WIDTH = 16;
/*  31 */   private static final ImmutableSet<String> ALWAYS_WATERLOGGED = ImmutableSet.of("minecraft:bubble_column", "minecraft:kelp", "minecraft:kelp_plant", "minecraft:seagrass", "minecraft:tall_seagrass");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  40 */   public ChunkProtoTickListFix(Schema outputSchema) { super(outputSchema, false); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  45 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/*  46 */     OpticFinder<?> levelFinder = chunkType.findField("Level");
/*  47 */     OpticFinder<?> sectionsFinder = levelFinder.type().findField("Sections");
/*  48 */     OpticFinder<?> sectionFinder = ((List.ListType)sectionsFinder.type()).getElement().finder();
/*  49 */     OpticFinder<?> blockStateContainerFinder = sectionFinder.type().findField("block_states");
/*  50 */     OpticFinder<?> biomeContainerFinder = sectionFinder.type().findField("biomes");
/*  51 */     OpticFinder<?> blockStatePaletteFinder = blockStateContainerFinder.type().findField("palette");
/*  52 */     OpticFinder<?> tileTickFinder = levelFinder.type().findField("TileTicks");
/*     */     
/*  54 */     return fixTypeEverywhereTyped("ChunkProtoTickListFix", chunkType, chunk -> chunk.updateTyped(levelFinder, ()));
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
/*     */   
/*     */   private Dynamic<?> makeTickList(Dynamic<?> tag, Int2ObjectMap<Supplier<PoorMansPalettedContainer>> palettedContainers, byte sectionMinY, int sectionX, int sectionZ, String protoTickListTag, Function<Dynamic<?>, String> typeGetter) {
/* 106 */     Stream<Dynamic<?>> newTickList = Stream.empty();
/* 107 */     List<? extends Dynamic<?>> ticksPerSection = tag.get(protoTickListTag).asList(Function.identity());
/* 108 */     for (int sectionYIndex = 0; sectionYIndex < ticksPerSection.size(); sectionYIndex++) {
/* 109 */       int sectionY = sectionYIndex + sectionMinY;
/* 110 */       Supplier<PoorMansPalettedContainer> container = (Supplier)palettedContainers.get(sectionY);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 115 */       Stream<? extends Dynamic<?>> newTickListForSection = ((Dynamic)ticksPerSection.get(sectionYIndex)).asStream().mapToInt(pos -> pos.asShort((short)-1)).filter(pos -> (pos > 0)).mapToObj(pos -> createTick(tag, container, sectionX, sectionY, sectionZ, pos, typeGetter));
/*     */       
/* 117 */       newTickList = Stream.concat(newTickList, newTickListForSection);
/*     */     } 
/* 119 */     return tag.createList(newTickList);
/*     */   }
/*     */ 
/*     */   
/* 123 */   private static String getBlock(Dynamic<?> blockState) { return (blockState != null) ? blockState.get("Name").asString("minecraft:air") : "minecraft:air"; }
/*     */ 
/*     */   
/*     */   private static String getLiquid(Dynamic<?> blockState) {
/* 127 */     if (blockState == null) {
/* 128 */       return "minecraft:empty";
/*     */     }
/* 130 */     String block = blockState.get("Name").asString("");
/* 131 */     if ("minecraft:water".equals(block)) {
/* 132 */       return (blockState.get("Properties").get("level").asInt(0) == 0) ? "minecraft:water" : "minecraft:flowing_water";
/*     */     }
/* 134 */     if ("minecraft:lava".equals(block)) {
/* 135 */       return (blockState.get("Properties").get("level").asInt(0) == 0) ? "minecraft:lava" : "minecraft:flowing_lava";
/*     */     }
/* 137 */     if (ALWAYS_WATERLOGGED.contains(block) || blockState.get("Properties").get("waterlogged").asBoolean(false)) {
/* 138 */       return "minecraft:water";
/*     */     }
/* 140 */     return "minecraft:empty";
/*     */   }
/*     */   
/*     */   private Dynamic<?> createTick(Dynamic<?> tag, Supplier<PoorMansPalettedContainer> container, int sectionX, int sectionY, int sectionZ, int pos, Function<Dynamic<?>, String> typeGetter) {
/* 144 */     int relativeX = pos & 0xF;
/* 145 */     int relativeY = pos >>> 4 & 0xF;
/* 146 */     int relativeZ = pos >>> 8 & 0xF;
/* 147 */     String type = (String)typeGetter.apply((container != null) ? ((PoorMansPalettedContainer)container.get()).get(relativeX, relativeY, relativeZ) : null);
/* 148 */     return tag.createMap(ImmutableMap.builder()
/* 149 */         .put(tag.createString("i"), tag.createString(type))
/* 150 */         .put(tag.createString("x"), tag.createInt(sectionX * 16 + relativeX))
/* 151 */         .put(tag.createString("y"), tag.createInt(sectionY * 16 + relativeY))
/* 152 */         .put(tag.createString("z"), tag.createInt(sectionZ * 16 + relativeZ))
/* 153 */         .put(tag.createString("t"), tag.createInt(0))
/* 154 */         .put(tag.createString("p"), tag.createInt(0))
/* 155 */         .build());
/*     */   }
/*     */   
/*     */   public static final class PoorMansPalettedContainer
/*     */   {
/*     */     private static final long SIZE_BITS = 4L;
/*     */     private final List<? extends Dynamic<?>> palette;
/*     */     private final long[] data;
/*     */     private final int bits;
/*     */     private final long mask;
/*     */     private final int valuesPerLong;
/*     */     
/*     */     public PoorMansPalettedContainer(List<? extends Dynamic<?>> palette, long[] data) {
/* 168 */       this.palette = palette;
/* 169 */       this.data = data;
/*     */       
/* 171 */       this.bits = Math.max(4, ChunkHeightAndBiomeFix.ceillog2(palette.size()));
/* 172 */       this.mask = (1L << this.bits) - 1L;
/* 173 */       this.valuesPerLong = (char)(64 / this.bits);
/*     */     }
/*     */     
/*     */     public Dynamic<?> get(int x, int y, int z) {
/* 177 */       int entryCount = this.palette.size();
/* 178 */       if (entryCount < 1) {
/* 179 */         return null;
/*     */       }
/* 181 */       if (entryCount == 1) {
/* 182 */         return (Dynamic)this.palette.getFirst();
/*     */       }
/*     */       
/* 185 */       int index = getIndex(x, y, z);
/* 186 */       int cellIndex = index / this.valuesPerLong;
/* 187 */       if (cellIndex < 0 || cellIndex >= this.data.length) {
/* 188 */         return null;
/*     */       }
/* 190 */       long cellValue = this.data[cellIndex];
/* 191 */       int bitIndex = (index - cellIndex * this.valuesPerLong) * this.bits;
/* 192 */       int paletteIndex = (int)(cellValue >> bitIndex & this.mask);
/* 193 */       if (paletteIndex < 0 || paletteIndex >= entryCount) {
/* 194 */         return null;
/*     */       }
/* 196 */       return (Dynamic)this.palette.get(paletteIndex);
/*     */     }
/*     */ 
/*     */     
/* 200 */     private int getIndex(int x, int y, int z) { return (y << 4 | z) << 4 | x; }
/*     */ 
/*     */ 
/*     */     
/* 204 */     public List<? extends Dynamic<?>> palette() { return this.palette; }
/*     */ 
/*     */ 
/*     */     
/* 208 */     public long[] data() { return this.data; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkProtoTickListFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */