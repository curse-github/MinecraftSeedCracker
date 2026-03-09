/*    */ package net.minecraft.network.chat.numbers;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class StyledFormat extends Record implements NumberFormat {
/* 11 */   public StyledFormat(Style style) { this.style = style; } private final Style style; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/numbers/StyledFormat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat; } public Style style() { return this.style; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/numbers/StyledFormat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/numbers/StyledFormat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 14 */   public static final NumberFormatType<StyledFormat> TYPE = new NumberFormatType<StyledFormat>() {
/* 15 */       private static final MapCodec<StyledFormat> CODEC = Style.Serializer.MAP_CODEC.xmap(StyledFormat::new, StyledFormat::style);
/*    */       
/* 17 */       private static final StreamCodec<RegistryFriendlyByteBuf, StyledFormat> STREAM_CODEC = StreamCodec.composite(Style.Serializer.TRUSTED_STREAM_CODEC, StyledFormat::style, StyledFormat::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 24 */       public MapCodec<StyledFormat> mapCodec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 29 */       public StreamCodec<RegistryFriendlyByteBuf, StyledFormat> streamCodec() { return STREAM_CODEC; }
/*    */     };
/*    */ 
/*    */   
/* 33 */   public static final StyledFormat NO_STYLE = new StyledFormat(Style.EMPTY);
/* 34 */   public static final StyledFormat SIDEBAR_DEFAULT = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.RED));
/* 35 */   public static final StyledFormat PLAYER_LIST_DEFAULT = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.YELLOW));
/*    */ 
/*    */ 
/*    */   
/* 39 */   public MutableComponent format(int value) { return Component.literal(Integer.toString(value)).withStyle(this.style); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 44 */   public NumberFormatType<StyledFormat> type() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\numbers\StyledFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */