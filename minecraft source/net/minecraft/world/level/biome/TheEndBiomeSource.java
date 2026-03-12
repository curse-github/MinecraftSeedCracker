/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.QuartPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.world.level.levelgen.DensityFunction;
/*    */ 
/*    */ public class TheEndBiomeSource extends BiomeSource {
/* 16 */   public static final MapCodec<TheEndBiomeSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 17 */         RegistryOps.retrieveElement(Biomes.THE_END), 
/* 18 */         RegistryOps.retrieveElement(Biomes.END_HIGHLANDS), 
/* 19 */         RegistryOps.retrieveElement(Biomes.END_MIDLANDS), 
/* 20 */         RegistryOps.retrieveElement(Biomes.SMALL_END_ISLANDS), 
/* 21 */         RegistryOps.retrieveElement(Biomes.END_BARRENS))
/* 22 */       .apply(i, i.stable(TheEndBiomeSource::new)));
/*    */   
/*    */   private final Holder<Biome> end;
/*    */   private final Holder<Biome> highlands;
/*    */   private final Holder<Biome> midlands;
/*    */   private final Holder<Biome> islands;
/*    */   private final Holder<Biome> barrens;
/*    */   
/*    */   public static TheEndBiomeSource create(HolderGetter<Biome> biomes) {
/* 31 */     return new TheEndBiomeSource(biomes
/* 32 */         .getOrThrow(Biomes.THE_END), biomes
/* 33 */         .getOrThrow(Biomes.END_HIGHLANDS), biomes
/* 34 */         .getOrThrow(Biomes.END_MIDLANDS), biomes
/* 35 */         .getOrThrow(Biomes.SMALL_END_ISLANDS), biomes
/* 36 */         .getOrThrow(Biomes.END_BARRENS));
/*    */   }
/*    */ 
/*    */   
/*    */   private TheEndBiomeSource(Holder<Biome> end, Holder<Biome> highlands, Holder<Biome> midlands, Holder<Biome> islands, Holder<Biome> barrens) {
/* 41 */     this.end = end;
/* 42 */     this.highlands = highlands;
/* 43 */     this.midlands = midlands;
/* 44 */     this.islands = islands;
/* 45 */     this.barrens = barrens;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   protected Stream<Holder<Biome>> collectPossibleBiomes() { return Stream.of(new Holder[] { this.end, this.highlands, this.midlands, this.islands, this.barrens }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   protected MapCodec<? extends BiomeSource> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
/* 60 */     int blockX = QuartPos.toBlock(quartX);
/* 61 */     int blockY = QuartPos.toBlock(quartY);
/* 62 */     int blockZ = QuartPos.toBlock(quartZ);
/*    */     
/* 64 */     int chunkX = SectionPos.blockToSectionCoord(blockX);
/* 65 */     int chunkZ = SectionPos.blockToSectionCoord(blockZ);
/*    */     
/* 67 */     if (chunkX * chunkX + chunkZ * chunkZ <= 4096L) {
/* 68 */       return this.end;
/*    */     }
/*    */     
/* 71 */     int weirdBlockX = (SectionPos.blockToSectionCoord(blockX) * 2 + 1) * 8;
/* 72 */     int weirdBlockZ = (SectionPos.blockToSectionCoord(blockZ) * 2 + 1) * 8;
/*    */     
/* 74 */     double heightValue = sampler.erosion().compute(new DensityFunction.SinglePointContext(weirdBlockX, blockY, weirdBlockZ));
/* 75 */     if (heightValue > 0.25D) {
/* 76 */       return this.highlands;
/*    */     }
/*    */     
/* 79 */     if (heightValue >= -0.0625D) {
/* 80 */       return this.midlands;
/*    */     }
/*    */     
/* 83 */     if (heightValue < -0.21875D) {
/* 84 */       return this.islands;
/*    */     }
/*    */     
/* 87 */     return this.barrens;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\TheEndBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */