/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.levelgen.synth.BlendedNoise;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ 
/*     */ public final class RandomState
/*     */ {
/*     */   private final PositionalRandomFactory random;
/*     */   private final HolderGetter<NormalNoise.NoiseParameters> noises;
/*     */   private final NoiseRouter router;
/*     */   private final Climate.Sampler sampler;
/*     */   private final SurfaceSystem surfaceSystem;
/*     */   private final PositionalRandomFactory aquiferRandom;
/*     */   private final PositionalRandomFactory oreRandom;
/*     */   private final Map<ResourceKey<NormalNoise.NoiseParameters>, NormalNoise> noiseIntances;
/*     */   private final Map<Identifier, PositionalRandomFactory> positionalRandoms;
/*     */   
/*  29 */   public static RandomState create(HolderGetter.Provider holders, ResourceKey<NoiseGeneratorSettings> noiseSettings, long seed) { return create((NoiseGeneratorSettings)holders.lookupOrThrow(Registries.NOISE_SETTINGS).getOrThrow(noiseSettings).value(), holders.lookupOrThrow(Registries.NOISE), seed); }
/*     */ 
/*     */ 
/*     */   
/*  33 */   public static RandomState create(NoiseGeneratorSettings settings, HolderGetter<NormalNoise.NoiseParameters> noises, long seed) { return new RandomState(settings, noises, seed); }
/*     */ 
/*     */   
/*     */   private RandomState(NoiseGeneratorSettings settings, HolderGetter<NormalNoise.NoiseParameters> noises, final long seed) {
/*  37 */     this.random = settings.getRandomSource().newInstance(seed).forkPositional();
/*  38 */     this.noises = noises;
/*  39 */     this.aquiferRandom = this.random.fromHashOf(Identifier.withDefaultNamespace("aquifer")).forkPositional();
/*  40 */     this.oreRandom = this.random.fromHashOf(Identifier.withDefaultNamespace("ore")).forkPositional();
/*  41 */     this.noiseIntances = new ConcurrentHashMap();
/*  42 */     this.positionalRandoms = new ConcurrentHashMap();
/*     */     
/*  44 */     this.surfaceSystem = new SurfaceSystem(this, settings.defaultBlock(), settings.seaLevel(), this.random);
/*     */     
/*  46 */     final boolean useLegacyInit = settings.useLegacyRandomSource();
/*     */     class NoiseWiringHelper implements DensityFunction.Visitor {
/*  48 */       private final Map<DensityFunction, DensityFunction> wrapped = new HashMap();
/*     */ 
/*     */       
/*  51 */       private RandomSource newLegacyInstance(long seedOffset) { return new LegacyRandomSource(seed + seedOffset); }
/*     */ 
/*     */ 
/*     */       
/*     */       public DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/*  56 */         Holder<NormalNoise.NoiseParameters> noiseData = noise.noiseData();
/*  57 */         if (useLegacyInit) {
/*  58 */           if (noiseData.is(Noises.TEMPERATURE)) {
/*  59 */             NormalNoise newNoise = NormalNoise.createLegacyNetherBiome(newLegacyInstance(0L), new NormalNoise.NoiseParameters(-7, 1.0D, new double[] { 1.0D }));
/*  60 */             return new DensityFunction.NoiseHolder(noiseData, newNoise);
/*     */           } 
/*  62 */           if (noiseData.is(Noises.VEGETATION)) {
/*  63 */             NormalNoise newNoise = NormalNoise.createLegacyNetherBiome(newLegacyInstance(1L), new NormalNoise.NoiseParameters(-7, 1.0D, new double[] { 1.0D }));
/*  64 */             return new DensityFunction.NoiseHolder(noiseData, newNoise);
/*     */           } 
/*  66 */           if (noiseData.is(Noises.SHIFT)) {
/*  67 */             NormalNoise newOffsetNoise = NormalNoise.create(RandomState.this.random.fromHashOf(Noises.SHIFT.identifier()), new NormalNoise.NoiseParameters(0, 0.0D, new double[0]));
/*  68 */             return new DensityFunction.NoiseHolder(noiseData, newOffsetNoise);
/*     */           } 
/*     */         } 
/*  71 */         NormalNoise instantiate = RandomState.this.getOrCreateNoise((ResourceKey)noiseData.unwrapKey().orElseThrow());
/*  72 */         return new DensityFunction.NoiseHolder(noiseData, instantiate);
/*     */       }
/*     */       
/*     */       private DensityFunction wrapNew(DensityFunction function) {
/*  76 */         if (function instanceof BlendedNoise) { BlendedNoise noise = (BlendedNoise)function;
/*  77 */           RandomSource terrainRandom = useLegacyInit ? newLegacyInstance(0L) : RandomState.this.random.fromHashOf(Identifier.withDefaultNamespace("terrain"));
/*  78 */           return noise.withNewRandom(terrainRandom); }
/*     */         
/*  80 */         if (function instanceof DensityFunctions.EndIslandDensityFunction) {
/*  81 */           return new DensityFunctions.EndIslandDensityFunction(seed);
/*     */         }
/*  83 */         return function;
/*     */       }
/*     */ 
/*     */       
/*     */       public DensityFunction apply(DensityFunction function) {
/*  88 */         return (DensityFunction)this.wrapped.computeIfAbsent(function, this::wrapNew);
/*     */       }
/*     */     };
/*     */     
/*  92 */     this.router = settings.noiseRouter().mapAll(new NoiseWiringHelper());
/*     */     
/*  94 */     DensityFunction.Visitor noiseFlattener = new DensityFunction.Visitor(this) {
/*  95 */         private final Map<DensityFunction, DensityFunction> wrapped = new HashMap();
/*     */         
/*     */         private DensityFunction wrapNew(DensityFunction function) {
/*  98 */           if (function instanceof DensityFunctions.HolderHolder) { DensityFunctions.HolderHolder holder = (DensityFunctions.HolderHolder)function;
/*  99 */             return (DensityFunction)holder.function().value(); }
/*     */           
/* 101 */           if (function instanceof DensityFunctions.Marker) { DensityFunctions.Marker marker = (DensityFunctions.Marker)function;
/* 102 */             return marker.wrapped(); }
/*     */           
/* 104 */           return function;
/*     */         }
/*     */ 
/*     */         
/*     */         public DensityFunction apply(DensityFunction input) {
/* 109 */           return (DensityFunction)this.wrapped.computeIfAbsent(input, this::wrapNew);
/*     */         }
/*     */       };
/*     */     
/* 113 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 120 */       .sampler = new Climate.Sampler(this.router.temperature().mapAll(noiseFlattener), this.router.vegetation().mapAll(noiseFlattener), this.router.continents().mapAll(noiseFlattener), this.router.erosion().mapAll(noiseFlattener), this.router.depth().mapAll(noiseFlattener), this.router.ridges().mapAll(noiseFlattener), settings.spawnTarget());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 125 */   public NormalNoise getOrCreateNoise(ResourceKey<NormalNoise.NoiseParameters> noise) { return (NormalNoise)this.noiseIntances.computeIfAbsent(noise, key -> Noises.instantiate(this.noises, this.random, noise)); }
/*     */ 
/*     */ 
/*     */   
/* 129 */   public PositionalRandomFactory getOrCreateRandomFactory(Identifier name) { return (PositionalRandomFactory)this.positionalRandoms.computeIfAbsent(name, key -> this.random.fromHashOf(name).forkPositional()); }
/*     */ 
/*     */ 
/*     */   
/* 133 */   public NoiseRouter router() { return this.router; }
/*     */ 
/*     */ 
/*     */   
/* 137 */   public Climate.Sampler sampler() { return this.sampler; }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public SurfaceSystem surfaceSystem() { return this.surfaceSystem; }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public PositionalRandomFactory aquiferRandom() { return this.aquiferRandom; }
/*     */ 
/*     */ 
/*     */   
/* 149 */   public PositionalRandomFactory oreRandom() { return this.oreRandom; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\RandomState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */