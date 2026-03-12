/*    */ package net.minecraft.core.particles;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public abstract class ParticleType<T extends ParticleOptions>
/*    */   extends Object {
/*    */   private final boolean overrideLimiter;
/*    */   
/* 11 */   protected ParticleType(boolean overrideLimiter) { this.overrideLimiter = overrideLimiter; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public boolean getOverrideLimiter() { return this.overrideLimiter; }
/*    */   
/*    */   public abstract MapCodec<T> codec();
/*    */   
/*    */   public abstract StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\ParticleType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */