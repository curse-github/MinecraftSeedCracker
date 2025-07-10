/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.serialization.Codec
 *  javax.annotation.Nullable
 */
package net.minecraft.structure;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class StrongholdGenerator {
    private static final int field_31624 = 3;
    private static final int field_31625 = 3;
    private static final int field_31626 = 50;
    private static final int field_31627 = 10;
    private static final boolean field_31628 = true;
    public static final int field_36417 = 64;
    private static final PieceData[] ALL_PIECES = new PieceData[]{new PieceData(Corridor.class, 40, 0), new PieceData(PrisonHall.class, 5, 5), new PieceData(LeftTurn.class, 20, 0), new PieceData(RightTurn.class, 20, 0), new PieceData(SquareRoom.class, 10, 6), new PieceData(Stairs.class, 5, 5), new PieceData(SpiralStaircase.class, 5, 5), new PieceData(FiveWayCrossing.class, 5, 4), new PieceData(ChestCorridor.class, 5, 4), new PieceData(Library.class, 10, 2){

        @Override
        public boolean canGenerate(int chainLength) {
            return super.canGenerate(chainLength) && chainLength > 4;
        }
    }, new PieceData(PortalRoom.class, 20, 1){

        @Override
        public boolean canGenerate(int chainLength) {
            return super.canGenerate(chainLength) && chainLength > 5;
        }
    }};
    private static List<PieceData> possiblePieces;
    static Class<? extends Piece> activePieceType;
    private static int totalWeight;
    static final StoneBrickRandomizer STONE_BRICK_RANDOMIZER;

    public static void init() {
        possiblePieces = Lists.newArrayList();
        for (PieceData $$0 : ALL_PIECES) {
            $$0.generatedCount = 0;
            possiblePieces.add($$0);
        }
        activePieceType = null;
    }

    private static boolean checkRemainingPieces() {
        boolean $$0 = false;
        totalWeight = 0;
        for (PieceData $$1 : possiblePieces) {
            if ($$1.limit > 0 && $$1.generatedCount < $$1.limit) {
                $$0 = true;
            }
            totalWeight += $$1.weight;
        }
        return $$0;
    }

    private static Piece createPiece(Class<? extends Piece> pieceType, StructurePiecesHolder holder, Random random, int x, int y, int z, @Nullable Direction orientation, int chainLength) {
        Piece $$8 = null;
        if (pieceType == Corridor.class) {
            $$8 = Corridor.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == PrisonHall.class) {
            $$8 = PrisonHall.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == LeftTurn.class) {
            $$8 = LeftTurn.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == RightTurn.class) {
            $$8 = RightTurn.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == SquareRoom.class) {
            $$8 = SquareRoom.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == Stairs.class) {
            $$8 = Stairs.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == SpiralStaircase.class) {
            $$8 = SpiralStaircase.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == FiveWayCrossing.class) {
            $$8 = FiveWayCrossing.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == ChestCorridor.class) {
            $$8 = ChestCorridor.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == Library.class) {
            $$8 = Library.create(holder, random, x, y, z, orientation, chainLength);
        } else if (pieceType == PortalRoom.class) {
            $$8 = PortalRoom.create(holder, x, y, z, orientation, chainLength);
        }
        return $$8;
    }

    private static Piece pickPiece(Start start, StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
        if (!StrongholdGenerator.checkRemainingPieces()) {
            return null;
        }
        if (activePieceType != null) {
            Piece $$8 = StrongholdGenerator.createPiece(activePieceType, holder, random, x, y, z, orientation, chainLength);
            activePieceType = null;
            if ($$8 != null) {
                return $$8;
            }
        }
        int attempts = 0;
        block0: while (attempts < 5) {
            ++attempts;
            int rnd = random.nextInt(totalWeight);
            for (PieceData possiblePiece : possiblePieces) {
                if ((rnd -= possiblePiece.weight) >= 0) continue;
                if (!possiblePiece.canGenerate(chainLength) || possiblePiece == start.lastPiece) continue block0;
                Piece piece = StrongholdGenerator.createPiece(possiblePiece.pieceType, holder, random, x, y, z, orientation, chainLength);
                if (piece == null) continue;
                ++possiblePiece.generatedCount;
                start.lastPiece = possiblePiece;
                if (!possiblePiece.canGenerate()) {
                    possiblePieces.remove(possiblePiece);
                }
                return piece;
            }
        }
        BlockBox $$13 = SmallCorridor.create(holder, random, x, y, z, orientation);
        if ($$13 != null && $$13.getMinY() > 1) {
            return new SmallCorridor(chainLength, $$13, orientation);
        }
        return null;
    }

    static StructurePiece pieceGenerator(Start start, StructurePiecesHolder holder, Random random, int x, int y, int z, @Nullable Direction orientation, int chainLength) {
        if (chainLength > 50) {
            return null;
        }
        if (Math.abs(x - start.getBoundingBox().getMinX()) > 112 || Math.abs(z - start.getBoundingBox().getMinZ()) > 112) {
            return null;
        }
        Piece $$8 = StrongholdGenerator.pickPiece(start, holder, random, x, y, z, orientation, chainLength + 1);
        if ($$8 != null) {
            holder.addPiece($$8);
            start.pieces.add($$8);
        }
        return $$8;
    }

    static {
        STONE_BRICK_RANDOMIZER = new StoneBrickRandomizer();
    }

    static class PieceData {
        public final Class<? extends Piece> pieceType;
        public final int weight;
        public int generatedCount;
        public final int limit;

        public PieceData(Class<? extends Piece> pieceType, int weight, int limit) {
            this.pieceType = pieceType;
            this.weight = weight;
            this.limit = limit;
        }

        public boolean canGenerate(int chainLength) {
            return this.limit == 0 || this.generatedCount < this.limit;
        }

        public boolean canGenerate() {
            return this.limit == 0 || this.generatedCount < this.limit;
        }
    }

    public static class Corridor
    extends Piece {
        private static final int SIZE_X = 5;
        private static final int SIZE_Y = 5;
        private static final int SIZE_Z = 7;
        private final boolean leftExitExists;
        private final boolean rightExitExists;

        public Corridor(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_CORRIDOR, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
            this.leftExitExists = random.nextInt(2) == 0;
            this.rightExitExists = random.nextInt(2) == 0;
        }

        public Corridor(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_CORRIDOR, nbt);
            this.leftExitExists = nbt.getBoolean("Left", false);
            this.rightExitExists = nbt.getBoolean("Right", false);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putBoolean("Left", this.leftExitExists);
            nbt.putBoolean("Right", this.rightExitExists);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            this.fillForwardOpening((Start)start, holder, random, 1, 1);
            if (this.leftExitExists) {
                this.fillNWOpening((Start)start, holder, random, 1, 2);
            }
            if (this.rightExitExists) {
                this.fillSEOpening((Start)start, holder, random, 1, 2);
            }
        }

        public static Corridor create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -1, -1, 0, 5, 5, 7, orientation);
            if (!Corridor.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new Corridor(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 4, 4, 6, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 1, 1, 0);
            this.generateEntrance(world, random, chunkBox, Piece.EntranceType.OPENING, 1, 1, 6);
            BlockState $$7 = (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.EAST);
            BlockState $$8 = (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.WEST);
            this.addBlockWithRandomThreshold(world, chunkBox, random, 0.1f, 1, 2, 1, $$7);
            this.addBlockWithRandomThreshold(world, chunkBox, random, 0.1f, 3, 2, 1, $$8);
            this.addBlockWithRandomThreshold(world, chunkBox, random, 0.1f, 1, 2, 5, $$7);
            this.addBlockWithRandomThreshold(world, chunkBox, random, 0.1f, 3, 2, 5, $$8);
            if (this.leftExitExists) {
                this.fillWithOutline(world, chunkBox, 0, 1, 2, 0, 3, 4, AIR, AIR, false);
            }
            if (this.rightExitExists) {
                this.fillWithOutline(world, chunkBox, 4, 1, 2, 4, 3, 4, AIR, AIR, false);
            }
        }
    }

    public static class PrisonHall
    extends Piece {
        protected static final int SIZE_X = 9;
        protected static final int SIZE_Y = 5;
        protected static final int SIZE_Z = 11;

        public PrisonHall(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_PRISON_HALL, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
        }

        public PrisonHall(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_PRISON_HALL, nbt);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            this.fillForwardOpening((Start)start, holder, random, 1, 1);
        }

        public static PrisonHall create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -1, -1, 0, 9, 5, 11, orientation);
            if (!PrisonHall.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new PrisonHall(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 8, 4, 10, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 1, 1, 0);
            this.fillWithOutline(world, chunkBox, 1, 1, 10, 3, 3, 10, AIR, AIR, false);
            this.fillWithOutline(world, chunkBox, 4, 1, 1, 4, 3, 1, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 4, 1, 3, 4, 3, 3, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 4, 1, 7, 4, 3, 7, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 4, 1, 9, 4, 3, 9, false, random, STONE_BRICK_RANDOMIZER);
            for (int $$7 = 1; $$7 <= 3; ++$$7) {
                this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, true)).with(PaneBlock.SOUTH, true), 4, $$7, 4, chunkBox);
                this.addBlock(world, (BlockState)((BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, true)).with(PaneBlock.SOUTH, true)).with(PaneBlock.EAST, true), 4, $$7, 5, chunkBox);
                this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, true)).with(PaneBlock.SOUTH, true), 4, $$7, 6, chunkBox);
                this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.WEST, true)).with(PaneBlock.EAST, true), 5, $$7, 5, chunkBox);
                this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.WEST, true)).with(PaneBlock.EAST, true), 6, $$7, 5, chunkBox);
                this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.WEST, true)).with(PaneBlock.EAST, true), 7, $$7, 5, chunkBox);
            }
            this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, true)).with(PaneBlock.SOUTH, true), 4, 3, 2, chunkBox);
            this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, true)).with(PaneBlock.SOUTH, true), 4, 3, 8, chunkBox);
            BlockState $$8 = (BlockState)Blocks.IRON_DOOR.getDefaultState().with(DoorBlock.FACING, Direction.WEST);
            BlockState $$9 = (BlockState)((BlockState)Blocks.IRON_DOOR.getDefaultState().with(DoorBlock.FACING, Direction.WEST)).with(DoorBlock.HALF, DoubleBlockHalf.UPPER);
            this.addBlock(world, $$8, 4, 1, 2, chunkBox);
            this.addBlock(world, $$9, 4, 2, 2, chunkBox);
            this.addBlock(world, $$8, 4, 1, 8, chunkBox);
            this.addBlock(world, $$9, 4, 2, 8, chunkBox);
        }
    }

    public static class LeftTurn
    extends Turn {
        public LeftTurn(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_LEFT_TURN, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
        }

        public LeftTurn(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_LEFT_TURN, nbt);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            Direction $$3 = this.getFacing();
            if ($$3 == Direction.NORTH || $$3 == Direction.EAST) {
                this.fillNWOpening((Start)start, holder, random, 1, 1);
            } else {
                this.fillSEOpening((Start)start, holder, random, 1, 1);
            }
        }

        public static LeftTurn create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -1, -1, 0, 5, 5, 5, orientation);
            if (!LeftTurn.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new LeftTurn(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 4, 4, 4, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 1, 1, 0);
            Direction $$7 = this.getFacing();
            if ($$7 == Direction.NORTH || $$7 == Direction.EAST) {
                this.fillWithOutline(world, chunkBox, 0, 1, 1, 0, 3, 3, AIR, AIR, false);
            } else {
                this.fillWithOutline(world, chunkBox, 4, 1, 1, 4, 3, 3, AIR, AIR, false);
            }
        }
    }

    public static class RightTurn
    extends Turn {
        public RightTurn(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_RIGHT_TURN, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
        }

        public RightTurn(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_RIGHT_TURN, nbt);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            Direction $$3 = this.getFacing();
            if ($$3 == Direction.NORTH || $$3 == Direction.EAST) {
                this.fillSEOpening((Start)start, holder, random, 1, 1);
            } else {
                this.fillNWOpening((Start)start, holder, random, 1, 1);
            }
        }

        public static RightTurn create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -1, -1, 0, 5, 5, 5, orientation);
            if (!RightTurn.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new RightTurn(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 4, 4, 4, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 1, 1, 0);
            Direction $$7 = this.getFacing();
            if ($$7 == Direction.NORTH || $$7 == Direction.EAST) {
                this.fillWithOutline(world, chunkBox, 4, 1, 1, 4, 3, 3, AIR, AIR, false);
            } else {
                this.fillWithOutline(world, chunkBox, 0, 1, 1, 0, 3, 3, AIR, AIR, false);
            }
        }
    }

    public static class SquareRoom
    extends Piece {
        protected static final int SIZE_X = 11;
        protected static final int SIZE_Y = 7;
        protected static final int SIZE_Z = 11;
        protected final int roomType;

        public SquareRoom(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_SQUARE_ROOM, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
            this.roomType = random.nextInt(5);
        }

        public SquareRoom(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_SQUARE_ROOM, nbt);
            this.roomType = nbt.getInt("Type", 0);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putInt("Type", this.roomType);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            this.fillForwardOpening((Start)start, holder, random, 4, 1);
            this.fillNWOpening((Start)start, holder, random, 1, 4);
            this.fillSEOpening((Start)start, holder, random, 1, 4);
        }

        public static SquareRoom create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -4, -1, 0, 11, 7, 11, orientation);
            if (!SquareRoom.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new SquareRoom(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 10, 6, 10, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 4, 1, 0);
            this.fillWithOutline(world, chunkBox, 4, 1, 10, 6, 3, 10, AIR, AIR, false);
            this.fillWithOutline(world, chunkBox, 0, 1, 4, 0, 3, 6, AIR, AIR, false);
            this.fillWithOutline(world, chunkBox, 10, 1, 4, 10, 3, 6, AIR, AIR, false);
            switch (this.roomType) {
                default: {
                    break;
                }
                case 0: {
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 5, 1, 5, chunkBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 5, 2, 5, chunkBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 5, 3, 5, chunkBox);
                    this.addBlock(world, (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.WEST), 4, 3, 5, chunkBox);
                    this.addBlock(world, (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.EAST), 6, 3, 5, chunkBox);
                    this.addBlock(world, (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.SOUTH), 5, 3, 4, chunkBox);
                    this.addBlock(world, (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.NORTH), 5, 3, 6, chunkBox);
                    this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 4, 1, 4, chunkBox);
                    this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 4, 1, 5, chunkBox);
                    this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 4, 1, 6, chunkBox);
                    this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 6, 1, 4, chunkBox);
                    this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 6, 1, 5, chunkBox);
                    this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 6, 1, 6, chunkBox);
                    this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 5, 1, 4, chunkBox);
                    this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 5, 1, 6, chunkBox);
                    break;
                }
                case 1: {
                    for (int $$7 = 0; $$7 < 5; ++$$7) {
                        this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3, 1, 3 + $$7, chunkBox);
                        this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 7, 1, 3 + $$7, chunkBox);
                        this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3 + $$7, 1, 3, chunkBox);
                        this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3 + $$7, 1, 7, chunkBox);
                    }
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 5, 1, 5, chunkBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 5, 2, 5, chunkBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 5, 3, 5, chunkBox);
                    this.addBlock(world, Blocks.WATER.getDefaultState(), 5, 4, 5, chunkBox);
                    break;
                }
                case 2: {
                    for (int $$8 = 1; $$8 <= 9; ++$$8) {
                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 1, 3, $$8, chunkBox);
                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 9, 3, $$8, chunkBox);
                    }
                    for (int $$9 = 1; $$9 <= 9; ++$$9) {
                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), $$9, 3, 1, chunkBox);
                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), $$9, 3, 9, chunkBox);
                    }
                    this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 5, 1, 4, chunkBox);
                    this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 5, 1, 6, chunkBox);
                    this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 5, 3, 4, chunkBox);
                    this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 5, 3, 6, chunkBox);
                    this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 4, 1, 5, chunkBox);
                    this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 6, 1, 5, chunkBox);
                    this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 4, 3, 5, chunkBox);
                    this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 6, 3, 5, chunkBox);
                    for (int $$10 = 1; $$10 <= 3; ++$$10) {
                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 4, $$10, 4, chunkBox);
                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 6, $$10, 4, chunkBox);
                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 4, $$10, 6, chunkBox);
                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), 6, $$10, 6, chunkBox);
                    }
                    this.addBlock(world, Blocks.WALL_TORCH.getDefaultState(), 5, 3, 5, chunkBox);
                    for (int $$11 = 2; $$11 <= 8; ++$$11) {
                        this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 2, 3, $$11, chunkBox);
                        this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 3, 3, $$11, chunkBox);
                        if ($$11 <= 3 || $$11 >= 7) {
                            this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 4, 3, $$11, chunkBox);
                            this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 5, 3, $$11, chunkBox);
                            this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 6, 3, $$11, chunkBox);
                        }
                        this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 7, 3, $$11, chunkBox);
                        this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 8, 3, $$11, chunkBox);
                    }
                    BlockState $$12 = (BlockState)Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.WEST);
                    this.addBlock(world, $$12, 9, 1, 3, chunkBox);
                    this.addBlock(world, $$12, 9, 2, 3, chunkBox);
                    this.addBlock(world, $$12, 9, 3, 3, chunkBox);
                    this.addChest(world, chunkBox, random, 3, 4, 8, LootTables.STRONGHOLD_CROSSING_CHEST);
                }
            }
        }
    }

    public static class Stairs
    extends Piece {
        private static final int SIZE_X = 5;
        private static final int SIZE_Y = 11;
        private static final int SIZE_Z = 8;

        public Stairs(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_STAIRS, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
        }

        public Stairs(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_STAIRS, nbt);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            this.fillForwardOpening((Start)start, holder, random, 1, 1);
        }

        public static Stairs create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -1, -7, 0, 5, 11, 8, orientation);
            if (!Stairs.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new Stairs(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 4, 10, 7, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 1, 7, 0);
            this.generateEntrance(world, random, chunkBox, Piece.EntranceType.OPENING, 1, 1, 7);
            BlockState $$7 = (BlockState)Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH);
            for (int $$8 = 0; $$8 < 6; ++$$8) {
                this.addBlock(world, $$7, 1, 6 - $$8, 1 + $$8, chunkBox);
                this.addBlock(world, $$7, 2, 6 - $$8, 1 + $$8, chunkBox);
                this.addBlock(world, $$7, 3, 6 - $$8, 1 + $$8, chunkBox);
                if ($$8 >= 5) continue;
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 1, 5 - $$8, 1 + $$8, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 2, 5 - $$8, 1 + $$8, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3, 5 - $$8, 1 + $$8, chunkBox);
            }
        }
    }

    public static class SpiralStaircase
    extends Piece {
        private static final int SIZE_X = 5;
        private static final int SIZE_Y = 11;
        private static final int SIZE_Z = 5;
        private final boolean isStructureStart;

        public SpiralStaircase(StructurePieceType structurePieceType, int chainLength, int x, int z, Direction orientation) {
            super(structurePieceType, chainLength, SpiralStaircase.createBox(x, 64, z, orientation, 5, 11, 5));
            this.isStructureStart = true;
            this.setOrientation(orientation);
            this.entryDoor = Piece.EntranceType.OPENING;
        }

        public SpiralStaircase(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_SPIRAL_STAIRCASE, chainLength, boundingBox);
            this.isStructureStart = false;
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
        }

        public SpiralStaircase(StructurePieceType type, NbtCompound nbt) {
            super(type, nbt);
            this.isStructureStart = nbt.getBoolean("Source", false);
        }

        public SpiralStaircase(NbtCompound nbt) {
            this(StructurePieceType.STRONGHOLD_SPIRAL_STAIRCASE, nbt);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putBoolean("Source", this.isStructureStart);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            if (this.isStructureStart) {
                activePieceType = FiveWayCrossing.class;
            }
            this.fillForwardOpening((Start)start, holder, random, 1, 1);
        }

        public static SpiralStaircase create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -1, -7, 0, 5, 11, 5, orientation);
            if (!SpiralStaircase.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new SpiralStaircase(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 4, 10, 4, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 1, 7, 0);
            this.generateEntrance(world, random, chunkBox, Piece.EntranceType.OPENING, 1, 1, 4);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 2, 6, 1, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 1, 5, 1, chunkBox);
            this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 1, 6, 1, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 1, 5, 2, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 1, 4, 3, chunkBox);
            this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 1, 5, 3, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 2, 4, 3, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3, 3, 3, chunkBox);
            this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 3, 4, 3, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3, 3, 2, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3, 2, 1, chunkBox);
            this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 3, 3, 1, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 2, 2, 1, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 1, 1, 1, chunkBox);
            this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 1, 2, 1, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 1, 1, 2, chunkBox);
            this.addBlock(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), 1, 1, 3, chunkBox);
        }
    }

    public static class FiveWayCrossing
    extends Piece {
        protected static final int SIZE_X = 10;
        protected static final int SIZE_Y = 9;
        protected static final int SIZE_Z = 11;
        private final boolean lowerLeftExists;
        private final boolean upperLeftExists;
        private final boolean lowerRightExists;
        private final boolean upperRightExists;

        public FiveWayCrossing(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_FIVE_WAY_CROSSING, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
            this.lowerLeftExists = random.nextBoolean();
            this.upperLeftExists = random.nextBoolean();
            this.lowerRightExists = random.nextBoolean();
            this.upperRightExists = random.nextInt(3) > 0;
        }

        public FiveWayCrossing(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_FIVE_WAY_CROSSING, nbt);
            this.lowerLeftExists = nbt.getBoolean("leftLow", false);
            this.upperLeftExists = nbt.getBoolean("leftHigh", false);
            this.lowerRightExists = nbt.getBoolean("rightLow", false);
            this.upperRightExists = nbt.getBoolean("rightHigh", false);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putBoolean("leftLow", this.lowerLeftExists);
            nbt.putBoolean("leftHigh", this.upperLeftExists);
            nbt.putBoolean("rightLow", this.lowerRightExists);
            nbt.putBoolean("rightHigh", this.upperRightExists);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            int $$3 = 3;
            int $$4 = 5;
            Direction $$5 = this.getFacing();
            if ($$5 == Direction.WEST || $$5 == Direction.NORTH) {
                $$3 = 8 - $$3;
                $$4 = 8 - $$4;
            }
            this.fillForwardOpening((Start)start, holder, random, 5, 1);
            if (this.lowerLeftExists) {
                this.fillNWOpening((Start)start, holder, random, $$3, 1);
            }
            if (this.upperLeftExists) {
                this.fillNWOpening((Start)start, holder, random, $$4, 7);
            }
            if (this.lowerRightExists) {
                this.fillSEOpening((Start)start, holder, random, $$3, 1);
            }
            if (this.upperRightExists) {
                this.fillSEOpening((Start)start, holder, random, $$4, 7);
            }
        }

        public static FiveWayCrossing create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -4, -3, 0, 10, 9, 11, orientation);
            if (!FiveWayCrossing.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new FiveWayCrossing(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 9, 8, 10, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 4, 3, 0);
            if (this.lowerLeftExists) {
                this.fillWithOutline(world, chunkBox, 0, 3, 1, 0, 5, 3, AIR, AIR, false);
            }
            if (this.lowerRightExists) {
                this.fillWithOutline(world, chunkBox, 9, 3, 1, 9, 5, 3, AIR, AIR, false);
            }
            if (this.upperLeftExists) {
                this.fillWithOutline(world, chunkBox, 0, 5, 7, 0, 7, 9, AIR, AIR, false);
            }
            if (this.upperRightExists) {
                this.fillWithOutline(world, chunkBox, 9, 5, 7, 9, 7, 9, AIR, AIR, false);
            }
            this.fillWithOutline(world, chunkBox, 5, 1, 10, 7, 3, 10, AIR, AIR, false);
            this.fillWithOutline(world, chunkBox, 1, 2, 1, 8, 2, 6, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 4, 1, 5, 4, 4, 9, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 8, 1, 5, 8, 4, 9, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 1, 4, 7, 3, 4, 9, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 1, 3, 5, 3, 3, 6, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 1, 3, 4, 3, 3, 4, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), Blocks.SMOOTH_STONE_SLAB.getDefaultState(), false);
            this.fillWithOutline(world, chunkBox, 1, 4, 6, 3, 4, 6, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), Blocks.SMOOTH_STONE_SLAB.getDefaultState(), false);
            this.fillWithOutline(world, chunkBox, 5, 1, 7, 7, 1, 8, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 5, 1, 9, 7, 1, 9, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), Blocks.SMOOTH_STONE_SLAB.getDefaultState(), false);
            this.fillWithOutline(world, chunkBox, 5, 2, 7, 7, 2, 7, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), Blocks.SMOOTH_STONE_SLAB.getDefaultState(), false);
            this.fillWithOutline(world, chunkBox, 4, 5, 7, 4, 5, 9, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), Blocks.SMOOTH_STONE_SLAB.getDefaultState(), false);
            this.fillWithOutline(world, chunkBox, 8, 5, 7, 8, 5, 9, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), Blocks.SMOOTH_STONE_SLAB.getDefaultState(), false);
            this.fillWithOutline(world, chunkBox, 5, 5, 7, 7, 5, 9, (BlockState)Blocks.SMOOTH_STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE), (BlockState)Blocks.SMOOTH_STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE), false);
            this.addBlock(world, (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.SOUTH), 6, 5, 6, chunkBox);
        }
    }

    public static class ChestCorridor
    extends Piece {
        private static final int SIZE_X = 5;
        private static final int SIZE_Y = 5;
        private static final int SIZE_Z = 7;
        private boolean chestGenerated;

        public ChestCorridor(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
        }

        public ChestCorridor(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, nbt);
            this.chestGenerated = nbt.getBoolean("Chest", false);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putBoolean("Chest", this.chestGenerated);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            this.fillForwardOpening((Start)start, holder, random, 1, 1);
        }

        public static ChestCorridor create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainlength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -1, -1, 0, 5, 5, 7, orientation);
            if (!ChestCorridor.isInBounds($$7) || holder.getIntersecting($$7) != null) {
                return null;
            }
            return new ChestCorridor(chainlength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 4, 4, 6, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 1, 1, 0);
            this.generateEntrance(world, random, chunkBox, Piece.EntranceType.OPENING, 1, 1, 6);
            this.fillWithOutline(world, chunkBox, 3, 1, 2, 3, 1, 4, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), false);
            this.addBlock(world, Blocks.STONE_BRICK_SLAB.getDefaultState(), 3, 1, 1, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICK_SLAB.getDefaultState(), 3, 1, 5, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICK_SLAB.getDefaultState(), 3, 2, 2, chunkBox);
            this.addBlock(world, Blocks.STONE_BRICK_SLAB.getDefaultState(), 3, 2, 4, chunkBox);
            for (int $$7 = 2; $$7 <= 4; ++$$7) {
                this.addBlock(world, Blocks.STONE_BRICK_SLAB.getDefaultState(), 2, 1, $$7, chunkBox);
            }
            if (!this.chestGenerated && chunkBox.contains(this.offsetPos(3, 2, 3))) {
                this.chestGenerated = true;
                this.addChest(world, chunkBox, random, 3, 2, 3, LootTables.STRONGHOLD_CORRIDOR_CHEST);
            }
        }
    }

    public static class Library
    extends Piece {
        protected static final int SIZE_X = 14;
        protected static final int field_31636 = 6;
        protected static final int SIZE_Y = 11;
        protected static final int SIZE_Z = 15;
        private final boolean tall;

        public Library(int chainLength, Random random, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_LIBRARY, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.entryDoor = this.getRandomEntrance(random);
            this.tall = boundingBox.getBlockCountY() > 6;
        }

        public Library(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_LIBRARY, nbt);
            this.tall = nbt.getBoolean("Tall", false);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putBoolean("Tall", this.tall);
        }

        public static Library create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$7 = BlockBox.rotated(x, y, z, -4, -1, 0, 14, 11, 15, orientation);
            if (!(Library.isInBounds($$7) && holder.getIntersecting($$7) == null || Library.isInBounds($$7 = BlockBox.rotated(x, y, z, -4, -1, 0, 14, 6, 15, orientation)) && holder.getIntersecting($$7) == null)) {
                return null;
            }
            return new Library(chainLength, random, $$7, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            int $$7 = 11;
            if (!this.tall) {
                $$7 = 6;
            }
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 13, $$7 - 1, 14, true, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, this.entryDoor, 4, 1, 0);
            this.fillWithOutlineUnderSeaLevel(world, chunkBox, random, 0.07f, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.getDefaultState(), Blocks.COBWEB.getDefaultState(), false, false);
            boolean $$8 = true;
            int $$9 = 12;
            for (int $$10 = 1; $$10 <= 13; ++$$10) {
                if (($$10 - 1) % 4 == 0) {
                    this.fillWithOutline(world, chunkBox, 1, 1, $$10, 1, 4, $$10, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
                    this.fillWithOutline(world, chunkBox, 12, 1, $$10, 12, 4, $$10, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
                    this.addBlock(world, (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.EAST), 2, 3, $$10, chunkBox);
                    this.addBlock(world, (BlockState)Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.WEST), 11, 3, $$10, chunkBox);
                    if (!this.tall) continue;
                    this.fillWithOutline(world, chunkBox, 1, 6, $$10, 1, 9, $$10, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
                    this.fillWithOutline(world, chunkBox, 12, 6, $$10, 12, 9, $$10, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
                    continue;
                }
                this.fillWithOutline(world, chunkBox, 1, 1, $$10, 1, 4, $$10, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                this.fillWithOutline(world, chunkBox, 12, 1, $$10, 12, 4, $$10, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                if (!this.tall) continue;
                this.fillWithOutline(world, chunkBox, 1, 6, $$10, 1, 9, $$10, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                this.fillWithOutline(world, chunkBox, 12, 6, $$10, 12, 9, $$10, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
            for (int $$11 = 3; $$11 < 12; $$11 += 2) {
                this.fillWithOutline(world, chunkBox, 3, 1, $$11, 4, 3, $$11, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                this.fillWithOutline(world, chunkBox, 6, 1, $$11, 7, 3, $$11, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
                this.fillWithOutline(world, chunkBox, 9, 1, $$11, 10, 3, $$11, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
            }
            if (this.tall) {
                this.fillWithOutline(world, chunkBox, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
                this.fillWithOutline(world, chunkBox, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
                this.fillWithOutline(world, chunkBox, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
                this.fillWithOutline(world, chunkBox, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
                this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 9, 5, 11, chunkBox);
                this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 8, 5, 11, chunkBox);
                this.addBlock(world, Blocks.OAK_PLANKS.getDefaultState(), 9, 5, 10, chunkBox);
                BlockState $$12 = (BlockState)((BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.WEST, true)).with(FenceBlock.EAST, true);
                BlockState $$13 = (BlockState)((BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.NORTH, true)).with(FenceBlock.SOUTH, true);
                this.fillWithOutline(world, chunkBox, 3, 6, 3, 3, 6, 11, $$13, $$13, false);
                this.fillWithOutline(world, chunkBox, 10, 6, 3, 10, 6, 9, $$13, $$13, false);
                this.fillWithOutline(world, chunkBox, 4, 6, 2, 9, 6, 2, $$12, $$12, false);
                this.fillWithOutline(world, chunkBox, 4, 6, 12, 7, 6, 12, $$12, $$12, false);
                this.addBlock(world, (BlockState)((BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.NORTH, true)).with(FenceBlock.EAST, true), 3, 6, 2, chunkBox);
                this.addBlock(world, (BlockState)((BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.SOUTH, true)).with(FenceBlock.EAST, true), 3, 6, 12, chunkBox);
                this.addBlock(world, (BlockState)((BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.NORTH, true)).with(FenceBlock.WEST, true), 10, 6, 2, chunkBox);
                for (int $$14 = 0; $$14 <= 2; ++$$14) {
                    this.addBlock(world, (BlockState)((BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.SOUTH, true)).with(FenceBlock.WEST, true), 8 + $$14, 6, 12 - $$14, chunkBox);
                    if ($$14 == 2) continue;
                    this.addBlock(world, (BlockState)((BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.NORTH, true)).with(FenceBlock.EAST, true), 8 + $$14, 6, 11 - $$14, chunkBox);
                }
                BlockState $$15 = (BlockState)Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.SOUTH);
                this.addBlock(world, $$15, 10, 1, 13, chunkBox);
                this.addBlock(world, $$15, 10, 2, 13, chunkBox);
                this.addBlock(world, $$15, 10, 3, 13, chunkBox);
                this.addBlock(world, $$15, 10, 4, 13, chunkBox);
                this.addBlock(world, $$15, 10, 5, 13, chunkBox);
                this.addBlock(world, $$15, 10, 6, 13, chunkBox);
                this.addBlock(world, $$15, 10, 7, 13, chunkBox);
                int $$16 = 7;
                int $$17 = 7;
                BlockState $$18 = (BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.EAST, true);
                this.addBlock(world, $$18, 6, 9, 7, chunkBox);
                BlockState $$19 = (BlockState)Blocks.OAK_FENCE.getDefaultState().with(FenceBlock.WEST, true);
                this.addBlock(world, $$19, 7, 9, 7, chunkBox);
                this.addBlock(world, $$18, 6, 8, 7, chunkBox);
                this.addBlock(world, $$19, 7, 8, 7, chunkBox);
                BlockState $$20 = (BlockState)((BlockState)$$13.with(FenceBlock.WEST, true)).with(FenceBlock.EAST, true);
                this.addBlock(world, $$20, 6, 7, 7, chunkBox);
                this.addBlock(world, $$20, 7, 7, 7, chunkBox);
                this.addBlock(world, $$18, 5, 7, 7, chunkBox);
                this.addBlock(world, $$19, 8, 7, 7, chunkBox);
                this.addBlock(world, (BlockState)$$18.with(FenceBlock.NORTH, true), 6, 7, 6, chunkBox);
                this.addBlock(world, (BlockState)$$18.with(FenceBlock.SOUTH, true), 6, 7, 8, chunkBox);
                this.addBlock(world, (BlockState)$$19.with(FenceBlock.NORTH, true), 7, 7, 6, chunkBox);
                this.addBlock(world, (BlockState)$$19.with(FenceBlock.SOUTH, true), 7, 7, 8, chunkBox);
                BlockState $$21 = Blocks.TORCH.getDefaultState();
                this.addBlock(world, $$21, 5, 8, 7, chunkBox);
                this.addBlock(world, $$21, 8, 8, 7, chunkBox);
                this.addBlock(world, $$21, 6, 8, 6, chunkBox);
                this.addBlock(world, $$21, 6, 8, 8, chunkBox);
                this.addBlock(world, $$21, 7, 8, 6, chunkBox);
                this.addBlock(world, $$21, 7, 8, 8, chunkBox);
            }
            this.addChest(world, chunkBox, random, 3, 3, 5, LootTables.STRONGHOLD_LIBRARY_CHEST);
            if (this.tall) {
                this.addBlock(world, AIR, 12, 9, 1, chunkBox);
                this.addChest(world, chunkBox, random, 12, 8, 1, LootTables.STRONGHOLD_LIBRARY_CHEST);
            }
        }
    }

    public static class PortalRoom
    extends Piece {
        protected static final int SIZE_X = 11;
        protected static final int SIZE_Y = 8;
        protected static final int SIZE_Z = 16;
        private boolean spawnerPlaced;

        public PortalRoom(int chainLength, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, chainLength, boundingBox);
            this.setOrientation(orientation);
        }

        public PortalRoom(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, nbt);
            this.spawnerPlaced = nbt.getBoolean("Mob", false);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putBoolean("Mob", this.spawnerPlaced);
        }

        @Override
        public void fillOpenings(StructurePiece start, StructurePiecesHolder holder, Random random) {
            if (start != null) {
                ((Start)start).portalRoom = this;
            }
        }

        public static PortalRoom create(StructurePiecesHolder holder, int x, int y, int z, Direction orientation, int chainLength) {
            BlockBox $$6 = BlockBox.rotated(x, y, z, -4, -1, 0, 11, 8, 16, orientation);
            if (!PortalRoom.isInBounds($$6) || holder.getIntersecting($$6) != null) {
                return null;
            }
            return new PortalRoom(chainLength, $$6, orientation);
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            BlockPos.Mutable $$22;
            this.fillWithOutline(world, chunkBox, 0, 0, 0, 10, 7, 15, false, random, STONE_BRICK_RANDOMIZER);
            this.generateEntrance(world, random, chunkBox, Piece.EntranceType.GRATES, 4, 1, 0);
            int $$7 = 6;
            this.fillWithOutline(world, chunkBox, 1, 6, 1, 1, 6, 14, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 9, 6, 1, 9, 6, 14, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 2, 6, 1, 8, 6, 2, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 2, 6, 14, 8, 6, 14, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 1, 1, 1, 2, 1, 4, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 8, 1, 1, 9, 1, 4, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 1, 1, 1, 1, 1, 3, Blocks.LAVA.getDefaultState(), Blocks.LAVA.getDefaultState(), false);
            this.fillWithOutline(world, chunkBox, 9, 1, 1, 9, 1, 3, Blocks.LAVA.getDefaultState(), Blocks.LAVA.getDefaultState(), false);
            this.fillWithOutline(world, chunkBox, 3, 1, 8, 7, 1, 12, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 4, 1, 9, 6, 1, 11, Blocks.LAVA.getDefaultState(), Blocks.LAVA.getDefaultState(), false);
            BlockState $$8 = (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, true)).with(PaneBlock.SOUTH, true);
            BlockState $$9 = (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.WEST, true)).with(PaneBlock.EAST, true);
            for (int $$10 = 3; $$10 < 14; $$10 += 2) {
                this.fillWithOutline(world, chunkBox, 0, 3, $$10, 0, 4, $$10, $$8, $$8, false);
                this.fillWithOutline(world, chunkBox, 10, 3, $$10, 10, 4, $$10, $$8, $$8, false);
            }
            for (int $$11 = 2; $$11 < 9; $$11 += 2) {
                this.fillWithOutline(world, chunkBox, $$11, 3, 15, $$11, 4, 15, $$9, $$9, false);
            }
            BlockState $$12 = (BlockState)Blocks.STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH);
            this.fillWithOutline(world, chunkBox, 4, 1, 5, 6, 1, 7, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 4, 2, 6, 6, 2, 7, false, random, STONE_BRICK_RANDOMIZER);
            this.fillWithOutline(world, chunkBox, 4, 3, 7, 6, 3, 7, false, random, STONE_BRICK_RANDOMIZER);
            for (int $$13 = 4; $$13 <= 6; ++$$13) {
                this.addBlock(world, $$12, $$13, 1, 4, chunkBox);
                this.addBlock(world, $$12, $$13, 2, 5, chunkBox);
                this.addBlock(world, $$12, $$13, 3, 6, chunkBox);
            }
            BlockState $$14 = (BlockState)Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.NORTH);
            BlockState $$15 = (BlockState)Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.SOUTH);
            BlockState $$16 = (BlockState)Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.EAST);
            BlockState $$17 = (BlockState)Blocks.END_PORTAL_FRAME.getDefaultState().with(EndPortalFrameBlock.FACING, Direction.WEST);
            boolean any_eye_filled = true;
            boolean[] eyes_filled = new boolean[12];
            for (int i = 0; i < eyes_filled.length; ++i) {
                eyes_filled[i] = random.nextFloat() > 0.9f;
                any_eye_filled &= eyes_filled[i];
            }
            this.addBlock(world, (BlockState)$$14.with(EndPortalFrameBlock.EYE, eyes_filled[0]), 4, 3, 8, chunkBox);
            this.addBlock(world, (BlockState)$$14.with(EndPortalFrameBlock.EYE, eyes_filled[1]), 5, 3, 8, chunkBox);
            this.addBlock(world, (BlockState)$$14.with(EndPortalFrameBlock.EYE, eyes_filled[2]), 6, 3, 8, chunkBox);
            this.addBlock(world, (BlockState)$$15.with(EndPortalFrameBlock.EYE, eyes_filled[3]), 4, 3, 12, chunkBox);
            this.addBlock(world, (BlockState)$$15.with(EndPortalFrameBlock.EYE, eyes_filled[4]), 5, 3, 12, chunkBox);
            this.addBlock(world, (BlockState)$$15.with(EndPortalFrameBlock.EYE, eyes_filled[5]), 6, 3, 12, chunkBox);
            this.addBlock(world, (BlockState)$$16.with(EndPortalFrameBlock.EYE, eyes_filled[6]), 3, 3, 9, chunkBox);
            this.addBlock(world, (BlockState)$$16.with(EndPortalFrameBlock.EYE, eyes_filled[7]), 3, 3, 10, chunkBox);
            this.addBlock(world, (BlockState)$$16.with(EndPortalFrameBlock.EYE, eyes_filled[8]), 3, 3, 11, chunkBox);
            this.addBlock(world, (BlockState)$$17.with(EndPortalFrameBlock.EYE, eyes_filled[9]), 7, 3, 9, chunkBox);
            this.addBlock(world, (BlockState)$$17.with(EndPortalFrameBlock.EYE, eyes_filled[10]), 7, 3, 10, chunkBox);
            this.addBlock(world, (BlockState)$$17.with(EndPortalFrameBlock.EYE, eyes_filled[11]), 7, 3, 11, chunkBox);
            if (any_eye_filled) {
                BlockState $$21 = Blocks.END_PORTAL.getDefaultState();
                this.addBlock(world, $$21, 4, 3, 9, chunkBox);
                this.addBlock(world, $$21, 5, 3, 9, chunkBox);
                this.addBlock(world, $$21, 6, 3, 9, chunkBox);
                this.addBlock(world, $$21, 4, 3, 10, chunkBox);
                this.addBlock(world, $$21, 5, 3, 10, chunkBox);
                this.addBlock(world, $$21, 6, 3, 10, chunkBox);
                this.addBlock(world, $$21, 4, 3, 11, chunkBox);
                this.addBlock(world, $$21, 5, 3, 11, chunkBox);
                this.addBlock(world, $$21, 6, 3, 11, chunkBox);
            }
            if (!this.spawnerPlaced && chunkBox.contains($$22 = this.offsetPos(5, 3, 6))) {
                this.spawnerPlaced = true;
                world.setBlockState($$22, Blocks.SPAWNER.getDefaultState(), 2);
                BlockEntity $$23 = world.getBlockEntity($$22);
                if ($$23 instanceof MobSpawnerBlockEntity) {
                    MobSpawnerBlockEntity $$24 = (MobSpawnerBlockEntity)$$23;
                    $$24.setEntityType(EntityType.SILVERFISH, random);
                }
            }
        }
    }

    static abstract class Piece
    extends StructurePiece {
        protected EntranceType entryDoor = EntranceType.OPENING;

        protected Piece(StructurePieceType type, int length, BlockBox boundingBox) {
            super(type, length, boundingBox);
        }

        public Piece(StructurePieceType type, NbtCompound nbt) {
            super(type, nbt);
            this.entryDoor = nbt.get("EntryDoor", EntranceType.CODEC).orElseThrow();
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            nbt.put("EntryDoor", EntranceType.CODEC, this.entryDoor);
        }

        protected void generateEntrance(StructureWorldAccess world, Random random, BlockBox boundingBox, EntranceType type, int x, int y, int z) {
            switch (type.ordinal()) {
                case 0: {
                    this.fillWithOutline(world, boundingBox, x, y, z, x + 3 - 1, y + 3 - 1, z, AIR, AIR, false);
                    break;
                }
                case 1: {
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x, y, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x, y + 1, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x, y + 2, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x + 1, y + 2, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x + 2, y + 2, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x + 2, y + 1, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x + 2, y, z, boundingBox);
                    this.addBlock(world, Blocks.OAK_DOOR.getDefaultState(), x + 1, y, z, boundingBox);
                    this.addBlock(world, (BlockState)Blocks.OAK_DOOR.getDefaultState().with(DoorBlock.HALF, DoubleBlockHalf.UPPER), x + 1, y + 1, z, boundingBox);
                    break;
                }
                case 2: {
                    this.addBlock(world, Blocks.CAVE_AIR.getDefaultState(), x + 1, y, z, boundingBox);
                    this.addBlock(world, Blocks.CAVE_AIR.getDefaultState(), x + 1, y + 1, z, boundingBox);
                    this.addBlock(world, (BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.WEST, true), x, y, z, boundingBox);
                    this.addBlock(world, (BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.WEST, true), x, y + 1, z, boundingBox);
                    this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.EAST, true)).with(PaneBlock.WEST, true), x, y + 2, z, boundingBox);
                    this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.EAST, true)).with(PaneBlock.WEST, true), x + 1, y + 2, z, boundingBox);
                    this.addBlock(world, (BlockState)((BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.EAST, true)).with(PaneBlock.WEST, true), x + 2, y + 2, z, boundingBox);
                    this.addBlock(world, (BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.EAST, true), x + 2, y + 1, z, boundingBox);
                    this.addBlock(world, (BlockState)Blocks.IRON_BARS.getDefaultState().with(PaneBlock.EAST, true), x + 2, y, z, boundingBox);
                    break;
                }
                case 3: {
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x, y, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x, y + 1, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x, y + 2, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x + 1, y + 2, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x + 2, y + 2, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x + 2, y + 1, z, boundingBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), x + 2, y, z, boundingBox);
                    this.addBlock(world, Blocks.IRON_DOOR.getDefaultState(), x + 1, y, z, boundingBox);
                    this.addBlock(world, (BlockState)Blocks.IRON_DOOR.getDefaultState().with(DoorBlock.HALF, DoubleBlockHalf.UPPER), x + 1, y + 1, z, boundingBox);
                    this.addBlock(world, (BlockState)Blocks.STONE_BUTTON.getDefaultState().with(ButtonBlock.FACING, Direction.NORTH), x + 2, y + 1, z + 1, boundingBox);
                    this.addBlock(world, (BlockState)Blocks.STONE_BUTTON.getDefaultState().with(ButtonBlock.FACING, Direction.SOUTH), x + 2, y + 1, z - 1, boundingBox);
                }
            }
        }

        protected EntranceType getRandomEntrance(Random random) {
            int $$1 = random.nextInt(5);
            switch ($$1) {
                default: {
                    return EntranceType.OPENING;
                }
                case 2: {
                    return EntranceType.WOOD_DOOR;
                }
                case 3: {
                    return EntranceType.GRATES;
                }
                case 4: 
            }
            return EntranceType.IRON_DOOR;
        }

        @Nullable
        protected StructurePiece fillForwardOpening(Start start, StructurePiecesHolder holder, Random random, int leftRightOffset, int heightOffset) {
            Direction facing = this.getFacing();
            if (facing != null) {
                switch (facing) {
                    case NORTH: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + leftRightOffset, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() - 1, facing, this.getChainLength());
                    }
                    case SOUTH: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + leftRightOffset, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMaxZ() + 1, facing, this.getChainLength());
                    }
                    case WEST: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() + leftRightOffset, facing, this.getChainLength());
                    }
                    case EAST: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() + leftRightOffset, facing, this.getChainLength());
                    }
                }
            }
            return null;
        }

        @Nullable
        protected StructurePiece fillNWOpening(Start start, StructurePiecesHolder holder, Random random, int heightOffset, int leftRightOffset) {
            Direction $$5 = this.getFacing();
            if ($$5 != null) {
                switch ($$5) {
                    case NORTH: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() + leftRightOffset, Direction.WEST, this.getChainLength());
                    }
                    case SOUTH: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() + leftRightOffset, Direction.WEST, this.getChainLength());
                    }
                    case WEST: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + leftRightOffset, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() - 1, Direction.NORTH, this.getChainLength());
                    }
                    case EAST: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + leftRightOffset, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() - 1, Direction.NORTH, this.getChainLength());
                    }
                }
            }
            return null;
        }

        @Nullable
        protected StructurePiece fillSEOpening(Start start, StructurePiecesHolder holder, Random random, int heightOffset, int leftRightOffset) {
            Direction $$5 = this.getFacing();
            if ($$5 != null) {
                switch ($$5) {
                    case NORTH: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() + leftRightOffset, Direction.EAST, this.getChainLength());
                    }
                    case SOUTH: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMinZ() + leftRightOffset, Direction.EAST, this.getChainLength());
                    }
                    case WEST: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + leftRightOffset, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMaxZ() + 1, Direction.SOUTH, this.getChainLength());
                    }
                    case EAST: {
                        return StrongholdGenerator.pieceGenerator(start, holder, random, this.boundingBox.getMinX() + leftRightOffset, this.boundingBox.getMinY() + heightOffset, this.boundingBox.getMaxZ() + 1, Direction.SOUTH, this.getChainLength());
                    }
                }
            }
            return null;
        }

        protected static boolean isInBounds(BlockBox boundingBox) {
            return boundingBox != null && boundingBox.getMinY() > 10;
        }

        protected static final class EntranceType
        extends Enum<EntranceType> {
            public static final /* enum */ EntranceType OPENING = new EntranceType();
            public static final /* enum */ EntranceType WOOD_DOOR = new EntranceType();
            public static final /* enum */ EntranceType GRATES = new EntranceType();
            public static final /* enum */ EntranceType IRON_DOOR = new EntranceType();
            @Deprecated
            public static final Codec<EntranceType> CODEC;
            private static final /* synthetic */ EntranceType[] field_15292;

            public static EntranceType[] values() {
                return (EntranceType[])field_15292.clone();
            }

            public static EntranceType valueOf(String string) {
                return Enum.valueOf(EntranceType.class, string);
            }

            private static /* synthetic */ EntranceType[] method_36762() {
                return new EntranceType[]{OPENING, WOOD_DOOR, GRATES, IRON_DOOR};
            }

            static {
                field_15292 = EntranceType.method_36762();
                CODEC = Codecs.enumByName(EntranceType::valueOf);
            }
        }
    }

    public static class Start
    extends SpiralStaircase {
        public PieceData lastPiece;
        @Nullable
        public PortalRoom portalRoom;
        public final List<StructurePiece> pieces = Lists.newArrayList();

        public Start(Random random, int posX, int posY) {
            super(StructurePieceType.STRONGHOLD_START, 0, posX, posY, Start.getRandomHorizontalDirection(random));
        }

        public Start(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_START, nbt);
        }

        @Override
        public BlockPos getCenter() {
            if (this.portalRoom != null) {
                return this.portalRoom.getCenter();
            }
            return super.getCenter();
        }
    }

    public static class SmallCorridor
    extends Piece {
        private final int length;

        public SmallCorridor(int chainLength, BlockBox boundingBox, Direction orientation) {
            super(StructurePieceType.STRONGHOLD_SMALL_CORRIDOR, chainLength, boundingBox);
            this.setOrientation(orientation);
            this.length = orientation == Direction.NORTH || orientation == Direction.SOUTH ? boundingBox.getBlockCountZ() : boundingBox.getBlockCountX();
        }

        public SmallCorridor(NbtCompound nbt) {
            super(StructurePieceType.STRONGHOLD_SMALL_CORRIDOR, nbt);
            this.length = nbt.getInt("Steps", 0);
        }

        @Override
        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putInt("Steps", this.length);
        }

        public static BlockBox create(StructurePiecesHolder holder, Random random, int x, int y, int z, Direction orientation) {
            int $$6 = 3;
            BlockBox box = BlockBox.rotated(x, y, z, -1, -1, 0, 5, 5, 4, orientation);
            StructurePiece intersectingBox = holder.getIntersecting(box);
            if (intersectingBox == null) {
                return null;
            }
            if (intersectingBox.getBoundingBox().getMinY() == box.getMinY()) {
                for (int i = 2; i >= 1; --i) {
                    box = BlockBox.rotated(x, y, z, -1, -1, 0, 5, 5, i, orientation);
                    if (intersectingBox.getBoundingBox().intersects(box)) continue;
                    return BlockBox.rotated(x, y, z, -1, -1, 0, 5, 5, i + 1, orientation);
                }
            }
            return null;
        }

        @Override
        public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            for (int $$7 = 0; $$7 < this.length; ++$$7) {
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 0, 0, $$7, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 1, 0, $$7, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 2, 0, $$7, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3, 0, $$7, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 4, 0, $$7, chunkBox);
                for (int $$8 = 1; $$8 <= 3; ++$$8) {
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 0, $$8, $$7, chunkBox);
                    this.addBlock(world, Blocks.CAVE_AIR.getDefaultState(), 1, $$8, $$7, chunkBox);
                    this.addBlock(world, Blocks.CAVE_AIR.getDefaultState(), 2, $$8, $$7, chunkBox);
                    this.addBlock(world, Blocks.CAVE_AIR.getDefaultState(), 3, $$8, $$7, chunkBox);
                    this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 4, $$8, $$7, chunkBox);
                }
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 0, 4, $$7, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 1, 4, $$7, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 2, 4, $$7, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 3, 4, $$7, chunkBox);
                this.addBlock(world, Blocks.STONE_BRICKS.getDefaultState(), 4, 4, $$7, chunkBox);
            }
        }
    }

    static class StoneBrickRandomizer
    extends StructurePiece.BlockRandomizer {
        StoneBrickRandomizer() {
        }

        @Override
        public void setBlock(Random random, int x, int y, int z, boolean placeBlock) {
            float $$5;
            this.block = placeBlock ? (($$5 = random.nextFloat()) < 0.2f ? Blocks.CRACKED_STONE_BRICKS.getDefaultState() : ($$5 < 0.5f ? Blocks.MOSSY_STONE_BRICKS.getDefaultState() : ($$5 < 0.55f ? Blocks.INFESTED_STONE_BRICKS.getDefaultState() : Blocks.STONE_BRICKS.getDefaultState()))) : Blocks.CAVE_AIR.getDefaultState();
        }
    }

    public static abstract class Turn
    extends Piece {
        protected static final int SIZE_X = 5;
        protected static final int SIZE_Y = 5;
        protected static final int SIZE_Z = 5;

        protected Turn(StructurePieceType type, int length, BlockBox boundingBox) {
            super(type, length, boundingBox);
        }

        public Turn(StructurePieceType type, NbtCompound nbt) {
            super(type, nbt);
        }
    }
}

