/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
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
/*     */ class SpikeCacheLoader
/*     */   extends CacheLoader<Long, List<SpikeFeature.EndSpike>>
/*     */ {
/*     */   public List<SpikeFeature.EndSpike> load(Long seed) {
/* 179 */     IntArrayList sizes = Util.toShuffledList(IntStream.range(0, 10), RandomSource.create(seed.longValue()));
/*     */     
/* 181 */     List<SpikeFeature.EndSpike> result = Lists.newArrayList();
/* 182 */     for (int i = 0; i < 10; i++) {
/* 183 */       int x = Mth.floor(42.0D * Math.cos(2.0D * (-3.141592653589793D + 0.3141592653589793D * i)));
/* 184 */       int z = Mth.floor(42.0D * Math.sin(2.0D * (-3.141592653589793D + 0.3141592653589793D * i)));
/* 185 */       int size = sizes.get(i).intValue();
/* 186 */       int radius = 2 + size / 3;
/* 187 */       int height = 76 + size * 3;
/* 188 */       boolean guarded = (size == 1 || size == 2);
/* 189 */       result.add(new SpikeFeature.EndSpike(x, z, radius, height, guarded));
/*     */     } 
/* 191 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SpikeFeature$SpikeCacheLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */