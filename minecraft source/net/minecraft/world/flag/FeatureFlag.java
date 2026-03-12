/*   */ package net.minecraft.world.flag;
/*   */ 
/*   */ public class FeatureFlag {
/*   */   final FeatureFlagUniverse universe;
/*   */   final long mask;
/*   */   
/*   */   FeatureFlag(FeatureFlagUniverse universe, int bit) {
/* 8 */     this.universe = universe;
/* 9 */     this.mask = 1L << bit;
/*   */   }
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\flag\FeatureFlag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */