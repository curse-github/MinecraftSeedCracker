/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.levelgen.PositionalRandomFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DirtyMarkingRandomSource
/*     */   implements RandomSource
/*     */ {
/*     */   private final RandomSource random;
/*     */   
/*  51 */   private DirtyMarkingRandomSource(RandomSource random) { this.random = random; }
/*     */ 
/*     */ 
/*     */   
/*     */   public RandomSource fork() {
/*  56 */     RandomSequences.this.setDirty();
/*  57 */     return this.random.fork();
/*     */   }
/*     */ 
/*     */   
/*     */   public PositionalRandomFactory forkPositional() {
/*  62 */     RandomSequences.this.setDirty();
/*  63 */     return this.random.forkPositional();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSeed(long seed) {
/*  68 */     RandomSequences.this.setDirty();
/*  69 */     this.random.setSeed(seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextInt() {
/*  74 */     RandomSequences.this.setDirty();
/*  75 */     return this.random.nextInt();
/*     */   }
/*     */ 
/*     */   
/*     */   public int nextInt(int bound) {
/*  80 */     RandomSequences.this.setDirty();
/*  81 */     return this.random.nextInt(bound);
/*     */   }
/*     */ 
/*     */   
/*     */   public long nextLong() {
/*  86 */     RandomSequences.this.setDirty();
/*  87 */     return this.random.nextLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nextBoolean() {
/*  92 */     RandomSequences.this.setDirty();
/*  93 */     return this.random.nextBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public float nextFloat() {
/*  98 */     RandomSequences.this.setDirty();
/*  99 */     return this.random.nextFloat();
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextDouble() {
/* 104 */     RandomSequences.this.setDirty();
/* 105 */     return this.random.nextDouble();
/*     */   }
/*     */ 
/*     */   
/*     */   public double nextGaussian() {
/* 110 */     RandomSequences.this.setDirty();
/* 111 */     return this.random.nextGaussian();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 116 */     if (this == obj) {
/* 117 */       return true;
/*     */     }
/* 119 */     if (obj instanceof DirtyMarkingRandomSource) { DirtyMarkingRandomSource other = (DirtyMarkingRandomSource)obj;
/* 120 */       return this.random.equals(other.random); }
/*     */     
/* 122 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\RandomSequences$DirtyMarkingRandomSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */