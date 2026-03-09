/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements DensityFunction.Visitor
/*     */ {
/*  95 */   private final Map<DensityFunction, DensityFunction> wrapped = new HashMap();
/*     */   
/*     */   private DensityFunction wrapNew(DensityFunction function) {
/*  98 */     if (function instanceof DensityFunctions.HolderHolder) { DensityFunctions.HolderHolder holder = (DensityFunctions.HolderHolder)function;
/*  99 */       return (DensityFunction)holder.function().value(); }
/*     */     
/* 101 */     if (function instanceof DensityFunctions.Marker) { DensityFunctions.Marker marker = (DensityFunctions.Marker)function;
/* 102 */       return marker.wrapped(); }
/*     */     
/* 104 */     return function;
/*     */   }
/*     */   
/*     */   null(RandomState this$0) {}
/*     */   
/* 109 */   public DensityFunction apply(DensityFunction input) { return (DensityFunction)this.wrapped.computeIfAbsent(input, this::wrapNew); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\RandomState$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */