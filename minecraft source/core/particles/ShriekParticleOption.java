/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class ShriekParticleOption implements ParticleOptions {
/* 11 */   public static final MapCodec<ShriekParticleOption> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 12 */         .fieldOf("delay").forGetter(()))
/* 13 */       .apply(i, ShriekParticleOption::new));
/*    */   
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShriekParticleOption> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, o -> 
/* 16 */       Integer.valueOf(o.delay), ShriekParticleOption::new);
/*    */ 
/*    */   
/*    */   private final int delay;
/*    */ 
/*    */ 
/*    */   
/* 23 */   public ShriekParticleOption(int delay) { this.delay = delay; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public ParticleType<ShriekParticleOption> getType() { return ParticleTypes.SHRIEK; }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public int getDelay() { return this.delay; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\ShriekParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */