/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Optional;
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
/*     */ public interface Tag
/*     */ {
/*     */   public static final int OBJECT_HEADER = 8;
/*     */   public static final int ARRAY_HEADER = 12;
/*     */   public static final int OBJECT_REFERENCE = 4;
/*     */   public static final int STRING_SIZE = 28;
/*     */   public static final byte TAG_END = 0;
/*     */   public static final byte TAG_BYTE = 1;
/*     */   public static final byte TAG_SHORT = 2;
/*     */   public static final byte TAG_INT = 3;
/*     */   public static final byte TAG_LONG = 4;
/*     */   public static final byte TAG_FLOAT = 5;
/*     */   public static final byte TAG_DOUBLE = 6;
/*     */   public static final byte TAG_BYTE_ARRAY = 7;
/*     */   public static final byte TAG_STRING = 8;
/*     */   public static final byte TAG_LIST = 9;
/*     */   public static final byte TAG_COMPOUND = 10;
/*     */   public static final byte TAG_INT_ARRAY = 11;
/*     */   public static final byte TAG_LONG_ARRAY = 12;
/*     */   public static final int MAX_DEPTH = 512;
/*     */   
/*     */   default void acceptAsRoot(StreamTagVisitor output) {
/*  52 */     StreamTagVisitor.ValueResult entryResult = output.visitRootEntry(getType());
/*  53 */     if (entryResult == StreamTagVisitor.ValueResult.CONTINUE) {
/*  54 */       accept(output);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  59 */   default Optional<String> asString() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */   
/*  63 */   default Optional<Number> asNumber() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */   
/*  67 */   default Optional<Byte> asByte() { return asNumber().map(Number::byteValue); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   default Optional<Short> asShort() { return asNumber().map(Number::shortValue); }
/*     */ 
/*     */ 
/*     */   
/*  75 */   default Optional<Integer> asInt() { return asNumber().map(Number::intValue); }
/*     */ 
/*     */ 
/*     */   
/*  79 */   default Optional<Long> asLong() { return asNumber().map(Number::longValue); }
/*     */ 
/*     */ 
/*     */   
/*  83 */   default Optional<Float> asFloat() { return asNumber().map(Number::floatValue); }
/*     */ 
/*     */ 
/*     */   
/*  87 */   default Optional<Double> asDouble() { return asNumber().map(Number::doubleValue); }
/*     */ 
/*     */ 
/*     */   
/*  91 */   default Optional<Boolean> asBoolean() { return asByte().map(b -> Boolean.valueOf((b.byteValue() != 0))); }
/*     */ 
/*     */ 
/*     */   
/*  95 */   default Optional<byte[]> asByteArray() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */   
/*  99 */   default Optional<int[]> asIntArray() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   default Optional<long[]> asLongArray() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   default Optional<CompoundTag> asCompound() { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */   
/* 111 */   default Optional<ListTag> asList() { return Optional.empty(); }
/*     */   
/*     */   void write(DataOutput paramDataOutput) throws IOException;
/*     */   
/*     */   String toString();
/*     */   
/*     */   byte getId();
/*     */   
/*     */   TagType<?> getType();
/*     */   
/*     */   Tag copy();
/*     */   
/*     */   int sizeInBytes();
/*     */   
/*     */   void accept(TagVisitor paramTagVisitor);
/*     */   
/*     */   StreamTagVisitor.ValueResult accept(StreamTagVisitor paramStreamTagVisitor);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\Tag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */