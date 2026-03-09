/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.BitSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.LongStream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public final class BelowZeroRetrogen {
/*  28 */   private static final BitSet EMPTY = new BitSet(0);
/*     */   
/*  30 */   private static final Codec<BitSet> BITSET_CODEC = Codec.LONG_STREAM.xmap(longStream -> BitSet.valueOf(longStream.toArray()), bitSet -> LongStream.of(bitSet.toLongArray()));
/*  31 */   private static final Codec<ChunkStatus> NON_EMPTY_CHUNK_STATUS = BuiltInRegistries.CHUNK_STATUS.byNameCodec().comapFlatMap(status -> 
/*  32 */       (status == ChunkStatus.EMPTY) ? DataResult.error(()) : DataResult.success(status), 
/*  33 */       Function.identity());
/*     */ 
/*     */   
/*  36 */   public static final Codec<BelowZeroRetrogen> CODEC = RecordCodecBuilder.create(i -> i.group(NON_EMPTY_CHUNK_STATUS
/*  37 */         .fieldOf("target_status").forGetter(BelowZeroRetrogen::targetStatus), BITSET_CODEC
/*  38 */         .lenientOptionalFieldOf("missing_bedrock").forGetter(()))
/*  39 */       .apply(i, BelowZeroRetrogen::new));
/*     */   
/*  41 */   private static final Set<ResourceKey<Biome>> RETAINED_RETROGEN_BIOMES = Set.of(Biomes.LUSH_CAVES, Biomes.DRIPSTONE_CAVES, Biomes.DEEP_DARK);
/*  42 */   public static final LevelHeightAccessor UPGRADE_HEIGHT_ACCESSOR = new LevelHeightAccessor()
/*     */     {
/*     */       public int getHeight() {
/*  45 */         return 64;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  50 */       public int getMinY() { return -64; }
/*     */     };
/*     */ 
/*     */   
/*     */   private final ChunkStatus targetStatus;
/*     */   private final BitSet missingBedrock;
/*     */   
/*     */   private BelowZeroRetrogen(ChunkStatus targetStatus, Optional<BitSet> missingBedrock) {
/*  58 */     this.targetStatus = targetStatus;
/*  59 */     this.missingBedrock = (BitSet)missingBedrock.orElse(EMPTY);
/*     */   }
/*     */   
/*     */   public static void replaceOldBedrock(ProtoChunk chunk) {
/*  63 */     int maxGeneratedBedrockY = 4;
/*  64 */     BlockPos.betweenClosed(0, 0, 0, 15, 4, 15).forEach(pos -> {
/*  65 */           if (chunk.getBlockState(pos).is(Blocks.BEDROCK)) {
/*  66 */             chunk.setBlockState(pos, Blocks.DEEPSLATE.defaultBlockState());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void applyBedrockMask(ProtoChunk chunk) {
/*  72 */     LevelHeightAccessor heightAccessor = chunk.getHeightAccessorForGeneration();
/*  73 */     int minY = heightAccessor.getMinY();
/*  74 */     int maxY = heightAccessor.getMaxY();
/*     */     
/*  76 */     for (int x = 0; x < 16; x++) {
/*  77 */       for (int z = 0; z < 16; z++) {
/*  78 */         if (hasBedrockHole(x, z)) {
/*  79 */           BlockPos.betweenClosed(x, minY, z, x, maxY, z).forEach(pos -> chunk.setBlockState(pos, Blocks.AIR.defaultBlockState()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  86 */   public ChunkStatus targetStatus() { return this.targetStatus; }
/*     */ 
/*     */ 
/*     */   
/*  90 */   public boolean hasBedrockHoles() { return !this.missingBedrock.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*  94 */   public boolean hasBedrockHole(int x, int z) { return this.missingBedrock.get((z & 0xF) * 16 + (x & 0xF)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BiomeResolver getBiomeResolver(BiomeResolver biomeResolver, ChunkAccess protoChunk) {
/*  99 */     if (!protoChunk.isUpgrading()) {
/* 100 */       return biomeResolver;
/*     */     }
/*     */     
/* 103 */     Objects.requireNonNull(RETAINED_RETROGEN_BIOMES); Predicate<ResourceKey<Biome>> retainedBiomes = RETAINED_RETROGEN_BIOMES::contains;
/*     */     
/* 105 */     return (quartX, quartY, quartZ, sampler) -> {
/* 106 */         Holder<Biome> noiseBiome = biomeResolver.getNoiseBiome(quartX, quartY, quartZ, sampler);
/*     */         
/* 108 */         if (noiseBiome.is(retainedBiomes)) {
/* 109 */           return noiseBiome;
/*     */         }
/*     */         
/* 112 */         return protoChunk.getNoiseBiome(quartX, 0, quartZ);
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\BelowZeroRetrogen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */