/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Keyable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public interface StringRepresentable
/*     */ {
/*     */   public static final int PRE_BUILT_MAP_THRESHOLD = 16;
/*     */   
/*     */   public static class StringRepresentableCodec<S extends StringRepresentable>
/*     */     extends Object
/*     */     implements Codec<S>
/*     */   {
/*     */     private final Codec<S> codec;
/*     */     
/*     */     public StringRepresentableCodec(S[] valueArray, Function<String, S> nameResolver, ToIntFunction<S> idResolver) {
/*  28 */       this.codec = ExtraCodecs.orCompressed(
/*  29 */           Codec.stringResolver(StringRepresentable::getSerializedName, nameResolver), 
/*  30 */           ExtraCodecs.idResolverCodec(idResolver, i -> (i >= 0 && i < valueArray.length) ? valueArray[i] : null, -1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  36 */     public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) { return this.codec.decode(ops, input); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  41 */     public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) { return this.codec.encode(input, ops, prefix); }
/*     */   }
/*     */   
/*     */   public static class EnumCodec<E extends Enum<E> & StringRepresentable>
/*     */     extends StringRepresentableCodec<E> {
/*     */     private final Function<String, E> resolver;
/*     */     
/*     */     public EnumCodec(E[] valueArray, Function<String, E> nameResolver) {
/*  49 */       super(valueArray, nameResolver, rec$ -> ((Enum)rec$).ordinal());
/*  50 */       this.resolver = nameResolver;
/*     */     }
/*     */ 
/*     */     
/*  54 */     public E byName(String name) { return (E)(Enum)this.resolver.apply(name); }
/*     */ 
/*     */ 
/*     */     
/*  58 */     public E byName(String name, E _default) { return (E)(Enum)Objects.requireNonNullElse(byName(name), _default); }
/*     */ 
/*     */ 
/*     */     
/*  62 */     public E byName(String name, Supplier<? extends E> defaultSupplier) { return (E)(Enum)Objects.requireNonNullElseGet(byName(name), defaultSupplier); }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   static <E extends Enum<E> & StringRepresentable> EnumCodec<E> fromEnum(Supplier<E[]> values) { return fromEnumWithMapping(values, s -> s); }
/*     */ 
/*     */   
/*     */   static <E extends Enum<E> & StringRepresentable> EnumCodec<E> fromEnumWithMapping(Supplier<E[]> values, Function<String, String> converter) {
/*  74 */     E[] valueArray = (E[])(Enum[])values.get();
/*  75 */     Function<String, E> lookupFunction = createNameLookup(valueArray, e -> (String)converter.apply(((StringRepresentable)e).getSerializedName()));
/*  76 */     return new EnumCodec(valueArray, lookupFunction);
/*     */   }
/*     */   
/*     */   static <T extends StringRepresentable> Codec<T> fromValues(Supplier<T[]> values) {
/*  80 */     T[] valueArray = (T[])(StringRepresentable[])values.get();
/*  81 */     Function<String, T> lookupFunction = createNameLookup(valueArray);
/*  82 */     ToIntFunction<T> indexLookup = Util.createIndexLookup(Arrays.asList(valueArray));
/*  83 */     return new StringRepresentableCodec(valueArray, lookupFunction, indexLookup);
/*     */   }
/*     */ 
/*     */   
/*  87 */   static <T extends StringRepresentable> Function<String, T> createNameLookup(T[] valueArray) { return createNameLookup(valueArray, StringRepresentable::getSerializedName); }
/*     */ 
/*     */   
/*     */   static <T> Function<String, T> createNameLookup(T[] valueArray, Function<T, String> converter) {
/*  91 */     if (valueArray.length > 16) {
/*  92 */       Map<String, T> byName = (Map)Arrays.stream(valueArray).collect(Collectors.toMap(converter, d -> d));
/*  93 */       Objects.requireNonNull(byName); return byName::get;
/*     */     } 
/*  95 */     return id -> {
/*  96 */         Object[] arrayOfObject; int i; byte b; for (arrayOfObject = valueArray, i = arrayOfObject.length, b = 0; b < i; ) { T value = (T)arrayOfObject[b];
/*  97 */           if (((String)converter.apply(value)).equals(id))
/*  98 */             return value; 
/*     */           b++; }
/*     */         
/* 101 */         return null;
/*     */       };
/*     */   }
/*     */   
/*     */   static Keyable keys(final StringRepresentable[] values) {
/* 106 */     return new Keyable()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 109 */           Objects.requireNonNull(ops); return Arrays.stream(values).map(StringRepresentable::getSerializedName).map(ops::createString);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   String getSerializedName();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\StringRepresentable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */