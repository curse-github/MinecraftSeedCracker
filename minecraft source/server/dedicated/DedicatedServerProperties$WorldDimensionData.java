/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class WorldDimensionData
/*     */   extends Record
/*     */ {
/*     */   private final JsonObject generatorSettings;
/*     */   private final String levelType;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #240	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #240	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #240	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 240 */   private WorldDimensionData(JsonObject generatorSettings, String levelType) { this.generatorSettings = generatorSettings; this.levelType = levelType; } public JsonObject generatorSettings() { return this.generatorSettings; } public String levelType() { return this.levelType; }
/* 241 */   private static final Map<String, ResourceKey<WorldPreset>> LEGACY_PRESET_NAMES = Map.of("default", WorldPresets.NORMAL, "largebiomes", WorldPresets.LARGE_BIOMES);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldDimensions create(HolderLookup.Provider registries) {
/* 247 */     HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.WORLD_PRESET);
/*     */     
/* 249 */     Holder.Reference<WorldPreset> defaultHolder = (Holder.Reference)registryLookup.get(WorldPresets.NORMAL).or(() -> worldPresets.listElements().findAny()).orElseThrow(() -> new IllegalStateException("Invalid datapack contents: can't find default preset"));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     Objects.requireNonNull(registryLookup);
/* 255 */     Holder<WorldPreset> worldPreset = (Holder)Optional.ofNullable(Identifier.tryParse(this.levelType)).map(id -> ResourceKey.create(Registries.WORLD_PRESET, id)).or(() -> Optional.ofNullable((ResourceKey)LEGACY_PRESET_NAMES.get(this.levelType))).flatMap(registryLookup::get).orElseGet(() -> {
/* 256 */           DedicatedServerProperties.LOGGER.warn("Failed to parse level-type {}, defaulting to {}", this.levelType, defaultHolder.key().identifier());
/* 257 */           return defaultHolder;
/*     */         });
/*     */     
/* 260 */     WorldDimensions worldDimensions = ((WorldPreset)worldPreset.value()).createWorldDimensions();
/*     */ 
/*     */     
/* 263 */     if (worldPreset.is(WorldPresets.FLAT)) {
/* 264 */       RegistryOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
/* 265 */       Objects.requireNonNull(DedicatedServerProperties.LOGGER); Optional<FlatLevelGeneratorSettings> parsedSettings = FlatLevelGeneratorSettings.CODEC.parse(new Dynamic(ops, generatorSettings())).resultOrPartial(DedicatedServerProperties.LOGGER::error);
/* 266 */       if (parsedSettings.isPresent()) {
/* 267 */         return worldDimensions.replaceOverworldGenerator(registries, new FlatLevelSource((FlatLevelGeneratorSettings)parsedSettings.get()));
/*     */       }
/*     */     } 
/*     */     
/* 271 */     return worldDimensions;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dedicated\DedicatedServerProperties$WorldDimensionData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */