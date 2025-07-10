/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.LongConsumer
 */
package net.minecraft.util.math;

import it.unimi.dsi.fastutil.longs.LongConsumer;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.util.CuboidBlockIterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.entity.EntityLike;

public class ChunkSectionPos
extends Vec3i {
    public static final int field_33096 = 4;
    public static final int field_33097 = 16;
    public static final int field_33100 = 15;
    public static final int field_33098 = 8;
    public static final int field_33099 = 15;
    private static final int field_33101 = 22;
    private static final int field_33102 = 20;
    private static final int field_33103 = 22;
    private static final long field_33104 = 0x3FFFFFL;
    private static final long field_33105 = 1048575L;
    private static final long field_33106 = 0x3FFFFFL;
    private static final int field_33107 = 0;
    private static final int field_33108 = 20;
    private static final int field_33109 = 42;
    private static final int field_33110 = 8;
    private static final int field_33111 = 0;
    private static final int field_33112 = 4;

    ChunkSectionPos(int x, int y, int z) {
        super(x, y, z);
    }

    public static ChunkSectionPos from(int x, int y, int z) {
        return new ChunkSectionPos(x, y, z);
    }

    public static ChunkSectionPos from(BlockPos pos) {
        return new ChunkSectionPos(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getY()), ChunkSectionPos.getSectionCoord(pos.getZ()));
    }

    public static ChunkSectionPos from(ChunkPos chunkPos, int y) {
        return new ChunkSectionPos(chunkPos.x, y, chunkPos.z);
    }

    public static ChunkSectionPos from(EntityLike entity) {
        return ChunkSectionPos.from(entity.getBlockPos());
    }

    public static ChunkSectionPos from(Position pos) {
        return new ChunkSectionPos(ChunkSectionPos.getSectionCoordFloored(pos.getX()), ChunkSectionPos.getSectionCoordFloored(pos.getY()), ChunkSectionPos.getSectionCoordFloored(pos.getZ()));
    }

    public static ChunkSectionPos from(long packed) {
        return new ChunkSectionPos(ChunkSectionPos.unpackX(packed), ChunkSectionPos.unpackY(packed), ChunkSectionPos.unpackZ(packed));
    }

    public static ChunkSectionPos from(Chunk chunk) {
        return ChunkSectionPos.from(chunk.getPos(), chunk.getBottomSectionCoord());
    }

    public static long offset(long packed, Direction direction) {
        return ChunkSectionPos.offset(packed, direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
    }

    public static long offset(long packed, int x, int y, int z) {
        return ChunkSectionPos.asLong(ChunkSectionPos.unpackX(packed) + x, ChunkSectionPos.unpackY(packed) + y, ChunkSectionPos.unpackZ(packed) + z);
    }

    public static int getSectionCoord(double coord) {
        return ChunkSectionPos.getSectionCoord(MathHelper.floor(coord));
    }

    public static int getSectionCoord(int coord) {
        return coord >> 4;
    }

    public static int getSectionCoordFloored(double coord) {
        return MathHelper.floor(coord) >> 4;
    }

    public static int getLocalCoord(int coord) {
        return coord & 0xF;
    }

    public static short packLocal(BlockPos pos) {
        int $$1 = ChunkSectionPos.getLocalCoord(pos.getX());
        int $$2 = ChunkSectionPos.getLocalCoord(pos.getY());
        int $$3 = ChunkSectionPos.getLocalCoord(pos.getZ());
        return (short)($$1 << 8 | $$3 << 4 | $$2 << 0);
    }

    public static int unpackLocalX(short packedLocalPos) {
        return packedLocalPos >>> 8 & 0xF;
    }

    public static int unpackLocalY(short packedLocalPos) {
        return packedLocalPos >>> 0 & 0xF;
    }

    public static int unpackLocalZ(short packedLocalPos) {
        return packedLocalPos >>> 4 & 0xF;
    }

    public int unpackBlockX(short packedLocalPos) {
        return this.getMinX() + ChunkSectionPos.unpackLocalX(packedLocalPos);
    }

    public int unpackBlockY(short packedLocalPos) {
        return this.getMinY() + ChunkSectionPos.unpackLocalY(packedLocalPos);
    }

    public int unpackBlockZ(short packedLocalPos) {
        return this.getMinZ() + ChunkSectionPos.unpackLocalZ(packedLocalPos);
    }

    public BlockPos unpackBlockPos(short packedLocalPos) {
        return new BlockPos(this.unpackBlockX(packedLocalPos), this.unpackBlockY(packedLocalPos), this.unpackBlockZ(packedLocalPos));
    }

    public static int getBlockCoord(int sectionCoord) {
        return sectionCoord << 4;
    }

    public static int getOffsetPos(int chunkCoord, int offset) {
        return ChunkSectionPos.getBlockCoord(chunkCoord) + offset;
    }

    public static int unpackX(long packed) {
        return (int)(packed << 0 >> 42);
    }

    public static int unpackY(long packed) {
        return (int)(packed << 44 >> 44);
    }

    public static int unpackZ(long packed) {
        return (int)(packed << 22 >> 42);
    }

    public int getSectionX() {
        return this.getX();
    }

    public int getSectionY() {
        return this.getY();
    }

    public int getSectionZ() {
        return this.getZ();
    }

    public int getMinX() {
        return ChunkSectionPos.getBlockCoord(this.getSectionX());
    }

    public int getMinY() {
        return ChunkSectionPos.getBlockCoord(this.getSectionY());
    }

    public int getMinZ() {
        return ChunkSectionPos.getBlockCoord(this.getSectionZ());
    }

    public int getMaxX() {
        return ChunkSectionPos.getOffsetPos(this.getSectionX(), 15);
    }

    public int getMaxY() {
        return ChunkSectionPos.getOffsetPos(this.getSectionY(), 15);
    }

    public int getMaxZ() {
        return ChunkSectionPos.getOffsetPos(this.getSectionZ(), 15);
    }

    public static long fromBlockPos(long blockPos) {
        return ChunkSectionPos.asLong(ChunkSectionPos.getSectionCoord(BlockPos.unpackLongX(blockPos)), ChunkSectionPos.getSectionCoord(BlockPos.unpackLongY(blockPos)), ChunkSectionPos.getSectionCoord(BlockPos.unpackLongZ(blockPos)));
    }

    public static long withZeroY(int x, int z) {
        return ChunkSectionPos.withZeroY(ChunkSectionPos.asLong(x, 0, z));
    }

    public static long withZeroY(long pos) {
        return pos & 0xFFFFFFFFFFF00000L;
    }

    public static long toChunkPos(long sectionPos) {
        return ChunkPos.toLong(ChunkSectionPos.unpackX(sectionPos), ChunkSectionPos.unpackZ(sectionPos));
    }

    public BlockPos getMinPos() {
        return new BlockPos(ChunkSectionPos.getBlockCoord(this.getSectionX()), ChunkSectionPos.getBlockCoord(this.getSectionY()), ChunkSectionPos.getBlockCoord(this.getSectionZ()));
    }

    public BlockPos getCenterPos() {
        int $$0 = 8;
        return this.getMinPos().add(8, 8, 8);
    }

    public ChunkPos toChunkPos() {
        return new ChunkPos(this.getSectionX(), this.getSectionZ());
    }

    public static long toLong(BlockPos pos) {
        return ChunkSectionPos.asLong(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getY()), ChunkSectionPos.getSectionCoord(pos.getZ()));
    }

    public static long asLong(int x, int y, int z) {
        long $$3 = 0L;
        $$3 |= ((long)x & 0x3FFFFFL) << 42;
        $$3 |= ((long)y & 0xFFFFFL) << 0;
        return $$3 |= ((long)z & 0x3FFFFFL) << 20;
    }

    public long asLong() {
        return ChunkSectionPos.asLong(this.getSectionX(), this.getSectionY(), this.getSectionZ());
    }

    @Override
    public ChunkSectionPos add(int x, int y, int z) {
        if (x == 0 && y == 0 && z == 0) {
            return this;
        }
        return new ChunkSectionPos(this.getSectionX() + x, this.getSectionY() + y, this.getSectionZ() + z);
    }

    public Stream<BlockPos> streamBlocks() {
        return BlockPos.stream(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    public static Stream<ChunkSectionPos> stream(ChunkSectionPos center, int radius) {
        int $$2 = center.getSectionX();
        int $$3 = center.getSectionY();
        int $$4 = center.getSectionZ();
        return ChunkSectionPos.stream($$2 - radius, $$3 - radius, $$4 - radius, $$2 + radius, $$3 + radius, $$4 + radius);
    }

    public static Stream<ChunkSectionPos> stream(ChunkPos center, int radius, int minY, int maxY) {
        int $$4 = center.x;
        int $$5 = center.z;
        return ChunkSectionPos.stream($$4 - radius, minY, $$5 - radius, $$4 + radius, maxY, $$5 + radius);
    }

    public static Stream<ChunkSectionPos> stream(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<ChunkSectionPos>((long)((maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)), 64){
            final CuboidBlockIterator iterator;
            {
                super($$0, $$1);
                this.iterator = new CuboidBlockIterator(minX, minY, minZ, maxX, maxY, maxZ);
            }

            @Override
            public boolean tryAdvance(Consumer<? super ChunkSectionPos> consumer) {
                if (this.iterator.step()) {
                    consumer.accept(new ChunkSectionPos(this.iterator.getX(), this.iterator.getY(), this.iterator.getZ()));
                    return true;
                }
                return false;
            }
        }, false);
    }

    public static void forEachChunkSectionAround(BlockPos pos, LongConsumer consumer) {
        ChunkSectionPos.forEachChunkSectionAround(pos.getX(), pos.getY(), pos.getZ(), consumer);
    }

    public static void forEachChunkSectionAround(long pos, LongConsumer consumer) {
        ChunkSectionPos.forEachChunkSectionAround(BlockPos.unpackLongX(pos), BlockPos.unpackLongY(pos), BlockPos.unpackLongZ(pos), consumer);
    }

    public static void forEachChunkSectionAround(int x, int y, int z, LongConsumer consumer) {
        int $$4 = ChunkSectionPos.getSectionCoord(x - 1);
        int $$5 = ChunkSectionPos.getSectionCoord(x + 1);
        int $$6 = ChunkSectionPos.getSectionCoord(y - 1);
        int $$7 = ChunkSectionPos.getSectionCoord(y + 1);
        int $$8 = ChunkSectionPos.getSectionCoord(z - 1);
        int $$9 = ChunkSectionPos.getSectionCoord(z + 1);
        if ($$4 == $$5 && $$6 == $$7 && $$8 == $$9) {
            consumer.accept(ChunkSectionPos.asLong($$4, $$6, $$8));
        } else {
            for (int $$10 = $$4; $$10 <= $$5; ++$$10) {
                for (int $$11 = $$6; $$11 <= $$7; ++$$11) {
                    for (int $$12 = $$8; $$12 <= $$9; ++$$12) {
                        consumer.accept(ChunkSectionPos.asLong($$10, $$11, $$12));
                    }
                }
            }
        }
    }
}

