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
/*     */ import net.minecraft.util.Mth;
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
/*     */ public final class Doubles
/*     */   extends Record
/*     */   implements MinMaxBounds<Double>
/*     */ {
/*     */   private final MinMaxBounds.Bounds<Double> bounds;
/*     */   private final MinMaxBounds.Bounds<Double> boundsSqr;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #80	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #80	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #80	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/*  80 */   public Doubles(MinMaxBounds.Bounds<Double> bounds, MinMaxBounds.Bounds<Double> boundsSqr) { this.bounds = bounds; this.boundsSqr = boundsSqr; } public MinMaxBounds.Bounds<Double> bounds() { return this.bounds; } public MinMaxBounds.Bounds<Double> boundsSqr() { return this.boundsSqr; }
/*  81 */   public static final Doubles ANY = new Doubles(MinMaxBounds.Bounds.any());
/*     */   
/*  83 */   public static final Codec<Doubles> CODEC = MinMaxBounds.Bounds.createCodec(Codec.DOUBLE)
/*  84 */     .validate(MinMaxBounds.Bounds::validateSwappedBoundsInCodec)
/*  85 */     .xmap(Doubles::new, Doubles::bounds);
/*     */   
/*  87 */   public static final StreamCodec<ByteBuf, Doubles> STREAM_CODEC = MinMaxBounds.Bounds.createStreamCodec(ByteBufCodecs.DOUBLE)
/*  88 */     .map(Doubles::new, Doubles::bounds);
/*     */ 
/*     */   
/*  91 */   private Doubles(MinMaxBounds.Bounds<Double> bounds) { this(bounds, bounds.map(Mth::square)); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   public static Doubles exactly(double value) { return new Doubles(MinMaxBounds.Bounds.exactly(Double.valueOf(value))); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static Doubles between(double min, double max) { return new Doubles(MinMaxBounds.Bounds.between(Double.valueOf(min), Double.valueOf(max))); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static Doubles atLeast(double value) { return new Doubles(MinMaxBounds.Bounds.atLeast(Double.valueOf(value))); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public static Doubles atMost(double value) { return new Doubles(MinMaxBounds.Bounds.atMost(Double.valueOf(value))); }
/*     */ 
/*     */   
/*     */   public boolean matches(double value) {
/* 111 */     if (this.bounds.min.isPresent() && ((Double)this.bounds.min.get()).doubleValue() > value) {
/* 112 */       return false;
/*     */     }
/* 114 */     return (this.bounds.max.isEmpty() || ((Double)this.bounds.max.get()).doubleValue() >= value);
/*     */   }
/*     */   
/*     */   public boolean matchesSqr(double valueSqr) {
/* 118 */     if (this.boundsSqr.min.isPresent() && ((Double)this.boundsSqr.min.get()).doubleValue() > valueSqr) {
/* 119 */       return false;
/*     */     }
/* 121 */     return (this.boundsSqr.max.isEmpty() || ((Double)this.boundsSqr.max.get()).doubleValue() >= valueSqr);
/*     */   }
/*     */   
/*     */   public static Doubles fromReader(StringReader reader) throws CommandSyntaxException {
/* 125 */     int start = reader.getCursor();
/*     */     
/* 127 */     Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS); MinMaxBounds.Bounds<Double> bounds = MinMaxBounds.Bounds.fromReader(reader, Double::parseDouble, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidDouble);
/*     */     
/* 129 */     if (bounds.areSwapped()) {
/* 130 */       reader.setCursor(start);
/* 131 */       throw ERROR_SWAPPED.createWithContext(reader);
/*     */     } 
/*     */     
/* 134 */     return new Doubles(bounds);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\MinMaxBounds$Doubles.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */