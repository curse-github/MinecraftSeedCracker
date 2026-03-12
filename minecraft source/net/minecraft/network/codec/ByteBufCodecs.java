/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.EndTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.Utf8String;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.network.VarLong;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ByteBufCodecs
/*     */ {
/*     */   public static final int MAX_INITIAL_COLLECTION_SIZE = 65536;
/*     */   
/*  62 */   public static final StreamCodec<ByteBuf, Boolean> BOOL = new StreamCodec<ByteBuf, Boolean>()
/*     */     {
/*     */       public Boolean decode(ByteBuf input) {
/*  65 */         return Boolean.valueOf(input.readBoolean());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  70 */       public void encode(ByteBuf output, Boolean value) { output.writeBoolean(value.booleanValue()); }
/*     */     };
/*     */ 
/*     */   
/*  74 */   public static final StreamCodec<ByteBuf, Byte> BYTE = new StreamCodec<ByteBuf, Byte>()
/*     */     {
/*     */       public Byte decode(ByteBuf input) {
/*  77 */         return Byte.valueOf(input.readByte());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  82 */       public void encode(ByteBuf output, Byte value) { output.writeByte(value.byteValue()); }
/*     */     };
/*     */ 
/*     */   
/*  86 */   public static final StreamCodec<ByteBuf, Float> ROTATION_BYTE = BYTE.map(Mth::unpackDegrees, Mth::packDegrees);
/*     */   
/*  88 */   public static final StreamCodec<ByteBuf, Short> SHORT = new StreamCodec<ByteBuf, Short>()
/*     */     {
/*     */       public Short decode(ByteBuf input) {
/*  91 */         return Short.valueOf(input.readShort());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  96 */       public void encode(ByteBuf output, Short value) { output.writeShort(value.shortValue()); }
/*     */     };
/*     */ 
/*     */   
/* 100 */   public static final StreamCodec<ByteBuf, Integer> UNSIGNED_SHORT = new StreamCodec<ByteBuf, Integer>()
/*     */     {
/*     */       public Integer decode(ByteBuf input) {
/* 103 */         return Integer.valueOf(input.readUnsignedShort());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 108 */       public void encode(ByteBuf output, Integer value) { output.writeShort(value.intValue()); }
/*     */     };
/*     */ 
/*     */   
/* 112 */   public static final StreamCodec<ByteBuf, Integer> INT = new StreamCodec<ByteBuf, Integer>()
/*     */     {
/*     */       public Integer decode(ByteBuf input) {
/* 115 */         return Integer.valueOf(input.readInt());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 120 */       public void encode(ByteBuf output, Integer value) { output.writeInt(value.intValue()); }
/*     */     };
/*     */ 
/*     */   
/* 124 */   public static final StreamCodec<ByteBuf, Integer> VAR_INT = new StreamCodec<ByteBuf, Integer>()
/*     */     {
/*     */       public Integer decode(ByteBuf input) {
/* 127 */         return Integer.valueOf(VarInt.read(input));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 132 */       public void encode(ByteBuf output, Integer value) { VarInt.write(output, value.intValue()); }
/*     */     };
/*     */ 
/*     */   
/* 136 */   public static final StreamCodec<ByteBuf, OptionalInt> OPTIONAL_VAR_INT = VAR_INT.map(i -> 
/* 137 */       (i.intValue() == 0) ? OptionalInt.empty() : OptionalInt.of(i.intValue() - 1), o -> 
/* 138 */       Integer.valueOf(o.isPresent() ? (o.getAsInt() + 1) : 0));
/*     */ 
/*     */   
/* 141 */   public static final StreamCodec<ByteBuf, Long> LONG = new StreamCodec<ByteBuf, Long>()
/*     */     {
/*     */       public Long decode(ByteBuf input) {
/* 144 */         return Long.valueOf(input.readLong());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 149 */       public void encode(ByteBuf output, Long value) { output.writeLong(value.longValue()); }
/*     */     };
/*     */ 
/*     */   
/* 153 */   public static final StreamCodec<ByteBuf, Long> VAR_LONG = new StreamCodec<ByteBuf, Long>()
/*     */     {
/*     */       public Long decode(ByteBuf input) {
/* 156 */         return Long.valueOf(VarLong.read(input));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 161 */       public void encode(ByteBuf output, Long value) { VarLong.write(output, value.longValue()); }
/*     */     };
/*     */ 
/*     */   
/* 165 */   public static final StreamCodec<ByteBuf, Float> FLOAT = new StreamCodec<ByteBuf, Float>()
/*     */     {
/*     */       public Float decode(ByteBuf input) {
/* 168 */         return Float.valueOf(input.readFloat());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 173 */       public void encode(ByteBuf output, Float value) { output.writeFloat(value.floatValue()); }
/*     */     };
/*     */ 
/*     */   
/* 177 */   public static final StreamCodec<ByteBuf, Double> DOUBLE = new StreamCodec<ByteBuf, Double>()
/*     */     {
/*     */       public Double decode(ByteBuf input) {
/* 180 */         return Double.valueOf(input.readDouble());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 185 */       public void encode(ByteBuf output, Double value) { output.writeDouble(value.doubleValue()); }
/*     */     };
/*     */ 
/*     */   
/*     */   static StreamCodec<ByteBuf, byte[]> byteArray(final int maxSize) {
/* 190 */     return new StreamCodec<ByteBuf, byte[]>()
/*     */       {
/*     */         public byte[] decode(ByteBuf input) {
/* 193 */           return FriendlyByteBuf.readByteArray(input, maxSize);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(ByteBuf output, byte[] value) {
/* 198 */           if (value.length > maxSize) {
/* 199 */             throw new EncoderException("ByteArray with size " + value.length + " is bigger than allowed " + maxSize);
/*     */           }
/* 201 */           FriendlyByteBuf.writeByteArray(output, value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 206 */   public static final StreamCodec<ByteBuf, byte[]> BYTE_ARRAY = new StreamCodec<ByteBuf, byte[]>()
/*     */     {
/*     */       public byte[] decode(ByteBuf input) {
/* 209 */         return FriendlyByteBuf.readByteArray(input);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 214 */       public void encode(ByteBuf output, byte[] value) { FriendlyByteBuf.writeByteArray(output, value); }
/*     */     };
/*     */ 
/*     */   
/* 218 */   public static final StreamCodec<ByteBuf, long[]> LONG_ARRAY = new StreamCodec<ByteBuf, long[]>()
/*     */     {
/*     */       public long[] decode(ByteBuf input) {
/* 221 */         return FriendlyByteBuf.readLongArray(input);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 226 */       public void encode(ByteBuf output, long[] value) { FriendlyByteBuf.writeLongArray(output, value); }
/*     */     };
/*     */ 
/*     */   
/*     */   static StreamCodec<ByteBuf, String> stringUtf8(final int maxStringLength) {
/* 231 */     return new StreamCodec<ByteBuf, String>()
/*     */       {
/*     */         public String decode(ByteBuf input) {
/* 234 */           return Utf8String.read(input, maxStringLength);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 239 */         public void encode(ByteBuf output, String value) { Utf8String.write(output, value, maxStringLength); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 244 */   public static final StreamCodec<ByteBuf, String> STRING_UTF8 = stringUtf8(32767);
/*     */   
/*     */   static StreamCodec<ByteBuf, Optional<Tag>> optionalTagCodec(final Supplier<NbtAccounter> accounter) {
/* 247 */     return new StreamCodec<ByteBuf, Optional<Tag>>()
/*     */       {
/*     */         public Optional<Tag> decode(ByteBuf input) {
/* 250 */           return Optional.ofNullable(FriendlyByteBuf.readNbt(input, (NbtAccounter)accounter.get()));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 255 */         public void encode(ByteBuf output, Optional<Tag> value) { FriendlyByteBuf.writeNbt(output, (Tag)value.orElse(null)); }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   static StreamCodec<ByteBuf, Tag> tagCodec(final Supplier<NbtAccounter> accounter) {
/* 261 */     return new StreamCodec<ByteBuf, Tag>()
/*     */       {
/*     */         public Tag decode(ByteBuf input) {
/* 264 */           Tag result = FriendlyByteBuf.readNbt(input, (NbtAccounter)accounter.get());
/* 265 */           if (result == null) {
/* 266 */             throw new DecoderException("Expected non-null compound tag");
/*     */           }
/* 268 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(ByteBuf output, Tag value) {
/* 273 */           if (value == EndTag.INSTANCE) {
/* 274 */             throw new EncoderException("Expected non-null compound tag");
/*     */           }
/* 276 */           FriendlyByteBuf.writeNbt(output, value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 281 */   public static final StreamCodec<ByteBuf, Tag> TAG = tagCodec(NbtAccounter::defaultQuota);
/* 282 */   public static final StreamCodec<ByteBuf, Tag> TRUSTED_TAG = tagCodec(NbtAccounter::unlimitedHeap);
/*     */   
/*     */   static StreamCodec<ByteBuf, CompoundTag> compoundTagCodec(Supplier<NbtAccounter> accounter) {
/* 285 */     return tagCodec(accounter)
/* 286 */       .map(tag -> {
/* 287 */           if (tag instanceof CompoundTag) return (CompoundTag)tag;
/*     */ 
/*     */           
/* 290 */           throw new DecoderException("Not a compound tag: " + String.valueOf(tag));
/*     */         
/* 292 */         }compoundTag -> compoundTag);
/*     */   }
/*     */ 
/*     */   
/* 296 */   public static final StreamCodec<ByteBuf, CompoundTag> COMPOUND_TAG = compoundTagCodec(NbtAccounter::defaultQuota);
/* 297 */   public static final StreamCodec<ByteBuf, CompoundTag> TRUSTED_COMPOUND_TAG = compoundTagCodec(NbtAccounter::unlimitedHeap);
/*     */ 
/*     */   
/* 300 */   static <T> StreamCodec<ByteBuf, T> fromCodecTrusted(Codec<T> codec) { return fromCodec(codec, NbtAccounter::unlimitedHeap); }
/*     */ 
/*     */ 
/*     */   
/* 304 */   static <T> StreamCodec<ByteBuf, T> fromCodec(Codec<T> codec) { return fromCodec(codec, NbtAccounter::defaultQuota); }
/*     */ 
/*     */   
/*     */   static <T, B extends ByteBuf, V> StreamCodec.CodecOperation<B, T, V> fromCodec(final DynamicOps<T> ops, final Codec<V> codec) {
/* 308 */     return original -> new StreamCodec<B, V>()
/*     */       {
/*     */         public V decode(B input) {
/* 311 */           T payload = (T)original.decode(input);
/* 312 */           return (V)codec.parse(ops, payload).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + String.valueOf(payload)));
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, V value) {
/* 317 */           T payload = (T)codec.encodeStart(ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + String.valueOf(value)));
/* 318 */           original.encode(output, payload);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 324 */   static <T> StreamCodec<ByteBuf, T> fromCodec(Codec<T> codec, Supplier<NbtAccounter> accounter) { return tagCodec(accounter).apply(fromCodec(NbtOps.INSTANCE, codec)); }
/*     */ 
/*     */ 
/*     */   
/* 328 */   static <T> StreamCodec<RegistryFriendlyByteBuf, T> fromCodecWithRegistriesTrusted(Codec<T> codec) { return fromCodecWithRegistries(codec, NbtAccounter::unlimitedHeap); }
/*     */ 
/*     */ 
/*     */   
/* 332 */   static <T> StreamCodec<RegistryFriendlyByteBuf, T> fromCodecWithRegistries(Codec<T> codec) { return fromCodecWithRegistries(codec, NbtAccounter::defaultQuota); }
/*     */ 
/*     */   
/*     */   static <T> StreamCodec<RegistryFriendlyByteBuf, T> fromCodecWithRegistries(final Codec<T> codec, Supplier<NbtAccounter> accounter) {
/* 336 */     final StreamCodec<ByteBuf, Tag> tagCodec = tagCodec(accounter);
/* 337 */     return new StreamCodec<RegistryFriendlyByteBuf, T>()
/*     */       {
/*     */         public T decode(RegistryFriendlyByteBuf input) {
/* 340 */           Tag tag = (Tag)tagCodec.decode(input);
/* 341 */           RegistryOps<Tag> ops = input.registryAccess().createSerializationContext(NbtOps.INSTANCE);
/* 342 */           return (T)codec.parse(ops, tag).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + String.valueOf(tag)));
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(RegistryFriendlyByteBuf output, T value) {
/* 347 */           RegistryOps<Tag> ops = output.registryAccess().createSerializationContext(NbtOps.INSTANCE);
/* 348 */           Tag tag = (Tag)codec.encodeStart(ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + String.valueOf(value)));
/* 349 */           tagCodec.encode(output, tag);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 354 */   public static final StreamCodec<ByteBuf, Optional<CompoundTag>> OPTIONAL_COMPOUND_TAG = new StreamCodec<ByteBuf, Optional<CompoundTag>>()
/*     */     {
/*     */       public Optional<CompoundTag> decode(ByteBuf input) {
/* 357 */         return Optional.ofNullable(FriendlyByteBuf.readNbt(input));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 362 */       public void encode(ByteBuf output, Optional<CompoundTag> value) { FriendlyByteBuf.writeNbt(output, (Tag)value.orElse(null)); }
/*     */     };
/*     */ 
/*     */   
/* 366 */   public static final StreamCodec<ByteBuf, Vector3fc> VECTOR3F = new StreamCodec<ByteBuf, Vector3fc>()
/*     */     {
/*     */       public Vector3fc decode(ByteBuf input) {
/* 369 */         return FriendlyByteBuf.readVector3f(input);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 374 */       public void encode(ByteBuf output, Vector3fc value) { FriendlyByteBuf.writeVector3f(output, value); }
/*     */     };
/*     */ 
/*     */   
/* 378 */   public static final StreamCodec<ByteBuf, Quaternionfc> QUATERNIONF = new StreamCodec<ByteBuf, Quaternionfc>()
/*     */     {
/*     */       public Quaternionfc decode(ByteBuf input) {
/* 381 */         return FriendlyByteBuf.readQuaternion(input);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 386 */       public void encode(ByteBuf output, Quaternionfc value) { FriendlyByteBuf.writeQuaternion(output, value); }
/*     */     };
/*     */ 
/*     */   
/* 390 */   public static final StreamCodec<ByteBuf, Integer> CONTAINER_ID = new StreamCodec<ByteBuf, Integer>()
/*     */     {
/*     */       public Integer decode(ByteBuf input) {
/* 393 */         return Integer.valueOf(FriendlyByteBuf.readContainerId(input));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 398 */       public void encode(ByteBuf output, Integer value) { FriendlyByteBuf.writeContainerId(output, value.intValue()); }
/*     */     };
/*     */ 
/*     */   
/*     */   static <B extends ByteBuf, V> StreamCodec<B, Optional<V>> optional(final StreamCodec<? super B, V> original) {
/* 403 */     return new StreamCodec<B, Optional<V>>()
/*     */       {
/*     */         public Optional<V> decode(B input) {
/* 406 */           if (input.readBoolean()) {
/* 407 */             return Optional.of(original.decode(input));
/*     */           }
/* 409 */           return Optional.empty();
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, Optional<V> value) {
/* 414 */           if (value.isPresent()) {
/* 415 */             output.writeBoolean(true);
/* 416 */             original.encode(output, value.get());
/*     */           } else {
/* 418 */             output.writeBoolean(false);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static int readCount(ByteBuf input, int maxSize) {
/* 425 */     int count = VarInt.read(input);
/* 426 */     if (count > maxSize) {
/* 427 */       throw new DecoderException("" + count + " elements exceeded max size of: " + count);
/*     */     }
/* 429 */     return count;
/*     */   }
/*     */   
/*     */   static void writeCount(ByteBuf output, int count, int maxSize) {
/* 433 */     if (count > maxSize) {
/* 434 */       throw new EncoderException("" + count + " elements exceeded max size of: " + count);
/*     */     }
/* 436 */     VarInt.write(output, count);
/*     */   }
/*     */ 
/*     */   
/* 440 */   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec<B, C> collection(IntFunction<C> constructor, StreamCodec<? super B, V> elementCodec) { return collection(constructor, elementCodec, 2147483647); }
/*     */ 
/*     */   
/*     */   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec<B, C> collection(final IntFunction<C> constructor, final StreamCodec<? super B, V> elementCodec, final int maxSize) {
/* 444 */     return new StreamCodec<B, C>()
/*     */       {
/*     */         public C decode(B input) {
/* 447 */           int count = ByteBufCodecs.readCount(input, maxSize);
/* 448 */           C result = (C)(Collection)constructor.apply(Math.min(count, 65536));
/* 449 */           for (int i = 0; i < count; i++) {
/* 450 */             result.add(elementCodec.decode(input));
/*     */           }
/* 452 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, C value) {
/* 457 */           ByteBufCodecs.writeCount(output, value.size(), maxSize);
/* 458 */           for (V element : value) {
/* 459 */             elementCodec.encode(output, element);
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 466 */   static <B extends ByteBuf, V, C extends Collection<V>> StreamCodec.CodecOperation<B, V, C> collection(IntFunction<C> constructor) { return original -> collection(constructor, original); }
/*     */ 
/*     */ 
/*     */   
/* 470 */   static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, List<V>> list() { return original -> collection(ArrayList::new, original); }
/*     */ 
/*     */ 
/*     */   
/* 474 */   static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, List<V>> list(int maxSize) { return original -> collection(ArrayList::new, original, maxSize); }
/*     */ 
/*     */ 
/*     */   
/* 478 */   static <B extends ByteBuf, K, V, M extends Map<K, V>> StreamCodec<B, M> map(IntFunction<? extends M> constructor, StreamCodec<? super B, K> keyCodec, StreamCodec<? super B, V> valueCodec) { return map(constructor, keyCodec, valueCodec, 2147483647); }
/*     */ 
/*     */   
/*     */   static <B extends ByteBuf, K, V, M extends Map<K, V>> StreamCodec<B, M> map(final IntFunction<? extends M> constructor, final StreamCodec<? super B, K> keyCodec, final StreamCodec<? super B, V> valueCodec, final int maxSize) {
/* 482 */     return new StreamCodec<B, M>()
/*     */       {
/*     */         public void encode(B output, M map) {
/* 485 */           ByteBufCodecs.writeCount(output, map.size(), maxSize);
/* 486 */           map.forEach((k, v) -> {
/* 487 */                 keyCodec.encode(output, k);
/* 488 */                 valueCodec.encode(output, v);
/*     */               });
/*     */         }
/*     */ 
/*     */         
/*     */         public M decode(B input) {
/* 494 */           int count = ByteBufCodecs.readCount(input, maxSize);
/* 495 */           M result = (M)(Map)constructor.apply(Math.min(count, 65536));
/* 496 */           for (int i = 0; i < count; i++) {
/* 497 */             K key = (K)keyCodec.decode(input);
/* 498 */             V value = (V)valueCodec.decode(input);
/* 499 */             result.put(key, value);
/*     */           } 
/* 501 */           return result;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B extends ByteBuf, L, R> StreamCodec<B, Either<L, R>> either(final StreamCodec<? super B, L> leftCodec, final StreamCodec<? super B, R> rightCodec) {
/* 507 */     return new StreamCodec<B, Either<L, R>>()
/*     */       {
/*     */         public Either<L, R> decode(B input) {
/* 510 */           if (input.readBoolean()) {
/* 511 */             return Either.left(leftCodec.decode(input));
/*     */           }
/* 513 */           return Either.right(rightCodec.decode(input));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void encode(B output, Either<L, R> value) {
/* 519 */           value.ifLeft(left -> {
/* 520 */                 output.writeBoolean(true);
/* 521 */                 leftCodec.encode(output, left);
/* 522 */               }).ifRight(right -> {
/* 523 */                 output.writeBoolean(false);
/* 524 */                 rightCodec.encode(output, right);
/*     */               });
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, V> lengthPrefixed(final int maxSize, final BiFunction<B, ByteBuf, B> decorator) {
/* 531 */     return original -> new StreamCodec<B, V>()
/*     */       {
/*     */         public V decode(B input) {
/* 534 */           int size = VarInt.read(input);
/* 535 */           if (size > maxSize) {
/* 536 */             throw new DecoderException("Buffer size " + size + " is larger than allowed limit of " + maxSize);
/*     */           }
/* 538 */           int index = input.readerIndex();
/* 539 */           B limitedSlice = (B)(ByteBuf)decorator.apply(input, input.slice(index, size));
/* 540 */           input.readerIndex(index + size);
/* 541 */           return (V)original.decode(limitedSlice);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(B output, V value) {
/* 546 */           scratchBuffer = (B)(ByteBuf)decorator.apply(output, output.alloc().buffer());
/*     */           try {
/* 548 */             original.encode(scratchBuffer, value);
/* 549 */             int size = scratchBuffer.readableBytes();
/* 550 */             if (size > maxSize) {
/* 551 */               throw new EncoderException("Buffer size " + size + " is  larger than allowed limit of " + maxSize);
/*     */             }
/* 553 */             VarInt.write(output, size);
/* 554 */             output.writeBytes(scratchBuffer);
/*     */           } finally {
/* 556 */             scratchBuffer.release();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 563 */   static <V> StreamCodec.CodecOperation<ByteBuf, V, V> lengthPrefixed(int maxSize) { return lengthPrefixed(maxSize, (parent, child) -> child); }
/*     */ 
/*     */ 
/*     */   
/* 567 */   static <V> StreamCodec.CodecOperation<RegistryFriendlyByteBuf, V, V> registryFriendlyLengthPrefixed(int maxSize) { return lengthPrefixed(maxSize, (parent, child) -> new RegistryFriendlyByteBuf(child, parent.registryAccess())); }
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> StreamCodec<ByteBuf, T> idMapper(final IntFunction<T> byId, final ToIntFunction<T> toId) {
/* 572 */     return new StreamCodec<ByteBuf, T>()
/*     */       {
/*     */         public T decode(ByteBuf input) {
/* 575 */           int id = VarInt.read(input);
/* 576 */           return (T)byId.apply(id);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(ByteBuf output, T value) {
/* 581 */           int id = toId.applyAsInt(value);
/* 582 */           VarInt.write(output, id);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 588 */   static <T> StreamCodec<ByteBuf, T> idMapper(IdMap<T> mapper) { Objects.requireNonNull(mapper); Objects.requireNonNull(mapper); return idMapper(mapper::byIdOrThrow, mapper::getIdOrThrow); }
/*     */ 
/*     */   
/*     */   private static <T, R> StreamCodec<RegistryFriendlyByteBuf, R> registry(final ResourceKey<? extends Registry<T>> registryKey, final Function<Registry<T>, IdMap<R>> mapExtractor) {
/* 592 */     return new StreamCodec<RegistryFriendlyByteBuf, R>()
/*     */       {
/* 594 */         private IdMap<R> getRegistryOrThrow(RegistryFriendlyByteBuf input) { return (IdMap)mapExtractor.apply(input.registryAccess().lookupOrThrow(registryKey)); }
/*     */ 
/*     */ 
/*     */         
/*     */         public R decode(RegistryFriendlyByteBuf input) {
/* 599 */           int id = VarInt.read(input);
/* 600 */           return (R)getRegistryOrThrow(input).byIdOrThrow(id);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(RegistryFriendlyByteBuf output, R value) {
/* 605 */           int id = getRegistryOrThrow(output).getIdOrThrow(value);
/* 606 */           VarInt.write(output, id);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/* 612 */   static <T> StreamCodec<RegistryFriendlyByteBuf, T> registry(ResourceKey<? extends Registry<T>> registryKey) { return registry(registryKey, r -> r); }
/*     */ 
/*     */ 
/*     */   
/* 616 */   static <T> StreamCodec<RegistryFriendlyByteBuf, Holder<T>> holderRegistry(ResourceKey<? extends Registry<T>> registryKey) { return registry(registryKey, Registry::asHolderIdMap); }
/*     */ 
/*     */   
/*     */   static <T> StreamCodec<RegistryFriendlyByteBuf, Holder<T>> holder(final ResourceKey<? extends Registry<T>> registryKey, final StreamCodec<? super RegistryFriendlyByteBuf, T> directCodec) {
/* 620 */     return new StreamCodec<RegistryFriendlyByteBuf, Holder<T>>()
/*     */       {
/*     */         private static final int DIRECT_HOLDER_ID = 0;
/*     */         
/* 624 */         private IdMap<Holder<T>> getRegistryOrThrow(RegistryFriendlyByteBuf input) { return input.registryAccess().lookupOrThrow(registryKey).asHolderIdMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public Holder<T> decode(RegistryFriendlyByteBuf input) {
/* 631 */           int id = VarInt.read(input);
/* 632 */           if (id == 0) {
/* 633 */             return Holder.direct(directCodec.decode(input));
/*     */           }
/* 635 */           return (Holder)getRegistryOrThrow(input).byIdOrThrow(id - 1);
/*     */         }
/*     */         
/*     */         public void encode(RegistryFriendlyByteBuf output, Holder<T> holder) {
/*     */           int id;
/* 640 */           switch (ByteBufCodecs.null.$SwitchMap$net$minecraft$core$Holder$Kind[holder.kind().ordinal()]) {
/*     */             case 1:
/* 642 */               id = getRegistryOrThrow(output).getIdOrThrow(holder);
/* 643 */               VarInt.write(output, id + 1);
/*     */               break;
/*     */             case 2:
/* 646 */               VarInt.write(output, 0);
/* 647 */               directCodec.encode(output, holder.value());
/*     */               break;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <T> StreamCodec<RegistryFriendlyByteBuf, HolderSet<T>> holderSet(final ResourceKey<? extends Registry<T>> registryKey) {
/* 655 */     return new StreamCodec<RegistryFriendlyByteBuf, HolderSet<T>>()
/*     */       {
/*     */         private static final int NAMED_SET = -1;
/* 658 */         private final StreamCodec<RegistryFriendlyByteBuf, Holder<T>> holderCodec = ByteBufCodecs.holderRegistry(registryKey);
/*     */ 
/*     */         
/*     */         public HolderSet<T> decode(RegistryFriendlyByteBuf input) {
/* 662 */           int count = VarInt.read(input) - 1;
/* 663 */           if (count == -1) {
/* 664 */             Registry<T> registry = input.registryAccess().lookupOrThrow(registryKey);
/* 665 */             return (HolderSet)registry.get(TagKey.create(registryKey, (Identifier)Identifier.STREAM_CODEC.decode(input))).orElseThrow();
/*     */           } 
/* 667 */           List<Holder<T>> holders = new ArrayList<Holder<T>>(Math.min(count, 65536));
/* 668 */           for (int i = 0; i < count; i++) {
/* 669 */             holders.add((Holder)this.holderCodec.decode(input));
/*     */           }
/* 671 */           return HolderSet.direct(holders);
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(RegistryFriendlyByteBuf output, HolderSet<T> value) {
/* 676 */           Optional<TagKey<T>> key = value.unwrapKey();
/* 677 */           if (key.isPresent()) {
/* 678 */             VarInt.write(output, 0);
/* 679 */             Identifier.STREAM_CODEC.encode(output, ((TagKey)key.get()).location());
/*     */           } else {
/* 681 */             VarInt.write(output, value.size() + 1);
/* 682 */             for (Holder<T> holder : value) {
/* 683 */               this.holderCodec.encode(output, holder);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 690 */   public static final StreamCodec<ByteBuf, PropertyMap> GAME_PROFILE_PROPERTIES = new StreamCodec<ByteBuf, PropertyMap>()
/*     */     {
/*     */       public PropertyMap decode(ByteBuf input) {
/* 693 */         int propertyCount = ByteBufCodecs.readCount(input, 16);
/* 694 */         ImmutableMultimap.Builder<String, Property> result = ImmutableMultimap.builder();
/* 695 */         for (int i = 0; i < propertyCount; i++) {
/* 696 */           String name = Utf8String.read(input, 64);
/* 697 */           String value = Utf8String.read(input, 32767);
/* 698 */           String signature = (String)FriendlyByteBuf.readNullable(input, in -> Utf8String.read(in, 1024));
/* 699 */           Property property = new Property(name, value, signature);
/* 700 */           result.put(property.name(), property);
/*     */         } 
/* 702 */         return new PropertyMap(result.build());
/*     */       }
/*     */ 
/*     */       
/*     */       public void encode(ByteBuf output, PropertyMap properties) {
/* 707 */         ByteBufCodecs.writeCount(output, properties.size(), 16);
/* 708 */         for (Property property : properties.values()) {
/* 709 */           Utf8String.write(output, property.name(), 64);
/* 710 */           Utf8String.write(output, property.value(), 32767);
/* 711 */           FriendlyByteBuf.writeNullable(output, property.signature(), (out, signature) -> Utf8String.write(out, signature, 1024));
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/* 716 */   public static final StreamCodec<ByteBuf, String> PLAYER_NAME = stringUtf8(16);
/*     */   
/* 718 */   public static final StreamCodec<ByteBuf, GameProfile> GAME_PROFILE = StreamCodec.composite(UUIDUtil.STREAM_CODEC, GameProfile::id, PLAYER_NAME, GameProfile::name, GAME_PROFILE_PROPERTIES, GameProfile::properties, GameProfile::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 725 */   public static final StreamCodec<ByteBuf, Integer> RGB_COLOR = new StreamCodec<ByteBuf, Integer>()
/*     */     {
/*     */       public Integer decode(ByteBuf input) {
/* 728 */         return Integer.valueOf(ARGB.color(input
/* 729 */               .readByte() & 0xFF, input
/* 730 */               .readByte() & 0xFF, input
/* 731 */               .readByte() & 0xFF));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void encode(ByteBuf output, Integer value) {
/* 737 */         output.writeByte(ARGB.red(value.intValue()));
/* 738 */         output.writeByte(ARGB.green(value.intValue()));
/* 739 */         output.writeByte(ARGB.blue(value.intValue()));
/*     */       }
/*     */     };
/*     */   
/*     */   static StreamCodec<ByteBuf, JsonElement> lenientJson(final int maxStringLength) {
/* 744 */     return new StreamCodec<ByteBuf, JsonElement>()
/*     */       {
/*     */         
/* 747 */         private static final Gson GSON = (new GsonBuilder()).disableHtmlEscaping().create();
/*     */ 
/*     */         
/*     */         public JsonElement decode(ByteBuf input) {
/* 751 */           String payload = Utf8String.read(input, maxStringLength);
/*     */           
/*     */           try {
/* 754 */             return LenientJsonParser.parse(payload);
/* 755 */           } catch (JsonSyntaxException e) {
/* 756 */             throw new DecoderException("Failed to parse JSON", e);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void encode(ByteBuf output, JsonElement value) {
/* 762 */           String payload = GSON.toJson(value);
/* 763 */           Utf8String.write(output, payload, maxStringLength);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\codec\ByteBufCodecs.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */