/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class ColorParticleOption implements ParticleOptions {
/*    */   private final ParticleType<ColorParticleOption> type;
/*    */   
/* 13 */   public static MapCodec<ColorParticleOption> codec(ParticleType<ColorParticleOption> type) { return ExtraCodecs.ARGB_COLOR_CODEC.xmap(color -> new ColorParticleOption(type, color.intValue()), o -> Integer.valueOf(o.color)).fieldOf("color"); }
/*    */   
/*    */   private final int color;
/*    */   
/* 17 */   public static StreamCodec<? super ByteBuf, ColorParticleOption> streamCodec(ParticleType<ColorParticleOption> type) { return ByteBufCodecs.INT.map(color -> new ColorParticleOption(type, color.intValue()), o -> Integer.valueOf(o.color)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ColorParticleOption(ParticleType<ColorParticleOption> type, int color) {
/* 24 */     this.type = type;
/* 25 */     this.color = color;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public ParticleType<ColorParticleOption> getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 34 */   public float getRed() { return ARGB.red(this.color) / 255.0F; }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public float getGreen() { return ARGB.green(this.color) / 255.0F; }
/*    */ 
/*    */ 
/*    */   
/* 42 */   public float getBlue() { return ARGB.blue(this.color) / 255.0F; }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public float getAlpha() { return ARGB.alpha(this.color) / 255.0F; }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public static ColorParticleOption create(ParticleType<ColorParticleOption> type, int color) { return new ColorParticleOption(type, color); }
/*    */ 
/*    */ 
/*    */   
/* 54 */   public static ColorParticleOption create(ParticleType<ColorParticleOption> type, float red, float green, float blue) { return create(type, ARGB.colorFromFloat(1.0F, red, green, blue)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\ColorParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */