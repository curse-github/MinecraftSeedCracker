/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public abstract class DelegatingOps<T>
/*     */   extends Object
/*     */   implements DynamicOps<T> {
/*     */   protected final DynamicOps<T> delegate;
/*     */   
/*  27 */   protected DelegatingOps(DynamicOps<T> delegate) { this.delegate = delegate; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   public T empty() { return (T)this.delegate.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public T emptyMap() { return (T)this.delegate.emptyMap(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public T emptyList() { return (T)this.delegate.emptyList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> outOps, T input) {
/*  48 */     if (Objects.equals(outOps, this.delegate)) {
/*  49 */       return (U)input;
/*     */     }
/*     */     
/*  52 */     return (U)this.delegate.convertTo(outOps, input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   public DataResult<Number> getNumberValue(T input) { return this.delegate.getNumberValue(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public T createNumeric(Number i) { return (T)this.delegate.createNumeric(i); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public T createByte(byte value) { return (T)this.delegate.createByte(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public T createShort(short value) { return (T)this.delegate.createShort(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public T createInt(int value) { return (T)this.delegate.createInt(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public T createLong(long value) { return (T)this.delegate.createLong(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public T createFloat(float value) { return (T)this.delegate.createFloat(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   public T createDouble(double value) { return (T)this.delegate.createDouble(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public DataResult<Boolean> getBooleanValue(T input) { return this.delegate.getBooleanValue(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   public T createBoolean(boolean value) { return (T)this.delegate.createBoolean(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   public DataResult<String> getStringValue(T input) { return this.delegate.getStringValue(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public T createString(String value) { return (T)this.delegate.createString(value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   public DataResult<T> mergeToList(T list, T value) { return this.delegate.mergeToList(list, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   public DataResult<T> mergeToList(T list, List<T> values) { return this.delegate.mergeToList(list, values); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   public DataResult<T> mergeToMap(T map, T key, T value) { return this.delegate.mergeToMap(map, key, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public DataResult<T> mergeToMap(T map, MapLike<T> values) { return this.delegate.mergeToMap(map, values); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   public DataResult<T> mergeToMap(T map, Map<T, T> values) { return this.delegate.mergeToMap(map, values); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   public DataResult<T> mergeToPrimitive(T prefix, T value) { return this.delegate.mergeToPrimitive(prefix, value); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public DataResult<Stream<Pair<T, T>>> getMapValues(T input) { return this.delegate.getMapValues(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   public DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T input) { return this.delegate.getMapEntries(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 157 */   public T createMap(Map<T, T> map) { return (T)this.delegate.createMap(map); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   public T createMap(Stream<Pair<T, T>> map) { return (T)this.delegate.createMap(map); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public DataResult<MapLike<T>> getMap(T input) { return this.delegate.getMap(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   public DataResult<Stream<T>> getStream(T input) { return this.delegate.getStream(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public DataResult<Consumer<Consumer<T>>> getList(T input) { return this.delegate.getList(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   public T createList(Stream<T> input) { return (T)this.delegate.createList(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public DataResult<ByteBuffer> getByteBuffer(T input) { return this.delegate.getByteBuffer(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   public T createByteList(ByteBuffer input) { return (T)this.delegate.createByteList(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   public DataResult<IntStream> getIntStream(T input) { return this.delegate.getIntStream(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   public T createIntList(IntStream input) { return (T)this.delegate.createIntList(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public DataResult<LongStream> getLongStream(T input) { return this.delegate.getLongStream(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   public T createLongList(LongStream input) { return (T)this.delegate.createLongList(input); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   public T remove(T input, String key) { return (T)this.delegate.remove(input, key); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 222 */   public boolean compressMaps() { return this.delegate.compressMaps(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class DelegateListBuilder
/*     */     extends Object
/*     */     implements ListBuilder<T>
/*     */   {
/*     */     private final ListBuilder<T> original;
/*     */ 
/*     */ 
/*     */     
/* 235 */     protected DelegateListBuilder(ListBuilder<T> original) { this.original = original; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 240 */     public DynamicOps<T> ops() { return DelegatingOps.this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 245 */     public DataResult<T> build(T prefix) { return this.original.build(prefix); }
/*     */ 
/*     */ 
/*     */     
/*     */     public ListBuilder<T> add(T value) {
/* 250 */       this.original.add(value);
/* 251 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<T> add(DataResult<T> value) {
/* 256 */       this.original.add(value);
/* 257 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <E> ListBuilder<T> add(E value, Encoder<E> encoder) {
/* 263 */       this.original.add(encoder.encodeStart(ops(), value));
/* 264 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <E> ListBuilder<T> addAll(Iterable<E> values, Encoder<E> encoder) {
/* 270 */       values.forEach(v -> this.original.add(encoder.encode(v, ops(), ops().empty())));
/* 271 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 276 */       this.original.withErrorsFrom(result);
/* 277 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListBuilder<T> mapError(UnaryOperator<String> onError) {
/* 282 */       this.original.mapError(onError);
/* 283 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 288 */     public DataResult<T> build(DataResult<T> prefix) { return this.original.build(prefix); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 294 */   public ListBuilder<T> listBuilder() { return new DelegateListBuilder(this.delegate.listBuilder()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class DelegateRecordBuilder
/*     */     extends Object
/*     */     implements RecordBuilder<T>
/*     */   {
/*     */     private final RecordBuilder<T> original;
/*     */ 
/*     */ 
/*     */     
/* 307 */     protected DelegateRecordBuilder(RecordBuilder<T> original) { this.original = original; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     public DynamicOps<T> ops() { return DelegatingOps.this; }
/*     */ 
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T key, T value) {
/* 317 */       this.original.add(key, value);
/* 318 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(T key, DataResult<T> value) {
/* 323 */       this.original.add(key, value);
/* 324 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(DataResult<T> key, DataResult<T> value) {
/* 329 */       this.original.add(key, value);
/* 330 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(String key, T value) {
/* 335 */       this.original.add(key, value);
/* 336 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> add(String key, DataResult<T> value) {
/* 341 */       this.original.add(key, value);
/* 342 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 348 */     public <E> RecordBuilder<T> add(String key, E value, Encoder<E> encoder) { return this.original.add(key, encoder.encodeStart(ops(), value)); }
/*     */ 
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> withErrorsFrom(DataResult<?> result) {
/* 353 */       this.original.withErrorsFrom(result);
/* 354 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> setLifecycle(Lifecycle lifecycle) {
/* 359 */       this.original.setLifecycle(lifecycle);
/* 360 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public RecordBuilder<T> mapError(UnaryOperator<String> onError) {
/* 365 */       this.original.mapError(onError);
/* 366 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 371 */     public DataResult<T> build(T prefix) { return this.original.build(prefix); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 376 */     public DataResult<T> build(DataResult<T> prefix) { return this.original.build(prefix); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 382 */   public RecordBuilder<T> mapBuilder() { return new DelegateRecordBuilder(this.delegate.mapBuilder()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\DelegatingOps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */