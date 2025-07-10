/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  javax.annotation.Nullable
 */
package net.minecraft.structure;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public abstract class StructurePiece {
    protected static final BlockState AIR = Blocks.CAVE_AIR.getDefaultState();
    protected BlockBox boundingBox;
    @Nullable
    private Direction facing;
    private BlockMirror mirror;
    private BlockRotation rotation;
    protected int chainLength;
    private final StructurePieceType type;
    private static final Set<Block> BLOCKS_NEEDING_POST_PROCESSING = ImmutableSet.builder().add((Object)Blocks.NETHER_BRICK_FENCE).add((Object)Blocks.TORCH).add((Object)Blocks.WALL_TORCH).add((Object)Blocks.OAK_FENCE).add((Object)Blocks.SPRUCE_FENCE).add((Object)Blocks.DARK_OAK_FENCE).add((Object)Blocks.PALE_OAK_FENCE).add((Object)Blocks.ACACIA_FENCE).add((Object)Blocks.BIRCH_FENCE).add((Object)Blocks.JUNGLE_FENCE).add((Object)Blocks.LADDER).add((Object)Blocks.IRON_BARS).build();

    protected StructurePiece(StructurePieceType type, int length, BlockBox boundingBox) {
        this.type = type;
        this.chainLength = length;
        this.boundingBox = boundingBox;
    }

    public StructurePiece(StructurePieceType type, NbtCompound nbt) {
        this(type, nbt.getInt("GD", 0), nbt.get("BB", BlockBox.CODEC).orElseThrow());
        int $$2 = nbt.getInt("O", 0);
        this.setOrientation($$2 == -1 ? null : Direction.fromHorizontalQuarterTurns($$2));
    }

    protected static BlockBox createBox(int x, int y, int z, Direction orientation, int width, int height, int depth) {
        if (orientation.getAxis() == Direction.Axis.Z) {
            return new BlockBox(x, y, z, x + width - 1, y + height - 1, z + depth - 1);
        }
        return new BlockBox(x, y, z, x + depth - 1, y + height - 1, z + width - 1);
    }

    protected static Direction getRandomHorizontalDirection(Random random) {
        return Direction.Type.HORIZONTAL.random(random);
    }

    public final NbtCompound toNbt(StructureContext context) {
        NbtCompound $$1 = new NbtCompound();
        $$1.putString("id", Registries.STRUCTURE_PIECE.getId(this.getType()).toString());
        $$1.put("BB", BlockBox.CODEC, this.boundingBox);
        Direction $$2 = this.getFacing();
        $$1.putInt("O", $$2 == null ? -1 : $$2.getHorizontalQuarterTurns());
        $$1.putInt("GD", this.chainLength);
        this.writeNbt(context, $$1);
        return $$1;
    }

    protected abstract void writeNbt(StructureContext var1, NbtCompound var2);

    public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
    }

    public abstract void generate(StructureWorldAccess var1, StructureAccessor var2, ChunkGenerator var3, Random var4, BlockBox var5, ChunkPos var6, BlockPos var7);

    public BlockBox getBoundingBox() {
        return this.boundingBox;
    }

    public int getChainLength() {
        return this.chainLength;
    }

    public void setChainLength(int chainLength) {
        this.chainLength = chainLength;
    }

    public boolean intersectsChunk(ChunkPos pos, int offset) {
        int $$2 = pos.getStartX();
        int $$3 = pos.getStartZ();
        return this.boundingBox.intersectsXZ($$2 - offset, $$3 - offset, $$2 + 15 + offset, $$3 + 15 + offset);
    }

    public BlockPos getCenter() {
        return new BlockPos(this.boundingBox.getCenter());
    }

    protected BlockPos.Mutable offsetPos(int x, int y, int z) {
        return new BlockPos.Mutable(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
    }

    protected int applyXTransform(int x, int z) {
        Direction $$2 = this.getFacing();
        if ($$2 == null) {
            return x;
        }
        switch ($$2) {
            case NORTH: 
            case SOUTH: {
                return this.boundingBox.getMinX() + x;
            }
            case WEST: {
                return this.boundingBox.getMaxX() - z;
            }
            case EAST: {
                return this.boundingBox.getMinX() + z;
            }
        }
        return x;
    }

    protected int applyYTransform(int y) {
        if (this.getFacing() == null) {
            return y;
        }
        return y + this.boundingBox.getMinY();
    }

    protected int applyZTransform(int x, int z) {
        Direction $$2 = this.getFacing();
        if ($$2 == null) {
            return z;
        }
        switch ($$2) {
            case NORTH: {
                return this.boundingBox.getMaxZ() - z;
            }
            case SOUTH: {
                return this.boundingBox.getMinZ() + z;
            }
            case WEST: 
            case EAST: {
                return this.boundingBox.getMinZ() + x;
            }
        }
        return z;
    }

    protected void addBlock(StructureWorldAccess world, BlockState block, int x, int y, int z, BlockBox box) {
        BlockPos.Mutable $$6 = this.offsetPos(x, y, z);
        if (!box.contains($$6)) {
            return;
        }
        if (!this.canAddBlock(world, x, y, z, box)) {
            return;
        }
        if (this.mirror != BlockMirror.NONE) {
            block = block.mirror(this.mirror);
        }
        if (this.rotation != BlockRotation.NONE) {
            block = block.rotate(this.rotation);
        }
        world.setBlockState($$6, block, 2);
        FluidState $$7 = world.getFluidState($$6);
        if (!$$7.isEmpty()) {
            world.scheduleFluidTick($$6, $$7.getFluid(), 0);
        }
        if (BLOCKS_NEEDING_POST_PROCESSING.contains(block.getBlock())) {
            world.getChunk($$6).markBlockForPostProcessing($$6);
        }
    }

    protected boolean canAddBlock(WorldView world, int x, int y, int z, BlockBox box) {
        return true;
    }

    protected BlockState getBlockAt(BlockView world, int x, int y, int z, BlockBox box) {
        BlockPos.Mutable $$5 = this.offsetPos(x, y, z);
        if (!box.contains($$5)) {
            return Blocks.AIR.getDefaultState();
        }
        return world.getBlockState($$5);
    }

    protected boolean isUnderSeaLevel(WorldView world, int x, int z, int y, BlockBox box) {
        BlockPos.Mutable $$5 = this.offsetPos(x, z + 1, y);
        if (!box.contains($$5)) {
            return false;
        }
        return $$5.getY() < world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, $$5.getX(), $$5.getZ());
    }

    protected void fill(StructureWorldAccess world, BlockBox bounds, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int $$8 = minY; $$8 <= maxY; ++$$8) {
            for (int $$9 = minX; $$9 <= maxX; ++$$9) {
                for (int $$10 = minZ; $$10 <= maxZ; ++$$10) {
                    this.addBlock(world, Blocks.AIR.getDefaultState(), $$9, $$8, $$10, bounds);
                }
            }
        }
    }

    protected void fillWithOutline(StructureWorldAccess world, BlockBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState outline, BlockState inside, boolean cantReplaceAir) {
        for (int $$11 = minY; $$11 <= maxY; ++$$11) {
            for (int $$12 = minX; $$12 <= maxX; ++$$12) {
                for (int $$13 = minZ; $$13 <= maxZ; ++$$13) {
                    if (cantReplaceAir && this.getBlockAt(world, $$12, $$11, $$13, box).isAir()) continue;
                    if ($$11 == minY || $$11 == maxY || $$12 == minX || $$12 == maxX || $$13 == minZ || $$13 == maxZ) {
                        this.addBlock(world, outline, $$12, $$11, $$13, box);
                        continue;
                    }
                    this.addBlock(world, inside, $$12, $$11, $$13, box);
                }
            }
        }
    }

    protected void fillWithOutline(StructureWorldAccess world, BlockBox box, BlockBox fillBox, BlockState outline, BlockState inside, boolean cantReplaceAir) {
        this.fillWithOutline(world, box, fillBox.getMinX(), fillBox.getMinY(), fillBox.getMinZ(), fillBox.getMaxX(), fillBox.getMaxY(), fillBox.getMaxZ(), outline, inside, cantReplaceAir);
    }

    protected void fillWithOutline(StructureWorldAccess world, BlockBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean cantReplaceAir, Random random, BlockRandomizer randomizer) {
        for (int $$11 = minY; $$11 <= maxY; ++$$11) {
            for (int $$12 = minX; $$12 <= maxX; ++$$12) {
                for (int $$13 = minZ; $$13 <= maxZ; ++$$13) {
                    if (cantReplaceAir && this.getBlockAt(world, $$12, $$11, $$13, box).isAir()) continue;
                    randomizer.setBlock(random, $$12, $$11, $$13, $$11 == minY || $$11 == maxY || $$12 == minX || $$12 == maxX || $$13 == minZ || $$13 == maxZ);
                    this.addBlock(world, randomizer.getBlock(), $$12, $$11, $$13, box);
                }
            }
        }
    }

    protected void fillWithOutline(StructureWorldAccess world, BlockBox box, BlockBox fillBox, boolean cantReplaceAir, Random random, BlockRandomizer randomizer) {
        this.fillWithOutline(world, box, fillBox.getMinX(), fillBox.getMinY(), fillBox.getMinZ(), fillBox.getMaxX(), fillBox.getMaxY(), fillBox.getMaxZ(), cantReplaceAir, random, randomizer);
    }

    protected void fillWithOutlineUnderSeaLevel(StructureWorldAccess world, BlockBox box, Random random, float blockChance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState outline, BlockState inside, boolean cantReplaceAir, boolean stayBelowSeaLevel) {
        for (int $$14 = minY; $$14 <= maxY; ++$$14) {
            for (int $$15 = minX; $$15 <= maxX; ++$$15) {
                for (int $$16 = minZ; $$16 <= maxZ; ++$$16) {
                    if (random.nextFloat() > blockChance || cantReplaceAir && this.getBlockAt(world, $$15, $$14, $$16, box).isAir() || stayBelowSeaLevel && !this.isUnderSeaLevel(world, $$15, $$14, $$16, box)) continue;
                    if ($$14 == minY || $$14 == maxY || $$15 == minX || $$15 == maxX || $$16 == minZ || $$16 == maxZ) {
                        this.addBlock(world, outline, $$15, $$14, $$16, box);
                        continue;
                    }
                    this.addBlock(world, inside, $$15, $$14, $$16, box);
                }
            }
        }
    }

    protected void addBlockWithRandomThreshold(StructureWorldAccess world, BlockBox bounds, Random random, float threshold, int x, int y, int z, BlockState state) {
        if (random.nextFloat() < threshold) {
            this.addBlock(world, state, x, y, z, bounds);
        }
    }

    protected void fillHalfEllipsoid(StructureWorldAccess world, BlockBox bounds, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState block, boolean cantReplaceAir) {
        float $$10 = maxX - minX + 1;
        float $$11 = maxY - minY + 1;
        float $$12 = maxZ - minZ + 1;
        float $$13 = (float)minX + $$10 / 2.0f;
        float $$14 = (float)minZ + $$12 / 2.0f;
        for (int $$15 = minY; $$15 <= maxY; ++$$15) {
            float $$16 = (float)($$15 - minY) / $$11;
            for (int $$17 = minX; $$17 <= maxX; ++$$17) {
                float $$18 = ((float)$$17 - $$13) / ($$10 * 0.5f);
                for (int $$19 = minZ; $$19 <= maxZ; ++$$19) {
                    float $$21;
                    float $$20 = ((float)$$19 - $$14) / ($$12 * 0.5f);
                    if (cantReplaceAir && this.getBlockAt(world, $$17, $$15, $$19, bounds).isAir() || !(($$21 = $$18 * $$18 + $$16 * $$16 + $$20 * $$20) <= 1.05f)) continue;
                    this.addBlock(world, block, $$17, $$15, $$19, bounds);
                }
            }
        }
    }

    protected void fillDownwards(StructureWorldAccess world, BlockState state, int x, int y, int z, BlockBox box) {
        BlockPos.Mutable $$6 = this.offsetPos(x, y, z);
        if (!box.contains($$6)) {
            return;
        }
        while (this.canReplace(world.getBlockState($$6)) && $$6.getY() > world.getBottomY() + 1) {
            world.setBlockState($$6, state, 2);
            $$6.move(Direction.DOWN);
        }
    }

    protected boolean canReplace(BlockState state) {
        return state.isAir() || state.isLiquid() || state.isOf(Blocks.GLOW_LICHEN) || state.isOf(Blocks.SEAGRASS) || state.isOf(Blocks.TALL_SEAGRASS);
    }

    protected boolean addChest(StructureWorldAccess world, BlockBox boundingBox, Random random, int x, int y, int z, RegistryKey<LootTable> lootTable) {
        return this.addChest(world, boundingBox, random, this.offsetPos(x, y, z), lootTable, null);
    }

    public static BlockState orientateChest(BlockView world, BlockPos pos, BlockState state) {
        Direction $$3 = null;
        for (Direction $$4 : Direction.Type.HORIZONTAL) {
            Vec3i $$5 = pos.offset($$4);
            BlockState $$6 = world.getBlockState((BlockPos)$$5);
            if ($$6.isOf(Blocks.CHEST)) {
                return state;
            }
            if (!$$6.isOpaqueFullCube()) continue;
            if ($$3 == null) {
                $$3 = $$4;
                continue;
            }
            $$3 = null;
            break;
        }
        if ($$3 != null) {
            return (BlockState)state.with(HorizontalFacingBlock.FACING, $$3.getOpposite());
        }
        Direction $$7 = state.get(HorizontalFacingBlock.FACING);
        Vec3i $$8 = pos.offset($$7);
        if (world.getBlockState((BlockPos)$$8).isOpaqueFullCube()) {
            $$7 = $$7.getOpposite();
            $$8 = pos.offset($$7);
        }
        if (world.getBlockState((BlockPos)$$8).isOpaqueFullCube()) {
            $$7 = $$7.rotateYClockwise();
            $$8 = pos.offset($$7);
        }
        if (world.getBlockState((BlockPos)$$8).isOpaqueFullCube()) {
            $$7 = $$7.getOpposite();
            $$8 = pos.offset($$7);
        }
        return (BlockState)state.with(HorizontalFacingBlock.FACING, $$7);
    }

    protected boolean addChest(ServerWorldAccess world, BlockBox boundingBox, Random random, BlockPos pos, RegistryKey<LootTable> lootTable, @Nullable BlockState block) {
        if (!boundingBox.contains(pos) || world.getBlockState(pos).isOf(Blocks.CHEST)) {
            return false;
        }
        if (block == null) {
            block = StructurePiece.orientateChest(world, pos, Blocks.CHEST.getDefaultState());
        }
        world.setBlockState(pos, block, 2);
        BlockEntity $$6 = world.getBlockEntity(pos);
        if ($$6 instanceof ChestBlockEntity) {
            ((ChestBlockEntity)$$6).setLootTable(lootTable, random.nextLong());
        }
        return true;
    }

    protected boolean addDispenser(StructureWorldAccess world, BlockBox boundingBox, Random random, int x, int y, int z, Direction facing, RegistryKey<LootTable> lootTable) {
        BlockPos.Mutable $$8 = this.offsetPos(x, y, z);
        if (boundingBox.contains($$8) && !world.getBlockState($$8).isOf(Blocks.DISPENSER)) {
            this.addBlock(world, (BlockState)Blocks.DISPENSER.getDefaultState().with(DispenserBlock.FACING, facing), x, y, z, boundingBox);
            BlockEntity $$9 = world.getBlockEntity($$8);
            if ($$9 instanceof DispenserBlockEntity) {
                ((DispenserBlockEntity)$$9).setLootTable(lootTable, random.nextLong());
            }
            return true;
        }
        return false;
    }

    public void translate(int x, int y, int z) {
        this.boundingBox.move(x, y, z);
    }

    public static BlockBox boundingBox(Stream<StructurePiece> pieces) {
        return BlockBox.encompass(pieces.map(StructurePiece::getBoundingBox)::iterator).orElseThrow(() -> new IllegalStateException("Unable to calculate boundingbox without pieces"));
    }

    @Nullable
    public static StructurePiece firstIntersecting(List<StructurePiece> pieces, BlockBox box) {
        for (StructurePiece $$2 : pieces) {
            if (!$$2.getBoundingBox().intersects(box)) continue;
            return $$2;
        }
        return null;
    }

    @Nullable
    public Direction getFacing() {
        return this.facing;
    }

    public void setOrientation(@Nullable Direction orientation) {
        this.facing = orientation;
        if (orientation == null) {
            this.rotation = BlockRotation.NONE;
            this.mirror = BlockMirror.NONE;
        } else {
            switch (orientation) {
                case SOUTH: {
                    this.mirror = BlockMirror.LEFT_RIGHT;
                    this.rotation = BlockRotation.NONE;
                    break;
                }
                case WEST: {
                    this.mirror = BlockMirror.LEFT_RIGHT;
                    this.rotation = BlockRotation.CLOCKWISE_90;
                    break;
                }
                case EAST: {
                    this.mirror = BlockMirror.NONE;
                    this.rotation = BlockRotation.CLOCKWISE_90;
                    break;
                }
                default: {
                    this.mirror = BlockMirror.NONE;
                    this.rotation = BlockRotation.NONE;
                }
            }
        }
    }

    public BlockRotation getRotation() {
        return this.rotation;
    }

    public BlockMirror getMirror() {
        return this.mirror;
    }

    public StructurePieceType getType() {
        return this.type;
    }

    public static abstract class BlockRandomizer {
        protected BlockState block = Blocks.AIR.getDefaultState();

        public abstract void setBlock(Random var1, int var2, int var3, int var4, boolean var5);

        public BlockState getBlock() {
            return this.block;
        }
    }
}

