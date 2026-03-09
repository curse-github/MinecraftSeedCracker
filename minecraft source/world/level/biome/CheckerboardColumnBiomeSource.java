/*    */ package net.minecraft.world.level.biome;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ 
/*    */ public class CheckerboardColumnBiomeSource extends BiomeSource {
/* 12 */   public static final MapCodec<CheckerboardColumnBiomeSource> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Biome.LIST_CODEC
/* 13 */         .fieldOf("biomes").forGetter(()), 
/* 14 */         Codec.intRange(0, 62).fieldOf("scale").orElse(Integer.valueOf(2)).forGetter(()))
/* 15 */       .apply(i, CheckerboardColumnBiomeSource::new));
/*    */   
/*    */   private final HolderSet<Biome> allowedBiomes;
/*    */   private final int bitShift;
/*    */   private final int size;
/*    */   
/*    */   public CheckerboardColumnBiomeSource(HolderSet<Biome> allowedBiomes, int size) {
/* 22 */     this.allowedBiomes = allowedBiomes;
/* 23 */     this.bitShift = size + 2;
/* 24 */     this.size = size;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   protected Stream<Holder<Biome>> collectPossibleBiomes() { return this.allowedBiomes.stream(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   protected MapCodec<? extends BiomeSource> codec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) { return this.allowedBiomes.get(Math.floorMod((quartX >> this.bitShift) + (quartZ >> this.bitShift), this.allowedBiomes.size())); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\CheckerboardColumnBiomeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */