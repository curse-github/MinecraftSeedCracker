/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
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
/*     */ public class BiomeBuilder
/*     */ {
/*     */   private boolean hasPrecipitation = true;
/*     */   private Float temperature;
/* 327 */   private Biome.TemperatureModifier temperatureModifier = Biome.TemperatureModifier.NONE;
/*     */   private Float downfall;
/* 329 */   private final EnvironmentAttributeMap.Builder attributes = EnvironmentAttributeMap.builder();
/*     */   private BiomeSpecialEffects specialEffects;
/*     */   private MobSpawnSettings mobSpawnSettings;
/*     */   private BiomeGenerationSettings generationSettings;
/*     */   
/*     */   public BiomeBuilder hasPrecipitation(boolean hasPrecipitation) {
/* 335 */     this.hasPrecipitation = hasPrecipitation;
/* 336 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder temperature(float temperature) {
/* 340 */     this.temperature = Float.valueOf(temperature);
/* 341 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder downfall(float downfall) {
/* 345 */     this.downfall = Float.valueOf(downfall);
/* 346 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder putAttributes(EnvironmentAttributeMap attributes) {
/* 350 */     this.attributes.putAll(attributes);
/* 351 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 355 */   public BiomeBuilder putAttributes(EnvironmentAttributeMap.Builder attributes) { return putAttributes(attributes.build()); }
/*     */ 
/*     */   
/*     */   public <Value> BiomeBuilder setAttribute(EnvironmentAttribute<Value> attribute, Value value) {
/* 359 */     this.attributes.set(attribute, value);
/* 360 */     return this;
/*     */   }
/*     */   
/*     */   public <Value, Parameter> BiomeBuilder modifyAttribute(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Parameter> modifier, Parameter value) {
/* 364 */     this.attributes.modify(attribute, modifier, value);
/* 365 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder specialEffects(BiomeSpecialEffects specialEffects) {
/* 369 */     this.specialEffects = specialEffects;
/* 370 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder mobSpawnSettings(MobSpawnSettings mobSpawnSettings) {
/* 374 */     this.mobSpawnSettings = mobSpawnSettings;
/* 375 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder generationSettings(BiomeGenerationSettings generationSettings) {
/* 379 */     this.generationSettings = generationSettings;
/* 380 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder temperatureAdjustment(Biome.TemperatureModifier temperatureModifier) {
/* 384 */     this.temperatureModifier = temperatureModifier;
/* 385 */     return this;
/*     */   }
/*     */   
/*     */   public Biome build() {
/* 389 */     if (this.temperature == null || this.downfall == null || this.specialEffects == null || this.mobSpawnSettings == null || this.generationSettings == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 395 */       throw new IllegalStateException("You are missing parameters to build a proper biome\n" + String.valueOf(this));
/*     */     }
/*     */     
/* 398 */     return new Biome(new Biome.ClimateSettings(this.hasPrecipitation, this.temperature
/* 399 */           .floatValue(), this.temperatureModifier, this.downfall.floatValue()), this.attributes
/* 400 */         .build(), this.specialEffects, this.generationSettings, this.mobSpawnSettings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 409 */   public String toString() { return "BiomeBuilder{\nhasPrecipitation=" + this.hasPrecipitation + ",\ntemperature=" + this.temperature + ",\ntemperatureModifier=" + String.valueOf(this.temperatureModifier) + ",\ndownfall=" + this.downfall + ",\nspecialEffects=" + String.valueOf(this.specialEffects) + ",\nmobSpawnSettings=" + String.valueOf(this.mobSpawnSettings) + ",\ngenerationSettings=" + String.valueOf(this.generationSettings) + ",\n}"; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Biome$BiomeBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */