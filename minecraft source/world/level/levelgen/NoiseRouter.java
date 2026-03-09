/*    */ package net.minecraft.world.level.levelgen;
/*    */ public final class NoiseRouter extends Record {
/*    */   private final DensityFunction barrierNoise;
/*    */   private final DensityFunction fluidLevelFloodednessNoise;
/*    */   private final DensityFunction fluidLevelSpreadNoise;
/*    */   private final DensityFunction lavaNoise;
/*    */   private final DensityFunction temperature;
/*    */   private final DensityFunction vegetation;
/*    */   private final DensityFunction continents;
/*    */   
/* 11 */   public NoiseRouter(DensityFunction barrierNoise, DensityFunction fluidLevelFloodednessNoise, DensityFunction fluidLevelSpreadNoise, DensityFunction lavaNoise, DensityFunction temperature, DensityFunction vegetation, DensityFunction continents, DensityFunction erosion, DensityFunction depth, DensityFunction ridges, DensityFunction preliminarySurfaceLevel, DensityFunction finalDensity, DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap) { this.barrierNoise = barrierNoise; this.fluidLevelFloodednessNoise = fluidLevelFloodednessNoise; this.fluidLevelSpreadNoise = fluidLevelSpreadNoise; this.lavaNoise = lavaNoise; this.temperature = temperature; this.vegetation = vegetation; this.continents = continents; this.erosion = erosion; this.depth = depth; this.ridges = ridges; this.preliminarySurfaceLevel = preliminarySurfaceLevel; this.finalDensity = finalDensity; this.veinToggle = veinToggle; this.veinRidged = veinRidged; this.veinGap = veinGap; } private final DensityFunction erosion; private final DensityFunction depth; private final DensityFunction ridges; private final DensityFunction preliminarySurfaceLevel; private final DensityFunction finalDensity; private final DensityFunction veinToggle; private final DensityFunction veinRidged; private final DensityFunction veinGap; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/NoiseRouter;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/NoiseRouter;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/NoiseRouter;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public DensityFunction barrierNoise() { return this.barrierNoise; } public DensityFunction fluidLevelFloodednessNoise() { return this.fluidLevelFloodednessNoise; } public DensityFunction fluidLevelSpreadNoise() { return this.fluidLevelSpreadNoise; } public DensityFunction lavaNoise() { return this.lavaNoise; } public DensityFunction temperature() { return this.temperature; } public DensityFunction vegetation() { return this.vegetation; } public DensityFunction continents() { return this.continents; } public DensityFunction erosion() { return this.erosion; } public DensityFunction depth() { return this.depth; } public DensityFunction ridges() { return this.ridges; } public DensityFunction preliminarySurfaceLevel() { return this.preliminarySurfaceLevel; } public DensityFunction finalDensity() { return this.finalDensity; } public DensityFunction veinToggle() { return this.veinToggle; } public DensityFunction veinRidged() { return this.veinRidged; } public DensityFunction veinGap() { return this.veinGap; }
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
/* 38 */   private static RecordCodecBuilder<NoiseRouter, DensityFunction> field(String name, Function<NoiseRouter, DensityFunction> getter) { return DensityFunction.HOLDER_HELPER_CODEC.fieldOf(name).forGetter(getter); }
/*    */ 
/*    */   
/* 41 */   public static final Codec<NoiseRouter> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 42 */         field("barrier", NoiseRouter::barrierNoise), 
/* 43 */         field("fluid_level_floodedness", NoiseRouter::fluidLevelFloodednessNoise), 
/* 44 */         field("fluid_level_spread", NoiseRouter::fluidLevelSpreadNoise), 
/* 45 */         field("lava", NoiseRouter::lavaNoise), 
/*    */         
/* 47 */         field("temperature", NoiseRouter::temperature), 
/* 48 */         field("vegetation", NoiseRouter::vegetation), 
/* 49 */         field("continents", NoiseRouter::continents), 
/* 50 */         field("erosion", NoiseRouter::erosion), 
/* 51 */         field("depth", NoiseRouter::depth), 
/* 52 */         field("ridges", NoiseRouter::ridges), 
/*    */         
/* 54 */         field("preliminary_surface_level", NoiseRouter::preliminarySurfaceLevel), 
/* 55 */         field("final_density", NoiseRouter::finalDensity), 
/*    */         
/* 57 */         field("vein_toggle", NoiseRouter::veinToggle), 
/* 58 */         field("vein_ridged", NoiseRouter::veinRidged), 
/* 59 */         field("vein_gap", NoiseRouter::veinGap))
/* 60 */       .apply(i, NoiseRouter::new));
/*    */   
/*    */   public NoiseRouter mapAll(DensityFunction.Visitor visitor) {
/* 63 */     return new NoiseRouter(this.barrierNoise
/* 64 */         .mapAll(visitor), this.fluidLevelFloodednessNoise
/* 65 */         .mapAll(visitor), this.fluidLevelSpreadNoise
/* 66 */         .mapAll(visitor), this.lavaNoise
/* 67 */         .mapAll(visitor), this.temperature
/* 68 */         .mapAll(visitor), this.vegetation
/* 69 */         .mapAll(visitor), this.continents
/* 70 */         .mapAll(visitor), this.erosion
/* 71 */         .mapAll(visitor), this.depth
/* 72 */         .mapAll(visitor), this.ridges
/* 73 */         .mapAll(visitor), this.preliminarySurfaceLevel
/* 74 */         .mapAll(visitor), this.finalDensity
/* 75 */         .mapAll(visitor), this.veinToggle
/* 76 */         .mapAll(visitor), this.veinRidged
/* 77 */         .mapAll(visitor), this.veinGap
/* 78 */         .mapAll(visitor));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\NoiseRouter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */