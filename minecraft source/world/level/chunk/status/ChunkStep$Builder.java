/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*     */   private final ChunkStatus status;
/*     */   private final ChunkStep parent;
/*     */   private ChunkStatus[] directDependenciesByRadius;
/*  54 */   private int blockStateWriteRadius = -1;
/*  55 */   private ChunkStatusTask task = ChunkStatusTasks::passThrough;
/*     */   
/*     */   protected Builder(ChunkStatus status) {
/*  58 */     if (status.getParent() != status) {
/*  59 */       throw new IllegalArgumentException("Not starting with the first status: " + String.valueOf(status));
/*     */     }
/*  61 */     this.status = status;
/*  62 */     this.parent = null;
/*  63 */     this.directDependenciesByRadius = new ChunkStatus[0];
/*     */   }
/*     */   
/*     */   protected Builder(ChunkStatus status, ChunkStep parent) {
/*  67 */     if (parent.targetStatus.getIndex() != status.getIndex() - 1) {
/*  68 */       throw new IllegalArgumentException("Out of order status: " + String.valueOf(status));
/*     */     }
/*  70 */     this.status = status;
/*  71 */     this.parent = parent;
/*  72 */     this.directDependenciesByRadius = new ChunkStatus[] { parent.targetStatus };
/*     */   }
/*     */   
/*     */   public Builder addRequirement(ChunkStatus status, int radius) {
/*  76 */     if (status.isOrAfter(this.status)) {
/*  77 */       throw new IllegalArgumentException("Status " + String.valueOf(status) + " can not be required by " + String.valueOf(this.status));
/*     */     }
/*  79 */     ChunkStatus[] previous = this.directDependenciesByRadius;
/*  80 */     int newLength = radius + 1;
/*  81 */     if (newLength > previous.length) {
/*  82 */       this.directDependenciesByRadius = new ChunkStatus[newLength];
/*  83 */       Arrays.fill(this.directDependenciesByRadius, status);
/*     */     } 
/*  85 */     for (int i = 0; i < Math.min(newLength, previous.length); i++) {
/*  86 */       this.directDependenciesByRadius[i] = ChunkStatus.max(previous[i], status);
/*     */     }
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public Builder blockStateWriteRadius(int radius) {
/*  92 */     this.blockStateWriteRadius = radius;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setTask(ChunkStatusTask task) {
/*  97 */     this.task = task;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 102 */   public ChunkStep build() { return new ChunkStep(this.status, new ChunkDependencies(ImmutableList.copyOf(this.directDependenciesByRadius)), new ChunkDependencies(ImmutableList.copyOf(buildAccumulatedDependencies())), this.blockStateWriteRadius, this.task); }
/*     */ 
/*     */   
/*     */   private ChunkStatus[] buildAccumulatedDependencies() {
/* 106 */     if (this.parent == null) {
/* 107 */       return this.directDependenciesByRadius;
/*     */     }
/* 109 */     int radiusOfParent = getRadiusOfParent(this.parent.targetStatus);
/* 110 */     ChunkDependencies parentDependencies = this.parent.accumulatedDependencies;
/* 111 */     ChunkStatus[] accumulatedDependencies = new ChunkStatus[Math.max(radiusOfParent + parentDependencies.size(), this.directDependenciesByRadius.length)];
/* 112 */     for (int distance = 0; distance < accumulatedDependencies.length; distance++) {
/* 113 */       int distanceInParent = distance - radiusOfParent;
/* 114 */       if (distanceInParent < 0 || distanceInParent >= parentDependencies.size()) {
/* 115 */         accumulatedDependencies[distance] = this.directDependenciesByRadius[distance];
/* 116 */       } else if (distance >= this.directDependenciesByRadius.length) {
/* 117 */         accumulatedDependencies[distance] = parentDependencies.get(distanceInParent);
/*     */       } else {
/* 119 */         accumulatedDependencies[distance] = ChunkStatus.max(this.directDependenciesByRadius[distance], parentDependencies.get(distanceInParent));
/*     */       } 
/*     */     } 
/* 122 */     return accumulatedDependencies;
/*     */   }
/*     */   
/*     */   private int getRadiusOfParent(ChunkStatus status) {
/* 126 */     for (int i = this.directDependenciesByRadius.length - 1; i >= 0; i--) {
/* 127 */       if (this.directDependenciesByRadius[i].isOrAfter(status)) {
/* 128 */         return i;
/*     */       }
/*     */     } 
/* 131 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\status\ChunkStep$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */