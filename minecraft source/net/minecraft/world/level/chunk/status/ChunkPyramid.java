/*     */ package net.minecraft.world.level.chunk.status;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ 
/*     */ public final class ChunkPyramid extends Record {
/*     */   private final ImmutableList<ChunkStep> steps;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/status/ChunkPyramid;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/ChunkPyramid; }
/*     */   
/*   9 */   public ChunkPyramid(ImmutableList<ChunkStep> steps) { this.steps = steps; } public ImmutableList<ChunkStep> steps() { return this.steps; }
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/status/ChunkPyramid;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/status/ChunkPyramid; }
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/status/ChunkPyramid;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/status/ChunkPyramid;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*  12 */   public static final ChunkPyramid GENERATION_PYRAMID = (new Builder())
/*  13 */     .step(ChunkStatus.EMPTY, s -> s)
/*  14 */     .step(ChunkStatus.STRUCTURE_STARTS, s -> s
/*  15 */       .setTask(ChunkStatusTasks::generateStructureStarts))
/*     */     
/*  17 */     .step(ChunkStatus.STRUCTURE_REFERENCES, s -> s
/*  18 */       .addRequirement(ChunkStatus.STRUCTURE_STARTS, 8)
/*  19 */       .setTask(ChunkStatusTasks::generateStructureReferences))
/*     */     
/*  21 */     .step(ChunkStatus.BIOMES, s -> s
/*  22 */       .addRequirement(ChunkStatus.STRUCTURE_STARTS, 8)
/*  23 */       .setTask(ChunkStatusTasks::generateBiomes))
/*     */     
/*  25 */     .step(ChunkStatus.NOISE, s -> s
/*  26 */       .addRequirement(ChunkStatus.STRUCTURE_STARTS, 8)
/*  27 */       .addRequirement(ChunkStatus.BIOMES, 1)
/*  28 */       .blockStateWriteRadius(0)
/*  29 */       .setTask(ChunkStatusTasks::generateNoise))
/*     */     
/*  31 */     .step(ChunkStatus.SURFACE, s -> s
/*  32 */       .addRequirement(ChunkStatus.STRUCTURE_STARTS, 8)
/*  33 */       .addRequirement(ChunkStatus.BIOMES, 1)
/*  34 */       .blockStateWriteRadius(0)
/*  35 */       .setTask(ChunkStatusTasks::generateSurface))
/*     */     
/*  37 */     .step(ChunkStatus.CARVERS, s -> s
/*  38 */       .addRequirement(ChunkStatus.STRUCTURE_STARTS, 8)
/*  39 */       .blockStateWriteRadius(0)
/*  40 */       .setTask(ChunkStatusTasks::generateCarvers))
/*     */     
/*  42 */     .step(ChunkStatus.FEATURES, s -> s
/*  43 */       .addRequirement(ChunkStatus.STRUCTURE_STARTS, 8)
/*  44 */       .addRequirement(ChunkStatus.CARVERS, 1)
/*  45 */       .blockStateWriteRadius(1)
/*  46 */       .setTask(ChunkStatusTasks::generateFeatures))
/*     */     
/*  48 */     .step(ChunkStatus.INITIALIZE_LIGHT, s -> s
/*  49 */       .setTask(ChunkStatusTasks::initializeLight))
/*     */     
/*  51 */     .step(ChunkStatus.LIGHT, s -> s
/*  52 */       .addRequirement(ChunkStatus.INITIALIZE_LIGHT, 1)
/*  53 */       .setTask(ChunkStatusTasks::light))
/*     */     
/*  55 */     .step(ChunkStatus.SPAWN, s -> s
/*  56 */       .addRequirement(ChunkStatus.BIOMES, 1)
/*  57 */       .setTask(ChunkStatusTasks::generateSpawn))
/*     */     
/*  59 */     .step(ChunkStatus.FULL, s -> s
/*  60 */       .setTask(ChunkStatusTasks::full))
/*     */     
/*  62 */     .build();
/*     */   
/*  64 */   public static final ChunkPyramid LOADING_PYRAMID = (new Builder())
/*  65 */     .step(ChunkStatus.EMPTY, s -> s)
/*  66 */     .step(ChunkStatus.STRUCTURE_STARTS, s -> s
/*  67 */       .setTask(ChunkStatusTasks::loadStructureStarts))
/*     */     
/*  69 */     .step(ChunkStatus.STRUCTURE_REFERENCES, s -> s)
/*  70 */     .step(ChunkStatus.BIOMES, s -> s)
/*  71 */     .step(ChunkStatus.NOISE, s -> s)
/*  72 */     .step(ChunkStatus.SURFACE, s -> s)
/*  73 */     .step(ChunkStatus.CARVERS, s -> s)
/*  74 */     .step(ChunkStatus.FEATURES, s -> s)
/*  75 */     .step(ChunkStatus.INITIALIZE_LIGHT, s -> s
/*  76 */       .setTask(ChunkStatusTasks::initializeLight))
/*     */     
/*  78 */     .step(ChunkStatus.LIGHT, s -> s
/*  79 */       .addRequirement(ChunkStatus.INITIALIZE_LIGHT, 1)
/*  80 */       .setTask(ChunkStatusTasks::light))
/*     */     
/*  82 */     .step(ChunkStatus.SPAWN, s -> s)
/*  83 */     .step(ChunkStatus.FULL, s -> s
/*  84 */       .setTask(ChunkStatusTasks::full))
/*     */     
/*  86 */     .build();
/*     */ 
/*     */   
/*  89 */   public ChunkStep getStepTo(ChunkStatus status) { return (ChunkStep)this.steps.get(status.getIndex()); }
/*     */   
/*     */   public static class Builder
/*     */   {
/*  93 */     private final List<ChunkStep> steps = new ArrayList();
/*     */ 
/*     */     
/*  96 */     public ChunkPyramid build() { return new ChunkPyramid(ImmutableList.copyOf(this.steps)); }
/*     */ 
/*     */     
/*     */     public Builder step(ChunkStatus status, UnaryOperator<ChunkStep.Builder> operator) {
/*     */       ChunkStep.Builder stepBuilder;
/* 101 */       if (this.steps.isEmpty()) {
/* 102 */         stepBuilder = new ChunkStep.Builder(status);
/*     */       } else {
/* 104 */         stepBuilder = new ChunkStep.Builder(status, (ChunkStep)this.steps.getLast());
/*     */       } 
/* 106 */       this.steps.add(((ChunkStep.Builder)operator.apply(stepBuilder)).build());
/* 107 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\status\ChunkPyramid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */