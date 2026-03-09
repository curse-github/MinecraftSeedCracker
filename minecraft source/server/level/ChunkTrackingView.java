/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public interface ChunkTrackingView
/*     */ {
/*   9 */   public static final ChunkTrackingView EMPTY = new ChunkTrackingView()
/*     */     {
/*     */       public boolean contains(int chunkX, int chunkZ, boolean includeNeighbors) {
/*  12 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<ChunkPos> consumer) {}
/*     */     };
/*     */ 
/*     */   
/*  21 */   static ChunkTrackingView of(ChunkPos center, int radius) { return new Positioned(center, radius); }
/*     */ 
/*     */   
/*     */   static void difference(ChunkTrackingView from, ChunkTrackingView to, Consumer<ChunkPos> onEnter, Consumer<ChunkPos> onLeave) {
/*  25 */     if (from.equals(to)) {
/*     */       return;
/*     */     }
/*     */     
/*  29 */     if (from instanceof Positioned) { Positioned last = (Positioned)from; if (to instanceof Positioned) { Positioned next = (Positioned)to; if (last.squareIntersects(next)) {
/*  30 */           int minX = Math.min(last.minX(), next.minX());
/*  31 */           int minZ = Math.min(last.minZ(), next.minZ());
/*  32 */           int maxX = Math.max(last.maxX(), next.maxX());
/*  33 */           int maxZ = Math.max(last.maxZ(), next.maxZ());
/*     */           
/*  35 */           for (int x = minX; x <= maxX; x++) {
/*  36 */             for (int z = minZ; z <= maxZ; z++) {
/*  37 */               boolean saw = last.contains(x, z);
/*  38 */               boolean sees = next.contains(x, z);
/*  39 */               if (saw != sees)
/*  40 */                 if (sees) {
/*  41 */                   onEnter.accept(new ChunkPos(x, z));
/*     */                 } else {
/*  43 */                   onLeave.accept(new ChunkPos(x, z));
/*     */                 }  
/*     */             } 
/*     */           }  return;
/*     */         }  }
/*     */        }
/*  49 */      from.forEach(onLeave);
/*  50 */     to.forEach(onEnter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  55 */   default boolean contains(ChunkPos pos) { return contains(pos.x, pos.z); }
/*     */ 
/*     */ 
/*     */   
/*  59 */   default boolean contains(int x, int z) { return contains(x, z, true); }
/*     */ 
/*     */   
/*     */   boolean contains(int paramInt1, int paramInt2, boolean paramBoolean);
/*     */ 
/*     */   
/*     */   void forEach(Consumer<ChunkPos> paramConsumer);
/*     */   
/*  67 */   default boolean isInViewDistance(int chunkX, int chunkZ) { return contains(chunkX, chunkZ, false); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   static boolean isInViewDistance(int centerX, int centerZ, int viewDistance, int chunkX, int chunkZ) { return isWithinDistance(centerX, centerZ, viewDistance, chunkX, chunkZ, false); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isWithinDistance(int centerX, int centerZ, int viewDistance, int chunkX, int chunkZ, boolean includeNeighbors) {
/*  77 */     int bufferRange = includeNeighbors ? 2 : 1;
/*  78 */     long deltaX = Math.max(0, Math.abs(chunkX - centerX) - bufferRange);
/*  79 */     long deltaZ = Math.max(0, Math.abs(chunkZ - centerZ) - bufferRange);
/*     */     
/*  81 */     long distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
/*  82 */     int radiusSquared = viewDistance * viewDistance;
/*     */     
/*  84 */     return (distanceSquared < radiusSquared);
/*     */   }
/*     */   public static final class Positioned extends Record implements ChunkTrackingView { private final ChunkPos center; private final int viewDistance;
/*  87 */     public Positioned(ChunkPos center, int viewDistance) { this.center = center; this.viewDistance = viewDistance; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  87 */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned; } public ChunkPos center() { return this.center; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*  87 */       //   0	8	1	o	Ljava/lang/Object; } public int viewDistance() { return this.viewDistance; }
/*     */     
/*  89 */     private int minX() { return this.center.x - this.viewDistance - 1; }
/*     */ 
/*     */ 
/*     */     
/*  93 */     private int minZ() { return this.center.z - this.viewDistance - 1; }
/*     */ 
/*     */ 
/*     */     
/*  97 */     private int maxX() { return this.center.x + this.viewDistance + 1; }
/*     */ 
/*     */ 
/*     */     
/* 101 */     private int maxZ() { return this.center.z + this.viewDistance + 1; }
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     protected boolean squareIntersects(Positioned other) {
/* 106 */       return (minX() <= other.maxX() && 
/* 107 */         maxX() >= other.minX() && 
/* 108 */         minZ() <= other.maxZ() && 
/* 109 */         maxZ() >= other.minZ());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 114 */     public boolean contains(int chunkX, int chunkZ, boolean includeNeighbors) { return ChunkTrackingView.isWithinDistance(this.center.x, this.center.z, this.viewDistance, chunkX, chunkZ, includeNeighbors); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<ChunkPos> consumer) {
/* 119 */       for (int x = minX(); x <= maxX(); x++) {
/* 120 */         for (int z = minZ(); z <= maxZ(); z++) {
/* 121 */           if (contains(x, z))
/* 122 */             consumer.accept(new ChunkPos(x, z)); 
/*     */         } 
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkTrackingView.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */