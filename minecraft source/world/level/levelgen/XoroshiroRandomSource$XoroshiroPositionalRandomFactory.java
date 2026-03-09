/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
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
/*     */ public class XoroshiroPositionalRandomFactory
/*     */   implements PositionalRandomFactory
/*     */ {
/*     */   private final long seedLo;
/*     */   private final long seedHi;
/*     */   
/*     */   public XoroshiroPositionalRandomFactory(long seedLo, long seedHi) {
/* 137 */     this.seedLo = seedLo;
/* 138 */     this.seedHi = seedHi;
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource at(int x, int y, int z) {
/* 143 */     long positionalSeed = Mth.getSeed(x, y, z);
/* 144 */     long randomSeed = positionalSeed ^ this.seedLo;
/* 145 */     return new XoroshiroRandomSource(randomSeed, this.seedHi);
/*     */   }
/*     */ 
/*     */   
/*     */   public RandomSource fromHashOf(String name) {
/* 150 */     RandomSupport.Seed128bit seed = RandomSupport.seedFromHashOf(name);
/* 151 */     return new XoroshiroRandomSource(seed.xor(this.seedLo, this.seedHi));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 156 */   public RandomSource fromSeed(long seed) { return new XoroshiroRandomSource(seed ^ this.seedLo, seed ^ this.seedHi); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 162 */   public void parityConfigString(StringBuilder sb) { sb.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\XoroshiroRandomSource$XoroshiroPositionalRandomFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */