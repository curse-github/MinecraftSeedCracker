/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public static enum WeatherState
/*     */   implements StringRepresentable
/*     */ {
/*     */   public static final IntFunction<WeatherState> BY_ID;
/*     */   public static final Codec<WeatherState> CODEC;
/* 120 */   UNAFFECTED("unaffected"),
/* 121 */   EXPOSED("exposed"),
/* 122 */   WEATHERED("weathered"),
/* 123 */   OXIDIZED("oxidized");
/*     */   static  {
/* 125 */     BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 126 */     CODEC = StringRepresentable.fromEnum(WeatherState::values);
/* 127 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
/*     */   }
/*     */   public static final StreamCodec<ByteBuf, WeatherState> STREAM_CODEC;
/*     */   private final String name;
/*     */   
/* 132 */   WeatherState(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public String getSerializedName() { return this.name; }
/*     */ 
/*     */ 
/*     */   
/* 141 */   public WeatherState next() { return (WeatherState)BY_ID.apply(ordinal() + 1); }
/*     */ 
/*     */ 
/*     */   
/* 145 */   public WeatherState previous() { return (WeatherState)BY_ID.apply(ordinal() - 1); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\WeatheringCopper$WeatherState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */