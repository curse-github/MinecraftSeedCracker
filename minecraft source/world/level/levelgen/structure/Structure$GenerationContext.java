/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GenerationContext
/*     */   extends Record
/*     */ {
/*     */   private final RegistryAccess registryAccess;
/*     */   private final ChunkGenerator chunkGenerator;
/*     */   private final BiomeSource biomeSource;
/*     */   private final RandomState randomState;
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   private final WorldgenRandom random;
/*     */   private final long seed;
/*     */   private final ChunkPos chunkPos;
/*     */   private final LevelHeightAccessor heightAccessor;
/*     */   private final Predicate<Holder<Biome>> validBiome;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 138 */   public GenerationContext(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, WorldgenRandom random, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) { this.registryAccess = registryAccess; this.chunkGenerator = chunkGenerator; this.biomeSource = biomeSource; this.randomState = randomState; this.structureTemplateManager = structureTemplateManager; this.random = random; this.seed = seed; this.chunkPos = chunkPos; this.heightAccessor = heightAccessor; this.validBiome = validBiome; } public RegistryAccess registryAccess() { return this.registryAccess; } public ChunkGenerator chunkGenerator() { return this.chunkGenerator; } public BiomeSource biomeSource() { return this.biomeSource; } public RandomState randomState() { return this.randomState; } public StructureTemplateManager structureTemplateManager() { return this.structureTemplateManager; } public WorldgenRandom random() { return this.random; } public long seed() { return this.seed; } public ChunkPos chunkPos() { return this.chunkPos; } public LevelHeightAccessor heightAccessor() { return this.heightAccessor; } public Predicate<Holder<Biome>> validBiome() { return this.validBiome; }
/*     */   
/* 140 */   public GenerationContext(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkPos chunkPos, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome) { this(registryAccess, chunkGenerator, biomeSource, randomState, structureTemplateManager, makeRandom(seed, chunkPos), seed, chunkPos, heightAccessor, validBiome); }
/*     */ 
/*     */   
/*     */   private static WorldgenRandom makeRandom(long seed, ChunkPos chunkPos) {
/* 144 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
/* 145 */     random.setLargeFeatureSeed(seed, chunkPos.x, chunkPos.z);
/* 146 */     return random;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\Structure$GenerationContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */