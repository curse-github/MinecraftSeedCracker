/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class Ints extends Record implements MinMaxBounds<Integer> {
/*    */   private final MinMaxBounds.Bounds<Integer> bounds;
/*    */   private final MinMaxBounds.Bounds<Long> boundsSqr;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 22 */   public Ints(MinMaxBounds.Bounds<Integer> bounds, MinMaxBounds.Bounds<Long> boundsSqr) { this.bounds = bounds; this.boundsSqr = boundsSqr; } public MinMaxBounds.Bounds<Integer> bounds() { return this.bounds; } public MinMaxBounds.Bounds<Long> boundsSqr() { return this.boundsSqr; }
/* 23 */   public static final Ints ANY = new Ints(MinMaxBounds.Bounds.any());
/*    */   
/* 25 */   public static final Codec<Ints> CODEC = MinMaxBounds.Bounds.createCodec(Codec.INT)
/* 26 */     .validate(MinMaxBounds.Bounds::validateSwappedBoundsInCodec)
/* 27 */     .xmap(Ints::new, Ints::bounds);
/*    */   
/* 29 */   public static final StreamCodec<ByteBuf, Ints> STREAM_CODEC = MinMaxBounds.Bounds.createStreamCodec(ByteBufCodecs.INT)
/* 30 */     .map(Ints::new, Ints::bounds);
/*    */ 
/*    */   
/* 33 */   private Ints(MinMaxBounds.Bounds<Integer> bounds) { this(bounds, bounds.map(i -> Long.valueOf(Mth.square(i.longValue())))); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static Ints exactly(int value) { return new Ints(MinMaxBounds.Bounds.exactly(Integer.valueOf(value))); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static Ints between(int min, int max) { return new Ints(MinMaxBounds.Bounds.between(Integer.valueOf(min), Integer.valueOf(max))); }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static Ints atLeast(int value) { return new Ints(MinMaxBounds.Bounds.atLeast(Integer.valueOf(value))); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public static Ints atMost(int value) { return new Ints(MinMaxBounds.Bounds.atMost(Integer.valueOf(value))); }
/*    */ 
/*    */   
/*    */   public boolean matches(int value) {
/* 53 */     if (this.bounds.min.isPresent() && ((Integer)this.bounds.min.get()).intValue() > value) {
/* 54 */       return false;
/*    */     }
/* 56 */     return (this.bounds.max.isEmpty() || ((Integer)this.bounds.max.get()).intValue() >= value);
/*    */   }
/*    */   
/*    */   public boolean matchesSqr(long valueSqr) {
/* 60 */     if (this.boundsSqr.min.isPresent() && ((Long)this.boundsSqr.min.get()).longValue() > valueSqr) {
/* 61 */       return false;
/*    */     }
/* 63 */     return (this.boundsSqr.max.isEmpty() || ((Long)this.boundsSqr.max.get()).longValue() >= valueSqr);
/*    */   }
/*    */   
/*    */   public static Ints fromReader(StringReader reader) throws CommandSyntaxException {
/* 67 */     int start = reader.getCursor();
/*    */     
/* 69 */     Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS); MinMaxBounds.Bounds<Integer> bounds = MinMaxBounds.Bounds.fromReader(reader, Integer::parseInt, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt);
/*    */     
/* 71 */     if (bounds.areSwapped()) {
/* 72 */       reader.setCursor(start);
/* 73 */       throw ERROR_SWAPPED.createWithContext(reader);
/*    */     } 
/*    */     
/* 76 */     return new Ints(bounds);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\MinMaxBounds$Ints.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */