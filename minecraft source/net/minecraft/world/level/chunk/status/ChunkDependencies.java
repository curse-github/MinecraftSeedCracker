/*    */ package net.minecraft.world.level.chunk.status;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.Locale;
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
/*    */ public final class ChunkDependencies
/*    */ {
/*    */   private final ImmutableList<ChunkStatus> dependencyByRadius;
/*    */   private final int[] radiusByDependency;
/*    */   
/*    */   public ChunkDependencies(ImmutableList<ChunkStatus> dependencyByRadius) {
/* 22 */     this.dependencyByRadius = dependencyByRadius;
/* 23 */     int size = dependencyByRadius.isEmpty() ? 0 : (((ChunkStatus)dependencyByRadius.getFirst()).getIndex() + 1);
/* 24 */     this.radiusByDependency = new int[size];
/* 25 */     for (int radius = 0; radius < dependencyByRadius.size(); radius++) {
/* 26 */       ChunkStatus dependency = (ChunkStatus)dependencyByRadius.get(radius);
/* 27 */       int index = dependency.getIndex();
/* 28 */       for (int statusIndex = 0; statusIndex <= index; statusIndex++) {
/* 29 */         this.radiusByDependency[statusIndex] = radius;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/* 36 */   public ImmutableList<ChunkStatus> asList() { return this.dependencyByRadius; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public int size() { return this.dependencyByRadius.size(); }
/*    */ 
/*    */   
/*    */   public int getRadiusOf(ChunkStatus status) {
/* 44 */     int index = status.getIndex();
/* 45 */     if (index >= this.radiusByDependency.length) {
/* 46 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Requesting a ChunkStatus(%s) outside of dependency range(%s)", new Object[] { status, this.dependencyByRadius }));
/*    */     }
/* 48 */     return this.radiusByDependency[index];
/*    */   }
/*    */ 
/*    */   
/* 52 */   public int getRadius() { return Math.max(0, this.dependencyByRadius.size() - 1); }
/*    */ 
/*    */ 
/*    */   
/* 56 */   public ChunkStatus get(int distance) { return (ChunkStatus)this.dependencyByRadius.get(distance); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public String toString() { return this.dependencyByRadius.toString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\status\ChunkDependencies.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */