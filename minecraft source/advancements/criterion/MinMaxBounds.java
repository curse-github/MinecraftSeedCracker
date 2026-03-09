/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public interface MinMaxBounds<T extends Number & Comparable<T>> {
/*     */   public static final class Ints extends Record implements MinMaxBounds<Integer> { private final MinMaxBounds.Bounds<Integer> bounds;
/*     */     private final MinMaxBounds.Bounds<Long> boundsSqr;
/*     */     
/*  22 */     public Ints(MinMaxBounds.Bounds<Integer> bounds, MinMaxBounds.Bounds<Long> boundsSqr) { this.bounds = bounds; this.boundsSqr = boundsSqr; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #22	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  22 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints; } public MinMaxBounds.Bounds<Integer> bounds() { return this.bounds; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #22	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #22	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Ints;
/*  22 */       //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Bounds<Long> boundsSqr() { return this.boundsSqr; }
/*  23 */     public static final Ints ANY = new Ints(MinMaxBounds.Bounds.any());
/*     */     
/*  25 */     public static final Codec<Ints> CODEC = MinMaxBounds.Bounds.createCodec(Codec.INT)
/*  26 */       .validate(MinMaxBounds.Bounds::validateSwappedBoundsInCodec)
/*  27 */       .xmap(Ints::new, Ints::bounds);
/*     */     
/*  29 */     public static final StreamCodec<ByteBuf, Ints> STREAM_CODEC = MinMaxBounds.Bounds.createStreamCodec(ByteBufCodecs.INT)
/*  30 */       .map(Ints::new, Ints::bounds);
/*     */ 
/*     */     
/*  33 */     private Ints(MinMaxBounds.Bounds<Integer> bounds) { this(bounds, bounds.map(i -> Long.valueOf(Mth.square(i.longValue())))); }
/*     */ 
/*     */ 
/*     */     
/*  37 */     public static Ints exactly(int value) { return new Ints(MinMaxBounds.Bounds.exactly(Integer.valueOf(value))); }
/*     */ 
/*     */ 
/*     */     
/*  41 */     public static Ints between(int min, int max) { return new Ints(MinMaxBounds.Bounds.between(Integer.valueOf(min), Integer.valueOf(max))); }
/*     */ 
/*     */ 
/*     */     
/*  45 */     public static Ints atLeast(int value) { return new Ints(MinMaxBounds.Bounds.atLeast(Integer.valueOf(value))); }
/*     */ 
/*     */ 
/*     */     
/*  49 */     public static Ints atMost(int value) { return new Ints(MinMaxBounds.Bounds.atMost(Integer.valueOf(value))); }
/*     */ 
/*     */     
/*     */     public boolean matches(int value) {
/*  53 */       if (this.bounds.min.isPresent() && ((Integer)this.bounds.min.get()).intValue() > value) {
/*  54 */         return false;
/*     */       }
/*  56 */       return (this.bounds.max.isEmpty() || ((Integer)this.bounds.max.get()).intValue() >= value);
/*     */     }
/*     */     
/*     */     public boolean matchesSqr(long valueSqr) {
/*  60 */       if (this.boundsSqr.min.isPresent() && ((Long)this.boundsSqr.min.get()).longValue() > valueSqr) {
/*  61 */         return false;
/*     */       }
/*  63 */       return (this.boundsSqr.max.isEmpty() || ((Long)this.boundsSqr.max.get()).longValue() >= valueSqr);
/*     */     }
/*     */     
/*     */     public static Ints fromReader(StringReader reader) throws CommandSyntaxException {
/*  67 */       int start = reader.getCursor();
/*     */       
/*  69 */       Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS); MinMaxBounds.Bounds<Integer> bounds = MinMaxBounds.Bounds.fromReader(reader, Integer::parseInt, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt);
/*     */       
/*  71 */       if (bounds.areSwapped()) {
/*  72 */         reader.setCursor(start);
/*  73 */         throw ERROR_SWAPPED.createWithContext(reader);
/*     */       } 
/*     */       
/*  76 */       return new Ints(bounds);
/*     */     } }
/*     */   public static final class Doubles extends Record implements MinMaxBounds<Double> { private final MinMaxBounds.Bounds<Double> bounds; private final MinMaxBounds.Bounds<Double> boundsSqr;
/*     */     
/*  80 */     public Doubles(MinMaxBounds.Bounds<Double> bounds, MinMaxBounds.Bounds<Double> boundsSqr) { this.bounds = bounds; this.boundsSqr = boundsSqr; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #80	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #80	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #80	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Doubles;
/*  80 */       //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Bounds<Double> bounds() { return this.bounds; } public MinMaxBounds.Bounds<Double> boundsSqr() { return this.boundsSqr; }
/*  81 */     public static final Doubles ANY = new Doubles(MinMaxBounds.Bounds.any());
/*     */     
/*  83 */     public static final Codec<Doubles> CODEC = MinMaxBounds.Bounds.createCodec(Codec.DOUBLE)
/*  84 */       .validate(MinMaxBounds.Bounds::validateSwappedBoundsInCodec)
/*  85 */       .xmap(Doubles::new, Doubles::bounds);
/*     */     
/*  87 */     public static final StreamCodec<ByteBuf, Doubles> STREAM_CODEC = MinMaxBounds.Bounds.createStreamCodec(ByteBufCodecs.DOUBLE)
/*  88 */       .map(Doubles::new, Doubles::bounds);
/*     */ 
/*     */     
/*  91 */     private Doubles(MinMaxBounds.Bounds<Double> bounds) { this(bounds, bounds.map(Mth::square)); }
/*     */ 
/*     */ 
/*     */     
/*  95 */     public static Doubles exactly(double value) { return new Doubles(MinMaxBounds.Bounds.exactly(Double.valueOf(value))); }
/*     */ 
/*     */ 
/*     */     
/*  99 */     public static Doubles between(double min, double max) { return new Doubles(MinMaxBounds.Bounds.between(Double.valueOf(min), Double.valueOf(max))); }
/*     */ 
/*     */ 
/*     */     
/* 103 */     public static Doubles atLeast(double value) { return new Doubles(MinMaxBounds.Bounds.atLeast(Double.valueOf(value))); }
/*     */ 
/*     */ 
/*     */     
/* 107 */     public static Doubles atMost(double value) { return new Doubles(MinMaxBounds.Bounds.atMost(Double.valueOf(value))); }
/*     */ 
/*     */     
/*     */     public boolean matches(double value) {
/* 111 */       if (this.bounds.min.isPresent() && ((Double)this.bounds.min.get()).doubleValue() > value) {
/* 112 */         return false;
/*     */       }
/* 114 */       return (this.bounds.max.isEmpty() || ((Double)this.bounds.max.get()).doubleValue() >= value);
/*     */     }
/*     */     
/*     */     public boolean matchesSqr(double valueSqr) {
/* 118 */       if (this.boundsSqr.min.isPresent() && ((Double)this.boundsSqr.min.get()).doubleValue() > valueSqr) {
/* 119 */         return false;
/*     */       }
/* 121 */       return (this.boundsSqr.max.isEmpty() || ((Double)this.boundsSqr.max.get()).doubleValue() >= valueSqr);
/*     */     }
/*     */     
/*     */     public static Doubles fromReader(StringReader reader) throws CommandSyntaxException {
/* 125 */       int start = reader.getCursor();
/*     */       
/* 127 */       Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS); MinMaxBounds.Bounds<Double> bounds = MinMaxBounds.Bounds.fromReader(reader, Double::parseDouble, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidDouble);
/*     */       
/* 129 */       if (bounds.areSwapped()) {
/* 130 */         reader.setCursor(start);
/* 131 */         throw ERROR_SWAPPED.createWithContext(reader);
/*     */       } 
/*     */       
/* 134 */       return new Doubles(bounds);
/*     */     } }
/*     */   public static final class FloatDegrees extends Record implements MinMaxBounds<Float> { private final MinMaxBounds.Bounds<Float> bounds;
/*     */     
/* 138 */     public FloatDegrees(MinMaxBounds.Bounds<Float> bounds) { this.bounds = bounds; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #138	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$FloatDegrees;
/* 138 */       //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Bounds<Float> bounds() { return this.bounds; }
/* 139 */     public static final FloatDegrees ANY = new FloatDegrees(MinMaxBounds.Bounds.any());
/*     */     
/* 141 */     public static final Codec<FloatDegrees> CODEC = MinMaxBounds.Bounds.createCodec(Codec.FLOAT)
/* 142 */       .xmap(FloatDegrees::new, FloatDegrees::bounds);
/*     */     
/* 144 */     public static final StreamCodec<ByteBuf, FloatDegrees> STREAM_CODEC = MinMaxBounds.Bounds.createStreamCodec(ByteBufCodecs.FLOAT)
/* 145 */       .map(FloatDegrees::new, FloatDegrees::bounds);
/*     */     
/*     */     public static FloatDegrees fromReader(StringReader reader) throws CommandSyntaxException {
/* 148 */       Objects.requireNonNull(CommandSyntaxException.BUILT_IN_EXCEPTIONS); MinMaxBounds.Bounds<Float> bounds = MinMaxBounds.Bounds.fromReader(reader, Float::parseFloat, CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat);
/* 149 */       return new FloatDegrees(bounds);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public static final SimpleCommandExceptionType ERROR_EMPTY = new SimpleCommandExceptionType(Component.translatable("argument.range.empty"));
/* 156 */   public static final SimpleCommandExceptionType ERROR_SWAPPED = new SimpleCommandExceptionType(Component.translatable("argument.range.swapped"));
/*     */ 
/*     */   
/*     */   Bounds<T> bounds();
/*     */   
/* 161 */   default Optional<T> min() { return (bounds()).min; }
/*     */ 
/*     */ 
/*     */   
/* 165 */   default Optional<T> max() { return (bounds()).max; }
/*     */ 
/*     */ 
/*     */   
/* 169 */   default boolean isAny() { return bounds().isAny(); }
/*     */   public static final class Bounds<T extends Number & Comparable<T>> extends Record { private final Optional<T> min; private final Optional<T> max;
/*     */     
/* 172 */     public Bounds(Optional<T> min, Optional<T> max) { this.min = min; this.max = max; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #172	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #172	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #172	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 172 */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/MinMaxBounds$Bounds<TT;>; } public Optional<T> min() { return this.min; } public Optional<T> max() { return this.max; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     public boolean isAny() { return (min().isEmpty() && max().isEmpty()); }
/*     */ 
/*     */     
/*     */     public DataResult<Bounds<T>> validateSwappedBoundsInCodec() {
/* 181 */       if (areSwapped()) {
/* 182 */         return DataResult.error(() -> "Swapped bounds in range: " + String.valueOf(min()) + " is higher than " + String.valueOf(max()));
/*     */       }
/* 184 */       return DataResult.success(this);
/*     */     }
/*     */ 
/*     */     
/* 188 */     public boolean areSwapped() { return (this.min.isPresent() && this.max.isPresent() && ((Comparable)this.min.get()).compareTo((Number)this.max.get()) > 0); }
/*     */ 
/*     */     
/*     */     public Optional<T> asPoint() {
/* 192 */       Optional<T> min = min();
/* 193 */       Optional<T> max = max();
/* 194 */       return min.equals(max) ? min : Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     public static <T extends Number & Comparable<T>> Bounds<T> any() { return new Bounds(Optional.empty(), Optional.empty()); }
/*     */ 
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Bounds<T> exactly(T value) {
/* 204 */       Optional<T> wrapped = Optional.of(value);
/* 205 */       return new Bounds(wrapped, wrapped);
/*     */     }
/*     */ 
/*     */     
/* 209 */     public static <T extends Number & Comparable<T>> Bounds<T> between(T min, T max) { return new Bounds(Optional.of(min), Optional.of(max)); }
/*     */ 
/*     */ 
/*     */     
/* 213 */     public static <T extends Number & Comparable<T>> Bounds<T> atLeast(T value) { return new Bounds(Optional.of(value), Optional.empty()); }
/*     */ 
/*     */ 
/*     */     
/* 217 */     public static <T extends Number & Comparable<T>> Bounds<T> atMost(T value) { return new Bounds(Optional.empty(), Optional.of(value)); }
/*     */ 
/*     */ 
/*     */     
/* 221 */     public <U extends Number & Comparable<U>> Bounds<U> map(Function<T, U> mapper) { return new Bounds(this.min.map(mapper), this.max.map(mapper)); }
/*     */ 
/*     */     
/*     */     static <T extends Number & Comparable<T>> Codec<Bounds<T>> createCodec(Codec<T> numberCodec) {
/* 225 */       Codec<Bounds<T>> rangeCodec = RecordCodecBuilder.create(i -> i.group(numberCodec
/* 226 */             .optionalFieldOf("min").forGetter(Bounds::min), numberCodec
/* 227 */             .optionalFieldOf("max").forGetter(Bounds::max))
/* 228 */           .apply(i, Bounds::new));
/*     */       
/* 230 */       return Codec.either(rangeCodec, numberCodec).xmap(either -> 
/* 231 */           (Bounds)either.map((), ()), bounds -> {
/*     */             
/* 233 */             Optional<T> point = bounds.asPoint();
/* 234 */             return point.isPresent() ? Either.right((Number)point.get()) : Either.left(bounds);
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     static <B extends ByteBuf, T extends Number & Comparable<T>> StreamCodec<B, Bounds<T>> createStreamCodec(final StreamCodec<B, T> numberCodec) {
/* 240 */       return new StreamCodec<B, Bounds<T>>()
/*     */         {
/*     */           private static final int MIN_FLAG = 1;
/*     */           private static final int MAX_FLAG = 2;
/*     */           
/*     */           public MinMaxBounds.Bounds<T> decode(B input) {
/* 246 */             byte flags = input.readByte();
/* 247 */             Optional<T> min = ((flags & true) != 0) ? Optional.of((Number)numberCodec.decode(input)) : Optional.empty();
/* 248 */             Optional<T> max = ((flags & 0x2) != 0) ? Optional.of((Number)numberCodec.decode(input)) : Optional.empty();
/* 249 */             return new MinMaxBounds.Bounds(min, max);
/*     */           }
/*     */ 
/*     */           
/*     */           public void encode(B output, MinMaxBounds.Bounds<T> value) {
/* 254 */             Optional<T> min = value.min();
/* 255 */             Optional<T> max = value.max();
/* 256 */             output.writeByte((min.isPresent() ? 1 : 0) | (max.isPresent() ? 2 : 0));
/* 257 */             min.ifPresent(v -> numberCodec.encode(output, v));
/* 258 */             max.ifPresent(v -> numberCodec.encode(output, v));
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public static <T extends Number & Comparable<T>> Bounds<T> fromReader(StringReader reader, Function<String, T> converter, Supplier<DynamicCommandExceptionType> parseExc) throws CommandSyntaxException {
/* 264 */       if (!reader.canRead()) {
/* 265 */         throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader);
/*     */       }
/*     */       
/* 268 */       int start = reader.getCursor();
/*     */       
/*     */       try {
/* 271 */         Optional<T> max, min = readNumber(reader, converter, parseExc);
/*     */         
/* 273 */         if (reader.canRead(2) && reader.peek() == '.' && reader.peek(1) == '.') {
/* 274 */           reader.skip();
/* 275 */           reader.skip();
/* 276 */           max = readNumber(reader, converter, parseExc);
/*     */         } else {
/* 278 */           max = min;
/*     */         } 
/*     */         
/* 281 */         if (min.isEmpty() && max.isEmpty()) {
/* 282 */           throw MinMaxBounds.ERROR_EMPTY.createWithContext(reader);
/*     */         }
/* 284 */         return new Bounds(min, max);
/* 285 */       } catch (CommandSyntaxException e) {
/* 286 */         reader.setCursor(start);
/* 287 */         throw new CommandSyntaxException(e.getType(), e.getRawMessage(), e.getInput(), start);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static <T extends Number> Optional<T> readNumber(StringReader reader, Function<String, T> converter, Supplier<DynamicCommandExceptionType> parseExc) throws CommandSyntaxException {
/* 292 */       int start = reader.getCursor();
/* 293 */       while (reader.canRead() && isAllowedInputChar(reader)) {
/* 294 */         reader.skip();
/*     */       }
/* 296 */       String number = reader.getString().substring(start, reader.getCursor());
/* 297 */       if (number.isEmpty()) {
/* 298 */         return Optional.empty();
/*     */       }
/*     */       try {
/* 301 */         return Optional.of((Number)converter.apply(number));
/* 302 */       } catch (NumberFormatException ex) {
/* 303 */         throw ((DynamicCommandExceptionType)parseExc.get()).createWithContext(reader, number);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static boolean isAllowedInputChar(StringReader reader) {
/* 308 */       char c = reader.peek();
/* 309 */       if ((c >= '0' && c <= '9') || c == '-') {
/* 310 */         return true;
/*     */       }
/*     */       
/* 313 */       if (c == '.') {
/* 314 */         return (!reader.canRead(2) || reader.peek(1) != '.');
/*     */       }
/*     */       
/* 317 */       return false;
/*     */     } }
/*     */ 
/*     */   
/*     */   class null extends Object implements StreamCodec<B, Bounds<T>> {
/*     */     private static final int MIN_FLAG = 1;
/*     */     private static final int MAX_FLAG = 2;
/*     */     
/*     */     public MinMaxBounds.Bounds<T> decode(B input) {
/*     */       byte flags = input.readByte();
/*     */       Optional<T> min = ((flags & true) != 0) ? Optional.of((Number)numberCodec.decode(input)) : Optional.empty();
/*     */       Optional<T> max = ((flags & 0x2) != 0) ? Optional.of((Number)numberCodec.decode(input)) : Optional.empty();
/*     */       return new MinMaxBounds.Bounds(min, max);
/*     */     }
/*     */     
/*     */     public void encode(B output, MinMaxBounds.Bounds<T> value) {
/*     */       Optional<T> min = value.min();
/*     */       Optional<T> max = value.max();
/*     */       output.writeByte((min.isPresent() ? 1 : 0) | (max.isPresent() ? 2 : 0));
/*     */       min.ifPresent(v -> numberCodec.encode(output, v));
/*     */       max.ifPresent(v -> numberCodec.encode(output, v));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\MinMaxBounds.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */