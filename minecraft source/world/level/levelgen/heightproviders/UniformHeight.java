/*    */ package net.minecraft.world.level.levelgen.heightproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*    */ import net.minecraft.world.level.levelgen.WorldGenerationContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class UniformHeight extends HeightProvider {
/* 15 */   public static final MapCodec<UniformHeight> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(VerticalAnchor.CODEC
/* 16 */         .fieldOf("min_inclusive").forGetter(()), VerticalAnchor.CODEC
/* 17 */         .fieldOf("max_inclusive").forGetter(()))
/* 18 */       .apply(i, UniformHeight::new));
/*    */   
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger(); private final VerticalAnchor minInclusive;
/*    */   private final VerticalAnchor maxInclusive;
/*    */   private final LongSet warnedFor;
/*    */   
/*    */   private UniformHeight(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
/* 25 */     this.warnedFor = new LongOpenHashSet();
/*    */ 
/*    */     
/* 28 */     this.minInclusive = minInclusive;
/* 29 */     this.maxInclusive = maxInclusive;
/*    */   }
/*    */ 
/*    */   
/* 33 */   public static UniformHeight of(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) { return new UniformHeight(minInclusive, maxInclusive); }
/*    */ 
/*    */ 
/*    */   
/*    */   public int sample(RandomSource random, WorldGenerationContext context) {
/* 38 */     int min = this.minInclusive.resolveY(context);
/* 39 */     int max = this.maxInclusive.resolveY(context);
/* 40 */     if (min > max) {
/* 41 */       if (this.warnedFor.add(min << 32 | max)) {
/* 42 */         LOGGER.warn("Empty height range: {}", this);
/*    */       }
/* 44 */       return min;
/*    */     } 
/*    */     
/* 47 */     return Mth.randomBetweenInclusive(random, min, max);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public HeightProviderType<?> getType() { return HeightProviderType.UNIFORM; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public String toString() { return "[" + String.valueOf(this.minInclusive) + "-" + String.valueOf(this.maxInclusive) + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\heightproviders\UniformHeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */