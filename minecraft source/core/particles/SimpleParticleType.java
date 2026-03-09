/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class SimpleParticleType extends ParticleType<SimpleParticleType> implements ParticleOptions {
/*  8 */   private final MapCodec<SimpleParticleType> codec = MapCodec.unit(this::getType);
/*    */   
/* 10 */   private final StreamCodec<RegistryFriendlyByteBuf, SimpleParticleType> streamCodec = StreamCodec.unit(this);
/*    */ 
/*    */   
/* 13 */   protected SimpleParticleType(boolean overrideLimiter) { super(overrideLimiter); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   public SimpleParticleType getType() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public MapCodec<SimpleParticleType> codec() { return this.codec; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public StreamCodec<RegistryFriendlyByteBuf, SimpleParticleType> streamCodec() { return this.streamCodec; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\SimpleParticleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */