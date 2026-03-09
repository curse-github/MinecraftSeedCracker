/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortArrayList;
/*    */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class ChunkToProtochunkFix extends DataFix {
/*    */   private static final int NUM_SECTIONS = 16;
/*    */   
/* 20 */   public ChunkToProtochunkFix(Schema outputSchema, boolean changesType) { super(outputSchema, changesType); }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 25 */     return writeFixAndRead("ChunkToProtoChunkFix", getInputSchema().getType(References.CHUNK), getOutputSchema().getType(References.CHUNK), chunk -> 
/* 26 */         chunk.update("Level", ChunkToProtochunkFix::fixChunkData));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> fixChunkData(Dynamic<T> tag) {
/*    */     String status;
/* 31 */     boolean terrainPopulated = tag.get("TerrainPopulated").asBoolean(false);
/*    */     
/* 33 */     boolean lightPopulated = (tag.get("LightPopulated").asNumber().result().isEmpty() || tag.get("LightPopulated").asBoolean(false));
/*    */ 
/*    */     
/* 36 */     if (terrainPopulated) {
/* 37 */       if (lightPopulated) {
/* 38 */         status = "mobs_spawned";
/*    */       } else {
/* 40 */         status = "decorated";
/*    */       } 
/*    */     } else {
/* 43 */       status = "carved";
/*    */     } 
/* 45 */     return repackTicks(repackBiomes(tag))
/* 46 */       .set("Status", tag.createString(status))
/* 47 */       .set("hasLegacyStructureData", tag.createBoolean(true));
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> repackBiomes(Dynamic<T> tag) {
/* 51 */     return tag.update("Biomes", biomes -> 
/* 52 */         (Dynamic)DataFixUtils.orElse(biomes
/* 53 */           .asByteBufferOpt().result().map(()), biomes));
/*    */   }
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
/*    */   private static <T> Dynamic<T> repackTicks(Dynamic<T> tag) {
/* 68 */     return (Dynamic)DataFixUtils.orElse(tag
/* 69 */         .get("TileTicks").asStreamOpt().result().map(ticks -> {
/* 70 */             List<ShortList> toBeTickedTag = (List)IntStream.range(0, 16).mapToObj(()).collect(Collectors.toList());
/* 71 */             ticks.forEach(());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/* 78 */             return tag.remove("TileTicks").set("ToBeTicked", tag.createList(toBeTickedTag.stream().map(())));
/*    */           }), tag);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   private static short packOffsetCoordinates(int x, int y, int z) { return (short)(x & 0xF | (y & 0xF) << 4 | (z & 0xF) << 8); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkToProtochunkFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */