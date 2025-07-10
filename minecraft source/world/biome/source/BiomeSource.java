/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Sets
 *  com.mojang.datafixers.util.Pair
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  javax.annotation.Nullable
 */
package net.minecraft.world.biome.source;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSupplier;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public abstract class BiomeSource
implements BiomeSupplier {
    public static final Codec<BiomeSource> CODEC = Registries.BIOME_SOURCE.getCodec().dispatchStable(BiomeSource::getCodec, Function.identity());
    private final Supplier<Set<RegistryEntry<Biome>>> biomes = Suppliers.memoize(() -> (Set)this.biomeStream().distinct().collect(ImmutableSet.toImmutableSet()));

    protected BiomeSource() {
    }

    protected abstract MapCodec<? extends BiomeSource> getCodec();

    protected abstract Stream<RegistryEntry<Biome>> biomeStream();

    public Set<RegistryEntry<Biome>> getBiomes() {
        return this.biomes.get();
    }

    public Set<RegistryEntry<Biome>> getBiomesInArea(int x, int y, int z, int radius, MultiNoiseUtil.MultiNoiseSampler sampler) {
        int $$5 = BiomeCoords.fromBlock(x - radius);
        int $$6 = BiomeCoords.fromBlock(y - radius);
        int $$7 = BiomeCoords.fromBlock(z - radius);
        int $$8 = BiomeCoords.fromBlock(x + radius);
        int $$9 = BiomeCoords.fromBlock(y + radius);
        int $$10 = BiomeCoords.fromBlock(z + radius);
        int $$11 = $$8 - $$5 + 1;
        int $$12 = $$9 - $$6 + 1;
        int $$13 = $$10 - $$7 + 1;
        HashSet $$14 = Sets.newHashSet();
        for (int $$15 = 0; $$15 < $$13; ++$$15) {
            for (int $$16 = 0; $$16 < $$11; ++$$16) {
                for (int $$17 = 0; $$17 < $$12; ++$$17) {
                    int $$18 = $$5 + $$16;
                    int $$19 = $$6 + $$17;
                    int $$20 = $$7 + $$15;
                    $$14.add(this.getBiome($$18, $$19, $$20, sampler));
                }
            }
        }
        return $$14;
    }

    @Nullable
    public Pair<BlockPos, RegistryEntry<Biome>> locateBiome(int x, int y, int z, int radius, Predicate<RegistryEntry<Biome>> predicate, Random random, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
        return this.locateBiome(x, y, z, radius, 1, predicate, random, false, noiseSampler);
    }

    @Nullable
    public Pair<BlockPos, RegistryEntry<Biome>> locateBiome(BlockPos origin, int radius, int horizontalBlockCheckInterval, int verticalBlockCheckInterval, Predicate<RegistryEntry<Biome>> predicate, MultiNoiseUtil.MultiNoiseSampler noiseSampler, WorldView world) {
        Set $$7 = this.getBiomes().stream().filter(predicate).collect(Collectors.toUnmodifiableSet());
        if ($$7.isEmpty()) {
            return null;
        }
        int $$8 = Math.floorDiv(radius, horizontalBlockCheckInterval);
        int[] $$9 = MathHelper.stream(origin.getY(), world.getBottomY() + 1, world.getTopYInclusive() + 1, verticalBlockCheckInterval).toArray();
        for (BlockPos.Mutable $$10 : BlockPos.iterateInSquare(BlockPos.ORIGIN, $$8, Direction.EAST, Direction.SOUTH)) {
            int $$11 = origin.getX() + $$10.getX() * horizontalBlockCheckInterval;
            int $$12 = origin.getZ() + $$10.getZ() * horizontalBlockCheckInterval;
            int $$13 = BiomeCoords.fromBlock($$11);
            int $$14 = BiomeCoords.fromBlock($$12);
            for (int $$15 : $$9) {
                int $$16 = BiomeCoords.fromBlock($$15);
                RegistryEntry<Biome> $$17 = this.getBiome($$13, $$16, $$14, noiseSampler);
                if (!$$7.contains($$17)) continue;
                return Pair.of((Object)new BlockPos($$11, $$15, $$12), $$17);
            }
        }
        return null;
    }

    @Nullable
    public Pair<BlockPos, RegistryEntry<Biome>> locateBiome(int x, int y, int z, int radius, int blockCheckInterval, Predicate<RegistryEntry<Biome>> predicate, Random random, boolean $$7, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
        int $$15;
        int $$9 = BiomeCoords.fromBlock(x);
        int $$10 = BiomeCoords.fromBlock(z);
        int $$11 = BiomeCoords.fromBlock(radius);
        int $$12 = BiomeCoords.fromBlock(y);
        Pair $$13 = null;
        int $$14 = 0;
        for (int $$16 = $$15 = $$7 ? 0 : $$11; $$16 <= $$11; $$16 += blockCheckInterval) {
            int $$17;
            int n = $$17 = SharedConstants.DEBUG_BIOME_SOURCE ? 0 : -$$16;
            while ($$17 <= $$16) {
                boolean $$18 = Math.abs($$17) == $$16;
                for (int $$19 = -$$16; $$19 <= $$16; $$19 += blockCheckInterval) {
                    int $$22;
                    int $$21;
                    RegistryEntry<Biome> $$23;
                    if ($$7) {
                        boolean $$20;
                        boolean bl = $$20 = Math.abs($$19) == $$16;
                        if (!$$20 && !$$18) continue;
                    }
                    if (!predicate.test($$23 = this.getBiome($$21 = $$9 + $$19, $$12, $$22 = $$10 + $$17, noiseSampler))) continue;
                    if ($$13 == null || random.nextInt($$14 + 1) == 0) {
                        BlockPos $$24 = new BlockPos(BiomeCoords.toBlock($$21), y, BiomeCoords.toBlock($$22));
                        if ($$7) {
                            return Pair.of((Object)$$24, $$23);
                        }
                        $$13 = Pair.of((Object)$$24, $$23);
                    }
                    ++$$14;
                }
                $$17 += blockCheckInterval;
            }
        }
        return $$13;
    }

    @Override
    public abstract RegistryEntry<Biome> getBiome(int var1, int var2, int var3, MultiNoiseUtil.MultiNoiseSampler var4);

    public void addDebugInfo(List<String> info, BlockPos pos, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
    }
}

