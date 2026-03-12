/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Object
/*    */   implements NumberFormatType<FixedFormat>
/*    */ {
/* 14 */   private static final MapCodec<FixedFormat> CODEC = ComponentSerialization.CODEC.fieldOf("value").xmap(FixedFormat::new, FixedFormat::value);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   private static final StreamCodec<RegistryFriendlyByteBuf, FixedFormat> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, FixedFormat::value, FixedFormat::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public MapCodec<FixedFormat> mapCodec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public StreamCodec<RegistryFriendlyByteBuf, FixedFormat> streamCodec() { return STREAM_CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\numbers\FixedFormat$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */