/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.QuartPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.RandomState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*    */ 
/*    */ public final class Context<C extends FeatureConfiguration> extends Record {
/*    */   private final ChunkGenerator chunkGenerator;
/*    */   private final BiomeSource biomeSource;
/*    */   private final RandomState randomState;
/*    */   private final long seed;
/*    */   private final ChunkPos chunkPos;
/*    */   private final C config;
/*    */   private final LevelHeightAccessor heightAccessor;
/*    */   private final Predicate<Holder<Biome>> validBiome;
/*    */   private final StructureTemplateManager structureTemplateManager;
/*    */   private final RegistryAccess registryAccess;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context<TC;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context<TC;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #32	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/pieces/PieceGeneratorSupplier$Context<TC;>; }
/*    */   
/* 32 */   public Context(ChunkGenerator chunkGenerator, BiomeSource biomeSource, RandomState randomState, long seed, ChunkPos chunkPos, C config, LevelHeightAccessor heightAccessor, Predicate<Holder<Biome>> validBiome, StructureTemplateManager structureTemplateManager, RegistryAccess registryAccess) { this.chunkGenerator = chunkGenerator; this.biomeSource = biomeSource; this.randomState = randomState; this.seed = seed; this.chunkPos = chunkPos; this.config = config; this.heightAccessor = heightAccessor; this.validBiome = validBiome; this.structureTemplateManager = structureTemplateManager; this.registryAccess = registryAccess; } public ChunkGenerator chunkGenerator() { return this.chunkGenerator; } public BiomeSource biomeSource() { return this.biomeSource; } public RandomState randomState() { return this.randomState; } public long seed() { return this.seed; } public ChunkPos chunkPos() { return this.chunkPos; } public C config() { return (C)this.config; } public LevelHeightAccessor heightAccessor() { return this.heightAccessor; } public Predicate<Holder<Biome>> validBiome() { return this.validBiome; } public StructureTemplateManager structureTemplateManager() { return this.structureTemplateManager; } public RegistryAccess registryAccess() { return this.registryAccess; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean validBiomeOnTop(Heightmap.Types type) {
/* 49 */     int blockX = this.chunkPos.getMiddleBlockX();
/* 50 */     int blockZ = this.chunkPos.getMiddleBlockZ();
/* 51 */     int blockY = this.chunkGenerator.getFirstOccupiedHeight(blockX, blockZ, type, this.heightAccessor, this.randomState);
/* 52 */     Holder<Biome> biome = this.chunkGenerator.getBiomeSource().getNoiseBiome(QuartPos.fromBlock(blockX), QuartPos.fromBlock(blockY), QuartPos.fromBlock(blockZ), this.randomState.sampler());
/* 53 */     return this.validBiome.test(biome);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pieces\PieceGeneratorSupplier$Context.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */