/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.longs.Long2FloatLinkedOpenHashMap;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryCodecs;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.level.DryFoliageColor;
/*     */ import net.minecraft.world.level.FoliageColor;
/*     */ import net.minecraft.world.level.GrassColor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public final class Biome
/*     */ {
/*  38 */   public static final Codec<Biome> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(ClimateSettings.CODEC
/*  39 */         .forGetter(()), EnvironmentAttributeMap.CODEC_ONLY_POSITIONAL
/*  40 */         .optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(()), BiomeSpecialEffects.CODEC
/*  41 */         .fieldOf("effects").forGetter(()), BiomeGenerationSettings.CODEC
/*  42 */         .forGetter(()), MobSpawnSettings.CODEC
/*  43 */         .forGetter(()))
/*  44 */       .apply(i, Biome::new));
/*     */   
/*  46 */   public static final Codec<Biome> NETWORK_CODEC = RecordCodecBuilder.create(i -> i.group(ClimateSettings.CODEC
/*  47 */         .forGetter(()), EnvironmentAttributeMap.NETWORK_CODEC
/*  48 */         .optionalFieldOf("attributes", EnvironmentAttributeMap.EMPTY).forGetter(()), BiomeSpecialEffects.CODEC
/*  49 */         .fieldOf("effects").forGetter(()))
/*  50 */       .apply(i, ()));
/*     */   
/*  52 */   public static final Codec<Holder<Biome>> CODEC = RegistryFileCodec.create(Registries.BIOME, DIRECT_CODEC);
/*  53 */   public static final Codec<HolderSet<Biome>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.BIOME, DIRECT_CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static final PerlinSimplexNoise TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(1234L)), ImmutableList.of(Integer.valueOf(0)));
/*  60 */   private static final PerlinSimplexNoise FROZEN_TEMPERATURE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(3456L)), ImmutableList.of(Integer.valueOf(-2), Integer.valueOf(-1), Integer.valueOf(0)));
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*  63 */   public static final PerlinSimplexNoise BIOME_INFO_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(2345L)), ImmutableList.of(Integer.valueOf(0))); private static final int TEMPERATURE_CACHE_SIZE = 1024; private final ClimateSettings climateSettings;
/*     */   private final BiomeGenerationSettings generationSettings;
/*     */   private final MobSpawnSettings mobSettings;
/*     */   private final EnvironmentAttributeMap attributes;
/*     */   private final BiomeSpecialEffects specialEffects;
/*     */   private final ThreadLocal<Long2FloatLinkedOpenHashMap> temperatureCache;
/*     */   
/*  70 */   public enum Precipitation implements StringRepresentable { NONE("none"),
/*  71 */     RAIN("rain"),
/*  72 */     SNOW("snow"); public static final Codec<Precipitation> CODEC; private final String name;
/*     */     
/*     */     static  {
/*  75 */       CODEC = StringRepresentable.fromEnum(Precipitation::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  80 */     Precipitation(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     public String getSerializedName() { return this.name; } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final abstract enum TemperatureModifier
/*     */     implements StringRepresentable
/*     */   {
/*     */     NONE("snow"),
/*     */     FROZEN("snow");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final Codec<TemperatureModifier> CODEC;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static  {
/*     */       // Byte code:
/*     */       //   0: new net/minecraft/world/level/biome/Biome$TemperatureModifier$1
/*     */       //   3: dup
/*     */       //   4: ldc 'NONE'
/*     */       //   6: iconst_0
/*     */       //   7: ldc 'none'
/*     */       //   9: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   12: putstatic net/minecraft/world/level/biome/Biome$TemperatureModifier.NONE : Lnet/minecraft/world/level/biome/Biome$TemperatureModifier;
/*     */       //   15: new net/minecraft/world/level/biome/Biome$TemperatureModifier$2
/*     */       //   18: dup
/*     */       //   19: ldc 'FROZEN'
/*     */       //   21: iconst_1
/*     */       //   22: ldc 'frozen'
/*     */       //   24: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
/*     */       //   27: putstatic net/minecraft/world/level/biome/Biome$TemperatureModifier.FROZEN : Lnet/minecraft/world/level/biome/Biome$TemperatureModifier;
/*     */       //   30: invokestatic $values : ()[Lnet/minecraft/world/level/biome/Biome$TemperatureModifier;
/*     */       //   33: putstatic net/minecraft/world/level/biome/Biome$TemperatureModifier.$VALUES : [Lnet/minecraft/world/level/biome/Biome$TemperatureModifier;
/*     */       //   36: <illegal opcode> get : ()Ljava/util/function/Supplier;
/*     */       //   41: invokestatic fromEnum : (Ljava/util/function/Supplier;)Lnet/minecraft/util/StringRepresentable$EnumCodec;
/*     */       //   44: putstatic net/minecraft/world/level/biome/Biome$TemperatureModifier.CODEC : Lcom/mojang/serialization/Codec;
/*     */       //   47: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #90	-> 0
/*     */       //   #96	-> 15
/*     */       //   #89	-> 30
/*     */       //   #121	-> 36
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     TemperatureModifier(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     public String getName() { return this.name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     public String getSerializedName() { return this.name; }
/*     */     
/*     */     public abstract float modifyTemperature(BlockPos param1BlockPos, float param1Float);
/*     */   }
/*     */   
/*     */   static enum null { public float modifyTemperature(BlockPos pos, float baseTemperature) { return baseTemperature; } }
/*     */   
/* 136 */   private Biome(ClimateSettings climateSettings, EnvironmentAttributeMap attributes, BiomeSpecialEffects specialEffects, BiomeGenerationSettings generationSettings, MobSpawnSettings mobSettings) { this.temperatureCache = ThreadLocal.withInitial(() -> {
/* 137 */           Long2FloatLinkedOpenHashMap map = new Long2FloatLinkedOpenHashMap(1024, 0.25F)
/*     */             {
/*     */               protected void rehash(int newN) {}
/*     */             };
/*     */           
/* 142 */           map.defaultReturnValue(NaNF);
/* 143 */           return map;
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     this.climateSettings = climateSettings;
/* 154 */     this.generationSettings = generationSettings;
/* 155 */     this.mobSettings = mobSettings;
/*     */     
/* 157 */     this.attributes = attributes;
/* 158 */     this.specialEffects = specialEffects; }
/*     */ 
/*     */ 
/*     */   
/* 162 */   public MobSpawnSettings getMobSettings() { return this.mobSettings; }
/*     */   static enum null {
/*     */     public float modifyTemperature(BlockPos pos, float baseTemperature) { double groundValueLargeVariation = Biome.FROZEN_TEMPERATURE_NOISE.getValue(pos.getX() * 0.05D, pos.getZ() * 0.05D, false) * 7.0D; double groundValueEdgeVariation = Biome.BIOME_INFO_NOISE.getValue(pos.getX() * 0.2D, pos.getZ() * 0.2D, false); double icePatches = groundValueLargeVariation + groundValueEdgeVariation; if (icePatches < 0.3D) { double groundValueSmallVariation = Biome.BIOME_INFO_NOISE.getValue(pos.getX() * 0.09D, pos.getZ() * 0.09D, false); if (groundValueSmallVariation < 0.8D)
/*     */           return 0.2F;  }
/* 166 */        return baseTemperature; } } public boolean hasPrecipitation() { return this.climateSettings.hasPrecipitation(); }
/*     */ 
/*     */   
/*     */   public Precipitation getPrecipitationAt(BlockPos pos, int seaLevel) {
/* 170 */     if (!hasPrecipitation()) {
/* 171 */       return Precipitation.NONE;
/*     */     }
/* 173 */     return coldEnoughToSnow(pos, seaLevel) ? Precipitation.SNOW : Precipitation.RAIN;
/*     */   }
/*     */   
/*     */   private float getHeightAdjustedTemperature(BlockPos pos, int seaLevel) {
/* 177 */     float adjustedTemperature = this.climateSettings.temperatureModifier.modifyTemperature(pos, getBaseTemperature());
/* 178 */     int snowLevel = seaLevel + 17;
/*     */     
/* 180 */     if (pos.getY() > snowLevel) {
/*     */       
/* 182 */       float v = (float)(TEMPERATURE_NOISE.getValue((pos.getX() / 8.0F), (pos.getZ() / 8.0F), false) * 8.0D);
/* 183 */       return adjustedTemperature - (v + pos.getY() - snowLevel) * 0.05F / 40.0F;
/*     */     } 
/* 185 */     return adjustedTemperature;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   private float getTemperature(BlockPos pos, int seaLevel) {
/* 191 */     long key = pos.asLong();
/* 192 */     Long2FloatLinkedOpenHashMap cache = (Long2FloatLinkedOpenHashMap)this.temperatureCache.get();
/* 193 */     float cached = cache.get(key);
/* 194 */     if (!Float.isNaN(cached)) {
/* 195 */       return cached;
/*     */     }
/* 197 */     float temp = getHeightAdjustedTemperature(pos, seaLevel);
/* 198 */     if (cache.size() == 1024) {
/* 199 */       cache.removeFirstFloat();
/*     */     }
/* 201 */     cache.put(key, temp);
/* 202 */     return temp;
/*     */   }
/*     */ 
/*     */   
/* 206 */   public boolean shouldFreeze(LevelReader level, BlockPos pos) { return shouldFreeze(level, pos, true); }
/*     */ 
/*     */   
/*     */   public boolean shouldFreeze(LevelReader level, BlockPos pos, boolean checkNeighbors) {
/* 210 */     if (warmEnoughToRain(pos, level.getSeaLevel())) {
/* 211 */       return false;
/*     */     }
/*     */     
/* 214 */     if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
/* 215 */       BlockState blockState = level.getBlockState(pos);
/* 216 */       FluidState fluidState = level.getFluidState(pos);
/* 217 */       if (fluidState.getType() == Fluids.WATER && blockState.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) {
/* 218 */         if (!checkNeighbors) {
/* 219 */           return true;
/*     */         }
/*     */         
/* 222 */         boolean surroundedByWater = (level.isWaterAt(pos.west()) && level.isWaterAt(pos.east()) && level.isWaterAt(pos.north()) && level.isWaterAt(pos.south()));
/* 223 */         if (!surroundedByWater) {
/* 224 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 232 */   public boolean coldEnoughToSnow(BlockPos pos, int seaLevel) { return !warmEnoughToRain(pos, seaLevel); }
/*     */ 
/*     */ 
/*     */   
/* 236 */   public boolean warmEnoughToRain(BlockPos pos, int seaLevel) { return (getTemperature(pos, seaLevel) >= 0.15F); }
/*     */ 
/*     */ 
/*     */   
/* 240 */   public boolean shouldMeltFrozenOceanIcebergSlightly(BlockPos pos, int seaLevel) { return (getTemperature(pos, seaLevel) > 0.1F); }
/*     */ 
/*     */   
/*     */   public boolean shouldSnow(LevelReader level, BlockPos pos) {
/* 244 */     if (getPrecipitationAt(pos, level.getSeaLevel()) != Precipitation.SNOW) {
/* 245 */       return false;
/*     */     }
/*     */     
/* 248 */     if (level.isInsideBuildHeight(pos.getY()) && level.getBrightness(LightLayer.BLOCK, pos) < 10) {
/* 249 */       BlockState state = level.getBlockState(pos);
/*     */ 
/*     */       
/* 252 */       if ((state.isAir() || state.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(level, pos)) {
/* 253 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 264 */   public BiomeGenerationSettings getGenerationSettings() { return this.generationSettings; }
/*     */ 
/*     */   
/*     */   public int getGrassColor(double x, double z) {
/* 268 */     int baseGrassColor = getBaseGrassColor();
/* 269 */     return this.specialEffects.grassColorModifier().modifyColor(x, z, baseGrassColor);
/*     */   }
/*     */   
/*     */   private int getBaseGrassColor() {
/* 273 */     Optional<Integer> colorOverride = this.specialEffects.grassColorOverride();
/*     */     
/* 275 */     if (colorOverride.isPresent()) {
/* 276 */       return ((Integer)colorOverride.get()).intValue();
/*     */     }
/* 278 */     return getGrassColorFromTexture();
/*     */   }
/*     */   
/*     */   private int getGrassColorFromTexture() {
/* 282 */     double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
/* 283 */     double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
/*     */     
/* 285 */     return GrassColor.get(temp, rain);
/*     */   }
/*     */ 
/*     */   
/* 289 */   public int getFoliageColor() { return ((Integer)this.specialEffects.foliageColorOverride().orElseGet(this::getFoliageColorFromTexture)).intValue(); }
/*     */ 
/*     */   
/*     */   private int getFoliageColorFromTexture() {
/* 293 */     double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
/* 294 */     double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
/* 295 */     return FoliageColor.get(temp, rain);
/*     */   }
/*     */ 
/*     */   
/* 299 */   public int getDryFoliageColor() { return ((Integer)this.specialEffects.dryFoliageColorOverride().orElseGet(this::getDryFoliageColorFromTexture)).intValue(); }
/*     */ 
/*     */   
/*     */   private int getDryFoliageColorFromTexture() {
/* 303 */     double temp = Mth.clamp(this.climateSettings.temperature, 0.0F, 1.0F);
/* 304 */     double rain = Mth.clamp(this.climateSettings.downfall, 0.0F, 1.0F);
/* 305 */     return DryFoliageColor.get(temp, rain);
/*     */   }
/*     */ 
/*     */   
/* 309 */   public float getBaseTemperature() { return this.climateSettings.temperature; }
/*     */ 
/*     */ 
/*     */   
/* 313 */   public EnvironmentAttributeMap getAttributes() { return this.attributes; }
/*     */ 
/*     */ 
/*     */   
/* 317 */   public BiomeSpecialEffects getSpecialEffects() { return this.specialEffects; }
/*     */ 
/*     */ 
/*     */   
/* 321 */   public int getWaterColor() { return this.specialEffects.waterColor(); }
/*     */   
/*     */   public static class BiomeBuilder
/*     */   {
/*     */     private boolean hasPrecipitation = true;
/*     */     private Float temperature;
/* 327 */     private Biome.TemperatureModifier temperatureModifier = Biome.TemperatureModifier.NONE;
/*     */     private Float downfall;
/* 329 */     private final EnvironmentAttributeMap.Builder attributes = EnvironmentAttributeMap.builder();
/*     */     private BiomeSpecialEffects specialEffects;
/*     */     private MobSpawnSettings mobSpawnSettings;
/*     */     private BiomeGenerationSettings generationSettings;
/*     */     
/*     */     public BiomeBuilder hasPrecipitation(boolean hasPrecipitation) {
/* 335 */       this.hasPrecipitation = hasPrecipitation;
/* 336 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder temperature(float temperature) {
/* 340 */       this.temperature = Float.valueOf(temperature);
/* 341 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder downfall(float downfall) {
/* 345 */       this.downfall = Float.valueOf(downfall);
/* 346 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder putAttributes(EnvironmentAttributeMap attributes) {
/* 350 */       this.attributes.putAll(attributes);
/* 351 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 355 */     public BiomeBuilder putAttributes(EnvironmentAttributeMap.Builder attributes) { return putAttributes(attributes.build()); }
/*     */ 
/*     */     
/*     */     public <Value> BiomeBuilder setAttribute(EnvironmentAttribute<Value> attribute, Value value) {
/* 359 */       this.attributes.set(attribute, value);
/* 360 */       return this;
/*     */     }
/*     */     
/*     */     public <Value, Parameter> BiomeBuilder modifyAttribute(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Parameter> modifier, Parameter value) {
/* 364 */       this.attributes.modify(attribute, modifier, value);
/* 365 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder specialEffects(BiomeSpecialEffects specialEffects) {
/* 369 */       this.specialEffects = specialEffects;
/* 370 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder mobSpawnSettings(MobSpawnSettings mobSpawnSettings) {
/* 374 */       this.mobSpawnSettings = mobSpawnSettings;
/* 375 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder generationSettings(BiomeGenerationSettings generationSettings) {
/* 379 */       this.generationSettings = generationSettings;
/* 380 */       return this;
/*     */     }
/*     */     
/*     */     public BiomeBuilder temperatureAdjustment(Biome.TemperatureModifier temperatureModifier) {
/* 384 */       this.temperatureModifier = temperatureModifier;
/* 385 */       return this;
/*     */     }
/*     */     
/*     */     public Biome build() {
/* 389 */       if (this.temperature == null || this.downfall == null || this.specialEffects == null || this.mobSpawnSettings == null || this.generationSettings == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 395 */         throw new IllegalStateException("You are missing parameters to build a proper biome\n" + String.valueOf(this));
/*     */       }
/*     */       
/* 398 */       return new Biome(new Biome.ClimateSettings(this.hasPrecipitation, this.temperature
/* 399 */             .floatValue(), this.temperatureModifier, this.downfall.floatValue()), this.attributes
/* 400 */           .build(), this.specialEffects, this.generationSettings, this.mobSpawnSettings);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 409 */     public String toString() { return "BiomeBuilder{\nhasPrecipitation=" + this.hasPrecipitation + ",\ntemperature=" + this.temperature + ",\ntemperatureModifier=" + String.valueOf(this.temperatureModifier) + ",\ndownfall=" + this.downfall + ",\nspecialEffects=" + String.valueOf(this.specialEffects) + ",\nmobSpawnSettings=" + String.valueOf(this.mobSpawnSettings) + ",\ngenerationSettings=" + String.valueOf(this.generationSettings) + ",\n}"; }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ClimateSettings
/*     */     extends Record
/*     */   {
/*     */     private final boolean hasPrecipitation;
/*     */     private final float temperature;
/*     */     private final Biome.TemperatureModifier temperatureModifier;
/*     */     private final float downfall;
/*     */     
/* 421 */     private ClimateSettings(boolean hasPrecipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall) { this.hasPrecipitation = hasPrecipitation; this.temperature = temperature; this.temperatureModifier = temperatureModifier; this.downfall = downfall; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/Biome$ClimateSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #421	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/biome/Biome$ClimateSettings;
/* 421 */       //   0	8	1	o	Ljava/lang/Object; } public boolean hasPrecipitation() { return this.hasPrecipitation; } public float temperature() { return this.temperature; } public Biome.TemperatureModifier temperatureModifier() { return this.temperatureModifier; } public float downfall() { return this.downfall; }
/* 422 */     public static final MapCodec<ClimateSettings> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 423 */           .fieldOf("has_precipitation").forGetter(()), Codec.FLOAT
/* 424 */           .fieldOf("temperature").forGetter(()), Biome.TemperatureModifier.CODEC
/* 425 */           .optionalFieldOf("temperature_modifier", Biome.TemperatureModifier.NONE).forGetter(()), Codec.FLOAT
/* 426 */           .fieldOf("downfall").forGetter(()))
/* 427 */         .apply(i, ClimateSettings::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Biome.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */