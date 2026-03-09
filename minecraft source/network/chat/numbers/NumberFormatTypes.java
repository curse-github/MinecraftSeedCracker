/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class NumberFormatTypes
/*    */ {
/* 15 */   public static final MapCodec<NumberFormat> MAP_CODEC = BuiltInRegistries.NUMBER_FORMAT_TYPE.byNameCodec().dispatchMap(NumberFormat::type, NumberFormatType::mapCodec);
/* 16 */   public static final Codec<NumberFormat> CODEC = MAP_CODEC.codec();
/*    */   
/* 18 */   public static final StreamCodec<RegistryFriendlyByteBuf, NumberFormat> STREAM_CODEC = ByteBufCodecs.registry(Registries.NUMBER_FORMAT_TYPE)
/* 19 */     .dispatch(NumberFormat::type, NumberFormatType::streamCodec);
/*    */   
/* 21 */   public static final StreamCodec<RegistryFriendlyByteBuf, Optional<NumberFormat>> OPTIONAL_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs::optional);
/*    */   
/*    */   public static NumberFormatType<?> bootstrap(Registry<NumberFormatType<?>> registry) {
/* 24 */     Registry.register(registry, "blank", BlankFormat.TYPE);
/* 25 */     Registry.register(registry, "styled", StyledFormat.TYPE);
/* 26 */     return (NumberFormatType)Registry.register(registry, "fixed", FixedFormat.TYPE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\numbers\NumberFormatTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */