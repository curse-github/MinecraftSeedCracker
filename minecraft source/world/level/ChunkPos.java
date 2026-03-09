/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkPos
/*     */ {
/*  26 */   public static final Codec<ChunkPos> CODEC = Codec.INT_STREAM.comapFlatMap(input -> 
/*  27 */       Util.fixedSize(input, 2).map(()), pos -> 
/*  28 */       IntStream.of(new int[] { pos.x, pos.z
/*  29 */         })).stable();
/*     */   
/*  31 */   public static final StreamCodec<ByteBuf, ChunkPos> STREAM_CODEC = new StreamCodec<ByteBuf, ChunkPos>()
/*     */     {
/*     */       public ChunkPos decode(ByteBuf input) {
/*  34 */         return FriendlyByteBuf.readChunkPos(input);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  39 */       public void encode(ByteBuf output, ChunkPos value) { FriendlyByteBuf.writeChunkPos(output, value); }
/*     */     };
/*     */ 
/*     */   
/*     */   private static final int SAFETY_MARGIN = 1056;
/*  44 */   public static final long INVALID_CHUNK_POS = asLong(1875066, 1875066);
/*     */   
/*  46 */   private static final int SAFETY_MARGIN_CHUNKS = (32 + ChunkPyramid.GENERATION_PYRAMID.getStepTo(ChunkStatus.FULL).accumulatedDependencies().size() + 1) * 2;
/*     */   
/*  48 */   public static final int MAX_COORDINATE_VALUE = SectionPos.blockToSectionCoord(BlockPos.MAX_HORIZONTAL_COORDINATE) - SAFETY_MARGIN_CHUNKS;
/*     */   
/*  50 */   public static final ChunkPos ZERO = new ChunkPos(0, 0);
/*     */   
/*     */   private static final long COORD_BITS = 32L;
/*     */   
/*     */   private static final long COORD_MASK = 4294967295L;
/*     */   
/*     */   private static final int REGION_BITS = 5;
/*     */   
/*     */   public static final int REGION_SIZE = 32;
/*     */   private static final int REGION_MASK = 31;
/*     */   
/*     */   public ChunkPos(int x, int z) {
/*  62 */     this.x = x;
/*  63 */     this.z = z;
/*     */   }
/*     */   public static final int REGION_MAX_INDEX = 31; public final int x; public final int z; private static final int HASH_A = 1664525; private static final int HASH_C = 1013904223; private static final int HASH_Z_XOR = -559038737;
/*     */   public ChunkPos(BlockPos pos) {
/*  67 */     this.x = SectionPos.blockToSectionCoord(pos.getX());
/*  68 */     this.z = SectionPos.blockToSectionCoord(pos.getZ());
/*     */   }
/*     */   
/*     */   public ChunkPos(long key) {
/*  72 */     this.x = (int)key;
/*  73 */     this.z = (int)(key >> 32);
/*     */   }
/*     */ 
/*     */   
/*  77 */   public static ChunkPos minFromRegion(int regionX, int regionZ) { return new ChunkPos(regionX << 5, regionZ << 5); }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static ChunkPos maxFromRegion(int regionX, int regionZ) { return new ChunkPos((regionX << 5) + 31, (regionZ << 5) + 31); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public boolean isValid() { return isValid(this.x, this.z); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static boolean isValid(int x, int z) { return (Mth.absMax(x, z) <= MAX_COORDINATE_VALUE); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public long toLong() { return asLong(this.x, this.z); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static long asLong(int x, int z) { return x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32; }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static long asLong(BlockPos pos) { return asLong(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())); }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public static int getX(long pos) { return (int)(pos & 0xFFFFFFFFL); }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static int getZ(long pos) { return (int)(pos >>> 32 & 0xFFFFFFFFL); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public int hashCode() { return hash(this.x, this.z); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hash(int x, int z) {
/* 125 */     int xTransform = 1664525 * x + 1013904223;
/* 126 */     int zTransform = 1664525 * (z ^ 0xDEADBEEF) + 1013904223;
/* 127 */     return xTransform ^ zTransform;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 132 */     if (this == o) {
/* 133 */       return true;
/*     */     }
/*     */     
/* 136 */     if (o instanceof ChunkPos) { ChunkPos chunkPos = (ChunkPos)o;
/* 137 */       return (this.x == chunkPos.x && this.z == chunkPos.z); }
/*     */ 
/*     */     
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 144 */   public int getMiddleBlockX() { return getBlockX(8); }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public int getMiddleBlockZ() { return getBlockZ(8); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public int getMinBlockX() { return SectionPos.sectionToBlockCoord(this.x); }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public int getMinBlockZ() { return SectionPos.sectionToBlockCoord(this.z); }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public int getMaxBlockX() { return getBlockX(15); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public int getMaxBlockZ() { return getBlockZ(15); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public int getRegionX() { return this.x >> 5; }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public int getRegionZ() { return this.z >> 5; }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public int getRegionLocalX() { return this.x & 0x1F; }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public int getRegionLocalZ() { return this.z & 0x1F; }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public BlockPos getBlockAt(int x, int y, int z) { return new BlockPos(getBlockX(x), y, getBlockZ(z)); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public int getBlockX(int offset) { return SectionPos.sectionToBlockCoord(this.x, offset); }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public int getBlockZ(int offset) { return SectionPos.sectionToBlockCoord(this.z, offset); }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public BlockPos getMiddleBlockPosition(int y) { return new BlockPos(getMiddleBlockX(), y, getMiddleBlockZ()); }
/*     */ 
/*     */   
/*     */   public boolean contains(BlockPos pos) {
/* 200 */     return (pos.getX() >= getMinBlockX() && pos.getZ() >= getMinBlockZ() && pos
/* 201 */       .getX() <= getMaxBlockX() && pos.getZ() <= getMaxBlockZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 206 */   public String toString() { return "[" + this.x + ", " + this.z + "]"; }
/*     */ 
/*     */ 
/*     */   
/* 210 */   public BlockPos getWorldPosition() { return new BlockPos(getMinBlockX(), 0, getMinBlockZ()); }
/*     */ 
/*     */ 
/*     */   
/* 214 */   public int getChessboardDistance(ChunkPos pos) { return getChessboardDistance(pos.x, pos.z); }
/*     */ 
/*     */ 
/*     */   
/* 218 */   public int getChessboardDistance(int x, int z) { return Mth.chessboardDistance(x, z, this.x, this.z); }
/*     */ 
/*     */ 
/*     */   
/* 222 */   public int distanceSquared(ChunkPos pos) { return distanceSquared(pos.x, pos.z); }
/*     */ 
/*     */ 
/*     */   
/* 226 */   public int distanceSquared(long pos) { return distanceSquared(getX(pos), getZ(pos)); }
/*     */ 
/*     */   
/*     */   private int distanceSquared(int x, int z) {
/* 230 */     int deltaX = x - this.x;
/* 231 */     int deltaZ = z - this.z;
/* 232 */     return deltaX * deltaX + deltaZ * deltaZ;
/*     */   }
/*     */ 
/*     */   
/* 236 */   public static Stream<ChunkPos> rangeClosed(ChunkPos center, int radius) { return rangeClosed(new ChunkPos(center.x - radius, center.z - radius), new ChunkPos(center.x + radius, center.z + radius)); }
/*     */ 
/*     */   
/*     */   public static Stream<ChunkPos> rangeClosed(final ChunkPos from, final ChunkPos to) {
/* 240 */     int xSize = Math.abs(from.x - to.x) + 1;
/* 241 */     int zSize = Math.abs(from.z - to.z) + 1;
/* 242 */     final int xDiff = (from.x < to.x) ? 1 : -1;
/* 243 */     final int zDiff = (from.z < to.z) ? 1 : -1;
/* 244 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<ChunkPos>((xSize * zSize), 64)
/*     */         {
/*     */           private ChunkPos pos;
/*     */           
/*     */           public boolean tryAdvance(Consumer<? super ChunkPos> action) {
/* 249 */             if (this.pos == null) {
/* 250 */               this.pos = from;
/*     */             } else {
/* 252 */               int x = this.pos.x;
/* 253 */               int z = this.pos.z;
/* 254 */               if (x == this.val$to.x) {
/* 255 */                 if (z == this.val$to.z) {
/* 256 */                   return false;
/*     */                 }
/* 258 */                 this.pos = new ChunkPos(this.val$from.x, z + zDiff);
/*     */               } else {
/* 260 */                 this.pos = new ChunkPos(x + xDiff, z);
/*     */               } 
/*     */             } 
/* 263 */             action.accept(this.pos);
/* 264 */             return true;
/*     */           }
/*     */         }false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\ChunkPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */