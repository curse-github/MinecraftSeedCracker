/*     */ package net.minecraft.world.level.levelgen.structure.placement;
/*     */ import com.mojang.datafixers.Products;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ 
/*     */ public abstract class StructurePlacement {
/*  23 */   public static final Codec<StructurePlacement> CODEC = BuiltInRegistries.STRUCTURE_PLACEMENT.byNameCodec().dispatch(StructurePlacement::type, StructurePlacementType::codec); private static final int HIGHLY_ARBITRARY_RANDOM_SALT = 10387320; private final Vec3i locateOffset; private final FrequencyReductionMethod frequencyReductionMethod; private final float frequency; private final int salt;
/*     */   private final Optional<ExclusionZone> exclusionZone;
/*     */   
/*     */   protected static <S extends StructurePlacement> Products.P5<RecordCodecBuilder.Mu<S>, Vec3i, FrequencyReductionMethod, Float, Integer, Optional<ExclusionZone>> placementCodec(RecordCodecBuilder.Instance<S> i) {
/*  27 */     return i.group(
/*  28 */         Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(StructurePlacement::locateOffset), FrequencyReductionMethod.CODEC
/*  29 */         .optionalFieldOf("frequency_reduction_method", FrequencyReductionMethod.DEFAULT).forGetter(StructurePlacement::frequencyReductionMethod), 
/*  30 */         Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", Float.valueOf(1.0F)).forGetter(StructurePlacement::frequency), ExtraCodecs.NON_NEGATIVE_INT
/*  31 */         .fieldOf("salt").forGetter(StructurePlacement::salt), ExclusionZone.CODEC
/*  32 */         .optionalFieldOf("exclusion_zone").forGetter(StructurePlacement::exclusionZone));
/*     */   }
/*     */   @FunctionalInterface
/*     */   public static interface FrequencyReducer {
/*     */     boolean shouldGenerate(long param1Long, int param1Int1, int param1Int2, int param1Int3, float param1Float); }
/*     */   @Deprecated
/*     */   public static final class ExclusionZone extends Record { private final Holder<StructureSet> otherSet; private final int chunkCount;
/*  39 */     public Holder<StructureSet> otherSet() { return this.otherSet; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement$ExclusionZone;
/*  39 */       //   0	8	1	o	Ljava/lang/Object; } public int chunkCount() { return this.chunkCount; }
/*  40 */     public ExclusionZone(Holder<StructureSet> otherSet, int chunkCount) { this.otherSet = otherSet; this.chunkCount = chunkCount; }
/*  41 */     public static final Codec<ExclusionZone> CODEC = RecordCodecBuilder.create(i -> i.group(
/*     */           
/*  43 */           RegistryFileCodec.create(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC, false).fieldOf("other_set").forGetter(ExclusionZone::otherSet), 
/*  44 */           Codec.intRange(1, 16).fieldOf("chunk_count").forGetter(ExclusionZone::chunkCount))
/*  45 */         .apply(i, ExclusionZone::new));
/*     */ 
/*     */     
/*  48 */     private boolean isPlacementForbidden(ChunkGeneratorStructureState state, int sourceX, int sourceZ) { return state.hasStructureChunkInRange(this.otherSet, sourceX, sourceZ, this.chunkCount); } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StructurePlacement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
/*  59 */     this.locateOffset = locateOffset;
/*  60 */     this.frequencyReductionMethod = frequencyReductionMethod;
/*  61 */     this.frequency = frequency;
/*  62 */     this.salt = salt;
/*  63 */     this.exclusionZone = exclusionZone;
/*     */   }
/*     */ 
/*     */   
/*  67 */   protected Vec3i locateOffset() { return this.locateOffset; }
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected FrequencyReductionMethod frequencyReductionMethod() { return this.frequencyReductionMethod; }
/*     */ 
/*     */ 
/*     */   
/*  75 */   protected float frequency() { return this.frequency; }
/*     */ 
/*     */ 
/*     */   
/*  79 */   protected int salt() { return this.salt; }
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected Optional<ExclusionZone> exclusionZone() { return this.exclusionZone; }
/*     */ 
/*     */   
/*     */   public boolean isStructureChunk(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
/*  87 */     return (isPlacementChunk(state, sourceX, sourceZ) && 
/*  88 */       applyAdditionalChunkRestrictions(sourceX, sourceZ, state.getLevelSeed()) && 
/*  89 */       applyInteractionsWithOtherStructures(state, sourceX, sourceZ));
/*     */   }
/*     */   
/*     */   public boolean applyAdditionalChunkRestrictions(int sourceX, int sourceZ, long levelSeed) {
/*  93 */     if (this.frequency < 1.0F && !this.frequencyReductionMethod.shouldGenerate(levelSeed, this.salt, sourceX, sourceZ, this.frequency)) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public boolean applyInteractionsWithOtherStructures(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
/* 101 */     if (this.exclusionZone.isPresent() && ((ExclusionZone)this.exclusionZone.get()).isPlacementForbidden(state, sourceX, sourceZ)) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract boolean isPlacementChunk(ChunkGeneratorStructureState paramChunkGeneratorStructureState, int paramInt1, int paramInt2);
/*     */   
/* 111 */   public BlockPos getLocatePos(ChunkPos chunkPos) { return (new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ())).offset(locateOffset()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract StructurePlacementType<?> type();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean probabilityReducer(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 122 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
/* 123 */     random.setLargeFeatureWithSalt(seed, salt, sourceX, sourceZ);
/* 124 */     return (random.nextFloat() < probability);
/*     */   }
/*     */   
/*     */   private static boolean legacyProbabilityReducerWithDouble(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 128 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
/* 129 */     random.setLargeFeatureSeed(seed, sourceX, sourceZ);
/* 130 */     return (random.nextDouble() < probability);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean legacyArbitrarySaltProbabilityReducer(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 135 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
/* 136 */     random.setLargeFeatureWithSalt(seed, sourceX, sourceZ, 10387320);
/* 137 */     return (random.nextFloat() < probability);
/*     */   }
/*     */   
/*     */   private static boolean legacyPillagerOutpostReducer(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 141 */     int cx = sourceX >> 4;
/* 142 */     int cz = sourceZ >> 4;
/*     */ 
/*     */     
/* 145 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
/* 146 */     random.setSeed((cx ^ cz << 4) ^ seed);
/* 147 */     random.nextInt();
/*     */     
/* 149 */     return (random.nextInt((int)(1.0F / probability)) == 0);
/*     */   }
/*     */   
/*     */   public enum FrequencyReductionMethod implements StringRepresentable {
/* 153 */     DEFAULT("default", StructurePlacement::probabilityReducer),
/* 154 */     LEGACY_TYPE_1("legacy_type_1", StructurePlacement::legacyPillagerOutpostReducer),
/* 155 */     LEGACY_TYPE_2("legacy_type_2", StructurePlacement::legacyArbitrarySaltProbabilityReducer),
/* 156 */     LEGACY_TYPE_3("legacy_type_3", StructurePlacement::legacyProbabilityReducerWithDouble); public static final Codec<FrequencyReductionMethod> CODEC; private final String name; private final StructurePlacement.FrequencyReducer reducer;
/*     */     
/*     */     static  {
/* 159 */       CODEC = StringRepresentable.fromEnum(FrequencyReductionMethod::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     FrequencyReductionMethod(String name, StructurePlacement.FrequencyReducer reducer) {
/* 165 */       this.name = name;
/* 166 */       this.reducer = reducer;
/*     */     }
/*     */ 
/*     */     
/* 170 */     public boolean shouldGenerate(long seed, int salt, int sourceX, int sourceZ, float probability) { return this.reducer.shouldGenerate(seed, salt, sourceX, sourceZ, probability); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\placement\StructurePlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */