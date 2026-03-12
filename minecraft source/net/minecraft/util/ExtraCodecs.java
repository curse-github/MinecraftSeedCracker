/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JavaOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import com.mojang.serialization.codecs.BaseMapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.floats.FloatArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.time.Instant;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.temporal.TemporalAccessor;
/*     */ import java.util.Arrays;
/*     */ import java.util.Base64;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HexFormat;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.apache.commons.lang3.StringEscapeUtils;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ import org.joml.AxisAngle4f;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector2f;
/*     */ import org.joml.Vector2fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector3i;
/*     */ import org.joml.Vector3ic;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtraCodecs
/*     */ {
/*     */   public static <T> Codec<T> converter(DynamicOps<T> ops) {
/*  86 */     return Codec.PASSTHROUGH.xmap(t -> 
/*  87 */         t.convert(ops).getValue(), t -> 
/*  88 */         new Dynamic(ops, t));
/*     */   }
/*     */ 
/*     */   
/*  92 */   public static final Codec<JsonElement> JSON = converter(JsonOps.INSTANCE);
/*  93 */   public static final Codec<Object> JAVA = converter(JavaOps.INSTANCE);
/*  94 */   public static final Codec<Tag> NBT = converter(NbtOps.INSTANCE);
/*     */   
/*  96 */   public static final Codec<Vector2fc> VECTOR2F = Codec.FLOAT.listOf().comapFlatMap(input -> 
/*  97 */       Util.fixedSize(input, 2).map(()), vec -> 
/*  98 */       List.of(Float.valueOf(vec.x()), Float.valueOf(vec.y())));
/*     */ 
/*     */   
/* 101 */   public static final Codec<Vector3fc> VECTOR3F = Codec.FLOAT.listOf().comapFlatMap(input -> 
/* 102 */       Util.fixedSize(input, 3).map(()), vec -> 
/* 103 */       List.of(Float.valueOf(vec.x()), Float.valueOf(vec.y()), Float.valueOf(vec.z())));
/*     */   
/* 105 */   public static final Codec<Vector3ic> VECTOR3I = Codec.INT.listOf().comapFlatMap(input -> 
/* 106 */       Util.fixedSize(input, 3).map(()), vec -> 
/* 107 */       List.of(Integer.valueOf(vec.x()), Integer.valueOf(vec.y()), Integer.valueOf(vec.z())));
/*     */ 
/*     */   
/* 110 */   public static final Codec<Vector4fc> VECTOR4F = Codec.FLOAT.listOf().comapFlatMap(input -> 
/* 111 */       Util.fixedSize(input, 4).map(()), vec -> 
/* 112 */       List.of(Float.valueOf(vec.x()), Float.valueOf(vec.y()), Float.valueOf(vec.z()), Float.valueOf(vec.w())));
/*     */ 
/*     */   
/* 115 */   public static final Codec<Quaternionfc> QUATERNIONF_COMPONENTS = Codec.FLOAT.listOf().comapFlatMap(input -> 
/* 116 */       Util.fixedSize(input, 4).map(()), q -> 
/* 117 */       List.of(Float.valueOf(q.x()), Float.valueOf(q.y()), Float.valueOf(q.z()), Float.valueOf(q.w())));
/*     */ 
/*     */   
/* 120 */   public static final Codec<AxisAngle4f> AXISANGLE4F = RecordCodecBuilder.create(i -> i.group(Codec.FLOAT
/* 121 */         .fieldOf("angle").forGetter(()), VECTOR3F
/* 122 */         .fieldOf("axis").forGetter(()))
/* 123 */       .apply(i, AxisAngle4f::new));
/*     */   
/* 125 */   public static final Codec<Quaternionfc> QUATERNIONF = Codec.withAlternative(QUATERNIONF_COMPONENTS, AXISANGLE4F
/*     */       
/* 127 */       .xmap(Quaternionf::new, AxisAngle4f::new));
/*     */ 
/*     */   
/* 130 */   public static final Codec<Matrix4fc> MATRIX4F = Codec.FLOAT.listOf().comapFlatMap(input -> 
/* 131 */       Util.fixedSize(input, 16).map(()), m -> {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 140 */         FloatArrayList floatArrayList = new FloatArrayList(16);
/* 141 */         for (int i = 0; i < 16; i++) {
/* 142 */           floatArrayList.add(m.getRowColumn(i >> 2, i & 0x3));
/*     */         }
/* 144 */         return floatArrayList;
/*     */       });
/*     */   
/*     */   private static final String HEX_COLOR_PREFIX = "#";
/*     */ 
/*     */   
/*     */   private static Codec<Integer> hexColor(int expectedDigits) {
/* 151 */     long maxValue = (1L << expectedDigits * 4) - 1L;
/* 152 */     return Codec.STRING.comapFlatMap(string -> {
/*     */           
/* 154 */           if (!string.startsWith("#")) {
/* 155 */             return DataResult.error(());
/*     */           }
/* 157 */           int digits = string.length() - "#".length();
/* 158 */           if (digits != expectedDigits) {
/* 159 */             return DataResult.error(());
/*     */           }
/*     */           try {
/* 162 */             long value = HexFormat.fromHexDigitsToLong(string, "#".length(), string.length());
/* 163 */             if (value < 0L || value > maxValue) {
/* 164 */               return DataResult.error(());
/*     */             }
/* 166 */             return DataResult.success(Integer.valueOf((int)value));
/* 167 */           } catch (NumberFormatException e) {
/* 168 */             return DataResult.error(());
/*     */           }
/*     */         
/* 171 */         }value -> "#" + HexFormat.of().toHexDigits(value.intValue(), expectedDigits));
/*     */   }
/*     */ 
/*     */   
/* 175 */   public static final Codec<Integer> RGB_COLOR_CODEC = Codec.withAlternative(Codec.INT, VECTOR3F, v -> 
/*     */       
/* 177 */       Integer.valueOf(ARGB.colorFromFloat(1.0F, v.x(), v.y(), v.z())));
/*     */   
/* 179 */   public static final Codec<Integer> ARGB_COLOR_CODEC = Codec.withAlternative(Codec.INT, VECTOR4F, v -> 
/*     */       
/* 181 */       Integer.valueOf(ARGB.colorFromFloat(v.w(), v.x(), v.y(), v.z())));
/*     */ 
/*     */ 
/*     */   
/* 185 */   public static final Codec<Integer> STRING_RGB_COLOR = Codec.withAlternative(
/* 186 */       hexColor(6).xmap(ARGB::opaque, ARGB::transparent), RGB_COLOR_CODEC);
/*     */ 
/*     */   
/* 189 */   public static final Codec<Integer> STRING_ARGB_COLOR = Codec.withAlternative(
/* 190 */       hexColor(8), ARGB_COLOR_CODEC);
/*     */ 
/*     */ 
/*     */   
/* 194 */   public static final Codec<Integer> UNSIGNED_BYTE = Codec.BYTE.flatComapMap(UnsignedBytes::toInt, integer -> {
/* 195 */         if (integer.intValue() > 255) {
/* 196 */           return DataResult.error(());
/*     */         }
/* 198 */         return DataResult.success(Byte.valueOf(integer.byteValue()));
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <P, I> Codec<I> intervalCodec(Codec<P> pointCodec, String lowerBoundName, String upperBoundName, BiFunction<P, P, DataResult<I>> makeInterval, Function<I, P> getMin, Function<I, P> getMax) {
/* 207 */     Codec<I> arrayCodec = Codec.list(pointCodec).comapFlatMap(list -> 
/* 208 */         Util.fixedSize(list, 2).flatMap(()), p -> 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 213 */         ImmutableList.of(getMin.apply(p), getMax.apply(p)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     Codec<I> objectCodec = RecordCodecBuilder.create(i -> i.group(pointCodec.fieldOf(lowerBoundName).forGetter(Pair::getFirst), pointCodec.fieldOf(upperBoundName).forGetter(Pair::getSecond)).apply(i, Pair::of)).comapFlatMap(p -> (DataResult)makeInterval.apply(p.getFirst(), p.getSecond()), i -> Pair.of(getMin.apply(i), getMax.apply(i)));
/*     */     
/* 221 */     Codec<I> arrayOrObjectCodec = Codec.withAlternative(arrayCodec, objectCodec);
/*     */     
/* 223 */     return Codec.either(pointCodec, arrayOrObjectCodec).comapFlatMap(either -> 
/* 224 */         (DataResult)either.map((), DataResult::success), p -> {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 229 */           P min = (P)getMin.apply(p);
/* 230 */           P max = (P)getMax.apply(p);
/* 231 */           if (Objects.equals(min, max)) {
/* 232 */             return Either.left(min);
/*     */           }
/* 234 */           return Either.right(p);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static <A> Codec.ResultFunction<A> orElsePartial(final A value) {
/* 240 */     return new Codec.ResultFunction<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> a) {
/* 243 */           MutableObject<String> message = new MutableObject<String>();
/* 244 */           Objects.requireNonNull(message); Optional<Pair<A, T>> result = a.resultOrPartial(message::setValue);
/* 245 */           if (result.isPresent()) {
/* 246 */             return a;
/*     */           }
/* 248 */           return DataResult.error(() -> "(" + (String)message.get() + " -> using default)", Pair.of(value, input));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 253 */         public <T> DataResult<T> coApply(DynamicOps<T> ops, A input, DataResult<T> t) { return t; }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 258 */         public String toString() { return "OrElsePartial[" + String.valueOf(value) + "]"; }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> Codec<E> idResolverCodec(ToIntFunction<E> toInt, IntFunction<E> fromInt, int unknownId) {
/* 264 */     return Codec.INT.flatXmap(id -> 
/* 265 */         (DataResult)Optional.ofNullable(fromInt.apply(id.intValue())).map(DataResult::success).orElseGet(()), e -> {
/*     */           
/* 267 */           int id = toInt.applyAsInt(e);
/* 268 */           return (id == unknownId) ? DataResult.error(()) : DataResult.success(Integer.valueOf(id));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static <I, E> Codec<E> idResolverCodec(Codec<I> value, Function<I, E> fromId, Function<E, I> toId) {
/* 274 */     return value.flatXmap(id -> {
/*     */           
/* 276 */           E element = (E)fromId.apply(id);
/* 277 */           return (element == null) ? DataResult.error(()) : DataResult.success(element);
/*     */         }e -> {
/*     */           
/* 280 */           I id = (I)toId.apply(e);
/* 281 */           if (id == null) {
/* 282 */             return DataResult.error(());
/*     */           }
/* 284 */           return DataResult.success(id);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> Codec<E> orCompressed(final Codec<E> normal, final Codec<E> compressed) {
/* 290 */     return new Codec<E>()
/*     */       {
/*     */         public <T> DataResult<T> encode(E input, DynamicOps<T> ops, T prefix) {
/* 293 */           if (ops.compressMaps()) {
/* 294 */             return compressed.encode(input, ops, prefix);
/*     */           }
/* 296 */           return normal.encode(input, ops, prefix);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input) {
/* 301 */           if (ops.compressMaps()) {
/* 302 */             return compressed.decode(ops, input);
/*     */           }
/* 304 */           return normal.decode(ops, input);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 309 */         public String toString() { return String.valueOf(normal) + " orCompressed " + String.valueOf(normal); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> MapCodec<E> orCompressed(final MapCodec<E> normal, final MapCodec<E> compressed) {
/* 315 */     return new MapCodec<E>()
/*     */       {
/*     */         public <T> RecordBuilder<T> encode(E input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 318 */           if (ops.compressMaps()) {
/* 319 */             return compressed.encode(input, ops, prefix);
/*     */           }
/* 321 */           return normal.encode(input, ops, prefix);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 326 */           if (ops.compressMaps()) {
/* 327 */             return compressed.decode(ops, input);
/*     */           }
/* 329 */           return normal.decode(ops, input);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 334 */         public <T> Stream<T> keys(DynamicOps<T> ops) { return compressed.keys(ops); }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 339 */         public String toString() { return String.valueOf(normal) + " orCompressed " + String.valueOf(normal); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> Codec<E> overrideLifecycle(Codec<E> codec, final Function<E, Lifecycle> decodeLifecycle, final Function<E, Lifecycle> encodeLifecycle) {
/* 345 */     return codec.mapResult(new Codec.ResultFunction<E>()
/*     */         {
/*     */           public <T> DataResult<Pair<E, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<E, T>> a) {
/* 348 */             return (DataResult)a.result().map(r -> a.setLifecycle((Lifecycle)decodeLifecycle.apply(r.getFirst()))).orElse(a);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 353 */           public <T> DataResult<T> coApply(DynamicOps<T> ops, E input, DataResult<T> t) { return t.setLifecycle((Lifecycle)encodeLifecycle.apply(input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 358 */           public String toString() { return "WithLifecycle[" + String.valueOf(decodeLifecycle) + " " + String.valueOf(encodeLifecycle) + "]"; }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 364 */   public static <E> Codec<E> overrideLifecycle(Codec<E> codec, Function<E, Lifecycle> lifecycleGetter) { return overrideLifecycle(codec, lifecycleGetter, lifecycleGetter); }
/*     */ 
/*     */ 
/*     */   
/* 368 */   public static <K, V> StrictUnboundedMapCodec<K, V> strictUnboundedMap(Codec<K> keyCodec, Codec<V> elementCodec) { return new StrictUnboundedMapCodec(keyCodec, elementCodec); }
/*     */ 
/*     */ 
/*     */   
/* 372 */   public static <E> Codec<List<E>> compactListCodec(Codec<E> elementCodec) { return compactListCodec(elementCodec, elementCodec.listOf()); }
/*     */ 
/*     */   
/*     */   public static <E> Codec<List<E>> compactListCodec(Codec<E> elementCodec, Codec<List<E>> listCodec) {
/* 376 */     return Codec.either(listCodec, elementCodec)
/*     */ 
/*     */       
/* 379 */       .xmap(e -> 
/* 380 */         (List)e.map((), List::of), v -> 
/* 381 */         (v.size() == 1) ? Either.right(v.getFirst()) : Either.left(v));
/*     */   }
/*     */   
/*     */   public static final class StrictUnboundedMapCodec<K, V>
/*     */     extends Record implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
/*     */     private final Codec<K> keyCodec;
/*     */     private final Codec<V> elementCodec;
/*     */     
/* 389 */     public StrictUnboundedMapCodec(Codec<K> keyCodec, Codec<V> elementCodec) { this.keyCodec = keyCodec; this.elementCodec = elementCodec; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #389	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec<TK;TV;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #389	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 389 */       //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$StrictUnboundedMapCodec<TK;TV;>; } public Codec<K> keyCodec() { return this.keyCodec; } public Codec<V> elementCodec() { return this.elementCodec; }
/*     */     
/*     */     public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 392 */       ImmutableMap.Builder<K, V> read = ImmutableMap.builder();
/*     */       
/* 394 */       for (Pair<T, T> pair : input.entries().toList()) {
/* 395 */         DataResult<K> k = keyCodec().parse(ops, pair.getFirst());
/* 396 */         DataResult<V> v = elementCodec().parse(ops, pair.getSecond());
/* 397 */         DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
/* 398 */         Optional<DataResult.Error<Pair<K, V>>> error = entry.error();
/* 399 */         if (error.isPresent()) {
/* 400 */           String errorMessage = ((DataResult.Error)error.get()).message();
/* 401 */           return DataResult.error(() -> {
/* 402 */                 if (k.result().isPresent()) {
/* 403 */                   return "Map entry '" + String.valueOf(k.result().get()) + "' : " + errorMessage;
/*     */                 }
/* 405 */                 return errorMessage;
/*     */               });
/*     */         } 
/*     */         
/* 409 */         if (entry.result().isPresent()) {
/* 410 */           Pair<K, V> kvPair = (Pair)entry.result().get();
/* 411 */           read.put(kvPair.getFirst(), kvPair.getSecond()); continue;
/*     */         } 
/* 413 */         return DataResult.error(() -> "Empty or invalid map contents are not allowed");
/*     */       } 
/*     */       
/* 416 */       ImmutableMap immutableMap = read.build();
/* 417 */       return DataResult.success(immutableMap);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 422 */     public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) { return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 427 */     public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) { return encode(input, ops, ops.mapBuilder()).build(prefix); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 432 */     public String toString() { return "StrictUnboundedMapCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]"; }
/*     */   }
/*     */ 
/*     */   
/*     */   private static Codec<Integer> intRangeWithMessage(int minInclusive, int maxInclusive, Function<Integer, String> error) {
/* 437 */     return Codec.INT.validate(value -> {
/* 438 */           if (value.compareTo(Integer.valueOf(minInclusive)) >= 0 && value.compareTo(Integer.valueOf(maxInclusive)) <= 0) {
/* 439 */             return DataResult.success(value);
/*     */           }
/* 441 */           return DataResult.error(());
/*     */         });
/*     */   }
/*     */   
/* 445 */   public static final Codec<Integer> NON_NEGATIVE_INT = intRangeWithMessage(0, 2147483647, n -> "Value must be non-negative: " + n);
/* 446 */   public static final Codec<Integer> POSITIVE_INT = intRangeWithMessage(1, 2147483647, n -> "Value must be positive: " + n);
/*     */ 
/*     */   
/* 449 */   public static Codec<Integer> intRange(int minInclusive, int maxInclusive) { return intRangeWithMessage(minInclusive, maxInclusive, n -> "Value must be within range [" + minInclusive + ";" + maxInclusive + "]: " + n); }
/*     */ 
/*     */   
/*     */   private static Codec<Long> longRangeWithMessage(long minInclusive, long maxInclusive, Function<Long, String> error) {
/* 453 */     return Codec.LONG.validate(value -> {
/* 454 */           if (value.compareTo(Long.valueOf(minInclusive)) >= 0L && value.compareTo(Long.valueOf(maxInclusive)) <= 0L) {
/* 455 */             return DataResult.success(value);
/*     */           }
/* 457 */           return DataResult.error(());
/*     */         });
/*     */   }
/*     */   
/* 461 */   public static final Codec<Long> NON_NEGATIVE_LONG = longRangeWithMessage(0L, Float.MAX_VALUE, n -> "Value must be non-negative: " + n);
/* 462 */   public static final Codec<Long> POSITIVE_LONG = longRangeWithMessage(1L, Float.MAX_VALUE, n -> "Value must be positive: " + n);
/*     */ 
/*     */   
/* 465 */   public static Codec<Long> longRange(int minInclusive, int maxInclusive) { return longRangeWithMessage(minInclusive, maxInclusive, n -> "Value must be within range [" + minInclusive + ";" + maxInclusive + "]: " + n); }
/*     */ 
/*     */   
/*     */   private static Codec<Float> floatRangeMinInclusiveWithMessage(float minInclusive, float maxInclusive, Function<Float, String> error) {
/* 469 */     return Codec.FLOAT.validate(value -> {
/* 470 */           if (value.compareTo(Float.valueOf(minInclusive)) >= 0 && value.compareTo(Float.valueOf(maxInclusive)) <= 0) {
/* 471 */             return DataResult.success(value);
/*     */           }
/* 473 */           return DataResult.error(());
/*     */         });
/*     */   }
/*     */   
/*     */   private static Codec<Float> floatRangeMinExclusiveWithMessage(float minExclusive, float maxInclusive, Function<Float, String> error) {
/* 478 */     return Codec.FLOAT.validate(value -> {
/* 479 */           if (value.compareTo(Float.valueOf(minExclusive)) > 0 && value.compareTo(Float.valueOf(maxInclusive)) <= 0) {
/* 480 */             return DataResult.success(value);
/*     */           }
/* 482 */           return DataResult.error(());
/*     */         });
/*     */   }
/*     */   
/* 486 */   public static final Codec<Float> NON_NEGATIVE_FLOAT = floatRangeMinInclusiveWithMessage(0.0F, Float.MAX_VALUE, n -> "Value must be non-negative: " + n);
/* 487 */   public static final Codec<Float> POSITIVE_FLOAT = floatRangeMinExclusiveWithMessage(0.0F, Float.MAX_VALUE, n -> "Value must be positive: " + n);
/*     */ 
/*     */   
/* 490 */   public static Codec<Float> floatRange(float minInclusive, float maxInclusive) { return floatRangeMinInclusiveWithMessage(minInclusive, maxInclusive, n -> "Value must be within range [" + minInclusive + ";" + maxInclusive + "]: " + n); }
/*     */ 
/*     */ 
/*     */   
/* 494 */   public static <T> Codec<List<T>> nonEmptyList(Codec<List<T>> listCodec) { return listCodec.validate(list -> list.isEmpty() ? DataResult.error(()) : DataResult.success(list)); }
/*     */ 
/*     */   
/*     */   public static <T> Codec<HolderSet<T>> nonEmptyHolderSet(Codec<HolderSet<T>> listCodec) {
/* 498 */     return listCodec.validate(list -> {
/* 499 */           if (list.unwrap().right().filter(List::isEmpty).isPresent()) {
/* 500 */             return DataResult.error(());
/*     */           }
/*     */           
/* 503 */           return DataResult.success(list);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/* 508 */   public static <M extends Map<?, ?>> Codec<M> nonEmptyMap(Codec<M> mapCodec) { return mapCodec.validate(map -> map.isEmpty() ? DataResult.error(()) : DataResult.success(map)); }
/*     */ 
/*     */   
/*     */   public static <E> MapCodec<E> retrieveContext(final Function<DynamicOps<?>, DataResult<E>> getter) {
/*     */     class ContextRetrievalCodec
/*     */       extends MapCodec<E>
/*     */     {
/* 515 */       public <T> RecordBuilder<T> encode(E input, DynamicOps<T> ops, RecordBuilder<T> prefix) { return prefix; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 520 */       public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) { return (DataResult)getter.apply(ops); }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 525 */       public String toString() { return "ContextRetrievalCodec[" + String.valueOf(getter) + "]"; }
/*     */ 
/*     */ 
/*     */       
/*     */       public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 530 */         return Stream.empty();
/*     */       }
/*     */     };
/*     */     
/* 534 */     return new ContextRetrievalCodec();
/*     */   }
/*     */   
/*     */   public static <E, L extends Collection<E>, T> Function<L, DataResult<L>> ensureHomogenous(Function<E, T> typeGetter) {
/* 538 */     return container -> {
/* 539 */         Iterator<E> it = container.iterator();
/* 540 */         if (it.hasNext()) {
/* 541 */           T firstType = (T)typeGetter.apply(it.next());
/* 542 */           while (it.hasNext()) {
/* 543 */             E next = (E)it.next();
/* 544 */             T nextType = (T)typeGetter.apply(next);
/* 545 */             if (nextType != firstType) {
/* 546 */               return DataResult.error(());
/*     */             }
/*     */           } 
/*     */         } 
/* 550 */         return DataResult.success(container, Lifecycle.stable());
/*     */       };
/*     */   }
/*     */   
/* 554 */   public static final Codec<Pattern> PATTERN = Codec.STRING.comapFlatMap(pattern -> {
/*     */         try {
/* 556 */           return DataResult.success(Pattern.compile(pattern));
/* 557 */         } catch (PatternSyntaxException e) {
/* 558 */           return DataResult.error(());
/*     */         } 
/*     */       }Pattern::pattern);
/*     */   
/*     */   public static <A> Codec<A> catchDecoderException(final Codec<A> codec) {
/* 563 */     return Codec.of(codec, new Decoder<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*     */             try {
/* 567 */               return codec.decode(ops, input);
/* 568 */             } catch (Exception e) {
/* 569 */               return DataResult.error(() -> "Caught exception decoding " + String.valueOf(input) + ": " + e.getMessage());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 582 */   public static Codec<TemporalAccessor> temporalCodec(DateTimeFormatter formatter) { Objects.requireNonNull(formatter); return Codec.STRING.comapFlatMap(s -> { try { return DataResult.success(formatter.parse(s)); } catch (Exception e) { Objects.requireNonNull(e); return DataResult.error(e::getMessage); }  }formatter::format); }
/*     */ 
/*     */   
/* 585 */   public static final Codec<Instant> INSTANT_ISO8601 = temporalCodec(DateTimeFormatter.ISO_INSTANT).xmap(Instant::from, Function.identity());
/*     */   
/* 587 */   public static final Codec<byte[]> BASE64_STRING = Codec.STRING.comapFlatMap(string -> {
/*     */         
/*     */         try {
/* 590 */           return DataResult.success(Base64.getDecoder().decode(string));
/* 591 */         } catch (IllegalArgumentException e) {
/* 592 */           return DataResult.error(());
/*     */         }
/*     */       
/* 595 */       }bytes -> Base64.getEncoder().encodeToString(bytes));
/*     */ 
/*     */   
/* 598 */   public static final Codec<String> ESCAPED_STRING = Codec.STRING.comapFlatMap(str -> DataResult.success(StringEscapeUtils.unescapeJava(str)), StringEscapeUtils::escapeJava);
/*     */   public static final class TagOrElementLocation extends Record { private final Identifier id; private final boolean tag;
/* 600 */     public TagOrElementLocation(Identifier id, boolean tag) { this.id = id; this.tag = tag; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #600	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #600	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/ExtraCodecs$TagOrElementLocation;
/* 600 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier id() { return this.id; } public boolean tag() { return this.tag; }
/*     */ 
/*     */     
/* 603 */     public String toString() { return decoratedId(); }
/*     */ 
/*     */ 
/*     */     
/* 607 */     private String decoratedId() { return this.tag ? ("#" + String.valueOf(this.id)) : this.id.toString(); } }
/*     */ 
/*     */ 
/*     */   
/* 611 */   public static final Codec<TagOrElementLocation> TAG_OR_ELEMENT_ID = Codec.STRING.comapFlatMap(name -> 
/* 612 */       name.startsWith("#") ? 
/* 613 */       Identifier.read(name.substring(1)).map(()) : 
/* 614 */       Identifier.read(name).map(()), TagOrElementLocation::decoratedId);
/*     */ 
/*     */ 
/*     */   
/* 618 */   public static final Function<Optional<Long>, OptionalLong> toOptionalLong = o -> (OptionalLong)o.map(OptionalLong::of).orElseGet(OptionalLong::empty);
/* 619 */   public static final Function<OptionalLong, Optional<Long>> fromOptionalLong = l -> l.isPresent() ? Optional.of(Long.valueOf(l.getAsLong())) : Optional.empty();
/*     */ 
/*     */   
/* 622 */   public static MapCodec<OptionalLong> asOptionalLong(MapCodec<Optional<Long>> fieldCodec) { return fieldCodec.xmap(toOptionalLong, fromOptionalLong); }
/*     */ 
/*     */   
/* 625 */   public static final Codec<BitSet> BIT_SET = Codec.LONG_STREAM.xmap(longStream -> 
/* 626 */       BitSet.valueOf(longStream.toArray()), bitSet -> 
/* 627 */       Arrays.stream(bitSet.toLongArray()));
/*     */   
/*     */   public static final int MAX_PROPERTY_NAME_LENGTH = 64;
/*     */   
/*     */   public static final int MAX_PROPERTY_VALUE_LENGTH = 32767;
/*     */   
/*     */   public static final int MAX_PROPERTY_SIGNATURE_LENGTH = 1024;
/*     */   public static final int MAX_PROPERTIES = 16;
/* 635 */   private static final Codec<Property> PROPERTY = RecordCodecBuilder.create(i -> i.group(
/* 636 */         Codec.sizeLimitedString(64).fieldOf("name").forGetter(Property::name), 
/* 637 */         Codec.sizeLimitedString(32767).fieldOf("value").forGetter(Property::value), 
/* 638 */         Codec.sizeLimitedString(1024).optionalFieldOf("signature").forGetter(()))
/* 639 */       .apply(i, ()));
/*     */   
/* 641 */   public static final Codec<PropertyMap> PROPERTY_MAP = Codec.either(
/* 642 */       Codec.unboundedMap(Codec.STRING, Codec.STRING.listOf()).validate(map -> 
/* 643 */         (map.size() > 16) ? 
/* 644 */         DataResult.error(()) : 
/* 645 */         DataResult.success(map)), PROPERTY
/*     */       
/* 647 */       .sizeLimitedListOf(16))
/* 648 */     .xmap(mapListEither -> {
/* 649 */         ImmutableMultimap.Builder<String, Property> result = ImmutableMultimap.builder();
/*     */         
/* 651 */         mapListEither.ifLeft(())
/*     */ 
/*     */ 
/*     */           
/* 655 */           .ifRight(());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 661 */         return new PropertyMap(result.build());
/* 662 */       }propertyMap -> Either.right(propertyMap.values().stream().toList()));
/*     */   
/* 664 */   public static final Codec<String> PLAYER_NAME = Codec.string(0, 16).validate(name -> {
/* 665 */         if (StringUtil.isValidPlayerName(name)) {
/* 666 */           return DataResult.success(name);
/*     */         }
/* 668 */         return DataResult.error(());
/*     */       });
/*     */ 
/*     */   
/* 672 */   private static MapCodec<GameProfile> gameProfileCodec(Codec<UUID> uuidCodec) { return RecordCodecBuilder.mapCodec(i -> i.group(uuidCodec
/* 673 */           .fieldOf("id").forGetter(GameProfile::id), PLAYER_NAME
/* 674 */           .fieldOf("name").forGetter(GameProfile::name), PROPERTY_MAP
/* 675 */           .optionalFieldOf("properties", PropertyMap.EMPTY).forGetter(GameProfile::properties))
/* 676 */         .apply(i, GameProfile::new)); }
/*     */ 
/*     */   
/* 679 */   public static final Codec<GameProfile> AUTHLIB_GAME_PROFILE = gameProfileCodec(UUIDUtil.AUTHLIB_CODEC).codec();
/*     */   
/* 681 */   public static final MapCodec<GameProfile> STORED_GAME_PROFILE = gameProfileCodec(UUIDUtil.CODEC);
/*     */   
/* 683 */   public static final Codec<String> NON_EMPTY_STRING = Codec.STRING.validate(value -> value.isEmpty() ? DataResult.error(()) : DataResult.success(value));
/*     */   
/* 685 */   public static final Codec<Integer> CODEPOINT = Codec.STRING.comapFlatMap(s -> {
/*     */         
/* 687 */         int[] codepoint = s.codePoints().toArray();
/* 688 */         if (codepoint.length != 1) {
/* 689 */           return DataResult.error(());
/*     */         }
/* 691 */         return DataResult.success(Integer.valueOf(codepoint[0]));
/*     */       }Character::toString);
/*     */   
/*     */   public static <K, V> Codec<Map<K, V>> sizeLimitedMap(Codec<Map<K, V>> codec, int maxSizeInclusive) {
/* 695 */     return codec.validate(map -> {
/* 696 */           if (map.size() > maxSizeInclusive) {
/* 697 */             return DataResult.error(());
/*     */           }
/* 699 */           return DataResult.success(map);
/*     */         });
/*     */   }
/*     */   
/* 703 */   public static final Codec<String> RESOURCE_PATH_CODEC = Codec.STRING.validate(s -> {
/* 704 */         if (!Identifier.isValidPath(s)) {
/* 705 */           return DataResult.error(());
/*     */         }
/* 707 */         return DataResult.success(s);
/*     */       });
/*     */   
/* 710 */   public static final Codec<URI> UNTRUSTED_URI = Codec.STRING.comapFlatMap(string -> {
/*     */         
/*     */         try {
/* 713 */           return DataResult.success(Util.parseAndValidateUntrustedUri(string));
/* 714 */         } catch (URISyntaxException e) {
/* 715 */           Objects.requireNonNull(e); return DataResult.error(e::getMessage);
/*     */         } 
/*     */       }URI::toString);
/*     */ 
/*     */ 
/*     */   
/* 721 */   public static final Codec<String> CHAT_STRING = Codec.STRING.validate(string -> {
/* 722 */         for (int i = 0; i < string.length(); i++) {
/* 723 */           char c = string.charAt(i);
/* 724 */           if (!StringUtil.isAllowedChatCharacter(c)) {
/* 725 */             return DataResult.error(());
/*     */           }
/*     */         } 
/* 728 */         return DataResult.success(string);
/*     */       });
/*     */ 
/*     */   
/* 732 */   public static <T> Codec<Object2BooleanMap<T>> object2BooleanMap(Codec<T> keyCodec) { return Codec.unboundedMap(keyCodec, Codec.BOOL).xmap(it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap::new, it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap::new); }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <K, V> MapCodec<V> dispatchOptionalValue(final String typeKey, final String valueKey, final Codec<K> typeCodec, final Function<? super V, ? extends K> typeGetter, final Function<? super K, ? extends Codec<? extends V>> valueCodec) {
/* 738 */     return new MapCodec<V>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 741 */           return Stream.of(new Object[] { ops.createString(typeKey), ops.createString(valueKey) });
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 746 */           T typeName = (T)input.get(typeKey);
/* 747 */           if (typeName == null) {
/* 748 */             return DataResult.error(() -> "Missing \"" + typeKey + "\" in: " + String.valueOf(input));
/*     */           }
/* 750 */           return typeCodec.decode(ops, typeName).flatMap(type -> {
/* 751 */                 Objects.requireNonNull(ops); T value = (T)Objects.requireNonNullElseGet(input.get(valueKey), ops::emptyMap);
/* 752 */                 return ((Codec)valueCodec.apply(type.getFirst())).decode(ops, value).map(Pair::getFirst);
/*     */               });
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> builder) {
/* 758 */           K type = (K)typeGetter.apply(input);
/* 759 */           builder.add(typeKey, typeCodec.encodeStart(ops, type));
/* 760 */           DataResult<T> parameters = encode((Codec)valueCodec.apply(type), input, ops);
/* 761 */           if (parameters.result().isEmpty() || !Objects.equals(parameters.result().get(), ops.emptyMap())) {
/* 762 */             builder.add(valueKey, parameters);
/*     */           }
/* 764 */           return builder;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 769 */         private <T, V2 extends V> DataResult<T> encode(Codec<V2> codec, V input, DynamicOps<T> ops) { return codec.encodeStart(ops, input); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <A> Codec<Optional<A>> optionalEmptyMap(final Codec<A> codec) {
/* 775 */     return new Codec<Optional<A>>()
/*     */       {
/*     */         public <T> DataResult<Pair<Optional<A>, T>> decode(DynamicOps<T> ops, T input) {
/* 778 */           if (ExtraCodecs.null.isEmptyMap(ops, input)) {
/* 779 */             return DataResult.success(Pair.of(Optional.empty(), input));
/*     */           }
/* 781 */           return codec.decode(ops, input).map(pair -> pair.mapFirst(Optional::of));
/*     */         }
/*     */         
/*     */         private static <T> boolean isEmptyMap(DynamicOps<T> ops, T input) {
/* 785 */           Optional<MapLike<T>> map = ops.getMap(input).result();
/* 786 */           return (map.isPresent() && ((MapLike)map.get()).entries().findAny().isEmpty());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<T> encode(Optional<A> input, DynamicOps<T> ops, T prefix) {
/* 791 */           if (input.isEmpty()) {
/* 792 */             return DataResult.success(ops.emptyMap());
/*     */           }
/* 794 */           return codec.encode(input.get(), ops, prefix);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 802 */   public static <E extends Enum<E>> Codec<E> legacyEnum(Function<String, E> valueOf) { return Codec.STRING.comapFlatMap(key -> {
/*     */           try {
/* 804 */             return DataResult.success((Enum)valueOf.apply(key));
/* 805 */           } catch (IllegalArgumentException ignored) {
/* 806 */             return DataResult.error(());
/*     */           } 
/*     */         }Enum::toString); }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class LateBoundIdMapper<I, V>
/*     */     extends Object
/*     */   {
/* 815 */     private final BiMap<I, V> idToValue = HashBiMap.create();
/*     */     
/*     */     public Codec<V> codec(Codec<I> idCodec) {
/* 818 */       BiMap<V, I> valueToId = this.idToValue.inverse();
/* 819 */       Objects.requireNonNull(this.idToValue); Objects.requireNonNull(valueToId); return ExtraCodecs.idResolverCodec(idCodec, this.idToValue::get, valueToId::get);
/*     */     }
/*     */ 
/*     */     
/*     */     public LateBoundIdMapper<I, V> put(I id, V value) {
/* 824 */       Objects.requireNonNull(value, () -> "Value for " + String.valueOf(id) + " is null");
/* 825 */       this.idToValue.put(id, value);
/* 826 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 830 */     public Set<V> values() { return Collections.unmodifiableSet(this.idToValue.values()); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\ExtraCodecs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */