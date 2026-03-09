/*     */ package net.minecraft.advancements.criterion;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ 
/*     */ public final class LocationPredicate extends Record {
/*     */   private final Optional<PositionPredicate> position;
/*     */   private final Optional<HolderSet<Biome>> biomes;
/*     */   private final Optional<HolderSet<Structure>> structures;
/*     */   private final Optional<ResourceKey<Level>> dimension;
/*     */   
/*  19 */   public LocationPredicate(Optional<PositionPredicate> position, Optional<HolderSet<Biome>> biomes, Optional<HolderSet<Structure>> structures, Optional<ResourceKey<Level>> dimension, Optional<Boolean> smokey, Optional<LightPredicate> light, Optional<BlockPredicate> block, Optional<FluidPredicate> fluid, Optional<Boolean> canSeeSky) { this.position = position; this.biomes = biomes; this.structures = structures; this.dimension = dimension; this.smokey = smokey; this.light = light; this.block = block; this.fluid = fluid; this.canSeeSky = canSeeSky; } private final Optional<Boolean> smokey; private final Optional<LightPredicate> light; private final Optional<BlockPredicate> block; private final Optional<FluidPredicate> fluid; private final Optional<Boolean> canSeeSky; public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/LocationPredicate;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate; } public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/LocationPredicate;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/LocationPredicate;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #19	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate;
/*  19 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<PositionPredicate> position() { return this.position; } public Optional<HolderSet<Biome>> biomes() { return this.biomes; } public Optional<HolderSet<Structure>> structures() { return this.structures; } public Optional<ResourceKey<Level>> dimension() { return this.dimension; } public Optional<Boolean> smokey() { return this.smokey; } public Optional<LightPredicate> light() { return this.light; } public Optional<BlockPredicate> block() { return this.block; } public Optional<FluidPredicate> fluid() { return this.fluid; } public Optional<Boolean> canSeeSky() { return this.canSeeSky; }
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
/*  30 */   public static final Codec<LocationPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(PositionPredicate.CODEC
/*  31 */         .optionalFieldOf("position").forGetter(LocationPredicate::position), 
/*  32 */         RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(LocationPredicate::biomes), 
/*  33 */         RegistryCodecs.homogeneousList(Registries.STRUCTURE).optionalFieldOf("structures").forGetter(LocationPredicate::structures), 
/*  34 */         ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("dimension").forGetter(LocationPredicate::dimension), Codec.BOOL
/*  35 */         .optionalFieldOf("smokey").forGetter(LocationPredicate::smokey), LightPredicate.CODEC
/*  36 */         .optionalFieldOf("light").forGetter(LocationPredicate::light), BlockPredicate.CODEC
/*  37 */         .optionalFieldOf("block").forGetter(LocationPredicate::block), FluidPredicate.CODEC
/*  38 */         .optionalFieldOf("fluid").forGetter(LocationPredicate::fluid), Codec.BOOL
/*  39 */         .optionalFieldOf("can_see_sky").forGetter(LocationPredicate::canSeeSky))
/*  40 */       .apply(i, LocationPredicate::new));
/*     */   
/*     */   public boolean matches(ServerLevel level, double x, double y, double z) {
/*  43 */     if (this.position.isPresent() && !((PositionPredicate)this.position.get()).matches(x, y, z)) {
/*  44 */       return false;
/*     */     }
/*     */     
/*  47 */     if (this.dimension.isPresent() && this.dimension.get() != level.dimension()) {
/*  48 */       return false;
/*     */     }
/*     */     
/*  51 */     BlockPos pos = BlockPos.containing(x, y, z);
/*  52 */     boolean loaded = level.isLoaded(pos);
/*     */     
/*  54 */     if (this.biomes.isPresent() && (!loaded || !((HolderSet)this.biomes.get()).contains(level.getBiome(pos)))) {
/*  55 */       return false;
/*     */     }
/*  57 */     if (this.structures.isPresent() && (!loaded || !level.structureManager().getStructureWithPieceAt(pos, (HolderSet)this.structures.get()).isValid())) {
/*  58 */       return false;
/*     */     }
/*  60 */     if (this.smokey.isPresent() && (!loaded || ((Boolean)this.smokey.get()).booleanValue() != CampfireBlock.isSmokeyPos(level, pos))) {
/*  61 */       return false;
/*     */     }
/*  63 */     if (this.light.isPresent() && !((LightPredicate)this.light.get()).matches(level, pos)) {
/*  64 */       return false;
/*     */     }
/*  66 */     if (this.block.isPresent() && !((BlockPredicate)this.block.get()).matches(level, pos)) {
/*  67 */       return false;
/*     */     }
/*  69 */     if (this.fluid.isPresent() && !((FluidPredicate)this.fluid.get()).matches(level, pos)) {
/*  70 */       return false;
/*     */     }
/*  72 */     if (this.canSeeSky.isPresent() && ((Boolean)this.canSeeSky.get()).booleanValue() != level.canSeeSky(pos)) {
/*  73 */       return false;
/*     */     }
/*     */     
/*  76 */     return true;
/*     */   }
/*     */   private static final class PositionPredicate extends Record { private final MinMaxBounds.Doubles x; private final MinMaxBounds.Doubles y; private final MinMaxBounds.Doubles z;
/*  79 */     private PositionPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) { this.x = x; this.y = y; this.z = z; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/LocationPredicate$PositionPredicate;
/*  79 */       //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Doubles x() { return this.x; } public MinMaxBounds.Doubles y() { return this.y; } public MinMaxBounds.Doubles z() { return this.z; }
/*  80 */     public static final Codec<PositionPredicate> CODEC = RecordCodecBuilder.create(i -> i.group(MinMaxBounds.Doubles.CODEC
/*  81 */           .optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::x), MinMaxBounds.Doubles.CODEC
/*  82 */           .optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::y), MinMaxBounds.Doubles.CODEC
/*  83 */           .optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::z))
/*  84 */         .apply(i, PositionPredicate::new));
/*     */     
/*     */     private static Optional<PositionPredicate> of(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) {
/*  87 */       if (x.isAny() && y.isAny() && z.isAny()) {
/*  88 */         return Optional.empty();
/*     */       }
/*  90 */       return Optional.of(new PositionPredicate(x, y, z));
/*     */     }
/*     */ 
/*     */     
/*  94 */     public boolean matches(double x, double y, double z) { return (this.x.matches(x) && this.y.matches(y) && this.z.matches(z)); } }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*  99 */     private MinMaxBounds.Doubles x = MinMaxBounds.Doubles.ANY;
/* 100 */     private MinMaxBounds.Doubles y = MinMaxBounds.Doubles.ANY;
/* 101 */     private MinMaxBounds.Doubles z = MinMaxBounds.Doubles.ANY;
/*     */     
/* 103 */     private Optional<HolderSet<Biome>> biomes = Optional.empty();
/* 104 */     private Optional<HolderSet<Structure>> structures = Optional.empty();
/* 105 */     private Optional<ResourceKey<Level>> dimension = Optional.empty();
/* 106 */     private Optional<Boolean> smokey = Optional.empty();
/*     */     
/* 108 */     private Optional<LightPredicate> light = Optional.empty();
/* 109 */     private Optional<BlockPredicate> block = Optional.empty();
/* 110 */     private Optional<FluidPredicate> fluid = Optional.empty();
/* 111 */     private Optional<Boolean> canSeeSky = Optional.empty();
/*     */ 
/*     */     
/* 114 */     public static Builder location() { return new Builder(); }
/*     */ 
/*     */ 
/*     */     
/* 118 */     public static Builder inBiome(Holder<Biome> biome) { return location().setBiomes(HolderSet.direct(new Holder[] { biome })); }
/*     */ 
/*     */ 
/*     */     
/* 122 */     public static Builder inDimension(ResourceKey<Level> dimension) { return location().setDimension(dimension); }
/*     */ 
/*     */ 
/*     */     
/* 126 */     public static Builder inStructure(Holder<Structure> structure) { return location().setStructures(HolderSet.direct(new Holder[] { structure })); }
/*     */ 
/*     */ 
/*     */     
/* 130 */     public static Builder atYLocation(MinMaxBounds.Doubles yLocation) { return location().setY(yLocation); }
/*     */ 
/*     */     
/*     */     public Builder setX(MinMaxBounds.Doubles x) {
/* 134 */       this.x = x;
/* 135 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setY(MinMaxBounds.Doubles y) {
/* 139 */       this.y = y;
/* 140 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setZ(MinMaxBounds.Doubles z) {
/* 144 */       this.z = z;
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBiomes(HolderSet<Biome> biomes) {
/* 149 */       this.biomes = Optional.of(biomes);
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setStructures(HolderSet<Structure> structures) {
/* 154 */       this.structures = Optional.of(structures);
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDimension(ResourceKey<Level> dimension) {
/* 159 */       this.dimension = Optional.of(dimension);
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLight(LightPredicate.Builder light) {
/* 164 */       this.light = Optional.of(light.build());
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setBlock(BlockPredicate.Builder block) {
/* 169 */       this.block = Optional.of(block.build());
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFluid(FluidPredicate.Builder fluid) {
/* 174 */       this.fluid = Optional.of(fluid.build());
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSmokey(boolean smokey) {
/* 179 */       this.smokey = Optional.of(Boolean.valueOf(smokey));
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCanSeeSky(boolean canSeeSky) {
/* 184 */       this.canSeeSky = Optional.of(Boolean.valueOf(canSeeSky));
/* 185 */       return this;
/*     */     }
/*     */     
/*     */     public LocationPredicate build() {
/* 189 */       Optional<LocationPredicate.PositionPredicate> position = LocationPredicate.PositionPredicate.of(this.x, this.y, this.z);
/* 190 */       return new LocationPredicate(position, this.biomes, this.structures, this.dimension, this.smokey, this.light, this.block, this.fluid, this.canSeeSky);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\LocationPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */