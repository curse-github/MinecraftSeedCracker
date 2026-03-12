package net.minecraft.world.level.levelgen.structure.placement;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public abstract class StructurePlacement {
    public static final Codec<StructurePlacement> CODEC = BuiltInRegistries.STRUCTURE_PLACEMENT.byNameCodec()
            .dispatch(StructurePlacement::type, StructurePlacementType::codec);
    private static final int HIGHLY_ARBITRARY_RANDOM_SALT = 10387320;
    private final Vec3i locateOffset;
    private final FrequencyReductionMethod frequencyReductionMethod;
    private final float frequency;
    private final int salt;
    private final Optional<ExclusionZone> exclusionZone;

    protected static <S extends StructurePlacement> Products.P5<RecordCodecBuilder.Mu<S>, Vec3i, FrequencyReductionMethod, Float, Integer, Optional<ExclusionZone>> placementCodec(
            RecordCodecBuilder.Instance<S> i) {
        return i.group(
                Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO)
                        .forGetter(StructurePlacement::locateOffset),
                FrequencyReductionMethod.CODEC
                        .optionalFieldOf("frequency_reduction_method", FrequencyReductionMethod.DEFAULT)
                        .forGetter(StructurePlacement::frequencyReductionMethod),
                Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", Float.valueOf(1.0F))
                        .forGetter(StructurePlacement::frequency),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(StructurePlacement::salt),
                ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(StructurePlacement::exclusionZone));
    }

    @FunctionalInterface
    public static interface FrequencyReducer {
        boolean shouldGenerate(long param1Long, int param1Int1, int param1Int2, int param1Int3, float param1Float);
    }

    @Deprecated
    public static final class ExclusionZone extends Record {
        private final Holder<StructureSet> otherSet;
        private final int chunkCount;

        public Holder<StructureSet> otherSet() {
            return this.otherSet;
        }

        public final String toString() {

        public ExclusionZone(Holder<StructureSet> otherSet, int chunkCount) {
            this.otherSet = otherSet;
            this.chunkCount = chunkCount;
        }

        public static final Codec<ExclusionZone> CODEC = RecordCodecBuilder.create(i -> i
                .group(RegistryFileCodec.create(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC, false)
                        .fieldOf("other_set").forGetter(ExclusionZone::otherSet),
                        Codec.intRange(1, 16).fieldOf("chunk_count").forGetter(ExclusionZone::chunkCount))
                .apply(i, ExclusionZone::new));

        private boolean isPlacementForbidden(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
            return state.hasStructureChunkInRange(this.otherSet, sourceX, sourceZ, this.chunkCount);
        }
    }

    protected StructurePlacement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency,
            int salt, Optional<ExclusionZone> exclusionZone) {
        this.locateOffset = locateOffset;
        this.frequencyReductionMethod = frequencyReductionMethod;
        this.frequency = frequency;
        this.salt = salt;
        this.exclusionZone = exclusionZone;
    }

    protected Vec3i locateOffset() {
        return this.locateOffset;
    }

    protected FrequencyReductionMethod frequencyReductionMethod() {
        return this.frequencyReductionMethod;
    }

    protected float frequency() {
        return this.frequency;
    }

    protected int salt() {
        return this.salt;
    }

    protected Optional<ExclusionZone> exclusionZone() {
        return this.exclusionZone;
    }

    public boolean isStructureChunk(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
        return (isPlacementChunk(state, sourceX, sourceZ)
                && applyAdditionalChunkRestrictions(sourceX, sourceZ, state.getLevelSeed())
                && applyInteractionsWithOtherStructures(state, sourceX, sourceZ));
    }

    public boolean applyAdditionalChunkRestrictions(int sourceX, int sourceZ, long levelSeed) {
        if (this.frequency < 1.0F && !this.frequencyReductionMethod.shouldGenerate(levelSeed, this.salt, sourceX,
                sourceZ, this.frequency)) {
            return false;
        }
        return true;
    }

    public boolean applyInteractionsWithOtherStructures(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
        if (this.exclusionZone.isPresent()
                && ((ExclusionZone) this.exclusionZone.get()).isPlacementForbidden(state, sourceX, sourceZ)) {
            return false;
        }
        return true;
    }

    protected abstract boolean isPlacementChunk(ChunkGeneratorStructureState paramChunkGeneratorStructureState,
            int paramInt1, int paramInt2);

    public BlockPos getLocatePos(ChunkPos chunkPos) {
        return (new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ())).offset(locateOffset());
    }

    public abstract StructurePlacementType<?> type();

    private static boolean probabilityReducer(long seed, int salt, int sourceX, int sourceZ, float probability) {
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setLargeFeatureWithSalt(seed, salt, sourceX, sourceZ);
        return (random.nextFloat() < probability);
    }

    private static boolean legacyProbabilityReducerWithDouble(long seed, int salt, int sourceX, int sourceZ,
            float probability) {
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setLargeFeatureSeed(seed, sourceX, sourceZ);
        return (random.nextDouble() < probability);
    }

    private static boolean legacyArbitrarySaltProbabilityReducer(long seed, int salt, int sourceX, int sourceZ,
            float probability) {
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setLargeFeatureWithSalt(seed, sourceX, sourceZ, 10387320);
        return (random.nextFloat() < probability);
    }

    private static boolean legacyPillagerOutpostReducer(long seed, int salt, int sourceX, int sourceZ,
            float probability) {
        int cx = sourceX >> 4;
        int cz = sourceZ >> 4;
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setSeed((cx ^ cz << 4) ^ seed);
        random.nextInt();
        return (random.nextInt((int) (1.0F / probability)) == 0);
    }

    public enum FrequencyReductionMethod implements StringRepresentable {
        DEFAULT("default", StructurePlacement::probabilityReducer),
        LEGACY_TYPE_1("legacy_type_1", StructurePlacement::legacyPillagerOutpostReducer),
        LEGACY_TYPE_2("legacy_type_2", StructurePlacement::legacyArbitrarySaltProbabilityReducer),
        LEGACY_TYPE_3("legacy_type_3", StructurePlacement::legacyProbabilityReducerWithDouble);

        public static final Codec<FrequencyReductionMethod> CODEC;
        private final String name;
        private final StructurePlacement.FrequencyReducer reducer;
        static {
            CODEC = StringRepresentable.fromEnum(FrequencyReductionMethod::values);
        }

        FrequencyReductionMethod(String name, StructurePlacement.FrequencyReducer reducer) {
            this.name = name;
            this.reducer = reducer;
        }

        public boolean shouldGenerate(long seed, int salt, int sourceX, int sourceZ, float probability) {
            return this.reducer.shouldGenerate(seed, salt, sourceX, sourceZ, probability);
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
