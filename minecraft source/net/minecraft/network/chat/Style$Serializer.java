/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Serializer
/*    */ {
/* 21 */   public static final MapCodec<Style> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TextColor.CODEC
/* 22 */         .optionalFieldOf("color").forGetter(()), ExtraCodecs.ARGB_COLOR_CODEC
/* 23 */         .optionalFieldOf("shadow_color").forGetter(()), Codec.BOOL
/* 24 */         .optionalFieldOf("bold").forGetter(()), Codec.BOOL
/* 25 */         .optionalFieldOf("italic").forGetter(()), Codec.BOOL
/* 26 */         .optionalFieldOf("underlined").forGetter(()), Codec.BOOL
/* 27 */         .optionalFieldOf("strikethrough").forGetter(()), Codec.BOOL
/* 28 */         .optionalFieldOf("obfuscated").forGetter(()), ClickEvent.CODEC
/* 29 */         .optionalFieldOf("click_event").forGetter(()), HoverEvent.CODEC
/* 30 */         .optionalFieldOf("hover_event").forGetter(()), Codec.STRING
/* 31 */         .optionalFieldOf("insertion").forGetter(()), FontDescription.CODEC
/* 32 */         .optionalFieldOf("font").forGetter(()))
/* 33 */       .apply(i, Style::create));
/*    */ 
/*    */   
/* 36 */   public static final Codec<Style> CODEC = MAP_CODEC.codec();
/* 37 */   public static final StreamCodec<RegistryFriendlyByteBuf, Style> TRUSTED_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\Style$Serializer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */