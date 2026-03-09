/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public abstract class ScalableParticleOptionsBase
/*    */   implements ParticleOptions {
/*    */   public static final float MIN_SCALE = 0.01F;
/*    */   public static final float MAX_SCALE = 4.0F;
/* 11 */   protected static final Codec<Float> SCALE = Codec.FLOAT
/* 12 */     .validate(v -> (v.floatValue() >= 0.01F && v.floatValue() <= 4.0F) ? 
/* 13 */       DataResult.success(v) : 
/* 14 */       DataResult.error(()));
/*    */ 
/*    */   
/*    */   private final float scale;
/*    */ 
/*    */   
/* 20 */   public ScalableParticleOptionsBase(float scale) { this.scale = Mth.clamp(scale, 0.01F, 4.0F); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public float getScale() { return this.scale; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\ScalableParticleOptionsBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */