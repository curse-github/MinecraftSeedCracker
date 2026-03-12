/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LegacyPositionalRandomFactory
/*    */   implements PositionalRandomFactory
/*    */ {
/*    */   private final long seed;
/*    */   
/* 64 */   public LegacyPositionalRandomFactory(long seed) { this.seed = seed; }
/*    */ 
/*    */ 
/*    */   
/*    */   public RandomSource at(int x, int y, int z) {
/* 69 */     long positionalSeed = Mth.getSeed(x, y, z);
/* 70 */     long randomSeed = positionalSeed ^ this.seed;
/* 71 */     return new LegacyRandomSource(randomSeed);
/*    */   }
/*    */ 
/*    */   
/*    */   public RandomSource fromHashOf(String name) {
/* 76 */     int positionalSeed = name.hashCode();
/* 77 */     return new LegacyRandomSource(positionalSeed ^ this.seed);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 82 */   public RandomSource fromSeed(long seed) { return new LegacyRandomSource(seed); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 88 */   public void parityConfigString(StringBuilder sb) { sb.append("LegacyPositionalRandomFactory{").append(this.seed).append("}"); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\LegacyRandomSource$LegacyPositionalRandomFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */