/*    */ package net.minecraft.util.valueproviders;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class TrapezoidFloat extends FloatProvider {
/* 11 */   public static final MapCodec<TrapezoidFloat> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 12 */         .fieldOf("min").forGetter(()), Codec.FLOAT
/* 13 */         .fieldOf("max").forGetter(()), Codec.FLOAT
/* 14 */         .fieldOf("plateau").forGetter(()))
/* 15 */       .apply(i, TrapezoidFloat::new)).validate(c -> {
/* 16 */         if (c.max < c.min) {
/* 17 */           return DataResult.error(());
/*    */         }
/* 19 */         if (c.plateau > c.max - c.min) {
/* 20 */           return DataResult.error(());
/*    */         }
/* 22 */         return DataResult.success(c);
/*    */       });
/*    */   
/*    */   private final float min;
/*    */   
/*    */   private final float max;
/*    */   private final float plateau;
/*    */   
/* 30 */   public static TrapezoidFloat of(float min, float max, float plateau) { return new TrapezoidFloat(min, max, plateau); }
/*    */ 
/*    */   
/*    */   private TrapezoidFloat(float min, float max, float plateau) {
/* 34 */     this.min = min;
/* 35 */     this.max = max;
/* 36 */     this.plateau = plateau;
/*    */   }
/*    */ 
/*    */   
/*    */   public float sample(RandomSource random) {
/* 41 */     float range = this.max - this.min;
/* 42 */     float plateauStart = (range - this.plateau) / 2.0F;
/* 43 */     float plateauEnd = range - plateauStart;
/*    */     
/* 45 */     return this.min + random.nextFloat() * plateauEnd + random.nextFloat() * plateauStart;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public float getMinValue() { return this.min; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 55 */   public float getMaxValue() { return this.max; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   public FloatProviderType<?> getType() { return FloatProviderType.TRAPEZOID; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   public String toString() { return "trapezoid(" + this.plateau + ") in [" + this.min + "-" + this.max + "]"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\valueproviders\TrapezoidFloat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */