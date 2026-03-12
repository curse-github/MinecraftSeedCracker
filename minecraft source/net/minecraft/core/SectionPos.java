/*     */ package net.minecraft.core;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.longs.LongConsumer;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.entity.EntityAccess;
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
/*     */ public class SectionPos
/*     */   extends Vec3i
/*     */ {
/*     */   public static final int SECTION_BITS = 4;
/*     */   public static final int SECTION_SIZE = 16;
/*     */   public static final int SECTION_MASK = 15;
/*     */   public static final int SECTION_HALF_SIZE = 8;
/*     */   public static final int SECTION_MAX_INDEX = 15;
/*     */   private static final int PACKED_X_LENGTH = 22;
/*     */   private static final int PACKED_Y_LENGTH = 20;
/*     */   private static final int PACKED_Z_LENGTH = 22;
/*     */   private static final long PACKED_X_MASK = 4194303L;
/*     */   private static final long PACKED_Y_MASK = 1048575L;
/*     */   private static final long PACKED_Z_MASK = 4194303L;
/*     */   private static final int Y_OFFSET = 0;
/*     */   private static final int Z_OFFSET = 20;
/*     */   private static final int X_OFFSET = 42;
/*     */   private static final int RELATIVE_X_SHIFT = 8;
/*     */   private static final int RELATIVE_Y_SHIFT = 0;
/*     */   private static final int RELATIVE_Z_SHIFT = 4;
/*  50 */   public static final StreamCodec<ByteBuf, SectionPos> STREAM_CODEC = ByteBufCodecs.LONG.map(SectionPos::of, SectionPos::asLong);
/*     */ 
/*     */   
/*  53 */   private SectionPos(int x, int y, int z) { super(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static SectionPos of(int x, int y, int z) { return new SectionPos(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/*  61 */   public static SectionPos of(BlockPos pos) { return new SectionPos(blockToSectionCoord(pos.getX()), blockToSectionCoord(pos.getY()), blockToSectionCoord(pos.getZ())); }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static SectionPos of(ChunkPos pos, int sectionY) { return new SectionPos(pos.x, sectionY, pos.z); }
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static SectionPos of(EntityAccess entity) { return of(entity.blockPosition()); }
/*     */ 
/*     */   
/*     */   public static SectionPos of(Position pos) {
/*  73 */     return new SectionPos(
/*  74 */         blockToSectionCoord(pos.x()), 
/*  75 */         blockToSectionCoord(pos.y()), 
/*  76 */         blockToSectionCoord(pos.z()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static SectionPos of(long sectionNode) { return new SectionPos(x(sectionNode), y(sectionNode), z(sectionNode)); }
/*     */ 
/*     */ 
/*     */   
/*  85 */   public static SectionPos bottomOf(ChunkAccess chunk) { return of(chunk.getPos(), chunk.getMinSectionY()); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static long offset(long sectionNode, Direction offset) { return offset(sectionNode, offset.getStepX(), offset.getStepY(), offset.getStepZ()); }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public static long offset(long sectionNode, int stepX, int stepY, int stepZ) { return asLong(x(sectionNode) + stepX, y(sectionNode) + stepY, z(sectionNode) + stepZ); }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public static int posToSectionCoord(double pos) { return blockToSectionCoord(Mth.floor(pos)); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static int blockToSectionCoord(int blockCoord) { return blockCoord >> 4; }
/*     */ 
/*     */ 
/*     */   
/* 105 */   public static int blockToSectionCoord(double coord) { return Mth.floor(coord) >> 4; }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static int sectionRelative(int blockCoord) { return blockCoord & 0xF; }
/*     */ 
/*     */   
/*     */   public static short sectionRelativePos(BlockPos pos) {
/* 113 */     int x = sectionRelative(pos.getX());
/* 114 */     int y = sectionRelative(pos.getY());
/* 115 */     int z = sectionRelative(pos.getZ());
/* 116 */     return (short)(x << 8 | z << 4 | y << 0);
/*     */   }
/*     */ 
/*     */   
/* 120 */   public static int sectionRelativeX(short relative) { return relative >>> 8 & 0xF; }
/*     */ 
/*     */ 
/*     */   
/* 124 */   public static int sectionRelativeY(short relative) { return relative >>> 0 & 0xF; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public static int sectionRelativeZ(short relative) { return relative >>> 4 & 0xF; }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public int relativeToBlockX(short relative) { return minBlockX() + sectionRelativeX(relative); }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public int relativeToBlockY(short relative) { return minBlockY() + sectionRelativeY(relative); }
/*     */ 
/*     */ 
/*     */   
/* 140 */   public int relativeToBlockZ(short relative) { return minBlockZ() + sectionRelativeZ(relative); }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public BlockPos relativeToBlockPos(short relative) { return new BlockPos(relativeToBlockX(relative), relativeToBlockY(relative), relativeToBlockZ(relative)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public static int sectionToBlockCoord(int sectionCoord) { return sectionCoord << 4; }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public static int sectionToBlockCoord(int sectionCoord, int offset) { return sectionToBlockCoord(sectionCoord) + offset; }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public static int x(long sectionNode) { return (int)(sectionNode << false >> 42); }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public static int y(long sectionNode) { return (int)(sectionNode << 44 >> 44); }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public static int z(long sectionNode) { return (int)(sectionNode << 22 >> 42); }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public int x() { return getX(); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public int y() { return getY(); }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public int z() { return getZ(); }
/*     */ 
/*     */ 
/*     */   
/* 184 */   public int minBlockX() { return sectionToBlockCoord(x()); }
/*     */ 
/*     */ 
/*     */   
/* 188 */   public int minBlockY() { return sectionToBlockCoord(y()); }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public int minBlockZ() { return sectionToBlockCoord(z()); }
/*     */ 
/*     */ 
/*     */   
/* 196 */   public int maxBlockX() { return sectionToBlockCoord(x(), 15); }
/*     */ 
/*     */ 
/*     */   
/* 200 */   public int maxBlockY() { return sectionToBlockCoord(y(), 15); }
/*     */ 
/*     */ 
/*     */   
/* 204 */   public int maxBlockZ() { return sectionToBlockCoord(z(), 15); }
/*     */ 
/*     */   
/*     */   public static long blockToSection(long blockNode) {
/* 208 */     return asLong(
/* 209 */         blockToSectionCoord(BlockPos.getX(blockNode)), 
/* 210 */         blockToSectionCoord(BlockPos.getY(blockNode)), 
/* 211 */         blockToSectionCoord(BlockPos.getZ(blockNode)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 216 */   public static long getZeroNode(int x, int z) { return getZeroNode(asLong(x, 0, z)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   public static long getZeroNode(long sectionNode) { return sectionNode & 0xFFFFFFFFFFF00000L; }
/*     */ 
/*     */ 
/*     */   
/* 225 */   public static long sectionToChunk(long sectionNode) { return ChunkPos.asLong(x(sectionNode), z(sectionNode)); }
/*     */ 
/*     */ 
/*     */   
/* 229 */   public BlockPos origin() { return new BlockPos(sectionToBlockCoord(x()), sectionToBlockCoord(y()), sectionToBlockCoord(z())); }
/*     */ 
/*     */   
/*     */   public BlockPos center() {
/* 233 */     int delta = 8;
/* 234 */     return origin().offset(8, 8, 8);
/*     */   }
/*     */ 
/*     */   
/* 238 */   public ChunkPos chunk() { return new ChunkPos(x(), z()); }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public static long asLong(BlockPos pos) { return asLong(blockToSectionCoord(pos.getX()), blockToSectionCoord(pos.getY()), blockToSectionCoord(pos.getZ())); }
/*     */ 
/*     */   
/*     */   public static long asLong(int x, int y, int z) {
/* 246 */     node = 0L;
/* 247 */     node |= (x & 0x3FFFFFL) << 42;
/* 248 */     node |= (y & 0xFFFFFL) << false;
/* 249 */     return (z & 0x3FFFFFL) << 20;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 254 */   public long asLong() { return asLong(x(), y(), z()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SectionPos offset(int x, int y, int z) {
/* 259 */     if (x == 0 && y == 0 && z == 0) {
/* 260 */       return this;
/*     */     }
/* 262 */     return new SectionPos(x() + x, y() + y, z() + z);
/*     */   }
/*     */ 
/*     */   
/* 266 */   public Stream<BlockPos> blocksInside() { return BlockPos.betweenClosedStream(minBlockX(), minBlockY(), minBlockZ(), maxBlockX(), maxBlockY(), maxBlockZ()); }
/*     */ 
/*     */   
/*     */   public static Stream<SectionPos> cube(SectionPos center, int radius) {
/* 270 */     int x = center.x();
/* 271 */     int y = center.y();
/* 272 */     int z = center.z();
/* 273 */     return betweenClosedStream(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> aroundChunk(ChunkPos center, int radius, int minSection, int maxSection) {
/* 277 */     int x = center.x;
/* 278 */     int z = center.z;
/* 279 */     return betweenClosedStream(x - radius, minSection, z - radius, x + radius, maxSection, z + radius);
/*     */   }
/*     */ 
/*     */   
/* 283 */   public static Stream<SectionPos> betweenClosedStream(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) { return StreamSupport.stream(new Spliterators.AbstractSpliterator<SectionPos>(((maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)), 64)
/*     */         {
/*     */           final Cursor3D cursor;
/*     */           
/*     */           public boolean tryAdvance(Consumer<? super SectionPos> action) {
/* 288 */             if (this.cursor.advance()) {
/* 289 */               action.accept(new SectionPos(this.cursor.nextX(), this.cursor.nextY(), this.cursor.nextZ()));
/* 290 */               return true;
/*     */             } 
/* 292 */             return false;
/*     */           }
/*     */         }false); }
/*     */ 
/*     */ 
/*     */   
/* 298 */   public static void aroundAndAtBlockPos(BlockPos blockPos, LongConsumer sectionConsumer) { aroundAndAtBlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ(), sectionConsumer); }
/*     */ 
/*     */ 
/*     */   
/* 302 */   public static void aroundAndAtBlockPos(long blockPos, LongConsumer sectionConsumer) { aroundAndAtBlockPos(BlockPos.getX(blockPos), BlockPos.getY(blockPos), BlockPos.getZ(blockPos), sectionConsumer); }
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
/*     */   public static void aroundAndAtBlockPos(int blockX, int blockY, int blockZ, LongConsumer sectionConsumer) {
/* 314 */     int minSectionX = blockToSectionCoord(blockX - 1);
/* 315 */     int maxSectionX = blockToSectionCoord(blockX + 1);
/*     */     
/* 317 */     int minSectionY = blockToSectionCoord(blockY - 1);
/* 318 */     int maxSectionY = blockToSectionCoord(blockY + 1);
/*     */     
/* 320 */     int minSectionZ = blockToSectionCoord(blockZ - 1);
/* 321 */     int maxSectionZ = blockToSectionCoord(blockZ + 1);
/*     */     
/* 323 */     if (minSectionX == maxSectionX && minSectionY == maxSectionY && minSectionZ == maxSectionZ) {
/* 324 */       sectionConsumer.accept(asLong(minSectionX, minSectionY, minSectionZ));
/*     */     } else {
/* 326 */       for (int sectionX = minSectionX; sectionX <= maxSectionX; sectionX++) {
/* 327 */         for (int sectionY = minSectionY; sectionY <= maxSectionY; sectionY++) {
/* 328 */           for (int sectionZ = minSectionZ; sectionZ <= maxSectionZ; sectionZ++)
/* 329 */             sectionConsumer.accept(asLong(sectionX, sectionY, sectionZ)); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\SectionPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */