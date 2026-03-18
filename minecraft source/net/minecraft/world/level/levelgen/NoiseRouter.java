 package net.minecraft.world.level.levelgen;
 public final class NoiseRouter extends Record {
   private final DensityFunction barrierNoise;
   private final DensityFunction fluidLevelFloodednessNoise;
   private final DensityFunction fluidLevelSpreadNoise;
   private final DensityFunction lavaNoise;
   private final DensityFunction temperature;
   private final DensityFunction vegetation;
   private final DensityFunction continents;
   public NoiseRouter(
        DensityFunction barrierNoise, DensityFunction fluidLevelFloodednessNoise, DensityFunction fluidLevelSpreadNoise, DensityFunction lavaNoise,
        DensityFunction temperature, DensityFunction vegetation, DensityFunction continents, DensityFunction erosion, DensityFunction depth, DensityFunction ridges,
        DensityFunction preliminarySurfaceLevel, DensityFunction finalDensity, DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap
    ) {
        this.barrierNoise = barrierNoise; this.fluidLevelFloodednessNoise = fluidLevelFloodednessNoise; this.fluidLevelSpreadNoise = fluidLevelSpreadNoise; this.lavaNoise = lavaNoise;
        this.temperature = temperature; this.vegetation = vegetation; this.continents = continents; this.erosion = erosion; this.depth = depth; this.ridges = ridges;
        this.preliminarySurfaceLevel = preliminarySurfaceLevel; this.finalDensity = finalDensity; this.veinToggle = veinToggle; this.veinRidged = veinRidged; this.veinGap = veinGap;
    }
    private final DensityFunction erosion;
    private final DensityFunction depth;
    private final DensityFunction ridges;
    private final DensityFunction preliminarySurfaceLevel;
    private final DensityFunction finalDensity;
    private final DensityFunction veinToggle;
    private final DensityFunction veinRidged;
    private final DensityFunction veinGap;
    public final String toString() { 
    private static RecordCodecBuilder<NoiseRouter, DensityFunction> field(String name, Function<NoiseRouter, DensityFunction> getter) { return DensityFunction.HOLDER_HELPER_CODEC.fieldOf(name).forGetter(getter); }
    public static final Codec<NoiseRouter> CODEC = RecordCodecBuilder.create(i -> i.group(
            field("barrier", NoiseRouter::barrierNoise), 
            field("fluid_level_floodedness", NoiseRouter::fluidLevelFloodednessNoise), 
            field("fluid_level_spread", NoiseRouter::fluidLevelSpreadNoise), 
            field("lava", NoiseRouter::lavaNoise), 
            field("temperature", NoiseRouter::temperature), 
            field("vegetation", NoiseRouter::vegetation), 
            field("continents", NoiseRouter::continents), 
            field("erosion", NoiseRouter::erosion), 
            field("depth", NoiseRouter::depth), 
            field("ridges", NoiseRouter::ridges), 
            field("preliminary_surface_level", NoiseRouter::preliminarySurfaceLevel), 
            field("final_density", NoiseRouter::finalDensity), 
            field("vein_toggle", NoiseRouter::veinToggle), 
            field("vein_ridged", NoiseRouter::veinRidged), 
            field("vein_gap", NoiseRouter::veinGap))
        .apply(i, NoiseRouter::new)
    );
    public NoiseRouter mapAll(DensityFunction.Visitor visitor) {
        return new NoiseRouter(this.barrierNoise
            .mapAll(visitor), this.fluidLevelFloodednessNoise
            .mapAll(visitor), this.fluidLevelSpreadNoise
            .mapAll(visitor), this.lavaNoise
            .mapAll(visitor), this.temperature
            .mapAll(visitor), this.vegetation
            .mapAll(visitor), this.continents
            .mapAll(visitor), this.erosion
            .mapAll(visitor), this.depth
            .mapAll(visitor), this.ridges
            .mapAll(visitor), this.preliminarySurfaceLevel
            .mapAll(visitor), this.finalDensity
            .mapAll(visitor), this.veinToggle
            .mapAll(visitor), this.veinRidged
            .mapAll(visitor), this.veinGap
            .mapAll(visitor));
    }
}