/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class CompoundTag implements Tag {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  29 */   public static final Codec<CompoundTag> CODEC = Codec.PASSTHROUGH.comapFlatMap(t -> {
/*     */         
/*  31 */         Tag tag = (Tag)t.convert(NbtOps.INSTANCE).getValue();
/*  32 */         if (tag instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)tag;
/*     */           
/*  34 */           return DataResult.success((compoundTag == t.getValue()) ? compoundTag.copy() : compoundTag); }
/*     */         
/*  36 */         return DataResult.error(());
/*     */       
/*  38 */       }t -> new Dynamic(NbtOps.INSTANCE, t.copy()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int SELF_SIZE_IN_BYTES = 48;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAP_ENTRY_SIZE_IN_BYTES = 32;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final TagType<CompoundTag> TYPE = new TagType.VariableSize<CompoundTag>()
/*     */     {
/*     */       public CompoundTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  67 */         accounter.pushDepth();
/*     */         try {
/*  69 */           return CompoundTag.null.loadCompound(input, accounter);
/*     */         } finally {
/*  71 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static CompoundTag loadCompound(DataInput input, NbtAccounter accounter) throws IOException {
/*  76 */         accounter.accountBytes(48L);
/*     */         
/*  78 */         Map<String, Tag> values = Maps.newHashMap();
/*     */         byte tagType;
/*  80 */         while ((tagType = input.readByte()) != 0) {
/*  81 */           String key = CompoundTag.null.readString(input, accounter);
/*  82 */           Tag tag = CompoundTag.readNamedTagData(TagTypes.getType(tagType), key, input, accounter);
/*  83 */           if (values.put(key, tag) == null) {
/*  84 */             accounter.accountBytes(36L);
/*     */           }
/*     */         } 
/*  87 */         return new CompoundTag(values);
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  92 */         accounter.pushDepth();
/*     */         try {
/*  94 */           return CompoundTag.null.parseCompound(input, output, accounter);
/*     */         } finally {
/*  96 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static StreamTagVisitor.ValueResult parseCompound(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/* 101 */         accounter.accountBytes(48L);
/*     */ 
/*     */         
/*     */         byte tagTypeId;
/*     */         
/* 106 */         while ((tagTypeId = input.readByte()) != 0) {
/* 107 */           TagType<?> tagType = TagTypes.getType(tagTypeId);
/*     */           
/* 109 */           switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitEntry(tagType).ordinal()]) {
/*     */             case 1:
/* 111 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case 2:
/* 113 */               StringTag.skipString(input);
/* 114 */               tagType.skip(input, accounter);
/*     */               break;
/*     */             case 3:
/* 117 */               StringTag.skipString(input);
/* 118 */               tagType.skip(input, accounter);
/*     */               continue;
/*     */           } 
/*     */           
/* 122 */           String key = CompoundTag.null.readString(input, accounter);
/* 123 */           switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitEntry(tagType, key).ordinal()]) {
/*     */             case 1:
/* 125 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case 2:
/* 127 */               tagType.skip(input, accounter);
/*     */               break;
/*     */             case 3:
/* 130 */               tagType.skip(input, accounter);
/*     */               continue;
/*     */           } 
/*     */           
/* 134 */           accounter.accountBytes(36L);
/* 135 */           switch (CompoundTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[tagType.parse(input, output, accounter).ordinal()]) {
/*     */             case 1:
/* 137 */               return StreamTagVisitor.ValueResult.HALT;
/*     */           } 
/*     */ 
/*     */ 
/*     */         
/*     */         } 
/* 143 */         if (tagTypeId != 0) {
/* 144 */           while ((tagTypeId = input.readByte()) != 0) {
/* 145 */             StringTag.skipString(input);
/* 146 */             TagTypes.getType(tagTypeId).skip(input, accounter);
/*     */           } 
/*     */         }
/*     */         
/* 150 */         return output.visitContainerEnd();
/*     */       }
/*     */       
/*     */       private static String readString(DataInput input, NbtAccounter accounter) throws IOException {
/* 154 */         String key = input.readUTF();
/* 155 */         accounter.accountBytes(28L);
/* 156 */         accounter.accountBytes(2L, key.length());
/* 157 */         return key;
/*     */       }
/*     */ 
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 162 */         accounter.pushDepth();
/*     */         try {
/*     */           byte tagTypeId;
/* 165 */           while ((tagTypeId = input.readByte()) != 0) {
/* 166 */             StringTag.skipString(input);
/* 167 */             TagTypes.getType(tagTypeId).skip(input, accounter);
/*     */           } 
/*     */         } finally {
/* 170 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 176 */       public String getName() { return "COMPOUND"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       public String getPrettyName() { return "TAG_Compound"; }
/*     */     };
/*     */ 
/*     */   
/*     */   private final Map<String, Tag> tags;
/*     */ 
/*     */   
/* 188 */   CompoundTag(Map<String, Tag> tags) { this.tags = tags; }
/*     */ 
/*     */ 
/*     */   
/* 192 */   public CompoundTag() { this(new HashMap()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/* 197 */     for (String key : this.tags.keySet()) {
/* 198 */       Tag tag = (Tag)this.tags.get(key);
/* 199 */       writeNamedTag(key, tag, output);
/*     */     } 
/* 201 */     output.writeByte(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/* 206 */     int size = 48;
/* 207 */     for (Map.Entry<String, Tag> entry : this.tags.entrySet()) {
/* 208 */       size += 28 + 2 * ((String)entry.getKey()).length();
/* 209 */       size += 36;
/* 210 */       size += ((Tag)entry.getValue()).sizeInBytes();
/*     */     } 
/* 212 */     return size;
/*     */   }
/*     */ 
/*     */   
/* 216 */   public Set<String> keySet() { return this.tags.keySet(); }
/*     */ 
/*     */ 
/*     */   
/* 220 */   public Set<Map.Entry<String, Tag>> entrySet() { return this.tags.entrySet(); }
/*     */ 
/*     */ 
/*     */   
/* 224 */   public Collection<Tag> values() { return this.tags.values(); }
/*     */ 
/*     */ 
/*     */   
/* 228 */   public void forEach(BiConsumer<String, Tag> consumer) { this.tags.forEach(consumer); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   public byte getId() { return 10; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 238 */   public TagType<CompoundTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */   
/* 242 */   public int size() { return this.tags.size(); }
/*     */ 
/*     */ 
/*     */   
/* 246 */   public Tag put(String name, Tag tag) { return (Tag)this.tags.put(name, tag); }
/*     */ 
/*     */ 
/*     */   
/* 250 */   public void putByte(String name, byte value) { this.tags.put(name, ByteTag.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 254 */   public void putShort(String name, short value) { this.tags.put(name, ShortTag.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 258 */   public void putInt(String name, int value) { this.tags.put(name, IntTag.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 262 */   public void putLong(String name, long value) { this.tags.put(name, LongTag.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 266 */   public void putFloat(String name, float value) { this.tags.put(name, FloatTag.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 270 */   public void putDouble(String name, double value) { this.tags.put(name, DoubleTag.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 274 */   public void putString(String name, String value) { this.tags.put(name, StringTag.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 278 */   public void putByteArray(String name, byte[] value) { this.tags.put(name, new ByteArrayTag(value)); }
/*     */ 
/*     */ 
/*     */   
/* 282 */   public void putIntArray(String name, int[] value) { this.tags.put(name, new IntArrayTag(value)); }
/*     */ 
/*     */ 
/*     */   
/* 286 */   public void putLongArray(String name, long[] value) { this.tags.put(name, new LongArrayTag(value)); }
/*     */ 
/*     */ 
/*     */   
/* 290 */   public void putBoolean(String name, boolean value) { this.tags.put(name, ByteTag.valueOf(value)); }
/*     */ 
/*     */ 
/*     */   
/* 294 */   public Tag get(String name) { return (Tag)this.tags.get(name); }
/*     */ 
/*     */ 
/*     */   
/* 298 */   public boolean contains(String name) { return this.tags.containsKey(name); }
/*     */ 
/*     */ 
/*     */   
/* 302 */   private Optional<Tag> getOptional(String name) { return Optional.ofNullable((Tag)this.tags.get(name)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 307 */   public Optional<Byte> getByte(String name) { return getOptional(name).flatMap(Tag::asByte); }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getByteOr(String name, byte defaultValue) {
/* 312 */     Object object = this.tags.get(name); if (object instanceof NumericTag) { NumericTag tag = (NumericTag)object;
/* 313 */       return tag.byteValue(); }
/*     */     
/* 315 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 320 */   public Optional<Short> getShort(String name) { return getOptional(name).flatMap(Tag::asShort); }
/*     */ 
/*     */   
/*     */   public short getShortOr(String name, short defaultValue) {
/* 324 */     Object object = this.tags.get(name); if (object instanceof NumericTag) { NumericTag tag = (NumericTag)object;
/* 325 */       return tag.shortValue(); }
/*     */     
/* 327 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 332 */   public Optional<Integer> getInt(String name) { return getOptional(name).flatMap(Tag::asInt); }
/*     */ 
/*     */   
/*     */   public int getIntOr(String name, int defaultValue) {
/* 336 */     Object object = this.tags.get(name); if (object instanceof NumericTag) { NumericTag tag = (NumericTag)object;
/* 337 */       return tag.intValue(); }
/*     */     
/* 339 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 344 */   public Optional<Long> getLong(String name) { return getOptional(name).flatMap(Tag::asLong); }
/*     */ 
/*     */   
/*     */   public long getLongOr(String name, long defaultValue) {
/* 348 */     Object object = this.tags.get(name); if (object instanceof NumericTag) { NumericTag tag = (NumericTag)object;
/* 349 */       return tag.longValue(); }
/*     */     
/* 351 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 356 */   public Optional<Float> getFloat(String name) { return getOptional(name).flatMap(Tag::asFloat); }
/*     */ 
/*     */   
/*     */   public float getFloatOr(String name, float defaultValue) {
/* 360 */     Object object = this.tags.get(name); if (object instanceof NumericTag) { NumericTag tag = (NumericTag)object;
/* 361 */       return tag.floatValue(); }
/*     */     
/* 363 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 368 */   public Optional<Double> getDouble(String name) { return getOptional(name).flatMap(Tag::asDouble); }
/*     */ 
/*     */   
/*     */   public double getDoubleOr(String name, double defaultValue) {
/* 372 */     Object object = this.tags.get(name); if (object instanceof NumericTag) { NumericTag tag = (NumericTag)object;
/* 373 */       return tag.doubleValue(); }
/*     */     
/* 375 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 380 */   public Optional<String> getString(String name) { return getOptional(name).flatMap(Tag::asString); }
/*     */ 
/*     */   
/*     */   public String getStringOr(String name, String defaultValue) {
/* 384 */     Object object = this.tags.get(name); if (object instanceof StringTag) { stringTag = (StringTag)object; try { String str; return str = stringTag.value(); } catch (Throwable stringTag) { throw new MatchException(stringTag.toString(), stringTag); }
/*     */        }
/*     */     
/* 387 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<byte[]> getByteArray(String name) {
/* 391 */     Object object = this.tags.get(name); if (object instanceof ByteArrayTag) { ByteArrayTag tag = (ByteArrayTag)object;
/* 392 */       return Optional.of(tag.getAsByteArray()); }
/*     */     
/* 394 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<int[]> getIntArray(String name) {
/* 398 */     Object object = this.tags.get(name); if (object instanceof IntArrayTag) { IntArrayTag tag = (IntArrayTag)object;
/* 399 */       return Optional.of(tag.getAsIntArray()); }
/*     */     
/* 401 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<long[]> getLongArray(String name) {
/* 405 */     Object object = this.tags.get(name); if (object instanceof LongArrayTag) { LongArrayTag tag = (LongArrayTag)object;
/* 406 */       return Optional.of(tag.getAsLongArray()); }
/*     */     
/* 408 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<CompoundTag> getCompound(String name) {
/* 412 */     Object object = this.tags.get(name); if (object instanceof CompoundTag) { CompoundTag tag = (CompoundTag)object;
/* 413 */       return Optional.of(tag); }
/*     */     
/* 415 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/* 419 */   public CompoundTag getCompoundOrEmpty(String name) { return (CompoundTag)getCompound(name).orElseGet(CompoundTag::new); }
/*     */ 
/*     */   
/*     */   public Optional<ListTag> getList(String name) {
/* 423 */     Object object = this.tags.get(name); if (object instanceof ListTag) { ListTag tag = (ListTag)object;
/* 424 */       return Optional.of(tag); }
/*     */     
/* 426 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/* 430 */   public ListTag getListOrEmpty(String name) { return (ListTag)getList(name).orElseGet(ListTag::new); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 435 */   public Optional<Boolean> getBoolean(String name) { return getOptional(name).flatMap(Tag::asBoolean); }
/*     */ 
/*     */ 
/*     */   
/* 439 */   public boolean getBooleanOr(String string, boolean defaultValue) { return (getByteOr(string, defaultValue ? 1 : 0) != 0); }
/*     */ 
/*     */ 
/*     */   
/* 443 */   public Tag remove(String name) { return (Tag)this.tags.remove(name); }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 448 */     StringTagVisitor visitor = new StringTagVisitor();
/* 449 */     visitor.visitCompound(this);
/* 450 */     return visitor.build();
/*     */   }
/*     */ 
/*     */   
/* 454 */   public boolean isEmpty() { return this.tags.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 458 */   protected CompoundTag shallowCopy() { return new CompoundTag(new HashMap(this.tags)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag copy() {
/* 464 */     HashMap<String, Tag> newTags = new HashMap<String, Tag>();
/* 465 */     this.tags.forEach((key, tag) -> newTags.put(key, tag.copy()));
/* 466 */     return new CompoundTag(newTags);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 471 */   public Optional<CompoundTag> asCompound() { return Optional.of(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 476 */     if (this == obj) {
/* 477 */       return true;
/*     */     }
/*     */     
/* 480 */     return (obj instanceof CompoundTag && Objects.equals(this.tags, ((CompoundTag)obj).tags));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 485 */   public int hashCode() { return this.tags.hashCode(); }
/*     */ 
/*     */   
/*     */   private static void writeNamedTag(String name, Tag tag, DataOutput output) throws IOException {
/* 489 */     output.writeByte(tag.getId());
/* 490 */     if (tag.getId() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 494 */     output.writeUTF(name);
/*     */     
/* 496 */     tag.write(output);
/*     */   }
/*     */   
/*     */   private static Tag readNamedTagData(TagType<?> type, String name, DataInput input, NbtAccounter accounter) {
/*     */     try {
/* 501 */       return type.load(input, accounter);
/* 502 */     } catch (IOException e) {
/* 503 */       CrashReport report = CrashReport.forThrowable(e, "Loading NBT data");
/* 504 */       CrashReportCategory category = report.addCategory("NBT Tag");
/* 505 */       category.setDetail("Tag name", name);
/* 506 */       category.setDetail("Tag type", type.getName());
/* 507 */       throw new ReportedNbtException(report);
/*     */     } 
/*     */   }
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
/*     */   public CompoundTag merge(CompoundTag other) {
/* 525 */     for (String tagName : other.tags.keySet()) {
/* 526 */       Tag otherTag = (Tag)other.tags.get(tagName);
/*     */ 
/*     */       
/* 529 */       if (otherTag instanceof CompoundTag) { CompoundTag otherCompound = (CompoundTag)otherTag; Object object = this.tags.get(tagName); if (object instanceof CompoundTag) { CompoundTag selfCompound = (CompoundTag)object;
/* 530 */           selfCompound.merge(otherCompound); continue; }
/*     */          }
/* 532 */        put(tagName, otherTag.copy());
/*     */     } 
/*     */     
/* 535 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 540 */   public void accept(TagVisitor visitor) { visitor.visitCompound(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 545 */     for (Map.Entry<String, Tag> entry : this.tags.entrySet()) {
/* 546 */       Tag value = (Tag)entry.getValue();
/* 547 */       TagType<?> type = value.getType();
/* 548 */       StreamTagVisitor.EntryResult entryParseResult = visitor.visitEntry(type);
/* 549 */       switch (entryParseResult) {
/*     */         case HALT:
/* 551 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case BREAK:
/* 553 */           return visitor.visitContainerEnd();
/*     */         
/*     */         case null:
/*     */           continue;
/*     */       } 
/* 558 */       entryParseResult = visitor.visitEntry(type, (String)entry.getKey());
/* 559 */       switch (entryParseResult) {
/*     */         case HALT:
/* 561 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case BREAK:
/* 563 */           return visitor.visitContainerEnd();
/*     */         
/*     */         case null:
/*     */           continue;
/*     */       } 
/* 568 */       StreamTagVisitor.ValueResult valueResult = value.accept(visitor);
/* 569 */       switch (valueResult) {
/*     */         case HALT:
/* 571 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case BREAK:
/* 573 */           return visitor.visitContainerEnd();
/*     */       } 
/*     */     } 
/* 576 */     return visitor.visitContainerEnd();
/*     */   }
/*     */ 
/*     */   
/* 580 */   public <T> void store(String name, Codec<T> codec, T value) { store(name, codec, NbtOps.INSTANCE, value); }
/*     */ 
/*     */   
/*     */   public <T> void storeNullable(String name, Codec<T> codec, T value) {
/* 584 */     if (value != null) {
/* 585 */       store(name, codec, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 590 */   public <T> void store(String name, Codec<T> codec, DynamicOps<Tag> ops, T value) { put(name, (Tag)codec.encodeStart(ops, value).getOrThrow()); }
/*     */ 
/*     */   
/*     */   public <T> void storeNullable(String name, Codec<T> codec, DynamicOps<Tag> ops, T value) {
/* 594 */     if (value != null) {
/* 595 */       store(name, codec, ops, value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 600 */   public <T> void store(MapCodec<T> codec, T value) { store(codec, NbtOps.INSTANCE, value); }
/*     */ 
/*     */ 
/*     */   
/* 604 */   public <T> void store(MapCodec<T> codec, DynamicOps<Tag> ops, T value) { merge((CompoundTag)codec.encoder().encodeStart(ops, value).getOrThrow()); }
/*     */ 
/*     */ 
/*     */   
/* 608 */   public <T> Optional<T> read(String name, Codec<T> codec) { return read(name, codec, NbtOps.INSTANCE); }
/*     */ 
/*     */   
/*     */   public <T> Optional<T> read(String name, Codec<T> codec, DynamicOps<Tag> ops) {
/* 612 */     Tag tag = get(name);
/* 613 */     if (tag == null) {
/* 614 */       return Optional.empty();
/*     */     }
/* 616 */     return codec.parse(ops, tag)
/* 617 */       .resultOrPartial(error -> LOGGER.error("Failed to read field ({}={}): {}", new Object[] { name, tag, error }));
/*     */   }
/*     */ 
/*     */   
/* 621 */   public <T> Optional<T> read(MapCodec<T> codec) { return read(codec, NbtOps.INSTANCE); }
/*     */ 
/*     */ 
/*     */   
/* 625 */   public <T> Optional<T> read(MapCodec<T> codec, DynamicOps<Tag> ops) { return codec.decode(ops, (MapLike)ops.getMap(this).getOrThrow())
/* 626 */       .resultOrPartial(error -> LOGGER.error("Failed to read value ({}): {}", this, error)); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\CompoundTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */