/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Stopwatch
 *  com.google.common.base.Ticker
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.logging.LogUtils
 *  it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  javax.annotation.Nullable
 *  org.slf4j.Logger
 */
package net.minecraft.world.gen.chunk.placement;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structure;
import org.slf4j.Logger;

public class StructurePlacementCalculator {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final NoiseConfig noiseConfig;
    private final BiomeSource biomeSource;
    private final long structureSeed;
    private final long concentricRingSeed;
    private final Map<Structure, List<StructurePlacement>> structuresToPlacements = new Object2ObjectOpenHashMap();
    private final Map<ConcentricRingsStructurePlacement, CompletableFuture<List<ChunkPos>>> concentricPlacementsToPositions = new Object2ObjectArrayMap();
    private boolean calculated;
    private final List<RegistryEntry<StructureSet>> structureSets;

    public static StructurePlacementCalculator create(NoiseConfig noiseConfig, long seed, BiomeSource biomeSource, Stream<RegistryEntry<StructureSet>> structureSets) {
        List<RegistryEntry<StructureSet>> $$4 = structureSets.filter(structureSet -> StructurePlacementCalculator.hasValidBiome((StructureSet)structureSet.value(), biomeSource)).toList();
        return new StructurePlacementCalculator(noiseConfig, biomeSource, seed, 0L, $$4);
    }

    public static StructurePlacementCalculator create(NoiseConfig noiseConfig, long seed, BiomeSource biomeSource, RegistryWrapper<StructureSet> structureSetRegistry) {
        List<RegistryEntry<StructureSet>> $$4 = structureSetRegistry.streamEntries().filter(structureSet -> StructurePlacementCalculator.hasValidBiome((StructureSet)structureSet.value(), biomeSource)).collect(Collectors.toUnmodifiableList());
        return new StructurePlacementCalculator(noiseConfig, biomeSource, seed, seed, $$4);
    }

    private static boolean hasValidBiome(StructureSet structureSet, BiomeSource biomeSource) {
        Stream $$2 = structureSet.structures().stream().flatMap(structure -> {
            Structure $$1 = structure.structure().value();
            return $$1.getValidBiomes().stream();
        });
        return $$2.anyMatch(biomeSource.getBiomes()::contains);
    }

    private StructurePlacementCalculator(NoiseConfig noiseConfig, BiomeSource biomeSource, long structureSeed, long concentricRingSeed, List<RegistryEntry<StructureSet>> structureSets) {
        this.noiseConfig = noiseConfig;
        this.structureSeed = structureSeed;
        this.biomeSource = biomeSource;
        this.concentricRingSeed = concentricRingSeed;
        this.structureSets = structureSets;
    }

    public List<RegistryEntry<StructureSet>> getStructureSets() {
        return this.structureSets;
    }

    private void calculate() {
        Set<RegistryEntry<Biome>> $$0 = this.biomeSource.getBiomes();
        this.getStructureSets().forEach(structureSet -> {
            StructurePlacement $$6;
            StructureSet $$2 = (StructureSet)structureSet.value();
            boolean $$3 = false;
            for (StructureSet.WeightedEntry $$4 : $$2.structures()) {
                Structure $$5 = $$4.structure().value();
                if (!$$5.getValidBiomes().stream().anyMatch($$0::contains)) continue;
                this.structuresToPlacements.computeIfAbsent($$5, structure -> new ArrayList()).add($$2.placement());
                $$3 = true;
            }
            if ($$3 && ($$6 = $$2.placement()) instanceof ConcentricRingsStructurePlacement) {
                ConcentricRingsStructurePlacement $$7 = (ConcentricRingsStructurePlacement)$$6;
                this.concentricPlacementsToPositions.put($$7, this.calculateConcentricsRingPlacementPos((RegistryEntry<StructureSet>)structureSet, $$7));
            }
        });
    }

    private CompletableFuture<List<ChunkPos>> calculateConcentricsRingPlacementPos(RegistryEntry<StructureSet> structureSetEntry, ConcentricRingsStructurePlacement placement) {
        if (placement.getCount() == 0) {
            return CompletableFuture.completedFuture(List.of());
        }
        Stopwatch $$2 = Stopwatch.createStarted((Ticker)Util.TICKER);
        int $$3 = placement.getDistance();
        int $$4 = placement.getCount();
        ArrayList<CompletableFuture<ChunkPos>> $$5 = new ArrayList<CompletableFuture<ChunkPos>>($$4);
        int $$6 = placement.getSpread();
        RegistryEntryList<Biome> $$7 = placement.getPreferredBiomes();
        Random rand = Random.create();
        rand.setSeed(this.concentricRingSeed);
        double $$9 = rand.nextDouble() * Math.PI * 2.0;
        int $$10 = 0;
        int $$11 = 0;
        for (int $$12 = 0; $$12 < $$4; ++$$12) {
            double $$13 = (double)(4 * $$3 + $$3 * $$11 * 6) + (rand.nextDouble() - 0.5) * ((double)$$3 * 2.5);
            int $$14 = (int)Math.round(Math.cos($$9) * $$13);
            int $$15 = (int)Math.round(Math.sin($$9) * $$13);
            Random $$16 = rand.split();
            $$5.add(CompletableFuture.supplyAsync(() -> {
                Pair<BlockPos, RegistryEntry<Biome>> $$4 = this.biomeSource.locateBiome(ChunkSectionPos.getOffsetPos($$14, 8), 0, ChunkSectionPos.getOffsetPos($$15, 8), 112, $$7::contains, $$16, this.noiseConfig.getMultiNoiseSampler());
                if ($$4 != null) {
                    BlockPos $$5 = (BlockPos)$$4.getFirst();
                    return new ChunkPos(ChunkSectionPos.getSectionCoord($$5.getX()), ChunkSectionPos.getSectionCoord($$5.getZ()));
                }
                return new ChunkPos($$14, $$15);
            }, Util.getMainWorkerExecutor().named("structureRings")));
            $$9 += Math.PI * 2 / (double)$$6;
            if (++$$10 != $$6) continue;
            $$10 = 0;
            $$6 += 2 * $$6 / (++$$11 + 1);
            $$6 = Math.min($$6, $$4 - $$12);
            $$9 += rand.nextDouble() * Math.PI * 2.0;
        }
        return Util.combineSafe($$5).thenApply(positions -> {
            double $$3 = (double)$$2.stop().elapsed(TimeUnit.MILLISECONDS) / 1000.0;
            LOGGER.debug("Calculation for {} took {}s", (Object)structureSetEntry, (Object)$$3);
            return positions;
        });
    }

    public void tryCalculate() {
        if (!this.calculated) {
            this.calculate();
            this.calculated = true;
        }
    }

    @Nullable
    public List<ChunkPos> getPlacementPositions(ConcentricRingsStructurePlacement placement) {
        this.tryCalculate();
        CompletableFuture<List<ChunkPos>> $$1 = this.concentricPlacementsToPositions.get(placement);
        return $$1 != null ? $$1.join() : null;
    }

    public List<StructurePlacement> getPlacements(RegistryEntry<Structure> structureEntry) {
        this.tryCalculate();
        return this.structuresToPlacements.getOrDefault(structureEntry.value(), List.of());
    }

    public NoiseConfig getNoiseConfig() {
        return this.noiseConfig;
    }

    public boolean canGenerate(RegistryEntry<StructureSet> structureSetEntry, int centerChunkX, int centerChunkZ, int chunkCount) {
        StructurePlacement $$4 = structureSetEntry.value().placement();
        for (int $$5 = centerChunkX - chunkCount; $$5 <= centerChunkX + chunkCount; ++$$5) {
            for (int $$6 = centerChunkZ - chunkCount; $$6 <= centerChunkZ + chunkCount; ++$$6) {
                if (!$$4.shouldGenerate(this, $$5, $$6)) continue;
                return true;
            }
        }
        return false;
    }

    public long getStructureSeed() {
        return this.structureSeed;
    }
}

