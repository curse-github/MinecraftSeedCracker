/*      */ package net.minecraft.network;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.gson.Gson;
/*      */ import com.google.gson.JsonElement;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.JsonOps;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import io.netty.handler.codec.DecoderException;
/*      */ import io.netty.handler.codec.EncoderException;
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*      */ import it.unimi.dsi.fastutil.ints.IntList;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.security.PublicKey;
/*      */ import java.time.Instant;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.ToIntFunction;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.EndTag;
/*      */ import net.minecraft.nbt.NbtAccounter;
/*      */ import net.minecraft.nbt.NbtIo;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.codec.StreamDecoder;
/*      */ import net.minecraft.network.codec.StreamEncoder;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.util.Crypt;
/*      */ import net.minecraft.util.CryptException;
/*      */ import net.minecraft.util.LenientJsonParser;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import org.joml.Quaternionf;
/*      */ import org.joml.Quaternionfc;
/*      */ import org.joml.Vector3f;
/*      */ import org.joml.Vector3fc;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FriendlyByteBuf
/*      */   extends ByteBuf
/*      */ {
/*      */   private final ByteBuf source;
/*      */   public static final short MAX_STRING_LENGTH = 32767;
/*      */   public static final int MAX_COMPONENT_STRING_LENGTH = 262144;
/*      */   private static final int PUBLIC_KEY_SIZE = 256;
/*      */   private static final int MAX_PUBLIC_KEY_HEADER_SIZE = 256;
/*      */   private static final int MAX_PUBLIC_KEY_LENGTH = 512;
/*   84 */   private static final Gson GSON = new Gson();
/*      */ 
/*      */   
/*   87 */   public FriendlyByteBuf(ByteBuf source) { this.source = source; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*  103 */   public <T> T readWithCodecTrusted(DynamicOps<Tag> ops, Codec<T> codec) { return (T)readWithCodec(ops, codec, NbtAccounter.unlimitedHeap()); }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T readWithCodec(DynamicOps<Tag> ops, Codec<T> codec, NbtAccounter accounter) {
/*  108 */     Tag tag = readNbt(accounter);
/*  109 */     return (T)codec.parse(ops, tag).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + String.valueOf(tag)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> FriendlyByteBuf writeWithCodec(DynamicOps<Tag> ops, Codec<T> codec, T value) {
/*  120 */     Tag tag = (Tag)codec.encodeStart(ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + String.valueOf(value)));
/*  121 */     writeNbt(tag);
/*  122 */     return this;
/*      */   }
/*      */   
/*      */   public <T> T readLenientJsonWithCodec(Codec<T> codec) {
/*  126 */     JsonElement json = LenientJsonParser.parse(readUtf());
/*  127 */     DataResult<T> result = codec.parse(JsonOps.INSTANCE, json);
/*  128 */     return (T)result.getOrThrow(error -> new DecoderException("Failed to decode JSON: " + error));
/*      */   }
/*      */   
/*      */   public <T> void writeJsonWithCodec(Codec<T> codec, T value) {
/*  132 */     DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, value);
/*  133 */     writeUtf(GSON.toJson((JsonElement)result.getOrThrow(error -> new EncoderException("Failed to encode: " + error + " " + String.valueOf(value)))));
/*      */   }
/*      */   
/*      */   public static <T> IntFunction<T> limitValue(IntFunction<T> original, int limit) {
/*  137 */     return value -> {
/*  138 */         if (value > limit) {
/*  139 */           throw new DecoderException("Value " + value + " is larger than limit " + limit);
/*      */         }
/*  141 */         return original.apply(value);
/*      */       };
/*      */   }
/*      */   
/*      */   public <T, C extends Collection<T>> C readCollection(IntFunction<C> ctor, StreamDecoder<? super FriendlyByteBuf, T> elementDecoder) {
/*  146 */     int count = readVarInt();
/*  147 */     C result = (C)(Collection)ctor.apply(count);
/*  148 */     for (int i = 0; i < count; i++) {
/*  149 */       result.add(elementDecoder.decode(this));
/*      */     }
/*  151 */     return result;
/*      */   }
/*      */   
/*      */   public <T> void writeCollection(Collection<T> collection, StreamEncoder<? super FriendlyByteBuf, T> encoder) {
/*  155 */     writeVarInt(collection.size());
/*  156 */     for (T element : collection) {
/*  157 */       encoder.encode(this, element);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  162 */   public <T> List<T> readList(StreamDecoder<? super FriendlyByteBuf, T> elementDecoder) { return (List)readCollection(Lists::newArrayListWithCapacity, elementDecoder); }
/*      */ 
/*      */   
/*      */   public IntList readIntIdList() {
/*  166 */     int count = readVarInt();
/*  167 */     IntArrayList intArrayList = new IntArrayList();
/*  168 */     for (int i = 0; i < count; i++) {
/*  169 */       intArrayList.add(readVarInt());
/*      */     }
/*  171 */     return intArrayList;
/*      */   }
/*      */   
/*      */   public void writeIntIdList(IntList ids) {
/*  175 */     writeVarInt(ids.size());
/*  176 */     ids.forEach(this::writeVarInt);
/*      */   }
/*      */   
/*      */   public <K, V, M extends Map<K, V>> M readMap(IntFunction<M> ctor, StreamDecoder<? super FriendlyByteBuf, K> keyDecoder, StreamDecoder<? super FriendlyByteBuf, V> valueDecoder) {
/*  180 */     int count = readVarInt();
/*  181 */     M result = (M)(Map)ctor.apply(count);
/*  182 */     for (int i = 0; i < count; i++) {
/*  183 */       K key = (K)keyDecoder.decode(this);
/*  184 */       V value = (V)valueDecoder.decode(this);
/*  185 */       result.put(key, value);
/*      */     } 
/*  187 */     return result;
/*      */   }
/*      */ 
/*      */   
/*  191 */   public <K, V> Map<K, V> readMap(StreamDecoder<? super FriendlyByteBuf, K> keyDecoder, StreamDecoder<? super FriendlyByteBuf, V> valueDecoder) { return readMap(Maps::newHashMapWithExpectedSize, keyDecoder, valueDecoder); }
/*      */ 
/*      */   
/*      */   public <K, V> void writeMap(Map<K, V> map, StreamEncoder<? super FriendlyByteBuf, K> keyEncoder, StreamEncoder<? super FriendlyByteBuf, V> valueEncoder) {
/*  195 */     writeVarInt(map.size());
/*  196 */     map.forEach((k, v) -> {
/*  197 */           keyEncoder.encode(this, k);
/*  198 */           valueEncoder.encode(this, v);
/*      */         });
/*      */   }
/*      */   
/*      */   public void readWithCount(Consumer<FriendlyByteBuf> reader) {
/*  203 */     int count = readVarInt();
/*  204 */     for (int i = 0; i < count; i++) {
/*  205 */       reader.accept(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public <E extends Enum<E>> void writeEnumSet(EnumSet<E> set, Class<E> clazz) {
/*  210 */     E[] values = (E[])(Enum[])clazz.getEnumConstants();
/*  211 */     BitSet mask = new BitSet(values.length);
/*  212 */     for (int i = 0; i < values.length; i++) {
/*  213 */       mask.set(i, set.contains(values[i]));
/*      */     }
/*  215 */     writeFixedBitSet(mask, values.length);
/*      */   }
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> readEnumSet(Class<E> clazz) {
/*  219 */     E[] values = (E[])(Enum[])clazz.getEnumConstants();
/*  220 */     BitSet mask = readFixedBitSet(values.length);
/*  221 */     EnumSet<E> result = EnumSet.noneOf(clazz);
/*  222 */     for (int i = 0; i < values.length; i++) {
/*  223 */       if (mask.get(i)) {
/*  224 */         result.add(values[i]);
/*      */       }
/*      */     } 
/*  227 */     return result;
/*      */   }
/*      */   
/*      */   public <T> void writeOptional(Optional<T> value, StreamEncoder<? super FriendlyByteBuf, T> valueWriter) {
/*  231 */     if (value.isPresent()) {
/*  232 */       writeBoolean(true);
/*  233 */       valueWriter.encode(this, value.get());
/*      */     } else {
/*  235 */       writeBoolean(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public <T> Optional<T> readOptional(StreamDecoder<? super FriendlyByteBuf, T> valueReader) {
/*  240 */     if (readBoolean()) {
/*  241 */       return Optional.of(valueReader.decode(this));
/*      */     }
/*  243 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   public <L, R> void writeEither(Either<L, R> value, StreamEncoder<? super FriendlyByteBuf, L> leftWriter, StreamEncoder<? super FriendlyByteBuf, R> rightWriter) {
/*  247 */     value.ifLeft(left -> {
/*  248 */           writeBoolean(true);
/*  249 */           leftWriter.encode(this, left);
/*  250 */         }).ifRight(right -> {
/*  251 */           writeBoolean(false);
/*  252 */           rightWriter.encode(this, right);
/*      */         });
/*      */   }
/*      */   
/*      */   public <L, R> Either<L, R> readEither(StreamDecoder<? super FriendlyByteBuf, L> leftReader, StreamDecoder<? super FriendlyByteBuf, R> rightReader) {
/*  257 */     if (readBoolean()) {
/*  258 */       return Either.left(leftReader.decode(this));
/*      */     }
/*  260 */     return Either.right(rightReader.decode(this));
/*      */   }
/*      */ 
/*      */   
/*  264 */   public <T> T readNullable(StreamDecoder<? super FriendlyByteBuf, T> valueDecoder) { return (T)readNullable(this, valueDecoder); }
/*      */ 
/*      */   
/*      */   public static <T, B extends ByteBuf> T readNullable(B input, StreamDecoder<? super B, T> valueDecoder) {
/*  268 */     if (input.readBoolean()) {
/*  269 */       return (T)valueDecoder.decode(input);
/*      */     }
/*  271 */     return null;
/*      */   }
/*      */ 
/*      */   
/*  275 */   public <T> void writeNullable(T value, StreamEncoder<? super FriendlyByteBuf, T> valueEncoder) { writeNullable(this, value, valueEncoder); }
/*      */ 
/*      */   
/*      */   public static <T, B extends ByteBuf> void writeNullable(B output, T value, StreamEncoder<? super B, T> valueEncoder) {
/*  279 */     if (value != null) {
/*  280 */       output.writeBoolean(true);
/*  281 */       valueEncoder.encode(output, value);
/*      */     } else {
/*  283 */       output.writeBoolean(false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  288 */   public byte[] readByteArray() { return readByteArray(this); }
/*      */ 
/*      */ 
/*      */   
/*  292 */   public static byte[] readByteArray(ByteBuf input) { return readByteArray(input, input.readableBytes()); }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeByteArray(byte[] bytes) {
/*  296 */     writeByteArray(this, bytes);
/*  297 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeByteArray(ByteBuf output, byte[] bytes) {
/*  301 */     VarInt.write(output, bytes.length);
/*  302 */     output.writeBytes(bytes);
/*      */   }
/*      */ 
/*      */   
/*  306 */   public byte[] readByteArray(int maxSize) { return readByteArray(this, maxSize); }
/*      */ 
/*      */   
/*      */   public static byte[] readByteArray(ByteBuf input, int maxSize) {
/*  310 */     int size = VarInt.read(input);
/*  311 */     if (size > maxSize) {
/*  312 */       throw new DecoderException("ByteArray with size " + size + " is bigger than allowed " + maxSize);
/*      */     }
/*  314 */     byte[] bytes = new byte[size];
/*  315 */     input.readBytes(bytes);
/*      */     
/*  317 */     return bytes;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarIntArray(int[] ints) {
/*  321 */     writeVarInt(ints.length);
/*      */     
/*  323 */     for (int i : ints) {
/*  324 */       writeVarInt(i);
/*      */     }
/*      */     
/*  327 */     return this;
/*      */   }
/*      */ 
/*      */   
/*  331 */   public int[] readVarIntArray() { return readVarIntArray(readableBytes()); }
/*      */ 
/*      */   
/*      */   public int[] readVarIntArray(int maxSize) {
/*  335 */     int size = readVarInt();
/*  336 */     if (size > maxSize) {
/*  337 */       throw new DecoderException("VarIntArray with size " + size + " is bigger than allowed " + maxSize);
/*      */     }
/*  339 */     int[] ints = new int[size];
/*      */     
/*  341 */     for (int i = 0; i < ints.length; i++) {
/*  342 */       ints[i] = readVarInt();
/*      */     }
/*      */     
/*  345 */     return ints;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeLongArray(long[] longs) {
/*  349 */     writeLongArray(this, longs);
/*  350 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeLongArray(ByteBuf output, long[] longs) {
/*  354 */     VarInt.write(output, longs.length);
/*  355 */     writeFixedSizeLongArray(output, longs);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeFixedSizeLongArray(long[] longs) {
/*  359 */     writeFixedSizeLongArray(this, longs);
/*  360 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeFixedSizeLongArray(ByteBuf output, long[] longs) {
/*  364 */     for (long l : longs) {
/*  365 */       output.writeLong(l);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  370 */   public long[] readLongArray() { return readLongArray(this); }
/*      */ 
/*      */ 
/*      */   
/*  374 */   public long[] readFixedSizeLongArray(long[] output) { return readFixedSizeLongArray(this, output); }
/*      */ 
/*      */   
/*      */   public static long[] readLongArray(ByteBuf input) {
/*  378 */     int size = VarInt.read(input);
/*  379 */     int maxSize = input.readableBytes() / 8;
/*  380 */     if (size > maxSize) {
/*  381 */       throw new DecoderException("LongArray with size " + size + " is bigger than allowed " + maxSize);
/*      */     }
/*  383 */     return readFixedSizeLongArray(input, new long[size]);
/*      */   }
/*      */   
/*      */   public static long[] readFixedSizeLongArray(ByteBuf input, long[] output) {
/*  387 */     for (int i = 0; i < output.length; i++) {
/*  388 */       output[i] = input.readLong();
/*      */     }
/*  390 */     return output;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  398 */   public BlockPos readBlockPos() { return readBlockPos(this); }
/*      */ 
/*      */ 
/*      */   
/*  402 */   public static BlockPos readBlockPos(ByteBuf input) { return BlockPos.of(input.readLong()); }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBlockPos(BlockPos pos) {
/*  406 */     writeBlockPos(this, pos);
/*  407 */     return this;
/*      */   }
/*      */ 
/*      */   
/*  411 */   public static void writeBlockPos(ByteBuf output, BlockPos pos) { output.writeLong(pos.asLong()); }
/*      */ 
/*      */ 
/*      */   
/*  415 */   public ChunkPos readChunkPos() { return new ChunkPos(readLong()); }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeChunkPos(ChunkPos pos) {
/*  419 */     writeLong(pos.toLong());
/*  420 */     return this;
/*      */   }
/*      */ 
/*      */   
/*  424 */   public static ChunkPos readChunkPos(ByteBuf input) { return new ChunkPos(input.readLong()); }
/*      */ 
/*      */ 
/*      */   
/*  428 */   public static void writeChunkPos(ByteBuf output, ChunkPos chunkPos) { output.writeLong(chunkPos.toLong()); }
/*      */ 
/*      */   
/*      */   public GlobalPos readGlobalPos() {
/*  432 */     ResourceKey<Level> dimension = readResourceKey(Registries.DIMENSION);
/*  433 */     BlockPos pos = readBlockPos();
/*  434 */     return GlobalPos.of(dimension, pos);
/*      */   }
/*      */   
/*      */   public void writeGlobalPos(GlobalPos globalPos) {
/*  438 */     writeResourceKey(globalPos.dimension());
/*  439 */     writeBlockPos(globalPos.pos());
/*      */   }
/*      */ 
/*      */   
/*  443 */   public Vector3f readVector3f() { return readVector3f(this); }
/*      */ 
/*      */ 
/*      */   
/*  447 */   public static Vector3f readVector3f(ByteBuf input) { return new Vector3f(input.readFloat(), input.readFloat(), input.readFloat()); }
/*      */ 
/*      */ 
/*      */   
/*  451 */   public void writeVector3f(Vector3f v) { writeVector3f(this, v); }
/*      */ 
/*      */   
/*      */   public static void writeVector3f(ByteBuf output, Vector3fc v) {
/*  455 */     output.writeFloat(v.x());
/*  456 */     output.writeFloat(v.y());
/*  457 */     output.writeFloat(v.z());
/*      */   }
/*      */ 
/*      */   
/*  461 */   public Quaternionf readQuaternion() { return readQuaternion(this); }
/*      */ 
/*      */ 
/*      */   
/*  465 */   public static Quaternionf readQuaternion(ByteBuf input) { return new Quaternionf(input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat()); }
/*      */ 
/*      */ 
/*      */   
/*  469 */   public void writeQuaternion(Quaternionf q) { writeQuaternion(this, q); }
/*      */ 
/*      */   
/*      */   public static void writeQuaternion(ByteBuf output, Quaternionfc value) {
/*  473 */     output.writeFloat(value.x());
/*  474 */     output.writeFloat(value.y());
/*  475 */     output.writeFloat(value.z());
/*  476 */     output.writeFloat(value.w());
/*      */   }
/*      */ 
/*      */   
/*  480 */   public static Vec3 readVec3(ByteBuf input) { return new Vec3(input.readDouble(), input.readDouble(), input.readDouble()); }
/*      */ 
/*      */ 
/*      */   
/*  484 */   public Vec3 readVec3() { return readVec3(this); }
/*      */ 
/*      */   
/*      */   public static void writeVec3(ByteBuf output, Vec3 v) {
/*  488 */     output.writeDouble(v.x());
/*  489 */     output.writeDouble(v.y());
/*  490 */     output.writeDouble(v.z());
/*      */   }
/*      */ 
/*      */   
/*  494 */   public void writeVec3(Vec3 v) { writeVec3(this, v); }
/*      */ 
/*      */ 
/*      */   
/*  498 */   public Vec3 readLpVec3() { return LpVec3.read(this); }
/*      */ 
/*      */ 
/*      */   
/*  502 */   public void writeLpVec3(Vec3 v) { LpVec3.write(this, v); }
/*      */ 
/*      */ 
/*      */   
/*  506 */   public <T extends Enum<T>> T readEnum(Class<T> clazz) { return (T)(Enum[])clazz.getEnumConstants()[readVarInt()]; }
/*      */ 
/*      */ 
/*      */   
/*  510 */   public FriendlyByteBuf writeEnum(Enum<?> value) { return writeVarInt(value.ordinal()); }
/*      */ 
/*      */   
/*      */   public <T> T readById(IntFunction<T> converter) {
/*  514 */     int id = readVarInt();
/*  515 */     return (T)converter.apply(id);
/*      */   }
/*      */   
/*      */   public <T> FriendlyByteBuf writeById(ToIntFunction<T> converter, T value) {
/*  519 */     int id = converter.applyAsInt(value);
/*  520 */     return writeVarInt(id);
/*      */   }
/*      */ 
/*      */   
/*  524 */   public int readVarInt() { return VarInt.read(this.source); }
/*      */ 
/*      */ 
/*      */   
/*  528 */   public long readVarLong() { return VarLong.read(this.source); }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeUUID(UUID uuid) {
/*  532 */     writeUUID(this, uuid);
/*  533 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeUUID(ByteBuf output, UUID uuid) {
/*  537 */     output.writeLong(uuid.getMostSignificantBits());
/*  538 */     output.writeLong(uuid.getLeastSignificantBits());
/*      */   }
/*      */ 
/*      */   
/*  542 */   public UUID readUUID() { return readUUID(this); }
/*      */ 
/*      */ 
/*      */   
/*  546 */   public static UUID readUUID(ByteBuf input) { return new UUID(input.readLong(), input.readLong()); }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeVarInt(int value) {
/*  550 */     VarInt.write(this.source, value);
/*  551 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarLong(long value) {
/*  555 */     VarLong.write(this.source, value);
/*  556 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeNbt(Tag tag) {
/*  560 */     writeNbt(this, tag);
/*  561 */     return this;
/*      */   }
/*      */   public static void writeNbt(ByteBuf output, Tag tag) {
/*      */     EndTag endTag;
/*  565 */     if (tag == null) {
/*  566 */       endTag = EndTag.INSTANCE;
/*      */     }
/*      */     
/*      */     try {
/*  570 */       NbtIo.writeAnyTag(endTag, new ByteBufOutputStream(output));
/*  571 */     } catch (IOException e) {
/*  572 */       throw new EncoderException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  577 */   public CompoundTag readNbt() { return readNbt(this); }
/*      */ 
/*      */   
/*      */   public static CompoundTag readNbt(ByteBuf input) {
/*  581 */     Tag result = readNbt(input, NbtAccounter.defaultQuota());
/*  582 */     if (result == null || result instanceof CompoundTag) {
/*  583 */       return (CompoundTag)result;
/*      */     }
/*  585 */     throw new DecoderException("Not a compound tag: " + String.valueOf(result));
/*      */   }
/*      */   
/*      */   public static Tag readNbt(ByteBuf input, NbtAccounter accounter) {
/*      */     try {
/*  590 */       Tag tag = NbtIo.readAnyTag(new ByteBufInputStream(input), accounter);
/*  591 */       if (tag.getId() == 0) {
/*  592 */         return null;
/*      */       }
/*  594 */       return tag;
/*  595 */     } catch (IOException e) {
/*  596 */       throw new EncoderException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*  601 */   public Tag readNbt(NbtAccounter accounter) { return readNbt(this, accounter); }
/*      */ 
/*      */ 
/*      */   
/*  605 */   public String readUtf() { return readUtf(32767); }
/*      */ 
/*      */ 
/*      */   
/*  609 */   public String readUtf(int maxLength) { return Utf8String.read(this.source, maxLength); }
/*      */ 
/*      */ 
/*      */   
/*  613 */   public FriendlyByteBuf writeUtf(String value) { return writeUtf(value, 32767); }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeUtf(String value, int maxLength) {
/*  617 */     Utf8String.write(this.source, value, maxLength);
/*  618 */     return this;
/*      */   }
/*      */ 
/*      */   
/*  622 */   public Identifier readIdentifier() { return Identifier.parse(readUtf(32767)); }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeIdentifier(Identifier identifier) {
/*  626 */     writeUtf(identifier.toString());
/*  627 */     return this;
/*      */   }
/*      */   
/*      */   public <T> ResourceKey<T> readResourceKey(ResourceKey<? extends Registry<T>> registry) {
/*  631 */     Identifier id = readIdentifier();
/*  632 */     return ResourceKey.create(registry, id);
/*      */   }
/*      */ 
/*      */   
/*  636 */   public void writeResourceKey(ResourceKey<?> key) { writeIdentifier(key.identifier()); }
/*      */ 
/*      */   
/*      */   public <T> ResourceKey<? extends Registry<T>> readRegistryKey() {
/*  640 */     Identifier id = readIdentifier();
/*  641 */     return ResourceKey.createRegistryKey(id);
/*      */   }
/*      */ 
/*      */   
/*  645 */   public Instant readInstant() { return Instant.ofEpochMilli(readLong()); }
/*      */ 
/*      */ 
/*      */   
/*  649 */   public void writeInstant(Instant value) { writeLong(value.toEpochMilli()); }
/*      */ 
/*      */   
/*      */   public PublicKey readPublicKey() {
/*      */     try {
/*  654 */       return Crypt.byteToPublicKey(readByteArray(512));
/*  655 */     } catch (CryptException e) {
/*  656 */       throw new DecoderException("Malformed public key bytes", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writePublicKey(PublicKey publicKey) {
/*  661 */     writeByteArray(publicKey.getEncoded());
/*  662 */     return this;
/*      */   }
/*      */   
/*      */   public BlockHitResult readBlockHitResult() {
/*  666 */     BlockPos pos = readBlockPos();
/*  667 */     Direction face = (Direction)readEnum(Direction.class);
/*  668 */     float clickX = readFloat();
/*  669 */     float clickY = readFloat();
/*  670 */     float clickZ = readFloat();
/*  671 */     boolean inside = readBoolean();
/*  672 */     boolean worldBorder = readBoolean();
/*      */     
/*  674 */     return new BlockHitResult(new Vec3(pos
/*  675 */           .getX() + clickX, pos
/*  676 */           .getY() + clickY, pos
/*  677 */           .getZ() + clickZ), face, pos, inside, worldBorder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeBlockHitResult(BlockHitResult blockHit) {
/*  686 */     BlockPos blockPos = blockHit.getBlockPos();
/*  687 */     writeBlockPos(blockPos);
/*  688 */     writeEnum(blockHit.getDirection());
/*  689 */     Vec3 location = blockHit.getLocation();
/*  690 */     writeFloat((float)(location.x - blockPos.getX()));
/*  691 */     writeFloat((float)(location.y - blockPos.getY()));
/*  692 */     writeFloat((float)(location.z - blockPos.getZ()));
/*  693 */     writeBoolean(blockHit.isInside());
/*  694 */     writeBoolean(blockHit.isWorldBorderHit());
/*      */   }
/*      */ 
/*      */   
/*  698 */   public BitSet readBitSet() { return BitSet.valueOf(readLongArray()); }
/*      */ 
/*      */ 
/*      */   
/*  702 */   public void writeBitSet(BitSet bitSet) { writeLongArray(bitSet.toLongArray()); }
/*      */ 
/*      */   
/*      */   public BitSet readFixedBitSet(int size) {
/*  706 */     byte[] bytes = new byte[Mth.positiveCeilDiv(size, 8)];
/*  707 */     readBytes(bytes);
/*  708 */     return BitSet.valueOf(bytes);
/*      */   }
/*      */   
/*      */   public void writeFixedBitSet(BitSet bitSet, int size) {
/*  712 */     if (bitSet.length() > size) {
/*  713 */       throw new EncoderException("BitSet is larger than expected size (" + bitSet.length() + ">" + size + ")");
/*      */     }
/*  715 */     byte[] bytes = bitSet.toByteArray();
/*  716 */     writeBytes(Arrays.copyOf(bytes, Mth.positiveCeilDiv(size, 8)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  721 */   public static int readContainerId(ByteBuf input) { return VarInt.read(input); }
/*      */ 
/*      */ 
/*      */   
/*  725 */   public int readContainerId() { return readContainerId(this.source); }
/*      */ 
/*      */ 
/*      */   
/*  729 */   public static void writeContainerId(ByteBuf output, int id) { VarInt.write(output, id); }
/*      */ 
/*      */ 
/*      */   
/*  733 */   public void writeContainerId(int id) { writeContainerId(this.source, id); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  740 */   public boolean isContiguous() { return this.source.isContiguous(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  745 */   public int maxFastWritableBytes() { return this.source.maxFastWritableBytes(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  750 */   public int capacity() { return this.source.capacity(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf capacity(int newCapacity) {
/*  755 */     this.source.capacity(newCapacity);
/*  756 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  761 */   public int maxCapacity() { return this.source.maxCapacity(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  766 */   public ByteBufAllocator alloc() { return this.source.alloc(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  771 */   public ByteOrder order() { return this.source.order(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  776 */   public ByteBuf order(ByteOrder endianness) { return this.source.order(endianness); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  781 */   public ByteBuf unwrap() { return this.source; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  786 */   public boolean isDirect() { return this.source.isDirect(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  791 */   public boolean isReadOnly() { return this.source.isReadOnly(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  796 */   public ByteBuf asReadOnly() { return this.source.asReadOnly(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  801 */   public int readerIndex() { return this.source.readerIndex(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readerIndex(int readerIndex) {
/*  806 */     this.source.readerIndex(readerIndex);
/*  807 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  812 */   public int writerIndex() { return this.source.writerIndex(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writerIndex(int writerIndex) {
/*  817 */     this.source.writerIndex(writerIndex);
/*  818 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setIndex(int readerIndex, int writerIndex) {
/*  823 */     this.source.setIndex(readerIndex, writerIndex);
/*  824 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  829 */   public int readableBytes() { return this.source.readableBytes(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  834 */   public int writableBytes() { return this.source.writableBytes(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  839 */   public int maxWritableBytes() { return this.source.maxWritableBytes(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  844 */   public boolean isReadable() { return this.source.isReadable(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  849 */   public boolean isReadable(int size) { return this.source.isReadable(size); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  854 */   public boolean isWritable() { return this.source.isWritable(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  859 */   public boolean isWritable(int size) { return this.source.isWritable(size); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf clear() {
/*  864 */     this.source.clear();
/*  865 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf markReaderIndex() {
/*  870 */     this.source.markReaderIndex();
/*  871 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf resetReaderIndex() {
/*  876 */     this.source.resetReaderIndex();
/*  877 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf markWriterIndex() {
/*  882 */     this.source.markWriterIndex();
/*  883 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf resetWriterIndex() {
/*  888 */     this.source.resetWriterIndex();
/*  889 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf discardReadBytes() {
/*  894 */     this.source.discardReadBytes();
/*  895 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf discardSomeReadBytes() {
/*  900 */     this.source.discardSomeReadBytes();
/*  901 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf ensureWritable(int minWritableBytes) {
/*  906 */     this.source.ensureWritable(minWritableBytes);
/*  907 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  912 */   public int ensureWritable(int minWritableBytes, boolean force) { return this.source.ensureWritable(minWritableBytes, force); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  917 */   public boolean getBoolean(int index) { return this.source.getBoolean(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  922 */   public byte getByte(int index) { return this.source.getByte(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  927 */   public short getUnsignedByte(int index) { return this.source.getUnsignedByte(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  932 */   public short getShort(int index) { return this.source.getShort(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  937 */   public short getShortLE(int index) { return this.source.getShortLE(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  942 */   public int getUnsignedShort(int index) { return this.source.getUnsignedShort(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  947 */   public int getUnsignedShortLE(int index) { return this.source.getUnsignedShortLE(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  952 */   public int getMedium(int index) { return this.source.getMedium(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  957 */   public int getMediumLE(int index) { return this.source.getMediumLE(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  962 */   public int getUnsignedMedium(int index) { return this.source.getUnsignedMedium(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  967 */   public int getUnsignedMediumLE(int index) { return this.source.getUnsignedMediumLE(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  972 */   public int getInt(int index) { return this.source.getInt(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  977 */   public int getIntLE(int index) { return this.source.getIntLE(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  982 */   public long getUnsignedInt(int index) { return this.source.getUnsignedInt(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  987 */   public long getUnsignedIntLE(int index) { return this.source.getUnsignedIntLE(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  992 */   public long getLong(int index) { return this.source.getLong(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  997 */   public long getLongLE(int index) { return this.source.getLongLE(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1002 */   public char getChar(int index) { return this.source.getChar(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1007 */   public float getFloat(int index) { return this.source.getFloat(index); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1012 */   public double getDouble(int index) { return this.source.getDouble(index); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst) {
/* 1017 */     this.source.getBytes(index, dst);
/* 1018 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst, int length) {
/* 1023 */     this.source.getBytes(index, dst, length);
/* 1024 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 1029 */     this.source.getBytes(index, dst, dstIndex, length);
/* 1030 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, byte[] dst) {
/* 1035 */     this.source.getBytes(index, dst);
/* 1036 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 1041 */     this.source.getBytes(index, dst, dstIndex, length);
/* 1042 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuffer dst) {
/* 1047 */     this.source.getBytes(index, dst);
/* 1048 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 1053 */     this.source.getBytes(index, out, length);
/* 1054 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1059 */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException { return this.source.getBytes(index, out, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1064 */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException { return this.source.getBytes(index, out, position, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1069 */   public CharSequence getCharSequence(int index, int length, Charset charset) { return this.source.getCharSequence(index, length, charset); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBoolean(int index, boolean value) {
/* 1074 */     this.source.setBoolean(index, value);
/* 1075 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setByte(int index, int value) {
/* 1080 */     this.source.setByte(index, value);
/* 1081 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setShort(int index, int value) {
/* 1086 */     this.source.setShort(index, value);
/* 1087 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setShortLE(int index, int value) {
/* 1092 */     this.source.setShortLE(index, value);
/* 1093 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setMedium(int index, int value) {
/* 1098 */     this.source.setMedium(index, value);
/* 1099 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setMediumLE(int index, int value) {
/* 1104 */     this.source.setMediumLE(index, value);
/* 1105 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setInt(int index, int value) {
/* 1110 */     this.source.setInt(index, value);
/* 1111 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setIntLE(int index, int value) {
/* 1116 */     this.source.setIntLE(index, value);
/* 1117 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setLong(int index, long value) {
/* 1122 */     this.source.setLong(index, value);
/* 1123 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setLongLE(int index, long value) {
/* 1128 */     this.source.setLongLE(index, value);
/* 1129 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setChar(int index, int value) {
/* 1134 */     this.source.setChar(index, value);
/* 1135 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setFloat(int index, float value) {
/* 1140 */     this.source.setFloat(index, value);
/* 1141 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setDouble(int index, double value) {
/* 1146 */     this.source.setDouble(index, value);
/* 1147 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src) {
/* 1152 */     this.source.setBytes(index, src);
/* 1153 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src, int length) {
/* 1158 */     this.source.setBytes(index, src, length);
/* 1159 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 1164 */     this.source.setBytes(index, src, srcIndex, length);
/* 1165 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, byte[] src) {
/* 1170 */     this.source.setBytes(index, src);
/* 1171 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 1176 */     this.source.setBytes(index, src, srcIndex, length);
/* 1177 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuffer src) {
/* 1182 */     this.source.setBytes(index, src);
/* 1183 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1188 */   public int setBytes(int index, InputStream in, int length) throws IOException { return this.source.setBytes(index, in, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1193 */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException { return this.source.setBytes(index, in, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1198 */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException { return this.source.setBytes(index, in, position, length); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setZero(int index, int length) {
/* 1203 */     this.source.setZero(index, length);
/* 1204 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1209 */   public int setCharSequence(int index, CharSequence sequence, Charset charset) { return this.source.setCharSequence(index, sequence, charset); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1214 */   public boolean readBoolean() { return this.source.readBoolean(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1219 */   public byte readByte() { return this.source.readByte(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1224 */   public short readUnsignedByte() { return this.source.readUnsignedByte(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1229 */   public short readShort() { return this.source.readShort(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1234 */   public short readShortLE() { return this.source.readShortLE(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1239 */   public int readUnsignedShort() { return this.source.readUnsignedShort(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1244 */   public int readUnsignedShortLE() { return this.source.readUnsignedShortLE(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1249 */   public int readMedium() { return this.source.readMedium(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1254 */   public int readMediumLE() { return this.source.readMediumLE(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1259 */   public int readUnsignedMedium() { return this.source.readUnsignedMedium(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1264 */   public int readUnsignedMediumLE() { return this.source.readUnsignedMediumLE(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1269 */   public int readInt() { return this.source.readInt(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1274 */   public int readIntLE() { return this.source.readIntLE(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1279 */   public long readUnsignedInt() { return this.source.readUnsignedInt(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1284 */   public long readUnsignedIntLE() { return this.source.readUnsignedIntLE(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1289 */   public long readLong() { return this.source.readLong(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1294 */   public long readLongLE() { return this.source.readLongLE(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1299 */   public char readChar() { return this.source.readChar(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1304 */   public float readFloat() { return this.source.readFloat(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1309 */   public double readDouble() { return this.source.readDouble(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1314 */   public ByteBuf readBytes(int length) { return this.source.readBytes(length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1319 */   public ByteBuf readSlice(int length) { return this.source.readSlice(length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1324 */   public ByteBuf readRetainedSlice(int length) { return this.source.readRetainedSlice(length); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst) {
/* 1329 */     this.source.readBytes(dst);
/* 1330 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst, int length) {
/* 1335 */     this.source.readBytes(dst, length);
/* 1336 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/* 1341 */     this.source.readBytes(dst, dstIndex, length);
/* 1342 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(byte[] dst) {
/* 1347 */     this.source.readBytes(dst);
/* 1348 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/* 1353 */     this.source.readBytes(dst, dstIndex, length);
/* 1354 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuffer dst) {
/* 1359 */     this.source.readBytes(dst);
/* 1360 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(OutputStream out, int length) throws IOException {
/* 1365 */     this.source.readBytes(out, length);
/* 1366 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1371 */   public int readBytes(GatheringByteChannel out, int length) throws IOException { return this.source.readBytes(out, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1376 */   public CharSequence readCharSequence(int length, Charset charset) { return this.source.readCharSequence(length, charset); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1381 */   public String readString(int length, Charset charset) { return this.source.readString(length, charset); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1386 */   public int readBytes(FileChannel out, long position, int length) throws IOException { return this.source.readBytes(out, position, length); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf skipBytes(int length) {
/* 1391 */     this.source.skipBytes(length);
/* 1392 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBoolean(boolean value) {
/* 1397 */     this.source.writeBoolean(value);
/* 1398 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeByte(int value) {
/* 1403 */     this.source.writeByte(value);
/* 1404 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeShort(int value) {
/* 1409 */     this.source.writeShort(value);
/* 1410 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeShortLE(int value) {
/* 1415 */     this.source.writeShortLE(value);
/* 1416 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeMedium(int value) {
/* 1421 */     this.source.writeMedium(value);
/* 1422 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeMediumLE(int value) {
/* 1427 */     this.source.writeMediumLE(value);
/* 1428 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeInt(int value) {
/* 1433 */     this.source.writeInt(value);
/* 1434 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeIntLE(int value) {
/* 1439 */     this.source.writeIntLE(value);
/* 1440 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeLong(long value) {
/* 1445 */     this.source.writeLong(value);
/* 1446 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeLongLE(long value) {
/* 1451 */     this.source.writeLongLE(value);
/* 1452 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeChar(int value) {
/* 1457 */     this.source.writeChar(value);
/* 1458 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeFloat(float value) {
/* 1463 */     this.source.writeFloat(value);
/* 1464 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeDouble(double value) {
/* 1469 */     this.source.writeDouble(value);
/* 1470 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src) {
/* 1475 */     this.source.writeBytes(src);
/* 1476 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src, int length) {
/* 1481 */     this.source.writeBytes(src, length);
/* 1482 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/* 1487 */     this.source.writeBytes(src, srcIndex, length);
/* 1488 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(byte[] src) {
/* 1493 */     this.source.writeBytes(src);
/* 1494 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/* 1499 */     this.source.writeBytes(src, srcIndex, length);
/* 1500 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuffer src) {
/* 1505 */     this.source.writeBytes(src);
/* 1506 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1511 */   public int writeBytes(InputStream in, int length) throws IOException { return this.source.writeBytes(in, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1516 */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException { return this.source.writeBytes(in, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1521 */   public int writeBytes(FileChannel in, long position, int length) throws IOException { return this.source.writeBytes(in, position, length); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeZero(int length) {
/* 1526 */     this.source.writeZero(length);
/* 1527 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1532 */   public int writeCharSequence(CharSequence sequence, Charset charset) { return this.source.writeCharSequence(sequence, charset); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1537 */   public int indexOf(int fromIndex, int toIndex, byte value) { return this.source.indexOf(fromIndex, toIndex, value); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1542 */   public int bytesBefore(byte value) { return this.source.bytesBefore(value); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1547 */   public int bytesBefore(int length, byte value) { return this.source.bytesBefore(length, value); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1552 */   public int bytesBefore(int index, int length, byte value) { return this.source.bytesBefore(index, length, value); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1557 */   public int forEachByte(ByteProcessor processor) { return this.source.forEachByte(processor); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1562 */   public int forEachByte(int index, int length, ByteProcessor processor) { return this.source.forEachByte(index, length, processor); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1567 */   public int forEachByteDesc(ByteProcessor processor) { return this.source.forEachByteDesc(processor); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1572 */   public int forEachByteDesc(int index, int length, ByteProcessor processor) { return this.source.forEachByteDesc(index, length, processor); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1577 */   public ByteBuf copy() { return this.source.copy(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1582 */   public ByteBuf copy(int index, int length) { return this.source.copy(index, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1587 */   public ByteBuf slice() { return this.source.slice(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1592 */   public ByteBuf retainedSlice() { return this.source.retainedSlice(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1597 */   public ByteBuf slice(int index, int length) { return this.source.slice(index, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1602 */   public ByteBuf retainedSlice(int index, int length) { return this.source.retainedSlice(index, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1607 */   public ByteBuf duplicate() { return this.source.duplicate(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1612 */   public ByteBuf retainedDuplicate() { return this.source.retainedDuplicate(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1617 */   public int nioBufferCount() { return this.source.nioBufferCount(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1622 */   public ByteBuffer nioBuffer() { return this.source.nioBuffer(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1627 */   public ByteBuffer nioBuffer(int index, int length) { return this.source.nioBuffer(index, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1632 */   public ByteBuffer internalNioBuffer(int index, int length) { return this.source.internalNioBuffer(index, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1637 */   public ByteBuffer[] nioBuffers() { return this.source.nioBuffers(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1642 */   public ByteBuffer[] nioBuffers(int index, int length) { return this.source.nioBuffers(index, length); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1647 */   public boolean hasArray() { return this.source.hasArray(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1652 */   public byte[] array() { return this.source.array(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1657 */   public int arrayOffset() { return this.source.arrayOffset(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1662 */   public boolean hasMemoryAddress() { return this.source.hasMemoryAddress(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1667 */   public long memoryAddress() { return this.source.memoryAddress(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1672 */   public String toString(Charset charset) { return this.source.toString(charset); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1677 */   public String toString(int index, int length, Charset charset) { return this.source.toString(index, length, charset); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1682 */   public int hashCode() { return this.source.hashCode(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1687 */   public boolean equals(Object obj) { return this.source.equals(obj); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1692 */   public int compareTo(ByteBuf buffer) { return this.source.compareTo(buffer); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1697 */   public String toString() { return this.source.toString(); }
/*      */ 
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf retain(int increment) {
/* 1702 */     this.source.retain(increment);
/* 1703 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf retain() {
/* 1708 */     this.source.retain();
/* 1709 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf touch() {
/* 1714 */     this.source.touch();
/* 1715 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf touch(Object hint) {
/* 1720 */     this.source.touch(hint);
/* 1721 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1726 */   public int refCnt() { return this.source.refCnt(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1731 */   public boolean release() { return this.source.release(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1736 */   public boolean release(int decrement) { return this.source.release(decrement); }
/*      */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\FriendlyByteBuf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */