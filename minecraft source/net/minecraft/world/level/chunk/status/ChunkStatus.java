/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkStatus
/*     */ {
/*     */   public static final int MAX_STRUCTURE_DISTANCE = 8;
/*  19 */   private static final EnumSet<Heightmap.Types> WORLDGEN_HEIGHTMAPS = EnumSet.of(Heightmap.Types.OCEAN_FLOOR_WG, Heightmap.Types.WORLD_SURFACE_WG);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   public static final EnumSet<Heightmap.Types> FINAL_HEIGHTMAPS = EnumSet.of(Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE, Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static final ChunkStatus EMPTY = register("empty", null, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  32 */   public static final ChunkStatus STRUCTURE_STARTS = register("structure_starts", EMPTY, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  33 */   public static final ChunkStatus STRUCTURE_REFERENCES = register("structure_references", STRUCTURE_STARTS, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  34 */   public static final ChunkStatus BIOMES = register("biomes", STRUCTURE_REFERENCES, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  35 */   public static final ChunkStatus NOISE = register("noise", BIOMES, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  36 */   public static final ChunkStatus SURFACE = register("surface", NOISE, WORLDGEN_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  37 */   public static final ChunkStatus CARVERS = register("carvers", SURFACE, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  38 */   public static final ChunkStatus FEATURES = register("features", CARVERS, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  39 */   public static final ChunkStatus INITIALIZE_LIGHT = register("initialize_light", FEATURES, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  40 */   public static final ChunkStatus LIGHT = register("light", INITIALIZE_LIGHT, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  41 */   public static final ChunkStatus SPAWN = register("spawn", LIGHT, FINAL_HEIGHTMAPS, ChunkType.PROTOCHUNK);
/*  42 */   public static final ChunkStatus FULL = register("full", SPAWN, FINAL_HEIGHTMAPS, ChunkType.LEVELCHUNK);
/*     */   
/*  44 */   public static final Codec<ChunkStatus> CODEC = BuiltInRegistries.CHUNK_STATUS.byNameCodec(); private final int index;
/*     */   private final ChunkStatus parent;
/*     */   
/*  47 */   private static ChunkStatus register(String name, ChunkStatus parent, EnumSet<Heightmap.Types> heightmaps, ChunkType chunkType) { return (ChunkStatus)Registry.register(BuiltInRegistries.CHUNK_STATUS, name, new ChunkStatus(parent, heightmaps, chunkType)); }
/*     */   private final ChunkType chunkType; private final EnumSet<Heightmap.Types> heightmapsAfter;
/*     */   
/*     */   public static List<ChunkStatus> getStatusList() {
/*  51 */     list = Lists.newArrayList();
/*  52 */     ChunkStatus status = FULL;
/*  53 */     while (status.getParent() != status) {
/*  54 */       list.add(status);
/*  55 */       status = status.getParent();
/*     */     } 
/*  57 */     list.add(status);
/*  58 */     Collections.reverse(list);
/*  59 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   protected ChunkStatus(ChunkStatus parent, EnumSet<Heightmap.Types> heightmapsAfter, ChunkType chunkType) {
/*  69 */     this.parent = (parent == null) ? this : parent;
/*  70 */     this.chunkType = chunkType;
/*  71 */     this.heightmapsAfter = heightmapsAfter;
/*  72 */     this.index = (parent == null) ? 0 : (parent.getIndex() + 1);
/*     */   }
/*     */ 
/*     */   
/*  76 */   public int getIndex() { return this.index; }
/*     */ 
/*     */ 
/*     */   
/*  80 */   public ChunkStatus getParent() { return this.parent; }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public ChunkType getChunkType() { return this.chunkType; }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public static ChunkStatus byName(String key) { return (ChunkStatus)BuiltInRegistries.CHUNK_STATUS.getValue(Identifier.tryParse(key)); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   public EnumSet<Heightmap.Types> heightmapsAfter() { return this.heightmapsAfter; }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public boolean isOrAfter(ChunkStatus step) { return (getIndex() >= step.getIndex()); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public boolean isAfter(ChunkStatus step) { return (getIndex() > step.getIndex()); }
/*     */ 
/*     */ 
/*     */   
/* 104 */   public boolean isOrBefore(ChunkStatus step) { return (getIndex() <= step.getIndex()); }
/*     */ 
/*     */ 
/*     */   
/* 108 */   public boolean isBefore(ChunkStatus step) { return (getIndex() < step.getIndex()); }
/*     */ 
/*     */ 
/*     */   
/* 112 */   public static ChunkStatus max(ChunkStatus a, ChunkStatus b) { return a.isAfter(b) ? a : b; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public String toString() { return getName(); }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public String getName() { return BuiltInRegistries.CHUNK_STATUS.getKey(this).toString(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\status\ChunkStatus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */