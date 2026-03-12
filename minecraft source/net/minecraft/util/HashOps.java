/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HashOps
/*     */   extends Object
/*     */   implements DynamicOps<HashCode>
/*     */ {
/*     */   private static final byte TAG_EMPTY = 1;
/*     */   private static final byte TAG_MAP_START = 2;
/*     */   private static final byte TAG_MAP_END = 3;
/*     */   private static final byte TAG_LIST_START = 4;
/*     */   private static final byte TAG_LIST_END = 5;
/*     */   private static final byte TAG_BYTE = 6;
/*     */   private static final byte TAG_SHORT = 7;
/*     */   private static final byte TAG_INT = 8;
/*     */   private static final byte TAG_LONG = 9;
/*     */   private static final byte TAG_FLOAT = 10;
/*     */   private static final byte TAG_DOUBLE = 11;
/*     */   private static final byte TAG_STRING = 12;
/*     */   private static final byte TAG_BOOLEAN = 13;
/*     */   private static final byte TAG_BYTE_ARRAY_START = 14;
/*     */   private static final byte TAG_BYTE_ARRAY_END = 15;
/*     */   private static final byte TAG_INT_ARRAY_START = 16;
/*     */   private static final byte TAG_INT_ARRAY_END = 17;
/*     */   private static final byte TAG_LONG_ARRAY_START = 18;
/*     */   private static final byte TAG_LONG_ARRAY_END = 19;
/*  58 */   private static final byte[] EMPTY_PAYLOAD = { 1 };
/*  59 */   private static final byte[] FALSE_PAYLOAD = { 13, 0 };
/*  60 */   private static final byte[] TRUE_PAYLOAD = { 13, 1 };
/*     */   
/*  62 */   public static final byte[] EMPTY_MAP_PAYLOAD = { 2, 3 };
/*  63 */   public static final byte[] EMPTY_LIST_PAYLOAD = { 4, 5 };
/*     */   
/*  65 */   private static final DataResult<Object> UNSUPPORTED_OPERATION_ERROR = DataResult.error(() -> "Unsupported operation");
/*     */   
/*  67 */   private static final Comparator<HashCode> HASH_COMPARATOR = Comparator.comparingLong(HashCode::padToLong);
/*     */   
/*  69 */   private static final Comparator<Map.Entry<HashCode, HashCode>> MAP_ENTRY_ORDER = Map.Entry.comparingByKey(HASH_COMPARATOR)
/*  70 */     .thenComparing(Map.Entry.comparingByValue(HASH_COMPARATOR));
/*     */   
/*  72 */   private static final Comparator<Pair<HashCode, HashCode>> MAPLIKE_ENTRY_ORDER = Comparator.comparing(Pair::getFirst, HASH_COMPARATOR)
/*  73 */     .thenComparing(Pair::getSecond, HASH_COMPARATOR);
/*     */   
/*  75 */   public static final HashOps CRC32C_INSTANCE = new HashOps(Hashing.crc32c());
/*     */   
/*     */   private final HashFunction hashFunction;
/*     */   
/*     */   private final HashCode empty;
/*     */   private final HashCode emptyMap;
/*     */   private final HashCode emptyList;
/*     */   private final HashCode trueHash;
/*     */   private final HashCode falseHash;
/*     */   
/*     */   public HashOps(HashFunction hashFunction) {
/*  86 */     this.hashFunction = hashFunction;
/*     */     
/*  88 */     this.empty = hashFunction.hashBytes(EMPTY_PAYLOAD);
/*  89 */     this.emptyMap = hashFunction.hashBytes(EMPTY_MAP_PAYLOAD);
/*  90 */     this.emptyList = hashFunction.hashBytes(EMPTY_LIST_PAYLOAD);
/*     */     
/*  92 */     this.falseHash = hashFunction.hashBytes(FALSE_PAYLOAD);
/*  93 */     this.trueHash = hashFunction.hashBytes(TRUE_PAYLOAD);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  98 */   public HashCode empty() { return this.empty; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public HashCode emptyMap() { return this.emptyMap; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public HashCode emptyList() { return this.emptyList; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode createNumeric(Number value) { // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_2
/*     */     //   7: iconst_0
/*     */     //   8: istore_3
/*     */     //   9: aload_2
/*     */     //   10: iload_3
/*     */     //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   16: tableswitch default -> 164, 0 -> 56, 1 -> 74, 2 -> 92, 3 -> 110, 4 -> 128, 5 -> 146
/*     */     //   56: aload_2
/*     */     //   57: checkcast java/lang/Byte
/*     */     //   60: astore #4
/*     */     //   62: aload_0
/*     */     //   63: aload #4
/*     */     //   65: invokevirtual byteValue : ()B
/*     */     //   68: invokevirtual createByte : (B)Lcom/google/common/hash/HashCode;
/*     */     //   71: goto -> 172
/*     */     //   74: aload_2
/*     */     //   75: checkcast java/lang/Short
/*     */     //   78: astore #5
/*     */     //   80: aload_0
/*     */     //   81: aload #5
/*     */     //   83: invokevirtual shortValue : ()S
/*     */     //   86: invokevirtual createShort : (S)Lcom/google/common/hash/HashCode;
/*     */     //   89: goto -> 172
/*     */     //   92: aload_2
/*     */     //   93: checkcast java/lang/Integer
/*     */     //   96: astore #6
/*     */     //   98: aload_0
/*     */     //   99: aload #6
/*     */     //   101: invokevirtual intValue : ()I
/*     */     //   104: invokevirtual createInt : (I)Lcom/google/common/hash/HashCode;
/*     */     //   107: goto -> 172
/*     */     //   110: aload_2
/*     */     //   111: checkcast java/lang/Long
/*     */     //   114: astore #7
/*     */     //   116: aload_0
/*     */     //   117: aload #7
/*     */     //   119: invokevirtual longValue : ()J
/*     */     //   122: invokevirtual createLong : (J)Lcom/google/common/hash/HashCode;
/*     */     //   125: goto -> 172
/*     */     //   128: aload_2
/*     */     //   129: checkcast java/lang/Double
/*     */     //   132: astore #8
/*     */     //   134: aload_0
/*     */     //   135: aload #8
/*     */     //   137: invokevirtual doubleValue : ()D
/*     */     //   140: invokevirtual createDouble : (D)Lcom/google/common/hash/HashCode;
/*     */     //   143: goto -> 172
/*     */     //   146: aload_2
/*     */     //   147: checkcast java/lang/Float
/*     */     //   150: astore #9
/*     */     //   152: aload_0
/*     */     //   153: aload #9
/*     */     //   155: invokevirtual floatValue : ()F
/*     */     //   158: invokevirtual createFloat : (F)Lcom/google/common/hash/HashCode;
/*     */     //   161: goto -> 172
/*     */     //   164: aload_0
/*     */     //   165: aload_1
/*     */     //   166: invokevirtual doubleValue : ()D
/*     */     //   169: invokevirtual createDouble : (D)Lcom/google/common/hash/HashCode;
/*     */     //   172: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #113	-> 0
/*     */     //   #114	-> 56
/*     */     //   #115	-> 74
/*     */     //   #116	-> 92
/*     */     //   #117	-> 110
/*     */     //   #119	-> 128
/*     */     //   #120	-> 146
/*     */     //   #121	-> 164
/*     */     //   #113	-> 172
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   62	12	4	v	Ljava/lang/Byte;
/*     */     //   80	12	5	v	Ljava/lang/Short;
/*     */     //   98	12	6	v	Ljava/lang/Integer;
/*     */     //   116	12	7	v	Ljava/lang/Long;
/*     */     //   134	12	8	v	Ljava/lang/Double;
/*     */     //   152	12	9	v	Ljava/lang/Float;
/*     */     //   0	173	0	this	Lnet/minecraft/util/HashOps;
/*     */     //   0	173	1	value	Ljava/lang/Number; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public HashCode createByte(byte value) { return this.hashFunction.newHasher(2).putByte((byte)6).putByte(value).hash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public HashCode createShort(short value) { return this.hashFunction.newHasher(3).putByte((byte)7).putShort(value).hash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public HashCode createInt(int value) { return this.hashFunction.newHasher(5).putByte((byte)8).putInt(value).hash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   public HashCode createLong(long value) { return this.hashFunction.newHasher(9).putByte((byte)9).putLong(value).hash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public HashCode createFloat(float value) { return this.hashFunction.newHasher(5).putByte((byte)10).putFloat(value).hash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public HashCode createDouble(double value) { return this.hashFunction.newHasher(9).putByte((byte)11).putDouble(value).hash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   public HashCode createString(String value) { return this.hashFunction.newHasher().putByte((byte)12).putInt(value.length()).putUnencodedChars(value).hash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public HashCode createBoolean(boolean value) { return value ? this.trueHash : this.falseHash; }
/*     */ 
/*     */   
/*     */   private static Hasher hashMap(Hasher hasher, Map<HashCode, HashCode> map) {
/* 166 */     hasher.putByte((byte)2);
/* 167 */     map.entrySet().stream().sorted(MAP_ENTRY_ORDER).forEach(e -> hasher.putBytes(((HashCode)e.getKey()).asBytes()).putBytes(((HashCode)e.getValue()).asBytes()));
/* 168 */     hasher.putByte((byte)3);
/* 169 */     return hasher;
/*     */   }
/*     */   
/*     */   private static Hasher hashMap(Hasher hasher, Stream<Pair<HashCode, HashCode>> map) {
/* 173 */     hasher.putByte((byte)2);
/* 174 */     map.sorted(MAPLIKE_ENTRY_ORDER).forEach(e -> hasher.putBytes(((HashCode)e.getFirst()).asBytes()).putBytes(((HashCode)e.getSecond()).asBytes()));
/* 175 */     hasher.putByte((byte)3);
/* 176 */     return hasher;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public HashCode createMap(Stream<Pair<HashCode, HashCode>> map) { return hashMap(this.hashFunction.newHasher(), map).hash(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public HashCode createMap(Map<HashCode, HashCode> map) { return hashMap(this.hashFunction.newHasher(), map).hash(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode createList(Stream<HashCode> input) {
/* 191 */     Hasher hasher = this.hashFunction.newHasher();
/* 192 */     hasher.putByte((byte)4);
/* 193 */     input.forEach(value -> hasher.putBytes(value.asBytes()));
/* 194 */     hasher.putByte((byte)5);
/* 195 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode createByteList(ByteBuffer input) {
/* 200 */     Hasher hasher = this.hashFunction.newHasher();
/* 201 */     hasher.putByte((byte)14);
/* 202 */     hasher.putBytes(input);
/* 203 */     hasher.putByte((byte)15);
/* 204 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode createIntList(IntStream input) {
/* 209 */     Hasher hasher = this.hashFunction.newHasher();
/* 210 */     hasher.putByte((byte)16);
/* 211 */     Objects.requireNonNull(hasher); input.forEach(hasher::putInt);
/* 212 */     hasher.putByte((byte)17);
/* 213 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode createLongList(LongStream input) {
/* 218 */     Hasher hasher = this.hashFunction.newHasher();
/* 219 */     hasher.putByte((byte)18);
/* 220 */     Objects.requireNonNull(hasher); input.forEach(hasher::putLong);
/* 221 */     hasher.putByte((byte)19);
/* 222 */     return hasher.hash();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public HashCode remove(HashCode input, String key) { return input; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   public RecordBuilder<HashCode> mapBuilder() { return new MapHashBuilder(); }
/*     */   
/*     */   private final class MapHashBuilder
/*     */     extends RecordBuilder.AbstractUniversalBuilder<HashCode, List<Pair<HashCode, HashCode>>> {
/*     */     public MapHashBuilder() {
/* 238 */       super(HashOps.this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 243 */     protected List<Pair<HashCode, HashCode>> initBuilder() { return new ArrayList(); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected List<Pair<HashCode, HashCode>> append(HashCode key, HashCode value, List<Pair<HashCode, HashCode>> builder) {
/* 248 */       builder.add(Pair.of(key, value));
/* 249 */       return builder;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<HashCode> build(List<Pair<HashCode, HashCode>> builder, HashCode prefix) {
/* 254 */       assert HashOps.this.isEmpty(prefix);
/* 255 */       return DataResult.success(HashOps.hashMap(HashOps.this.hashFunction.newHasher(), builder.stream()).hash());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 261 */   public ListBuilder<HashCode> listBuilder() { return new ListHashBuilder(); }
/*     */   
/*     */   private class ListHashBuilder
/*     */     extends AbstractListBuilder<HashCode, Hasher> {
/*     */     public ListHashBuilder() {
/* 266 */       super(HashOps.this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 271 */     protected Hasher initBuilder() { return HashOps.this.hashFunction.newHasher().putByte((byte)4); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     protected Hasher append(Hasher hasher, HashCode value) { return hasher.putBytes(value.asBytes()); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected DataResult<HashCode> build(Hasher hasher, HashCode prefix) {
/* 281 */       assert prefix.equals(HashOps.this.empty);
/* 282 */       hasher.putByte((byte)5);
/* 283 */       return DataResult.success(hasher.hash());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 289 */   public String toString() { return "Hash " + String.valueOf(this.hashFunction); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 296 */   public <U> U convertTo(DynamicOps<U> outOps, HashCode input) { throw new UnsupportedOperationException("Can't convert from this type"); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 301 */   public Number getNumberValue(HashCode input, Number defaultValue) { return defaultValue; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 306 */   public HashCode set(HashCode input, String key, HashCode value) { return input; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 311 */   public HashCode update(HashCode input, String key, Function<HashCode, HashCode> function) { return input; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 316 */   public HashCode updateGeneric(HashCode input, HashCode key, Function<HashCode, HashCode> function) { return input; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 321 */   private static <T> DataResult<T> unsupported() { return UNSUPPORTED_OPERATION_ERROR; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   public DataResult<HashCode> get(HashCode input, String key) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 331 */   public DataResult<HashCode> getGeneric(HashCode input, HashCode key) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 336 */   public DataResult<Number> getNumberValue(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 341 */   public DataResult<Boolean> getBooleanValue(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 346 */   public DataResult<String> getStringValue(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */   
/* 350 */   private boolean isEmpty(HashCode value) { return value.equals(this.empty); }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<HashCode> mergeToList(HashCode prefix, HashCode value) {
/* 355 */     if (isEmpty(prefix)) {
/* 356 */       return DataResult.success(createList(Stream.of(value)));
/*     */     }
/* 358 */     return unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<HashCode> mergeToList(HashCode prefix, List<HashCode> values) {
/* 363 */     if (isEmpty(prefix)) {
/* 364 */       return DataResult.success(createList(values.stream()));
/*     */     }
/*     */     
/* 367 */     return unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<HashCode> mergeToMap(HashCode prefix, HashCode key, HashCode value) {
/* 372 */     if (isEmpty(prefix)) {
/* 373 */       return DataResult.success(createMap(Map.of(key, value)));
/*     */     }
/* 375 */     return unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<HashCode> mergeToMap(HashCode prefix, Map<HashCode, HashCode> values) {
/* 380 */     if (isEmpty(prefix)) {
/* 381 */       return DataResult.success(createMap(values));
/*     */     }
/* 383 */     return unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<HashCode> mergeToMap(HashCode prefix, MapLike<HashCode> values) {
/* 388 */     if (isEmpty(prefix)) {
/* 389 */       return DataResult.success(createMap(values.entries()));
/*     */     }
/* 391 */     return unsupported();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 396 */   public DataResult<Stream<Pair<HashCode, HashCode>>> getMapValues(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 401 */   public DataResult<Consumer<BiConsumer<HashCode, HashCode>>> getMapEntries(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 406 */   public DataResult<Stream<HashCode>> getStream(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 411 */   public DataResult<Consumer<Consumer<HashCode>>> getList(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 416 */   public DataResult<MapLike<HashCode>> getMap(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 421 */   public DataResult<ByteBuffer> getByteBuffer(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 426 */   public DataResult<IntStream> getIntStream(HashCode input) { return unsupported(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 431 */   public DataResult<LongStream> getLongStream(HashCode input) { return unsupported(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\HashOps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */