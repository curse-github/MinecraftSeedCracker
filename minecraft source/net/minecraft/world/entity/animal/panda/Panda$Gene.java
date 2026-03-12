/*     */ package net.minecraft.world.entity.animal.panda;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum Gene
/*     */   implements StringRepresentable
/*     */ {
/* 306 */   NORMAL(0, "normal", false),
/* 307 */   LAZY(1, "lazy", false),
/* 308 */   WORRIED(2, "worried", false),
/* 309 */   PLAYFUL(3, "playful", false),
/* 310 */   BROWN(4, "brown", true),
/* 311 */   WEAK(5, "weak", true),
/* 312 */   AGGRESSIVE(6, "aggressive", false); public static final Codec<Gene> CODEC;
/*     */   static  {
/* 314 */     CODEC = StringRepresentable.fromEnum(Gene::values);
/*     */     
/* 316 */     BY_ID = ByIdMap.continuous(Gene::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */   }
/*     */   private static final IntFunction<Gene> BY_ID; private static final int MAX_GENE = 6;
/*     */   private final int id;
/*     */   private final String name;
/*     */   private final boolean isRecessive;
/*     */   
/*     */   Gene(int id, String name, boolean isRecessive) {
/* 324 */     this.id = id;
/* 325 */     this.name = name;
/* 326 */     this.isRecessive = isRecessive;
/*     */   }
/*     */ 
/*     */   
/* 330 */   public int getId() { return this.id; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 335 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 339 */   public boolean isRecessive() { return this.isRecessive; }
/*     */ 
/*     */   
/*     */   private static Gene getVariantFromGenes(Gene mainGene, Gene hiddenGene) {
/* 343 */     if (mainGene.isRecessive()) {
/* 344 */       if (mainGene == hiddenGene) {
/* 345 */         return mainGene;
/*     */       }
/* 347 */       return NORMAL;
/*     */     } 
/*     */ 
/*     */     
/* 351 */     return mainGene;
/*     */   }
/*     */ 
/*     */   
/* 355 */   public static Gene byId(int id) { return (Gene)BY_ID.apply(id); }
/*     */ 
/*     */   
/*     */   public static Gene getRandom(RandomSource random) {
/* 359 */     int nextInt = random.nextInt(16);
/* 360 */     if (nextInt == 0) {
/* 361 */       return LAZY;
/*     */     }
/* 363 */     if (nextInt == 1) {
/* 364 */       return WORRIED;
/*     */     }
/* 366 */     if (nextInt == 2) {
/* 367 */       return PLAYFUL;
/*     */     }
/* 369 */     if (nextInt == 4) {
/* 370 */       return AGGRESSIVE;
/*     */     }
/* 372 */     if (nextInt < 9) {
/* 373 */       return WEAK;
/*     */     }
/* 375 */     if (nextInt < 11) {
/* 376 */       return BROWN;
/*     */     }
/*     */     
/* 379 */     return NORMAL;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\panda\Panda$Gene.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */