/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  it.unimi.dsi.fastutil.ints.IntArraySet
 *  it.unimi.dsi.fastutil.ints.IntSet
 *  it.unimi.dsi.fastutil.longs.LongSet
 *  it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
 *  it.unimi.dsi.fastutil.objects.ObjectArraySet
 *  javax.annotation.Nullable
 *  org.apache.commons.lang3.mutable.MutableBoolean
 */
package net.minecraft.world.gen.chunk;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructurePresence;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.structure.Structure;
import org.apache.commons.lang3.mutable.MutableBoolean;

public abstract class ChunkGenerator {
    public static final Codec<ChunkGenerator> CODEC = Registries.CHUNK_GENERATOR.getCodec().dispatchStable(ChunkGenerator::getCodec, Function.identity());
    protected final BiomeSource biomeSource;
    private final Supplier<List<PlacedFeatureIndexer.IndexedFeatures>> indexedFeaturesListSupplier;
    private final Function<RegistryEntry<Biome>, GenerationSettings> generationSettingsGetter;

    public ChunkGenerator(BiomeSource biomeSource) {
        this(biomeSource, biomeEntry -> ((Biome)biomeEntry.value()).getGenerationSettings());
    }

    public ChunkGenerator(BiomeSource biomeSource, Function<RegistryEntry<Biome>, GenerationSettings> generationSettingsGetter) {
        this.biomeSource = biomeSource;
        this.generationSettingsGetter = generationSettingsGetter;
        this.indexedFeaturesListSupplier = Suppliers.memoize(() -> PlacedFeatureIndexer.collectIndexedFeatures(List.copyOf(biomeSource.getBiomes()), biomeEntry -> ((GenerationSettings)generationSettingsGetter.apply((RegistryEntry<Biome>)biomeEntry)).getFeatures(), true));
    }

    public void initializeIndexedFeaturesList() {
        this.indexedFeaturesListSupplier.get();
    }

    protected abstract MapCodec<? extends ChunkGenerator> getCodec();

    public StructurePlacementCalculator createStructurePlacementCalculator(RegistryWrapper<StructureSet> structureSetRegistry, NoiseConfig noiseConfig, long seed) {
        return StructurePlacementCalculator.create(noiseConfig, seed, this.biomeSource, structureSetRegistry);
    }

    public Optional<RegistryKey<MapCodec<? extends ChunkGenerator>>> getCodecKey() {
        return Registries.CHUNK_GENERATOR.getKey(this.getCodec());
    }

    public CompletableFuture<Chunk> populateBiomes(NoiseConfig noiseConfig, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.supplyAsync(() -> {
            chunk.populateBiomes(this.biomeSource, noiseConfig.getMultiNoiseSampler());
            return chunk;
        }, Util.getMainWorkerExecutor().named("init_biomes"));
    }

    public abstract void carve(ChunkRegion var1, long var2, NoiseConfig var4, BiomeAccess var5, StructureAccessor var6, Chunk var7);

    @Nullable
    public Pair<BlockPos, RegistryEntry<Structure>> locateStructure(ServerWorld world, RegistryEntryList<Structure> structures, BlockPos center, int radius, boolean skipReferencedStructures) {
        StructurePlacementCalculator $$5 = world.getChunkManager().getStructurePlacementCalculator();
        Object2ObjectArrayMap $$6 = new Object2ObjectArrayMap();
        for (RegistryEntry registryEntry : structures) {
            for (StructurePlacement $$8 : $$5.getPlacements(registryEntry)) {
                $$6.computeIfAbsent($$8, placement -> new ObjectArraySet()).add(registryEntry);
            }
        }
        if ($$6.isEmpty()) {
            return null;
        }
        Pair<BlockPos, RegistryEntry<Structure>> $$9 = null;
        double d2 = Double.MAX_VALUE;
        StructureAccessor $$11 = world.getStructureAccessor();
        ArrayList $$12 = new ArrayList($$6.size());
        for (Map.Entry $$13 : $$6.entrySet()) {
            StructurePlacement $$14 = (StructurePlacement)$$13.getKey();
            if ($$14 instanceof ConcentricRingsStructurePlacement) {
                BlockPos blockPos;
                double $$18;
                ConcentricRingsStructurePlacement $$15 = (ConcentricRingsStructurePlacement)$$14;
                Pair<BlockPos, RegistryEntry<Structure>> $$16 = this.locateConcentricRingsStructure((Set)$$13.getValue(), world, $$11, center, skipReferencedStructures, $$15);
                if ($$16 == null || !(($$18 = center.getSquaredDistance(blockPos = (BlockPos)$$16.getFirst())) < d2)) continue;
                d2 = $$18;
                $$9 = $$16;
                continue;
            }
            if (!($$14 instanceof RandomSpreadStructurePlacement)) continue;
            $$12.add($$13);
        }
        if (!$$12.isEmpty()) {
            int $$19 = ChunkSectionPos.getSectionCoord(center.getX());
            int $$20 = ChunkSectionPos.getSectionCoord(center.getZ());
            for (int $$21 = 0; $$21 <= radius; ++$$21) {
                boolean $$22 = false;
                for (Map.Entry entry : $$12) {
                    RandomSpreadStructurePlacement $$24 = (RandomSpreadStructurePlacement)entry.getKey();
                    Pair<BlockPos, RegistryEntry<Structure>> $$25 = ChunkGenerator.locateRandomSpreadStructure((Set)entry.getValue(), world, $$11, $$19, $$20, $$21, skipReferencedStructures, $$5.getStructureSeed(), $$24);
                    if ($$25 == null) continue;
                    $$22 = true;
                    double $$26 = center.getSquaredDistance((Vec3i)$$25.getFirst());
                    if (!($$26 < d2)) continue;
                    d2 = $$26;
                    $$9 = $$25;
                }
                if (!$$22) continue;
                return $$9;
            }
        }
        return $$9;
    }

    @Nullable
    private Pair<BlockPos, RegistryEntry<Structure>> locateConcentricRingsStructure(Set<RegistryEntry<Structure>> structures, ServerWorld world, StructureAccessor structureAccessor, BlockPos center, boolean skipReferencedStructures, ConcentricRingsStructurePlacement placement) {
        List<ChunkPos> $$6 = world.getChunkManager().getStructurePlacementCalculator().getPlacementPositions(placement);
        if ($$6 == null) {
            throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist");
        }
        Pair<BlockPos, RegistryEntry<Structure>> $$7 = null;
        double $$8 = Double.MAX_VALUE;
        BlockPos.Mutable $$9 = new BlockPos.Mutable();
        for (ChunkPos $$10 : $$6) {
            Pair<BlockPos, RegistryEntry<Structure>> $$13;
            $$9.set(ChunkSectionPos.getOffsetPos($$10.x, 8), 32, ChunkSectionPos.getOffsetPos($$10.z, 8));
            double $$11 = $$9.getSquaredDistance(center);
            boolean $$12 = $$7 == null || $$11 < $$8;
            if (!$$12 || ($$13 = ChunkGenerator.locateStructure(structures, world, structureAccessor, skipReferencedStructures, placement, $$10)) == null) continue;
            $$7 = $$13;
            $$8 = $$11;
        }
        return $$7;
    }

    @Nullable
    private static Pair<BlockPos, RegistryEntry<Structure>> locateRandomSpreadStructure(Set<RegistryEntry<Structure>> structures, WorldView world, StructureAccessor structureAccessor, int centerChunkX, int centerChunkZ, int radius, boolean skipReferencedStructures, long seed, RandomSpreadStructurePlacement placement) {
        int $$9 = placement.getSpacing();
        for (int $$10 = -radius; $$10 <= radius; ++$$10) {
            boolean $$11 = $$10 == -radius || $$10 == radius;
            for (int $$12 = -radius; $$12 <= radius; ++$$12) {
                int $$15;
                int $$14;
                ChunkPos $$16;
                Pair<BlockPos, RegistryEntry<Structure>> $$17;
                boolean $$13;
                boolean bl = $$13 = $$12 == -radius || $$12 == radius;
                if (!$$11 && !$$13 || ($$17 = ChunkGenerator.locateStructure(structures, world, structureAccessor, skipReferencedStructures, placement, $$16 = placement.getStartChunk(seed, $$14 = centerChunkX + $$9 * $$10, $$15 = centerChunkZ + $$9 * $$12))) == null) continue;
                return $$17;
            }
        }
        return null;
    }

    @Nullable
    private static Pair<BlockPos, RegistryEntry<Structure>> locateStructure(Set<RegistryEntry<Structure>> structures, WorldView world, StructureAccessor structureAccessor, boolean skipReferencedStructures, StructurePlacement placement, ChunkPos pos) {
        for (RegistryEntry<Structure> $$6 : structures) {
            StructurePresence $$7 = structureAccessor.getStructurePresence(pos, $$6.value(), placement, skipReferencedStructures);
            if ($$7 == StructurePresence.START_NOT_PRESENT) continue;
            if (!skipReferencedStructures && $$7 == StructurePresence.START_PRESENT) {
                return Pair.of((Object)placement.getLocatePos(pos), $$6);
            }
            Chunk $$8 = world.getChunk(pos.x, pos.z, ChunkStatus.STRUCTURE_STARTS);
            StructureStart $$9 = structureAccessor.getStructureStart(ChunkSectionPos.from($$8), $$6.value(), $$8);
            if ($$9 == null || !$$9.hasChildren() || skipReferencedStructures && !ChunkGenerator.checkNotReferenced(structureAccessor, $$9)) continue;
            return Pair.of((Object)placement.getLocatePos($$9.getPos()), $$6);
        }
        return null;
    }

    private static boolean checkNotReferenced(StructureAccessor structureAccessor, StructureStart start) {
        if (start.isNeverReferenced()) {
            structureAccessor.incrementReferences(start);
            return true;
        }
        return false;
    }

    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
        ChunkPos $$3 = chunk.getPos();
        if (SharedConstants.isOutsideGenerationArea($$3)) {
            return;
        }
        ChunkSectionPos $$4 = ChunkSectionPos.from($$3, world.getBottomSectionCoord());
        BlockPos $$5 = $$4.getMinPos();
        RegistryWrapper.Impl $$6 = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE);
        Map<Integer, List<Structure>> $$7 = $$6.stream().collect(Collectors.groupingBy(structureType -> structureType.getFeatureGenerationStep().ordinal()));
        List<PlacedFeatureIndexer.IndexedFeatures> $$8 = this.indexedFeaturesListSupplier.get();
        ChunkRandom $$9 = new ChunkRandom(new Xoroshiro128PlusPlusRandom(RandomSeed.getSeed()));
        long $$10 = $$9.setPopulationSeed(world.getSeed(), $$5.getX(), $$5.getZ());
        ObjectArraySet $$11 = new ObjectArraySet();
        ChunkPos.stream($$4.toChunkPos(), 1).forEach(arg_0 -> ChunkGenerator.method_39787(world, (Set)$$11, arg_0));
        $$11.retainAll(this.biomeSource.getBiomes());
        int $$12 = $$8.size();
        try {
            RegistryWrapper.Impl $$13 = world.getRegistryManager().getOrThrow(RegistryKeys.PLACED_FEATURE);
            int $$14 = Math.max(GenerationStep.Feature.values().length, $$12);
            for (int $$15 = 0; $$15 < $$14; ++$$15) {
                int $$16 = 0;
                if (structureAccessor.shouldGenerateStructures()) {
                    List $$17 = $$7.getOrDefault($$15, Collections.emptyList());
                    for (Structure $$18 : $$17) {
                        $$9.setDecoratorSeed($$10, $$16, $$15);
                        Supplier<String> $$19 = () -> ChunkGenerator.method_38272((Registry)$$6, $$18);
                        try {
                            world.setCurrentlyGeneratingStructureName($$19);
                            structureAccessor.getStructureStarts($$4, $$18).forEach(start -> start.place(world, structureAccessor, this, $$9, ChunkGenerator.getBlockBoxForChunk(chunk), $$3));
                        }
                        catch (Exception $$20) {
                            CrashReport $$21 = CrashReport.create($$20, "Feature placement");
                            $$21.addElement("Feature").add("Description", $$19::get);
                            throw new CrashException($$21);
                        }
                        ++$$16;
                    }
                }
                if ($$15 >= $$12) continue;
                IntArraySet $$22 = new IntArraySet();
                for (RegistryEntry $$23 : $$11) {
                    List<RegistryEntryList<PlacedFeature>> $$24 = this.generationSettingsGetter.apply($$23).getFeatures();
                    if ($$15 >= $$24.size()) continue;
                    RegistryEntryList<PlacedFeature> $$25 = $$24.get($$15);
                    PlacedFeatureIndexer.IndexedFeatures $$26 = $$8.get($$15);
                    $$25.stream().map(RegistryEntry::value).forEach(arg_0 -> ChunkGenerator.method_39788((IntSet)$$22, $$26, arg_0));
                }
                int $$27 = $$22.size();
                int[] $$28 = $$22.toIntArray();
                Arrays.sort($$28);
                PlacedFeatureIndexer.IndexedFeatures $$29 = $$8.get($$15);
                for (int $$30 = 0; $$30 < $$27; ++$$30) {
                    int $$31 = $$28[$$30];
                    PlacedFeature $$32 = $$29.features().get($$31);
                    Supplier<String> $$33 = () -> ChunkGenerator.method_38271((Registry)$$13, $$32);
                    $$9.setDecoratorSeed($$10, $$31, $$15);
                    try {
                        world.setCurrentlyGeneratingStructureName($$33);
                        $$32.generate(world, this, $$9, $$5);
                        continue;
                    }
                    catch (Exception $$34) {
                        CrashReport $$35 = CrashReport.create($$34, "Feature placement");
                        $$35.addElement("Feature").add("Description", $$33::get);
                        throw new CrashException($$35);
                    }
                }
            }
            world.setCurrentlyGeneratingStructureName(null);
        }
        catch (Exception $$36) {
            CrashReport $$37 = CrashReport.create($$36, "Biome decoration");
            $$37.addElement("Generation").add("CenterX", $$3.x).add("CenterZ", $$3.z).add("Decoration Seed", $$10);
            throw new CrashException($$37);
        }
    }

    private static BlockBox getBlockBoxForChunk(Chunk chunk) {
        ChunkPos $$1 = chunk.getPos();
        int $$2 = $$1.getStartX();
        int $$3 = $$1.getStartZ();
        HeightLimitView $$4 = chunk.getHeightLimitView();
        int $$5 = $$4.getBottomY() + 1;
        int $$6 = $$4.getTopYInclusive();
        return new BlockBox($$2, $$5, $$3, $$2 + 15, $$6, $$3 + 15);
    }

    public abstract void buildSurface(ChunkRegion var1, StructureAccessor var2, NoiseConfig var3, Chunk var4);

    public abstract void populateEntities(ChunkRegion var1);

    public int getSpawnHeight(HeightLimitView world) {
        return 64;
    }

    public BiomeSource getBiomeSource() {
        return this.biomeSource;
    }

    public abstract int getWorldHeight();

    public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(RegistryEntry<Biome> biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
        Map<Structure, LongSet> $$4 = accessor.getStructureReferences(pos);
        for (Map.Entry<Structure, LongSet> $$5 : $$4.entrySet()) {
            Structure $$6 = $$5.getKey();
            StructureSpawns $$7 = $$6.getStructureSpawns().get(group);
            if ($$7 == null) continue;
            MutableBoolean $$8 = new MutableBoolean(false);
            Predicate<StructureStart> $$9 = $$7.boundingBox() == StructureSpawns.BoundingBox.PIECE ? start -> accessor.structureContains(pos, (StructureStart)start) : start -> start.getBoundingBox().contains(pos);
            accessor.acceptStructureStarts($$6, $$5.getValue(), start -> {
                if ($$8.isFalse() && $$9.test((StructureStart)start)) {
                    $$8.setTrue();
                }
            });
            if (!$$8.isTrue()) continue;
            return $$7.spawns();
        }
        return biome.value().getSpawnSettings().getSpawnEntries(group);
    }

    public void setStructureStarts(DynamicRegistryManager registryManager, StructurePlacementCalculator placementCalculator, StructureAccessor structureAccessor, Chunk chunk, StructureTemplateManager structureTemplateManager, RegistryKey<World> dimension) {
        ChunkPos $$6 = chunk.getPos();
        ChunkSectionPos $$7 = ChunkSectionPos.from(chunk);
        NoiseConfig $$8 = placementCalculator.getNoiseConfig();
        placementCalculator.getStructureSets().forEach(structureSet -> {
            StructurePlacement $$10 = ((StructureSet)structureSet.value()).placement();
            List<StructureSet.WeightedEntry> $$11 = ((StructureSet)structureSet.value()).structures();
            for (StructureSet.WeightedEntry $$12 : $$11) {
                StructureStart $$13 = structureAccessor.getStructureStart($$7, $$12.structure().value(), chunk);
                if ($$13 == null || !$$13.hasChildren()) continue;
                return;
            }
            if (!$$10.shouldGenerate(placementCalculator, $$4.x, $$4.z)) {
                return;
            }
            if ($$11.size() == 1) {
                this.trySetStructureStart($$11.get(0), structureAccessor, registryManager, $$8, structureTemplateManager, placementCalculator.getStructureSeed(), chunk, $$6, $$7, dimension);
                return;
            }
            ArrayList<StructureSet.WeightedEntry> $$14 = new ArrayList<StructureSet.WeightedEntry>($$11.size());
            $$14.addAll($$11);
            ChunkRandom $$15 = new ChunkRandom(new CheckedRandom(0L));
            $$15.setCarverSeed(placementCalculator.getStructureSeed(), $$4.x, $$4.z);
            int $$16 = 0;
            for (StructureSet.WeightedEntry $$17 : $$14) {
                $$16 += $$17.weight();
            }
            while (!$$14.isEmpty()) {
                StructureSet.WeightedEntry $$20;
                int $$18 = $$15.nextInt($$16);
                int $$19 = 0;
                Iterator iterator = $$14.iterator();
                while (iterator.hasNext() && ($$18 -= ($$20 = (StructureSet.WeightedEntry)iterator.next()).weight()) >= 0) {
                    ++$$19;
                }
                StructureSet.WeightedEntry $$21 = (StructureSet.WeightedEntry)$$14.get($$19);
                if (this.trySetStructureStart($$21, structureAccessor, registryManager, $$8, structureTemplateManager, placementCalculator.getStructureSeed(), chunk, $$6, $$7, dimension)) {
                    return;
                }
                $$14.remove($$19);
                $$16 -= $$21.weight();
            }
        });
    }

    private boolean trySetStructureStart(StructureSet.WeightedEntry weightedEntry, StructureAccessor structureAccessor, DynamicRegistryManager dynamicRegistryManager, NoiseConfig noiseConfig, StructureTemplateManager structureManager, long seed, Chunk chunk, ChunkPos pos, ChunkSectionPos sectionPos, RegistryKey<World> dimension) {
        Structure structure = weightedEntry.structure().value();// the structure_set.structures[i].structure
        int $$11 = ChunkGenerator.getStructureReferences(structureAccessor, chunk, sectionPos, structure);
        RegistryEntryList<Biome> $$12 = structure.getValidBiomes();
        Predicate<RegistryEntry<Biome>> $$13 = $$12::contains;
        StructureStart $$14 = structure.createStructureStart(weightedEntry.structure(), dimension, dynamicRegistryManager, this, this.biomeSource, noiseConfig, structureManager, seed, pos, $$11, chunk, $$13);
        if ($$14.hasChildren()) {
            structureAccessor.setStructureStart(sectionPos, structure, $$14, chunk);
            return true;
        }
        return false;
    }

    private static int getStructureReferences(StructureAccessor structureAccessor, Chunk chunk, ChunkSectionPos sectionPos, Structure structure) {
        StructureStart $$4 = structureAccessor.getStructureStart(sectionPos, structure, chunk);
        return $$4 != null ? $$4.getReferences() : 0;
    }

    public void addStructureReferences(StructureWorldAccess world, StructureAccessor structureAccessor, Chunk chunk) {
        int $$3 = 8;
        ChunkPos $$4 = chunk.getPos();
        int $$5 = $$4.x;
        int $$6 = $$4.z;
        int $$7 = $$4.getStartX();
        int $$8 = $$4.getStartZ();
        ChunkSectionPos $$9 = ChunkSectionPos.from(chunk);
        for (int $$10 = $$5 - 8; $$10 <= $$5 + 8; ++$$10) {
            for (int $$11 = $$6 - 8; $$11 <= $$6 + 8; ++$$11) {
                long $$12 = ChunkPos.toLong($$10, $$11);
                for (StructureStart $$13 : world.getChunk($$10, $$11).getStructureStarts().values()) {
                    try {
                        if (!$$13.hasChildren() || !$$13.getBoundingBox().intersectsXZ($$7, $$8, $$7 + 15, $$8 + 15)) continue;
                        structureAccessor.addStructureReference($$9, $$13.getStructure(), $$12, chunk);
                        DebugInfoSender.sendStructureStart(world, $$13);
                    }
                    catch (Exception $$14) {
                        CrashReport $$15 = CrashReport.create($$14, "Generating structure reference");
                        CrashReportSection $$16 = $$15.addElement("Structure");
                        Optional<Registry<Structure>> $$17 = world.getRegistryManager().getOptional(RegistryKeys.STRUCTURE);
                        $$16.add("Id", () -> $$17.map(structureTypeRegistry -> structureTypeRegistry.getId($$13.getStructure()).toString()).orElse("UNKNOWN"));
                        $$16.add("Name", () -> Registries.STRUCTURE_TYPE.getId($$13.getStructure().getType()).toString());
                        $$16.add("Class", () -> $$13.getStructure().getClass().getCanonicalName());
                        throw new CrashException($$15);
                    }
                }
            }
        }
    }

    public abstract CompletableFuture<Chunk> populateNoise(Blender var1, NoiseConfig var2, StructureAccessor var3, Chunk var4);

    public abstract int getSeaLevel();

    public abstract int getMinimumY();

    public abstract int getHeight(int var1, int var2, Heightmap.Type var3, HeightLimitView var4, NoiseConfig var5);

    public abstract VerticalBlockSample getColumnSample(int var1, int var2, HeightLimitView var3, NoiseConfig var4);

    public int getHeightOnGround(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return this.getHeight(x, z, heightmap, world, noiseConfig);
    }

    public int getHeightInGround(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return this.getHeight(x, z, heightmap, world, noiseConfig) - 1;
    }

    public abstract void appendDebugHudText(List<String> var1, NoiseConfig var2, BlockPos var3);

    @Deprecated
    public GenerationSettings getGenerationSettings(RegistryEntry<Biome> biomeEntry) {
        return this.generationSettingsGetter.apply(biomeEntry);
    }

    private static /* synthetic */ String method_38271(Registry $$0, PlacedFeature $$1) {
        return $$0.getKey($$1).map(Object::toString).orElseGet($$1::toString);
    }

    private static /* synthetic */ void method_39788(IntSet $$0, PlacedFeatureIndexer.IndexedFeatures $$1, PlacedFeature feature) {
        $$0.add($$1.indexMapping().applyAsInt(feature));
    }

    private static /* synthetic */ String method_38272(Registry $$0, Structure $$1) {
        return $$0.getKey($$1).map(Object::toString).orElseGet($$1::toString);
    }

    private static /* synthetic */ void method_39787(StructureWorldAccess $$0, Set $$1, ChunkPos pos) {
        Chunk $$3 = $$0.getChunk(pos.x, pos.z);
        for (ChunkSection $$4 : $$3.getSectionArray()) {
            $$4.getBiomeContainer().forEachValue($$1::add);
        }
    }
}

