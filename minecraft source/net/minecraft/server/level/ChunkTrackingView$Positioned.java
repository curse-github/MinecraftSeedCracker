/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Positioned
/*     */   extends Record
/*     */   implements ChunkTrackingView
/*     */ {
/*     */   private final ChunkPos center;
/*     */   private final int viewDistance;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkTrackingView$Positioned;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  87 */   public Positioned(ChunkPos center, int viewDistance) { this.center = center; this.viewDistance = viewDistance; } public ChunkPos center() { return this.center; } public int viewDistance() { return this.viewDistance; }
/*     */   
/*  89 */   private int minX() { return this.center.x - this.viewDistance - 1; }
/*     */ 
/*     */ 
/*     */   
/*  93 */   private int minZ() { return this.center.z - this.viewDistance - 1; }
/*     */ 
/*     */ 
/*     */   
/*  97 */   private int maxX() { return this.center.x + this.viewDistance + 1; }
/*     */ 
/*     */ 
/*     */   
/* 101 */   private int maxZ() { return this.center.z + this.viewDistance + 1; }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   protected boolean squareIntersects(Positioned other) {
/* 106 */     return (minX() <= other.maxX() && 
/* 107 */       maxX() >= other.minX() && 
/* 108 */       minZ() <= other.maxZ() && 
/* 109 */       maxZ() >= other.minZ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public boolean contains(int chunkX, int chunkZ, boolean includeNeighbors) { return ChunkTrackingView.isWithinDistance(this.center.x, this.center.z, this.viewDistance, chunkX, chunkZ, includeNeighbors); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<ChunkPos> consumer) {
/* 119 */     for (int x = minX(); x <= maxX(); x++) {
/* 120 */       for (int z = minZ(); z <= maxZ(); z++) {
/* 121 */         if (contains(x, z))
/* 122 */           consumer.accept(new ChunkPos(x, z)); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkTrackingView$Positioned.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */