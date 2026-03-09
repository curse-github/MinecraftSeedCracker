/*    */ package net.minecraft.world.level.levelgen.presets;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.dimension.LevelStem;
/*    */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*    */ 
/*    */ public class WorldPreset {
/* 19 */   public static final Codec<WorldPreset> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(
/* 20 */         Codec.unboundedMap(ResourceKey.codec(Registries.LEVEL_STEM), LevelStem.CODEC).fieldOf("dimensions").forGetter(()))
/* 21 */       .apply(i, WorldPreset::new)).validate(WorldPreset::requireOverworld);
/*    */   
/* 23 */   public static final Codec<Holder<WorldPreset>> CODEC = RegistryFileCodec.create(Registries.WORLD_PRESET, DIRECT_CODEC);
/*    */   
/*    */   private final Map<ResourceKey<LevelStem>, LevelStem> dimensions;
/*    */ 
/*    */   
/* 28 */   public WorldPreset(Map<ResourceKey<LevelStem>, LevelStem> dimensions) { this.dimensions = dimensions; }
/*    */ 
/*    */   
/*    */   private ImmutableMap<ResourceKey<LevelStem>, LevelStem> dimensionsInOrder() {
/* 32 */     ImmutableMap.Builder<ResourceKey<LevelStem>, LevelStem> builder = ImmutableMap.builder();
/* 33 */     WorldDimensions.keysInOrder(this.dimensions.keySet().stream()).forEach(key -> {
/* 34 */           LevelStem levelStem = (LevelStem)this.dimensions.get(key);
/* 35 */           if (levelStem != null) {
/* 36 */             builder.put(key, levelStem);
/*    */           }
/*    */         });
/* 39 */     return builder.build();
/*    */   }
/*    */ 
/*    */   
/* 43 */   public WorldDimensions createWorldDimensions() { return new WorldDimensions(dimensionsInOrder()); }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Optional<LevelStem> overworld() { return Optional.ofNullable((LevelStem)this.dimensions.get(LevelStem.OVERWORLD)); }
/*    */ 
/*    */ 
/*    */   
/*    */   private static DataResult<WorldPreset> requireOverworld(WorldPreset preset) {
/* 52 */     if (preset.overworld().isEmpty()) {
/* 53 */       return DataResult.error(() -> "Missing overworld dimension");
/*    */     }
/* 55 */     return DataResult.success(preset, Lifecycle.stable());
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\presets\WorldPreset.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */