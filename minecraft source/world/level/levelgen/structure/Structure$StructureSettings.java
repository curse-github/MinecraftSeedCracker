/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
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
/*    */ public final class StructureSettings
/*    */   extends Record
/*    */ {
/*    */   private final HolderSet<Biome> biomes;
/*    */   private final Map<MobCategory, StructureSpawnOverride> spawnOverrides;
/*    */   private final GenerationStep.Decoration step;
/*    */   private final TerrainAdjustment terrainAdaptation;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 51 */   public StructureSettings(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawnOverrides, GenerationStep.Decoration step, TerrainAdjustment terrainAdaptation) { this.biomes = biomes; this.spawnOverrides = spawnOverrides; this.step = step; this.terrainAdaptation = terrainAdaptation; } public HolderSet<Biome> biomes() { return this.biomes; } public Map<MobCategory, StructureSpawnOverride> spawnOverrides() { return this.spawnOverrides; } public GenerationStep.Decoration step() { return this.step; } public TerrainAdjustment terrainAdaptation() { return this.terrainAdaptation; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   private static final StructureSettings DEFAULT = new StructureSettings(HolderSet.direct(new net.minecraft.core.Holder[0]), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
/* 58 */   public static final MapCodec<StructureSettings> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 59 */         RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(StructureSettings::biomes), 
/* 60 */         Codec.simpleMap(MobCategory.CODEC, StructureSpawnOverride.CODEC, StringRepresentable.keys(MobCategory.values())).fieldOf("spawn_overrides").forGetter(StructureSettings::spawnOverrides), GenerationStep.Decoration.CODEC
/* 61 */         .fieldOf("step").forGetter(StructureSettings::step), TerrainAdjustment.CODEC
/* 62 */         .optionalFieldOf("terrain_adaptation", DEFAULT.terrainAdaptation).forGetter(StructureSettings::terrainAdaptation))
/* 63 */       .apply(i, StructureSettings::new));
/*    */ 
/*    */ 
/*    */   
/* 67 */   public StructureSettings(HolderSet<Biome> biomes) { this(biomes, DEFAULT.spawnOverrides, DEFAULT.step, DEFAULT.terrainAdaptation); }
/*    */   public static class Builder { private final HolderSet<Biome> biomes;
/*    */     private Map<MobCategory, StructureSpawnOverride> spawnOverrides;
/*    */     
/*    */     public Builder(HolderSet<Biome> biomes) {
/* 72 */       this.spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides;
/* 73 */       this.step = Structure.StructureSettings.DEFAULT.step;
/* 74 */       this.terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation;
/*    */ 
/*    */       
/* 77 */       this.biomes = biomes;
/*    */     }
/*    */     private GenerationStep.Decoration step; private TerrainAdjustment terrainAdaption;
/*    */     public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
/* 81 */       this.spawnOverrides = spawnOverrides;
/* 82 */       return this;
/*    */     }
/*    */     
/*    */     public Builder generationStep(GenerationStep.Decoration step) {
/* 86 */       this.step = step;
/* 87 */       return this;
/*    */     }
/*    */     
/*    */     public Builder terrainAdapation(TerrainAdjustment terrainAdaption) {
/* 91 */       this.terrainAdaption = terrainAdaption;
/* 92 */       return this;
/*    */     }
/*    */ 
/*    */     
/* 96 */     public Structure.StructureSettings build() { return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\Structure$StructureSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */