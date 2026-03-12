/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ 
/*    */ public class WeightedListHeight extends HeightProvider {
/* 10 */   public static final MapCodec<WeightedListHeight> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 11 */         WeightedList.nonEmptyCodec(HeightProvider.CODEC).fieldOf("distribution").forGetter(()))
/* 12 */       .apply(i, WeightedListHeight::new));
/*    */   
/*    */   private final WeightedList<HeightProvider> distribution;
/*    */ 
/*    */   
/* 17 */   public WeightedListHeight(WeightedList<HeightProvider> distribution) { this.distribution = distribution; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public int sample(RandomSource random, WorldGenerationContext heightAccessor) { return ((HeightProvider)this.distribution.getRandomOrThrow(random)).sample(random, heightAccessor); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public HeightProviderType<?> getType() { return HeightProviderType.WEIGHTED_LIST; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\heightproviders\WeightedListHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */