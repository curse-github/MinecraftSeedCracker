/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.gameevent.PositionSource;
/*    */ 
/*    */ public class VibrationParticleOption implements ParticleOptions {
/* 14 */   private static final Codec<PositionSource> SAFE_POSITION_SOURCE_CODEC = PositionSource.CODEC
/*    */     
/* 16 */     .validate(e -> (e instanceof net.minecraft.world.level.gameevent.EntityPositionSource) ? DataResult.error(()) : DataResult.success(e));
/*    */   
/* 18 */   public static final MapCodec<VibrationParticleOption> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(SAFE_POSITION_SOURCE_CODEC
/* 19 */         .fieldOf("destination").forGetter(VibrationParticleOption::getDestination), Codec.INT
/* 20 */         .fieldOf("arrival_in_ticks").forGetter(VibrationParticleOption::getArrivalInTicks))
/* 21 */       .apply(i, VibrationParticleOption::new));
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, VibrationParticleOption> STREAM_CODEC = StreamCodec.composite(PositionSource.STREAM_CODEC, VibrationParticleOption::getDestination, ByteBufCodecs.VAR_INT, VibrationParticleOption::getArrivalInTicks, VibrationParticleOption::new);
/*    */ 
/*    */   
/*    */   private final PositionSource destination;
/*    */ 
/*    */   
/*    */   private final int arrivalInTicks;
/*    */ 
/*    */   
/*    */   public VibrationParticleOption(PositionSource destination, int arrivalInTicks) {
/* 33 */     this.destination = destination;
/* 34 */     this.arrivalInTicks = arrivalInTicks;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public ParticleType<VibrationParticleOption> getType() { return ParticleTypes.VIBRATION; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public PositionSource getDestination() { return this.destination; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public int getArrivalInTicks() { return this.arrivalInTicks; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\VibrationParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */