/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class DustParticleOptions extends ScalableParticleOptionsBase {
/* 14 */   public static final DustParticleOptions REDSTONE = new DustParticleOptions(16711680, 1.0F);
/*    */   public static final int REDSTONE_PARTICLE_COLOR = 16711680;
/* 16 */   public static final MapCodec<DustParticleOptions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.RGB_COLOR_CODEC
/* 17 */         .fieldOf("color").forGetter(()), SCALE
/* 18 */         .fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale))
/* 19 */       .apply(i, DustParticleOptions::new));
/*    */   
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, DustParticleOptions> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, o -> 
/* 22 */       Integer.valueOf(o.color), ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, DustParticleOptions::new);
/*    */ 
/*    */   
/*    */   private final int color;
/*    */ 
/*    */ 
/*    */   
/*    */   public DustParticleOptions(int color, float scale) {
/* 30 */     super(scale);
/* 31 */     this.color = color;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public ParticleType<DustParticleOptions> getType() { return ParticleTypes.DUST; }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public Vector3f getColor() { return ARGB.vector3fFromRGB24(this.color); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\DustParticleOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */