/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.resources.RegistryOps;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class MultiNoiseBiomeSourceParameterList {
/* 26 */   public static final Codec<MultiNoiseBiomeSourceParameterList> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(Preset.CODEC
/* 27 */         .fieldOf("preset").forGetter(()), 
/* 28 */         RegistryOps.retrieveGetter(Registries.BIOME))
/* 29 */       .apply(i, MultiNoiseBiomeSourceParameterList::new));
/*    */   
/* 31 */   public static final Codec<Holder<MultiNoiseBiomeSourceParameterList>> CODEC = RegistryFileCodec.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, DIRECT_CODEC);
/*    */   
/*    */   private final Preset preset;
/*    */   
/*    */   private final Climate.ParameterList<Holder<Biome>> parameters;
/*    */   
/*    */   public MultiNoiseBiomeSourceParameterList(Preset preset, HolderGetter<Biome> biomes) {
/* 38 */     this.preset = preset;
/* 39 */     Objects.requireNonNull(biomes); this.parameters = preset.provider.apply(biomes::getOrThrow);
/*    */   }
/*    */ 
/*    */   
/* 43 */   public Climate.ParameterList<Holder<Biome>> parameters() { return this.parameters; }
/*    */ 
/*    */   
/*    */   public static Map<Preset, Climate.ParameterList<ResourceKey<Biome>>> knownPresets() {
/* 47 */     return (Map)Preset.BY_NAME.values().stream().collect(Collectors.toMap(e -> 
/* 48 */           e, e -> 
/* 49 */           e.provider().apply(())));
/*    */   }
/*    */   public static final class Preset extends Record { private final Identifier id; private final SourceProvider provider;
/*    */     
/* 53 */     public Preset(Identifier id, SourceProvider provider) { this.id = id; this.provider = provider; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 53 */       //   0	7	0	this	Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset; } public Identifier id() { return this.id; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/biome/MultiNoiseBiomeSourceParameterList$Preset;
/* 53 */       //   0	8	1	o	Ljava/lang/Object; } public SourceProvider provider() { return this.provider; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 59 */     public static final Preset NETHER = new Preset(Identifier.withDefaultNamespace("nether"), new SourceProvider()
/*    */         {
/*    */           public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> lookup) {
/* 62 */             return new Climate.ParameterList(List.of(
/* 63 */                   Pair.of(Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.NETHER_WASTES)), 
/* 64 */                   Pair.of(Climate.parameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.SOUL_SAND_VALLEY)), 
/* 65 */                   Pair.of(Climate.parameters(0.4F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.CRIMSON_FOREST)), 
/* 66 */                   Pair.of(Climate.parameters(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.375F), lookup.apply(Biomes.WARPED_FOREST)), 
/* 67 */                   Pair.of(Climate.parameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.175F), lookup.apply(Biomes.BASALT_DELTAS))));
/*    */           }
/*    */         }); @FunctionalInterface
/*    */     private static interface SourceProvider {
/*    */       <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> param2Function); }
/* 72 */     public static final Preset OVERWORLD = new Preset(Identifier.withDefaultNamespace("overworld"), new SourceProvider()
/*    */         {
/*    */           public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> lookup) {
/* 75 */             return MultiNoiseBiomeSourceParameterList.Preset.generateOverworldBiomes(lookup);
/*    */           }
/*    */         });
/*    */     
/* 79 */     private static final Map<Identifier, Preset> BY_NAME = (Map)Stream.of(new Preset[] { NETHER, OVERWORLD
/* 80 */         }).collect(Collectors.toMap(Preset::id, p -> p));
/*    */     
/* 82 */     public static final Codec<Preset> CODEC = Identifier.CODEC.flatXmap(name -> 
/* 83 */         (DataResult)Optional.ofNullable((Preset)BY_NAME.get(name)).map(DataResult::success).orElseGet(()), p -> 
/* 84 */         DataResult.success(p.id));
/*    */ 
/*    */     
/*    */     private static <T> Climate.ParameterList<T> generateOverworldBiomes(Function<ResourceKey<Biome>, T> lookup) {
/* 88 */       ImmutableList.Builder<Pair<Climate.ParameterPoint, T>> builder = ImmutableList.builder();
/* 89 */       (new OverworldBiomeBuilder()).addBiomes(p -> builder.add(p.mapSecond(lookup)));
/* 90 */       return new Climate.ParameterList(builder.build());
/*    */     }
/*    */ 
/*    */     
/* 94 */     public Stream<ResourceKey<Biome>> usedBiomes() { return this.provider.apply(e -> e).values().stream().map(Pair::getSecond).distinct(); } }
/*    */ 
/*    */   
/*    */   class null implements Preset.SourceProvider {
/*    */     public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> lookup) {
/*    */       return new Climate.ParameterList(List.of(Pair.of(Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.NETHER_WASTES)), Pair.of(Climate.parameters(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.SOUL_SAND_VALLEY)), Pair.of(Climate.parameters(0.4F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), lookup.apply(Biomes.CRIMSON_FOREST)), Pair.of(Climate.parameters(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.375F), lookup.apply(Biomes.WARPED_FOREST)), Pair.of(Climate.parameters(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.175F), lookup.apply(Biomes.BASALT_DELTAS))));
/*    */     }
/*    */   }
/*    */   
/*    */   class null implements Preset.SourceProvider {
/*    */     public <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> lookup) { return MultiNoiseBiomeSourceParameterList.Preset.generateOverworldBiomes(lookup); }
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   private static interface SourceProvider {
/*    */     <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> param1Function);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */