/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public class SpellParticleOption implements ParticleOptions {
/*    */   private final ParticleType<SpellParticleOption> type;
/*    */   
/*    */   public static MapCodec<SpellParticleOption> codec(ParticleType<SpellParticleOption> type) {
/* 15 */     return RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.RGB_COLOR_CODEC
/* 16 */           .optionalFieldOf("color", Integer.valueOf(-1)).forGetter(()), Codec.FLOAT
/* 17 */           .optionalFieldOf("power", Float.valueOf(1.0F)).forGetter(()))
/* 18 */         .apply(i, ()));
/*    */   }
/*    */   private final int color; private final float power;
/*    */   public static StreamCodec<? super ByteBuf, SpellParticleOption> streamCodec(ParticleType<SpellParticleOption> type) {
/* 22 */     return StreamCodec.composite(ByteBufCodecs.INT, o -> 
/* 23 */         Integer.valueOf(o.color), ByteBufCodecs.FLOAT, o -> 
/* 24 */         Float.valueOf(o.power), (color, power) -> 
/* 25 */         new SpellParticleOption(type, color.intValue(), power.floatValue()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private SpellParticleOption(ParticleType<SpellParticleOption> type, int color, float power) {
/* 34 */     this.type = type;
/* 35 */     this.color = color;
/* 36 */     this.power = power;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public ParticleType<SpellParticleOption> getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public float getRed() { return ARGB.red(this.color) / 255.0F; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public float getGreen() { return ARGB.green(this.color) / 255.0F; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public float getBlue() { return ARGB.blue(this.color) / 255.0F; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public float getPower() { return this.power; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static SpellParticleOption create(ParticleType<SpellParticleOption> type, int color, float power) { return new SpellParticleOption(type, color, power); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public static SpellParticleOption create(ParticleType<SpellParticleOption> type, float red, float green, float blue, float power) { return create(type, ARGB.colorFromFloat(1.0F, red, green, blue), power); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\SpellParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */