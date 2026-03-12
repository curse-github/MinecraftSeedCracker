/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NullOps
/*     */   extends Object
/*     */   implements DynamicOps<Unit>
/*     */ {
/*  27 */   public static final NullOps INSTANCE = new NullOps();
/*     */   
/*  29 */   private static final MapLike<Unit> EMPTY_MAP = new MapLike<Unit>()
/*     */     {
/*     */       public Unit get(Unit key) {
/*  32 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  37 */       public Unit get(String key) { return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  42 */       public Stream<Pair<Unit, Unit>> entries() { return Stream.empty(); }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public <U> U convertTo(DynamicOps<U> outOps, Unit input) { return (U)outOps.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public Unit empty() { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   public Unit emptyMap() { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public Unit emptyList() { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public Unit createNumeric(Number value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   public Unit createByte(byte value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public Unit createShort(short value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public Unit createInt(int value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public Unit createLong(long value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   public Unit createFloat(float value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   public Unit createDouble(double value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public Unit createBoolean(boolean value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   public Unit createString(String value) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   public DataResult<Number> getNumberValue(Unit input) { return DataResult.success(Integer.valueOf(0)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public DataResult<Boolean> getBooleanValue(Unit input) { return DataResult.success(Boolean.valueOf(false)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   public DataResult<String> getStringValue(Unit input) { return DataResult.success(""); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public DataResult<Unit> mergeToList(Unit input, Unit value) { return DataResult.success(Unit.INSTANCE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   public DataResult<Unit> mergeToList(Unit input, List<Unit> values) { return DataResult.success(Unit.INSTANCE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 141 */   public DataResult<Unit> mergeToMap(Unit input, Unit key, Unit value) { return DataResult.success(Unit.INSTANCE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   public DataResult<Unit> mergeToMap(Unit input, Map<Unit, Unit> values) { return DataResult.success(Unit.INSTANCE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public DataResult<Unit> mergeToMap(Unit input, MapLike<Unit> values) { return DataResult.success(Unit.INSTANCE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public DataResult<Stream<Pair<Unit, Unit>>> getMapValues(Unit input) { return DataResult.success(Stream.empty()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<Unit, Unit>>> getMapEntries(Unit input) {
/* 161 */     return DataResult.success(consumer -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/* 166 */   public DataResult<MapLike<Unit>> getMap(Unit input) { return DataResult.success(EMPTY_MAP); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public DataResult<Stream<Unit>> getStream(Unit input) { return DataResult.success(Stream.empty()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<Consumer<Unit>>> getList(Unit input) {
/* 176 */     return DataResult.success(consumer -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/* 181 */   public DataResult<ByteBuffer> getByteBuffer(Unit input) { return DataResult.success(ByteBuffer.wrap(new byte[0])); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   public DataResult<IntStream> getIntStream(Unit input) { return DataResult.success(IntStream.empty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 191 */   public DataResult<LongStream> getLongStream(Unit input) { return DataResult.success(LongStream.empty()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 196 */   public Unit createMap(Stream<Pair<Unit, Unit>> map) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 201 */   public Unit createMap(Map<Unit, Unit> map) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 206 */   public Unit createList(Stream<Unit> input) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   public Unit createByteList(ByteBuffer input) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 216 */   public Unit createIntList(IntStream input) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   public Unit createLongList(LongStream input) { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 226 */   public Unit remove(Unit input, String key) { return input; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 231 */   public RecordBuilder<Unit> mapBuilder() { return new NullMapBuilder(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 236 */   public ListBuilder<Unit> listBuilder() { return new NullListBuilder(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 241 */   public String toString() { return "Null"; }
/*     */   
/*     */   private static final class NullMapBuilder
/*     */     extends RecordBuilder.AbstractUniversalBuilder<Unit, Unit>
/*     */   {
/* 246 */     public NullMapBuilder(DynamicOps<Unit> ops) { super(ops); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     protected Unit initBuilder() { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     protected Unit append(Unit key, Unit value, Unit builder) { return builder; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     protected DataResult<Unit> build(Unit builder, Unit prefix) { return DataResult.success(prefix); }
/*     */   }
/*     */   
/*     */   private static final class NullListBuilder
/*     */     extends AbstractListBuilder<Unit, Unit>
/*     */   {
/* 267 */     public NullListBuilder(DynamicOps<Unit> ops) { super(ops); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 272 */     protected Unit initBuilder() { return Unit.INSTANCE; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     protected Unit append(Unit builder, Unit value) { return builder; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 282 */     protected DataResult<Unit> build(Unit builder, Unit prefix) { return DataResult.success(builder); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\NullOps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */