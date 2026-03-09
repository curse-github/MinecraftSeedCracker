/*     */ package net.minecraft.world;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ 
/*     */ public class RandomSequences extends SavedData {
/*  18 */   public static final Codec<RandomSequences> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/*  19 */         .fieldOf("salt").forGetter(RandomSequences::salt), Codec.BOOL
/*  20 */         .optionalFieldOf("include_world_seed", Boolean.valueOf(true)).forGetter(RandomSequences::includeWorldSeed), Codec.BOOL
/*  21 */         .optionalFieldOf("include_sequence_id", Boolean.valueOf(true)).forGetter(RandomSequences::includeSequenceId), 
/*  22 */         Codec.unboundedMap(Identifier.CODEC, RandomSequence.CODEC).fieldOf("sequences").forGetter(()))
/*  23 */       .apply(i, RandomSequences::new));
/*     */   
/*  25 */   public static final SavedDataType<RandomSequences> TYPE = new SavedDataType("random_sequences", RandomSequences::new, CODEC, DataFixTypes.SAVED_DATA_RANDOM_SEQUENCES);
/*     */ 
/*     */   
/*     */   private int salt;
/*     */ 
/*     */   
/*     */   private boolean includeWorldSeed = true;
/*     */   
/*     */   private boolean includeSequenceId = true;
/*     */   
/*  35 */   private final Map<Identifier, RandomSequence> sequences = new Object2ObjectOpenHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RandomSequences(int salt, boolean includeWorldSeed, boolean includeSequenceId, Map<Identifier, RandomSequence> sequences) {
/*  41 */     this.salt = salt;
/*  42 */     this.includeWorldSeed = includeWorldSeed;
/*  43 */     this.includeSequenceId = includeSequenceId;
/*  44 */     this.sequences.putAll(sequences);
/*     */   }
/*     */   
/*     */   private class DirtyMarkingRandomSource
/*     */     implements RandomSource {
/*     */     private final RandomSource random;
/*     */     
/*  51 */     private DirtyMarkingRandomSource(RandomSource random) { this.random = random; }
/*     */ 
/*     */ 
/*     */     
/*     */     public RandomSource fork() {
/*  56 */       RandomSequences.this.setDirty();
/*  57 */       return this.random.fork();
/*     */     }
/*     */ 
/*     */     
/*     */     public PositionalRandomFactory forkPositional() {
/*  62 */       RandomSequences.this.setDirty();
/*  63 */       return this.random.forkPositional();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSeed(long seed) {
/*  68 */       RandomSequences.this.setDirty();
/*  69 */       this.random.setSeed(seed);
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextInt() {
/*  74 */       RandomSequences.this.setDirty();
/*  75 */       return this.random.nextInt();
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextInt(int bound) {
/*  80 */       RandomSequences.this.setDirty();
/*  81 */       return this.random.nextInt(bound);
/*     */     }
/*     */ 
/*     */     
/*     */     public long nextLong() {
/*  86 */       RandomSequences.this.setDirty();
/*  87 */       return this.random.nextLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean nextBoolean() {
/*  92 */       RandomSequences.this.setDirty();
/*  93 */       return this.random.nextBoolean();
/*     */     }
/*     */ 
/*     */     
/*     */     public float nextFloat() {
/*  98 */       RandomSequences.this.setDirty();
/*  99 */       return this.random.nextFloat();
/*     */     }
/*     */ 
/*     */     
/*     */     public double nextDouble() {
/* 104 */       RandomSequences.this.setDirty();
/* 105 */       return this.random.nextDouble();
/*     */     }
/*     */ 
/*     */     
/*     */     public double nextGaussian() {
/* 110 */       RandomSequences.this.setDirty();
/* 111 */       return this.random.nextGaussian();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 116 */       if (this == obj) {
/* 117 */         return true;
/*     */       }
/* 119 */       if (obj instanceof DirtyMarkingRandomSource) { DirtyMarkingRandomSource other = (DirtyMarkingRandomSource)obj;
/* 120 */         return this.random.equals(other.random); }
/*     */       
/* 122 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public RandomSource get(Identifier key, long worldSeed) {
/* 127 */     RandomSource random = ((RandomSequence)this.sequences.computeIfAbsent(key, rl -> createSequence(rl, worldSeed))).random();
/* 128 */     return new DirtyMarkingRandomSource(random);
/*     */   }
/*     */ 
/*     */   
/* 132 */   private RandomSequence createSequence(Identifier key, long worldSeed) { return createSequence(key, worldSeed, this.salt, this.includeWorldSeed, this.includeSequenceId); }
/*     */ 
/*     */   
/*     */   private RandomSequence createSequence(Identifier key, long worldSeed, int salt, boolean includeWorldSeed, boolean includeSequenceId) {
/* 136 */     long seed = (includeWorldSeed ? worldSeed : 0L) ^ salt;
/* 137 */     return new RandomSequence(seed, includeSequenceId ? Optional.of(key) : Optional.empty());
/*     */   }
/*     */ 
/*     */   
/* 141 */   public void forAllSequences(BiConsumer<Identifier, RandomSequence> consumer) { this.sequences.forEach(consumer); }
/*     */ 
/*     */   
/*     */   public void setSeedDefaults(int salt, boolean includeWorldSeed, boolean includeSequenceId) {
/* 145 */     this.salt = salt;
/* 146 */     this.includeWorldSeed = includeWorldSeed;
/* 147 */     this.includeSequenceId = includeSequenceId;
/*     */   }
/*     */   
/*     */   public int clear() {
/* 151 */     int count = this.sequences.size();
/* 152 */     this.sequences.clear();
/* 153 */     return count;
/*     */   }
/*     */ 
/*     */   
/* 157 */   public void reset(Identifier id, long worldSeed) { this.sequences.put(id, createSequence(id, worldSeed)); }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public void reset(Identifier id, long worldSeed, int salt, boolean includeWorldSeed, boolean includeSequenceId) { this.sequences.put(id, createSequence(id, worldSeed, salt, includeWorldSeed, includeSequenceId)); }
/*     */ 
/*     */ 
/*     */   
/* 165 */   private int salt() { return this.salt; }
/*     */ 
/*     */ 
/*     */   
/* 169 */   private boolean includeWorldSeed() { return this.includeWorldSeed; }
/*     */ 
/*     */ 
/*     */   
/* 173 */   private boolean includeSequenceId() { return this.includeSequenceId; }
/*     */   
/*     */   public RandomSequences() {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\RandomSequences.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */