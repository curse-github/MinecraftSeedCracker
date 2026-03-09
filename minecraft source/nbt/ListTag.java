/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ListTag
/*     */   extends AbstractList<Tag>
/*     */   implements CollectionTag
/*     */ {
/*     */   private static final String WRAPPER_MARKER = "";
/*     */   private static final int SELF_SIZE_IN_BYTES = 36;
/*     */   
/*  29 */   public static final TagType<ListTag> TYPE = new TagType.VariableSize<ListTag>()
/*     */     {
/*     */       public ListTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  32 */         accounter.pushDepth();
/*     */         try {
/*  34 */           return ListTag.null.loadList(input, accounter);
/*     */         } finally {
/*  36 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static ListTag loadList(DataInput input, NbtAccounter accounter) throws IOException {
/*  41 */         accounter.accountBytes(36L);
/*  42 */         byte typeId = input.readByte();
/*  43 */         int count = ListTag.null.readListCount(input);
/*  44 */         if (typeId == 0 && count > 0) {
/*  45 */           throw new NbtFormatException("Missing type on ListTag");
/*     */         }
/*  47 */         accounter.accountBytes(4L, count);
/*  48 */         TagType<?> type = TagTypes.getType(typeId);
/*  49 */         ListTag list = new ListTag(new ArrayList(count));
/*  50 */         for (int i = 0; i < count; i++) {
/*  51 */           list.addAndUnwrap(type.load(input, accounter));
/*     */         }
/*  53 */         return list;
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  58 */         accounter.pushDepth();
/*     */         try {
/*  60 */           return ListTag.null.parseList(input, output, accounter);
/*     */         } finally {
/*  62 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static StreamTagVisitor.ValueResult parseList(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  67 */         accounter.accountBytes(36L);
/*  68 */         TagType<?> elementType = TagTypes.getType(input.readByte());
/*  69 */         int count = ListTag.null.readListCount(input);
/*  70 */         switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[output.visitList(elementType, count).ordinal()]) {
/*     */           case 1:
/*  72 */             return StreamTagVisitor.ValueResult.HALT;
/*     */           case 2:
/*  74 */             elementType.skip(input, count, accounter);
/*  75 */             return output.visitContainerEnd();
/*     */         } 
/*     */         
/*  78 */         accounter.accountBytes(4L, count);
/*     */         
/*     */         int i;
/*  81 */         for (i = 0; i < count; i++) {
/*  82 */           switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$EntryResult[output.visitElement(elementType, i).ordinal()]) {
/*     */             case 1:
/*  84 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case 2:
/*  86 */               elementType.skip(input, accounter);
/*     */               break;
/*     */             case 3:
/*  89 */               elementType.skip(input, accounter);
/*     */               break;
/*     */             
/*     */             default:
/*  93 */               switch (ListTag.null.$SwitchMap$net$minecraft$nbt$StreamTagVisitor$ValueResult[elementType.parse(input, output, accounter).ordinal()]) {
/*     */                 case 1:
/*  95 */                   return StreamTagVisitor.ValueResult.HALT;
/*     */                 case 2:
/*     */                   break;
/*     */               }  break;
/*     */           } 
/* 100 */         }  int amountToSkip = count - 1 - i;
/* 101 */         if (amountToSkip > 0) {
/* 102 */           elementType.skip(input, amountToSkip, accounter);
/*     */         }
/* 104 */         return output.visitContainerEnd();
/*     */       }
/*     */       
/*     */       private static int readListCount(DataInput input) throws IOException {
/* 108 */         int count = input.readInt();
/* 109 */         if (count < 0) {
/* 110 */           throw new NbtFormatException("ListTag length cannot be negative: " + count);
/*     */         }
/* 112 */         return count;
/*     */       }
/*     */ 
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 117 */         accounter.pushDepth();
/*     */         try {
/* 119 */           TagType<?> type = TagTypes.getType(input.readByte());
/* 120 */           int count = input.readInt();
/* 121 */           type.skip(input, count, accounter);
/*     */         } finally {
/* 123 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 129 */       public String getName() { return "LIST"; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 134 */       public String getPrettyName() { return "TAG_List"; }
/*     */     };
/*     */ 
/*     */   
/*     */   private final List<Tag> list;
/*     */ 
/*     */   
/* 141 */   public ListTag() { this(new ArrayList()); }
/*     */ 
/*     */ 
/*     */   
/* 145 */   ListTag(List<Tag> list) { this.list = list; }
/*     */ 
/*     */   
/*     */   private static Tag tryUnwrap(CompoundTag tag) {
/* 149 */     if (tag.size() == 1) {
/* 150 */       Tag value = tag.get("");
/* 151 */       if (value != null) {
/* 152 */         return value;
/*     */       }
/*     */     } 
/* 155 */     return tag;
/*     */   }
/*     */ 
/*     */   
/* 159 */   private static boolean isWrapper(CompoundTag tag) { return (tag.size() == 1 && tag.contains("")); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Tag wrapIfNeeded(byte elementType, Tag tag) {
/* 164 */     if (elementType != 10) {
/* 165 */       return tag;
/*     */     }
/* 167 */     if (tag instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)tag; if (!isWrapper(compoundTag))
/* 168 */         return compoundTag;  }
/*     */     
/* 170 */     return wrapElement(tag);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 175 */   private static CompoundTag wrapElement(Tag tag) { return new CompoundTag(Map.of("", tag)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/* 180 */     byte elementType = identifyRawElementType();
/* 181 */     output.writeByte(elementType);
/* 182 */     output.writeInt(this.list.size());
/* 183 */     for (Tag element : this.list) {
/* 184 */       wrapIfNeeded(elementType, element).write(output);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   byte identifyRawElementType() {
/* 190 */     byte homogenousType = 0;
/* 191 */     for (Tag element : this.list) {
/* 192 */       byte elementType = element.getId();
/* 193 */       if (homogenousType == 0) {
/* 194 */         homogenousType = elementType; continue;
/* 195 */       }  if (homogenousType != elementType)
/*     */       {
/* 197 */         return 10;
/*     */       }
/*     */     } 
/* 200 */     return homogenousType;
/*     */   }
/*     */   
/*     */   public void addAndUnwrap(Tag tag) {
/* 204 */     if (tag instanceof CompoundTag) { CompoundTag compound = (CompoundTag)tag;
/* 205 */       add(tryUnwrap(compound)); }
/*     */     else
/* 207 */     { add(tag); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/* 213 */     int size = 36;
/* 214 */     size += 4 * this.list.size();
/* 215 */     for (Tag child : this.list) {
/* 216 */       size += child.sizeInBytes();
/*     */     }
/* 218 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 223 */   public byte getId() { return 9; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 228 */   public TagType<ListTag> getType() { return TYPE; }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 233 */     StringTagVisitor visitor = new StringTagVisitor();
/* 234 */     visitor.visitList(this);
/* 235 */     return visitor.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 240 */   public Tag remove(int index) { return (Tag)this.list.remove(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 245 */   public boolean isEmpty() { return this.list.isEmpty(); }
/*     */ 
/*     */   
/*     */   public Optional<CompoundTag> getCompound(int index) {
/* 249 */     Tag tag1 = getNullable(index); if (tag1 instanceof CompoundTag) { CompoundTag tag = (CompoundTag)tag1;
/* 250 */       return Optional.of(tag); }
/*     */     
/* 252 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/* 256 */   public CompoundTag getCompoundOrEmpty(int index) { return (CompoundTag)getCompound(index).orElseGet(CompoundTag::new); }
/*     */ 
/*     */   
/*     */   public Optional<ListTag> getList(int index) {
/* 260 */     Tag tag1 = getNullable(index); if (tag1 instanceof ListTag) { ListTag tag = (ListTag)tag1;
/* 261 */       return Optional.of(tag); }
/*     */     
/* 263 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/* 267 */   public ListTag getListOrEmpty(int index) { return (ListTag)getList(index).orElseGet(ListTag::new); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 272 */   public Optional<Short> getShort(int index) { return getOptional(index).flatMap(Tag::asShort); }
/*     */ 
/*     */   
/*     */   public short getShortOr(int index, short defaultValue) {
/* 276 */     Tag tag1 = getNullable(index); if (tag1 instanceof NumericTag) { NumericTag tag = (NumericTag)tag1;
/* 277 */       return tag.shortValue(); }
/*     */     
/* 279 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 284 */   public Optional<Integer> getInt(int index) { return getOptional(index).flatMap(Tag::asInt); }
/*     */ 
/*     */   
/*     */   public int getIntOr(int index, int defaultValue) {
/* 288 */     Tag tag1 = getNullable(index); if (tag1 instanceof NumericTag) { NumericTag tag = (NumericTag)tag1;
/* 289 */       return tag.intValue(); }
/*     */     
/* 291 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<int[]> getIntArray(int index) {
/* 295 */     Tag tag1 = getNullable(index); if (tag1 instanceof IntArrayTag) { IntArrayTag tag = (IntArrayTag)tag1;
/* 296 */       return Optional.of(tag.getAsIntArray()); }
/*     */     
/* 298 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<long[]> getLongArray(int index) {
/* 302 */     Tag tag1 = getNullable(index); if (tag1 instanceof LongArrayTag) { LongArrayTag tag = (LongArrayTag)tag1;
/* 303 */       return Optional.of(tag.getAsLongArray()); }
/*     */     
/* 305 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 310 */   public Optional<Double> getDouble(int index) { return getOptional(index).flatMap(Tag::asDouble); }
/*     */ 
/*     */   
/*     */   public double getDoubleOr(int index, double defaultValue) {
/* 314 */     Tag tag1 = getNullable(index); if (tag1 instanceof NumericTag) { NumericTag tag = (NumericTag)tag1;
/* 315 */       return tag.doubleValue(); }
/*     */     
/* 317 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 322 */   public Optional<Float> getFloat(int index) { return getOptional(index).flatMap(Tag::asFloat); }
/*     */ 
/*     */   
/*     */   public float getFloatOr(int index, float defaultValue) {
/* 326 */     Tag tag1 = getNullable(index); if (tag1 instanceof NumericTag) { NumericTag tag = (NumericTag)tag1;
/* 327 */       return tag.floatValue(); }
/*     */     
/* 329 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 334 */   public Optional<String> getString(int index) { return getOptional(index).flatMap(Tag::asString); }
/*     */ 
/*     */   
/*     */   public String getStringOr(int index, String defaultValue) {
/* 338 */     Tag tag = getNullable(index);
/* 339 */     if (tag instanceof StringTag) { stringTag = (StringTag)tag; try { String str; return str = stringTag.value(); } catch (Throwable stringTag) { throw new MatchException(stringTag.toString(), stringTag); }
/*     */        }
/*     */     
/* 342 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/* 346 */   private Tag getNullable(int index) { return (index >= 0 && index < this.list.size()) ? (Tag)this.list.get(index) : null; }
/*     */ 
/*     */ 
/*     */   
/* 350 */   private Optional<Tag> getOptional(int index) { return Optional.ofNullable(getNullable(index)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 355 */   public int size() { return this.list.size(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 360 */   public Tag get(int index) { return (Tag)this.list.get(index); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 365 */   public Tag set(int index, Tag tag) { return (Tag)this.list.set(index, tag); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 370 */   public void add(int index, Tag tag) { this.list.add(index, tag); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setTag(int index, Tag tag) {
/* 375 */     this.list.set(index, tag);
/* 376 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int index, Tag tag) {
/* 381 */     this.list.add(index, tag);
/* 382 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListTag copy() {
/* 387 */     List<Tag> copy = new ArrayList<Tag>(this.list.size());
/* 388 */     for (Tag tag : this.list) {
/* 389 */       copy.add(tag.copy());
/*     */     }
/* 391 */     return new ListTag(copy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 396 */   public Optional<ListTag> asList() { return Optional.of(this); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 401 */     if (this == obj) {
/* 402 */       return true;
/*     */     }
/*     */     
/* 405 */     return (obj instanceof ListTag && Objects.equals(this.list, ((ListTag)obj).list));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 410 */   public int hashCode() { return this.list.hashCode(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 415 */   public Stream<Tag> stream() { return super.stream(); }
/*     */ 
/*     */   
/*     */   public Stream<CompoundTag> compoundStream() {
/* 419 */     return stream().mapMulti((tag, output) -> {
/* 420 */           if (tag instanceof CompoundTag) { CompoundTag compound = (CompoundTag)tag;
/* 421 */             output.accept(compound); }
/*     */         
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 428 */   public void accept(TagVisitor visitor) { visitor.visitList(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 433 */   public void clear() { this.list.clear(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 438 */     byte elementType = identifyRawElementType();
/* 439 */     switch (visitor.visitList(TagTypes.getType(elementType), this.list.size())) {
/*     */       case HALT:
/* 441 */         return StreamTagVisitor.ValueResult.HALT;
/*     */       case BREAK:
/* 443 */         return visitor.visitContainerEnd();
/*     */     } 
/* 445 */     for (int i = 0; i < this.list.size(); i++) {
/* 446 */       Tag tag = wrapIfNeeded(elementType, (Tag)this.list.get(i));
/* 447 */       switch (visitor.visitElement(tag.getType(), i)) {
/*     */         case HALT:
/* 449 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case SKIP:
/*     */           break;
/*     */         case BREAK:
/* 453 */           return visitor.visitContainerEnd();
/*     */         default:
/* 455 */           switch (tag.accept(visitor)) {
/*     */             case HALT:
/* 457 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case BREAK:
/* 459 */               return visitor.visitContainerEnd();
/*     */           }  break;
/*     */       } 
/* 462 */     }  return visitor.visitContainerEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\nbt\ListTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */