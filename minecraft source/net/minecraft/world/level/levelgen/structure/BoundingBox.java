/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BoundingBox {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  26 */   public static final Codec<BoundingBox> CODEC = Codec.INT_STREAM.comapFlatMap(input -> 
/*  27 */       Util.fixedSize(input, 6).map(()), bb -> 
/*  28 */       IntStream.of(new int[] { bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ
/*  29 */         })).stable();
/*     */   
/*  31 */   public static final StreamCodec<ByteBuf, BoundingBox> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, box -> 
/*  32 */       new BlockPos(box.minX, box.minY, box.minZ), BlockPos.STREAM_CODEC, box -> 
/*  33 */       new BlockPos(box.maxX, box.maxY, box.maxZ), (min, max) -> 
/*  34 */       new BoundingBox(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ()));
/*     */   
/*     */   private int minX;
/*     */   
/*     */   private int minY;
/*     */   
/*     */   private int minZ;
/*     */   private int maxX;
/*     */   private int maxY;
/*     */   private int maxZ;
/*     */   
/*  45 */   public BoundingBox(BlockPos content) { this(content.getX(), content.getY(), content.getZ(), content.getX(), content.getY(), content.getZ()); }
/*     */ 
/*     */   
/*     */   public BoundingBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
/*  49 */     this.minX = minX;
/*  50 */     this.minY = minY;
/*  51 */     this.minZ = minZ;
/*  52 */     this.maxX = maxX;
/*  53 */     this.maxY = maxY;
/*  54 */     this.maxZ = maxZ;
/*     */     
/*  56 */     if (maxX < minX || maxY < minY || maxZ < minZ) {
/*  57 */       Util.logAndPauseIfInIde("Invalid bounding box data, inverted bounds for: " + String.valueOf(this));
/*     */       
/*  59 */       this.minX = Math.min(minX, maxX);
/*  60 */       this.minY = Math.min(minY, maxY);
/*  61 */       this.minZ = Math.min(minZ, maxZ);
/*  62 */       this.maxX = Math.max(minX, maxX);
/*  63 */       this.maxY = Math.max(minY, maxY);
/*  64 */       this.maxZ = Math.max(minZ, maxZ);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  69 */   public static BoundingBox fromCorners(Vec3i pos0, Vec3i pos1) { return new BoundingBox(Math.min(pos0.getX(), pos1.getX()), Math.min(pos0.getY(), pos1.getY()), Math.min(pos0.getZ(), pos1.getZ()), Math.max(pos0.getX(), pos1.getX()), Math.max(pos0.getY(), pos1.getY()), Math.max(pos0.getZ(), pos1.getZ())); }
/*     */ 
/*     */ 
/*     */   
/*  73 */   public static BoundingBox infinite() { return new BoundingBox(-2147483648, -2147483648, -2147483648, 2147483647, 2147483647, 2147483647); }
/*     */ 
/*     */   
/*     */   public static BoundingBox orientBox(int footX, int footY, int footZ, int offX, int offY, int offZ, int width, int height, int depth, Direction direction) {
/*  77 */     switch (direction) {
/*     */ 
/*     */       
/*     */       default:
/*  81 */         return new BoundingBox(footX + offX, footY + offY, footZ + offZ, footX + width - 1 + offX, footY + height - 1 + offY, footZ + depth - 1 + offZ);
/*     */       
/*     */       case NORTH:
/*  84 */         return new BoundingBox(footX + offX, footY + offY, footZ - depth + 1 + offZ, footX + width - 1 + offX, footY + height - 1 + offY, footZ + offZ);
/*     */       
/*     */       case WEST:
/*  87 */         return new BoundingBox(footX - depth + 1 + offZ, footY + offY, footZ + offX, footX + offZ, footY + height - 1 + offY, footZ + width - 1 + offX);
/*     */       case EAST:
/*     */         break;
/*  90 */     }  return new BoundingBox(footX + offZ, footY + offY, footZ + offX, footX + depth - 1 + offZ, footY + height - 1 + offY, footZ + width - 1 + offX);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<ChunkPos> intersectingChunks() {
/*  95 */     int minChunkX = SectionPos.blockToSectionCoord(minX());
/*  96 */     int minChunkZ = SectionPos.blockToSectionCoord(minZ());
/*  97 */     int maxChunkX = SectionPos.blockToSectionCoord(maxX());
/*  98 */     int maxChunkZ = SectionPos.blockToSectionCoord(maxZ());
/*  99 */     return ChunkPos.rangeClosed(new ChunkPos(minChunkX, minChunkZ), new ChunkPos(maxChunkX, maxChunkZ));
/*     */   }
/*     */ 
/*     */   
/* 103 */   public boolean intersects(BoundingBox other) { return (this.maxX >= other.minX && this.minX <= other.maxX && this.maxZ >= other.minZ && this.minZ <= other.maxZ && this.maxY >= other.minY && this.minY <= other.maxY); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public boolean intersects(int minX, int minZ, int maxX, int maxZ) { return (this.maxX >= minX && this.minX <= maxX && this.maxZ >= minZ && this.minZ <= maxZ); }
/*     */ 
/*     */   
/*     */   public static Optional<BoundingBox> encapsulatingPositions(Iterable<BlockPos> iterable) {
/* 111 */     Iterator<BlockPos> iterator = iterable.iterator();
/* 112 */     if (!iterator.hasNext()) {
/* 113 */       return Optional.empty();
/*     */     }
/*     */     
/* 116 */     BoundingBox result = new BoundingBox((BlockPos)iterator.next());
/* 117 */     Objects.requireNonNull(result); iterator.forEachRemaining(result::encapsulate);
/* 118 */     return Optional.of(result);
/*     */   }
/*     */   
/*     */   public static Optional<BoundingBox> encapsulatingBoxes(Iterable<BoundingBox> iterable) {
/* 122 */     Iterator<BoundingBox> iterator = iterable.iterator();
/* 123 */     if (!iterator.hasNext()) {
/* 124 */       return Optional.empty();
/*     */     }
/*     */     
/* 127 */     BoundingBox first = (BoundingBox)iterator.next();
/* 128 */     BoundingBox result = new BoundingBox(first.minX, first.minY, first.minZ, first.maxX, first.maxY, first.maxZ);
/* 129 */     Objects.requireNonNull(result); iterator.forEachRemaining(result::encapsulate);
/* 130 */     return Optional.of(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox encapsulate(BoundingBox other) {
/* 138 */     this.minX = Math.min(this.minX, other.minX);
/* 139 */     this.minY = Math.min(this.minY, other.minY);
/* 140 */     this.minZ = Math.min(this.minZ, other.minZ);
/* 141 */     this.maxX = Math.max(this.maxX, other.maxX);
/* 142 */     this.maxY = Math.max(this.maxY, other.maxY);
/* 143 */     this.maxZ = Math.max(this.maxZ, other.maxZ);
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public static BoundingBox encapsulating(BoundingBox a, BoundingBox b) {
/* 148 */     return new BoundingBox(
/* 149 */         Math.min(a.minX, b.minX), 
/* 150 */         Math.min(a.minY, b.minY), 
/* 151 */         Math.min(a.minZ, b.minZ), 
/* 152 */         Math.max(a.maxX, b.maxX), 
/* 153 */         Math.max(a.maxY, b.maxY), 
/* 154 */         Math.max(a.maxZ, b.maxZ));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox encapsulate(BlockPos pos) {
/* 163 */     this.minX = Math.min(this.minX, pos.getX());
/* 164 */     this.minY = Math.min(this.minY, pos.getY());
/* 165 */     this.minZ = Math.min(this.minZ, pos.getZ());
/* 166 */     this.maxX = Math.max(this.maxX, pos.getX());
/* 167 */     this.maxY = Math.max(this.maxY, pos.getY());
/* 168 */     this.maxZ = Math.max(this.maxZ, pos.getZ());
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BoundingBox move(int dx, int dy, int dz) {
/* 177 */     this.minX += dx;
/* 178 */     this.minY += dy;
/* 179 */     this.minZ += dz;
/* 180 */     this.maxX += dx;
/* 181 */     this.maxY += dy;
/* 182 */     this.maxZ += dz;
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 191 */   public BoundingBox move(Vec3i amount) { return move(amount.getX(), amount.getY(), amount.getZ()); }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public BoundingBox moved(int dx, int dy, int dz) { return new BoundingBox(this.minX + dx, this.minY + dy, this.minZ + dz, this.maxX + dx, this.maxY + dy, this.maxZ + dz); }
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
/* 206 */   public BoundingBox inflatedBy(int amountToAddAllDirections) { return inflatedBy(amountToAddAllDirections, amountToAddAllDirections, amountToAddAllDirections); }
/*     */ 
/*     */   
/*     */   public BoundingBox inflatedBy(int inflateX, int inflateY, int inflateZ) {
/* 210 */     return new BoundingBox(
/* 211 */         minX() - inflateX, 
/* 212 */         minY() - inflateY, 
/* 213 */         minZ() - inflateZ, 
/* 214 */         maxX() + inflateX, 
/* 215 */         maxY() + inflateY, 
/* 216 */         maxZ() + inflateZ);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 221 */   public boolean isInside(Vec3i pos) { return isInside(pos.getX(), pos.getY(), pos.getZ()); }
/*     */ 
/*     */ 
/*     */   
/* 225 */   public boolean isInside(int x, int y, int z) { return (x >= this.minX && x <= this.maxX && z >= this.minZ && z <= this.maxZ && y >= this.minY && y <= this.maxY); }
/*     */ 
/*     */ 
/*     */   
/* 229 */   public Vec3i getLength() { return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ); }
/*     */ 
/*     */ 
/*     */   
/* 233 */   public int getXSpan() { return this.maxX - this.minX + 1; }
/*     */ 
/*     */ 
/*     */   
/* 237 */   public int getYSpan() { return this.maxY - this.minY + 1; }
/*     */ 
/*     */ 
/*     */   
/* 241 */   public int getZSpan() { return this.maxZ - this.minZ + 1; }
/*     */ 
/*     */ 
/*     */   
/* 245 */   public BlockPos getCenter() { return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2); }
/*     */ 
/*     */   
/*     */   public void forAllCorners(Consumer<BlockPos> consumer) {
/* 249 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 250 */     consumer.accept(pos.set(this.maxX, this.maxY, this.maxZ));
/* 251 */     consumer.accept(pos.set(this.minX, this.maxY, this.maxZ));
/* 252 */     consumer.accept(pos.set(this.maxX, this.minY, this.maxZ));
/* 253 */     consumer.accept(pos.set(this.minX, this.minY, this.maxZ));
/* 254 */     consumer.accept(pos.set(this.maxX, this.maxY, this.minZ));
/* 255 */     consumer.accept(pos.set(this.minX, this.maxY, this.minZ));
/* 256 */     consumer.accept(pos.set(this.maxX, this.minY, this.minZ));
/* 257 */     consumer.accept(pos.set(this.minX, this.minY, this.minZ));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 262 */   public String toString() { return MoreObjects.toStringHelper(this)
/* 263 */       .add("minX", this.minX)
/* 264 */       .add("minY", this.minY)
/* 265 */       .add("minZ", this.minZ)
/* 266 */       .add("maxX", this.maxX)
/* 267 */       .add("maxY", this.maxY)
/* 268 */       .add("maxZ", this.maxZ)
/* 269 */       .toString(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 274 */     if (this == o) {
/* 275 */       return true;
/*     */     }
/* 277 */     if (o instanceof BoundingBox) { BoundingBox that = (BoundingBox)o;
/* 278 */       return (this.minX == that.minX && this.minY == that.minY && this.minZ == that.minZ && this.maxX == that.maxX && this.maxY == that.maxY && this.maxZ == that.maxZ); }
/*     */     
/* 280 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 285 */   public int hashCode() { return Objects.hash(new Object[] { Integer.valueOf(this.minX), Integer.valueOf(this.minY), Integer.valueOf(this.minZ), Integer.valueOf(this.maxX), Integer.valueOf(this.maxY), Integer.valueOf(this.maxZ) }); }
/*     */ 
/*     */ 
/*     */   
/* 289 */   public int minX() { return this.minX; }
/*     */ 
/*     */ 
/*     */   
/* 293 */   public int minY() { return this.minY; }
/*     */ 
/*     */ 
/*     */   
/* 297 */   public int minZ() { return this.minZ; }
/*     */ 
/*     */ 
/*     */   
/* 301 */   public int maxX() { return this.maxX; }
/*     */ 
/*     */ 
/*     */   
/* 305 */   public int maxY() { return this.maxY; }
/*     */ 
/*     */ 
/*     */   
/* 309 */   public int maxZ() { return this.maxZ; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\BoundingBox.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */