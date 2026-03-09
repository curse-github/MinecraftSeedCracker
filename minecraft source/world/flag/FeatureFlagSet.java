/*     */ package net.minecraft.world.flag;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ public final class FeatureFlagSet
/*     */ {
/*  10 */   private static final FeatureFlagSet EMPTY = new FeatureFlagSet(null, 0L);
/*     */   
/*     */   public static final int MAX_CONTAINER_SIZE = 64;
/*     */   
/*     */   private final FeatureFlagUniverse universe;
/*     */   private final long mask;
/*     */   
/*     */   private FeatureFlagSet(FeatureFlagUniverse universe, long mask) {
/*  18 */     this.universe = universe;
/*  19 */     this.mask = mask;
/*     */   }
/*     */ 
/*     */   
/*     */   static FeatureFlagSet create(FeatureFlagUniverse universe, Collection<FeatureFlag> flags) {
/*  24 */     if (flags.isEmpty()) {
/*  25 */       return EMPTY;
/*     */     }
/*  27 */     long mask = computeMask(universe, 0L, flags);
/*  28 */     return new FeatureFlagSet(universe, mask);
/*     */   }
/*     */ 
/*     */   
/*  32 */   public static FeatureFlagSet of() { return EMPTY; }
/*     */ 
/*     */ 
/*     */   
/*  36 */   public static FeatureFlagSet of(FeatureFlag flag) { return new FeatureFlagSet(flag.universe, flag.mask); }
/*     */ 
/*     */   
/*     */   public static FeatureFlagSet of(FeatureFlag flag, FeatureFlag... flags) {
/*  40 */     long mask = (flags.length == 0) ? flag.mask : computeMask(flag.universe, flag.mask, Arrays.asList(flags));
/*  41 */     return new FeatureFlagSet(flag.universe, mask);
/*     */   }
/*     */   
/*     */   private static long computeMask(FeatureFlagUniverse universe, long mask, Iterable<FeatureFlag> flags) {
/*  45 */     for (FeatureFlag f : flags) {
/*  46 */       if (universe != f.universe) {
/*  47 */         throw new IllegalStateException("Mismatched feature universe, expected '" + String.valueOf(universe) + "', but got '" + String.valueOf(f.universe) + "'");
/*     */       }
/*  49 */       mask |= f.mask;
/*     */     } 
/*  51 */     return mask;
/*     */   }
/*     */   
/*     */   public boolean contains(FeatureFlag flag) {
/*  55 */     if (this.universe != flag.universe) {
/*  56 */       return false;
/*     */     }
/*  58 */     return ((this.mask & flag.mask) != 0L);
/*     */   }
/*     */ 
/*     */   
/*  62 */   public boolean isEmpty() { return equals(EMPTY); }
/*     */ 
/*     */   
/*     */   public boolean isSubsetOf(FeatureFlagSet set) {
/*  66 */     if (this.universe == null) {
/*  67 */       return true;
/*     */     }
/*  69 */     if (this.universe != set.universe) {
/*  70 */       return false;
/*     */     }
/*  72 */     return ((this.mask & (set.mask ^ 0xFFFFFFFFFFFFFFFFL)) == 0L);
/*     */   }
/*     */   
/*     */   public boolean intersects(FeatureFlagSet set) {
/*  76 */     if (this.universe == null || set.universe == null || this.universe != set.universe) {
/*  77 */       return false;
/*     */     }
/*  79 */     return ((this.mask & set.mask) != 0L);
/*     */   }
/*     */   
/*     */   public FeatureFlagSet join(FeatureFlagSet other) {
/*  83 */     if (this.universe == null) {
/*  84 */       return other;
/*     */     }
/*  86 */     if (other.universe == null) {
/*  87 */       return this;
/*     */     }
/*  89 */     if (this.universe != other.universe) {
/*  90 */       throw new IllegalArgumentException("Mismatched set elements: '" + String.valueOf(this.universe) + "' != '" + String.valueOf(other.universe) + "'");
/*     */     }
/*  92 */     return new FeatureFlagSet(this.universe, this.mask | other.mask);
/*     */   }
/*     */   
/*     */   public FeatureFlagSet subtract(FeatureFlagSet other) {
/*  96 */     if (this.universe == null || other.universe == null) {
/*  97 */       return this;
/*     */     }
/*  99 */     if (this.universe != other.universe) {
/* 100 */       throw new IllegalArgumentException("Mismatched set elements: '" + String.valueOf(this.universe) + "' != '" + String.valueOf(other.universe) + "'");
/*     */     }
/* 102 */     long newMask = this.mask & (other.mask ^ 0xFFFFFFFFFFFFFFFFL);
/* 103 */     if (newMask == 0L) {
/* 104 */       return EMPTY;
/*     */     }
/* 106 */     return new FeatureFlagSet(this.universe, newMask);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 111 */     if (this == o) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (o instanceof FeatureFlagSet) { FeatureFlagSet that = (FeatureFlagSet)o; if (this.universe == that.universe && this.mask == that.mask); }  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public int hashCode() { return (int)HashCommon.mix(this.mask); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\flag\FeatureFlagSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */