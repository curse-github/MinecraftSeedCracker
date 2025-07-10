/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  com.google.common.base.Suppliers
 *  com.google.common.collect.Sets
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.mutable.MutableObject
 */
package net.minecraft.world.gen.chunk;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.BiomeSupplier;
import net.minecraft.world.chunk.BelowZeroRetrogen;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.apache.commons.lang3.mutable.MutableObject;

public final class NoiseChunkGenerator
extends ChunkGenerator {
    public static final MapCodec<NoiseChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group((App)BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource), (App)ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(generator -> generator.settings)).apply((Applicative)instance, instance.stable(NoiseChunkGenerator::new)));
    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private final RegistryEntry<ChunkGeneratorSettings> settings;
    private final Supplier<AquiferSampler.FluidLevelSampler> fluidLevelSampler;

    public NoiseChunkGenerator(BiomeSource biomeSource, RegistryEntry<ChunkGeneratorSettings> settings) {
        super(biomeSource);
        this.settings = settings;
        this.fluidLevelSampler = Suppliers.memoize(() -> NoiseChunkGenerator.createFluidLevelSampler((ChunkGeneratorSettings)settings.value()));
    }

    private static AquiferSampler.FluidLevelSampler createFluidLevelSampler(ChunkGeneratorSettings settings) {
        AquiferSampler.FluidLevel $$1 = new AquiferSampler.FluidLevel(-54, Blocks.LAVA.getDefaultState());
        int $$2 = settings.seaLevel();
        AquiferSampler.FluidLevel $$3 = new AquiferSampler.FluidLevel($$2, settings.defaultFluid());
        AquiferSampler.FluidLevel $$4 = new AquiferSampler.FluidLevel(DimensionType.MIN_HEIGHT * 2, Blocks.AIR.getDefaultState());
        return (x, y, z) -> {
            if (y < Math.min(-54, $$2)) {
                return $$1;
            }
            return $$3;
        };
    }

    @Override
    public CompletableFuture<Chunk> populateBiomes(NoiseConfig noiseConfig, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.supplyAsync(() -> {
            this.populateBiomes(blender, noiseConfig, structureAccessor, chunk);
            return chunk;
        }, Util.getMainWorkerExecutor().named("init_biomes"));
    }

    private void populateBiomes(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        ChunkNoiseSampler $$4 = chunk.getOrCreateChunkNoiseSampler(chunkx -> this.createChunkNoiseSampler((Chunk)chunkx, structureAccessor, blender, noiseConfig));
        BiomeSupplier $$5 = BelowZeroRetrogen.getBiomeSupplier(blender.getBiomeSupplier(this.biomeSource), chunk);
        chunk.populateBiomes($$5, $$4.createMultiNoiseSampler(noiseConfig.getNoiseRouter(), this.settings.value().spawnTarget()));
    }

    private ChunkNoiseSampler createChunkNoiseSampler(Chunk chunk, StructureAccessor world, Blender blender, NoiseConfig noiseConfig) {
        return ChunkNoiseSampler.create(chunk, noiseConfig, StructureWeightSampler.createStructureWeightSampler(world, chunk.getPos()), this.settings.value(), this.fluidLevelSampler.get(), blender);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    public RegistryEntry<ChunkGeneratorSettings> getSettings() {
        return this.settings;
    }

    public boolean matchesSettings(RegistryKey<ChunkGeneratorSettings> settings) {
        return this.settings.matchesKey(settings);
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return this.sampleHeightmap(world, noiseConfig, x, z, null, heightmap.getBlockPredicate()).orElse(world.getBottomY());
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        MutableObject $$4 = new MutableObject();
        this.sampleHeightmap(world, noiseConfig, x, z, (MutableObject<VerticalBlockSample>)$$4, null);
        return (VerticalBlockSample)$$4.getValue();
    }

    @Override
    public void appendDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
        DecimalFormat $$3 = new DecimalFormat("0.000");
        NoiseRouter $$4 = noiseConfig.getNoiseRouter();
        DensityFunction.UnblendedNoisePos $$5 = new DensityFunction.UnblendedNoisePos(pos.getX(), pos.getY(), pos.getZ());
        double $$6 = $$4.ridges().sample($$5);
        text.add("NoiseRouter T: " + $$3.format($$4.temperature().sample($$5)) + " V: " + $$3.format($$4.vegetation().sample($$5)) + " C: " + $$3.format($$4.continents().sample($$5)) + " E: " + $$3.format($$4.erosion().sample($$5)) + " D: " + $$3.format($$4.depth().sample($$5)) + " W: " + $$3.format($$6) + " PV: " + $$3.format(DensityFunctions.getPeaksValleysNoise((float)$$6)) + " AS: " + $$3.format($$4.initialDensityWithoutJaggedness().sample($$5)) + " N: " + $$3.format($$4.finalDensity().sample($$5)));
    }

    private OptionalInt sampleHeightmap(HeightLimitView world, NoiseConfig noiseConfig, int x, int z, @Nullable MutableObject<VerticalBlockSample> columnSample, @Nullable Predicate<BlockState> stopPredicate) {
        BlockState[] $$12;
        GenerationShapeConfig $$6 = this.settings.value().generationShapeConfig().trimHeight(world);
        int $$7 = $$6.verticalCellBlockCount();
        int $$8 = $$6.minimumY();
        int $$9 = MathHelper.floorDiv($$8, $$7);
        int $$10 = MathHelper.floorDiv($$6.height(), $$7);
        if ($$10 <= 0) {
            return OptionalInt.empty();
        }
        if (columnSample == null) {
            Object $$11 = null;
        } else {
            $$12 = new BlockState[$$6.height()];
            columnSample.setValue((Object)new VerticalBlockSample($$8, $$12));
        }
        int $$13 = $$6.horizontalCellBlockCount();
        int $$14 = Math.floorDiv(x, $$13);
        int $$15 = Math.floorDiv(z, $$13);
        int $$16 = Math.floorMod(x, $$13);
        int $$17 = Math.floorMod(z, $$13);
        int $$18 = $$14 * $$13;
        int $$19 = $$15 * $$13;
        double $$20 = (double)$$16 / (double)$$13;
        double $$21 = (double)$$17 / (double)$$13;
        ChunkNoiseSampler $$22 = new ChunkNoiseSampler(1, noiseConfig, $$18, $$19, $$6, DensityFunctionTypes.Beardifier.INSTANCE, this.settings.value(), this.fluidLevelSampler.get(), Blender.getNoBlending());
        $$22.sampleStartDensity();
        $$22.sampleEndDensity(0);
        for (int $$23 = $$10 - 1; $$23 >= 0; --$$23) {
            $$22.onSampledCellCorners($$23, 0);
            for (int $$24 = $$7 - 1; $$24 >= 0; --$$24) {
                BlockState $$28;
                int $$25 = ($$9 + $$23) * $$7 + $$24;
                double $$26 = (double)$$24 / (double)$$7;
                $$22.interpolateY($$25, $$26);
                $$22.interpolateX(x, $$20);
                $$22.interpolateZ(z, $$21);
                BlockState $$27 = $$22.sampleBlockState();
                BlockState blockState = $$28 = $$27 == null ? this.settings.value().defaultBlock() : $$27;
                if ($$12 != null) {
                    int $$29 = $$23 * $$7 + $$24;
                    $$12[$$29] = $$28;
                }
                if (stopPredicate == null || !stopPredicate.test($$28)) continue;
                $$22.stopInterpolation();
                return OptionalInt.of($$25 + 1);
            }
        }
        $$22.stopInterpolation();
        return OptionalInt.empty();
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        if (SharedConstants.isOutsideGenerationArea(chunk.getPos())) {
            return;
        }
        HeightContext $$4 = new HeightContext(this, region);
        this.buildSurface(chunk, $$4, noiseConfig, structures, region.getBiomeAccess(), (Registry<Biome>)region.getRegistryManager().getOrThrow(RegistryKeys.BIOME), Blender.getBlender(region));
    }

    @VisibleForTesting
    public void buildSurface(Chunk chunk, HeightContext heightContext, NoiseConfig noiseConfig, StructureAccessor structureAccessor, BiomeAccess biomeAccess, Registry<Biome> biomeRegistry, Blender blender) {
        ChunkNoiseSampler $$7 = chunk.getOrCreateChunkNoiseSampler(chunkx -> this.createChunkNoiseSampler((Chunk)chunkx, structureAccessor, blender, noiseConfig));
        ChunkGeneratorSettings $$8 = this.settings.value();
        noiseConfig.getSurfaceBuilder().buildSurface(noiseConfig, biomeAccess, biomeRegistry, $$8.usesLegacyRandom(), heightContext, chunk, $$7, $$8.surfaceRule());
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk) {
        BiomeAccess $$6 = biomeAccess.withSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getBiome(biomeX, biomeY, biomeZ, noiseConfig.getMultiNoiseSampler()));
        ChunkRandom $$7 = new ChunkRandom(new CheckedRandom(RandomSeed.getSeed()));
        int $$8 = 8;
        ChunkPos $$9 = chunk.getPos();
        ChunkNoiseSampler $$10 = chunk.getOrCreateChunkNoiseSampler(chunkx -> this.createChunkNoiseSampler((Chunk)chunkx, structureAccessor, Blender.getBlender(chunkRegion), noiseConfig));
        AquiferSampler $$11 = $$10.getAquiferSampler();
        CarverContext $$12 = new CarverContext(this, chunkRegion.getRegistryManager(), chunk.getHeightLimitView(), $$10, noiseConfig, this.settings.value().surfaceRule());
        CarvingMask $$13 = ((ProtoChunk)chunk).getOrCreateCarvingMask();
        for (int $$14 = -8; $$14 <= 8; ++$$14) {
            for (int $$15 = -8; $$15 <= 8; ++$$15) {
                ChunkPos $$16 = new ChunkPos($$9.x + $$14, $$9.z + $$15);
                Chunk $$17 = chunkRegion.getChunk($$16.x, $$16.z);
                GenerationSettings $$18 = $$17.getOrCreateGenerationSettings(() -> this.getGenerationSettings(this.biomeSource.getBiome(BiomeCoords.fromBlock($$16.getStartX()), 0, BiomeCoords.fromBlock($$16.getStartZ()), noiseConfig.getMultiNoiseSampler())));
                Iterable<RegistryEntry<ConfiguredCarver<?>>> $$19 = $$18.getCarversForStep();
                int $$20 = 0;
                for (RegistryEntry<ConfiguredCarver<?>> $$21 : $$19) {
                    ConfiguredCarver<?> $$22 = $$21.value();
                    $$7.setCarverSeed(seed + (long)$$20, $$16.x, $$16.z);
                    if ($$22.shouldCarve($$7)) {
                        $$22.carve($$12, chunk, $$6::getBiome, $$7, $$11, $$16, $$13);
                    }
                    ++$$20;
                }
            }
        }
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        GenerationShapeConfig $$4 = this.settings.value().generationShapeConfig().trimHeight(chunk.getHeightLimitView());
        int $$5 = $$4.minimumY();
        int $$6 = MathHelper.floorDiv($$5, $$4.verticalCellBlockCount());
        int $$7 = MathHelper.floorDiv($$4.height(), $$4.verticalCellBlockCount());
        if ($$7 <= 0) {
            return CompletableFuture.completedFuture(chunk);
        }
        return CompletableFuture.supplyAsync(() -> {
            int $$8 = chunk.getSectionIndex($$7 * $$4.verticalCellBlockCount() - 1 + $$5);
            int $$9 = chunk.getSectionIndex($$5);
            HashSet $$10 = Sets.newHashSet();
            for (int $$11 = $$8; $$11 >= $$9; --$$11) {
                ChunkSection $$12 = chunk.getSection($$11);
                $$12.lock();
                $$10.add($$12);
            }
            try {
                Chunk chunk = this.populateNoise(blender, structureAccessor, noiseConfig, chunk, $$6, $$7);
                return chunk;
            }
            finally {
                for (ChunkSection $$13 : $$10) {
                    $$13.unlock();
                }
            }
        }, Util.getMainWorkerExecutor().named("wgen_fill_noise"));
    }

    private Chunk populateNoise(Blender blender, StructureAccessor structureAccessor, NoiseConfig noiseConfig, Chunk chunk, int minimumCellY, int cellHeight) {
        ChunkNoiseSampler $$6 = chunk.getOrCreateChunkNoiseSampler(chunkx -> this.createChunkNoiseSampler((Chunk)chunkx, structureAccessor, blender, noiseConfig));
        Heightmap $$7 = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap $$8 = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        ChunkPos $$9 = chunk.getPos();
        int $$10 = $$9.getStartX();
        int $$11 = $$9.getStartZ();
        AquiferSampler $$12 = $$6.getAquiferSampler();
        $$6.sampleStartDensity();
        BlockPos.Mutable $$13 = new BlockPos.Mutable();
        int $$14 = $$6.getHorizontalCellBlockCount();
        int $$15 = $$6.getVerticalCellBlockCount();
        int $$16 = 16 / $$14;
        int $$17 = 16 / $$14;
        for (int $$18 = 0; $$18 < $$16; ++$$18) {
            $$6.sampleEndDensity($$18);
            for (int $$19 = 0; $$19 < $$17; ++$$19) {
                int $$20 = chunk.countVerticalSections() - 1;
                ChunkSection $$21 = chunk.getSection($$20);
                for (int $$22 = cellHeight - 1; $$22 >= 0; --$$22) {
                    $$6.onSampledCellCorners($$22, $$19);
                    for (int $$23 = $$15 - 1; $$23 >= 0; --$$23) {
                        int $$24 = (minimumCellY + $$22) * $$15 + $$23;
                        int $$25 = $$24 & 0xF;
                        int $$26 = chunk.getSectionIndex($$24);
                        if ($$20 != $$26) {
                            $$20 = $$26;
                            $$21 = chunk.getSection($$26);
                        }
                        double $$27 = (double)$$23 / (double)$$15;
                        $$6.interpolateY($$24, $$27);
                        for (int $$28 = 0; $$28 < $$14; ++$$28) {
                            int $$29 = $$10 + $$18 * $$14 + $$28;
                            int $$30 = $$29 & 0xF;
                            double $$31 = (double)$$28 / (double)$$14;
                            $$6.interpolateX($$29, $$31);
                            for (int $$32 = 0; $$32 < $$14; ++$$32) {
                                int $$33 = $$11 + $$19 * $$14 + $$32;
                                int $$34 = $$33 & 0xF;
                                double $$35 = (double)$$32 / (double)$$14;
                                $$6.interpolateZ($$33, $$35);
                                BlockState $$36 = $$6.sampleBlockState();
                                if ($$36 == null) {
                                    $$36 = this.settings.value().defaultBlock();
                                }
                                if (($$36 = this.getBlockState($$6, $$29, $$24, $$33, $$36)) == AIR || SharedConstants.isOutsideGenerationArea(chunk.getPos())) continue;
                                $$21.setBlockState($$30, $$25, $$34, $$36, false);
                                $$7.trackUpdate($$30, $$24, $$34, $$36);
                                $$8.trackUpdate($$30, $$24, $$34, $$36);
                                if (!$$12.needsFluidTick() || $$36.getFluidState().isEmpty()) continue;
                                $$13.set($$29, $$24, $$33);
                                chunk.markBlockForPostProcessing($$13);
                            }
                        }
                    }
                }
            }
            $$6.swapBuffers();
        }
        $$6.stopInterpolation();
        return chunk;
    }

    private BlockState getBlockState(ChunkNoiseSampler chunkNoiseSampler, int x, int y, int z, BlockState state) {
        return state;
    }

    @Override
    public int getWorldHeight() {
        return this.settings.value().generationShapeConfig().height();
    }

    @Override
    public int getSeaLevel() {
        return this.settings.value().seaLevel();
    }

    @Override
    public int getMinimumY() {
        return this.settings.value().generationShapeConfig().minimumY();
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        if (this.settings.value().mobGenerationDisabled()) {
            return;
        }
        ChunkPos $$1 = region.getCenterPos();
        RegistryEntry<Biome> $$2 = region.getBiome($$1.getStartPos().withY(region.getTopYInclusive()));
        ChunkRandom $$3 = new ChunkRandom(new CheckedRandom(RandomSeed.getSeed()));
        $$3.setPopulationSeed(region.getSeed(), $$1.getStartX(), $$1.getStartZ());
        SpawnHelper.populateEntities(region, $$2, $$1, $$3);
    }
}

