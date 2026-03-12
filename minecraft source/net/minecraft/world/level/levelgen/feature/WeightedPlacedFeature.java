/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class WeightedPlacedFeature {
/* 13 */   public static final Codec<WeightedPlacedFeature> CODEC = RecordCodecBuilder.create(i -> i.group(PlacedFeature.CODEC
/* 14 */         .fieldOf("feature").forGetter(()), 
/* 15 */         Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(()))
/* 16 */       .apply(i, WeightedPlacedFeature::new));
/*    */   
/*    */   public final Holder<PlacedFeature> feature;
/*    */   public final float chance;
/*    */   
/*    */   public WeightedPlacedFeature(Holder<PlacedFeature> feature, float chance) {
/* 22 */     this.feature = feature;
/* 23 */     this.chance = chance;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) { return ((PlacedFeature)this.feature.value()).place(level, chunkGenerator, random, origin); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\WeightedPlacedFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */