/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public class DustColorTransitionOptions extends ScalableParticleOptionsBase {
/* 14 */   public static final DustColorTransitionOptions SCULK_TO_REDSTONE = new DustColorTransitionOptions(3790560, 16711680, 1.0F);
/*    */   public static final int SCULK_PARTICLE_COLOR = 3790560;
/* 16 */   public static final MapCodec<DustColorTransitionOptions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ExtraCodecs.RGB_COLOR_CODEC
/* 17 */         .fieldOf("from_color").forGetter(()), ExtraCodecs.RGB_COLOR_CODEC
/* 18 */         .fieldOf("to_color").forGetter(()), SCALE
/* 19 */         .fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale))
/* 20 */       .apply(i, DustColorTransitionOptions::new));
/*    */   
/* 22 */   public static final StreamCodec<RegistryFriendlyByteBuf, DustColorTransitionOptions> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, o -> 
/* 23 */       Integer.valueOf(o.fromColor), ByteBufCodecs.INT, o -> 
/* 24 */       Integer.valueOf(o.toColor), ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, DustColorTransitionOptions::new);
/*    */ 
/*    */   
/*    */   private final int fromColor;
/*    */   
/*    */   private final int toColor;
/*    */ 
/*    */   
/*    */   public DustColorTransitionOptions(int fromColor, int toColor, float scale) {
/* 33 */     super(scale);
/* 34 */     this.fromColor = fromColor;
/* 35 */     this.toColor = toColor;
/*    */   }
/*    */ 
/*    */   
/* 39 */   public Vector3f getFromColor() { return ARGB.vector3fFromRGB24(this.fromColor); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public Vector3f getToColor() { return ARGB.vector3fFromRGB24(this.toColor); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 48 */   public ParticleType<DustColorTransitionOptions> getType() { return ParticleTypes.DUST_COLOR_TRANSITION; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\DustColorTransitionOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */