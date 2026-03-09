/*    */ package net.minecraft.world;
/*    */ 
/*    */ import javax.annotation.concurrent.Immutable;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class DifficultyInstance
/*    */ {
/*    */   private static final float DIFFICULTY_TIME_GLOBAL_OFFSET = -72000.0F;
/*    */   private static final float MAX_DIFFICULTY_TIME_GLOBAL = 1440000.0F;
/*    */   private static final float MAX_DIFFICULTY_TIME_LOCAL = 3600000.0F;
/*    */   private final Difficulty base;
/*    */   private final float effectiveDifficulty;
/*    */   
/*    */   public DifficultyInstance(Difficulty base, long totalGameTime, long localGameTime, float moonBrightness) {
/* 22 */     this.base = base;
/* 23 */     this.effectiveDifficulty = calculateDifficulty(base, totalGameTime, localGameTime, moonBrightness);
/*    */   }
/*    */ 
/*    */   
/* 27 */   public Difficulty getDifficulty() { return this.base; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public float getEffectiveDifficulty() { return this.effectiveDifficulty; }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public boolean isHard() { return (this.effectiveDifficulty >= Difficulty.HARD.ordinal()); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public boolean isHarderThan(float requiredDifficulty) { return (this.effectiveDifficulty > requiredDifficulty); }
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
/*    */   public float getSpecialMultiplier() {
/* 52 */     if (this.effectiveDifficulty < 2.0F) {
/* 53 */       return 0.0F;
/*    */     }
/* 55 */     if (this.effectiveDifficulty > 4.0F) {
/* 56 */       return 1.0F;
/*    */     }
/* 58 */     return (this.effectiveDifficulty - 2.0F) / 2.0F;
/*    */   }
/*    */   
/*    */   private float calculateDifficulty(Difficulty base, long totalGameTime, long localGameTime, float moonBrightness) {
/* 62 */     if (base == Difficulty.PEACEFUL) {
/* 63 */       return 0.0F;
/*    */     }
/*    */     
/* 66 */     boolean isHard = (base == Difficulty.HARD);
/* 67 */     float scale = 0.75F;
/*    */ 
/*    */     
/* 70 */     float globalScale = Mth.clamp(((float)totalGameTime + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;
/* 71 */     scale += globalScale;
/*    */     
/* 73 */     float localScale = 0.0F;
/*    */ 
/*    */     
/* 76 */     localScale += Mth.clamp((float)localGameTime / 3600000.0F, 0.0F, 1.0F) * (isHard ? 1.0F : 0.75F);
/* 77 */     localScale += Mth.clamp(moonBrightness * 0.25F, 0.0F, globalScale);
/*    */     
/* 79 */     if (base == Difficulty.EASY) {
/* 80 */       localScale *= 0.5F;
/*    */     }
/* 82 */     scale += localScale;
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 87 */     return base.getId() * scale;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\DifficultyInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */