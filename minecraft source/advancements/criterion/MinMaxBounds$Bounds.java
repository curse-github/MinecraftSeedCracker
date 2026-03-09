/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
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
/*     */ public final class Bounds<T extends Number & Comparable<T>>
/*     */   extends Record
/*     */ {
/*     */   private final Optional<T> min;
/*     */   private final Optional<T> max;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #172	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds<TT;>; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #172	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds<TT;>; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #172	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds<TT;>; }
/*     */   
/* 172 */   public Bounds(Optional<T> min, Optional<T> max) { this.min = min; this.max = max; } public Optional<T> min() { return this.min; } public Optional<T> max() { return this.max; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public boolean isAny() { return (min().isEmpty() && max().isEmpty()); }
/*     */ 
/*     */   
/*     */   public DataResult<Bounds<T>> validateSwappedBoundsInCodec() {
/* 181 */     if (areSwapped()) {
/* 182 */       return DataResult.error(() -> "Swapped bounds in range: " + String.valueOf(min()) + " is higher than " + String.valueOf(max()));
/*     */     }
/* 184 */     return DataResult.success(this);
/*     */   }
/*     */ 
/*     */   
/* 188 */   public boolean areSwapped() { return (this.min.isPresent() && this.max.isPresent() && ((Comparable)this.min.get()).compareTo((Number)this.max.get()) > 0); }
/*     */ 
/*     */   
/*     */   public Optional<T> asPoint() {
/* 192 */     Optional<T> min = min();
/* 193 */     Optional<T> max = max();
/* 194 */     return min.equals(max) ? min : Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 200 */   public static <T extends Number & Comparable<T>> Bounds<T> any() { return new Bounds(Optional.empty(), Optional.empty()); }
/*     */ 
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Bounds<T> exactly(T value) {
/* 204 */     Optional<T> wrapped = Optional.of(value);
/* 205 */     return new Bounds(wrapped, wrapped);
/*     */   }
/*     */ 
/*     */   
/* 209 */   public static <T extends Number & Comparable<T>> Bounds<T> between(T min, T max) { return new Bounds(Optional.of(min), Optional.of(max)); }
/*     */ 
/*     */ 
/*     */   
/* 213 */   public static <T extends Number & Comparable<T>> Bounds<T> atLeast(T value) { return new Bounds(Optional.of(value), Optional.empty()); }
/*     */ 
/*     */ 
/*     */   
/* 217 */   public static <T extends Number & Comparable<T>> Bounds<T> atMost(T value) { return new Bounds(Optional.empty(), Optional.of(value)); }
/*     */ 
/*     */ 
/*     */   
/* 221 */   public <U extends Number & Comparable<U>> Bounds<U> map(Function<T, U> mapper) { return new Bounds(this.min.map(mapper), this.max.map(mapper)); }
/*     */ 
/*     */   
/*     */   static <T extends Number & Comparable<T>> Codec<Bounds<T>> createCodec(Codec<T> numberCodec) {
/* 225 */     Codec<Bounds<T>> rangeCodec = RecordCodecBuilder.create(i -> i.group(numberCodec
/* 226 */           .optionalFieldOf("min").forGetter(Bounds::min), numberCodec
/* 227 */           .optionalFieldOf("max").forGetter(Bounds::max))
/* 228 */         .apply(i, Bounds::new));
/*     */     
/* 230 */     return Codec.either(rangeCodec, numberCodec).xmap(either -> 
/* 231 */         (Bounds)either.map((), ()), bounds -> {
/*     */           
/* 233 */           Optional<T> point = bounds.asPoint();
/* 234 */           return point.isPresent() ? Either.right((Number)point.get()) : Either.left(bounds);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static <B extends ByteBuf, T extends Number & Comparable<T>> StreamCodec<B, Bounds<T>> createStreamCodec(final StreamCodec<B, T> numberCodec) {
/* 240 */     return new StreamCodec<B, Bounds<T>>()
/*     */       {
/*     */         private static final int MIN_FLAG = 1;
/*     */         private static final int MAX_FLAG = 2;
/*     */         
/*     */         public MinMaxBounds.Bounds<T> decode(B input) {
/* 246 */           byte flags = input.readByte();
/* 247 */           Optional<T> min = ((flags & true) != 0) ? Optional.of((Number)numberCodec.decode(input)) : Optional.empty();
/* 248 */           Optional<T> max = ((flags & 0x2) != 0) ? Optional.of((Number)numberCodec.decode(input)) : Optional.empty();
/* 249 */           return new MinMaxBounds.Bounds(min, max);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, MinMaxBounds.Bounds<T> value) {
/* 254 */           Optional<T> min = value.min();
/* 255 */           Optional<T> max = value.max();
/* 256 */           output.writeByte((min.isPresent() ? 1 : 0) | (max.isPresent() ? 2 : 0));
/* 257 */           min.ifPresent(v -> numberCodec.encode(output, v));
/* 258 */           max.ifPresent(v -> numberCodec.encode(output, v));
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static <T extends Number & Comparable<T>> Bounds<T> fromReader(StringReader reader, Function<String, T> converter, Supplier<DynamicCommandExceptionType> parseExc) throws CommandSyntaxException {
/* 264 */     if (!reader.canRead()) {
/* 265 */       throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader);
/*     */     }
/*     */     
/* 268 */     int start = reader.getCursor();
/*     */     
/*     */     try {
/* 271 */       Optional<T> max, min = readNumber(reader, converter, parseExc);
/*     */       
/* 273 */       if (reader.canRead(2) && reader.peek() == '.' && reader.peek(1) == '.') {
/* 274 */         reader.skip();
/* 275 */         reader.skip();
/* 276 */         max = readNumber(reader, converter, parseExc);
/*     */       } else {
/* 278 */         max = min;
/*     */       } 
/*     */       
/* 281 */       if (min.isEmpty() && max.isEmpty()) {
/* 282 */         throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader);
/*     */       }
/* 284 */       return new Bounds(min, max);
/* 285 */     } catch (CommandSyntaxException e) {
/* 286 */       reader.setCursor(start);
/* 287 */       throw new CommandSyntaxException(e.getType(), e.getRawMessage(), e.getInput(), start);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T extends Number> Optional<T> readNumber(StringReader reader, Function<String, T> converter, Supplier<DynamicCommandExceptionType> parseExc) throws CommandSyntaxException {
/* 292 */     int start = reader.getCursor();
/* 293 */     while (reader.canRead() && isAllowedInputChar(reader)) {
/* 294 */       reader.skip();
/*     */     }
/* 296 */     String number = reader.getString().substring(start, reader.getCursor());
/* 297 */     if (number.isEmpty()) {
/* 298 */       return Optional.empty();
/*     */     }
/*     */     try {
/* 301 */       return Optional.of((Number)converter.apply(number));
/* 302 */     } catch (NumberFormatException ex) {
/* 303 */       throw ((DynamicCommandExceptionType)parseExc.get()).createWithContext(reader, number);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAllowedInputChar(StringReader reader) {
/* 308 */     char c = reader.peek();
/* 309 */     if ((c >= '0' && c <= '9') || c == '-') {
/* 310 */       return true;
/*     */     }
/*     */     
/* 313 */     if (c == '.') {
/* 314 */       return (!reader.canRead(2) || reader.peek(1) != '.');
/*     */     }
/*     */     
/* 317 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\MinMaxBounds$Bounds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */