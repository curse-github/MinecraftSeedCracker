/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class PowerParticleOption
/*    */   implements ParticleOptions {
/* 11 */   public static MapCodec<PowerParticleOption> codec(ParticleType<PowerParticleOption> type) { return Codec.FLOAT.xmap(power -> new PowerParticleOption(type, power.floatValue()), o -> Float.valueOf(o.power)).optionalFieldOf("power", create(type, 1.0F)); }
/*    */   private final ParticleType<PowerParticleOption> type;
/*    */   private final float power;
/*    */   
/* 15 */   public static StreamCodec<? super ByteBuf, PowerParticleOption> streamCodec(ParticleType<PowerParticleOption> type) { return ByteBufCodecs.FLOAT.map(color -> new PowerParticleOption(type, color.floatValue()), o -> Float.valueOf(o.power)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private PowerParticleOption(ParticleType<PowerParticleOption> type, float power) {
/* 22 */     this.type = type;
/* 23 */     this.power = power;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public ParticleType<PowerParticleOption> getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public float getPower() { return this.power; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public static PowerParticleOption create(ParticleType<PowerParticleOption> type, float power) { return new PowerParticleOption(type, power); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\PowerParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */