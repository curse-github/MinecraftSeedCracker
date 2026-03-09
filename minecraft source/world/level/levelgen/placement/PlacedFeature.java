/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public final class PlacedFeature extends Record {
/*    */   private final Holder<ConfiguredFeature<?, ?>> feature;
/*    */   private final List<PlacementModifier> placement;
/*    */   
/* 23 */   public PlacedFeature(Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> placement) { this.feature = feature; this.placement = placement; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 23 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/placement/PlacedFeature; } public Holder<ConfiguredFeature<?, ?>> feature() { return this.feature; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #23	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;
/* 23 */     //   0	8	1	o	Ljava/lang/Object; } public List<PlacementModifier> placement() { return this.placement; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final Codec<PlacedFeature> DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group(ConfiguredFeature.CODEC
/* 28 */         .fieldOf("feature").forGetter(()), PlacementModifier.CODEC
/* 29 */         .listOf().fieldOf("placement").forGetter(()))
/* 30 */       .apply(i, PlacedFeature::new));
/*    */   
/* 32 */   public static final Codec<Holder<PlacedFeature>> CODEC = RegistryFileCodec.create(Registries.PLACED_FEATURE, DIRECT_CODEC);
/* 33 */   public static final Codec<HolderSet<PlacedFeature>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.PLACED_FEATURE, DIRECT_CODEC);
/*    */   
/* 35 */   public static final Codec<List<HolderSet<PlacedFeature>>> LIST_OF_LISTS_CODEC = RegistryCodecs.homogeneousList(Registries.PLACED_FEATURE, DIRECT_CODEC, true).listOf();
/*    */ 
/*    */   
/* 38 */   public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin) { return placeWithContext(new PlacementContext(level, generator, Optional.empty()), random, origin); }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public boolean placeWithBiomeCheck(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin) { return placeWithContext(new PlacementContext(level, generator, Optional.of(this)), random, origin); }
/*    */ 
/*    */   
/*    */   private boolean placeWithContext(PlacementContext context, RandomSource random, BlockPos origin) {
/* 46 */     Stream<BlockPos> placements = Stream.of(origin);
/* 47 */     for (PlacementModifier placementModifier : this.placement) {
/* 48 */       placements = placements.flatMap(p -> placementModifier.getPositions(context, random, p));
/*    */     }
/*    */     
/* 51 */     ConfiguredFeature<?, ?> feature = (ConfiguredFeature)this.feature.value();
/* 52 */     MutableBoolean placedAny = new MutableBoolean();
/* 53 */     placements.forEach(pos -> {
/* 54 */           if (feature.place(context.getLevel(), context.generator(), random, pos)) {
/* 55 */             placedAny.setTrue();
/* 56 */             if (SharedConstants.DEBUG_FEATURE_COUNT) {
/* 57 */               FeatureCountTracker.featurePlaced(context.getLevel().getLevel(), feature, context.topFeature());
/*    */             }
/*    */           } 
/*    */         });
/* 61 */     return placedAny.isTrue();
/*    */   }
/*    */ 
/*    */   
/* 65 */   public Stream<ConfiguredFeature<?, ?>> getFeatures() { return ((ConfiguredFeature)this.feature.value()).getFeatures(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 70 */   public String toString() { return "Placed " + String.valueOf(this.feature); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\PlacedFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */