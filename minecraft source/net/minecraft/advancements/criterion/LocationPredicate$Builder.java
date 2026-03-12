/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
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
/*     */ public class Builder
/*     */ {
/*  99 */   private MinMaxBounds.Doubles x = MinMaxBounds.Doubles.ANY;
/* 100 */   private MinMaxBounds.Doubles y = MinMaxBounds.Doubles.ANY;
/* 101 */   private MinMaxBounds.Doubles z = MinMaxBounds.Doubles.ANY;
/*     */   
/* 103 */   private Optional<HolderSet<Biome>> biomes = Optional.empty();
/* 104 */   private Optional<HolderSet<Structure>> structures = Optional.empty();
/* 105 */   private Optional<ResourceKey<Level>> dimension = Optional.empty();
/* 106 */   private Optional<Boolean> smokey = Optional.empty();
/*     */   
/* 108 */   private Optional<LightPredicate> light = Optional.empty();
/* 109 */   private Optional<BlockPredicate> block = Optional.empty();
/* 110 */   private Optional<FluidPredicate> fluid = Optional.empty();
/* 111 */   private Optional<Boolean> canSeeSky = Optional.empty();
/*     */ 
/*     */   
/* 114 */   public static Builder location() { return new Builder(); }
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static Builder inBiome(Holder<Biome> biome) { return location().setBiomes(HolderSet.direct(new Holder[] { biome })); }
/*     */ 
/*     */ 
/*     */   
/* 122 */   public static Builder inDimension(ResourceKey<Level> dimension) { return location().setDimension(dimension); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public static Builder inStructure(Holder<Structure> structure) { return location().setStructures(HolderSet.direct(new Holder[] { structure })); }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public static Builder atYLocation(MinMaxBounds.Doubles yLocation) { return location().setY(yLocation); }
/*     */ 
/*     */   
/*     */   public Builder setX(MinMaxBounds.Doubles x) {
/* 134 */     this.x = x;
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setY(MinMaxBounds.Doubles y) {
/* 139 */     this.y = y;
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setZ(MinMaxBounds.Doubles z) {
/* 144 */     this.z = z;
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setBiomes(HolderSet<Biome> biomes) {
/* 149 */     this.biomes = Optional.of(biomes);
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setStructures(HolderSet<Structure> structures) {
/* 154 */     this.structures = Optional.of(structures);
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDimension(ResourceKey<Level> dimension) {
/* 159 */     this.dimension = Optional.of(dimension);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLight(LightPredicate.Builder light) {
/* 164 */     this.light = Optional.of(light.build());
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setBlock(BlockPredicate.Builder block) {
/* 169 */     this.block = Optional.of(block.build());
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setFluid(FluidPredicate.Builder fluid) {
/* 174 */     this.fluid = Optional.of(fluid.build());
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setSmokey(boolean smokey) {
/* 179 */     this.smokey = Optional.of(Boolean.valueOf(smokey));
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setCanSeeSky(boolean canSeeSky) {
/* 184 */     this.canSeeSky = Optional.of(Boolean.valueOf(canSeeSky));
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public LocationPredicate build() {
/* 189 */     Optional<LocationPredicate.PositionPredicate> position = LocationPredicate.PositionPredicate.of(this.x, this.y, this.z);
/* 190 */     return new LocationPredicate(position, this.biomes, this.structures, this.dimension, this.smokey, this.light, this.block, this.fluid, this.canSeeSky);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\LocationPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */