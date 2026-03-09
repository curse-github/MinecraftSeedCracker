/*    */ package net.minecraft.network.chat.numbers;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class FixedFormat extends Record implements NumberFormat {
/* 10 */   public FixedFormat(Component value) { this.value = value; } private final Component value; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/numbers/FixedFormat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat; } public Component value() { return this.value; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/numbers/FixedFormat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/numbers/FixedFormat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 13 */   public static final NumberFormatType<FixedFormat> TYPE = new NumberFormatType<FixedFormat>() {
/* 14 */       private static final MapCodec<FixedFormat> CODEC = ComponentSerialization.CODEC.fieldOf("value").xmap(FixedFormat::new, FixedFormat::value);
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 19 */       private static final StreamCodec<RegistryFriendlyByteBuf, FixedFormat> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, FixedFormat::value, FixedFormat::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 26 */       public MapCodec<FixedFormat> mapCodec() { return CODEC; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 31 */       public StreamCodec<RegistryFriendlyByteBuf, FixedFormat> streamCodec() { return STREAM_CODEC; }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public MutableComponent format(int value) { return this.value.copy(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public NumberFormatType<FixedFormat> type() { return TYPE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\numbers\FixedFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */