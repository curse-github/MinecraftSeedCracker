/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkGeneratorStructureState
/*     */ {
/*  36 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*     */   private final RandomState randomState;
/*     */   
/*     */   private final BiomeSource biomeSource;
/*     */   
/*     */   private final long levelSeed;
/*     */   
/*     */   private final long concentricRingsSeed;
/*     */   
/*     */   private final Map<Structure, List<StructurePlacement>> placementsForStructure;
/*     */   
/*     */   private final Map<ConcentricRingsStructurePlacement, CompletableFuture<List<ChunkPos>>> ringPositions;
/*     */   
/*     */   private boolean hasGeneratedPositions;
/*     */   
/*     */   private final List<Holder<StructureSet>> possibleStructureSets;
/*     */ 
/*     */   
/*     */   public static ChunkGeneratorStructureState createForFlat(RandomState randomState, long levelSeed, BiomeSource biomeSource, Stream<Holder<StructureSet>> structureOverrides) {
/*  57 */     List<Holder<StructureSet>> structures = structureOverrides.filter(structureSet -> hasBiomesForStructureSet((StructureSet)structureSet.value(), biomeSource)).toList();
/*     */     
/*  59 */     return new ChunkGeneratorStructureState(randomState, biomeSource, levelSeed, 0L, structures);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ChunkGeneratorStructureState createForNormal(RandomState randomState, long levelSeed, BiomeSource biomeSource, HolderLookup<StructureSet> allStructures) {
/*  67 */     List<Holder<StructureSet>> structures = (List)allStructures.listElements().filter(structureSet -> hasBiomesForStructureSet((StructureSet)structureSet.value(), biomeSource)).collect(Collectors.toUnmodifiableList());
/*     */     
/*  69 */     return new ChunkGeneratorStructureState(randomState, biomeSource, levelSeed, levelSeed, structures);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean hasBiomesForStructureSet(StructureSet structureSet, BiomeSource biomeSource) {
/*  74 */     Stream<Holder<Biome>> structureBiomes = structureSet.structures().stream().flatMap(entry -> {
/*  75 */           Structure structure = (Structure)entry.structure().value();
/*  76 */           return structure.biomes().stream();
/*     */         });
/*     */     
/*  79 */     Objects.requireNonNull(biomeSource.possibleBiomes()); return structureBiomes.anyMatch(biomeSource.possibleBiomes()::contains);
/*     */   } private ChunkGeneratorStructureState(RandomState randomState, BiomeSource biomeSource, long levelSeed, long concentricRingsSeed, List<Holder<StructureSet>> possibleStructureSets) {
/*     */     this.placementsForStructure = new Object2ObjectOpenHashMap();
/*     */     this.ringPositions = new Object2ObjectArrayMap();
/*  83 */     this.randomState = randomState;
/*  84 */     this.levelSeed = levelSeed;
/*  85 */     this.biomeSource = biomeSource;
/*  86 */     this.concentricRingsSeed = concentricRingsSeed;
/*  87 */     this.possibleStructureSets = possibleStructureSets;
/*     */   }
/*     */ 
/*     */   
/*  91 */   public List<Holder<StructureSet>> possibleStructureSets() { return this.possibleStructureSets; }
/*     */ 
/*     */   
/*     */   private void generatePositions() {
/*  95 */     Set<Holder<Biome>> possibleBiomes = this.biomeSource.possibleBiomes();
/*  96 */     possibleStructureSets().forEach(setHolder -> {
/*     */           
/*  98 */           StructureSet set = (StructureSet)setHolder.value();
/*  99 */           boolean hasAnyPlaceableStructures = false;
/* 100 */           for (StructureSet.StructureSelectionEntry entry : set.structures()) {
/* 101 */             Structure structure = (Structure)entry.structure().value();
/* 102 */             Objects.requireNonNull(possibleBiomes); if (structure.biomes().stream().anyMatch(possibleBiomes::contains)) {
/* 103 */               ((List)this.placementsForStructure.computeIfAbsent(structure, ())).add(set.placement());
/* 104 */               hasAnyPlaceableStructures = true;
/*     */             } 
/*     */           } 
/* 107 */           if (hasAnyPlaceableStructures) { StructurePlacement patt0$temp = set.placement(); if (patt0$temp instanceof ConcentricRingsStructurePlacement) { ConcentricRingsStructurePlacement ringsPlacement = (ConcentricRingsStructurePlacement)patt0$temp;
/* 108 */               this.ringPositions.put(ringsPlacement, generateRingPositions(setHolder, ringsPlacement)); }
/*     */              }
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   private CompletableFuture<List<ChunkPos>> generateRingPositions(Holder<StructureSet> structureSet, ConcentricRingsStructurePlacement placement) {
/* 115 */     if (placement.count() == 0) {
/* 116 */       return CompletableFuture.completedFuture(List.of());
/*     */     }
/*     */     
/* 119 */     Stopwatch stopwatch = Stopwatch.createStarted(Util.TICKER);
/*     */     
/* 121 */     int distance = placement.distance();
/* 122 */     int count = placement.count();
/*     */     
/* 124 */     List<CompletableFuture<ChunkPos>> tasks = new ArrayList<CompletableFuture<ChunkPos>>(count);
/*     */     
/* 126 */     int spread = placement.spread();
/* 127 */     HolderSet<Biome> preferredBiomes = placement.preferredBiomes();
/*     */     
/* 129 */     RandomSource random = RandomSource.create();
/*     */ 
/*     */     
/* 132 */     random.setSeed(this.concentricRingsSeed);
/*     */     
/* 134 */     double angle = random.nextDouble() * Math.PI * 2.0D;
/*     */     
/* 136 */     int positionInCircle = 0;
/* 137 */     int circle = 0;
/* 138 */     for (int i = 0; i < count; i++) {
/* 139 */       double dist = (4 * distance + distance * circle * 6) + (random.nextDouble() - 0.5D) * distance * 2.5D;
/* 140 */       int initialX = (int)Math.round(Math.cos(angle) * dist);
/* 141 */       int initialZ = (int)Math.round(Math.sin(angle) * dist);
/*     */       
/* 143 */       RandomSource biomeSearchGenerator = random.fork();
/* 144 */       tasks.add(CompletableFuture.supplyAsync(() -> {
/* 145 */               Objects.requireNonNull(preferredBiomes); Pair<BlockPos, Holder<Biome>> closestBiome = this.biomeSource.findBiomeHorizontal(SectionPos.sectionToBlockCoord(initialX, 8), 0, SectionPos.sectionToBlockCoord(initialZ, 8), 112, preferredBiomes::contains, biomeSearchGenerator, this.randomState.sampler());
/* 146 */               if (closestBiome != null) {
/* 147 */                 BlockPos position = (BlockPos)closestBiome.getFirst();
/* 148 */                 return new ChunkPos(SectionPos.blockToSectionCoord(position.getX()), SectionPos.blockToSectionCoord(position.getZ()));
/*     */               } 
/* 150 */               return new ChunkPos(initialX, initialZ);
/* 151 */             }Util.backgroundExecutor().forName("structureRings")));
/*     */       
/* 153 */       angle += 6.283185307179586D / spread;
/*     */       
/* 155 */       if (++positionInCircle == spread) {
/* 156 */         circle++;
/* 157 */         positionInCircle = 0;
/* 158 */         spread += 2 * spread / (circle + 1);
/* 159 */         spread = Math.min(spread, count - i);
/* 160 */         angle += random.nextDouble() * Math.PI * 2.0D;
/*     */       } 
/*     */     } 
/*     */     
/* 164 */     return Util.sequence(tasks).thenApply(ringPositions -> {
/* 165 */           double elapsedSeconds = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS) / 1000.0D;
/* 166 */           LOGGER.debug("Calculation for {} took {}s", structureSet, Double.valueOf(elapsedSeconds));
/* 167 */           return ringPositions;
/*     */         });
/*     */   }
/*     */   
/*     */   public void ensureStructuresGenerated() {
/* 172 */     if (!this.hasGeneratedPositions) {
/* 173 */       generatePositions();
/* 174 */       this.hasGeneratedPositions = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<ChunkPos> getRingPositionsFor(ConcentricRingsStructurePlacement placement) {
/* 179 */     ensureStructuresGenerated();
/* 180 */     CompletableFuture<List<ChunkPos>> result = (CompletableFuture)this.ringPositions.get(placement);
/* 181 */     return (result != null) ? (List)result.join() : null;
/*     */   }
/*     */   
/*     */   public List<StructurePlacement> getPlacementsForStructure(Holder<Structure> structure) {
/* 185 */     ensureStructuresGenerated();
/* 186 */     return (List)this.placementsForStructure.getOrDefault(structure.value(), List.of());
/*     */   }
/*     */ 
/*     */   
/* 190 */   public RandomState randomState() { return this.randomState; }
/*     */ 
/*     */   
/*     */   public boolean hasStructureChunkInRange(Holder<StructureSet> structureSet, int sourceX, int sourceZ, int range) {
/* 194 */     StructurePlacement placement = ((StructureSet)structureSet.value()).placement();
/* 195 */     for (int testX = sourceX - range; testX <= sourceX + range; testX++) {
/* 196 */       for (int testZ = sourceZ - range; testZ <= sourceZ + range; testZ++) {
/* 197 */         if (placement.isStructureChunk(this, testX, testZ)) {
/* 198 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 207 */   public long getLevelSeed() { return this.levelSeed; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\ChunkGeneratorStructureState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */