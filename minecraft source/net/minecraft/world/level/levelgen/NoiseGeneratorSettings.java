/*     */ package net.minecraft.world.level.levelgen;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.SurfaceRuleData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public final class NoiseGeneratorSettings extends Record {
/*     */   private final NoiseSettings noiseSettings;
/*     */   private final BlockState defaultBlock;
/*     */   private final BlockState defaultFluid;
/*     */   private final NoiseRouter noiseRouter;
/*     */   private final SurfaceRules.RuleSource surfaceRule;
/*     */   
/*  21 */   public NoiseGeneratorSettings(NoiseSettings noiseSettings, BlockState defaultBlock, BlockState defaultFluid, NoiseRouter noiseRouter, SurfaceRules.RuleSource surfaceRule, List<Climate.ParameterPoint> spawnTarget, int seaLevel, boolean disableMobGeneration, boolean aquifersEnabled, boolean oreVeinsEnabled, boolean useLegacyRandomSource) { this.noiseSettings = noiseSettings; this.defaultBlock = defaultBlock; this.defaultFluid = defaultFluid; this.noiseRouter = noiseRouter; this.surfaceRule = surfaceRule; this.spawnTarget = spawnTarget; this.seaLevel = seaLevel; this.disableMobGeneration = disableMobGeneration; this.aquifersEnabled = aquifersEnabled; this.oreVeinsEnabled = oreVeinsEnabled; this.useLegacyRandomSource = useLegacyRandomSource; } private final List<Climate.ParameterPoint> spawnTarget; private final int seaLevel; private final boolean disableMobGeneration; private final boolean aquifersEnabled; private final boolean oreVeinsEnabled; private final boolean useLegacyRandomSource; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #21	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;
/*  21 */     //   0	8	1	o	Ljava/lang/Object; } public NoiseSettings noiseSettings() { return this.noiseSettings; } public BlockState defaultBlock() { return this.defaultBlock; } public BlockState defaultFluid() { return this.defaultFluid; } public NoiseRouter noiseRouter() { return this.noiseRouter; } public SurfaceRules.RuleSource surfaceRule() { return this.surfaceRule; } public List<Climate.ParameterPoint> spawnTarget() { return this.spawnTarget; } public int seaLevel() { return this.seaLevel; } public boolean aquifersEnabled() { return this.aquifersEnabled; } public boolean useLegacyRandomSource() { return this.useLegacyRandomSource; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  34 */   public static final Codec<NoiseGeneratorSettings> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(NoiseSettings.CODEC
/*  35 */         .fieldOf("noise").forGetter(NoiseGeneratorSettings::noiseSettings), BlockState.CODEC
/*  36 */         .fieldOf("default_block").forGetter(NoiseGeneratorSettings::defaultBlock), BlockState.CODEC
/*  37 */         .fieldOf("default_fluid").forGetter(NoiseGeneratorSettings::defaultFluid), NoiseRouter.CODEC
/*  38 */         .fieldOf("noise_router").forGetter(NoiseGeneratorSettings::noiseRouter), SurfaceRules.RuleSource.CODEC
/*  39 */         .fieldOf("surface_rule").forGetter(NoiseGeneratorSettings::surfaceRule), Climate.ParameterPoint.CODEC
/*  40 */         .listOf().fieldOf("spawn_target").forGetter(NoiseGeneratorSettings::spawnTarget), Codec.INT
/*  41 */         .fieldOf("sea_level").forGetter(NoiseGeneratorSettings::seaLevel), Codec.BOOL
/*  42 */         .fieldOf("disable_mob_generation").forGetter(NoiseGeneratorSettings::disableMobGeneration), Codec.BOOL
/*  43 */         .fieldOf("aquifers_enabled").forGetter(NoiseGeneratorSettings::isAquifersEnabled), Codec.BOOL
/*  44 */         .fieldOf("ore_veins_enabled").forGetter(NoiseGeneratorSettings::oreVeinsEnabled), Codec.BOOL
/*  45 */         .fieldOf("legacy_random_source").forGetter(NoiseGeneratorSettings::useLegacyRandomSource))
/*  46 */       .apply(i, NoiseGeneratorSettings::new));
/*     */   
/*  48 */   public static final Codec<Holder<NoiseGeneratorSettings>> CODEC = RegistryFileCodec.create(Registries.NOISE_SETTINGS, DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  53 */   public boolean disableMobGeneration() { return this.disableMobGeneration; }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public boolean isAquifersEnabled() { return (this.aquifersEnabled && !SharedConstants.DEBUG_DISABLE_AQUIFERS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public boolean oreVeinsEnabled() { return (this.oreVeinsEnabled && !SharedConstants.DEBUG_DISABLE_ORE_VEINS); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public WorldgenRandom.Algorithm getRandomSource() { return this.useLegacyRandomSource ? WorldgenRandom.Algorithm.LEGACY : WorldgenRandom.Algorithm.XOROSHIRO; }
/*     */ 
/*     */   
/*  69 */   public static final ResourceKey<NoiseGeneratorSettings> OVERWORLD = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("overworld"));
/*  70 */   public static final ResourceKey<NoiseGeneratorSettings> LARGE_BIOMES = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("large_biomes"));
/*  71 */   public static final ResourceKey<NoiseGeneratorSettings> AMPLIFIED = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("amplified"));
/*  72 */   public static final ResourceKey<NoiseGeneratorSettings> NETHER = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("nether"));
/*  73 */   public static final ResourceKey<NoiseGeneratorSettings> END = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("end"));
/*  74 */   public static final ResourceKey<NoiseGeneratorSettings> CAVES = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("caves"));
/*  75 */   public static final ResourceKey<NoiseGeneratorSettings> FLOATING_ISLANDS = ResourceKey.create(Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("floating_islands"));
/*     */   
/*     */   public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
/*  78 */     context.register(OVERWORLD, overworld(context, false, false));
/*  79 */     context.register(LARGE_BIOMES, overworld(context, false, true));
/*  80 */     context.register(AMPLIFIED, overworld(context, true, false));
/*  81 */     context.register(NETHER, nether(context));
/*  82 */     context.register(END, end(context));
/*  83 */     context.register(CAVES, caves(context));
/*  84 */     context.register(FLOATING_ISLANDS, floatingIslands(context));
/*     */   }
/*     */   
/*     */   private static NoiseGeneratorSettings end(BootstrapContext<?> context) {
/*  88 */     return new NoiseGeneratorSettings(NoiseSettings.END_NOISE_SETTINGS, Blocks.END_STONE
/*     */         
/*  90 */         .defaultBlockState(), Blocks.AIR
/*  91 */         .defaultBlockState(), 
/*  92 */         NoiseRouterData.end(context.lookup(Registries.DENSITY_FUNCTION)), 
/*  93 */         SurfaceRuleData.end(), 
/*  94 */         List.of(), 0, true, false, false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NoiseGeneratorSettings nether(BootstrapContext<?> context) {
/* 104 */     return new NoiseGeneratorSettings(NoiseSettings.NETHER_NOISE_SETTINGS, Blocks.NETHERRACK
/*     */         
/* 106 */         .defaultBlockState(), Blocks.LAVA
/* 107 */         .defaultBlockState(), 
/* 108 */         NoiseRouterData.nether(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE)), 
/* 109 */         SurfaceRuleData.nether(), 
/* 110 */         List.of(), 32, false, false, false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NoiseGeneratorSettings overworld(BootstrapContext<?> context, boolean isAmplified, boolean largeBiomes) {
/* 120 */     return new NoiseGeneratorSettings(NoiseSettings.OVERWORLD_NOISE_SETTINGS, Blocks.STONE
/*     */         
/* 122 */         .defaultBlockState(), Blocks.WATER
/* 123 */         .defaultBlockState(), 
/* 124 */         NoiseRouterData.overworld(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE), largeBiomes, isAmplified), 
/* 125 */         SurfaceRuleData.overworld(), (new OverworldBiomeBuilder())
/* 126 */         .spawnTarget(), 63, false, true, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NoiseGeneratorSettings caves(BootstrapContext<?> context) {
/* 136 */     return new NoiseGeneratorSettings(NoiseSettings.CAVES_NOISE_SETTINGS, Blocks.STONE
/*     */         
/* 138 */         .defaultBlockState(), Blocks.WATER
/* 139 */         .defaultBlockState(), 
/* 140 */         NoiseRouterData.caves(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE)), 
/* 141 */         SurfaceRuleData.overworldLike(false, true, true), 
/* 142 */         List.of(), 32, false, false, false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static NoiseGeneratorSettings floatingIslands(BootstrapContext<?> context) {
/* 152 */     return new NoiseGeneratorSettings(NoiseSettings.FLOATING_ISLANDS_NOISE_SETTINGS, Blocks.STONE
/*     */         
/* 154 */         .defaultBlockState(), Blocks.WATER
/* 155 */         .defaultBlockState(), 
/* 156 */         NoiseRouterData.floatingIslands(context.lookup(Registries.DENSITY_FUNCTION), context.lookup(Registries.NOISE)), 
/* 157 */         SurfaceRuleData.overworldLike(false, false, false), 
/* 158 */         List.of(), -64, false, false, false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NoiseGeneratorSettings dummy() {
/* 168 */     return new NoiseGeneratorSettings(NoiseSettings.OVERWORLD_NOISE_SETTINGS, Blocks.STONE
/*     */         
/* 170 */         .defaultBlockState(), Blocks.AIR
/* 171 */         .defaultBlockState(), 
/* 172 */         NoiseRouterData.none(), 
/* 173 */         SurfaceRuleData.air(), 
/* 174 */         List.of(), 63, true, false, false, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseGeneratorSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */