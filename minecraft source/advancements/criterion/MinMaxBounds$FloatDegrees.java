/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
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
/*     */ public final class FloatDegrees
/*     */   extends Record
/*     */   implements MinMaxBounds<Float>
/*     */ {
/*     */   private final MinMaxBounds.Bounds<Float> bounds;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #138	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 138 */   public FloatDegrees(MinMaxBounds.Bounds<Float> bounds) { this.bounds = bounds; } public MinMaxBounds.Bounds<Float> bounds() { return this.bounds; }
/* 139 */   public static final FloatDegrees ANY = new FloatDegrees(MinMaxBounds.Bounds.any());
/*     */   
/* 141 */   public static final Codec<FloatDegrees> CODEC = MinMaxBounds.Bounds.createCodec(Codec.FLOAT)
/* 142 */     .xmap(FloatDegrees::new, FloatDegrees::bounds);
/*     */   
/* 144 */   public static final StreamCodec<ByteBuf, FloatDegrees> STREAM_CODEC = MinMaxBounds.Bounds.createStreamCodec(ByteBufCodecs.FLOAT)
/* 145 */     .map(FloatDegrees::new, FloatDegrees::bounds);
/*     */   
/*     */   public static FloatDegrees fromReader(StringReader reader) throws CommandSyntaxException {
/* 148 */     Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS); MinMaxBounds.Bounds<Float> bounds = MinMaxBounds.Bounds.fromReader(reader, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat);
/* 149 */     return new FloatDegrees(bounds);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\MinMaxBounds$FloatDegrees.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */