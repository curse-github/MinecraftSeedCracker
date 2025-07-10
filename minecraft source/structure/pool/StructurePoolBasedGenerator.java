/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.logging.LogUtils
 *  org.apache.commons.lang3.mutable.MutableObject
 *  org.slf4j.Logger
 */
package net.minecraft.structure.pool;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.block.JigsawBlock;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.EmptyPoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.PriorityIterator;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.DimensionPadding;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

public class StructurePoolBasedGenerator {
    static final Logger LOGGER = LogUtils.getLogger();
    private static final int HEIGHT_NOT_SET = Integer.MIN_VALUE;

    public static Optional<Structure.StructurePosition> generate(Structure.Context context, RegistryEntry<StructurePool> structurePool, Optional<Identifier> id, int size, BlockPos pos, boolean useExpansionHack, Optional<Heightmap.Type> projectStartToHeightmap, int maxDistanceFromCenter, StructurePoolAliasLookup aliasLookup, DimensionPadding dimensionPadding, StructureLiquidSettings liquidSettings) {
        BlockPos $$23;
        DynamicRegistryManager $$11 = context.dynamicRegistryManager();
        ChunkGenerator $$12 = context.chunkGenerator();
        StructureTemplateManager $$13 = context.structureTemplateManager();
        HeightLimitView $$14 = context.world();
        ChunkRandom $$15 = context.random();
        RegistryWrapper.Impl $$16 = $$11.getOrThrow(RegistryKeys.TEMPLATE_POOL);
        BlockRotation $$17 = BlockRotation.random($$15);
        StructurePool $$18 = structurePool.getKey().flatMap(arg_0 -> StructurePoolBasedGenerator.method_55604((Registry)$$16, aliasLookup, arg_0)).orElse(structurePool.value());
        StructurePoolElement $$19 = $$18.getRandomElement($$15);
        if ($$19 == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        }
        if (id.isPresent()) {
            Identifier $$20 = id.get();
            Optional<BlockPos> $$21 = StructurePoolBasedGenerator.findStartingJigsawPos($$19, $$20, pos, $$17, $$13, $$15);
            if ($$21.isEmpty()) {
                LOGGER.error("No starting jigsaw {} found in start pool {}", (Object)$$20, (Object)structurePool.getKey().map(key -> key.getValue().toString()).orElse("<unregistered>"));
                return Optional.empty();
            }
            BlockPos $$22 = $$21.get();
        } else {
            $$23 = pos;
        }
        BlockPos $$24 = $$23.subtract(pos);
        BlockPos $$25 = pos.subtract($$24);
        PoolStructurePiece $$26 = new PoolStructurePiece($$13, $$19, $$25, $$19.getGroundLevelDelta(), $$17, $$19.getBoundingBox($$13, $$25, $$17), liquidSettings);
        BlockBox $$27 = $$26.getBoundingBox();
        int $$28 = ($$27.getMaxX() + $$27.getMinX()) / 2;
        int $$29 = ($$27.getMaxZ() + $$27.getMinZ()) / 2;
        int $$30 = projectStartToHeightmap.isEmpty() ? $$25.getY() : pos.getY() + $$12.getHeightOnGround($$28, $$29, projectStartToHeightmap.get(), $$14, context.noiseConfig());
        int $$31 = $$27.getMinY() + $$26.getGroundLevelDelta();
        $$26.translate(0, $$30 - $$31, 0);
        if (StructurePoolBasedGenerator.method_65173($$14, dimensionPadding, $$26.getBoundingBox())) {
            LOGGER.debug("Center piece {} with bounding box {} does not fit dimension padding {}", new Object[]{$$19, $$26.getBoundingBox(), dimensionPadding});
            return Optional.empty();
        }
        int $$32 = $$30 + $$24.getY();
        return Optional.of(new Structure.StructurePosition(new BlockPos($$28, $$32, $$29), collector -> StructurePoolBasedGenerator.method_39824($$26, size, $$28, maxDistanceFromCenter, $$32, $$14, dimensionPadding, $$29, $$27, context, useExpansionHack, $$12, $$13, $$15, (Registry)$$16, aliasLookup, liquidSettings, collector)));
    }

    private static boolean method_65173(HeightLimitView $$0, DimensionPadding $$1, BlockBox $$2) {
        if ($$1 == DimensionPadding.NONE) {
            return false;
        }
        int $$3 = $$0.getBottomY() + $$1.bottom();
        int $$4 = $$0.getTopYInclusive() - $$1.top();
        return $$2.getMinY() < $$3 || $$2.getMaxY() > $$4;
    }

    private static Optional<BlockPos> findStartingJigsawPos(StructurePoolElement pool, Identifier id, BlockPos pos, BlockRotation rotation, StructureTemplateManager structureManager, ChunkRandom random) {
        List<StructureTemplate.JigsawBlockInfo> $$6 = pool.getStructureBlockInfos(structureManager, pos, rotation, random);
        for (StructureTemplate.JigsawBlockInfo $$7 : $$6) {
            if (!id.equals($$7.name())) continue;
            return Optional.of($$7.info().pos());
        }
        return Optional.empty();
    }

    private static void generate(NoiseConfig noiseConfig, int maxSize, boolean modifyBoundingBox, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, HeightLimitView heightLimitView, Random random, Registry<StructurePool> structurePoolRegistry, PoolStructurePiece firstPiece, List<PoolStructurePiece> pieces, VoxelShape pieceShape, StructurePoolAliasLookup aliasLookup, StructureLiquidSettings liquidSettings) {
        StructurePoolGenerator $$13 = new StructurePoolGenerator(structurePoolRegistry, maxSize, chunkGenerator, structureTemplateManager, pieces, random);
        $$13.generatePiece(firstPiece, (MutableObject<VoxelShape>)new MutableObject((Object)pieceShape), 0, modifyBoundingBox, heightLimitView, noiseConfig, aliasLookup, liquidSettings);
        while ($$13.structurePieces.hasNext()) {
            ShapedPoolStructurePiece $$14 = (ShapedPoolStructurePiece)$$13.structurePieces.next();
            $$13.generatePiece($$14.piece, $$14.pieceShape, $$14.depth, modifyBoundingBox, heightLimitView, noiseConfig, aliasLookup, liquidSettings);
        }
    }

    public static boolean generate(ServerWorld world, RegistryEntry<StructurePool> structurePool, Identifier id, int size, BlockPos pos, boolean keepJigsaws) {
        ChunkGenerator $$6 = world.getChunkManager().getChunkGenerator();
        StructureTemplateManager $$7 = world.getStructureTemplateManager();
        StructureAccessor $$8 = world.getStructureAccessor();
        Random $$9 = world.getRandom();
        Structure.Context $$10 = new Structure.Context(world.getRegistryManager(), $$6, $$6.getBiomeSource(), world.getChunkManager().getNoiseConfig(), $$7, world.getSeed(), new ChunkPos(pos), world, biome -> true);
        Optional<Structure.StructurePosition> $$11 = StructurePoolBasedGenerator.generate($$10, structurePool, Optional.of(id), size, pos, false, Optional.empty(), 128, StructurePoolAliasLookup.EMPTY, JigsawStructure.DEFAULT_DIMENSION_PADDING, JigsawStructure.DEFAULT_LIQUID_SETTINGS);
        if ($$11.isPresent()) {
            StructurePiecesCollector $$12 = $$11.get().generate();
            for (StructurePiece $$13 : $$12.toList().pieces()) {
                if (!($$13 instanceof PoolStructurePiece)) continue;
                PoolStructurePiece $$14 = (PoolStructurePiece)$$13;
                $$14.generate((StructureWorldAccess)world, $$8, $$6, $$9, BlockBox.infinite(), pos, keepJigsaws);
            }
            return true;
        }
        return false;
    }

    private static /* synthetic */ void method_39824(PoolStructurePiece $$0, int $$1, int $$2, int $$3, int $$4, HeightLimitView $$5, DimensionPadding $$6, int $$7, BlockBox $$8, Structure.Context $$9, boolean $$10, ChunkGenerator $$11, StructureTemplateManager $$12, ChunkRandom $$13, Registry $$14, StructurePoolAliasLookup $$15, StructureLiquidSettings $$16, StructurePiecesCollector collector) {
        ArrayList $$18 = Lists.newArrayList();
        $$18.add($$0);
        if ($$1 <= 0) {
            return;
        }
        Box $$19 = new Box($$2 - $$3, Math.max($$4 - $$3, $$5.getBottomY() + $$6.bottom()), $$7 - $$3, $$2 + $$3 + 1, Math.min($$4 + $$3 + 1, $$5.getTopYInclusive() + 1 - $$6.top()), $$7 + $$3 + 1);
        VoxelShape $$20 = VoxelShapes.combineAndSimplify(VoxelShapes.cuboid($$19), VoxelShapes.cuboid(Box.from($$8)), BooleanBiFunction.ONLY_FIRST);
        StructurePoolBasedGenerator.generate($$9.noiseConfig(), $$1, $$10, $$11, $$12, $$5, $$13, $$14, $$0, $$18, $$20, $$15, $$16);
        $$18.forEach(collector::addPiece);
    }

    private static /* synthetic */ Optional method_55604(Registry $$0, StructurePoolAliasLookup $$1, RegistryKey key) {
        return $$0.getOptionalValue($$1.lookup(key));
    }

    static final class StructurePoolGenerator {
        private final Registry<StructurePool> registry;
        private final int maxSize;
        private final ChunkGenerator chunkGenerator;
        private final StructureTemplateManager structureTemplateManager;
        private final List<? super PoolStructurePiece> children;
        private final Random random;
        final PriorityIterator<ShapedPoolStructurePiece> structurePieces = new PriorityIterator();

        StructurePoolGenerator(Registry<StructurePool> registry, int maxSize, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager, List<? super PoolStructurePiece> children, Random random) {
            this.registry = registry;
            this.maxSize = maxSize;
            this.chunkGenerator = chunkGenerator;
            this.structureTemplateManager = structureTemplateManager;
            this.children = children;
            this.random = random;
        }

        void generatePiece(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int depth, boolean modifyBoundingBox, HeightLimitView world, NoiseConfig noiseConfig, StructurePoolAliasLookup aliasLookup, StructureLiquidSettings liquidSettings) {
            StructurePoolElement $$8 = piece.getPoolElement();
            BlockPos $$9 = piece.getPos();
            BlockRotation $$10 = piece.getRotation();
            StructurePool.Projection $$11 = $$8.getProjection();
            boolean $$12 = $$11 == StructurePool.Projection.RIGID;
            MutableObject $$13 = new MutableObject();
            BlockBox $$14 = piece.getBoundingBox();
            int $$15 = $$14.getMinY();
            block0: for (StructureTemplate.JigsawBlockInfo $$16 : $$8.getStructureBlockInfos(this.structureTemplateManager, $$9, $$10, this.random)) {
                StructurePoolElement $$32;
                MutableObject<VoxelShape> $$29;
                StructureTemplate.StructureBlockInfo $$17 = $$16.info();
                Direction $$18 = JigsawBlock.getFacing($$17.state());
                BlockPos $$19 = $$17.pos();
                Vec3i $$20 = $$19.offset($$18);
                int $$21 = $$19.getY() - $$15;
                int $$22 = Integer.MIN_VALUE;
                RegistryKey<StructurePool> $$23 = aliasLookup.lookup($$16.pool());
                Optional $$24 = this.registry.getOptional($$23);
                if ($$24.isEmpty()) {
                    LOGGER.warn("Empty or non-existent pool: {}", (Object)$$23.getValue());
                    continue;
                }
                RegistryEntry $$25 = (RegistryEntry)$$24.get();
                if (((StructurePool)$$25.value()).getElementCount() == 0 && !$$25.matchesKey(StructurePools.EMPTY)) {
                    LOGGER.warn("Empty or non-existent pool: {}", (Object)$$23.getValue());
                    continue;
                }
                RegistryEntry<StructurePool> $$26 = ((StructurePool)$$25.value()).getFallback();
                if ($$26.value().getElementCount() == 0 && !$$26.matchesKey(StructurePools.EMPTY)) {
                    LOGGER.warn("Empty or non-existent fallback pool: {}", (Object)$$26.getKey().map(key -> key.getValue().toString()).orElse("<unregistered>"));
                    continue;
                }
                boolean $$27 = $$14.contains($$20);
                if ($$27) {
                    MutableObject $$28 = $$13;
                    if ($$13.getValue() == null) {
                        $$13.setValue((Object)VoxelShapes.cuboid(Box.from($$14)));
                    }
                } else {
                    $$29 = pieceShape;
                }
                ArrayList $$30 = Lists.newArrayList();
                if (depth != this.maxSize) {
                    $$30.addAll(((StructurePool)$$25.value()).getElementIndicesInRandomOrder(this.random));
                }
                $$30.addAll($$26.value().getElementIndicesInRandomOrder(this.random));
                int $$31 = $$16.placementPriority();
                Iterator iterator = $$30.iterator();
                while (iterator.hasNext() && ($$32 = (StructurePoolElement)iterator.next()) != EmptyPoolElement.INSTANCE) {
                    for (BlockRotation $$33 : BlockRotation.randomRotationOrder(this.random)) {
                        int $$37;
                        List<StructureTemplate.JigsawBlockInfo> $$34 = $$32.getStructureBlockInfos(this.structureTemplateManager, BlockPos.ORIGIN, $$33, this.random);
                        BlockBox $$35 = $$32.getBoundingBox(this.structureTemplateManager, BlockPos.ORIGIN, $$33);
                        if (!modifyBoundingBox || $$35.getBlockCountY() > 16) {
                            boolean $$36 = false;
                        } else {
                            $$37 = $$34.stream().mapToInt(jigsawInfo -> {
                                StructureTemplate.StructureBlockInfo $$3 = jigsawInfo.info();
                                if (!$$35.contains($$3.pos().offset(JigsawBlock.getFacing($$3.state())))) {
                                    return 0;
                                }
                                RegistryKey<StructurePool> $$4 = aliasLookup.lookup(jigsawInfo.pool());
                                Optional $$5 = this.registry.getOptional($$4);
                                Optional<RegistryEntry> $$6 = $$5.map(entry -> ((StructurePool)entry.value()).getFallback());
                                int $$7 = $$5.map(entry -> ((StructurePool)entry.value()).getHighestY(this.structureTemplateManager)).orElse(0);
                                int $$8 = $$6.map(entry -> ((StructurePool)entry.value()).getHighestY(this.structureTemplateManager)).orElse(0);
                                return Math.max($$7, $$8);
                            }).max().orElse(0);
                        }
                        for (StructureTemplate.JigsawBlockInfo $$38 : $$34) {
                            int $$59;
                            int $$55;
                            int $$48;
                            if (!JigsawBlock.attachmentMatches($$16, $$38)) continue;
                            BlockPos $$39 = $$38.info().pos();
                            BlockPos $$40 = ((BlockPos)$$20).subtract($$39);
                            BlockBox $$41 = $$32.getBoundingBox(this.structureTemplateManager, $$40, $$33);
                            int $$42 = $$41.getMinY();
                            StructurePool.Projection $$43 = $$32.getProjection();
                            boolean $$44 = $$43 == StructurePool.Projection.RIGID;
                            int $$45 = $$39.getY();
                            int $$46 = $$21 - $$45 + JigsawBlock.getFacing($$17.state()).getOffsetY();
                            if ($$12 && $$44) {
                                int $$47 = $$15 + $$46;
                            } else {
                                if ($$22 == Integer.MIN_VALUE) {
                                    $$22 = this.chunkGenerator.getHeightOnGround($$19.getX(), $$19.getZ(), Heightmap.Type.WORLD_SURFACE_WG, world, noiseConfig);
                                }
                                $$48 = $$22 - $$45;
                            }
                            int $$49 = $$48 - $$42;
                            BlockBox $$50 = $$41.offset(0, $$49, 0);
                            BlockPos $$51 = $$40.add(0, $$49, 0);
                            if ($$37 > 0) {
                                int $$52 = Math.max($$37 + 1, $$50.getMaxY() - $$50.getMinY());
                                $$50.encompass(new BlockPos($$50.getMinX(), $$50.getMinY() + $$52, $$50.getMinZ()));
                            }
                            if (VoxelShapes.matchesAnywhere((VoxelShape)$$29.getValue(), VoxelShapes.cuboid(Box.from($$50).contract(0.25)), BooleanBiFunction.ONLY_SECOND)) continue;
                            $$29.setValue((Object)VoxelShapes.combine((VoxelShape)$$29.getValue(), VoxelShapes.cuboid(Box.from($$50)), BooleanBiFunction.ONLY_FIRST));
                            int $$53 = piece.getGroundLevelDelta();
                            if ($$44) {
                                int $$54 = $$53 - $$46;
                            } else {
                                $$55 = $$32.getGroundLevelDelta();
                            }
                            PoolStructurePiece $$56 = new PoolStructurePiece(this.structureTemplateManager, $$32, $$51, $$55, $$33, $$50, liquidSettings);
                            if ($$12) {
                                int $$57 = $$15 + $$21;
                            } else if ($$44) {
                                int $$58 = $$48 + $$45;
                            } else {
                                if ($$22 == Integer.MIN_VALUE) {
                                    $$22 = this.chunkGenerator.getHeightOnGround($$19.getX(), $$19.getZ(), Heightmap.Type.WORLD_SURFACE_WG, world, noiseConfig);
                                }
                                $$59 = $$22 + $$46 / 2;
                            }
                            piece.addJunction(new JigsawJunction($$20.getX(), (int)($$59 - $$21 + $$53), $$20.getZ(), $$46, $$43));
                            $$56.addJunction(new JigsawJunction($$19.getX(), $$59 - $$45 + $$55, $$19.getZ(), -$$46, $$11));
                            this.children.add($$56);
                            if (depth + 1 > this.maxSize) continue block0;
                            ShapedPoolStructurePiece $$60 = new ShapedPoolStructurePiece($$56, $$29, depth + 1);
                            this.structurePieces.enqueue($$60, $$31);
                            continue block0;
                        }
                    }
                }
            }
        }
    }

    static final class ShapedPoolStructurePiece
    extends Record {
        final PoolStructurePiece piece;
        final MutableObject<VoxelShape> pieceShape;
        final int depth;

        ShapedPoolStructurePiece(PoolStructurePiece piece, MutableObject<VoxelShape> pieceShape, int currentSize) {
            this.piece = piece;
            this.pieceShape = pieceShape;
            this.depth = currentSize;
        }

        @Override
        public final String toString() {
            return ObjectMethods.bootstrap("toString", new MethodHandle[]{ShapedPoolStructurePiece.class, "piece;free;depth", "piece", "pieceShape", "depth"}, this);
        }

        @Override
        public final int hashCode() {
            return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{ShapedPoolStructurePiece.class, "piece;free;depth", "piece", "pieceShape", "depth"}, this);
        }

        @Override
        public final boolean equals(Object $$0) {
            return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{ShapedPoolStructurePiece.class, "piece;free;depth", "piece", "pieceShape", "depth"}, this, $$0);
        }

        public PoolStructurePiece piece() {
            return this.piece;
        }

        public MutableObject<VoxelShape> pieceShape() {
            return this.pieceShape;
        }

        public int depth() {
            return this.depth;
        }
    }
}

